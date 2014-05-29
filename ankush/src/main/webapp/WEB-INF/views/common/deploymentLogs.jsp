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
<!-- Page for cluster level deployment logs -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
$(document).ready(function() {
	var clusterId = '<c:out value="${clusterId}"/>';
	var clusterTechnology = '<c:out value='${clusterTechnology}'/>';
//	com.impetus.ankush.oClusterSetup.deploymentProgress(clusterId);
});

</script>
<div class="section-header">
	<div class="row-fluid">
		<div class="span4">
			<h3>Deployment Progress</h3>
		</div>
		<div class="span3 header-align">
			<span class="label" id="deploymentStatusLabel"></span>
		</div>
		<div class="span5 text-right header-align">
			<button id="clusterDeleteBtn" class="btn display-none"
				onclick="com.impetus.ankush.common.deleteDialog('deleteDialog');">Delete</button>
		</div>
	</div>
</div>
<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div id="deleteDialog" class="modal hide fade" style="display: none;">

			<div class="modal-header text-center">
				<h4 style="text-align: center; color: black">Cluster Delete</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left;">The Cluster
						will be permanently deleted. Once deleted data cannot be
						recovered.</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn" id="cancelDeleteButton">Cancel</a>
				<a href="#" id="confirmDeleteButton"
					onclick="com.impetus.ankush.common.deleteCluster('deleteDialog');"
					class="btn">Delete</a>
			</div>
		</div>
		<div class="row-fluid"
			onclick="com.impetus.ankush.common.loadSetUpDetail('<c:out value='${clusterTechnology}'/>');">
			<div class="span2 ">
				<h4 class="section-heading">
					<a class="lbl-black" href="##">Setup Details</a>
				</h4>
			</div>
			<div class="">
				<a style="line-height: 35px;" href="##"><img
					src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
			</div>
		</div>
		<div class="row-fluid"
			onclick="com.impetus.ankush.common.loadNodeStatus()">
			<div class="span2 ">
				<h4 class="section-heading">
					<a href="##" class="lbl-black">Node Status</a>
				</h4>
			</div>
			<div class="">
				<a style="line-height: 35px;" href="##"><img
					src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span3">
				<h4 class="section-heading lbl-black">Deployment Progress</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span10" id='deploymentProgressDiv'></div>
		</div>



	</div>
</div>
