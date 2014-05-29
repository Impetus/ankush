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
	src="<c:out value='${baseUrl}' />/public/js/kafka/kafkaMonitoring.js"
	type="text/javascript"></script>
<script>
var brokerListTable = null;
var topicListTable = null;
function kafkatablesPageAutorefresh(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autorefresh_monitoringKafkaBrokerTableHybrid'; 
	obj1.callFunction = "com.impetus.ankush.kafkaMonitoring.brokerListTable();";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function(){
	if(com.impetus.ankush.commonMonitoring.clusterTechnology != 'Hybrid'){
		var obj1 = {};
		obj1.varName = 'is_autorefresh_monitoringKafkaBrokerTableHybrid'; 
		obj1.callFunction = "com.impetus.ankush.kafkaMonitoring.brokerListTable();";
		obj1.time = 30000;
		obj1.varName = setInterval(obj1.callFunction,obj1.time);
		autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
	}
	else{
		kafkatablesPageAutorefresh();
	}
	brokerListTable  =	$('#brokerListTable').dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : false,
		"bSort" : true,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		"aaSorting": [[0,'asc']],
		"aoColumnDefs" : [ {
			'sType' : "ip-address",
			'aTargets' : [ 1 ]
		}],
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
	    }
	});
	topicListTable=	$('#topicListTable').dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : false,
		"bSort" : true,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		"aoColumnDefs" : [ 
		{
			'bSortable' : false,
			'aTargets' : [ 3 ]
		} ],
		"aaSorting": [[0,'asc']],
		"oLanguage": {
			"sEmptyTable": 'Loading...',
	    }
	});
	com.impetus.ankush.kafkaMonitoring.brokerListTable();
});


</script>

<div id="monitoringKafkaDetails">
	<div class="row-fluid">
		<div class="span2 text-left">
			<h4 class="section-heading">Broker List</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id="brokerListTable"
				width="100%">
				<thead class="text-left">
					<tr>
						<th>Broker ID</th>
						<th>Broker IP</th>
						<th>Leader Count</th>
						<th>Followers Count</th>
						<!-- <th></th> -->
						<!-- <th>Topic Count</th> -->
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span2 text-left">
			<h4 class="section-heading">Topic List</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id="topicListTable"
				width="100%">
				<thead class="text-left">
					<tr>
						<th>Topic</th>
						<th>Partition Count</th>
						<th>Replicas</th>
						<th></th>
						<!-- <th>Replication Factor</th>
						<th>Status</th>
						<th>Size</th> -->
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>
