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
<!-- Job Schedular Configuration page  -->

<%@ include file="../../layout/blankheader.jsp"%>

<script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/js/hadoop/jobScheduler.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/js/hadoop/hadoopJobs.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				var clusterId = "<c:out value='${clusterId}'/>";
				$('#jobScheduler_default').attr('checked', 'checked');
				com.impetus.ankush.jobScheduler.tooltip_FairScheduler();
				com.impetus.ankush.jobScheduler.initTables_FairScheduler();
				com.impetus.ankush.jobScheduler.initializeQueueTable();
				com.impetus.ankush.jobScheduler.appendModalPopUp();
				com.impetus.ankush.jobScheduler.getDefaultJobSchedulerValues(clusterId);
			});
	$('.dropdown-toggle').dropdown();
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Job Scheduler</h2>
			
			<button class="btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>

<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
-->

		<div class="span5 text-right">
			<button class="btn mrgr10"
				onclick="com.impetus.ankush.removeCurrentChild();">Revert</button>
			<button class="btn" id="saveSchedulerButton"
				onclick="com.impetus.ankush.jobScheduler.postJobScheduler();">Save</button>
		</div>
	</div>
</div>

<div class="section-body content-body mrgnlft8">
<div class="container-fluid ">
	<div id="div_RequestPlaced_JobScheduler" class="modal hide fade" data-backdrop="static" 
		style="display: none;">
		<div class="modal-header text-center">
			<h4>Job Scheduler Request</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12 font14 text-left">
					Job scheduler request in progress, please wait....</div>
			</div>
		</div>
	</div>
	<div id="div_RequestError_JobScheduler" class="modal hide fade"
		style="display: none;">
		<div class="modal-header text-center">
			<h4>Job Scheduler Request</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12  font14 text-left" >
					Unknown error while processing Job scheduler request.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn" id="div_RequestError_JobScheduler_OK">OK</a>
		</div>
	</div>
	<div class="row-fluid">
		<div id="error-div-hadoop" class="span12 error-div-hadoop"
			style="display: none;">
			<span id="popover-content"
				style="color: red;"></span>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label">Job Scheduler:</label>
		</div>
		<div class="span10 text-left inline" style="margin-top: 15px;">
			<input type="radio" name="jobScheduler" id="jobScheduler_default"
				onclick="com.impetus.ankush.jobScheduler.showDefaultScheduler();" /><span>&nbsp;<b>Default</b></span>
				
				<input type="radio" name="jobScheduler" id="jobScheduler_capacity"
				onclick="com.impetus.ankush.jobScheduler.showCapacityScheduler();" style="margin-left:20px"/><span>&nbsp;<b>Capacity</b></span>
				
				<input type="radio" name="jobScheduler" id="jobScheduler_fair"
				onclick="com.impetus.ankush.jobScheduler.showFairScheduler()" style="margin-left:20px" /><span>&nbsp;<b>Fair</b></span>
				
		</div>
		
	</div>
	<%@ include file="_capacity_scheduler.jsp"%>
	<%@ include file="_fair_Scheduler.jsp"%>
	</div>
</div>
