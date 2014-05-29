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
<%@ include file="../layout/blankheader.jsp"%>
<script>
var ipNodeInspect = '';
$(document).ready(function(){
	setTimeout(function(){
	ipNodeInspect = $('#nodeDetailHead').text();	
},0);
});
</script>
	<div class="section-header">
		<div class="row-fluid">
			<div class="span10">
				<h3>
					<a
						href="<c:out value='${baseUrl}'/>/oClusterCreate/oDeploymentProgress"
						id="nodeDetailHead"></a>
				</h3>
			</div>
		</div>
	</div>
	<div class="section-body">
	<div class="container-fluid  mrgnlft8">
	<br>
		<div class="row-fluid">
			<div class="span10">
				<h4 class="section-heading" >Node Details</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right" id="">Status:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeStatus"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Admin:</label>
			</div>
			<div class="span10">
				<label class="text-left " style="color: black;" id="adminStatus"></label>
			</div>
		</div>
		<br>
			<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Storage Dir:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeStorageDir"></label>
			</div>
		</div>
		<br>
			<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Datacenter:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeDatacenter"></label>
			</div>
		</div>
		<br>
		<!-- 	<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Rack:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeRack"></label>
			</div>
		</div>
		<br> -->
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Capacity:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeCapacity"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">CPUs:</label>
			</div>
			<div class=" span10">
				<label class="text-left" style="color: black;" id="nodeCpu"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Memory:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeMemory"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Registry Port:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeRegPort"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">HA Port Range:</label>
			</div>
			<div class="span1">
				<label class="text-left" style="color: black;" id="nodeHA1"></label>
			</div>
			<div class="span1">
				<label class="text-left" style="color: black;" id="nodeHA2"></label>
			</div>
		</div>
			<div class="row-fluid" id="errorNodeDiv" style="display: none;">
			<div class="span2">
				<label class=" text-right form-label">Error:</label>
			</div>
			<div class="span10" id="nodeDeploymentError"></div>
		</div>
		<%@ include file="../common/validationTable.jsp"%>
		</div>
	</div>
