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
var addNodeCommonClusterTable = null;
var nodeIpProgressTable = null;
var nodeDeployLogDataCommon = null;
var addingNodeUrlData = null;
var nodeDeploymentIntervalCommon = null;
var refreshIntervalCommon = 5000;
var logFileNameOutputCommon = null;
var nodeHadoopJSON_AddNodesCommon = null;
var saveNodePattern = null;
var loadClusterLevelGraphsStartTime = 'lasthour';
var fileIPAddress_ServerPath = null;
var inspectNodeData = {};
var nodeTableLength_AddNodes = 0;
var monitorClusterTechnology="";
com.impetus.ankush.commonMonitoring = {
        auditTrailRows : [],
        eventRows : [],
        nodesData : null,
        errorCount_NodeDrillDown : 0,
        nodeIndexForAutoRefresh : null,
        commonHeatMaps : function(currentClusterId){
            getCommonHeatMapChart(currentClusterId,'0');
        },
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
            result.push({key: legend, values: [], yvalues : []});
        });
        data.forEach(function(data, dataIndex)
        {
          legends.forEach(function(legend, legendIndex)
          {
            result[legendIndex].values.push([Number(data.t) , Number(data.v[legendIndex])]);
            result[legendIndex].yvalues.push(Number(data.v[legendIndex]));
          });         
        });     
        return result;
      },
           //this function will take node page on heatmap individual node click
      graphOnClick : function(nodeIp) {
    	  var url = baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+nodeIp;
    	  $(location).attr('href',url);
        },
        //this will send delete request for cluster
      deleteCluster : function() {
    	  	if(!com.impetus.ankush.validate.empty($("#passForDelete").val())){
    	  		$("#passForDeleteError").text("Password must not be empty.").addClass('text-error');
            	$("#passForDelete").addClass('error-box');
            	return;
    	  	}
    	  	else if (com.impetus.ankush.commonMonitoring.clusterId == null) {
    	  		$("#passForDelete").addClass('error-box').text("Invalid ClusterId.").addClass('text-error');;
                return;
            } else {
            	$("#confirmDeleteButtonHadoopMonitor").button('loading');
                var dataParam = {
                		"password" : $("#passForDelete").val()
                };
                var deleteUrl = baseUrl + '/cluster/removemixcluster/' + com.impetus.ankush.commonMonitoring.clusterId;
	            com.impetus.ankush.placeAjaxCall(deleteUrl,'DELETE',true,dataParam,function(result){
	            		$("#confirmDeleteButtonHadoopMonitor").button('reset');
	            		if (result.output.status) {
	            			$('#deleteClusterDialogcommonMonitor').modal('hide');
	            			$(location).attr('href',(baseUrl + '/dashboard'));
	                    } else{
	                    	$("#passForDeleteError").text(result.output.error).addClass('text-error');
	                    	$("#passForDelete").addClass('error-box');
	                    }
	           });
            }
        },
        
       //this function will validate pattern
        validatePattern : function(value, pattern) {
            if(value.match(pattern) != null) {
                return true;
            }
            return false;
        },
        //this will check whether percentage lie or not
        percentageCheck : function(x, min, max) {
            x = parseInt(x);
            min = parseInt(min);
            max = parseInt(max);
            return x >= min && x <= max;
        },
        /*Function for clienT-side validations of add node*/
        validateNodes : function() {
            $('#addNodeTable').show();
            $('#validateError').hide();
            $('#errorDivMainAddNode').hide();
            $('#validateError').html('');
            $("#errorDivMainAddNode").empty();
            var errorMsg = '';
            var ipRangeAddNode = $.trim($('#ipRangeAddNode').val());
            var filePathAddNode = $('#filePathAddNode').val();
            errorCount = 0;
            var flag = false;
            if (ipRangeAddNode != '' || filePathAddNode != '') {
                com.impetus.ankush.commonMonitoring.getAllNodes();
            }else {
                if ($('input[name=nodeSearchType]:checked').val() == 0) {
                    if (!com.impetus.ankush.validation.empty($('#ipRangeAddNode').val())) {
                        // $('#ipRangeAddNode').addClass('error-box');
                        errorCount++;
                        errorMsg = 'Host Name Field Empty';
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('ipRangeAddNode', 'Host Name cannot be empty');
                        flag = true;
                        var divId = 'ipRangeAddNode';
                        $("#errorDivMainAddNode").append(
                                        "<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.oClusterMonitoring.focusDiv(\""
                                                + divId
                                                + "\")' style='color: #5682C2;'>"
                                                + errorCount
                                                + ". "
                                                + errorMsg
                                                + "</a></div>");
                    }else {
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('ipRangeAddNode', 'Enter Host Name.');
                        $('#ipRangeAddNode').removeClass('error-box');
                    }
                }
                if ($('input[name=nodeSearchType]:checked').val() == 1) {
                    if (!com.impetus.ankush.validation.empty($('#filePathAddNode').val())) {
                        // $('#filePathAddNode').addClass('error-box');
                        errorCount++;
                        errorMsg = 'File path field empty';
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('filePathAddNode', 'File path cannot be empty');
                        flag = true;
                        var divId = 'filePathAddNode';
                        $("#errorDivMainAddNode").append(
                                        "<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.oClusterMonitoring.focusDiv(\""
                                                + divId
                                                + "\")' style='color: #5682C2;'>"
                                                + errorCount
                                                + ". "
                                                + errorMsg
                                                + "</a></div>");
                    } else {
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('filePathAddNode', ' Browse file.');
                        $('#filePathAddNode').removeClass('error-box');
                    }
                }
                $('#validateError').show().html(errorCount + ' Error');
                $("#errorDivMainAddNode").show();

            }
        },
        // function used to retrieve nodes during add node operation
        getNewlyAddedNodes : function() {
        	$("#inspectNodeBtn").attr('disabled',true);
        	inspectNodeData = {};
            var typeNode = {
                    'Cassandra':"SeedNode",
                };
            var id = com.impetus.ankush.commonMonitoring.clusterId;
            com.impetus.ankush.commonMonitoring.validateRetrieveEvent();                                                 

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
            //if ($('#ipFile').attr('checked')) {
            if($("#addNodeGroupBtn .active").data("value")=="Upload File"){
                nodeData.isFileUploaded = true;
                nodeData.nodePattern = fileIPAddress_ServerPath;
            } else {
                nodeData.nodePattern = $('#ipRangeHadoop').val();
                saveNodePattern = $('#ipRangeHadoop').val();
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
                        addNodeCommonClusterTable.fnClearTable();
                        nodeTableLength_AddNodes = result.output.nodes.length;
                        nodeHadoopJSON_AddNodesCommon = result.output.nodes;
                        if (nodeTableLength_AddNodes > 0) {

                            for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
                                var checkNodeHadoop = '<input type="checkbox" class="inspect-node checkBoxAddNode" onclick="com.impetus.ankush.headerCheckedOrNot(\'checkBoxAddNode\',\'addNodeCheckHead\')" id="hadoopAddNodeCheckBox-'
                                    + i
                                    + '" />';
                                var ipHadoop = nodeHadoopJSON_AddNodesCommon[i][0];
                                var os = '<span style="font-weight:bold;" id="addNodeOS-'
                                    + i
                                    + '">'
                                    + nodeHadoopJSON_AddNodesCommon[i][4]
                                + '</span>';
                                var navigationHadoop = '<a href="#"><img id="navigationImgAddHadoop'
                                    + i
                                    + '" src="'
                                    + baseUrl
                                    + '/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.commonMonitoring.loadNodeDetailCommon('
                                    + i + ');"/></a>';
                                var addId = addNodeCommonClusterTable
                                .fnAddData([
                                             checkNodeHadoop,
                                             ipHadoop,
                                             typeNode[com.impetus.ankush.commonMonitoring.clusterTechnology],
                                             os,
                                             navigationHadoop
                                             ]);
                                var theNode = addNodeCommonClusterTable.fnSettings().aoData[addId[0]].nTr;
                                nodeHadoopJSON_AddNodesCommon[i][0].split('.').join('_');
                                theNode.setAttribute('id', 'addNode-' + i);
                                if (!(nodeHadoopJSON_AddNodesCommon[i][1]
                                && nodeHadoopJSON_AddNodesCommon[i][2] && nodeHadoopJSON_AddNodesCommon[i][3])) {
                                    $('#hadoopAddNodeCheckBox-' + i).attr(
                                            'disabled', true);
                                    $("#addSecNameNode-" + i).attr(
                                            'disabled', true);
                                    $("#addDataNode-" + i).attr('disabled',
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
       
        // function used to add nodes to cluster
        addNodeSetup : function() {
            var hashForClass = {
                'Cassandra':"com.impetus.ankush.cassandra.CassandraClusterConf",
            };
            var clusterId = com.impetus.ankush.commonMonitoring.clusterId;
            com.impetus.ankush.commonMonitoring.validateNodeSelection();
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
            addNodeData["@class"] = hashForClass[com.impetus.ankush.commonMonitoring.clusterTechnology];
            addNodeData.newNodes = new Array();
            if($("#addNodeGroupBtn .active").data("value")=="Range")
            //if ($('#ipRange').attr('checked'))
                addNodeData.ipPattern = $('#ipRangeHadoop').val();
            else
                addNodeData.patternFile = fileIPAddress_ServerPath;
            var numberOfRetrievedNodes = $('#addNodeIpTableHadoop tr').length - 1;
            if (numberOfRetrievedNodes > 0) {
                for ( var i = 0; i < numberOfRetrievedNodes; i++) {
                    if ($('#hadoopAddNodeCheckBox-' + i).is(':checked')) {
                        node = {
                                "publicIp" : nodeHadoopJSON_AddNodesCommon[i][0],
                                "privateIp" : nodeHadoopJSON_AddNodesCommon[i][0],
                                "os" : $("#addNodeOS-" + i).text(),
                                "nodeState" : "adding"
                        };
                        addNodeData.newNodes.push(node);
                    }
                }
            }
            var addNodeUrl = baseUrl + "/cluster/" + clusterId + "/add/";
            com.impetus.ankush.placeAjaxCall(addNodeUrl, "POST", true, addNodeData, function(result) {
                    if(result.output.status) {
                        com.impetus.ankush.removeChild(2);   
                    }
                    else {
                        com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content-commonAddNode', 'error-div-commonAddNode', 'errorBtnCommonAddNode');
                        return;
                    }
                
            });
        },
        // function used to validate whether a node is available for adding to cluster
        validateNodeSelection : function() {
            var flag_NodeCount = false;
            com.impetus.ankush.validation.errorCount = 0;
            $("#popover-content-commonAddNode").empty();
            if (nodeTableLength_AddNodes == null || nodeTableLength_AddNodes == 0) {
                errorMsg = 'Select at-least one node or retrieve node.';
                com.impetus.ankush.validation.showAjaxCallErrors([errorMsg], 'popover-content-commonAddNode', 'error-div-commonAddNode', 'errorBtnCommonAddNode');
            } else {
                for ( var i = 0; i < nodeTableLength_AddNodes; i++) {

                    if ($("#hadoopAddNodeCheckBox-" + i).attr("checked")) {
                        flag_NodeCount = true;
                        break;
                    }
                }
                if (!flag_NodeCount) {
                    errorMsg = 'Select at-least one node or retrieve node.';
                    com.impetus.ankush.validation.showAjaxCallErrors([errorMsg], 'popover-content-commonAddNode', 'error-div-commonAddNode', 'errorBtnCommonAddNode');
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
        // function used to validate whether a log file can be dowloaded or not
        validateLogs : function(urlData,errorType) {
            var focusDivId = null;
            if(errorType == "nodeDown"){
                focusDivId = "nodeIP";
            }else if(errorType == "filename"){
                focusDivId = "fileName";
            }
            $("#popover-content-hadoopLogs").empty();
            $("#error-div-hadoopLogs").css("display", "none");
            $('#errorBtnHadoopLogs').text("");
            $('#errorBtnHadoopLogs').css("display", "none");
            com.impetus.ankush.validation.errorCount = 0;
            var i=0;
            $.each( urlData.output.error,
                    function(index,value){
                i = index + 1;
                com.impetus.ankush.validation.addNewErrorToDiv(focusDivId,'popover-content-hadoopLogs',value,null);
            });
            if(com.impetus.ankush.validation.errorCount > 0) {
                com.impetus.ankush.validation.showErrorDiv('error-div-hadoopLogs', 'errorBtnHadoopLogs');
                $('#viewLogs').attr('disabled',true);
				$('#downloadLogs').attr('disabled',true);
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
        //this function will open confirmation dialog for restart cluster
        startStopClusterDialog : function(event){
        	$("#passForStartStopError").empty();
    		$("#passForStartStop").val("");
        	$("#confirmStartStopCluster").appendTo('body').modal('show');
            $('.ui-dialog-titlebar').hide();
            $('.ui-dialog :button').blur();
            var headingButton = event;
			headingButton = headingButton.replace( /([A-Z])/g, " $1" );
			headingButton = headingButton.charAt(0).toUpperCase()+headingButton.slice(1);
            $("#startStopButton").unbind('click');
			$("#startStopButton").text(headingButton);
			$("#startStopDialogHeading").text(headingButton);
			$("#startStopButton").bind('click',function(){
				com.impetus.ankush.commonMonitoring.startStopCluster(event);
			});
			setTimeout(function() {
            	$("#passForStartStop").focus();
            }, 500);
		},
        //Delete cluster dialog
        deleteClusterDialog : function(){
            $("#deleteClusterDialogcommonMonitor").appendTo('body').modal('show');
            $("#passForDelete").val('');
            $("#passForDeleteError").text('');
    	  	$("#passForDelete").removeClass('error-box');
            $('.ui-dialog-titlebar').hide();
            $('.ui-dialog :button').blur();
            $("#deleteDiv").empty();
            var deleted = [];
            for(var key in clusterComponentsResponseObject){
            		deleted.push(key.indexOf("Zookeeper") > -1 ? "Zookeeper" : key);
            }
            if(deleted.length != 0){
        		$("#deleteDiv").append(deleted.join(', ')+" will be deleted.");
        	}
            setTimeout(function() {
            	$("#passForDelete").focus();
            }, 500);
        },
      
       
        //this fuction will start/stop cluster
        startStopCluster : function(event){
        	var eventName = event.split('Cluster')[0];
        	var url = baseUrl+'/service/'+clusterId+'/'+eventName;
        	/*var progressState = com.impetus.ankush.commonMonitoring.startStopProgressCheck();
        	if(progressState == "InProgress"){
        		return;
        	}*/
        	var data = {
        			"password" : $("#passForStartStop").val()
        	};
        	com.impetus.ankush.placeAjaxCall(url,'POST',true,data,function(result){
        		com.impetus.ankush.createCommonTiles();
        		if(result.output.status === false){
        			$("#passForStartStopError").text(result.output.error[0]);
        		}else
        			$("#confirmStartStopCluster").modal('hide');
        	});
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
                        com.impetus.ankush.commonMonitoring.uploadFile(uploadUrl,
                                "fileBrowse_IPAddressFile", "fileIPAddress");
                    });
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
					if (!$('#addNode-' + i).hasClass('error')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('.notSelected').hide();
			} else if (status == 'Error') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if ($('#addNode-' + i).hasClass('error')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('#addNodeCheckHead').removeAttr('checked');
				$('.notSelected').hide();
			}
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
      
        
        
};


