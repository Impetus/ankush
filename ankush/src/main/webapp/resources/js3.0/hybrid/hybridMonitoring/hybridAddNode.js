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
var hybridTechnologiesData = null;
var postDataAddnode = {};
postDataAddnode.components = {};
postDataAddnode.nodes = {};
var jsonDataHybrid={};
var  inspectNodeData = {};
var sudoFlag=true;
var isSudo=true;
com.impetus.ankush.hybridAddNode = {
		fillTechTableHybrid : function(){
			if(addNodehybridTechTable != null)
				addNodehybridTechTable.fnClearTable();
			 var url = baseUrl + '/monitor/' + clusterId + '/clustercomponents';	
			  com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
				  if(result.output.status){
				      var hybridTechData=result.output;
				      hybridTechnologiesData = result.output.components;
					  for(var key in hybridTechnologiesData){
						  var tech = key;
						  var newNodes = 0;
						  //var linkMapNodes = '<a onclick="com.impetus.ankush.hybridMonitoring.loadMapNode(\''+key+'\')" href="###" id="nodeMap-'+tech+'">Map Nodes</a>';
						  var linkMapNodes ='<button id="nodeMap-'+tech+'" class="btn btn-default nodeMap" disabled="disabled" onclick="com.impetus.ankush.hybridAddNode.loadMapNode(\''+key+'\')" style="height:25px;">Map Nodes</button>';
						  var indexZookeeper = key.indexOf("Zookeeper");
						  if((indexZookeeper !== -1) || (key === 'Ganglia'))
							  continue ;
						  var checkNodeHybridAddNode = '<input type="checkbox" class="checkedElement techCheck" onclick="com.impetus.ankush.hybridAddNode.enableMapNode(\''+tech+'\')" id="hadoopAddNodeCheckBox-'
								+ tech
								+ '" />';
						  addNodehybridTechTable.fnAddData([
						                       checkNodeHybridAddNode,
		  				                       key,
		  				                       hybridTechnologiesData[key].nodes,
		  				                       '<span id="count-nodeMap-'+tech+'" class="count-nodeMap">'+newNodes+'</span>',
		  				                       linkMapNodes ]);
						  $("#hadoopAddNodeCheckBox-"+tech).parent().parent().attr('id','techTableRow-'+tech);
		  				}  
				  }
			  });
			 
			
		},
		//this function will enable map node button
		enableMapNode : function(tech){
			com.impetus.ankush.headerCheckedOrNot('checkedElement','addNodeOnTechHybrid');
			if($('#hadoopAddNodeCheckBox-'+tech).is(':checked')){
				$('#nodeMap-'+tech).attr('disabled',false);
				if(($('#count-nodeMap-'+tech).text() == 0))
					$('#nodeMap-'+tech).addClass('btn-danger');
			}else{
				$('#nodeMap-'+tech).attr('disabled',true);
				$('#nodeMap-'+tech).removeClass('btn-danger');
			}
		},
		//this function will enable all map nodes button to map nodes
		enableAllMapNode : function(elem){
			com.impetus.ankush.checked_unchecked_all('checkedElement',elem);
			if($('#addNodeOnTechHybrid').is(':checked')){
				$('.nodeMap').each(function(){
					var id = $(this).attr('id');
					if($('#count-'+id).text() == 0)
						$('#'+id).addClass('btn-danger');
				});
				$('.techCheck').prop('checked',true);
				$('.nodeMap').attr('disabled',false);
			}else{
				$('.nodeMap').attr('disabled',true);
				$('.nodeMap').removeClass('btn-danger');
			}
		},
		// function used to show information on tooltip of node IP details
		divShowOnClickIPAddress : function(clickId) {
			$('#ipRangeHadoop').removeClass('error-box');
			$('#ipRangeHadoop').tooltip();
			com.impetus.ankush.common.tooltipOriginal(
					'ipRangeHadoop', 'Enter Host Name');

			$('#filePath_IPAddressFile').removeClass('error-box');
			$('#filePath_IPAddressFile').tooltip();
			com.impetus.ankush.common.tooltipOriginal(
					'filePath_IPAddressFile', 'Upload Host Name File');

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
						com.impetus.ankush.hybridAddNode.uploadFile(uploadUrl,
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
		getNodes : function(){
			postDataAddnode = {};
			postDataAddnode.components = {};
			postDataAddnode.nodes = {};
			$("#inspectNodeBtn").attr("disabled",true);
			com.impetus.ankush.hybridAddNode.validateRetrieveEvent();                                                  
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			$('#retrieve').text('Retrieving...');
			$('#retrieve').attr('disabled', true);
			$('#addNode').attr('disabled', true);
			$("#error-div-hadoop").css("display", "none");
			$('#errorBtnCommonAddnode').text("");
			$('#errorBtnCommonAddnode').css("display", "none");
			var url = baseUrl + "/cluster/detectNodes";
			var nodeData = {};
			nodeData.isFileUploaded = false;
			if($("#addNodeGroupBtn .active").data("value")=="Upload File"){
		//	if ($('#ipFile').attr('checked')) {
				nodeData.isFileUploaded = true;
				nodeData.nodePattern = fileIPAddress_ServerPath;
			} else {
				nodeData.nodePattern = $('#ipRangeHadoop').val();
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
				  com.impetus.ankush.placeAjaxCall(url, "POST", true, nodeData, function(result) {
						addNodeIpRole = {};
						
						com.impetus.ankush.hybridAddNode.enableOnRetrieveNode();
						$("#inspectNodeBtn").attr("disabled",false);
						nodeStatus = result.output;
						$('#retrieve').text('Retrieve');
						$('#retrieve').removeAttr('disabled');
						$('#addNode').removeAttr('disabled');
						// IP pattern invalid error message
						if (result.output.error != null) {
							com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,
									'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
							return;
						}
						addNodeHybridClusterTable.fnClearTable();
						nodeTableLength_AddNodes = result.output.nodes.length;
						nodeHadoopJSON_AddNodesCommon = result.output.nodes;
						if (nodeTableLength_AddNodes > 0) {
							
							for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
								addNodeIpRole[nodeHadoopJSON_AddNodesCommon[i][0]] = [];
								postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]] = {};
								postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].roles = {};
								var ipHadoop = 	nodeHadoopJSON_AddNodesCommon[i][0];
								var os = '<span style="" id="addNodeOS-'
									+ i
									+ '">'
									+ nodeHadoopJSON_AddNodesCommon[i][4]
								+ '</span>';
								var navigationHadoop = '<a href="#" onclick="com.impetus.ankush.hybridAddNode.pageDetailValue('
									+ i
									+ ',this);"><i id="navigationImg-'+i+'" class=" navigationImg glyphicon glyphicon-chevron-right"></i></a>';
								var nodeRight="Non-Sudo";
			    				if(nodeHadoopJSON_AddNodesCommon[i][12]){
			    					nodeRight="Sudo";
			    				}
								 var addId = addNodeHybridClusterTable
								.fnAddData([ 
								             '<input type="checkbox" class="hide inspect-node">',                        
								             ipHadoop,
								             os,
								             nodeHadoopJSON_AddNodesCommon[i][7],
								             nodeHadoopJSON_AddNodesCommon[i][8],
								             nodeHadoopJSON_AddNodesCommon[i][9]+' GB',
								             nodeHadoopJSON_AddNodesCommon[i][10]+' GB',
								             '<span class="nodeRight" id="nodeRight'+i+'">'+nodeRight+'</span>',
								             nodeHadoopJSON_AddNodesCommon[i][11],
								             navigationHadoop
								             ]);
								 var theNode = addNodeHybridClusterTable.fnSettings().aoData[addId[0]].nTr;
	                                nodeHadoopJSON_AddNodesCommon[i][0].split('.').join('_');
	                                theNode.setAttribute('id', 'node'+ ipHadoop.split('.').join('_'));
	                                sudoFlag=true;
	                				if(!isSudo){
	                					sudoFlag=false;
	                				}
	                                if (!(nodeHadoopJSON_AddNodesCommon[i][1]
	                                && nodeHadoopJSON_AddNodesCommon[i][2] && nodeHadoopJSON_AddNodesCommon[i][3]) || (sudoFlag && !nodeHadoopJSON_AddNodesCommon[i][12])) {
	                                	$('#node' + ipHadoop.split('.').join('_')+" td" )
	                                    .addClass('alert-danger');
	                                	$('#node' + ipHadoop.split('.').join('_')).addClass('alert-danger');
	                                }
	                        }
							addNodeHybridClusterTable.fnSetColumnVis(9, false);	
						}
						enabledNodes = nodeTableLength_AddNodes - errorNodeCount;
				});
			} else {
				$('#error').empty();
				$('#error').append("Please specify valid node pattern.");
			}
		},
		// function used to validate whether node Ip address is provided or not for node retrieval during add node operation
        validateRetrieveEvent : function() {
            $("#popover-content").empty();
            com.impetus.ankush.validation.errorCount = 0;
            if($("#addNodeGroupBtn .active").data("value")=="Range"){

//            if ($('#ipRange').attr('checked')){
                com.impetus.ankush.validation.validateField('required', 'ipRangeHadoop', 'Host Name', 'popover-content');
            }
            else{
                com.impetus.ankush.validation.validateField('required', 'filePath_IPAddressFile', 'Upload Host Name File', 'popover-content');
            }
        },
        enableOnRetrieveNode : function(){
			$('.count-nodeMap').text(0);
			$('.checkedElement').each(function(){
				var id = $(this).attr('id');
				id = id.split('-')[1];
				if($(this).is(":checked")){
					$('#nodeMap-'+id).addClass('btn-danger');
				}
			});
		},
		pageDetailValue:function(node,elem){
			$(".navigationImg").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-right");
			var nodeIp=$("td:nth-child(2)", $(elem).parents('tr')).text();
			var nodeIpNew=nodeIp.split('.').join('_');
			if(!$("#node_"+nodeIpNew).hasClass("open")){
				$("#navigationImg-"+node).removeClass("glyphicon-chevron-right").addClass("glyphicon-chevron-down");
				var page='/ankush/commonMonitoring/retrievedNodeDetail/'+nodeIpNew;
				$(".nodeDiv").remove();
				$("#node"+nodeIpNew).after('<tr class="nodeDiv"><td id="node_'+nodeIpNew+'" class="open" colspan="9" style="height:auto !important"></td></tr>');
				jQuery("#node_"+nodeIpNew).load(page, function() {
					$('#nodeChildPage').text(nodeIp);
					com.impetus.ankush.hybridAddNode.inspectNodeFunction();
				});
			}else{
				$(".nodeDiv").remove();
			}
		},
		inspectNodeFunction:function(){
			$('.validation-inspect-node').hide();
			validationTable = $('#validationTable').dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : true,
				"bFilter" : true,
				"bSort" : false,
				"bInfo" : false,
				"bAutoWidth" : false,
				"sPaginationType" : "full_numbers",
				"bAutoWidth" : false,
				"bRetrieve" : true,
				"oLanguage" : {
					"sEmptyTable" : 'Loading...',
				}
			});
			$("#validationTable_filter").css({
				'display' : 'none'
			});
			$('#searchvalidationTable').keyup(
					function() {
						$("#validationTable").dataTable().fnFilter(
								$(this).val());
					});
			if (Object.keys(inspectNodeData).length != 0) {
				$('.validation-inspect-node').show();
				var ip = $('#nodeChildPage').text();
				if (undefined == inspectNodeData[ip]) {
					$('.validation-inspect-node').hide();
					return;
				}
				var classInspect = {
					"Ok" : 'text-success',
					"Critical" : 'text-error',
					"Warning" : 'text-error'
				};
				if (inspectNodeData.status == true) {
					for ( var key in inspectNodeData[ip]) {
						validationTable
								.fnAddData([
										inspectNodeData[ip][key].label,
										'<span class="'+classInspect[inspectNodeData[ip][key].status]+'">'
												+ inspectNodeData[ip][key].status
												+ '</span>',
										inspectNodeData[ip][key].message ]);
					}
				} else {
					validationTable.fnSettings().oLanguage.sEmptyTable = inspectNodeData.error[0];
					validationTable.fnClearTable();
				}
			}
		},
	
		//this will populate tables for add map node
		mapNodesPopulate:function(tableVar,technology){
			var hashForTech = {
					"Hadoop" : "hadoopNewNodes",
			}
			if(eval(tableVar)!=null){
				eval(tableVar).fnClearTable();
			}
			
			if(!((nodeStatus.nodes == undefined) && (nodeStatus.nodes == null) && (nodeStatus.nodes.length == 0))){
				 for (var i = 0; i < nodeStatus.nodes.length; i++) {
		           	  var addId=null;
		                    addId = eval(tableVar)
		                    .fnAddData([
		                                    nodeStatus.nodes[i][0],
		                                    addNodeIpRole[nodeStatus.nodes[i][0]].join('/'),
		                                    '<input type="checkbox" name="" value=""  id="checkbox-'+technology+'--'
		                                    + nodeStatus.nodes[i][0].replace(/[\.]+/g,'_') 
		                                    + '" class="checkedElementMapNode" onclick="com.impetus.ankush.headerCheckedOrNot(\'checkedElementMapNode\',\'mapNodeCheckBoxAddNode\')"/>',
		                                    nodeStatus.nodes[i][4],''
		                              ]);
		                /*if(postDataAddnode[technology] != undefined){    
			                for(var checkNode = 0 ; checkNode < postDataAddnode[technology][hashForTech[technology]].length ; checkNode++){
			                	if(postDataAddnode[technology][hashForTech[technology]][checkNode].publicIp ==  nodeStatus.nodes[i][0])
			                		$("#checkbox-"+technology+'--'+nodeStatus.nodes[i][0].replace(/[\.]+/g,'_')).attr("checked",true);
			                }    
		                }*/
		                var theNode = eval(tableVar).fnSettings().aoData[addId[0]].nTr;
		                theNode.setAttribute('id', technology+'--'+ nodeStatus.nodes[i][0].split('.').join('_'));
		                var sudoFlag=false;
						if(isSudo){	
							if($("#nodeRight"+i).text()=="Sudo"){
							}else{
								sudoFlag=true;
							}
						}
		                if (nodeStatus.nodes[i][1] != true
		                        || nodeStatus.nodes[i][2] != true
		                        || nodeStatus.nodes[i][3] != true || sudoFlag) {
		                    var rowId = nodeStatus.nodes[i][0].split('.').join('_');
		                    $("#"+technology+"--"+rowId+" td").addClass('alert-danger');
		                    $('#checkbox-' + technology+'--'+rowId).attr('disabled', true);
		                }
		          }	
				 com.impetus.ankush.hybridAddNode.mapNodesPopulateNode(technology);
			}else{
				eval(tableVar).fnSettings().oLanguage.sEmptyTable = "No data Available.";
				eval(tableVar).fnClearTable();
			}
			
		},
		mapNodesPopulateNode:function(tech){
			switch (tech) {
			case "Hadoop":
				for(var key in postDataAddnode.nodes){
					var id=key.split(".").join("_");
					if(undefined != postDataAddnode.nodes[key].roles.Hadoop && postDataAddnode.nodes[key].roles.Hadoop.indexOf("DataNode") != -1){
						$("#checkbox-Hadoop--"+id).attr("checked","checked");
					}
				}
				break;
			default:
				break;
			}
		},
	
		 //this will populate delve add node page
		getDefaultValue : function() {
			jsonDataHybrid={};
				 var url = baseUrl + '/app/metadata/hybrid';
				 com.impetus.ankush.placeAjaxCall(url, "GET", false,null, function(result){
						 jsonDataHybrid.hybrid = result.output.hybrid;
					  });
		},
	getUserType:function(){
		 var url = baseUrl + '/monitor/'+clusterId+'/clusterInstallationtype';
		 com.impetus.ankush.placeAjaxCall(url, "GET", false,null, function(result){
			 if(result.output.installationType=="NONSUDO"){
				 isSudo=false;	 
			 }else{
				 isSudo=true;
			 }
			 
			  });
	},
      
		//this function will be called on add button click
        addNodeDataHybrid : function(){
        	com.impetus.ankush.validation.errorCount=0;
                $("#addNode").button('loading');
                var errorCount = 0;
                var errorflag = false;
                $('#popover-content').empty();
                var data = {};
                var flag = false;
                var url =  baseUrl+'/cluster/'+clusterId+'/addnode';
                data = postDataAddnode;
                for(var key in hybridTechnologiesData){
                        var tech = key;
                        
                          if(key == 'Zookeeper')
                                  continue;
                         if($('#hadoopAddNodeCheckBox-'+tech).is(':checked')){
                                 errorflag = true;
                                 break;
                         }
                }
                if(errorflag == false){
                	com.impetus.ankush.validation.errorCount++;
                	var error = ["Select atleast one technology and mapnodes to add node."];
                    com.impetus.ankush.validation.showAjaxCallErrors(error, 'popover-content', 'error-div-hadoop', 'errorBtnCommonAddnode');
                        $('#error-div-hadoop').show();
                        $("#addNode").button('reset');
                        return ;
                }
                for(var key in hybridTechnologiesData){
                         var tech = key;
                        
                          if(key == 'Zookeeper')
                                  continue;
                         if(!$('#hadoopAddNodeCheckBox-'+tech).is(':checked')){
                                 delete postDataAddnode[key];
                         }
                         else{
                                if(postDataAddnode.components[key] == undefined){
                                        var error = ["Map nodes for All selected technology."];
                                        com.impetus.ankush.validation.showAjaxCallErrors(error, 'popover-content', 'error-div-hadoop', 'errorBtnCommonAddnode');
                                        $("#addNode").button('reset');
                                        return;
                                }
                                
                         }
                }
                if((Object.keys(nodeStatus)).length == 0){
                	com.impetus.ankush.validation.errorCount++;
                	var error = ["Select atleast one node or retrieve node to add node."];
                    com.impetus.ankush.validation.showAjaxCallErrors(error, 'popover-content', 'error-div-hadoop', 'errorBtnCommonAddnode');
                        flag = true;
                } 
                if((Object.keys(postDataAddnode.components)).length == 0){
                	com.impetus.ankush.validation.errorCount++;
                	var error = ["Select atleast one technology and mapnodes to add node."];
                    com.impetus.ankush.validation.showAjaxCallErrors(error, 'popover-content', 'error-div-hadoop', 'errorBtnCommonAddnode');
                        flag = true;
                }
                if(flag == true){
                        $('#error-div-hadoop').show();
                        $("#addNode").button('reset');
                        return ;
                }
                  com.impetus.ankush.placeAjaxCall(url, "POST", true, data, function(result) {
                                if(result.output.status == true){
                                        $("#addNode").button('reset');
                                        url = baseUrl + '/commonMonitoring/'+clusterName+'/C-D/'+clusterId+'/'+clusterTechnology;
                                        	$(location).attr('href',(url));
                                }else{
                                        com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content', 'error-div-hadoop', 'errorBtnCommonAddnode');
                                        $("#addNode").button('reset');
                                        return;
                                }
                        });
        },
		//this function will load page for map node
		loadMapNode : function(key){
			if((Object.keys(nodeStatus)).length == 0){
				var error = ["Select atleast one node or retrieve node to mapnodes."];
				com.impetus.ankush.validation.showAjaxCallErrors(error, 'popover-content', 'error-div-hadoop', 'errorBtnCommonAddnode');
				return ;
			}
			var hash = {

					
					'Hadoop' : 'hadoop',
					'Cassandra' : 'cassandra',
				};
			var url = baseUrl+'/hybrid-monitoring/hybridAddNode/'+hash[key];
			com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(data){
				$(".newRowForData").remove();
				$("#techTableRow-"+key).closest( "tr" ).after('<tr id="newRowForData-'+key+'" class="newRowForData"><td colspan="5">'+data+'</td></tr>');
			});
		},
		nodeMappingCancel:function(tech){
			$(".newRowForData").remove();
		},
		nodeMappingApply: function(tech){
			var errorFlag = true;
			$('.checkedElementMapNode').each(function(){
				if($(this).is(':checked')){
					errorFlag = false;
					return;
				}
			});
			if(errorFlag == true){
				var error = ['Select atleast one node.'];
				com.impetus.ankush.validation.showAjaxCallErrors(error,'popover-content-mapNodeAddNode', 'error-div-mapNodeAddNode', 'validateErrorMapNodeAdd');
				return;
			}else{
				$("#popover-content-mapNodeAddNode").html('');
				$("#validateErrorMapNodeAdd").text('').hide();
			}
			switch (tech) {
			case "Hadoop":
				postDataAddnode.components.Hadoop = {};
				var nodes={};
				for ( var i = 0; i < nodeHadoopJSON_AddNodesCommon.length; i++) {
					var id=nodeHadoopJSON_AddNodesCommon[i][0].split(".").join("_");
					if($('#checkbox-Hadoop--'+id).is(":checked")){
						nodes[nodeHadoopJSON_AddNodesCommon[i][0]]={};
					}
				if(undefined==postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]]){
					postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]]={};
					postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].roles={};
				}
				postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].roles.Hadoop=new Array();
				postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].roles.Hadoop.push("DataNode");
				var url =  baseUrl+'/monitor/'+clusterId+'/hadoopversion?component=Hadoop';
				com.impetus.ankush.placeAjaxCall(url, "GET", false, null, function(result) {
					 if(result.output.status){
						 if(result.output.isHadoop2){
								postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].roles.Hadoop.push("NodeManager");
						 }else{
							 postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].roles.Hadoop.push("TaskTracker");
						 }
					 }
				});
				postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].host=nodeHadoopJSON_AddNodesCommon[i][0];
				postDataAddnode.nodes[nodeHadoopJSON_AddNodesCommon[i][0]].publicHost=nodeHadoopJSON_AddNodesCommon[i][0];
				}
				postDataAddnode.components.Hadoop.nodes=nodes;
				$("#count-nodeMap-"+tech).text(Object.keys(postDataAddnode.components.Hadoop.nodes).length);
				break;
			
			default:
				break;
			}
			$("#nodeMap-"+tech).removeClass("btn-danger");
			$(".newRowForData").remove();
		},
		inspectNodesObject : function(id){
			var data = {};
			data.nodePorts = {};
			$('.inspect-node').each(function(){
					$(this).addClass('inspect-node-ok');
					var ip = $(this).parent().next().text();
					data.nodePorts[ip] = [];
			});
			data.clusterId = clusterId;
			com.impetus.ankush.inspectNodesCall(data,id,'retrieve');
			addNodeHybridClusterTable.fnSetColumnVis(9, true);	
		},
		
		
};
