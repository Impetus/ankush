<!------------------------------------------------------------------------------
-===========================================================
-Ankush : Big Data Cluster Management Solution
-===========================================================
-
-(C) Copyright 2014, by Impetus Technologies
-
-This is free software; you can redistribute it and/or modify it under
-the terms of the GNU Lesser General Public License (LGPL v3) as
-published by the Free Software Foundation;
-
-This software is distributed in the hope that it will be useful, but
-WITHOUT ANY WARRANTY; without even the implied warranty of
-MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-See the GNU Lesser General Public License for more details.
-
-You should have received a copy of the GNU Lesser General Public License 
-along with this software; if not, write to the Free Software Foundation, 
-Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->
<%@ include file="../layout/blankheader.jsp"%>
<script src="<c:out value='${baseUrl}' />/public/js/hadoop/heatMap.js"
	type="text/javascript"></script>
<script>
function autoRefreshMonitoringPage(){
	var obj1 = {};
	var obj2 = {};
	var obj3 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autorefreshTiles_monitoringPage'; 
	obj1.callFunction = 'com.impetus.ankush.commonMonitoring.createCommonTiles('+com.impetus.ankush.commonMonitoring.clusterId+')';
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	obj2.varName = 'is_autoRefreshHeatMap_monitoringPage';
	obj2.callFunction = "com.impetus.ankush.commonMonitoring.commonHeatMaps("+com.impetus.ankush.commonMonitoring.clusterId+");";
	obj2.time = 30000;
	autoRefreshArray.push(obj2);
	obj3.varName = 'is_autoRefreshSparkLine_monitoringPage';
	obj3.callFunction = "get_sparkline_json();";
	obj3.time = 15000;
	autoRefreshArray.push(obj3);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	$(document)
			.ready(
					function() {
						 $('#allTilesCluster').masonry({ 
							itemSelector : '.item',
							columnWidth : 100,
							isAnimated : true
						});
						 $('#allTilesCluster').bind('resize', function(e) {
							  com.impetus.ankush.commonMonitoring.createCommonTiles(com.impetus.ankush.commonMonitoring.clusterId);
							});
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
						var monitoringClusterEnv = '<c:out value='${clusterEnv}'/>';
						com.impetus.ankush.commonMonitoring.clusterId = monitoringClusterId;
						com.impetus.ankush.commonMonitoring.clusterTechnology = monitoringClusterTechnology;
						com.impetus.ankush.commonMonitoring.clusterName = monitoringClusterName;
						com.impetus.ankush.commonMonitoring.clusterEnv = monitoringClusterEnv;
						var nodesUrl = baseUrl + '/monitor/'
								+ com.impetus.ankush.commonMonitoring.clusterId
								+ '/nodes';
						com.impetus.ankush
								.placeAjaxCall(nodesUrl, 'GET', true, null, function(data){
									com.impetus.ankush.commonMonitoring.nodesData = data;
								});
						/* Technology will be set Default if no technology found */
						if ((undefined == com.impetus.ankush.commonMonitoring[monitoringClusterTechnology])
								|| (monitoringClusterTechnology == null)) {
							monitoringClusterTechnology = "Default";
						}
						$("#commonMonitoringClusterNameHeading").html(
								monitoringClusterName);
						$("#commonMonitoringClusterEnv").html(
								monitoringClusterEnv.toUpperCase());
						/* loop will fill Action Drop down button on header page */
						for ( var key in com.impetus.ankush.commonMonitoring[monitoringClusterTechnology].actions){
							var idForKey = '';
							if(key.split('...').length > 1)
								idForKey = key.split('...')[0];
							else
								idForKey = key;
							$("#actionsDropDown")
									.append(
											' <li id="action_'+idForKey.split(' ').join('_')+'"><a tabindex="-1" href="#" class="text-left" id="link_'+idForKey.split(' ').join('_')+'" onclick="com.impetus.ankush.commonMonitoring[\''
													+ monitoringClusterTechnology
													+ '\'].actions[\''
													+ key
													+ '\'].methodCall()">'
													+ key + '</a></li>');
						}
						/* loop will create links on monitoring page */
						for ( var key in com.impetus.ankush.commonMonitoring[monitoringClusterTechnology].links)
							$("#commonLinks")
									.append(
											' <div class="row-fluid"><div class="span8 text-left" id="link_'+key.split(' ').join('_')+'"><h4 class="section-heading" ><a href="#" onclick="com.impetus.ankush.commonMonitoring[\''
													+ monitoringClusterTechnology
													+ '\'].links[\''
													+ key
													+ '\'].methodCall()">'
													+ key
													+ '&nbsp;&nbsp;&nbsp;<img src="'+
		                                            baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
						/* function will create common tiles on the basis of cluster id */
						
						com.impetus.ankush.commonMonitoring
								.createCommonTiles(monitoringClusterId);
						/* function will create heatmaps on the basis of cluster id */
						com.impetus.ankush.commonMonitoring
								.commonHeatMaps(monitoringClusterId);
						/*  Auto refresh for common monitoring page */
						autoRefreshMonitoringPage();
						
						$('.dropdown-toggle').dropdown();
						var Url = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/sparkline?startTime=lastHour';
						//alert(Url);
						com.impetus.ankush.placeAjaxCall(Url, "GET", true, null, function(jd){
						jd = jd.output.sparklineData;
						//$('#test').live('click', function() {
						setTimeout(function() {
							generate_sparkline(jd);
							
//								setInterval(function(){
//									update_sparkline_json();
//								}, 3000);
							
						}, 1000);
					//});
					
					
				});});
						//alert(jd.toSource());
						//console.log(jd.toSource());
						
// 						jd = '{"output":'
// 		+ '{'
// 		+ '"nodeData":'
// 		+ '['
// 		+ '{"id":11,"nodeIp":"192.168.145.54","rackId":"/rack1","status":"normal","value":"12"}'
// 		+ '],'
// 		+ '"sparklineData":'
// 		+ '['
// 		+ '{'
// 		+ '"id":1,'
// 		+ '"values":[1, 5, 20, 70, 50, 20, 10, 30, 10, 30, 80, 90, 20, 90, 50, 90, 30, 60, 30, 60, 20, 70, 50, 20, 10, 50, 90, 30, 60, 30, 60, 20, 10, 50, 90, 30, 60, 30, 60, 20, 50, 90, 30, 60, 30, 60, 20, 60, 10, 1, 5, 20, 70, 50, 20, 10, 30, 10, 30, 80, 90, 20, 90, 50, 90, 30, 60, 30, 60, 20, 70, 50, 20, 10, 50, 90, 30, 60, 30, 60, 20, 10, 50, 90, 30, 60, 30, 60, 20, 50, 90, 30, 60, 30, 60, 20, 60, 10],'
// 		+ '"normalValue":50,'
// 		+ '"maxValue":100,'
// 		+ '"label":"CPU",'
// 		+ '"unit":"%",'
// 		+ '"delay":500'
// 		+ '},'
// 		+ '{'
// 		+ '"id":2,'
// 		+ '"values":[0.5, 0.5, 40, 60, 30, 20, 10, 40, 50, 60, 90, 80, 70, 40, 70, 40, 50, 80, 90, 60, 20, 50, 30, 90, 10, 10, 20, 50, 20, 40, 50, 50, 60, 90, 80, 50, 20, 30, 20, 50, 20, 10, 20, 40, 50, 80, 70, 40, 50, 10, 100, 10, 30, 40],'
// 		+ '"normalValue":80,'
// 		+ '"maxValue":100,'
// 		+ '"label":"Memory",'
// 		+ '"unit":"%",'
// 		+ '"delay":500'
// 		+ '},'
// 		+ '{'
// 		+ '"id":3,'
// 		+ '"values":[0.5, 0.5, 40, 900, 80, 20, 30, 70, 50, 60, 50, 50, 90, 40, 60, 500, 40, 30, 10, 20, 90, 40, 80, 70, 30, 60, 20, 50, 40, 0, 1000, 0, 70, 30, 90, 10, 90, 20, 60, 30, 50, 50, 20, 40, 10, 90, 20, 70, 60],'
// 		+ '"normalValue":30,'
// 		+ '"maxValue":1000,'
// 		+ '"label":"Network",'
// 		+ '"unit":"B/s",'
// 		+ '"delay":500'
// 		+ '},'
// 		+ '{'
// 		+ '"id":4,'
// 		+ '"values":[0.5, 0.5, 90, 20, 80, 30, 80, 30, 70, 20, 70, 10, 50, 20, 60, 10, 70, 90, 80, 40, 80, 60, 80, 20, 50, 40, 90, 10, 50, 60, 60, 60, 20, 70, 50, 80, 90, 10, 20, 40, 80, 30, 90, 10, 90, 10, 90, 60, 40],'
// 		+ '"normalValue":30,' + '"label":"I/O",' + '"unit":"%",'
// 		+ '"maxValue":100,'
// 		+ '"delay":500' + '}' + '],' + '"status":true,' + '"totalRack":1,'
// 		+ '"rackInfo":' + '[' + '{"rackId":"/rack1","totalnode":1}' + ']'
// 		+ '},' + '"status":"200",' + '"description":"Cluster details.",'
// 		+ '"errors":null' + '}';
						
// 						jd = JSON.parse(jd);
						
						
</script>

<!--this is main page of monitoring  -->
 <style>
            circle { stroke: #000; fill: none; }
            .red {fill:rgb(255,0,0)}
            .blue {fill:rgb(0,0,255)}
            .green {fill:rgb(0,255,0)}
            .purple {fill:rgb(76,0,153)}
            .black {fill:rgb(0,0,0)}
            .orange {fill:rgb(255,128,0)}
        </style>   


<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="text-left span6 row-fluid">
			<h2 class="heading text-left left"
				id="commonMonitoringClusterNameHeading"></h2>
			<span id="commonMonitoringClusterEnv" class="text-left inline head_subtext"></span>
		</div>
		<div class="span6 text-right">
			<div class="btn-group">
				<a class="btn dropdown-toggle hgt25" data-toggle="dropdown" href="#">Actions <span class="caret"></span></a>
				<ul class="dropdown-menu pull-right" id="actionsDropDown">
				</ul>
			</div>
		</div>
	</div>
</div>

<div class="section-body common-tooltip">
	<div class="container-fluid"
		id="div_commonMonitoringDetails">
		<div class="row-fluid">
			<div class="masonry mrgt10 span12" id="allTilesCluster"></div>
		</div>
		<!-- <div class="row-fluid transitions-enabled" style="margin-top: 10px;"
			id="running_tilesCluster"></div> -->
		<div class="row-fluid mrgt10">
			<div id="tileViewDetail"></div>
		</div>
		<div class="row-fluid mrgb10">
			<style>
				.sparkline_tab_heading {
					color:#6F7971;
					font-size:14px;
					font-weight:bold;
				}
				.sparkline_labels {
					color:#555;
				}
			</style>
		
			<div class="span12 mrgl0">
				
				
				
				
				
				<div class="cluster_utili_div">
				<div class="thumbnail cluster_utili_block">
							<div id="sparkline_container" class="uti_trend_head">
								<table cellspacing="0" cellpadding="0" id="spark_line">
									<tr>
										<th  align="left" valign="top"  colspan="2"><div class="sparkline_tab_heading">Cluster Utilization<span class="clusterpart_line">|</span> <span><a onclick="com.impetus.ankush.commonMonitoring.utilizationTrend();" href="#">View All Metrics</a></span></div>
										
										
										
										</th>
										
										<th width="90" align="center" valign="top" colspan="2"><span class="sparkline_tab_heading">Current</span></th>
										
									</tr>
									<tbody>
									<tr>
										<td width="75">Memory</td>
										<td width="330"><div id="sparkline_1" class="sparkline_contain_div"><svg width="100%" height="100%"><rect width="100%" height="100%" y="0%" style="fill: rgb(235, 235, 235);"></rect></svg></div></td>
										<td align="right" class="sl_current_val" width="40">0</td>
										<td>%</td>
										
									</tr>
									<tr>
										<td>CPU</td>
										<td><div id="sparkline_2"  class="sparkline_contain_div"><svg width="100%" height="100%"><rect width="100%" height="100%" y="0%" style="fill: rgb(235, 235, 235);"></rect></svg></div></td>
										<td align="right" class="sl_current_val">0</td>
										<td>%</td>
										
									</tr>
									<tr>
										<td>Load</td>
										<td><div id="sparkline_3"  class="sparkline_contain_div"><svg width="100%" height="100%"><rect width="100%" height="100%" y="0%" style="fill: rgb(235, 235, 235);"></rect></svg></div></td>
										<td align="right" class="sl_current_val">0</td>
										<td>%</td>
										
									</tr>
									<tr>
										<td>Network</td>
										<td><div id="sparkline_4"  class="sparkline_contain_div"><svg width="100%" height="100%"><rect width="100%" height="100%" y="0%" style="fill: rgb(235, 235, 235);"></rect></svg></div></td>
										<td align="right" class="sl_current_val">0</td>
										<td>MB/s</td>
										
									</tr>
									</tbody>
								</table>
							</div>
					</div>
				</div>
				
				
				
				
				
				
					<div class="heatmap_tbl left">
					
					<div class="thumbnail heatmap_block">
					
						<div class="left mrgr25">
							<c:choose>
								<c:when test="${clusterTechnology eq 'Cassandra'}">
      								<div class="form-image text-left btn-group "
										id="graphButtonGroup_JobMonitoring"
										data-toggle="buttons-radio" style="margin-top:-4px;">
										<button class="btn active" id="btnLastDay_HNDD"
											onclick="com.impetus.ankush.cassandraMonitoring.showHeatmapOrRing('cassandra-heat-map','cassandra-ring-graph')">Heat
											Map</button>
										<button class="btn" id="btnLastHour_HNDD"
											onclick="com.impetus.ankush.cassandraMonitoring.showHeatmapOrRing('cassandra-ring-graph','cassandra-heat-map')">Ring
											Topology</button>
									</div>
								</c:when>

								<c:otherwise>
									<label class=" mrgt0 form-label section-heading">Heat
										Map</label>
								</c:otherwise>
							</c:choose>
						</div>
							<div class="heatmap_rgtblock" id="heatmap_rgtblock">
							<div class="left mrgr25 mrgb7">
								<div class="text-left btn-group"	data-toggle="buttons-radio">
									<button type="button" class="btn active" id="cpuHeatMap"
										onclick="commonHeatMapTypeSelect('CPU');">CPU</button>
									<button type="button" class="btn" id="memoryHeatMap"
										onclick="commonHeatMapTypeSelect('Memory');">Memory</button>
								</div>
							</div>
							<div class="legend lowhigh_block mrgr25 mrgl0">
								<div class="drow">
									<div class="alignl">Low</div>
									<div class="legand-normal"></div>
									<div class="legand-warning"></div>
									<div class="legand-critical"></div>
<!-- 								<div class="legand-alert-unavailable"></div> -->
									<div class="alignr">High</div>
								</div>
							</div>
							<div class="legend lowhigh_block mrgl0">
								<div class="drow">
									<div class="alignl">
										<b>Not Available</b>
									</div>
									<div class="legand-alert-unavailable"></div>
								</div>
							</div>
							</div>
						<div class="clear"></div>
						<div class="heat_map_line_break"></div>
												
						<div  class="text-left"  id="cassandra-heat-map">						
							<div id="heat_map" style=""></div>
						</div>
						<c:if test="${clusterTechnology eq 'Cassandra'}">	
						 <%@ include file="../cassandra/cassandraClusterMonitoring/ringTopology.jsp"%>
						</c:if>
						<div class="clear"></div>
					</div>
					</div>
					<div class="clear"></div>
				</div>
								

			
		</div>
		<!-- This div is for hadoop table common monitoring page -->
		<c:if test="${clusterTechnology eq 'Hadoop'}">
			 <%@ include file="../hadoop/monitoring_hadoop/tileTables.jsp"%>
		    <%@ include file="../hadoop/monitoring_hadoop/_hadoopMonitoring_hadoop_details.jsp"%>
			<%@ include file="../hadoop/monitoring_hadoop/_config_hadoop_ecosystem.jsp"%>
		</c:if>
		<c:if test="${clusterTechnology eq 'Hadoop2'}">
			 <%@ include file="../hadoop/monitoring_hadoop/tileTables.jsp"%>
		    <%@ include file="../hadoop/monitoring_hadoop/_hadoopMonitoring_hadoop_details.jsp"%>
			<%@ include file="../hadoop/monitoring_hadoop/_config_hadoop_ecosystem.jsp"%>
		</c:if>
		<c:if test="${clusterTechnology eq 'Storm'}">
   			<%@ include file="../storm/stormMonitoring/topologySummaryTable.jsp"%>
		</c:if>
		<c:if test="${clusterTechnology eq 'Kafka'}">
   			<%@ include file="../kafka/kafkaMonitoring/kafkaTables.jsp"%>
		</c:if>
		<c:if test="${clusterTechnology eq 'ElasticSearch'}">
		    <%@ include file="../elasticSearch/elasticSearchMonitoring/elasticSearchTable.jsp"%>
		</c:if>
		<!-- This div is for links in common monitoring page -->
		<div id="commonLinks"></div>
	</div>
</div>
<c:if test="${clusterTechnology eq 'Cassandra'}">
<div id="div_RequestSuccess_Action" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Action Request</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
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
<div id="deleteClusterDialogcommonMonitor" class="modal hide fade"
	style="display: none;">
	<div class="modal-header text-center">
		<h4>Delete Cluster</h4>
	</div>
	<div class="modal-body">
		<div class="row-fluid">
			<div class="span12" style="text-align: left; font-size: 14px;">
				The Cluster will be permanently deleted. Once deleted data cannot be
				recovered.</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
			id="confirmDeleteButtonHadoopMonitor"
			onclick="com.impetus.ankush.commonMonitoring.deleteCluster();"
			class="btn">Delete</a>
	</div>
	
</div>
<div id="invalidAddNodeOperation" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
		<h4>Invalid Operation</h4>
	</div>
	<div class="modal-body">
		<div class="row-fluid">
			<div class="span12" style="text-align: left; font-size: 14px;">
				Node addition operation is not allowed for cloud cluster.</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="#" data-dismiss="modal" class="btn">Ok</a>
	</div>
</div>
