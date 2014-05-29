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

var resultData = null;
var myDialog =null;
var autoRefreshCallSystemOverview;
com.impetus.ankush.dashboard = {
//this method will create tiles based on cluster state on cluster overview page 
	createTile : function() {
		var $allTile = $('#allTile'), $boxes, innerHtml, clusterTilesInfo, boxMaker, itr, tileClass, tileStatus, bottomInfo;
		boxMaker = {};
		var Url = baseUrl + "/clusteroverview";
		$.ajax({
					'type' : 'GET',
					'url' : Url,
					'contentType' : 'application/json',
					'dataType' : 'json',
					'success' : function(data) {
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
						if (('undefined' != clusterData) && (clusterData.state != 'removing')){
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
								case 'error':
								case 'down':
								case 'critical':
								case 'deploymentfailed':
									errorTiles.push(d);
									break;
								case 'warning':
									warningTiles.push(d);
									break;
								case 'deploying':
								case 'removing':
									deployingTiles.push(d);
									break;
								case 'Adding Nodes':
								case 'Removing Nodes':
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
										case 'warning':
											tileClass = 'span3 yellowtiles';
											tileStatus = 'Warning';
											break;
										case 'error':
											dashboardIconSelectionFlag = false;
											tileClass = 'span3 redtiles';
											tileStatus = 'Deployment Error';
											break;
										case 'down':
											dashboardIconSelectionFlag = false;
											tileClass = 'span3 redtiles';
											tileStatus = 'Down';
											break;
										case 'critical':
											dashboardIconSelectionFlag = false;
											tileClass = 'span3 redtiles';
											tileStatus = 'Critical';
											break;
										case 'deploymentfailed':
											dashboardIconSelectionFlag = false;
											tileClass = 'span3 redtiles';
											tileStatus = 'Deployment failed';
											break;
										case 'deploying':
											tileClass = 'span3 bluetiles';
											tileStatus = 'Deploying...';
											break;
										case 'removing':
											tileClass = 'span3 bluetiles';
											tileStatus = 'Removing...';
											break;
										case 'Adding Nodes':
											tileClass = 'span3 greentiles';
											tileStatus = 'Adding Nodes...';
											break;
										case 'Removing Nodes':
											tileClass = 'span3 greentiles';
											tileStatus = 'Removing Nodes...';
											break;
										default:
											tileClass = 'span3 greentiles';
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
										if (tile.data) {
											var divId = "div_" + itr;
											itr++;
											innerHtml += '<div class="chart" id="'
													+ divId + '"></div>';
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
											getUtilizationChart(divId,
													tile.data);
										}
									}
								}
							}
							if (dashboardIconSelectionFlag == false) {
								$('#dashboardIcon').addClass('imgAnimate');
							} else {
								$('#dashboardIcon').removeClass('imgAnimate');
							}
						}
						innerHtml = '<div class="thumbnail" id="_tileCreateNewCluster" ><a href="#" onclick="com.impetus.ankush.dashboard.createTechnologyPopUp();"><img src="'
								+ baseUrl
								+ '/public/images/plus.png" id="_iconCreateNewCluster" /></a></div>  ';
						$boxes = $(boxMaker.makeBoxes(innerHtml,
								'span3 addtiles', 'undefined'));
						$allTile.append($boxes).masonry($boxes);
					},
					error : function(data) {

					}
				});
	},
	// this method will open a dialog in which create cluster technology will be
	// shown
	createTechnologyPopUp : function() {
		$("#technoloigyList").empty();
		$("#technologyDialogBox").appendTo('body').modal('show');
		$("#headingTechDialog").html('Technology Stack');
		var linkImage = {
			"Cassandra" : '<a href="#" id="cassandra" ><img src="'
					+ baseUrl
					+ '/public/images/technology-icon/cassandra.gif" alt="Cassandra"/></a>',
			"Hadoop" : '<a href="#" id="hadoop" ><img src="'
					+ baseUrl
					+ '/public/images/technology-icon/Hadoop.png" style="padding-top:10px;" alt="Hadoop"/></a>',
			"Oracle NoSQL Database" : '<a href="#" id="oracleNoSql" ><img src="'
					+ baseUrl
					+ '/public/images/technology-icon/oracle.gif" style="width:140px;height:55px;" alt="Oracle NoSQL Database"/></a>',
			"Storm" : '<a href="#" id="storm" ><img src="'
					+ baseUrl
					+ '/public/images/technology-icon/storm.jpg" style="padding-top:10px;" alt="Storm"/></a>',
			"Kafka" : '<a href="#" id="kafka" ><img src="'
					+ baseUrl
						+ '/public/images/technology-icon/kafka.jpg" style="padding-top:10px;" alt="Kafka"/></a>',
			"ElasticSearch" : '<div style="padding-top:0px;"><a href="#" id="elasticSearch" ><img src="'
				+ baseUrl
				+ '/public/images/technology-icon/ElasticSearch.png"  alt="ElasticSearch" style="padding-top:20px;"/></a></div>',
		};
		var hash = {
			'Cassandra'	: 'com.impetus.ankush.selectTechnology.createCluster(\'cassandra-cluster\')',
			'Hadoop' : 'com.impetus.ankush.selectTechnology.createCluster(\'hadoop-cluster\')',
			'Oracle NoSQL Database' : 'com.impetus.ankush.selectTechnology.createCluster(\'oracle-cluster\')',
			'Storm' : 'com.impetus.ankush.selectTechnology.createCluster(\'storm-cluster\')',
			'Kafka' : 'com.impetus.ankush.selectTechnology.createCluster(\'kafka-cluster\')',
			'ElasticSearch' : 'com.impetus.ankush.selectTechnology.createCluster(\'elasticSearch-cluster\')',
		};
		var url = baseUrl + '/app/metadata/technologies';
		var tech = new Array();
		$.ajax({
                   'type' : 'GET',
                   'url' : url,
                   'contentType' : 'application/json',
                   'dataType' : 'json',
                   'async' : true,
                   'success' : function(result) {
                		   for(var key in result.output.technologies){
                				$("#technoloigyList").append('<div class="box infobox masonry-brick"style="float:left;margin:10px;"><div class="thumbnail text-center" style="width:150px;height:60px;background-color:white ! important;" onclick="'+hash[result.output.technologies[key]]+'">'+linkImage[result.output.technologies[key]]+'</div></div>');
                		   }
                   },
                   error : function() {
                   }
		});
		
		
		
	},

//this method will call functions required in monitoring page on the basis of technology 
	goToStateDetails : function(data) {
		if(data.technology == 'Oracle NoSQL Database'){
			if ((data.state == 'error') || (data.state == 'deploying')) {
				com.impetus.ankush.dashboard.clusterDetail(data.id,data.name,data.technology,data);
			}
			else{
				  $('#content-panel').sectionSlider('addChildPanel', {
									current : 'login-panel',
                      url : baseUrl + '/oClusterMonitoring/oDeployedCluster',
									method : 'get',
									title : data.name,
									tooltipTitle : 'Cluster Overview',
									previousCallBack : "com.impetus.ankush.oClusterMonitoring.unloadDetailPage()",
									params : {
										clusterId : data.id,
										clusterName : data.name
									}
								});
			}

		}
		else {
			if((data.state == 'error') || (data.state == 'deploying')){
				com.impetus.ankush.dashboard.clusterDetail(data.id,data.name,data.technology,data);
			}
			else{
				com.impetus.ankush.commonMonitoring.init(data);
			}
		}
	},

	clusterDetail : function(clusterId, clusterName, clusterTechnology,clusterData) {
		var url = baseUrl + '/monitor/' + clusterId + '/logs';
	        $.ajax({
					'type' : 'GET',
					'url' : url,
					'contentType' : 'application/json',
					'dataType' : 'json',
					'async' : true,
					'success' : function(result) {
						var id = clusterId;
						clusterDetailData = result;
	                if(clusterData.state=='deploying' || clusterData.state=='error' ){
	                    if(document.getElementById("deploymentStatusLabel") == null){
	                     $('#content-panel').sectionSlider('addChildPanel', {
													current : 'login-panel',
	                            url : baseUrl + '/common-cluster/deploymentProgress/'+clusterId,
													method : 'get',
													title : 'Deployment Progress',
													tooltipTitle : 'Cluster Overview',
													callback : function(data) {
	                            com.impetus.ankush.common.deploymentProgress(data.clusterId,clusterDetailData);
													},
													previousCallBack : "com.impetus.ankush.dashboard.dashboardRefreshCall()",

													params : {
														clusterTechnology : clusterTechnology
													},
													callbackData : {
														clusterId : clusterId,
													},
												});
							}
						} else {
							if(clusterTechnology == 'Oracle NoSQL Database'){
								 $('#content-panel').sectionSlider('addChildPanel', {
										current : 'login-panel',
	                      url : baseUrl + '/oClusterMonitoring/oDeployedCluster',
										method : 'get',
										title : clusterData.name,
										tooltipTitle : 'Cluster Overview',
										previousCallBack : "com.impetus.ankush.oClusterMonitoring.unloadDetailPage()",
										params : {
											clusterId : clusterData.id,
											clusterName : clusterData.name
										}
									});
							}
							else
							com.impetus.ankush.commonMonitoring.init(clusterData);
	                }/*else if(result.output.state=='error'){
	                
	                    if(document.getElementById("nodeIpTable") == null){
	                    defaultValuesError=result.output;
	                     $('#content-panel').sectionSlider('addChildPanel', {
	                            current : 'login-panel',
	                            url : baseUrl + '/oracle-cluster/home/'+clusterId,
	                            method : 'get',
	                            title : 'Cluster Overview',
	                            tooltipTitle : 'Cluster Overview',
	                            previousCallBack : "com.impetus.ankush.oClusterSetup.unloadErrorPage()",
	                            callback : function(data) {
	                                com.impetus.ankush.oClusterSetup.clusterError(data.id,clusterDetailData);
	                                },
	                                callbackData : {
	                                    id : id,
	                                },
	                         });
					}
	                }*//*else if(result.output.clusterId){
	                    if(document.getElementById("shardNodeTable") == null){
	                     $('#content-panel').sectionSlider('addChildPanel', {
	                            current : 'login-panel',
	                            url : baseUrl + '/oClusterMonitoring/oDeployedCluster',
	                            method : 'get',
	                            title : clusterName,
	                            tooltipTitle : 'Cluster Overview',
	                            previousCallBack : "com.impetus.ankush.oClusterMonitoring.unloadDetailPage()",
	                               params : {
	                                   clusterId : result.output.clusterId,
	                                   clusterName : clusterName
	                               }
				});
	                    }
	                }*/
	            }
	        });	
	},
	// this method will load system overview page and create tiles on that page
	systemOverview : function() {
		var Url = baseUrl + '/systemoverview';
		var clusterTiles = {};
        var data = com.impetus.ankush.placeAjaxCall(Url, "GET", false);
       // var data={"output":[{"line1":"Agent","line2":"Down","line3":"1 Nodes | testAkhil","data":{"type":"Hadoop","clusterId":8},"status":"Notify","url":null},{"line1":"80% above","line2":"Memory","line3":"1 Nodes | testAkhil","data":{"type":"Hadoop","clusterId":8},"status":"Critical","url":null},{"line1":"2","line2":"Hadoop/In Premise running","line3":null,"data":null,"status":"Normal","url":null},{"line1":"Agent","line2":"Down","line3":"2 Nodes | aStormCluster","data":{"type":"Storm","clusterId":18},"status":"Warning","url":null},{"line1":"supervisor","line2":"Down","line3":"1 Nodes | aStormCluster","data":{"type":"Storm","clusterId":18},"status":"Error","url":null},{"line1":"60% - 80% ","line2":"Memory  Utilization","line3":"1 Nodes | Hadoop_RAT","data":{"type":"Hadoop","clusterId":19},"status":"Error","url":null},{"line1":"60% - 80% ","line2":"Memory  Utilization","line3":"1  | kafkaTesting","data":{"type":"Kafka","clusterId":22},"status":"Notify","url":null}],"status":"200","description":"system overview details.","Errors":null};
		if ((data != null) && (data != undefined)) {
        	 if($('#SystemOverviewJsonVariable').val() == JSON.stringify(data.output)){
				return;
			}
        	else{
     			clusterTiles = com.impetus.ankush.tileReordring(data.output);
     		}
        	/*$('#running_tiles').empty();
            $('#error_tiles').empty();*/
			$('#allTilesSystemOverview').empty();
			var tile = document.getElementById('allTilesSystemOverview');
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
			$('.clip').tooltip();
			$('#allTilesSystemOverview').masonry({ itemSelector : '.item',
				columnWidth : 100 });
		}
		$('#SystemOverviewJsonVariable').val(JSON.stringify(data.output));
	},
	// this method will clear all auto refresh interval in application
	clearAllAutorefreshInterval : function() {
		if(!(typeof(hClusterDeployInterval) === 'undefined') && (hClusterDeployInterval != null))
			hClusterDeployInterval = window.clearInterval(hClusterDeployInterval);	
		/*if(!(typeof(autoRefreshCallInterval) === 'undefined') && (autoRefreshCallInterval != null))
			autoRefreshCallInterval = window.clearInterval(autoRefreshCallInterval);*/
		if(!(typeof(autoRefreshCallSystemOverview) === 'undefined') && (autoRefreshCallSystemOverview != null))
			autoRefreshCallSystemOverview = window.clearInterval(autoRefreshCallSystemOverview);
		if(!(typeof(hClusterMapView) === 'undefined') && (hClusterMapView != null))
			hClusterMapView = window.clearInterval(hClusterMapView);
		if(!(typeof(hClusterTilesView) === 'undefined') && (hClusterTilesView != null))
			hClusterTilesView = window.clearInterval(hClusterTilesView);
		if(!(typeof(hAddNodeProgressInterval) === 'undefined') && (hAddNodeProgressInterval != null))
			hAddNodeProgressInterval = window.clearInterval(hAddNodeProgressInterval);
		if(!(typeof(hNodeListInterval) === 'undefined') && (hNodeListInterval != null))
			hNodeListInterval = window.clearInterval(hNodeListInterval);
		if(!(typeof(refreshInterval_NDD) === 'undefined') && (refreshInterval_NDD != null))
			refreshInterval_NDD = window.clearInterval(refreshInterval_NDD);
		if(!(typeof(refreshInterval_JobMonitoring) === 'undefined') && (refreshInterval_JobMonitoring != null))
			refreshInterval_JobMonitoring = window.clearInterval(refreshInterval_JobMonitoring);
		if(!(typeof(refreshInterval_JobDetails) === 'undefined') && (refreshInterval_JobDetails != null))
			refreshInterval_JobDetails = window.clearInterval(refreshInterval_JobDetails);
		if(!(typeof(clusterDeployInterval) === 'undefined') && (clusterDeployInterval != null))
			clusterDeployInterval = window.clearInterval(clusterDeployInterval);
		if(!(typeof(nodeStatusDeployInterval) === 'undefined') && (nodeStatusDeployInterval != null))
			nodeStatusDeployInterval = window.clearInterval(nodeStatusDeployInterval);
		if(!(typeof(clusterDeploymentInterval) === 'undefined') && (clusterDeploymentInterval != null))
			clusterDeploymentInterval = window.clearInterval(clusterDeploymentInterval);
		if(!(typeof(nodeStatusDeploymentInterval) === 'undefined') && (nodeStatusDeploymentInterval != null))
			nodeStatusDeploymentInterval = window.clearInterval(nodeStatusDeploymentInterval);
		if(!(typeof(nodeDeploymentInterval) === 'undefined') && (nodeDeploymentInterval != null))
			nodeDeploymentInterval = window.clearInterval(nodeDeploymentInterval);
		if(!(typeof(addNodeChildInterval) === 'undefined') && (addNodeChildInterval != null))
			addNodeChildInterval = window.clearInterval(addNodeChildInterval);
	},
	dashboardRefreshCall : function(){
		//com.impetus.ankush.dashboard.clearAllAutorefreshInterval();
		//autoRefreshCallInterval = setInterval("com.impetus.ankush.dashboard.createTile()",15000);
	},
	// this method will get user name will be shown in profile pop up box
	getUserId : function() {
		url = baseUrl + '/user/userid';
		var userId = null;
		$.ajax({
			type : "GET",
			contentType : 'application/json',
			dataType : "json",
			url : url,
			success : function(data) {
				userId = data.output.username;
				$('#userId').text(userId);
			},
			error : function(data) {

			}
		});
	},
	// this method will highlight dashboard icon
	makeDashboardSelected : function() {
		$('ul.left-nav li').each(function() {
			$(this).removeClass('selected');
		});
		$('#dashboardSelected').addClass('selected');
	},
	// this method will highlight appropriate bar icon
	makeBarSelected : function(barIconId) {
		$('ul.left-nav li').each(function() {
			$(this).removeClass('selected');
		});
		$('#' + barIconId).addClass('selected');
	},
	// this method will remove all dialogs div
	removeChildDialog : function() {
		$('#actionDialog').remove();
		$('#actionErrorDialog').remove();
		$('#migrateNodeDialog').remove();
		$('#increaseRepFactorDialog').remove();
		$('#deleteClusterDialog').remove();
		$("#deleteClusterDialogHadoopMonitor").remove();
		$("#invalidAddNodeOperation").remove();
		$("#fileSizeExceed").remove();
		$("#div_RequestSuccess_NodeDD").remove();
		$("#deleteNodeDialogHadoop").remove();
		$('#deleteClusterDialogHadoopMonitor').remove();
		$("#div_addParameter").remove();
		$("#deleteParameterDialogHadoop").remove();
		$('#div_RequestSuccess').remove();
		$('#div_Request_JobMonitoring').remove();
		$("#deleteQueueDialog").remove();
		$("#div_RequestPlaced_JobScheduler").remove();
		$('#deleteClusterDialogHadoop').remove();
	},
	// General method for showing error or errors
	getProperNounForError : function(errorCount) {
		var errorCountMsg = errorCount + " Errors";
		if (errorCount == 1) {
			errorCountMsg = errorCount + " Error";
		}
		return errorCountMsg;
	}
	
};
