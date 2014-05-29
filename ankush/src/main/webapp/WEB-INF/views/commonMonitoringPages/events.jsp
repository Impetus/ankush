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
<%@ include file="../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js"
	type="text/javascript"></script>
<script>

	$(document).ready(function(){
		/* this function will fill events datatable */
		//com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
		com.impetus.ankush.commonMonitoring.commonEvents();
	});
</script>
<!-- This page will show events page -->
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Events</h2>
		</div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="span2 padt20">
					<label class='section-heading'>Events</label>
				</div>

				<div class="text-right">
					<input id="searchEvents" type="text" placeholder="Search">
				</div>
			</div>
		</div>
		<div class="row-fluid" id="eventsTableDiv">
			<div class="span12">
				<table class="table table-striped table-border" width="100%" id="eventsTable">
					<thead style="text-align: left;">
						<tr>
							<th>Event</th>
							<th>Type</th>
							<th>Severity</th>
							<th>Host</th>
							<th>Status</th>
							<th>Time</th>
						</tr>
					</thead>
					<tbody style="text-align: left;">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
