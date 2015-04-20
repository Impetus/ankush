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

<!-- Hadoop Cluster Job Monitoring page -->


<html>
<head>
<%@ include file="../../layout/header.jsp"%>
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hadoop/hadoopJobs.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		appMonitorDrillDown = $('#appMonitorDrillDown').dataTable({
			"bJQueryUI" : false,
			"bPaginate" : false,
			"bLengthChange" : true,
			"bFilter" : true,
			"bSort" : false,
			"bInfo" : false,
			"bAutoWidth" : false,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false,
			"bRetrieve" : true,
			"oLanguage" : {
				"sEmptyTable" : 'Loading...',
			}
		});
		$("#appMonitorDrillDown_filter").css({
			'display' : 'none'
		});
		$('#searchAppMonitorDrillDown').keyup(function() {
			$("#appMonitorDrillDown").dataTable().fnFilter($(this).val());
		});
	});
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1>Application Details</h1>
		</div>
		<div class="page-body" id="main-content">
			<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
				<div class="row-fluid">
					<div id="error-div-appMonitoring"
						class="col-md-12 error-div-hadoop" style="display: none;">
						<span id="popover-content-appMonitoring" style="color: red;"></span>
					</div>
				</div>


				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">Attempt List</h3>
						<div class="pull-right panelSearch">
							<input id="searchAppMonitorDrillDown" class="search-datatable"
								type="text" placeholder="Search">
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 text-left">
							<table class="table" id="appMonitorDrillDown">
								<thead style="text-align: left;">
									<tr>
										<th>Attempt Number</th>
										<th>Start Time</th>
										<th>Node ID</th>
										<th>Logs Link</th>
										<th>Node Address</th>
										<th>Container Id</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				<div class="row col-md-12 mrgt15 mrgl0">
					<div class="text-left left">
						<label class='section-heading text-left mrgt15'></label>
					</div>
				</div>
				<div id="all_tiles_JobInfo"></div>
				<div class="row-fluid col-md-12 mrgt15 mrgl0">
					<div class="text-left left">
						<label class='section-heading text-left mrgt15'></label>
					</div>
				</div>
				<div id="all_tiles_appInfo"></div>
				<div id="div_Request_JobMonitoring" class="modal hide fade"
					style="display: none;">
					<div class="modal-header text-center">
						<h4>Job Request</h4>
					</div>
					<div class="row-fluid modal-body" id="lblJobRequestMessage"></div>
					<br>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal" id="divOkbtn_JobMonitoring"
							class="btn"
							onclick="com.impetus.ankush.hadoopJobs.closeDialog_Request_JobMonitoring()">OK</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			var appId = '<c:out value='${applicationId}'/>';
			com.impetus.ankush.hadoopJobs.appMonitorDrillDownLoad(appId);
		});
	</script>


</body>
</html>
