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

var tileData = null;
var hadoopServiceStatusTable = null;
var IsAutoRefreshON_NDD = false;
var refreshInterval_NDD = null;
var refreshTimeInterval_NDD = 60000;
var currentClusterId = null;
var currentNodeIp = null;
var currentNodeId = null;
var currentStartTime = null;

com.impetus.ankush.hadoopNodeDrillDown={
		firstTimeLoad : true,

		// function used to start autorefresh call to update node details after fixed scheduled interval
		autoRefresh_NDD : function(functionName) {
			refreshInterval_NDD = setInterval(function() {
				eval(functionName);
			}, refreshTimeInterval_NDD);
		},

		// function used to inilitialze glo
		initializeCurrentVariables : function(clusterId, nodeId, nodeIp) {
			currentClusterId = clusterId;
			currentNodeIp = nodeIp;
			currentNodeId = nodeId;
		},

		// function used to hide all error-box and calls a function to show service status details
		initHadoopServiceStatus:function() {
			$("#errorBtn_NodeDrillDown").text("");
			$('#errorBtn_NodeDrillDown').css("display", "none");
			$("#error-div-NodeDrillDown").css("display", "none");
			$("#popover-content_NodeDrillDown").text("");
			$("#popover-content_NodeDrillDown").css("display", "none");
			com.impetus.ankush.hadoopNodeDrillDown.fillNodeServiceStatus();
		},

		// function used to show service status details
		fillNodeServiceStatus : function() {
			if(hadoopServiceStatusTable == null) {
				$("#hadoopNodeServiceDetail").dataTable().fnDestroy();
				hadoopServiceStatusTable=$('#hadoopNodeServiceDetail').dataTable({
					"bJQueryUI" : false,
					"bPaginate" : false,
					"bLengthChange" : true,
					"bFilter" : false,
					"bSort" : false,
					"bInfo" : false,
					"bAutoWidth" : false,
					"sPaginationType": "full_numbers",
					"bAutoWidth" : false, 
					"fnRowCallback" : function(nRow, aData, iDisplayIndex,
							iDisplayIndexFull) {
						$(nRow).attr('id', 'serviceRow-' + iDisplayIndex);
					}
				});
			}
			hadoopServiceStatusTable.fnClearTable();
			var serviceStatusUrl=baseUrl+'/monitor/' + currentClusterId +'/services?ip=' + currentNodeIp;
			var serviceStatusData=com.impetus.ankush.placeAjaxCall(serviceStatusUrl,'GET',false);
			var tableData = [];
			var iIndex=0;
			for(var service in serviceStatusData.output ){
				if(service!="error"){
					var rowData = [];
					var checkBox_Service = '<input type="checkbox" id="checkboxService-'+ iIndex +'">'; 
					var serviceName = '<span style="font-weight:bold;" id="serviceName-'+ iIndex +'">'+service+'</span>';
					var setServiceStatus = 'Running';
					if(!serviceStatusData.output[service]) {
						setServiceStatus = 'Stopped';
					}
					var status = '<span id="serviceStatus-'+ iIndex +'" style="font-weight:bold;">'+setServiceStatus+'</span>';

					if(service!="status"){
						iIndex++;
						rowData.push(checkBox_Service);
						rowData.push(serviceName);
						rowData.push(status);
						tableData.push(rowData);
					}
				}
			}
			hadoopServiceStatusTable.fnAddData(tableData);
			var serviceCount = hadoopServiceStatusTable.fnGetData().length;
			for(var i = 0; i < serviceCount; i++) {
				if($("#serviceStatus-" + i).text() == "Stopped") {
					$("#serviceRow-" + i).addClass('error-row');	
				}
			}
		},

		// function used to place request for starting / stopping service 
		hadoopServiceAction : function(action){
			var serviceurl=baseUrl+"/manage/" + currentClusterId + "/manage";
			var serviceData={};
			serviceData.ip = currentNodeIp;
			serviceData.action = action;
			serviceData.services = new Array();
			var serviceCount = hadoopServiceStatusTable.fnGetData().length;
			var serviceStatus = "Running";
			if(action == 'start' || action == 'start all') 
				serviceStatus = "Stopped";
			if(action == 'start' || action == 'stop') {
				for(var i=0;i<serviceCount;i++){
					if($('#checkboxService-'+i).attr("checked") && 
							$('#serviceStatus-'+i).text() == serviceStatus) {
						serviceData.services.push($('#serviceName-'+i).text());
					}
				}
			}
			else if(action == 'start all' || action == 'stop all') {
				serviceData.action = (action.split(' '))[0];
				for(var i=0;i<serviceCount;i++){
					if($('#serviceStatus-'+i).text() == serviceStatus) {
						serviceData.services.push($('#serviceName-'+i).text());
					}
				}
			}
			if(serviceData.services.length > 0) {
				$.ajax({
					'type' : 'POST',
					'url' : serviceurl,
					'contentType' : 'application/json',
					'data' : JSON.stringify(serviceData),
					"async": false,
					'dataType' : 'json',
					'success' : function(result) {
						if (result.output == "OK"){
							$("#div_RequestSuccess_NodeDD").modal('show');
							$('.ui-dialog-titlebar').hide();
						}
						else {
							com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 
									'popover-content_NodeDrillDown', 'error-div-NodeDrillDown', 'errorBtn_NodeDrillDown');
							return;
						}
					}
				});	
			}
		},

		// function used to close service placed dialog
		closeRequestDialog_RequestSuccess : function() {
			$("#div_RequestSuccess_NodeDD").modal('hide');
			$("#checkHead_NodeService").attr("checked", false);
			com.impetus.ankush.hadoopNodeDrillDown.checkAll_NodeService();
		},

		// function used to close invalid request dialog
		closeRequestDialog_RequestInvalid : function() {
			$("#div_RequestInvalid_NodeDD").modal('hide');
			$("#checkHead_NodeService").attr("checked", false);
			com.impetus.ankush.hadoopNodeDrillDown.checkAll_NodeService();
		},

		// function used to check all the services on click of header-check box
		checkAll_NodeService : function() {
			var serviceCount = hadoopServiceStatusTable.fnGetData().length;
			var flag = false;
			if($("#checkHead_NodeService").attr("checked"))
				flag = true;

			for(var i = 0; i < serviceCount; i++)
				$("#checkboxService-"+i).attr("checked", flag);
		},

		// function used to call functions to show node level tiles and utilization graphs
		loadPageContent_TileAndGraph : function(startTime) {
			com.impetus.ankush.hadoopNodeDrillDown.nodeTileCreate();
			com.impetus.ankush.hadoopNodeDrillDown.drawGraph_JobMonitoring(startTime);
		},

		// function used to refresh page content after a fixed interval
		refreshPageContent : function(startTime) {
			if(document.getElementById('div_HadoopNodeDrillDown') == null) {
				com.impetus.ankush.hadoopNodeDrillDown.stopAutoRefreshCalls();
				return;				
			}
			com.impetus.ankush.hadoopNodeDrillDown.fillNodeServiceStatus();
			com.impetus.ankush.hadoopNodeDrillDown.nodeTileCreate();
			com.impetus.ankush.hadoopNodeDrillDown.drawGraph_JobMonitoring(currentStartTime);
		},

		// function called on page load to show node details
		pageLoadFunction_NodeDrillDown : function(clusterId, nodeId, nodeIp) {

			com.impetus.ankush.hadoopNodeDrillDown.stopAutoRefreshCalls();
			com.impetus.ankush.hadoopNodeDrillDown.initializeCurrentVariables(clusterId, nodeId, nodeIp);
			com.impetus.ankush.hadoopNodeDrillDown.loadPageContent_TileAndGraph('lasthour');
			com.impetus.ankush.hadoopNodeDrillDown.initHadoopServiceStatus();
			$("#div_RequestSuccess_NodeDD").appendTo('body');
			com.impetus.ankush.hadoopNodeDrillDown.autoRefresh_NDD("com.impetus.ankush.hadoopNodeDrillDown.refreshPageContent(\"lasthour\");");
		},

		// function used to create node-level tile
		nodeTileCreate : function() {
			var tileUrl = baseUrl + '/monitor/'+ currentClusterId + '/nodetiles?ip=' + currentNodeIp;
			tileData = com.impetus.ankush.placeAjaxCall(tileUrl,"GET",false);
			$('#all_tilesNodes_NDD').empty();
			var $wrapperAll = $('#all_tilesNodes_NDD'), $boxes, innerHtml, clustorJson = "data.json", clusterTilesInfo, boxMaker = {}, itr = 1, tileClass, line1Class;
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
				tiles = [ runningTiles, warningTiles, errorTiles];
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
								};
								break;
							}
							innerHtml += '</div>';
							$boxes = $(boxMaker.makeBoxes(innerHtml, tileClass, tileAction));
							$wrapperAll.prepend($boxes).masonry();

						}
					}

				}
			}
		},

		// function used to open a page containing node's Swap or OS details
		showSwapOrOSDetails : function(nodeId, jspPage) {
			var PageTitle = 'OS Details';
			if(jspPage == 'nodeSwapDetails') {
				PageTitle = 'Swap Details';
			}
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/' + jspPage + "/" + nodeId,
				method : 'get',
				title : PageTitle,
				params : {}
			});
		},

		// function used to load node data
		loadNodeData : function(nodeId , action) {
			var url = baseUrl + "/monitor/node/" + nodeId + "/" + action;
			var resultData = com.impetus.ankush.placeAjaxCall(url, 'GET', false);
			$.each(resultData.output, function(key, value) {
				$("#" + key).html(value);
			});
		},

		// function used to stop autorefresh calls
		stopAutoRefreshCalls : function() {
			IsAutoRefreshON_NDD = false;
			refreshInterval_NDD = window.clearInterval(refreshInterval_NDD);
		},

		// function used to draw node level graphs
		drawGraph_JobMonitoring : function(startTime){
			url = baseUrl + '/monitor/' + currentClusterId + '/nodegraph?ip='+ currentNodeIp +'&startTime='+startTime;
			currentStartTime = startTime;
			com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result){			
				if(result.output.status){
					result = com.impetus.ankush.hadoopNodeDrillDown.normalizeData(result.output.json);
					$('#nodeDrillDown_Graph svg').css('height', '500px');
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
						var chart = nv.models.cumulativeLineChart()
						.x(function(d) { return d[0]; }) 
						.y(function(d) { return d[1]; })
						.clipEdge(true);

						chart.showControls(false);
						chart.xAxis
						.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); })
						.ticks(10);

						chart.yAxis
						.tickFormat(d3.format(',.2f'))
						.ticks(10);

						var svg = d3.select('#nodeDrillDown_Graph svg')
						.datum(result)
						.transition().duration(500).call(chart);
						svg.attr("width","100%");
						svg.attr("display","block");

						nv.utils.windowResize(chart.update);

					});
				}
				else {
					$('#nodeDrillDown_Graph').empty();
					$('#nodeDrillDown_Graph').append('Sorry, Unable to fetch Graph information for node');
					$('#graphButtonGroup_JobMonitoring').css("display", "none");
				} 
			});	
		},

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
};
