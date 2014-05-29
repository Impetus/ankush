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

td.group {
	padding-left: 100px;
}
td.expanded-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.expanded-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center #c0e1ff !important;
}
td.collapsed-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.collapsed-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center #c0e1ff !important;
}
 .table-striped tbody > tr:nth-child(2n) > td, .table-striped tbody > tr:nth-child(2n) > th {
    background-color: #FFFFFF;
}
.table-striped tbody > tr:nth-child(2n+1) > td, .table-striped tbody > tr:nth-child(2n+1) > th {
    background-color: #F9F9F9;
}
</style>
<script>
var topicMoreDetailDrillTable = null;
var partitionDetail = null;
$(document).ready(function(){
	topicMoreDetailDrillTable=	$('#topicMoreDetailDrillTable').dataTable({
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
	}).rowGrouping({
		bExpandableGrouping : true,
        iGroupingColumnIndex : 0});
	$("#topicMoreDetailDrillTable_filter").css({
		'display' : 'none'
	});
	$('#searchtopicMoreDetailDrillTable').keyup(function() {
		$("#topicMoreDetailDrillTable").dataTable().fnFilter($(this).val());
	});
	partitionDetail=	$('#partitionDetail').dataTable({
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
	}).rowGrouping({
		bExpandableGrouping : true,
        iGroupingColumnIndex : 0});
	$("#partitionDetail_filter").css({
		'display' : 'none'
	});
	$('#searchpartitionDetail').keyup(function() {
		$("#partitionDetail").dataTable().fnFilter($(this).val());
	});
});


</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left" id="topicMoreDrillDown"></h2>
		</div>
</div>
</div>
<div class="section-body">

<div class="container-fluid">
<div class="row-fluid">
		<div class="span6 text-left">
			<h4 class="section-heading" style="text-align: left;">Topic Details</h4>
		</div>
		<div class="text-right span6">
					<input id="searchtopicMoreDetailDrillTable" type="text"
						placeholder="Search">
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="topicMoreDetailDrillTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Category</th>
						<th>Category</th>
						<th>Parameter</th>
						<th>Value</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span6 text-left">
			<h4 class="section-heading" style="text-align: left;">Partition Details</h4>
		</div>
		<div class="text-right span6">
					<input id="searchpartitionDetail" type="text"
						placeholder="Search">
		</div>
	</div>
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="partitionDetail"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Partition</th>
						<th>Partition</th>
						<th>Parameter</th>
						<th>Value</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	</div>
</div>
