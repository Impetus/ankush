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
<link rel="stylesheet" type="text/css"
	href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all" />
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hybrid/hybridMonitoring/hybridMonitoring_common.js"
	type="text/javascript"></script>

<script>
var cassandraNodeListTables = null;
$(document).ready(function(){
	cassandraNodeListTables = $('#cassandraNodeListTables').dataTable({
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
	$('#allTilesClusterCassandra').masonry({ 	
		itemSelector : '.item',
		columnWidth : 100,
		isAnimated : true
	});
	hybridTechnology = 'Cassandra';
	$("#logLink")
	.append(
			' <div class="row-fluid"><div class="span8 text-left" id="link_Logs"><h4 class="section-heading" ><a href="#" onclick="com.impetus.ankush.techListsAndActions[\'Cassandra\'].links[\'Logs\'].methodCall()">Logs&nbsp;&nbsp;&nbsp;<img src="'+
                    baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
	$("#logLink")
	.append(
			' <div class="row-fluid"><div class="span8 text-left" id="link_Logs"><h4 class="section-heading" ><a href="#" onclick="com.impetus.ankush.techListsAndActions[\'Cassandra\'].links[\'Configuration\'].confLinks[\'Parameters\'].methodCall()">Parameters&nbsp;&nbsp;&nbsp;<img src="'+
                    baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
	$("#logLink")
	.append(
			' <div class="row-fluid"><div class="span8 text-left" id="link_Logs"><h4 class="section-heading" ><a href="#" onclick="com.impetus.ankush.techListsAndActions[\'Cassandra\'].links[\'Keyspaces\'].methodCall()">Keyspaces&nbsp;&nbsp;&nbsp;<img src="'+
                    baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
	
	$("#hybridCommonMonitoringClusterNameHeading").html(clusterName+'/Cassandra');
	/* function will create common tiles on the basis of cluster id */
	//com.impetus.ankush.hybridMonitoring_Cassandra.createTiles();
	$('.dropdown-toggle').dropdown();
	com.impetus.ankush.hybridMonitoring_common.createTiles('Cassandra');
	com.impetus.ankush.cassandraMonitoring.nodeListPopulate();
	
});
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<div class="col-md-6 text-left">
				<h1 id="hybridCommonMonitoringClusterNameHeading"></h1>
			</div>
		</div>
		<div class="page-body" id="main-content">
			<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="section-body common-tooltip">
				<div class="container-fluid">
					<div class="row-fluid">
						<div id="allTilesClusterCassandra" class="masonry mrgt10"></div>
					</div>
					<div class="row" style="margin-top: 10px;">
						<div id="tileViewDetailCassandra"></div>
					</div>
					<div>
						<%@ include
							file="../../cassandra/cassandraClusterMonitoring/ringTopology.jsp"%>
					</div>
					<div class="panel">
						<div class="panel-heading">
							<h3 class="panel-title">Node List</h3>
						</div>
						<div class="row panel-body">
							<div class="col-md-12 text-left">
								<table class="table" id="cassandraNodeListTables">
									<thead style="text-align: left;">
										<tr>
											<th>Host Name</th>
											<th>Roles</th>
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
	<div class="modal-backdrop loadingPage" id="showLoading" style=""></div>
</body>
</html>

