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
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/ankush.validation.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hadoop/hadoopJobs.js"
	type="text/javascript"></script>
<script>
$(document).ready(function(){
	$('.dropdown-toggle').dropdown();
	com.impetus.ankush.hadoopJobs.pageLoad_JobMonitoring(clusterId); 
});
</script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1>Job Monitoring</h1>
</div>
<div class="page-body" id="main-content">
<%@ include file="../../layout/breadcrumbs.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<div id="error-div-hadoop" class="col-md-12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content-hadoop"
					style="color: red;"></span>
			</div>
		</div>

		<div class="row">
			<div id="all_tiles_JobMonitoring" class="masonry mrgt10"></div>
		</div>


		<div class="row col-md-12 mrgt15 mrgl0">
			<!-- <div class="text-left left">
				<label class='section-heading text-left'>Jobs</label>
			</div> -->
			<%-- <div class="text-left col-md-3 left">
					<button type="button" class="btn btn-default"
						onclick="com.impetus.ankush.hadoopJobs.killJobs(<c:out value='${clusterId}'/>);">Kill</button>
					<button type="button" class="btn btn-default mrgl10"
						onclick="com.impetus.ankush.hadoopJobs.updateJobsPriority(<c:out value='${clusterId}'/>);">Update
						Priority</button>
			</div>
			<div class="col-md-5 form-image text-right btn-group"
				id="toggle-button_JobMonitoring" data-toggle="buttons-radio"
				style="">
				<button type="button" class="btn active" id="btnAll_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('ALL');">All</button>
				<button type="button" class="btn btn-default" id="btnRunning_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('RUNNING');">Running</button>
				<button type="button" class="btn btn-default" id="btnCompleted_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('SUCCEEDED');">Succeeded</button>
				<button type="button" class="btn btn-default" id="btnFailed_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('FAILED');">Failed</button>
				<button type="button" class="btn btn-default" id="btnKilled_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('KILLED');">Killed</button>
			</div> --%>
			<div class="text-right">
				
			</div>
		</div>
		<!-- <div class="row  col-md-12 mrgl0">
			<div>
				<table class="table table-striped table-border" id="tblJobMonitoring"
					width="100%">
					<thead style="text-align: left;">
						<tr>
							<th><input type='checkbox' id='checkHead_JobMonitoring'
								name="nodeCheckHead"
								onclick="com.impetus.ankush.hadoopJobs.checkAllNode_JobMonitoring()"></th>
							<th>JobId</th>
							<th>Job</th>
							<th>Priority</th>
							<th>Map % Complete</th>
							<th>Reduce % Complete</th>
							<th>State</th>
							<th></th>
						</tr>
					</thead>
					<tbody style="text-align: left;">
					</tbody>
				</table>
			</div>
		</div> -->
		<div class="panel">
		<div class="panel-heading">
			<%-- <div class="col-md-2">
				<h3 class="panel-title">Jobs</h3>
		    </div>
		    <div class="col-md-3">
				<a href="#" id="addParameters" class="btn btn-default btn-sm" onclick="com.impetus.ankush.hadoopJobs.killJobs(<c:out value='${clusterId}'/>);">Kill</a>	
				<a href="#" id="addParameters" class="btn btn-default btn-sm" onclick="com.impetus.ankush.hadoopJobs.updateJobsPriority(<c:out value='${clusterId}'/>);">Update Priority</a>
			</div>
			<div class="col-md-5 form-image text-right btn-group"
				id="toggle-button_JobMonitoring" data-toggle="buttons-radio"
				style="">
				<a href="#" class="btn btn-default active" id="btnAll_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('ALL');">All</a>
				<a href="#" class="btn btn-default" id="btnRunning_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('RUNNING');">Running</a>
				<a href="#" class="btn btn-default" id="btnCompleted_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('SUCCEEDED');">Succeeded</a>
				<a href="#" class="btn btn-default" id="btnFailed_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('FAILED');">Failed</a>
				<a href="#" class="btn btn-default" id="btnKilled_HJM"
					onclick="com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table('KILLED');">Failed</a>
			</div> 
			
			<div class="col-md-2 text-right">
				<input id="searchJobsTableHadoop" class="" type="text"
					placeholder="Search">
			</div> --%>
			<h3 class="panel-title left mrgt5 mrgl5">Jobs</h3>
						<div class="btn-group mrgl10" id="graphButtonGroup_JobMonitoring"
									id="toggle-button_JobMonitoring" data-toggle="buttons-radio" >
									<button type="button" class="btn btn-default active" id="btnAll_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('');">All</button>
            						<button type="button" class="btn btn-default" id="btnRunning_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('RUNNING');">Running</button>
            						<button type="button" class="btn btn-default" id="btnCompleted_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('SUCCEEDED');">Succeeded</button>
            						<button type="button" class="btn btn-default" id="btnFailed_HJM"  onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('FAILED');">Failed</button>	
									<button type="button" class="btn btn-default" id="btnKilled_HJM" onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('KILLED');">Killed</button>
								</div>
								<input id="searchJobsTableHadoop" class="pull-right search-datatable" type="text" placeholder="Search">
		</div>
		<div class="row panel-body">
			<div class="col-md-12 text-left">
				<table class="table" id="tblJobMonitoring"
					width="100%">
					<thead style="text-align: left;">
						<tr>
							<th><input type='checkbox' id='checkHead_JobMonitoring'
								name="nodeCheckHead"
								onclick="com.impetus.ankush.hadoopJobs.checkAllNode_JobMonitoring()"></th>
							<th>JobId</th>
							<th>Job</th>
							<th>Priority</th>
							<th>Map % Complete</th>
							<th>Reduce % Complete</th>
							<th>State</th>
							<!-- <th></th> -->
						</tr>
					</thead>
					<tbody style="text-align: left;">
					</tbody>
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
</body>
</html>
