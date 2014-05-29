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
<%@include file="../layout/blankheader.jsp"%>
<script type="text/javascript">
function autoRefreshUtilizationTrendsPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autoRefresh_utilizationTrends'; 
	obj1.callFunction = "com.impetus.ankush.commonMonitoring.loadClusterLevelGraphs(\""+loadClusterLevelGraphsStartTime+"\",'autorefresh');";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	$(document).ready(
			function() {
				com.impetus.ankush.commonMonitoring.loadClusterLevelGraphs('lasthour');
				autoRefreshUtilizationTrendsPage();
			
	});
</script>
<!-- This page will show detailed VIew of Utilization Trend Graphs -->
<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span6">
			<h2 class="heading text-left" id= "pageHeadingUtilization"></h2>
		</div>
		<div class="span6 text-right">
			<div class="form-image text-left btn-group"
					id="graphButtonGroup_utilizationTrend" data-toggle="buttons-radio"
					style="margin-top: -2px;">
					<button class="btn" id="btnLastYear_HNDD" onclick="com.impetus.ankush.commonMonitoring.loadClusterLevelGraphs('lastyear');">1y</button>
					<button class="btn" id="btnLastMonth_HNDD" onclick="com.impetus.ankush.commonMonitoring.loadClusterLevelGraphs('lastmonth');">1m</button>
					<button class="btn" id="btnLastWeek_HNDD" onclick="com.impetus.ankush.commonMonitoring.loadClusterLevelGraphs('lastweek');">1w</button>
					<button class="btn" id="btnLastDay_HNDD" onclick="com.impetus.ankush.commonMonitoring.loadClusterLevelGraphs('lastday');">1d</button>
					<button class="btn active" id="btnLastHour_HNDD" onclick="com.impetus.ankush.commonMonitoring.loadClusterLevelGraphs('lasthour');">1h</button>
				</div>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="container-fluid">
		<div id="utilizationTrendGraphs">
		</div>
	</div>
</div>



				
