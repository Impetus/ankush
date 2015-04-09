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
<%@ include file="../layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all"/>
<%@ include file="../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.dateFormat-1.0.js"
	type="text/javascript"></script>
<script type="text/javascript">
var cassandraParameterTable = null;
var jspPage = null;
var hybridTechnology = null;
$(document).ready(function(){
	//row grouping datatable for cassandra parameters
	jspPage = '<c:out value='${page}'/>';
	hybridTechnology = '<c:out value='${hybridTechnology}'/>';
	cassandraParameterTable=$('#cassandraParameterTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : true,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		 "aoColumnDefs": [
		                    { "bSortable": false, "aTargets": [0, 1,3,4] }
		                ],
		"aaSorting": [[2,'asc']],
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
		},
		 "fnCreatedRow": function( nRow, aData, iDataIndex ) {
	        var tableRowData = $(nRow).children("td");
	        var tableHeaderRows = $("#cassandraParameterTable").find("th");
	 		for(var i = 0; i < tableRowData.length; i++)
	        {
	        	$(tableRowData[i]).css("word-wrap", "	-word");
	            $(tableRowData[i]).css("max-width", ($(tableHeaderRows[i]).css("width").replace("px", "") - 10) + "px");
	        }
	    },
	}).rowGrouping({
		bExpandableGrouping : true,
        iGroupingColumnIndex : 0});
	$("#cassandraParameterTable_filter").css({
		'display' : 'none'
	});
	$('#searchcassandraParameterTable').keyup(function() {
		$("#cassandraParameterTable").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.configurationParam.confParameterPopulate();
});
</script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">
	<h1 class="left">Configuration Parameters</h1>	
	<div class="pull-right">
   		<button class="btn btn-default mrgr10"
				onclick="com.impetus.ankush.configurationParam.confParameterPopulate();">Revert</button>
			<button class="btn btn-default" id="applyParameters" data-loading-text="Applying..."
				onclick="com.impetus.ankush.configurationParam.applyChangesPostParamObj();">Apply</button>
		
		</div>
		<ul class="notifications">
				<li
					class="dropdown dropdown-extra dropdown-notifications pull-right"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#"
					aria-expanded="false"> <i class="fa fa-lg fa-fw fa-bell"></i> <span
						class="badge-op badge-op-alert" id="notificationAlertsCount"></span>
				</a>
					<ul class="dropdown-menu">
					<li><p id="alertNotifications">You have <span id="alertsPara"></span> alert(s)</p></li>
						<li>
							<ul class="dropdown-menu-list dropdown-scroller"
								style="overflow: hidden; width: 233px; height:auto;margin-top:-12px;"
								id="notificationAlertsShow">
							</ul>
						</li>
						<li><a href="<c:out value='${baseUrl}' />/commonMonitoring/${clusterName}/events/C-D/${clusterId}/${clusterTechnology}">See all alerts<span class="fa fa-fw fa-arrow-circle-o-right pull-right mrg" style="margin-top:3;font-size:17"></span></a></li>
					</ul></li>
				<li
					class="dropdown dropdown-extra dropdown-notifications pull-right"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#"
					aria-expanded="false"> <i class="fa fa-lg fa-fw fa-clock-o"></i>
						<span class="badge-op badge-op-operation" id="notificationOperationCount"></span>
				</a>
					<ul class="dropdown-menu">
					<li><p id="operationNotifications">You have <span id="operationsPara"></span> operation(s) in progress</p></li>
						<li>
							<ul class="dropdown-menu-list dropdown-scroller"
								style="overflow: hidden; width: 250px; height: auto;margin-top:-10px;"
								id="notificationOperationShow">
							</ul>
						</li>
						<li><a href="<c:out value='${baseUrl}' />/commonMonitoring/${clusterName}/operations/C-D/${clusterId}/${clusterTechnology}">See all operations<span class="fa fa-lg fa-arrow-circle-o-right pull-right mrg" style="margin-top:4;font-size:17"></span></a></li>
					</ul></li>
			</ul>
</div>
		<div class="page-body" id="main-content">
		<%@ include file="../layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
			<div class="row">
			<div id="error-div" class="col-md-12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content"></span>
			</div>
		</div>
				<div>
					<div class="panel">
						<div class="panel-heading buttonTextOnPanel mrgt5">
							 <a href="#" id="addParameters" class="btn btn-default btn-sm" onclick="com.impetus.ankush.configurationParam.addParamDialog();">Add Parameter</a>
							<div class="pull-right ">
								<input id="searchcassandraParameterTable" class="search-dataTable" type="text"
									placeholder="Search">
							</div>
						</div>
					<div class="row panel-body">
						<div class="col-md-12 text-left">
							<table class="table" id="cassandraParameterTable">
								<thead style="text-align: left;">
									<tr>
										<th style="width: 20%">File</th>
										<th style="width: 18%">File</th>
										<th style="width: 30%">Parameter</th>
										<th style="width: 30%">Value</th>
										<th style="width: 4%"></th>
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
	<div id="deleteParameterDialogCassandra" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
			<div class="modal-header text-center">
				<h4>Parameter Delete</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;">
						Parameter has been marked on delete click. Changes will be applicable
						on Apply.</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a> <a href="#"
					id="confirmDeleteParameterCassandra" class="btn btn-default">Delete</a>
			</div>
			</div>
			</div>
		</div>
	<div class="modal fade" id="div_addParameter" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">Add Parameter</h4>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-4 text-right">
								<label class="form-label">File name:</label>
							</div>
							<div class="col-md-6 text-left" style="margin-left: 13px">
								<select id="fileNameValueAddParam" data-placement="right" class="form-control"></select>
							</div>
						</div>
						<div class="row mrgt10">
							<div class="col-md-4 text-right">
								<label class="form-label">Parameter name:</label>
							</div>
							<div class="col-md-6 text-left">
								<input type="text" class="input-large form-control" id="newParameterName"
									style="margin-left: 13px" data-toggle="tooltip"
									data-placement="right" title="Enter Parameter Name"></input>
							</div>
						</div>
						<div class="text-right row mrgt10">
							<div class="col-md-4">
								<label class="form-label">Parameter value:</label>
							</div>
							<div class="col-md-6 text-left">
								<input type="text" class="input-large form-control" id="newParameterValue"
									style="margin-left: 13px" data-toggle="tooltip"
									data-placement="right" title="Enter Parameter Value"></input>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<a class="btn btn-default" data-dismiss="modal" href="#">Cancel</a>
						<button class="btn btn-default" id="addParamCassandra" href="#">Add</button>
					</div>
				</div>
			</div>
		</div>
	<div class="modal-backdrop loadingPage element-hide" id="showLoading"
		style=""></div>
</body>
</html>




