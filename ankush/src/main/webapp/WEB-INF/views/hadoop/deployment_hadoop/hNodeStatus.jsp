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
<!-- List of Nodes with deployment progress during Cluster Deployment Progress   -->

<%@ include file="../../layout/blankheader.jsp"%>

<script>
	var clusterId = '<c:out value="${clusterId}"/>';
</script>
<body style="background: none;">
	<div class="section-header" style="">
		<div  class="row-fluid mrgt20">
	  		<div class="span4"><h2 class="heading text-left">Node Status</h2></div>
		</div>
	</div>
	<div class="section-body">
	
	<div class="container-fluid">
		<!-- <div class="row-fluid">
			<div class="span4">
				<h4 class="section-heading" style="text-align: left;">Node List</h4>
			</div>
		</div> -->
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-stripped" id="nodeIpStatusTable" width="100%"
					style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
					<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
						<tr>
							<th>IP</th>
							<th>Type</th>
							<th>Status</th>
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
