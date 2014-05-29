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
<!-- Cluster Monitoring home page -->
<%@ include file="../layout/blankheader.jsp"%>
<script type="text/javascript">
	var clusterId = "<c:out value='${clusterId}'/>";
	var clusterName = "<c:out value='${clusterName}'/>";
</script>
<style>
/* td.group{padding-left: 0 0 0 8px !important;} */
td.expanded-group {
	/* background: url("../../images/icon/hadoopIcon.png") no-repeat scroll 0 0 transparent; */
	padding-left: 35px !important;
	background: url("../public/images/newUI-Icons/icon_minus.png") no-repeat
		10px center scroll transparent;
}

tr:hover td.expanded-group {
	padding-left: 35px !important;
	background: url("../public/images/newUI-Icons/icon_minus.png") no-repeat
		10px center scroll #c0e1ff !important;
}

td.collapsed-group {
	padding-left: 35px !important;
	background: url("../public/images/newUI-Icons/icon_plus.png") no-repeat
		10px center scroll transparent;
}

tr:hover td.collapsed-group {
	padding-left: 35px !important;
	background: url("../public/images/newUI-Icons/icon_plus.png") no-repeat
		10px center scroll #c0e1ff !important;
}

.shardGraph {width: 90%;min-height: 30%;}
.node{font:10px sans-serif;}
.shradSearchBox .btn-group{vertical-align:top;text-align:left;padding-right:5px;}
</style>

<script>
	$(document).ready(function() {
						$('.dropdown-toggle').dropdown();
						com.impetus.ankush.oClusterMonitoring.monitorPageLoad(clusterId, clusterName);
						com.impetus.ankush.oClusterMonitoring.initMonitorPageLoad(clusterId, clusterName);
						$("#nodeServiceDialog").appendTo('body');
						/* $("#shardMultiselect")
								.multiselect(
										{
											buttonClass : 'btn',
											maxHeight : '150',
											buttonText : function(options,
													select) {
												if (options.length == 0) {
													return 'None selected <b class="caret"></b>';
												} else if (options.length > 2) {
													return options.length
															+ ' selected <b class="caret"></b>';
												} else {
													var selected = '';
													options.each(function() {
														selected += $(this)
																.text()
																+ ', ';
													});
													return selected
															.substr(
																	0,
																	selected.length - 2)
															+ ' <b class="caret"></b>';
												}
											},
										});
					   $(".multiselect").click(function() {
							$(".dropdown-menu").toggle('fast');
						});
						$(document).click(function(e) {
							$(".dropdown-menu").hide();
						});
						$(".multiselect").click(function(e) {
							e.stopPropagation();
						});   */
						//$(".multiselect").attr('disabled', true);

						/* 	com.impetus.ankush.oClusterMonitoring.oracleMonitoringTile(clusterId);
							com.impetus.ankush.oClusterMonitoring.initializeShardNodeTable(clusterId);
							com.impetus.ankush.oClusterMonitoring.initializeStorageNodeTable(clusterId); */

					});
</script>
<div class="section-header">
	<!-- <input id="oracleDetailsJsonVariable" style="display:none"/>
<input id="shardTableJsonVariable" style="display:none"/>
<input id="storageTableJsonVariable" style="display:none"/>
<input id="treeGraphJsonVariable" style="display:none"/> -->
	<div class="row-fluid mrgt20">
		<div class="span6">
			<h2 id="clusterNameHead" class="heading">
				<c:out value='${clusterName}' />
			</h2>
		</div>
		<!-- 	<div class="actionDropDown span2" style="margin-top: 15px;">
			<ul class="nav nav-pills">

				<li class="dropdown"><a class="dropdown-toggle" id="drop5"
					role="button" data-toggle="dropdown" href="#">Actions<b
						class="caret"></b></a>
					<ul id="menu3" class="dropdown-menu" role="menu"
						aria-labelledby="drop5" style="right: 0; left: auto;"> -->
		<!-- 	<div class="btn-group actionDropDown span3 text-left" style="margin-top: 15px;">
						<button class="btn">Action</button>
						<button class="btn dropdown-toggle" data-toggle="dropdown">
						<span class="caret"></span>
						</button> -->
		<div class="span6 text-right">
			<div class="btn-group actionDropDown "
				style="margin-left: 0px;">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#"
					id="clusterActions" style="height: 24px; padding-top: 2px;">
					Actions <span class="caret"></span>
				</a>
				<ul id="menu3" class="dropdown-menu pull-right" role="menu" aria-labelledby="drop5" style="right: 0; left: auto;">
					<li><a role="menuitem" tabindex="-1" class="text-left"
						onclick="com.impetus.ankush.oClusterMonitoring.loadAddNode();"
						href="#">Add Nodes...</a></li>
					<li><a href="#" class="text-left"
						onclick="com.impetus.ankush.oClusterMonitoring.openIncreaseRepFactorDialog();">Increase
							Replication Factor...</a></li>
					<li><a href="#" class="text-left"
						onclick="com.impetus.ankush.oClusterMonitoring.commonActionDialog('Rebalance');">Rebalance</a></li>
					<li><a role="menuitem" tabindex="-1" href="#"
						class="text-left"
						onclick="com.impetus.ankush.oClusterMonitoring.commonActionDialog('Redistribute')">Redistribute</a></li>
					<li role="presentation"><a role="menuitem" tabindex="-1"
						class="text-left" href="#"
						onclick="com.impetus.ankush.oClusterMonitoring.loadVerifyLogPage()">Verify</a></li>
					<li><a class="text-left"
						onclick="com.impetus.ankush.oClusterMonitoring.loadManageConfig();"
						href="#">Manage Configuration...</a></li>
					<li><a class="text-left"
						onclick="com.impetus.ankush.oClusterMonitoring.openMigrateNodeDialog();"
						href="#">Migrate Node...</a></li>
					<li><a role="menuitem" tabindex="-1" href="#"
						class="text-left" id="deleteCluster"
						onclick="com.impetus.ankush.oClusterMonitoring.commonActionDialog('Delete Cluster');">Delete
							Store</a></li>
					<!-- <li><a role="menuitem" tabindex="-1" href="#">Stop Store</a></li> -->
				</ul>
			</div>
			<div></div>
		</div>
	</div>
</div>
<div class="section-body common-tooltip">
<div id=nodeServiceDialog class="modal hide fade" style="display: none;">
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
			<a href="#" data-dismiss="modal" class="btn">OK</a>
		</div>
	</div>
	<div id="redistributeDialog" class="box-dialog container-fluid" style="display: none;">
		<div class="row-fluid">
			<div class="span12">
				<h4 style="text-align: center; color: black">Redistribute</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="text-align: left;">
				Insufficient Capacity <br> Increase the capacity or Add Nodes
				to run Redistribute Command.
			</div>
		</div>
		<br>
		<div class="row-fluid text-right">
			<button class="btn  span2 offset7" id="okButton"
				onclick="com.impetus.ankush.oClusterMonitoring.closeDialog();">OK</button>

		</div>
	</div>

	<div class="container-fluid">
		<!-- 	<div class="row-fluid">
			<div class=" transitions-enabled" id="error_tilesO"></div>
		</div>
		<div clear="both"></div>
		<div class="row-fluid">
			<div class=" transitions-enabled" id="running_tilesO"></div>
		</div> -->
		<div class="row-fluid">
			<div class="masonry mrgt10" id="allTilesOracle"></div>
		</div>
		<div clear="both"></div>
		<div class="row-fluid">
			<div class="span4">
				<h4 class="label-black" style="font-size: 14px;">Topology</h4>
				<div class="row-fluid shardGraph" id="shardGraphDiv"
					style="margin-top: 30px; margin-bottom: 30px;"></div>
			</div>
			
				<div class="span8 shardsblo">
					<div class="row-fluid" id="shardNodeTableHeaderDiv">
						<div class="span6">
							<div class="row-fluid">
								<div class="span10 minwidth320">
									<div class="btn-group startShardDropDown"
										style="float: left;">
										<button class="btn"
											onclick="com.impetus.ankush.oClusterMonitoring.startRepNode(clusterId,'repNodeCheckBox');">Start</button>
										<button class="btn dropdown-toggle" data-toggle="dropdown">
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">

											<li><a tabindex="-1" href="#"
												onclick="com.impetus.ankush.oClusterMonitoring.startRepNode(clusterId,'repNodeCheckBox');">Start</a></li>
											<li><a tabindex="-1" href="#"
												onclick="com.impetus.ankush.oClusterMonitoring.startAllRepNodes(clusterId,'repNodeCheckBox');">Start
													All</a></li>
										</ul>
									</div>
									<div class="btn-group startShardDropDown"
										style="float: left; margin-left: 8px;">
										<button class="btn"
											onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId,'repNodeCheckBox');">Stop</button>
										<button class="btn dropdown-toggle" data-toggle="dropdown">
											<span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li><a tabindex="-1" href="#"
												onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId,'repNodeCheckBox');">Stop</a></li>
											<li><a tabindex="-1" href="#"
												onclick="com.impetus.ankush.oClusterMonitoring.stopAllRepNodes(clusterId,'repNodeCheckBox');">Stop
													All</a></li>
										</ul>
									</div>

								</div>
								<div style="clear: both;"></div>
								<!-- <div class="startShardDropDown span3">
						<ul class="nav nav-pills">
							<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Start<b	class="caret"></b></a>
								<ul id="menu3" class="dropdown-menu">
									<li><a tabindex="-1" href="#" onclick="com.impetus.ankush.oClusterMonitoring.startRepNode(clusterId);">Start</a></li>
									<li><a tabindex="-1"href="#" onclick="com.impetus.ankush.oClusterMonitoring.startAllRepNodes(clusterId);">Start All</a></li>
								</ul>
							</li>
						</ul>
					</div> -->
								<!-- <div class="stopShardDropDown span3">
						<ul class="nav nav-pills">
							<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Stop<b class="caret"></b></a>
								<ul id="menu3" class="dropdown-menu">
									<li><a tabindex="-1" href="#" onclick="com.impetus.ankush.oClusterMonitoring.stopRepNode(clusterId);">Stop</a></li>
									<li><a tabindex="-1"href="#" onclick="com.impetus.ankush.oClusterMonitoring.stopAllRepNodes(clusterId);">Stop All</a></li>
								</ul>
							</li>
						</ul>
					</div> -->
							</div>
						</div>
						<!--<div class="span3">


						 	<select class="multiSelect" id="shardMultiselect"
								data-placement="right" data-toggle="tooltip"
								title="Select nodes for HRegion Servers"
								data-bind="multiselect: true, options: Options, selectedOptions: SelectedOptions, optionsValue: $data"
								multiple="multiple">
								<option value="singleInt">singleInt</option>
								<option value="singleCum">singleCum</option>
								<option value="multiInt">multiInt</option>
								<option value="multiCum">multiCum</option>
							</select> 
						</div>-->
						<div class="span6  text-right">
								
							<div class="shradSearchBox">
						<!-- 	<select id="shardNodeTableColumns" multiple="multiple">
							<option value="4">Port</option>
							<option value="5" selected>Single-Throughput</option>
							<option value="6" selected>Single-Avg</option>
							<option value="7" selected>Single-TotalOps</option>
							<option value="8">SingleCum-Throughput</option>
							<option value="9">SingleCum-Avg</option>
							<option value="10">SingleCum-TotalOps</option>
							<option value="11">Multi-Throughput</option>
							<option value="12">Multi-Avg</option>
							<option value="13">Multi-TotalOps</option>
							</select> -->
							
							<input type="text" id="shardNodeSearchBox"
								style="margin-top: 0;" placeholder="Search" class="smallinputhgt" />
							</div>
						
						
						</div>
					</div>
					<div id="shardNodeTableDiv">
						<table class="table table-striped" id="shardNodeTable"
							style="border: 1px solid; border-color: #CCCCCC; width: 100%">
							<thead>
								<tr>
									<th></th>
									<th style=""><input type="checkbox" id="repNodeCheckBox"
										style=""
										onclick="com.impetus.ankush.oClusterMonitoring.checkedAllNodes('repNodeCheckBox')" />
										<span style="margin: 5px; margin-left: 10px; font-size: 12px;">Shards</span></th>
									<th>Replica-Node</th>
									<th>Storage Node</th>
									<th>Port</th>
									<th>Single-Throughput</th>
									<th>Single-Avg</th>
									<th>Single-TotalOps</th>
									<th>SingleCum-Throughput</th>
									<th>SingleCum-Avg</th>
									<th>SingleCum-TotalOps</th>
									<th>Multi-Throughput</th>
									<th>Multi-Avg</th>
									<th>Multi-TotalOps</th>
									<th></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			
		</div>
		<br style="line-height: 30px;">

		<!-- <div id="tileViewDetail3"></div>
			<div style="clear: both;margin-bottom: 10px;margin-top: 10px"></div>
			<div id="tileViewDetail"></div>
			
			<div style="clear: both;margin-bottom: 50px;"></div> -->
		<div class="row-fluid">
			<div class="span4 ">
				<h4 class="section-heading"
					style="text-align: left; cursor: pointer;">
					<a
						onclick="com.impetus.ankush.oClusterMonitoring.loadUtilizationGraphPage();"
						href="#"> Utilization Trends&nbsp;&nbsp;&nbsp;&nbsp;<img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png"></a>
				</h4>
			</div>
			<!--    <div class="shardGraph" id="shardGraphDiv"></div> -->
		</div>


		<div style="clear: both;"></div>
		

		<div id="storageNodeTableHeaderDiv">
			<div class="row-fluid">
				<div class="span6">
					<div class="row-fluid">
						<div class="span10 minwidth320">
							<h4
								style="line-height: 10px; float: left; color: black; font-size: 14px; font-weight: bold;">Storage
								Nodes</h4>
							<div class="btn-group startStorageDropDown"
								style="float: left; margin-left: 20px;">
								<button class="btn"
									onclick="com.impetus.ankush.oClusterMonitoring.startStorageNode(clusterId,'storageNodeCheckBox');">Start</button>
								<button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.startStorageNode(clusterId,'storageNodeCheckBox');">Start</a></li>
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.startAllStorageNodes(clusterId,'storageNodeCheckBox');">Start
											All</a></li>
								</ul>
							</div>

							<div class="btn-group stopStorageDropDown"
								style="float: left; margin-left: 8px;">
								<button class="btn"
									onclick="com.impetus.ankush.oClusterMonitoring.stopStorageNode(clusterId,'storageNodeCheckBox');">Stop</button>
								<button class="btn dropdown-toggle" data-toggle="dropdown">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.stopStorageNode(clusterId,'storageNodeCheckBox');">Stop</a></li>
									<li><a tabindex="-1" href="#"
										onclick="com.impetus.ankush.oClusterMonitoring.stopAllStorageNodes(clusterId,'storageNodeCheckBox');">Stop
											All</a></li>
								</ul>
							</div>
							<div style="clear: both;"></div>
						</div>
						<div style="clear: both;"></div>
						<!-- <div class="startStorageDropDown span3">
							<ul class="nav nav-pills">
								<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Start<b	class="caret"></b></a>
									<ul id="menu3" class="dropdown-menu">
										<li><a tabindex="-1" href="#" onclick="com.impetus.ankush.oClusterMonitoring.startStorageNode(clusterId,'storageNodeCheckBox');">Start</a></li>
										<li><a tabindex="-1"href="#" onclick="com.impetus.ankush.oClusterMonitoring.startAllStorageNodes(clusterId,'storageNodeCheckBox');">Start All</a></li>
									</ul>
								</li>
							</ul>
						</div> -->
						<!-- <div class="stopStorageDropDown span3">
							<ul class="nav nav-pills">
								<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Stop<b class="caret"></b></a>
									<ul id="menu3" class="dropdown-menu">
										<li><a tabindex="-1" href="#" onclick="com.impetus.ankush.oClusterMonitoring.stopStorageNode(clusterId);">Stop</a></li>
										<li><a tabindex="-1"href="#" onclick="com.impetus.ankush.oClusterMonitoring.stopAllStorageNodes(clusterId);">Stop All</a></li>
									</ul>
								</li>
							</ul>
						</div> -->
					</div>
				</div>
				<div class="span6 text-right">
					<input type="text" id="storageNodeSearchBox" style="margin-top: 0;"
						placeholder="Search" class="smallinputhgt" />
				</div>
			</div>
		</div>
		<div id="storageNodeTableDiv">
			<table class="table table-striped" id="storageNodeTable"
				style="border: 1px solid; border-color: #CCCCCC; width: 100%">
				<thead>
					<tr>
						<th><input type="checkbox" id="storageNodeCheckBox"
							class="storageNodeChecked"
							onclick="com.impetus.ankush.oClusterMonitoring.checkedAllNodes('storageNodeCheckBox')" /></th>
						<th>IP</th>
						<th>Rep.Nodes</th>
						<th>Capacity</th>
						<th>Port</th>
						<th>Admin</th>
						<th></th>

					</tr>
				</thead>
			</table>
		</div>

		<div id="actionDialog" class="modal hide fade" style="display: none;">
			<div class="modal-header text-center">
				<h4 class="dialog-heading" id="actionDialogHeader"></h4>
			</div>





			<!-- <div class="span12">
					<h4 style="text-align: center; color: black">Cluster Delete</h4>
				</div> -->

			<div class="modal-body">
				<div class="row-fluid">
					<div class="span11" style="text-align: left;" id="actionDialogBody">
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal"
					onclick="com.impetus.ankush.oClusterMonitoring.commonDialogCancel();"
					class="btn">Cancel</a> <a href="#" id="confirmActionButton"
					class="btn">Confirm</a>

			</div>

			<!-- <div class="row-fluid text-right">
			<button class="btn span3 offset5" id="cancelActionButton" onclick="com.impetus.ankush.oClusterMonitoring.cancelActionDialog('actionDialog');">Cancel</button>
				<button class="btn  span3" id="confirmActionButton"
					onclick="">Confirm</button>
				
			</div> -->
		</div>



		<div id="actionErrorDialog" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center"></div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span11"
						style="text-align: left; background-color: #F2DEDE; color: #B94A48; padding: 5px;"
						id="actionErrorDiv"></div>
				</div>
			</div>

			<div class="modal-footer">
				<a href="#" data-dismiss="modal"
					class="btn">Ok</a>
			</div>

			<!-- <div class="row-fluid text-right">
				<button class="btn span2 offset9" id="confirmActionErrorButton" onclick="com.impetus.ankush.oClusterMonitoring.cancelActionDialog('actionErrorDialog')">OK</button>
			</div> -->
		</div>

		<%@ include file="../oClusterMonitoring/_increaseRepFactor.jsp"%>
		<!-- Migrate Node Dialog Box---------- -->
		<div id="migrateNodeDialog" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4 class="dialog-heading">Migrate Node</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span3 text-right">
						<label class="form-label">Old Storage Nodes</label>
					</div>
					<div class="span2 text-left">
						<select id="oldStorageNodeSelectBox" style="width: 140px;"></select>
					</div>
					<div class="span3 text-right offset1">
						<label class="form-label">New Storage Nodes</label>
					</div>
					<div class="span2 text-left">
						<select id="newStorageNodeSelectBox" style="width: 140px;"></select>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<a href="#" data-dismiss="modal" id="" class="btn">Cancel</a> <a
					href="#" id="migrateButton"
					onclick="com.impetus.ankush.oClusterMonitoring.migrateNode();"
					class="btn">Migrate</a>
			</div>

			<!-- <div class="row-fluid" style="margin-top:30px;">
				<div class="span2 offset8"><button id="migrateButton" class="btn" onclick="com.impetus.ankush.oClusterMonitoring.migrateNode();">Migrate</button></div>
				<div class="span2 text-left"><button class="btn" onclick="com.impetus.ankush.oClusterMonitoring.closeMigrateNodeDialog();">Cancel</button></div>
			</div> -->
		</div>

		<br>
		<div class="row-fluid">
			<div class="span8" id="planLink"
				onclick="com.impetus.ankush.oClusterMonitoring.loadPlanHistoryPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="#">Plan History</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
				<div style="clear: both;"></div>
			</div>

			<%-- 	<div class="span1">
			<a style="line-height: 40px;" href="##"
				onclick="com.impetus.ankush.oClusterMonitoring.loadPlanHistoryPage();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div> --%>
		</div>

		<div class="row-fluid">
			<div class="span8 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadStoreEventPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="#">Store Events</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
				<div style="clear: both;"></div>
			</div>

			<%-- <div class="span1">
			<a style="line-height: 40px;" href="##"
				onclick="com.impetus.ankush.oClusterMonitoring.loadStoreEventPage();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div> --%>
		</div>

		<div class="row-fluid">
			<div class="span8 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadEventPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="#">Events</a>
					</h4>
				</div>

				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>

				</div>
				<div style="clear: both;"></div>
			</div>

			<%-- <div class="span1">
			<a style="line-height: 40px;" href="##"
				onclick="com.impetus.ankush.oClusterMonitoring.loadEventPage();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div> --%>
		</div>

		<div class="row-fluid">
			<div class="span8"
				onclick="com.impetus.ankush.oClusterMonitoring.loadLogsPage();">
				<div style="float: left;">

					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" # href="#">Logs</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
				<div style="clear: both;"></div>
			</div>
			<%-- <div class="span1">
			<a style="line-height: 40px;" href="##"
				onclick="com.impetus.ankush.oClusterMonitoring.loadLogsPage();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div> --%>
		</div>

		<div class="row-fluid">
			<div class="span8 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadManageConfig();">

				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="##">Configurations</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
				<div style="clear: both;"></div>
			</div>
		</div>
		<%-- <div class="span1">
			<a style="line-height: 40px;" href="##" onclick="com.impetus.ankush.oClusterMonitoring.loadManageConfig();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div> --%>

		<div class="row-fluid">
			<div class="span8 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadAuditTrailPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="#">Audit Trail</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
				<div style="clear: both;"></div>


			</div>
			<%-- <div class="span1">
			<a style="line-height: 40px;" href="##"
				onclick="com.impetus.ankush.oClusterMonitoring.loadAuditTrailPage();"><img
				src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
		</div> --%>
		</div>
	</div>
</div>
