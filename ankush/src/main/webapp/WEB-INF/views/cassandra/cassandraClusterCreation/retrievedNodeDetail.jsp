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
<!-- Page for retrieved node details -->
<%@ include file="../../layout/blankheader.jsp"%>
<script>
var ipNodeInspect = '';
$(document).ready(function(){
	setTimeout(function(){
	ipNodeInspect = $('#cassandraNodeHead').text();	
},0);
});
</script>
<div class="section-header">
	<div class="row-fluid">
		<div class="span10">
			<h3>
				<a id="cassandraNodeHead"></a>
			</h3>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="container-fluid">

		<br>
		<div class="row-fluid">
			<div class="span10">
				<h4 class="section-heading">Node Details</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right" id="">Status:</label>
			</div>
			<div class="span10">
				<label class="text-left lbl-black" id="cassandraNodeStatus"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">OS:</label>
			</div>
			<div class="span10">
				<label class="text-left lbl-black" id="cassandraOs"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Datacenter:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="cassandraDatacenter"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Rack:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="cassandraRack"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">CPU:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="cassandraCpu"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Disk Count:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;"
					id="cassandraDiskCount"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Disk Size:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;"
					id="cassandraDiskSize"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">RAM:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="cassandraRam"></label>
			</div>
		</div>
		<br>
		
		<div class="row-fluid" id="isAuth">
			<div class="span2 ">
				<label class="text-right">Authenticated:</label>
			</div>
			<div class="span10">
				<label class="text-left lbl-black" id="isAuthenticated"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid" id="errorNodeDiv" style="display: none;">
			<div class="span2">
				<label class=" text-right">Error</label>
			</div>
			<div class="span10" id="nodeDeploymentError"></div>
		</div>
<%@ include file="../../common/validationTable.jsp"%>
	</div>
</div>
