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
<!-- Page for node addition in cluster -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	$(document).ready(function() {
		var clusterId = null;
		$.fn.editable.defaults.mode = 'inline';

		$("#try").tooltip();
		clusterId = '<c:out value="${clusterId}"/>';
		//	com.impetus.ankush.oClusterMonitoring.getDefaultValue();
		//			com.impetus.ankush.oClusterSetup.editableTable();
		//		com.impetus.ankush.oClusterSetup.tooltip();
		oClusterAddNodeTable = $("#addNodeIpTable").dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : true,
			"bInfo" : false,
			"aaSorting" : [ [ 1, "asc" ] ],
			"aoColumnDefs" : [ {
				'sType' : "ip-address",
				'aTargets' : [ 1 ]
			}, {
				'bSortable' : false,
				'aTargets' : [ 0, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]
			} ],
		});
		$("#addNodeIpTable_filter").css({
			'text-align' : 'right'
		});
		$("#addNodeIpTable_filter").hide();
		$('#nodeSearch').keyup(function() {
			oClusterAddNodeTable.fnFilter($(this).val());
		});

		/* $("#addNodeIpTable_filter").prepend(
						'<div style="float:left;margin-top:15px;" id="nodeListOracleDatatable"></div><div style="float:left;margin:28px;font-size:14px;color:black;display:none;" id="nodeStatus"></div><div class="span2" style="margin-top:15px;"><button id="inspectNodeBtn" type="button" data-loading-text="Inspecting Nodes..." class="btn" onclick="com.impetus.ankush.oClusterMonitoring.inspectNodesObject(\''+inspectNode+'\');">Inspect Nodes</button></div><div class="form-image text-right btn-group span5" id="" data-toggle="buttons-radio" style="float:left;margin-left: 10px;"><button type="button" class="btn active" onclick="com.impetus.ankush.oClusterMonitoring.toggleDatatable(\''
								+ all
								+ '\');">All</button><button type="button" class="btn" onclick="com.impetus.ankush.oClusterMonitoring.toggleDatatable(\''
								+ selected
								+ '\');">Selected</button><button type="button" class="btn" onclick="com.impetus.ankush.oClusterMonitoring.toggleDatatable(\''
								+ available
								+ '\');">Available</button><button type="button" class="btn" onclick="com.impetus.ankush.oClusterMonitoring.toggleDatatable(\''
								+ error
								+ '\');">Error</button></div>');
		$("#nodeListOracleDatatable").html('').append(
				'<h4 class="section-heading">Node List</h4>'); */
		//	com.impetus.ankush.oClusterMonitoring.toggleButtonInitialize();
	});
</script>

<body style="background: none;">
	<div class="section-header">
		<div class="row-fluid mrgt20">
			<div class="span5">
				<h2 class="heading text-left left">Add Nodes</h2>
				<button class="btn-error" id="validateError"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important; padding: 0 15px; left: 15px; position: relative"
					onclick="com.impetus.ankush.oClusterMonitoring.focusError();">2
					Errors</button>

			</div>

			<!--  
			<div class="span3">
				<button class="span3 btn-error" id="validateError"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"
					onclick="com.impetus.ankush.oClusterMonitoring.focusError();">2
					Errors</button>
			</div>
			-->

			<div class="span2 text-right offset5">
				<button class="btn"
					onclick="com.impetus.ankush.oClusterMonitoring.addNodeValidate();">Add</button>
			</div>
		</div>
	</div>
	<div class="section-body">

		<div class="container-fluid mrgnlft8">
			<div id="errorDivMainAddNode" class="commonErrorDiv error-div-hadoop"
				style="display: none;"></div>
			<div class="row-fluid">
				<div class="span12 ">
					<h4 class="section-heading" style="text-align: left;">Search
						and Select Nodes</h4>
				</div>
			</div>
			<!-- <div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Mode</label>
				</div>
				<div class="span10 text-left radioDiv">
					
						<input type="radio" name="nodeSearchType" id="throughPassword"
							value="0" checked="checked"
							onclick="com.impetus.ankush.oClusterMonitoring.toggleAuthenticate();" style="margin-top:-2px" /><span>&nbsp;&nbsp;IP
							Range</span>
					
						<input type="radio" id="throughSharedKey" value="1"
							name="nodeSearchType"
							onclick="com.impetus.ankush.oClusterMonitoring.toggleAuthenticate();"  style="margin-left:20px; margin-top:-2px"/><span>&nbsp;&nbsp;File
							Upload</span>
				
				</div>
			</div> -->
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Mode :</label>
				</div>
				<div class="span10 ">
					<div class="btn-group" data-toggle="buttons-radio"
						id="ipModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
						<button class="btn nodeListRadio active btnGrp" data-value="0"
							id="throughPassword"
							onclick="com.impetus.ankush.oClusterMonitoring.toggleAuthenticate();">IP
							Range</button>
						<button class="btn nodeListRadio btnGrp" data-value="1"
							id="throughSharedKey"
							onclick="com.impetus.ankush.oClusterMonitoring.toggleAuthenticate();">File
							Upload</button>
					</div>
				</div>
			</div>
			<div class="row-fluid" id="ipRangeDiv">
				<div class="span2">
					<label class=" text-right form-label">IP Range:</label>
				</div>
				<div class="span10">
					<input id="ipRangeAddNode" type="text" class=""
						style="width: 300px;" placeholder="IP Range" data-toggle="tooltip"
						title="Key in the list of nodes:Some special characters are used to simplify node list entry, they are:'-' Used to give IP range(e.g. 192.168.100-101.10-50). ';' Used to separate multiple patterns (e.g. 192.168.10.2,5;kvstore-test)',' Used to separate multiple octant values in IP address(e.g. 192.168.100,105.10,20). '/' Used for CDIR(Classless Inter-Domain Routing).
						It will take some time to detect nodes matching given criteria & once detected it will populate the grid below with detected nodes"
						data-placement="right"></input>
				</div>
			</div>
			<div class="row-fluid" id="fileUploadDiv" style="display: none">
				<div class="span2">
					<label class=" text-right form-label">File Path:</label>
				</div>
				<div class="span10">
					<iframe style="width: 1px; height: 1px; border: 0px;"
						id="uploadframesharekey" name="uploadframesharekey"
						style="float:left;"></iframe>
					<form action="" id="uploadFormShareKeyAddnode"
						target="uploadframesharekey" enctype="multipart/form-data"
						method="post" style="float: left; height: 32px;">
						<input style='visibility: hidden;' id='fileBrowseAddNode'
							type='file' class='' name='file'></input><input type="text"
							id="filePathAddNode" readonly="readonly"
							style="cursor: default; float: left;" data-toggle="tooltip"
							placeholder="Upload File"
							title=":Please upload plain text file having list of nodes: The file should contain IP address or host names or supported node detection patterns one on each line.
							It will takesome time to detect nodes matching given criteria & once detected it will populate the grid below with detected nodes"
							data-placement="right"
							onclick="com.impetus.ankush.oClusterMonitoring.getNodesUpload();"></input>
					</form>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2"></div>
				<div class="span10">
					<button class="btn  " id="retrieveAddNodeButton"
						data-loading-text="Retrieving..." style="color: #333333;"
						onclick="com.impetus.ankush.oClusterMonitoring.validateNodes();">Retrieve</button>
				</div>
			</div>
			<br />
			<div class="row-fluid" id="addNodeTableDiv" style="display: none;">
				<div class="row-fluid">
					<div class="span4">
						<label class="node-dt left">Node List</label>
						<button type="button" class="btn left mrgl10 mrgt12"
							id="inspectNodeBtn" data-loading-text="Inspecting Nodes..."
							onclick="com.impetus.ankush.oClusterMonitoring.inspectNodesObject('inspectNodeBtn');">Inspect
							Nodes</button>
					</div>
					<div class=" span6 form-image text-right btn-group"
						id="toggleSelectButton" data-toggle="buttons-radio"
						style="float: left; margin-top: 10px;">
						<button type="button" class="btn active" id="nodeAll"
							name="nodeSelectBtn" data-value="all"
							onclick="com.impetus.ankush.common.toggleDatatable('All');">All</button>
						<button type="button" class="btn" id="nodeSelected"
							name="nodeSelectBtn" data-value="selected"
							onclick="com.impetus.ankush.common.toggleDatatable('Selected');">Selected</button>
						<button type="button" class="btn" id="nodeAvailable"
							name="nodeSelectBtn" data-value="availabel"
							onclick="com.impetus.ankush.common.toggleDatatable('Available');">Available</button>
						<button type="button" class="btn" id="nodeError"
							name="nodeSelectBtn" data-value="error"
							onclick="com.impetus.ankush.common.toggleDatatable('Error');">Error</button>
					</div>
					<div class="span2 text-right">
						<input type="text" id="nodeSearch" style="margin-top: 0"
							class="input-medium" placeholder="Search" />
					</div>
				</div>




				<div class=" " style="">
					<table class="table table-striped" id="addNodeIpTable"
						style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
						<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
							<tr>
								<th><input type='checkbox' id='nodeCheckHead'
									name="nodeCheckHead"
									onclick="com.impetus.ankush.oClusterMonitoring.checkAllNodeMonitor(this)"></th>
								<th style="width: 70px;">IP</th>
								<th>Admin</th>
								<th>Storage Dirs</th>
								<th>Capacity</th>
								<th>CPUs</th>
								<th>Memory</th>
								<th>Registry Port</th>
								<th style="width: 40px;">HA Port Start</th>
								<th style="width: 35px;">HA Port End</th>
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
</body>
