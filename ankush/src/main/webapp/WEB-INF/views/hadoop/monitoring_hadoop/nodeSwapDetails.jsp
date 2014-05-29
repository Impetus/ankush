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
<!-- Hadoop Cluster's nodes Swap Details Page  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script>
	$(document).ready(function(){
		var clusterId = "<c:out value='${clusterId}'/>";
		var nodeId = "<c:out value='${nodeId}'/>";
		var nodeIp = "<c:out value='${nodeIp}'/>";
		com.impetus.ankush.hadoopNodeDrillDown.loadNodeData(nodeId, "swapInfo");
	});
</script>

<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left">Swap Details</h2>
		</div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Page In :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="pageIn"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Page Out :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="pageOut"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Total System Swap :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="totalSystemSwap"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Used System Swap :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="usedSystemSwap"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Free System Swap:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="freeSystemSwap"></label>
			</div>
		</div>
	</div>
</div>
