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
<!-- Page for node status table -->
	<div class="row" id="nodeListDiv">
		<div class="span4 offset1">
			<h4 style="text-align: left;">Node List</h4>
		</div>
	</div>

	<div class="row">
		<div class="span12 offset1">
			<table class="table table-striped table-bordered"
				id="setUpDetailNodeTable" style="width: 100%;">
				<thead>
					<tr>
						<th>IP</th>
						<th>Type</th>
						<th>Status</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<tr class="odd gradeX" style="border-bottom: 2px solid #c4c4c4">
						<td>192.168.145.180</td>
						<td>Admin</td>
						<td>Deploying...</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
					<tr class="odd gradeX" style="border-bottom: 2px solid #c4c4c4">
						<td>192.168.145.180</td>
						<td>Storage</td>
						<td>Copying Bundle file...</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
					<tr class="odd gradeX">
						<td>192.168.145.180</td>
						<td>Storage</td>
						<td>Done</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
