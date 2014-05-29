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
<!-- Node Deployment Details (Node Details and Logs) during Cluster Deployment Progress   -->

<%@ include file="../../layout/blankheader.jsp"%>

<script>
	var clusterId = '<c:out value="${clusterId}"/>';
</script>
<body style="background: none;">
<div class="section-header">
	<div  class="row-fluid"   style="margin-top:20px;">
	  	<div class="span4"><h2 class="heading text-left">
	  	<a href="##" id="NSD_nodeDetailHead"></a>
	  	</h2>
	  </div>
	</div>
	
</div>
<div class="section-body">
	<div class="row-fluid">
		<div class="span4">
				<h4 class="section-heading" style="color: black; text-align: left;">Node Details</h4>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Status:</label>
		</div>
		<div class="span10">
			<label class="text-left data" id="NSD_nodeStatus"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Private Ip:</label>
		</div>
		<div class="span10">
			<label class="text-left data" style="color: black;" id="NSD_nodePrivateIP"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Type:</label>
		</div>
		<div class="span10">
			<label class="text-left data" id="NSD_nodeType"></label>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">OS:</label>
		</div>
		<div class="span10">
			<label class="text-left data" id="NSD_nodeOS"></label>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span4">
				<h4 class="section-heading" style="color: black; text-align: left;">Node Deployment</h4>
		</div>
		<div id="NSD_nodeDeploymentProgressDiv" class="span11 offset1">
		</div>
	</div>
</div>
</body>
