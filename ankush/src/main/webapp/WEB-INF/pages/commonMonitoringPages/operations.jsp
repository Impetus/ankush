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
<%@ include file="../layout/header.jsp"%>
<%@ include file="../layout/navigation.jsp"%>
<script type="text/javascript">
	var operationsTable = null;
	$(document).ready(function() {
		operationsTable = $('#operationsTable').dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : true,
			"bFilter" : true,
			"bSort" : true,
			"bInfo" : false,
			"bAutoWidth" : false,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false,
			"aaSorting" : [],
			"aoColumnDefs" : [ {
				'bSortable' : false,
				'aTargets' : [ 5 ]
			}, {
				'sType' : "date",
				'aTargets' : [ 3, 4 ]
			} ],
			"oLanguage" : {
				"sEmptyTable" : 'Loading...',
			}
		});
		$("#operationsTable_filter").css({
			'display' : 'none'
		});
		$('#searchoperationsTable').keyup(function() {
			$("#operationsTable").dataTable().fnFilter($(this).val());
		});
		com.impetus.ankush.operations.populateOperations();
		var operationList = {};
		operationList.method = function() {
			com.impetus.ankush.operations.populateOperations();
		};
		pageLevelAutorefreshArray.push(operationList);
		com.impetus.ankush.createAutorefresh(pageLevelAutorefreshArray);
	});
</script>

</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 class="left">Operations</h1>
			<div class="pull-right"
				onclick="com.impetus.ankush.alerts.saveAlertsAndHAConf();"
				id="saveAlertsButton">&nbsp;</div>
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
			<div class="container-fluid">



				<div id="nodesDetail">
					<div class="panel">
						<div class="panel-heading">
							<h3 class="panel-title">Operations</h3>
							<div class="pull-right panelSearch">
								<input id="searchoperationsTable" class="form-control"
									type="text" placeholder="Search">
							</div>
						</div>
						<div class="row panel-body">
							<div class="col-md-12 text-left">
								<table class="table" id="operationsTable">
									<thead style="text-align: left;">
										<tr>
											<th>Operation</th>
											<th>Status</th>
											<th>Started By</th>
											<th>Started At</th>
											<th>Completed At</th>
											<th></th>
										</tr>
									</thead>
									<tbody style="text-align: left;">
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
