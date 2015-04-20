/*******************************************************************************
 * ===========================================================
 * Ankush : Big Data Cluster Management Solution
 * ===========================================================
 * 
 * (C) Copyright 2014, by Impetus Technologies
 * 
 * This is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation;
 * 
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/
package com.impetus.ankush.common.controller.listener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.impetus.ankush.AppStore;
import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.domain.AppConf;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.framework.ServerCrashManager;
import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.service.AsyncExecutorService;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.service.impl.AsyncExecutorServiceImpl;
import com.impetus.ankush2.agent.AgentDeployer;
import com.impetus.ankush2.agent.AgentUpgrader;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * <p>
 * StartupListener class used to initialize and database settings and populate
 * any application-wide drop-downs.
 * <p/>
 * <p>
 * Keep in mind that this listener is executed outside of
 * OpenSessionInViewFilter, so if you're using Hibernate you'll have to
 * explicitly initialize all loaded data at the GenericDao or service level to
 * avoid LazyInitializationException. Hibernate.initialize() works well for
 * doing this.
 * 
 * @see StartupEvent
 */
public class StartupListener implements ServletContextListener {

	private static final String STATE = "state";

	/** The log. */
	private static AnkushLogger log = new AnkushLogger(StartupListener.class);

	/** The application context. */
	private ApplicationContext applicationContext;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void contextInitialized(final ServletContextEvent event) {

		log.info("Initializing context...");

		ServletContext context = event.getServletContext();
		applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);

		try {
			// setting application context.
			AppStoreWrapper.setApplicationContext(applicationContext);
			// setting servlet context.
			AppStoreWrapper.setServletContext(context);

			// setting ankush config reader and config properties reader.
			setAnkushConfigurator();
			// set ankush confiration classes.
			// AppStoreWrapper.setAnkushConfigurableClassNames();

			AppStoreWrapper.setComponentConfiguration();

			AppStoreWrapper.setCompConfigClasses();

			// Read VERSION.txt file in agent.tar.gz and set current agent
			// version in server context
			setAgentVersion();

			// For Reading ankush-hadoop-config.xml
			// HadoopUtils.setHadoopConfigClasses();

			// setting mail manager
			setupMailManager();

			// setting App access URL
			setupAppAccessURL();

			// setting asyc executor.
			setAsyncExecutor();

			addDefaultAdminUser();

			setServerHost();

			// initialising the application dependencies.
			initializeAppDependency();

			// process inconsistent operations in the operation table,which have
			// op-status as "InProgress",set to "Failed"
			processInconsistentOperation();

			// Start Agent Upgrade procedure
			new AgentUpgrader().asyncUpgradeAgent();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Process inconsistent operation.
	 */
	private void processInconsistentOperation() {
		GenericManager<Operation, Long> operationManager = AppStoreWrapper
				.getManager(Constant.Manager.OPERATION, Operation.class);
		List<Operation> operationList = operationManager.getAll(0, 1,
				"-startedAt");
		if (operationList != null && operationList.size() != 0) {
			Operation operation = operationList.get(0);
			if (operation.getStatus().equals(
					Constant.Operation.Status.INPROGRESS.toString())) {
				operation
						.setStatus(Constant.Operation.Status.FAILED.toString());
				operationManager.save(operation);
			}
		}
	}

	/**
	 * 
	 */
	private void setServerHost() {
		AppConfService appConfService = AppStoreWrapper.getService(
				Constant.Service.APPCONF, AppConfService.class);

		appConfService.setDefaultHostAddress();
	}

	/**
	 * Sets the agent version.
	 */
	private static void setAgentVersion() {
		// current agent version
		String agentBuildVersion = new String();
		try {
			// Resource base path.
			String basePath = AppStoreWrapper.getResourcePath();
			// Creating agent bundle path.
			String agentBundlePath = basePath + "scripts/agent/"
					+ AgentDeployer.AGENT_BUNDLE_NAME;

			FileInputStream fileInputStream = new FileInputStream(
					agentBundlePath);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					fileInputStream);
			GzipCompressorInputStream gzInputStream = new GzipCompressorInputStream(
					bufferedInputStream);
			TarArchiveInputStream tarInputStream = new TarArchiveInputStream(
					gzInputStream);
			TarArchiveEntry entry = null;

			while ((entry = (TarArchiveEntry) tarInputStream.getNextEntry()) != null) {
				if (entry.getName()
						.equals(AgentDeployer.AGENT_VERSION_FILENAME)) {
					final int BUFFER = 10;
					byte data[] = new byte[BUFFER];
					tarInputStream.read(data, 0, BUFFER);
					String version = new String(data);
					agentBuildVersion = version.trim();
					// Set the agent version in the AppStore with key as
					// agentVersion
					AppStore.setObject(AppStoreWrapper.KEY_AGENT_VERISON,
							agentBuildVersion);
				}
			}
		} catch (Exception e) {
			// log error message.
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * @throws BeansException
	 */
	private void addDefaultAdminUser() throws BeansException {
		if (applicationContext.getBean("userManager") != null) {
			UserManager userManager = (UserManager) applicationContext
					.getBean("userManager");
			userManager.addAdminUser();
		}
	}

	/**
	 * Method to set ankush conf reader and ankush properties.
	 * 
	 */
	private void setAnkushConfigurator() {
		try {
			// Setting configuration reader.
			Resource resource = new ClassPathResource("/ankush_constants.xml");
			ConfigurationReader ankushConfReader = new ConfigurationReader(
					resource.getFile().getAbsolutePath());
			if (ankushConfReader != null) {
				AppStoreWrapper.setAnkushConfReader(ankushConfReader);
			}
		} catch (Exception e) {
			log.debug("Unable to set Configuration Reader or properties reader in Singletone Store.");
		}
	}

	private void setupAppAccessURL() {
		try {
			Map appServerAccessConf = null;
			if (applicationContext.getBean("appConfManager") != null) {
				GenericManager<AppConf, Long> appConfManager = (GenericManager<AppConf, Long>) applicationContext
						.getBean("appConfManager");
				AppConf appConf = appConfManager.getByPropertyValueGuarded(
						"confKey", "serverIP");
				if (appConf != null) {
					appServerAccessConf = (Map) appConf.getObject();
				}
				AppStoreWrapper.setAppAccessURL(appServerAccessConf);
			}
		} catch (Exception n) {
			log.info("Error in App Server access conf setup...");
		}
	}

	/**
	 * Method to set mail manager.
	 */
	private void setupMailManager() {
		try {
			Map appMailObj = null;
			if (applicationContext.getBean("appConfManager") != null) {
				GenericManager<AppConf, Long> appConfManager = (GenericManager<AppConf, Long>) applicationContext
						.getBean("appConfManager");
				AppConf appConf = appConfManager.getByPropertyValueGuarded(
						"confKey", "email");
				if (appConf != null) {
					appMailObj = (Map) appConf.getObject();
				}
				AppStoreWrapper.setupMail(appMailObj);
			}
		} catch (Exception n) {
			log.info("mailConfManager not found...");
			// ignore, should only happen when testing
		}
	}

	/**
	 * Method to set async executor.
	 */
	private void setAsyncExecutor() {
		try {
			AsyncExecutorService executor = (AsyncExecutorService) applicationContext
					.getBean("asyncExecutorService");
			if (executor != null) {
				AppStoreWrapper.setExecutor(executor);
			} else {
				log.error("AsyncExecutorService bean not found, ignoring...");
			}
		} catch (NoSuchBeanDefinitionException n) {
			log.debug("AsyncExecutorService bean not found, ignoring...");
			// ignore, should only happen when testing
		}
	}

	/**
	 * Initialize app dependency.
	 * 
	 */
	private void initializeAppDependency() {

		// server crash manager.
		final ServerCrashManager serverCrashManager = new ServerCrashManager();

		Runnable r = new Runnable() {

			@Override
			public void run() {
				ensureAppFoldersExistance();

				serverCrashManager.handleDeployingRemovingClusters();

				serverCrashManager.handleDeployingRemovingNodes();

				// Changing state of inprogress operations to server crashed
				serverCrashManager.handleInProgressOperations();

				// process inconsistent nodes.
				// processInconsistentNodes();
			}
		};
		new Thread(r).start();
	}

	/**
	 * Ensure app folders existance.
	 */
	private void ensureAppFoldersExistance() {
		String repoPath = AppStoreWrapper.getServerRepoPath();
		String patchesRepo = AppStoreWrapper.getServerPatchesRepoPath();
		String serverMetadataPath = AppStoreWrapper.getServerMetadataPath();
		String mdPaths[] = { serverMetadataPath, repoPath, patchesRepo };
		for (int index = 0; index < mdPaths.length; ++index) {
			File f = new File(mdPaths[index]);
			f.mkdirs();
		}
	}

	/**
	 * Process inconsistent nodes.
	 */
	// private void processInconsistentNodes() {
	// GenericManager<Cluster, Long> clusterManager = (GenericManager<Cluster,
	// Long>) this.applicationContext
	// .getBean(Constant.Manager.CLUSTER);
	//
	// // Creating property map for deploying state.
	// Map<String, Object> propMap1 = new HashMap<String, Object>();
	// propMap1.put(STATE, Constant.Cluster.State.DEPLOYED);
	// // Creating property map for removing state.
	// Map<String, Object> propMap2 = new HashMap<String, Object>();
	// propMap2.put(STATE, Constant.Cluster.State.ADDING_NODES);
	// // making list of maps
	// List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	// maps.add(propMap1);
	// maps.add(propMap2);
	//
	// // iterating over the all deploying state clusters.
	// for (Cluster cluster : clusterManager
	// .getAllByDisjunctionveNormalQuery(maps)) {
	// if (cluster.getTechnology().equals(Constant.Technology.HADOOP)) {
	// try {
	// // getting clusterable object
	// Clusterable clusterable = ObjectFactory
	// .getClusterableInstanceById(cluster.getTechnology());
	// // getting cluster conf.
	// HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) clusterable
	// .getClusterConf(cluster);
	//
	// // Getting cluster state
	// String clusterState = cluster.getState();
	// // For Node addition scenario
	// if (clusterState
	// .equals(Constant.Cluster.State.ADDING_NODES)) {
	// GenericManager<Node, Long> nodeManager = (GenericManager<Node, Long>)
	// this.applicationContext
	// .getBean(Constant.Manager.NODE);
	//
	// // removing entry from database if node state is adding
	// nodeManager.deleteAllByPropertyValue(STATE,
	// Constant.Node.State.ADDING);
	//
	// // setting adding nodes to null
	// hadoopClusterConf.setNewNodes(null);
	//
	// // setting state as error.
	// hadoopClusterConf
	// .setState(Constant.Cluster.State.DEPLOYED);
	// // saving cluster.
	// clusterable.updateClusterDetails(hadoopClusterConf);
	// } else {
	// List<NodeConf> nodeConfs = hadoopClusterConf
	// .getNodeConfs();
	// for (NodeConf conf : nodeConfs) {
	// HadoopNodeConf nodeConf = (HadoopNodeConf) conf;
	// if (nodeConf.getNodeState().equals(
	// Constant.Node.State.REMOVING)) {
	// // setting state as error.
	// nodeConf.setNodeState(Constant.Node.State.ERROR);
	// // saving cluster.
	// clusterable
	// .updateClusterDetails(hadoopClusterConf);
	// }
	// }
	// }
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// }
	// }
	// }

	/**
	 * Shutdown servlet context (currently a no-op method).
	 * 
	 * @param servletContextEvent
	 *            The servlet context event
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent servletContextEvent) {
		try {
			if (applicationContext != null) {
				ThreadPoolTaskExecutor pooledExecutor = (ThreadPoolTaskExecutor) applicationContext
						.getBean("pooledExecutor");
				log.debug("Status of pooledExec daemon "
						+ pooledExecutor.isDaemon());
				pooledExecutor.shutdown();

				ThreadPoolTaskScheduler pooledScheduler = (ThreadPoolTaskScheduler) applicationContext
						.getBean("pooledScheduler");
				log.debug("Status of pooledScheduler daemon "
						+ pooledScheduler.isDaemon());
				pooledScheduler.shutdown();

				AsyncExecutorServiceImpl executor = (AsyncExecutorServiceImpl) applicationContext
						.getBean("asyncExecutorService");
				executor.shutdown();
			}
			AppStore.destroyStore();
			applicationContext = null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
