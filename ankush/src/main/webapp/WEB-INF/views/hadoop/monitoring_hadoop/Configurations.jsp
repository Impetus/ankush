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
<!-- Cluster Configuration Page containing links to various Cluster Configurations, viz, Alerts, Auto-Provision,etc.  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script>
$(document).ready(function(){
	var clusterId = "<c:out value='${clusterId}'/>";
});
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Configurations</h2>
		</div>
	</div>
</div>

<div class="section-body" style="margin-left: 25px;">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="color: black; text-align: left; cursor: pointer;">
						<a 
							onclick="com.impetus.ankush.hadoopMonitoring.configurationCluster(<c:out value='${clusterId}'/>);">Cluster</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.configurationCluster(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="color: black; text-align: left; cursor: pointer;">
						<a 
							onclick="com.impetus.ankush.hadoopMonitoring.configurationHadoopEcosystem(<c:out value='${clusterId}'/>);">Hadoop
							Ecosystem</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.configurationHadoopEcosystem(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="color: black; text-align: left; cursor: pointer;">
						<a 
							onclick="com.impetus.ankush.hadoopMonitoring.jobSchedulers();">Job
							Scheduler</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.jobSchedulers();"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="color: black; text-align: left; cursor: pointer;">
						<a 
							onclick="com.impetus.ankush.hadoopMonitoring.configurationParameters();">Parameters</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.configurationParameters();"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="color: black; text-align: left; cursor: pointer;">
						<a 
							onclick="com.impetus.ankush.hadoopMonitoring.configurationAlerts(<c:out value='${clusterId}'/>);">Alerts</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.configurationAlerts(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
	</div>
</div>
