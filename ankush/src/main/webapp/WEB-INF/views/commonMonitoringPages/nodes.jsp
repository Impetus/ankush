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
<%@include file="../layout/blankheader.jsp"%>
<script type="text/javascript">
function autoRefreshNodesPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autoRefresh_nodeTilesAndDetails'; 
	obj1.callFunction = "com.impetus.ankush.commonMonitoring.nodeDetails();";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	$(document).ready(
			function() {
				if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Kafka')
					com.impetus.ankush.kafkaMonitoring.removeKafkaMonitoringPageAutorefresh();
				com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
				nodeDetailsTable = $('#nodesDetailTable').dataTable(
						{
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"bAutoWidth" : false,
							"sPaginationType" : "full_numbers",
							"bAutoWidth" : false,
							"aoColumnDefs": [
							                 { 'bSortable': false, 'aTargets': [ 0,5,8 ] },
							                 { 'sType': "ip-address", 'aTargets': [1] }
							                 ], 
							                 'fnRowCallback' : function(nRow, aData, iDisplayIndex,
							                		 iDisplayIndexFull) {
							                	 $(nRow).attr('id', 'rowIdNode-' + iDisplayIndex);
							                 }
						});

				/* this function will fill node details page tiles and datatables */
				com.impetus.ankush.commonMonitoring.nodeDetails();
				if($('#commonMonitoringClusterEnv').text() == 'CLOUD') {
					$('#btnNodeList_DeleteNodes').css('display' ,'none');
					}
				$('#btnNodeList_DeleteNodes').tooltip();
				/*  Auto refresh for common nodes page */
				autoRefreshNodesPage();
				/*  Auto refresh for common nodes page */
			});
</script>
<!-- This page will show details of nodes in datatables and tiles -->

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Nodes</h2>
			<button class="btn-error" id="errorBtn-hadoopNodesDetail"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>
		
		<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtn-hadoopNodesDetail"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;"></button>
		</div>
		-->
	</div>
</div>
<div class="section-body common-tooltip">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="error-div-hadoopNodesDetail" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content-hadoopNodesDetail"
					style="color: red;"></span>
			</div>
		</div>

		<div class="row-fluid">
			<div class="masonry mrgt10" id="tilesNodesPage"></div>
		</div>

		<div>
			<div class="row-fluid">
				<div class="text-left padt20 left">
					<label class='section-heading text-left'>Node List</label>
				</div>
				<div class="span2 text-left mrgl15 padt15">
					<button class="btn disabled" id="btnNodeList_DeleteNodes"
						onclick="" data-toggle="tooltip" data-placement="right"
						title="At least one node should be selected to complete this operation.">Delete</button>
				</div>
				<div class="text-right">
					<input id="searchNodeDetailTable" type="text"
						placeholder="Search">
				</div>
			</div>
		</div>
		<div id="deleteNodeDialogCommon" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Node Delete</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;">
						Selected nodes will be permanently deleted. Once deleted, data
						cannot be retrieved.</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					onclick="com.impetus.ankush.commonMonitoring.deleteNodeCommon();"
					class="btn">Delete</a>
			</div>
		</div>

		<div id="nodesDetail">
			<div class="row-fluid">
				<table class="table table-striped table-border" width="100%"
					id="nodesDetailTable">
					<thead>
						<tr>
							<th><input type='checkbox' id='nodeListCommonTech'
								name="checkHead_NodeList"
								onclick="com.impetus.ankush.commonMonitoring.checked_unchecked_all('checkboxoption',this)"></th>
							<th>IP</th>
							<th>Type</th>
							<th>State</th>
							<th>CPU Usage</th>
							<th>Total Memory</th>
							<th>Used Memory</th>
							<th>OS</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
</div>
