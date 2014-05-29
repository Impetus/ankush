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
<script type="text/javascript">
	$(document).ready(function(){
	appMonitorDrillDown =	$('#appMonitorDrillDown').dataTable({
			"bJQueryUI" : false,
			"bPaginate" : false,
			"bLengthChange" : true,
			"bFilter" : true,
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
		$("#appMonitorDrillDown_filter").css({
			'display' : 'none'
		});
		$('#searchAppMonitorDrillDown').keyup(function() {
			$("#appMonitorDrillDown").dataTable().fnFilter($(this).val());
		});
	});
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Application Details</h2>
			
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

		
		
		


		<div class="row-fluid span12 mrgt15 mrgl0">
			<div class="text-left left">
				<label class='section-heading text-left mrgt15'>Attempt List</label>
			</div>
			
			
			<div class="text-right">
				<input id="searchAppMonitorDrillDown" type="text" placeholder="Search"
					style="width: 200px;">
			</div>
		</div>
		<div class="row-fluid  span12 mrgl0">
			<div>
				<table class="table table-striped table-border" id="appMonitorDrillDown"
					width="100%">
					<thead style="text-align: left;">
						<tr>
							<th>Attempt Number</th>
							<th>Start Time</th>
							<th>Node ID</th>
							<th>Logs Link</th>
							<th>Node Address</th>
							<th>Container Id</th>
						</tr>
					</thead>
					<tbody style="text-align: left;">
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid span12 mrgt15 mrgl0">
			<div class="text-left left">
					<label class='section-heading text-left mrgt15'></label>
				</div>
		</div>	
		<div id="all_tiles_JobInfo"></div>
		<div class="row-fluid span12 mrgt15 mrgl0">
		<div class="text-left left">
				<label class='section-heading text-left mrgt15'></label>
			</div>
			</div>
		<div id="all_tiles_appInfo"></div>
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
