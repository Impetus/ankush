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

<html>
<head>
<%@ include file="../../layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all"/>
<%@ include file="../../layout/navigation.jsp"%>
<style>
td.expanded-group > ins{
 background-position: -288px -118px;
}
td.collapsed-group > ins{
  background-position: -314px -118px;
}
.deleteParameter > ins{
  background-position: -25px -93px;
}
</style>

</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
  
		
		<h1 id="columnFamilyName" class="left"></h1>
	<div class="btn-group pull-right element-hide" id="columnfamilyActionsDropDownSpan">
	  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	    Actions&nbsp;<span class="caret"></span>
	  </button>
	  <ul class="dropdown-menu" role="menu" id="columnfamilyActionsDropDown">
	    
	  </ul>
	</div>
</div>
<div class="page-body" id="main-content">
<%@ include file="../../layout/breadcrumbs.jsp"%>
	<div class="container-fluid">
<div class="row">
			<div id="error-div-ColumnfamilyDrillDown" class="col-md-12 error-div-hadoop" style="display: none;">
				<span id="popover-content-cassandraParameters" style="color: red;"></span>
			</div>
		</div>
	<div class="modal fade" id="confirmationDialogsColumnfamily" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4>Confirmation</h4>
					</div>
						<div class="modal-body">
							<div class="row">
								<div class="col-md-12" style="text-align: left; font-size: 14px;" id="Columnfamily-message">MASSAGE.</div>
							</div>

						</div>
						<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
						<a href="#" data-dismiss="modal" class="btn btn-default" id="confirmationDialogsOKColumnfamily">Ok</a>
					</div>
				</div>
			</div>
		</div>
				<div class="row">
					<div class="col-md-12 text-left">
						<div class="panel">
							<div class="panel-heading">
								<h3 class="panel-title">Properties</h3>
								<div class="pull-right panelSearch">
									<input id="searchColumnFamiliesDrillDownTable" class="form-control"
										type="text" placeholder="Search">
								</div>
							</div>
							<div class="row panel-body">
								<div class="col-md-12 text-left">
									<table class="table" id="columnFamiliesDrillDownTable">
										<thead style="text-align: left;">
											<tr>
												<th>Category</th>
												<th>Category</th>
												<th>Parameter</th>
												<th>Value</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>

					</div>
				</div>
				<div class="row">
		<div class="col-md-6 text-left">
			<h4 class="section-heading" style="text-align: left;">Column Metadata</h4>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12 text-left">
			<pre id="columnMetaData"></pre>
		</div>
	</div>
	</div>	
</div>
</div>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/cassandra/columnFamilyDrilldown.js"
	type="text/javascript"></script>
	<script
	src="<c:out value='${baseUrl}' />/public/js3.0/cassandra/commonAction.js"
	type="text/javascript"></script>
<script>
var columnMetaData = null;
var columnFamiliesDrillDownTable = null;
var keyspaceName = null;
var columnFamilyName = null;
$(document).ready(function(){
	keyspaceName = '<c:out value ='${keyspaceName}'/>';
	columnFamilyName = '<c:out value ='${columnFamilyName}'/>';
	$("#columnFamilyName").text(columnFamilyName);
	columnFamiliesDrillDownTable=	$('#columnFamiliesDrillDownTable').dataTable({
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
	}).rowGrouping({
		bExpandableGrouping : true,
        iGroupingColumnIndex : 0});
	$("#columnFamiliesDrillDownTable_filter").css({
		'display' : 'none'
	});
	$('#searchColumnFamiliesDrillDownTable').keyup(function() {
		$("#columnFamiliesDrillDownTable").dataTable().fnFilter($(this).val());
	});
	if((keyspaceName != 'system') && (keyspaceName != 'system_traces')){
		$("#columnfamilyActionsDropDownSpan").show();
		var columnFamilyActionUrl = null;
		if(clusterTechnology === 'Hybrid'){
			columnFamilyActionUrl = baseUrl+'/monitor/'+clusterId+'/columnfamilyactionlist?component=Cassandra';
		}else{
			columnFamilyActionUrl = baseUrl+'/monitor/'+clusterId+'/columnfamilyactionlist';
		}
		$.ajax({
			'type' : 'GET',
			'url' : columnFamilyActionUrl,
			'contentType' : 'application/json',
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				if(result.output.status == true){
					for(var i = 0 ; i < result.output.actions.length ; i++){
						$("#columnfamilyActionsDropDown").append(' <li><a tabindex="-1" href="#" class="text-left" onclick="com.impetus.ankush.commonAction.postNodeActionServiceDialog(\''+result.output.actions[i]+'\',\''+keyspaceName+'\',\''+columnFamilyName+'\',\'Columnfamily\')">'+result.output.actions[i]+'...</a></li>');
					}
				}else{
					
				}
			},
			'error' : function(){
			}
		});
	}else
		$("#columnfamilyActionsDropDownSpan").hide();
	com.impetus.ankush.columnFamilyDrilldown.columnFamilyDrillDownPopulate(keyspaceName,columnFamilyName);
});
</script>
<div class="modal-backdrop loadingPage" id="showLoading"
		style=""></div>
</body>
</html>
