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

<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->
<script>
var nodeOverviewTable = null;
$(document).ready(function(){
	nodeOverviewTable=	$('#nodeOverviewTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : false,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		"oLanguage": {
			"sEmptyTable": 'Loading...',
	    }
	});
	
});


</script>

	<div id="confirmationDialogsNode" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Confirmation</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12" style="text-align: left; font-size: 14px;" id="Node-message">
						MASSAGE</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					id="confirmationDialogsOKNode"
					class="btn">OK</a>
			</div>
		</div>
	<div class="row">
		<div class="col-md-12 text-left">
		<div class="panel">
						<div class="panel-heading">
				<h3 class="panel-title">Node Overview</h3>
		</div>
		<div class="row panel-body">
			<div class="col-md-12 text-left">
				<table class="table"id="nodeOverviewTable">
					<thead class="text-left">
						<tr>
							<th>Key</th>
							<th>Value</th>
							<th></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
</div>
	</div>
	</div>

<script>
	$(document).ready(function(){
		com.impetus.ankush.nodeOverview.nodeOverviewPopulate();
	})
</script>
