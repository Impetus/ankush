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
<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->
<style>
 .table-striped tbody > tr:nth-child(2n) > td, .table-striped tbody > tr:nth-child(2n) > th {
    background-color: #F9F9F9;
}
.table-striped tbody > tr:nth-child(2n+1) > td, .table-striped tbody > tr:nth-child(2n+1) > th {
    background-color: #FFFFFF;
}
</style>
<script>
var processDetailTable = null;
var osListTable = null;
var threadPoolTable = null;
var fileSystemTable = null;
var indicesNodeTable = null;
var jvmTable = null;
var networkTable = null;
$(document).ready(function(){
	var obj1 = {};
	obj1.varName = 'is_autorefresh_monitoringElasticSearchTileTable'; 
	obj1.callFunction = "com.impetus.ankush.elasticSearchMonitoring.populateTileTables('ip','nodeinformation',\""+ipForNodeDrillDown+"\",'output');";
	obj1.time = 30000;
	obj1.varName = setInterval(obj1.callFunction,obj1.time);
	autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
	processDetailTable=	$('#processDetailTable').dataTable({
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
	$("#processDetailTable_filter").css({
		'display' : 'none'
	});
	$('#searchprocessDetailTable').keyup(function() {
		$("#processDetailTable").dataTable().fnFilter($(this).val());
	});
	osListTable =	$('#osListTable').dataTable({
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
	$("#osListTable_filter").css({
		'display' : 'none'
	});
	$('#searchosListTable').keyup(function() {
		$("#osListTable").dataTable().fnFilter($(this).val());
	});
	threadPoolTable =	$('#threadPoolTable').dataTable({
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
	$("#threadPoolTable_filter").css({
		'display' : 'none'
	});
	$('#searchthreadPoolTable').keyup(function() {
		$("#threadPoolTable").dataTable().fnFilter($(this).val());
	});
	fileSystemTable =	$('#fileSystemTable').dataTable({
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
	$("#fileSystemTable_filter").css({
		'display' : 'none'
	});
	$('#searchfileSystemTable').keyup(function() {
		$("#fileSystemTable").dataTable().fnFilter($(this).val());
	});
	indicesNodeTable =	$('#indicesNodeTable').dataTable({
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
	$("#indicesNodeTable_filter").css({
		'display' : 'none'
	});
	$('#searchindicesNodeTable').keyup(function() {
		$("#indicesNodeTable").dataTable().fnFilter($(this).val());
	});
	jvmTable =	$('#jvmTable').dataTable({
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
	$("#jvmTable_filter").css({
		'display' : 'none'
	});
	$('#searchjvmTable').keyup(function() {
		$("#jvmTable").dataTable().fnFilter($(this).val());
	});
	networkTable =	$('#networkTable').dataTable({
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
	$("#networkTable_filter").css({
		'display' : 'none'
	});
	$('#searchnetworkTable').keyup(function() {
		$("#networkTable").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.elasticSearchMonitoring.populateTileTables('ip','nodeinformation',ipForNodeDrillDown,'output');
});
</script>
	<!-- <div class="row-fluid">
			<div id="error-broker-drilldown" class="span12 error-div-hadoop"
				style="display: none;">
		<span id="popover-content" style="color: red"></span>
	</div>
	</div> -->
		<!-- <div class="row-fluid">
			<div class="row-fluid transitions-enabled" id="tilesBrokerDrillDown"
				style="margin-top: 10px;"></div>
		</div> -->
		<div class="row-fluid" id="elastic-tables">
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					Process<span id="process-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="processDetailTable"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					Os<span id="os-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="osListTable"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					Thread Pool<span id="thread-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="threadPoolTable"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
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
			<div class="row-fluid">
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					File System<span id="fs-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="fileSystemTable"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					Indices<span id="indices-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="indicesNodeTable"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					JVM<span id="jvm-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="jvmTable"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
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
			<div class="row-fluid">
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					Network<span id="network-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="networkTable"
							width="100%" style="border: 0px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
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
