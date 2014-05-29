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
<!-- Page for plan history -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	planHistoryTable = $("#planHistoryTable").dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : false,
		"bFilter" : true,
		"bSort" : true,
		"bInfo" : false,
		"bAutoWidth" : false,
		"aaSorting":[],
		"aoColumns" : [
		               null,
		               null,
		               null,
		               null,
		               null,
		               null,
		               null,
						{
							'bSortable': false ,
						},
						 ],
	});
	
	$("#planHistoryTable_filter").hide();
	planHistoryTable = $('#planHistoryTable').dataTable();
	$('#planHistorySearchBox').keyup(function() {
		planHistoryTable.fnFilter($(this).val());
	});
	/* $("#planHistoryTable_filter").css({
		'text-align' : 'right'
	});
	$("#planHistoryTable_filter")
			.prepend(
					'<div style="float:left;margin-top:15px;" id="planHistoryDatatable"></div>');
	$("#planHistoryDatatable").append(
			"<h4 class='section-heading'>Plan History</h4>"); */
</script>
<body style="background: none;">
	<div class="section-header" style="">
		<div class="row-fluid mrgt20">
			<div class="span6">
				<h2 class="heading left">Plan History</h2>
				<button class="btn-error" id="planHistoryButton"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative" onclick="com.impetus.ankush.oClusterMonitoring.focusError()"></button>
			</div>
			
			
			<!--  
			<div class="span3 minhgt0">
				<button class="span3 btn-error" id="planHistoryButton"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;" onclick="com.impetus.ankush.oClusterMonitoring.focusError()"></button>
			</div>
			-->
		</div>
	</div>
	<div class="section-body">
		<div class="container-fluid">
		<div id="planHistoryError" class="commonErrorDiv" style="display: none;"></div>
			<div class="row-fluid">
			<div class="span2 text-left">
				<h4 class="section-heading">Plan History</h4>
			</div>
			
			
			
			
			<div class="span4 offset6 text-right">
				<input type="text" id="planHistorySearchBox" placeholder="Search" />
			</div>
		</div>
				<div>
					<table class="table table-striped" id="planHistoryTable"
						style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
						<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
							<tr>
								<th>Plan</th>
								<th>Type</th>
								<th>Status</th>
									<th>Action</th>
								<th>Attempts</th>
								<th>Start</th>
								<th>Stop</th>
								<th></th>
							</tr>
						</thead>
						<tbody style="text-align: left;">
						</tbody>
					</table>
				</div>
			
		</div>
	</div>
</body>
