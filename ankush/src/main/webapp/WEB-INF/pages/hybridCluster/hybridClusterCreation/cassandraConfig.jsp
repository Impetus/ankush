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

<%@ include file="../../layout/blankheader.jsp"%>
<head>
<script
	src="<c:out value='${baseUrl}'/>/public/js3.0/tooltip/cassandraClusterCreationTooltip.js"
	type="text/javascript"></script>
</head>
<body>
	<div class="">
		<!-- header section -->
		<div class="">
			<div class="">
				<div class="col-md-4">
					<!-- <h2>Cassandra/Configuration</h2> -->
				</div>
				<!-- <div class="col-md-1">
					<button class="btn btn-default btn-danger mrgt20" id="validateErrorCassandra"
						onclick="com.impetus.ankush.common.focusError();"
						style="display: none; padding: 0 15px; left: 15px; position: relative"></button>
				</div>
				<div class="col-md-7 text-right mrgt20 padr45">
					<button id="cassandraRevertBtn" class="btn btn-default"
						onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
					<button class="btn btn-default" id="cassandraApplyBtn"
						onclick="com.impetus.ankush.hybrid_Cassandra.cassandraConfigValidate();">Apply</button>
				</div> -->
			</div>
		</div>
		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row mrgb20" style="">
					<!-- <div id="errorDivMainCassandra" class="col-md-12 errorDiv"
						style="display: none;"></div> -->
				</div>
				<div class="panel ">
					<div class="panel-heading">
						<div class="">
							<h3 class="panel-title col-md-2 mrgt5">Cassandra/Configuration</h3>
						<!-- 	<button class="btn btn-default btn-danger"
								id="validateErrorCassandra"
								onclick="com.impetus.ankush.common.focusError();"
								style="display: none;"></button> -->
							<button id="cassandraRevertBtn" class="btn btn-default"
								onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
							<button class="btn btn-default" id="cassandraApplyBtn"
								onclick="com.impetus.ankush.hybrid_Cassandra.cassandraConfigValidate();">Apply</button>
						</div>
						<div class="row">
					<div id="errorDivMainCassandra" class="col-md-12 errorDiv"
						style="display: none;"></div>
				</div>
						<!-- <div class="pull-right panelSearch">
							<input type="text" id="nodeSearchBoxCasandra"
								placeholder="Search" class="input-medium form-control" />
						</div> -->
					</div>
					<div class="row panel-body">
						<!-- <div class="col-md-12" style="">
							<table class="table table-striped tblborder1"
								id="cassandraNodeTable">
								<thead class="tblborder2">
									<tr>
										<th><input type='checkbox' id='nodeCheckCassandraHead'
											onclick="com.impetus.ankush.hybrid_Cassandra.checkAllNodes('nodeCheckCassandraHead','cassandraNodeCheckBox')"></th>
										<th>IP</th>
										<th>Node Roles</th>
										<th>SeedNode</th>
										<th>OS</th>
										<th>VNode Count</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div> -->



						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblVendor-Cassandra" class="form-label">Vendor:</label>
							</div>
							<div class="col-md-2 col-lg-2 form-group">
								<select id="vendorDropdown" title="Select Cassandra Vendor"
									class="form-control" data-placement="right"
									onclick="com.impetus.ankush.hybrid_Cassandra.vendorOnChangeCassandra();">

								</select>
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblVersion-Cassandra" class=" form-label">Version:</label>
							</div>
							<div class="col-md-2 col-lg-2 form-group">
								<select id="versionDropdown" title="Select Cassandra Vendor"
									class="form-control" data-placement="right"
									onclick="com.impetus.ankush.hybrid_Cassandra.versionOnChangeCassandra();">

								</select>
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblBundleSource-Cassandra" class=" form-label ">Source:</label>
							</div>
							<div class="col-md-10 col-lg-2 form-group ">
								<div class="btn-group" data-toggle="buttons-radio"
									id="sourcePathBtnGrp">
									<button class="btn btn-default nodeListRadio active btnGrp"
										data-value="0" id="bundleSourceDownload-Cassandra"
										name="sourcePathBtnCassandra"
										onclick="com.impetus.ankush.common.buttonClick('downloadPathDiv','localPathDiv');">Download</button>
									<button class="btn btn-default nodeListRadio btnGrp"
										data-value="1" id="bundleSourceLocalPath-Cassandra"
										onclick="com.impetus.ankush.common.buttonClick('localPathDiv','downloadPathDiv');">Local
									</button>
								</div>
							</div>
						</div>
						<div class="row" id="downloadPathDiv">
							<div class="col-md-2"></div>
							<div class="col-md-5 col-lg-4 form-group">
								<input type="text" name="downloadPath" id="downloadPath"
									class="input-xlarge form-control" placeholder="Download Path"
									title="Download Path" data-placement="right">
							</div>
						</div>
						<div class="row" id="localPathDiv" style="display: none;">
							<div class="col-md-2"></div>

							<div class="col-md-5 col-lg-4 form-group ">
								<input type="text" name="localPath" id="localPath"
									class="input-xlarge form-control" placeholder="Local Path"
									title="Local Path" data-placement="right">
							</div>
						</div>

						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblinstallationPath-Casandra" class=" form-label">Installation
									Path:</label>
							</div>
							<div class="col-md-5 col-lg-4 form-group">
								<input type="text" value="" name="inputInstallionPath"
									id="installationPathCassandra"
									class="input-xlarge form-control"
									placeholder="Installation Path" title="Installation Path"
									data-placement="right">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblPartitioner" class=" form-label">Partitioner:</label>
							</div>
							<div class="col-md-2 col-lg-2 form-group">
								<select id="partitionerDropDown" name="" class="form-control"
									title="The partitioner is responsible for distributing rows (by key) across nodes in the cluster."
									data-placement="right">
								</select>
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblSnitch" class=" form-label">Snitch:</label>
							</div>
							<div class="col-md-2 col-lg-2 form-group">
								<select id="snitchDropDown" name="" data-placement="right"
									class="form-control"
									title="A snitch maps IPs to racks and data centers. It defines how the nodes are grouped together within the overall network topology.">
								</select>
							</div>
						</div>
						<div class="row" style="display: none;">
							<div class="col-md-2 text-right">
								<label id="lblVirtualNode" class=" form-label">Virtual
									Node:</label>
							</div>
							<div class="col-md-10 col-lg-2 form-group">
								<input type="checkbox" checked="checked"
									id="vNodecol-md-10 col-lg-2 form-group"
									style="margin-top: 15px;" value="" name="">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblRPCPort" class=" form-label">RPC Port:</label>
							</div>
							<div class="col-md-2 col-lg-1 form-group">
								<input type="text" id="rpcPort" class="input-mini form-control"
									placeholder="RPC Port" title="RPC Port" data-placement="right">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblStoragePort" class=" form-label">Storage
									Port:</label>
							</div>
							<div class="col-md-2 col-lg-1 form-group">
								<input type="text" id="storagePort"
									class="input-mini form-control" placeholder="Storage Port "
									title="Storage Port" data-placement="right">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblDataDir" class=" form-label">Data
									Directory:</label>
							</div>
							<div class="col-md-5 col-lg-4 form-group">
								<input type="text" id="dataDir"
									class="input-xlarge form-control" placeholder="Data Directory "
									title="Data Directory" data-placement="right">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblLogDir" class=" form-label">Log Directory:</label>
							</div>
							<div class="col-md-5 col-lg-4 form-group">
								<input type="text" id="logDir" class="input-xlarge form-control"
									placeholder="Log Directory" title="Log Directory"
									data-placement="right">
							</div>
						</div>

						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblSavedCacheDir" class=" form-label">Saved
									Cache Directory:</label>
							</div>
							<div class="col-md-5 col-lg-4 form-group">
								<input type="text" id="savedCachesDir"
									class="input-xlarge form-control"
									placeholder="Saved Cache Directory"
									title="Saved Cache Directory" data-placement="right">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblCommitLogDir" class=" form-label">Commit
									Log Directory:</label>
							</div>
							<div class="col-md-5 col-lg-4 form-group">
								<input type="text" id="commitlogDir"
									class="input-xlarge form-control"
									placeholder="Commit Log Directory" title="Commit Log Directory"
									data-placement="right">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2 text-right">
								<label id="lblJMXPort" class=" form-label">JMX Port:</label>
							</div>
							<div class="col-md-2 col-lg-1 form-group">
								<input type="text" id="jmxPort" class="input-mini form-control"
									placeholder="JMX Port" title="JMX Port" data-placement="right">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			$('#vendorDropdown').focus();
			com.impetus.ankush.hybrid_Cassandra.cassandraConfigPopulate();
		});
	</script>
</body>
