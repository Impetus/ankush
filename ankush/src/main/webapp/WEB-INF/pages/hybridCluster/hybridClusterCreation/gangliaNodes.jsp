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
		gangliaNodeTable = $("#gangliaNodeTable").dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : true,
			"bInfo" : false,
			"aaSorting" : [ [ 0, "asc" ] ],
			"aoColumnDefs" : [ {
				'sType' : "ip-address",
				'aTargets' : [ 0 ]
			}, {
				'bSortable' : false,
				'aTargets' : [ 1, 2, 3, 4,]
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

<!-- header section -->
<div class="section-header">
	<div class="row-fluid headermargin">
		<div class="span7">
			<h2 class="heading text-left left">${technology}</h2>
			<button class="btn-error header_errmsg" id="validateErrorGanglia"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none; padding: 0 15px; left: 15px; position: relative"></button>

		</div>
		<div class="span5 text-right padr45">
			<button id="gangliaNodesRevert" class="btn headerright-setting"
				onclick="com.impetus.ankush.hybrid_Ganglia.gangliaNodesPopulate('error');">Cancel</button>
			<button class="btn" id="gangliaNodesApply"
				onclick="com.impetus.ankush.hybrid_Ganglia.gangliaNodesPopulateApplyValidate();">Apply</button>
		</div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="errorDivGangliaNodes" class="span12 errorDiv"
				style="display: none;"></div>
		</div>
		<div class="row-fluid">
			<div class="span4">
				<div class="row-fluid">
					<div class="span10">

						<label class="node-dt">Node List</label>
						<div class="btn-group startShardDropDown"
							style="float: left; margin-left: 20px;"></div>
						<div class="btn-group startShardDropDown"
							style="float: left; margin-left: 8px;"></div>

					</div>
					<div style="clear: both;"></div>
				</div>
			</div>
			<div class="span2 offset6 text-right">
				<input type="text" id="gangliaNodesSearchBox" style=""
					class="input-medium" placeholder="Search" />
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12 " style="">
				<table class="table table-striped" id="gangliaNodeTable"
					style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
					<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
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
