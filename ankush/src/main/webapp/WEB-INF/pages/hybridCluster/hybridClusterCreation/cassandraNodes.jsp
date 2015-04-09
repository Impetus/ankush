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

<!-- header section -->
<body>
	<div class="">
		<div class="">
			<div class="">
				<!-- <div class="col-md-4">
					<h2 class="">Cassandra</h2>
				</div> -->
				<div class="col-md-1">
				<!-- 	<button class="btn btn-danger" id="validateErrorCassandra"
						onclick="com.impetus.ankush.common.focusError();"
						style="display: none; padding: 0 15px; left: 15px; position: relative"></button> -->
				</div>
			</div>
			<div class="col-md-7 text-right mrgt20">
				<!-- 	<button id="cassandraNodesRevert" class="btn btn-default"
					onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
				<button class="btn btn-default" id="cassandraNodesApply"
					onclick="com.impetus.ankush.hybrid_Cassandra.cassandraNodesPopulateApplyValidate();">Apply</button> -->
			</div>
		</div>
		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row">
					<!-- <div id="errorDivCassandraNodes" class="col-md-12 errorDiv"
						style="display: none;"></div> -->
				</div>
				<div class="panel">
					<div class="panel-heading">
						<div class="">
							<div class="">
								<h3 class="panel-title col-md-3 mrgt5">Cassandra Node
									Mapping</h3>
								<button id="cassandraNodesRevert" class="btn btn-default"
									onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
								<button class="btn btn-default" id="cassandraNodesApply"
									onclick="com.impetus.ankush.hybrid_Cassandra.cassandraNodesPopulateApplyValidate();">Apply</button>
							</div>
							<div class="pull-right panelSearch">
								<input type="text" id="nodeSearchBoxCasandra"
									placeholder="Search" class="input-medium form-control" />
							</div>
							<div id="errorDivCassandraNodes" class="errorDiv"
								style="display: none;"></div>
						</div>
					</div>
					<div class="row panel-body">
						<div class="col-md-12" style="">
							<table class="table table-striped tblborder1"
								id="cassandraNodeTable">
								<thead class="tblborder2">
									<tr>
										<th><input type='checkbox' id='nodeCheckCassandraHead'
											onclick="com.impetus.ankush.hybrid_Cassandra.checkAllNodes('nodeCheckCassandraHead','cassandraNodeCheckBox')"></th>
										<th>Host Name</th>
										<th>Node Roles</th>
										<th>SeedNode</th>
										<th>OS</th>
										<th>VNode Count</th>
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
	<script>
		$(document).ready(function() {
			cassandraNodeTable = $('#cassandraNodeTable').dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : true,
				"aaSorting" : [ [ 1, "asc" ] ],
				"bInfo" : false,
				"aoColumnDefs" : [ {
					'bSortable' : false,
					'aTargets' : [ 0, 2, 3, 4, 5, 6 ]
				} ],
			});
			$("#cassandraNodeTable_filter").hide();
			$('#nodeSearchBoxCasandra').keyup(function() {
				cassandraNodeTable.fnFilter($(this).val());
			});
			com.impetus.ankush.hybrid_Cassandra.cassandraNodesPopulate();
		});
	</script>
</body>
