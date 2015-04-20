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
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hybrid/hybridMonitoring/hybridMonitoring_common.js"
	type="text/javascript"></script>

</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 id="monitoring-page-hadoop" class="left"></h1>
			<div class="btn-group pull-right">
				<button type="button" class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					Actions&nbsp; <span class="caret"></span>
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
			<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="section-body common-tooltip">
				<div class="container-fluid">
					<div class="row" style="display: none;">
						<div id="tilesAnkush"></div>
					</div>
					<%@ include file="../../hadoop/monitoring_hadoop/tileTables.jsp"%>
				</div>
			</div>

		</div>

	</div>
	<script>
		var hybridTechnology = null;
		$(document)
				.ready(
						function() {

							/* These variables will be set throught the common monitoring pages */
							$("#monitoring-page-hadoop").text(
									clusterName + '/Hadoop');
							var url = baseUrl + '/monitor/' + clusterId
									+ '/hadoopversion?component=Hadoop';
							var techHadoop = 'Job';
							if (com.impetus.ankush.isHadoop2(clusterId))
								techHadoop = 'Application';
							var action = 'Submit ' + techHadoop + '...';
							$("#hybridActionsDropDown")
									.append(
											' <li><a tabindex="-1" href="#" class="text-left" onclick="com.impetus.ankush.techListsAndActions[\''
													+ hybridTechnology
													+ '\'].actions[\''
													+ action
													+ '\'].methodCall()">Submit '
													+ techHadoop
													+ '...</a></li>');
							com.impetus.ankush.createCommonTiles('?category='
									+ hybridTechnology);
						});
	</script>
</body>
</html>
