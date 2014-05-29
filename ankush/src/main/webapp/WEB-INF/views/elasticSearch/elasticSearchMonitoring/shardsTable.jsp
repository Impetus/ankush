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
<style>
 .table-striped tbody > tr:nth-child(2n) > td, .table-striped tbody > tr:nth-child(2n) > th {
    background-color: #FFFFFF;
}
.table-striped tbody > tr:nth-child(2n+1) > td, .table-striped tbody > tr:nth-child(2n+1) > th {
    background-color: #F9F9F9;
}
</style>
<script>
var shardsElasticSearchTable = null;
function elasticSearchShardsTablePageAutorefresh(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autorefresh_monitoringElasticSearchShardsTable'; 
	obj1.callFunction = "com.impetus.ankush.elasticSearchMonitoring.populateShardsTable();";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function() {
	shardsElasticSearchTable=$('#shardsElasticSearchTable').dataTable({
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
	$("#shardsElasticSearchTable_filter").css({
		'display' : 'none'
	});
	$('#searchshardsElasticSearchTable').keyup(function() {
		$("#shardsElasticSearchTable").dataTable().fnFilter($(this).val());
	});
	elasticSearchShardsTablePageAutorefresh();
});
</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span6">
			<h2 class="heading text-left" id="indexNameElasticSearchShards"></h2>
		</div>
</div>
</div>
<div class="section-body">
<div class="container-fluid">
<div class="row-fluid">
			<div id="error-div-KeyspaceDrillDown" class="span12 error-div-hadoop" style="display: none;">
				<span id="popover-content-cassandraParameters" style="color: red;"></span>
			</div>
		</div>

			<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="shardsElasticSearchTable"
							width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
								<tr>
									<th>State</th>
									<th>Node</th>
									<th>Primary</th>
									<th>Docs</th>
									<th>Shard</th>
									<th>Size</th>
								</tr>
							</thead>
						</table>
					</div>	
</div>
</div>
</div>
