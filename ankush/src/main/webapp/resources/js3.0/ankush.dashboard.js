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

var resultData = null;
var myDialog =null;
var maintenanceNodeTable=null;
var lastLogMaintenance=0;
var nodeMaintenanceInterval=null;
var clusterId="";
var host="";
var right_arrow = '<img src= " ' + baseUrl + '/public/images/icon-chevron-right.png"/>';
com.impetus.ankush.dashboard = {
//this method will create tiles based on cluster state on cluster overview page 
	createTile : function() {
		var $allTile = $('#allTile'), $boxes, innerHtml, clusterTilesInfo, boxMaker, itr, tileClass, tileStatus, bottomInfo;
		boxMaker = {};
		var Url = baseUrl + "/clusteroverview";
		 com.impetus.ankush.placeAjaxCall(Url, "GET", true, null, function(data) {
			 var clusterMaintenanceFlag = false;
				 if ((data != null) && (data != undefined)) {
	        				var dashboardIconSelectionFlag = true;
							$('#allTile').empty();
							itr = 1;
							bottomInfo = '';

							var tiles = [], runningTiles = [], errorTiles = [], warningTiles = [], deployingTiles = [], pendingTiles = [];
							var clustorTiles, tile;
							/*
							 * $wrapperError.masonry({ itemSelector : '.box' });
							 * 
							 * $wrapperOther.masonry({ itemSelector : '.box' });
							 */
							/*$allTile.masonry({
								itemSelector : '.box'
							});*/
							boxMaker.makeBoxes = function(innerHtml, boxClass,
									clusterData) {
								var boxes = [];
								var box = document.createElement('div');
						if (('undefined' != clusterData)){
									box.style.cursor = "pointer";
									box.onclick = function dbClick(e) {
										
										box.onclick = null;
						    	            com.impetus.ankush.dashboard.goToStateDetails(clusterData);
						    	            setTimeout(function()
						    	            		{
											box.onclick = dbClick;
										}, 2000);
									};
								}
								box.className = 'box ' + boxClass;
								box.innerHTML = innerHtml;
								boxes.push(box);
								return boxes;
							};

							clusterTilesInfo = data.output;
							clusterTilesInfo.forEach(function(d) {
								switch (d.status) {
								case com.impetus.ankush.constants.stateError:
								case com.impetus.ankush.constants.stateDown:
								case com.impetus.ankush.constants.stateCritical:
									errorTiles.push(d);
									break;
								case com.impetus.ankush.constants.stateWarning:
									warningTiles.push(d);
									break;
								case com.impetus.ankush.constants.stateDeploying:
								case com.impetus.ankush.constants.stateRemoving:
									deployingTiles.push(d);
									break;
								case com.impetus.ankush.constants.stateAddNode:
								case com.impetus.ankush.constants.stateRemoveNode:
								case com.impetus.ankush.constants.stateMaintenance:
								default:
									runningTiles.push(d);
									break;
								}
							});
						tiles = [ pendingTiles, deployingTiles, runningTiles, warningTiles,
								errorTiles];

							for ( var i = 0; i < tiles.length; i++) {
								if (tiles[i].length != 0) {
									clustorTiles = tiles[i];
									for ( var j = 0; j < clustorTiles.length; j++) {
										tile = clustorTiles[j];
										switch (tile.status) {
										case com.impetus.ankush.constants.stateWarning:
											tileClass = 'col-md-3 yellowtiles';
											tileStatus = 'Warning';
											break;
										case com.impetus.ankush.constants.stateError:
											dashboardIconSelectionFlag = false;
											tileClass = 'col-md-3 redtiles';
											tileStatus = 'Deployment Error';
											break;
										case com.impetus.ankush.constants.stateDown:
											dashboardIconSelectionFlag = false;
											tileClass = 'col-md-3 redtiles';
											tileStatus = 'Down';
											break;
										case com.impetus.ankush.constants.stateCritical:
											dashboardIconSelectionFlag = false;
											tileClass = 'col-md-3 redtiles';
											tileStatus = 'Critical';
											break;
										case com.impetus.ankush.constants.stateDeploying:
											tileClass = 'col-md-3 bluetiles';
											tileStatus = 'Deploying...';
											break;
										case com.impetus.ankush.constants.stateRegistering:
											tileClass = 'col-md-3 bluetiles';
											tileStatus = 'Registering...';
											break;	
										case com.impetus.ankush.constants.stateRemoving:
											tileClass = 'col-md-3 bluetiles';
											tileStatus = 'Removing...';
											break;
										case com.impetus.ankush.constants.stateAddNode:
											tileClass = 'col-md-3 greentiles';
											tileStatus = 'Adding Nodes...';
											break;
										case com.impetus.ankush.constants.stateRemoveNode:
											tileClass = 'col-md-3 greentiles';
											tileStatus = 'Removing Nodes...';
											break;
										case com.impetus.ankush.constants.stateMaintenance:
											if(clusterMaintenanceFlag === false){
												$('#maintInfoDashboard').show();
												$('#maintInfoDashboard').addClass('license-Warning');
												clusterMaintenanceFlag = true;
											}
											tileClass = 'col-md-3 greentiles';
											tileStatus = 'Maintenance...';
											break;
										default:
											tileClass = 'col-md-3 greentiles';
											tileStatus = 'Running...';
											break;
										}
										if (tile.name.length > 19) {
											innerHtml = '<div class="thumbnail" id="'
													+ tile.name
													+ '" ><h3 title="'
													+ tile.name
													+ '">'
													+ tile.name
															.substring(0, 18)
													+ '...</h3><p>'
													+ tileStatus + '</p>';
										} else
											innerHtml = '<div class="thumbnail" id="'
													+ tile.name
													+ '" ><h3 title="'
													+ tile.name
													+ '">'
													+ tile.name
													+ '</h3><p>'
													+ tileStatus + '</p>';
										if (tile.notifications) {
											innerHtml += '<div class="notification"><div>&nbsp;';
											if (tile.notifications.alerts) {
												innerHtml += '<span class="count">'
														+ tile.notifications.alerts
														+ '</span><span class="notify">Alerts</span>';
											}
											if (tile.notifications.warnings) {
												innerHtml += '<span class="count">'
														+ tile.notifications.warnings
														+ '</span><span class="notify">Warnings</span>';
											}
											innerHtml += '</div></div>';
										}
										if((tile.status !== 'ERROR') && (tile.status !== 'DEPLOYMENTFAILED') && (tile.status !== 'REMOVING') && (tile.status !== 'DEPLOYING')){
											if (Object.keys(tile.data).length !== 0) {
												var divId = "div_" + itr;
												itr++;
												innerHtml += '<div class="chart" id="'
														+ divId + '"></div>';
											}
										}
										if (tile.technology) {
                                        
                                                    bottomInfo = tile.technology;
                                    }

										if (tile.environment) {
											if (bottomInfo == '') {
												bottomInfo = tile.environment;
											} else {
												bottomInfo += '/'
														+ tile.environment;
											}
										}

									innerHtml += '<div class="bottomtitle">' + bottomInfo
											+ '</div></div>';
										var clusterData = {
											'id' : tile.clusterId,
											'technology' : tile.technology,
											'state' : tile.status,
											'name' : tile.name,
											'env' : tile.environment
										};
										$boxes = $(boxMaker.makeBoxes(
												innerHtml, tileClass,
												clusterData));
										/*
										 * if (i > 2) {
										 * 
										 * else {
										 *
										 */
										$allTile.prepend($boxes);

										if (tile.data) {
											tile.data = {
												"CPU" : [
														{
															"total" : "<100",
															"utilization" : tile.data.cpu
														},
														{
															"total" : "0-100",
															"utilization" : 100 - tile.data.cpu
														} ],
												"MEMORY" : [
														{
															"total" : "<100",
															"utilization" : tile.data.memory
														},
														{
															"total" : "0-100",
															"utilization" : 100 - tile.data.memory
														} ]
											};
											if((tile.status !== 'ERROR') && (tile.status !== 'DEPLOYMENTFAILED') && (tile.status !== 'REMOVING') && (tile.status !== 'DEPLOYING'))
												getUtilizationChart(divId,tile.data);
										}
									}
									
								}
								
							}
							if(clusterMaintenanceFlag === false){
								$('#maintInfoDashboard').hide();
								$('#maintInfoDashboard').removeClass('license-Warning');
							}
							if (dashboardIconSelectionFlag == false) {
								$('#dashboardIcon').addClass('imgAnimate');
							} else {
								$('#dashboardIcon').removeClass('imgAnimate');
							}
						}
						innerHtml = '<div class="thumbnail" id="_tileCreateNewCluster" ><a href="#" onclick="com.impetus.ankush.dashboard.createTechnologyPopUp();"><img src="'
								+ baseUrl
								+ '/public/images/plus.png" id="_iconCreateNewCluster" style="max-width : -1% ! important;"/></a></div>  ';
						$boxes = $(boxMaker.makeBoxes(innerHtml,
								'col-md-3 addtiles', 'undefined'));
						$allTile.append($boxes).masonry($boxes);
				});
	},
	// this method will open a dialog in which create cluster technology will be
	// shown
	createTechnologyPopUp : function() {
		/*com.impetus.ankush.selectTechnology.createCluster('hybrid-cluster');
		return;*/
		/*if(projectEnvVariable != 'Ankush'){
			com.impetus.ankush.selectTechnology.createCluster('hybrid-cluster');
			return;
		}*/
		$("#technoloigyList").empty();
		$("#technologyDialogBox").modal('show');
		//$("#headingTechDialog").html('Technology Stack');
		var linkImage = {
			"Cassandra" : '<a href="#" id="cassandra" ><img src="'
					+ baseUrl
					+ '/public/images/technology-icon/cassandra.gif" style="height: 55px;" alt="Cassandra"/></a>',
			"Hadoop" : '<a href="#" id="hadoop" ><img src="'
					+ baseUrl
					+ '/public/images/technology-icon/Hadoop.png" style="padding-top:10px; width:140px" alt="Hadoop"/></a>',
		};
		var hash = {
			'Cassandra'	: 'com.impetus.ankush.selectTechnology.createCluster(\'hybrid-cluster\',\'\',\'Cassandra\')',
			'Hadoop' : 'com.impetus.ankush.selectTechnology.createCluster(\'hybrid-cluster\',\'\',\'Hadoop\')',
		};
		var url = baseUrl + '/app/metadata/technologies';
		var tech = new Array();
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
                		   for(var key in result.output.technologies){
                				$("#technoloigyList").append('<div class="box infobox masonry-brick"style="float:left;margin:10px;"><div class="thumbnail text-center" style="width:150px;height:60px;background-color:white ! important;" onclick="'+hash[result.output.technologies[key]]+'">'+linkImage[result.output.technologies[key]]+'</div></div>');
                		   }
		});
	},

//this method will call functions required in monitoring page on the basis of technology 
	goToStateDetails : function(data) {
		
			if((data.state == com.impetus.ankush.constants.stateError) || (data.state == com.impetus.ankush.constants.stateDeploying)|| data.state=='registering'){
				com.impetus.ankush.dashboard.clusterDetail(data.id,data.name,data.technology,data);
			}else if(data.state == com.impetus.ankush.constants.stateRemoving){
				var url=baseUrl + '/common-cluster/'+data.name+'/C-D/deleteCluster/'+data.id+'/'+data.technology;
            	$(location).attr('href',(url));
			}else{
				data.technology = "hybrid";
				$(location).attr('href',(baseUrl + '/commonMonitoring/'+data.name+'/C-D/'+data.id+'/'+data.technology));
			}
	},

	clusterDetail : function(clusterId, clusterName, clusterTechnology,clusterData) {
		var url = baseUrl + '/monitor/' + clusterId + '/logs';
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
						var id = clusterId;
						clusterDetailData = result;
						if(clusterData.state==com.impetus.ankush.constants.stateDeploying || clusterData.state==com.impetus.ankush.constants.stateError	 ||clusterData.state=='registering' ){
		                    if(document.getElementById("deploymentStatusLabel") == null){
		                    	var url=baseUrl + '/common-cluster/'+clusterName+'/C-D/'+clusterId+'/'+clusterTechnology;
		                    	$(location).attr('href',(url));
							}
						}else {
							com.impetus.ankush.commonMonitoring.init(clusterData);
	                }
	        });	
	},
	
	
	
	// this method will get user name will be shown in profile pop up box
	getUserId : function() {
		url = baseUrl + '/user/userid';
		var userId = null;
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(data) {
				userId = data.output.username;
				$('#userId').text(userId);
		});
	},
	// General method for showing error or errors
	getProperNounForError : function(errorCount) {
		var errorCountMsg = errorCount + " Errors";
		if (errorCount == 1) {
			errorCountMsg = errorCount + " Error";
		}
		return errorCountMsg;
	},
	underMaintenanceClusters : function(){
		url = baseUrl + '/dashboardnew/clusterMaintenance';
		$('#content-panel').sectionSlider('addChildPanel', {
            current : 'login-panel',
            url : url,
            method : 'get',
            params : {
            	
            },
            title : 'Cluster Maintenance',
            tooltipTitle : 'Cluster Overview',
            callback : function(data) {
            com.impetus.ankush.dashboard.getMaintenanceClusterData();
            },
        	callbackData : {
            }
        });	
	},
	
	getMaintenanceClusterData:function(){
		var url=baseUrl + '/monitor/maintenancedetails';
		com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
		
			var tableObj = new Array();
			for ( var i = 0; i < result.output.clusters.length; i++) {
				for ( var j = 0; j < result.output.clusters[i].nodes.length; j++) {
					var rowObj = new Array();
					rowObj.push( '<ins class="left"></ins><div class="left">&nbsp;&nbsp;&nbsp;&nbsp;'+result.output.clusters[i].clusterName+'</div>');
					rowObj.push("");
					rowObj.push(result.output.clusters[i].nodes[j].host);
					rowObj.push(result.output.clusters[i].nodes[j].status);
					rowObj.push("<a href='#' onclick='com.impetus.ankush.dashboard.loadMaintenanceNode(\""+ result.output.clusters[i].clusterId+"\",\""+ result.output.clusters[i].nodes[j].host+"\")'>"+ right_arrow+ "</a>");
					tableObj.push(rowObj);
				}
			}if(maintenanceNodeTable==null){
				maintenanceNodeTable.dataTable();
			}else{
				maintenanceNodeTable.fnClearTable();
			}	
			maintenanceNodeTable.fnAddData(tableObj);	
		});
	},
	loadMaintenanceNode : function(clusterId,host){
		url = baseUrl + '/dashboardnew/nodeMaintenance';
		lastLogMaintenance=0;
		$('#content-panel').sectionSlider('addChildPanel', {
            current : 'login-panel',
            url : url,
            method : 'get',
            params : {
            	clusterId:clusterId,
            	host:host
            },
            title : 'Node Maintenance',
            tooltipTitle : 'Cluster Maintenance',
            callback : function(data) {
            com.impetus.ankush.dashboard.getMaintenanceNodeData();
            },
        	callbackData : {
            }
        });	
	},
	getMaintenanceNodeData:function(){
		var url=baseUrl + '/monitor/'+clusterId+'/mlogs?ip='+host+'&lastlog=' + lastLogMaintenance;
		com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
			lastLogMaintenance=result.output.lastlog;
			$("#nodeMaintenanceStatus").text(result.output.nodeStatus);
				$("#nodeMaintenanceStatusLabel").removeClass('label-success').removeClass('label-important');
				 if (result.output.nodeStatus=="Failed") {
					$("#nodeMaintenanceStatusLabel").addClass('label-important');
					$("#nodeMaintenanceStatusLabel").html('').text("Error during maintenance");
				} else if (result.output.nodeStatus=="Done") {
					$("#nodeMaintenanceStatusLabel").addClass('label-success');
					$("#nodeMaintenanceStatusLabel").html('').text("Maintenance progress completed");
				}else{
					$("#nodeMaintenanceStatusLabel").html('').text("Maintenance in progress");
				}
				for ( var i = 0; i < result.output.logs.length; i++) {
					if (result.output.logs[i].type == 'error') {
						$('#nodeMaintenanceProgress').prepend('<div style="color:red">'	+ result.output.logs[i].longMessage+ '</div>');
					} else {
						$('#nodeMaintenanceProgress').prepend('<div>'+ result.output.logs[i].longMessage + '</div>');
					}
				}
				if(result.output.nodeStatus=="Done" ||result.output.nodeStatus=="Failed"){
					autoRefreshCallsObject[$.data(
							document, "panels").children.length][0].varName = window.clearInterval(autoRefreshCallsObject[$.data(
							document, "panels").children.length][0].varName);
					autoRefreshCallsObject[$.data(
							document, "panels").children.length][0].varName = window.clearInterval(autoRefreshCallsObject[$.data(
							document, "panels").children.length][0].varName);
				}
				
				
				if (result.output.nodeStatus=="Failed") {
					$('#nodeErrorDiv').show();
				}
				$('#errorOnNodeDiv').css("margin-top","12px");
				for ( var key in result.output.errors) {
					$('#errorOnNodeDiv').append('<label class="text-left" style="color: black;" id="'+ key+ '">'+ result.output.errors[key]+ '</label>');
				}
				
				
		});
	},
	dialogRemove:function(){
		$("#templateCreate").remove();
		$("#confirmTemplate").remove();
		$("#deleteClusterDialogDeploy").remove();
		$("#deleteClusterDialogcommonMonitor").remove();
	},
};