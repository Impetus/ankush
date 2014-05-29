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
<script>
var topologyBoltsTables = null;
var topologySpoutsTables = null;
$(document).ready(function(){
	com.impetus.ankush.stormMonitoring.removeStormMonitoringPageAutorefresh();
	topologyBoltsTables=	$('#topologyBoltsTables').dataTable({
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
	topologySpoutsTables=	$('#topologySpoutsTables').dataTable({
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
	//com.impetus.ankush.stormMonitoring.boltTable();
	//com.impetus.ankush.stormMonitoring.spoutsTable();
});


</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left" id="topologyDrillDown"></h2>
		</div>
		<div class="span6 offset2 text-right">
			<div class="form-image text-left btn-group"
					id="graphButtonGroup_utilizationTrend" data-toggle="buttons-radio"
					style="margin-top: -2px;">
					<button class="btn actionBtn" id="btnLastYear_HNDD" onclick="com.impetus.ankush.stormMonitoring.actionTopology('activate');">Activate</button>
					<button class="btn actionBtn" id="btnLastMonth_HNDD" onclick="com.impetus.ankush.stormMonitoring.actionTopology('deactivate');">Deactivate</button>
					<button class="btn actionBtn" id="btnLastWeek_HNDD" onclick="com.impetus.ankush.stormMonitoring.actionTopology('rebalance');">Rebalance</button>
					<button class="btn actionBtn" id="btnLastDay_HNDD" onclick="com.impetus.ankush.stormMonitoring.actionTopology('kill');">Kill</button>
				</div>
		</div>
</div>
</div>
<div class="section-body">

<div class="container-fluid">
<div class="row-fluid">
		<div class="span2 text-left">
			<h4 class="section-heading" style="text-align: left;">Spouts (All time)</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="topologySpoutsTables"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Id</th>
						<th>Executors</th>
						<th>Tasks</th>
						<th>Parallelism</th>
						<th>Complete Latency (ms)</th>
						<th>Emitted</th>
						<th>Transferred</th>
						<th>Acked</th>
						<th>Failed</th>
						<th>Last Error</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 text-left">
			<h4 class="section-heading" style="text-align: left;">Bolts (All time)</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="topologyBoltsTables"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Bolts</th>
						<th>Executors</th>
						<th>Tasks</th>
						<th>Parallelism</th>
						<th>Execute Latency (ms)</th>
						<th>Process Latency (ms)</th>
						<th>Emitted</th>
						<th>Transferred</th>
						<th>Executed</th>
						<th>Acked</th>
						<th>Failed</th>
						<th>Last Error</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	</div>
</div>
