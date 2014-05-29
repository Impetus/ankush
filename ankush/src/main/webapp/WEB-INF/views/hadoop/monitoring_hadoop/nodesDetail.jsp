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
<!-- Hadoop Cluster's Nodes Page containing tiles and table containing cluster's nodes details and a link for deleting a node from the Cluster  -->

<%@include file="../../layout/blankheader.jsp"%>
<script type="text/javascript">
	$(document).ready(
			function() {
				if($('#clusterEnv').text() == 'CLOUD') {
					$('#btnNodeList_DeleteNodes').css('display' ,'none');
					}
				$('#btnNodeList_DeleteNodes').tooltip();
			});
</script>

<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left">Nodes</h2>
		</div>
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtn-hadoopNodesDetail"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;"></button>
		</div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="error-div-hadoopNodesDetail" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content-hadoopNodesDetail"
					style="color: red;"></span>
			</div>
		</div>
		<div class="row-fluid transitions-enabled" style="margin-top: 10px;"
			id="allTilesNodes"></div>

		<div style="margin-top: 10px;">
			<div class="span12 row-fluid">
				<div class="text-left" style="padding-top: 20px; float: left">
					<label class='section-heading text-left'>Node List</label>
				</div>
				<div class="span3 text-left"
					style="margin-left: 15px; padding-top: 15px; padding-right: 48px;">
					<button class="btn disabled" id="btnNodeList_DeleteNodes"
						onclick="" data-toggle="tooltip" data-placement="right"
						title="At least one node should be selected to complete this operation.  NameNode cannot be deleted">Delete
						Nodes</button>
				</div>
				<div class="text-right">
					<input id="searchNodeDetailTableHadoop" type="text"
						placeholder="Search">
				</div>
			</div>
		</div>
		<div id="nodesDetail">
			<div class="span12 row-fluid">
				<table class="table table-striped" width="100%"
					id="nodesDetailTable"
					style="border: 1px solid; border-color: #CCCCCC">
					<thead style="text-align: left;">
						<tr>
							<th><input type='checkbox' id='checkHead_NodeList'
								name="checkHead_NodeList"
								onclick="com.impetus.ankush.hadoopMonitoring.checkAll_NodeList(this)"></th>
							<th>IP</th>
							<th>Type</th>
							<th>State</th>
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
	<div id="deleteNodeDialogHadoop" class="modal hide fade"
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
		<a href="#" data-dismiss="modal" class="btn">Cancel</a>
			<a href="#"
				onclick="com.impetus.ankush.hadoopMonitoring.deleteNodeHadoop();"
				class="btn">Delete</a> 
		</div>
	</div>
	<div id="div_RequestSuccess_NodeDetails" class="box-dialog"
		style="display: none;">
		<div class="row-fluid">
			<div class="span12">
				<h4 style="text-align: center; color: black">Delete Node
					Request</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="text-align: left;">Delete node
				request placed successfully.</div>
		</div>
		<br>
		<div class="row-fluid text-right">
			<button class="btn  span2 offset10" id="divOkbtn_NodeDetails"
				onclick="com.impetus.ankush.hadoopMonitoring.closeSuccessDialog_ND();">OK</button>
		</div>
	</div>
	<div id="div_RequestInvalid_NodeDetails" class="box-dialog"
		style="display: none;">
		<div class="row-fluid">
			<div class="span12">
				<h4 style="text-align: center; color: black">Invalid Request</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="text-align: left;">
				<span id="invalidRequestMsg_ND" class="text-left"
					style="margin-top: 10px; font-size: 12px;"></span>
			</div>
		</div>
		<br>
		<div class="row-fluid text-right">
			<button class="btn  span2 offset10"
				id="divOkbtn_ReqInvalid_NodeDetails"
				onclick="com.impetus.ankush.hadoopMonitoring.closeReqInvalidDialog_ND();">OK</button>
		</div>
	</div>
</div>
