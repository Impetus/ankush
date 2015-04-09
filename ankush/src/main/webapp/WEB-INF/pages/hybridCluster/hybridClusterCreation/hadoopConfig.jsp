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
	src="<c:out value='${baseUrl}'/>/public/js3.0/tooltip/hadoopClusterCreationTooltip.js"
	type="text/javascript"></script>
</head>
<body>
	<div class="">
		<!-- header section -->
		<div class="">
			<div class="">
				<div class="col-md-4">
					<!-- 	<h2>Hadoop/Configuration</h2> -->

				</div>
				<div class="col-md-1">
					<!-- <button class="btn btn-danger mrgt20" id="validateErrorHadoop"
						onclick="com.impetus.ankush.common.focusError();"
						style="display: none;"></button> -->
				</div>
				<div class="col-md-7 text-right mrgt20 padr45">
					<!-- <button id="hadoopRevertBtn" class="btn btn-default"
						onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
					<button class="btn btn-default" id="hadoopApplyBtn"
						onclick="com.impetus.ankush.hybrid_Hadoop.hadoopConfigValidate();">Apply</button> -->
				</div>
			</div>
		</div>
		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row">
					<!-- <div id="errorDivMainHadoop" class="col-md-12 errorDiv"
						style="display: none;"></div> -->
				</div>
				<div class="panel ">
					<div class="panel-heading">
						<div class="">
							<h3 class="panel-title col-md-2 mrgt5">Hadoop/Configuration</h3>
							<!-- 	<button class="btn btn-default btn-danger"
								id="validateErrorCassandra"
								onclick="com.impetus.ankush.common.focusError();"
								style="display: none;"></button> -->
							<button id="hadoopRevertBtn" class="btn btn-default"
								onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
							<button class="btn btn-default" id="hadoopApplyBtn"
								onclick="com.impetus.ankush.hybrid_Hadoop.hadoopConfigValidate();">Apply</button>
						</div>
						<div class="row">
							<div id="errorDivMainHadoop" class="errorDiv mrgt10"
								style="display: none;"></div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Vendor:</label>
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<select id="vendorDropdown" title="Select Hadoop vendor"
								class="form-control" data-placement="right"
								onchange="com.impetus.ankush.hybrid_Hadoop.vendorOnChangeHadoop();">
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Version:</label>
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<select id="versionDropdown" title="Select Hadoop version"
								class="form-control" data-placement="right"
								onchange="com.impetus.ankush.hybrid_Hadoop.versionOnChangeHadoop();">
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Source:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group ">
							<div class="btn-group" data-toggle="buttons-radio"
								id="sourcePathBtnGrp">
								<button class="btn btn-default nodeListRadio active btnGrp"
									data-value="0" id="downloadRadio"
									onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('downloadPathDiv','localPathDiv');">Download</button>
								<button class="btn btn-default nodeListRadio btnGrp"
									data-value="1" id="localRadio"
									onclick="com.impetus.ankush.hybridClusterCreation.buttonClick('localPathDiv','downloadPathDiv');">Local
								</button>
							</div>
						</div>
					</div>
					<div class="row" id="downloadPathDiv">
						<div class="col-md-2"></div>
						<div class="col-md-5 col-lg-4 form-group">
							<input type="text" name="downloadPath" id="downloadPath"
								class="input-xlarge form-control" placeholder="Download Path"
								title="Hadoop Download Path" data-placement="right">
						</div>
					</div>
					<div class="row" id="localPathDiv" style="display: none;">
						<div class="col-md-2"></div>
						<div class="col-md-5 col-lg-4 form-group ">
							<input type="text" name="localPath" id="localPath"
								class="input-xlarge form-control" placeholder="Local Path"
								title="Hadoop Local Path" data-placement="right">
						</div>
					</div>
					<div class="row">
						<div class=" col-md-2 text-right">
							<label class=" form-label">Installation Path:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group">
							<input id="installationPathHadoop" type="text"
								data-toggle="tooltip" class="input-xlarge form-control"
								placeholder="Hadoop Installation Path"
								title="Enter installation path for Hadoop"
								data-placement="right"></input>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">DFS Replication:</label>
						</div>
						<div class="col-md-2 col-lg-1 form-group">
							<input type="text" value="" id="dfsReplication"
								class="input-mini form-control" placeholder="DFS Replication"
								title="Enter DFS replication" data-placement="right">
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">NameNode Path:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group">
							<input type="text" value="" id="nameNodePath"
								class="input-xlarge form-control" placeholder="NameNode Path"
								title="Enter namenode path" data-placement="right">
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">DataNode Path:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group">
							<input type="text" value="" id="dataNodePath"
								class="input-xlarge form-control" placeholder="Data Node Path"
								title="Enter datanode path" data-placement="right">
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Mapred Temp Path:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group">
							<input type="text" value="" id="mapredTempPath"
								class="input-xlarge form-control" placeholder="Mapred Temp Path"
								title="Enter mapred temp path" data-placement="right">
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Hadoop Temp Path:</label>
						</div>
						<div class="col-md-5 col-lg-4 form-group">
							<input type="text" value="" id="hadoopTempPath"
								class="input-xlarge form-control"
								placeholder="Hadoop  Temp Path" title="Enter hadoop temp path"
								data-placement="right">
						</div>
					</div>
					<!-- <div class="row">
					<div class=" col-md-2 text-right">
						<label class=" form-label"></label>
					</div>
					<div class="col-md-10 col-lg-2 form-group" style="padding-top: 12px">
						<input id="includes3Check" type="checkbox"
							onclick="com.impetus.ankush.hybrid_Hadoop.checkBoxClick('includes3Check','includes3Div')">Include
						S3 Support</input>
					</div>
				</div>
				<div class="" id="includes3Div" style="display: none;">
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Access Key</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group">
							<input type="text" value="" id="s3AccessKey" class="input-xlarge"
								placeholder="Access Key" title="Enter access key"
								data-placement="right">
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Secret Key:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group">
							<input type="text" value="" id="s3SecretKey" class="input-xlarge"
								placeholder="Secret Key" title="Enter secret key"
								data-placement="right">
						</div>
					</div>
				</div>
				<div class="row">
					<div class=" col-md-2 text-right">
						<label class=" form-label"></label>
					</div>
					<div class="col-md-10 col-lg-2 form-group" style="padding-top: 12px">
						<input id="includes3nCheck" type="checkbox"
							onclick="com.impetus.ankush.hybrid_Hadoop.checkBoxClick('includes3nCheck','includes3nDiv')">Include
						S3n Support</input>
					</div>
				</div>
				<div class="" id="includes3nDiv" style="display: none;">
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Access Key</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group">
							<input type="text" value="" id="s3nAccessKey"
								class="input-xlarge" placeholder="Access Key"
								title="Enter access key" data-placement="right">
						</div>
					</div>
					<div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Secret Key:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group">
							<input type="text" value="" id="s3nSecretKey"
								class="input-xlarge" placeholder="Secret Key"
								title="Enter secret key" data-placement="right">
						</div>
					</div>
				</div> -->



					<!--------------- For Hadoop 2.0 ----------------->
					<div id="hadoop2Config">
						<!-- <div class="row">
						<div class="col-md-2 text-right">
							<label class=" form-label">Web Application Proxy:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group ">
							<div style="margin-top: 8px; margin-bottom: 15px"
								id="webAppProxyGroupBtn" data-toggle="buttons-radio"
								class="btn-group">
								<button
									onclick="com.impetus.ankush.hybrid_Hadoop.divToggle('webAppProxyDiv','Enable');"
									id="webAppProxyEnable" data-value="0" class="btn btn-default active btnGrp">Enable</button>
								<button
									onclick="com.impetus.ankush.hybrid_Hadoop.divToggle('webAppProxyDiv','Disable');"
									id="webAppProxyDisable" data-value="1" class="btn btn-default btnGrp">Disable</button>
							</div>
						</div>
					</div> -->


						<!-- 	<div id="webAppProxyDiv">

						<div class="row">
							<div class="col-md-2 text-right">
								<label class=" form-label">Web App Proxy Port:</label>
							</div>
							<div class="col-md-10 col-lg-2 form-group">
								<input type="text" id="webAppProxyPort" class="input-mini"
									placeholder="Web App Proxy Port" data-placement="right"
									data-toggle="tooltip"
									data-original-title="Enter port for Web Application Proxy service">
							</div>
						</div>
					</div>

 -->


						<div class="row">
							<div class="col-md-2 text-right">
								<label class=" form-label">High Availability:</label>
							</div>
							<div class="col-md-10 col-lg-2 form-group ">
								<div class="btn-group" data-toggle="buttons-radio" id="haBtnGrp">
									<button class="btn btn-default nodeListRadio active btnGrp"
										data-value="0" id="haBtnEnable"
										onclick="com.impetus.ankush.hybrid_Hadoop.divToggle('haPropDiv','Enable');">Enable</button>
									<button class="btn btn-default nodeListRadio btnGrp"
										data-value="1" id="haBtnDisable"
										onclick="com.impetus.ankush.hybrid_Hadoop.divToggle('haPropDiv','Disable');">Disable</button>
								</div>
							</div>
						</div>
						<div id="haPropDiv">
							<div class="row">
								<div class="col-md-2 text-right">
									<label class=" form-label">Nameservice ID:</label>
								</div>
								<div class="col-md-3 col-lg-2 form-group">
									<input type="text" value="" name="Nameservice ID"
										id="nameserviceId" class="input-large form-control"
										placeholder="Nameservice ID" title="Enter Nameservice ID"
										data-placement="right">
								</div>
							</div>

							<div class="row">
								<div class="col-md-2 text-right">
									<label class=" form-label">NameNode ID 1:</label>
								</div>
								<div class="col-md-3 col-lg-2 form-group">
									<input type="text" value="" name="nameNodeId1" id="nameNodeId1"
										class="input-large form-control" placeholder="NameNode ID1"
										title="Enter NameNode ID1" data-placement="right">
								</div>
							</div>
							<div class="row">
								<div class="col-md-2 text-right">
									<label class=" form-label">NameNode ID 2:</label>
								</div>
								<div class="col-md-3 col-lg-2 form-group">
									<input type="text" value="" name="nameNodeId2" id="nameNodeId2"
										class="input-large form-control" placeholder="NameNode ID2"
										title="Enter NameNode ID2" data-placement="right">
								</div>
							</div>
							<div class="row">
								<div class="col-md-2 text-right">
									<label class=" form-label">Journal Nodes Dir:</label>
								</div>
								<div class="col-md-5 col-lg-4 form-group">
									<input type="text" value="" name="port" id="journalNodesDir"
										class="input-xlarge form-control"
										placeholder="Journal Nodes Directory"
										title="Enter directory path for journal nodes "
										data-placement="right">
								</div>
							</div>
							<div class="row">
								<div class="col-md-2 text-right">
									<label class=" form-label">Automatic Failover:</label>
								</div>
								<div class="col-md-10 col-lg-2 ">
									<div class="btn-group" data-toggle="buttons-radio"
										id="autoFailBtnGrp">
										<button class="btn btn-default nodeListRadio active btnGrp"
											data-value="0" id="autoFailEnable" onclick="">Enable</button>
										<button class="btn btn-default nodeListRadio btnGrp"
											data-value="1" id="autoFailDisable" onclick="">Disable</button>
									</div>
								</div>
							</div>
							<div class="row" style="display: none;">
								<div class="col-md-2">
									<label class="text-right form-label">Zookeeper
										EnsembleId:</label>
								</div>
								<div class="col-md-2 col-lg-2">
									<select id="zookeeperensembleIdHadoop" title=""
										data-placement="right">
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			$("#vendorDropdown").focus();
			com.impetus.ankush.hybrid_Hadoop.hadoopConfigPopulate();
		});
	</script>
</body>
