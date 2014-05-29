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
	src="<c:out value='${baseUrl}' />/public/js/ElasticSearch/elasticSearchMonitoring.js"
	type="text/javascript"></script>
<script>
var indicesListTable = null;
$(document).ready(function(){
		var obj1 = {};
		obj1.varName = 'is_autorefresh_monitoringElasticSearchTableHybrid'; 
		obj1.callFunction = "com.impetus.ankush.elasticSearchMonitoring.indicesListTable();";
		obj1.time = 30000;
		obj1.varName = setInterval(obj1.callFunction,obj1.time);
		autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
	indicesListTable  =	$('#indicesListTable').dataTable({
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
	com.impetus.ankush.elasticSearchMonitoring.indicesListTable();
});


</script>

<div id="monitoringKafkaDetails">
	<div class="row-fluid">
		<div class="span2 text-left">
			<h4 class="section-heading">Indices</h4>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id="indicesListTable"
				width="100%">
				<thead class="text-left">
					<tr>
						<th>Index</th>
						<th>Docs</th>
						<th>Primary Size</th>
						<th>No of Shards</th>
						<th>No of Replicas</th>
						<th>Open</th>
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>
