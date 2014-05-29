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
	src="<c:out value='${baseUrl}' />/public/js/cassandra/cassandraMonitoring.js"
	type="text/javascript"></script>
<script>
var nodeOverviewTable = null;
$(document).ready(function(){
	/* var obj1 = {};
	obj1.varName = 'is_autorefresh_monitoringKafkaBrokerTableHybrid'; 
	obj1.callFunction = "com.impetus.ankush.kafkaMonitoring.brokerListTable();";
	obj1.time = 30000;
	obj1.varName = setInterval(obj1.callFunction,obj1.time);
	autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1); */
	nodeOverviewTable=	$('#nodeOverviewTable').dataTable({
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
	com.impetus.ankush.cassandraMonitoring.nodeOverview(com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh);
});


</script>


	<div class="row-fluid">
		<div class="span2 text-left">
			<h4 class="section-heading">Node Overview</h4>
		</div>
	</div>
	<div id="confirmationDialogsNode" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Confirmation</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;" id="Node-message">
						MASSAGE</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					id="confirmationDialogsOKNode"
					class="btn">OK</a>
			</div>
		</div>
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id="nodeOverviewTable"
				width="100%">
				<thead class="text-left">
					<tr>
						<th>Key</th>
						<th>Value</th>
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
