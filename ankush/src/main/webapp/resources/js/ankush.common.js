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

var currentClusterId=null;
var lastLogDeploy=null;
var clusterDeploymentInterval=null;
var deploymentData=null;
var nodeStatusDeploymentInterval=null;
var nodeDeploymentInterval=null;
var nodeDeployLogData=null;
var nodeStatusData=null;
var refreshInterval=15000;
var techMap={
		'Cassandra' : 'cassandra',
		'Kafka' : 'kafka',
		'Oracle NoSQL Database' : 'oracle',
		'Storm' : 'storm',
		'Hadoop' : 'hadoop',
		'Hadoop2' : 'hadoop',
		'ElasticSearch' : 'elasticSearch',
};
var deploymentStatusOnHead={
        'deploying' : 'Cluster deployment in progress',
        'deployed' : 'Cluster deployment successful',
        'error' :  'Error in cluster deployment'
};
com.impetus.ankush.common={

/*Function For Changing css tooltip in case of error */
		changeCssForError : function() {
			    $('.tooltip-inner').css({
	                'background-color' : 'white',
	                'color' : 'black',
	                'border' : "1px solid #EF3024",
	                '-webkit-border-radius' : ' 4px',
	                '-moz-border-radius' : '4px',
	                'border-radius' : '4px',
	                '-webkit-box-shadow' : '3px 3px 3px 3px #888888',
	                '-moz-box-shadow' : ' 3px 3px 3px 3px #888888',
	                'box-shadow' : '3px 3px 3px 3px #888888',
	            });
	            $('.tooltip.right').css('border-right-color', '#EF3024');
	            $(".tooltip.right .tooltip-arrow").css({
	                "top" : "50%",
	                "left" : "0",
	                "margin-top" : "-5px",
	                "border-right-color" : "#EF3024",
	                "border-width" : "5px 5px 5px 0"
	            });
		},
/*Function For Changing css tooltip to original */
		changeCssForNrml : function() {
			$('.tooltip-inner').css({
	              'width': '92%',
	              'border':'none',
	              'font-family':'Arial',
	              'color': '#040404',
	              'box-shadow' : '0 0 0 0',
	              'text-align': 'center',
	              'background-color': '#92E5FA',
	            });
	            $('.tooltip-inner').css({
	            	'background-color': '#92E5FA',
	            	'color': '#040404',
	            	'font-family':'Arial',
	            });
	            $('.tooltip').css({
	            'max-width' : '200px',
	    	 	'padding' : '4px',
	        	'text-align' :'center',
	        	'text-decoration': 'none',
	       		'border': 'none' ,
	    		'box-shadow' : '0px',
	    		'background-color' :'white'
	            });
	            $('.tooltip.top .tooltip-arrow').css('border-top-color', '#92E5FA');
	            $('.tooltip.bottom .tooltip-arrow').css('border-bottom-color', '#92E5FA');
	            $('.tooltip.right .tooltip-arrow').css('border-right-color', '#92E5FA');
	            $('.tooltip.left .tooltip-arrow').css('border-left-color', '#92E5FA');
		},
/*Function For Changing css tooltip for a field in case of error during Hover & Focus events*/
	    tooltipMsgChange : function(id, msg) {
	    	$("#" + id).addClass('error-box');
	    	$("#" + id).hover(function() {
				com.impetus.ankush.common.changeCssForError();
			});
			$("#" + id).focus(function() {
				com.impetus.ankush.common.changeCssForError();
			});
	    	$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip('fixTitle');
	    },	   
/*Function for changing css of tootip for a field to original during Hover & Focus events*/
	    tooltipOriginal : function(id, msg ){
	    	$("#" + id).removeClass('error-box');
	    	$("#" + id).hover(function() {
				com.impetus.ankush.common.changeCssForNrml();
			});
			$("#" + id).focus(function() {
				com.impetus.ankush.common.changeCssForNrml();
			});
	        $("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip('fixTitle');
	    },	
/*Function for toggling between two div on radio button click*/
	    toggleAuthenticate:function(name,div1,div2){
			if($('input:radio[name='+name+']:checked').val()==0){
				$('#'+div2).hide();
				$('#'+div1).show();
				
			}else{
				$('#'+div1).hide();
				$('#'+div2).show();
			}
		},
		/*Function for toggling prepend textbox on checkbox click*/
		inputPrependToggle:function(checkBox,inputBox){
			if($('#'+checkBox).is(':checked')){
				$('#'+inputBox).removeAttr('disabled').css({
					'background-color' : 'white',
				});
			}else{
				$('#'+inputBox).attr('disabled',true);	
			}
		},
		
		
		checkBoxToggle:function(id,div1,div2){
			if($('#'+id).is(':checked')){
				$('#'+div1).removeAttr('disabled');
				$('#'+div2).attr('disabled','disabled');
			}else{
				$('#'+div1).attr('disabled','disabled');
				$('#'+div2).removeAttr('disabled');
			}
		},
		
		buttonClick:function(div1,div2,functionCall){
			$('#'+div2).hide();
			$('#'+div1).show();
			if(undefined!=functionCall){
				eval(functionCall);	
			}
			
		},
/*Function for toggle datatable according to button group of node availability*/
	    toggleDatatable : function(status){
	    	if(nodeStatus==null){
	    		return;
	    	}
	        $('.notSelected').show();
	        $('.notSelected').removeClass('notSelected');
	        $('.selected').removeClass('selected');
	        if(status == 'All'){
	            $('.notSelected').show();
	            $('.notSelected').removeClass('notSelected');
	            $('.selected').removeClass('selected');
	        }
	        else if(status == 'Selected') {
	            for ( var i = 0; i < nodeStatus.nodes.length; i++) {
	                var rowId = nodeStatus.nodes[i][0].split('.').join('_');
	                if ($('#nodeCheck' + i).attr('checked')) {
	                    $('#node' + rowId).addClass('selected');
	                } else
	                    $('#node' + rowId).addClass('notSelected');
	            }
	            $('.notSelected').hide();
	        }
	        else if(status == 'Available'){
	            for ( var i = 0; i < nodeStatus.nodes.length; i++) {
	                var rowId = nodeStatus.nodes[i][0].split('.').join('_');
	                if(!$('#node' + rowId).hasClass('error-row')){
	                    $('#node' + rowId).addClass('selected');
	                }
	                else
	                    $('#node' + rowId).addClass('notSelected');
	            }
	            $('.notSelected').hide();
	        }
	        else if(status == 'Error'){
	            for ( var i = 0; i < nodeStatus.nodes.length; i++) {
	                var rowId = nodeStatus.nodes[i][0].split('.').join('_');
	                if($('#node' + rowId).hasClass('error-row')){
	                    $('#node' + rowId).addClass('selected');
	                }
	                else
	                    $('#node' + rowId).addClass('notSelected');
	            }
	            $('#nodeCheckHead').removeAttr('checked');
	            $('.notSelected').hide();
	        }
	    },
/*Function for check/uncheck check boxes on click on header check box*/
		checkAllNodes : function(id,nodeClass) {
			if ($('#' + id).is(':checked')) {
				$("."+nodeClass).each(function() {
					if($(this).is(':disabled')){
						$(this).attr('checked', false);
					}else{
						$(this).attr('checked', true);
					}
				});
			} else{
				$('.' + nodeClass).attr('checked', false);
			}
		},
/*Function for enable disable div according to the check box*/
		divChangeOnCheck:function(id,div1,div2){
			if ($('#' + id).is(':checked')) {
				$('#' + div1).attr('disabled', false);
				$('#' + div2).attr('disabled', true);
			}else{
				$('#' + div2).attr('disabled', false);
				$('#' + div1).attr('disabled', true);
			}
		},
/*Function for focusing particular input type on click of an error link*/
    focusDiv:function(id){
        $('#'+id).focus().tooltip('show').addClass('error-box');
    },
    /*Function for focusing particular div on click of an error link*/
    focusTableDiv:function(id){
    	$("#"+id).attr("tabindex",-1).focus();
    }, 
/*Function for scrolling page to top on click of error button*/ 
    focusError:function(){
         $(window).scrollTop(0);
    },
/*Function for loading SetupDetail page*/
		loadSetUpDetail:function(technology){
			 if(clusterDeploymentInterval != null){
				 clusterDeploymentInterval= window.clearInterval(clusterDeploymentInterval);
		        }
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/'+techMap[technology]+'-cluster/setupDetail/'+ currentClusterId,
				method : 'get',
				params : {},
				title : 'Setup Detail',
				tooltipTitle : 'Deployment Progress',
				callback : function(data) {
					if(deploymentData.output.state!='error'){
						com.impetus.ankush.common.pageStyleChange();
					}
					if(technology=='Storm'){
						setTimeout(function(){com.impetus.ankush.stormSetupDetail.setupDetailValuePopulate(currentClusterId,deploymentData.output.state);},1000);
					}
					else if(technology=='Cassandra'){
						 setTimeout(function(){com.impetus.ankush.cassandraSetupDetail.setupDetailValuePopulate(currentClusterId,deploymentData.output.state);},1000);
					}else if(technology=='Kafka'){
						 setTimeout(function(){com.impetus.ankush.kafkaSetupDetail.setupDetailValuePopulate(currentClusterId,deploymentData.output.state);},1000);
					}
					else if(technology=='Oracle NoSQL Database'){
						 setTimeout(function(){com.impetus.ankush.oracleSetupDetail.setupDetailValuePopulate(currentClusterId,deploymentData.output.state);},1000);
					}
					else if(technology=='Hadoop2' || technology=='Hadoop'){
						 setTimeout(function(){
							 com.impetus.ankush.hadoopCluster.setupDetailValuePopulate(currentClusterId);},1000);
					}
					else if(technology=='ElasticSearch'){
						 setTimeout(function(){com.impetus.ankush.elasticSearchSetupDetail.setupDetailValuePopulate(currentClusterId,deploymentData.output.state);},1000);	
					}
			},
				previousCallBack : "com.impetus.ankush.common.deploymentProgressRefresh()",
				callbackData : {
					currentClusterId : currentClusterId
				}
			});
		},
/*Function for changing style of setup detail page*/	
		pageStyleChange:function(){
			$('input[type="text"], input[type="password"],input[type="email"]').attr({'disabled':'disabled','placeholder': ''}).css(
					{
						"backgroundColor": "white",
						"cursor": "text",
						"border": "none",
						"padding" :"0",
						"box-shadow": "0  0 rgba(0, 0, 0, 0.075) inset",
						"padding-top" :"4px",
						"margin-top" :"4px",
						"width" : "100%"
							
					}).removeClass('input-medium input-mini input-xlarge');
			$('input[type="radio"], input[type="checkbox"]').attr('disabled','disabled').css(
					{
						"cursor": "default",
							
					});
			$('select').attr('disabled',true).css(
					{
						"cursor": "default",
						"backgroundColor": "white",
							
					});
			$('.add-on').css({
				'background-color':'white',
				'border':'none',
			});
		},
		/*Function for opening delete cluster dialog box*/
	    deleteDialog : function(id) {
	        $("#"+id).appendTo('body').modal('show');
	        $('.ui-dialog-titlebar').hide();
	        $('.ui-dialog :button').blur();
	    },
	    
/*Function for cluster delete*/
    deleteCluster : function(id) {
    	$('#'+id).modal('hide');
        url = baseUrl + '/cluster/remove/' + currentClusterId;
        $.ajax({
			'type' : 'DELETE',
			'url' : url,
			'contentType' : 'application/json',
			'dataType' : 'json',
			'success' : function(result) {
				if (!result.output.status) {
					alert(result.output.error[0]);
				}else{
					 $('#'+id).remove();
					 com.impetus.ankush.removeChild(1);   
				}
			},
			error : function(data) {
			}
		});
    },
	    
/*Function for populating defaul values on cluster creation page*/
	getDefaultValue : function(technology,functionName) {
		var url = baseUrl + '/app/metadata/'+technology;
		 com.impetus.ankush.placeAjaxCall(url, "GET", true,null, function(result){
			jsonResult= JSON.stringify(result);
			 var functionN='com.impetus.ankush.'+functionName;
			 if(undefined != functionName || functionName!=''){
				 eval(functionN+'('+jsonResult+')');
			 }
		  });
	},
	
/*Function for loading dashboard by removing child page*/
	loadDashboard : function() {
		com.impetus.ankush.dashboard.createTile();
		com.impetus.ankush.removeChild(1);    
	},
/*Function for showing deployment logs*/
	deploymentProgress:function(clusterId,clusterDetailData){
		 if(clusterDeploymentInterval != null){
			 clusterDeploymentInterval= window.clearInterval(clusterDeploymentInterval);
	        }
	        deploymentLogs=null;
	        deploymentData=null;
	        currentClusterId=null;
	        lastLogDeploy=null;
	        $('#deployButton').removeAttr('disabled');
	        $("#commonDeleteButton").attr('disabled', 'disabled');
	        currentClusterId = clusterId;
	            $("#deploymentStatusLabel").removeClass('label-success').removeClass('label-important');
	            deploymentData = clusterDetailData;
	            if(deploymentData.output.state == 'deploying'){
	                $("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);  
	            }else if(deploymentData.output.state == 'error'){
	                $("#deploymentStatusLabel").addClass('label-important');
	                $("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);
	                $('#clusterDeleteBtn').removeClass('display-none');
	            }else if(deploymentData.output.state == 'deployed'){
	                $("#deploymentStatusLabel").addClass('label-success');
	                $("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);  
	            }
	          
	            lastLogDeploy=deploymentData.output.lastlog;
	            for ( var i = 0; i < deploymentData.output.logs.length; i++) {
	                if(deploymentData.output.logs[i].type == 'error'){
	                    $('#deploymentProgressDiv').prepend('<div style="color:red">' +
	                            deploymentData.output.logs[i].longMessage + '</div>');
	                }else{
	                    $('#deploymentProgressDiv').prepend('<div>' +
	                            deploymentData.output.logs[i].longMessage + '</div>');
	                }
	            }
	            if (deploymentData.output.state != 'deploying') {
	                lastData = deploymentData;
	                deployFlag = true;
	                clusterDeploymentInterval=window.clearInterval(clusterDeploymentInterval);
	            } else {
	                com.impetus.ankush.common.initProgressFunction("com.impetus.ankush.common.deploymentProgressRefresh();",refreshInterval);
	            }
	},
	
/* Function for initializing auto-refresh call of cluster deployment logs function*/
    initProgressFunction : function(functionName,timeInterval) {
    	clusterDeploymentInterval=window.clearInterval(clusterDeploymentInterval);
    	nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
        clusterDeploymentInterval=setInterval(functionName,timeInterval);
    },
    
/*Function to be called for auto-refresh of deployment logs*/
	deploymentProgressRefresh:function(){
		 if(document.getElementById("deploymentProgressDiv") == null ){
			 clusterDeploymentInterval= window.clearInterval(clusterDeploymentInterval); 
			 return;
		        }
		            var url = baseUrl + '/monitor/' + currentClusterId + '/logs?lastlog=' + lastLogDeploy;
		            $.ajax({
		                        'type' : 'GET',
		                        'url' : url,
		                        'contentType' : 'application/json',
		                        'dataType' : 'json',
		                        'async' : true,
		                        'success' : function(result) {
		                        if(result.output.status){
		                        deploymentData = result;
		                        $("#deploymentStatusLabel").removeClass('label-success').removeClass('label-important');
		                        if(deploymentData.output.state == 'deploying'){
		                            $("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);  
		                        }else if(deploymentData.output.state == 'error'){
		                            $("#deploymentStatusLabel").addClass('label-important');
		                            $("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);
		                            $('#clusterDeleteBtn').removeClass('display-none');
		                            clusterDeploymentInterval=window.clearInterval(clusterDeploymentInterval);
		                        }else if(deploymentData.output.state == 'deployed'){
		                            $("#deploymentStatusLabel").addClass('label-success');
		                            $("#deploymentStatusLabel").html('').text(deploymentStatusOnHead[deploymentData.output.state]);  
		                            clusterDeploymentInterval=window.clearInterval(clusterDeploymentInterval);
		                        }
		                            lastLogDeploy=deploymentData.output.lastlog;
		                            for ( var i = 0; i < deploymentData.output.logs.length; i++) {
		                                if(deploymentData.output.logs[i].type == 'error'){
		                                    $('#deploymentProgressDiv').prepend('<div style="color:red">' +
		                                    		deploymentData.output.logs[i].longMessage + '</div>');
		                                }else{
		                                    $('#deploymentProgressDiv').prepend('<div>' +
		                                    		deploymentData.output.logs[i].longMessage + '</div>');
		                                }
		                            }  
		                            if (deploymentData.output.state == 'deployed') {
		                                deployFlag = true;
		                                lastData = deploymentData;
		                                clusterDeploymentInterval=window.clearInterval(clusterDeploymentInterval);
		                            } else if (deploymentData.output.state != 'deploying') {
		                            	clusterDeploymentInterval=window.clearInterval(clusterDeploymentInterval);
		                            } else if(clusterDeploymentInterval==null ||clusterDeploymentInterval==undefined){
		                            	 com.impetus.ankush.common.initProgressFunction("com.impetus.ankush.common.deploymentProgressRefresh();",refreshInterval);
		                            }
		                        }
		                        },
		                        error : function() {
		                        }
		                    });
	},
/*Function for loading node drilldown page from setupdetail page*/
	loadNodeStatusNode : function(i,title) {
		$('#content-panel').sectionSlider('addChildPanel',{
			current : 'login-panel',
			url : baseUrl + '/common-cluster/nodeStatusNode/'+ currentClusterId + '/details',
			method : 'get',
			params : {},
			title : 'Node Setup Detail',
			tooltipTitle : title,
			callback : function(data) {
				com.impetus.ankush.common.nodeLogs(data.i);
			},
			previousCallBack : "com.impetus.ankush.common.nodeStatus("+currentClusterId+")",
			callbackData : {
				i : i,
			}
		});
	},
/*Function for showing node level deployment logs*/
	nodeLogs:function(node){
		clusterDeploymentInterval=window.clearInterval(clusterDeploymentInterval);
		nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
		 ipAddress =nodeStatusData.output.nodes[node].publicIp;
		 $('#nodeDetailHead1').html('').text(ipAddress);
		 for(var key in nodeStatusData.output.nodes[node]){
			 var newKey=com.impetus.ankush.camelCaseKey(key);
			 if(key !='errors')
			 $('#nodeDeploymentField').append('<div class="row-fluid"><div class="span2"><label class=" text-right form-label">'+newKey+':</label></div><div class="span10"><label class="form-label label-black">'+nodeStatusData.output.nodes[node][key]+'</label></div></div>'); 
		 }
		
		 var url = baseUrl + '/monitor/' + currentClusterId + '/logs?ip='+ipAddress;
		 $.ajax({
	            'type' : 'GET',
	            'url' : url,
	            'contentType' : 'application/json',
	            'dataType' : 'json',
	            'async' : true,
	            'success' : function(result) {
	            	  if(result.output.status){
	            	 nodeDeployLogData=result.output;
	            	  for ( var i = 0; i < nodeDeployLogData.logs.length; i++) {
	                      if(nodeDeployLogData.logs[i].type == 'error'){
	                          $('#nodeDeployProgress').prepend('<div style="color:red">' +
	                                  nodeDeployLogData.logs[i].longMessage +  '</div>');
	                      }else{
	                          $('#nodeDeployProgress').prepend('<div>' +
	                                  nodeDeployLogData.logs[i].longMessage +  '</div>');
	                      }              
	                  }
		            	 if(nodeStatusData.output.state =='error'){
		            		 nodeDeploymentInterval=window.clearInterval(nodeDeploymentInterval);
		            		 if(Object.keys(nodeStatusData.output.nodes[node].errors).length>0){
		            			 $('#nodeErrorDiv').show();
		            		 }
		            		  $('#errorOnNodeDiv').css("margin-top","10px");
		            		  for(var key in nodeStatusData.output.nodes[node].errors){
		            	          $('#errorOnNodeDiv').append('<label class="text-left" style="color: black;" id="'+key+'">'+nodeStatusData.output.nodes[node].errors[key]+'</label>');
		            	           }
		            	  }  
	                  if(nodeDeployLogData.state =='deployed' || nodeDeployLogData.state =='error'){
	                	  nodeDeploymentInterval=window.clearInterval(nodeDeploymentInterval);
	                  } else{
	                	  com.impetus.ankush.common.initNodeLogs();
	                  } 
	            }
	            },
		 });
	},
	
/* Function for initializing auto-refresh call of node deployment logs function*/
	initNodeLogs:function(){
		nodeDeploymentInterval=window.clearInterval(nodeDeploymentInterval);
		 nodeDeploymentInterval = setInterval('com.impetus.ankush.common.nodeLogsRefresh();', refreshInterval);
	},
/*Function to be called for auto-refresh of node level deployment logs*/
	nodeLogsRefresh:function(){
		 if(document.getElementById("nodeDeployProgress") == null){
	            nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
	            return;
	        }
	        var url = baseUrl + '/monitor/' + currentClusterId+ '/logs?ip=' + ipAddress +'&lastlog='+nodeDeployLogData.lastlog;
	        $.ajax({
	            'type' : 'GET',
	            'url' : url,
	            'contentType' : 'application/json',
	            'dataType' : 'json',
	            'async' : true,
	            'success' : function(result) {
            	  if(result.output.status){	
	                nodeDeployLogData = result.output;
	                $('#nodeDetailHead1').html('').text(ipAddress);
	                for ( var i = 0; i < nodeDeployLogData.logs.length; i++) {
	                    if(nodeDeployLogData.logs[i].type == 'error'){
	                        $('#nodeDeployProgress').prepend('<div style="color:red">' +
	                                nodeDeployLogData.logs[i].longMessage +  '</div>');
	                    }else{
	                        $('#nodeDeployProgress').prepend('<div>' +
	                                nodeDeployLogData.logs[i].longMessage +  '</div>');
	                    }              
	                }
	                if(nodeDeployLogData.state =='deployed' || nodeDeployLogData.state =='error'){
	                	nodeDeploymentInterval=window.clearInterval(nodeDeploymentInterval);
	                }
	            }
	            },
	            error : function() {
	            }
	        });
	},
	
/*Function for loading node status page*/
	loadNodeStatus:function(){
		 $('#content-panel').sectionSlider('addChildPanel', {
	            current : 'login-panel',
	            url : baseUrl + '/common-cluster/nodeStatus',
	            method : 'get',
	            params : {},
	            title : 'Node Status',
	            tooltipTitle : 'Deployment Progress',
	            callback : function(data) {
	                com.impetus.ankush.common.nodeStatus(currentClusterId);
	            },
	        	previousCallBack : "com.impetus.ankush.common.deploymentProgressRefresh()",
	            callbackData : {
	                currentClusterId : currentClusterId
	            }
	        });

	},
	
/*Function for populating node status tabel */
	nodeStatus:function(currentClusterId){
		clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
		nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		 var url = baseUrl + '/monitor/' + currentClusterId + '/nodelist';
		 $.ajax({
	            'type' : 'GET',
	            'url' : url,
	            'contentType' : 'application/json',
	            'dataType' : 'json',
	            'async' : true,
	            'success' : function(result) {
	            	  if(result.output.status){
	            	nodeStatusData=result;
	                  if (nodeIpStatusTable != null) {
	                      nodeIpStatusTable.fnClearTable();
	                  } else
	                      nodeIpStatusTable = $("#nodeIpStatusTable").dataTable();
	                  $("#deploymentStatusNodeListLabel").removeClass('label-success').removeClass('label-important');
                      if(nodeStatusData.output.state == 'deploying'){
                          $("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);  
                      }else if(nodeStatusData.output.state == 'error'){
                          $("#deploymentStatusNodeListLabel").addClass('label-important');
                          $("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
                          $('#clusterDeleteBtn').removeClass('display-none');
                          nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
                      }else if(nodeStatusData.output.state == 'deployed'){
                          $("#deploymentStatusNodeListLabel").addClass('label-success');
                          $("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);  
                          nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
                      }
	                      for ( var i = 0; i < result.output.nodes.length; i++) {
	                          var status=result.output.nodes[i].message;
	                          if(status==null)
	                              status='';
	                          var addId = null;
          					addId = nodeIpStatusTable.fnAddData([
	                                          result.output.nodes[i].publicIp,
	                                          '<a class=""><label id="nodeIpType' + i + '">'
	                                                  + result.output.nodes[i].type
	                                                  + '</label></a>',
	                                          '<a class=""><label id="nodeIpStatus' + i
	                                                  + '">'
	                                                  + status
	                                                  + '</label></a>',
	                                          '<a href="#" onclick="com.impetus.ankush.common.loadNodeStatusNode(\''
	                                                  + i
	                                                  + '\',\'Node Status\');" ><img id="navigationImg'
	                                                  + i
	                                                  + '" src="'
	                                                  + baseUrl
	                                                  + '/public/images/icon-chevron-right.png"/></a>' ]);
          					var theNode = nodeIpStatusTable.fnSettings().aoData[addId[0]].nTr;
            				theNode.setAttribute('id', 'node'+ result.output.nodes[i].publicIp.split('.').join('_'));
            				if (Object.keys(result.output.nodes[i].errors).length>0 ){
            					rowId = result.output.nodes[i].publicIp.split('.').join('_');
            					$('td', $('#node'+rowId)).addClass('error-row');
            					$('#node' + rowId).addClass('error-row');
            				}
	                        
	                      }
	                      if (result.output.state =='deployed' || result.output.state =='error') {
	                    	  nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
	                      }else{
	                    		com.impetus.ankush.common.initNodeStatus();
	                      }
	            	  }
	            }
		 });
	},
/* Function for initializing auto-refresh call of node status tabel*/
	initNodeStatus:function(){
		nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
		nodeStatusDeploymentInterval=setInterval('com.impetus.ankush.common.nodeStatusRefresh()',refreshInterval);
	},
/*Function to be called for auto-refresh of node status table*/
	nodeStatusRefresh:function(){
		 if(document.getElementById("nodeIpStatusTable") == null){
			 nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
	            return;
	        }
	        var url = baseUrl + '/monitor/' + currentClusterId + '/nodelist';
			 $.ajax({
		            'type' : 'GET',
		            'url' : url,
		            'contentType' : 'application/json',
		            'dataType' : 'json',
		            'async' : true,
		            'success' : function(result) {
		            	if (nodeIpStatusTable != null) {
		    	            nodeIpStatusTable.fnClearTable();
		    	        }
		            	  if(result.output.status){
		            	nodeStatusData=result;
		            	 $("#deploymentStatusNodeListLabel").removeClass('label-success').removeClass('label-important');
	                      if(nodeStatusData.output.state == 'deploying'){
	                          $("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);  
	                      }else if(nodeStatusData.output.state == 'error'){
	                          $("#deploymentStatusNodeListLabel").addClass('label-important');
	                          $("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);
	                          $('#clusterDeleteBtn').removeClass('display-none');
	                          nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
	                      }else if(nodeStatusData.output.state == 'deployed'){
	                          $("#deploymentStatusNodeListLabel").addClass('label-success');
	                          $("#deploymentStatusNodeListLabel").html('').text(deploymentStatusOnHead[nodeStatusData.output.state]);  
	                          nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
	                      }
		            	for ( var i = 0; i < result.output.nodes.length; i++){
			                var status=result.output.nodes[i].message;
			                if(status==null)
			                    status='';
			                var addId = null;
          					addId =nodeIpStatusTable.fnAddData([
			                                        result.output.nodes[i].publicIp,
			                                '<a class=""><label id="nodeType' + i + '">'
			                                        + result.output.nodes[i].type
			                                        + '</label></a>',
			                                '<a class=""><label id="nodeStatus' + i + '">'
			                                        + status
			                                        + '</label></a>',
			                                '<a href="#" onclick="com.impetus.ankush.common.loadNodeStatusNode('
			                                        + i
			                                        + ',\'Node Status\');" ><img id="navigationImg'
			                                        + i
			                                        + '" src="'
			                                        + baseUrl
			                                        + '/public/images/icon-chevron-right.png"/></a>' ]);
          					var theNode = nodeIpStatusTable.fnSettings().aoData[addId[0]].nTr;
            				theNode.setAttribute('id', 'node'+ nodeStatusData.output.nodes[i].publicIp.split('.').join('_'));
            				if (Object.keys(result.output.nodes[i].errors).length>0 ){
            					rowId = result.output.nodes[i].publicIp.split('.').join('_');
            					$('td', $('#node'+rowId)).addClass('error-row');
            					$('#node' + rowId).addClass('error-row');
            				}
		            	}
	            if (result.output.state == 'deployed' || result.output.state == 'error') {
	                deployFlag = true;
	                nodeStatusDeploymentInterval=window.clearInterval(nodeStatusDeploymentInterval);
	            	}
		            }
		            }
		   });
	},
};
