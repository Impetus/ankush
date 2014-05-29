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
<%@ include file="../../layout/blankheader.jsp"%>
<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->
<script>
function autoRefreshKeyspaceDrillDownPage(index){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autoRefresh_KeyspaceDrillDown'; 
	obj1.callFunction = "com.impetus.ankush.cassandraMonitoring.removePageColumnfamilyDrillDown("+index+");";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
var columnFamiliesTables = null;
$(document).ready(function(){
	columnFamiliesTables=	$('#columnFamiliesTables').dataTable({
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
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
	    }
	});
	$("#columnFamiliesTables_filter").css({
		'display' : 'none'
	});
	$('#searchColumnFamiliesTables').keyup(function() {
		$("#columnFamiliesTables").dataTable().fnFilter($(this).val());
	});
	var keyspace = '<c:out value ='${keyspace}'/>';
	if((keyspace != 'system') && (keyspace != 'system_traces')){
		var keyspaceActionUrl = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/keyspaceactionlist';
		$.ajax({
			'type' : 'GET',
			'url' : keyspaceActionUrl,
			'contentType' : 'application/json',
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				if(result.output.status == true){
					for(var i = 0 ; i < result.output.actions.length ; i++){
						$("#keyspaceActionsDropDown").append(' <li><a tabindex="-1" href="#" class="text-left" onclick="com.impetus.ankush.cassandraMonitoring.postNodeActionServiceDialog(\''+result.output.actions[i]+'\',\''+keyspace+'\',\'\',\'Keyspace\')">'+result.output.actions[i]+'...</a></li>');
					}
				}else{
					
				}
			},
			'error' : function(){
			}
		});
	}else
		$("#keyspaceActionsDropDownSpan").hide();
	var index = '<c:out value ='${index}'/>'
	autoRefreshKeyspaceDrillDownPage(index);
});


</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span6">
			<h2 class="heading text-left" id="keyspaceName"></h2>
		</div>
		
	   		<div class="span6 text-right" id="keyspaceActionsDropDownSpan">
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#"
						style="height: 25px">Actions <span class="caret"></span></a>
					<ul class="dropdown-menu pull-right" id="keyspaceActionsDropDown">
					</ul>
				</div>
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
			<div class="masonry mrgt10 span12" id="tilesColumnFamilies"></div>
		</div>

<div class="row-fluid">
		<div class="span6 text-left">
			<h4 class="section-heading" style="text-align: left;">Column Families</h4>
		</div>
		<div class="text-right span6">
					<input id="searchColumnFamiliesTables" type="text"
						placeholder="Search">
		</div>
	</div>
	
	<div id="confirmationDialogsKeyspace" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Confirmation</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;" id="Keyspace-message">
						MASSAGE.</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					id="confirmationDialogsOKKeyspace"
					class="btn">OK</a>
			</div>
		</div>
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="columnFamiliesTables"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Column Family</th>
						<th>SSTable Count</th>
						<th>Read Latency</th>
						<th>Write Latency</th>
						<th>Pending Tasks</th>
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	</div>
</div>
