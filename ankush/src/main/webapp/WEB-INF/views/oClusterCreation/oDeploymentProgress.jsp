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
<!-- Page for deployment progress of oracle cluster creation -->
<%@ include file="../layout/blankheader.jsp"%>
 
<script>
$(document).ready(function() {
	var clusterId = '<c:out value="${clusterId}"/>';
//	com.impetus.ankush.oClusterSetup.deploymentProgress(clusterId);
});

</script>
<div class="section-header">
	<div class="row-fluid">
		<div class="span3">
			<h3>Deployment Progress</h3>
		</div>
		<div class="span1" style="padding-top: 24px;">
		<span class="label" id="deploymentStatusLabel"></span>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="row-fluid">
		<div class="span2 ">
			<h4 class="section-heading" style="color: black; text-align: left;"><a style="color: black; href="##" >Setup Details</a></h4>
		</div>
		<div class="span1">
			<a style="line-height: 35px;" href="##" onclick="com.impetus.ankush.oClusterSetup.loadSetupDetailOracle();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<h4 class="section-heading" style="color: black; text-align: left;"><a style="color: black; href="##" >Node Status</a></h4>
		</div>
		<div class="span1">
			<a style="line-height: 35px;" href="##" onclick="com.impetus.ankush.oClusterSetup.loadNodeStatusOracle();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span3">
			<h4 class="section-heading" style="color: black; text-align: left;">Deployment Progress</h4>
		</div>
		<div class="span10 offset1" id='deploymentProgressDiv'></div>
	</div>
</div>
