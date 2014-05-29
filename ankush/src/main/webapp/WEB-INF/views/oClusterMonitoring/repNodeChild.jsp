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
<!-- Page for replication node table child drill down -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	var repGpId = '<c:out value="${repGpId}"/>';
	var repNodeId = '<c:out value="${repNodeId}"/>';
	$('.dropdown-toggle').dropdown();
	$('#labelHeader').text("Shard " + repGpId + "/Replication Node " + repNodeId);
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span5 text-left">
			<h2 id="labelHeader" class="form-label heading"></h2>
		</div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid">
		<!-- <div class="row-fluid" style="margin-bottom: 30px; margin-top: 30px;">
				<div class="repNodeNodeShrad" id="repNodeNodeShrad"
			></div>
		</div> -->
		<%-- <div class="row-fluid">
			<div class="span5 text-left" onclick="com.impetus.ankush.oClusterMonitoring.loadRepNodeChildParam(repGpId,repNodeId)">
				<h4 class="section-heading" style="color: black;float: left;">Parameters</h4>
			
			<div class="" style="float: left;margin-left: 20px;">
				<a href="#" style="line-height: 40px;">
					<img
					src="<c:out value="${baseUrl}"/>/public/images/icon-chevron-right.png" />
				</a>
			</div></div>
			<div style="clear: both;"></div>
		</div> --%>

		<div id="repNodeDrillTableHeaderDiv" style="margin-top: 20px;">
			<div class="row-fluid">
				<div class="span5">
					<div class="row-fluid">
						<div class="span10">
							<h4 style="line-height: 12px; float: left; color: black;"
								class="section-heading">Shards</h4>

							<div class=" " style="float: left; margin-left: 20px;">
								<button class="btn"
									onclick="com.impetus.ankush.oClusterMonitoring.startReplicationNode(repGpId,repNodeId);">Start</button>
							<!-- 	<button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button> -->
							<!-- 	<ul class="dropdown-menu">
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.startReplicationNode(clusterId,'storageNodeChildCheckBox');">Start</a></li>
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.startAllRepNodes(clusterId,'storageNodeChildCheckBox');">Start
											All</a></li>
								</ul> -->
							</div>
							<div class=" " style="float: left; margin-left: 8px;">
								<button class="btn"
									onclick="com.impetus.ankush.oClusterMonitoring.stopReplicationNode(repGpId,repNodeId);">Stop</button>
								<!-- <button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId,'storageNodeChildCheckBox');">Stop</a></li>
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.stopAllRepNodes(clusterId,'storageNodeChildCheckBox');">Stop
											All</a></li>
								</ul> -->
							</div>
						</div>
						<div style="clear: both;"></div>
					</div>
				</div>
				<div class="span3 offset4 text-right">
					<input type="text" id="repNodeDrillSearchBox"
						style="margin-top: 0px;" placeholder="Search" />
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<table id="replicationNodeDrill" class="table table-striped"
				style="border: 1px solid; border-color: #CCCCCC">
				<thead>
					<tr>
						<th>Replication Node</th>
						<th>Storage Node</th>
					<!-- 	<th>Port</th> -->
						<!-- <th>95 %</th> -->
						<th>Throughput</th>
						<th>Latency</th>
						<!-- <th>Multi-Latency</th> -->
					</tr>
				</thead>
			</table>
		</div>


		<div class="row-fluid">
			<div class="span6" id="">
				<div class="row-fluid">
					<div class="span5" id=""
						onclick="">
						<h4 class="section-heading" style="color: black; float: left;">Parameters</h4>
					<%-- 	<img style="padding: 12px;"
							src="<c:out value="${baseUrl}"/>/public/images/icon-chevron-right.png" /> --%>
					</div>
					<div class="span2 offset2 text-right">
						<button class="btn" id="" style="margin-top: 5px;"
					onclick="com.impetus.ankush.oClusterMonitoring.loadRepNodeChildParam(repGpId,repNodeId)">Manage</button>
				</div>
					
					<div class="span3 text-right">
						<input type="text" id="repNodeDrillParamSearchBox"
							style="margin-top: 0px; width: 80%" placeholder="Search" />
					</div>
				</div>
				<div class="row-fluid">
					<table id="repNodeDrillParam" class="table table-striped"
						style="border: 1px solid; border-color: #CCCCCC">
						<thead>
							<tr style="display: none;">
								<th></th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>

			</div>
			<div class="span6" id="">
				<!-- <div class="row-fluid">
					<div class="span12">
						<h4 style="float: left; color: black;" class="section-heading">Utilization</h4>
					</div>

				</div>
				<div class="row-fluid">
					<div style="border: 1px solid #CCCCCC; height: 100px;"
						class="span12"></div>
				</div> -->
			</div>
		</div>
	</div>
</div>
