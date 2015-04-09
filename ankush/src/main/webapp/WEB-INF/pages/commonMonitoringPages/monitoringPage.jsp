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
<script src="<c:out value='${baseUrl}' />/public/js3.0/sparkline.js"
	type="text/javascript"></script>
<script src="<c:out value='${baseUrl}' />/public/js3.0/heatMap.js"
	type="text/javascript"></script>
<script>
	$(document)
			.ready(
					function() {
						$("#invalidAddNodeOperation").appendTo('body');
						utilizationTrend = $('#utilizationTrend').dataTable(
								{
									"bJQueryUI" : false,
									"bPaginate" : false,
									"bLengthChange" : true,
									"bFilter" : false,
									"bSort" : false,
									"bInfo" : false,
									"bAutoWidth" : false,
									"sPaginationType" : "full_numbers",
									"bAutoWidth" : false,
									"fnRowCallback" : function(nRow, aData,
											iDisplayIndex, iDisplayIndexFull) {
										$(nRow).attr('id',
												'serviceRow-' + iDisplayIndex);
									}
								});
						/* These variables will be set throught the common monitoring pages */
						var monitoringClusterId = '<c:out value='${clusterId}'/>';
						var monitoringClusterName = '<c:out value='${clusterName}'/>';
						var monitoringClusterTechnology = '<c:out value='${clusterTechnology}'/>';
						jspPage = '<c:out value='${page}'/>';
						com.impetus.ankush.commonMonitoring.clusterId = monitoringClusterId;
						com.impetus.ankush.commonMonitoring.clusterTechnology = monitoringClusterTechnology;
						com.impetus.ankush.commonMonitoring.clusterName = monitoringClusterName;
						$("#monitoring-page").text(monitoringClusterName);
						
						//com.impetus.ankush.changeWidth('monitoringPageRightDiv', -2);
						com.impetus.ankush.createCommonTiles();
						/* function will create heatmaps on the basis of cluster id */
						com.impetus.ankush.commonMonitoring.commonHeatMaps(com.impetus.ankush.commonMonitoring.clusterId);
						com.impetus.ankush.sparkline.graphsLoad();
						for ( var key in com.impetus.ankush.techListsAndActions[clusterTechnology].actions) {
							var idForKey = '';
							if (key.split('...').length > 1)
								idForKey = key.split('...')[0];
							else
								idForKey = key;
							$("#actionsDropDown")
									.append(
											' <li id="action_'
													+ idForKey
															.split(
																	' ')
															.join(
																	'_')
													+ '"><a tabindex="-1" href="#" class="text-left" id="link_'
													+ idForKey	
															.split(
																	' ')
															.join(
																	'_')
													+ '" onclick="com.impetus.ankush.techListsAndActions[\''
													+ clusterTechnology
													+ '\'].actions[\''
													+ key
													+ '\'].methodCall()">'
													+ key
													+ '</a></li>');
						}
						/* autorefresh code */
						var clusterGraph = {};
						clusterGraph.method = function(){
							com.impetus.ankush.sparkline.graphsLoad();
						};
						var clusterHeatMap = {};
						clusterHeatMap.method = function(){
							com.impetus.ankush.commonMonitoring.commonHeatMaps(com.impetus.ankush.commonMonitoring.clusterId);
						};
						var clusterTiles = {};
						clusterTiles.method = function(){
							com.impetus.ankush.createCommonTiles();
						};
						pageLevelAutorefreshArray.push(clusterGraph);
						pageLevelAutorefreshArray.push(clusterHeatMap);
						pageLevelAutorefreshArray.push(clusterTiles);
						com.impetus.ankush.createAutorefresh(pageLevelAutorefreshArray);
						var tooltipShow = '<div class="left" style="width:80px">Normal</div><div class="legand-normal"></div><br/><div class="left" style="width:80px">Warning</div><div class="legand-warning"></div><br/><div class="left" style="width:80px">Critical</div><div class="legand-critical"></div><br/><div class="left" style="width:80px">Unavailable</div><div class="legand-alert-unavailable">';
						$("#legend-tooltip").attr('data-original-title', tooltipShow);
						$("#legend-tooltip").attr('data-placement', "top");
						$("#legend-tooltip").popover({
							html : true,
							template : '<div class="popover" role="tooltip" style="width:130px"><div class="arrow"></div><h3 class="popover-title" style="background-color:#fff;border-bottom:none;">Legend</h3><div class="popover-content">'+tooltipShow+'</div></div>'
						});
					});
</script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1 id="monitoring-page" class="left"></h1>
	<div class="btn-group pull-right">
	  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	    Actions&nbsp;<span class="caret"></span>
	  </button>
	  <ul class="dropdown-menu" role="menu" id="actionsDropDown">
	    
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
<%@ include file="../layout/breadcrumbs.jsp"%>	
	<div class="container-fluid" id="div_commonMonitoringDetails">
	<div class="row">
			<div id="error-div" class="col-md-12 error-div-hadoop"
				style="display: none;">
		<span id="popover-content" style="color: red"></span>
	</div>
		</div>
		<div class="row">
			<div class="col-md-7">
				<div class="row" style="display:none;">
					<div id="tilesAnkush"></div>	
				</div>
				<div class="row"></div>
					<%@ include file="../hybridCluster/hybridMonitoring/monitoringTechTable.jsp"%>
				</div>
			<!-- Section for heatmap graphs and sparklinegraphs -->
			<div class="left col-md-5" id="monitoringPageRightDiv"
				>
				<!-- Graphs box for sparkline -->
				<div id="sparkLineGraphBox" class="mrgl10" style="width: 100%;">
								<div class="panel">
									<div class="panel-heading">
										<h3 class="panel-title">Cluster Utilization</h3>
										<a href="<c:out value="${baseUrl}" />/commonMonitoring/<c:out value="${clusterName}" />/utilizationTrend/C-D/<c:out value="${clusterId}" />/<c:out value="${clusterTechnology}" />" class="fa fa-chevron-right mrgl10 pull-right mrgr10 mrgtneg15" style="font-size: 16px;"></a>
									</div>
									<div class="panel-body">
										<div class="boxToExpand col-md-12" style="height: auto;">
											<div style="display: block; height: 150px;"
												class="contentExpand"
												onclick="">
												<div class="row box infobox masonry-brick">
													<div style="box-shadow: 0 0 0 !important;"
														class="left col-md-6 loadingDiv">
														<div id="cpuSpark">
															<svg></svg>
														</div>
													</div>
													<div style="box-shadow: 0 0 0 !important;"
														class="left col-md-6 loadingDiv">
														<div id="memorySpark">
															<svg></svg>
														</div>
													</div>
												</div>
												<div class="row box infobox masonry-brick">
													<div style="box-shadow: 0 0 0 !important;"
														class="left col-md-6 loadingDiv">
														<div id="networkSpark">
															<svg></svg>
														</div>
													</div>
													<div style="box-shadow: 0 0 0 !important;"
														class="left col-md-6 loadingDiv">
														<div id="loadSpark">
															<svg></svg>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

							</div>
				<div class="heatmap_tbl left mrgl10" style="width:100%">
								<div class="panel">
									<div class="panel-heading">
											<h3 class="panel-title">Heat Map</h3>
											<a class="mrgl10 pull-right mrgr10 mrgtneg20" data-toggle="tooltip" data-placement="top" style="font-size: 16px;"href="#" id="legend-tooltip">Legend</a>
											<div class="pull-right" style="margin-top:-22px;">
													<div class="btn-group" data-toggle="buttons-radio" >
														<!-- <button type="button" class="btn btn-default active"
															id="cpuHeatMap" onclick="commonHeatMapTypeSelect('CPU');">CPU</button>
														<button type="button" class="btn btn-default"
															id="memoryHeatMap"
															onclick="commonHeatMapTypeSelect('Memory');">Memory</button> -->
															<a href="#" class="btn btn-default btn-sm active" id="cpuHeatMap" onclick="commonHeatMapTypeSelect('CPU');">CPU</a>
            												<a href="#" class="btn btn-default btn-sm" id="memoryHeatMap"
															onclick="commonHeatMapTypeSelect('Memory');">Memory</a>
													</div>
												</div>
												
											</div>
										<div class="panel-body">
										<div class="text-left" id="cassandra-heat-map">
											<div id="heat_map" style=""></div>
										</div>
									</div>
									</div>
								</div>
								<!-- <div class="thumbnail heatmap_block col-md-12 pad10"> -->
						
						<!-- <div class="heat_map_line_break"></div> -->
						
						<div class="clear"></div>
					</div>
					
				
		</div>
		<div id="commonLinks"></div>
	</div>


<c:if test="${clusterTechnology eq 'Cassandra'}">
	<div id="div_RequestSuccess_Action" class="modal hide fade"
		style="display: none;">
		<div class="modal-header text-center">
			<h4>Action Request</h4>
		</div>
		<div class="modal-body">
			<div class="row">
				<div class="col-md-12" style="text-align: left; font-size: 14px;">
					Action update request placed successfully. This will take some
					time.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn">OK</a>
		</div>
	</div>
</c:if>

	<div class="modal fade" id="confirmStartStopCluster" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 id="startStopDialogHeading"></h4>
					</div>
					<div class="modal-body">
						<div class="row">
			<div class="col-md-12" style="text-align: left; font-size: 14px;">
				Enter your password to Continue.</div>
		</div>
		<div class="row mrgt20">
								<div class="col-md-2 text-right" style="font-size: 14px;" id="">
									<label class="form-label">Password:</label>
								</div>
								<div class="col-md-10 col-lg-4 text-left">
									<input type="password" class="form-control" palaceholder="Enter Password" id="passForStartStop"/>
								</div>
								 <div id="passForStartStopError" class="error"></div>
							</div>
		
					</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a> <a href="#"
			class="btn btn-default" id="startStopButton"></a>
					</div>
				</div>
			</div>
		</div>
	

<!-- Dialog for error message in Start/stop -->
<div class="modal fade" id="errorStartStopCluster" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4>Error</h4>
					</div>
						<div class="modal-body">
							<div class="row">
								<div class="col-md-12" id="startStopError">
								</div>
							</div>

						</div>
						<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn btn-default">Ok</a>
					</div>
				</div>
			</div>
		</div>



<div class="modal fade" id="deleteClusterDialogcommonMonitor" tabindex="-1"
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
								<div class="col-md-12" id="deleteDiv"
									style="text-align: left; font-size: 14px;"></div>

							</div>
							<div class="row mrgt20">
								<div class="col-md-2 text-right" style="font-size: 14px;" id="">
									<label class="form-label">Password:</label>
								</div>
								<div class="col-md-10 col-lg-4 text-left">
									<input type="password" class="form-control" palaceholder="Enter Password" id="passForDelete" />
								</div>
								<div id="passForDeleteError" class="error"></div>
							</div>

						</div>
						<div class="modal-footer">
							<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a> <a
								href="#" id="confirmDeleteButtonHadoopMonitor"
								onclick="com.impetus.ankush.commonMonitoring.deleteCluster();"
								class="btn btn-default" data-loading-text="Deleting...">Delete</a>
						</div>
					</div>
			</div>
		</div>




<div class="modal fade" id="unregisterClusterDialogcommonMonitor" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4>Unregister Cluster</h4>
					</div>
						<div class="modal-body">
							<div class="row">
								<div class="col-md-12"
									style="text-align: left; font-size: 14px;">The Cluster
									will be permanently deleted. Once deleted data cannot be
									recovered. Please enter your password to continue.</div>
							</div>
							<div class="row mrgt20">
								<div class="col-md-12" id="deltech"
									style="text-align: left; font-size: 14px;"></div>
							</div>
							<div class="row mrgt20">
								<div class="col-md-12" id="unregtech"
									style="text-align: left; font-size: 14px;"></div>
							</div>
							<div class="row mrgt20">
								<div class="col-md-2 text-right" style="font-size: 14px;" id="">
									<label class="form-label">Password:</label>
								</div>
								<div class="col-md-10 col-lg-4 text-left">
									<input type="password" class="form-control" id="passForUnregister" />
								</div>
								<div
										id="passForUnregisterError"></div>
							</div>
						</div>


						<div class="modal-footer">
							<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a> <a
								href="#" id="confirmUnregisterButtonHadoopMonitor"
								onclick="com.impetus.ankush.commonMonitoring.unregisterCluster();"
								class="btn btn-default" data-loading-text="Unregistering...">Unregister</a>
						</div>
					</div>
			</div>
		</div>

<div id="invalidAddNodeOperation" class="modal hide fade"
	style="display: none;">
	<div class="modal-header text-center">
		<h4>Invalid Operation</h4>
	</div>
	<div class="modal-body">
		<div class="row">
			<div class="col-md-12" style="text-align: left; font-size: 14px;">
				Node addition operation is not allowed for cloud cluster.</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" data-dismiss="modal" class="btn">Ok</a>
	</div>

</div>	
</div>
</div>
</body>
</html>
	
