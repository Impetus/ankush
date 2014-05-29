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
<!-- Hadoop Cluster Events page  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js"
	type="text/javascript"></script>
<script>
	
	$(document).ready(function(){
		var clusterId = "<c:out value='${clusterId}'/>";
		com.impetus.ankush.hadoopMonitoring.initHadoopEvents(clusterId);
	});
</script>
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
				<div class="span2 text-left" style="padding-top: 20px; float: left">
					<label class='section-heading text-left'>Events</label>
				</div>

				<div class="text-right">
					<input id="searchEvents" type="text" placeholder="Search">
				</div>
			</div>
		</div>
		<div class="row-fluid" id="eventsTable">
			<div class="span12">
				<table class="table table-striped" width="100%" id="hadoopEventsTable"
					style="border: 1px solid; border-color: #CCCCCC">
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
