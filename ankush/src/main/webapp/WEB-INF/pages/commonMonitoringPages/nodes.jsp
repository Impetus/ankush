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

<script type="text/javascript">

	$(document).ready(
			function() {
				nodeDetailsTable = $('#nodesDetailTable').dataTable(
						{
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"bAutoWidth" : false,
							"sPaginationType" : "full_numbers",
							"bAutoWidth" : false,
							"aaSorting": [],
							"aoColumnDefs": [
							                 { 'bSortable': false, 'aTargets': [ 0,2,3,4,5,6] },
							                 ], 
			                 'fnRowCallback' : function(nRow, aData, iDisplayIndex,
			                		 iDisplayIndexFull) {
			                	 $(nRow).attr('id', 'rowIdNode-' + iDisplayIndex);
			                 },
			                 "fnCreatedRow": function( nRow, aData, iDataIndex ) {
			         	        var tableRowData = $(nRow).children("td");
			         	        var tableHeaderRows = $("#nodesDetailTable").find("th");
			         	 		for(var i = 0; i < tableRowData.length; i++)
			         	        {
			         	        	$(tableRowData[i]).css("word-wrap", "break-word");
			         	            $(tableRowData[i]).css("max-width", ($(tableHeaderRows[i]).css("width").replace("px", "") - 10) + "px");
			         	        }
			         	    },
						});
				$('#btnNodeList_DeleteNodes').tooltip();
				$("#nodesDetailTable_filter").css({
					'display' : 'none'
				});
				$('#searchnodeDetailsTable').keyup(function() {
					$("#nodesDetailTable").dataTable().fnFilter($(this).val());
				});
				com.impetus.ankush.nodes.nodeDetails();
				var nodeList = {};
				nodeList.method = function(){
					com.impetus.ankush.nodes.nodeDetails();
				};
				pageLevelAutorefreshArray.push(nodeList);
				com.impetus.ankush.createAutorefresh(pageLevelAutorefreshArray);
			});
</script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1 class="left">Nodes</h1>
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
<div class="row">
			<div id="error-div" class="col-md-12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content"></span>
			</div>
		</div>
	<div id="nodesDetail">
<div class="panel">
		<div class="panel-heading">
		<h3 class="panel-title left mrgt10 mrgl5">Nodes</h3>
			 <button class="btn btn-default" data-loading-text="Deleting..."  onclick="com.impetus.ankush.nodes.canDeleteNodeCheck();">Delete</button>
			<div class="pull-right">
				<input id="searchnodeDetailsTable" class="search-datatable" type="text"
						placeholder="Search">
			</div>
		</div>
	<div class="row panel-body">
		<div class="col-md-12 text-left">
			<table class="table"
					id="nodesDetailTable">
					<thead>
						<tr>
							<th><input type='checkbox' id='nodeListCommonTech'
								name="checkHead_NodeList"
								onclick="com.impetus.ankush.nodes.checked_unchecked_all('checkboxoption',this)"></th>
							<th>Host Name</th>
							<th style="width:50%;">Type</th>
							<th>State</th>
							<th>CPU Usage</th>
							<th>Total Memory</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
		</div>
	</div>
									
		</div>	
		
			
		</div>	
		</div>
</div>
</div>
<div class="modal fade" id="deleteNodesDialog" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4>Delete Node(s)</h4>
					</div>
						<div class="modal-body">
							<div class="row">
								<div class="col-md-12"
									style="text-align: left; font-size: 14px;">Node(s)
									will be permanently deleted. Once deleted data cannot be
									recovered. Please enter your password to continue.</div>
							</div>
							<div class="row mrgt20">
								<div class="col-md-2 text-right" style="font-size: 14px;" id="">
									<label class="form-label">Password:</label>
								</div>
								<div class="col-md-10 col-lg-4 text-left">
									<input type="password" class="form-control" palaceholder="Enter Password" id="passForDelete" />
								</div>
								<div id="passForDeleteError" class="error"></div>
							</div>

						</div>
						<div class="modal-footer">
							<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a> <button
								href="#" id="deleteNodes"
								class="btn btn-default" data-loading-text="Deleting...">Delete</button>
						</div>
					</div>
			</div>
		</div>
</body>
</html>	
