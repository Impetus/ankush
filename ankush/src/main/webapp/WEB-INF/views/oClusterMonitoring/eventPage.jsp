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
<!-- Page for cluster events -->
<%@ include file="../layout/blankheader.jsp"%>
<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js" type="text/javascript"></script>
<script>

	eventTable = $("#eventTable").dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : false,
		"bFilter" : true,
		"bSort" : true,
		"aaSorting":[],
		"bInfo" : false,
		"bAutoWidth" : false,
		"aoColumnDefs": [
		                  { 'bSortable': false, 'aTargets': [ 5 ] }
		               ],
	});
	$("#eventTable_filter").hide();
	eventTable = $('#eventTable').dataTable();
	$('#eventPageSearchBox').keyup(function() {
		eventTable.fnFilter($(this).val());
	});
</script>
<body style="background: none;">
	<div class="section-header" style="">
		<div class="row-fluid mrgt20">
			<div class="span12">
				<h2 class="heading">Events</h2>
			</div>
		</div>
	</div>
	<div class="section-body">
		<div class="container-fluid">
			<div class="row-fluid">
			<div class="span2 text-left">
				<h4 class="section-heading">Events</h4>
			</div>
			<div class="span4 offset6 text-right">
				<input type="text" id="eventPageSearchBox" placeholder="Search" />
			</div>
		</div>
				<div>
					<table class="table table-striped" id="eventTable"
						style="border: 1px solid #E1E3E4;">
						<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
							<tr>
								<!-- <th>Key</th> -->
								<th>Event</th>
								<th>Type</th>
								<th>Severity</th>
								<th>Status</th>
								<th>Time</th>
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
</body>
