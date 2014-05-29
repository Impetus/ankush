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
<!-- Page for oracle cluster cretaion -->
<%@ include file="../layout/blankheader.jsp"%>

<script>
	$(document).ready(
					function() {
						var clusterId = null;
						$.fn.editable.defaults.mode = 'inline';
						less = {
							env : "development", // or "production"
							async : false, // load imports async
							fileAsync : false, // load imports async when in a page under 

							// a file protocol
							poll : 1000, // when in watch mode, time in ms between polls
							functions : {}, // user functions, keyed by name
							dumpLineNumbers : "comments", // or "mediaQuery" or "all"
							relativeUrls : false,// whether to adjust url's to be relative
							// if false, url's are already relative to the
							// entry less file
							rootpath : ":/a.com/"// a path to add on to the start of every url 
						//resource
						};
						$("#try").tooltip();
						clusterId = "<c:out value='${clusterId}' />";
						
						dataCenterMapTable = $("#dataCenterMapTable").dataTable({
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : false,
							"bFilter" : false,
							"bSort" : false,
							"bInfo" : false,
						});
						oNodeTable = $("#nodeIpTable").dataTable({
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : false,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"aoColumnDefs" : [ {
								'sType' : "ip-address",
								'aTargets' : [ 1 ]
							},

							{
								'bSortable' : false,
								'aTargets' : [ 0, 2, 3, 4, 5, 6, 7, 8, 9, 10,11 ]
							} ],
						});
						$("#nodeIpTable_filter").css({
							'text-align' : 'right'
						});
						var selected = 'Selected';
						var all = 'All';
						var available = 'Available';
						var error = 'Error';
						$("#nodeIpTable_filter").hide();
						$('#nodeSearch').keyup(function() {
							oNodeTable.fnFilter($(this).val());
						});
						var inspectNode = 'inspectNodeBtn';
						/* $("#nodeIpTable_filter").prepend(
										'<div style="float:left;margin-top:15px;" id="nodeListOracleDatatable" class=""></div><div class="span2 text-left" style="float:left;margin-top:28px;font-size:14px;color:black; display:none" id="nodeRetreiveStatus"></div> <div class="span2" style="margin-top:15px;"><button type="button" class="btn" data-loading-text="Inspecting Nodes..." id="inspectNodeBtn" onclick="com.impetus.ankush.oClusterSetup.inspectNodesObject(\''+inspectNode+'\');">Inspect Nodes</button></div><div class=" span5 form-image text-right btn-group" id="toggleButtonOracle" data-toggle="buttons-radio" style="float:left;margin-top:15px;"><button type="button" class="btn active" onclick="com.impetus.ankush.common.toggleDatatable(\''
												+ all
												+ '\');">All</button><button type="button" class="btn" onclick="com.impetus.ankush.common.toggleDatatable(\''
												+ selected
												+ '\');">Selected</button><button type="button" class="btn" onclick="com.impetus.ankush.common.toggleDatatable(\''
												+ available
												+ '\');">Available</button><button type="button" class="btn" onclick="com.impetus.ankush.common.toggleDatatable(\''
												+ error
												+ '\');">Error</button></div>');
						$("#nodeListOracleDatatable").html('').append(
								"<h4 class='section-heading'>Node List</h4>");
						$("#nodeListOracleDatatable").append(""); */
						if (clusterId == '' || clusterId == null) {
							com.impetus.ankush.oClusterSetup.getDefaultValue();
							com.impetus.ankush.oClusterSetup.populateOracleDBPackage();
							com.impetus.ankush.oClusterSetup.tooltip();
							//	com.impetus.ankush.oClusterSetup.editableTable();
						}/*  else {
																																							com.impetus.ankush.oClusterSetup.clusterError(clusterId);
																																						} */
					});
</script>
<body style="background: none;">
	<div class="section-header">
		<div class="row-fluid" style="margin-top: 20px;">
			<div class="span7">
				<h2 class="heading text-left left">Oracle NoSQL Database v2</h2>
				<button class="btn-error" id="validateError"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"
					onclick="com.impetus.ankush.common.focusError();"></button>
				
			</div>
			
			<!--  
			<div class="span3 minhgt0">
				<button class="span3 btn-error" id="validateError"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"
					onclick="com.impetus.ankush.common.focusError();"></button>
			</div>
			-->
			<div class="span5 text-right">
				<button id="commonDeleteButton" class="btn"
					style="margin-right: 8px;" disabled="disabled"
					onclick="com.impetus.ankush.common.deleteDialog('deleteClusterDialog');">Delete</button>
				<!-- <button class="btn" style="margin-right:8px;">Validate</button> -->
				<button class="btn" id="clusterDeploy" data-loading-text="Deploying..."
					onclick="com.impetus.ankush.oClusterSetup.validatePage();">Deploy</button>
			</div>
		</div>
	</div>

	<div class="section-body content-body">
		<div class="container-fluid mrgnlft8">
			<div id="deleteClusterDialog" class="modal hide fade"
				style="display: none;">

				<div class="modal-header text-center">
					<h4 style="text-align: center; color: black">Cluster Delete</h4>
				</div>
				<div class="modal-body">
					<div class="row-fluid">
						<div class="span12" style="text-align: left;">The Cluster
							will be permanently deleted. Once deleted data cannot be
							recovered.</div>
					</div>
				</div>
				<div class="modal-footer">
					<a href="#" data-dismiss="modal" class="btn"
						id="cancelDeleteButton">Cancel</a> <a href="#"
						id="confirmDeleteButton"
						onclick="com.impetus.ankush.common.deleteCluster('deleteClusterDialog');"
						class="btn">Delete</a>
				</div>
			</div>
			<div class="row-fluid">
				<div id="errorDivMain" class="span12 error-div-hadoop" style="display: none;"></div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<h4 class="section-heading" style="text-align: left;">General
						Details</h4>
				</div>
			</div>

			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">Store:</label>
				</div>
				<div class="span10">
					<input id="storeName" type="text" data-toggle="tooltip"
						placeholder="Store Name"
						title="Provide a unique alphanumeric name to the KV Store. This name will be used to form the path to records kept on the store"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Datacenter:</label>
				</div>
				<div class="span10 ">
					<input id="dataCenter" type="text" data-toggle="tooltip"
						placeholder="DataCenter"
						title=" Datacenter can be used to represent group of nodes based on their physical location"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label form-label">Topology:</label>
				</div>
				<div class="span10 ">
					<input id="topology" type="text" data-toggle="tooltip"
						placeholder="Topology"
						title="Topology represents the configuration of the Store"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">Replication Factor:</label>
				</div>
				<div class="span10 ">
					<input id="repFactor" type="text" class="" style="width: 70px;"
						placeholder="Replication Factor" data-toggle="tooltip"
						title=" Replication Factor is the number of nodes belonging to a Shard. The default value is 3"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">Partitions:</label>
				</div>
				<div class="span10 ">
					<input id="partitions" type="text" class="" style="width: 70px;"
						placeholder="Partitions" data-toggle="tooltip"
						title="Each Shard contains one or more partitions. As a rough rule of thumb, there should be at least 10 to 20 partitions per shard. The number of partitions cannot be changed after the initial deployment, you should consider the maximum future size of the store when specifying the number of partitions."
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">Registry Port:</label>
				</div>
				<div class="span10">
					<input id="registryPort" type="text" style="width: 110px;"
						placeholder="Registry Port" data-toggle="tooltip"
						title="The TCP/IP port on which Oracle NoSQL Database will be contacted. 
					This port should be free (unused) on each node. 
					The default value is 5000."
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">HA Port Range:</label>
				</div>
				<div class="span2">
					<input id="haPort1" type="text" class="" style="width: 110px;"
						placeholder="Ha Port1" data-toggle="tooltip"
						title="A range of free ports which the nodes use to communicate amongst themselves. These ports must be sequential and at least equal to the total number of Replication Nodes running on each Storage Node. The default port range value is 5010 and 5020."
						data-placement="right"></input>
				</div>
				<div class="">
					<input id="haPort2" type="text" class="" style="width: 110px;"
						placeholder="HA Port2" data-toggle="tooltip"
						title="A range of free ports which the nodes use to communicate amongst themselves. These ports must be sequential and at least equal to the total number of Replication Nodes running on each Storage Node. The default port range value is 5010 and 5020."
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2 ">
					<label class=" text-right form-label">Base Directory:</label>
				</div>
				<div class="span10">
					<input id="baseDir" type="text" class="" style="width: 360px;"
						onchange="com.impetus.ankush.oClusterSetup.changePath();"
						placeholder="Base Directory" data-toggle="tooltip"
						title="The base directory to store Oracle NoSQL installation and data files.The default path is /home/oracle."
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">Installation Path:</label>
				</div>
				<div class="span10">
					<input id="installationPath" type="text" class=""
						placeholder="Installation Directory" style="width: 360px;"
						data-toggle="tooltip"
						title="A directory where the Oracle NoSQL Database package files (libraries, Javadoc, scripts, and so forth) should reside. (KVHOME)"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">Data Path:</label>
				</div>
				<div class="span10">
					<input id="dataPath" type="text" class="" style="width: 360px;"
						placeholder="Data Path" data-toggle="tooltip"
						title="A directory where the Oracle NoSQL Database data resides (KVROOT)"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class=" span2 ">
					<label class="text-right form-label">DB Package:</label>
				</div>
				<div class="span2">
					<select id="selectDBPackage" data-toggle="tooltip"
						style="width: 100%" placeholder="OracleNoSQL DB package"
						title="Select or upload the Oracle NoSQL DB Package version. Currently Support <kv-ee-2.0.26>"
						data-placement="right"></select>
				</div>
				<div class="span2" style="padding-top: 10px;">
					<input type="button" id="filePathDb" class="btn"
						data-loading-text="Uploading..." data-toggle="tooltip"
						placeholder="Upload File" title="" data-placement="right"
						value="Upload"
						onclick="com.impetus.ankush.oClusterSetup.oracleDbPackageUpload(this);"></input>
					<input style='visibility: hidden; height: 0px;' id='fileBrowseDb'
						type='file' class='' name='file'></input>
				</div>
				<div class="span2">
					<iframe style="width: 1px; height: 1px; border: 0px;"
						id="uploadframesharekeyDb" name="uploadframesharekeyDb"
						style="float:left;"></iframe>
					<form action="" id="uploadframesharekeyDbPackage"
						target="uploadframesharekeyDb" enctype="multipart/form-data"
						method="post" style="float: right; margin: 0px; height: 36px;"></form>
				</div>
			</div>

			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">NTP Server:</label>
				</div>
				<div class="span10">
					<input id="ntpServer" type="text" data-toggle="tooltip"
						placeholder="NTP Server"
						title="The NTP (Network Time Protocol) server used to synchronize time across all nodes to ensure effective consistency policy during read operations."
						data-placement="right"></input>
				</div>
			</div>
			<br />
			<div class="row-fluid">
				<div class="span12">
					<h4 class="section-heading" style="text-align: left;">Node
						Authentication</h4>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class=" text-right form-label">Username:</label>
				</div>
				<div class="span10">
					<input id="userName" type="text" style="width: 200px;"
						placeholder="User Name" data-toggle="tooltip"
						title=" A common username for authenticating nodes. User must have administrative rights on the nodes."
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Authentication Type:</label>
				</div>
				<!-- <div class="span10" style="margin-top: 8px;">
				<label class="radio inline" style="padding-top: 0px;"> <input
					type="radio" name="authenticationType" id="passwordAuthRadio"
					value="0" checked="checked"
					onclick="com.impetus.ankush.common.toggleAuthenticate('authenticationType','passwordAuthDiv','sharedKeyFileUploadDiv');" />
					<span>Password</span>
				</label> <label class="radio inline" style="padding-top: 0px;"> <input
					type="radio" id="sharedKeyAuthRadio" value="1"
					name="authenticationType"
					onclick="com.impetus.ankush.common.toggleAuthenticate('authenticationType','passwordAuthDiv','sharedKeyFileUploadDiv');" /><span>Shared
						Key</span>
				</label>
			</div> -->
				<div class="span10 ">
					<div class="btn-group" data-toggle="buttons-radio"
						id="authGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
						<button class="btn authRadio active btnGrp" data-value="0"
							name="authRadioBtn" id="passwordAuthRadio"
							onclick="com.impetus.ankush.common.buttonClick('passwordAuthDiv','sharedKeyFileUploadDiv')">Password</button>
						<button class="btn authRadio btnGrp" data-value="1"
							name="authRadioBtn" id="sharedKeyAuthRadio"
							onclick="com.impetus.ankush.common.buttonClick('sharedKeyFileUploadDiv','passwordAuthDiv')">Shared
							Key</button>
					</div>
				</div>
			</div>
			<div class="row-fluid" id="passwordAuthDiv">
				<div class="span2">
					<label class=" text-right form-label">Password:</label>
				</div>
				<div class="span10">
					<input id="password" type="password" style="width: 200px;"
						placeholder="Password" data-toggle="tooltip"
						title="The password to authenticate the nodes."
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid" id="sharedKeyFileUploadDiv"
				style="display: none">
				<div class="span2">
					<label class=" text-right form-label">File Upload:</label>
				</div>
				<div class="span10">
					<iframe style="width: 1px; height: 1px; border: 0px;"
						id="uploadShareKey" name="uploadShareKey" style="float:left;"></iframe>
					<form action="" id="uploadFormShareKeyAuth" target="uploadShareKey"
						enctype="multipart/form-data" method="post"
						style="float: left; height: 32px;">
						<input style="visibility: hidden;" id='fileBrowseShareKey'
							type='file' class='' name='file'></input><input type="text"
							id="filePathShareKey" readonly="readonly" class="disabled-field"
							style="float: left;" data-toggle="tooltip"
							placeholder="Upload File" title="Upload share key file"
							onclick="com.impetus.ankush.oClusterSetup.shareKeyFileUpload(this);"></input>
					</form>
				</div>
			</div>
			<br />
			<div class="row-fluid">
				<div class="span12 ">
					<h4 class="section-heading" style="text-align: left;">Search
						and Select Nodes</h4>
				</div>
			</div>
			<!-- <div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label"></label>
			</div>
			<div class="span10 text-left radioDiv">
				<div class="span2">
					<input type="radio" name="authentication" id="throughPassword"
						value="0" checked="checked"
						onclick="com.impetus.ankush.common.toggleAuthenticate('authentication','ipRangeDiv','fileUploadDiv');" /><span>&nbsp;&nbsp;IP
						Range</span>
				</div>
				<div class="span2">
					<input type="radio" id="throughSharedKey" value="1"
						name="authentication"
						onclick="com.impetus.ankush.common.toggleAuthenticate('authentication','ipRangeDiv','fileUploadDiv');" /><span>&nbsp;&nbsp;File
						Upload</span>
				</div>
			</div>
		</div> -->
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Mode :</label>
				</div>
				<div class="span10 ">
					<div class="btn-group" data-toggle="buttons-radio"
						id="ipModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
						<button class="btn nodeListRadio active btnGrp" data-value="0"
							id="throughPassword"
							onclick="com.impetus.ankush.common.buttonClick('ipRangeDiv','fileUploadDiv')">IP
							Range</button>
						<button class="btn nodeListRadio btnGrp" data-value="1"
							id="throughSharedKey"
							onclick="com.impetus.ankush.common.buttonClick('fileUploadDiv','ipRangeDiv')">File
							Upload</button>
					</div>
				</div>
			</div>
			<div class="row-fluid" id="ipRangeDiv" style="">
				<div class="span2">
					<label class=" text-right form-label">IP Range:</label>
				</div>
				<div class="span10">
					<input id="ipRange" type="text" class="" placeholder="IP Range"
						data-toggle="tooltip"
						title="IPs for a node or set of nodes. E.g. 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;kvstore-test. For more options refer the User Guide"
						data-placement="right"></input>
				</div>
			</div>


			<div class="row-fluid" id="fileUploadDiv" style="display: none">
				<div class="span2">
					<label class=" text-right form-label">File Path:</label>
				</div>
				<div class="span10">
					<iframe style="width: 1px; height: 1px; border: 0px;"
						id="uploadframesharekey" name="uploadframesharekey"
						style="float:left;"></iframe>
					<form action="" id="uploadFormShareKeyAddnode"
						target="uploadframesharekey" enctype="multipart/form-data"
						method="post" style="float: left; height: 32px;">
						<input style='visibility: hidden;' id='fileBrowse' type='file'
							class='' name='file'></input><input type="text" id="filePath"
							readonly="readonly" style="float: left;" class="disabled-field"
							data-toggle="tooltip" placeholder="Upload File"
							title="A text file (*.txt) that contains a node or set of nodes specified in the form of 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;kvstore-test. Each unique pattern / IP should be in a new line. For more options refer the User Guide"
							data-placement="right"
							onclick="com.impetus.ankush.oClusterSetup.getNodesUpload(this);"></input>
					</form>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Rack Mapping :</label>
				</div>
				<div class="span10 text-left">
					<div class="input-prepend" id="divRackMapping">
						<span style="float: left; height: 28px; margin-top: 5px;"
							class="add-on"> <input type="checkbox"
							onclick="com.impetus.ankush.common.inputPrependToggle('rackMapCheck','rackFilePath');"
							id="rackMapCheck" class="inputSelect">
						</span>
						<iframe name="uploadRackFile" id="uploadRackFile"
							style="width: 1px; height: 1px; border: 0px;"></iframe>
						<form style="margin: 0px;" method="post"
							enctype="multipart/form-data" target="uploadRackFile"
							id="uploadRackFileForm" action="">
							<input id="rackFileBrowse" type="file" name="file" class=""
								style="visibility: hidden; float: right"> <input
								type="text" id="rackFilePath" class="input-medium"
								onclick="com.impetus.ankush.oClusterSetup.rackFilepload(this);"
								style="cursor: default;" readonly="readonly"
								data-placement="right"
								title="File with Rack &amp; IP Address Mapping"
								placeholder="Upload File" data-toggle="tooltip"
								disabled="disabled">
						</form>
					</div>

				</div>
				<div class="row-fluid">
					<div class="span2"></div>
					<div class="span10">
						<button type="button" class="btn" id="retrieveNodeButton"
							data-loading-text="Retrieving..." style="color: #333333;"
							onclick="com.impetus.ankush.oClusterSetup.validateAuthenticate();">Retrieve</button>
					</div>
				</div>
				<br />
				
				<div class="row-fluid" id="nodeTable" style="display: none;">
				<div class="row-fluid">
				<div class="span4">
								<label class="node-dt left">Node List</label>
								<button type="button" class="btn left mrgl10 mrgt12" id="inspectNodeBtn" data-loading-text="Inspecting Nodes..." onclick="com.impetus.ankush.oClusterSetup.inspectNodesObject('inspectNodeBtn');">Inspect Nodes</button>
								
				</div>
				
				<div class=" span6 form-image text-right btn-group"
					id="toggleSelectButton" data-toggle="buttons-radio"
					style="float: left; margin-top: 10px;">
					<button type="button" class="btn active" id="nodeAll"
						name="nodeSelectBtn" data-value="all"
						onclick="com.impetus.ankush.common.toggleDatatable('All');">All</button>
					<button type="button" class="btn" id="nodeSelected"
						name="nodeSelectBtn" data-value="selected"
						onclick="com.impetus.ankush.common.toggleDatatable('Selected');">Selected</button>
					<button type="button" class="btn" id="nodeAvailable"
						name="nodeSelectBtn" data-value="availabel"
						onclick="com.impetus.ankush.common.toggleDatatable('Available');">Available</button>
					<button type="button" class="btn" id="nodeError"
						name="nodeSelectBtn" data-value="error"
						onclick="com.impetus.ankush.common.toggleDatatable('Error');">Error</button>
				</div>
				<div class="span2 text-right">
					<input type="text" id="nodeSearch" style="margin-top: 0"
						class="input-medium" placeholder="Search" />
				</div>
		</div>
					<div class=" " style="">
						<table class="table table-striped" id="nodeIpTable"
							style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
							<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
								<tr>
									<th><input type='checkbox' id='nodeCheckHead'
										name="nodeCheckHead"
										onclick="com.impetus.ankush.oClusterSetup.checkAllNode(this)"></th>
									<th>IP</th>
									<th>Admin</th>
									<th>Storage Dir</th>
									<th>Datacenter</th>
								<!-- 	<th>Rack</th> -->
									<th>Capacity</th>
									<th>CPUs</th>
									<th>Memory</th>
									<th>Registry Port</th>
									<th>HA Port Start</th>
									<th>HA Port End</th>
									<th></th>
								</tr>
							</thead>
							<tbody style="text-align: left;">
							</tbody>
						</table>
					</div>
				</div>
				
				
				
				
				
				<div class="row-fluid" style="display: none;">
				<div class="span12 ">
					<h4 class="section-heading" style="text-align: left;">Datacenter Mapping</h4>
				</div>
			</div>
				
				<div class="row-fluid" id="dataCenterTable" style="display: none;">
					<div class="span12 " style="">
						<table class="table table-striped" id="dataCenterMapTable"
							style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
							<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
								<tr>
									<th>Datacenter</th>
									<th>Node</th>
									<th>Capacity</th>
									<th>Replication Factor</th>
								</tr>
							</thead>
							<tbody style="text-align: left;">
							</tbody>
						</table>
					</div>
				</div>
				
				
				
				
				
				<div style="display: none;">
					<span id="popover-content"></span>
				</div>
			</div>
		</div></div>
</body>
</html>
