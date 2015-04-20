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
<%@ include file="../../layout/navigation.jsp"%>

</head>

<body>
<div class="page-wrapper">
		<div class="page-header heading">	
   <h1 id="keyspaceName" class="left"></h1>
	<div class="btn-group pull-right element-hide" id="keyspaceActionsDropDownSpan">
	  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	    Actions&nbsp;<span class="caret"></span>
	  </button>
	  <ul class="dropdown-menu" role="menu" id="keyspaceActionsDropDown">
	    
	  </ul>
	</div>
		
</div>
<div class="page-body" id="main-content">
<%@ include file="../../layout/breadcrumbs.jsp"%>
	<div class="container-fluid">
<div class="row">
			<div id="error-div-KeyspaceDrillDown" class="col-md-12 error-div-hadoop" style="display: none;">
				<span id="popover-content-cassandraParameters" style="color: red;"></span>
			</div>
		</div>
<div class="row">
			<div class="masonry col-md-12" id="tilesColumnFamilies"></div>
		</div>
	<div class="modal fade" id="confirmationDialogsKeyspace" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4>Confirmation</h4>
					</div>
						<div class="modal-body">
							<div class="row">
								<div class="col-md-12" style="text-align: left; font-size: 14px;" id="Keyspace-message">MASSAGE.</div>
							</div>

						</div>
						<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
						<a href="#" data-dismiss="modal" class="btn btn-default" id="confirmationDialogsOKKeyspace">Ok</a>
					</div>
				</div>
			</div>
		</div>
	<div class="row">
		<div class="col-md-12 text-left">
		<div class="panel">
						<div class="panel-heading">
							<h3 class="panel-title">Column Families</h3>
							<div class="pull-right panelSearch">
				<input id="searchColumnFamiliesTables" class="form-control" type="text"
					placeholder="Search">
			</div>
						</div>
							<div class="row panel-body">
								<div class="col-md-12 text-left">
									<table class="table" id="columnFamiliesTables">
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
	</div>
	</div>
</div>
</div>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/cassandra/keyspaceDrilldown.js"
	type="text/javascript"></script>
	<script
	src="<c:out value='${baseUrl}' />/public/js3.0/cassandra/commonAction.js"
	type="text/javascript"></script>
<script>
var columnFamiliesTables = null;
var keyspaceName = null;
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
	keyspaceName = '<c:out value ='${keyspaceName}'/>';
	$("#keyspaceName").text(keyspaceName);
	if((keyspaceName !== 'system') && (keyspaceName !== 'system_traces')){
		$("#keyspaceActionsDropDownSpan").show();
		var keyspaceActionUrl = baseUrl+'/monitor/'+clusterId+'/keyspaceactionlist?component=Cassandra';;
		$.ajax({
			'type' : 'GET',
			'url' : keyspaceActionUrl,
			'contentType' : 'application/json',
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				if(result.output.status == true){
					for(var i = 0 ; i < result.output.actions.length ; i++){
						$("#keyspaceActionsDropDown").append(' <li><a tabindex="-1" href="#" class="text-left" onclick="com.impetus.ankush.commonAction.postNodeActionServiceDialog(\''+result.output.actions[i]+'\',\''+keyspaceName+'\',\'\',\'Keyspace\')">'+result.output.actions[i]+'...</a></li>');
					}
				}else{
					
				}
			},
			'error' : function(){
			}
		});
	}else
		$("#keyspaceActionsDropDownSpan").hide();
		com.impetus.ankush.keyspaceDrilldown.keyspaceDrillDownPopulate(keyspaceName);
});


</script>
<div class="modal-backdrop loadingPage element-hide" id="showLoading"
		style=""></div>
</body>
</html>
