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
package com.impetus.ankush.common.utils.validator;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.FileExists;
import com.impetus.ankush.common.scripting.impl.UrlExists;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;

public class SourceValidator implements Validator{

	private final AnkushLogger logger = new AnkushLogger(SourceValidator.class);

	private String compName;
	private NodeConfig nodeConfig;
	private ComponentConfig compConfig;

	private String errMessage;

	public SourceValidator(String compName, NodeConfig nodeConfig,
			ComponentConfig compConfig) {
		this.compName = compName;
		this.nodeConfig = nodeConfig;
		this.compConfig = compConfig;
		this.errMessage = null;
	}
	
	@Override
	public boolean validate() {
		try {
			if (!compConfig.getSource().isEmpty()) {
				if (compConfig.getSourceType() == ComponentConfig.SourceType.LOCAL) {
					logger.info("Validating local bundle existence-"
							+ compConfig.getSource(), compName,
							this.nodeConfig.getHost());
					AnkushTask fileExist = new FileExists(
							compConfig.getSource());
					if (nodeConfig.getConnection().exec(fileExist).rc != 0) {
						errMessage = "Could not find file "
								+ compConfig.getSource()
								+ ". Please specify the correct path.";
						return false;
					}
				} else if (compConfig.getSourceType() == ComponentConfig.SourceType.DOWNLOAD) {
					logger.info("Validating download url existence-"
							+ compConfig.getSource(), compName,
							this.nodeConfig.getHost());
					CustomTask task = new UrlExists(compConfig.getSource());
					Result rs = nodeConfig.getConnection().exec(task);
					if (rs.rc != 0) {
						errMessage = "Unable to access the download URL "
								+ compConfig.getSource();
						return false;
					}
				}
			}else{
				errMessage = "Please provide a valid source for the component.";
			}
		} catch (Exception e) {
			errMessage = Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG;
			logger.error(
					Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG
							+ e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	@Override
	public String getErrMsg() {
		return errMessage;
	}
}
