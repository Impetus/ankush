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
<!--Page for elasticSearch cluster creation  -->
<%@ include file="../../layout/blankheader.jsp"%>
<%-- <link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/css/commonSetupPage.css" media="all"/> --%>
<script>
	$(document).ready(
			function() {
				var clusterId = null;
				clusterId = "<c:out value='${clusterId}' />";
				oNodeTable = $("#nodeList").dataTable({
					"bJQueryUI" : true,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : true,
					"aaSorting" : [],
					"bInfo" : false,
					"aoColumnDefs" : [ {
						'sType' : "ip-address",
						'aTargets' : [ 1 ]
					}, {
						'bSortable' : false,
						'aTargets' : [ 0, 2, 3, 4, 5, 6, 7, 8, 9 ]
					} ],
				});
				if (clusterId == '' || clusterId == null) {
					com.impetus.ankush.common.getDefaultValue('elasticSearch',
							'elasticSearchSetupDetail.populateDefaultValues');
					com.impetus.ankush.elasticSearchClusterCreation
							.tooltipInitialize();
				}
			});
</script>

<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h2 id="hlbl-ElasticSearch" class="heading text-left left">Elastic
				Search</h2>

			<button class="btn-error" id="validateError"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important; padding: 0px 15px; left: 15px; position: relative;"
				onclick="com.impetus.ankush.common.focusError();"></button>
		</div>

		<!--  
		<div class="span3">
			<button class="span3 btn-error" id="validateError"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"
				onclick="com.impetus.ankush.common.focusError();"></button>
		</div>
		-->

		<div class="span5 text-right">
			<button id="clusterDelete" class="btn" style="margin-right: 8px;"
				disabled="disabled"
				onclick="com.impetus.ankush.common.deleteDialog('deleteClusterDialog');">Delete</button>
			<!-- <button class="btn" style="margin-right:8px;">Validate</button> -->
			<button class="btn" id="clusterDeploy"
				data-loading-text="Deploying..."
				onclick="com.impetus.ankush.elasticSearchClusterCreation.validateCluster();">Deploy</button>
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
		<div class="row-fluid">
			<div id="errorDivMain" class="span12 error-div-hadoop"
				style="display: none;"></div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<h4 id="slblGeneralDetails" class="section-heading"
					style="text-align: left;">General Details</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label id="lblClusterName" class="text-right form-label">Cluster:</label>
			</div>
			<div class="span10">
				<input id="clusterName" type="text" data-toggle="tooltip"
					class="input-medium" placeholder="Cluster Name"
					title="Enter name of the cluster" data-placement="right" value=""></input>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span12">
				<h4 id="slblJava" class="section-heading" style="text-align: left;">Java</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Oracle Java JDK:</label>
			</div>
			<div class="span10">
				<div class="input-prepend" style="white-space: inherit;">
					<span class="add-on" style="height: 28px; margin-top: 4px;"><input
						type="checkbox" class="inputSelect" name="" id="chkInstallJava"
						onclick="com.impetus.ankush.common.checkBoxToggle('chkInstallJava','bundlePathJava','homePathJava');"
						style="margin-left: 0px; position: relative;"></span> <input
						type="text" class="inputText" disabled="disabled" name=""
						title="Bundle Path" id="bundlePathJava" placeholder="Bundle Path" />
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Java Home:</label>
			</div>
			<div class="span10">
				<input type="text" placeholder="Default Java Home" id="homePathJava"
					class="input-medium" title="Default Java Home"
					data-placement="right">
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span12">
				<h4 id="slblNodeAuth" class="section-heading"
					style="text-align: left;">Node Authentication</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblUserName" class=" text-right form-label">Username:</label>
			</div>
			<div class="span10">
				<input id="commonUserName" type="text" placeholder="User Name"
					class="input-medium"
					onchange="com.impetus.ankush.elasticSearchClusterCreation.pathChange();"
					data-toggle="tooltip"
					title=" A common username for authenticating nodes. User must have administrative rights on the nodes."
					data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblAuthType" class=" text-right form-label">Authentication
					Type:</label>
			</div>

			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio" id="authGroupBtn"
					style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn authRadio active btnGrp" data-value="0"
						name="authTypeBtn" id="authTypePassword"
						onclick="com.impetus.ankush.common.buttonClick('passwordDivAuth','sharedKeyDivAuth')">Password</button>
					<button class="btn authRadio btnGrp" data-value="1"
						name="authTypeBtn" id="authTypeSharedKey"
						onclick="com.impetus.ankush.common.buttonClick('sharedKeyDivAuth','passwordDivAuth')">Shared
						Key</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="passwordDivAuth">
			<div class="span2">
				<label id="lblPassword" class="text-right form-label">Password:</label>
			</div>
			<div class="span10">
				<input id="commonUserPassword" type="password"
					placeholder="Password" class="input-medium" data-toggle="tooltip"
					title="The password to authenticate the nodes."
					data-placement="right" value=""></input>
			</div>
		</div>

		<div class="row-fluid" id="sharedKeyDivAuth" style="display: none;">
			<div class="span2">
				<label id="lblSharedKeyFile" class=" text-right form-label">File
					Path:</label>
			</div>
			<div class="span10">
				<iframe style="width: 1px; height: 1px; border: 0px;"
					id="sharedKeyUpload" name="sharedKeyUpload" style="float:left;"></iframe>
				<form action="" id="uploadFormShareKeyAddnode"
					class="iframe-formclass" target="sharedKeyUpload"
					enctype="multipart/form-data" method="post"
					style="float: left; height: 32px">
					<input type="text" class="input-medium disabled-field"
						id="pathSharedKey" readonly="readonly" data-toggle="tooltip"
						placeholder="Upload File" title="Upload the file"
						data-placement="right"
						onclick="com.impetus.ankush.elasticSearchClusterCreation.uploadShareKeyFile('fileUploadSharedKey','pathSharedKey');"></input>
					<input style='visibility: hidden;' id='fileUploadSharedKey'
						type='file' class='' name='file'></input>
				</form>
			</div>
		</div>



		<br>
		<div class="row-fluid">
			<div class="span12 ">
				<h4 id="slblSSNode" class="section-heading"
					style="text-align: left;">Search and Select Nodes</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblMode" class="text-right form-label">Mode:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="ipModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn nodeListRadio active btnGrp" data-value="0"
						id="modeIPRange" name="ipModeBtn"
						onclick="com.impetus.ankush.common.buttonClick('ipRangeDiv','fileUploadDiv')">IP
						Range</button>
					<button class="btn nodeListRadio btnGrp" data-value="1"
						id="modeIPFile" name="ipModeBtn"
						onclick="com.impetus.ankush.common.buttonClick('fileUploadDiv','ipRangeDiv')">File
						Upload</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="ipRangeDiv" style="">
			<div class="span2">
				<label id="lblIPRange" class=" text-right form-label">IP
					Range:</label>
			</div>
			<div class="span10">
				<input id="ipRange" type="text" class="input-medium"
					placeholder="IP Range" data-toggle="tooltip"
					title="IPs for a node or set of nodes. E.g. 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. For more options refer the User Guide"
					data-placement="right" value=""></input>
			</div>
		</div>
		<div class="row-fluid" id="fileUploadDiv" style="display: none;">
			<div class="span2">
				<label id="lblIPFile" class=" text-right form-label">File
					Path:</label>
			</div>
			<div class="span10">
				<iframe style="width: 1px; height: 1px; border: 0px;"
					id="uploadframesharekey" name="uploadframesharekey"
					style="float:left;"></iframe>
				<form action="" id="uploadFormShareKeyAddnode"
					target="uploadframesharekey" enctype="multipart/form-data"
					method="post" style="float: left; height: 32px"
					class="iframe-formclass">
					<input type="text" id="pathIPFile"
						class="input-medium disabled-field" readonly="readonly"
						data-toggle="tooltip" placeholder="Upload File"
						title="A text file (*.txt) that contains a node or set of nodes specified in the form of 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. Each unique pattern / IP should be in a new line. For more options refer the User Guide"
						data-placement="right"
						onclick="com.impetus.ankush.elasticSearchClusterCreation.getNodesUpload();"></input>
					<input style='visibility: hidden;' id='fileUploadIPFile'
						type='file' class='' name='file'></input>
				</form>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Rack Mapping:</label>
			</div>
			<div class="span10 text-left">
				<div class="input-prepend" id="divRackMapping">
					<span style="float: left; height: 28px; margin-top: 5px;"
						class="add-on"> <input type="checkbox"
						onclick="com.impetus.ankush.elasticSearchClusterCreation.inputPrependToggle('rackMapCheck','rackFilePath');"
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
							onclick="com.impetus.ankush.elasticSearchClusterCreation.rackFilepload(this);"
							style="cursor: default;" readonly="readonly"
							data-placement="right"
							title="File with Rack &amp; IP Address Mapping"
							placeholder="Upload File" data-toggle="tooltip"
							disabled="disabled">
					</form>
				</div>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2"></div>
			<div class="span10">
				<button type="button" class="btn" id="retrieveNodes"
					data-loading-text="Retrieving..." style="color: #333333;"
					onclick="com.impetus.ankush.elasticSearchClusterCreation.validateNode();">Retrieve</button>
			</div>
		</div>
		<div id="shardNodeTableHeaderDiv" style="display: none;">
			<div class="row-fluid">
				<div class="span4">
					<label class="node-dt left">Node List</label>
					<button type="button" class="btn left mrgl10 mrgt12"
						id="inspectNodeBtn" data-loading-text="Inspecting Nodes..."
						onclick="com.impetus.ankush.elasticSearchClusterCreation.inspectNodesObject('inspectNodeBtn');">Inspect
						Nodes</button>

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
		</div>
		<div class="row-fluid" id="nodeTableDiv"
			style="display: none; outline: none;" tabindex="-1">
			<div class="span12 " style="">
				<table class="table table-striped" id="nodeList"
					style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4; width: 100% !important">
					<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
						<tr>
							<th>ElasticSearch</th>
							<th>IP</th>
							<th>OS</th>
							<th>Datacenter</th>
							<th>Rack</th>
							<th>CPU</th>
							<th>Disk Count</th>
							<th>Disk Size</th>
							<th>RAM</th>
							<th></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

		<br>
		<div class="row-fluid">
			<div class="span12 ">
				<h4 id="slbl-ElasticSearch" class="section-heading"
					style="text-align: left;">Elastic Search Configuration</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label id="lblVendor-ElasticSearch" class="text-right form-label">Vendor:</label>
			</div>
			<div class="span10" id="elasticSearchVendorDropdownDiv">
				<select id="vendor-ElasticSearch" data-toggle="tooltip"
					title="Select ElasticSearch vendor"
					onchange="com.impetus.ankush.elasticSearchClusterCreation.vendorOnChangeElasticSearch();"
					data-placement="right"></select>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label id="lblVersion-ElasticSearch" class="text-right form-label">Version:</label>
			</div>
			<div class="span10">
				<select id="version-ElasticSearch" data-toggle="tooltip"
					title="Select ElasticSearch version"
					onchange="com.impetus.ankush.elasticSearchClusterCreation.versionOnChangeElasticSearch();"
					data-placement="right"></select>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label id="lblBundleSource-ElasticSearch"
					class="text-right form-label">Source:</label>
			</div>

			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="sourcePathBtnGrp" style="margin-top: 8px;">
					<button class="btn nodeListRadio active btnGrp" data-value="0"
						id="bundleSourceDownload-ElasticSearch"
						name="sourceStyleBtnElasticSearch"
						onclick="com.impetus.ankush.common.buttonClick('elasticSearchDownloadPathDiv','elasticSearchLocalPathDiv');">Download</button>
					<button class="btn nodeListRadio btnGrp" data-value="1"
						id="bundleSourceLocalPath-ElasticSearch"
						name="sourceStyleBtnElasticSearch"
						onclick="com.impetus.ankush.common.buttonClick('elasticSearchLocalPathDiv','elasticSearchDownloadPathDiv');">Local</button>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<!-- <label class="text-right form-label">Path:</label> -->
			</div>
			<div class="span5" id="elasticSearchDownloadPathDiv">
				<input id="downloadPath-ElasticSearch" type="text"
					data-toggle="tooltip" class="input-xlarge"
					placeholder="Download Path"
					title="Enter download path of elasticSearch binary file"
					data-placement="right"></input>
			</div>
			<div class="span5" style="display: none; margin-left: 8px;"
				id="elasticSearchLocalPathDiv">
				<input id="sourcePath-ElasticSearch" type="text"
					data-toggle="tooltip" class="input-xlarge" placeholder="Local Path"
					title="Enter path of elasticSearch local binary file"
					data-placement="right" value=""></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Action AutoCreate
					Index:</label>
			</div>
			<div class="span10" style="padding-top: 8px">
				<input id="actionAutoCreateIndex" type="checkbox"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Bootstrap Mlockall:</label>
			</div>
			<div class="span10" style="padding-top: 8px">
				<input id="bootstrapMlockall" type="checkbox"></input>
			</div>
		</div>



		<div class="row-fluid">
			<div class="span2">
				<label id="lblInstallationPath-ElasticSearch"
					class="text-right form-label">Installation Path:</label>
			</div>
			<div class="span10">
				<input id="installationPath-ElasticSearch" type="text"
					data-toggle="tooltip" class="input-xlarge"
					placeholder="Installation Path"
					title="Enter installation path for elasticSearch"
					data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label id="lblDataPath" class="text-right form-label">Data
					Path:</label>
			</div>
			<div class="span10">
				<input id="dataPath-ElasticSearch" type="text" data-toggle="tooltip"
					class="input-xlarge" placeholder="Data Directory Path"
					title="Enter data directory path for elasticSearch"
					data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Heap Size:</label>
			</div>
			<div class="span10">
				<input id="heapSize-ElasticSearch" type="text" data-toggle="tooltip"
					class="input-xlarge" placeholder="Heap Size"
					title="Enter Heap Size" data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Max. Local Storage
					Nodes:</label>
			</div>
			<div class="span10">
				<input id="localStorageNodes-ElasticSearch" type="text"
					data-toggle="tooltip" class="input-xlarge"
					placeholder="max. Local Storage Nodes"
					title="Enter max. local storage nodes" data-placement="right"></input>
			</div>
		</div>

		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Shard Index </label>
			</div>
			<div class="span10">
				<input id="shardIndex-ElasticSearch" type="text"
					data-toggle="tooltip" class="input-xlarge"
					placeholder="Index of Shards" title="Enter shard index"
					data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Replica Index </label>
			</div>
			<div class="span10">
				<input id="replicaIndex-ElasticSearch" type="text"
					data-toggle="tooltip" class="input-xlarge"
					placeholder="Index of replicas" title="Enter replicas index"
					data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">HTTP Port </label>
			</div>
			<div class="span10">
				<input id="httpPort-ElasticSearch" type="text" data-toggle="tooltip"
					class="input-xlarge" placeholder="HTTP Port"
					title="Enter http port" data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">Transport TCP Port </label>
			</div>
			<div class="span10">
				<input id="tcpPort-ElasticSearch" type="text" data-toggle="tooltip"
					class="input-xlarge" placeholder="TCP Port" title="Enter tcp port"
					data-placement="right"></input>
			</div>
		</div>

	</div>
</div>

