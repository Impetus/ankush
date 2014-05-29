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
<!-- Hadoop Add Nodes Page containing field for IP Address of node(s) to be added and a table showing availability of node after node retrieval  -->

<%@ include file="../layout/blankheader.jsp"%>
<script>
function autoRefreshAddNodesPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = ''; 
	obj1.callFunction = "";
	obj1.time = 0;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function(){
	$('#addNodesIPRange').empty();
 	$('#addNodesFilePath').empty();
 	$('#ipRangeHadoop').tooltip();
 	$('#filePath_IPAddressFile').tooltip();
 	addNodeCommonClusterTable = $("#addNodeIpTableHadoop").dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : false,
		"bFilter" : true,
		"bSort" : false,
		"bInfo" : false,
		"aoColumnDefs": [
		               //  { 'bSortable': false, 'aTargets': [ 0,2,3,5 ] },
		                // { 'sType': "ip-address", 'aTargets': [1] }
		                 ], 
	});
 	//com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
	$('#addNodeIpTableHadoop_filter').hide();
	$('#searchAddNodeTableHadoop').keyup(function() {
		$("#addNodeIpTableHadoop").dataTable().fnFilter($(this).val());
	});

	$("#addNodeIpTableHadoop_filter").css({
		'display' : 'none'
	});
	autoRefreshAddNodesPage();
});
</script>
<div class="section-header">
<div  class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Add Nodes</h2>
			<button class="btn-error" id="errorBtnCommonAddNode"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important; position:relative; padding:0 15px; left:15px"></button>
		</div>
	 
		<!-- <div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnCommonAddNode"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
			</div> -->
			
 		<div class="span5 text-right">
			<button id="commonDeleteButtonHadoop" class="btn mrgr10" onclick="com.impetus.ankush.removeChild(2);">Cancel</button>
		  	<button class="btn" id="addNode"
		  	 onclick="com.impetus.ankush.commonMonitoring.addNodeSetup()">Add</button>
		 </div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="error-div-commonAddNode" class="span12 error-div-hadoop"
				style="display: none;">
		<span id="popover-content-commonAddNode" style="color: red"></span>
	</div>
		</div>
		<div class="row-fluid">
			<div class="span3 text-left">
				<label class="form-label section-heading">Search
					and Select Nodes</label>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Mode :</label>
			</div>
			<div class="span10 ">
				<div class="btn-group mrgt8 mrgb15" data-toggle="buttons-radio"
					id="addNodeGroupBtn">
					<button class="btn ipAddress active btnGrp" data-value="Range"
						id="ipRange" name="ipAddress" 
						onclick="com.impetus.ankush.commonMonitoring.divShowOnClickIPAddress('div_IPRange');">IP
						Range</button>
					<button class="btn ipAddress btnGrp" data-value="Upload File"
						id="ipFile" name="ipAddress"
						onclick="com.impetus.ankush.commonMonitoring.divShowOnClickIPAddress('div_IPFileUpload');">File
						Upload</button>
				</div>
			</div>
		</div>
		
		
		
		
		
		
		<!-- <div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">IP Address:</label>
			</div>
			<div class="span10 text-left">
			<label class="radio inline text-left" style="padding-top: 0px; margin-top:5px">
					<input type="radio" name="ipAddress" id="ipRange" value="Range"
					checked="checked" style="vertical-align: middle; float: none;"
				onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPRange');"/><span>&nbsp;&nbsp;Range&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
				</label> <label class="radio inline text-left"
					style="padding-top: 0px; margin-left: 20px; margin-top:5px"> <input
					type="radio" name="ipAddress" id="ipFile" value="Upload File"
					style="vertical-align: middle; float: none;"
					onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPFileUpload');" /><span>&nbsp;&nbsp;Upload
						File</span>
			</label>
			</div>
		</div> -->
		<div class="row-fluid" id="div_IPRange">
			<div class="span2">
				<label class="text-right form-label">IP Range:</label>
			</div>
			<div class="span10">
				<input type="text" class="input-large" id="ipRangeHadoop"
					placeholder="IP Range" value="" data-toggle="tooltip"
					data-placement="right"
					title="E.g 192.168.145.10-21 192.168.41.1,10"></input>
			</div>
		</div>
		<div class="row-fluid" id="div_IPFileUpload" style="display: none;">
			<div class="span2">
				<label class="text-right form-label">File Upload: </label>
			</div>
			<div class="span10">
				<iframe style="width:1px;height: 1px; border: 0px;"
					id="uploadframeIPAddressFile" name="uploadframeIPAddressFile"
					class="left"></iframe>
				<form action="" id="uploadframeIPAddressFile_Form"
					target="uploadframeIPAddressFile" enctype="multipart/form-data"
					method="post" class="left mrg0">
					<input style='visibility: hidden;float:right;' id='fileBrowse_IPAddressFile'
						type='file' class='' name='file'></input><input type="text"
						id="filePath_IPAddressFile" data-toggle="tooltip"
						placeholder="Upload File" title="File with IP Address List"
						data-placement="right" readonly="readonly"
						style="cursor: pointer;float:left;"
						onclick="com.impetus.ankush.commonMonitoring.hadoopIPAddressFileUpload();" class="input-large"></input>
				</form>
			</div>
		</div>
		<div class="row-fluid">
		<div class="span2">&nbsp;</div>
		
			<div class="span10 text-left">
				<button class="btn" id="retrieve"
					onclick="com.impetus.ankush.commonMonitoring.getNewlyAddedNodes()">Retrieve</button>
			</div>
		</div>
	<div id="addNodeTable">
		<div class="row-fluid">
		<div class="span12 row-fluid mrgl0">
			<div class="text-left span3 padt20 left">
				<label class='section-heading text-left left'>Node List</label>
				<button type="button" class="btn left mrgl10 mrgtn5" id="inspectNodeBtn" data-loading-text="Inspecting Nodes..." onclick="com.impetus.ankush.commonMonitoring.inspectNodesObject('inspectNodeBtn');">Inspect Nodes</button>
			</div>
			<div class="text-left span2  padt20 left">
				<label class='text-left' id="retrieveStatus"></label>
			</div>
			<div class="span5 form-image text-right btn-group" id="HadoopCreate_NodeIPTable" data-toggle="buttons-radio">
    			<button type="button" id="btnAll_HSN" class="btn active" onclick="com.impetus.ankush.commonMonitoring.toggleDatatable('All');">All</button>
    			<button type="button" id="btnSelected_HSN" class="btn" onclick="com.impetus.ankush.commonMonitoring.toggleDatatable('Selected');">Selected</button>
    			<button type="button" id="btnAvailable_HSN" class="btn" onclick="com.impetus.ankush.commonMonitoring.toggleDatatable('Available');">Available</button>
    			<button type="button" id="btnError_HSN" class="btn" onclick="com.impetus.ankush.commonMonitoring.toggleDatatable('Error');">Error</button>
    		</div>
			<div class="text-right">
						<input id="searchAddNodeTableHadoop" type="text"
							placeholder="Search">
			</div>
		</div>
		<div class="row-fluid mrgl0">
			<div class="span12 text-left mrgl0">
				<table class="table table-border table-striped" id="addNodeIpTableHadoop">
					<thead class="text-center">
						<tr>
							<th><input type='checkbox' id='addNodeCheckHead'
								name="addNodeCheckHead"
								onclick="com.impetus.ankush.checked_unchecked_all('checkBoxAddNode',this)"></th>
								<th>IP</th>
								<th>Type</th>
								<th>OS</th>
								<th></th>
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
</div>
