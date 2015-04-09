/*******************************************************************************
*  ===========================================================
*  Ankush : Big Data Cluster Management Solution
*  ===========================================================
*  
*  (C) Copyright 2014, by Impetus Technologies
*  
*  This is free software; you can redistribute it and/or modify it under
*  the terms of the GNU Lesser General Public License (LGPL v3) as
*  published by the Free Software Foundation;
*  
*  This software is distributed in the hope that it will be useful, but
*  WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*  See the GNU Lesser General Public License for more details.
*  
*  You should have received a copy of the GNU Lesser General Public License 
*  along with this software; if not, write to the Free Software Foundation, 
* Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*******************************************************************************/
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
			data.clusterId = clusterId;
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
				com.impetus.ankush.placeAjaxCall(url, "POST", true, nodeData, function(result) {
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
			 com.impetus.ankush.placeAjaxCall(addNodeUrl, "POST", false, addNodeData, function(result) {
					if(result.output.status) {
						com.impetus.ankush.removeCurrentChild();	
					}
					else {
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content', 'error-div-cassandra', 'errorBtnCassandra');
						return;
					}
				
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
		
		columnFamilyDrillDownPopulate : function(ksIndex,cfIndex){
			var url = null;
            if(clusterTechnology == 'Hybrid'){
            	url = baseUrl + '/monitor/'+clusterId+'/columnfamilydetails?keyspace='+keyspacesData.Keyspaces[ksIndex].keyspaceName+'&columnfamily='+columnFamilyDetailData.output.ColumnFamilies[cfIndex].columnFamilyName+'&component='+hybridTechnology;
            }
            else
            	url = baseUrl + '/monitor/'+clusterId+'/columnfamilydetails?keyspace='+keyspacesData.Keyspaces[ksIndex].keyspaceName+'&columnfamily='+columnFamilyDetailData.output.ColumnFamilies[cfIndex].columnFamilyName;
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
							for(var key in result.output.ColumnFamilyDetails){
								if(typeof result.output.ColumnFamilyDetails[key] === 'object'){
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
									if(key == "columnMetadata"){
										if(result.output.ColumnFamilyDetails[key] == "")
											$("#columnMetaData").html("No Records found.");
										else
											$("#columnMetaData").html(result.output.ColumnFamilyDetails[key]);
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
	            tooltipTitle : ipForNodeDrillDown,
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
					var deleteNodeUrl = baseUrl + '/cluster/' + clusterId + '/remove';
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
			var postUrl = null;
			if(clusterTechnology === 'Hybrid'){
				postUrl = baseUrl + '/monitor/'+clusterId+'/action?component=Cassandra';
			}else{
				postUrl = baseUrl + '/monitor/'+clusterId+'/action';
			}
			var data = {};
			data.action = action;
			if(keyspace == '')
				data.ip = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp;
			data.keyspace = keyspace;
			data.columnfamily = columnfamily;
			$('#confirmationDialogs'+dialogName).modal('hide');
			 com.impetus.ankush.placeAjaxCall(postUrl, "POST", true, data, function(result) {
					$('#error-div-'+dialogName+'DrillDown').hide();
					if(result.output.status){
					
					}
					else{
						$('#confirmationDialogs'+dialogName).appendTo('body').modal('hide');
						$('#error-div-'+dialogName+'DrillDown').show();
						$('#error-div-'+dialogName+'DrillDown').html(result.output.error[0]);
					}
			});
		},
		//this will populate 
	  	nodeListPopulate : function(){
			var nodeListUrl =  baseUrl+'/monitor/'+clusterId+'/nodelist?component='+hybridTechnology;
			if(cassandraNodeListTables != null)
				cassandraNodeListTables.fnClearTable();
			 com.impetus.ankush.placeAjaxCall(nodeListUrl, "GET", false, null, function(result) {
					if(result.output.status == true){
						if((result.output.nodes == undefined) || (result.output.nodes == null))
							return;
						for(var j = 0 ; j < result.output.nodes.length ; j++){
							var nodeListUrl = '<span><a href="'+baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+result.output.nodes[j].host+'" class="fa fa-chevron-right"></a></span>'
							cassandraNodeListTables.fnAddData([
							                           	result.output.nodes[j].host,
							                           	result.output.nodes[j].roles,
							                           	nodeListUrl
							                           ]);
						}
					}else{
						cassandraNodeListTables.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						cassandraNodeListTables.fnClearTable();
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
			//com.impetus.ankush.cassandraMonitoring.keyspaceDrillDownPopulate(i);
		},
		removePageKeyspaceDrillDown : function(){
			$('#confirmationDialogsKeyspace').remove();
			//com.impetus.ankush.cassandraMonitoring.keyspacePopulate();
		}
		
};
