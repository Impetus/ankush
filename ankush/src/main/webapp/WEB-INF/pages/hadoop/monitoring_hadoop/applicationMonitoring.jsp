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
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hadoop/hadoopJobs.js"
	type="text/javascript"></script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1>Application Monitoring</h1>
</div>
<div class="page-body" id="main-content">
<%@ include file="../../layout/breadcrumbs.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<div id="error-div-hadoop" class="col-md-12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content"
					style="color: red;"></span>
			</div>
		</div>

		<div class="row">
			<div id="all_tiles_JobMonitoring" class="masonry mrgt10"></div>
		</div>


		
		
		<div class="panel">
		
		<div class="panel-heading" style="padding:2px;">
								<h3 class="panel-title left mrgt5 mrgl5">Applications</h3>
								<div class="btn-group mrgl10" id="graphButtonGroup_JobMonitoring"
									id="toggle-button_JobMonitoring" data-toggle="buttons-radio" >
									<button type="button" class="btn btn-default active" id="btnAll_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('');">All</button>
            						<button type="button" class="btn btn-default" id="btnFailed_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('FAILED');">Failed</button>
            						<button type="button" class="btn btn-default" id="btnCompleted_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('FINISHED');">Finished</button>
            						<button type="button" class="btn btn-default" id="btnKilled_HJM"  onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('KILLED');">Killed</button>	
									<button type="button" class="btn btn-default" id="btnNew_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('NEW');">New</button>
									<button type="button" class="btn btn-default" id="btnRunning_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('RUNNING');">Running</button>
								</div>
								<input id="searchJobsTableHadoop" class="pull-right search-datatable" type="text" placeholder="Search">
									
		</div>						
							
		<div class="row">
			<div class="col-md-12 text-left">
				<table class="table" id="tblJobMonitoring">
					<thead style="text-align: left;">
						<tr>
							<th>Id</th>
							<th>User</th>
							<th>Name</th>
							<th>Queue</th>
							<th>State</th>
							<th>Final Status</th>
							<th>Application Type</th>
							<th>Progress</th>
							<th>Started Time</th>
							<th>Elapsed Time</th>
							<th></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

	</div>
		
		<div id="div_Request_JobMonitoring" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Job Request</h4>
			</div>
			<div class="row modal-body" id="lblJobRequestMessage"></div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" id="divOkbtn_JobMonitoring"
					class="btn"
					onclick="com.impetus.ankush.hadoopJobs.closeDialog_Request_JobMonitoring()">OK</a>
			</div>
		</div>
	</div>
</div>
</div>
<script>

$(document).ready(function(){
	$('.dropdown-toggle').dropdown();
	com.impetus.ankush.hadoopJobs.pageLoad_AppMonitoring(clusterId); 
	var appList = {};
	appList.method = function(){
		com.impetus.ankush.hadoopJobs.pageLoad_AppMonitoring(clusterId); 
	};
	com.impetus.ankush.createAutorefresh([appList]);
});
</script>


</body>
</html>
