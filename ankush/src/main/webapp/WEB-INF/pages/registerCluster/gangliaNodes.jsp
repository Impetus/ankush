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

<body>
	<div class="">
		<div class="">
			<div class="">
				<div class="col-md-4">
				</div>
				<div class="col-md-1">
				</div>
				<div class="col-md-7 text-right mrgt20 padr45">
				</div>
			</div>
		</div>

		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row">
				</div>
				<div class="panel">
					<div class="panel-heading">
						<div class="">
							<div class="">
								<h3 class="panel-title col-md-3 mrgt5">Ganglia Node Mapping</h3>
								<button id="gangliaNodesRevert" class="btn btn-default"
									onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
								<button class="btn btn-default" id="gangliaNodesApply"
									onclick="com.impetus.ankush.hybrid_Ganglia.gangliaNodesPopulateApplyValidate();">Apply</button>
							</div>
							<div class="pull-right panelSearch">
								<input type="text" id="gangliaNodesSearchBox" style=""
									class="input-medium form-control" placeholder="Search" />
							</div>
							<div id="errorDivGangliaNodes" class="errorDiv mrgt10"
								style="display: none;"></div>
						</div>
					</div>
					<div class="row panel-body">
						<div class="col-md-12 " style="">
							<table class="table table-striped" id="gangliaNodeTable"
								style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
								<thead
									style="text-align: left; border-bottom: 1px solid #E1E3E4">
									<tr>
										<th>Host Name</th>
										<th>Node Roles</th>
										<th>GangliaMaster</th>
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
	<script>
		$(document).ready(function() {
			gangliaNodeTable = $("#gangliaNodeTable").dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : true,
				"bInfo" : false,
				"aaSorting" : [ [ 0, "asc" ] ],
				"aoColumnDefs" : [ {
					'bSortable' : false,
					'aTargets' : [ 1, 2, 3, 4, ]
				} ],
			});
			$('#gangliaNodeTable_filter').hide();
			gangliaNodeTable = $('#gangliaNodeTable').dataTable();
			$('#gangliaNodesSearchBox').keyup(function() {
				gangliaNodeTable.fnFilter($(this).val());
			});
			com.impetus.ankush.hybrid_Ganglia.gangliaNodesPopulate();
		});
	</script>
</body>
