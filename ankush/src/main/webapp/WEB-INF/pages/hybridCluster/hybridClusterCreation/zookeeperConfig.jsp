<!------------------------------------------------------------------------------
-  ===========================================================
-  Ankush : Big Data Cluster Management Solution
-  ===========================================================
-  
-  (C) Copyright 2014, by Impetus Technologies
-  
-  This is free software; you can redistribute it and/or modify it under
-  the terms of the GNU Lesser General Public License (LGPL v3) as
-  published by the Free Software Foundation;
-  
-  This software is distributed in the hope that it will be useful, but
-  WITHOUT ANY WARRANTY; without even the implied warranty of
-  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-  See the GNU Lesser General Public License for more details.
-  
-  You should have received a copy of the GNU Lesser General Public License 
-  along with this software; if not, write to the Free Software Foundation, 
- Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->

<html>
<head>
<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}'/>/public/js3.0/tooltip/zookeeperClusterCreationTooltip.js"
	type="text/javascript">
	
</script>
</head>
<div class="">
	<div class="">
		<div class="">
			<div class="col-md-4">
				<h2 id="zookeeperConfigHeader" style="display: none;">Configuration</h2>
			</div>
			<div class="col-md-1"></div>
			<div class="col-md-7 text-right mrgt20 padr45"></div>
		</div>
	</div>
	<div class="" id="main-content">
		<div class="container-fluid mrgnlft8">
			<div class="row"></div>

			<div class="panel ">
				<div class="panel-heading">
					<div class="">
						<h2 id="zookeeperConfigHeader1" class="panel-title col-md-2 mrgt5">Zookeeper/Configuration</h2>
						<button id="revertZookeeper" class="btn btn-default"
							onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
						<button class="btn btn-default" id="applyZookeeper"
							onclick="com.impetus.ankush.hybrid_Zookeeper.zookeeperConfigValidate();">Apply</button>
					</div>
					<div class="row">
						<div id="errorDivMainZookeeper" class="errorDiv mrgt10"
							style="display: none;"></div>
					</div>
				</div>
				<div class="row panel-body">
					<div class="row">
						<div class=" col-md-2 text-right ">
							<label class=" form-label">Vendor:</label>
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<select id="zookeeperVendorDropdown" class="form-control"
								title="Select Zookeeper vendor."
								onchange="com.impetus.ankush.hybrid_Zookeeper.vendorOnChangeZookeeper();"
								data-placement="right"></select>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right ">
							<label class=" form-label">Version:</label>
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<select id="zookeeperVersionDropdown" class="form-control"
								title="Select Zookeeper version."
								onchange="com.impetus.ankush.hybrid_Zookeeper.versionChangeZookeeper();"
								data-placement="right"></select>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">Source:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group ">
							<div class="btn-group" id="sourcePathBtnGrp"
								data-toggle="buttons-radio">
								<button class="btn btn-default nodeListRadio active btnGrp"
									data-value="0" id="zookeeperDownloadRadio"
									onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('zookeeperDownloadPathDiv','zookeeperLocalPathDiv');">Download</button>
								<button class="btn btn-default nodeListRadio btnGrp"
									data-value="1" id="zookeeperLocalRadio"
									onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('zookeeperLocalPathDiv','zookeeperDownloadPathDiv');">Local
								</button>
							</div>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right"></div>
						<div class="col-md-5 col-lg-4 form-group"
							id="zookeeperDownloadPathDiv">
							<input id="zookeeperDownloadPath" type="text"
								class="input-xlarge form-control" placeholder="Download Path"
								title="Enter download path of zookeeper binary file"
								data-placement="right"></input>
						</div>
						<div class="col-md-5 col-lg-4 form-group" style="display: none;"
							id="zookeeperLocalPathDiv">
							<input id="zookeeperLocalPath" type="text"
								class="input-xlarge form-control" placeholder="Source  Path"
								title="Enter local path of zookeeper binary file"
								data-placement="right" value=""></input>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">Installation Path:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group">
							<input id="installationPathZookeeper" type="text"
								class="input-xlarge form-control"
								placeholder="Installation Path"
								title="Enter installation path for zookeeper"
								data-placement="right"></input>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">Data Path:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group">
							<input id="dataDirZookeeper" type="text"
								class="input-xlarge form-control	"
								placeholder="Data Directory Path"
								title="Enter data directory path for zookeeper"
								data-placement="right"></input>
						</div>
					</div>

					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">Client Port:</label>
						</div>
						<div class="col-md-2 col-lg-1 form-group">
							<input id="clientPort" type="text"
								class="input-mini form-control" placeholder="Client Port"
								title="Enter client port" data-placement="right"></input>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">JMX Port:</label>
						</div>
						<div class="col-md-2 col-lg-1 form-group">
							<input id="jmxPort" type="text" class="input-mini form-control"
								placeholder="JMX Port" title="Enter jmx port"
								data-placement="right"></input>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">Sync Limit:</label>
						</div>
						<div class="col-md-2 col-lg-1 form-group">
							<input id="syncLimit" type="text" class="input-mini form-control"
								placeholder="Sync Limit" title="Enter sync limit"
								data-placement="right"></input>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Init Limit:</label>
						</div>
						<div class="col-md-2 col-lg-1 form-group">
							<input id="initLimit" type="text" class="input-mini form-control"
								placeholder="Init Limit" title="Enter init limit"
								data-placement="right"></input>
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">Tick Time:</label>
						</div>
						<div class="col-md-2 col-lg-1 form-group">
							<input id="tickTime" type="text" class="input-mini form-control"
								placeholder="Tick Time" title="Enter tick-time"
								data-placement="right"></input>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(
			function() {
				var technology = null;
				technology = "<c:out value='${technology}' />";
				com.impetus.ankush.hybrid_Zookeeper
						.zookeeperConfigPopulate(technology);
				$("#zookeeperVendorDropdown").focus();
			});
</script>
</html>
