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

<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript"></script>
<script>
$(document).ready(function(){
	var clusterId = "<c:out value='${clusterId}'/>";
 	$('#hadoopAddNodesIPRange').empty();
 	$('#hadoopAddNodesFilePath').empty();
 	$('#ipRangeHadoop').tooltip();
 	$('#filePath_IPAddressFile').tooltip();
 	com.impetus.ankush.hadoopMonitoring.initTables_addNodes();
});
</script>
<div class="section-header">
<div  class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Add Nodes</h2>
			<button class="btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
			
		</div>
		
		<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
			</div>
			-->
			
 		<div class="span5 text-right">
			<button id="commonDeleteButtonHadoop" class="btn mrgr10" onclick="com.impetus.ankush.removeChild(2);">Cancel</button>
		  	<button class="btn" id="addNode"
		  	 onclick="com.impetus.ankush.hadoopMonitoring.addNodeSetup(<c:out value='${clusterId}'/>)">Add</button>
		 </div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="error-div-hadoop" class="span12 error-div-hadoop"
				style="display: none;">
		<span id="popover-content" style="color: red"></span>
	</div>
		</div>
		<div class="row-fluid">
			<div class="span3 text-left">
				<label class="form-label section-heading">Search
					and Select Nodes</label>
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
		
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">IP Address:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="ipModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn nodeListRadio active btnGrp" data-value="Range"
						id="ipRange"
						onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPRange');">Range</button>
					<button class="btn nodeListRadio btnGrp" data-value="Upload File" id="ipFile"
						onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPFileUpload');">Upload
						File</button>
				</div>
			</div>
		</div>
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
					style="float:left;"></iframe>
				<form action="" id="uploadframeIPAddressFile_Form"
					target="uploadframeIPAddressFile" enctype="multipart/form-data"
					method="post" style="float: left; margin: 0px;">
					<input style='visibility: hidden;float:right;' id='fileBrowse_IPAddressFile'
						type='file' class='' name='file'></input><input type="text"
						id="filePath_IPAddressFile" data-toggle="tooltip"
						placeholder="Upload File" title="File with IP Address List"
						data-placement="right" readonly="readonly"
						style="cursor: pointer;float:left;background:white"
						onclick="com.impetus.ankush.hadoopMonitoring.hadoopIPAddressFileUpload();" class="input-large"></input>
				</form>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Rack Mapping: </label>
			</div>
			<div class="span10 text-left">
			<div id="divRackMapping" class="input-prepend">
				<span class="add-on" style="float:left;height:28px;margin-top:4px">
					<input type="checkbox"  class="inputSelect" id="chkBxRackEnable"
					onclick="com.impetus.ankush.hadoopMonitoring.toggleRackMapping();">
				</span>
				<iframe style="width: 1px; height: 0px; border: 0px;"
					id="uploadframeRackFile" name="uploadframeRackFile"
					style="float:left;"></iframe>
				<form action="" id="uploadframeRackFile_Form"
					target="uploadframeRackFile" enctype="multipart/form-data"
					method="post" style="margin: 0px;">
				<input id='fileBrowse_RackFile' type='file' style="visibility : hidden;float:right"
					class='' name='file'></input><input type="text" id="filePath_RackFile" disabled="disabled"
					data-toggle="tooltip" placeholder="Upload File" title="File with Rack & IP Address Mapping"
					data-placement="right" readonly="readonly" style="cursor:default;"
					onclick="com.impetus.ankush.hadoopMonitoring.hadoopRackFileUpload();"></input>		
				</form>
			</div>
        	
			</div>
		</div>
		<div class="row-fluid">
		
		<div class="span2">&nbsp;			</div>
			<div class="text-left">
				<button class="btn" id="retrieve" style="margin-right: 123px; margin-left:8px;"
					onclick="com.impetus.ankush.hadoopMonitoring.getNewlyAddedNodes(<c:out value='${clusterId}'/>)">Retrieve</button>
			</div>
		</div>
	<div id="addNodeTable">
		<div class="row-fluid">
		<div class="span12 row-fluid mrgl0">
			<div class="text-left span3" style="padding-top: 20px;float:left">
				<label class='section-heading text-left left'>Node List</label>
				<button type="button" class="btn left mrgl10 mrgtn5" data-loading-text="Inspecting Nodes..." onclick="com.impetus.ankush.hadoopMonitoring.inspectNodesObject('inspectNodeBtn');" id="inspectNodeBtn">Inspect Nodes</button>
			</div>
			<div class="span6 form-image text-right btn-group" id="HadoopCreate_NodeIPTable" data-toggle="buttons-radio">
    			<button type="button" id="btnAll_HSN" class="btn active" onclick="com.impetus.ankush.hadoopMonitoring.toggleDatatable('All');">All</button>
    			<button type="button" id="btnSelected_HSN" class="btn" onclick="com.impetus.ankush.hadoopMonitoring.toggleDatatable('Selected');">Selected</button>
    			<button type="button" id="btnAvailable_HSN" class="btn" onclick="com.impetus.ankush.hadoopMonitoring.toggleDatatable('Available');">Available</button>
    			<button type="button" id="btnError_HSN" class="btn" onclick="com.impetus.ankush.hadoopMonitoring.toggleDatatable('Error');">Error</button>
    		</div>
			<div class="text-right">
						<input id="searchAddNodeTableHadoop" type="text"
							placeholder="Search">
			</div>
		</div>
		<div class="row-fluid mrgl0">
			<div class="span12 text-left" style="margin-left:0">
				<table class="table table-border table-striped" id="addNodeIpTableHadoop">
					<thead class="text-center">
						<tr>
							<th><input type='checkbox' id='addNodeCheckHead'
								name="addNodeCheckHead"
								onclick="com.impetus.ankush.hadoopMonitoring.checkAllNode(this)"></th>
								<th>IP</th>
<!-- 								<th>Sec Name Node</th> -->
								<th>Data Node</th>
								<th>DataCenter</th>
								<th>Rack</th>
								<th>OS</th>
								<th></th>
						</tr>
					</thead>
					<tbody style="text-align: left;">
					</tbody>
				</table>
			</div>
		</div>
		</div>
	 </div>
	</div>
</div>
