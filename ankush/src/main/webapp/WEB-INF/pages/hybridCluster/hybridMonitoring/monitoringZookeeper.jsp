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
<%@ include file="../../layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all"/>
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/zookeeper/zookeeperMonitoring.js"
	type="text/javascript"></script>
<script>
var hybridTechnology = null;
var zookeeperNodeListTables = null;
	$(document)
			.ready(
					function() {
						$("#hybridActionsDropDown")
								.append(
										' <li><a tabindex="-1" href="#" class="text-left" onclick="com.impetus.ankush.zookeeperMonitoring.loadRunCommandPage();">Run Command</a></li>');
						zookeeperNodeListTables = $('#zookeeperNodeListTables').dataTable({
							"bJQueryUI" : false,
							"bPaginate" : false,
							"bLengthChange" : true,
							"bFilter" : false,
							"bSort" : false,
							"bInfo" : false,
							"bAutoWidth" : false,
							"sPaginationType" : "full_numbers",
							"bAutoWidth" : false,
							"bRetrieve" : true,
							"oLanguage": {
						        "sEmptyTable": 'Loading...',
						    }
						});
						$("#monitoring-page-zookeeper").text(clusterName+'/Zookeeper');
						com.impetus.ankush.zookeeperMonitoring.nodeListPopulate();
					});
</script>
</head>

<body>
<div class="page-wrapper">
		<div class="page-header heading">
				<h1 id="monitoring-page-zookeeper" class="left"></h1>
				<div class="btn-group pull-right">
					<button type="button" class="btn btn-default dropdown-toggle"
						data-toggle="dropdown">
						Actions <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" id="hybridActionsDropDown">

					</ul>
				</div>
				<ul class="notifications">
				<li
					class="dropdown dropdown-extra dropdown-notifications pull-right"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#"
					aria-expanded="false"> <i class="fa fa-lg fa-fw fa-bell"></i> <span
						class="badge-op badge-op-alert" id="notificationAlertsCount"></span>
				</a>
					<ul class="dropdown-menu">
					<li><p id="alertNotifications">You have <span id="alertsPara"></span> alert(s)</p></li>
						<li>
							<ul class="dropdown-menu-list dropdown-scroller"
								style="overflow: hidden; width: 233px; height:auto;margin-top:-12px;"
								id="notificationAlertsShow">
							</ul>
						</li>
						<li><a href="<c:out value='${baseUrl}' />/commonMonitoring/${clusterName}/events/C-D/${clusterId}/${clusterTechnology}">See all alerts<span class="fa fa-fw fa-arrow-circle-o-right pull-right mrg" style="margin-top:3;font-size:17"></span></a></li>
					</ul></li>
				<li
					class="dropdown dropdown-extra dropdown-notifications pull-right"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#"
					aria-expanded="false"> <i class="fa fa-lg fa-fw fa-clock-o"></i>
						<span class="badge-op badge-op-operation" id="notificationOperationCount"></span>
				</a>
					<ul class="dropdown-menu">
					<li><p id="operationNotifications">You have <span id="operationsPara"></span> operation(s) in progress</p></li>
						<li>
							<ul class="dropdown-menu-list dropdown-scroller"
								style="overflow: hidden; width: 250px; height: auto;margin-top:-10px;"
								id="notificationOperationShow">
							</ul>
						</li>
						<li><a href="<c:out value='${baseUrl}' />/commonMonitoring/${clusterName}/operations/C-D/${clusterId}/${clusterTechnology}">See all operations<span class="fa fa-lg fa-arrow-circle-o-right pull-right mrg" style="margin-top:4;font-size:17"></span></a></li>
					</ul></li>
			</ul>
		</div>
		<div class="page-body" id="main-content">
		<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="section-body common-tooltip">
				<div class="container-fluid">
					<div class="row" style="">
						<div class="masonry mrgt10 col-md-12" id="allTilesClusterDelve"></div>
					</div>
					<div class="panel">
						<div class="panel-heading">
							<h3 class="panel-title">Node List</h3>
						</div>
						<div class="row panel-body">
			<div class="col-md-12 text-left">
						<table class="table" id="zookeeperNodeListTables">
							<thead style="text-align: left;">
								<tr>
									<th>Host Name</th>
									<th>Server ID</th>
									<th>Server Type</th>
								</tr>
							</thead>
						</table>
						</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-backdrop loadingPage element-hide" id="showLoading"
		style=""></div>
</body>
</html>
