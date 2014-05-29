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
<!-- Hadoop Cluster monitoring main page containing cluster level tiles, heat maps, and links to various cluster level details, like nodes info, Audits, events , etc. -->

<%@ include file="../../layout/blankheader.jsp"%>

<script>
$(document).ready(function(){
	$('.dropdown-toggle').dropdown();
	var clusterId = "<c:out value='${clusterId}'/>";
	$("#deleteClusterDialogHadoopMonitor").appendTo('body');
	$("#invalidAddNodeOperation").appendTo('body');
	com.impetus.ankush.hadoopMonitoring.loadClusterData(clusterId);
});
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="text-left span6 row-fluid">
			<h2 class="heading text-left" style="width: auto; float: left"
				id="hadoopClusterName"></h2>
			<span id="clusterEnv" class="span2 text-left inline"
				style="margin-top: 10px; width: auto; margin-left: 10px; font-size: 12px;"></span>
		</div>
		<div class="span6 text-right">
			<div class="btn-group">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#"
					id="clusterActions" style="height: 25px">Actions <span
					class="caret"></span></a>
				<ul class="dropdown-menu pull-right">
					<li><a tabindex="-1" href="#" class="text-left"
						id="hadoopAddNodes"
						onclick="com.impetus.ankush.hadoopMonitoring.addNodes(<c:out value='${clusterId}'/>)">Add
							Nodes...</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.submitJobPage(<c:out value='${clusterId}'/>)">Submit
							Job...</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.commands(<c:out value='${clusterId}'/>)">Run
							Commands...</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.configuration(<c:out value='${clusterId}'/>);">Manage
							Configurations...</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.logs(<c:out value='${clusterId}'/>);">View
							Logs...</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.jobMonitoringPage(<c:out value='${clusterId}'/>)">Manage
							Jobs</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.events(<c:out value='${clusterId}'/>)">View
							Events</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.auditTrail(<c:out value='${clusterId}'/>)">View
							Audit Trail</a></li>
					<li><a tabindex="-1" href="#" class="text-left"
						onclick="com.impetus.ankush.hadoopMonitoring.deleteDialogHadoop()"
						id="deleteCluster">Delete Cluster</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>

<div class="section-body content-body" style="margin-left: 15px;">
	<div class="container-fluid" style="margin-left: 15px;"
		id="div_HadoopMonitoringDetails">
		<div style="margin-top: 10px;" class="row-fluid transitions-enabled"
			id="allTilesCluster"></div>
		<div class="row-fluid" style="margin-top: 30px;">
			<div id="tileViewDetail"></div>
		</div>
		<div id="deleteClusterDialogHadoopMonitor" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Delete Cluster</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;">
						The Cluster will be permanently deleted. Once deleted data cannot
						be recovered.</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					id="confirmDeleteButtonHadoopMonitor"
					onclick="com.impetus.ankush.hadoopMonitoring.deleteClusterHadoop();"
					class="btn">Delete</a>
			</div>
		</div>
		<div id="invalidAddNodeOperation" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Node Addition</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;">
						<label class="span12" style="text-align: left; font-size: 14px;"
							id="lbl_invalidAddNodeOperation"> </label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">OK</a>
			</div>
		</div>
		<div class="row-fluid">
			<div style="float: left;">
				<label class="form-label section-heading">Heat Map</label>
			</div>
			<div style="float: left;">
				<div class="form-image text-left btn-group"
					data-toggle="buttons-radio"
					style="margin-left: 20px; margin-top: 8px;">
					<button type="button" class="btn active" id="cpuHeatMap"
						onclick="heatMapTypeSelect('CPU');">CPU</button>
					<button type="button" class="btn" id="memoryHeatMap"
						onclick="heatMapTypeSelect('Memory');">Memory</button>
				</div>
			</div>
			<div style="float: left; margin-top: 8px;" class="legend">
				<div class="drow">
					<div class="alignl">Low</div>
					<div class="legand-normal"></div>
					<div class="legand-warning"></div>
					<div class="legand-critical"></div>
					<!-- 					<div class="legand-alert-unavailable"></div> -->
					<div class="alignr">High</div>
				</div>
			</div>
			<div style="float: left; margin-top: 8px;" class="legend">
				<div class="drow">
					<div class="alignl">
						<b>Not Available</b>
					</div>
					<div class="legand-alert-unavailable"></div>
				</div>
			</div>
			<div style="float: left; margin-left: 20px; margin-top: 4px;">
				<div>
					<h4 style="text-align: left; cursor: pointer;"
						class="section-heading">
						<a href="#"
							onclick="com.impetus.ankush.hadoopMonitoring.utilizationTrend(<c:out value='${clusterId}'/>);">
							Utilization Trends&nbsp;&nbsp;&nbsp;&nbsp;<img
							src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
						</a>
					</h4>
				</div>
			</div>
		</div>
		<div class="text-left row-fluid" id="heat_map"></div>


		<a name='hadoopMonitoring_hadoopDetails_Jump'> </a> </br>
		<%@ include file="_hadoopMonitoring_hadoop_details.jsp"%>
		<%@ include file="_config_hadoop_ecosystem.jsp"%>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="text-align: left; cursor: pointer;">
						<a
							onclick="com.impetus.ankush.hadoopMonitoring.nodeDetails(<c:out value='${clusterId}'/>);">Nodes</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.nodeDetails(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="text-align: left; cursor: pointer;">
						<a
							onclick="com.impetus.ankush.hadoopMonitoring.jobMonitoringPage(<c:out value='${clusterId}'/>);">Job
							Monitoring</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.jobMonitoringPage(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="text-align: left; cursor: pointer;">
						<a
							onclick="com.impetus.ankush.hadoopMonitoring.configuration(<c:out value='${clusterId}'/>);">Configurations</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.configuration(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="text-align: left; cursor: pointer;">
						<a
							onclick="com.impetus.ankush.hadoopMonitoring.events(<c:out value='${clusterId}'/>);">Events</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.events(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="text-align: left; cursor: pointer;">
						<a
							onclick="com.impetus.ankush.hadoopMonitoring.logs(<c:out value='${clusterId}'/>);">Logs</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.logs(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span8 ">
				<div style="float: left;">
					<h4 class="section-heading"
						style="text-align: left; cursor: pointer;">
						<a
							onclick="com.impetus.ankush.hadoopMonitoring.auditTrail(<c:out value='${clusterId}'/>);">Audit
							Trail</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"
						onclick="com.impetus.ankush.hadoopMonitoring.auditTrail(<c:out value='${clusterId}'/>);"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
	</div>
</div>
