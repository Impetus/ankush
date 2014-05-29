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
<%@ include file="../layout/blankheader.jsp"%>
<script>
var nodeIndexVar = '<c:out value ='${nodeIndex}'/>';
var lineChartToReDraw = null;
var ipForNodeDrillDown = null;
function autoRefreshNodeDrillDownPage(){
	var getLastTime = 'lasthour';
	var obj1 = {};
	var obj2 = {};
	var obj3 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autorefresh_individualNode'; 
	obj1.callFunction = "com.impetus.ankush.commonMonitoring.createTilesForIndividualNode("+com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh+");";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	obj2.varName = 'is_autorefresh_services';
	obj2.callFunction = "com.impetus.ankush.commonMonitoring.initServiceStatus("+com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh+");";
	obj2.time = 30000;
	autoRefreshArray.push(obj2);
	obj3.varName = 'is_autorefresh_graphs';
	obj3.callFunction = "com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring(\""+getLastTime+"\","+com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh+");";
	obj3.time = 30000;
	autoRefreshArray.push(obj3);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}

    $(document).ready(function() {
    	var nodeIndexVar = '<c:out value ='${nodeIndex}'/>'
    	ipForNodeDrillDown = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndexVar].publicIp;
    	//$(window).unbind('resize');
    	$('#tilesNodes_NDD').masonry({ 
			itemSelector : '.item',
			columnWidth : 100,
			isAnimated : true
		});
    	nodeServiceDetail = $('#nodeServiceDetail').dataTable({
			"bJQueryUI" : false,
			"bPaginate" : false,
			"bLengthChange" : true,
			"bFilter" : false,
			"bSort" : false,
			"bInfo" : false,
			"bAutoWidth" : false,
			"sPaginationType": "full_numbers",
			"bAutoWidth" : false, 
			"fnRowCallback" : function(nRow, aData, iDisplayIndex,
					iDisplayIndexFull) {
				$(nRow).attr('id', 'serviceRow-' + iDisplayIndex);
			}
		});
    	if(com.impetus.ankush.commonMonitoring.	clusterTechnology == 'Cassandra'){
    		var nodeActionUrl = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/nodeactionlist';
    		$.ajax({
				'type' : 'GET',
				'url' : nodeActionUrl,
				'contentType' : 'application/json',
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
					if(result.output.status == true){
						for(var i = 0 ; i < result.output.actions.length ; i++){
							$("#nodeLevelActionsDropDown").append(' <li><a tabindex="-1" href="#" class="text-left" onclick="com.impetus.ankush.cassandraMonitoring.postNodeActionServiceDialog(\''+result.output.actions[i]+'\',\'\',\'\',\'Node\')">'+result.output.actions[i]+'...</a></li>');
						}
					}else{
						
					}
				},
				'error' : function(){
				}
			});
    	}
    	$('.dropdown-toggle').dropdown();
    	/* var needed in autorefresh calls */
    	com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh = '<c:out value ='${nodeIndex}'/>';
    	/*  Auto refresh for common nodedrilldown page */
    	autoRefreshNodeDrillDownPage();
    });
</script>

<!-- Style for individual node drill down and graphs -->
<style>
td.group {
	padding-left: 90px;
}
td.expanded-group {
	/* background: url("../../images/icon/hadoopIcon.png") no-repeat scroll 0 0 transparent; */
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.expanded-group {
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center #c0e1ff !important;
}
td.collapsed-group {
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.collapsed-group {
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center #c0e1ff !important;
}
</style>
<!-- This page for individual node drill down  -->
<div class="section-header">
	<input id="nodeTileJsonVariable" class="display-none" />
	<div class="row-fluid mrgt20">
		<div class="span8">
			<h2 id="commonNodeDrillDown_NodeIP" class="heading text-left left">
				
			</h2><button class="btn-error" id="errorBtn_NodeDrillDown"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>
		
		<!-- <div class="span4 minhgt0">
			<button class="span3 btn-error" id="errorBtn_NodeDrillDown"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;"></button>
		</div> -->
		
		<c:if test="${clusterTechnology eq 'Cassandra'}">
	   		<div class="span4 text-right">
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#"
						style="height: 25px">Actions<span class="caret"></span></a>
					<ul class="dropdown-menu pull-right" id="nodeLevelActionsDropDown">
					</ul>
				</div>
			</div>
		</c:if>
	</div>
</div>

<div class="section-body common-tooltip" id="div_HadoopNodeDrillDown">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="error-div-NodeDrillDown" class="span12 errorDiv"
				style="display: none;">
				<span id="popover-content_NodeDrillDown" style="color: red;"></span>
			</div>
		</div>
		<div class="row-fluid">
			<div class="masonry mrgt10" id="tilesNodes_NDD"></div>
		</div>
		<!-- This div is for hadoop table common monitoring page -->
		<c:if test="${clusterTechnology eq 'Cassandra'}">
   			<%@ include file="../cassandra/cassandraClusterMonitoring/nodeOverviewTable.jsp"%>
		</c:if>
		<c:if test="${clusterTechnology eq 'Kafka'}">
   			<%@ include file="../kafka/kafkaMonitoring/brokerDrillDown.jsp"%>
   		</c:if>
   		<c:if test="${hybridTechnology eq 'Kafka'}">
   			<%@ include file="../kafka/kafkaMonitoring/brokerDrillDown.jsp"%>
   		</c:if>
   		<c:if test="${clusterTechnology eq 'ElasticSearch'}">
   			<%@ include file="../elasticSearch/elasticSearchMonitoring/tileTables.jsp"%>
   		</c:if>
   		<c:if test="${hybridTechnology eq 'ElasticSearch'}">
   			<%@ include file="../elasticSearch/elasticSearchMonitoring/tileTables.jsp"%>
   		</c:if>
   		<div class="clear"></div>
		<div class="row-fluid" id="common-section">
			<div class="boxToExpand new-box span6">
				<div class="titleExpand">
					<div class="left mrgb5">Utilization Graphs&nbsp;&nbsp;</div>
					
					<div style="min-width:250px; float:left">
					<div class="btn-group"
					id="graphButtonGroup_JobMonitoring" data-toggle="buttons-radio" style="margin-top:-4px;">
					
					<button class="btn" id="btnLastYear_HNDD"
						onclick="com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring('lastyear',<c:out value ='${nodeIndex}'/>);" style="height:20px;">1 y</button>
						<button class="btn" id="btnLastMonth_HNDD"
						onclick="com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring('lastmonth',<c:out value ='${nodeIndex}'/>);" style="height:20px;">1 m</button>
					<button class="btn" id="btnLastWeek_HNDD"
						onclick="com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring('lastweek',<c:out value ='${nodeIndex}'/>);" style="height:20px;">1 w</button>
					<button class="btn" id="btnLastDay_HNDD"
						onclick="com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring('lastday',<c:out value ='${nodeIndex}'/>);" style="height:20px;">1 d</button>
					<button class="btn active" id="btnLastHour_HNDD"
						onclick="com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring('lasthour',<c:out value ='${nodeIndex}'/>);" style="height:20px;">1 h</button>
				</div><span id="topic-drilldown-arrow"><a href="#"
									onclick="com.impetus.ankush.commonMonitoring.nodeUtilizationTrend();">
									&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></span>
									
					</div>				
					<div class="clearfix"></div>				
				</div>
				<div class="clearfix"></div>
				<div class="contentExpand" id="nodeDrillDown_Graph">
					<svg></svg>
				</div>
			</div>
			<div class="boxToExpand span6">
				<div class="titleExpand">
					Services&nbsp;&nbsp;<div class="btn-group">
				<a href="#" data-toggle="dropdown" class="btn dropdown-toggle hgt25"  style="height:20px;margin-top:-4px;">Actions<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="#"
							onclick="com.impetus.ankush.commonMonitoring.serviceAction('startChecked',<c:out value ='${nodeIndex}'/>); " style="height:20px;">Start
								</a></li>
						<li><a href="#"
							onclick="com.impetus.ankush.commonMonitoring.serviceAction('stopChecked',<c:out value ='${nodeIndex}'/>);" style="height:20px;">Stop
								</a></li>
					</ul>
				</div>
				</div>
				<div style="display: block;"class="contentExpand ">
					<div class="row-fluid box infobox masonry-brick">
					<div class="text-left">
				<table class="table table-border0" id="nodeServiceDetail">
					<thead class="text-left">
						<tr>
							<th><input type="checkbox" onclick="com.impetus.ankush.checked_unchecked_all('checkedServices',this,1)" id="servicesHeadCheckbox"></th>
							<th>Service</th>
							<th>Status</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody class="text-left">
					</tbody>
				</table>
			</div>
				</div>
				</div>
			</div>
			</div>
	
	  <div id="div_RequestSuccess_NodeServices" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Service Request</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Service update request placed successfully. This will take some
					time.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn">OK</a>
		</div>
	</div>		
<!-- Services -->		
<%-- <div id="NodeServiceDetailsDiv" class="container-fluid">
		<div class="row-fluid">
			<div class="text-left">
				<label class="form-label section-heading left">Services</label>
			</div>
			<div class="row-fluid span5 text-left">
				<div class="btn-group mrgt10">
				<a href="#" data-toggle="dropdown" class="btn dropdown-toggle hgt25">Actions <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="#"
							onclick="com.impetus.ankush.commonMonitoring.serviceAction('start all',<c:out value ='${nodeIndex}'/>);">Start
								All</a></li>
						<li><a href="#"
							onclick="com.impetus.ankush.commonMonitoring.serviceAction('stop all',<c:out value ='${nodeIndex}'/>);">Stop
								All</a></li>
					</ul>
				</div>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span5 text-left">
				<table class="table table-border" id="nodeServiceDetail">
					<thead class="text-left">
						<tr>
							<th>Service</th>
							<th>Status</th>
						</tr>
					</thead>
					<tbody class="text-left">
					</tbody>
				</table>
			</div>
		</div> --%>
		<c:if test="${clusterTechnology eq 'Cassandra'}">
   			<div class="row-fluid">
			<div>
				<h4 class="section-heading">
					<a href="#"
						onclick="com.impetus.ankush.cassandraMonitoring.loadNodeParameters(<c:out value ='${nodeIndex}'/>);">
						Parameter&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</h4>
			</div>
	</div>
		</c:if>
		
</div>
</div> 
</div>

<!-- Service Request dialog -->
