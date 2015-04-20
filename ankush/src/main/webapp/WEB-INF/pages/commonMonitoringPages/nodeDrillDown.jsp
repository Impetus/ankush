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
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/commonMonitoring/nodeDrillDown.js"
	type="text/javascript"></script>
<script>
var hostName = '';
var nodeServiceDetail = null;
	$(document).ready(function(){
		nodeServiceDetail=	$('#nodeServiceDetail').dataTable({
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
		});
		$("#nodeServiceDetail_filter").css({
			'display' : 'none'
		});
		$('#searchnodeServiceDetail').keyup(function() {
			$("#nodeServiceDetail").dataTable().fnFilter($(this).val());
		});
		hostName = '<c:out value='${hostName}'/>';
		$("#commonNodeDrillDown_NodeIP").text(hostName);
		com.impetus.ankush.nodeDrillDown.pageLoadFunction_NodeDrillDown(hostName);
		var nodeDrillDown = {};
		nodeDrillDown.method = function(){
			com.impetus.ankush.nodeDrillDown.pageLoadFunction_NodeDrillDown(hostName);
		};
		pageLevelAutorefreshArray.push(nodeDrillDown);
		com.impetus.ankush.createAutorefresh(pageLevelAutorefreshArray);
	});
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 id="commonNodeDrillDown_NodeIP" class="left"></h1>
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
				<div class="row">
					<div id="error-div" class="col-md-12 error-div-hadoop"
						style="display: none;">
						<span id="popover-content"></span>
					</div>
				</div>
				<div class="row">
					<div class="masonry mrgt10" id="tilesNodes_NDD"></div>
				</div>
				<!-- This div is for hadoop table common monitoring page -->
				<c:if test="${clusterTechnology eq 'Hybrid'}">
					<%@ include
						file="../hybridCluster/hybridMonitoring/technologyData.jsp"%>
				</c:if>
				<div class="clear"></div>
				<div class="row" id="common-section">
					<div class="boxToExpand col-md-6">
						<div class="panel">
							<div class="panel-heading">
								<h3 class="panel-title left">Utilization Graphs</h3>
								<div class="btn-group" id="graphButtonGroup_JobMonitoring"
									data-toggle="buttons-radio">
									<a href="#" class="btn btn-default btn-sm active"
										id="btnLastYear_HNDD"
										onclick="com.impetus.ankush.nodeDrillDown.drawGraph_JobMonitoring('lastyear','<c:out value ='${hostName}'/>');">1
										y</a> <a href="#" class="btn btn-default btn-sm"
										id="btnLastMonth_HNDD"
										onclick="com.impetus.ankush.nodeDrillDown.drawGraph_JobMonitoring('lastmonth','<c:out value ='${hostName}'/>');">1
										m</a> <a href="#" class="btn btn-default btn-sm"
										id="btnLastWeek_HNDD"
										onclick="com.impetus.ankush.nodeDrillDown.drawGraph_JobMonitoring('lastweek','<c:out value ='${hostName}'/>');">1
										w</a> <a href="#" class="btn btn-default btn-sm"
										id="btnLastDay_HNDD"
										onclick="com.impetus.ankush.nodeDrillDown.drawGraph_JobMonitoring('lastday','<c:out value ='${hostName}'/>');">1
										d</a> <a href="#" class="btn btn-default btn-sm"
										id="btnLastHour_HNDD"
										onclick="com.impetus.ankush.nodeDrillDown.drawGraph_JobMonitoring('lasthour','<c:out value ='${hostName}'/>');">1
										h</a>
								</div>
								<a
									href="<c:out value='${baseUrl}'/>/commonMonitoring/<c:out value='${clusterName}'/>/nodeDetails/nodeUtilizationTrend/C-D/<c:out value='${clusterId}'/>/<c:out value='${clusterTechnology}'/>/<c:out value='${hostName}'/>"
									class="fa fa-chevron-right mrgl10 pull-right mrgt5 mrgr10"
									style="font-size: 16px;"></a>

							</div>
							<div class="panel-body" id="node-drill-down-graph">
								<div class="contentExpand" id="nodeDrillDown_Graph">
									<svg></svg>
								</div>
							</div>
						</div>
					</div>
					<div class="boxToExpand col-md-6">
						<div class="panel">
							<div class="panel-heading">
								<h3 class="panel-title left">Services</h3>
								<div class="btn-group mrgl10">
									<a href="#" data-toggle="dropdown"
										class="btn btn-default dropdown-toggle">Actions&nbsp;<span
										class="caret"></span></a>
									<ul class="dropdown-menu">
										<li><a href="#"
											onclick="com.impetus.ankush.nodeDrillDown.serviceAction('startChecked'); "
											style="height: 20px;">Start </a></li>
										<li><a href="#"
											onclick="com.impetus.ankush.nodeDrillDown.serviceAction('stopChecked');"
											style="height: 20px;">Stop </a></li>
									</ul>
								</div>
								<div class="pull-right panelSearch">
									<input id="searchnodeServiceDetail"
										class="search-datatable search-datatable-button" type="text"
										placeholder="Search">
								</div>
							</div>
							<div class="row panel-body">
								<div class="col-md-12 text-left">
									<table class="table" id="nodeServiceDetail">
										<thead style="text-align: left;">
											<tr>
												<th><input type="checkbox"
													onclick="com.impetus.ankush.checked_unchecked_all('checkedServices',this,1)"
													id="servicesHeadCheckbox" /></th>
												<th>Component</th>
												<th>Service</th>
												<th>Status</th>
												<th>Actions</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="modal fade" id="div_RequestSuccess_NodeServices"
					tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4>Service Request</h4>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-md-12"
										style="text-align: left; font-size: 14px;"
										id="service-message"></div>
								</div>

							</div>
							<div class="modal-footer">
								<a href="#" data-dismiss="modal" class="btn btn-default">OK</a>
							</div>
						</div>
					</div>
				</div>
				<c:if test="${clusterTechnology eq 'Cassandra'}">
					<div class="row">
						<div>
							<h4 class="section-heading">
								<a href="#"
									onclick="com.impetus.ankush.cassandraMonitoring.loadNodeParameters(<c:out value ='${nodeIndex}'/>);">
									Parameter&nbsp;&nbsp;&nbsp;&nbsp;<img
									src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
								</a>
							</h4>
						</div>
					</div>
				</c:if>

			</div>
		</div>
	</div>
	<div class="modal-backdrop loadingPage element-hide" id="showLoading"
		style=""></div>
</body>
</html>
