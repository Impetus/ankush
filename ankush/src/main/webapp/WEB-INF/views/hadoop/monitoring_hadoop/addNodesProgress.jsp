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
<!-- Add Nodes Progress Page containing information, viz. IP information of nodes added and table showing node addition progress details  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script>
	var clusterId = '<c:out value="${clusterId}"/>';
	com.impetus.ankush.hadoopMonitoring.setCurrentClusterId(clusterId);
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Add Nodes</h2>
		</div>
		<div class="span3">
			<div style="width: 180px; display: block"
				id="div_status_AddNodeProgress" class="text-center">
				<span id="status_AddNodeProgress" class="label">Node
					Addition successful</span>
			</div>
			<button id="errorBtnHadoop" class="span3 btn-error"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		<div class="span5 text-right" style="display: none;">
			<button id="commonDeleteButton" class="btn"
				style="margin-right: 8px;"
				onclick="com.impetus.ankush.removeChild(2);">Cancel</button>
			<button class="btn" style="margin-right: 100px;"
				onclick="com.impetus.ankush.hadoopMonitoring.stopNodeAddDialog();">Stop</button>
		</div>
	</div>
</div>
<div class="section-body">
	<div id="addNodeStop" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Stop Operation</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Node Add is in progress.<br>Stopping will halt the current
					operation.
				</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" id="confirmStopButton"
				onclick="com.impetus.ankush.hadoopMonitoring.deleteClusterHadoop();"
				class="btn">Stop</a> <a href="#" data-dismiss="modal" class="btn">Cancel</a>
		</div>
	</div>
	<div class="container-fluid">

		<div class="row-fluid">
			<div id="error-div-hadoop" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content" style="color: red;"></span>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12 ">
				<h4 class="section-heading" style="text-align: left;">Search
					and Select Nodes</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">IP Address:</label>
			</div>
			<div class="span10 text-left radioDiv">
				
					<input type="radio" name="ipAddress" id="ipRange" value="Range"
						checked="checked"
						onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPRange');" /><span>&nbsp;&nbsp;Range</span>
				
					<input type="radio" name="ipAddress" id="ipFile"
						value="Upload File"
						onclick="com.impetus.ankush.hadoopMonitoring.divShowOnClickIPAddress('div_IPFileUpload');" style="margin-left:20px;"/><span>&nbsp;&nbsp;Upload
						File</span>
				
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
				<iframe style="width: 1px; height: 1px; border: 0px;"
					id="uploadframeIPAddressFile" name="uploadframeIPAddressFile"
					style="float:left;"></iframe>
				<form action="" id="uploadframeIPAddressFile_Form"
					target="uploadframeIPAddressFile" enctype="multipart/form-data"
					method="post" style="float: left; margin: 0px;">
					<input style='display: none;' id='fileBrowse_IPAddressFile'
						type='file' class='' name='file'></input><input type="text"
						id="filePath_IPAddressFile" data-toggle="tooltip"
						placeholder="Upload File" title="File with IP Address List"
						data-placement="right"
						onclick="com.impetus.ankush.hadoopMonitoring.hadoopIPAddressFileUpload();"></input>
				</form>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2"></div>
			<div class="span10">
				<button class="btn" id="retrieveAddNodeButton"
					onclick="com.impetus.ankush.hadoopMonitoring.getNewlyAddedNodes(<c:out value='${clusterId}'/>)"
					style="color: #848584" disabled="disabled">Retrieve</button>
			</div>
		</div>
		<div id="addNodeTable">
			<div class="row-fluid">

				<div class="span12 row-fluid">
					<div class="text-left span2 " style="padding-top: 20px; float: left">
						<label class='section-heading text-left'>Node List</label>
					</div>
					<div class="text-left span3" style="padding-top: 20px; float: left">
						<label class='text-left' id="retrieveStatus"></label>
					</div>
					<div class="span5 form-image text-right btn-group"
						id="HadoopCreate_NodeIPTable" data-toggle="buttons-radio"
						style="display: none">
						<button type="button" class="btn active"
							onclick="com.impetus.ankush.hadoopMonitoring.toggleDatatable_AddProgress('All');">All</button>
						<button type="button" class="btn"
							onclick="com.impetus.ankush.hadoopMonitoring.toggleDatatable_AddProgress('Error');">Error</button>
					</div>
					<div class="text-right">
						<input id="searchAddNodeTableHadoop" type="text"
							placeholder="Search" style="float: right;">
					</div>
				</div>
				<div class="row-fluid">
					<div class="span11 offset1 text-left">
						<table class="table" id="addNodeProgressTable" width="100%"
							style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
							<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
								<tr>
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
	</div>
</div>
