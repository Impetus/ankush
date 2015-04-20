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

<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->

<style>
#hadoop-tables  .panel>.panel-heading:after {
	border-bottom: 0px;
	content: "";
	display: block;
	height: 0;
	padding-bottom: 0px;
}
</style>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hadoop/hadoopMonitoring.js"
	type="text/javascript"></script>
<script>
	var nodessummaryTable = null;
	var hdfssummaryTable = null;
	var mapreducesummaryTable = null;
	var processsummaryTable = null;
	var processsummaryTableNameNode = null;
	var processsummaryTableJobTracker = null;
	var processsummaryTableResourceManager = null;
	$(document)
			.ready(
					function() {
						var process = "JobTracker";
						if (com.impetus.ankush.isHadoop2(clusterId)) {
							process = "ResourceManager";
							$("#processsummaryTableJobTracker").attr('id',
									'processsummaryTableResourceManager');
						}
						com.impetus.ankush.hadoopMonitoring
								.populateProcessSummaryTables('processsummary',
										process);
						nodessummaryTable = $('#nodessummaryTable').dataTable({
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
							"oLanguage" : {
								"sEmptyTable" : 'Loading...',
							}
						});
						processsummaryTableJobTracker = $(
								'#processsummaryTableJobTracker').dataTable({
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
							"oLanguage" : {
								"sEmptyTable" : 'Loading...',
							}
						});
						processsummaryTableResourceManager = $(
								'#processsummaryTableResourceManager')
								.dataTable({
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
									"oLanguage" : {
										"sEmptyTable" : 'Loading...',
									}
								});
						processsummaryTableNameNode = $(
								'#processsummaryTableNameNode').dataTable({
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
							"oLanguage" : {
								"sEmptyTable" : 'Loading...',
							}
						});
						$("#hadoopProcessSummary_filter").css({
							'display' : 'none'
						});
						$('#searchhadoopProcessSummary').keyup(
								function() {
									$("#hadoopProcessSummary").dataTable()
											.fnFilter($(this).val());
								});
						hdfssummaryTable = $('#hdfssummaryTable').dataTable({
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
							"oLanguage" : {
								"sEmptyTable" : 'Loading...',
							}
						});
						$("#hdfsMonitoringUsage_filter").css({
							'display' : 'none'
						});
						$('#searchhdfsMonitoringUsage').keyup(
								function() {
									$("#hdfsMonitoringUsage").dataTable()
											.fnFilter($(this).val());
								});
						mapreducesummaryTable = $('#mapreducesummaryTable')
								.dataTable({
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
									"oLanguage" : {
										"sEmptyTable" : 'Loading...',
									}
								});
						$("#mapredMonitoringJobs_filter").css({
							'display' : 'none'
						});
						$('#searchmapredMonitoringJobs').keyup(
								function() {
									$("#mapredMonitoringJobs").dataTable()
											.fnFilter($(this).val());
								});
						com.impetus.ankush.hadoopMonitoring
								.populateTileTables('nodessummary');
						com.impetus.ankush.hadoopMonitoring
								.populateTileTables('hdfssummary');
						com.impetus.ankush.hadoopMonitoring
								.populateTileTables('mapreducesummary');
						com.impetus.ankush.hadoopMonitoring
								.populateProcessSummaryTables('processsummary',
										'NameNode');
					});
</script>

<div id="hadoop-tables">
	<div class="row">

		<div class="col-sm-4">
			<div class="panel">
				<div class="panel-heading">
					<h3 class="panel-title">Nodes Summary</h3>
				</div>
				<div class="row panel-body">
					<div class="col-md-12 text-left">
						<table class="table" id="nodessummaryTable">
							<thead style="text-align: left; display: none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>

		<div class="col-sm-4">
			<div class="panel">
				<div class="panel-heading">
					<h3 class="panel-title">MapReduce Summary</h3>
				</div>
				<div class="row panel-body">
					<div class="col-md-12 text-left">
						<table class="table" id="mapreducesummaryTable">
							<thead style="text-align: left; display: none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>

		<div class="col-sm-4">
			<div class="panel">
				<div class="panel-heading">
					<h3 class="panel-title">HDFS Summary</h3>
				</div>
				<div class="row panel-body">
					<div class="col-md-12 text-left">
						<table class="table" id="hdfssummaryTable">
							<thead style="text-align: left; display: none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-4">
			<div class="panel">
				<div class="panel-heading">
					<h3 class="panel-title" style="width: 100%">
						Process Summary-<span class="ResourceManager JobTracker"></span>
					</h3>
				</div>
				<div class="row panel-body">
					<div class="col-md-12 text-left">
						<table class="table" id="processsummaryTableJobTracker">
							<thead style="text-align: left; display: none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="col-sm-4">
			<div class="panel">
				<div class="panel-heading">
					<h3 class="panel-title" style="width: 100%">
						Process Summary-<span class="NameNode"></span>
					</h3>
				</div>
				<div class="row panel-body">
					<div class="col-md-12 text-left">
						<table class="table" id="processsummaryTableNameNode">
							<thead style="text-align: left; display: none">
								<tr>
									<th></th>
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



