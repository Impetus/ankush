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
<%@include file="../../layout/blankheader.jsp"%>
<script>
var topicIndexVariable = '<c:out value='${topicIndex}' />';
function topicDrillDownPageAutoRefresh(){
    var obj1 = {};
    var autoRefreshArray = [];
    obj1.varName = 'is_autoRefresh_topicDrillDownPage';
    obj1.callFunction = "com.impetus.ankush.kafkaMonitoring.topicDrillDownDetailTable("+topicIndexVariable+");";
    obj1.time = 30000;
    autoRefreshArray.push(obj1);
    com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
var topicDrillDownDetailTable = null;
$(document).ready(function(){
	com.impetus.ankush.kafkaMonitoring.removeKafkaMonitoringPageAutorefresh();
	topicDrillDownDetailTable=	$('#topicDrillDownDetailTable').dataTable({
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
	topicDrillDownPageAutoRefresh();
});


</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left" id="topicDrillDown"></h2>
		</div>
</div>
</div>
<div class="section-body">

<div class="container-fluid">
<div class="row-fluid">
		<div class="span2 text-left">
			<h4 class="section-heading" style="text-align: left;">Topic Details</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="topicDrillDownDetailTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Partition</th>
						<th>Leader</th>
						<th>Replicas</th>
						<th>ISR</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	</div>
</div>
