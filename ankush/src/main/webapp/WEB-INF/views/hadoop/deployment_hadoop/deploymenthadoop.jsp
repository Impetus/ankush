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
<!-- Cluster Deployment Progress Page : Links for deployment details and deployment logs   -->

<%@ include file="../../layout/blankheader.jsp"%>
<script src="<c:out value='${baseUrl}' />/public/js/hadoop/ankush.hadoop-cluster.js" type="text/javascript"></script>

<script>

$(document).ready(function() {
	var clusterId = '<c:out value="${clusterId}"/>';
	com.impetus.ankush.hadoopCluster.setCurrentClusterId(clusterId);
});
</script>

<div class="section-header">
	<div  class="row-fluid"   style="margin-top:20px;">
	  	<div class="span4"><h2 class="heading text-left">Deployment Progress</h2></div>
	  	<div class="span3">
				<div style="width:200px;margin-top:8px;" id="div_status_ClusterDeploymentProgress" class="text-center">
					<span id="status_ClusterDeploymentProgress" class="label">Cluster Deployment In Progress</span>
				</div>
		</div>
	</div>
<!-- 	<div style="margin-top: 10px;"><hr></div> -->
</div>

<div class="section-body">
		
		<div class="row-fluid">
			<a href="#" onclick="com.impetus.ankush.hadoopCluster.loadSetupDetailHadoop();">
			<div class="span2">
				<h4 class="section-heading" style="color: black; text-align: left;">Setup Details</h4>
			</div>
			<div class="span1">
			<img src="<%=baseUrl%>/public/images/icon-chevron-right.png" style="margin-top:12px;margin-bottom:12px;"/>
			</div>
			</a>
		</div>	
		
		<div class="row-fluid">
		<a style="line-height: 40px;" href="##" onclick="com.impetus.ankush.hadoopCluster.loadNodeStatusHadoop(${clusterId});">
		<div class="span2">
			<h4 class="section-heading" style="color: black; text-align: left;">Node Status</h4>
		</div>
			<div class="span1">
				<img src="<%=baseUrl%>/public/images/icon-chevron-right.png" style="margin-top:12px;margin-bottom:12px;"/>
			</div>
			</a>
		</div>
		
		<div class="row-fluid">
		<div class="span3">
			<h4 class="section-heading" style="color: black; text-align: left;">Deployment Progress</h4>
		</div>
		<div class="span10 offset1" id='deploymentProgressDivHadoop'></div>
	</div>
</div>
