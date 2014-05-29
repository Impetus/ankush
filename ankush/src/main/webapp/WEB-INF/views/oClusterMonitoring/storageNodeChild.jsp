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
<!-- Page for storage node table child drill down -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	var storageNodeId = '<c:out value="${storageNodeId}"/>';
	var storageNodeIP = '<c:out value = "${storageNodeIP}"/>';
	$('#storageNodeIP').html(storageNodeIP);
	$(document).ready(function() {
		$('.dropdown-toggle').dropdown();
	});
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4 text-left">
			<h2 id="storageNodeIP" class="form-label heading"></h2>
		</div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid">
		<div class="row-fluid transitions-enabled" id="error_tilesNode">
		</div>
		<br clear="both" />
		<div class="row-fluid transitions-enabled" id="running_tilesNode">
		</div>
		<div class="row-fluid" style="margin-top: 30px; margin-bottom: 30px;">
			<div class="storageNodeShrad span6" id="storageNodeShrad"></div>

			<!-- <div class="row-fluid">
			
		</div> -->
			<div class="span6">
				<div id="repNodeTableHeaderDiv" class="row-fluid">
					<div class="span9">
						<div class="row-fluid">
							<div class="span10">
								<h4 style="line-height: 12px; float: left; color: black;"
									class="section-heading">Replication Nodes</h4>

								<div class="btn-group startStorageDropDown"
									style="float: left; margin-left: 20px;">
									<button class="btn"
										onclick="com.impetus.ankush.oClusterMonitoring.startRepNode(clusterId,'storageNodeChildCheckBox');">Start</button>
									<button class="btn dropdown-toggle" data-toggle="dropdown">
										<span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a tabindex="-1" href="#"
											onclick="com.impetus.ankush.oClusterMonitoring.startRepNode(clusterId,'storageNodeChildCheckBox');">Start</a></li>
										<li><a tabindex="-1" href="#"
											onclick="com.impetus.ankush.oClusterMonitoring.startAllRepNodes(clusterId,'storageNodeChildCheckBox');">Start
												All</a></li>
									</ul>
								</div>
								<div class="btn-group stopStorageDropDown"
									style="float: left; margin-left: 8px;">
									<button class="btn"
										onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId,'storageNodeChildCheckBox');">Stop</button>
									<button class="btn dropdown-toggle" data-toggle="dropdown">
										<span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a tabindex="-1" href="#"
											onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId,'storageNodeChildCheckBox');">Stop</a></li>
										<li><a tabindex="-1" href="#"
											onclick="com.impetus.ankush.oClusterMonitoring.stopAllRepNodes(clusterId,'storageNodeChildCheckBox');">Stop
												All</a></li>
									</ul>
								</div>
							</div>
							<div style="clear: both;"></div>
						</div>
					</div>
					<div class="span3 text-right">
						<input type="text" id="storageNodeChildSearchBox"
							style="margin-top: 0px; width: 80%" placeholder="Search" />
					</div>
				</div>
				<div class="row-fluid">
					<table id="replicationNode" class="table table-striped"
						style="border: 1px solid; border-color: #CCCCCC">
						<thead>
							<tr>
								<th></th>
								<th>Replication Node</th>
								<th>Shards</th>
								<th>Master</th>
								<th>Port</th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<%-- <div class="row-fluid">
			<div class="span10 text-left"
				onclick="com.impetus.ankush.oClusterMonitoring.loadStorageNodeChildParam(storageNodeId)">
				<h4 class="section-heading" style="color: black; float: left;">Parameters</h4>
				<div class="span2 text-left">
					<a href="#" style="line-height: 40px; float: left;"> <img
						src="<c:out value="${baseUrl}"/>/public/images/icon-chevron-right.png" />
					</a>
				</div>
			</div>
			<div style="clear: both;"></div>
		</div> --%>
		<br>

		<div class="row-fluid">
			<div class="span6" >
				<div class="row-fluid">
			<div class="span5 text-left"
				onclick="">
				<h4 class="section-heading" style="color: black; float: left;">Parameters</h4>
				<div class="span2 text-left">
					<%-- <a href="#" style="line-height: 40px; float: left;"> <img
						src="<c:out value="${baseUrl}"/>/public/images/icon-chevron-right.png" />
					</a> --%>
				</div>
			</div>
			
				<div class="span2 offset2 text-right">
						<button class="btn" id="" style="margin-top: 5px;"
					onclick="com.impetus.ankush.oClusterMonitoring.loadStorageNodeChildParam(storageNodeId)">Manage</button>
				</div>
			<div class="span3  text-right">
						<input type="text" id="storageNodeDrillParamSearchBox"
							style="margin-top: 0px; width: 80%" placeholder="Search" />
					</div>
			
			</div>
			<div class="row-fluid">
					<table id="storageNodeDrillParam" class="table table-striped"
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
			<div class="span6">
			<div class="row-fluid">
			<div class="span4">
				<!-- <h4 class="section-heading label-black">Utilization Graphs</h4> -->
				<h4 style="text-align: left; cursor: pointer;"
					class="section-heading">
					<a href="#" class="label-black"
						onclick="com.impetus.ankush.oClusterMonitoring.nodeUtilizationTrend();">
						Utilization Trends&nbsp;&nbsp;&nbsp;&nbsp;<img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
					</a>
				</h4>
			</div>
			<div class="span3">
				<div class="btn-group" style="display: none;" id="graphButtonGroup">
					<button class="btn"
						onclick="com.impetus.ankush.oClusterMonitoring.graphDraw('lastyear');">1y</button>
					<button class="btn"
						onclick="com.impetus.ankush.oClusterMonitoring.graphDraw('lastmonth');">1m</button>
					<button class="btn"
						onclick="com.impetus.ankush.oClusterMonitoring.graphDraw('lastweek');">1w</button>
					<button class="btn"
						onclick="com.impetus.ankush.oClusterMonitoring.graphDraw('lastday');">1d</button>
					<button class="btn"
						onclick="com.impetus.ankush.oClusterMonitoring.graphDraw('lasthour');">1h</button>
				</div>
			</div>
			<%-- <div class="span2 text-left">
				<h4 style="text-align: left; cursor: pointer;"
					class="section-heading">
					<a href="#" class="label-black"
						onclick="com.impetus.ankush.oClusterMonitoring.nodeUtilizationTrend();">
						Utilization Trends&nbsp;&nbsp;&nbsp;&nbsp;<img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
					</a>
				</h4>
			</div>
 --%>		</div>
				<div id="graphErrorDiv">
					<div class="storageNodeStack" id="storageNodeStack">

						<svg></svg>
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid"></div>
	</div>
</div>
