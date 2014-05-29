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
<%@include file="../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js"
	type="text/javascript"></script>
<script>
	$(document).ready(function() {
		/* This function will fill audit trail datatable */
		com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
		com.impetus.ankush.commonMonitoring.commonAuditTrail();
	});
</script>
<!--This page will show Audit trail page  -->
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Audit Trail</h2>
		</div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="span2 padt20">
					<label class='section-heading text-left'>Audit Trail</label>
				</div>
				<div class="text-right">
					<input id="searchAuditTrail" type="text" placeholder="Search">
				</div>
			</div>
		</div>
		<div class="row-fluid" id="auditTrailTable1">
			<div class="span12">
				<table class="table table-striped table-border" id="auditTrailTable" >
					<thead class="text-left">
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
					<tbody class="text-left">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
