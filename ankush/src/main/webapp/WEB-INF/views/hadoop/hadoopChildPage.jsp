<!------------------------------------------------------------------------------
-===========================================================
-Ankush : Big Data Cluster Management Solution
-===========================================================
-
-(C) Copyright 2014, by Impetus Technologies
-
-This is free software; you can redistribute it and/or modify it under
-the terms of the GNU Lesser General Public License (LGPL v3) as
-published by the Free Software Foundation;
-
-This software is distributed in the hope that it will be useful, but
-WITHOUT ANY WARRANTY; without even the implied warranty of
-MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-See the GNU Lesser General Public License for more details.
-
-You should have received a copy of the GNU Lesser General Public License 
-along with this software; if not, write to the Free Software Foundation, 
-Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->
<!-- Fields related to Hadoop Advanced Deployment Details during Cluster Creation  -->

<script>
	$(document).ready(function() {
		com.impetus.ankush.hadoopCluster.tooltipHadoopAdvSettings();
	});
</script>
<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h2 class="heading text-left left">Hadoop Advanced Settings</h2>
			
			<button id="errorBtnHadoop-HadoopAdvSettings" class="btn-error"
				onclick="com.impetus.ankush.hadoopCluster.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>
		
		<!-- 
		<div class="span3 minhgt0">
			<button id="errorBtnHadoop-HadoopAdvSettings" class="span3 btn-error"
				onclick="com.impetus.ankush.hadoopCluster.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		 -->
		
		<div class="span5 text-right">
			<button class="btn mrgr10"
				onclick="com.impetus.ankush.hadoopCluster.revertHadoop();"
				id="revertAdv-Hadoop">Revert</button>
			<button class="btn"
				onclick="com.impetus.ankush.hadoopCluster.applyHadoop();"
				id="applyAdv-Hadoop">Apply</button>
		</div>
	</div>
</div>
<div class="section-body content-body ">

<div class="container-fluid  mrgnlft8">
	<div class="row-fluid">
		<div id="error-div-hadoop-HadoopAdvSettings"
			class="span12 error-div-hadoop" style="display: none;">
			<span id="popover-content-HadoopAdvSettings" style="color: red;"></span>
		</div>
	</div>
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span2">
			<label class="text-right form-label">DFS Replication:</label>
		</div>
		<div class="span10">
			<input type="text" id="dfsReplicationHadoop" style="width: 100px"
				placeholder="DFS Replication" data-toggle="tooltip"
				data-placement="right"
				title="This is the Replication Factor for each block. It can be updated later. The default value at is set to 3" />
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Vendor:</label>
		</div>
		<div class="span10">
			<select id="vendor-Hadoop"
				onchange="com.impetus.ankush.hadoopCluster.componentInternalVendorChange('Hadoop');"
				data-toggle="tooltip" data-placement="right"
				title="Select the Vendor forr Hadoop Installation. Ecosystems are dependent on this selection"></select>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Version:</label>
		</div>
		<div class="span10">
			<select id="version-Hadoop" data-toggle="tooltip"
				data-placement="right"
				onchange="com.impetus.ankush.hadoopCluster.componentInternalVersionChange('Hadoop');"
				title="Select the Version for Hadoop Installation. Ecosystems are dependent on this selection"></select>
		</div>
	</div>
	
	<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Source:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="sourceTypeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="0"
						id="downloadRadio-Hadoop"
						onclick="com.impetus.ankush.hadoopCluster.downloadUrl('Hadoop');">Download</button>
					<button class="btn btnGrp" data-value="1" id="localPathRadio-Hadoop"
						onclick="com.impetus.ankush.hadoopCluster.localUrl('Hadoop');">Local Path</button>
				</div>
			</div>
	</div>
	
	<div class="row-fluid">
	<div class="span2">&nbsp;</div>
	
		<div class="span10">
			<input type="text" data-toggle="tooltip" data-placement="right"
				title="URL / Location of the Hadoop set up bundle"
				style="width: 300px;" id="path-Hadoop"
				placeholder="Download URL/Local Path">
		</div>
	</div>

	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">NameNode Path:</label>
		</div>
		<div class="span10">
			<input type="text" id="nameNodePathHadoop" data-toggle="tooltip"
				style="width: 300px;" data-placement="right"
				placeholder="NameNode Directory"
				title="The path where name table(fsimage) is stored. If this is a comma-delimited list of directories then the name table is replicated in all of the directories, for redundancy." />
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">DataNode Path:</label>
		</div>
		<div class="span10">
			<input type="text" id="dataNodePathHadoop" data-toggle="tooltip"
				style="width: 300px;" data-placement="right"
				placeholder="Datanode Directory"
				title="The path where DataNode stores its blocks. If this is a comma-delimited list of directories, then data will be stored in all named directories, typically on different devices." />
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Mapred Temp Path:</label>
		</div>
		<div class="span10">
			<input type="text" data-toggle="tooltip" id="mapredTempPathHadoop"
				style="width: 300px;" data-placement="right"
				placeholder="Mapred Temporary Directory"
				title="A shared directory for mapreduce temporary files." />
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Hadoop Temp Path:</label>
		</div>
		<div class="span10">
			<input type="text" data-toggle="tooltip" id="tempPathHadoop"
				style="width: 300px;" data-placement="right"
				placeholder="Hadoop Temporary Directory"
				title="A base location for hadoop temporary directories" />
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Installation Path:</label>
		</div>
		<div class="span10">
			<input type="text" data-toggle="tooltip" style="width: 300px;"
				data-placement="right" id="installationPath-Hadoop"
				placeholder="Installation Path"
				title="Location where the hadoop installation should be done" />
		</div>
	</div>
	<div class="row-fluid">
	
	<div class="span2">&nbsp;</div>
		<div class="span10">
			<label class="inline"><input type="checkbox"
				id="includeS3SupportHadoop"
				onclick="com.impetus.ankush.hadoopCluster.s3Property();" style="margin-top:-2px">&nbsp;&nbsp;Include
				S3 Support</label>
		</div>
	</div>
	<div id="s3Div" style="display: none;">
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Access Key:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" id="acessKeyS3"
					class="input-large" data-placement="right"
					title="ID of the account to access S3"
					placeholder="Access Key Value">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Secret Key:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" class="input-large"
					title="Authentication key of the account to access S3"
					data-placement="right" id="secretKeyS3"
					placeholder="Secret Key Value">
			</div>
		</div>
	</div>
	<div class="row-fluid">
	<div class="span2">&nbsp;</div>
		<div class="span10" style="padding-top: 8px;">
			<label class="inline"> <input type="checkbox"
				id="includeS3nSupportHadoop"
				onclick="com.impetus.ankush.hadoopCluster.s3nProperty();"  style="margin-top:-2px"><span>&nbsp;&nbsp;Include
					S3n Support</span>
			</label>
		</div>
	</div>
	<div id="s3nDiv" style="display: none;">
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Access Key:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" class="input-large"
					data-placement="right" id="acessKeyS3n"
					placeholder="Access Key Value"
					title="ID of the account to access S3n">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 \">
				<label class="text-right form-label">Secret Key:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip"
					title="Authentication key of the account to access S3n"
					class="input-large" data-placement="right" id="secretKeys3n"
					placeholder="Secret Key Value">
			</div>
		</div>
	</div>
	
	<div id="hadoop2Settings" style="display: block;">
	
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Resource Manager Node:</label>
			</div>
			<div class="span10 ">
				<select id="resourceManagerNode" name="resourceManagerNode"
					data-placement="right" data-toggle="tooltip"
					title="Select a node for Resource Manager"></select>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Job History Server:</label>
			</div>
			<div class="span10 ">
			
				<div class="input-prepend" style="white-space: inherit;" id="startJobHistoryServer">
					<span class="add-on" style="height: 28px; margin-top: 4px;">
						<input type="checkbox" class="inputSelect" name="" id="checkboxJobHistoryServer"
						onclick="com.impetus.ankush.hadoopCluster.toggleJobHistoryServer();"
						style="margin-left: 0px;">
					</span> 
					<select id="jobHistoryServer" name="jobHistoryServer"
					data-placement="right" data-toggle="tooltip" disabled="disabled" 
					title="Select a node for Job HistoryServer"></select>
				</div>
			
				
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Web Application Proxy:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="webAppProxyGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="0" id="webAppProxyEnable"
						onclick="com.impetus.ankush.hadoopCluster.webAppProxyDivShow();">Enable</button>
					<button class="btn btnGrp" data-value="1" id="webAppProxyDisable"
						onclick="com.impetus.ankush.hadoopCluster.webAppProxyDivHide();">Disable</button>
				</div>
			</div>
		</div>
		<div id="webAppProxyDiv" style="display: block;">
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Web App Proxy Node:</label>
				</div>
				<div class="span10 ">
					<select id="webAppProxyNode" name="webAppProxyNode"
						data-placement="right" data-toggle="tooltip" 
						title="Select a node for Web Application Proxy"></select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Web App Proxy Port:</label>
				</div>
				<div class="span10">
					<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
						title="Enter port for Web Application Proxy service"
						data-placement="right" placeholder="Web App Proxy Port" id="webAppProxyPort">
				</div>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">High Availability:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="hadoopHAGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="0" id="hadoopHAEnable"
						onclick="com.impetus.ankush.hadoopCluster.hadoopHADivShow();">Enable</button>
					<button class="btn btnGrp" data-value="1" id="hadoopHADisable"
						onclick="com.impetus.ankush.hadoopCluster.hadoopHADivHide();">Disable</button>
				</div>
			</div>
		</div>
		<div id="hadoopHADiv" style="display: block;">
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Nameservice ID:</label>
				</div>
				<div class="span10 ">
					<input type="text" data-toggle="tooltip" class="input-large"
					data-placement="right" id="nameserviceId"
					placeholder="Nameservice ID"
					title="Logical name for new nameservice">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">StandBy NameNode:</label>
				</div>
				<div class="span10 ">
					<select id="haStandByNode" name="haStandByNode"
						data-placement="right" data-toggle="tooltip"
						title="Select a StandBy NameNode for HA"></select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">NameNode ID 1:</label>
				</div>
				<div class="span10 ">
					<input type="text" data-toggle="tooltip" class="input-large"
					data-placement="right" id="nameNodeId1"
					placeholder="NameNode ID 1"
					title="Unique identifier for first NameNode in the nameservice">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">NameNode ID 2:</label>
				</div>
				<div class="span10 ">
					<input type="text" data-toggle="tooltip" class="input-large"
					data-placement="right" id="nameNodeId2"
					placeholder="NameNode ID 2"
					title="Unique identifier for second NameNode in the nameservice">
				</div>
			</div> 
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Journal Nodes:</label>
				</div>
				<div class="span10 ">
					<select class="multiSelect" id="haJournalNodes"
						data-placement="right" data-toggle="tooltip"
						title="Select Journal nodes for HA"
						data-bind="multiselect: true, options: Options, selectedOptions: SelectedOptions, optionsValue: $data"
						multiple="multiple"></select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Journal Nodes Dir:</label>
				</div>
				<div class="span10 ">
					<input type="text" data-toggle="tooltip" 
					data-placement="right" id="journalNodeEditsDir"
					style="width: 300px;" placeholder="Journal Nodes Dir"
					title="Path where the JournalNode daemon will store its local state">
				</div>
			</div>
			<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Automatic Failover:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="automaticFailoverGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="0"
						id="automaticFailoverEnabled">Enable</button>
					<button class="btn btnGrp" data-value="1" id="automaticFailoverDisabled">Disable</button>
				</div>
			</div>
		</div>
		</div>
	</div>
	
	</div>
</div>
