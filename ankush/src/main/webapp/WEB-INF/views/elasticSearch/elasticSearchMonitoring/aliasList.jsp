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
var aliasElasticSearchTable = null;
function elasticSearchAliasesTablePageAutorefresh(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autorefresh_monitoringElasticSearchAliasesTable'; 
	obj1.callFunction = "com.impetus.ankush.elasticSearchMonitoring.populateAliasTable();";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function() {
	aliasElasticSearchTable=$('#aliasElasticSearchTable').dataTable({
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
	$("#aliasElasticSearchTable_filter").css({
		'display' : 'none'
	});
	$('#searchaliasElasticSearchTable').keyup(function() {
		$("#aliasElasticSearchTable").dataTable().fnFilter($(this).val());
	});
	elasticSearchAliasesTablePageAutorefresh();
});
</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span6">
			<h2 class="heading text-left left" id="indexNameElasticSearchAlias"></h2>
			<button class="btn-error header_errmsg"
				id="validateErrorAliasList"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;padding:0 15px; left:15px; position:relative"></button>
		</div>
</div>
</div>
<div class="section-body">
<div class="container-fluid">
	<div class="row-fluid">
			<div id="errorDivAliasList" class="span12 errorDiv"
				style="display: none;"></div>
			</div>
		

	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="aliasElasticSearchTable"
							width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
								<tr>
									<th>Alias</th>
									<th>Index Routing</th>
									<th>Search Routing</th>
									<th>Action</th>
								</tr>
							</thead>
						</table>
					</div>	
				
</div>
<div id="div_RequestSuccess_deleteAlias" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Delete Alias</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Do you want to delete alias ?</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn">Cancel</a>
			<a href="#" class="btn" data-dismiss="modal" id="deleteAliasButton">Confirm</a>
		</div>
	</div>	
</div>
<div id="div_RequestSuccess_deleteAliasConfirm" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Delete Alias</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Alias delete request placed successfully.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn">Ok</a>
		</div>
	</div>	
</div>
</div>
