/*******************************************************************************
*===========================================================
*Ankush : Big Data Cluster Management Solution
*===========================================================
*
*(C) Copyright 2014, by Impetus Technologies
*
*This is free software; you can redistribute it and/or modify it under
*the terms of the GNU Lesser General Public License (LGPL v3) as
*published by the Free Software Foundation;
*
*This software is distributed in the hope that it will be useful, but
*WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*See the GNU Lesser General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License 
*along with this software; if not, write to the Free Software Foundation, 
*Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/

var hadoopClusterData = null;
var clusterConfigurationDetail = null;
var addNodeHadoopClusterTable = null;
var addNodeProgressTable = null;
var hadoopClusterEcosystemTable_HM = null;
var hadoopServiceStatusTable = null;
var nodeHadoopJSON_AddNodes = null;
var nodeTableLength_AddNodes = null;
/*var eventsData = null;
var eventsOutput = null;
var logData = null;*/
var refreshTimeInterval = 5000;
var refreshTimeIntervalTiles = 90000;
var clusterDetailResult = null;
var deployFlagHadoop = false;
var IsAutoRefreshON = false;
var IsAutoRefreshON_NodeLogs = false;
var IsAutoRefreshON_NodeList = false;
var refreshTimeInterval_NodeList = 60000;
var IsAutoRefreshON_AddNodeProgress = false;
var hNodeListInterval = null;
/*var currentClusterId = null;*/
/*var auditTrailRows = [];*/
/*var eventsRows = [];*/
var fileRack_ServerPath = null;
var successText = "Request has been placed successfully";
var editedFields = [];
var fileIPAddress_ServerPath = null;
var hAddNodeProgressInterval = null;
var IsAutoRefreshON_clusterTile = false;
var hClusterTilesView = null;
var nodesData = null;
var nodeDetailsTable = null;
var readCount = null;
var logFileNameOutput = null;
var nodeDeleteData = null;
var isClusterRackEnabled = false;
var isHadoop2Cluster = false;
var inspectNodeData = {};
var StatusOnHead_nodeProgress={
		'Adding Nodes' : 'Node Addition in progress',
		'deployed' : 'Node Addition successful',
};

function autoRefresh(functionName) {
	hAddNodeProgressInterval = setInterval(function() {
		eval(functionName);
	}, refreshTimeInterval);
}

function autoRefresh_NodeList(functionName) {
	hNodeListInterval = setInterval(function() {
		eval(functionName);
	}, refreshTimeInterval_NodeList);
}

function autoRefreshTiles(functionName) {
	hClusterTilesView = setInterval(function() {
		eval(functionName);
	}, refreshTimeIntervalTiles);
}

com.impetus.ankush.hadoopMonitoring = {
		compNameHadoop : 'Hadoop',
        //function will load data tables in hadoop monitoring page COMMON MONITORING 
		loadHadoopMonitoringDatatables : function(clusterId){
			
			/*
			 * 
			 * JSON call for showing cluster environment on main page header
			 * 
			 */
			var clusterConfigUrl = baseUrl + "/monitor/" + clusterId
			+ "/clusterdetails";
			clusterConfigurationDetail = com.impetus.ankush.placeAjaxCall(
					clusterConfigUrl, 'GET', false);
			if (clusterConfigurationDetail.output.environment.toUpperCase() == "CLOUD") {
				$('#lbl_invalidAddNodeOperation').empty();
				$('#lbl_invalidAddNodeOperation').text('Node Addition request is not applicable for clusters deployed on cloud.');
				$('#hadoopAddNodes').attr("onclick","com.impetus.ankush.hadoopMonitoring.showDialog_InvalidAddNode()");
			}
			isClusterRackEnabled = clusterConfigurationDetail.output.rackEnabled;

			if (hadoopClusterData != null) {
				// displaying cluster name in header
				if(hadoopClusterData.output.ecosystem.Hadoop == null) {
					com.impetus.ankush.hadoopMonitoring.compNameHadoop = 'Hadoop2';
					isHadoop2Cluster = true;
					hadoopClusterData.output.ecosystem.Hadoop = hadoopClusterData.output.ecosystem.Hadoop2;
				}

				$("#hadoopMonitoring_dfsReplication").empty();
				$("#hadoopMonitoring_dfsReplication")
				.append(
						hadoopClusterData.output.ecosystem.Hadoop.advancedConf.replicationFactorDFS);
				$("#hadoopMonitoring_vendor").empty();
				$("#hadoopMonitoring_vendor").append(
						hadoopClusterData.output.ecosystem.Hadoop.componentVendor);
				$("#hadoopMonitoring_version").empty();
				$("#hadoopMonitoring_version").append(
						hadoopClusterData.output.ecosystem.Hadoop.componentVersion);
				
					hadoopClusterEcosystemTable_HM = $('#configHadoopEcoSystemTable')
					.dataTable({
						"bJQueryUI" : false,
						"bPaginate" : false,
						"bLengthChange" : true,
						"bFilter" : false,
						"bSort" : false,
						"bInfo" : false,
						"sPaginationType" : "full_numbers",
						"bAutoWidth" : false,
					});
                    for ( var componentName in hadoopClusterData.output.ecosystem) {
						var componentNameTable = '<span style="font-weight:bold;" >'
							+ componentName + '</span>';
						var componentVendor = '<span style="font-weight:bold;" >'
							+ hadoopClusterData.output.ecosystem[componentName].componentVendor
							+ '</span>';
						var componentVersion = '<span style="font-weight:bold;" >'
							+ hadoopClusterData.output.ecosystem[componentName].componentVersion
							+ '</span>';
						var status = '<span style="font-weight:bold;">'
							+ hadoopClusterData.output.ecosystem[componentName].certification + '</span>';
						if ( !(componentName == 'Hadoop' || componentName == 'Hadoop2') ) {
							hadoopClusterEcosystemTable_HM
							.fnAddData([
							            componentNameTable,
							            componentVendor,
							            componentVersion,
							            status,
							            '<div><a href="#" onclick="com.impetus.ankush.hadoopMonitoring.hadoopComponentsDetailView(\''
							            + componentName
							            + '\',\'1\');"><img  src="'
							            + baseUrl
							            + '/public/images/icon-chevron-right.png" /></a></div>' ]);
						}
					}
				
			}
        },
		// function to scroll to top of page
		scrollToTop:function(){
			$(window).scrollTop(0);
		},
		// function to open node detail page containing graphs 
		hadoopNodeDrillDown : function(nodeId, nodeIp, clusterId) {

			if ((!(typeof (refreshInterval_NDD) === 'undefined'))
					&& (refreshInterval_NDD != null)) {
				refreshInterval_NDD = window.clearInterval(refreshInterval_NDD);
			}

			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/nodeDrillDown',
						method : 'get',
						previousCallBack : "com.impetus.ankush.hadoopMonitoring.nodepage();",
						title : nodeIp,
						tooltipTitle : 'Nodes',
						params : {
							nodeId : nodeId,
							nodeIp : nodeIp,
							clusterId : clusterId
						},
					});
		},

		// function used to focus on element having invalid data
		focusError : function(id) {
			$('#' + id).focus();
		},

		// function used to validate whether node Ip address is provided or not for node retrieval during add node operation
		validateRetrieveEvent : function() {
			$("#popover-content").empty();
			com.impetus.ankush.validation.errorCount = 0;
//			if ($('#ipRange').attr('checked')){
			if($('#ipRange').hasClass('active')){
				com.impetus.ankush.validation.validateField('required', 'ipRangeHadoop', 'IP Range', 'popover-content');
			}
			else{
				com.impetus.ankush.validation.validateField('required', 'filePath_IPAddressFile', 'IP Address File', 'popover-content');
			}
			com.impetus.ankush.hadoopMonitoring.validateRackMappingFileUpload();
		},
		validateRackMappingFileUpload : function() {
			if($('#chkBxRackEnable').attr('checked')) {
				if(fileRack_ServerPath == null) {
					var errMsg = 'Rack Mapping file not uploaded';
					var toolTipMsg = 'Upload rack mapping file';
					com.impetus.ankush.validation.addNewErrorToDiv('filePath_RackFile', 'popover-content', errMsg, toolTipMsg);
				}
				else {
					$('#filePath_RackFile').removeClass('error-box');
					com.impetus.ankush.common.tooltipOriginal('filePath_RackFile', 'File with Rack & IP Address Mapping');
				}
			}
			else {
				$('#filePath_RackFile').removeClass('error-box');
				com.impetus.ankush.common.tooltipOriginal('filePath_RackFile', 'File with Rack & IP Address Mapping');
			}
		},
		// function used to initialize add node table
		initTables_addNodes : function() {
			if(!isClusterRackEnabled){
				$('#chkBxRackEnable').attr('disabled', 'disabled');
			}
			addNodeHadoopClusterTable = $("#addNodeIpTableHadoop").dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : true,
				"bInfo" : false,
				"aoColumnDefs": [
				                 { 'bSortable': false, 'aTargets': [ 0,2,6 ] },
				                 { 'sType': "ip-address", 'aTargets': [1] }
				                 ], 
			});

			$('#addNodeIpTableHadoop_filter').hide();
			$('#searchAddNodeTableHadoop').keyup(function() {
				$("#addNodeIpTableHadoop").dataTable().fnFilter($(this).val());
			});

			$("#addNodeIpTableHadoop_filter").css({
				'display' : 'none'
			});
		},
		toggleRackMapping : function() {
			if($('#chkBxRackEnable').attr('checked')) {
				$('#filePath_RackFile').removeAttr('disabled');
				$('#filePath_RackFile').css('cursor', 'pointer');
				$('#filePath_RackFile').css('background', 'white');
			}
			else {
				$('#filePath_RackFile').css('cursor', 'default');
				$('#filePath_RackFile').attr('disabled', 'disabled');
				$('#filePath_RackFile').css('background', '#EEEEEE');
				fileRack_ServerPath = null;
				$('#filePath_RackFile').val('');
				$('#filePath_RackFile').removeClass('error-box');
				com.impetus.ankush.hadoopMonitoring.clearRackMappingFromTable();	
				com.impetus.ankush.common.tooltipOriginal('filePath_RackFile', 'File with Rack & IP Address Mapping');
			}
		},

		// Function to clear rack mapping from node list table 
		clearRackMappingFromTable : function() {
			if(addNodeHadoopClusterTable == null) {
				return;
			}
			var nodesCount = addNodeHadoopClusterTable.fnGetData().length;
			for(var i = 0; i < nodesCount; i++ ) {
				$('#rackInfo-'+i).text('--');
				$('#dcInfo-'+i).text('--');
			}
		},
		// Function to upload IP Address & Rack mapping file		
		hadoopRackFileUpload : function() {
			fileRack_ServerPath = null;
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_RackFile').click();
			$('#fileBrowse_RackFile').change(
					function() {
						$('#filePath_RackFile').val(document.forms['uploadframeRackFile_Form'].elements['fileBrowse_RackFile'].value);
						com.impetus.ankush.hadoopMonitoring.uploadFile(uploadUrl,"fileBrowse_RackFile", "fileRack");
						com.impetus.ankush.validation.removeFakePathFromPath('filePath_RackFile');
					});
		},
		
		// function used to retrieve nodes during add node operation
		getNewlyAddedNodes : function(id) {
			$("#inspectNodeBtn").attr('disabled',true);
			inspectNodeData = {};
			com.impetus.ankush.hadoopMonitoring.validateRetrieveEvent();
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			$('#retrieve').text('Retrieving...');
			$('#retrieve').attr('disabled', true);
			$('#addNode').attr('disabled', true);

			$("#nodeCheckHead").removeAttr("checked");
			$("#error-div-hadoop").css("display", "none");
			$('#errorBtnHadoop').text("");
			$('#errorBtnHadoop').css("display", "none");
			var clusterId = id.toString();
			var url = baseUrl + "/cluster/detectNodes";
			var nodeData = {};

			nodeData.isFileUploaded = false;
//			if ($('#ipFile').attr('checked')) {
			if($('#ipFile').hasClass('active')){
				nodeData.isFileUploaded = true;
				nodeData.nodePattern = fileIPAddress_ServerPath;
			} else {
				nodeData.nodePattern = $('#ipRangeHadoop').val();
				saveNodePattern = $('#ipRangeHadoop').val();
			}
			// Rack POST Data for Node Retrival
			nodeData.isRackEnabled = false;
			nodeData.filePathRackMap = "";

			if($('#chkBxRackEnable').attr('checked')) {
				nodeData.isRackEnabled = true;
				nodeData.filePathRackMap = fileRack_ServerPath;	
			}

			nodeData.clusterId = clusterId;
			var errorNodeCount = null;
			$('#btnAll_HSN').removeClass('active');
			$('#btnSelected_HSN').removeClass('active');
			$('#btnAvailable_HSN').removeClass('active');
			$('#btnError_HSN').removeClass('active');
			$('#btnAll_HSN').addClass('active');
			if (($('#hadoopAddNodesIPRange').val() != '')
					|| ($('#hadoopAddNodesFilePath').val() != '')) {
				$
				.ajax({
					'type' : 'POST',
					'url' : url,
					'contentType' : 'application/json',
					'data' : JSON.stringify(nodeData),
					"async" : true,
					'dataType' : 'json',
					'success' : function(result) {
						$("#inspectNodeBtn").attr('disabled',false);
						$('#retrieve').text('Retrieve');
						$('#retrieve').removeAttr('disabled');
						$('#addNode').removeAttr('disabled');

						// IP pattern invalid error message
						if (result.output.error != null) {
							com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,
									'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
							return;
						}
						addNodeHadoopClusterTable.fnClearTable();
						nodeTableLength_AddNodes = result.output.nodes.length;
						nodeHadoopJSON_AddNodes = result.output.nodes;
						if (nodeTableLength_AddNodes > 0) {

							for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
								var dcText = '';
								if(nodeHadoopJSON_AddNodes[i][5] != undefined) {
									dcText = nodeHadoopJSON_AddNodes[i][5];
								}
								var rackText = '';
								if(nodeHadoopJSON_AddNodes[i][6] != undefined) {
									rackText = nodeHadoopJSON_AddNodes[i][6];
								}
								var checkNodeHadoop = '<input type="checkbox" class="inspect-node checkedElement" id="hadoopAddNodeCheckBox-'
									+ i
									+ '" onclick="com.impetus.ankush.hadoopMonitoring.updateHeaderCheckBox(\'hadoopAddNodeCheckBox-'
									+ i + '\');"/>';
								var ipHadoop = nodeHadoopJSON_AddNodes[i][0];
//								var secNameNode = '<input type="radio" name="addSecNameNode" id="addSecNameNode-'
//									+ i
//									+ '" onclick="com.impetus.ankush.hadoopMonitoring.updateSecNameNodeCheckBox(\'addSecNameNode-'
//									+ i + '\');"/>';
								var dataNode = '<input type="checkbox" id="addDataNode-'
									+ i
									+ '" onclick="com.impetus.ankush.hadoopMonitoring.updateDataNodeCheckBox(\'addDataNode-'
									+ i + '\');"/>';
								var dcInfo  = '<span  id="dcInfo-' + i + '"/>'+dcText+'</span>';
								var rackInfo = '<span  id="rackInfo-' + i + '"/>'+rackText+'</span>';
								var os = '<span style="font-weight:bold;" id="addNodeOS-'
									+ i
									+ '">'
									+ nodeHadoopJSON_AddNodes[i][4]
								+ '</span>';
								var navigationHadoop = '<a href="#"><img id="navigationImgAddHadoop'
									+ i
									+ '" src="'
									+ baseUrl
									+ '/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.hadoopMonitoring.loadNodeDetailHadoop('
									+ i + ');"/></a>';
								var addId = addNodeHadoopClusterTable
								.fnAddData([ checkNodeHadoop,
								             ipHadoop, 
//								             secNameNode,
								             dataNode, dcInfo, rackInfo, os,
								             navigationHadoop

								             ]);
								var theNode = addNodeHadoopClusterTable.fnSettings().aoData[addId[0]].nTr;
								nodeHadoopJSON_AddNodes[i][0].split('.').join('_');
								theNode.setAttribute('id', 'addNode-' + i);
								if (!(nodeHadoopJSON_AddNodes[i][1]
								&& nodeHadoopJSON_AddNodes[i][2] && nodeHadoopJSON_AddNodes[i][3])) {
									$('#hadoopAddNodeCheckBox-' + i).attr(
											'disabled', true);
//									$("#addSecNameNode-" + i).attr(
//											'disabled', true);
									$("#addDataNode-" + i).attr('disabled',
											true);
									$('#addNode-' + i)
									.addClass('error-row');
									errorNodeCount++;
								}
								$('#addNode-' + i).addClass('selected');
							}
						}
						enabledNodes = nodeTableLength_AddNodes - errorNodeCount;
					}
				});
			} else {
				$('#error').empty();
				$('#error').append("Please specify valid node pattern.");
			}
		},
		//this function will inspect nodes
		inspectNodesObject : function(id){
			var data = {};
			data.nodePorts = {};
			$('.inspect-node').each(function(){
				if($(this).is(':checked')){
					$(this).addClass('inspect-node-ok');
					var ip = $(this).parent().next().text();
					data.nodePorts[ip] = [];
				}
			});
			data.clusterId = com.impetus.ankush.commonMonitoring.clusterId;
			com.impetus.ankush.inspectNodesCall(data,id,'retrieve');
			
		},
		
		// function used to show information on tooltip of node IP details
		divShowOnClickIPAddress : function(clickId) {
			$('#ipRangeHadoop').removeClass('error-box');
			$('#ipRangeHadoop').tooltip();
			com.impetus.ankush.common.tooltipOriginal(
					'ipRangeHadoop', 'Enter IP Address Range');

			$('#filePath_IPAddressFile').removeClass('error-box');
			$('#filePath_IPAddressFile').tooltip();
			com.impetus.ankush.common.tooltipOriginal(
					'filePath_IPAddressFile', 'Upload IP Address File');

			$('#div_IPRange').attr('style', 'display:none');
			$('#div_IPFileUpload').attr('style', 'display:none;');
			$('#' + clickId).attr('style', 'display:block;');
		},

		// function used to upload a file containing nodes IP to be added to cluster
		hadoopIPAddressFileUpload : function() {
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_IPAddressFile').click();
			$('#fileBrowse_IPAddressFile').change(
					function() {
						$('#filePath_IPAddressFile').val(
								$('#fileBrowse_IPAddressFile').val());
						com.impetus.ankush.hadoopMonitoring.uploadFile(uploadUrl,
								"fileBrowse_IPAddressFile", "fileIPAddress");
					});
		},

		// function used to upload a file
		uploadFile : function(uploadUrl, fileId, uploadType, callback, context) {
			jsonFileUploadString = null;
			$.ajaxFileUpload({
				url : uploadUrl,
				secureuri : false,
				fileElementId : fileId,
				dataType : 'text',
				accept : "application/png",
				success : function(result) {
					if (callback)
						callback(result, context);
					var htmlObject = $(result);
					var resultJSON = new Object();
					eval("resultJSON  = " + htmlObject.text());
					if (uploadType == "fileIPAddress") {
						fileIPAddress_ServerPath = resultJSON.output;
					}
					else if(uploadType == "fileRack") {
						fileRack_ServerPath = resultJSON.output;
					}
				},
				error : function() {
					alert('Unable to upload the file');
				}
			});
		},

		// function used to validate whether a node is available for adding to cluster
		validateNodeSelection : function() {
			var flag_NodeCount = false;
			com.impetus.ankush.validation.errorCount = 0;
			$("#popover-content").empty();
			if (nodeTableLength_AddNodes == null || nodeTableLength_AddNodes == 0) {
				errorMsg = 'Select at-least one node';
				com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
			} else {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {

					if ($("#hadoopAddNodeCheckBox-" + i).attr("checked")) {
						flag_NodeCount = true;
						break;
					}
				}
				if (!flag_NodeCount) {
					errorMsg = 'Select at-least one node';
					com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
				}
				com.impetus.ankush.hadoopMonitoring.validateRackMappingFileUpload();
			}
		},

		// function used to add nodes to cluster
		addNodeSetup : function(clusterId) {
			com.impetus.ankush.hadoopMonitoring.validateNodeSelection();
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			$('#ipRangeHadoop').val($.trim($('#ipRangeHadoop').val()));
			$("#nodeCheckHead").removeAttr("checked");
			$("#error-div-hadoop").css("display", "none");
			$('#errorBtnHadoop').text("");
			$('#errorBtnHadoop').css("display", "none");
			var node = null;
			var addNodeData = {};
			addNodeData.technology = 'Hadoop';
			addNodeData["@class"] = "com.impetus.ankush.hadoop.config.HadoopClusterConf";
			addNodeData.newNodes = new Array();
//			if ($('#ipRange').attr('checked'))
			if($('#ipRange').hasClass('active'))
				addNodeData.ipPattern = $('#ipRangeHadoop').val();
			else
				addNodeData.patternFile = fileIPAddress_ServerPath;
			var numberOfRetrievedNodes = $('#addNodeIpTableHadoop tr').length - 1;
			if (numberOfRetrievedNodes > 0) {
				for ( var i = 0; i < numberOfRetrievedNodes; i++) {
					if ($('#hadoopAddNodeCheckBox-' + i).is(':checked')) {
						var flag_SecNN = false;
						var flag_Datanode = false;
//						if ($('#addSecNameNode-' + i).is(':checked')) {
//							flag_SecNN = true;
//						}
						if ($('#addDataNode-' + i).is(':checked')) {
							flag_Datanode = true;
						}
						var nodeDcInfo = $("#dcInfo-"+i).text();
						if(nodeDcInfo == '') {
							nodeDcInfo = "default-dc";
						}
						
						var nodeRackInfo = $("#rackInfo-"+i).text();
						if(nodeRackInfo == '') {
							nodeRackInfo = "default-rack";
						}
						var type = "";
						if(flag_Datanode) {
							type = "DataNode/";
							if(isHadoop2Cluster) {
								type += "NodeManager/";	
						}
							else {
								type += "TaskTracker/";
							}
						}
						if(flag_SecNN) {
							type += "SecondaryNameNode/";
						}
						type = type.substring(0, type.length - 1);
						node = {
								"publicIp" : nodeHadoopJSON_AddNodes[i][0],
								"privateIp" : nodeHadoopJSON_AddNodes[i][0],
								"nameNode" : "false",
								"dataNode" : flag_Datanode,
								"secondaryNameNode" : flag_SecNN,
								"os" : $("#addNodeOS-" + i).text(),
								"type" : type,
								"datacenter" : nodeDcInfo,
								"rack" : nodeRackInfo
						};
						addNodeData.newNodes.push(node);
					}
				}
				
				addNodeData.rackFileName = "";
				if($('#chkBxRackEnable').attr('checked')) {
					addNodeData.rackFileName = fileRack_ServerPath;
				}
			}
			var addNodeUrl = baseUrl + "/cluster/" + clusterId + "/add/";
			$.ajax({
				'type' : 'POST',
				'url' : addNodeUrl,
				'contentType' : 'application/json',
				'data' : JSON.stringify(addNodeData),
				"async" : false,
				'dataType' : 'json',
				'success' : function(result) {
					if(result.output.status) {
						com.impetus.ankush.hadoopMonitoring.loadMonitoringHomePage(clusterId);	
					}
					else {
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
						return;
					}
				},
				'error' : function() {
				},
			});
		},

		// function used to load hadoop cluster monitoring page
		loadMonitoringHomePage : function(clusterId) {
			com.impetus.ankush.removeChild(2);
		},

		// function used to open add node progress details page during node(s) addition
		loadAddNodeProgress : function(clusterId) {
			addNodeProgressTable = null;
			$('#content-panel').sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/addNodesProgress/'
						+ clusterId,
						method : 'get',
						previousCallBack : "com.impetus.ankush.hadoopMonitoring.stopNodeProgressAutorefresh();",
						params : {},
						title : 'Add Nodes Progress',
						tooltipTitle : hadoopClusterData.output.clusterName,
						callback : function(data) {
							com.impetus.ankush.hadoopMonitoring.nodeAddition(data.clusterId);
						},

						callbackData : {
							clusterId : clusterId
						}
					});
		},

		// function used to stop node addition autorefresh call 
		stopNodeProgressAutorefresh : function() {
			com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();	
		},

		// function used to select an error row in add node progress table during add node operation
		toggleDatatable_AddProgress : function(status) {
			$('.notSelected').show();
			$('.notSelected').removeClass('notSelected');
			$('.selected').removeClass('selected');
			var countNodes = addNodeProgressTable.fnGetData().length;
			if (status == 'Error') {
				for ( var i = 0; i < countNodes; i++) {
					if ($('#rowNodeProgress-' + i).hasClass('error-row')) {
						$('#rowNodeProgress-' + i).addClass('selected');
					} else
						$('#rowNodeProgress-' + i).addClass('notSelected');
				}
				$('.notSelected').hide();
			}
		},

		// function used to display retreived nodes on the basis of filtering, whether All, Selected, Available, or error nodes is to be displayed
		toggleDatatable : function(status) {
			$('.notSelected').show();
			$('.notSelected').removeClass('notSelected');
			$('.selected').removeClass('selected');
			$('#addNodeCheckHead').removeAttr('disabled');
			if (status == 'Selected') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if ($('#hadoopAddNodeCheckBox-' + i).attr('checked')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('#addNodeCheckHead').attr('disabled', true);
				$('.notSelected').hide();
			} else if (status == 'Available') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if (!$('#addNode-' + i).hasClass('error-row')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('.notSelected').hide();
			} else if (status == 'Error') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if ($('#addNode-' + i).hasClass('error-row')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('#addNodeCheckHead').removeAttr('checked');
				$('.notSelected').hide();
			}
		},

		// function used to set a global variable to a current cluster id
		setCurrentClusterId : function(clusterId) {
			currentClusterId = clusterId;
		},

		// function used to stop add nodes progress autorefresh call
		stopAutoRefreshCall_NodeAddition : function() {
			deployFlagHadoop = false;
			IsAutoRefreshON_AddNodeProgress = false;
			hAddNodeProgressInterval = window.clearInterval(hAddNodeProgressInterval);
		},

		// function used to load data of node addtion progress in a table
		nodeAddition : function(clusterId) {
			if (document.getElementById('addNodeProgressTable') == null) {
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}

			if($("#status_AddNodeProgress").hasClass('label-success')) {
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}
			var clusterDetailUrl = baseUrl + '/monitor/' + clusterId
			+ '/nodeprogress';
			clusterDetail = com.impetus.ankush.placeAjaxCall(clusterDetailUrl,
					"GET", false);
			clusterDetailResult = clusterDetail.output;
			if (addNodeProgressTable == null) {
				addNodeProgressTable = $("#addNodeProgressTable").dataTable(
						{
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : false,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"aoColumnDefs": [
							                 { 'bSortable': false, 'aTargets': [ 3 ] },
							                 { 'sType': "ip-address", 'aTargets': [0] }
							                 ], 							
							                 'fnRowCallback' : function(nRow, aData, iDisplayIndex,
							                		 iDisplayIndexFull) {
							                	 $(nRow).attr('id',
							                			 'rowNodeProgress-' + iDisplayIndex);
							                	 $(nRow).addClass('selected');
							                 }
						});
				$('#searchAddNodeTableHadoop').keyup(function(){
					$("#addNodeProgressTable").dataTable().fnFilter( $(this).val() );
				});
				$("#addNodeProgressTable_filter").css({
					'display' : 'none'
				});

			} 
			if (clusterDetailResult.ipPattern != "") {
//				$('#ipRange').attr('checked', true);
				$('#ipRange').addClass('active');
				$('#ipFile').removeClass('active');
				$('#ipRangeHadoop').val(clusterDetailResult.ipPattern);
				com.impetus.ankush.hadoopMonitoring
				.divShowOnClickIPAddress('div_IPRange');
			} else {
//				$('#ipFile').attr('checked', true);
				$('#ipFile').addClass('active');
				$('#ipRange').removeClass('active');
				$('#filePath_IPAddressFile').val(clusterDetailResult.patternFile);
				com.impetus.ankush.hadoopMonitoring
				.divShowOnClickIPAddress('div_IPFileUpload');
			}
			$('#ipRange').attr('disabled', true);
			$('#ipRangeHadoop').attr('disabled', true);
			$('#ipFile').attr('disabled', true);
			$('#filePath_IPAddressFile').attr('disabled', true);
			$('#retrieveAddNodeButton').attr('disabled', true);

			var tableData = [];
			var flag_ErrorNodeProgress = false;  
			for ( var i = 0; i < clusterDetailResult.nodes.length; i++) {
				var nodeType = null;
				if(clusterDetailResult.nodes[i].nodeState == 'error')
					flag_ErrorNodeProgress = true;	
				var rowData = [];

				if (clusterDetailResult.nodes[i].nameNode) {
					nodeType = "NameNode";
				}
				if (clusterDetailResult.nodes[i].dataNode) {
					if (nodeType != null) {
						nodeType = nodeType + "/DataNode";
					} else
						nodeType = "DataNode";
				}
				if (clusterDetailResult.nodes[i].secondaryNameNode) {
					if (nodeType != null) {
						nodeType = nodeType + "/SecondaryNameNode";
					} else
						nodeType = "SecondaryNameNode";
				}

				var col_PublicIp = clusterDetailResult.nodes[i].publicIp;
				var col_NodeType = '<a class="" id="nodeType-' + i + '">'
				+ nodeType + '</a>';
				var col_Message = '<a class="" id="nodeStatus-' + i + '">'
				+ clusterDetailResult.nodes[i].message + '</a>';
				var col_Image = '<a href="##" onclick="com.impetus.ankush.hadoopMonitoring.loadAddNodeProgressChild('
					+ i
					+ ');"><img id="navigationImg-'
					+ i
					+ '" src="'
					+ baseUrl
					+ '/public/images/icon-chevron-right.png" /></a>';

				rowData.push(col_PublicIp);
				rowData.push(col_NodeType);
				rowData.push(col_Message);
				rowData.push(col_Image);
				tableData.push(rowData);
			}
			addNodeProgressTable.fnClearTable();
			addNodeProgressTable.fnAddData(tableData);
			$("#status_AddNodeProgress").removeClass('label-success');
			$("#div_status_AddNodeProgress").css("display", "block");

			if (flag_ErrorNodeProgress) {
				var errNodesCount = 0;
				for ( var i = 0; i < clusterDetailResult.nodes.length; i++) {
					if (clusterDetailResult.nodes[i].nodeState == 'error') {
						$('#rowNodeProgress-' + i).addClass('error-row');
						errNodesCount++;
						clusterErrorMessage = errNodesCount
						+ ". Node addition failed : "
						+ clusterDetailResult.nodes[i].publicIp;
						$("#popover-content").append(
								'<div class="errorLineDiv"><a style="color: #5682C2;" href="#">' + clusterErrorMessage
								+ '</a></br></div>');
					}
				}
				$("#error-div-hadoop").css("display", "block");
				if (errNodesCount > 1)
					$('#errorBtnHadoop').text(errNodesCount + " Errors");
				else
					$('#errorBtnHadoop').text(errNodesCount + " Error");
				$('#errorBtnHadoop').css("display", "block");
				$("#div_status_AddNodeProgress").css("display", "none");
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}
			else if(clusterDetailResult.state == 'Adding Nodes'){
				$('#status_AddNodeProgress').text(StatusOnHead_nodeProgress['Adding Nodes']);
			}
			else {
				deployFlagHadoop = true;
				$("#div_status_AddNodeProgress").css("display", "block");
				$("#status_AddNodeProgress").addClass('label-success');
				$("#status_AddNodeProgress").html('').text(StatusOnHead_nodeProgress['deployed']);
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}
			if (!deployFlagHadoop) {
				if (!IsAutoRefreshON_AddNodeProgress) {
					IsAutoRefreshON_AddNodeProgress = true;
					com.impetus.ankush.hadoopMonitoring
					.initProgressNodeStatusNodeTable(clusterId);
				}
			}
		},

		// function used to start autorefresh call for node addition to display progress logs  
		initProgressNodeStatusNodeTable : function(currentClusterId) {
			autoRefresh("com.impetus.ankush.hadoopMonitoring.nodeAddition(\""
					+ currentClusterId + "\");");
		},

		// function used to open node addition child page containing deployment progress logs
		loadAddNodeProgressChild : function(nodeRowId) {
			$('#content-panel').sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/hAddNodeSetupDetail',
						method : 'get',
						params : {

						},
						title : 'Node Progress',
						tooltipTitle : 'Add Nodes Progress',
						callback : function() {
							com.impetus.ankush.hadoopMonitoring
							.deploymentPrg_loadNodeSetupDetail(nodeRowId);
						},
						callbackData : {

						}
					});
		},

		// function used to check all available nodes, retrieved during node addtion
		checkAllNode : function(elem) {
			if (addNodeHadoopClusterTable.fnGetData().length != 0) {
				for ( var i = 0; i < nodeHadoopJSON_AddNodes.length; i++) {
					var val = $('input:[name=addNodeCheckHead]').is(':checked');
					if (val) {
						if (nodeHadoopJSON_AddNodes[i][1] && nodeHadoopJSON_AddNodes[i][2]
						&& nodeHadoopJSON_AddNodes[i][3]) {
							$("#hadoopAddNodeCheckBox-" + i).attr('checked',
							'checked');
							$("#addDataNode-" + i).attr('checked', 'checked');
						}
					} else {
						$("#hadoopAddNodeCheckBox-" + i).removeAttr('checked');
//						$("#addSecNameNode-" + i).removeAttr('checked');
						$("#addDataNode-" + i).removeAttr('checked');
					}
				}
			}
		},

		// function used to open retrieved nodes details page containing information viz. Availability, Reachability, etc.
		loadNodeDetailHadoop : function(id) {
			$('#content-panel').sectionSlider(
					'addChildPanel',
					{
						url : baseUrl + '/hadoop-cluster/hadoopNodeDetails',
						method : 'get',
						params : {},
						title : 'Node Status',
						tooltipTitle : 'Add Nodes',
						callback : function(data) {
							com.impetus.ankush.hadoopMonitoring
							.loadHadoopNodePage(data.id);
						},
						callbackData : {
							id : id
						}
					});
		},

		// function used to show retrieved nodes details viz. Availability, Reachability, etc.
		loadHadoopNodePage : function(id) {
			$("#nodeIp-Hadoop").text(nodeHadoopJSON_AddNodes[id][0]);
			var nodeType = "";

			if ($("#addDataNode-" + id).attr("checked"))
				nodeType = "DataNode";

//			if ($("#addSecNameNode-" + id).attr("checked")) {
//				if (nodeType.length == 0)
//					nodeType = "SecondaryNameNode";
//				else
//					nodeType += " / SecondaryNameNode";
//			}
			$("#nodeType-Hadoop").text(nodeType);
			if (nodeHadoopJSON_AddNodes[id][1])
				$("#nodeStatus-Hadoop").empty().append("Available");
			else
				$("#nodeStatus-Hadoop").empty().append("Unavailable");

			if (nodeHadoopJSON_AddNodes[id][2])
				$("#nodeReachable-Hadoop").empty().append("Yes");
			else
				$("#nodeReachable-Hadoop").empty().append("No");

			if (nodeHadoopJSON_AddNodes[id][3])
				$("#nodeAuthentication-Hadoop").empty().append('Yes');
			else
				$("#nodeAuthentication-Hadoop").empty().append('No');

			if (nodeHadoopJSON_AddNodes[id][4] != "")
				$("#OSName-Hadoop").empty().append(nodeHadoopJSON_AddNodes[id][4]);
			else
				$("#OSName-Hadoop").empty().append("--");
		},

		// function used to select/ deselect node type during checking/unchecking of node
		updateHeaderCheckBox : function(id) {
			var rowId = (id.split('-'))[1];
			if (!$('#' + id).attr('checked')) {
				$("#addNodeCheckHead").removeAttr('checked');
				$("#addDataNode-" + rowId).removeAttr('checked');
//				$("#addSecNameNode-" + rowId).removeAttr('checked');
			} else
				$("#addDataNode-" + rowId).attr('checked', true);
			com.impetus.ankush.headerCheckedOrNot('checkedElement','addNodeCheckHead');
		},

		// function used to check/uncheck node header check box during checking/unchecking of datanode type
		updateDataNodeCheckBox : function(id) {
			var rowId = (id.split('-'))[1];
			if (!$('#' + id).attr('checked')) {
					$("#addNodeCheckHead").removeAttr('checked');
					$("#hadoopAddNodeCheckBox-" + rowId).removeAttr('checked');
				
//				if (!$('#addSecNameNode-' + rowId).attr('checked')) {
//					$("#addNodeCheckHead").removeAttr('checked');
//					$("#hadoopAddNodeCheckBox-" + rowId).removeAttr('checked');
//				}
			} else {
				$("#hadoopAddNodeCheckBox-" + rowId).attr('checked', true);
			}
		},

		// function used to check/uncheck node header check box during checking/unchecking of secondary namenode type
		updateSecNameNodeCheckBox : function(id) {
			var rowId = (id.split('-'))[1];
			if (!$('#' + id).attr('checked')) {
				if (!$("#addDataNode-" + rowId).attr('checked')) {
					$("#addNodeCheckHead").removeAttr('checked');
					$("#hadoopAddNodeCheckBox-" + rowId).removeAttr('checked');
				}
			} else {
				$("#hadoopAddNodeCheckBox-" + rowId).attr('checked', true);
			}
		},

		// function used to load hadoop cluster monitoring data
		loadClusterData : function(clusterId) {
			currentClusterId = clusterId;
			com.impetus.ankush.hadoopMonitoring.heatMapToggle();
			com.impetus.ankush.hadoopMonitoring.loadClusterEcosystem(clusterId);
		},

		// function used to show heat map graphs
		heatMapToggle : function() {
			$('#heat_map').empty();
			getHeatMapChart(currentClusterId,'0');
		},

		// function used to hadoop cluster and its ecosystem components details on main monitoring page
		loadClusterEcosystem : function(clusterId) {
			var url = baseUrl + "/monitor/" + clusterId + "/ecosystem";
			hadoopClusterData = com.impetus.ankush.placeAjaxCall(url, 'GET', false);

			/*
			 * 
			 * JSON call for showing cluster environment on main page header
			 * 
			 */
			var clusterConfigUrl = baseUrl + "/monitor/" + clusterId
			+ "/clusterdetails";
			clusterConfigurationDetail = com.impetus.ankush.placeAjaxCall(
					clusterConfigUrl, 'GET', false);

			$('#clusterEnv').empty();
			$('#clusterEnv').append(clusterConfigurationDetail.output.environment.toUpperCase());
			if (clusterConfigurationDetail.output.environment.toUpperCase() == "CLOUD") {
				$('#lbl_invalidAddNodeOperation').empty();
				$('#lbl_invalidAddNodeOperation').text('Node Addition request is not applicable for clusters deployed on cloud.');
				$('#hadoopAddNodes').attr("onclick","com.impetus.ankush.hadoopMonitoring.showDialog_InvalidAddNode()");
			}
			isClusterRackEnabled = clusterConfigurationDetail.output.rackEnabled;
			if (hadoopClusterData != null) {
				
				if(hadoopClusterData.output.ecosystem.Hadoop == null)
					if(hadoopClusterData.output.ecosystem.Hadoop2 != null) {
						isHadoop2Cluster = true;
						hadoopClusterData.output.ecosystem.Hadoop = hadoopClusterData.output.ecosystem.Hadoop2;	
					}
					
				// displaying cluster name in header
				$("#hadoopClusterName").empty();
				$("#hadoopClusterName").prepend(
						hadoopClusterData.output.clusterName);

				$("#hadoopMonitoring_dfsReplication").empty();
				$("#hadoopMonitoring_dfsReplication")
				.append(
						hadoopClusterData.output.ecosystem.Hadoop.advancedConf.replicationFactorDFS);
				$("#hadoopMonitoring_vendor").empty();
				$("#hadoopMonitoring_vendor").append(
						hadoopClusterData.output.ecosystem.Hadoop.componentVendor);
				$("#hadoopMonitoring_version").empty();
				$("#hadoopMonitoring_version").append(
						hadoopClusterData.output.ecosystem.Hadoop.componentVersion);
				if (hadoopClusterEcosystemTable_HM == null) {
					hadoopClusterEcosystemTable_HM = $('#configHadoopEcoSystemTable')
					.dataTable({
						"bJQueryUI" : false,
						"bPaginate" : false,
						"bLengthChange" : true,
						"bFilter" : false,
						"bSort" : false,
						"bInfo" : false,
						"sPaginationType" : "full_numbers",
						"bAutoWidth" : false,
					});

					for ( var componentName in hadoopClusterData.output.ecosystem) {
						var componentNameTable = '<span style="font-weight:bold;" >'
							+ componentName + '</span>';
						var componentVendor = '<span style="font-weight:bold;" >'
							+ hadoopClusterData.output.ecosystem[componentName].componentVendor
							+ '</span>';
						var componentVersion = '<span style="font-weight:bold;" >'
							+ hadoopClusterData.output.ecosystem[componentName].componentVersion
							+ '</span>';
						var status = '<span style="font-weight:bold;">'
							+ hadoopClusterData.output.ecosystem[componentName].certification + '</span>';
						if (componentName != 'Hadoop') {
							hadoopClusterEcosystemTable_HM
							.fnAddData([
							            componentNameTable,
							            componentVendor,
							            componentVersion,
							            status,
							            '<div><a href="#" onclick="com.impetus.ankush.hadoopMonitoring.hadoopComponentsDetailView(\''
							            + componentName
							            + '\',\'1\');"><img  src="'
							            + baseUrl
							            + '/public/images/icon-chevron-right.png" /></a></div>' ]);
						}
					}
				}
			}
		},

		// function used to define tooltip title on the slider of hadoop Ecosystem components details page
		toolTipDecide : function(pageId){
			var toolTipTitle = null;
			if (pageId == '1'){
				toolTipTitle = hadoopClusterData.output.clusterName;
			}else{
				toolTipTitle = 'Hadoop Ecosystem';
			}
			return toolTipTitle;
		},

		// function used to open Ecosystem components details page
		hadoopComponentsDetailView : function(componentName,pageId) {
			var sliderTitle = com.impetus.ankush.hadoopMonitoring.toolTipDecide(pageId);
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/componentDetailsView/'
						+ componentName,
						method : 'get',
						title : componentName,
						tooltipTitle : sliderTitle,
						params : {},
						callback : function(data) {
							com.impetus.ankush.hadoopMonitoring
							.showAdvSettingsDiv(data.componentName);
							com.impetus.ankush.hadoopMonitoring
							.ecosystemDetailsView(data.componentName);
						},
						callbackData : {
							componentName : componentName
						}
					});
		},

		// function used to show advanced configuration parameters used in Hive, Hbase & Zookeeper component
		showAdvSettingsDiv : function(componentName) {
			if (componentName == "Hive" || componentName == "Zookeeper"
				|| componentName == "Hbase") {
				$(".component-" + componentName).show();
			} else {
				$(".advanceComponents").hide();
			}
		},

		// function used to show hadoop ecosystem components configuration parameters
		ecosystemDetailsView : function(componentName) {
			$("#vendor-" + componentName).empty();
			$("#vendor-" + componentName)
			.append(
					hadoopClusterData.output.ecosystem[componentName].componentVendor);
			$("#version-" + componentName).empty();
			$("#version-" + componentName)
			.append(
					hadoopClusterData.output.ecosystem[componentName].componentVersion);
			$("#certified-" + componentName).empty();
			$("#certified-" + componentName).append(hadoopClusterData.output.ecosystem[componentName].certification);

			if (hadoopClusterData.output.ecosystem[componentName].tarballUrl == "") {
				$("#localPathRadio-" + componentName).attr("checked", true);
				$("#path-" + componentName)
				.append(
						hadoopClusterData.output.ecosystem[componentName].localBinaryFile);
			} else {
				$("#DownloadRadio-" + componentName).attr("checked", true);
				$("#path-" + componentName)
				.append(
						hadoopClusterData.output.ecosystem[componentName].tarballUrl);
			}
			$("#installationPath-" + componentName).empty();
			$("#installationPath-" + componentName)
			.append(
					hadoopClusterData.output.ecosystem[componentName].installationPath);
			switch (componentName) {
			case "Hive":
				if (hadoopClusterData.output.ecosystem[componentName].node.publicIp != null) {
					$("#hiveServerHadoop")
					.text(
							hadoopClusterData.output.ecosystem[componentName].node.publicIp);
				} else {
					$("#hiveServerHadoop").text('--');
				}

				for ( var i = 0; i < hadoopClusterData.output.ecosystem[componentName].advancedConf["javax.jdo.option.ConnectionPassword"].length; i++) {
					$("#hiveConnectionPassword").append("*");
				}
				$("#hiveConnectionURL")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["javax.jdo.option.ConnectionURL"]);
				$("#hiveConnectionDriverName")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["javax.jdo.option.ConnectionDriverName"]);
				$("#hiveConnectionUserName")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["javax.jdo.option.ConnectionUserName"]);

				break;
			case "Zookeeper":
				$("#zookeeperNodes").text('');
				var zkNodes = hadoopClusterData.output.ecosystem[componentName].nodes;
				var zkNodesArr = [];
				$.each(zkNodes, function(index, node) {
					zkNodesArr.push(node.publicIp);
				});
				$("#zookeeperNodes").append(zkNodesArr.join(", "));

				$("#zookeeperClientPort")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf.clientPort);
				$("#zookeeperDataDir")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf.dataDir);
				$("#zookeeperSyncLimit")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf.syncLimit + " milliseconds");
				$("#zookeeperInitLimit")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf.initLimit + " milliseconds");
				$("#zookeeperTickTime")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf.tickTime + " milliseconds");

				break;
			case "Hbase":
				$("#hbaseMasterHadoop").append(hadoopClusterData.output.ecosystem[componentName].hbaseMasterNode.publicIp);
				$("#hbaseRegionServersHadoop").text('');
				var hbaseRS = hadoopClusterData.output.ecosystem[componentName].hbaseRegionServerNodes;
				var hbaseRSArr = [];
				
				$.each(hbaseRS , function(index, node) {
					hbaseRSArr.push(node.publicIp);
				});
				$("#hbaseRegionServersHadoop").append(hbaseRSArr.join(", "));

				$("#hbaseCompactionThreshold")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.hstore.compactionThreshold"]);
				$("#hbaseCacheSize")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hfile.block.cache.size"] + " %");
				$("#hbaseFilesize")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.hregion.max.filesize"] + " bytes");
				$("#hbaseCaching")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.client.scanner.caching"]);
				$("#hbaseTimeout")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["zookeeper.session.timeout"]  + " milliseconds");
				$("#hbaseMultiplier")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.hregion.memstore.block.multiplier"]);
				$("#hbaseMajorcompaction")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.hregion.majorcompaction"] + " milliseconds");
				$("#hbaseMaxsize")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.client.keyvalue.maxsize"] + " bytes");
				$("#hbaseFlushSize")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.hregion.memstore.flush.size"] + " bytes");
				$("#hbaseHandlerCount")
				.append(
						hadoopClusterData.output.ecosystem[componentName].advancedConf["hbase.regionserver.handler.count"]);

				break;
			}
		},

		// function used to open hadoop cluster advanced settings monitoring page
		hadoopEcosystemAdvanceSettings : function(pageId) {
			var sliderTitle = com.impetus.ankush.hadoopMonitoring.toolTipDecide(pageId);
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/configurations/hadoopEcosystemAdvancedSettings',
						method : 'get',
						title : 'Hadoop Advanced Settings',
						tooltipTitle : sliderTitle,
						params : {},
						callback : function() {
							com.impetus.ankush.hadoopMonitoring
							.loadAdvancedData();
						}
					});
		},

		// function used to show hadoop cluster advanced settings data
		loadAdvancedData : function() {
			$("#hadoopMonitoringAdvancedSettings_includeS3SupportHadoop").attr(
					"disabled", true);
			$("#hadoopMonitoringAdvancedSettings_includeS3nSupportHadoop").attr(
					"disabled", true);
			if (hadoopClusterData != null) {
				$("#hadoopMonitoringAdvancedSettings_dfsReplication")
				.append(
						hadoopClusterData.output.ecosystem.Hadoop.advancedConf.replicationFactorDFS);
				$("#hadoopMonitoringAdvancedSettings_vendor").append(
						hadoopClusterData.output.ecosystem.Hadoop.componentVendor);
				$("#hadoopMonitoringAdvancedSettings_version").append(
						hadoopClusterData.output.ecosystem.Hadoop.componentVersion);
				$("#hadoopMonitoringAdvancedSettings_nameNodePath")
				.append(
						hadoopClusterData.output.ecosystem.Hadoop.advancedConf.nameNodePath);
				$("#hadoopMonitoringAdvancedSettings_dataNodePath")
				.append(
						hadoopClusterData.output.ecosystem.Hadoop.advancedConf.dataDir);
				$("#hadoopMonitoringAdvancedSettings_mapredTempPath")
				.append(
						hadoopClusterData.output.ecosystem.Hadoop.advancedConf.mapRedTmpDir);
				$("#hadoopMonitoringAdvancedSettings_hadoopTempPath")
				.append(
						hadoopClusterData.output.ecosystem.Hadoop.advancedConf.hadoopTmpDir);
				$("#hadoopMonitoringAdvancedSettings_installationPath").append(
						hadoopClusterData.output.ecosystem.Hadoop.installationPath);
				if (hadoopClusterData.output.ecosystem.Hadoop.tarballUrl == "") {
					$("#hadoopRadioSourceLocal").attr("checked", true);
					$("#hadoopSourcePathView")
					.append(
							hadoopClusterData.output.ecosystem.Hadoop.localBinaryFile);
				} else {
//					$("#hadoopRadioSourceDownload").attr("checked", true);
					$('#hadoopRadioSourceDownload').addClass('active');
					$('#hadoopRadioSourceLocal').removeClass('active');
					$("#hadoopSourcePathView").append(
							hadoopClusterData.output.ecosystem.Hadoop.tarballUrl);
				}
				if (hadoopClusterData.output.ecosystem.Hadoop.advancedConf.includes3) {
					$("#s3Div").css("display", "block");
					$("#hadoopMonitoringAdvancedSettings_includeS3SupportHadoop")
					.attr("checked", true);
					$("#hadoopMonitoringAdvancedSettings_acessKeyS3")
					.append(
							hadoopClusterData.output.ecosystem.Hadoop.advancedConf.s3AccessKey);
					$("#hadoopMonitoringAdvancedSettings_secretKeyS3")
					.append(
							hadoopClusterData.output.ecosystem.Hadoop.advancedConf.s3SecretKey);
				}
				if (hadoopClusterData.output.ecosystem.Hadoop.advancedConf.includes3n) {
					$("#s3nDiv").css("display", "block");
					$("#hadoopMonitoringAdvancedSettings_includeS3nSupportHadoop")
					.attr("checked", true);
					$("#hadoopMonitoringAdvancedSettings_acessKeyS3n")
					.append(
							hadoopClusterData.output.ecosystem.Hadoop.advancedConf.s3nAccessKey);
					$("#hadoopMonitoringAdvancedSettings_secretKeyS3n")
					.append(
							hadoopClusterData.output.ecosystem.Hadoop.advancedConf.s3nSecretKey);
				}

			}
		},

		// function used to open hadoop cluster events page
		/*events : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/events/'
						+ clusterId,
						method : 'get',
						title : "Events",
						tooltipTitle : hadoopClusterData.output.clusterName,
						params : {},
					});
		},
*/
		// function used to open hadoop cluster logs page
		/*logs : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl + '/hadoop-cluster-monitoring/logs/'
						+ clusterId,
						method : 'get',
						previousCallBack : "com.impetus.ankush.hadoopMonitoring.logsPreviousCallBack()",
						title : "Logs",
						tooltipTitle : hadoopClusterData.output.clusterName,
						params : {},
					});
		},

*/
		// function used to open hadoop cluster audit trails page
		/*auditTrail : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/auditTrail/'
						+ clusterId,
						method : 'get',
						title : "Audit Trails",
						tooltipTitle : hadoopClusterData.output.clusterName,
						params : {},
					});
		},
*/
		// function used to open hadoop cluster audit trail detail page
		/*auditTrailDetail : function(i) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/auditTrailDetail',
				method : 'get',
				title : "Audit Trail Details",
				tooltipTitle : 'Audit Trails',
				params : auditTrailRows[i]
			});
		},
*/
		// function used to open hadoop cluster add node page
		addNodes : function(clusterId) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/addNodes/' + clusterId,
				method : 'get',
				title : 'Add Nodes',
				tooltipTitle : hadoopClusterData.output.clusterName,
				previousCallBack : "com.impetus.ankush.hadoopMonitoring.slideOut_AddNodesPage();",
				params : {},
			});
		},

		// function used to open hadoop cluster commands page
		commands : function(clusterId) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/commands',
				method : 'get',
				title : 'Commands',
				tooltipTitle : hadoopClusterData.output.clusterName,
				params : {},
			});
		},

		// function used to open hadoop cluster Archive command page
		commandArchive : function() {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/commands/archive',
				method : 'get',
				title : 'Command-Archive',
				tooltipTitle : 'Commands',
				params : {},
			});
		},

		// function used to open hadoop cluster Distcp command page
		commandDistcp : function() {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/commands/distcp',
				method : 'get',
				title : 'Command-Distcp',
				tooltipTitle : 'Commands',
				params : {},
			});
		},

		// function used to open hadoop cluster Balancer command page
		commandBalancer : function() {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/commands/balancer',
				method : 'get',
				title : 'Command-Balancer',
				tooltipTitle : 'Commands',
				params : {},
			});
		},

		// function used to open hadoop cluster Fsck command page
		commandFsck : function() {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/commands/fsck',
				method : 'get',
				title : 'Command-Fsck',
				tooltipTitle : 'Commands',
				params : {},
			});
		},

		// function used to open Configurations page
		configuration : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/configurations/'
						+ clusterId,
						method : 'get',
						title : "Configurations",
						tooltipTitle : hadoopClusterData.output.clusterName,
						params : {},
					});
		},

		// function used to open cluster Configurations page
		configurationCluster : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/configurations/cluster',
						method : 'get',
						params : {},
						title : 'Cluster Configurations',
						tooltipTitle : 'Configurations',
						callback : function(data) {
							com.impetus.ankush.hadoopMonitoring
							.loadClusterInfo(data.clusterId);
						},
						callbackData : {
							clusterId : clusterId
						}
					});
		},

		// function used to show cluster details
		loadClusterInfo : function(clusterId) {
			if (clusterConfigurationDetail != null) {
				$("#clusterConfiguration_clusterName").append(
						clusterConfigurationDetail.output.clusterName);
				$("#clusterConfiguration_description").append(
						clusterConfigurationDetail.output.description);
				$("#clusterConfiguration_environment").append(
						clusterConfigurationDetail.output.environment);

				if (clusterConfigurationDetail.output.javaConf.install) {
					$("#config_java_checkbox").attr("checked", true);
					$("#config_javaBundlePath").empty();
					$("config_javaBundlePath").append(clusterConfigurationDetail.output.javaConf.javaBundle);
					$("#config_javaHomePath").empty();
					$("#config_javaHomePath").append('N/A');
				} else {
					$("#config_java_checkbox").removeAttr("checked");
					$("config_javaBundlePath").empty();
					$("config_javaBundlePath").append('N/A');
					$("#config_javaHomePath").empty();
					$("#config_javaHomePath").append(clusterConfigurationDetail.output.javaConf.javaHomePath);
				}
				if (clusterConfigurationDetail.output.environment == "Cloud") {
					$('#clouddetails').show();
					$('#cloudEnv-action').show();
					$('#lbl_hCloud_ServiceProvider').html('').text(clusterConfigurationDetail.output.cloudConf.providerName);
					$('#lbl_hCloud_Username').html('').text(clusterConfigurationDetail.output.cloudConf.username);
					$('#lbl_hCloud_ClusterSize').html('').text(clusterConfigurationDetail.output.cloudConf.clusterSize);
					$('#lbl_hCloud_KeyPairs').html('').text(clusterConfigurationDetail.output.cloudConf.keyPairs);
					$('#lbl_hCloud_SecurityGroup').html('').text(clusterConfigurationDetail.output.cloudConf.securityGroup);
					$('#lbl_hCloud_Region').html('').text(clusterConfigurationDetail.output.cloudConf.region);
					$('#lbl_hCloud_Zone').html('').text(clusterConfigurationDetail.output.cloudConf.zone);
					$('#lbl_hloud_InstanceType').html('').text(clusterConfigurationDetail.output.cloudConf.instanceType);
					$('#lbl_hCloud_MachineImage').html('').text(clusterConfigurationDetail.output.cloudConf.machineImage);
					$('#autoTerminateYes').attr('disabled', true);
					$('#autoTerminateNo').attr('disabled', true);
					if(clusterConfigurationDetail.output.cloudConf.autoTerminate) {
						$('#autoTerminateYes-action').css('display' ,'block');
						$('#autoTerminateYes').attr('checked', true);
						$('#autoTerminateNo').attr('checked', false);
						$('#autoTerminateYes-action').show();

						$('#lbl_hCloud_TimeoutInterval').text(clusterConfigurationDetail.output.cloudConf.timeOutInterval);
					}
					else {
						$('#autoTerminateYes-action').hide();
						$('#autoTerminateYes-action').css('display' ,'none');
						$('#autoTerminateNo').attr('checked', true);
						$('#autoTerminateYes').attr('checked', false);
						$('#lbl_hCloud_TimeoutInterval').text('--');
					}
				}
				$("#config_clusterUserName").empty().append(
						clusterConfigurationDetail.output.authConf.username);

				switch (clusterConfigurationDetail.output.authConf.type) {
				case "password":
					$("#config_nodeauth_radio_password").attr("checked", true);
					$("#config_nodeauth_authType").empty();
					$("#config_nodeauth_authType").append('Password:');
					for ( var i = 0; i < clusterConfigurationDetail.output.authConf.password.length; i++) {
						$("#config_clusterPassword").append("*");
					}
					break;
				case "sharedKey":
					$("#config_nodeauth_radio_sharedkey").attr("checked", true);
					$("#config_nodeauth_authType").empty();
					$("#config_nodeauth_authType").append('Shared Key:');
					$("#config_clusterPassword").empty();
					$("#config_clusterPassword").append(
							clusterConfigurationDetail.output.authConf.password);
					break;
				}
			}
		},

		// function used to open Hadoop Ecosystem Configurations page
		configurationHadoopEcosystem : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/configurations/hadoopEcosystem',
						method : 'get',
						title : 'Hadoop Ecosystem',
						tooltipTitle : 'Configurations',
						params : {},
						callback : function(data) {
							com.impetus.ankush.hadoopMonitoring.loadHadoopEcosystemInfo(data.clusterId);
						},
						callbackData : {
							clusterId : clusterId
						}
					});
		},

		// function used to load hadoop cluster details viz. replication factor, vendor, version, etc.
		loadHadoopEcosystemInfo : function(clusterId) {
			if (hadoopClusterData != null) {
				$("#configHadoopDfsReplication")
				.append(hadoopClusterData.output.ecosystem.Hadoop.advancedConf.replicationFactorDFS);
				$("#configHadoopVendor").append(hadoopClusterData.output.ecosystem.Hadoop.componentVendor);
				$("#configHadoopVersion").append(hadoopClusterData.output.ecosystem.Hadoop.componentVersion);
				com.impetus.ankush.hadoopMonitoring.loadClusterEcosystemTable(clusterId);
			}
		},

		// function used to show hadoop ecosystem components details in a table
		loadClusterEcosystemTable : function(clusterId) {
			var configurationPageEcosystemTable = $(
			'#HadoopEcoSystemTable_ManageConfig').dataTable({
				"bJQueryUI" : false,
				"bPaginate" : false,
				"bLengthChange" : true,
				"bFilter" : false,
				"bSort" : false,
				"bInfo" : false,
				"bAutoWidth" : false,
				"sPaginationType" : "full_numbers",
				"bAutoWidth" : false,
				"bRetrieve" : true,
			});
			for ( var componentName in hadoopClusterData.output.ecosystem) {
				var componentNameTable = '<span style="font-weight:bold;" >'
					+ componentName + '</span>';
				var componentVendor = '<span style="font-weight:bold;" >'
					+ hadoopClusterData.output.ecosystem[componentName].componentVendor
					+ '</span>';
				var componentVersion = '<span style="font-weight:bold;" >'
					+ hadoopClusterData.output.ecosystem[componentName].componentVersion
					+ '</span>';
				var status = '<span style="font-weight:bold;">' + hadoopClusterData.output.ecosystem[componentName].certification
				+ '</span>';
				if ( !(componentName == 'Hadoop' || componentName == 'Hadoop2') ) {
					configurationPageEcosystemTable
					.fnAddData([
					            componentNameTable,
					            componentVendor,
					            componentVersion,
					            status,
					            '<div><a href="#" onclick="com.impetus.ankush.hadoopMonitoring.hadoopComponentsDetailView(\''
					            + componentName
					            + '\',\'3\');"><img  src="'
					            + baseUrl
					            + '/public/images/icon-chevron-right.png" /></a></div>' ]);
				}
			}
		},

		// function used to open alerts configurations page
		configurationAlerts : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/configurations/alerts/'
						+ clusterId,
						title : 'Alerts',
						tooltipTitle : 'Configurations',
						method : 'get',
						title : "Alerts",
						params : {},
					});
		},

		// function used to show default alerts value
		getDefaultAlertValues : function(clusterId) {
			var alertUrl = baseUrl + '/monitor/' + clusterId + '/alertsConf';
			alertResult = com.impetus.ankush.placeAjaxCall(alertUrl, "GET", false);
			$("#refreshInterval").html('').text(alertResult.output.refreshInterval);
			if (alertResult.output.mailingList != "") {
				$("#mailingList").html('').text(alertResult.output.mailingList);
			} else {
				$("#mailingList").html('').text("--");
			}
			if (alertResult.output.informAllAdmins) {
				$("#alertInform").attr('checked', 'checked');
			} else {
				$("#alertInform").removeAttr('checked');
			}
			if (configAlertsUsageTable != null) {
				configAlertsUsageTable.fnClearTable();
			} else {
				configAlertsUsageTable.datatable();
			}
			for ( var i = 0; i < alertResult.output.thresholds.length; i++) {
				configAlertsUsageTable.fnAddData([
				                                  '<a class="" id="metricName' + i
				                                  + '" style="text-align: right;">'
				                                  + alertResult.output.thresholds[i].metricName
				                                  + '</a>',
				                                  '<span class="editableLabel" id="warningLevel-' + i
				                                  + '" style="width:20px;">'
				                                  + alertResult.output.thresholds[i].warningLevel
				                                  + '</span>',
				                                  '<span class="editableLabel" id="alertLevel-' + i + '">'
				                                  + alertResult.output.thresholds[i].alertLevel
				                                  + '</span>', ]);
			}
			$('.editableLabel').editable({
				type : 'text',
				validate :function(value){
					return com.impetus.ankush.hadoopMonitoring.checkUsageValue(this,value);}
			});
		},

		// function used to validate warning and alert level values, viz. both values should be witin 0-100 & warning level should be less than alert level 
		checkUsageValue : function(elem,value) {
			if(!com.impetus.ankush.validation.pattern(value,/^[\d]*$/)) {
				return "Invalid value";
			}else if(!com.impetus.ankush.validation.between(value,'0','100')){
				return "Invalid value. Should be within 0-100";
			}
			var elementId= elem.id;
			var id = elementId.split('-');
			if(id[0]=="warningLevel"){
				if (parseInt(value,10) >= parseInt($('#alertLevel-'+id[1]).text(),10)) {
					return "Invalid value";
				}
			}else if(id[0]=="alertLevel"){
				if (parseInt(value,10) <= parseInt($('#warningLevel-'+id[1]).text(),10)) {
					return "Invalid value";
				}
			}
		},

		// function used to initialize audit-trail table and load values in it.
		initHadoopAuditTrail : function(clusterId) {
			var hadoopAuditTrailTable = $('#hadoopAuditTrailTable').dataTable({
				"aoColumns" : [ {
					"sWidth" : "23%"
				}, {
					"sWidth" : "5%"
				}, {
					"sWidth" : "24%"
				}, {
					"sWidth" : "10%"
				}, {
					"sWidth" : "6%"
				}, {
					"sWidth" : "22%"
				}, {
					"sWidth" : "10%"
				}, ],
				"bJQueryUI" : false,
				"bPaginate" : false,
				"bLengthChange" : true,
				"bFilter" : true,
				"bSort" : false,
				"bInfo" : false,
				"bAutoWidth" : false,
				"sPaginationType" : "full_numbers"
			});
			$('#hadoopAuditTrailTable_filter').hide();
			$('#searchAuditTrail').keyup(function() {
				$("#hadoopAuditTrailTable").dataTable().fnFilter($(this).val());
			});
			var auditTrailUrl = baseUrl + '/monitor/' + clusterId + '/audits';
			var auditTrailData = com.impetus.ankush.placeAjaxCall(auditTrailUrl,
					'GET', false);
			var auditTrailLength = auditTrailData.output.length;
			var auditTrailOutput = auditTrailData.output;
			var mainList = [];
			if (auditTrailLength > 0) {
				for ( var i = 0; i < auditTrailLength; i++) {
					var dataList = [];
					var tempDate = new Date(parseInt(auditTrailOutput[i].date));
					var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
					var fileName = '<span style="font-weight:bold;" id="fileName-'
						+ i + '"/>' + auditTrailOutput[i].source + '</span>';
					var auditType = '<span style="font-weight:bold;" id="auditType-'
						+ i + '">' + auditTrailOutput[i].type + '</span>';
					var auditPropertyName = '<span style="font-weight:bold;" id="auditPropertyName-'
						+ i
						+ '">'
						+ auditTrailOutput[i].propertyName
						+ '</span>';
					var auditPropertyValue = '<span style="font-weight:bold;" id="auditPropertyValue-'
						+ i
						+ '">'
						+ auditTrailOutput[i].propertyValue
						+ '</span>';
					var auditUser = '<span style="font-weight:bold;" id="auditUser-'
						+ i + '">' + auditTrailOutput[i].username + '</span>';
					var auditTime = '<span style="font-weight:bold;" id="auditTime-'
						+ i + '">' + date + '</span>';
					var childPageLink = '<a href="#" id="navigationImgAddHadoop'
						+ i
						+ '" onclick="com.impetus.ankush.hadoopMonitoring.auditTrailDetail('
						+ i + ');"><img src="' + baseUrl
						+ '/public/images/icon-chevron-right.png"/></a>';
					auditTrailRows[i] = auditTrailOutput[i];
					dataList.push(fileName);
					dataList.push(auditType);
					dataList.push(auditPropertyName);
					dataList.push(auditPropertyValue);
					dataList.push(auditUser);
					dataList.push(auditTime);
					dataList.push(childPageLink);
					mainList.push(dataList);
				}
			}
			hadoopAuditTrailTable.fnAddData(mainList);
		},

		// function used to initialize events table and load values in it.
		initHadoopEvents : function(clusterId) {
			var hadoopEventsTable = $('#hadoopEventsTable').dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : true,
				"bFilter" : true,
				"bSort" : true,
				"bInfo" : false,
				"bAutoWidth" : false,
				"sPaginationType" : "full_numbers",
				"bAutoWidth" : false,
				"aoColumnDefs": [
				                 { 'sType': "ip-address", 'aTargets': [3] }
				                 ], 
			});
			$('#hadoopEventsTable_filter').hide();
			$('#searchEvents').keyup(function() {
				$("#hadoopEventsTable").dataTable().fnFilter($(this).val());
			});
			var eventsUrl = baseUrl + '/monitor/' + clusterId + "/events";
			eventsData = com.impetus.ankush.placeAjaxCall(eventsUrl, 'GET', false);
			var eventsLength = eventsData.output.events.length;
			eventsOutput = eventsData.output.events;
			var mainList = [];
			if (eventsLength > 0) {
				for ( var i = 0; i < eventsLength; i++) {
					var dataList = [];
					var tempDate = new Date(parseInt(eventsOutput[i].date));
					var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
					var eventName = eventsOutput[i].name;
					var eventType = eventsOutput[i].type;
					var eventSeverity =eventsOutput[i].severity;
					var eventHost = eventsOutput[i].host;
					var eventDescription = eventsOutput[i].currentValue;
					var eventTime = date;
					eventsRows[i] = eventsOutput[i];
					dataList.push(eventName);
					dataList.push(eventType);
					dataList.push(eventSeverity);
					dataList.push(eventHost);
					dataList.push(eventDescription);
					dataList.push(eventTime);
					mainList.push(dataList);
				}
			}
			hadoopEventsTable.fnAddData(mainList);
		},

		// function used to show diffrent types of node in a cluster, viz. namenode , secondary namenode, etc.
		getDefaultLogDownloadValue : function(clusterId) {
			$("#fileSizeExceed").appendTo('body');
			readCount = 0;
			var logUrl = baseUrl + '/monitor/' + clusterId
			+ '/techlogs';
			logData = com.impetus.ankush.placeAjaxCall(logUrl, 'GET', false);
			if (logData != null) {
				$("#nodeType").empty();
				for ( var type in logData.output) {
					if (type != "status") {
						$("#nodeType").append(
								"<option id='" + type + "'>" + type + "</option>");
					}
				}
				com.impetus.ankush.hadoopMonitoring.fillNodeType(clusterId);
			}
		},

		// function used to get nodes Ip depending on the type of node selected in the node type Dropdown
		fillNodeType : function(clusterId) {
			com.impetus.ankush.hadoopMonitoring.removeErrorClass();

			$("#nodeIP").empty();
			var currentNodeType = $("#nodeType").val();
			var numberOfNodes = logData.output[currentNodeType].length;
			logDataOutput = logData.output[currentNodeType];
			$("#div_Logs").css("display", "none");
			if (numberOfNodes > 0) {

				for ( var node in logData.output[currentNodeType]) {

					$("#nodeIP").append(
							"<option id='node-" + node + "'>" + logDataOutput[node]
							+ "</option>");
				}
				com.impetus.ankush.hadoopMonitoring.nodeIPChange(clusterId);
			}
		},

		// function used to show all the log files in hadoop cluster of node selected
		nodeIPChange : function(clusterId) {
			com.impetus.ankush.hadoopMonitoring.removeErrorClass();
			var currentNodeType = $("#nodeType").val();
			var currentNodeIP = $("#nodeIP").val();
			var logFilenameUrl = baseUrl + '/monitor/' + clusterId
			+ '/files?ip=' + currentNodeIP + '&type=' + currentNodeType;

			var logFileNameData = com.impetus.ankush.placeAjaxCall(logFilenameUrl,
					'GET', false);
			$("#div_Logs").css("display", "none");
			if (logFileNameData != null) {
				if(!logFileNameData.output.status){
					com.impetus.ankush.hadoopMonitoring.validateLogs(logFileNameData,"nodeDown");
					$("#fileName").empty();
				}else{

					logFileNameOutput = logFileNameData.output.files;
					$("#fileName").empty();
					var i =0 ;
					$.each(logFileNameOutput, function(key, value) {
						$("#fileName").append(
								"<option id='filename-" + i + "'>"
								+ value + "</option>");
						i++;
					});
					$("#logView").empty();
				}
			}
		},

		// function called during selection of a diffrent log file in a dropdown to clear the previous file data
		fileNameChange : function() {
			com.impetus.ankush.hadoopMonitoring.removeErrorClass();
			$("#logView").empty();
			$("#div_Logs").css("display", "none");
		},

		// function used to display logs of the file selected. Data is loaded using lazy loading
		logDisplay : function(clusterId) {
			readCount = 0;
			$(window).unbind("scroll");
			$(window).bind("scroll" ,function () {        	
				if ($(window).scrollTop() == ( $(document).height() - $(window).height())) {
					com.impetus.ankush.hadoopMonitoring.appendLog(clusterId);
				}
			});

			var currentNodeIP = $("#nodeIP").val();
			var currentFileName = $("#fileName").val();

			var logFileURL = baseUrl + '/monitor/' + clusterId + '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000';

			var logFileData = com.impetus.ankush.placeAjaxCall(logFileURL, 'GET',
					false);
			if (logFileData != null) {
				if(logFileData.output.status) {
					$("#div_Logs").css("display", "block");
					$("#logView").empty();
					$("#logView").append(logFileData.output.content);
					readCount = logFileData.output.readCount;
				}else{
					com.impetus.ankush.hadoopMonitoring.validateLogs(logFileData,"filename");
				}
			} else {
				alert("Sorry, unable to retrieve the Log file");
			}
		},

		// function used to download a log file
		download : function(clusterId) {
			var currentNodeIP = $("#nodeIP").val();
			var currentFileName = $("#fileName").val();

			var downloadUrl = baseUrl + '/monitor/' + clusterId
			+ '/download?ip=' + currentNodeIP + '&fileName='+ currentFileName;

			var downloadFileSize = logFileNameOutput[currentFileName];

			if(downloadFileSize>5120){
				$("#fileSizeExceed").modal('show');
				$('.ui-dialog-titlebar').hide();
				$('.ui-dialog :button').blur();
				return;
			}
			var downloadUrlData = com.impetus.ankush.placeAjaxCall(downloadUrl,
					'GET', false);
			if (downloadUrlData.output.status == true) {
				var downloadFilePath = baseUrl + downloadUrlData.output.downloadPath;
				var hiddenIFrameID = 'hiddenDownloader', iframe = document.getElementById(hiddenIFrameID);
				if (iframe === null) {
					iframe = document.createElement('iframe');
					iframe.id = hiddenIFrameID;
					iframe.style.display = 'none';
					document.body.appendChild(iframe);
				}
				iframe.src = downloadFilePath;
			}else {
				com.impetus.ankush.hadoopMonitoring.validateLogs(downloadUrlData,"filename");
			}
		},

		// function used to validate whether a log file can be dowloaded or not
		validateLogs : function(urlData,errorType) {
			var focusDivId = null;
			if(errorType=="nodeDown"){
				focusDivId = "nodeIP";
			}else if(errorType=="filename"){
				focusDivId = "fileName";
			}
			$("#popover-content-hadoopLogs").empty();
			var i=0;
			$.each( urlData.output.error,
					function(index,value){
				i = index + 1;
				com.impetus.ankush.validation.addNewErrorToDiv(focusDivId,'popover-content-hadoopLogs',value,null);
			});
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoopLogs', 'errorBtnHadoopLogs');
				return;
			}
		},

		// function used to remove error-class
		removeErrorClass : function() {
			$('#nodeIP').removeClass('error-box');
			$('#fileName').removeClass('error-box');
			$("#error-div-hadoopLogs").css("display", "none");
			$('#errorBtnHadoopLogs').css("display", "none");
		},

		// function used to hide generic options used in commands
		genericOptionsDivHide : function(command) {
			$("#command_"+command+"OptionsValue").empty();
			$("#command_"+command+"OptionsValueText").css("display", "none");
		},

		// function used to show generic options used in commands
		genericOptionsDivShow : function(command) {
			$("#command_"+command+"OptionsValue").empty();
			$("#command_"+command+"OptionsValueText").css("display", "block");
		},

		// function used to append log data, during lazy-load, at the end of the previously shown logs
		appendLog : function(clusterId) {
			var currentNodeIP = $("#nodeIP").val();
			var currentFileName = $("#fileName").val();

			var logFileURL = baseUrl + '/monitor/' + clusterId
			+ '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000';

			var logFileData = com.impetus.ankush.placeAjaxCall(logFileURL, 'GET',
					false);
			if (logFileData != null) {
				if(logFileData.output.status) {
					$("#div_Logs").css("display", "block");
					$("#logView").append(logFileData.output.content);
					readCount = logFileData.output.readCount;
				}
				else{
					$(window).unbind("scroll");
				}
			} else {
				alert("Sorry, unable to retrieve the Log file");
			}
		},
		
		removePage : function(){
			com.impetus.ankush.removeChild($.data(document, "panels").children.length);
		},

		// function used to validate commands data
		validateRunEvent : function(command) {
			$("#popover-content").empty();
			$("#errorBtnHadoop").val('');
			$("#error-div-hadoop").css('display', 'none');
			$("#errorBtnHadoop").css('display', 'none');
			
			com.impetus.ankush.validation.errorCount = 0;
			switch (command) {
			case "archive":
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_archive', 'Archive', 'popover-content');
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_archive_parentPath', 'Parent Path', 'popover-content');
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_archive_src', 'Source Directory', 'popover-content');
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_archive_dest', 'Destination directory', 'popover-content');
				break;
			case "distcp":
				var optionsDistcpDiv = document.getElementById("command_distcpOptionsValueText");
				if(optionsDistcpDiv.style.display != "none"){

					com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_distcpOptionsValue', 'Options', 'popover-content');
				}
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_distcp_srcurl', 'Source URL', 'popover-content');
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_distcp_desturl', 'Destination URL', 'popover-content');
				break;
			case "balancer":

				if($('#command_balancerThreshold').val()!=""){

					com.impetus.ankush.validation.validateField('between_O_100', 'command_balancerThreshold', 'Threshold', 'popover-content');
				};
				break;
			case "fsck":

				var optionsFsckDiv = document.getElementById("command_fsckOptionsValueText");
				if(optionsFsckDiv.style.display != "none"){
					com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_fsckOptionsValue', 'Generic Options', 'popover-content');
				}
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'command_fsckPath', 'Path', 'popover-content');
				break;
			}
		},

		// function used to run command selected
		commandsData : function(command) {
			com.impetus.ankush.hadoopMonitoring.validateRunEvent(command);

			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			var commandsUrl = baseUrl + "/cluster/" + com.impetus.ankush.commonMonitoring.clusterId
			+ "/commands/hadoop/" + command;
			var commandData = {};
			switch (command) {
			case "archive":
				commandData.archiveName = $("#command_archive").val();
				commandData.parentDirectory = $("#command_archive_parentPath").val();
				commandData.sourceDirectory = $("#command_archive_src").val();
				commandData.destinationDirectory = $("#command_archive_dest").val();
				break;
			case "distcp":
				if(document.getElementById("command_distcpOptionsValueText").style.display != "none"){
					commandData.options = $("#command_distcpOptions").val() + $("#command_distcpOptionsValue").val();
				}else{
					commandData.options = $("#command_distcpOptions").val();
				}
				commandData.sourceURL = $("#command_distcp_srcurl").val();
				commandData.destinatinURL = $("#command_distcp_desturl").val();

				break;
			case "balancer":
				commandData.threshold = $("#command_balancerThreshold").val();
				break;
			case "fsck":

				if(document.getElementById("command_fsckOptionsValueText").style.display != "none"){
					commandData.genericOptons = $("#command_fsckOptions").val() + $("#command_fsckOptionsValue").val();
				}else{
					commandData.genericOptons = $("#command_fsckOptions").val();
				}
				commandData.path = $("#command_fsckPath").val();
				commandData.otherOptions = $("#command_fsckOtherOptions").val();
				break;
			}
			$.ajax({
				'type' : 'POST',
				'url' : commandsUrl,
				'contentType' : 'application/json',
				'data' : JSON.stringify(commandData),
				"async" : false,
				'dataType' : 'json',
				'success' : function(result) {
					com.impetus.ankush.removeChild(3);
				}
			});
		},
		// this function will populate tiletable for index drill down and node drill
		// down
		populateTileTables : function(tableName) {
				var url = baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/'+tableName;
				com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
					if (eval(tableName) != null)
						eval(tableName).fnClearTable();
					if(result.output.status == true){
						for ( var dataKey in result.output.data) {
							eval(tableName).fnAddData([
							                           dataKey,
							                           result.output.data[dataKey] 
							                          ]);
						}
					}else{
						eval(tableName).fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						eval(tableName).fnClearTable();
					}
				});
		},
		// function used to save alerts data
	/*	saveAlerts : function(clusterId) {
			var data = {};
			data.thresholds = new Array();
			var alertUrl = baseUrl + '/monitor/' + clusterId + '/alertsConf';
			data.refreshInterval = $("#refreshInterval").text();
			if($("#mailingList").text() == "--"){
				data.mailingList = "";
			}else{
				data.mailingList = $("#mailingList").text();
			}

			if ($("#alertInform").is(':checked')) {
				data.informAllAdmins = true;
			} else {
				data.informAllAdmins = false;
			}

			for ( var i = 0; i < alertResult.output.thresholds.length; i++) {
				var rowData = {
						"metricName" : $("#metricName" + i).text(),
						"warningLevel" : $("#warningLevel-" + i).text(),
						"alertLevel" : $("#alertLevel-" + i).text()
				};
				data.thresholds.push(rowData);
			}
			var result = com.impetus.ankush.placeAjaxCall(alertUrl, "POST", false,
					data);
			com.impetus.ankush.removeChild(3);
		},*/

		// function used to show dialog when node addition operation is stopped
		stopNodeAddDialog : function() {
			$("#addNodeStop").appendTo('body').modal('show');;
			$('.ui-dialog-titlebar').hide();
			$('.ui-dialog :button').blur();

			$("#cancelDeleteButton").click(function() {
				$('#addNodeStop').modal('hide');
			});
		},

		// functiom used to show individual node data, viz. graphs, service status, etc.
		/*nodepage : function() {
			com.impetus.ankush.hadoopMonitoring.loadNodesData(currentClusterId);
			com.impetus.ankush.hadoopMonitoring.nodesTileCreate();
			com.impetus.ankush.hadoopNodeDrillDown.stopAutoRefreshCalls();
		},
*/
		// function used to open page containing details of nodes in a cluster
		/*nodeDetails : function(clusterId) {
			currentClusterId = clusterId;
			// loading the child page.
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/nodesDetail',
						title : "Nodes",
						tooltipTitle : hadoopClusterData.output.clusterName,
						previousCallBack : "com.impetus.ankush.hadoopMonitoring.scrollOutPage_NodeList();",
						method : 'get',
						params : {},
						callback : function(data) {
							// calling the action method to load the data on page from server.
							com.impetus.ankush.hadoopMonitoring.initNodeDetailsTable();
							com.impetus.ankush.hadoopMonitoring.loadNodeDetailsPageContent(clusterId);
						},
						callbackData : {
							clusterId : clusterId
						}
					});
		},*/

		// function used to show nodes details and start autorefresh call to update node status in table after a scheduled interval
		/*loadNodeDetailsPageContent : function(clusterId) {

			if(document.getElementById('nodesDetailTable') == null) {
				IsAutoRefreshON_NodeList = false;
				hNodeListInterval = window.clearInterval(hNodeListInterval);
				return false;
			}
			com.impetus.ankush.hadoopMonitoring.loadNodesData(clusterId);
			com.impetus.ankush.hadoopMonitoring.nodesTileCreate();

			if(!IsAutoRefreshON_NodeList) {
				IsAutoRefreshON_NodeList = true;
				autoRefresh_NodeList("com.impetus.ankush.hadoopMonitoring.loadNodeDetailsPageContent(\""
						+ clusterId + "\");");
			}
		},
*/
		// function used to stop autorefresh call when scrolling-out from nodelist page
		scrollOutPage_NodeList : function() {
			IsAutoRefreshON_NodeList = false;
			hNodeListInterval = window.clearInterval(hNodeListInterval);
		},

		// function used to initialize node details table
		/*initNodeDetailsTable : function() {
			$("#deleteNodeDialogHadoop").appendTo('body');
			nodeDetailsTable = $('#nodesDetailTable').dataTable(
					{
						"bJQueryUI" : true,
						"bPaginate" : false,
						"bLengthChange" : true,
						"bFilter" : true,
						"bSort" : true,
						"bInfo" : false,
						"bAutoWidth" : false,
						"sPaginationType" : "full_numbers",
						"bAutoWidth" : false,
						"aoColumnDefs": [
						                 { 'bSortable': false, 'aTargets': [ 0,5 ] },
						                 { 'sType': "ip-address", 'aTargets': [1] }
						                 ], 
						                 'fnRowCallback' : function(nRow, aData, iDisplayIndex,
						                		 iDisplayIndexFull) {
						                	 $(nRow).attr('id', 'rowIdNode-' + iDisplayIndex);
						                 }
					});
			$("#nodesDetailTable_filter").css({
				'display' : 'none'
			});
			$('#searchNodeDetailTableHadoop').keyup(function() {
				$("#nodesDetailTable").dataTable().fnFilter($(this).val());
			});
		},*/

		// function used to check/uncheck all the nodes that can be deleted from the cluster during checking/unchecking of header check box
		checkAll_NodeList : function(elem) {
			var val = $('input:[name=checkHead_NodeList]').is(':checked');
			var nodeCount = nodeDetailsTable.fnGetData().length;
			var flag = false;
			if(val){
				flag = true;
				var j=0;

				for ( var i = 0; i < nodeCount; i++){
					if(!($("#checkboxNode-" + i).attr('disabled'))){
						$("#checkboxNode-" + i).attr("checked", flag);
						j++;
					}
				}
				if(j>0){
					$('#btnNodeList_DeleteNodes').removeClass('disabled');
					$("#btnNodeList_DeleteNodes").attr("onclick","com.impetus.ankush.hadoopMonitoring.sendDeleteNodesRequest()");
				}
			}else{
				if(!($("#btnNodeList_DeleteNodes").hasClass('disabled'))){
					$('#btnNodeList_DeleteNodes').addClass('disabled');
					$("#btnNodeList_DeleteNodes").removeAttr("onclick");
				}
				for ( var i = 0; i < nodeCount; i++){
					if(!($("#checkboxNode-" + i).attr('disabled'))){
						$("#checkboxNode-" + i).removeAttr('checked');
					}
				}
			}
		},

		// function used to send node delete request
		sendDeleteNodesRequest : function() {
			var nodeCount = nodeDetailsTable.fnGetData().length;
			nodeDeleteData = new Object();
			var IsNamenodeSelected = false;
			nodeDeleteData.technology = 'Hadoop';
			nodeDeleteData["@class"] = "com.impetus.ankush.hadoop.config.HadoopClusterConf";
			nodeDeleteData.nodes = [];
			for ( var i = 0; i < nodeCount; i++) {
				if ($("#checkboxNode-" + i).attr("checked")) {
					if(nodesData.output.nodes[i].nameNode) {
						IsNamenodeSelected = true;
						break;
					}
					var node = new Object();
					node.publicIp = nodesData.output.nodes[i].publicIp;
					node.privateIp = nodesData.output.nodes[i].privateIp;
					node.nameNode = nodesData.output.nodes[i].nameNode;
					node.dataNode = nodesData.output.nodes[i].dataNode;
					node.secondaryNameNode = nodesData.output.nodes[i].secondaryNameNode;
					nodeDeleteData.nodes.push(node);
				}
			}
			if(IsNamenodeSelected) {
				$("#invalidRequestMsg_ND").text('Name node cannot be deleted.');

				$("#div_RequestInvalid_NodeDetails").dialog({
					modal : true,
					resizable : false,
					dialogClass : 'alert',
					width : 400,
					draggable : true,
					modal : true,
					position : 'center',
					close : function(ev, ui) {
						$(this).dialog("destroy");
					},
				});

				$('.ui-dialog-titlebar').hide();
				$('.ui-dialog :button').blur();

				$("#checkHead_NodeList").attr("checked", false);

				com.impetus.ankush.hadoopMonitoring.checkAll_NodeList();
				return;
			}
			if (nodeDeleteData.nodes.length > 0) {
				com.impetus.ankush.hadoopMonitoring.initDeleteNodeDialog();
			} else {
				$("#invalidRequestMsg_ND").text('Select at least one node for this action.');
				$("#div_RequestInvalid_NodeDetails").dialog({
					modal : true,
					resizable : false,
					dialogClass : 'alert',
					width : 400,
					draggable : true,
					modal : true,
					position : 'center',
					close : function(ev, ui) {
						$(this).dialog("destroy");
					},
				});
				$('.ui-dialog-titlebar').hide();
				$('.ui-dialog :button').blur();
			}
			$("#checkHead_NodeList").attr("checked", false);
			com.impetus.ankush.hadoopMonitoring.checkAll_NodeList();
		},

		// function used to initialize delete node dialog
		/*initDeleteNodeDialog : function() {
			$("#deleteNodeDialogHadoop").modal('show');
			$('.ui-dialog-titlebar').hide();
			$('.ui-dialog :button').blur();
		},*/

		// function used to delete node from the cluster
		deleteNodeHadoop : function() {
			$('#deleteNodeDialogHadoop').modal('hide');
			deleteNodesUrl = baseUrl + '/cluster/' + currentClusterId
			+ '/remove';
			$.ajax({
				'type' : 'POST',
				'url' : deleteNodesUrl,
				'contentType' : 'application/json',
				'data' : JSON.stringify(nodeDeleteData),
				'dataType' : 'json',
				'success' : function(result) {

					if (result.output.status) {
						com.impetus.ankush.hadoopMonitoring.loadNodesData(currentClusterId);
						com.impetus.ankush.hadoopMonitoring.nodesTileCreate();
					}
					else {

						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content-hadoopNodesDetail', 'error-div-hadoopNodesDetail', 'errorBtn-hadoopNodesDetail');
						return;
					}
				},
			});
		},

		// function used to close dialog after request is placed
		closeSuccessDialog_ND : function() {
			$("#div_RequestSuccess_NodeDetails").dialog('close');
		},

		// function used to close dialog after request is placed
		closeReqInvalidDialog_ND : function() {
			$("#div_RequestInvalid_NodeDetails").dialog('close');
		},

		// function used to load nodes data in the node list table
		loadNodesData : function(clusterId) {
			if(document.getElementById("nodesDetail") == null) {
				IsAutoRefreshON_NodeList = false;
				hNodeListInterval = window.clearInterval(hNodeListInterval);
				return;
			}
			$("#popover-content-hadoopNodesDetail").empty();
			$("#popover-content-hadoopNodesDetail").css('display', 'none');
			$("#error-div-hadoopNodesDetail").css('display', 'none');
			$("#errorBtn-hadoopNodesDetail").css('display', 'none');

			if(nodeDetailsTable != null)
				nodeDetailsTable.fnClearTable();

			$("#checkHead_NodeList").attr("checked", false);
			var nodesUrl = baseUrl + '/monitor/' + clusterId + '/nodes';
			nodesData = com.impetus.ankush.placeAjaxCall(nodesUrl, 'GET', false);
			var flag_DeleteProgress = false;
			var mainList = [];

			// iterating the over the nodes list.
			$
			.each(
					nodesData.output.nodes,
					function(index, node) {
						var dataList = [];
						var checkboxNode =null;
						if(node.nameNode){
							checkboxNode = '<input type="checkbox" id="checkboxNode-'
								+ index + '" disabled="disabled" onclick="com.impetus.ankush.hadoopMonitoring.enableDeleteButton(\'checkboxNode-'
								+ index + '\')"/ >';
						}else{
							checkboxNode = '<input type="checkbox" id="checkboxNode-'
								+ index + '" onclick="com.impetus.ankush.hadoopMonitoring.enableDeleteButton(\'checkboxNode-'
								+ index + '\')"/ >';
						}
						var navigationLink = '<a href="#"><img id="nodeNavigation-'
							+ index
							+ '"'
							+ 'src="'
							+ baseUrl
							+ '/public/images/icon-chevron-right.png"'
							+ ' onclick="com.impetus.ankush.hadoopMonitoring.hadoopNodeDrillDown(\''
							+ node.id
							+ '\',\''
							+ node.publicIp
							+ '\',\'' + clusterId + '\');"/></a>';
						if(node.nodeState == 'removing') {
							flag_DeleteProgress = true;
							checkboxNode = '<input type="checkbox" id="checkboxNode-'
								+ index + '" disabled="disabled"/ >';
							navigationLink = '<a href="#"><img id="nodeNavigation-'
								+ index
								+ '"'
								+ 'src="'
								+ baseUrl
								+ '/public/images/icon-chevron-right.png"/></a>';
							$("#btnNodeList_DeleteNodes").addClass('disabled');
							$("#btnNodeList_DeleteNodes").removeAttr("onclick");
						}
						mainList.length = 0;
						dataList.push(checkboxNode);
						dataList.push(node.publicIp);
						dataList.push(com.impetus.ankush.hadoopMonitoring
								.getNodeType(node));
						if(node.nodeState=='deployed'){
							if(node.serviceStatus){
								dataList.push('running');
							}else{
								dataList.push('down');
							}
						}else{
							dataList.push(node.nodeState);
						}
						dataList.push(node.os);
						dataList.push(navigationLink);
						mainList.push(dataList);
						nodeDetailsTable.fnAddData(mainList);
						if(!node.serviceStatus){
							$('td', $('#rowIdNode-' + index)).addClass('error-row');
						}
					});
			if(flag_DeleteProgress) {
				var nodeCount = nodeDetailsTable.fnGetData().length;
				$("#btnNodeList_DeleteNodes").addClass('disabled');
				for(var i = 0; i < nodeCount; i++) {
					$("#checkboxNode-"+i).attr('disabled', true);
				}
			}
		},

		// function used to enable/disable delete button during checking/unchecking of node
		/*enableDeleteButton : function(id) {
			if (!$('#' + id).attr('checked')) {
				$("#checkHead_NodeList").removeAttr('checked');
				var nodesCount = nodeDetailsTable.fnGetData().length;
				var flagDisableButton = true;
				for(var i = 0; i < nodesCount; i++) {
					if ($('#checkboxNode-' + i).attr('checked')) {
						flagDisableButton = false;
					}
				}
				if(flagDisableButton ) {
					$("#btnNodeList_DeleteNodes").addClass('disabled');
					$("#btnNodeList_DeleteNodes").removeAttr("onclick");	
				}
			} else{
				$("#btnNodeList_DeleteNodes").removeClass('disabled');
				$("#btnNodeList_DeleteNodes").attr("onclick","com.impetus.ankush.hadoopMonitoring.sendDeleteNodesRequest()");
			}
		},
*/
		// function used to get node type string
		getNodeType : function(node) {
			var nameNode = "NameNode/";
			var dataNode = "DataNode/";
			var secNode = "SecondaryNameNode/";
			var type = "";
			if (node.nameNode) {
				type = nameNode;
			}
			if (node.dataNode) {
				type = type + dataNode;
			}
			if (node.secondaryNameNode) {
				type = type + secNode;
			}
			return type.substring(0, type.length - 1);
		},

		// function used to get node type string
		getNodeTypeValue : function(nodeObject) {
			var nodeType = "";
			if (nodeObject.nameNode)
				nodeType = "NameNode";
			if (nodeObject.dataNode) {
				if (nodeType.length == 0)
					nodeType = "DataNode";
				else
					nodeType += " / DataNode";
			}
			if (nodeObject.secondaryNameNode) {
				if (nodeType.length == 0)
					nodeType = "SecondaryNameNode";
				else
					nodeType += " / SecondaryNameNode";
			}
			return nodeType;
		},

		// function used to show node addition setup details logs
		deploymentPrg_loadNodeSetupDetail : function(nodeRowId) {
			$('#NSD_nodeDetailHead').html('').text(
					clusterDetailResult.nodes[nodeRowId].publicIp);
			$('#NSD_nodePrivateIP').html('').text(
					clusterDetailResult.nodes[nodeRowId].privateIp);
			$('#NSD_nodeStatus').html('').text(
					clusterDetailResult.nodes[nodeRowId].message);
			$('#NSD_nodeType')
			.html('')
			.text(com.impetus.ankush.hadoopMonitoring.getNodeTypeValue(clusterDetailResult.nodes[nodeRowId]));
			$('#NSD_nodeOS').html('').text(clusterDetailResult.nodes[nodeRowId].os);
			com.impetus.ankush.hadoopMonitoring
			.deploymentPrg_loadNodeSetupLogs(nodeRowId);
		},

		// function used to show node addition setup logs
		deploymentPrg_loadNodeSetupLogs : function(nodeRowId) {
			if(document.getElementById("NSD_nodeDeploymentProgressDiv") == null) {
				return;
			}
			$('#NSD_nodeDeploymentProgressDiv').empty();
			for ( var i = 0; i < clusterDetailResult.nodes[nodeRowId].logs.length; i++) {
				if (clusterDetailResult.nodes[nodeRowId].logs[i].type == 'error') {
					$('#NSD_nodeDeploymentProgressDiv')
					.prepend(
							'<div style="color:red">'
							+ clusterDetailResult.nodes[nodeRowId].logs[i].longMessage
							+ '</div>');
				} else {
					$('#NSD_nodeDeploymentProgressDiv')
					.prepend(
							'<div>'
							+ clusterDetailResult.nodes[nodeRowId].logs[i].longMessage
							+ '</div>');
				}
			}
			if (clusterDetailResult.nodes[nodeRowId].nodeState == 'error') {
				$('#NSD_nodeErrorList').empty();
				$('#div_NodeErrorList').css("display", "block");
				var nodeObject = clusterDetailResult.nodes[nodeRowId];
				for ( var i = 0; i < (Object.keys(nodeObject.errors)).length; i++) {

					$('#NSD_nodeErrorList')
					.append(
							'<div class="row-fluid"><div class="span3 "><label class="text-right form-label">'
							+ (Object.keys(nodeObject.errors))[i]
							+ ':</label></div><div class="span8"><label class="text-left data" style="color: black;">'
							+ nodeObject.errors[(Object
									.keys(nodeObject.errors))[i]]
							+ '</label></div></div>');
				}
			}
			if (clusterDetailResult.state == 'deployed'
				|| clusterDetailResult.state == 'error') {
				deployFlagHadoop = true;
				IsAutoRefreshON_NodeLogs = false;
				hAddNodeProgressInterval = window.clearInterval(hAddNodeProgressInterval);
				return;
			}
			if (!deployFlagHadoop) {
				if (!IsAutoRefreshON_NodeLogs) {
					IsAutoRefreshON_NodeLogs = true;
					com.impetus.ankush.hadoopMonitoring
					.initProgressNodeSetupLogs(nodeRowId);
				}
			} else {
				hAddNodeProgressInterval = window
				.clearInterval(hAddNodeProgressInterval);
				IsAutoRefreshON_NodeLogs = false;
				return;
			}
		},

		// function used to start autorefresh call of node addition deployment progress to update progress logs after scheduled interval
		initProgressNodeSetupLogs : function(nodeRowId) {
			autoRefresh("com.impetus.ankush.hadoopMonitoring.deploymentPrg_loadNodeSetupLogs(\""
					+ nodeRowId + "\");");
		},

		// function used to load dashboard after scrolling-out from monitoring main page
		/*loadDashboardHadoop : function(i) {
			$('#deleteClusterDialogHadoopMonitor').remove();
			com.impetus.ankush.dashboard.createTile();
			com.impetus.ankush.removeChild(1);
		},*/

		// function used to initialize delete cluster cluster
		/*initDeleteClusterDialog : function() {
			$("#deleteClusterDialogHadoopMonitor").modal('show');
			$('.ui-dialog-titlebar').hide();
			$('.ui-dialog :button').blur();
		},*/

		// function used to open a delete cluster dialog
		/*deleteDialogHadoop : function() {
			com.impetus.ankush.hadoopMonitoring.initDeleteClusterDialog();
		},*/

		// function used to place a delete cluster request
		/*deleteClusterHadoop : function() {
			$('#deleteClusterDialogHadoopMonitor').modal('hide');
			$('#deleteClusterDialogHadoopMonitor').remove();
			if (currentClusterId == null) {
				com.impetus.ankush.hadoopMonitoring.loadDashboardHadoop();
				return;
			} else {
				var deleteUrl = baseUrl + '/cluster/remove/' + currentClusterId;
				$
				.ajax({
					'type' : 'DELETE',
					'url' : deleteUrl,
					'contentType' : 'application/json',
					'dataType' : 'json',
					'success' : function(result) {
						if (result.output.status) {
							if (autoRefreshCallInterval == undefined)
								autoRefreshCallInterval = setInterval(
										"com.impetus.ankush.dashboard.createTile()",
										15000);
							com.impetus.ankush.hadoopMonitoring
							.loadDashboardHadoop();
						} else
							alert(result.output.error[0]);
					},
					error : function(data) {

					}
				});
			}
		},*/

		// function used to open a hadoop cluster configuration parameters page
		configurationParameters : function() {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/configurations/parameters/'
						+ currentClusterId,
						title : 'Hadoop Parameters',
						tooltipTitle : 'Configurations',
						method : 'get',
						params : {},
					});
		},

		// function used to open a submit job page
		submitJobPage : function(clusterId) {
			$('#content-panel').sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl + '/hadoop-cluster-monitoring/submitJobs/'
						+ clusterId,
						method : 'get',
						params : {},
						title : 'Submit Job',
						tooltipTitle : hadoopClusterData.output.clusterName,
						previousCallBack : "com.impetus.ankush.hadoopMonitoring.slideOut_SubmitJob()",
						callback : function(data) {
							com.impetus.ankush.hadoopJobs.initTables_SubmitJob();
						},
					});
		},

		// function used to open a job monitoring page
		jobMonitoringPage : function(clusterId) {
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/jobMonitoring/'
						+ clusterId,
						method : 'get',
						title : "Job Monitoring",
						tooltipTitle : hadoopClusterData.output.clusterName,
						params : {},
					});
		},

		// function used to open a job-scheduler page
		jobSchedulers : function() {
			$('#content-panel').sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl + '/hadoop-cluster-monitoring/jobscheduler/'
						+ currentClusterId,
						title : "Job Scheduler",
						tooltipTitle : 'Configurations',
						method : 'get',
						params : {},
					});
		},

		// function used to show dialog during invalid add node operation
		showDialog_InvalidAddNode : function() {
			$("#invalidAddNodeOperation").modal('show');
		},
		// function specifying action to be taken on click of monitoring page cluster level tiles
		/*actionFunction_ClusterMonitoringTiles : function(name, key, data, line3) {
			if (key == '' || key == null) {
				return;
			}
			else {
				if (typeof String.prototype.startsWith != 'function') {
					// see below for better implementation!
					String.prototype.startsWith = function (str){
						return this.indexOf(str) == 0;
					};
				}
				if ((key == 'Adding Nodes|progress')
						|| (key == 'Adding Nodes|error')) {
					if (!(typeof (hAddNodeProgressInterval) === 'undefined')
							&& (hAddNodeProgressInterval != null))
						hAddNodeProgressInterval = window
						.clearInterval(hAddNodeProgressInterval);
					com.impetus.ankush.hadoopMonitoring
					.loadAddNodeProgress(currentClusterId);
				} else if (key == 'Adding Nodes|success') {
					var clusterDetailUrl = baseUrl + '/monitor/'
					+ currentClusterId + '/nodeprogress';
					com.impetus.ankush.placeAjaxCall(clusterDetailUrl, "GET", false);
					com.impetus.ankush.hadoopMonitoring.nodeDetails(currentClusterId);
				}
				else if(key == 'Node List') {
					com.impetus.ankush.hadoopMonitoring.nodeDetails(currentClusterId);
				}
				else if(key == 'Ecosystem') {
					com.impetus.ankush.hadoopMonitoring.jumpToAnchorTag('hadoopMonitoring_hadoopDetails_Jump');
				}
				else if(key == 'Job Monitoring') {
					com.impetus.ankush.hadoopMonitoring.jobMonitoringPage(currentClusterId);
				}
				else if(key.startsWith('NodeDrillDown|')) {
					var nodeId = (key.split('|'))[1];
					if(nodeId == null)
						return;
					var nodeIp = line3;
					com.impetus.ankush.hadoopMonitoring.graphOnClick(nodeId, nodeIp);
				}
			}
		},*/

		// function used to jump to a specific element on a specific click event
		/*jumpToAnchorTag : function (tagName) {
			window.location.hash = tagName;
		},

		// function used to create a cluster level tiles
		clusterTileCreate : function() {

			if (document.getElementById('div_HadoopMonitoringDetails') == null) {
				com.impetus.ankush.hadoopMonitoring
				.stopMonitoringPageAutoRefreshCalls();
				return false;
			}
			var tileUrl = baseUrl + '/monitor/' + currentClusterId + '/clustertiles';
			var tileData = com.impetus.ankush.placeAjaxCall(tileUrl, "GET", false);
			$('#allTilesCluster').empty();
			var $wrapperAll = $('#allTilesCluster'), $boxes, innerHtml, boxMaker = {}, tileClass, line1Class;
			var tileAction;
			var tiles = [], runningTiles = [], errorTiles = [], warningTiles = [];;
			var clustorTiles, tile;
			$wrapperAll.masonry({
				itemSelector : '.box'
			});
			boxMaker.makeBoxes = function(innerHtml, boxClass, tileAction) {
				var boxes = [];
				var box = document.createElement('div');
				if(tileAction.action != null) {
					box.style.cursor = "pointer";
					box.onclick = function() {
						com.impetus.ankush.hadoopMonitoring
						.actionFunction_ClusterMonitoringTiles(
								tileAction.actionName, tileAction.action,
								tileAction.data, tileAction.line3);
					};
				}
				box.className = 'box ' + boxClass;
				box.innerHTML = innerHtml;
				// add box DOM node to array of new elements
				boxes.push(box);
				return boxes;
			};
			if (!IsAutoRefreshON_clusterTile) {
				com.impetus.ankush.hadoopMonitoring.stopMonitoringPageAutoRefreshCalls();
				IsAutoRefreshON_clusterTile = true;
				com.impetus.ankush.hadoopMonitoring.initProgressClusterTile();
			}
			prependAppendTiles();
			function prependAppendTiles() {
				if (tileData.output == null) {
					return;
				}
				var clusterTilesInfo = tileData.output;
				for ( var i = 0; i < clusterTilesInfo.tiles.length; i++) {
					var d = clusterTilesInfo.tiles[i];
					switch (d.status) {
					case 'Error':
						errorTiles.push(d);
						break;
					case 'Critical':
						errorTiles.push(d);
						break;
					case 'Normal':
						runningTiles.push(d);
						break;
					case 'Warning':
						warningTiles.push(d);
						break;
					}
				}
				tiles = [runningTiles, warningTiles, errorTiles ];
				var flagAddNode_Disable = false;
				for ( var i = 0; i < tiles.length; i++) {
					if (tiles[i].length != 0) {
						clustorTiles = tiles[i];
						for ( var j = 0; j < clustorTiles.length; j++) {

							tile = clustorTiles[j];
							innerHtml = '<div class="thumbnail">';
							switch (tile.status) {
							case 'Normal':
								tileClass = 'span2 infobox';
								line1Class = 'greenTitle';
								if (tile.line1) {
									innerHtml += '<div class="' + line1Class + '">'
									+ tile.line1 + '</div>';
								}
								if (tile.line2) {
									innerHtml += '<div class="descTitle">'
										+ tile.line2 + '</div>';
								}
								var tileId = 0;
								if (tile.data != null) {
									tileId = tile.data.tileid;
								}
								tileAction = {

										'actionName' : tile.line1,
										'action' : tile.url,
										'data' : tileId,
										'line3' : tile.line3,
								};
								break;
							case 'Error':
								tileClass = 'span2 errorbox';
								line1Class = 'redTitle';
								if (tile.line1) {
									innerHtml += '<div class="'+line1Class+'">'
									+ tile.line1;
									if (tile.line2 || tile.line3) {
										innerHtml += '<br><div class="descStyle" ><span>'+tile.line2 +'</span> <span>'+ tile.line3 +'</span></div>' ;
									}
									innerHtml += '</div>';
								}
								var tileId = 0;
								if (tile.data != null) {
									tileId = tile.data.tileid;
								}
								tileAction = {
										'actionName' : tile.line1,
										'action' : tile.url,
										'data' : tileId,
										'line3' : tile.line3,
								};
								break;
							case 'Critical':
								tileClass = 'span2 errorbox';
								line1Class = 'redTitle';
								if (tile.line1) {
									innerHtml += '<div class="'+line1Class+'">'
									+ tile.line1;
									if (tile.line2 || tile.line3) {
										innerHtml += '<br><div class="descStyle" ><span>'+tile.line2 +'</span> <span>'+ tile.line3 +'</span></div>' ;
									}
									innerHtml += '</div>';
								}
								var tileId = 0;
								if (tile.data != null) {
									tileId = tile.data.tileid;
								}
								tileAction = {
										'actionName' : tile.line1,
										'action' : tile.url,
										'data' : tileId,
										'line3' : tile.line3,
								};
								break;
							case 'Warning':

								tileClass = 'span2 warningbox';
								line1Class = 'yellowTitle';
								if (tile.line1) {
									innerHtml += '<div class="'+line1Class+'">'
									+ tile.line1;
									if (tile.line2 || tile.line3) {
										innerHtml += '<br><div class="descStyle" ><span>'+tile.line2 +'</span> <span>'+ tile.line3 +'</span></div>' ;
									}
									innerHtml += '</div>';
								}
								var tileId = 0;
								if (tile.data != null) {
									tileId = tile.data.tileid;
								}
								tileAction = {
										'actionName' : tile.line1,
										'action' : tile.url,
										'data' : tileId,
										'line3' : tile.line3,
								};
								break;
							}

							innerHtml += '</div>';
							$boxes = $(boxMaker.makeBoxes(innerHtml, tileClass, tileAction));
							

							if (tile.url == "Adding Nodes|progress") {
								flagAddNode_Disable = true;
							}
						}
						if (flagAddNode_Disable) {
							//showDialog_InvalidAddNode
							$('#lbl_invalidAddNodeOperation').empty();
							$('#lbl_invalidAddNodeOperation').text('Node Addition is already in progress. A new request can be submitted once the current operation completes.');
							$('#hadoopAddNodes').attr("onclick",
							"com.impetus.ankush.hadoopMonitoring.showDialog_InvalidAddNode()");
						} else {
							if(!($('#clusterEnv').text() == 'CLOUD')) {
								$('#hadoopAddNodes').attr("onclick",
										"com.impetus.ankush.hadoopMonitoring.addNodes("
										+ currentClusterId + ")");
							}
						}
					}
				}
			}
		},

		// function used to start autorefresh function on cluster level tiles to update tile details after scheduled interval
		initProgressClusterTile : function() {
			autoRefreshTiles("com.impetus.ankush.hadoopMonitoring.clusterTileCreate();");
		},*/

		// function usde to create node level tiles
		/*nodesTileCreate : function() {
			var tileData = nodesData;
			$('#allTilesNodes').empty();
			var $wrapperAll = $('#allTilesNodes'), $boxes, innerHtml, clustorJson = "data.json", clusterTilesInfo, boxMaker = {}, itr = 1, tileClass, line1Class;
			var tileAction;
			var tiles = [], runningTiles = [], errorTiles = [], warningTiles = [];
			var clustorTiles, tile;
			$wrapperAll.masonry({
				itemSelector : '.box'
			});
			boxMaker.makeBoxes = function(innerHtml, boxClass, tileAction) {
				var boxes = [];
				var box = document.createElement('div');
				box.onclick = function() {
				};
				box.className = 'box ' + boxClass;
				box.innerHTML = innerHtml;
				// add box DOM node to array of new elements
				boxes.push(box);
				return boxes;
			};
			prependAppendTiles();
			function prependAppendTiles() {
				if (tileData.output == null)
					return;
				clusterTilesInfo = tileData.output;
				for ( var i = 0; i < clusterTilesInfo.tiles.length; i++) {
					var d = clusterTilesInfo.tiles[i];
					switch (d.status) {
					case 'Error':
						errorTiles.push(d);
						break;
					case 'Critical':
						errorTiles.push(d);
						break;
					case 'Warning':
						warningTiles.push(d);
						break;
					case 'Normal':
						runningTiles.push(d);
						break;
					}
				}
				tiles = [ runningTiles, warningTiles,errorTiles ];

				for ( var i = 0; i < tiles.length; i++) {
					if (tiles[i].length != 0) {
						clustorTiles = tiles[i];
						for ( var j = 0; j < clustorTiles.length; j++) {
							tile = clustorTiles[j];
							innerHtml = '<div class="thumbnail">';
							switch (tile.status) {
							case 'Normal':
								tileClass = 'span2 infobox';
								line1Class = 'greenTitle';
								if (tile.line1) {
									innerHtml += '<div class="' + line1Class + '">'
									+ tile.line1 + '</div>';
								}
								if (tile.line2) {
									innerHtml += '<div class="descTitle">'
										+ tile.line2 + '</div>';
								}
								tileAction = {
										'actionName' : tile.line1,
										'action' : tile.url
								}
								break;
							case 'Error':
								tileClass = 'span2 errorbox';
								line1Class = 'redTitle';
								if (tile.line1) {
									innerHtml += '<div class="'+line1Class+'">'
									+ tile.line1;
									if (tile.line2 || tile.line3) {
										innerHtml += '<br><div class="descStyle" ><span>'+tile.line2 +'</span> <span>'+ tile.line3 +'</span></div>' ;
									}
									innerHtml += '</div>';
								}
								tileAction = {
										'actionName' : tile.line1,
										'action' : tile.url
								}
								break;
							}
							innerHtml += '</div>';
							$boxes = $(boxMaker.makeBoxes(innerHtml, tileClass, tileAction));
							
						}
					}
				}
			}
		},

		// function used to stop monitoring page autorefreah calls
		stopMonitoringPageAutoRefreshCalls : function() {
			IsAutoRefreshON_clusterTile = false;
			IsAutoRefreshON_heatMap = false;
			hClusterTilesView = window.clearInterval(hClusterTilesView);
			if (!(typeof (hClusterMapView) === 'undefined'))
				hClusterMapView = window.clearInterval(hClusterMapView);
		},

		// function used to call cluster level tile create function to create cluster level tiles 
		monitoringTiles : function(data) {
			com.impetus.ankush.hadoopMonitoring.clusterTileCreate();
		},

		// function specifying on click event of heat map graphs
		graphOnClick : function(nodeId, nodeIp) {
			if ((!(typeof (refreshInterval_NDD) === 'undefined'))
					&& (refreshInterval_NDD != null)) {
				refreshInterval_NDD = window.clearInterval(refreshInterval_NDD);
			}
			$('#content-panel')
			.sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl
						+ '/hadoop-cluster-monitoring/nodeDrillDown',
						method : 'get',
						previousCallBack : "com.impetus.ankush.hadoopMonitoring.autorefresh_graphOnClick()",
						title : nodeIp,
						tooltipTitle : hadoopClusterData.output.clusterName,
						params : {
							nodeId : nodeId,
							nodeIp : nodeIp,
							clusterId : currentClusterId
						},
					});
		},

		utilizationTrend : function(clusterId){
			$('#content-panel').sectionSlider('addChildPanel',
					{
				current : 'login-panel',
				url : baseUrl
				+ '/hadoop-cluster-monitoring/hadoopUtilizationTrend',
				method : 'get',
				//previousCallBack : "com.impetus.ankush.hadoopMonitoring.autorefresh_graphOnClick()",
				title : hadoopClusterData.output.clusterName+'/Utilization Metrics',
				tooltipTitle : hadoopClusterData.output.clusterName,
				params : {
					clusterId : currentClusterId
				},
					});
		},
		// graph related function
		loadClusterLevelGraphs : function(startTime) {
			$('#utilizationTrendGraphs').empty();
			$('#pageHeadingUtilization').text(hadoopClusterData.output.clusterName+'/Utilization Metrics');
			// to get the json for cpu graph.
			com.impetus.ankush.hadoopMonitoring.loadGraph(startTime, 'cpu', 'cpu_*.rrd');
			// to get the json for memory graph.
			com.impetus.ankush.hadoopMonitoring.loadGraph(startTime, 'memory', 'mem_*.rrd');
			// to get the json for network graph.
			com.impetus.ankush.hadoopMonitoring.loadGraph(startTime, 'network', 'bytes_*.rrd');
			// to get the json for load graph.
			com.impetus.ankush.hadoopMonitoring.loadGraph(startTime, 'load', 'load_*.rrd proc_run.rrd');
		},
		
		loadGraph : function(startTime, key, value) {
			var graphUrl =  baseUrl + '/monitor/'+currentClusterId+'/graphs?startTime='+startTime+'&pattern=' + value;
			$("#utilizationTrendGraphs").append('<div class="span6" style="margin-top:10px;margin-left:50px;"><div class="row-fluid section-heading" style="margin-top:10px;">'+key.toUpperCase()+'</div><div class="row-fluid"><div id="graph_'+key+'" style="margin-top:10px;box-shadow:4px 4px 4px 4px;;background-color:#FFFFFF"><svg></svg></div></div></div>');
			$('#graph_'+key+' svg').css({
				'height': '300px',
				'width': '450px',
			});
			com.impetus.ankush.placeAjaxCall(graphUrl, "GET", true, null, function(result) {
				if(result.output.status) {
					var formatString = '';
					switch(startTime){
					case 'lastday':
						formatString = '%H:%M';
						break;
					case 'lastweek':
						formatString = '%a %H:%M';
						break;
					case 'lastmonth':
						formatString = '%d/%m';
						break;
					case 'lastyear':
						formatString = '%b';
						break;
					default:
						formatString = '%H:%M:%S';
					}
					var newResult = com.impetus.ankush.hadoopMonitoring.normalizeData(result.output.json);
					nv.addGraph(function() {
						var chart = nv.models.cumulativeLineChart()
							.x(function(d) { return d[0]; }) 
						    .y(function(d) { return d[1]; })
						    .clipEdge(true);
						
						chart.showControls(false);
						chart.xAxis
						    .tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); })
						    .ticks(10);
						 
						chart.yAxis.tickFormat(d3.format("f")).ticks(10);
						chart.forceY([0,10]);
						var format = null;
						if(key == 'network' || key == 'memory') {
							chart.yAxis.ticks(10).
							tickFormat(function (d) {
							    var suffix = '';
							    var decimals = com.impetus.ankush.hadoopMonitoring.countDecimals(d);
							    if(decimals < 3 ) {
							    	d = d;
							    } else if(decimals < 6 ) {
						        	d = (Math.round(d/10)/100);
						        	suffix = " kB";
						        } else if(decimals < 9 ){ 
						        	d = (Math.round(d/10000)/100);
						        	suffix = " MB";
						        } else if(decimals < 12){ 
						        	d = (Math.round(d/10000000)/100); 
						        	suffix = " GB";
						        } else { 
						        	d = (Math.round(d/10000000000)/100); 
						        	suffix = " TB";
						        }
							    d = d3.format("f")(d);
							    return d + suffix;});
						}
//						var legend = chart.legend();
						var svg = d3.select('#graph_'+key+' svg')
						    .datum(newResult)
						    .transition().duration(0).call(chart);
						nv.utils.windowResize(chart.update);
				   });
				}
				else {
					$("#graph_"+key).empty();
					$("#graph_"+key).append("<div id=error_"+key+"></div>");
					$("#error_"+key).append('<h2>Sorry, Unable to get '+key+' graph.</h2>').css({
						'text-align' : 'center',
						'height': '300px',
						'width': '450px',
					});
					$('#graphButtonGroup_JobMonitoring').css("display", "none");
				}
			});	
		},
		countDecimals : function(v) {
		    var test = v, count = 0;
		    while(test > 10) {
		        test /= 10;
		        count++;
		    }
		    return count;
		},
		// custom formatting functions:
		toTerra :function (d) { return (Math.round(d/10000000000)/100) + " T"; },
		toGiga : function(d)  { return (Math.round(d/10000000)/100) + " G"; },
		toMega :function(d)  { return (Math.round(d/10000)/100) + " M"; },
		toKilo: function(d)  { return (Math.round(d/10)/100) + " k"; },

		// create tiles on node deatail page
		// will draw graph on individual node detail page
		//normalize graph data , will be called from drawGraph_JobMonitoring function
		normalizeData: function (rrdData)
		{
			if(rrdData == null){
				return [];
			}
			var meta = rrdData.meta;
			var data = rrdData.data;
			var legends = meta.legend;
			var result = [];

			legends.forEach(function(legend, index)
					{
				result.push({key: legend, values: []});
					});
			data.forEach(function(data, dataIndex)
					{
				legends.forEach(function(legend, legendIndex)
						{
					result[legendIndex].values.push([Number(data.t) , Number(data.v[legendIndex])]);
						});		  
					});	 
			return result;
		},
*/

};
