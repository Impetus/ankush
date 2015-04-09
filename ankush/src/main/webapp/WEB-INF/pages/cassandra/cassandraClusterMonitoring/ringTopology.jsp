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

<script
	src="<c:out value='${baseUrl}' />/public/js3.0	/cassandra/cassandraMonitoring.js"
	type="text/javascript"></script>
	<script
	src="<c:out value='${baseUrl}' />/public/js3.0	/cassandra/ringTopology.js"
	type="text/javascript"></script>
<script>
var clusterSummaryTable = null;
$(document).ready(function(){
	clusterSummaryTable =	$('#clusterSummaryTable').dataTable({
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
		/* var obj1 = {};
		obj1.varName = 'is_autorefresh_monitoringCassandraRingTopology'; 
		obj1.callFunction = "com.impetus.ankush.cassandraMonitoring.ringTopology();";
		obj1.time = 30000;
		obj1.varName = setInterval(obj1.callFunction,obj1.time);
		autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
		prePagePopulateCallsObject[$.data(document, "panels").children.length].push("com.impetus.ankush.cassandraMonitoring.ringTopology();"); */
		com.impetus.ankush.ringTopology.ringTopology();
})
</script>
<div id="cassandra-ring-graph" class="row">
			<div class="col-sm-6">
                         <div class="panel">
                            <div class="panel-heading">
                              <h3 class="panel-title">Ring Topology</h3>
                            </div>
                            <div class="row-fluid box infobox masonry-brick">
						<div class="text-left" id="ring_graph" style="padding:10px!important;"></div>					
					</div>
                          </div>
            </div>
			<div class="col-sm-6" id="clusterSummaryPanel">
                         <div class="panel">
                            <div class="panel-heading">
                              <h3 class="panel-title">Cluster Summary</h3>
                            </div>
                            <div class="row panel-body">
		<div class="col-md-12 text-left">
                            <table class="table" id="clusterSummaryTable">
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
	<div class="clear"></div>
</div>

<style>
#clusterSummaryPanel > .panel > .panel-heading:after {
    border-bottom: 0px !important;
    content: "";
    display: block;
    height: 0;
    padding-bottom: 0px;
}
</style>

						
						
						
