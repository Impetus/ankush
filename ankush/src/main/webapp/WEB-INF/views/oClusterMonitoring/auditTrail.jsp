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
<!-- Page for audit-taril -->
<%@ include file="../layout/blankheader.jsp"%>
<script type="text/javascript">
	var auditTrailTable = $('#auditTrailTable').dataTable({
		"bJQueryUI" : true,
		'bPaginate' : false,
		'bFilter' : true,
		'bLengthChange' : false,
		'bSort' : true,
		"bAutoWidth" : false,
		"aoColumns" : [
		               null,
		               null,
		               null,
		               null,
		               null,
		               null,
						{
							'bSortable': false ,
							"fnRender" : function(
									oObj) {
								return "<a href='#' onclick='com.impetus.ankush.oClusterMonitoring.loadAuditTrailChildPage("
								+ oObj.aData[6] + ");'>" + right_arrow + "</a>";
							}
						},
						 ],
	});
	$("#auditTrailTable_filter").hide();
	auditTrailTable = $('#auditTrailTable').dataTable();
	$('#auditTrailSearchBox').keyup(function() {
		auditTrailTable.fnFilter($(this).val());
	});
</script>
<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js" type="text/javascript"></script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span3 text-left">
			<h2 class="heading">Audit Trail</h2>
		</div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span2 text-left">
				<h4 class="section-heading">Audit Trail</h4>
			</div>
			<div class="span4 offset6 text-right">
				<input type="text" id="auditTrailSearchBox" placeholder="Search" />
			</div>
		</div>
		<div>
			<table id="auditTrailTable" class="table table-striped datatable"
				style="border: 1px solid; border-color: #cccccc">
				<thead>
					<tr>
						<th>File</th>
						<th>Type</th>
						<th>Property Name</th>
						<th>Property Value</th>
						<th>User</th>
						<th>Time</th>
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>
