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
<!-- Page for Kafka deployment -->
<%@ include file="../../layout/blankheader.jsp"%>
<script>
	$(document)
			.ready(
					function() {
						var clusterId = null;
						clusterId = "<c:out value='${clusterId}' />";
						oNodeTable = $('#nodeIpTable').dataTable({
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : false,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"aoColumnDefs" : [ {
								'sType' : "ip-address",
								'aTargets' : [ 1 ]
							}, {
								'bSortable' : false,
								'aTargets' : [ 0, 2, 3, 4, 5, 6, 7, 8 ]
							} ],
						});
						$("#nodeIpTable_filter").hide();
						$('#nodeSearchBox').keyup(function() {
							oNodeTable.fnFilter($(this).val());
						});
						if (clusterId == '' || clusterId == null) {
							com.impetus.ankush.kafkaClusterCreation
									.tooltipInitialize();
							com.impetus.ankush.common.getDefaultValue('kafka',
									'kafkaSetupDetail.populateDefaultValues');
						}
						/* // error tool tip console.log($(this).next());
						$('#inputUserName').mouseover(function(){				
							var color = "red";

							$(this).next().find('.tooltip-inner').css('background-color', color);
							$(this).next().find('.tooltip-arrow').css('border-right-color', color);
							
						}); */
					});
</script>
<!-- header section -->
<div class="section-header">
	<div class="row-fluid headermargin">
		<div class="span7">
			<h2 class="heading text-left left">Kafka</h2>
			<button class="btn-error header_errmsg" id="validateError"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none; padding: 0 15px; left: 15px; position: relative"></button>
		</div>

		<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error header_errmsg" id="validateError"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;"></button>
		</div>
		-->

		<div class="span5 text-right">
			<button id="commonDeleteButton" class="btn headerright-setting"
				onclick="com.impetus.ankush.common.deleteDialog('deleteClusterDialog');"
				disabled="disabled">Delete</button>
			<button class="btn" id="clusterDeploy" data-loading-text="Deploying..."
				onclick="com.impetus.ankush.kafkaClusterCreation.validateCluster();">Deploy</button>
		</div>
	</div>
</div>
<!-- body section  -->
<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div id="deleteClusterDialog" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4 style="text-align: center; color: black">Cluster Delete</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left;">
						The Cluster will be permanently deleted. Once deleted, <br>data
						cannot be recovered.
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn" id="cancelDeleteButton">Cancel</a>
				<a href="#" id="confirmDeleteButton"
					onclick="com.impetus.ankush.common.deleteCluster('deleteClusterDialog');"
					class="btn">Delete</a>
			</div>
		</div>
		<div class="row-fluid ">
			<div id="errorDivMain" class="display-none error-div-hadoop"></div>
		</div>
		<div class="row-fluid">
			<div class="pull-left">
				<h4 class="section-heading">General Details</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Cluster:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="" id="inputCluster"
					placeholder="Cluster Name" class="input-medium"
					title="Kafka cluster name" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class=" pull-left">
				<h4 class="section-heading">Java Details</h4>
			</div>
		</div>
		<!-- <div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Oracle Java JDK :</label>
			</div>
			<div class="span10 text-left">
				<div style="float: left; margin-top: 2px;">
					<label class="inline" id="javaCheckLabel"> <input
						type="checkbox" checked="checked" id="installJavaCheck"
						onclick="com.impetus.ankush.common.divChangeOnCheck('installJavaCheck','inputBundlePath','inputJavaHome')"
						value="" name="">&nbsp;&nbsp;Install
					</label>
				</div>
				<div class="span8 text-left">
					<input type="text" value="" name="" id="inputBundlePath"
						style="margin-top: 0px" class="input-medium"
						placeholder="Bundle Path" title="Oracle Java JDR  Bundle Path"
						data-placement="right">
				</div>
			</div>
		</div> -->
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Oracle Java JDK:</label>
			</div>
			<div class="span10">
				<div class="input-prepend" style="white-space: inherit;">
					<span class="add-on" style="height: 28px; margin-top: 4px;"><input
						type="checkbox" class="inputSelect" name="" id="installJavaCheck"
						onclick="com.impetus.ankush.common.checkBoxToggle('installJavaCheck','inputBundlePath','inputJavaHome');"
						style="margin-left: 0px; position: relative;"></span> <input
						type="text" class="inputText" disabled="disabled" name=""
						title="Bundle Path" id="inputBundlePath" placeholder="Bundle Path" />
				</div>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Java Home:</label>
			</div>
			<div class="span10">
				<input type="text" placeholder="Default Java Home"
					id="inputJavaHome" class="input-medium" title="Default Java Home"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class=" pull-left">
				<h4 class="section-heading">Node Authentication</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">User Name:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="" id="inputUserName"
					class="input-medium" placeholder="Username"
					onchange="com.impetus.ankush.kafkaClusterCreation.pathChange();"
					title=" A common username for authenticating nodes. User must have administrative rights on the nodes."
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Authentication Type:</label>
			</div>
			<!-- <div class="span10" style="margin-top: 10px;">
				<label class="radio inline" style="padding-top: 0px;"> <input
					class="radio_buttons required" id="" name="authenticationType"
					onclick="com.impetus.ankush.common.toggleAuthenticate('authenticationType','passwordAuthDiv','sharedKeyFileUploadDiv');"
					type="radio" value="0" checked="checked"><span
					class="lbl-black">Password</span></label>
				 <label class="radio inline">
						   <input class="radio_buttons required" id="" name="user[authentication]" type="radio" value=""><span>Passwordless-SSH</span></label>
						  
				<label class="radio inline" style="padding-top: 0px;"> <input
					class="radio_buttons required" id="" name="authenticationType"
					onclick="com.impetus.ankush.common.toggleAuthenticate('authenticationType','passwordAuthDiv','sharedKeyFileUploadDiv');"
					type="radio" value="1"><span class="lbl-black">Shared-Key</span></label>
			</div> -->
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio" id="authGroupBtn"
					style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn authRadio active btnGrp" data-value="0"
						name="authRadioBtn" id="authTypePassword"
						onclick="com.impetus.ankush.common.buttonClick('passwordAuthDiv','sharedKeyFileUploadDiv')">Password</button>
					<button class="btn authRadio btnGrp" data-value="1"
						name="authRadioBtn" id="authTypeSharedKey"
						onclick="com.impetus.ankush.common.buttonClick('sharedKeyFileUploadDiv','passwordAuthDiv')">Shared
						Key</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="passwordAuthDiv">
			<div class="span2">
				<label class="text-right form-label">Password:</label>
			</div>
			<div class="span10">
				<input type="password" value="" name="" id="inputPassword"
					placeholder="Password" class="input-medium"
					title="The password to authenticate the nodes."
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid display-none" id="sharedKeyFileUploadDiv">
			<div class="span2">
				<label class=" text-right form-label">File Upload:</label>
			</div>
			<div class="span10">
				<iframe id="uploadShareKey" name="uploadShareKey"
					class="iframe-display"></iframe>
				<form action="" id="uploadFormShareKeyAuth" target="uploadShareKey"
					enctype="multipart/form-data" method="post"
					class="iframe-formclass" style="height: 32px;">
					<input type="text" id="filePathShareKey" readonly="readonly"
						class="file-sharekey input-medium disabled-field"
						data-toggle="tooltip" placeholder="Upload File"
						title="Upload share key file"
						onclick="com.impetus.ankush.kafkaClusterCreation.shareKeyFileUpload(this);"
						data-placement="right"></input> <input id='fileBrowseShareKey'
						type='file' style="visibility: hidden;" name='file'></input>
				</form>
			</div>
		</div>
		<div class="row-fluid">
			<div class="pull-left">
				<h4 class="section-heading">Search and Select Nodes</h4>
			</div>
		</div>
		<!-- 	<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label"></label>
			</div>
			<div class="span10">
				<label class="radio inline"> <input
					class="radio_buttons required" id="ipRangeRadio"
					name="authentication"
					onclick="com.impetus.ankush.common.toggleAuthenticate('authentication','ipRangeDiv','fileUploadDiv');"
					type="radio" value="0" checked="checked"><span
					class="lbl-black">IP Range</span></label>
				 <label class="radio inline">
						   <input class="radio_buttons required" id="" name="user[authentication]" type="radio" value=""><span>Passwordless-SSH</span></label>
						  
				<label class="radio inline"> <input
					class="radio_buttons required" id="fileUploadRadio"
					name="authentication"
					onclick="com.impetus.ankush.common.toggleAuthenticate('authentication','ipRangeDiv','fileUploadDiv');"
					type="radio" value="1"><span class="lbl-black">File
						Upload</span></label>
			</div>
		</div> -->
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Mode:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="ipModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn nodeListRadio active btnGrp" data-value="0"
						id="ipRangeRadio"
						onclick="com.impetus.ankush.common.buttonClick('ipRangeDiv','fileUploadDiv')">IP
						Range</button>
					<button class="btn nodeListRadio btnGrp" data-value="1"
						id="fileUploadRadio"
						onclick="com.impetus.ankush.common.buttonClick('fileUploadDiv','ipRangeDiv')">File
						Upload</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="ipRangeDiv">
			<div class="span2">
				<label class="text-right form-label">IP Range:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="" id="inputIpRange"
					placeholder="IP Range" class="input-medium"
					title="IPs for a node or set of nodes. E.g. 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. For more options refer the User Guide"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid display-none" id="fileUploadDiv">
			<div class="span2">
				<label class="text-right form-label">File Path:</label>
			</div>
			<div class="span10">
				<iframe id="uploadframesharekey" name="uploadframesharekey"
					class="iframe-display"></iframe>
				<form action="" id="uploadFormShareKeyAddnode"
					target="uploadframesharekey" enctype="multipart/form-data"
					method="post" class="iframe-formclass" style="height: 32px;">
					<input type="text" id="filePath" readonly="readonly"
						class="file-sharekey input-medium disabled-field"
						data-toggle="tooltip" placeholder="Upload File"
						title="A text file (*.txt) that contains a node or set of nodes specified in the form of 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. Each unique pattern / IP should be in a new line. For more options refer the User Guide"
						data-placement="right"
						onclick="com.impetus.ankush.kafkaClusterCreation.getNodesUpload(this);"></input>
					<input id='fileBrowse' type='file' style="visibility: hidden;"
						name='file'></input>
				</form>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2"></div>
			<div class="span10">
				<button data-loading-text="Retrieving..." id="nodeRetrieveBtn"
					class="btn" type="button"
					onclick="com.impetus.ankush.kafkaClusterCreation.validateNode()">Retrieve</button>
			</div>
		</div>
		<div class="row-fluid display-none" id="nodeListDiv">

			<div class="row-fluid">
				<div class="text-left span4">
					<label class="node-dt left">Node List</label>
					<button type="button" class="btn left mrgl10 mrgt12"
						data-loading-text="Inspecting Nodes..." id="inspectNodeBtn"
						onclick="com.impetus.ankush.kafkaClusterCreation.inspectNodesObject('inspectNodeBtn');">Inspect
						Nodes</button>
				</div>
				<div data-toggle="buttons-radio" id="toggleSelectButton"
					class="span6 form-image text-right btn-group">
					<button type="button" class="btn active"
						onclick="com.impetus.ankush.common.toggleDatatable('All');">All</button>
					<button type="button" class="btn"
						onclick="com.impetus.ankush.common.toggleDatatable('Selected');">Selected</button>
					<button type="button" class="btn"
						onclick="com.impetus.ankush.common.toggleDatatable('Available');">Available</button>
					<button type="button" class="btn"
						onclick="com.impetus.ankush.common.toggleDatatable('Error');">Error</button>
				</div>
				<div class="span2 text-right">
					<input type="text" id="nodeSearchBox" placeholder="Search"
						class="input-medium" />
				</div>
			</div>
			<table class="table table-striped tblborder1" id="nodeIpTable">
				<thead class="tblborder2">
					<tr>
						<th><input type='checkbox' id='nodeCheckHead' name=""
							onclick="com.impetus.ankush.common.checkAllNodes('nodeCheckHead','nodeCheckBox')"></th>
						<th>IP</th>
						<th>Zookeeper</th>
						<th>OS</th>
						<th>CPU</th>
						<th>Disk Count</th>
						<th>Disk Size</th>
						<th>RAM</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div class="row-fluid">
			<div class="pull-left">
				<h4 class="section-heading">Kafka Configuration</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Vendor:</label>
			</div>
			<div class="span10">
				<select id="vendorDropdown" title="Select Kafka Vendor"
					data-placement="right"
					onclick="com.impetus.ankush.kafkaClusterCreation.vendorOnChangeKafka();">
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Version:</label>
			</div>
			<div class="span10">
				<select id="versionDropdown" title="Select Kafka Version"
					data-placement="right"
					onclick="com.impetus.ankush.kafkaClusterCreation.versionOnChangeKafka();">
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label ">Source:</label>
			</div>
			<!-- <div class="span7 ">
				<label class="radio inline"> <input type="radio" value="0"
					onclick="com.impetus.ankush.common.toggleAuthenticate('bundlePathRadio','downloadPathDiv','localPathDiv');"
					name="bundlePathRadio" id="downloadRadio"
					class="radio_buttons required" checked="checked"><span
					class="lbl-black">Download</span></label> <label class="radio inline">
					<input type="radio" value="1"
					onclick="com.impetus.ankush.common.toggleAuthenticate('bundlePathRadio','downloadPathDiv','localPathDiv');"
					name="bundlePathRadio" id="localRadio"
					class="radio_buttons required"><span class="lbl-black">Local</span>
				</label>
			</div> -->
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="sourcePathBtnGrp" style="margin-top: 8px;">
					<button class="btn nodeListRadio active btnGrp" data-value="0"
						id="downloadRadio"
						onclick="com.impetus.ankush.common.buttonClick('downloadPathDiv','localPathDiv');">Download</button>
					<button class="btn nodeListRadio btnGrp" data-value="1"
						id="localRadio"
						onclick="com.impetus.ankush.common.buttonClick('localPathDiv','downloadPathDiv');">Local
					</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="downloadPathDiv">
			<div class="span2">
				<!-- 		<label class="text-right form-label">Path :</label> -->
			</div>
			<div class="span10">
				<input type="text" name="downloadPath" id="downloadPath"
					class="input-xlarge" placeholder="Download Path"
					title="Kafka Download Path" data-placement="right">
			</div>
		</div>
		<div class="row-fluid display-none " id="localPathDiv">
			<div class="span2">
				<!-- 	<label class="text-right form-label">Path :</label> -->
			</div>
			<div class="span10 ">
				<input type="text" name="localPath" id="localPath"
					class="input-xlarge" placeholder="Local Path"
					title="Kafka Local Path" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Installation Path:</label>
			</div>
			<div class="span10">
				<input id="installationPathKafka" type="text" data-toggle="tooltip"
					class="input-xlarge" placeholder="Kafka Installation Path"
					title="Enter installation path for Kafka" data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid" id="replicationFactorDiv"
			style="display: none;">
			<div class=" span2 ">
				<label class="text-right form-label">Replication Factor:</label>
			</div>
			<div class="span10">
				<input id="replicationFactor" type="text" data-toggle="tooltip"
					class="input-mini" placeholder="Replication Factor"
					title="Enter replication Factor for Kafka" data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Port:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="port" id="port" class="input-mini"
					placeholder="Port" title="Kafka Port" data-placement="right">
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">JMX Port:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="jmxPort_Kafka" id="jmxPort_kafka"
					class="input-mini" placeholder="JMX Port" title="Kafka JMX Port"
					data-placement="right">
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Log Directory:</label>
			</div>
			<div class="span10">
				<input type="text" id="logDirectory" class="input-xlarge"
					placeholder="Log Directory " title="Log Directory"
					data-placement="right">
			</div>
		</div>





		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Partitions:</label>
			</div>
			<div class="span10">
				<input type="text" id="numPartitions" class="input-mini"
					placeholder="Kafka Partitions" title="Kafka Partitions"
					data-placement="right">
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Network Threads:</label>
				</div>
				<div class="span10">
					<input type="text" id="numOfNetworkThreads" class="input-mini"
						placeholder="Kafka Network Threads" title="Kafka Network Threads"
						data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">IO Threads:</label>
				</div>
				<div class="span10">
					<input type="text" id="numOfIOThreads" class="input-mini"
						placeholder="Kafka IO Threads" title="Kafka IO Threads"
						data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Queued Max. Requests:</label>
				</div>
				<div class="span10">
					<input type="text" id="queuedMaxRequests" class="input-mini"
						placeholder="Kafka Queued Max. Requests"
						title="Kafka Queued Max. Requests" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Log Retention Hours:</label>
				</div>
				<div class="span10">
					<input type="text" id="logRetentionHours" class="input-mini"
						placeholder="Kafka Log Retention Hours"
						title="Kafka Log Retention Hours" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Log Retention Bytes:</label>
				</div>
				<div class="span10">
					<input type="text" id="logRetentitionBytes" class="input-mini"
						placeholder="Kafka Log Retention Bytes"
						title="Kafka Log Retention Bytes" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Log Cleanup Interval
						Mins:</label>
				</div>
				<div class="span10">
					<input type="text" id="logCleanupIntervalMins" class="input-mini"
						placeholder="Kafka Log Cleanup Interval Mins"
						title="Kafka Log Cleanup Interval Mins" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Log Flush Interval
						Message:</label>
				</div>
				<div class="span10">
					<input type="text" id="logFlushIntervalMessage" class="input-mini"
						placeholder="Kafka Log Flush Interval Message"
						title="Kafka Log Flush Interval Message" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Log Flush Scheduler
						Interval:</label>
				</div>
				<div class="span10">
					<input type="text" id="logFlushSchedularIntervalMs"
						class="input-mini"
						placeholder="Kafka Log Flush Schedular Interval"
						title="Kafka Log Flush Schedular Interval" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Log Flush Interval:</label>
				</div>
				<div class="span10">
					<input type="text" id="logFlushIntervalMs" class="input-mini"
						placeholder="Kafka Log Flush Interval"
						title="Kafka Log Flush Interval" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Controlled Shutdown
						Enable:</label>
				</div>
				<div class="span10">
					<input type="checkbox" id="controlledShutdownEnable" class="mrgt15"
						data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Controlled Shutdown
						Max Retries:</label>
				</div>
				<div class="span10">
					<input type="text" id="controlledShutdownMaxRetries"
						class="input-mini"
						placeholder="Kafka Controlled Shutdown Max Retries"
						title="Kafka Controlled Shutdown Max Retries"
						data-placement="right">
				</div>
			</div>
			<!-- 	<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Request Logger:</label>
				</div>
				<div class="span10">
					<input type="text" id="requestLogger" class="input-large"
						placeholder="Kafka Request Logger"
						title="Enter Kafka Request Logger" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Logger Kafka
						Controller:</label>
				</div>
				<div class="span10">
					<input type="text" id="loggerKafkaController" class="input-large"
						placeholder="Logger Kafka Controller"
						title="Enter Logger Kafka Controller" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">State Change Logger:</label>
				</div>
				<div class="span10">
					<input type="text" id="stateChangeLogger" class="input-large"
						placeholder="Kafka State Change Logger"
						title="Enter Kafka State Change Logger" data-placement="right">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Logger Request
						Channel:</label>
				</div>
				<div class="span10">
					<input type="text" id="loggerRequestChannel" class="input-large"
						placeholder="Kafka Logger Request Channel"
						title="Enter Kafka Logger Request Channel" data-placement="right">
				</div>
			</div> -->
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Log Level:</label>
				</div>
				<div class="span10">
					<select id="logLevelDropdown" title="Select Log Level"
						data-placement="right">
					</select>
				</div>
			</div>

			<div class="row-fluid">
				<div class="pull-left">
					<h4 class="section-heading">Zookeeper Configuration</h4>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Vendor:</label>
				</div>
				<div class="span10">
					<select id="zookeeperVendorDropdown" data-toggle="tooltip"
						title="Select Zookeeper vendor"
						onchange="com.impetus.ankush.kafkaClusterCreation.vendorOnChangeZookeeper();"
						data-placement="right"></select>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Version:</label>
				</div>
				<div class="span10">
					<select id="zookeeperVersionDropdown" data-toggle="tooltip"
						title="Select Zookeeper vendor"
						onchange="com.impetus.ankush.kafkaClusterCreation.versionOnChangeZookeeper();"
						data-placement="right"></select>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Source:</label>
				</div>
				<!-- 	<div class="span2" style="width: 100px; padding-top: 12px;">
				<input type="radio" name="zookeeperRadio"
					id="zookeeperDownloadRadio" value="0" checked="checked"
					style="vertical-align: middle;"
					onclick="com.impetus.ankush.common.toggleAuthenticate('zookeeperRadio','zookeeperDownloadPathDiv','zookeeperLocalPathDiv');" />
				<span class="lbl-black">&nbsp;&nbsp;Download</span>
			</div>
			<div class="span2" style="padding-top: 12px;">
				<input type="radio" name="zookeeperRadio" id="zookeeperLocalRadio"
					value="1" style="vertical-align: middle;"
					onclick="com.impetus.ankush.common.toggleAuthenticate('zookeeperRadio','zookeeperDownloadPathDiv','zookeeperLocalPathDiv');" />
				<span class="lbl-black">&nbsp;&nbsp;Local</span>
			</div> -->
				<div class="span10 ">
					<div class="btn-group" data-toggle="buttons-radio"
						id="zookeeperPathBtnGrp" style="margin-top: 8px;">
						<button class="btn nodeListRadio active btnGrp" data-value="0"
							id="zookeeperDownloadRadio"
							onclick="com.impetus.ankush.common.buttonClick('zookeeperDownloadPathDiv','zookeeperLocalPathDiv');">Download</button>
						<button class="btn nodeListRadio btnGrp" data-value="1"
							id="zookeeperLocalRadio"
							onclick="com.impetus.ankush.common.buttonClick('zookeeperLocalPathDiv','zookeeperDownloadPathDiv');">Local
						</button>
					</div>
				</div>
			</div>
			<div class="row-fluid" id="zookeeperDownloadPathDiv">
				<div class=" span2 ">
					<!-- <label class="text-right form-label">Path:</label> -->
				</div>
				<div class="span10">
					<input id="zookeeperDownloadPath" type="text" data-toggle="tooltip"
						class="input-xlarge" placeholder="Download Path"
						title="Enter download path of zookeeper binary file"
						data-placement="right"></input>
				</div>

			</div>
			<div class="row-fluid display-none" id="zookeeperLocalPathDiv">
				<div class=" span2 ">
					<!-- 	<label class="text-right form-label">Path:</label> -->
				</div>
				<div class="span10">
					<input id="zookeeperLocalPath" type="text" data-toggle="tooltip"
						class="input-xlarge" placeholder="Source  Path"
						title="Enter local path of zookeeper binary file"
						data-placement="right" value=""></input>
				</div>

			</div>

			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Installation Path:</label>
				</div>
				<div class="span10">
					<input id="installationPathZookeeper" type="text"
						data-toggle="tooltip" class="input-xlarge"
						placeholder="Installation Path"
						title="Enter installation path for zookeeper"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Data Path:</label>
				</div>
				<div class="span10">
					<input id="dataDirZookeeper" type="text" data-toggle="tooltip"
						class="input-xlarge" placeholder="Data Directory Path"
						title="Enter data directory path for zookeeper"
						data-placement="right"></input>
				</div>
			</div>

			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Client Port:</label>
				</div>
				<div class="span10">
					<input id="clientPort" type="text" data-toggle="tooltip"
						class="input-mini" placeholder="Client Port"
						title="Enter client port" data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">JMX Port:</label>
				</div>
				<div class="span10">
					<input id="jmxPort_Zookeeper" type="text" data-toggle="tooltip"
						class="input-mini" placeholder="Zookeeper JMX Port"
						title="Enter Zookeeper JMX port" data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Sync Limit:</label>
				</div>
				<div class="span10">
					<input id="syncLimit" type="text" data-toggle="tooltip"
						style="float: left;" class="input-mini" placeholder="Sync Limit"
						title="Enter sync limit" data-placement="right"></input> <label
						style="margin-left: 10px; float: left"
						class="text-left tooltiptext form-label">milliseconds</label>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2 ">
					<label class="text-right form-label">Init Limit:</label>
				</div>
				<div class="span10">
					<input id="initLimit" type="text" data-toggle="tooltip"
						style="float: left;" class="input-mini" placeholder="Init Limit"
						title="Enter init limit" data-placement="right"></input> <label
						style="margin-left: 10px; float: left"
						class="text-left tooltiptext form-label">milliseconds</label>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Tick Time:</label>
				</div>
				<div class="span10">
					<input id="tickTime" type="text" data-toggle="tooltip"
						style="float: left;" class="input-mini" placeholder="Tick Time"
						title="Enter tick-time" data-placement="right"></input> <label
						style="margin-left: 10px; float: left"
						class="text-left tooltiptext form-label">milliseconds</label>
				</div>
			</div>
		</div>
	</div>
</div>
