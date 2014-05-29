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
<!-- <style>
 .table-striped tbody > tr:nth-child(2n) > td, .table-striped tbody > tr:nth-child(2n) > th {
    background-color: #F9F9F9;
}
.table-striped tbody > tr:nth-child(2n+1) > td, .table-striped tbody > tr:nth-child(2n+1) > th {
    background-color: #FFFFFF;
}
</style> -->
<script src="<c:out value='${baseUrl}' />/public/js/hadoop/hadoopMonitoring.js" type="text/javascript"></script>
<script>
var hadoopNodesSummary = null;
var hdfsMonitoringUsage = null;
var mapredMonitoringJobs = null;
$(document).ready(function(){
	var obj1 = {};
	obj1.varName = 'is_autorefresh_monitoringHadoopTileTable'; 
	obj1.callFunction = "com.impetus.ankush.hadoopMonitoring.populateTileTables('hadoopNodesSummary');";
	obj1.time = 30000;
	obj1.varName = setInterval(obj1.callFunction,obj1.time);
	var obj2 = {};
	obj2.varName = 'is_autorefresh_monitoringHadoopTileTable'; 
	obj2.callFunction = "com.impetus.ankush.hadoopMonitoring.populateTileTables('hdfsMonitoringUsage');";
	obj2.time = 30000;
	obj2.varName = setInterval(obj2.callFunction,obj2.time);
	var obj3 = {};
	obj3.varName = 'is_autorefresh_monitoringHadoopTileTable'; 
	obj3.callFunction = "com.impetus.ankush.hadoopMonitoring.populateTileTables('mapredMonitoringJobs');";
	obj3.time = 30000;
	obj3.varName = setInterval(obj3.callFunction,obj3.time);	
	autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
	autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj2);
	autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj3);
	hadoopNodesSummary=	$('#hadoopNodesSummary').dataTable({
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
	$("#hadoopNodesSummary_filter").css({
		'display' : 'none'
	});
	$('#searchhadoopNodesSummary').keyup(function() {
		$("#hadoopNodesSummary").dataTable().fnFilter($(this).val());
	});
	hdfsMonitoringUsage = $('#hdfsMonitoringUsage').dataTable({
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
	$("#hdfsMonitoringUsage_filter").css({
		'display' : 'none'
	});
	$('#searchhdfsMonitoringUsage').keyup(function() {
		$("#hdfsMonitoringUsage").dataTable().fnFilter($(this).val());
	});
	mapredMonitoringJobs = $('#mapredMonitoringJobs').dataTable({
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
	$("#mapredMonitoringJobs_filter").css({
		'display' : 'none'
	});
	$('#searchmapredMonitoringJobs').keyup(function() {
		$("#mapredMonitoringJobs").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.hadoopMonitoring.populateTileTables('hadoopNodesSummary');
	com.impetus.ankush.hadoopMonitoring.populateTileTables('hdfsMonitoringUsage');
	com.impetus.ankush.hadoopMonitoring.populateTileTables('mapredMonitoringJobs');
});
</script>
	<!-- <div class="row-fluid">
			<div id="error-broker-drilldown" class="span12 error-div-hadoop"
				style="display: none;">
		<span id="popover-content" style="color: red"></span>
	</div>
	</div> -->
		<!-- <div class="row-fluid">
			<div class="row-fluid transitions-enabled" id="tilesBrokerDrillDown"
				style="margin-top: 10px;"></div>
		</div> -->
		<div class="row-fluid" id="hadoop-tables">
			<div class="boxToExpand new-box span4" style="height:185px;">
				<div class="titleExpand">
					 Nodes Summary<span id="hadoopNodesSummary-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="hadoopNodesSummary"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div class="boxToExpand new-box span4" style="height:185px;">
				<div class="titleExpand">
					HDFS Usage<span id="hdfsMonitoringUsage-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="hdfsMonitoringUsage"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div class="boxToExpand new-box span4" style="height:185px;">
				<div class="titleExpand">
					 Jobs Summary<span id="mapredMonitoringJobs-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="mapredMonitoringJobs"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			</div>
			
			
