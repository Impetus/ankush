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

//auto refresh variable for tiles
//var is_autorefresh_individualNode = null;
//auto refresh variable for services
//var is_autorefresh_services = null;
//auto refresh variable for graphs
//var is_autorefresh_graphs = null;
// data table variable for services datatable
var nodeServiceDetail = null;
var utilizationTrend = null;
var preserve_autorefresh_time = 'lasthour';
com.impetus.ankush.commonMonitoring.nodeDrillDown = function(nodeIndex){
			$('#content-panel')
				.sectionSlider(
						'addChildPanel',
						{
							current : 'login-panel',
							url : baseUrl
							+ '/commonMonitoring/nodeDrillDown',
							method : 'get',
							previousCallBack : "com.impetus.ankush.commonMonitoring.removeNodesDrillDownPageLoad(1);",
							title : com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp,
							tooltipTitle : 'Nodes',
							params : {
							nodeIndex :	nodeIndex,
							clusterTechnology :	com.impetus.ankush.commonMonitoring.clusterTechnology,
							hybridTechnology :	com.impetus.ankush.commonMonitoring.hybridTechnology
							},
							callback : function(){
								$('#commonNodeDrillDown_NodeIP').html(com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp);
								com.impetus.ankush.commonMonitoring.pageLoadFunction_NodeDrillDown(nodeIndex);
							}
						});
		};
		//this function will load node drill down page
		com.impetus.ankush.commonMonitoring.pageLoadFunction_NodeDrillDown = function(nodeIndex) {
			com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring('lasthour',nodeIndex);
			com.impetus.ankush.commonMonitoring.createTilesForIndividualNode(nodeIndex);
			com.impetus.ankush.commonMonitoring.initServiceStatus(nodeIndex);
			console.log(com.impetus.ankush.commonMonitoring.nodesData);
		};
		//this function will create tile on node drill down page
		com.impetus.ankush.commonMonitoring.createTilesForIndividualNode = function(nodeIndex){
			if(nodeIndex == undefined)
				return;
			var tileUrl = baseUrl + '/monitor/'+ com.impetus.ankush.commonMonitoring.clusterId + '/nodetiles?ip=' + com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp;
			 com.impetus.ankush.placeAjaxCall(tileUrl,"GET",true, null, function(tileData){
				 	var clusterTiles = {} ;
					if(tileData.output.status == false)
						return;
					else{
						clusterTiles = com.impetus.ankush.tileReordring(tileData.output.tiles);
					}	
					//this var will set whether tile is 2*2 or 1*2
					var tileFlag = false;
					for(var j = 0 ; j < clusterTiles.length ; j++){
						if(clusterTiles[j].tileType == 'big_text'){
							tileFlag = true;
							break;
						}
					}
					$('#tilesNodes_NDD').empty();
					var tileVar = {
							'leftCss' : 0,
							'topCss' : 0,
							'lineCounter' : 1,
							'tyleType' : 'bigTile',
					}; 
					var tile = document.getElementById('tilesNodes_NDD');
					for(var i = 0 ; i < clusterTiles.length ; i++){
						if((tileVar.leftCss+200) > ($('#tilesNodes_NDD').width())){
							tileVar.leftCss = 0;
							tileVar.topCss = tileVar.lineCounter*200;
							tileVar.lineCounter++;
						}
						if((i == 0) && ((clusterTiles[i].tileType == 'small_text') || (clusterTiles[i].tileType == undefined)))
							tileVar.tyleType = 'smallTileOdd';
						com.impetus.ankush.createTyleUsingType(tileVar,i,clusterTiles,tile,'node-id-for-tile');
					}
					if(tileFlag == false){
						$('#tilesNodes_NDD').masonry('destroy');
						$('#tilesNodes_NDD').masonry({ 
							itemSelector : '.item',
							columnWidth : 100,
							isAnimated : true
						});
					}else
						setTimeout(function(){$('#tilesNodes_NDD').css('height',(tileVar.lineCounter)*200+'px');},50);
					$('.clip').tooltip();
			});
		};
		com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring = function(startTime,nodeIndex){
			if(lineChartToReDraw == null){
				var url = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/nodegraph?ip='+ com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp+'&startTime='+startTime;
				currentStartTime = startTime;
				com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result){			
					if(result.output.status){
						result = com.impetus.ankush.commonMonitoring.normalizeData(result.output.json);
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
						nv.addGraph(function() {
							lineChartToReDraw = nv.models.lineChart()
								.x(function(d) { return d[0]; }) 
							    .y(function(d) { return d[1]; })
							    .clipEdge(true);
							lineChartToReDraw.margin().left = 60;
							lineChartToReDraw.margin().right = 40;
							var maxValue = 0;
							$.each(result, function(key, value){
								var keyMaxVal = Math.max.apply(Math, value.yvalues);
								if(keyMaxVal > maxValue) {
									maxValue = keyMaxVal;
								}
							});
							var maxDecimals = com.impetus.ankush.commonMonitoring.countDecimals(maxValue);
							lineChartToReDraw.xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
							lineChartToReDraw.yAxis.ticks(10).tickFormat(function(d){return d3.format(".1f")(d);});
							var svg = d3.select('#nodeDrillDown_Graph svg')
								.attr('height',300)
								.data([result])
								.transition().duration(1000).call(lineChartToReDraw);
							svg.attr('width','90%');
							svg.attr('display','block');
							nv.utils.windowResize(lineChartToReDraw.update);
					   });
				}
					else {
						$('#nodeDrillDown_Graph').empty();
						$('#nodeDrillDown_Graph').append('Sorry, Unable to fetch Graph information for node');
						$('#graphButtonGroup_JobMonitoring').css("display", "none");
					} 
			});	
		}else{
			com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring_autorefresh(startTime,nodeIndex);
		}
	  };
	  com.impetus.ankush.commonMonitoring.drawGraph_JobMonitoring_autorefresh = function(startTime,nodeIndex){
		  var url = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/nodegraph?ip='+ com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp+'&startTime='+startTime;
			currentStartTime = startTime;
			com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result){			
				if(result.output.status){
					result = com.impetus.ankush.commonMonitoring.normalizeData(result.output.json);
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
				var maxValue = 0;
				$.each(result, function(key, value){
					var keyMaxVal = Math.max.apply(Math, value.yvalues);
					if(keyMaxVal > maxValue) {
						maxValue = keyMaxVal;
					}
				});
				var maxDecimals = com.impetus.ankush.commonMonitoring.countDecimals(maxValue);
				lineChartToReDraw.xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
				lineChartToReDraw.yAxis.ticks(10).tickFormat(function(d){return d3.format(".1f")(d);});
				var svg = d3.select('#nodeDrillDown_Graph svg')
							.attr('height',300)
							.data([result])
							.transition().duration(1000).call(lineChartToReDraw);
						svg.attr('width','90%');
						svg.attr('display','block');
						nv.utils.windowResize(lineChartToReDraw.update);
			}
				else {
					$('#nodeDrillDown_Graph').empty();
					$('#nodeDrillDown_Graph').append('Sorry, Unable to fetch Graph information for node');
					$('#graphButtonGroup_JobMonitoring').css("display", "none");
				} 
		});	
	  };
	  com.impetus.ankush.commonMonitoring.initServiceStatus = function(nodeIndex){
		    $("#errorBtn_NodeDrillDown").text("");
			$('#errorBtn_NodeDrillDown').css("display", "none");
			$("#error-div-NodeDrillDown").css("display", "none");
			$("#popover-content_NodeDrillDown").text("");
			$("#popover-content_NodeDrillDown").css("display", "none");
			com.impetus.ankush.commonMonitoring.fillNodeServiceStatus(nodeIndex);
	  };
	  // this function will fill service data table in individual node page
	  com.impetus.ankush.commonMonitoring.fillNodeServiceStatus = function(nodeIndex){
		 // $("#nodeServiceDetail").dataTable().fnDestroy();
		
		var serviceStatusUrl=baseUrl+'/monitor/' +com.impetus.ankush.commonMonitoring.clusterId +'/services?ip=' + com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp;
		com.impetus.ankush.placeAjaxCall(serviceStatusUrl,'GET',true,null,function(serviceStatusData){
			if(serviceStatusData.output.status == true){
				if(nodeServiceDetail != null)
					nodeServiceDetail.fnClearTable(); 
				var tableData = [];
				var iIndex=0;
				for(var service in serviceStatusData.output ){
					if(service!="error"){
						if(service != "status"){
							var rowData = [];
							var serviceName = '<span style="font-weight:bold;" id="serviceName-'+ iIndex +'">'+service+'</span>';
							var status = '<div id="status'+iIndex+'"></div>';
							var checkboxes = "<input type='checkbox' id='serviceCheckboxes"+iIndex+"' class='checkedServices' onclick='com.impetus.ankush.headerCheckedOrNot(\"checkedServices\",\"servicesHeadCheckbox\")'/>";
							var actions = '<div id="actions'+iIndex+'"></div>';
							nodeServiceDetail.fnAddData([
							                             checkboxes,
							                             serviceName,
							                             status,
							                             actions
							                             ]);
							if(serviceStatusData.output[service] == true){
								$('#status'+iIndex).html('<svg height="16" width="16" style="margin-top:2px;float:left;"><circle cx="8" cy="8" r="6" style="stroke:#77933C ! important;fill:#77933C;"></circle></svg><div style="color:#77933C;margin-left:5px;float:left">Running</div' );
								$('#actions'+iIndex).html('<button class="btn" id="serviceButton'+iIndex+'" onclick="com.impetus.ankush.commonMonitoring.serviceAction(\'stop\','+nodeIndex+',\''+service+'\');" style="height:20px;">Stop</button>');
							}
							else{
								$('#status'+iIndex).html('<svg height="16" width="16" style="margin-top:2px;float:left;"><circle cx="8" cy="8" r="6" style="stroke:#94000C ! important;fill:#94000C;"/></svg><div style="color:#94000C;margin-left:5px;float:left">Stopped</div>' );
								$('#actions'+iIndex).html('<button class="btn" id="serviceButton'+iIndex+'" onclick="com.impetus.ankush.commonMonitoring.serviceAction(\'start\','+nodeIndex+',\''+service+'\');" style="height:20px;background-color:#85B71E;background-image:none;">Start</button>');
							}
							iIndex++;
						}	
					}
				}
				if($("#servicesHeadCheckbox").is(':checked'))
					$('.checkedServices').attr('checked',true);
			}else{
				nodeServiceDetail.fnSettings().oLanguage.sEmptyTable = serviceStatusData.output.error[0];
				nodeServiceDetail.fnClearTable();
			}
		});
		
	  };
		 com.impetus.ankush.commonMonitoring.serviceAction = function(action,nodeIndex,serviceName){
			 	$("#error-div-NodeDrillDown").css("display", "none");
				$('#errorBtn_NodeDrillDown').css("display", "none");
			 	var serviceData={};
			 	serviceData.services = new Array();
			 	serviceData.action = action;
			 	if((action == 'startChecked') || (action == 'stopChecked')){
			 		serviceData.action = action.split('Checked')[0];
			 		$('.checkedServices').each(function(){
			 			if($(this).is(':checked')){
				 			var serviceItrate = $(this).parent().next().text();
				 			serviceData.services.push(serviceItrate);
			 			}
			 		});
			 	}
			 	else
			 		serviceData.services.push(serviceName);
			    serviceData.ip = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp;
				com.impetus.ankush.commonMonitoring.servicePostRequest(serviceData);
		};
		
		com.impetus.ankush.commonMonitoring.servicePostRequest = function(serviceData){
			var serviceurl=baseUrl+"/manage/" + com.impetus.ankush.commonMonitoring.clusterId + "/manage";
			if(serviceData.services.length > 0) {
				$.ajax({
					'type' : 'POST',
					'url' : serviceurl,
					'contentType' : 'application/json',
					'data' : JSON.stringify(serviceData),
					"async": false,
					'dataType' : 'json',
					'success' : function(result) {
						if (result.output.status){
							$("#div_RequestSuccess_NodeServices").appendTo('body').modal('show');
							$('.ui-dialog-titlebar').hide();
						}
						else {
							com.impetus.ankush.commonMonitoring.errorCount_NodeDrillDown = 0;
							$("#error-div-NodeDrillDown").empty();
							$("#popover-content_NodeDrillDown").empty();
							
							for(var i = 0; i < result.output.error.length; i++) {
								com.impetus.ankush.commonMonitoring.errorCount_NodeDrillDown++;
								$("#error-div-NodeDrillDown").append("<div class='errorLineDiv'><a href='#'>"+com.impetus.ankush.commonMonitoring.errorCount_NodeDrillDown+". "+result.output.error[i]+".</a></div>");
								$("#popover-content_NodeDrillDown").append('<a href="#">'+result.output.error[i]+'</a></br>');
							}
							$('#errorBtn_NodeDrillDown').text(com.impetus.ankush.commonMonitoring.errorCount_NodeDrillDown + " Error");
							$("#error-div-NodeDrillDown").css("display", "block");
							$('#errorBtn_NodeDrillDown').css("display", "block");
							$("#popover-content_NodeDrillDown").css("display", "block");
						}
					}
				});	
			}
			else{
				com.impetus.ankush.commonMonitoring.errorCount_NodeDrillDown = 1;
				$("#error-div-NodeDrillDown").empty();
				$('#errorBtn_NodeDrillDown').text(com.impetus.ankush.commonMonitoring.errorCount_NodeDrillDown + " Error");
				$("#error-div-NodeDrillDown").append("<div class='errorLineDiv'><a href='#'>1. Select atleast one service.</a></div>");
				$('#error-div-NodeDrillDown').css("display", "block");
				$('#errorBtn_NodeDrillDown').css("display", "block");
				$("#popover-content_NodeDrillDown").css("display", "block");
			}
		};
		// this function will select all services to stop or start
		com.impetus.ankush.commonMonitoring.checkAll_NodeService = function() {
			var serviceCount = nodeServiceDetail.fnGetData().length;
			var flag = false;
			if($("#checkHead_NodeService").attr("checked"))
				flag = true;
			for(var i = 0; i < serviceCount; i++)
				$("#checkboxService-"+i).attr("checked", flag);
		};
		//this function will do the task necessary on slider child out
		com.impetus.ankush.commonMonitoring.removeChildPreviousNodesDrillDownPageLoad = function(removeDialogflag){
			$('#div_RequestSuccess_NodeServices').remove();
			if(removeDialogflag == 1){
				if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Cassandra')
					$('#div_RequestSuccess_Action').remove();
			}
		};
		com.impetus.ankush.commonMonitoring.removeNodesDrillDownPageLoad = function(removeDialogflag){
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Cassandra')
				$('#confirmationDialogsNode').remove();
			if($.data(document, "panels").children.length == 1)
				com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();
			else{
				com.impetus.ankush.commonMonitoring.nodeDetails()
				if(removeDialogflag == 1)
					$('#div_RequestSuccess_NodeServices').remove();
			}
		};
		

	
