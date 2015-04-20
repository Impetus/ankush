<!------------------------------------------------------------------------------
-  ===========================================================
-  Ankush : Big Data Cluster Management Solution
-  ===========================================================
-  
-  (C) Copyright 2014, by Impetus Technologies
-  
-  This is free software; you can redistribute it and/or modify it under
-  the terms of the GNU Lesser General Public License (LGPL v3) as
-  published by the Free Software Foundation;
-  
-  This software is distributed in the hope that it will be useful, but
-  WITHOUT ANY WARRANTY; without even the implied warranty of
-  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-  See the GNU Lesser General Public License for more details.
-  
-  You should have received a copy of the GNU Lesser General Public License 
-  along with this software; if not, write to the Free Software Foundation, 
- Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->

<head>
<%@ include file="../../layout/header.jsp"%>
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hybrid/hybridClusterCreation.js/"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hybrid/hybridSetupDetail.js"
	type="text/javascript"></script>
<style type="text/css">
.table td {
	background-color: #f9f9f9 !important;
}

#accordion>.panel {
	border: 1px solid #ddd !important;
}

#accordion>.panel>.panel-heading:after {
	border-bottom: none !important;
	content: none !important;
}

#techPanel {
	margin-bottom: -1px;;
}

#techPanel>.panel>.panel-heading:after {
	padding-bottom: 2px !important;
}

.technologyTable>thead>tr>th,.technologyTable>tbody>tr>th,.technologyTable>tfoot>tr>th,.technologyTable>thead>tr>td,.technologyTable>tbody>tr>td,.technologyTable>tfoot>tr>td
	{
	border-top: 6px solid #eee;
}
</style>
</head>
<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 id="hybridCCHeading" class="left"></h1>
			<div class="col-md-4 left">
				<button class="btn btn-danger" id="validateError"
					onclick="com.impetus.ankush.common.focusError();"
					style="display: none;" type="button"></button>
			</div>
			<div class="col-md-2 left mrgt5">
				<select id="selectTemplate" class="form-control "
					style="height: 31px; margin-top: 4px;"
					onchange="com.impetus.ankush.hybridSetupDetail.loadTemplate()">
				</select>
			</div>
			<div class="btn-group left ">
				<button class="btn btn-default saveTemplate" type="button"
					onclick="com.impetus.ankush.hybridClusterCreation.openTemplateDialog();">Save</button>
				<button data-toggle="dropdown"
					class="btn btn-default dropdown-toggle saveTemplate" type="button">
					<span class="caret" style="height: 14px; margin-top: 6px"></span>
				</button>
				<ul role="menu" class="dropdown-menu" style="min-width: 95px;">
					<li><a href="#"
						onclick="com.impetus.ankush.hybridClusterCreation.openTemplateDialog();">Save</a></li>
					<li><a href="#"
						onclick="com.impetus.ankush.hybridClusterCreation.templateDialogCreate();">Save
							As</a></li>
				</ul>
			</div>
			<div class="pull-right ">
				<button id="commonDeleteButton" class="btn btn-default"
					onclick="com.impetus.ankush.hybridClusterCreation.deleteClusterDialog();"
					disabled="disabled">Delete</button>
				<button class="btn btn-default " id="clusterDeploy"
					data-loading-text="Deploying..."
					onclick="com.impetus.ankush.hybridClusterCreation.validateCluster();">Deploy</button>
			</div>
		</div>
		<div class="page-body" id="main-content">
			<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="container-fluid mrgl15">
				<div class="modal fade" id="div_mapReduceCheck" tabindex="-1"
					role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4>Map-R Client Check</h4>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-md-12">Please ensure that MAP-R client is
										present on nodes on which OLAPEngine will be .</div>
								</div>
							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a
									href="#" id=""
									onclick="com.impetus.ankush.registerCluster.registerCluster();"
									class="btn">Proceed</a>
							</div>
						</div>
					</div>
				</div>

				<div class="modal fade" id="deleteClusterDialogDeploy" tabindex="-1"
					role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4>Delete Cluster</h4>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-md-12"
										style="text-align: left; font-size: 14px;">The Cluster
										will be permanently deleted. Once deleted data cannot be
										recovered. Please enter your password to continue.</div>
								</div>

								<div class="row mrgt20">
									<div class="col-md-2 text-right" style="font-size: 14px;" id="">
										<label class="form-label">Password:</label>
									</div>
									<div class="col-md-10 col-lg-4 text-left form-group">
										<input type="password" id="passForDelete" class="form-control" /><br />
										<span id="passForDeleteError"></span>
									</div>
								</div>

							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
								<a href="#" id="confirmDeleteButtonDeployDialog"
									onclick="com.impetus.ankush.hybridClusterCreation.deleteCluster();"
									class="btn btn-default" data-loading-text="Deleting...">Delete</a>
							</div>
						</div>
					</div>
				</div>
				<div class="modal fade" id="nodeRightChangeDialog" tabindex="-1"
					role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4>Node Right Change</h4>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-md-12"
										style="text-align: left; font-size: 14px;">This will
										remove node mapping from all the components. Confirm do you
										want to continue?</div>
								</div>
							</div>
							<div class="modal-footer">
								<a href="#" class="btn btn-default"
									onclick="com.impetus.ankush.hybridClusterCreation.nodeRightDialogCancel()">Cancel</a>
								<a href="#" id="confirmNodeRightChangeDialog"
									onclick="com.impetus.ankush.hybridClusterCreation.nodeRightToggle();"
									class="btn btn-default">Ok</a>
							</div>
						</div>
					</div>
				</div>
				<div class="modal fade" id="templateCreate" tabindex="-1"
					role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">Save Template</h4>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-md-3 text-right">
										<label class="form-label">Template Name:</label>
									</div>
									<div class="col-md-4 form-group">
										<input type="text" value="" name="" id="templateName"
											placeholder="Template Name" class="input-medium form-control"
											title="Template name" data-placement="right">
									</div>
									<div class="col-md-5 text-left">
										<label id="errorLabel"
											style="color: #DD514C; padding-top: 12px"></label>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
								<a href="#" id="confirmActionButton"
									onclick="com.impetus.ankush.hybridClusterCreation.saveTemplate();"
									class="btn btn-default">Save</a>
							</div>
						</div>
					</div>
				</div>
				<div class="modal fade" id="confirmTemplate" tabindex="-1"
					role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">Save Template</h4>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-md-12">
										<label class="text-left form-label">Template with same
											name already exists, saving will override the previous data.
											<br>Do you want to continue?
										</label>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
								<a href="#" id=""
									onclick="com.impetus.ankush.hybridClusterCreation.updateTemplateData();"
									class="btn btn-default">OK</a>
							</div>
						</div>
					</div>
				</div>

				<div class="row ">
					<div id="errorDivMain" class=" error-div-hadoop col-md-12"
						style='display: none; padding-left: 0; padding-right: 40px;'></div>
				</div>

				<div class="row">
					<div class="pull-left">
						<h4 class="section-heading">General Details</h4>
					</div>
				</div>

				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Cluster:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<input type="text" value="" name="" id="inputClusterName"
							placeholder="Cluster Name" class="input-large form-control"
							title="Cluster name" data-placement="right">
					</div>
				</div>
				<div class="row">
					<div class=" pull-left">
						<h4 class="section-heading">Java</h4>
					</div>
				</div>

				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Oracle Java JDK:</label>
					</div>
					<div class="col-md-5 col-lg-4 form-group">
						<div class="input-group">
							<span class="input-group-addon"> <input type="checkbox"
								id="oracleBundleCheck"
								onclick="com.impetus.ankush.hybridClusterCreation.checkBoxToggle('oracleBundleCheck','oracleBundleFilePath','inputJavaHome');">
							</span> <select class="form-control" disabled="disabled" name=""
								id="oracleBundleFilePath" data-placement="right"
								placeholder="Bundle Path"></select>
						</div>
					</div>
					<div class="col-md-2 col-lg-2">
						<button type="button" id="filePathDbJava"
							class="btn btn-default ladda-button" data-style="expand-right"
							data-size="l" data-color="mint" data-spinner-color="#000000"
							style="margin-bottom: 18px;" data-toggle="tooltip"
							placeholder="Upload File" title="" data-placement="right"
							onclick="com.impetus.ankush.hybridClusterCreation.javaDbPackageUpload('fileBrowseDbJava');">
							<span class="ladda-label">Upload</span>
						</button>
					</div>
					<div class="col-md-2 text-right " style="padding-top: 10px;">
						<input style='visibility: hidden; height: 10px'
							id='fileBrowseDbJava' type='file' class='' name='file'></input>
						<iframe style="width: 1px; height: 1px; border: 0px;"
							id="uploadframesharekeyJava" name="uploadframesharekeyJava"
							style="float:left;"></iframe>
						<form action="" id="uploadframesharekeyDbPackageJava"
							target="uploadframesharekeyJava" enctype="multipart/form-data"
							method="post" style="float: right; margin: 0px;"></form>
					</div>
				</div>

				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Java Home:</label>
					</div>
					<div class="col-md-5 col-lg-4 form-group">
						<input type="text" placeholder="Default Java Home"
							id="inputJavaHome" class="input-large form-control"
							title="Default Java Home" data-placement="right">
					</div>
				</div>
				<!-- 	

 -->
				<div class="row">
					<div class=" pull-left">
						<h4 class="section-heading">Node Authentication</h4>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Username:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<input type="text" value="" name="" id="inputUserName"
							onchange="com.impetus.ankush.hybridClusterCreation.userNameOnChange()"
							class="input-large form-control" placeholder="Username"
							title=" A common username for authenticating nodes. User must have administrative rights on the nodes."
							data-placement="right">
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Authentication Type:</label>
					</div>
					<div class="col-md-3 col-lg-4	 form-group ">
						<div class="btn-group" data-toggle="buttons-radio"
							id="authGroupBtn">
							<button class="btn btn-default authRadio active" data-value="0"
								name="authRadioBtn" id="btnPassword"
								onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('passwordAuthDiv','sharedKeyFileUploadDiv')">Password</button>
							<button class="btn btn-default authRadio" data-value="1"
								name="authRadioBtn" id="btnSharedKey"
								onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('sharedKeyFileUploadDiv','passwordAuthDiv')">Shared
								Key</button>
						</div>
					</div>
				</div>

				<div class="row" id="passwordAuthDiv">
					<div class="col-md-2 text-right">
						<label class=" form-label">Password:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<input type="password" value="" name="" id="inputPassword"
							placeholder="Password" class="input-large form-control	"
							title="The password to authenticate the nodes."
							data-placement="right">
					</div>
				</div>
				<div class="row " id="sharedKeyFileUploadDiv" style="display: none;">
					<div class="col-md-2 text-right">
						<label class="  form-label">Shared Key:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<iframe id="uploadShareKey" name="uploadShareKey"
							style="width: 1px; height: 0px; border: 0px;"
							class="iframe-display"></iframe>
						<form action="" id="uploadFormShareKeyAuth"
							target="uploadShareKey" enctype="multipart/form-data"
							method="post" class="iframe-formclass"
							style="margin: 0; height: 34px">
							<input id='fileBrowseShareKey' type='file'
								style='display: none; float: right;' name='file'></input> <input
								type="text" id="filePathShareKey"
								style="background-color: white; cursor: pointer;"
								readonly="readonly"
								class="file-sharekey input-medium form-control"
								data-toggle="tooltip" placeholder="Upload File"
								title="Upload share key file"
								onclick="com.impetus.ankush.hybridClusterCreation.shareKeyFileUpload(this);"
								data-placement="right"></input>
						</form>
					</div>
				</div>

				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Node Rights:</label>
					</div>
					<div class="col-md-3 col-lg-4	 form-group ">
						<div class="btn-group" data-toggle="buttons-radio"
							id="nodeRightGroupBtn">
							<button class="btn btn-default authRadio active" data-value="0"
								name="nodeRightRadioBtn" id="btnSudoNode"
								onclick="com.impetus.ankush.hybridClusterCreation.nodeRightDialogOpen('Sudo');">Sudo</button>
							<button class="btn btn-default authRadio" data-value="1"
								name="nodeRightRadioBtn" id="btnNonSudoNode"
								onclick="com.impetus.ankush.hybridClusterCreation.nodeRightDialogOpen('');">Non-Sudo</button>
						</div>
					</div>
				</div>
				<div id="nodeRightWarning"></div>

				<div class="row">
					<div class="pull-left">
						<h4 class="section-heading">Agent</h4>
					</div>
				</div>

				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Agent Install Dir:</label>
					</div>
					<div class="col-md-5 col-lg-4 form-group">
						<input type="text" value="" name="" id="agentInstallDir"
							onchange="com.impetus.ankush.hybridClusterCreation.agentPathOnChange()"
							placeholder="Default Agent Install Dir"
							class="input-large form-control"
							title="Default Agent Install Dir" data-placement="right">
					</div>
				</div>
				<div class="row">
					<div class="pull-left">
						<h4 class="section-heading">Search and Select Nodes</h4>
					</div>
				</div>

				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Mode:</label>
					</div>
					<div class="col-md-10 col-lg-4 form-group ">
						<div class="btn-group" data-toggle="buttons-radio"
							id="ipModeGroupBtn">
							<button class="btn btn-default nodeListRadio active"
								data-value="0" id="ipRangeBtn"
								onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('ipRangeDiv','fileUploadDiv')">Host
								Name List</button>
							<button class="btn btn-default nodeListRadio" data-value="1"
								id="fileUploadBtn"
								onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('fileUploadDiv','ipRangeDiv')">File
								Upload</button>
						</div>
					</div>
				</div>
				<div class="row" id="ipRangeDiv">
					<div class="col-md-2 text-right">
						<label class=" form-label">Source:</label>
					</div>
					<div class="col-md-5 col-lg-4 form-group">
						<input type="text" value="" name="" id="inputIpRange"
							placeholder="Host Name List" class="input-xlarge form-control"
							title="Comma seperated list of Host Hame to choose Nodes from"
							data-placement="right">
					</div>
				</div>
				<div class="row" id="fileUploadDiv" style='display: none;'>
					<div class="col-md-2 text-right">
						<label class=" form-label">File Path:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<iframe id="uploadframesharekey" name="uploadframesharekey"
							style="width: 1px; height: 0px; border: 0px;"
							class="iframe-display"></iframe>
						<form action="" id="uploadFormShareKeyAddnode"
							target="uploadframesharekey" enctype="multipart/form-data"
							method="post" class="iframe-formclass"
							style="margin: 0; height: 34px">
							<input type="text" id="filePath" readonly="readonly"
								style="background-color: white; cursor: pointer;"
								class="file-sharekey input-medium form-control"
								data-toggle="tooltip" placeholder="Upload File" title=""
								data-placement="right"
								onclick="com.impetus.ankush.hybridClusterCreation.getNodesUpload(this);"></input>
							<input id='fileBrowse' type='file' style="display: none;"
								name='file'></input>
						</form>
					</div>
				</div>
			
				<div class="row">
					<div class="col-md-2"></div>
					<div class="col-md-10 col-lg-2 form-group">
						<button id="nodeRetrieveBtn" class="btn btn-default ladda-button"
							data-style="expand-right" data-size="l" data-color="mint"
							data-spinner-color="#000000" type="button"
							onclick="com.impetus.ankush.hybridClusterCreation.validateNode()">
							<span class="ladda-label">Retrieve</span>
						</button>
					</div>
				</div>
				<div class="" id="nodeListDiv" style="display: none;">
					<div class="panel">
						<div class="panel-heading">
							<h3 class="panel-title left mrgt10 mrgl5">Node List</h3>
							<button type="button" class="btn btn-default ladda-button mrgl10"
								data-style="expand-right" data-size="l" data-color="mint"
								data-spinner-color="#000000" id="inspectNodeBtn"
								onclick="com.impetus.ankush.hybridClusterCreation.validateNode('inspectNodeBtn');">
								<span class="ladda-label">Inspect Nodes</span>
							</button>
							<div class="pull-right">
								<input type="text" id="nodeSearchBox" placeholder="Search"
									class="input-medium form-control" />
							</div>
							<div data-toggle="buttons-radio" id="toggleSelectButton"
								class="btn-group mrgr20 pull-right">
								<button type="button" class="btn btn-default active"
									onclick="com.impetus.ankush.common.toggleDatatable('All');">All</button>
								<button type="button" class="btn btn-default"
									onclick="com.impetus.ankush.common.toggleDatatable('Available');">Available</button>
								<button type="button" class="btn btn-default"
									onclick="com.impetus.ankush.common.toggleDatatable('Error');">Error</button>
							</div>
						</div>
						<div class="row panel-body">
							<div class="col-md-12 text-left">
								<table class="table tblborder1" id="nodeIpTable">
									<thead class="tblborder2">
										<tr>
											<th class=""></th>
											<th>Host Name</th>
											<th>OS</th>
											<th>CPU</th>
											<th>Disk Count</th>
											<th>Disk Size</th>
											<th>RAM</th>
											<th>Role</th>
											<th>Node Rights</th>
											<th>Status</th>
											<th></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>


				<div class="panel" id="techPanel">
					<div class="panel-heading" id="techPanHead">
						<h3 class="panel-title">Technologies</h3>
					</div>
				</div>
				<div class="panel-group" id="accordion" role="tablist"
					aria-multiselectable="true">
				</div>
			</div>
		</div>
		<script>
			$(document)
					.ready(
							function() {
								var clusterId = null;
								var cluster_State = null;
								clusterId = "<c:out value='${clusterId}' />";
								cluster_State = localStorage
										.getItem("storedClusterState");
								var templateName = "<c:out value='${templateName}' />";
								var clusterTechnology = "<c:out value='${clusterTechnology}' />";
								$("#hybridCCHeading").text(clusterTechnology);
								nodesTable = $('#nodeIpTable').dataTable(
										{
											"bJQueryUI" : true,
											"bPaginate" : false,
											"bLengthChange" : false,
											"bFilter" : true,
											"bSort" : true,
											"aaSorting" : [ [ 1, "asc" ] ],
											"bInfo" : false,
											"bAutoWidth" : false,
											"aoColumns" : [ {
												'sWidth' : '2%',
											}, {
												'sWidth' : '8%',
											}, {
												'sWidth' : '8%',
											}, {
												'sWidth' : '8%',
											}, {
												'sWidth' : '8%',
											}, {
												'sWidth' : '8%',
											}, {
												'sWidth' : '8%',
											}, {
												'sWidth' : '30%',
											}, {
												'sWidth' : '5%',
											}, {
												'sWidth' : '12%',
											}, {
												'sWidth' : '3%',
											}, ],
											"aoColumnDefs" : [ {
												'bSortable' : false,
												'aTargets' : [ 0, 2, 3, 4, 5,
														6, 7, 8, 9, 10 ]
											} ],
										});
								$("#nodeIpTable_filter").hide();
								$('#nodeSearchBox').keyup(function() {
									nodesTable.fnFilter($(this).val());
								});
								if (clusterId == '' || clusterId == null) {
									$("#inputClusterName").focus();
									com.impetus.ankush.hybridClusterCreation
											.tooltipInitialize();
									com.impetus.ankush.hybridClusterCreation
											.buttonClick();
								}
								com.impetus.ankush.hybridClusterCreation
										.getSAModule(clusterTechnology);
								com.impetus.ankush.hybridClusterCreation
										.getTemplateData(clusterTechnology);
								if (templateName !== "") {
									com.impetus.ankush.hybridSetupDetail
											.loadTemplate(templateName);
									$("#selectTemplate").val(templateName);
								}
								if (null != clusterId && clusterId != '') {
									com.impetus.ankush.hybridSetupDetail
											.setupDetailValuePopulate(
													clusterId, cluster_State);
								}
								if (cluster_State == "ERROR") {
									$("#inputClusterName").focus();
								}
								$('#selectTemplate').val(templateName);
								$("#templateCreate").appendTo('body');
								$("#confirmTemplate").appendTo('body');
								$("#nodeRightChangeDialog").appendTo('body');
							});
		</script>
	</div>
	<div class="modal-backdrop loadingPage element-hide" id="showLoading"
		style=""></div>
</body>
</html>