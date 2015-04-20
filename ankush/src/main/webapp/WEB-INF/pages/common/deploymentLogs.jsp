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

<!-- Page for cluster level deployment logs -->
<head>
<%@ include file="../layout/header.jsp"%>
<%@ include file="../layout/navigation.jsp"%>
<script>
$(document).ready(function() {
	var clusterId = '<c:out value="${clusterId}"/>';
	var clusterTechnology = '<c:out value='${clusterTechnology}'/>';
	var clusterName = '<c:out value='${clusterName}'/>';
	nodeIpStatusTable = $("#nodeIpStatusTable").dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : false,
		"bFilter" : true,
		"bSort" : true,
		"bInfo" : false,
		"bAutoWidth" : false,
		"aoColumns" : [ {
			'sWidth' : '8%',
			'bSortable' : false,
		}, {
			'sWidth' : '50%',
		}, {
			'sWidth' : '15%',
			'bSortable' : false,
		}, {
			'sWidth' : '25%',
			'bSortable' : false,
		},{
			'sWidth' : '2%',
			'bSortable' : false,
		}],
		"aoColumnDefs" : [
		{
			'bSortable' : false,
			'aTargets' : [ 1, 2, 3 ]
		} ],
	});
	$("#nodeIpStatusTable_filter").hide();
	$('#nodeSearchBox').keyup(function() {
		nodeIpStatusTable.fnFilter($(this).val());
		});
	com.impetus.ankush.common.nodeStatus(clusterId,clusterTechnology);
});
</script>
</head>
<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 class="left">Deployment Progress</h1>
			<div class="col-md-5">
				<div class="pull-right mrgt5">
					<h4>
						<span class="label label-default"
							id="deploymentStatusNodeListLabel"></span>
					</h4>
				</div>
			</div>
			<div class="pull-right mrgt5">
				<button id="clusterDeleteBtn" class="btn btn-default"
					onclick="com.impetus.ankush.common.deleteDialog();">Delete</button>
			</div>
		</div>
		<div class="page-body" id="main-content">
		<%@ include file="../layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
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
									<div class="col-md-10 col-lg-4 text-left">
										<input type="password" id="passForDelete" class="form-control" /><br />
										<span id="passForDeleteError"></span>
									</div>
								</div>

							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
								<a href="#" id="confirmDeleteButtonDeployDialog"
									onclick="com.impetus.ankush.common.deleteCluster(${clusterId});"
									class="btn btn-default" data-loading-text="Deleting...">Delete</a>
							</div>
						</div>
					</div>
				</div>
				<div class="modal fade" id="deleteDialog" tabindex="-1"
					role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4>Delete Cluster</h4>
							</div>
							<div class="modal-body">

								<div class="row">
									<div class="col-md-12" style="text-align: left;">The
										Cluster will be permanently deleted. Once deleted data cannot
										be recovered.</div>
								</div>
							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn"
									id="cancelDeleteButton">Cancel</a> <a href="#"
									id="confirmDeleteButton"
									onclick="com.impetus.ankush.common.deleteCluster('deleteDialog');"
									class="btn">Delete</a>
							</div>
						</div>
					</div>
				</div>
				<div class="row" id="setupDetailLink">
					<div class="col-md-2 ">
						<h4 class="section-heading">
							<a class="" href="##"
								onclick="com.impetus.ankush.common.loadSetUpDetail(<c:out value='${clusterId}'/>,'<c:out value='${clusterTechnology}'/>')">Setup
								Details</a>
						</h4>
					</div>
					<div class="col-md-1"
						onclick="com.impetus.ankush.common.loadSetUpDetail(<c:out value='${clusterId}'/>,'<c:out value='${clusterTechnology}'/>')">
						<a style="line-height: 35px;" href="##"><i
							class="fa fa-angle-right fa-2x" style="line-height: 1.4"></i></a>
					</div>
				</div>
				<div class="row" id="nodeStatusLink">
					<div class="col-md-2 ">
						<h4 class="section-heading">
							<a href="##" class=""
								onclick="com.impetus.ankush.common.loadNodeStatus(<c:out value='${clusterId}'/>,'<c:out value='${clusterName}'/>','<c:out value='${clusterTechnology}'/>','<c:out value='${clusterState}'/>')">Deployment
								Logs</a>
						</h4>
					</div>
					<div class="col-md-1"
						onclick="com.impetus.ankush.common.loadNodeStatus(<c:out value='${clusterId}'/>,'<c:out value='${clusterName}'/>','<c:out value='${clusterTechnology}'/>','<c:out value='${clusterState}'/>')">
						<a style="line-height: 35px;" href="##"><i
							class="fa fa-angle-right fa-2x" style="line-height: 1.4"></i></a>
					</div>
				</div>
				<br>
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">Node List</h3>
						<div class="pull-right panelSearch">
							<input type="text" id="nodeSearchBox" placeholder="Search"
								class="input-medium form-control" />
						</div>
					</div>
					<div class="row panel-body">
						<div class="col-md-12 text-left">
							<table class="table table-striped" id="nodeIpStatusTable"
								>
								<thead
									style="text-align: left; border-bottom: 1px solid #E1E3E4">
									<tr>
										<th>Host Name</th>
										<th>Type</th>
										<th>Status</th>
										<th>Message</th>
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
		</div>
	</div>
</body>
