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

<!-- Hadoop Add Nodes Page containing field for IP Address of node(s) to be added and a table showing availability of node after node retrieval  -->


<!-- <div class="section-header">
<div  class="row mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Add Nodes</h2>
		</div>
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnCommonAddnode"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
			</div>
 		<div class="span5 text-right">
			<button id="commonDeleteButtonHadoop" class="btn mrgr10" onclick="com.impetus.ankush.removeChild(2);">Cancel</button>
		  	<button class="btn" id="addNode" data-loading-text="Adding Nodes..."
		  	 onclick="com.impetus.ankush.hybridMonitoring.addNodeDataHybrid()">Add</button>
		 </div>
	</div>
</div>

<div class="section-body content-body">
	
</div> -->


<html>
<head>
<%@ include file="../../layout/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all" />
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hybrid/hybridMonitoring/hybridAddNode.js"
	type="text/javascript"></script>
<script>
	var addNodeHybridClusterTable = null;
	var addNodehybridTechTable = null;
	postDataAddnode = {};
	nodeStatus = {};
	$(document).ready(
			function() {
				$('#addNodesIPRange').empty();
				$('#addNodesFilePath').empty();
				$('#ipRangeCommon').tooltip();
				$('#filePath_IPAddressFile').tooltip();
				addNodeHybridClusterTable = $("#addNodeHybridClusterTable")
						.dataTable({
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
						/* "aoColumnDefs": [
						                 { 'bSortable': false, 'aTargets': [ 0 ] },
						                 { 'sType': "ip-address", 'aTargets': [1] }
						                 ], */
						});
				$('#addNodeHybridClusterTable_filter').hide();
				$('#searchaddNodeHybridClusterTable').keyup(
						function() {
							$("#addNodeHybridClusterTable").dataTable()
									.fnFilter($(this).val());
						});

				$("#addNodeIpTableHybrid_filter").css({
					'display' : 'none'
				});
				addNodehybridTechTable = $("#addNodeHybridTechTable")
						.dataTable({
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"bAutoWidth" : false,
							"sPaginationType" : "full_numbers",
							"bAutoWidth" : false,
							"aoColumnDefs" : [
							//  { 'bSortable': false, 'aTargets': [ 0,2,3,5 ] },
							// { 'sType': "ip-address", 'aTargets': [1] }
							],
						});
				$('#addNodeHybridTechTable_filter').hide();
				/* $('#searchaddNodeHybridClusterTable').keyup(function() {
					$("#addNodeIpTableHadoop").dataTable().fnFilter($(this).val());
				}); */
				$('#searchaddNodeHybridTechTable').keyup(
						function() {
							$("#addNodeHybridTechTable").dataTable().fnFilter(
									$(this).val());
						});
				$("#ipRangeHadoop").tooltip();
				com.impetus.ankush.hybridAddNode.fillTechTableHybrid();
				com.impetus.ankush.hybridAddNode.getUserType();
			});
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 class="left">Add Node</h1>
			<button class="btn btn-default pull-right mrgt5"
				data-loading-text="Adding..."
				onclick="com.impetus.ankush.hybridAddNode.addNodeDataHybrid();">Add</button>
		</div>
		<div class="page-body" id="main-content">
			<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="container-fluid mrgnlft8">
				<div class="row">
					<div id="error-div-hadoop" class="col-md-12 error-div-hadoop"
						style="display: none;">
						<span id="popover-content" style="color: red"></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 text-left">
						<label class="form-label section-heading">Search and
							Select Nodes</label>
					</div>
				</div>
				<!-- <div class="row">
			<div class="span2">
				<label class="text-right form-label">IP Address:</label>
			</div>
			<div class="span10 text-left">
			<label class="radio inline text-left form-label" style="padding-top: 0px;">
					<input type="radio" name="ipAddress" id="ipRange" value="Range"
					checked="checked" style="vertical-align: middle; float: none; margin-top:-2px"
				onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPRange');"/><span>&nbsp;&nbsp;Range&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				</label> <label class="radio inline text-left form-label"
					style="padding-top: 0px; margin-left: 20px;"> <input
					type="radio" name="ipAddress" id="ipFile" value="Upload File"
					style="vertical-align: middle; float: none;  margin-top:-2px"
					onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPFileUpload');" /><span>&nbsp;&nbsp;Upload
						File</span>
			</label>
			</div>
		</div> -->

				<div class="row">
					<div class="text-right col-md-2">
						<label class="form-label">Mode :</label>
					</div>
					<div class="col-md-10 form-group">
						<div class="btn-group" data-toggle="buttons-radio"
							id="addNodeGroupBtn" style="margin-top: 0px;">
							<button class="btn btn-default nodeListRadio active"
								data-value="Range" name="ipAddress" id="ipRange"
								onclick="com.impetus.ankush.hybridAddNode.divShowOnClickIPAddress('div_IPRange');">Host
								Name</button>
							<button class="btn btn-default nodeListRadio"
								data-value="Upload File" id="ipFile" name="ipAddress"
								onclick="com.impetus.ankush.hybridAddNode.divShowOnClickIPAddress('div_IPFileUpload');">File
								Upload</button>
						</div>
					</div>
				</div>


				<div class="row" id="div_IPRange">
					<div class="text-right col-md-2">
						<label class="form-label">Source :</label>
					</div>
					<div class="col-md-10 col-lg-2 form-group">
						<input type="text" class="input-large form-control"
							id="ipRangeHadoop" placeholder="Host Name" value=""
							data-toggle="tooltip" data-placement="right"
							title="Comma seperated list of hosts to choose Nodes from"></input>
					</div>
				</div>
				<div class="row" id="div_IPFileUpload" style="display: none;">
					<div class="col-md-2 text-right">
						<label class="text-right form-label">File Upload: </label>
					</div>
					<div class="col-md-10 form-group">
						<iframe style="width: 1px; height: 1px; border: 0px;"
							id="uploadframeIPAddressFile" name="uploadframeIPAddressFile"
							style="float:left;"></iframe>
						<form action="" id="uploadframeIPAddressFile_Form"
							target="uploadframeIPAddressFile" enctype="multipart/form-data"
							method="post" style="float: left; margin: 0px;">
							<input style='visibility: hidden; float: right; height: 0'
								id='fileBrowse_IPAddressFile' type='file' class='' name='file'></input><input
								type="text" id="filePath_IPAddressFile" data-toggle="tooltip"
								placeholder="Upload File" title="File with Host Name List"
								data-placement="right" readonly="readonly"
								style="cursor: pointer; float: left;"
								onclick="com.impetus.ankush.hybridAddNode.hadoopIPAddressFileUpload();"
								class="input-large form-control"></input>
						</form>
					</div>
				</div>
				<div class="row">

					<div class="col-md-2">&nbsp;</div>
					<div class="col-md-10 text-left form-group">
						<button class="btn btn-default" id="retrieve"
							style="margin-right: 123px;"
							onclick="com.impetus.ankush.hybridAddNode.getNodes()">Retrieve</button>
					</div>
				</div>
				<!-- <div id="addNodeTable">
		<div class="row">
		<div class="col-md-12 row">
			<div class="text-left col-md-3" style="padding-top: 20px;float:left">
				<label class='section-heading text-left left'>Node List</label>
				<button type="button" class="btn btn-default left mrgl10" id="inspectNodeBtn" data-loading-text="Inspecting Nodes..." onclick="com.impetus.ankush.hybridMonitoring.inspectNodesObject('inspectNodeBtn');">Inspect Nodes</button>
			</div>
			<div class="text-left col-md-3" style="padding-top: 20px;float:left">
				<label class='text-left' id="retrieveStatus"></label>
			</div>
			<div class="text-right">
						<input id="searchaddNodeHybridClusterTable" type="text"
							placeholder="Search">
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-left">
				<table class="table table-striped" id="addNodeHybridClusterTable" 
							style="border: 1px solid; border-color: #CCCCCC; text-align: center; vertical-align: middle;"
							width="100%">
					<thead style="text-align: center;">
						<tr>
								<th style="width :1%"></th>
							    <th>IP</th>
								<th>OS</th>
								<th></th>
						</tr>
					</thead>
					<tbody style="text-align: left;">
					</tbody>
				</table>
			</div>
		</div> -->
				<div class="panel">
					<!-- <div class="panel-heading">
						<h3 class="panel-title">Node List</h3>
						<button type="button"
							class="btn btn-default ladda-button"
							data-style="expand-right" data-size="l" data-color="mint"
							data-spinner-color="#000000" id="inspectNodeBtn"
							onclick="com.impetus.ankush.hybridAddNode.inspectNodesObject('inspectNodeBtn');">Inspect
							Nodes</button>
						<div class="pull-right panelSearch">
							<input id="searchaddNodeHybridClusterTable" class="form-control" type="text"
								placeholder="Search">
						</div>
					</div>
				 -->
					<div class="panel-heading">
						<h3 class="panel-title left mrgt10 mrgl5">Node List</h3>
						<button type="button" class="btn btn-default ladda-button mrgl10"
							data-style="expand-right" data-size="l" data-color="mint"
							data-spinner-color="#000000" id="inspectNodeBtn"
							disabled="disabled"
							onclick="com.impetus.ankush.hybridAddNode.inspectNodesObject('inspectNodeBtn');">
							<span class="ladda-label">Inspect Nodes</span>
						</button>
						<div class="pull-right">
							<input type="text" id="searchaddNodeHybridClusterTable"
								placeholder="Search" class="input-medium form-control" />
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
							<table class="table" id="addNodeHybridClusterTable">
								<thead style="text-align: center;">
									<tr>
										<th style="width: 1%"></th>
										<th>Host Name</th>
										<th>OS</th>
										<th>CPU</th>
										<th>Disk Count</th>
										<th>Disk Size</th>
										<th>RAM</th>
										<th>Node Rights</th>
										<th>Status</th>
										<th></th>
									</tr>
								</thead>
								<tbody style="text-align: left;">
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">Technologies</h3>
						<div class="pull-right panelSearch">
							<input id="searchaddNodeHybridTechTable" class="form-control"
								type="text" placeholder="Search">
						</div>
					</div>
					<div class="row panel-body">
						<div class="col-md-12 text-left">
							<table class="table" id="addNodeHybridTechTable">
								<thead style="text-align: left;">
									<tr>
										<th><input type='checkbox' id='addNodeOnTechHybrid'
											name="addNodeCheckHead"
											onclick="com.impetus.ankush.hybridAddNode.enableAllMapNode(this)"></th>
										<th>Technology</th>
										<th>Existing Nodes</th>
										<th style="width: 3%;">Nodes</th>
										<th style="width: 15%;"></th>
									</tr>

								</thead>
							</table>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>
