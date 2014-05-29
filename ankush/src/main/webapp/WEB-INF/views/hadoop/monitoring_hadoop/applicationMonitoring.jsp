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
<!-- Hadoop Cluster Job Monitoring page -->

<%@ include file="../../layout/blankheader.jsp"%>

<script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/js/hadoop/hadoopJobs.js"
	type="text/javascript"></script>
<script type="text/javascript">
function autoRefreshHadoopAppMonitoringPagePage(clusterId){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autoRefresh_HadoopApps'; 
	obj1.callFunction = "com.impetus.ankush.hadoopJobs.pageLoad_AppMonitoring("+clusterId+");";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	$(document).ready(function(){
		$('.dropdown-toggle').dropdown();
		var clusterId = "<c:out value='${clusterId}'/>";
		com.impetus.ankush.hadoopJobs.pageLoad_AppMonitoring(clusterId); 
		autoRefreshHadoopAppMonitoringPagePage(clusterId);
	});
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Application Monitoring</h2>
			
			<button class="btn-error" id="errorBtnHadoop"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>
		
		<!--  
		<div class="span3 minhgt0">
			
		</div>
		-->
	</div>
</div>

<div class="section-body" >
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="error-div-hadoop" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content"
					style="color: red;"></span>
			</div>
		</div>

		<div class="row-fluid">
			<div id="all_tiles_JobMonitoring" class="masonry mrgt10"></div>
		</div>


		<div class="row-fluid span12 mrgt15 mrgl0">
			<div class="text-left left">
				<label class='section-heading text-left mrgt15'>Applications</label>
			</div>
			<div class="text-left span3 left" style="display: none;">
				<button type="button" class="btn mrgt10"
					onclick="com.impetus.ankush.hadoopJobs.killJobs(<c:out value='${clusterId}'/>);">Kill</button>
				<button type="button" class="btn mrgl10 mrgt10"
					onclick="com.impetus.ankush.hadoopJobs.updateJobsPriority(<c:out value='${clusterId}'/>);">Update
					Priority</button>
					
			</div>
			<div class="span5 form-image text-right btn-group"
				id="toggle-button_JobMonitoring" data-toggle="buttons-radio"
				style="">
				<button type="button" class="btn active" id="btnAll_HJM"
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('');">All</button>
					<button type="button" class="btn" id="btnAll_HJM" style="display: none;"
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('ACCEPTED');">Accepted</button>
				<button type="button" class="btn" id="btnRunning_HJM"
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('FAILED');">Failed</button>
				<button type="button" class="btn" id="btnCompleted_HJM"
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('FINISHED');">Finished</button>
				<button type="button" class="btn" id="btnFailed_HJM" 
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('KILLED');">Killed</button>
				<button type="button" class="btn" id="btnKilled_HJM" 
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('NEW');">New</button>
					<button type="button" class="btn" id="btnKilled_HJM" style="display: none;"
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('NEW SAVING');">New Saving</button>
					<button type="button" class="btn" id="btnKilled_HJM"
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('RUNNING');">Running</button>
					<button type="button" class="btn" id="btnKilled_HJM" style="display: none;"
					onclick="com.impetus.ankush.hadoopJobs.filterHadoop2App('SUBMITTED');">Submitted</button>
			</div>
			<div class="text-right">
				<input id="searchJobsTableHadoop" type="text" placeholder="Search"
					style="width: 200px;">
			</div>
		</div>
		<div class="row-fluid  span12 mrgl0">
			<div>
				<table class="table table-striped table-border" id="tblJobMonitoring"
					width="100%">
					<thead style="text-align: left;">
						<tr>
<!-- 							<th><input type='checkbox' id='checkHead_JobMonitoring' -->
<!-- 								name="nodeCheckHead" -->
<!-- 								onclick="com.impetus.ankush.hadoopJobs.checkAllNode_JobMonitoring()"></th> -->
							<th>Id</th>
							<th>User</th>
							<th>Name</th>
							<th>Queue</th>
							<th>State</th>
							<th>Final Status</th>
							<th>Application Type</th>
							<th>Progress</th>
<!-- 							<th>Cluster Id</th> -->
							<th>Started Time</th>
<!-- 							<th>Finished Time</th> -->
							<th>Elapsed Time</th>
							<th></th>
						</tr>
					</thead>
					<tbody style="text-align: left;">
					</tbody>
				</table>
			</div>
		</div>
		<div id="div_Request_JobMonitoring" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Job Request</h4>
			</div>
			<div class="row-fluid modal-body" id="lblJobRequestMessage"></div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" id="divOkbtn_JobMonitoring"
					class="btn"
					onclick="com.impetus.ankush.hadoopJobs.closeDialog_Request_JobMonitoring()">OK</a>
			</div>
		</div>
	</div>
</div>
