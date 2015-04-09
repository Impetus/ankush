<!------------------------------------------------------------------------------
-  ===========================================================
-  Ankush : Big Data Cluster Management Solution
-  ===========================================================
-  
-  (C) Copyright 2014, by Impetus Technologies
-  
-  This is free software; you can redistribute it and/or modify it under
-  the terms of the GNU Lesser General Public License (LGPL v3) as
-  published by the Free Software Foundation;
-  
-  This software is distributed in the hope that it will be useful, but
-  WITHOUT ANY WARRANTY; without even the implied warranty of
-  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-  See the GNU Lesser General Public License for more details.
-  
-  You should have received a copy of the GNU Lesser General Public License 
-  along with this software; if not, write to the Free Software Foundation, 
- Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->

<script>
	var hadoopNodeTable = null;
	$(document).ready(
			function() {
				hadoopNodeTable = $('#hadoopNodeTable').dataTable({
					"bJQueryUI" : true,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : true,
					"aaSorting" : [],
					"bInfo" : false,
					"aoColumnDefs" : [ {
						'sType' : "ip-address",
						'aTargets' : [ 0 ]
					}, {
						'bSortable' : false,
						'aTargets' : [ 1, 2, 3, 4 ]
					} ],
				});
				$("#hadoopNodeTable_filter").hide();
				$('#nodeSearchBox').keyup(function() {
					hadoopNodeTable.fnFilter($(this).val());
				});
				com.impetus.ankush.hybridAddNode.mapNodesPopulate(
						"hadoopNodeTable", "Hadoop");
			});
</script>

<!-- header section -->
<!-- <div class="section-header">
	<div class="row headermargin">
		<div class="col-md-7">
			<h2 class="heading text-left left">Hadoop</h2>
			<button class="btn btn-danger" id="validateErrorMapNodeAdd"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;"></button>

		</div>

		
		<div class="span3">
			<button class="span3 btn-error header_errmsg" id="validateErrorMapNodeAdd"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;"></button>
		</div>
		

		<div class="col-md-5 text-right">
			<button id="hadoopNodesRevert"
				class="btn btn-default headerright-setting"
				onclick="com.impetus.ankush.hybridAddNode.nodeMappingCancel('Hadoop');">Cancel</button>
			<button class="btn btn-default" id="hadoopNodesApply"
				onclick="com.impetus.ankush.hybridAddNode.nodeMappingApply('Hadoop');">Apply</button>
		</div>
	</div>
</div> -->
<div class="">
	<div class="">
		<div class="">
			<div class="col-md-4">
				<!-- <h2>Node Mapping</h2> -->
			</div>
			<div class="col-md-1">
				<!-- 	<button class="btn-error header_errmsg" id="validateError"
						onclick="com.impetus.ankush.common.focusError();"
						style="display: none; padding: 0 15px; left: 15px; position: relative"></button> -->
			</div>
			<div class="col-md-7 text-right mrgt20 padr45"></div>
		</div>
	</div>
	<div class="main-content">
		<div class="container-fluid">
			<div class="row">
				<!-- <div id="error-div-mapNodeAddNode" class="col-md-12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content-mapNodeAddNode" style="color: red"></span>
			</div> -->
			</div>
			<div class="panel">
				<div class="panel-heading">
					<div class="">
						<div class="">
							<h3 class="panel-title col-md-3 mrgt5">Node List</h3>
							<button id="hadoopNodesRevert"
								class="btn btn-default headerright-setting"
								onclick="com.impetus.ankush.hybridAddNode.nodeMappingCancel('Hadoop');">Cancel</button>
							<button class="btn btn-default" id="hadoopNodesApply"
								onclick="com.impetus.ankush.hybridAddNode.nodeMappingApply('Hadoop');">Apply</button>
						</div>
						<div class="pull-right panelSearch">
							<input type="text" id="nodeSearchBox" placeholder="Search"
								class="input-medium form-control" />
						</div>
						<div id="error-div-mapNodeAddNode"
							class="mrgt10" style="display: none;">
							<span id="popover-content-mapNodeAddNode" style="color: red"></span>
						</div>
					</div>
				</div>
				<div class="row panel-body">
					<div class="col-md-12 " style="">
						<table class="table table-striped tblborder1" id="hadoopNodeTable">
							<thead class="tblborder2">
								<tr>
									<th>Host Name</th>
									<th>Node Roles</th>
									<th>DataNode</th>
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
	</div>


</div>
