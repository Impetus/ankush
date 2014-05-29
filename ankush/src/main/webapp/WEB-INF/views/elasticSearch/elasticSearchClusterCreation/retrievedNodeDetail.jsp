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
<!-- Page for retrieved node detail -->
<%@ include file="../../layout/blankheader.jsp"%>
<script>
	var ipNodeInspect = '';
	$(document).ready(function() {
		setTimeout(function() {
			ipNodeInspect = $('#elasticSearchNodeHead').text();
		}, 0);
	});
</script>
<div class="section-header">
	<div class="row-fluid">
		<div class="span10">
			<h3>
				<a id="elasticSearchNodeHead"></a>
			</h3>
		</div>
	</div>
</div>
<div class="section-body">
	<br>
	<div class="row-fluid">
		<div class="span10 offset1">
			<h4 class="section-heading">Node Details</h4>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right" id="">Status:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;"
				id="elasticSearchNodeStatus"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">Type:</label>
		</div>
		<div class="span10">
			<label class="text-left " style="color: black;" id="nodeType"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">OS:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;" id="elasticSearchOs"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">Datacenter:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;"
				id="elasticSearchDatacenter"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">Rack:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;" id="elasticSearchRack"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">CPU:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;" id="elasticSearchCpu"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">Disk Count:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;"
				id="elasticSearchDiskCount"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">Disk Size:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;" id="elasticSearchDiskSize"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right">RAM:</label>
		</div>
		<div class="span10">
			<label class="text-left" style="color: black;" id="elasticSearchRam"></label>
		</div>
	</div>
	<br>
	<div class="row-fluid" id="errorNodeDiv" style="display: none;">
		<div class="span2">
			<label class=" text-right form-label">Error:</label>
		</div>
		<div class="span10" id="nodeDeploymentError"></div>
	</div>
	<%@ include file="../../common/validationTable.jsp"%>
</div>
