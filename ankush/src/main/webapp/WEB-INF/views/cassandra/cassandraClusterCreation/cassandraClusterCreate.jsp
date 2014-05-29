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
<!-- Page for cassandra deployment -->
<%@ include file="../../layout/blankheader.jsp"%>
<script src="<c:out value='${baseUrl}' />/public/js/tooltip/cassandraClusterCreationTooltip.js" type="text/javascript"></script>
<script>
	$(document).ready(
			function() {
				var clusterId = null;
				clusterId = "<c:out value='${clusterId}' />";
				oNodeTable = $('#nodeList').dataTable({
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
						'aTargets' : [ 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ]
					} ],
				});
				$("#nodeList_filter").hide();
				$('#nodeSearch').keyup(function() {
					oNodeTable.fnFilter($(this).val());
				});
				if (clusterId == '' || clusterId == null) {
					com.impetus.ankush.cassandraClusterCreation
							.tooltipInitialize();
					com.impetus.ankush.common.getDefaultValue('cassandra',
							'cassandraSetupDetail.populateDefaultValues');
				}
				/* // error tool tip console.log($(this).next());
				$('#commonUserName').mouseover(function(){				
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
			<h2 class="heading text-left left">Cassandra</h2>
			<button class="btn-error header_errmsg" id="validateError"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none; padding: 0 15px; left: 15px; position: relative"></button>

		</div>

		<!-- 
		<div class="span3 minhgt0">
			<button class="span3 btn-error header_errmsg" id="validateError"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;"></button>
		</div> -->

		<div class="span5 text-right">
			<button id="clusterDelete" class="btn headerright-setting"
				onclick="com.impetus.ankush.common.deleteDialog('deleteClusterDialog');"
				disabled="disabled">Delete</button>
			<button class="btn" id="clusterDeploy" data-loading-text="Deploying..."
				onclick="com.impetus.ankush.cassandraClusterCreation.validatePage();">Deploy</button>
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
			<div id="errorDivMain" class="display-none"></div>
		</div>

		<div class="row-fluid">
			<div class="pull-left">
				<h4 id="slblGeneralDetails" class="section-heading">General
					Details</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblClusterName" class="text-right form-label">Cluster:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="" id="clusterName"
					placeholder="Cluster Name" class="input-medium"
					title="Cluster name" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblClusterDescription" class="text-right form-label">Cluster
					Description:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="" id="inputClusterDesc"
					placeholder="Cluster Description" class="input-medium"
					title="Cluster description" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class=" pull-left">
				<h4 id="slblJava" class="section-heading">Java</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblOracleJava" class="text-right form-label">Oracle
					Java JDK:</label>
			</div>
			<div class="span10">
				<div class="input-prepend" style="white-space: inherit;">
					<span class="add-on" style="height: 28px; margin-top: 4px"><input
						type="checkbox" class="inputSelect" name="" id="chkInstallJava"
						onclick="com.impetus.ankush.common.checkBoxToggle('chkInstallJava','bundlePathJava','homePathJava');"
						style="margin-left: 0px; position: relative;"></span> <input
						type="text" class="inputText input-medium" disabled="disabled"
						name="" title="Bundle Path" id="bundlePathJava"
						placeholder="Bundle Path" style="margin-top: 4px;" />
				</div>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblJavaHome" class="text-right form-label">Java
					Home:</label>
			</div>
			<div class="span10">
				<input type="text" placeholder="Default Java Home" id="homePathJava"
					class="input-medium" title="Default Java Home"
					data-placement="right">
			</div>
		</div>


		<div class="row-fluid">
			<div class=" pull-left">
				<h4 id="slblNodeAuth" class="section-heading">Node
					Authentication</h4>
			</div>
		</div>


		<div class="row-fluid">
			<div class="span2">
				<label id="lblCommonUserName" class="text-right form-label">User
					Name:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="" id="commonUserName"
					class="input-medium" placeholder="Username"
					onchange="com.impetus.ankush.cassandraClusterCreation.pathChange();"
					title=" A common username for authenticating nodes. User must have administrative rights on the nodes."
					data-placement="right">
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblAuthType" class="text-right form-label">Authentication
					Type:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio" id="authGroupBtn"
					style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn authRadio active btnGrp" data-value="0"
						name="authTypeBtn" id="authTypePassword"
						onclick="com.impetus.ankush.common.buttonClick('passwordAuthDiv','sharedKeyFileUploadDiv')">Password</button>
					<button class="btn authRadio btnGrp" data-value="1"
						name="authTypeBtn" id="authTypeSharedKey"
						onclick="com.impetus.ankush.common.buttonClick('sharedKeyFileUploadDiv','passwordAuthDiv')">Shared
						Key</button>
				</div>
			</div>
		</div>

		<div class="row-fluid" id="passwordAuthDiv">
			<div class="span2">
				<label id="lblCommonUserPassword" class="text-right form-label">Password:</label>
			</div>
			<div class="span10">
				<input type="password" value="" name="" id="commonUserPassword"
					placeholder="Password" class="input-medium"
					title="The password to authenticate the nodes."
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid display-none" id="sharedKeyFileUploadDiv">
			<div class="span2">
				<label id="lblSharedKeyFile" class=" text-right form-label">File
					Path:</label>
			</div>
			<div class="span10">
				<iframe id="sharedKeyUpload" name="sharedKeyUpload"
					class="iframe-display"></iframe>
				<form action="" id="uploadFormShareKeyAuth" target="sharedKeyUpload"
					enctype="multipart/form-data" method="post"
					class="iframe-formclass" style="height: 32px;">
					<input type="text" id="pathSharedKey" readonly="readonly"
						class="file-sharekey input-medium disabled-field"
						data-toggle="tooltip" placeholder="Upload File"
						title="Upload share key file"
						onclick="com.impetus.ankush.cassandraClusterCreation.shareKeyFileUpload(this);"
						data-placement="right"></input> <input id='fileBrowseShareKey'
						type='file' style="visibility: hidden;" name='file'></input>
				</form>
			</div>
		</div>

				<div class="row-fluid" style="display: none;">
			<div class="span2">
				<label id="lblVirtualNode" class="text-right form-label">Virtual
					Node:</label>
			</div>
			<div class="span10">
				<input type="checkbox" checked="checked" id="vNodeCheck"
					style="margin-top: 15px;"
					onclick="com.impetus.ankush.cassandraClusterCreation.vNodeCheckToggle();"
					value="" name="">
			</div>
		</div>


		<div class="row-fluid">
			<div class="pull-left">
				<h4 id="slblSSNode" class="section-heading">Search and Select
					Nodes</h4>
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
		<div class="row-fluid" id="ipRangeDiv">
			<div class="span2">
				<label id="lblIPRange" class="text-right form-label">IP
					Range:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="" id="ipRange"
					placeholder="IP Range" class="input-medium"
					title="IPs for a node or set of nodes. E.g. 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. For more options refer the User Guide"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid display-none" id="fileUploadDiv">
			<div class="span2">
				<label id="lblIPFile" class="text-right form-label">File
					Path:</label>
			</div>
			<div class="span10">
				<iframe id="uploadframesharekey" name="uploadframesharekey"
					class="iframe-display"></iframe>
				<form action="" id="uploadFormShareKeyAddnode"
					target="uploadframesharekey" enctype="multipart/form-data"
					method="post" class="iframe-formclass" style="height: 32px;">
					<input type="text" id="pathIPFile" readonly="readonly"
						class="file-sharekey input-medium disabled-field"
						data-toggle="tooltip" placeholder="Upload File"
						title="A text file (*.txt) that contains a node or set of nodes specified in the form of 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. Each unique pattern / IP should be in a new line. For more options refer the User Guide"
						data-placement="right"
						onclick="com.impetus.ankush.cassandraClusterCreation.getNodesUpload(this);"></input>
					<input id='fileUploadIPFile' type='file'
						style="visibility: hidden;" name='file'></input>
				</form>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblRackMapping" class="text-right form-label">Rack
					Mapping:</label>
			</div>
			<div class="span10 text-left">
				<div class="input-prepend" id="divRackMapping">
					<span style="float: left; height: 28px; margin-top: 5px;"
						class="add-on"> <input type="checkbox"
						onclick="com.impetus.ankush.cassandraClusterCreation.inputPrependToggle('rackMapCheck','rackFilePath');"
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
							onclick="com.impetus.ankush.cassandraClusterCreation.rackFilepload(this);"
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
				<button data-loading-text="Retrieving..." id="retrieveNodes"
					class="btn" type="button"
					onclick="com.impetus.ankush.cassandraClusterCreation.validateNode()">Retrieve</button>
			</div>
		</div>

		<div class="row-fluid display-none" id="nodeListDiv">

			<div class="row-fluid">
				<div class="text-left span4">
					<label id="slblNodeList" class="node-dt left">Node List</label>
						<button type="button" class="btn left mrgl10 mrgt12"
						data-loading-text="Inspecting Nodes..." id="inspectNodeBtn"
						onclick="com.impetus.ankush.cassandraClusterCreation.inspectNodesObject('inspectNodeBtn');">Inspect
						Nodes</button>
				</div>
				<div data-toggle="buttons-radio" id="toggleSelectButton"
					class="span6 form-image text-right btn-group">
					<button type="button" class="btn active btnGrp" id="nodeAll"
						name="nodeSelectBtn"
						onclick="com.impetus.ankush.common.toggleDatatable('All');">All</button>
					<button type="button" class="btn btnGrp" id="nodeSelected"
						name="nodeSelectBtn"
						onclick="com.impetus.ankush.common.toggleDatatable('Selected');">Selected</button>
					<button type="button" class="btn btnGrp" id="nodeAvailable"
						name="nodeSelectBtn"
						onclick="com.impetus.ankush.common.toggleDatatable('Available');">Available</button>
					<button type="button" class="btn btnGrp" id="nodeError"
						name="nodeSelectBtn"
						onclick="com.impetus.ankush.common.toggleDatatable('Error');">Error</button>
				</div>
				<div class="span2 text-right">
					<input type="text" id="nodeSearch" placeholder="Search"
						class="input-medium" />
				</div>
			</div>
			<table class="table table-striped tblborder1" id="nodeList">
				<thead class="tblborder2">
					<tr>
						<th><input type='checkbox' id='nodeCheckHead' name=""
							onclick="com.impetus.ankush.cassandraClusterCreation.checkAllNodes('nodeCheckHead','nodeCheckBox', 'seedNodeCheckBox')"></th>
						<th>IP</th>
						<th>Seed Node</th>
						<th>OS</th>
						<th>Datacenter</th>
						<th>Rack</th>
						<th>CPU</th>
						<th>Disk Count</th>
						<th>Disk Size</th>
						<th>RAM</th>
						<th>VNode Count</th>
						<th>Authenticated</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div class="row-fluid">
			<div class="pull-left">
				<h4 id="slbl-Configuration" class="section-heading">Configuration</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblVendor-Cassandra" class="text-right form-label">Vendor:</label>
			</div>
			<div class="span10">
				<select id="vendor-Cassandra" title="Select Cassandra Vendor"
					data-placement="right"
					onclick="com.impetus.ankush.cassandraClusterCreation.vendorOnChangeCassandra();">

				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblVersion-Cassandra" class="text-right form-label">Version:</label>
			</div>
			<div class="span10">
				<select id="version-Cassandra" title="Select Cassandra Vendor"
					data-placement="right"
					onclick="com.impetus.ankush.cassandraClusterCreation.versionOnChangeCassandra();">

				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblBundleSource-Cassandra" class="text-right form-label ">Source:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="sourcePathBtnGrp" style="margin-top: 8px;">
					<button class="btn nodeListRadio active btnGrp" data-value="0"
						id="bundleSourceDownload-Cassandra" name="sourcePathBtnCassandra"
						onclick="com.impetus.ankush.common.buttonClick('downloadPathDiv','localPathDiv');">Download</button>
					<button class="btn nodeListRadio btnGrp" data-value="1"
						id="bundleSourceLocalPath-Cassandra"
						onclick="com.impetus.ankush.common.buttonClick('localPathDiv','downloadPathDiv');">Local
					</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="downloadPathDiv">
			<div class="span2">
				<!-- 	<label class="text-right form-label">Path :</label> -->
			</div>
			<div class="span10">
				<input type="text" name="downloadPath" id="downloadPath-Cassandra"
					class="input-xlarge" placeholder="Download Path"
					title="Download Path" data-placement="right">
			</div>
		</div>
		<div class="row-fluid display-none " id="localPathDiv">
			<div class="span2">
				<!-- 		<label class="text-right form-label">Path :</label> -->
			</div>

			<div class="span10 ">
				<input type="text" name="localPath" id="localPath-Cassandra"
					class="input-xlarge" placeholder="Local Path" title="Local Path"
					data-placement="right">
			</div>

		</div>


		<div class="row-fluid">
			<div class="span2">
				<label id="lblinstallationPath-Casandra"
					class="text-right form-label">Installation Path:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="inputInstallionPath"
					id="installationPath-Cassandra" class="input-xlarge"
					placeholder="Installation Path" title="Installation Path"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblPartitioner" class="text-right form-label">Partitioner:</label>
			</div>
			<div class="span10">
				<select id="partitionerDropDown" name=""
					title="The partitioner is responsible for distributing rows (by key) across nodes in the cluster."
					data-placement="right">
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblSnitch" class="text-right form-label">Snitch:</label>
			</div>
			<div class="span10">
				<select id="snitchDropDown" name="" data-placement="right"
					title="A snitch maps IPs to racks and data centers. It defines how the nodes are grouped together within the overall network topology.">
				</select>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblRPCPort" class="text-right form-label">RPC
					Port:</label>
			</div>
			<div class="span10">
				<input type="text" id="rpcPort" class="input-mini"
					placeholder="RPC Port" title="RPC Port" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblStoragePort" class="text-right form-label">Storage
					Port:</label>
			</div>
			<div class="span10">
				<input type="text" id="storagePort" class="input-mini"
					placeholder="Storage Port " title="Storage Port"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblDataDir" class="text-right form-label">Data
					Directory:</label>
			</div>
			<div class="span10">
				<input type="text" id="dataDir" class="input-xlarge"
					placeholder="Data Directory " title="Data Directory"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblLogDir" class="text-right form-label">Log
					Directory:</label>
			</div>
			<div class="span10">
				<input type="text" id="logDir" class="input-xlarge"
					placeholder="Log Directory" title="Log Directory"
					data-placement="right">
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label id="lblSavedCacheDir" class="text-right form-label">Saved
					Cache Directory:</label>
			</div>
			<div class="span10">
				<input type="text" id="savedCachesDir" class="input-xlarge"
					placeholder="Saved Cache Directory" title="Saved Cache Directory"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblCommitLogDir" class="text-right form-label">Commit
					Log Directory:</label>
			</div>
			<div class="span10">
				<input type="text" id="commitlogDir" class="input-xlarge"
					placeholder="Commit Log Directory" title="Commit Log Directory"
					data-placement="right">
			</div>
		</div>
	</div>
</div>
