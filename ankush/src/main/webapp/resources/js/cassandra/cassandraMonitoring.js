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

var addNodeCassandraClusterTable = null;
var nodeTableLength_AddNodes = null;
var nodeCassandraJSON_AddNodes = null;
var keyspacesData = null;
var columnFamilyDetailData = null;
var enabledNodes = 0;
var inspectNodeData = {};
com.impetus.ankush.cassandraMonitoring = {
		// function used to initialize add node table
		initTables_addNodes : function() {
			addNodeCassandraClusterTable = $("#addNodeIpTableCassandra").dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : true,
				"bInfo" : false,
				"aoColumnDefs": [
//				                 { 'bSortable': false, 'aTargets': [ 0,2,3,6 ] },
//				                 { 'sType': "ip-address", 'aTargets': [1] }
				                 ], 
			});
			$('#addNodeIpTableCassandra_filter').hide();
			$('#searchAddNodeTableCassandra').keyup(function() {
				$("#addNodeIpTableCassandra").dataTable().fnFilter($(this).val());
			});

			$("#addNodeIpTableCassandra_filter").css({
				'display' : 'none'
			});
		},
		
		// function to scroll to top of page
		scrollToTop:function(){
			$(window).scrollTop(0);
		},

		// function used to show information on tooltip of node IP details
		divShowOnClickIPAddress : function(clickId) {
			$('#ipRangeCassandra').removeClass('error-box');
			$('#ipRangeCassandra').tooltip();
			com.impetus.ankush.common.tooltipOriginal(
					'ipRangeCassandra', 'Enter IP Address Range');

			$('#filePath_IPAddressFile').removeClass('error-box');
			$('#filePath_IPAddressFile').tooltip();
			com.impetus.ankush.common.tooltipOriginal(
					'filePath_IPAddressFile', 'Upload IP Address File');

			$('#div_IPRange').attr('style', 'display:none');
			$('#div_IPFileUpload').attr('style', 'display:none;');
			$('#' + clickId).attr('style', 'display:block;');
		},

		// function used to upload a file containing nodes IP to be added to cluster
		cassandraIPAddressFileUpload : function() {
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_IPAddressFile').click();
			$('#fileBrowse_IPAddressFile').change(
					function() {
						$('#filePath_IPAddressFile').val(
								$('#fileBrowse_IPAddressFile').val());
						com.impetus.ankush.cassandraMonitoring.uploadFile(uploadUrl,
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

		// function used to check all available nodes, retrieved during node addtion
		checkAllNode : function(elem) {
			if (addNodeCassandraClusterTable.fnGetData().length != 0) {
				for ( var i = 0; i < nodeCassandraJSON_AddNodes.length; i++) {
					var val = $('input:[name=addNodeCheckHead]').is(':checked');
					if (val) {
						if (nodeCassandraJSON_AddNodes[i][1] && nodeCassandraJSON_AddNodes[i][2]
						&& nodeCassandraJSON_AddNodes[i][3]) {
							$("#cassandraAddNodeCheckBox-" + i).attr('checked',
							'checked');
							$("#addSeedNode-" + i).attr('checked', 'checked');
						}
					} else {
						$("#cassandraAddNodeCheckBox-" + i).removeAttr('checked');
						$("#addSeedNode-" + i).removeAttr('checked');
					}
				}
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
		// function used to retrieve nodes during add node operation
		getNewlyAddedNodes : function(id) {
			$("#inspectNodeBtn").attr('disabled',true);
			inspectNodeData = {};
			com.impetus.ankush.cassandraMonitoring.validateRetrieveEvent();
			
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-cassandra', 'errorBtnCassandra');
				return;
			}
			
			addNodeCassandraClusterTable.fnClearTable();
			
			$('#retrieve').text('Retrieving...');
			$('#retrieve').attr('disabled', true);
			$('#addNode').attr('disabled', true);
			
			$("#addNodeCheckHead").removeAttr("checked");
			$("#error-div-cassandra").css("display", "none");
			$('#errorBtnCassandra').text("");
			$('#errorBtnCassandra').css("display", "none");
			
			var clusterId = id.toString();
			var url = baseUrl + "/cluster/detectNodes";
			var nodeData = {};
			
			nodeData.isFileUploaded = false;
			if ($('#ipFile').attr('checked')) {
				nodeData.isFileUploaded = true;
				nodeData.nodePattern = fileIPAddress_ServerPath;
			} else {
				nodeData.nodePattern = $('#ipRangeCassandra').val();
				saveNodePattern = $('#ipRangeCassandra').val();
			}
			
			// Rack POST Data for Node Retrival
			nodeData.isRackEnabled = false;
			nodeData.filePathRackMap = "";
			
			nodeData.clusterId = clusterId;
			
			var errorNodeCount = 0;
			$('#btnAll_CN').removeClass('active');
			$('#btnSelected_CN').removeClass('active');
			$('#btnAvailable_CN').removeClass('active');
			$('#btnError_CN').removeClass('active');
			$('#btnAll_CN').addClass('active');
			
			if (($('#cassandraAddNodesIPRange').val() != '')
					|| ($('#cassandraAddNodesFilePath').val() != '')) {
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
									'popover-content', 'error-div-cassandra', 'errorBtnCassandra');
							return;
						}
						addNodeCassandraClusterTable.fnClearTable();
						nodeTableLength_AddNodes = result.output.nodes.length;
						nodeCassandraJSON_AddNodes = result.output.nodes;
						if (nodeTableLength_AddNodes > 0) {

							for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
								var rackText = '--';
								if(nodeCassandraJSON_AddNodes[i][5] != undefined) {
									rackText = nodeCassandraJSON_AddNodes[i][5];
								}
								var checkNodeCassandra = '<input type="checkbox" id="cassandraAddNodeCheckBox-'
									+ i
									+ '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.cassandraMonitoring.updateHeaderCheckBox(\'cassandraAddNodeCheckBox-'
									+ i + '\');"/>';
								var ipCassandra = nodeCassandraJSON_AddNodes[i][0];
								
								var seedNode = '<input type="checkbox" id="addSeedNode-'
									+ i
									+ '" onclick="com.impetus.ankush.cassandraMonitoring.updateSeedNodeCheckBox(\'addSeedNode-'
									+ i + '\');"/>';
								
								var rackInfo = '<span  id="rackInfo-' + i + '"/>'+rackText+'</span>';
								var os = '<span style="font-weight:bold;" id="addNodeOS-'
									+ i
									+ '">'
									+ nodeCassandraJSON_AddNodes[i][4]
								+ '</span>';
								var navigationCassandra = '<a href="#"><img id="navigationImgAddCassandra'
									+ i
									+ '" src="'
									+ baseUrl
									+ '/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.cassandraMonitoring.loadNodeDetailCassandra('
									+ i + ');"/></a>';
								var addId = addNodeCassandraClusterTable
								.fnAddData([ checkNodeCassandra,
								             ipCassandra,
								             seedNode, rackInfo, os,
								             navigationCassandra

								             ]);
								var theNode = addNodeCassandraClusterTable.fnSettings().aoData[addId[0]].nTr;
								nodeCassandraJSON_AddNodes[i][0].split('.').join('_');
								theNode.setAttribute('id', 'addNode-' + i);
								if (!(nodeCassandraJSON_AddNodes[i][1]
								&& nodeCassandraJSON_AddNodes[i][2] && nodeCassandraJSON_AddNodes[i][3])) {
									$('#cassandraAddNodeCheckBox-' + i).attr(
											'disabled', true);
									$("#addSeedNode-" + i).attr('disabled',
											true);
									$('#addNode-' + i)
									.addClass('error');
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
		
		// function used to select/ deselect node type during checking/unchecking of node
		updateHeaderCheckBox : function(id) {
			if ($("#addNodeIpTableCassandra .nodeCheckBox:checked").length == enabledNodes) {
				$("#addNodeCheckHead").prop("checked", true);
			} else {
	            $("#addNodeCheckHead").removeAttr("checked");
	        }
			
			var rowId = (id.split('-'))[1];
			if (!$('#' + id).attr('checked')) {
				$("#addNodeCheckHead").removeAttr('checked');
				$("#addSeedNode-" + rowId).removeAttr('checked');
			} else{
				$("#addSeedNode-" + rowId).attr('checked', true);
			}
		},
		
		// function used to check/uncheck node header check box during checking/unchecking of datanode type
		updateSeedNodeCheckBox : function(id) {
			var rowId = (id.split('-'))[1];
			
			if ($('#' + id).attr('checked')) {
				if (!$('#cassandraAddNodeCheckBox-' + rowId).attr('checked')) {
					$("#cassandraAddNodeCheckBox-" + rowId).attr('checked', true);
				}
			}
			if ($("#addNodeIpTableCassandra .nodeCheckBox:checked").length > (enabledNodes-1)) {
				$("#addNodeCheckHead").prop("checked", true);
			}
		},
		
		// function used to open retrieved nodes details page containing information viz. Availability, Reachability, etc.
		loadNodeDetailCassandra : function(id) {
			$('#content-panel').sectionSlider(
					'addChildPanel',
					{
						url : baseUrl + '/hadoop-cluster/hadoopNodeDetails',
						method : 'get',
						params : {},
						title : 'Node Status',
						tooltipTitle : 'Add Nodes',
						callback : function(data) {
							com.impetus.ankush.cassandraMonitoring
							.loadCassandraNodePage(data.id);
						},
						callbackData : {
							id : id
						}
					});
		},
		
		// function used to show retrieved nodes details viz. Availability, Reachability, etc.
		loadCassandraNodePage : function(id) {
			$("#nodeIp-Hadoop").text(nodeCassandraJSON_AddNodes[id][0]);
			var nodeType = "";

			if ($("#addSeedNode-" + id).attr("checked"))
				nodeType = "SeedNode";
			else
				nodeType = "NonSeedNode";

			$("#nodeType-Hadoop").text(nodeType);
			if (nodeCassandraJSON_AddNodes[id][1])
				$("#nodeStatus-Hadoop").empty().append("Available");
			else
				$("#nodeStatus-Hadoop").empty().append("Unavailable");

			if (nodeCassandraJSON_AddNodes[id][2])
				$("#nodeReachable-Hadoop").empty().append("Yes");
			else
				$("#nodeReachable-Hadoop").empty().append("No");

			if (nodeCassandraJSON_AddNodes[id][3])
				$("#nodeAuthentication-Hadoop").empty().append('Yes');
			else
				$("#nodeAuthentication-Hadoop").empty().append('No');

			if (nodeCassandraJSON_AddNodes[id][4] != "")
				$("#OSName-Hadoop").empty().append(nodeCassandraJSON_AddNodes[id][4]);
			else
				$("#OSName-Hadoop").empty().append("--");
		},
		
		// function used to add nodes to cluster
		addNodeSetup : function(clusterId) {
			com.impetus.ankush.cassandraMonitoring.validateNodeSelection();
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-cassandra', 'errorBtnCassandra');
				return;
			}
			$('#ipRangeCassandra').val($.trim($('#ipRangeCassandra').val()));
			$("#nodeCheckHead").removeAttr("checked");
			$("#error-div-cassandra").css("display", "none");
			$('#errorBtnCassandra').text("");
			$('#errorBtnCassandra').css("display", "none");
			var node = null;
			var addNodeData = {};
//			addNodeData.technology = 'Cassandra';
			addNodeData["@class"] = "com.impetus.ankush.cassandra.CassandraClusterConf";
			addNodeData.newNodes = new Array();
			if ($('#ipRange').attr('checked'))
				addNodeData.ipPattern = $('#ipRangeCassandra').val();
			else
				addNodeData.patternFile = fileIPAddress_ServerPath;
			var numberOfRetrievedNodes = $('#addNodeIpTableCassandra tr').length - 1;
			if (numberOfRetrievedNodes > 0) {
				for ( var i = 0; i < numberOfRetrievedNodes; i++) {
					if ($('#cassandraAddNodeCheckBox-' + i).is(':checked')) {
						var flag_SeedNode = false;
						if ($('#addSeedNode-' + i).is(':checked')) {
							flag_SeedNode = true;
						}
//						var nodeRackInfo = $("#rackInfo-"+i).text();
//						if(nodeRackInfo == '--') {
//							nodeRackInfo = "/default-rack";
//						}
//						var type = "";
//						if(flag_SeedNode) {
//							type = "DataNode/";
//							if(isHadoop2Cluster) {
//								type += "NodeManager/";	
//						}
//							else {
//								type += "TaskTracker/";
//							}
//						}
//						if(flag_SecNN) {
//							type += "SecondaryNameNode/";
//						}
//						type = type.substring(0, type.length - 1);
						node = {
								"publicIp" : nodeCassandraJSON_AddNodes[i][0],
								"privateIp" : nodeCassandraJSON_AddNodes[i][0],
								"seedNode" : flag_SeedNode,
								"os" : $("#addNodeOS-" + i).text(),
								"nodeState":"adding"
//								"rackInfo" : nodeRackInfo
						};
						addNodeData.newNodes.push(node);
					}
				}
				
//				addNodeData.rackFileName = "";
//				if($('#chkBxRackEnable').attr('checked')) {
//					addNodeData.rackFileName = fileRack_ServerPath;
//				}
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
						com.impetus.ankush.removeCurrentChild();	
					}
					else {
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content', 'error-div-cassandra', 'errorBtnCassandra');
						return;
					}
				},
				'error' : function() {
				},
			});
		},
		
		// function used to load cassandra cluster monitoring page
		loadMonitoringHomePage : function(clusterId) {
			com.impetus.ankush.removeChild(2);
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

					if ($("#cassandraAddNodeCheckBox-" + i).attr("checked")) {
						flag_NodeCount = true;
						break;
					}
				}
				if (!flag_NodeCount) {
					errorMsg = 'Select at-least one node';
					com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
				}
//				com.impetus.ankush.hadoopMonitoring.validateRackMappingFileUpload();
			}
		},
		
		// function used to validate whether node Ip address is provided or not for node retrieval during add node operation
		validateRetrieveEvent : function() {
			$("#popover-content").empty();
			com.impetus.ankush.validation.errorCount = 0;
			if ($('#ipRange').attr('checked')){
				com.impetus.ankush.validation.validateField('required', 'ipRangeCassandra', 'IP Range', 'popover-content');
			}
			else{
				com.impetus.ankush.validation.validateField('required', 'filePath_IPAddressFile', 'IP Address File', 'popover-content');
			}
//			com.impetus.ankush.hadoopMonitoring.validateRackMappingFileUpload();
		},
		
		// function used to display retreived nodes on the basis of filtering, whether All, Selected, Available, or error nodes is to be displayed
		toggleDatatable : function(status) {
			$('.notSelected').show();
			$('.notSelected').removeClass('notSelected');
			$('.selected').removeClass('selected');
			$('#addNodeCheckHead').removeAttr('disabled');
			if (status == 'Selected') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if ($('#cassandraAddNodeCheckBox-' + i).attr('checked')) {
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
		//this will create a table at node drilldown page
		nodeOverview : function(nodeIndex){
			var nodeOverviewUrl = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/nodeoverview?ip='+com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp;
			$.ajax({
				'type' : 'GET',
				'url' : nodeOverviewUrl,
				'contentType' : 'application/json',
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
					if(nodeOverviewTable != null)
						nodeOverviewTable.fnClearTable();
					if(result.output.status == true){
						if(result.output.nodeData == undefined){
							nodeOverviewTable.fnSettings().oLanguage.sEmptyTable = "No data Available.";
							nodeOverviewTable.fnClearTable();
							return;
						}
						for(var key in result.output.nodeData){
							var firstColumn = key;
							firstColumn = firstColumn.replace( /([A-Z])/g, " $1" );
							firstColumn = firstColumn.charAt(0).toUpperCase()+firstColumn.slice(1);
							var secondColumn = result.output.nodeData[key];
							var thirdColumn = '';
							if(typeof result.output.nodeData[key] === 'object'){
								secondColumn = '';
								for(var prop in result.output.nodeData[key]){
									var text = prop;
									text = text.replace( /([A-Z])/g, " $1" );
									text = text.charAt(0).toUpperCase()+text.slice(1);
									secondColumn += '<div style="float:left;text-wrap:suppress;" class="span4">'+text+'&nbsp;&nbsp;&nbsp;&nbsp;</div><div style="float:left" class="span4">'+result.output.nodeData[key][prop]+'</div><div style="clear:both"/>';
								}
								if(typeof result.output.nodeData[key].length === 'number'){
									continue;
								}
							}
							if(key == 'Token Count'){
								thirdColumn = '<a href="#"><img src="'
									+ baseUrl
									+ '/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.cassandraMonitoring.loadTokenList(\''+result.output.nodeData['tokens']+'\');"/></a>';
							}
							nodeOverviewTable.fnAddData([
							                            firstColumn,
							                            secondColumn,
							                            thirdColumn
							                           ]);
						}
					}else{
						nodeOverviewTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						nodeOverviewTable.fnClearTable();
					}
				},
				'error' : function(){
				}
			});
		},
		loadTokenList : function(tokenList){
			$('#content-panel').sectionSlider('addChildPanel', {
	            url : baseUrl + '/cassandraMonitoring/tokenList',
	            method : 'get',
	            title : 'Token List',
	            tooltipTitle : com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp,
	            previousCallBack : "com.impetus.ankush.commonMonitoring.removeChildPreviousNodeUtilizationTrendPageLoad();",
	            callback : function() {
	            	$('#tokenListPreTag').append(tokenList);
	            },
	            callbackData : {
	            }
	        });
		},
		keyspacePopulate : function(){
			var keyspaceUrl =  null;
			if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				keyspaceUrl = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/keyspaces?technology='+ com.impetus.ankush.commonMonitoring.hybridTechnology;
			else
				keyspaceUrl = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/keyspaces';
			$.ajax({
				'type' : 'GET',
				'url' : keyspaceUrl,
				'contentType' : 'application/json',
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
					if(keyspaceTable != null)
						keyspaceTable.fnClearTable();
					keyspacesData = result.output;
					if(result.output.status == true){
						if((result.output.Keyspaces == undefined) || (result.output.Keyspaces == null) || (result.output.Keyspaces.length == 0)){
							keyspaceTable.fnSettings().oLanguage.sEmptyTable = "No data available.";
							keyspaceTable.fnClearTable();
							return;
						}	
						for(var i = 0 ; i < result.output.Keyspaces.length ; i++){
							var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.cassandraMonitoring.keyspaceDrillDown(' + (i) + ');"><img id="navigationImgTopologyDrillDown'
							+ i
							+ '" src="'
							+ baseUrl
							+ '/public/images/icon-chevron-right.png"/></a>';
							keyspaceTable.fnAddData([
							                           	result.output.Keyspaces[i].keyspaceName,
//							                           	result.output.Keyspaces[i].replicationFactor,
							                           	result.output.Keyspaces[i].strategyOptions,
							                           	result.output.Keyspaces[i].replicationStrategy,
							                           	result.output.Keyspaces[i].cfCount,
							                           	navigationStormTopology,
							                           ]);
							com.impetus.ankush.cassandraMonitoring.tileCreateKeyspaces();	
						}
					}else{
						keyspaceTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						keyspaceTable.fnClearTable();
					}
				},
				'error' : function(){
				}
				
			});
		},
		keyspaceDrillDown : function(i){
			$('#content-panel').sectionSlider('addChildPanel', {
	            url : baseUrl + '/cassandraMonitoring/keyspaceDrillDown',
	            method : 'get',
	            title : keyspacesData.Keyspaces[i].keyspaceName,
	            tooltipTitle : 'Keyspaces',
	            previousCallBack : "com.impetus.ankush.cassandraMonitoring.removePageKeyspaceDrillDown()",
	            callback : function() {
	            	$('#keyspaceName').text(keyspacesData.Keyspaces[i].keyspaceName);
	            	com.impetus.ankush.cassandraMonitoring.keyspaceDrillDownPopulate(i);
	            },
	            params : {
					keyspace :	keyspacesData.Keyspaces[i].keyspaceName,
					index : i
					},
	            callbackData : {
	            }
	        });
		},
		keyspaceDrillDownPopulate : function(i){
			var keyspaceUrl =  null;
			if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				keyspaceUrl = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/columnfamilies?keyspace='+keyspacesData.Keyspaces[i].keyspaceName+'&technology='+ com.impetus.ankush.commonMonitoring.hybridTechnology;
			else
				keyspaceUrl = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/columnfamilies?keyspace='+keyspacesData.Keyspaces[i].keyspaceName;
			$.ajax({
				'type' : 'GET',
				'url' : keyspaceUrl,
				'contentType' : 'application/json',
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
					if(columnFamiliesTables != null)
						columnFamiliesTables.fnClearTable();
					columnFamilyDetailData = result;
					if(result.output.status == true){
						if((result.output.ColumnFamilies == undefined) || (result.output.ColumnFamilies == null)){
							columnFamiliesTables.fnSettings().oLanguage.sEmptyTable = "No data available.";
							columnFamiliesTables.fnClearTable();
							return;
						}
						for(var j = 0 ; j < result.output.ColumnFamilies.length ; j++){
							var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.cassandraMonitoring.columnFamilyDrillDown(' + (i) + ',' + (j) + ');"><img id="navigationImgTopologyDrillDown'
							+ j
							+ '" src="'
							+ baseUrl
							+ '/public/images/icon-chevron-right.png"/></a>';
							columnFamiliesTables.fnAddData([
							                           	result.output.ColumnFamilies[j].columnFamilyName,
							                           	result.output.ColumnFamilies[j].liveSSTableCount,
							                           	result.output.ColumnFamilies[j].readLatency,
							                           	result.output.ColumnFamilies[j].writelatency,
							                           	result.output.ColumnFamilies[j].pendingTasks,
							                           	navigationStormTopology
							                           ]);
						}
						com.impetus.ankush.cassandraMonitoring.tileCreateColumnFamilies()
					}else{
						columnFamiliesTables.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						columnFamiliesTables.fnClearTable();
					}
				},
				'error' : function(){
				}
				
			});
		},
		columnFamilyDrillDown : function(i,j){
			$('#content-panel').sectionSlider('addChildPanel', {
	            url : baseUrl + '/cassandraMonitoring/columnFamilyDrillDown',
	            method : 'get',
	            title : columnFamilyDetailData.output.ColumnFamilies[j].columnFamilyName,
	            tooltipTitle : keyspacesData.Keyspaces[i].keyspaceName,
	            previousCallBack : "com.impetus.ankush.cassandraMonitoring.removePageColumnfamilyDrillDown("+i+")",
	            callback : function() {
	            	com.impetus.ankush.cassandraMonitoring.columnFamilyDrillDownPopulate(i,j);
	            	$('#columnFamilyName').text(keyspacesData.Keyspaces[i].keyspaceName+'/'+columnFamilyDetailData.output.ColumnFamilies[j].columnFamilyName);
	            },
	            params : {
					keyspace :	keyspacesData.Keyspaces[i].keyspaceName,
					columnfamily : columnFamilyDetailData.output.ColumnFamilies[j].columnFamilyName
				},
	            callbackData : {
	            }
	        });
		},
		columnFamilyDrillDownPopulate : function(ksIndex,cfIndex){
			var url = null;
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
            	//url = baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/columnfamilydetails?keyspace='+keyspacesData.Keyspaces[ksIndex].keyspaceName+'&columnfamily='+columnFamilyDetailData.output.ColumnFamilies[cfIndex].columnFamilyName+'&technology='
            }
            else
            	url = baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/columnfamilydetails?keyspace='+keyspacesData.Keyspaces[ksIndex].keyspaceName+'&columnfamily='+columnFamilyDetailData.output.ColumnFamilies[cfIndex].columnFamilyName;
            com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result){
	            if(columnFamiliesDrillDownTable != null)
					columnFamiliesDrillDownTable.fnClearTable();
						if(result.output.status == true){
							if((result.output.ColumnFamilyDetails == undefined) || (result.output.ColumnFamilyDetails == null)){
								columnFamiliesDrillDownTable.fnSettings().oLanguage.sEmptyTable = "No data available.";
								columnFamiliesDrillDownTable.fnClearTable();
								columnMetaData.fnSettings().oLanguage.sEmptyTable = "No data available.";
								columnMetaData.fnClearTable();
								return;
							}
							console.log(result.output.ColumnFamilyDetails);
							for(var key in result.output.ColumnFamilyDetails){
								if(typeof result.output.ColumnFamilyDetails[key] === 'object'){
									if(key != 'columnMetadata'){
										for(var prop in result.output.ColumnFamilyDetails[key]){
											var text = key;
											text = text.replace( /([A-Z])/g, " $1" );
											text = text.charAt(0).toUpperCase()+text.slice(1);
											var property = prop;
											property = property.replace( /([A-Z])/g, " $1" );
											property = property.charAt(0).toUpperCase()+property.slice(1);
											columnFamiliesDrillDownTable.fnAddData([
											                                        text,'',
											                                        property,
											                                        result.output.ColumnFamilyDetails[key][prop]
											                                        ]);
										}
									}else{
										if(Object.keys(result.output.ColumnFamilyDetails[key]).length == 0){
											columnMetaData.fnSettings().oLanguage.sEmptyTable = "No records found";
											columnMetaData.fnClearTable();
										}
										for(var key1 in result.output.ColumnFamilyDetails[key]){
											for(var prop in result.output.ColumnFamilyDetails[key][key1]){
																columnMetaData.fnAddData([
												                                        key1,'',
												                                        prop,
												                                        result.output.ColumnFamilyDetails[key][key1][prop]
												                                        ]);
											}
										}
									}
								}
							}
						}else{
							columnFamiliesDrillDownTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							columnFamiliesDrillDownTable.fnClearTable();
							columnMetaData.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							columnMetaData.fnClearTable();
						}
            });
		},
		//this will load cassandra node parameter page
		loadNodeParameters : function(i){
			$('#content-panel').sectionSlider('addChildPanel', {
	            url : baseUrl + '/cassandraMonitoring/nodeParameters',
	            method : 'get',
	            title : 'Node Parameters',
	            tooltipTitle : '',
	            previousCallBack : "com.impetus.ankush.nodeParameters.removePage();",
	            callback : function() {
	            	com.impetus.ankush.nodeParameters.confParameterPopulate(i);
	            },
	            callbackData : {
	            }
	        });
		},
		postNodeActionServiceDialog : function(action,keyspace,columnfamily,dialogName){
			$('#'+dialogName+'-message').html(action+' operation will be done.');
			$('#confirmationDialogs'+dialogName).appendTo('body').modal('show');
			$('#confirmationDialogsOK'+dialogName).bind('click',function(){ com.impetus.ankush.cassandraMonitoring.postNodeActionService(action,keyspace,columnfamily,dialogName); });
		},
		postNodeActionService : function(action,keyspace,columnfamily,dialogName){
			$('#confirmationDialogsOK'+dialogName).unbind('click');
			action = action.replace(' ','').toLowerCase();
			if((keyspace == '') && (columnfamily == '')){
				if(action == 'decommission'){
					var deleteNodeUrl = baseUrl + '/cluster/' + com.impetus.ankush.commonMonitoring.clusterId + '/remove';
					var deleteNodePost = {};
					deleteNodePost['@class'] = "com.impetus.ankush.cassandra.CassandraClusterConf";
					deleteNodePost.newNodes = [];
					deleteNodePost.newNodes[0] = {};
					deleteNodePost.newNodes[0].publicIp = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp;
					com.impetus.ankush.placeAjaxCall(deleteNodeUrl, 'POST', true, deleteNodePost, function(result){
						$('#error-div-'+dialogName+'DrillDown').hide();
						if(result.output.status){
							$('#confirmationDialogs'+dialogName).appendTo('body').modal('hide');
						}
						else{
							$('#confirmationDialogs'+dialogName).modal('hide');
							$('#error-div-'+dialogName+'DrillDown').show();
							$('#error-div-'+dialogName+'DrillDown').html(result.output.error[0]);
						}
					});
					return;
				}
			}
			var postUrl = baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/action';
			var data = {};
			data.action = action;
			if(keyspace == '')
				data.ip = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp;
			data.keyspace = keyspace;
			data.columnfamily = columnfamily;
			$('#confirmationDialogs'+dialogName).modal('hide');
			$.ajax({
				'type' : 'POST',
				'url' : postUrl,
				'contentType' : 'application/json',
				'data' : JSON.stringify(data),
				"async" : true,
				'dataType' : 'json',
				'success' : function(result) {
					$('#error-div-'+dialogName+'DrillDown').hide();
					if(result.output.status){
					
					}
					else{
						$('#confirmationDialogs'+dialogName).appendTo('body').modal('hide');
						$('#error-div-'+dialogName+'DrillDown').show();
						$('#error-div-'+dialogName+'DrillDown').html(result.output.error[0]);
					}
				},
				'error' : function(){
				}
			});
		},
		tileCreateKeyspaces : function(){
			var clusterTiles = {};
			clusterTiles = com.impetus.ankush.tileReordring(keyspacesData.tiles);
			$('#tilesKeyspaces').empty();
			var tile = '';
			tile = document.getElementById('tilesKeyspaces');
			for(var i = 0 ; i < clusterTiles.length ; i++){
				switch (clusterTiles[i].status) {
				case 'Error':
					tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="redTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Critical':
					tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="redTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Warning':
					tile.innerHTML += '<div class="item grid-1c2text warningbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="yellowTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Normal':
					tile.innerHTML += '<div class="item grid-1c2text infobox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="greenTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle">'+clusterTiles[i].line2+'</div></div></div>';
					break;
				}
				
			}
			var tileId = '';
			tileId = 'tilesKeyspaces';
			$('.clip').tooltip();
			$('#'+tileId).masonry({ itemSelector : '.item',
				columnWidth : 100 });
		},
		tileCreateColumnFamilies : function(){
				var clusterTiles = {};
				clusterTiles = com.impetus.ankush.tileReordring(columnFamilyDetailData.output.tiles);
				$('#tilesColumnFamilies').empty();
				var tile = '';
				tile = document.getElementById('tilesColumnFamilies');
				for(var i = 0 ; i < clusterTiles.length ; i++){
					switch (clusterTiles[i].status) {
					case 'Error':
						tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
						'<div class="tile-1c2text thumbnail">'+
						'<div class="redTitle">'+
						'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
						'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
						break;
					case 'Critical':
						tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
						'<div class="tile-1c2text thumbnail">'+
						'<div class="redTitle">'+
						'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
						'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
						break;
					case 'Warning':
						tile.innerHTML += '<div class="item grid-1c2text warningbox">'+
						'<div class="tile-1c2text thumbnail">'+
						'<div class="yellowTitle">'+
						'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
						'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
						break;
					case 'Normal':
						tile.innerHTML += '<div class="item grid-1c2text infobox">'+
						'<div class="tile-1c2text thumbnail">'+
						'<div class="greenTitle">'+
						'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
						'<div class="descTitle">'+clusterTiles[i].line2+'</div></div></div>';
						break;
					}
					
				}
				var tileId = '';
				tileId = 'tilesColumnFamilies';
				$('.clip').tooltip();
				$('#'+tileId).masonry({ itemSelector : '.item',
					columnWidth : 100 });
		},
		ringTopology : function(){
				var width = 761,
				height = 500;
				var NodeColorClass ;        
				var node , diffInNodes , datacenters , totalNodes = 0;        
				var cy = 45; 
				var node = [];
				var nodeCtr = 0;
				var minCy = 45;
				var g_xaxis = 60;
                var ringUrl = baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/clusteroverview';
                $("#ring_graph").empty();
                com.impetus.ankush.placeAjaxCall(ringUrl, "GET", true, null, function(result) {
			    	if(result.output.status == true){
			    		if((result.output.datacenters == undefined) || (result.output.datacenters == null)){
			    			return;
			    		}
				    	datacenters = result.output.datacenters;
		                for(var i = 0; i < datacenters.length; i++){
		                
		                    for(var j = 0; j < datacenters[i].racks.length; j++){
		                        totalNodes += datacenters[i].racks[j].nodes.length;                        
		                        for(var k = 0; k < datacenters[i].racks[j].nodes.length; k++){                      
		                            node[nodeCtr] = datacenters[i].racks[j].nodes[k];
		                            nodeCtr++;
		                        }
		                    
		                    }
		                } 
		                diffInNodes = 360/totalNodes;
	
		                if(totalNodes > 6){
		                    cy += (totalNodes - 6) * 10;
		                    g_xaxis +=  (totalNodes - 6) * 10;
		                }
		            
		                width = cy + g_xaxis + 12;
		                height = cy + g_xaxis + 10;
		            
		                var svgContainer = d3.select("#ring_graph").append("svg")
		                .attr("width", width)
		                .attr("height", height)                             
		                .append("g")
		                .attr("transform", "translate("+g_xaxis+",10)")
		                .append("g")
		                .attr("transform", "translate(0,0)")
		                .attr("id","dataparent")
		                .append("circle")
		                .attr("cy",cy)
		                .attr("r",cy); 
		               
		                d3.select("#dataparent").selectAll(".dataCircle")
		                .data(node)
		                .enter()
		                .append("circle")
		                .attr("class","dataCircle")
		          
		                                                                  
		                .attr("r",5)
		                .attr("transform", function(d,i) {                             
		                    a = i*diffInNodes;                               
		                    return "rotate(" + [a, 0,cy] + ")"
		                })       
		                .attr("class",function(d){		                			                	
		                    switch(d.state){
		                        case  "NORMAL":
		                            NodeColorClass="green";
		                            break;
		                        case  "Moving":
		                            NodeColorClass="blue";
		                            break;
		                        case  "Leaving":
		                            NodeColorClass="purple";
		                            break;
		                        case  "Joining":
		                            NodeColorClass="black";
		                            break;
		                        case  "Down":
		                            NodeColorClass="orange";
		                            break;
		                        case  "Error":
		                            NodeColorClass="red";
		                            break;
		                    }
		                    if(d.status == "DOWN"){
		                    	NodeColorClass="red";
		                    }
		                    return "bead " +NodeColorClass;
		                })
		                .on("mouseover", function(d){  tooltip.style("display", "block");
		                tooltipInner.text("Node IP: " + d.host + " Token count: " + d.tokenCount + " Status: " + d.status )})
		                .on("mousemove", function(){console.log("top = "+d3.event.pageY);return tooltip.style("top",(d3.event.pageY-58)+"px").style("left",(d3.event.pageX-45)+"px");})
		                .on("mouseout", function(){return tooltip.style("display", "none");});                       
		            
		                                            
		                    var tooltip = d3.select("#ring_graph")
		                                .append("div")
		                                .attr("class","tooltip fade right in")
		                                .attr("id","crTooltip")
		                                .style("display","none")    ;
	
		                   var tooltipArrow = d3.select("#crTooltip")
		                                    .append("div")
		                                    .attr("class","tooltip-arrow");
	
		                   var tooltipInner = d3.select("#crTooltip")
		                                    .append("div")
		                                    .attr("class","tooltip-inner");   
			    	}
			    	else{
			    		$("#ring_graph").text(result.output.error[0]);
			    	}
			    });
		},			
		showHeatmapOrRing : function(id1, id2){
			if(id1 == 'cassandra-heat-map'){
				$("#heatmap_rgtblock").show();
			}
			else{
				$("#heatmap_rgtblock").hide();
				com.impetus.ankush.cassandraMonitoring.ringTopology();
			}
			$('#'+id1).show();
			$('#'+id2).hide();
		},			
		removePageColumnfamilyDrillDown : function(i){
			$('#error-div-KeyspaceDrilldown').hide();
			$('#confirmationDialogsColumnfamily').remove();
			com.impetus.ankush.cassandraMonitoring.keyspaceDrillDownPopulate(i);
		},
		removePageKeyspaceDrillDown : function(){
			$('#confirmationDialogsKeyspace').remove();
			com.impetus.ankush.cassandraMonitoring.keyspacePopulate();
		}
		
};
