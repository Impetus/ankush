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
	$(document).ready(function() {
		zookeeperNodeTable = $("#zookeeperNodeTable").dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : true,
			"aaSorting" : [ [ 0, "asc" ] ],
			"bInfo" : false,
			"aoColumns" : [ {
				'sWidth' : '20%'
			}, {
				'sWidth' : '40%'
			}, {
				'sWidth' : '15%'
			}, {
				'sWidth' : '15%'
			}, {
				'sWidth' : '10%'
			}, ],
			"aoColumnDefs" : [ {
				'bSortable' : false,
				'aTargets' : [ 1, 2, 3, 4 ]
			} ],
		});
		$('#zookeeperNodeTable_filter').hide();
		zookeeperNodeTable = $('#zookeeperNodeTable').dataTable();
		$('#zookepeerNodesSearchBox').keyup(function() {
			zookeeperNodeTable.fnFilter($(this).val());
		});
		com.impetus.ankush.hybrid_Zookeeper.zookeeperNodesPopulate();
	});
</script>
<body>
	<div class="">
		<!-- header section -->
		<div class="">
			<div class="">
				<div class="col-md-4">
					<%-- <h2 id="zookeeperNodeHeader1">${technology}/Node Mapping</h2>
						<h2 id="zookeeperNodeHeader" style="display: none;">${technology}/Node Mapping</h2> --%>
				</div>
				<div class="col-md-1">
					<!-- <button class="btn-error" id="validateErrorZookeeper"
						onclick="com.impetus.ankush.common.focusError();"
						style="display: none; padding: 0 15px; left: 15px; position: relative"></button> -->
				</div>
				<div class="col-md-7 text-right mrgt20 padr45">
					<!-- 	<button id="zookeeperNodesRevert" class="btn headerright-setting btn-default"
						onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
					<button class="btn btn-default" id="zookeeperNodesApply"
						onclick="com.impetus.ankush.hybrid_Zookeeper.zookeeperNodesPopulateApplyValidate();">Apply</button> -->
				</div>
			</div>
		</div>

		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row">
					<!-- 	<div id="errorDivZookeeperNodes" class="col-md-12 errorDiv"
						style="display: none;"></div> -->
				</div>
				<div class="panel">
					<div class="panel-heading">
						<div class="">
							<div class="">
								<h2 id="zookeeperNodeHeader1" class="panel-title col-md-3 mrgt5">${technology}/Node
									Mapping</h2>
								<h2 id="zookeeperNodeHeader" class="panel-title"
									style="display: none;">${technology}/Node Mapping</h2>
								<!-- <h3 class="panel-title">Node List</h3> -->
								<button id="zookeeperNodesRevert"
									class="btn headerright-setting btn-default"
									onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
								<button class="btn btn-default" id="zookeeperNodesApply"
									onclick="com.impetus.ankush.hybrid_Zookeeper.zookeeperNodesPopulateApplyValidate();">Apply</button>
							</div>
							<div class="pull-right panelSearch">
								<input type="text" id="zookepeerNodesSearchBox" style=""
									class="input-medium form-control" placeholder="Search" />
							</div>
							<div id="errorDivZookeeperNodes" class="col-md-12 errorDiv"
								style="display: none;"></div>
						</div>
						<div class="row panel-body">
							<div class="col-md-12" style="">
								<table class="table table-striped" id="zookeeperNodeTable"
									style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4; width: 100% !important">
									<thead
										style="text-align: left; border-bottom: 1px solid #E1E3E4">
										<tr>
											<th>Host Name</th>
											<th>Node Roles</th>
											<th>Zookeeper</th>
											<th>OS</th>
											<th></th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
