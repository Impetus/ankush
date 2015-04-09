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
<%-- <script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript"></script>
<script>
function autoRefreshKeyspacesPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autoRefresh_Keyspaces'; 
	obj1.callFunction = "com.impetus.ankush.cassandraMonitoring.keyspacePopulate();";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
var keyspaceTable = null;
$(document).ready(function(){
	keyspaceTable=	$('#keyspaceTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : true,
		"bSortClasses": false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
	    }
	});
	$("#keyspaceTable_filter").css({
		'display' : 'none'
	});
	$('#searchKeyspaceTable').keyup(function() {
		$("#keyspaceTable").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.cassandraMonitoring.keyspacePopulate();
	autoRefreshKeyspacesPage();
	var func1 = "com.impetus.ankush.cassandraMonitoring.keyspacePopulate()";
	var funcArray = [func1];
	com.impetus.ankush.addPreviousCallbackCall(funcArray);
});


</script>
<div class="section-header" on>
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left">Keyspaces</h2>
		</div>
</div>
</div>
<div class="section-body common-tooltip">
<div class="container-fluid">
	<div class="row-fluid">
		<div class="masonry mrgt10" id="tilesKeyspaces"></div>
	</div>
<div class="row-fluid">
		<div class="span6 text-left">
			<h4 class="section-heading" style="text-align: left;">Keyspaces</h4>
		</div>
		<div class="text-right span6">
					<input id="searchKeyspaceTable" type="text"
						placeholder="Search">
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="keyspaceTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Keyspace</th>
						<th>Strategy Options</th>
						<th>Strategy Class</th>
						<th>Column Families</th>
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	</div>
</div> --%>

<html>
<head>
<%@ include file="../../layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all"/>
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/cassandra/keyspaces.js"
	type="text/javascript"></script>

<script>
var keyspaceTable = null;
$(document).ready(function(){
	keyspaceTable=	$('#keyspaceTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : true,
		"bSortClasses": false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
	    }
	});
	$("#keyspaceTable_filter").css({
		'display' : 'none'
	});
	$('#searchKeyspaceTable').keyup(function() {
		$("#keyspaceTable").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.keyspaces.keyspacePopulate();
});
</script>

</head>

<body>
<div class="page-wrapper">
		<div class="page-header heading">
			<div class="col-md-6 text-left">
				<h1>Keyspaces</h1>
			</div>
		</div>
		<div class="page-body" id="main-content">
<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
				<div class="row">
					<div class="masonry" id="tilesKeyspaces"></div>
				</div>
				<div class="row">
					<div class="col-md-12 text-left">
						<div class="panel">
							<div class="panel-heading">
								<h3 class="panel-title">Keyspace</h3>
								<div class="pull-right panelSearch">
									<input id="searchKeyspaceTable" class="form-control" type="text"
										placeholder="Search">
								</div>
							</div>
							<div class="row panel-body">
		<div class="col-md-12 text-left">
							<table class="table" id="keyspaceTable">
								<thead style="text-align: left;">
									<tr>
										<th>Keyspace</th>
										<th>Strategy Options</th>
										<th>Strategy Class</th>
										<th>Column Families</th>
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
	<div class="modal-backdrop loadingPage element-hide" id="showLoading"
		style=""></div>
</body>
</html>
