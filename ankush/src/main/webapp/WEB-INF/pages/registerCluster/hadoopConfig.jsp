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

<%@ include file="../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}'/>/public/js3.0/tooltip/hadoopClusterCreationTooltip.js"
	type="text/javascript"></script>
<!-- header section -->
<body>
	<div class="">
		<div class="">
			<div class="">
				<div class="col-md-4">
					<h2>Hadoop/Configuration</h2>

				</div>
				<div class="col-md-1">
					<button class="btn btn-danger mrgt20" id="validateErrorHadoop"
						onclick="com.impetus.ankush.common.focusError();"
						style="display: none;"></button>
				</div>
				<div class="col-md-7 text-right mrgt20 padr45">
					<button id="hadoopRevertBtn" class="btn btn-default"
						onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
					<button class="btn btn-default" id="hadoopApplyBtn"
						onclick="com.impetus.ankush.register_Hadoop.hadoopConfigValidate();">Apply</button>
				</div>
			</div>
		</div>
		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row">
					<div id="errorDivMainHadoop" class="col-md-12 errorDiv form-group"
						style="display: none;"></div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Vendor:</label>
					</div>
					<div class="col-md-2 col-lg-2 form-group">
						<select id="vendorDropdown" title="Select Hadoop vendor"
							class="form-control" data-placement="right"
							onchange="com.impetus.ankush.register_Hadoop.vendorOnChangeHadoop();">
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right">
						<label class=" form-label">Version:</label>
					</div>
					<div class="col-md-2 col-lg-2 form-group">
						<select id="versionDropdown" title="Select Hadoop version"
							class="form-control"
							onchange="com.impetus.ankush.register_Hadoop.versionOnChangeHadoop();"
							data-placement="right">
						</select>
					</div>
				</div>



				<div class="row">
					<div class=" col-md-2 text-right">
						<label class=" form-label">NameNode:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<input id="namenode" type="text" data-toggle="tooltip"
							class="input-small form-control" placeholder="Namenode"
							title="Enter Namenode" data-placement="right"></input>
					</div>
				</div>

				<div class="row"  id="resourceManagerDiv">
					<div class=" col-md-2 text-right">
						<label class=" form-label">Resource Manager:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<input id="resourceManager" type="text" data-toggle="tooltip"
							class="input-small form-control" placeholder="Resource Manager"
							title="Enter Resource Manager" data-placement="right"></input>
					</div>
				</div>

				<div class="row" id="jobTrackerDiv" style="display: none;">
					<div class=" col-md-2 text-right">
						<label class=" form-label">JobTracker:</label>
					</div>
					<div class="col-md-3 col-lg-2 form-group">
						<input id="jobTracker" type="text" data-toggle="tooltip"
							class="input-small form-control" placeholder="JobTracker"
							title="Enter JobTracker" data-placement="right"></input>
					</div>
				</div>
				<!-- <div class="row" id="homeDirDiv">
					<div class=" col-md-2 text-right">
						<label class=" form-label">Home Directory:</label>
					</div>
					<div class="col-md-10 col-lg-2 form-group">
						<input id="homeDir" type="text" data-toggle="tooltip"
							class="input-small form-control" placeholder="Home Directory"
							title="Enter Home Directory" data-placement="right"></input>
					</div>
				</div> -->
				<div class="row">
					<div class=" col-md-2 text-right">
						<label class=" form-label">HTTP Port NameNode:</label>
					</div>
					<div class="col-md-2 col-lg-1 form-group">
						<input id="httpPortNameNode" type="text" data-toggle="tooltip"
							class="input-small form-control" placeholder="HTTP Port Namenode"
							title="Enter HTTP port for Namenode" data-placement="right"></input>
					</div>
				</div>

				<div class="row" id="installationTypeDiv">
					<div class="col-md-2 text-right">
						<label class=" form-label">Installation Type:</label>
					</div>
					<div class="col-md-2 col-lg-2 form-group">
						<select id="installationType" title="Select installation type"
							class="form-control" data-placement="right">
						</select>
					</div>
				</div>
				<div class="row" id="">
					<div class="col-md-2 text-right">
						<label class=" form-label">Registration Type:</label>
					</div>
					<div class="col-md-2 col-lg-2 form-group">
						<select id="registerLevel" title="Select Registration type"
							class="form-control" data-placement="right">
						</select>
					</div>
				</div>
				<!-- <div id="hadoop2Conf">
					<div class="row" id="webAppProxyDiv">
						<div class="col-md-2 text-right">
							<label class=" form-label">Web Application Proxy:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group">
							<div style="margin-top: 8px; margin-bottom: 15px"
								id="webAppProxyGroupBtn" data-toggle="buttons-radio"
								class="btn-group">
								<button onclick="" id="webAppProxyEnable" data-value="0"
									class="btn btn-default active btnGrp">Enable</button>
								<button onclick="" id="webAppProxyDisable" data-value="1"
									class="btn btn-default btnGrp">Disable</button>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">High Availability:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group">
							<div class="btn-group" data-toggle="buttons-radio" id="haBtnGrp"
								style="margin-top: 8px;">
								<button class="btn btn-default nodeListRadio active btnGrp"
									data-value="0" id="haBtnEnable"
									onclick="com.impetus.ankush.register_Hadoop.divToggle('haPropDiv','Enable');">Enable</button>
								<button class="btn btn-default nodeListRadio btnGrp"
									data-value="1" id="haBtnDisable"
									onclick="com.impetus.ankush.register_Hadoop.divToggle('haPropDiv','Disable');">Disable</button>
							</div>
						</div>
					</div>
					<div id="haPropDiv">
						<div class="row">
							<div class="col-md-2 text-right">
								<label class=" form-label">Automatic Failover:</label>
							</div>
							<div class="col-md-10 col-lg-2 form-group">
								<div class="btn-group" data-toggle="buttons-radio"
									id="autoFailBtnGrp" style="margin-top: 8px;">
									<button class="btn btn-default nodeListRadio active btnGrp"
										data-value="0" id="autoFailEnable" onclick="">Enable</button>
									<button class="btn btn-default nodeListRadio btnGrp"
										data-value="1" id="autoFailDisable" onclick="">Disable</button>
								</div>
							</div>
						</div>
					</div>
				</div> -->
			</div>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			com.impetus.ankush.register_Hadoop.hadoopConfigPopulate();
		});
	</script>
</body>
