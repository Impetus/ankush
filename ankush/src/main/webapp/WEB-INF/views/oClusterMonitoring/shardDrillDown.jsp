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
<!-- Page for shard table -->
<%@ include file="../layout/blankheader.jsp"%>
<script type="text/javascript">
	var clusterId = "<c:out value='${clusterId}'/>";
	var clusterName = "<c:out value='${clusterName}'/>";
	$('.dropdown-toggle').dropdown();
</script>
<style>
td.group {
	padding-left: 90px;
}

td.expanded-group {
	padding-left: 45px !important;
	/* background: url("../../images/icon/hadoopIcon.png") no-repeat scroll 0 0 transparent; */
	background: url("../public/images/newUI-Icons/icon_minus.png") no-repeat
		10px center scroll transparent;
}

tr:hover td.expanded-group {
	padding-left: 45px !important;
	background: url("../public/images/newUI-Icons/icon_minus.png") no-repeat
		10px center scroll #c0e1ff !important;
}

td.collapsed-group {
	padding-left: 45px !important;
	background: url("../public/images/newUI-Icons/icon_plus.png") no-repeat
		10px center scroll transparent;
}

tr:hover td.collapsed-group {
	padding-left: 45px !important;
	background: url("../public/images/newUI-Icons/icon_plus.png") no-repeat
		10px center scroll #c0e1ff !important;
}

.shardTreeGraphDiv {
	width: 90%;
	min-height: 30%;
	.
	node
	{
	font
	:
	10px
	sans-serif;
}
}
</style>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span10">
			<h2 id="shardNameHead" class="heading"></h2>
		</div>

	</div>
</div>
<div class="section-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="shardTreeGraphDiv" id="shardTreeGraphDiv"
				style="margin-top: 30px; margin-bottom: 30px;"></div>
		</div>
		<div id="shardDrillTableHeaderDiv">
			<div class="row-fluid">
				<div class="span4">
					<div class="row-fluid">
						<div class="span10">
							<h4 style="line-height: 12px; float: left; color: black;"
								class="section-heading">Shards</h4>

							<div class="btn-group startShardDropDown"
								style="float: left; margin-left: 20px;">
								<button class="btn"
									onclick="com.impetus.ankush.oClusterMonitoring.startRepNode(clusterId,'repNodeDrillCheckBox');">Start</button>
								<button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">

									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.startRepNode(clusterId,'repNodeDrillCheckBox');">Start</a></li>
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.startAllRepNodes(clusterId,'repNodeDrillCheckBox');">Start
											All</a></li>
								</ul>
							</div>
							<div class="btn-group startShardDropDown"
								style="float: left; margin-left: 8px;">
								<button class="btn"
									onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId,'repNodeDrillCheckBox');">Stop</button>
								<button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId,'repNodeDrillCheckBox');">Stop</a></li>
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.stopAllRepNodes(clusterId,'repNodeDrillCheckBox');">Stop
											All</a></li>
								</ul>
							</div>
						</div>
						<div style="clear: both;"></div>
					</div>
				</div>
				<div class="span3 offset5 text-right">
					<input type="text" id="shardDrillSearchBox" style="margin-top: 0"
						placeholder="Search" />
				</div>
			</div>
		</div>
		<div id="shardDrillTableDiv">
			<table class="table table-striped" id="shardDrillTable"
				style="border: 1px solid; border-color: #CCCCCC; width: 100%">
				<thead>
					<tr>
						<th></th>
						<th style=""><input type="checkbox"
							id="repNodeDrillCheckBox" style=""
							onclick="com.impetus.ankush.oClusterMonitoring.checkedAllNodes('repNodeDrillCheckBox')" /><span style="margin: 5px;margin-left: 25px;">Shards</span></th>
						<th>Replica Node</th>
						<th>Storage Node</th>
						<th>Port</th>
						<th>95 %</th>
						<th>Throughput</th>
						<th>Latency</th>
						<!-- <th>Multi-Throughput</th> -->
					<!-- 	<th>Multi-Latency</th> -->
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>
