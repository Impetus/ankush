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
<!-- Hadoop Cluster's nodes OS Details Page  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script>
$(document).ready(function(){
		var clusterId = "<c:out value='${clusterId}'/>";
		var nodeId = "<c:out value='${nodeId}'/>";
		var nodeIp = "<c:out value='${nodeIp}'/>";
		com.impetus.ankush.hadoopNodeDrillDown.loadNodeData(nodeId, "osInfo");
	});
</script>
<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left">OS Details</h2>
		</div>
	</div>
</div>
<div class="section-body" >
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">System Data Model :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="dataModel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Description :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="description"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">OS :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="vendor"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Machine Name :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="machineName"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Architecture :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="arch"></label>
			</div>
		</div>
	</div>
</div>
