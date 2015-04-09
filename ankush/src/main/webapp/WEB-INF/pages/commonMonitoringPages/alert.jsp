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
<title>Alerts and High Availability</title>
<head>
<%@ include file="../layout/header.jsp"%>
<%@ include file="../layout/navigation.jsp"%>
<script src="<c:out value='${baseUrl}' />/public/js3.0/navigation.js"
	type="text/javascript"></script>
<style>
.edit {
	background-color: 'red';
}
</style>
<script>
	var serviceHAConfTable = null;
	var configAlertsUsageTable = null;
	$(document).ready(function() {
		$.fn.editable.defaults.mode = 'inline';
		configAlertsUsageTable = $('#configAlertsUsageTable').dataTable({
			"bJQueryUI" : false,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : false,
			"bSort" : false,
			"bInfo" : false,
			"bAutoWidth" : false,
		});
		serviceHAConfTable = $('#serviceHAConfTable').dataTable({
			"bJQueryUI" : false,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : false,
			"bSort" : false,
			"bInfo" : false,
			"bAutoWidth" : false,
			"oLanguage" : {
				"sEmptyTable" : 'Loading...',
			}
		});
		com.impetus.ankush.alerts.getDefaultAlertValues(clusterId);
		com.impetus.ankush.alerts.populateTableHA();
		$(".tooltipAlertPage").tooltip();

	});
</script>

</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 class="left">Alerts and High Availability</h1>

			<button class="btn btn-default pull-right"
				data-loading-text="Saving..."
				onclick="com.impetus.ankush.alerts.saveAlertsAndHAConf();"
				id="saveAlertsButton">Save</button>
			<ul class="notifications">
				<li
					class="dropdown dropdown-extra dropdown-notifications pull-right"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#"
					aria-expanded="false"> <i class="fa fa-lg fa-fw fa-bell"></i> <span
						class="badge-op badge-op-alert" id="notificationAlertsCount"></span>
				</a>
					<ul class="dropdown-menu">
						<li><p id="alertNotifications">
								You have <span id="alertsPara"></span> alert(s)
							</p></li>
						<li>
							<ul class="dropdown-menu-list dropdown-scroller"
								style="overflow: hidden; width: 233px; height: auto; margin-top: -12px;"
								id="notificationAlertsShow">
							</ul>
						</li>
						<li><a
							href="<c:out value='${baseUrl}' />/commonMonitoring/${clusterName}/events/C-D/${clusterId}/${clusterTechnology}">See
								all alerts<span
								class="fa fa-fw fa-arrow-circle-o-right pull-right mrg"
								style="margin-top: 3; font-size: 17"></span>
						</a></li>
					</ul></li>
				<li
					class="dropdown dropdown-extra dropdown-notifications pull-right"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#"
					aria-expanded="false"> <i class="fa fa-lg fa-fw fa-clock-o"></i>
						<span class="badge-op badge-op-operation"
						id="notificationOperationCount"></span>
				</a>
					<ul class="dropdown-menu">
						<li><p id="operationNotifications">
								You have <span id="operationsPara"></span> operation(s) in
								progress
							</p></li>
						<li>
							<ul class="dropdown-menu-list dropdown-scroller"
								style="overflow: hidden; width: 250px; height: auto; margin-top: -10px;"
								id="notificationOperationShow">
							</ul>
						</li>
						<li><a
							href="<c:out value='${baseUrl}' />/commonMonitoring/${clusterName}/operations/C-D/${clusterId}/${clusterTechnology}">See
								all operations<span
								class="fa fa-lg fa-arrow-circle-o-right pull-right mrg"
								style="margin-top: 4; font-size: 17"></span>
						</a></li>
					</ul></li>
			</ul>
		</div>

		<div class="page-body" id="main-content">
			<%@ include file="../layout/breadcrumbs.jsp"%>
			<div class="container-fluid mrgnlft8">
				<div class="row">
					<div id="error-div" class="col-md-12 error-div-hadoop"
						style="display: none;">
						<span id="popover-content" style="color: red"></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right">
						<label class="form-label">Refresh Interval(Sec):</label>
					</div>
					<div class="col-md-1" style="margin-top: 5px;">
						<span id="refreshInterval">-</span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right">
						<label class="form-label">Mailing List:</label>
					</div>
					<div class="col-md-5 form-group">
						<input type="checkbox" class="left" id="alertInform"
							style="margin-right: 10px; margin-top: 7px" /><label
							class="text-left  form-label left" style="color: black;">Inform
							All Administrators</label>
					</div>

				</div>
				<div class="row">
					<div class="text-right col-md-2 ">
						<label class="form-label">Email:</label>
					</div>
					<div class="col-md-3 form-group">
						<input type="text" id="mailingList"
							title="Enter mail list seperated by commas."
							class="tooltipAlertPage form-control" data-placement="right"
							placeholder="Email" />
					</div>
				</div>


				<div class="panel" style="width: 50%;">
					<div class="row panel-body">
						<div class="col-md-12 text-left">
							<table class="table" id="configAlertsUsageTable">
								<thead style="text-align: left;">
									<tr>
										<th style="width: 30%;">Property</th>
										<th style="width: 35%;">Warn(%)</th>
										<th style="width: 35%;">Alert(%)</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>

				</div>

				<div class="panel" style="width: 50%;">
					<div class="panel-heading">
						<h3 class="panel-title">HA Configurations</h3>
						<div class="pull-right panelSearch">
							<input id="searchhybridDetailsTable" class="search-datatable"
								type="text" placeholder="Search">
						</div>
					</div>
					<div class="row panel-body">
						<div class="col-md-12">
							<table class="table" id="serviceHAConfTable">
								<thead style="text-align: left;">
									<tr>
										<th style="width: 10%;"><input type="checkbox"
											onclick="com.impetus.ankush.alerts.checkAll('checkedHAServices',this,1)"
											id="haservicesHeadCheckbox" /></th>
										<th style="width: 15%;">Component</th>
										<th style="width: 15%;">Service</th>
										<th style="width: 30%;">Try Count</th>
										<th style="width: 30%;">Delay Interval(Sec)</th>
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
