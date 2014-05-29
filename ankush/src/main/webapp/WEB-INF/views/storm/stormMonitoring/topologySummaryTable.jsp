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
<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->

<script
	src="<c:out value='${baseUrl}' />/public/js/storm/stormMonitoring.js"
	type="text/javascript"></script>
<script>
var topologySummaryTable = null;
var supervisorSummaryTable = null;
function topologySummaryTablePage(){
	var obj1 = {};
	var obj2 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autorefresh_monitoringStormTableHybrid'; 
	obj1.callFunction = "com.impetus.ankush.stormMonitoring.createTopologySummaryTable();";
	obj1.time = 30000;
	obj2.varName = 'is_autorefresh_monitoringStormSupervisorTableHybrid'; 
	obj2.callFunction = "com.impetus.ankush.stormMonitoring.supervisorsTable();";
	obj2.time = 30000;
	autoRefreshArray.push(obj1);
	autoRefreshArray.push(obj2);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function(){
	if(com.impetus.ankush.commonMonitoring.clusterTechnology != 'Hybrid'){
		var obj1 = {};
		obj1.varName = 'is_autorefresh_monitoringStormTableHybrid'; 
		obj1.callFunction = "com.impetus.ankush.stormMonitoring.createTopologySummaryTable();";
		obj1.time = 30000;
		obj1.varName = setInterval(obj1.callFunction,obj1.time);
		var obj2 = {};
		obj2.varName = 'is_autorefresh_monitoringStormSupervisorTableHybrid'; 
		obj2.callFunction = "com.impetus.ankush.stormMonitoring.supervisorsTable();";
		obj2.time = 30000;
		obj2.varName = setInterval(obj2.callFunction,obj2.time);
		autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
		autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj2);
	}
	else{
		topologySummaryTablePage();
	}
	topologySummaryTable = $('#topologySummaryTable').dataTable({
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
	});
	supervisorSummaryTable=	$('#supervisorSummaryTable').dataTable({
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
	});
	com.impetus.ankush.stormMonitoring.createTopologySummaryTable();
	com.impetus.ankush.stormMonitoring.supervisorsTable();
});


</script>


	<div class="row-fluid">
		<div class="text-left">
			<h4 class="section-heading text-left">Topology Summary</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id="topologySummaryTable">
				<thead class="text-left">
					<tr>
					    <th>Id</th>
						<th>Name</th>
						<th>Status</th>
						<th>Uptime (s)</th>
						<th>Workers</th>
						<th>Executors</th>
						<th>Tasks</th>
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="text-left">
			<h4 class="section-heading text-left">Supervisor Summary</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id="supervisorSummaryTable">
				<thead class="text-left">
					<tr>
						<th>Host</th>
							<th>Uptime (s)</th>
						<th>Slots</th>
						<th>Used Slots</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
