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
<!-- Page for node level deployment logs -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	var clusterId = '<c:out value="${clusterId}"/>';
</script>
<body style="background: none;">
	<div class="section-header">
		<div class="row-fluid">
			<div class="span5">
				<h3 id="nodeDetailHead1"></h3>
			</div>
		</div>
	</div>
	<div class="section-body">
	<div class="container-fluid  mrgnlft8">

		<div class="row-fluid">
			<div class="span4 ">
				<h4 class="section-heading" style="color: black;">Node Details</h4>
			</div>
		</div>
			<div id="nodeDeploymentField">
		</div>
		<div class="row-fluid" id="nodeErrorDiv" style="display: none;">
			<div class="span2">
				<label class=" text-right form-label">Error:</label>
			</div>
			<div class="span10" id="errorOnNodeDiv"></div>
		</div>

		<div class="row-fluid">
			<div class="span4">
				<h4 class="section-heading" style="color: black; text-align: left;">Node
					Deployment</h4>
			</div>
			<div class="row-fluid">
				<div id="nodeDeployProgress" class="span10"></div>
			</div>
		</div>
		</div>
	</div>
</body>
