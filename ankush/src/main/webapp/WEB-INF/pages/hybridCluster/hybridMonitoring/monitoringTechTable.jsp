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

<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->
<style>
td > ins{
 background-position: -288px -118px;
}
</style>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hybrid/hybridMonitoring/hybridMonitoring.js"
	type="text/javascript"></script>
<script>
var hybridTechnologiesData = null;
$(document).ready(function(){
	hybridDetailsTable=	$('#hybridDetailsTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
	});
	$("#hybridDetailsTable_filter").css({
		'display' : 'none'
	});
	$('#searchhybridDetailsTable').keyup(function() {
		$("#hybridDetailsTable").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.hybridMonitoring.techTableCreate();
});


</script>

<div id="monitoringHybridDetails">
	<div class="panel">
		<div class="panel-heading">
			<h3 class="panel-title">Technologies</h3>
			<div class="pull-right panelSearch">
				<input id="searchhybridDetailsTable" class="search-datatable" type="text"
					placeholder="Search">
			</div>
		</div>
		<div class="row panel-body">
			<div class="col-md-12 text-left">
				<table class="table" id="hybridDetailsTable">
					<thead style="text-align: left;">
						<tr>
							<th style="width: 5px;"></th>
							<th>Technology</th>
							<th>Nodes</th>
							<th>Vendor</th>
							<th>Version</th>
							<th></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

	</div>
</div>
	

