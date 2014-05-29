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
var brokerDrillDownDetailTable = null;
var logFlushDetailTable = null;
var topicNameListTable = null;
function brokerDrillDownPageAutorefresh(){	
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autorefresh_monitoringKafkaBrokerDrillDown'; 
	obj1.callFunction = "com.impetus.ankush.kafkaMonitoring.brokerDrillDownDetailTable(\""+ipForNodeDrillDown+"\");";
	obj1.time = 30000;
	obj1.autorefreshFlag = false;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function(){
	if(com.impetus.ankush.commonMonitoring.clusterTechnology != 'Hybrid'){
		var obj1 = {};
		obj1.varName = 'is_autorefresh_monitoringKafkaBrokerDrillDown'; 
		obj1.callFunction = "com.impetus.ankush.kafkaMonitoring.brokerDrillDownDetailTable(\""+ipForNodeDrillDown+"\");";
		obj1.time = 30000;
		obj1.varName = setInterval(obj1.callFunction,obj1.time);
		autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
	}
	else{
		brokerDrillDownPageAutorefresh();
	}
	brokerDrillDownDetailTable=	$('#brokerDrillDownDetailTable').dataTable({
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
	$("#brokerDrillDownDetailTable_filter").css({
		'display' : 'none'
	});
	$('#searchbrokerDrillDownDetailTable').keyup(function() {
		$("#brokerDrillDownDetailTable").dataTable().fnFilter($(this).val());
	});
	topicNameListTable =	$('#topicNameListTable').dataTable({
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
	$("#topicNameListTable_filter").css({
		'display' : 'none'
	});
	$('#searchtopicNameListTable').keyup(function() {
		$("#topicNameListTable").dataTable().fnFilter($(this).val());
	});
	logFlushDetailTable =	$('#logFlushDetailTable').dataTable({
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
	$("#logFlushDetailTable_filter").css({
		'display' : 'none'
	});
	$('#searchlogFlushDetailTable').keyup(function() {
		$("#logFlushDetailTable").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.kafkaMonitoring.brokerDrillDownDetailTable(ipForNodeDrillDown);
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
		<div class="row-fluid" id="kafka-tables">
			<div class="boxToExpand new-box span4">
				<div class="titleExpand">
					Broker Details<span id="broker-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="brokerDrillDownDetailTable"
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
					Log Details<span id="log-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="logFlushDetailTable"
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
					Topic Details<span id="topic-drilldown-arrow"></span>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
						<table class="table table-striped" id="topicNameListTable"
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
