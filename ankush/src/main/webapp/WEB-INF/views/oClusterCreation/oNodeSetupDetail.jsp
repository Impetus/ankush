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
<!-- node drill down page of Setup detail page for oracle cluster creation -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	var clusterId = '<c:out value="${clusterId}"/>';
	$('#example_filter').css('text-align', 'right');
</script>
<body style="background: none;">
<div class="section-header">
	<div class="row-fluid">
		<div class="span5">
				<h3 id="nodeDetailHead1">
			</h3>
		</div>
	</div>
</div>
<div class="section-body">

	<div class="row-fluid">
		<div class="span4 ">
			<h4 class="section-heading" style="color: black;">Node Details</h4>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label" id="">Status:</label>
		</div>
		<div class="span10">
			<label class="text-left form-label" style="color: black;" id="nodeStatusDeploy1"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Admin:</label>
		</div>
		<div class="span10">
			<label class="text-left form-label" style="color: black;" id="adminStatus1"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Capacity:</label>
		</div>
		<div class="span10">
			<label class="text-left form-label" style="color: black;" id="nodeCapacity1"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">CPUs:</label>
		</div>
		<div class=" span10">
			<label class="text-left form-label" style="color: black;" id="nodeCpu1"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Memory:</label>
		</div>
		<div class="span10">
			<label class="text-left form-label" style="color: black;" id="nodeMemory1"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Registry Port:</label>
		</div>
		<div class="span10">
			<label class="text-left form-label" style="color: black;" id="nodeRegPort1"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">HA Port Range:</label>
		</div>
		<div class="span1">
			<label class="text-left form-label" style="color: black;" id="nodeHA11"></label>
		</div>
		<div class="span1">
			<label class="text-left form-label" style="color: black;" id="nodeHA21"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span4">
				<h4 class="section-heading" style="color: black; text-align: left;">Node Deployment</h4>
		</div>
		<div id="nodeDeploymentProgressDiv" class="span10 offset1">
	
		</div>
	</div>
</div></body>
