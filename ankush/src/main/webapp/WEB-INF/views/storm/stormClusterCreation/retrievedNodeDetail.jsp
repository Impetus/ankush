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
$(document).ready(function(){
	setTimeout(function(){
	ipNodeInspect = $('#stormNodeHead').text();	
},0);
});
</script>
	<div class="section-header">
		<div class="row-fluid">
			<div class="span10">
				<h3>
					<a
						
						id="stormNodeHead"></a>
				</h3>
			</div>
		</div>
	</div>
	<div class="section-body">
	<div class="container-fluid mrgnlft8">
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
				<label class="text-left" style="color: black;" id="stormNodeStatus"></label>
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
				<label class="text-left" style="color: black;" id="stormOs"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">CPU:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="stormCpu"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Disk Count:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="stormDiskCount"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Disk Size:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="stormDiskSize"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">RAM:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="stormRam"></label>
			</div>
		</div>
		<br>
		<!-- <div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Hostname:</label>
			</div>
			<div class=" span10">
				<label class="text-left" style="color: black;" id=""></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Java:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id=""></label>
			</div>
		</div>
		<br> -->
		<div class="row-fluid" id="isAuthDiv">
			<div class="span2 ">
				<label class="text-right">Authenticated:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="isAuthenticated"></label>
			</div>
		</div>
		<div class="row-fluid" id="errorNodeDiv" style="display: none;">
			<div class="span2">
				<label class=" text-right form-label">Error:</label>
			</div>
			<div class="span10" id="nodeDeploymentError"></div>
		</div>
		<%@ include file="../../common/validationTable.jsp"%>
		</div>
		
	</div>
