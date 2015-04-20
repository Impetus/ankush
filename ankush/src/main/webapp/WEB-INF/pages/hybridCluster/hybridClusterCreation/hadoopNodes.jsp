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
				<div class="col-md-4"></div>
				<div class="col-md-1"></div>
				<div class="col-md-7 text-right mrgt20 padr45"></div>
			</div>
		</div>
		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row"></div>
				<div class="panel">
					<div class="panel-heading">
						<div class="">
							<div class="">
								<h3 class="panel-title col-md-3 mrgt5">Hadoop/Node Mapping</h3>
								<button id="hadoopNodesRevert" class="btn btn-default"
									onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
								<button class="btn btn-default" id="hadoopNodesApply"
									onclick="com.impetus.ankush.hybrid_Hadoop.hadoopNodesPopulateApplyValidate();">Apply</button>
							</div>
							<div class="pull-right panelSearch">
								<input type="text" id="nodeSearchBoxHadoop" placeholder="Search"
									class="input-medium form-control" />
							</div>
							<div id="errorDivHadoopNodes" class="errorDiv mrgt10"
								style="display: none;"></div>
						</div>
					</div>
					<div class="row panel-body">
						<div class="col-md-12 text-left">
							<table class="table table-striped tblborder1"
								id="hadoopNodeTable">
								<thead class="tblborder2">
									<tr>
										<th><input type='checkbox' id='nodeCheckHadoopHead'
											onclick="com.impetus.ankush.hybrid_Hadoop.hadoopHeadNodeCheck()"></th>
										<th>Host Name</th>
										<th>Node Roles</th>
										<th>NameNode</th>
										<th>Sec. NameNode</th>
										<th>DataNode</th>
										<th>JobTracker Node</th>
										<th>Resource Manager</th>
										<th>Job History Server</th>
										<th>WebApp Proxy</th>
										<th>Standby NameNode</th>
										<th>Journal Node</th>
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
	<script>
		$(document).ready(function() {
			hadoopNodeTable = $('#hadoopNodeTable').dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : true,
				"aaSorting" : [ [ 1, "asc" ] ],
				"bInfo" : false,
				"aoColumnDefs" : [ {
					'bSortable' : false,
					'aTargets' : [ 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 ]
				} ],
			});
			$("#hadoopNodeTable_filter").hide();
			$('#nodeSearchBoxHadoop').keyup(function() {
				hadoopNodeTable.fnFilter($(this).val());
			});
			com.impetus.ankush.hybrid_Hadoop.hadoopNodesPopulate();
		});
	</script>
</body>
