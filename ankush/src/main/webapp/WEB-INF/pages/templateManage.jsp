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

<html>
<head>
<%@ include file="layout/header.jsp"%>
<%@ include file="layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/templateManagement.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/ankush.techlist.js"
	type="text/javascript"></script>
<style>
.deleteTemplate>ins {
	background-position: -25px -93px;
}
</style>
<script>
	$(document).ready(function() {
		com.impetus.ankush.template.getTemplate();
		templateTable = $("#templateTable").dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : true,
			"aaSorting" : [],
			"bInfo" : false,
			"bAutoWidth" : false,
			"aaSorting" : [ [ 0, "asc" ] ],
			"aoColumnDefs" : [ {
				'bSortable' : false,
				'aTargets' : [ 4 ]
			} ],
		});
		$("#templateTable_filter").hide();
		templateTable = $('#templateTable').dataTable();
		$("#confirmDeleteTemplate").appendTo('body');
		$('#templatePageSearchBox').keyup(function() {
			templateTable.fnFilter($(this).val());
		});
	});
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1>Templates</h1>
		</div>
		<div class="page-body" id="main-content">
		<%@ include file="layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
				<div class="modal fade" id="confirmDeleteTemplate" tabindex="-1"
					role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4>Delete Template</h4>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-md-12"
										style="text-align: left; font-size: 14px;">Do you want
										to delete the template?</div>
								</div>
							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
								<a href="#" id="confirmDeleteButtonDeployDialog"
									onclick="com.impetus.ankush.template.deleteTemplate();"
									class="btn btn-default" data-loading-text="Deleting...">Delete</a>
							</div>
						</div>
					</div>
				</div>
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">Templates</h3>
						<div class="pull-right panelSearch">
							<input type="text" id="templatePageSearchBox" style=""
								class="input-medium form-control" placeholder="Search" />
						</div>
					</div>
					<div class="row panel-body">
						<div class="col-md-12" style="">
							<table class="table table-striped" id="templateTable">
								<thead
									style="text-align: left; border-bottom: 1px solid #E1E3E4">
									<tr>
										<th>Template</th>
										<th>Technology</th>
										<th>Last Modified By</th>
										<th>Last Updated</th>
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
</html>

