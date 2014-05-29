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
var indexingHealthTable = null;
var indexingTotalTable = null;
var searchTotalTable = null;
var mergeActivityTable = null;
var documentsTable = null;
var getTotalTable = null;
var operationsTable = null;
$(document).ready(function(){
	indexingHealthTable=	$('#indexingHealthTable').dataTable({
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
	$("#indexingHealthTable_filter").css({
		'display' : 'none'
	});
	$('#searchindexingHealthTable').keyup(function() {
		$("#indexingHealthTable").dataTable().fnFilter($(this).val());
	});
	indexingTotalTable=	$('#indexingTotalTable').dataTable({
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
	$("#indexingTotalTable_filter").css({
		'display' : 'none'
	});
	$('#searchindexingTotalTable').keyup(function() {
		$("#indexingTotalTable").dataTable().fnFilter($(this).val());
	});
	searchTotalTable =	$('#searchTotalTable').dataTable({
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
	$("#searchTotalTable_filter").css({
		'display' : 'none'
	});
	$('#searchsearchTotalTable').keyup(function() {
		$("#searchTotalTable").dataTable().fnFilter($(this).val());
	});
	mergeActivityTable =	$('#mergeActivityTable').dataTable({
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
	$("#mergeActivityTable_filter").css({
		'display' : 'none'
	});
	$('#searchmergeActivityTable').keyup(function() {
		$("#mergeActivityTable").dataTable().fnFilter($(this).val());
	});
	documentsTable =	$('#documentsTable').dataTable({
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
	$("#documentsTable_filter").css({
		'display' : 'none'
	});
	$('#searchdocumentsTable').keyup(function() {
		$("#documentsTable").dataTable().fnFilter($(this).val());
	});
	getTotalTable =	$('#getTotalTable').dataTable({
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
	$("#getTotalTable_filter").css({
		'display' : 'none'
	});
	$('#searchgetTotalTable').keyup(function() {
		$("#getTotalTable").dataTable().fnFilter($(this).val());
	});
	operationsTable =	$('#operationsTable').dataTable({
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
	$("#operationsTable_filter").css({
		'display' : 'none'
	});
	$('#searchoperationsTable').keyup(function() {
		$("#operationsTable").dataTable().fnFilter($(this).val());
	});
	//com.impetus.ankush.elasticSearchMonitoring.populateIndexTileTables(ipForNodeDrillDown);
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
					Health<span id="indexinfo-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="indexingHealthTable"
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
					Indexing Totals<span id="indexinfo-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="indexingTotalTable"
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
					Search Totals<span id="searchtotal-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="searchTotalTable"
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
		<div class="row-fluid" id="elastic-tables">
			
			
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					Merge Activity<span id="mergeActivity-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="mergeActivityTable"
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
					Documents<span id="documents-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="documentsTable"
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
					Get Totals<span id="getTotal-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="getTotalTable"
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
					Operations<span id="operations-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="operationsTable"
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
			
		
