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
    src="<c:out value='${baseUrl}' />/public/js3.0/commonMonitoring/nodeUtilizationTrend.js"
    type="text/javascript"></script>


<style>
.demo{
    clear: both;
    float: none;
    margin: 10px auto;
    width: auto;
    height : 300px;
}
.jstree.jstree-proton li {
    font-size: 12px;
    line-height: 24px;
}
.jstree ins{
	width : 16px !important;
}
.jstree.jstree-proton a > ins{
	width : 17px !important;
}
#graphsPanel.panel > .panel-heading:after {
    border-bottom: 0px !important;
    content: "";
    display: block;
    height: 0;
    padding-bottom: 0px;
}

#graphsPanel.panel > .panel-heading{
	padding: 4px 9px;
}
#graphsPanel.panel .panel-body{
	padding: 2px;
	color : black!important;
}
</style>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1 id="pageHeadingNodeUtilization" class="left"></h1>
   <div class="form-image text-left btn-group pull-right"
                    id="graphButtonGroup_utilizationTrend" data-toggle="buttons-radio"
                    style="margin-top: -2px;">
                    <button class="btn btn-default" id="btnLastYear_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastyear');">1y</button>
                    <button class="btn btn-default" id="btnLastMonth_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastmonth');">1m</button>
                    <button class="btn btn-default" id="btnLastWeek_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastweek');">1w</button>
                    <button class="btn btn-default" id="btnLastDay_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastday');">1d</button>
                    <button class="btn btn-default active" id="btnLastHour_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lasthour');">1h</button>
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
	<div class="container-fluid">
	
		<div id="error-div"style="display:none;">
			<div id="popover-content" class="error-div-hadoop mrgb10"></div>
	
		</div>
        		<div class="panel panel-default" id="graphsPanel">
					<div class="panel-heading">
						<div class="btn-group" id="buttonGroup_tree" data-toggle="buttons-radio">
							<button class="btn btn-default" onclick="com.impetus.ankush.jstreeExpandAll('jstreeNodeGraph');">Expand All</button>
							<button class="btn btn-default active" onclick="com.impetus.ankush.jstreeCollapseAll('jstreeNodeGraph');">Collapse All</button>
						</div>
					</div>
					<div class="panel-body">
						<div id="jstreeNodeGraph" class="left" style="width:18%;display:none;"><ul></ul></div>
            			<div id="nodeUtilizationTrendGraphsDivs" class="left" style="border-left: 1px solid #E6E6E6;width:80%;"></div>
					</div>
				</div>
			</div>
</div>
</div>
<script type="text/javascript">
	var nodeLevelGraphsStartTime = 'lasthour';
	var hostName = null;
	$(document)
			.ready(
					function() {
						hostName = '<c:out value='${hostName}'/>';
						$(window)
								.scroll(
										$.debounce(
														500,
														false,
														com.impetus.ankush.nodeUtilizationTrend.graphsViewport));
						$("#pageHeadingNodeUtilization").text(
								hostName + '/Utilization Metrics');
						$("#jstreeNodeGraph").on(
								'open_node.jstree',
								function() {
									com.impetus.ankush.nodeUtilizationTrend
											.changeBorder();
								});
						$("#jstreeNodeGraph").on(
								'close_node.jstree',
								function() {
									com.impetus.ankush.nodeUtilizationTrend
											.changeBorder();
								});
						$("#jstreeNodeGraph")
								.bind(
										"change_state.jstree",
										function(e, d) {
											$("#jstreeNodeGraph").unbind(
													"loaded.jstree");
											if ((d.args[0].tagName == "A" || d.args[0].tagName == "INS")
													&& (d.inst.data.core.refreshing != true && d.inst.data.core.refreshing != "undefined"))
												var divId = d.rslt.attr("id");
											var checked = $("#" + divId
													+ ".jstree-checked").length != 0;
											if (checked == false) {
												com.impetus.ankush.nodeUtilizationTrend
														.removeGraphUsingId(divId);
												return;
											}
											var pattern = divId.replace(
													/[\_]+/g, '.');
											com.impetus.ankush.nodeUtilizationTrend
													.getGraphUsingAjax(
															clusterId,
															hostName,
															nodeLevelGraphsStartTime,
															pattern
																	+ '(\\.|_).*');
										});
						$("#jstreeNodeGraph").bind(
								"loaded.jstree",
								function(e, d) {
									com.impetus.ankush.nodeUtilizationTrend
											.loadAllSavedGraphs();
								});
						com.impetus.ankush.nodeUtilizationTrend.jstreeCall();
						var graphsautorefresh = {};
						graphsautorefresh.method = function(){
							com.impetus.ankush.nodeUtilizationTrend.graphsViewport(true);
						};
						pageLevelAutorefreshArray.push(graphsautorefresh);
						com.impetus.ankush.createAutorefresh(pageLevelAutorefreshArray);
					});
</script>
</body>
</html>

