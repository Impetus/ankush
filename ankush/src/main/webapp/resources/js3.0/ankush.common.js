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
var currentClusterId = null;
var lastLogDeploy = null;
var clusterDeploymentInterval = null;
var deploymentData = null;
var clusterDeleteInterval = null;
var deleteData = null;
var nodeStatusDeploymentInterval = null;
var nodeDeploymentInterval = null;
var nodeDeployLogData = null;
var nodeStatusData = null;
var clusterState='';
var refreshInterval = 5000;
var techMap = {
	'Cassandra' : 'hybrid',
	'Hadoop' : 'hybrid',
	'Hybrid' : 'hybrid',
};
var deploymentStatusOnHead = {
	'INPROGRESS' : 'Cluster deployment in progress',
	'DEPLOYING' : 'Cluster deployment in progress',
	'DEPLOYED' : 'Cluster deployment successful',
	'COMPLETED' : 'Cluster deployment successful',
	'ERROR' : 'Error in cluster deployment'
};
com.impetus.ankush.common = {

	
	changeCssForError : function() {
		$('.tooltip-inner').css({
			'background' : 'linear-gradient(to bottom, #e76858 0px, #e3503e 100%) repeat-x scroll 0 0 #e66454',
			'color' : '#fff',
			'border-color' : "#e3503e #e3503e #df3823",
			'-webkit-border-radius' : ' 4px',
			'-moz-border-radius' : '4px',
			'border-radius' : '4px',
		});
		$(".tooltip.top .tooltip-arrow").css({
			"bottom" : "0",
			"left" : "50%",
			"margin-left" : "-5px",
			"border-top-color" : "#e3503e",
			"border-width" : "5px 5px 0"
		});
		$(".tooltip.right .tooltip-arrow").css({
			"top" : "50%",
			"left" : "0",
			"margin-top" : "-5px",
			"border-right-color" : "#e3503e",
			"border-width" : "5px 5px 5px 0"
		});
		$(".tooltip.left .tooltip-arrow").css({
			"top" : "50%",
			"right" : "0",
			"margin-top" : "-5px",
			"border-left-color" : "#e3503e",
			"border-width" : "5px 0 5px 5px"
		});
		$(".tooltip.bottom .tooltip-arrow").css({
			"top" : "0",
			"left" : "50%",
			"margin-left" : "-5px",
			"border-bottom-color" : "#e3503e",
			"border-width" : "0 5px 5px"
		});
	},
	/* Function For Changing css tooltip to original */
	changeCssForNrml : function() {
		
		$('.tooltip-inner').css({
			'background' : 'linear-gradient(to bottom, #5fc2df 0px, #46b8da 100%) repeat-x scroll 0 0 #5bc0de',
			'border-color' : "#46b8da #46b8da #2caed5",
			'color' : 'white',
			 'width':'180px',
			 'text-align': 'left'
		});
		$(".tooltip.top .tooltip-arrow").css({
			"bottom" : "0",
			"left" : "50%",
			"margin-left" : "-5px",
			"border-top-color" : "#46b8da",
			"border-width" : "5px 5px 0"
		});
		$(".tooltip.right .tooltip-arrow").css({
			"top" : "50%",
			"left" : "0",
			"margin-top" : "-5px",
			"border-right-color" : "#46b8da",
			"border-width" : "5px 5px 5px 0"
		});
		$(".tooltip.left .tooltip-arrow").css({
			"top" : "50%",
			"right" : "0",
			"margin-top" : "-5px",
			"border-left-color" : "#46b8da",
			"border-width" : "5px 0 5px 5px"
		});
		$(".tooltip.bottom .tooltip-arrow").css({
			"top" : "0",
			"left" : "50%",
			"margin-left" : "-5px",
			"border-bottom-color" : "#46b8da",
			"border-width" : "0 5px 5px"
		});
	},
	/*
	 * Function For Changing css tooltip for a field in case of error during
	 * Hover & Focus events
	 */
	tooltipMsgChange : function(id, msg) {
		$("#" + id).addClass('error-box');
		$("#" + id).hover(function() {
			com.impetus.ankush.common.changeCssForError();
		});
		$("#" + id).focus(function() {
			com.impetus.ankush.common.changeCssForError();
		});
		$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
				'fixTitle');
	},
	/*
	 * Function for changing css of tootip for a field to original during Hover &
	 * Focus events
	 */
	tooltipOriginal : function(id, msg) {
		$("#" + id).removeClass('error-box');
		$("#" + id).hover(function() {
			com.impetus.ankush.common.changeCssForNrml();
		});
		$("#" + id).focus(function() {
			com.impetus.ankush.common.changeCssForNrml();
		});
		$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip('fixTitle');
	},
	/* Function for toggling between two div on radio button click */
	toggleAuthenticate : function(name, div1, div2) {
		if ($('input:radio[name=' + name + ']:checked').val() == 0) {
			$('#' + div2).hide();
			$('#' + div1).show();

		} else {
			$('#' + div1).hide();
			$('#' + div2).show();
		}
	},
	/* Function for toggling prepend textbox on checkbox click */
	inputPrependToggle : function(checkBox, inputBox) {
		if ($('#' + checkBox).is(':checked')) {
			$('#' + inputBox).removeAttr('disabled').css({
				'background-color' : 'white',
			});
		} else {
			$('#' + inputBox).attr('disabled', true);
		}
	},

	checkBoxToggle : function(id, div1, div2) {
		if ($('#' + id).is(':checked')) {
			$('#' + div1).removeAttr('disabled');
			$('#' + div2).attr('disabled', 'disabled');
		} else {
			$('#' + div1).attr('disabled', 'disabled');
			$('#' + div2).removeAttr('disabled');
		}
	},

	buttonClick : function(div1, div2, functionCall) {
		$('#' + div2).hide();
		$('#' + div1).show();
		if (undefined != functionCall) {
			eval(functionCall);
		}

	},
	/*
	 * Function for toggle datatable according to button group of node
	 * availability
	 */
	toggleDatatable : function(status,nodeId) {
		if (nodeStatus == null) {
			return;
		}
		if(undefined==nodeId){
			nodeId="node";
		}
		$(".nodeDiv").remove();
		$(".navigationImg").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-right");
		$('.notSelected').show();
		$('.notSelected').removeClass('notSelected');
		$('.selected').removeClass('selected');
		if (status == 'All') {
			$('.notSelected').show();
			$('.notSelected').removeClass('notSelected');
			$('.selected').removeClass('selected');
		} else if (status == 'Selected') {
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var rowId = nodeStatus.nodes[i][0].split('.').join('_');
				if ($('#nodeCheck' + i).attr('checked')) {
					$('#'+nodeId +''+ rowId).addClass('selected');
				}else
					$('#'+nodeId +''+ rowId).addClass('notSelected');
			}
			$('.notSelected').hide();
		} else if (status == 'Available') {
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var rowId = nodeStatus.nodes[i][0].split('.').join('_');
				if (!$('#'+nodeId+'' + rowId).hasClass('alert-danger')) {
					$('#'+nodeId+''+ rowId).addClass('selected');
				} else
					$('#'+nodeId+'' + rowId).addClass('notSelected');
			}
			$('.notSelected').hide();
		} else if (status == 'Error') {
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var rowId = nodeStatus.nodes[i][0].split('.').join('_');
				if ($('#'+nodeId+'' + rowId).hasClass('alert-danger')) {
					$('#'+nodeId+'' + rowId).addClass('selected');
				} else
					$('#'+nodeId+'' + rowId).addClass('notSelected');
			}
			$('#nodeCheckHead').removeAttr('checked');
			$('.notSelected').hide();
		}
	},
	/* Function for check/uncheck check boxes on click on header check box */
	checkAllNodes : function(id, nodeClass) {
		if ($('#' + id).is(':checked')) {
			$("." + nodeClass).each(function() {
				if ($(this).is(':disabled')) {
					$(this).attr('checked', false);
				} else {
					$(this).attr('checked', true);
				}
			});
		} else {
			$('.' + nodeClass).attr('checked', false);
		}
	},
	/* Function for enable disable div according to the check box */
	divChangeOnCheck : function(id, div1, div2) {
		if ($('#' + id).is(':checked')) {
			$('#' + div1).attr('disabled', false);
			$('#' + div2).attr('disabled', true);
		} else {
			$('#' + div2).attr('disabled', false);
			$('#' + div1).attr('disabled', true);
		}
	},
	/* Function for focusing particular input type on click of an error link */
	focusDiv : function(id) {
		$('#' + id).focus().tooltip('show').addClass('error-box');
	},
	/* Function for focusing particular div on click of an error link */
	focusTableDiv : function(id) {
		$("#" + id).attr("tabindex", -1).focus().css("outline","none");
	},
	/* Function for scrolling page to top on click of error button */
	focusError : function() {
		$(window).scrollTop(0);
	},
	/* Function for loading SetupDetail page */
	loadSetUpDetail : function(clusterId,clusterTechnology) {
		if (clusterDeploymentInterval != null) {
			clusterDeploymentInterval= window.clearInterval(clusterDeploymentInterval);
		}
		localStorage.removeItem("storedClusterState");
		localStorage.setItem("storedClusterState", cluster_State);
		var url=  baseUrl + '/' + techMap[clusterTechnology]+ '-cluster/'+clusterName+'/setupDetail/C-D/'+ clusterId+'/'+clusterTechnology;
		$(location).attr('href',(url));
	},
	/* Function for changing style of setup detail page */
	pageStyleChange : function() {
		$('input[type="text"], input[type="password"],input[type="email"]').attr({
					'disabled' : 'disabled',
					'placeholder' : ''
				}).css({
					"backgroundColor" : "white",
					"cursor" : "text",
					"border" : "none",
					"padding" : "0",
					"box-shadow" : "0  0 rgba(0, 0, 0, 0.075) inset",
					"padding-top" : "4px",
					"margin-top" : "4px",
					"width" : "100%"
				}).removeClass('input-medium input-mini input-xlarge');
		$('input[type="radio"], input[type="checkbox"]').attr('disabled',
				'disabled').css({
			"cursor" : "default",

		}).blur();
		$('select').attr('disabled', true).css({
			"cursor" : "default",
			"backgroundColor" : "white",
		}).blur();
		$('.input-group-addon').css({
			'background-color' : 'white',
			'border' : 'none',
		});
	},
	/* Function for opening delete cluster dialog box */
	deleteDialog : function() {
		$("#deleteClusterDialogDeploy").appendTo('body').modal('show');;
        $("#passForDelete").val('');
        $("#passForDeleteError").text('');
  	  	$("#passForDelete").removeClass('error-box');
        $('.ui-dialog-titlebar').hide();
        $('.ui-dialog :button').blur();
        $('#deleteClusterDialogDeploy').on('shown.bs.modal', function () {
				$('input:password:visible:first', this).focus();
			 });
	},

	/* Function for cluster delete */
	deleteCluster : function(id) {
		if(!com.impetus.ankush.validate.empty($("#passForDelete").val())){
	  		$("#passForDeleteError").text("Password must not be empty.").addClass('text-error');
        	$("#passForDelete").addClass('error-box');
        	return;
	  	}
	  	currentClusterId=id;
        if (currentClusterId == null || currentClusterId == "") {
           // com.impetus.ankush.removeChild(1);
            return;
        }else {
        	$("#confirmDeleteButtonDeployDialog").button('loading');
            var dataParam = {
            		"password" : $("#passForDelete").val()
            };
            var deleteUrl = baseUrl + '/cluster/removemixcluster/' + currentClusterId;
            com.impetus.ankush.placeAjaxCall(deleteUrl,'DELETE',true,dataParam,function(result){
            		$("#confirmDeleteButtonDeployDialog").button('reset');
            		if (result.output.status) {
            			$('#deleteClusterDialogDeploy').modal('hide');
            			$(location).attr('href',(baseUrl + '/dashboard'));
                    } else{
                    	$("#passForDeleteError").text(result.output.error).addClass('text-error');
                    	$("#passForDelete").addClass('error-box');
                    }
           });
        }
	},

	/* Function for populating defaul values on cluster creation page */
	getDefaultValue : function(technology, functionName) {
		var url = baseUrl + '/app/metadata/' + technology;
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			jsonResult = JSON.stringify(result);
			var functionN = 'com.impetus.ankush.' + functionName;
			if (undefined != functionName || functionName != '') {
				eval(functionN + '(' + jsonResult + ')');
			}
		});
	},

	/* Function for loading dashboard by removing child page */
	loadDashboard : function() {
		com.impetus.ankush.dashboard.createTile();
		com.impetus.ankush.removeChild(1);
	},
	/* Function for showing deployment logs */	
	deploymentProgress : function(clusterId, clusterDetailData,queryStringId) {
		if (clusterDeploymentInterval != null) {
			clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
		}
		nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		
		var url = baseUrl + '/monitor/' + clusterId + '/logs';
		//extra code for operation details log
		if((queryStringId != undefined) && (queryStringId != null)){
			url = url+'?operationId='+queryStringId;
		}
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
		if (result.output.status) {
			deploymentData = result;	
			$('#deployButton').removeAttr('disabled');
			$("#commonDeleteButton").attr('disabled', 'disabled');
			currentClusterId = clusterId;
			$("#deploymentStatusLabel").removeClass('label-success').removeClass('label-danger');
			if (deploymentData.output.operationState == com.impetus.ankush.constants.statusInProgress) {
				$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.operationState]);
			} else if (deploymentData.output.operationState == com.impetus.ankush.constants.stateError) {
				$("#deploymentStatusLabel").addClass('label-danger');
				$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.operationState]);
				$('#clusterDeleteBtn').removeClass('display-none');
			} else if (deploymentData.output.operationState === com.impetus.ankush.constants.statusCompleted) {
				//extra code for operation details log
				if((queryStringId != undefined) && (queryStringId != null)){
					com.impetus.ankush.autorefreshremover(pageLevelAutorefreshArray);
				}
				$("#deploymentStatusLabel").addClass('label-success');
				$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.operationState]);
			}
	
			lastLogDeploy = deploymentData.output.lastlog;
			for ( var i = 0; i < deploymentData.output.logs.length; i++) {
				if (deploymentData.output.logs[i].type == com.impetus.ankush.constants.typeError) {
					$('#deploymentProgressDiv').prepend('<div style="color:red">' + deploymentData.output.logs[i].longMessage + '</div>');
				}else if (deploymentData.output.logs[i].type == com.impetus.ankush.constants.typeWarn) {
					$('#deploymentProgressDiv').prepend('<div style="color:#996300">' + deploymentData.output.logs[i].longMessage + '</div>');
				} else {
					$('#deploymentProgressDiv').prepend('<div>' + deploymentData.output.logs[i].longMessage	+ '</div>');
				}
			}
			if (deploymentData.output.operationState != com.impetus.ankush.constants.statusInProgress) {
				lastData = deploymentData;
				deployFlag = true;
				clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
			} else {
				com.impetus.ankush.common.initProgressFunction("com.impetus.ankush.common.deploymentProgressRefresh();",refreshInterval);
			}
		}
		});
	},

	/*
	 * Function for initializing auto-refresh call of cluster deployment logs
	 * function
	 */
	initProgressFunction : function(functionName, timeInterval) {
		clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
		nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		clusterDeploymentInterval = setInterval(functionName, timeInterval);
	},

	/* Function to be called for auto-refresh of deployment logs */
	deploymentProgressRefresh : function() {
		/*if (document.getElementById("deploymentProgressDiv") == null) {
			clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
			return;
		}*/
		var url = baseUrl + '/monitor/' + currentClusterId + '/logs?lastlog='+ lastLogDeploy;
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
							if (result.output.status) {
								deploymentData = result;
								$("#deploymentStatusLabel").removeClass('label-success').removeClass('label-danger');
								if (deploymentData.output.operationState == com.impetus.ankush.constants.statusInProgress) {
									$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.operationState]);
								} else if (deploymentData.output.operationState == com.impetus.ankush.constants.stateError) {
									$("#deploymentStatusLabel").addClass('label-danger');
									$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.operationState]);
									$('#clusterDeleteBtn').removeClass('display-none');
									clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
								} else if (deploymentData.output.operationState == com.impetus.ankush.constants.statusCompleted) {
									$("#deploymentStatusLabel").addClass('label-success');
									$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.operationState]);
									clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
								}
								lastLogDeploy = deploymentData.output.lastlog;
								for ( var i = 0; i < deploymentData.output.logs.length; i++) {
									if (deploymentData.output.logs[i].type == com.impetus.ankush.constants.typeError) {
										$('#deploymentProgressDiv').prepend('<div style="color:red">' + deploymentData.output.logs[i].longMessage+ '</div>');
									} else if (deploymentData.output.logs[i].type == com.impetus.ankush.constants.typeWarn) {
										$('#deploymentProgressDiv').prepend('<div style="color:#996300">' + deploymentData.output.logs[i].longMessage + '</div>');
									} else {
										$('#deploymentProgressDiv').prepend('<div>' + deploymentData.output.logs[i].longMessage+ '</div>');
									}
								}
								if (deploymentData.output.operationState == com.impetus.ankush.constants.statusCompleted) {
									deployFlag = true;
									lastData = deploymentData;
									clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
								}else if (deploymentData.output.operationState != com.impetus.ankush.constants.statusInProgress) {
									clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
								}else if (clusterDeploymentInterval == null || clusterDeploymentInterval == undefined) {
									com.impetus.ankush.common.initProgressFunction("com.impetus.ankush.common.deploymentProgressRefresh();",refreshInterval);
								}
							}
						});
	},
	
	/* Function for showing delete cluster logs */
	deleteClusterProgress : function(clusterId) {
		if (clusterDeleteInterval != null) {
			clusterDeleteInterval = window.clearInterval(clusterDeleteInterval);
		}
		var url = baseUrl + '/monitor/' + clusterId + '/logs';
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
		if (result.output.status) {
			deleteData = result;	
			currentClusterId = clusterId;
			$("#deleteClusterLabel").removeClass('label-success').removeClass('label-danger');
			if (deleteData.output.state == com.impetus.ankush.constants.stateRemoving) {
				$("#deleteClusterLabel").html('').text("Cluster removal in progress");
			}
			lastLogDeploy = deleteData.output.lastlog;
			for ( var i = 0; i < deleteData.output.logs.length; i++) {
				if (deleteData.output.logs[i].type == com.impetus.ankush.constants.typeError) {
					$('#deleteClusterProgressDiv').prepend('<div style="color:red">' + deleteData.output.logs[i].longMessage + '</div>');
				} else if (deleteData.output.logs[i].type == com.impetus.ankush.constants.typeWarn) {
					$('#deleteClusterProgressDiv').prepend('<div style="color:#996300">' + deleteData.output.logs[i].longMessage + '</div>');
				} else {
					$('#deleteClusterProgressDiv').prepend('<div>' + deleteData.output.logs[i].longMessage	+ '</div>');
				}
			}
			com.impetus.ankush.common.initProgressFunction("com.impetus.ankush.common.deleteClusterProgressRefresh();",refreshInterval);
		}else{
			clusterDeleteInterval = window.clearInterval(clusterDeleteInterval);
			$("#deleteClusterLabel").removeClass('label-success').removeClass('label-danger');
			$("#deleteClusterLabel").html('').text("Cluster removed successfully");
			$("#deleteClusterLabel").addClass('label-success');
		}
		});
	},

	/*
	 * Function for initializing auto-refresh call of cluster deployment logs
	 * function
	 */
	initProgressFunction : function(functionName, timeInterval) {
		clusterDeleteInterval = window.clearInterval(clusterDeleteInterval);
		clusterDeleteInterval = setInterval(functionName, timeInterval);
	},

	/* Function to be called for auto-refresh of deployment logs */
	deleteClusterProgressRefresh : function() {
		var url = baseUrl + '/monitor/' + currentClusterId + '/logs?lastlog='+ lastLogDeploy;
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
							if (result.output.status) {
								deleteData = result;
								$("#deleteClusterLabel").removeClass('label-success').removeClass('label-danger');
								if (deleteData.output.state == com.impetus.ankush.constants.stateRemoving) {
									$("#deleteClusterLabel").html('').text("Cluster removal in progress");
								}
								lastLogDeploy = deleteData.output.lastlog;
								for ( var i = 0; i < deleteData.output.logs.length; i++) {
									if (deleteData.output.logs[i].type == com.impetus.ankush.constants.typeError) {
										$('#deleteClusterProgressDiv').prepend('<div style="color:red">' + deleteData.output.logs[i].longMessage+ '</div>');
									}else if (deleteData.output.logs[i].type == com.impetus.ankush.constants.typeWarn) {
										$('#deleteClusterProgressDiv').prepend('<div style="color:#996300">' + deleteData.output.logs[i].longMessage + '</div>');
									}else {
										$('#deleteClusterProgressDiv').prepend('<div>' + deleteData.output.logs[i].longMessage+ '</div>');
									}
								}
							}else{
								clusterDeleteInterval = window.clearInterval(clusterDeleteInterval);
								$("#deleteClusterLabel").removeClass('label-success').removeClass('label-danger');
								$("#deleteClusterLabel").html('').text("Cluster removed successfully");
								$("#deleteClusterLabel").addClass('label-success');
							}
						});
	},

/* Function for loading node drilldown page from setupdetail page */
	loadNodeStatusNode : function(hostName,clusterId) {
		currentClusterId=clusterId;
		var url= baseUrl + '/common-cluster/'+clusterName+'/nodeDeploymentLog/C-D/'+ currentClusterId+'/'+clusterTechnology+'/'+hostName;
		$(location).attr('href',(url));
	},
	/* Function for showing node level deployment logs */
	nodeLogs : function(clusterId,ipAdd) {
		currentClusterId=clusterId;
		clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
		nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
		ipAddress = ipAdd;
		var nodeUrl=baseUrl + '/monitor/' + currentClusterId + '/deployingnode?node='+ ipAddress;
		$('#nodeDetailHead1').html('').text(ipAddress);
		com.impetus.ankush.placeAjaxCall(nodeUrl,"GET",true,null,function(result) {
			nodeStatusData=result;
			for ( var key in nodeStatusData.output.node) {
				var newKey = com.impetus.ankush.camelCaseKey(key);
				var value=nodeStatusData.output.node[key];
				if(key=="roles"){
					var role=com.impetus.ankush.common.roleStringConvert(nodeStatusData.output.node.roles);
					value=role;
				}
				if (key != 'errors')
					if(value != null && !(key == 'id' || key == 'status')){
						$('#nodeDeploymentField').append(
								'<div class="row"><div class="col-md-2"><label class=" text-right form-label">'
										+ newKey
										+ ':</label></div><div class="col-md-10"><label class="form-label label-black">'
										+ value
										+ '</label></div></div>');	
					}
			}
		});
		var url = baseUrl + '/monitor/' + currentClusterId + '/logs?host='+ ipAddress;
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
							if (result.output.status) {
								nodeDeployLogData = result.output;
								for ( var i = 0; i < nodeDeployLogData.logs.length; i++) {
									if (nodeDeployLogData.logs[i].type == com.impetus.ankush.constants.typeError) {
										$('#nodeDeployProgress').prepend('<div style="color:red">'	+ nodeDeployLogData.logs[i].longMessage+ '</div>');
									}else if (nodeDeployLogData.logs[i].type == com.impetus.ankush.constants.typeWarn) {
										$('#nodeDeployProgress').prepend('<div style="color:#996300">' + nodeDeployLogData.logs[i].longMessage + '</div>');
									} else {
										$('#nodeDeployProgress').prepend('<div>'+ nodeDeployLogData.logs[i].longMessage + '</div>');
									}
								}
								if (nodeStatusData.output.state == com.impetus.ankush.constants.typeError) {
									nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
									if (Object.keys(nodeStatusData.output.nodes[node].errors).length > 0) {
										$('#nodeErrorDiv').show();
									}
									$('#errorOnNodeDiv').css("margin-top","10px");
									for ( var key in nodeStatusData.output.nodes[node].errors) {
										$('#errorOnNodeDiv').append('<label class="text-left" style="color: black;" id="'+ key+ '">'+ nodeStatusData.output.nodes[node].errors[key]+ '</label>');
									}
								}
								if (nodeDeployLogData.state == com.impetus.ankush.constants.stateDeployed|| nodeDeployLogData.state ==  com.impetus.ankush.constants.stateError) {
									nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
								} else {
									com.impetus.ankush.common.initNodeLogs();
								}
							}
						});
	},

	/*
	 * Function for initializing auto-refresh call of node deployment logs
	 * function
	 */
	initNodeLogs : function() {
		nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
		nodeDeploymentInterval = setInterval('com.impetus.ankush.common.nodeLogsRefresh();', refreshInterval);
	},
	/* Function to be called for auto-refresh of node level deployment logs */
	nodeLogsRefresh : function() {
		if (document.getElementById("nodeDeployProgress") == null) {
			nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
			return;
		}
		var url = baseUrl + '/monitor/' + currentClusterId + '/logs?host='+ ipAddress+ '&lastlog=' + nodeDeployLogData.lastlog;
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			if (result.output.status) {
				nodeDeployLogData = result.output;
				$('#nodeDetailHead1').html('').text(ipAddress);
				for ( var i = 0; i < nodeDeployLogData.logs.length; i++) {
					if (nodeDeployLogData.logs[i].type == com.impetus.ankush.constants.typeError) {
						$('#nodeDeployProgress').prepend('<div style="color:red">' + nodeDeployLogData.logs[i].longMessage + '</div>');
					} else if (nodeDeployLogData.logs[i].type == com.impetus.ankush.constants.typeWarn) {
						$('#nodeDeployProgress').prepend('<div style="color:#996300">' + nodeDeployLogData.logs[i].longMessage + '</div>');
					}else {
						$('#nodeDeployProgress').prepend(
								'<div>' + nodeDeployLogData.logs[i].longMessage + '</div>');
					}
				}
				if (nodeDeployLogData.state == com.impetus.ankush.constants.stateDeployed || nodeDeployLogData.state == com.impetus.ankush.constants.stateError) {
					nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
				}
			}

		});
	},

/* Function for loading node status page */
loadNodeStatus:function(clusterId,clusterName,clusterTechnology,clusterState){
	 /*$('#content-panel').sectionSlider('addChildPanel', {
												current : 'login-panel',
							url : baseUrl + '/common-cluster/nodeStatus',
							method : 'get',
							params : {},
							title : 'Deployment Logs',
							tooltipTitle : 'Deployment Progress',
							callback : function(data) {
								//com.impetus.ankush.common.nodeStatus(currentClusterId);
								com.impetus.ankush.common.deploymentProgress(currentClusterId,clusterDetailData);
							},
							previousCallBack : "com.impetus.ankush.common.nodeStatus("+currentClusterId+")",
							callbackData : {
								currentClusterId : currentClusterId
							}
						});*/
	var url= baseUrl + '/common-cluster/'+clusterName+'/deploymentLogs/C-D/'+clusterId+'/'+clusterTechnology;
	$(location).attr('href',(url));
	},
	roleStringConvert:function(roleArray){
		var string="";
		for (var key in roleArray){
			if(roleArray[key].length>0){
				for (var i=0;i<roleArray[key].length;i++){
					string=string + "<span class='label label-default mrgl5'>"+roleArray[key][i]+"</span>";
					}
			}
				
		}
		return string;
	},
	/* Function for populating node status tabel */
	nodeStatus : function(clusterId) {
		currentClusterId=clusterId;
	//	deploymentData = clusterDetailData;
		clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
		nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		var url = baseUrl + '/monitor/' + currentClusterId + '/deployingnodes';
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
							cluster_State=result.output.state;
							if (result.output.status) {
								nodeStatusData = result;
								if (nodeIpStatusTable != null) {
									nodeIpStatusTable.fnClearTable();
								} else
									nodeIpStatusTable = $("#nodeIpStatusTable").dataTable();
								$("#deploymentStatusNodeListLabel").removeClass('label-success').removeClass('label-danger');
								if (nodeStatusData.output.state == com.impetus.ankush.constants.stateDeploying) {
									$("#clusterDeleteBtn").attr("disabled","disabled");
									$("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
								} else if (nodeStatusData.output.state == com.impetus.ankush.constants.stateError) {
									$("#clusterDeleteBtn").removeAttr("disabled");
									$("#deploymentStatusNodeListLabel").addClass('label-danger');
									$("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
									$('#clusterDeleteBtn').removeClass('display-none');
									nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
								} else if (nodeStatusData.output.state == 'deployed') {
									$("#clusterDeleteBtn").removeAttr("disabled");
									$("#deploymentStatusNodeListLabel").addClass('label-success');
									$("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
									nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
								}
								for ( var i = 0; i < result.output.nodelist.length; i++) {
									var status = result.output.nodelist[i].params.message;
									if (status == null)
										status = '';
									var addId = null;
									var role="";
									addId = nodeIpStatusTable.fnAddData([
													result.output.nodelist[i].host,
													'<a class=""><label id="nodeIpType'
															+ i
															+ '">'
															+ role
															+ '</label></a>',
													'<a class=""><label id="nodeIpStatus'
															+ i + '">' + result.output.nodelist[i].state
															+ '</label></a>',
													'<a class=""><label id="nodeIpMessage'
															+ i + '">' + status
															+ '</label></a>',
													'<a href="#" onclick="com.impetus.ankush.common.loadNodeStatusNode(\''
															+ result.output.nodelist[i].host
															+ '\',\''+clusterId
															+ '\');" ><img id="navigationImg'
															+ i
															+ '" src="'
															+ baseUrl
															+ '/public/images/icon-chevron-right.png"/></a>' ]);
									
									var theNode = nodeIpStatusTable.fnSettings().aoData[addId[0]].nTr;
									theNode.setAttribute('id', 'nodeDeploy'+ result.output.nodelist[i].host.split('.').join('_'));
									if (Object.keys(result.output.nodelist[i].errors).length > 0) {
										rowId = result.output.nodelist[i].host.split('.').join('_');
										$('td', $('#nodeDeploy' + rowId)).addClass('alert-danger');
										//$('#nodeDeploy' + rowId).addClass('error-row');
									}
									role=com.impetus.ankush.common.roleStringConvert(result.output.nodelist[i].roles);
									var ip=result.output.nodelist[i].host.split('.').join('_');
									var rowIndex = nodeIpStatusTable.fnGetPosition( $('#nodeDeploy'+ip).closest('tr')[0] );
									var nodeData='<td class="nodeIpType" id="nodeIpType'+i+'">'+role+'</td>';
									nodeIpStatusTable.fnUpdate( nodeData, rowIndex ,1);
								}
								if (result.output.state == com.impetus.ankush.constants.stateDeployed || result.output.state == com.impetus.ankush.constants.stateError) {
									nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
								} else {
									com.impetus.ankush.common.initNodeStatus();
								}
							}
						});
	},
	/* Function for initializing auto-refresh call of node status tabel */
	initNodeStatus : function() {
		nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		nodeStatusDeploymentInterval = setInterval('com.impetus.ankush.common.nodeStatusRefresh()',refreshInterval);
	},
	/* Function to be called for auto-refresh of node status table */
	nodeStatusRefresh : function() {
		if (document.getElementById("nodeIpStatusTable") == null) {
			nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
			return;
		}
		var url = baseUrl + '/monitor/' + currentClusterId + '/deployingnodes';
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
			if (nodeIpStatusTable != null) {
				nodeIpStatusTable.fnClearTable();
			}
			cluster_State=result.output.state;
			if (result.output.status) {
				nodeStatusData = result;
				$("#deploymentStatusNodeListLabel").removeClass('label-success').removeClass('label-danger');
				if (nodeStatusData.output.state == com.impetus.ankush.constants.stateDeploying) {
					$("#clusterDeleteBtn").attr("disabled","disabled");
					$("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
				} else if (nodeStatusData.output.state == com.impetus.ankush.constants.stateError) {
					$("#clusterDeleteBtn").removeAttr("disabled");
					$("#deploymentStatusNodeListLabel").addClass('label-danger');
					$("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
					$('#clusterDeleteBtn').removeClass('display-none');
					nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
				} else if (nodeStatusData.output.state == com.impetus.ankush.constants.stateDeployed) {
					$("#clusterDeleteBtn").removeAttr("disabled");
					$("#deploymentStatusNodeListLabel").addClass('label-success');
					$("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
					nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
				}
				for ( var i = 0; i < result.output.nodelist.length; i++){
					var status = result.output.nodelist[i].params.message;
					if (status == null)
						status = '';
					var addId = null;
					var role="";
					addId = nodeIpStatusTable.fnAddData([
									result.output.nodelist[i].host,
										'<a class=""><label id="nodeType'
										+ i
										+ '">'
										+ role
										+ '</label></a>',
									'<a class=""><label id="nodeIpStatus'
										+ i + '">' + result.output.nodelist[i].state
										+ '</label></a>',
									'<a class=""><label id="nodeStatus'
										+ i + '">' + status
										+ '</label></a>',
									'<a href="#" onclick="com.impetus.ankush.common.loadNodeStatusNode(\''
										+ result.output.nodelist[i].host
										+ '\',\''
										+ currentClusterId
										+ '\');" ><img id="navigationImg'
										+ i
										+ '" src="'
										+ baseUrl
										+ '/public/images/icon-chevron-right.png"/></a>' ]);
					var theNode = nodeIpStatusTable.fnSettings().aoData[addId[0]].nTr;
					theNode.setAttribute('id','nodeDeploy'
					+ nodeStatusData.output.nodelist[i].host.split('.').join('_'));
					if (Object.keys(result.output.nodelist[i].errors).length > 0) {
						rowId = result.output.nodelist[i].host.split('.').join('_');
						$('td', $('#nodeDeploy' + rowId)).addClass('alert-danger');
						//$('#nodeDeploy' + rowId).addClass('error-row');
					}
					role=com.impetus.ankush.common.roleStringConvert(result.output.nodelist[i].roles);
					var ip=result.output.nodelist[i].host.split('.').join('_');
					var rowIndex = nodeIpStatusTable.fnGetPosition( $('#nodeDeploy'+ip).closest('tr')[0] );
					var nodeData='<td class="nodeIpType" id="nodeIpType'+i+'">'+role+'</td>';
					nodeIpStatusTable.fnUpdate( nodeData, rowIndex ,1);
				}
				if (result.output.state == com.impetus.ankush.constants.stateDeployed || result.output.state == com.impetus.ankush.constants.stateError) {
					deployFlag = true;
					nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
				}
			}
		});
	},
	preDeploymentLog:function(clusterId){
		var url = baseUrl + '/monitor/' + clusterId + '/deploymentlogs';
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
		if (result.output.status) {
			deploymentData = result;	
			$("#deploymentStatusLabel").removeClass('label-success').removeClass('label-important');
			if (deploymentData.output.state == 'deploying') {
				$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);
			} else if (deploymentData.output.state == 'error') {
				$("#deploymentStatusLabel").addClass('label-important');
				$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);
				$('#clusterDeleteBtn').removeClass('display-none');
			} else if (deploymentData.output.state == 'deployed') {
				$("#deploymentStatusLabel").addClass('label-success');
				$("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);
			}
			for ( var i = 0; i < deploymentData.output.logs.length; i++) {
				if (deploymentData.output.logs[i].type == 'error') {
					$('#deploymentProgressDiv').prepend('<div style="color:red">' + deploymentData.output.logs[i].longMessage + '</div>');
				} else if (deploymentData.output.logs[i].type == com.impetus.ankush.constants.typeWarn) {
					$('#deploymentProgressDiv').prepend('<div style="color:#996300">' + deploymentData.output.logs[i].longMessage + '</div>');
				}else {
					$('#deploymentProgressDiv').prepend('<div>' + deploymentData.output.logs[i].longMessage	+ '</div>');
				}
			}
		}
		});
	}
};