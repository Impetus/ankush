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
<!-- Hadoop Cluster's Nodes Details page containing node level tiles, Graphs, service status table, and Swap & OS details  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js/hadoop/hadoopNodeDrillDown.js"
	type="text/javascript"></script>
<script>
    $(document).ready(function() {
        $('.dropdown-toggle').dropdown();
        var nodeIp = "<c:out value='${nodeIp}'/>";
        var clusterId = "<c:out value='${clusterId}'/>";
        var nodeId = "<c:out value='${nodeId}'/>";
        com.impetus.ankush.hadoopNodeDrillDown.pageLoadFunction_NodeDrillDown(clusterId, nodeId, nodeIp);
    });
</script>
<style>
td.group {
	padding-left: 90px;
}
td.expanded-group {
	/* background: url("../../images/icon/hadoopIcon.png") no-repeat scroll 0 0 transparent; */
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.expanded-group {
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center #c0e1ff !important;
}
td.collapsed-group {
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.collapsed-group {
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center #c0e1ff !important;
}
.nodeDrillDown_Graph {
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
	<input id="nodeTileJsonVariable" style="display: none" />
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 id="nodeDrillDown_NodeIP" class="heading text-left">
				<c:out value='${nodeIp}' />
			</h2>
		</div>
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtn_NodeDrillDown"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;"></button>
		</div>
	</div>
</div>

<div class="section-body" style="margin-left: 15px;"
	id="div_HadoopNodeDrillDown">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="error-div-NodeDrillDown" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content_NodeDrillDown" style="color: red;"></span>
			</div>
		</div>
		<div style="margin-top: 15px;" class="row-fluid transitions-enabled"
			id="all_tilesNodes_NDD"></div>
		<br>
		<%@ include file="_hadoopNode_service_table.jsp"%>
		<div class="span12 text-left">
			<div style="float: left;">
				<label class="form-label section-heading" style="margin-top: 17px;">
					Utilization Graphs</label>
			</div>
			<div style="float: left;">
				<div class="form-image text-left btn-group"
					id="graphButtonGroup_JobMonitoring" data-toggle="buttons-radio"
					style="margin-left: 20px;">
					<button class="btn" id="btnLastYear_HNDD"
						onclick="com.impetus.ankush.hadoopNodeDrillDown.drawGraph_JobMonitoring('lastyear');">1y</button>
					<button class="btn" id="btnLastMonth_HNDD"
						onclick="com.impetus.ankush.hadoopNodeDrillDown.drawGraph_JobMonitoring('lastmonth');">1m</button>
					<button class="btn" id="btnLastWeek_HNDD"
						onclick="com.impetus.ankush.hadoopNodeDrillDown.drawGraph_JobMonitoring('lastweek');">1w</button>
					<button class="btn" id="btnLastDay_HNDD"
						onclick="com.impetus.ankush.hadoopNodeDrillDown.drawGraph_JobMonitoring('lastday');">1d</button>
					<button class="btn active" id="btnLastHour_HNDD"
						onclick="com.impetus.ankush.hadoopNodeDrillDown.drawGraph_JobMonitoring('lasthour');">1h</button>
				</div>
			</div>
		</div>
		<div class="row-fluid span12">
			<div class="nodeDrillDown_Graph" id="nodeDrillDown_Graph">
				<svg></svg>
			</div>
		</div>
		<br>
		<div class="row-fluid span4" style="margin-top: 5px">
			<a href="##"
				onclick="com.impetus.ankush.hadoopNodeDrillDown.showSwapOrOSDetails(<c:out value='${nodeId}'/>, 'nodeSwapDetails');">
				<div class="span2 text-left">
					<label class="form-label section-heading">Swap Details</label>
				</div>
				<div class="span1 text-left">
					<img style="margin-top: 15px;"
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
				</div>
			</a>
		</div>
		<div class="row-fluid span4">
			<a href="##"
				onclick="com.impetus.ankush.hadoopNodeDrillDown.showSwapOrOSDetails(<c:out value='${nodeId}'/>, 'nodeOsDetails');">
				<div class="span2 text-left">
					<label class="form-label section-heading">OS Details</label>
				</div>
				<div class="span1 text-left">
					<img style="margin-top: 15px;"
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
				</div>
			</a>
		</div>
	</div>
	<div id="div_RequestSuccess_NodeDD" class="modal hide fade"
		style="display: none;">
		<div class="modal-header text-center">
			<h4>Service Request</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Service update request placed successfully. This will take some
					time.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" id="divOkbtn_NodeDD" class="btn"
				onclick="com.impetus.ankush.hadoopNodeDrillDown.closeRequestDialog_RequestSuccess();">OK</a>
		</div>
	</div>
	<div id="div_RequestInvalid_NodeDD" class="box-dialog"
		style="display: none;">
		<div class="row-fluid">
			<div class="span12">
				<h4 style="text-align: center; color: black">Invalid Request</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="text-align: left;">Invalid request,
				please select valid service list.</div>
		</div>
		<br>
		<div class="row-fluid text-right">
			<button class="btn  span2 offset10" id="divOkbtn_ReqInvalid_NodeDD"
				onclick="com.impetus.ankush.hadoopNodeDrillDown.closeRequestDialog_RequestInvalid();">OK</button>
		</div>
	</div>
</div>
