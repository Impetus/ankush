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
<!-- Hadoop Job details Page containing placed job details -->

<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js"
	type="text/javascript"></script>
<script type="text/javascript">
function autoRefreshjobDetailChildPagePage(jobId){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'refreshInterval_JobDetails'; 
	obj1.callFunction = "com.impetus.ankush.hadoopJobs.loadJobDetails(\""+jobId+"\");";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	$(document).ready(function(){
		$('.dropdown-toggle').dropdown();
		var jobId = "<c:out value='${jobId}'/>";
		com.impetus.ankush.hadoopJobs.loadJobDetails("<c:out value='${jobId}'/>");
		autoRefreshjobDetailChildPagePage("<c:out value='${jobId}'/>");
	});
</script>
<div class="section-header">
	<div  class="row-fluid"   style="margin-top:20px;">
	  	<div class="span4"><h2 class="heading text-left">Job Details</h2>
	  	<button class="btn-error" id="errorBtnHadoop_JobDetails"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
	  	</div>
	  	
	</div>
</div>
<div class="section-body">

<div class="container-fluid mrgnlft8">
	<div class="row-fluid">
		<div id="error-div-hadoop_JobDetails" class="span12 error-div-hadoop"
			style="display: none;">
			<span id="popover-content_JobDetails"
				style="color: red;"></span>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Job Id:</label></div>
		<div class="span10"><label class="text-left data" id="jobDetails_JobId"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label" >Job Name:</label></div>
		<div class="span10"><label class="text-left data" id="jobDetails_jobName"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Job State:</label></div>
		<div class="span10"><label class="text-left data" id="jobDetails_jobState"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">User Name:</label></div>
		<div class="span10">
			<label class="text-left data" id="jobDetails_userName"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Job Priority:</label></div>
		<div class="span10">
			<label class="text-left data" id="jobDetails_jobPriority"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Job Start Time:</label></div>
		<div class="span10">
			<label class="text-left data" id="jobDetails_jobStartTime"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Scheduling Info:</label></div>
		<div class="span10"><label class="text-left data" id="jobDetails_schedulingInfo"></label></div>
	</div>
	<div class="row-fluid" style="display: none;">
		<div class="span2"><label class="text-right form-label" >Job Complete:</label></div>
		<div class="span10"><label class="text-left data" id="jobDetails_jobComplete"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Map % Complete:</label></div>
		<div class="span10"><label class="text-left data" id="jobDetails_mapProgress"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Map Total:</label></div>
		<div class="span10">
			<label class="text-left data" id="jobDetails_mapTotal"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Map Completed:</label></div>
		<div class="span10">
			<label class="text-left data" id="jobDetails_mapCompleted"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Reduce % Complete:</label></div>
		<div class="span10"><label class="text-left data" id="jobDetails_reduceProgress"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Reduce Total:</label></div>
		<div class="span10">
			<label class="text-left data" id="jobDetails_reduceTotal"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Reduce Completed:</label></div>
		<div class="span10">
			<label class="text-left data" id="jobDetails_reduceCompleted"></label>
		</div>
	</div>
	</div>
</div>	
