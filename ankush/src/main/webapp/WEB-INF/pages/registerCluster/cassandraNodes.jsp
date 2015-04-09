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
		cassandraNodeTable = $('#cassandraNodeTable').dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : true,
			"aaSorting" : [ [ 1, "asc" ] ],
			"bInfo" : false,
			"aoColumnDefs" : [ {
				'sType' : "ip-address",
				'aTargets' : [ 1 ]
			}, {
				'bSortable' : false,
				'aTargets' : [ 0, 2, 3 ,4]
			} ],
		});
		$("#cassandraNodeTable_filter").hide();
		$('#nodeSearchBoxCasandra').keyup(function() {
			cassandraNodeTable.fnFilter($(this).val());
		});
	});
</script>

<!-- header section -->
<div class="section-header">
	<div class="row-fluid headermargin">
		<div class="span7">
			<h2 class="heading text-left left">${technology}</h2>

			<button class="btn-error header_errmsg" id="validateErrorCassandra"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none; padding: 0 15px; left: 15px; position: relative"></button>
		</div>
		<div class="span5 text-right">
			<button id="cassandraNodesRevert" class="btn headerright-setting"
				onclick="com.impetus.ankush.register_Cassandra.cassandraNodesPopulate('error');">Revert</button>
			<button class="btn" id="cassandraNodesApply"
				onclick="com.impetus.ankush.register_Cassandra.cassandraNodesPopulateApplyValidate();">Apply</button>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="errorDivCassandraNodes" class="span12 errorDiv"
				style="display: none;"></div>
		</div>
		<div class="row-fluid ">
			<div class="row-fluid">
				<div class="text-left span9">
					<label class="node-dt">Node List</label>
				</div>

				<div class="span3 text-right">
					<input type="text" id="nodeSearchBoxCasandra" placeholder="Search"
						class="input-medium" />
				</div>
			</div>
			<table class="table table-striped tblborder1" id="cassandraNodeTable">
				<thead class="tblborder2">
					<tr>
						<th>Host Name</th>
						<th>Node Roles</th>
						<th>Cassandra Node</th>
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
