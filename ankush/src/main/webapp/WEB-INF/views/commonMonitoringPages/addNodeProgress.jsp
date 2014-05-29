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
<!-- Page for showing node addition progress -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
function addNodeProgressAutorefresh(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autoRefresh_nodeTilesAndDetails'; 
	obj1.callFunction = "com.impetus.ankush.commonMonitoring.addNodeProgress();";
	obj1.time = 30000;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	
	var nodeIpProgressTable = null;
	$(document).ready(function(){
		nodeIpProgressTable = $("#nodeIpProgressTable").dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : true,
			"bInfo" : false,
			"bAutoWidth" : false,
			"aoColumnDefs": [
			                 { 'sType': "ip-address", 'aTargets': [1] },
			           			{ 'bSortable': false, 'aTargets': [ 0,2,3,4 ] }
			               ],
		});
		$("#nodeIpProgressTable_filter").css({
			'text-align' : 'right'
		});
		$("#nodeIpProgressTable_filter").prepend('<div style="float:left;margin-top:15px;" id="nodeIpProgressDatatable"></div><div style="float:left;margin-top:15px;display:none" id="nodeDeployCount"></div>');	
	$('#addNodeStatusLabel').text('Node Deployment in progress'); 
	addNodeProgressAutorefresh();
	});	
</script>
<body style="background: none;">
	<div class="section-header">
		<div class="row-fluid mrgt20">
			<div class="span4">
				<h2 class="heading text-left">Add Nodes</h2>
			</div>
			<!-- <div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnAddnodeProgress"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
			</div> -->
			<div class="span3 header-align" style="margin-top:0px;">
			<span class="label" id="addNodeStatusLabel"></span>
		</div>
		</div>
	</div>
	<div class="section-body">
		<div class="container-fluid mrgnlft8">
		<!-- <div class="row-fluid">
			<div id="error-div-addNodeProgress" class="span12 error-div-hadoop"
				style="display: none;">
		<span id="popover-content-addNodeProgress" style="color: red"></span>
	</div>
		</div> -->
			<div id="addNodeStop" class="box-dialog" style="display: none;">
				<div class="row-fluid">
					<div class="span12">
						<h4 style="text-align: center; color: black">Stop Operation</h4>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span12" style="text-align: left;">
						Node Add is in progress.<br>Stopping will halt the current
						operation.
					</div>
				</div>
				<br>
				<div class="row-fluid text-right">
					<button class="btn  span2 offset8" id="confirmStopButton"
						onclick="com.impetus.ankush.oClusterMonitoring.stopNodeAdd();">Stop</button>
					<button class="btn span2" id="cancelDeleteButton">	</button>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12 ">
					<h4 class="section-heading" style="text-align: left;">Search
						and Select Nodes</h4>
				</div>
			</div>
			<div class="row-fluid" id="ipRangeAddNodeProgress">
				<div class="span2">
					<label class=" text-right form-label">IP Range:</label>
				</div>
				<div class="span10">
					<input id="ipRangeAddNode" type="text" class=""
						style="width: 300px; cursor: default;"
						placeholder="IP Range" data-toggle="tooltip"
						data-placement="right" disabled="disabled" ></input>
				</div>
			</div>
			<div class="row-fluid" id="filePathAddNOdeProgress">
				<div class="span2">
					<label class=" text-right form-label">File Path:</label>
				</div>
				<div class="span10">
					<iframe style="width: 1px; height: 1px; border: 0px;"
						id="uploadframesharekey" name="uploadframesharekey"
						style="float:left;"></iframe>
					<form action="" id="uploadFormShareKeyAddnode"
						target="uploadframesharekey" enctype="multipart/form-data"
						method="post" style="float: left; height:32px">
						<input style='display: none;' id='fileBrowse' type='file' class=''
							name='file'></input><input type="text" id="filePath" style="cursor: default;"
							data-toggle="tooltip" placeholder="Upload File"
							data-placement="right"
							disabled="disabled"></input>
					</form>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2"></div>
				<div class="span10">
					<button class="btn btn-large " id="retrieveAddNodeButton"
						onclick="com.impetus.ankush.oClusterMonitoring.validateNodes();"
						style="color: #848584" disabled="disabled">Retrieve</button>
				</div>
			</div>
			<br />
			<div class="row-fluid">
				<div class="span12">
					<table class="table table-striped" id="nodeIpProgressTable"
						style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
						<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
							<tr>
								<th></th>
								<th>IP</th>
								<th>Type</th>
								<th>Status</th>
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
</body>
