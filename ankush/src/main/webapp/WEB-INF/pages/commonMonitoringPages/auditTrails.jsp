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
<%@ include file="../layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all"/>
<%@ include file="../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.dateFormat-1.0.js"
	type="text/javascript"></script>
<script type="text/javascript">
var auditTrailTable = null;
$(document).ready(function() {
	auditTrailTable = $('#auditTrailTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"fnCreatedRow": function( nRow, aData, iDataIndex ) {
		        var tableRowData = $(nRow).children("td");
		        var tableHeaderRows = $("#auditTrailTable").find("th");
		 		for(var i = 0; i < tableRowData.length; i++)
		        {
		        	$(tableRowData[i]).css("word-wrap", "break-word");
		            $(tableRowData[i]).css("max-width", ($(tableHeaderRows[i]).css("width").replace("px", "") - 10) + "px");
		        }
		},				 
	});
	$("#auditTrailTable_filter").hide();
	auditTrailTable = $('#auditTrailTable').dataTable();
	$('#auditTrailSearchBox').keyup(function() {
		auditTrailTable.fnFilter($(this).val());
	});
	/* This function will fill audit trail datatable */
	com.impetus.ankush.auditTrail.commonAuditTrail();
});

</script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1 class="left">Audit Trail</h1>
   <div class="pull-right"
				onclick="com.impetus.ankush.alerts.saveAlertsAndHAConf();" id="saveAlertsButton">&nbsp;</div>
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
	<div id="nodesDetail">
<div class="panel">
		<div class="panel-heading">
				<h3 class="panel-title">Audit Trail</h3>
			<div class="pull-right panelSearch">
				<input id="auditTrailSearchBox" class="search-datatable" type="text"
					placeholder="Search">
			</div>
		</div>
	<div class="row panel-body">
		<div class="col-md-12 text-left">
			<table class="table" id="auditTrailTable" >
					<thead class="text-left">
						<tr>
						<th style="width:15%">File</th>
						<th style="width:10%">Type</th>
						<th style="width:15%">Property Name</th>
						<th style="width:35%">Property Value</th>
						<th style="width:8%">User</th>
						<th style="width:15%">Time</th>
						
						</tr>
					</thead>
					<tbody class="text-left">
					</tbody>
				</table>
		</div>
	</div>
									
		</div>	
		
	</div>		
		</div>	
</div>
</div>
</body>
</html>	
