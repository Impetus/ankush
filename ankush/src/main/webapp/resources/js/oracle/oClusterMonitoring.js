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

var loadingImage = '<img src="' + baseUrl + '/public/images/loading.gif"  width="15px" height="15px"/>';
var right_arrow = '<img src= " ' + baseUrl + '/public/images/icon-chevron-right.png"/>';
var okIcon = '<img src="' + baseUrl + '/public/images/icon-ok.png"  width="15px" height="15px"/>';
var crossIcon = '<img src="' + baseUrl + '/public/images/icon-cross.png"  width="15px" height="15px"/>';
var planHistoryTable = null;
var planHistoryChildTable = null;
var oAddNodeTable = null;
var detectedNodes = null;
var nodeAddInterval = null;
var nodeIpProgressTable = null;
var oClusterAddNodeTable = null;
var monitorClusterId = null;
var currentClusterName = null;
var defaultValuesNode = null;
var shardNodeData = null;
var storageNodeData = null;
var adminParamJson = null;
var repNodeParamJson = null;
var policyParamJson = null;
var policyParamResult = null;
var eventTable = null;
var storeEventTable = null;
var storageNodeChildParamJson = null;
var repNodeChildParamJson = null;
var shardNodeTable = null;
var storageNodeTable = null;
var nodeProgressDetailResult = null;
var clusterDetailResult = null;
var eventResult = null;
var redistributeTimeStamp = null;
var redistributeId = null;
var redistributeLogInterval = null;
var rebalanceLastTimeStamp = null;
var rebalanceLogInterval = null;
var populate = {};
var lastLogAddNode = 0;
var deployedNodesCount = 0;
var auditTrailJson = null;
var incRepFactorLastTimeStamp = null;
var increaseRepFactorLogInterval = null;
var alertTable = null;
var alertResult = null;
var commonActionId = null;
var commonActionTimestamp = null;
var monitorPageLoadTimeInterval = null;
var ipAdd;
var addNodeChildInterval = null;
var addNodeChildProgressData = null;
var hostNodeIp = null;
var monitorPageInterval = null;
var shardDrillTable = null;
var storeEventTimeStamp = null;
var refreshFlag = false;
var overviewData = null;
var pageInterval = 60000;
var logInterval = 8000;
var tileId = '';
var graphDrawInterval = null;
var graphInitialTime = 'lasthour';
var graphInterval = 60000;
var colorArray = new Array();
colorArray.push('green');
colorArray.push('red');
colorArray.push('orange');
colorArray.push('gray');
var inspectNodeData = {};
/* Function to convert camelcase into normal letters */
function camelCaseKey(key) {
	return key.split(/(?=[A-Z])/).map(function(p) {
		return p.charAt(0).toUpperCase() + p.slice(1);
	}).join(' ');
};

/* Function to convert timestamp into date format */
function getDateTime(txtDate) {
	dateTime = 0;
	if ($(txtDate).val().trim().length > 0) {
		var strDate = $(txtDate).val();
		strDate = strDate.split("-").join("/");
		dateTime = new Date(strDate).getTime();
	}
	return dateTime;
}
com.impetus.ankush.oClusterMonitoring = {
		parentPageCallRermove:function(){
			monitorPageInterval = window.clearInterval(monitorPageInterval);
	},
	/*
	 * Function to be called on monitoring page load for populating storage node
	 * table,replication node table, tile creation and graph ploting
	 */
	monitorPageLoad : function(clusterId, clusterName) {
		monitorClusterId = null;
		currentClusterName = null;
		monitorClusterId = clusterId;
		currentClusterName = clusterName;
		$('#clusterNameHead').html('').text(clusterName);
		var url = baseUrl + '/monitor/' + clusterId + '/clusteroverview';
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			if(result.output.status){	
				overviewData = result;
				com.impetus.ankush.oClusterMonitoring.initializeShardNodeTable();
				com.impetus.ankush.oClusterMonitoring.initializeStorageNodeTable();
				com.impetus.ankush.oClusterMonitoring.oracleMonitoringTile(clusterId, 'tiles', 'allTilesOracle');
				com.impetus.ankush.oClusterMonitoring.plotTree(overviewData,'shardGraphDiv');
			}
		
		});
	},
	/* Function for initializing auto-refresh of monitoring page */
	initMonitorPageLoad : function(clusterId, clusterName) {
		if (monitorPageInterval != null) {
			monitorPageInterval = window.clearInterval(monitorPageInterval);
			monitorPageInterval = null;
		} else {
			monitorPageInterval = setInterval(
					'com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(\'' + clusterId + '\',\'' + clusterName + '\');',pageInterval);
		}
	},

	/* Function to be called on auto-refresh of monitoring page */
	monitorPageRefresh : function(clusterId, clusterName) {
		$('#clusterNameHead').html('').text(currentClusterName);
		monitorClusterId = clusterId;
		var url = baseUrl + '/monitor/' + clusterId + '/clusteroverview';
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null,function(result) {
					if (document.getElementById("clusterNameHead") == null) {
						monitorPageInterval = window.clearInterval(monitorPageInterval);
					} else {
						if(result.output.status){
						overviewData = result;
						com.impetus.ankush.oClusterMonitoring.initializeShardNodeTable();
						com.impetus.ankush.oClusterMonitoring.initializeStorageNodeTable();
						com.impetus.ankush.oClusterMonitoring.oracleMonitoringTile(monitorClusterId,'tiles', 'allTilesOracle');
						com.impetus.ankush.oClusterMonitoring.plotTree(overviewData, 'shardGraphDiv');
						}
					}
				});
	},

	/* Function for changing css of tooltip in case of error */
	tooltipMsgChange : function(id, msg) {
		$("#" + id).hover(function() {
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
		});
		$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
				'fixTitle');

	},

	/* Function for changing css of tooltip to original */
	tooltipOriginal : function(id, msg) {
		$("#" + id).hover(function() {
			$('.tooltip-inner').css({
				'max-width' : '250px',
				'padding' : '8px',
				'color' : '#686A6C',
				'text-align' : 'center',
				'text-decoration' : 'none',
				'border' : "1px solid #E7EFFF",
				'-webkit-box-shadow' : '0px',
				'-moz-box-shadow' : ' 0px;',
				'box-shadow' : '0px;',
				'background-color' : '#E7EFFF',
				'-webkit-border-radius' : '4px',
				'-moz-border-radius' : '4px',
				'border-radius' : '4px',
				'font-family' : 'Franklin Gothic Book',
				'font-size' : '14px',
			});
			$('.tooltip.right').css('border-right-color', 'E7EFFF');
			$(".tooltip.right .tooltip-arrow").css({
				"top" : "50%",
				"left" : "0",
				"margin-top" : "-5px",
				"border-right-color" : "#E7EFFF",
				"border-width" : "5px 5px 5px 0"
			});
		});
		$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
				'fixTitle');
	},
	/* Function for focusing particular div on click of a link */
	focusDiv : function(id) {
		$('#' + id).focus().tooltip('hide').addClass('error-box');
	},
	/* Function for scrolling page to top on click */
	focusError : function() {
		$(window).scrollTop(0);
	},
	/* Function for making tile on monitoring page */
	oracleMonitoringTile : function(clusterId, urlPart, divId) {
		var monitorUrl = baseUrl + '/monitor/' + clusterId + '/' + urlPart;
		var monitorData = overviewData.output;
		var clusterTiles = {};
		if (!overviewData.output.status) {
			return;
		}
		else{
			clusterTiles = com.impetus.ankush.tileReordring(overviewData.output.tiles);
		}
		
		$('#' + divId).html('');
		/*
		 * var $wrapperError = $('#'+divIdError), $wrapperOther =
		 * $('#'+divIdRunning),
		 */

		var tile = document.getElementById(divId);
		for(var i = 0 ; i < clusterTiles.length ; i++){
			var tileId = 0;
			if (clusterTiles[i].data != null) {
						tileId = clusterTiles[i].data.tileid;
					}
			var tileAction = {
								'actionName' : clusterTiles[i].line1,
								'action' : clusterTiles[i].url,
								'data' : tileId,
							};
			switch (clusterTiles[i].status) {
			case 'Error':
				tile.innerHTML += '<div class="item grid-1c2text errorbox" id="oracleid-'+i+'" onclick="com.impetus.ankush.oClusterMonitoring.actionFunction(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\');">'+
				'<div class="tile-1c2text thumbnail">'+
				'<div class="redTitle">'+
				'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
				'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
				break;
			case 'Critical':
				tile.innerHTML += '<div class="item grid-1c2text errorbox" id="oracleid-'+i+'" onclick="com.impetus.ankush.oClusterMonitoring.actionFunction(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\');">'+
				'<div class="tile-1c2text thumbnail">'+
				'<div class="redTitle">'+
				'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
				'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
				break;
			case 'Warning':
				tile.innerHTML += '<div class="item grid-1c2text warningbox" id="oracleid-'+i+'" onclick="com.impetus.ankush.oClusterMonitoring.actionFunction(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\');">'+
				'<div class="tile-1c2text thumbnail">'+
				'<div class="yellowTitle">'+
				'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
				'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
				break;
			case 'Normal':
				tile.innerHTML += '<div class="item grid-1c2text infobox" id="oracleid-'+i+'" onclick="com.impetus.ankush.oClusterMonitoring.actionFunction(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\');">'+
				'<div class="tile-1c2text thumbnail">'+
				'<div class="greenTitle">'+
				'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
				'<div class="descTitle">'+clusterTiles[i].line2+'</div></div></div>';
				break;
			}
			if (tileAction.action != null) {
				var tileClickCursor = document.getElementById('oracleid-'+i);
				tileClickCursor.style.cursor = "pointer";
			}	
		}
		$('.clip').tooltip();
		$('#' + divId).masonry({ itemSelector : '.item',
			columnWidth : 100 });
		
	},
	/* Function for uploading file */
	uploadFile : function(uploadUrl, fileId, callback, context) {
		$.ajaxFileUpload({
			url : uploadUrl,
			secureuri : false,
			fileElementId : fileId,
			dataType : 'text',
			success : function(data) {
				if (callback)
					callback(data, context);
			},
			error : function() {
				alert('error');
			}
		});
	},

	/* This is used to decide and call function on click of a tile */
	actionFunction : function(name,key,data) {
		if (key == 'topologyOperation') {
			com.impetus.ankush.oClusterMonitoring.loadCommonActionLogPage(name);
		} else if ((key == 'Adding Nodes|progress')|| (key == 'Adding Nodes|error')|| (key == 'Adding Nodes|success')) {
			com.impetus.ankush.oClusterMonitoring.loadNodeAddProgress(data);
		} else if (key == 'redistribute') {
			com.impetus.ankush.oClusterMonitoring.loadRedistributeLogPage();
		} else if (key == 'incRepFactor') {
			com.impetus.ankush.oClusterMonitoring.loadIncreaseRepFactorLog();
		} else if (key == 'rebalance') {
			com.impetus.ankush.oClusterMonitoring.loadRebalanceLogPage();
		} else if (key == 'verification') {
			com.impetus.ankush.oClusterMonitoring.loadVerifyLogPage();
		} else if (key == 'shardNodeTable') {
			var divId = key + 'Div';
			com.impetus.ankush.oClusterMonitoring.focusDiv(divId);
		} else if (key == 'storageNodeTable') {
			var divId = key + 'Div';
			com.impetus.ankush.oClusterMonitoring.focusDiv(divId);
		}
	},

	/* Function for focusing particular element according to the id */
	focusDiv : function(id) {
		$('#' + id)[0].scrollIntoView(false);
	},

	/* Function for loading shard table page */
	loadShardDrillDown : function(shardId) {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel', {
							url : baseUrl + '/oClusterMonitoring/shardDrillDown',
							method : 'get',
							params : {},
							title : 'Shard' + shardId,
							tooltipTitle : 'Cluster Monitoring',
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.initializeShardDrillTable(data.shardId);
							},
							callbackData : {
								shardId : shardId,
							}
						});
	},

	/* Function to populate shard drill down page */
	initializeShardDrillTable : function(shardId) {
		$('#shardNameHead').html('').text('Shard' + shardId);
		$('#shardDrillTable').dataTable().fnDestroy();
		shardDrillTable = $('#shardDrillTable').dataTable(
				{
					"bJQueryUI" : true,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : true,
					"bInfo" : false,
					"aoColumns" : [ {
						sWidth : '5%',
						"fnRender" : function(oObj) {
							return 'Shard' + oObj.aData[0];
						}
					}, {
						sWidth : '13%'
					}, {
						sWidth : '16%',
						"fnRender" : function(oObj) {
							if (oObj.aData[3]) {
								return 'RN' + oObj.aData[2] + '(Master)';
							} else
								return 'RN' + oObj.aData[2];
						}
					}, {
						sWidth : '15%',
						"fnRender" : function(oObj) {
							return oObj.aData[4];
						}
					}, {
						sWidth : '12%',
						sType : 'numeric',
						"fnRender" : function(oObj) {
							return oObj.aData[5];
						}
					}, {
						sWidth : '12%',
						sType : 'numeric',
						"fnRender" : function(oObj) {
							return oObj.aData[6];
						}
					}, {
						sWidth : '12%',
						sType : 'numeric',
						"fnRender" : function(oObj) {
							return (oObj.aData[7]+oObj.aData[9])+' ops/sec';
						}
					}, {
						sWidth : '12%',
						sType : 'numeric',
						"fnRender" : function(oObj) {
							var maxVal=oObj.aData[8] > oObj.aData[10] ? oObj.aData[8] :oObj.aData[10];
							if(maxVal>0){
								return com.impetus.ankush.decimal(maxVal,2)+' ms';	
							}
							else{
							return 0; 	
							}
						}
					},/*{
						sWidth : '11%',
						sType : 'numeric',
						"fnRender" : function(oObj) {
							return oObj.aData[9]+' ops/sec';
						}
					}, *//*{
						sWidth : '12%',
						sType : 'numeric',
						"fnRender" : function(oObj) {
							return com.impetus.ankush.decimal(oObj.aData[10],2)+' ms';
						}
					},*/
					{
						sWidth : '3%',
						"fnRender" : function(oObj) {
							return oObj.aData[11];
						}
					}, {
						"bVisible" : false
					} ],
					"aoColumnDefs" : [ {
						'sType' : "ip-address",
						'aTargets' : [ 4 ]
					}, {
						'bSortable' : false,
						'aTargets' : [ 0, 1, 8 ]
					} ],
					"fnRowCallback" : function(nRow, aData, iDisplayIndex,
							iDisplayIndexFull) {
						if (aData[12] == 1) {
							$('td', $(nRow)).addClass('error-row');
						} else {
							$('td', $(nRow)).removeClass('error-row');
						}
					}
				}).rowGrouping({
			bExpandableGrouping : true,
			iGroupingColumnIndex : 0
		});
		$('#shardDrillTable').css('width', '100%');
		$('#shardDrillTable_filter').hide();
		shardDrillTable = $('#shardDrillTable').dataTable();
		$('#shardDrillSearchBox').keyup(function() {
			shardDrillTable.fnFilter($(this).val());
		});
		result = overviewData;
		if (result.output.status == true) {
			result = result.output.shards;
			var tableObj = new Array();
			for ( var i = 0; i < result.length; i++) {
				if (result[i].rgId == shardId) {
					var rowObj = new Array();
					rowObj.push(result[i].rgId);
					rowObj.push("<input type = 'checkbox' id='repNodeDrillCheckBox-"
									+ i
									+ "' class='repNodeDrillCheckBoxClass' onclick='com.impetus.ankush.oClusterMonitoring.shardChildNodeCheck();'/>");
					rowObj.push(result[i].nodeNum);
					rowObj.push(result[i].master);
					rowObj.push(result[i].hostname);
					rowObj.push(result[i].port);
					rowObj.push(result[i].singleInt.percent95);
					rowObj.push(result[i].singleInt.throughput);
					rowObj.push(result[i].singleInt.avg);
					rowObj.push(result[i].multiInt.throughput);
					rowObj.push(result[i].multiInt.avg);
					rowObj.push("<a href='#' onclick='com.impetus.ankush.oClusterMonitoring.loadRepNodeChild("
									+ result[i].rgId
									+ ","
									+ result[i].nodeNum
									+ ",\"Shard Table\")'>"
									+ right_arrow
									+ "</a>");
					rowObj.push(result[i].active);
					tableObj.push(rowObj);
				}
			}
			shardDrillTable.fnAddData(tableObj);
			/*
			 * for(var i=0;i<result.length;i++){ if(result[i].rgId==shardId){
			 * if (result[i].active == 1) { $('td', $('#RepNodeDrill-' +
			 * i)).addClass('error-row'); }else{ $('td', $('#RepNodeDrill-' +
			 * i)).removeClass('error-row'); } } }
			 */
		}
		com.impetus.ankush.oClusterMonitoring.plotShardNodeTree(shardId,'shardTreeGraphDiv');
	},

	/* Function for populating Shard table */
	initializeShardNodeTable : function() {
		result = overviewData.output.shards;
		/*Need to delete this block for dummy json Maneesh*/
		 	//var tmp = '{"output":{"shards":[{"rgId":3,"hostname":"192.168.145.42","nodeNum":2,"snId":5,"port":5011,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0}},{"rgId":3,"hostname":"192.168.145.49","nodeNum":1,"snId":4,"port":5010,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0}},{"rgId":3,"hostname":"192.168.145.45","nodeNum":3,"snId":6,"port":5010,"active":0,"master":true,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":1127,"avg":0.06520865,"totalOps":67658,"percent95":0,"percent99":1},"multiCum":{"throughput":14479,"avg":0.21002917,"totalOps":33055883,"percent95":1,"percent99":2}},{"rgId":4,"hostname":"192.168.145.42","nodeNum":2,"snId":5,"port":5012,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0}},{"rgId":4,"hostname":"192.168.145.45","nodeNum":3,"snId":6,"port":5011,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":2,"avg":0.41005972,"totalOps":135,"percent95":0,"percent99":1},"multiCum":{"throughput":1,"avg":0.7642168,"totalOps":810,"percent95":1,"percent99":2}},{"rgId":4,"hostname":"192.168.145.49","nodeNum":1,"snId":4,"port":5011,"active":0,"master":true,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":1,"avg":0.387397,"totalOps":3,"percent95":0,"percent99":0},"multiInt":{"throughput":923,"avg":1.6236587,"totalOps":55388,"percent95":0,"percent99":1},"multiCum":{"throughput":13319,"avg":0.3336184,"totalOps":30356239,"percent95":0,"percent99":2}},{"rgId":1,"hostname":"192.168.145.43","nodeNum":1,"snId":1,"port":5011,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0}},{"rgId":1,"hostname":"192.168.145.40","nodeNum":3,"snId":3,"port":5011,"active":0,"master":true,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":1041,"avg":0.33948672,"totalOps":62472,"percent95":0,"percent99":1},"multiCum":{"throughput":13349,"avg":0.26346663,"totalOps":30624505,"percent95":1,"percent99":2}},{"rgId":1,"hostname":"192.168.145.41","nodeNum":2,"snId":2,"port":5010,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0}},{"rgId":2,"hostname":"192.168.145.43","nodeNum":1,"snId":1,"port":5012,"active":0,"master":true,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":907,"avg":0.07032656,"totalOps":54465,"percent95":0,"percent99":1},"multiCum":{"throughput":13974,"avg":0.20957652,"totalOps":31988053,"percent95":0,"percent99":2}},{"rgId":2,"hostname":"192.168.145.41","nodeNum":2,"snId":2,"port":5011,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0}},{"rgId":2,"hostname":"192.168.145.40","nodeNum":3,"snId":3,"port":5012,"active":0,"master":false,"singleInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"singleCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiInt":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0},"multiCum":{"throughput":0,"avg":0.0,"totalOps":0,"percent95":0,"percent99":0}}],"tiles":[{"line1":"4000","line2":"Throughput","line3":null,"data":null,"status":"Normal","url":null},{"line1":"4","line2":"Shards","line3":null,"data":null,"status":"Normal","url":"shardNodeTable"},{"line1":"0","line2":"Storage Node Available","line3":null,"data":null,"status":"Normal","url":"storageNodeTable"}],"tree":{"childCount":12,"name":"stockTickTopology","active":0,"children":[{"shard":1,"name":"Shard1","active":0,"children":[{"sn":1,"rn":1,"name":"RN1","active":0},{"sn":3,"rn":3,"name":"RN3","active":0},{"sn":2,"rn":2,"name":"RN2","active":0}]},{"shard":2,"name":"Shard2","active":0,"children":[{"sn":1,"rn":1,"name":"RN1","active":0},{"sn":2,"rn":2,"name":"RN2","active":0},{"sn":3,"rn":3,"name":"RN3","active":0}]},{"shard":3,"name":"Shard3","active":0,"children":[{"sn":5,"rn":2,"name":"RN2","active":0},{"sn":4,"rn":1,"name":"RN1","active":0},{"sn":6,"rn":3,"name":"RN3","active":0}]},{"shard":4,"name":"Shard4","active":0,"children":[{"sn":5,"rn":2,"name":"RN2","active":0},{"sn":6,"rn":3,"name":"RN3","active":0},{"sn":4,"rn":1,"name":"RN1","active":0}]}]},"status":true,"datacenters":[{"name":"stockticker","repFactor":3,"datacenterId":1,"storageNodeList":[{"snId":1,"registryPort":5000,"adminPort":5001,"hostname":"192.168.145.43","active":0,"capacity":2,"rnCount":2},{"snId":2,"registryPort":5000,"adminPort":0,"hostname":"192.168.145.41","active":0,"capacity":2,"rnCount":2},{"snId":3,"registryPort":5000,"adminPort":5001,"hostname":"192.168.145.40","active":0,"capacity":2,"rnCount":2},{"snId":4,"registryPort":5000,"adminPort":0,"hostname":"192.168.145.49","active":0,"capacity":2,"rnCount":2},{"snId":5,"registryPort":5000,"adminPort":5001,"hostname":"192.168.145.42","active":0,"capacity":2,"rnCount":2},{"snId":6,"registryPort":5000,"adminPort":0,"hostname":"192.168.145.45","active":0,"capacity":2,"rnCount":2}]}],"storename":"oracle-stockticker"},"status":"200","description":"Cluster details.","errors":null}';
			//var obj = eval ("(" + tmp + ")");
			//result = obj.output.shards;
		/*Need to delete this block Maneesh*/
			if ($('#shardTableJsonVariable').val() == JSON.stringify(result)) {
			return;
		}
		if (shardNodeTable == null) {
			shardNodeTable = $('#shardNodeTable').dataTable(
					{
						"bJQueryUI" : true,
						"bPaginate" : false,
						"bLengthChange" : false,
						"bFilter" : true,
						"bSort" : true,
						"bInfo" : false,
						"bAutoWidth" : true,
						//"bStateSave": true,
						"aoColumns" : [ {
							sWidth : '4%',
							"fnRender" : function(oObj) {
								return 'Shard' + oObj.aData[0];
							}
						}, 
						{
							sWidth : '10%'//shrads
						}, 
					    {
							sWidth : '10%',
							"fnRender" : function(oObj) {
								if (oObj.aData[3]) {
									return 'RN' + oObj.aData[2] + '(Master)';
								} else
									return 'RN' + oObj.aData[2];
							}
							//Replication Node
							
						}, 
						{
							sWidth : '10%',
							"fnRender" : function(oObj) {
								return oObj.aData[4];
							}
							//Storage Node
						}, 
						{
							sWidth : '8%',
							"fnRender" : function(oObj) {
								return oObj.aData[5];
							},
							"bVisible" : false // port
						}, 
						{
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return oObj.aData[6]+' ops/sec';
								//return (oObj.aData[5]+oObj.aData[7])+' ops/sec';
							}
							//Single Int Throughput
						}, 
						 {
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return com.impetus.ankush.decimal(oObj.aData[7],2)+' ms';
								//var maxVal= oObj.aData[6]>oObj.aData[8] ? oObj.aData[6] : oObj.aData[8];
								//if(maxVal>0){
									//return com.impetus.ankush.decimal(maxVal,2)+' ms';	
								//}else{
									//return 0;
								//}								
							}
							//Single Int Avg Latency
						}, 
					    {
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return oObj.aData[8];
							}
							//Single Int TotalOps
						}, 
					   	{
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return oObj.aData[9]+' ops/sec';
							},
							"bVisible" : false
							//Single Cum Throughput
						},
						{
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return com.impetus.ankush.decimal(oObj.aData[10],2)+' ms';
							},
							"bVisible" : false
							//Single Cum Avg Latency
						},
						{
							sWidth : '10%',
							"fnRender" : function(oObj) {
								return oObj.aData[11];
							},
							"bVisible" : false
							//Single Cum TotalOps
						},
						{
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return oObj.aData[12]+' ops/sec';
							},
							"bVisible" : false
							//Multi Int Throughput
						},
						{
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return com.impetus.ankush.decimal(oObj.aData[13],2)+' ms';
							},
							"bVisible" : false
							//Multi Int Avg Latency
						},
						{
							sWidth : '10%',
							sType : 'numeric',
							"fnRender" : function(oObj) {
								return oObj.aData[14];
							},
							"bVisible" : false
							//Multi Int TotalOps
						},
						{
							sWidth : '3%',
							"fnRender" : function(oObj) {
								return oObj.aData[15];
							}
						},
						{
							sWidth : '10%',
							"bVisible" : false
						} ],
						"aoColumnDefs" : [ {
							'sType' : "ip-address",
							'aTargets' : [ 4 ]
						}, {
							'bSortable' : false,
							'aTargets' : [ 0, 1, 14]
						} ],
						"fnRowCallback" : function(nRow, aData, iDisplayIndex,
								iDisplayIndexFull) {
							  $(nRow).attr('id',aData[0].split('Shard')[1]+''+aData[2].split('RN')[1].split('(Master)')[0]);
							if (aData[16] == 1) {
								$('td', $(nRow)).addClass('error-row');
							} else {
								$('td', $(nRow)).removeClass('error-row');
							}
						},
						/*"fnStateLoaded": function (oSettings, oData) {
					    	//getting save state columns and showing bydefault in multi select box
							//alert(oData.toSource());
							var tmploop=0;
							for(tmploop=4;tmploop<=13;tmploop++){
								if(oData.abVisCols[tmploop]===true){
									$("#shardNodeTableColumns").multiselect('select', tmploop);
								}
								else
								{
									$("#shardNodeTableColumns").multiselect('deselect', tmploop);
								}
							}
					    }*/
					}).rowGrouping({
				bExpandableGrouping : true,
				iGroupingColumnIndex : 0
			});
			$('#shardNodeTable').css('width', '100%');
		} else {
			shardNodeTable.fnClearTable();
		}
		$('#shardNodeTable_filter').hide();
		shardNodeTable = $('#shardNodeTable').dataTable();
		$("#storageTableJsonVariable").val(JSON.stringify(result));
		$('#shardNodeSearchBox').keyup(function() {
			shardNodeTable.fnFilter($(this).val());
		});
		if (overviewData.output.status == true) {
			shardNodeData = result;
			var tableObj = new Array();
			for ( var i = 0; i < result.length; i++) {
				var rowObj = new Array();
				rowObj.push(result[i].rgId);
				rowObj.push("<input type = 'checkbox' id='repNodeCheckBox-"
								+ i
								+ "' class='repNodeCheckBoxClass' onclick='com.impetus.ankush.oClusterMonitoring.repNodeChildNodeCheck("
								+ i + ")'/>");
				rowObj.push(result[i].nodeNum);
				rowObj.push(result[i].master);
				rowObj.push(result[i].hostname);
				rowObj.push(result[i].port);
				rowObj.push(result[i].singleInt.throughput);
				rowObj.push(result[i].singleInt.avg);
				rowObj.push(result[i].singleInt.totalOps);
				rowObj.push(result[i].singleCum.throughput);
				rowObj.push(result[i].singleCum.avg);
				rowObj.push(result[i].singleCum.totalOps);
				rowObj.push(result[i].multiInt.throughput);
				rowObj.push(result[i].multiInt.avg);
				rowObj.push(result[i].multiInt.totalOps);
				rowObj.push("<a href='#' onclick='com.impetus.ankush.oClusterMonitoring.loadRepNodeChild("
								+ result[i].rgId
								+ ","
								+ result[i].nodeNum
								+ ",\"Cluster Monitoring\")'>"
								+ right_arrow
								+ "</a>");
				rowObj.push(result[i].active);
				tableObj.push(rowObj);
			}
			shardNodeTable.fnAddData(tableObj);
		}
	},

	/* Function for starting a replication node */
	startRepNode : function(clusterId, checkBoxId) {
		dataToSend = new Array();
		for ( var i = 0; i < shardNodeData.length; i++) {
			data = new Array();
			if ($("#" + checkBoxId + "-" + i).is(':checked')) {
				data.push((shardNodeData[i].rgId).toString());
				data.push((shardNodeData[i].nodeNum).toString());
				dataToSend.push(data);
			}
		}
		if (dataToSend.length == 0) {
			return;
		}
		url = baseUrl + '/manage/' + monitorClusterId + '/service/startrepnode';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, dataToSend,
				function(result) {
					$('#' + checkBoxId).removeAttr('checked');
					$('.' + checkBoxId + 'Class').each(function() {
						$(this).removeAttr('checked');
					});
					if (!result.output.status) {
						$("#nodeServiceDialog").modal('hide');
						com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
					}
					com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
				});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},

	/* Function for stopping a replication node */
	stopRepNode : function(clusterId, checkBoxId) {
		dataToSend = new Array();
		for ( var i = 0; i < shardNodeData.length; i++) {
			data = new Array();
			if ($("#" + checkBoxId + "-" + i).is(':checked')) {
				data.push((shardNodeData[i].rgId).toString());
				data.push((shardNodeData[i].nodeNum).toString());
				dataToSend.push(data);
			}
		}
		if (dataToSend.length == 0) {
			return;
		}
		url = baseUrl + '/manage/' + monitorClusterId + '/service/stoprepnode';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, dataToSend,
				function(result) {
					$('#' + checkBoxId).removeAttr('checked');
					$('.' + checkBoxId + 'Class').each(function() {
						$(this).removeAttr('checked');
					});
					if (!result.output.status) {
						$("#nodeServiceDialog").modal('hide');
						com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
					}
					com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
				});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},
	/* Function for starting all replication nodes */
	startAllRepNodes : function(clusterId, checkBoxId) {
		url = baseUrl + '/manage/' + monitorClusterId+ '/service/startallrepnodes';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, null, function(result) {
			$('#' + checkBoxId).removeAttr('checked');
			$('.' + checkBoxId + 'Class').each(function() {
				$(this).removeAttr('checked');
			});
			if (!result.output.status) {
				$("#nodeServiceDialog").modal('hide');
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
			com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
		});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},
	/* Function for stopping all replication nodes */
	stopAllRepNodes : function(clusterId, checkBoxId) {
		url = baseUrl + '/manage/' + monitorClusterId+ '/service/stopallrepnodes';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, null, function(result) {
			$('#' + checkBoxId).removeAttr('checked');
			$('.' + checkBoxId + 'Class').each(function() {
				$(this).removeAttr('checked');
			});
			if (!result.output.status) {
				$("#nodeServiceDialog").modal('hide');
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
			com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
		});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},
	/* Function for loading replication node child drill down page */
	loadRepNodeChild : function(repGpId, repNodeId, title) {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		var functionCall = "com.impetus.ankush.oClusterMonitoring.graphDraw(\'"+ graphInitialTime + "\');";
		if ($.data(document, "panels").children.length < 2) {
			functionCall = "com.impetus.ankush.oClusterMonitoring.monitoringTiles(" + monitorClusterId + ");";
		}
		$('#content-panel').sectionSlider('addChildPanel',{
					url : baseUrl + '/oClusterMonitoring/repNodeChild/' + repGpId + '/' + repNodeId,
					method : 'get',
					params : {},
					previousCallBack : functionCall,
					// previousCallBack :
					// "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+monitorClusterId+")",
					title : 'Shard' + repGpId + '/ReplicationNode' + repNodeId,
					tooltipTitle : title,
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.repNodeDrillPage(repGpId, repNodeId, 'repNodeNodeShrad');
					},
				});
	},

	repNodeDrillPage : function(repGpId, repNodeId) {
		var replicationNodeDrill = $('#replicationNodeDrill').dataTable(
				{
					"bJQueryUI" : true,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : true,
					"bInfo" : false,
					"bAutoWidth" : true,
					"aoColumns" : [ {
						sWidth : '28%',
					}, {
						sWidth : '26%',
					}, {
						sWidth : '23%',
						sType : 'numeric',
					}, {
						sWidth : '23%',
						sType : 'numeric',
					}, /*{
						sWidth : '19%',
						sType : 'numeric',
					},*/ /*{
						sWidth : '15%',
						sType : 'numeric',
					},*/
					],
					"aoColumnDefs" : [ {
						'sType' : "ip-address",
						'aTargets' : [ 1 ]
					}, ],
					"fnRowCallback" : function(nRow, aData, iDisplayIndex,iDisplayIndexFull) {
						if (aData[6] == 1) {
							$('td', $(nRow)).addClass('error-row');
						} else {
							$('td', $(nRow)).removeClass('error-row');
						}
					}
				});
		$('#replicationNodeDrill').css('width', '100%');
		$('#replicationNodeDrill_filter').hide();
		replicationNodeDrill = $('#replicationNodeDrill').dataTable();
		$('#repNodeDrillSearchBox').keyup(function() {
			replicationNodeDrill.fnFilter($(this).val());
		});
		var rowIndex = shardNodeTable.fnGetPosition($("#" + repGpId + repNodeId).closest('tr')[0]);
		result = shardNodeTable.fnGetData(rowIndex);
		if (overviewData.output.status == true) {
			var tableObj = new Array();
			var rowObj = new Array();
			rowObj.push(result[2]);
			rowObj.push(result[3]);
			rowObj.push(result[4]);
			rowObj.push(result[5]);
			rowObj.push(result[6]);
			rowObj.push(result[7]);
			rowObj.push(result[16]);
			tableObj.push(rowObj);
			replicationNodeDrill.fnAddData(tableObj);
		}

		var repNodeDrillParam = $('#repNodeDrillParam').dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : false,
			"bInfo" : false,
			"bAutoWidth" : true,
		});
		$('#repNodeDrillParam_filter').hide();
		repNodeDrillParam = $('#repNodeDrillParam').dataTable();
		$('#repNodeDrillParamSearchBox').keyup(function() {
			repNodeDrillParam.fnFilter($(this).val());
		});
		var url = baseUrl + '/monitor/' + monitorClusterId + '/repnodeparams?rgid=' + repGpId + '&rnid=' + repNodeId+'&all';
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			result = result.output;
			for ( var key in result) {
				labelName = camelCaseKey(key);
				labelVal = result[key];
				statusKey = "status";
				if (labelName.toLowerCase() == statusKey.toLowerCase()) {
					continue;
				}
				repNodeDrillParam.fnAddData([ labelName, labelVal ]);
			}
		});
	},
	/* Function for starting a replication node */
	startReplicationNode : function(rgId, nodeNum) {
		dataToSend = new Array();
		data = new Array();
		data.push(rgId);
		data.push(nodeNum);
		dataToSend.push(data);
		if (dataToSend.length == 0) {
			return;
		}
		url = baseUrl + '/manage/' + monitorClusterId + '/service/startrepnode';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, dataToSend,
				function(result) {
					if (!result.output.status) {
						$("#nodeServiceDialog").modal('hide');
						com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
					}
					com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
				});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},

	/* Function for stopping a replication node */
	stopReplicationNode : function(rgId, nodeNum) {
		dataToSend = new Array();
		data = new Array();
		data.push(rgId);
		data.push(nodeNum);
		dataToSend.push(data);
		if (dataToSend.length == 0) {
			return;
		}
		url = baseUrl + '/manage/' + monitorClusterId + '/service/stoprepnode';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, dataToSend,
				function(result) {
					if (!result.output.status) {
						$("#nodeServiceDialog").modal('hide');
						com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
					}
					com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
				});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},

	/* Function for loading shard replication node parameter page */
	loadRepNodeChildParam : function(repGpId, repNodeId, pageId) {
		$('#content-panel').sectionSlider('addChildPanel',{
					url : baseUrl+ '/oClusterMonitoring/repNodeChildParameter/'+ repGpId + '/' + repNodeId,
					method : 'get',
					params : {},
					title : 'Parameters',
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.populateRepNodeChildPage(repGpId, repNodeId);
					},
					callbackData : {
						repGpId : repGpId,
						repNodeId : repNodeId
					}
				});
	},
	repNodeChildParamCancel : function(repGpId, repNodeId) {
		com.impetus.ankush.oClusterMonitoring.populateRepNodeChildPage(repGpId,repNodeId);
		com.impetus.ankush.removeChild($.data(document, "panels").children.length);
	},
	/* Function for populating replication node child page */
	populateRepNodeChildPage : function(repGpId, repNodeId) {
		url = baseUrl + '/monitor/' + monitorClusterId + '/repnodeparams?rgid='+ repGpId + '&rnid=' + repNodeId;
		com.impetus.ankush.placeAjaxCall(
						url,
						"GET",
						true,
						null,
						function(result) {
							result = result.output;
							$('#repNodeChildParamBody').html('');
							repNodeChildParamJson = result;
							for (key in result) {
								labelName = camelCaseKey(key);
								labelVal = result[key];
								statusKey = "status";
								if (labelName.toLowerCase() == statusKey.toLowerCase()) {
									continue;
								}
								$('#repNodeChildParamBody').append(
												"<div class='row-fluid'>"
														+ "<div class='span2 text-right'><label class='form-label'>"
														+ labelName
														+ ":</label></div>"
														+ "<div id='"
														+ key
														+ "' class='span4 text-left editable' style='padding-top:15px;color:black;'>"
														+ labelVal + "</div>"
														+ "</div>");
							}
							$('.editable').textEditable({
								parentClass : 'dummy'
							});
							$('.dummy').css({
								'padding-top' : '0px'
							});
						});
	},

	/* Function for populating storagenode table */
	initializeStorageNodeTable : function() {
		var result = new Array();
		for ( var i = 0; i < overviewData.output.datacenters.length; i++) {
			for ( var j = 0; j < overviewData.output.datacenters[i].storageNodeList.length; j++) {
				result.push(overviewData.output.datacenters[i].storageNodeList[j]);
			}
		}
		// result=overviewData.output.storagenodes;
		if (storageNodeTable == null) {
			storageNodeTable = $('#storageNodeTable').dataTable(
					{
						"bJQueryUI" : true,
						"bPaginate" : false,
						"bLengthChange" : false,
						"bFilter" : true,
						"bSort" : true,
						"bInfo" : false,
						"bAutoWidth" : false,
						"aoColumns" : [ {
							sWidth : '5%'
						}, {
							sWidth : '20%'
						}, {
							sWidth : '20%',
							sType : 'numeric'
						}, {
							sWidth : '20%',
							sType : 'numeric'
						}, {
							sWidth : '15%',
							sType : 'numeric'
						}, {
							sWidth : '15%',
							sType : 'numeric'
						}, {
							sWidth : '5%'
						}, {
							"bVisible" : false
						} ],
						"aoColumnDefs" : [ {
							'bSortable' : false,
							'aTargets' : [ 0, 6 ]
						}, {
							'sType' : "ip-address",
							'aTargets' : [ 1 ]
						} ],
						"fnRowCallback" : function(nRow, aData, iDisplayIndex,iDisplayIndexFull) {
							if (aData[7] == 1) {
								$('td', $(nRow)).addClass('error-row');
							} else {
								$('td', $(nRow)).removeClass('error-row');
							}
						}
					});
		} else {
			storageNodeTable.fnClearTable();
		}
		$('#storageNodeTable_filter').hide();
		storageNodeTable = $('#storageNodeTable').dataTable();
		$('#storageNodeSearchBox').keyup(function() {
			storageNodeTable.fnFilter($(this).val());
		});
		if (overviewData.output.status) {
			storageNodeData = result;
			var tableObj = new Array();
			for ( var i = 0; i < result.length; i++) {
				var rowObj = new Array();
				rowObj.push("<input type='checkbox' id='storageNodeCheckBox-"
								+ i
								+ "' class='storageNodeCheckBoxClass' onclick='com.impetus.ankush.oClusterMonitoring.storageNodeChildNodeCheck("
								+ i + ")'/>");
				rowObj.push(result[i].hostname);
				rowObj.push(result[i].rnCount);
				rowObj.push(result[i].capacity);
				rowObj.push(result[i].registryPort);
				rowObj.push(result[i].adminPort);
				rowObj.push('<a href="#" onclick="com.impetus.ankush.oClusterMonitoring.loadStorageNodeChild(\''
								+ result[i].snId
								+ '\',\''
								+ result[i].hostname
								+ '\')">' + right_arrow + '</a>');
				rowObj.push(result[i].active);
				tableObj.push(rowObj);
			}
			$('#storageNodeTable').dataTable().fnAddData(tableObj);
		}
	},

	/* Function for checking/unchecking all nodes on click on header checkbox */
	checkedAllNodes : function(id) {
		if ($('#' + id).is(':checked')) {
			$('.' + id + 'Class').attr('checked', true);
		} else
			$('.' + id + 'Class').attr('checked', false);
	},

	/*
	 * Function for checking/unchecking header checkbox on click on node click
	 * of storage node table
	 */
	storageNodeChildNodeCheck : function(i) {
		if ($("#storageNodeTable .storageNodeCheckBoxClass:checked").length == $("#storageNodeTable .storageNodeCheckBoxClass").length) {
			$("#storageNodeCheckBox").prop("checked", true);
		} else {
			$("#storageNodeCheckBox").prop("checked", false);
		}
	},

	/*
	 * Function for checking/unchecking header checkbox on click on node click
	 * of replication node table
	 */
	repNodeChildNodeCheck : function(i) {
		if ($("#shardNodeTable .repNodeCheckBoxClass:checked").length == $("#shardNodeTable .repNodeCheckBoxClass").length) {
			$("#repNodeCheckBox").prop("checked", true);
		} else {
			$("#repNodeCheckBox").prop("checked", false);
		}
	},

	/*
	 * Function for checking/unchecking header checkbox on click on node click
	 * of shard node table
	 */
	shardChildNodeCheck : function() {
		if ($("#shardDrillTable .repNodeDrillCheckBoxClass:checked").length == $("#shardDrillTable .repNodeDrillCheckBoxClass").length) {
			$("#repNodeDrillCheckBox").prop("checked", true);
		} else {
			$("#repNodeDrillCheckBox").prop("checked", false);
		}
	},

	/* Function for starting a storage node */
	startStorageNode : function(clusterId, checkBoxId) { // passes checkBoxId
		// as same method
		// call is needed
		// from its
		// child-page also.
		data = new Array();
		for ( var i = 0; i < storageNodeData.length; i++) {
			if ($("#" + checkBoxId + "-" + i).is(':checked')) {
				data.push((storageNodeData[i].snId).toString());
			}
		}
		if (data.length == 0) {
			return;
		}
		url = baseUrl + '/manage/' + monitorClusterId + '/service/startstoragenode';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, data, function(result) {
			$('#' + checkBoxId).removeAttr('checked');
			$('.' + checkBoxId + 'Class').each(function() {
				$(this).removeAttr('checked');
			});
			if (!result.output.status) {
				$("#nodeServiceDialog").modal('hide');
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
		});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');

	},

	/* Function for stopping a storage node */
	stopStorageNode : function(clusterId, checkBoxId) {
		data = new Array();
		for ( var i = 0; i < storageNodeData.length; i++) {
			if ($("#" + checkBoxId + "-" + i).is(':checked')) {
				data.push((storageNodeData[i].snId).toString());
			}
		}
		if (data.length == 0) {
			return;
		}
		url = baseUrl + '/manage/' + monitorClusterId+ '/service/stopstoragenode';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, data, function(result) {
			$('#' + checkBoxId).removeAttr('checked');
			$('.' + checkBoxId + 'Class').each(function() {
				$(this).removeAttr('checked');
			});
			if (!result.output.status) {
				$("#nodeServiceDialog").modal('hide');
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
			com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
		});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},

	/* Function for starting all storage node */
	startAllStorageNodes : function(clusterId, checkBoxId) {
		url = baseUrl + '/manage/' + monitorClusterId+ '/service/startallstoragenodes';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, null, function(result) {
			$('#' + checkBoxId).removeAttr('checked');
			$('.' + checkBoxId + 'Class').each(function() {
				$(this).removeAttr('checked');
			});
			if (!result.output.status) {
				$("#nodeServiceDialog").modal('hide');
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
		});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},

	/* Function for stopping a storage node */
	stopAllStorageNodes : function(clusterId, checkBoxId) {
		url = baseUrl + '/manage/' + monitorClusterId+ '/service/stopallstoragenodes';
		com.impetus.ankush.placeAjaxCall(url, "POST", true, null, function(result) {
			$('#' + checkBoxId).removeAttr('checked');
			$('.' + checkBoxId + 'Class').each(function() {
				$(this).removeAttr('checked');
			});
			if (!result.output.status) {
				$("#nodeServiceDialog").modal('hide');
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
			com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
		});
		$("#nodeServiceDialog").removeClass('nodeServiceDialog nodeServiceDialog3').addClass('nodeServiceDialog'+$.data(document, "panels").children.length);
		$(".nodeServiceDialog"+$.data(document, "panels").children.length).modal('show');
	},
	/* Function for loading storage node child page */
	loadStorageNodeChild : function(storageNodeId, storageNodeIP) {
		hostNodeIp = storageNodeIP;
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							url : baseUrl+ '/oClusterMonitoring/storageNodeChild/',
							method : 'post',
							params : {
								storageNodeId : storageNodeId,
								storageNodeIP : storageNodeIP,
							},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							title : storageNodeIP,
							tooltipTitle : 'Cluster Monitoring',
							callback : function() {
								com.impetus.ankush.oClusterMonitoring.populateRepNodeTable(storageNodeId);
							}
						});
	},

	/* Function for loading storage node child parameter page */
	loadStorageNodeChildParam : function(storageNodeId) {
		$('#content-panel').sectionSlider('addChildPanel',{
							url : baseUrl+ '/oClusterMonitoring/storageNodeChildParameter/'+ storageNodeId,
							method : 'get',
							params : {},
							title : 'Parameters',
							tooltipTitle : 'Cluster Monitoring',
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.graphDraw(\'"+ graphInitialTime + "\')",
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.populateStorageNodeChildPage(storageNodeId);
							},
							callbackData : {
								storageNodeId : storageNodeId
							}
						});
	},

	storageNodeChildParamCancel : function(storageNodeId) {
		com.impetus.ankush.oClusterMonitoring.populateStorageNodeChildPage(storageNodeId);
		com.impetus.ankush.removeChild($.data(document, "panels").children.length);
	},
	/* Function for populating storagenode child parameters */
	populateStorageNodeChildPage : function(storageNodeId) {
		url = baseUrl + '/monitor/' + monitorClusterId+ '/storagenodeparams?id=' + storageNodeId;
		com.impetus.ankush.placeAjaxCall(
						url,
						"GET",
						true,
						null,
						function(result) {
							result = result.output;
							$('#storageNodeChildParamBody').html('');
							storageNodeChildParamJson = result;
							for (key in result) {
								labelName = camelCaseKey(key);
								labelVal = result[key];
								statusKey = "status";
								if (labelName.toLowerCase() == statusKey.toLowerCase()) {
									continue;
								}
								$('#storageNodeChildParamBody').append(
												"<div class='row-fluid'>"
														+ "<div class='span2 text-right'><label class='form-label'>"
														+ labelName
														+ ":</label></div>"
														+ "<div id='"
														+ key
														+ "' class='span4 text-left editable' style='padding-top:15px;color:black;'>"
														+ labelVal + "</div>"
														+ "</div>");
							}
							$('.editable').textEditable({});
						});
	},

	/* Function for populating replpication node table */
	populateRepNodeTable : function(storageNodeId) {
		com.impetus.ankush.oClusterMonitoring.plotStorageNodeTree(storageNodeId, 'storageNodeShrad');
		var replicationNodeTable = $('#replicationNode').dataTable(
				{
					"bJQueryUI" : false,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : false,
					"bInfo" : false,
					"aoColumnDefs" : [ {
						'bVisible' : false,
						'aTargets' : [ 6 ]
					} ],
					"fnRowCallback" : function(nRow, aData, iDisplayIndex,iDisplayIndexFull) {
						if (aData[6] == 1) {
							$('td', $(nRow)).addClass('error-row');
						} else {
							$('td', $(nRow)).removeClass('error-row');
						}
					}
				});
		$('#replicationNode_filter').hide();
		url = baseUrl + '/monitor/' + monitorClusterId + '/storagenode?id='+ storageNodeId;
		result = overviewData;
		if (result.output.status) {
			var tableObj = new Array();
			for ( var i = 0; i < result.output.shards.length; i++) {
				if (result.output.shards[i].snId == storageNodeId) {
					hostNodeIp = result.output.shards[i].hostname;
					var rowObj = new Array();
					rowObj.push("<input type='checkbox' id='storageNodeChildCheckBox-"
									+ i
									+ "' class='storageNodeChildCheckBoxClass'/>");
					rowObj.push("RN" + result.output.shards[i].nodeNum);
					rowObj.push("Shard" + result.output.shards[i].rgId);
					rowObj.push(result.output.shards[i].master);
					rowObj.push(result.output.shards[i].port);
					rowObj.push("<a href='#' onclick='com.impetus.ankush.oClusterMonitoring.loadRepNodeChild("
									+ result.output.shards[i].rgId
									+ ","
									+ result.output.shards[i].nodeNum
									+ ",\"Replication Node\");'>"
									+ right_arrow
									+ "</a>");
					rowObj.push(result.output.shards[i].active);
					tableObj.push(rowObj);
				}
			}
			replicationNodeTable.fnAddData(tableObj);
		}

		var storageNodeDrillParam = $('#storageNodeDrillParam').dataTable({
			"bJQueryUI" : true,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : true,
			"bSort" : false,
			"bInfo" : false,
			"bAutoWidth" : true,
		});
		$('#storageNodeDrillParam_filter').hide();
		storageNodeDrillParam = $('#storageNodeDrillParam').dataTable();
		$('#storageNodeDrillParamSearchBox').keyup(function() {
			storageNodeDrillParam.fnFilter($(this).val());
		});
		url = baseUrl + '/monitor/' + monitorClusterId+ '/storagenodeparams?id=' + storageNodeId+'&all';
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			result = result.output;
			for (key in result) {
				labelName = camelCaseKey(key);
				labelVal = result[key];
				statusKey = "status";
				if (labelName.toLowerCase() == statusKey.toLowerCase()) {
					continue;
				}
				storageNodeDrillParam.fnAddData([ labelName, labelVal ]);

			}
		});

		com.impetus.ankush.oClusterMonitoring.graphDraw('lasthour');

	},

	normalizeData : function(rrdData) {
		if (rrdData == null) {
			return [];
		}
		var meta = rrdData.meta;
		var data = rrdData.data;
		var legends = meta.legend;
		var result = [];

		legends.forEach(function(legend, index) {
			result.push({
				key : legend,
				values : [],
				yvalues : []
			});
		});
		data.forEach(function(data, dataIndex) {
			legends.forEach(function(legend, legendIndex) {
				result[legendIndex].values.push([ Number(data.t),Number(data.v[legendIndex]) ]);
				result[legendIndex].yvalues.push(Number(data.v[legendIndex]));
			});
		});
		return result;
	},

	initGraphDraw : function() {
		graphDrawInterval = window.clearInterval(graphDrawInterval);
		graphDrawInterval = setInterval("com.impetus.ankush.oClusterMonitoring.graphDraw(\'"+ graphInitialTime + "\');", graphInterval);

	},
	/* Function for drawing graph */
	graphDraw : function(startTime) {
		if (document.getElementById("storageNodeStack") == null) {
			graphDrawInterval = window.clearInterval(graphDrawInterval);
			return;
		}
		graphInitialTime = startTime;
		url = baseUrl + '/monitor/' + monitorClusterId + '/nodegraph?ip='+ hostNodeIp + '&startTime=' + startTime;
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			if (result.output.status) {
				$('#graphButtonGroup').show();
				$('#storageNodeStack svg').css('height', '250px');
				var formatString = '';
				switch (startTime) {
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
					var chart = nv.models.cumulativeLineChart().x(function(d) {
						return d[0];
					}).y(function(d) {
						return d[1];
					}).clipEdge(true);

					chart.showControls(false);
					chart.xAxis.tickFormat(
							function(d) {
								return d3.time.format(formatString)(
										new Date(d * 1000));
							}).ticks(10);

					chart.yAxis.tickFormat(d3.format(',.2f')).ticks(10);

					var svg = d3.select('#storageNodeStack svg').datum(
							com.impetus.ankush.oClusterMonitoring.normalizeData(result.output.json)).transition().duration(500).call(chart);
					svg.attr("width", "100%");
					svg.attr("display", "block");
					nv.utils.windowResize(chart.update);

				});
			} else {
				$('#graphErrorDiv').prepend('Sorry, Unable to fetch Graph information for node');
			}
		});
		com.impetus.ankush.oClusterMonitoring.initGraphDraw();
	},

	/* Function for opening common dialog for cluster level actions */
	commonActionDialog : function(actionType) {
		var actionMesssage = {
			'Rebalance' : 'Rebalance might take some time to complete.',
			'Redistribute' : 'Redistribute might take some time to complete.  ',
			'Delete Cluster' : 'The Cluster will be permanently deleted. Once deleted data cannot be recovered.',
		};
		$("#actionDialog").appendTo('body').modal('show');
		$('#actionDialogBody').html('').append(actionMesssage[actionType]);
		$('#actionDialogHeader').html('').html(actionType);
		$(".ui-dialog-titlebar").hide();
		$('.ui-dialog :button').blur();

		if (actionType == 'Delete Cluster') {
			$('#confirmActionButton').text('Delete');
		} else {

			$('#confirmActionButton').text('Confirm');
		}
		var actionMap = {
			"Redistribute" : function() {
				com.impetus.ankush.oClusterMonitoring.redistributeCluster();
			},
			"Rebalance" : function() {
				com.impetus.ankush.oClusterMonitoring.rebalanceCluster();
			},
			"Delete Cluster" : function() {
				com.impetus.ankush.oClusterMonitoring.deleteCluster();
			},
		};
		$("#confirmActionButton").click(
				function() {
					com.impetus.ankush.oClusterMonitoring.cancelActionDialog('actionDialog');
					actionMap[actionType].call(this);
					$("#confirmActionButton").unbind('click');
				});

	},

	/* Function to be called on dialog cancel button */
	commonDialogCancel : function() {
		$("#confirmActionButton").unbind('click');
	},

	/* Function to be called on dialog cancel button to hide dialogbox */
	cancelActionDialog : function(id) {
		$("#" + id).modal('hide');
	},

	/* Function for opening dialog for error */
	commonActionError : function(error) {
		$("#actionErrorDialog").appendTo('body').modal('show');
		$(".ui-dialog-titlebar").hide();
		$('.ui-dialog :button').blur();
		$('#actionErrorDiv').html('').append(error);
	},

	/* Function for opening dialog of increase replication factor */
	openIncreaseRepFactorDialog : function() {
		var incUrl = baseUrl + '/monitor/' + monitorClusterId+ '/repfactorparams';
		com.impetus.ankush.placeAjaxCall(incUrl, "GET", true, null, function(result) {
			var incResult = result;
			if (incResult.output.status) {
				$("#increaseRepFactorDialog").appendTo('body').modal('show');
				$('#repFactorToIncrease').val('');
				$(".ui-dialog-titlebar").hide();
				$('.ui-dialog :button').blur();
				$('#nodesInUseRepDialog').text(incResult.output.totalStorageNodes);
				$('#shardsRepDialog').text(incResult.output.shardCount);
				$('#availableStorageNodeRepDialog').text(incResult.output.availableStorageNodes);
				$('#availableCapacityeRepDialog').text(incResult.output.availableCapacity);
				$('#repFactorRepDialog').text(incResult.output.repFactor);
			} else {
				com.impetus.ankush.oClusterMonitoring.commonActionError(incResult.output.error[0]);
			}
		});
	},

	/* Function for replication factor increasing */
	increaseReplicationFactor : function() {
		var repFactor = $("#repFactorToIncrease").val();
		var shardNum =  parseInt($("#shardsRepDialog").text());
		var capacity =  parseInt($("#availableCapacityeRepDialog").text());
		var oldRepFac = parseInt($("#repFactorRepDialog").text());
		var calCapacity=shardNum * (repFactor-oldRepFac);
		if(capacity< calCapacity){
			com.impetus.ankush.oClusterMonitoring.cancelActionDialog('increaseRepFactorDialog');
			com.impetus.ankush.oClusterMonitoring.commonActionError('For changing replicatation factor to '+repFactor+' capacity should be equal or greater than '+calCapacity);
			return;
		}
		var incUrl = baseUrl + '/manage/' + monitorClusterId+ '/changerepfactor?repfactor=' + repFactor;
		com.impetus.ankush.placeAjaxCall(incUrl, "POST", true, null, function(result) {
			com.impetus.ankush.oClusterMonitoring.cancelActionDialog('increaseRepFactorDialog');
			if (result.output.status) {
				commonActionId = result.output.lasttimestamp;
				com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
			} else {
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
		});
	},

	/* Function for loading page of increasing replication factor logs */
	loadIncreaseRepFactorLog : function() {
		$('#content-panel').sectionSlider('addChildPanel',{
							url : baseUrl+ '/oClusterMonitoring/increaseRepFactorLog',
							method : 'get',
							params : {},
							title : 'Increase Repliccation Factor Logs',
							tooltipTitle : 'Cluster Monitoring',
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.commonActionLogs();
							},
							callbackData : {}
						});

	},
	/* Function for opening dialog of migrate node */
	openMigrateNodeDialog : function() {
		url = baseUrl + '/monitor/' + monitorClusterId + '/migratenodeparams';
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			if (result.output.status) {
				$('#migrateNodeDialog').appendTo('body').modal('show');
				$(".ui-dialog-titlebar").hide();
				$('.ui-dialog :button').blur();
				$('#oldStorageNodeSelectBox').empty();
				$('#newStorageNodeSelectBox').empty();
				for ( var key in result.output.oldStorageNodes) {
					$('#oldStorageNodeSelectBox').append(
							'<option value="' + key + '">'
									+ result.output.oldStorageNodes[key]
									+ '</option>');
				}
				for ( var key in result.output.newStorageNodes) {
					$('#newStorageNodeSelectBox').append(
							'<option value="' + key + '">'
									+ result.output.newStorageNodes[key]
									+ '</option>');
				}
			} else {
				$('#actionErrorDialog').appendTo('body').modal('show');
				$(".ui-dialog-titlebar").hide();
				$('.ui-dialog :button').blur();
				$('#actionErrorDiv').html('').append(result.output.error[0]);
			}
		});

	},

	/* Function for node migaration */
	migrateNode : function() {
		var oldStorageNodeId = $('#oldStorageNodeSelectBox').val();
		var newStorageNodeId = $('#newStorageNodeSelectBox').val();
		url = baseUrl + '/manage/' + monitorClusterId + '/migratenode?oldsnid='+ oldStorageNodeId + '&newsnid=' + newStorageNodeId;
		com.impetus.ankush.placeAjaxCall(url, "POST", true, null, function(result) {
			com.impetus.ankush.oClusterMonitoring.cancelActionDialog('migrateNodeDialog');
			if (result.output.status) {
				commonActionId = result.output.lasttimestamp;
				com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
			} else {
				com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
			}
		});
	},
	/* Function for loading add node page */
	loadAddNode : function(i) {
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl+ '/oClusterMonitoring/oMonitorAddNode',
							method : 'get',
							params : {},
							title : 'Add Nodes',
							tooltipTitle : 'Cluster Monitoring',
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.addNodesPrevious("+ monitorClusterId + ")",
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.getDefaultValue();
							},
							callbackData : {

							}
						});

	},
	addNodesPrevious : function() {
		oClusterAddNodeTable = null;
		com.impetus.ankush.oClusterMonitoring.monitoringTiles(monitorClusterId);
	},

	/* Function for loading add node child drill down page */
	loadNodeDetailMonitoring : function(i) {
		$('#content-panel').sectionSlider('addChildPanel',{
					current : 'login-panel',
					url : baseUrl + '/oClusterMonitoring/addNodeChild',
					method : 'get',
					params : {},
					title : 'Node Detail',
					tooltipTitle : 'Add Node',
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.addNodeChildDetail(data.i);
					},
					callbackData : {
						i : i
					}
				});

	},

	/* Function for loading add node progress page */
	loadNodeAddProgress : function(tileid) {
		tileId = tileid;
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl+ '/oClusterMonitoring/nodeAddProgress',
							method : 'get',
							params : {},
							title : 'Add Node Progress',
							tooltipTitle : 'Cluster Monitoring',
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.nodeAddition(data.tileid);
							},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							callbackData : {
								tileid : tileid
							}
						});

	},

	/* Function for loading add node child page progress */
	loadAddNodeProgressChild : function(i) {

		$('#content-panel').sectionSlider('addChildPanel',{
					current : 'login-panel',
					url : baseUrl + '/oClusterMonitoring/addNodeChildProgress',
					method : 'get',
					params : {},
					title : 'Add Node Child Detail',
					tooltipTitle : 'Add Node Progress',
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.addNodeChildProgressDetail(data.i);
					},
					callbackData : {
						i : i
					}
				});
	},

	/* Function for loading monitoring page form second child */
	loadHomePage : function(i) {
		com.impetus.ankush.removeChild(2);
		com.impetus.ankush.oClusterMonitoring.monitoringTiles(monitorClusterId);
	},

	/* Function for loading page for cluster verify log */
	loadVerifyLogPage : function(i) {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl + '/oClusterMonitoring/verifyLogPage',
							method : 'get',
							title : 'Verify Logs',
							tooltipTitle : 'Cluster Monitoring',
							params : {},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.verifyFunction();
							},
							callbackData : {}
						});

	},

	/* Function for loading page of rebalance logs */
	loadRebalanceLogPage : function() {
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl+ '/oClusterMonitoring/rebalanceLogPage',
							method : 'get',
							params : {},
							title : 'Rebalance Logs',
							tooltipTitle : 'Cluster Monitoring',
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.commonActionLogs();
							},
							callbackData : {

							}
						});

	},
	/* Function for loading page of logs for all actions */
	loadCommonActionLogPage : function(name) {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl+ '/oClusterMonitoring/commonActionLogPage',
							method : 'get',
							params : {},
							title : name,
							tooltipTitle : 'Cluster Monitoring',
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.commonActionLogs(data.name);
							},
							callbackData : {
								name : name
							}
						});

	},
	/* Function for loading page of redistribute logs */
	loadRedistributeLogPage : function(name) {
		$('#content-panel').sectionSlider('addChildPanel',{
					current : 'login-panel',
					url : baseUrl + '/oClusterMonitoring/redistributeLogPage',
					method : 'get',
					params : {},
					title : 'Redistribute Logs',
					tooltipTitle : 'Cluster Monitoring',
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.commonActionLogs(data.name);
					},
					callbackData : {
						name : name
					}
				});

	},
	/* Function for loading configuration page */
	loadManageConfig : function() {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl + '/oClusterMonitoring/manageConfig',
							method : 'get',
							params : {},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							title : 'Configuration',
							tooltipTitle : 'Cluster Monitoring',
							callback : function(data) {
							},
							callbackData : {

							}
						});

	},
	/* Function for loading store page */
	loadStorePage : function(i) {
		$('#content-panel').sectionSlider('addChildPanel',{
					current : 'login-panel',
					url : baseUrl + '/oClusterMonitoring/storeConf/'+monitorClusterId,
				//url : baseUrl + '/oClusterMonitoring/storeConfig',
					method : 'get',
					params : {
					},
					title : 'Store Configuration',
					tooltipTitle : 'Configuration',
					callback : function(data) {
						com.impetus.ankush.oracleSetupDetail.setupDetailValuePopulate(monitorClusterId);
					//	com.impetus.ankush.oClusterMonitoring.populateStoreConfigurationPage();
					},
					callbackData : {
					}
				});

	},

	/* Function for populating store configuration page */
	populateStoreConfigurationPage : function() {
		url = baseUrl + '/monitor/' + monitorClusterId + '/storeconfiguration';
		com.impetus.ankush.placeAjaxCall(url,"GET",	true,	null,function(result) {
							$('#storeLabel').html(result.output.clusterName);
							$('#dataCenterLabel').html(result.output.oracleNoSQLConf.datacenterName);
							$('#topologyLabel').html(result.output.oracleNoSQLConf.topologyName);
							$('#repFactorLabel').html(result.output.oracleNoSQLConf.replicationFactor);
							$('#partitionLabel').html(result.output.oracleNoSQLConf.partitionCount);
							$('#regPortLable').html(result.output.oracleNoSQLConf.registryPort);
							$('#haPortRange').html(result.output.oracleNoSQLConf.haPortRangeStart+ "-" + result.output.oracleNoSQLConf.haPortRangeEnd);
							$('#baseDirLabel').html(result.output.oracleNoSQLConf.basePath);
							$('#installationDirLabel').html(result.output.oracleNoSQLConf.installationPath);
							$('#dataDirLabel').html(result.output.oracleNoSQLConf.dataPath);
							$('#ntpLabel').html(result.output.oracleNoSQLConf.ntpServer);
							$('#userNameLabel').html(result.output.username);
							$('#passwordLabel').html(result.output.password);
						});
	},
	/* Function for loading admin param page */
	loadAdminParamPage : function(i) {
		$('#content-panel').sectionSlider('addChildPanel', {
			current : 'login-panel',
			url : baseUrl + '/oClusterMonitoring/adminParam',
			method : 'get',
			params : {},
			title : 'Admin Parameters',
			tooltipTitle : 'Configuration',
			callback : function(data) {
				com.impetus.ankush.oClusterMonitoring.populateAdminParamPage();
			},
			callbackData : {}
		});
	},

	/* Function for populating admin parameters page */
	populateAdminParamPage : function() {
		url = baseUrl + '/monitor/' + monitorClusterId + '/adminparams';
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
							if (!result.output.status) {
								$('#adminParamError').empty();
								var count = 0;
								for ( var i = 0; i < result.output.error.length; i++) {
									count++;
									$("#adminParamError").append(
											"<div class='errorLineDiv'><a href='#' style='color: #5682C2;'>"
													+ count + ". "
													+ result.output.error[i]
													+ "</a></div>");
								}
								$('#adminParamButton').html('').text(count + ' Error');
								$('#adminParamError').show();
								$('#adminParamButton').show();
								return;
							}
							result = result.output;
							adminParamJson = result;
							for (key in result) {
								labelName = camelCaseKey(key);
								labelVal = result[key];
								statusKey = "status";
								if (labelName.toLowerCase() == statusKey.toLowerCase()) {
									continue;
								}
								$('#adminParamBody').append(
												"<div class='row-fluid mrgnlft8'>"
														+ "<div class='span2 text-right'><label class='form-label'>"
														+ labelName
														+ ":</label></div>"
														+ "<div id='"
														+ key
														+ "' class='span4 text-left editable label-black' style='padding-top:15px; margin-left:8px;'>"
														+ labelVal + "</div>"
														+ "</div>");
							}
							$('.editable').textEditable({
								parentClass : 'dummy'
							});
							$('.dummy').css({
								'padding-top' : '0px'
							});
						});
	},

	/* Common function for saving parameters */
	saveParam : function(param) {
		var url = {
			'adminParam' : baseUrl + "/manage/" + monitorClusterId+ "/config/adminparams",
			'policyParam' : baseUrl + "/manage/" + monitorClusterId+ "/config/policyparams",
			'repNodeParam' : baseUrl + "/manage/" + monitorClusterId+ "/config/allrepnodeparams",
		};
		var json = {
			'adminParam' : adminParamJson,
			'policyParam' : policyParamJson,
			'repNodeParam' : repNodeParamJson
		};
		var data = {};
		for (key in json[param]) {
			statusKey = "status";
			if (key.toLowerCase() == statusKey.toLowerCase()) {
				continue;
			}
			var fieldData = $('#' + key).text();
			if (fieldData == 'Empty') {
				fieldData = '';
			}
			data[key] = fieldData;
		}
		var result = com.impetus.ankush.placeAjaxCall(url[param], "POST", true,
				data);
		com.impetus.ankush.removeChild(3);
	},

	/* Function for saving replication node parameters */
	saveRepNodeChildParam : function(repGpId, repNodeId) {
		url = baseUrl + '/manage/' + monitorClusterId+ '/config/repnodeparams?rgid=' + repGpId + '&rnid='+ repNodeId;
		var data = {};
		for (key in repNodeChildParamJson) {
			statusKey = "status";
			if (key.toLowerCase() == statusKey.toLowerCase()) {
				continue;
			}
			var fieldData = $('#' + key).text();
			if (fieldData == 'Empty') {
				fieldData = '';
			}
			data[key] = fieldData;
		}
		var result = com.impetus.ankush.placeAjaxCall(url, "POST", true, data);
		com.impetus.ankush.removeChild(3);

	},

	/* Function for saving storage node parameters */
	saveStorageNodeChildParam : function(storageNodeId) {
		url = baseUrl + '/manage/' + monitorClusterId+ '/config/storagenodeparams?id=' + storageNodeId;
		var data = {};
		for (key in storageNodeChildParamJson) {
			statusKey = "status";
			if (key.toLowerCase() == statusKey.toLowerCase()) {
				continue;
			}
			var fieldData = $('#' + key).text();
			if (fieldData == 'Empty') {
				fieldData = '';
			}
			data[key] = fieldData;
		}
		var result = com.impetus.ankush.placeAjaxCall(url, "POST", true, data);
		com.impetus.ankush.removeChild(3);
	},

	/* CommonFunction to be called on cancel button click of parameters page */
	cancelParam : function(param) {
		var json = {
			'adminParam' : adminParamJson,
			'policyParam' : policyParamJson,
			'repNodeParam' : repNodeParamJson,
			'alertParam' : alertResult
		};
		for (key in json[param]) {
			$('#' + key).html('').text(json[param][key]);
		}
		com.impetus.ankush.removeChild(3);
	},
	/* Function for loading policy parameter page */
	loadPolicyParamPage : function() {
		$('#content-panel').sectionSlider('addChildPanel', {
			current : 'login-panel',
			url : baseUrl + '/oClusterMonitoring/policyParam',
			method : 'get',
			params : {},
			title : 'Policy Parameters',
			tooltipTitle : 'Configuration',
			callback : function(data) {
				com.impetus.ankush.oClusterMonitoring.policyParam();
			},
			callbackData : {

			}
		});
	},
	/* Function for loading repplication node parameter page */
	loadRepNodeParamPage : function(i) {
		$('#content-panel').sectionSlider(
				'addChildPanel',
				{
					current : 'login-panel',
					url : baseUrl + '/oClusterMonitoring/repNodeParam',
					method : 'get',
					params : {},
					title : 'Replication Node Parameters',
					tooltipTitle : 'Configuration',
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.populateRepNodeParamPage();
					},
					callbackData : {}
				});
	},

	/* Function for populating replication page parameters */
	populateRepNodeParamPage : function() {
		url = baseUrl + '/monitor/' + monitorClusterId + '/allrepnodeparams';
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
							repNodeParamJson = result.output;
							for (key in repNodeParamJson) {
								labelName = camelCaseKey(key);
								labelVal = repNodeParamJson[key];
								statusKey = "status";
								if (labelName.toLowerCase() == statusKey.toLowerCase()) {
									continue;
								}
								$('#repNodeParamBody').append(
												"<div class='row-fluid'>"
														+ "<div class='span2 text-right'><label class='form-label'>"
														+ labelName
														+ ":</label></div>"
														+ "<div id='"
														+ key
														+ "' class='span4 text-left editable' style='padding-top:15px;color:black;'>"
														+ labelVal + "</div>"
														+ "</div>");
							}
							$('.editable').textEditable();
						});
	},
	/* Function for loading alerts page */
	loadAlertPage : function() {
		$('#content-panel').sectionSlider('addChildPanel', {
			current : 'login-panel',
			url : baseUrl + '/oClusterMonitoring/alertPage',
			method : 'get',
			params : {},
			title : 'Alerts Parameters',
			tooltipTitle : 'Configuration',
			callback : function(data) {
				com.impetus.ankush.oClusterMonitoring.clusterAlerts();
			},
			callbackData : {}
		});
	},
	/* Function for loading plan history page */
	loadPlanHistoryPage : function() {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl + '/oClusterMonitoring/planHistory',
							method : 'get',
							params : {},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							title : 'Plan History',
							tooltipTitle : 'Cluster Monitoring',
							callback : function(data) {
								$('#planLink').attr('disabled', 'disabled');
								com.impetus.ankush.oClusterMonitoring.planHistory();
							},
							callbackData : {}
						});
	},
	/* Function for loading plan history child page */
	loadPlanHistoryChildPage : function(id) {
		$('#content-panel').sectionSlider('addChildPanel',{
					current : 'login-panel',
					url : baseUrl + '/oClusterMonitoring/planHistoryChild',
					method : 'get',
					params : {},
					title : 'Plan History Child',
					tooltipTitle : 'Plan History',
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.planHistoryChild(data.id);
					},
					callbackData : {
						id : id
					}
				});
	},
	/* Function for loading event page */
	loadEventPage : function() {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl + '/oClusterMonitoring/eventPage',
							method : 'get',
							params : {},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							title : 'Events',
							tooltipTitle : 'Cluster Monitoring',
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.eventView();
							},
							callbackData : {}
						});
	},
	/* Function for loading event child page */
	loadEventChildPage : function(i) {
		$('#content-panel').sectionSlider('addChildPanel', {
			current : 'login-panel',
			url : baseUrl + '/oClusterMonitoring/eventChildPage',
			method : 'get',
			params : {},
			title : 'Events Child',
			tooltipTitle : 'Events',
			callback : function(data) {
				com.impetus.ankush.oClusterMonitoring.eventViewChild(i);
			},
			callbackData : {
				i : i
			}
		});
	},
	/* Function for loading store event page */
	loadStoreEventPage : function() {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl+ '/oClusterMonitoring/storeEventPage',
							method : 'get',
							params : {},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							title : 'Store Events',
							tooltipTitle : 'Cluster Monitoring',
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.storeEventView();
							},
							callbackData : {}
						});
	},
	/* Function for loading audit trail page */
	loadAuditTrailPage : function() {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							url : baseUrl + '/oClusterMonitoring/auditTaril',
							method : 'get',
							params : {},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							title : 'Audit Trail',
							tooltipTitle : 'Cluster Monitoring',
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.populateAuditTrailPage();
							},
							callbackData : {}
						});
	},

	/* Function for populating audit-trail page */
	populateAuditTrailPage : function() {
		url = baseUrl + '/monitor/' + monitorClusterId + '/audits';
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			auditTrailJson = result.output;
			var tableObj = new Array();
			for ( var i = 0; i < result.output.length; i++) {
				var date1 = new Date(parseInt(result.output[i].date));
				var date2 = $.format.date(date1, "dd/MM/yyyy hh:mm:ss");
				var rowObj = new Array();
				rowObj.push(result.output[i].source);
				rowObj.push(result.output[i].type);
				rowObj.push(result.output[i].propertyName);
				rowObj.push(result.output[i].propertyValue);
				rowObj.push(result.output[i].username);
				rowObj.push(date2);
				rowObj.push(i);
				tableObj.push(rowObj);
			}
			auditTrailTable.fnAddData(tableObj);
		});
	},
	/* Function for loading audit trail child page */
	loadAuditTrailChildPage : function(rowNo) {
		$('#content-panel').sectionSlider('addChildPanel',{
					current : 'login-panel',
					url : baseUrl+ '/oClusterMonitoring/auditTaril/auditTarilChild',
					method : 'get',
					params : {},
					title : 'Audit Trail Child',
					tooltipTitle : 'Audit Trail',
					callback : function(data) {
						com.impetus.ankush.oClusterMonitoring.populateAuditTrailChildPage(rowNo);
					},
					callbackData : {}
				});
	},

	/* Function for populating audit-trail child page */
	populateAuditTrailChildPage : function(rowNo) {
		for ( var i = 0; i < auditTrailJson.length; i++) {
			if (i == rowNo) {
				var obj = auditTrailJson[i];
				for (key in obj) {
					var object = obj[key];
					if (key == 'date') {
						var date1 = new Date(parseInt(obj[key]));
						var date2 = $.format.date(date1, "dd/MM/yyyy hh:mm:ss");
						object = date2;
					}
					$('#auditTrailChildBody').append(
									"<div class='row-fluid'>"
											+ "<div class='span2 text-right'><form class='form-label'>"
											+ key
											+ ":</form></div>"
											+ "<div class='span4 text-left' style='padding-top:12px;color:black;'>"
											+ object + "</div>" + "</div>");
				}
				break;
			}
		}
	},
	/* Function for loading logs page */
	loadLogsPage : function() {
		com.impetus.ankush.oClusterMonitoring.parentPageCallRermove();
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl + '/oClusterMonitoring/logPage',
							method : 'get',
							params : {},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							title : 'Logs',
							tooltipTitle : 'Cluster Monitoring',
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.logViewByTypeNodeFile();
							},
							callbackData : {

							}
						});
	},

	closeDialog : function() {
		$('#redistributeDialog').dialog('close');
	},

	/*
	 * stopNodeAddDialog : function() { $("#addNodeStop").dialog({ modal : true,
	 * resizable : false, dialogClass : 'alert', width : 500, draggable : true,
	 * modal : true, position : 'center', }); $('.ui-dialog-titlebar').hide();
	 * $("#cancelDeleteButton").click(function() {
	 * $('#addNodeStop').dialog('close'); }); },
	 * 
	 * stopAddNode : function() { $('#addNodeStop').dialog('close'); var
	 * stopAddNodeUrl = baseUrl + '/cluster/remove/' + currentClusterId;
	 * $.ajax({ 'type' : 'DELETE', 'url' : stopAddNodeUrl, 'contentType' :
	 * 'application/json', 'dataType' : 'json', 'success' : function(result) { },
	 * error : function(data) { } }); },
	 */
	/*
	 * Function to check/uncheck check box on click of header check box in add
	 * node table
	 */
	checkAllNodeMonitor : function(elem) {
		var val = $('input:[name=nodeCheckHead]').is(':checked');
		if (val == true) {
			for ( var i = 0; i < detectedNodes.nodes.length; i++) {
				if ($('#nodeCheck' + i).is(':disabled') == false) {
					$('#nodeCheck' + i).prop("checked", true);
					$('#adminCheckBox' + i).prop("disabled", false);
				}
			}
		} else {
			$('.nodeCheckBox').prop("checked", false);
			$('.adminCheck').prop("disabled", true);
			$('.adminCheck').prop("checked", false);
		}
	},

	/*
	 * Function to check/uncheck header check box on click of each check box in
	 * add node table
	 */
	nodeCheckBoxMonitor : function(i) {
		if ($('#nodeCheck' + i).is(':checked')) {
			$('#adminCheckBox' + i).prop("disabled", false);
		} else {
			$('#adminCheckBox' + i).prop("disabled", true);
			$('.adminCheck').prop("checked", false);
		}
		if ($("#addNodeIpTable .nodeCheckBox:checked").length == ($("#addNodeIpTable .nodeCheckBox").length - disableNodeCount)) {
			$("#nodeCheckHead").prop("checked", true);
		} else {
			$("#nodeCheckHead").prop("checked", false);
		}
	},

	adminCheck : function(i) {
		/*
		 * if ($('#adminCheckBox' + i).is(':checked')) { $('#adminPort' +
		 * i).show(); } else { $('#adminPort' + i).hide(); }
		 */
	},

	/* Function for clienT-side validations of add node */
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
			com.impetus.ankush.oClusterMonitoring.getAllNodes();
		} else {
			if ($("#ipModeGroupBtn .active").data("value") == 0) {
				if (!com.impetus.ankush.validation.empty($('#ipRangeAddNode').val())) {
					// $('#ipRangeAddNode').addClass('error-box');
					errorCount++;
					errorMsg = 'IP Range Field Empty';
					com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('ipRangeAddNode', 'IP Range cannot be empty');
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

				} else {
					com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('ipRangeAddNode', 'Enter IP Range.');
					$('#ipRangeAddNode').removeClass('error-box');
				}
			}
			if ($("#ipModeGroupBtn .active").data("value") == 1) {
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

	/* Function to toggle between text-box on click of respective radio button */
	toggleAuthenticate : function() {
		if ($("#ipModeGroupBtn .active").data("value") == 1) {
			$('#ipRangeDiv').show();
			$('#fileUploadDiv').hide();
		} else {
			$('#fileUploadDiv').show();
			$('#ipRangeDiv').hide();
		}
	},

	/* Function to enable/disable button */
	enableButton : function(id) {
		$('#' + id).attr('disabled', false);
		$('#ipRangeAddNode').change(function() {
			if ($('#ipRangeAddNode').val() == '') {
				$('#' + id).attr('disabled', true);
			}
		});
	},

	/* Function for retrieving default values for nodes */
	getDefaultValue : function() {
		$("#ipRangeAddNode").tooltip();
		$("#filePathAddNode").tooltip();
		var getDefaultUrl = baseUrl + '/app/metadata/Oracle NoSQL Database';
		com.impetus.ankush.placeAjaxCall(getDefaultUrl, "GET", true, null,function(result) {
					defaultValuesNode = result.output['Oracle NoSQL Database'];
				});
	},

	/* Function for uploading file for node retrieving */
	getNodesUpload : function() {
		uploadFileFlag = true;
		var uploadUrl = baseUrl + '/uploadFile';
		$('#fileBrowseAddNode').click();
		$('#fileBrowseAddNode').change(function() {
					$('#filePathAddNode').val($('#fileBrowseAddNode').val());
					com.impetus.ankush.oClusterMonitoring.uploadFile(uploadUrl,"fileBrowseAddNode", function(data) {
								var htmlObject = $(data);
								var jsonData = JSON.parse(htmlObject.text());
								uploadFilePath = jsonData.output;
							});
				});
	},

	/* Function for node retrieving and populating in node table */
	getAllNodes : function() {
		$('#retrieveAddNodeButton').button();
		$('#retrieveAddNodeButton').button('loading');
		$('#inspectNodeBtn').attr('disabled','disabled');
		getNodeFlag = true;
		disableNodeCount = 0;
		var nodeData = {};
		var addId;
		if ($("#ipModeGroupBtn .active").data("value") == 1) {
			nodeData.nodePattern = uploadFilePath;
			nodeData.isFileUploaded = true;
		} else {
			nodeData.nodePattern = $.trim($('#ipRangeAddNode').val());
			nodeData.isFileUploaded = false;
		}
		nodeData.clusterId = monitorClusterId;
		var nodeUrl = baseUrl + '/cluster/detectNodes';
		if (oClusterAddNodeTable != null) {
			oClusterAddNodeTable.fnClearTable();
		}
		$("#addNodeTableDiv").show();
		$("#errorDivMainAddNode").html('');
		var herfFunction;
		var tooltipMsg;
		var divId;
		$.ajax({
					'type' : 'POST',
					'url' : nodeUrl,
					'contentType' : 'application/json',
					'data' : JSON.stringify(nodeData),
					'dataType' : 'json',
					'success' : function(result) {
						$('#inspectNodeBtn').removeAttr('disabled');
						$('#retrieveAddNodeButton').button('reset');
						$('#validateError').html('');
						$("#errorDivMainAddNode").empty();
						detectedNodes = result.output;
						$('#addNodeIpTable tbody tr').css('border-bottom','1px solid #E1E3E4"');

						if ($("#ipModeGroupBtn .active").data("value") == 1) {
							divId = 'filePathAddNode';
							tooltipMsg = 'IP pattern is not valid in file';
							herfFunction = "javascript:com.impetus.ankush.oClusterMonitoring.focusDiv(\""+ divId + "\")";

						} else {
							divId = 'ipRangeAddNode';
							tooltipMsg = 'IP pattern is not valid.';
							herfFunction = "javascript:com.impetus.ankush.oClusterMonitoring.focusDiv(\""+ divId + "\")";
						}

						if (detectedNodes.nodes.length == 0) {
							errorMsg = detectedNodes.error;
							flag = true;
							$("#errorDivMainAddNode").append(
									"<div class='errorLineDiv'><a href="
											+ herfFunction
											+ " style='color: #5682C2;'>1. "
											+ errorMsg + "</a></div>");
							com.impetus.ankush.oClusterMonitoring.tooltipMsgChange(divId, tooltipMsg);
							$("#errorDivMainAddNode").show();
							$('#validateError').show().html('1 Error');
						} else {
							if ($("#ipModeGroupBtn .active").data("value") == 1) {
								com.impetus.ankush.oClusterMonitoring.tooltipOriginal('filePathAddNode','Browse File.');
								$('#filePathAddNode').removeClass('error-box');
							} else {
								com.impetus.ankush.oClusterMonitoring.tooltipOriginal('ipRangeAddNode','Enter IP pattern.');
								$('#ipRangeAddNode').removeClass('error-box');
							}
							$("#errorDivMainAddNode").hide();
							$('#validateError').hide();
						}
						for ( var i = 0; i < detectedNodes.nodes.length; i++) {
							if (detectedNodes.nodes[i][1] == "false") {
								addId = oClusterAddNodeTable.fnAddData([
												'<input type="checkbox" name="" value=""  id="nodeCheck'
														+ i
														+ '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.oClusterMonitoring.nodeCheckBoxMonitor();"/>',
												detectedNodes.nodes[i][0],
												'<input id="adminCheckBox'
														+ i
														+ '" type="checkbox" onclick="com.impetus.ankush.oClusterMonitoring.adminCheck();"><a href="#" class="editableLabel" id="adminPort'
														+ i
														+ '"><label id=""></label></a>',
												'<a class="editableLabel" id="storageDirs'
														+ i + '"></a>',
												'<a class="editableLabel" id="capacity'
														+ i + '">-</a>',
												'<a class="editableLabel" id="cpu'
														+ i + '">-</a>',
												'<a class="editableLabel" id="memory'
														+ i + '">-</a>',
												'<a class="editableLabel" id="registryPort'
														+ i + '">-</a>',
												'<div><div style="float:left"><a class="editableLabel" id="haPort1'
														+ i + '">-</a></div>',
												'<div style="float:left"><a class="editableLabel" id="haPort2'
														+ i
														+ '">-</a></div></div>',
												'<a href="##" onclick="com.impetus.ankush.oClusterMonitoring.loadNodeDetailMonitoring('
														+ i
														+ ');"><img id="navigationImg-'
														+ i
														+ '" src="'
														+ baseUrl
														+ '/public/images/icon-chevron-right.png" /></a>' ]);
							} else {
								addId = oClusterAddNodeTable.fnAddData([
												'<input type="checkbox" name="" value=""  id="nodeCheck'
														+ i
														+ '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.oClusterMonitoring.nodeCheckBoxMonitor('
														+ i + ');"/>',
												detectedNodes.nodes[i][0],
												'<input id="adminCheckBox'
														+ i
														+ '" class="adminCheck" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.oClusterMonitoring.adminCheck('
														+ i
														+ ');"><a style=margin:left:5px;" href="#" class="editableLabel" id="adminPort'
														+ i
														+ '">'
														+ defaultValuesNode.adminPort
														+ '</a>',
												'<a class="editableLabel" id="storageDirs'
														+ i + '"></a>',
												'<a class="editableLabel" id="capacity'
														+ i
														+ '">'
														+ defaultValuesNode.capacity
														+ '</a>',
												'<a class="editableLabel" id="cpu'
														+ i
														+ '">'
														+ defaultValuesNode.cpuNo
														+ '</a>',
												'<a class="editableLabel" id="memory'
														+ i
														+ '">'
														+ defaultValuesNode.memory
														+ '</a>',
												'<a class="editableLabel" id="registryPort'
														+ i
														+ '">'
														+ defaultValuesNode.registryPort
														+ '</a>',
												'<div><div style="float:left"><a class="editableLabel" id="haPort1'
														+ i
														+ '">'
														+ defaultValuesNode.haPortRangeStart
														+ '</a></div>',
												'<div style="float:left"><a class="editableLabel" id="haPort2'
														+ i
														+ '">'
														+ defaultValuesNode.haPortRangeEnd
														+ '</a></div></div>',
												'<a href="##" onclick="com.impetus.ankush.oClusterMonitoring.loadNodeDetailMonitoring('
														+ i
														+ ');"><img id="navigationImg-'
														+ i
														+ '" src="'
														+ baseUrl
														+ '/public/images/icon-chevron-right.png" /></a>' ]);
							}
							var theNode = oClusterAddNodeTable.fnSettings().aoData[addId[0]].nTr;
							theNode.setAttribute('id', 'node'+ detectedNodes.nodes[i][0].split('.').join('_'));
							if (detectedNodes.nodes[i][1] != true
									|| detectedNodes.nodes[i][2] != true
									|| detectedNodes.nodes[i][3] != true) {
								rowId = detectedNodes.nodes[i][0].split('.').join('_');
								$('#node' + rowId).addClass('error-row');
								$('td', '#node' + rowId).addClass('error-row');
								$('#nodeCheck' + i).attr('disabled', true);
								$('#adminCheckBox' + i).attr('disabled', true);
								disableNodeCount++;
							} else {
								$('#nodeCheck' + i).prop("checked", true);
							}
							if (detectedNodes.nodes[i][1] != true) {
								var status = 'Unavailable';
								$("#errorDivMainAddNode").append(
										"<div class='errorLineDiv'><a href='#addNodeIpTable' style='color: #5682C2;' >"
												+ disableNodeCount + ". Node "
												+ detectedNodes.nodes[i][0]
												+ " " + status + "</a></div>");
							} else if (detectedNodes.nodes[i][2] != true) {
								var status = 'Unreachable';
								$("#errorDivMainAddNode").append(
										"<div class='errorLineDiv'><a href='#addNodeIpTable' style='color: #5682C2;' >"
												+ disableNodeCount + ". Node "
												+ detectedNodes.nodes[i][0]
												+ " " + status + "</a></div>");
							} else if (detectedNodes.nodes[i][3] != true) {
								var status = 'Unauthenticated';
								$("#errorDivMainAddNode").append(
										"<div class='errorLineDiv'><a href='#addNodeIpTable' style='color: #5682C2;' >"
												+ disableNodeCount + ". Node "
												+ detectedNodes.nodes[i][0]
												+ " " + status + "</a></div>");
							}
						}
						if (disableNodeCount != detectedNodes.nodes.length) {
							$('#nodeCheckHead').prop("checked", true);
						}
						$('.editableLabel').editable({
							type : 'text',
						});
					}
				});

	},

	/*
	 * Function for initializing toggle button of node selecting according to
	 * status
	 */
	toggleButtonInitialize : function() {
		$('#toggle-button').toggleButtons({
			onChange : function($el, status, e) {
				com.impetus.ankush.oClusterMonitoring.toggleDatatable(status);
			},
			width : 150,
			height : 25,
			font : {
				'font-size' : '12px',
				'font-style' : 'franklin gothik book'
			},
			animated : false,
			transitionspeed : 0, // Accepted values float or "percent" [ 1,
			// 0.5, "150%" ]
			label : {
				enabled : "All",
				disabled : "Selected"
			},
			style : {
				// Accepted values ["primary", "danger", "info", "success",
				// "warning"] or nothing
				enabled : "primary",
				disabled : "primary",
				custom : {
					enabled : {
						background : "#FF00FF",
						gradient : "#D300D3",
						color : "#FFFFFF"
					},
					disabled : {
						background : "#FF00FF",
						gradient : "#D300D3",
						color : "#FFFFFF"
					}
				}
			}
		});
		$('#toggle-button').toggleButtons('toggleActivation'); // to toggle the
		// disabled
		// status

	},

	/* Function for data loading in table according to toggle button click */
	toggleDatatable : function(status) {
		$('.notSelected').show();
		$('.notSelected').removeClass('notSelected');
		$('.selected').removeClass('selected');
		if (status == 'All') {
			$('.notSelected').show();
			$('.notSelected').removeClass('notSelected');
			$('.selected').removeClass('selected');
		} else if (status == 'Selected') {
			for ( var i = 0; i < detectedNodes.nodes.length; i++) {
				var rowId = detectedNodes.nodes[i][0].split('.').join('_');
				if ($('#nodeCheck' + i).attr('checked')) {
					$('#node' + rowId).addClass('selected');
				} else
					$('#node' + rowId).addClass('notSelected');
			}
			$('.notSelected').hide();
		} else if (status == 'Available') {
			for ( var i = 0; i < detectedNodes.nodes.length; i++) {
				var rowId = detectedNodes.nodes[i][0].split('.').join('_');
				if (!$('#node' + rowId).hasClass('error-row')) {
					$('#node' + rowId).addClass('selected');
				} else
					$('#node' + rowId).addClass('notSelected');
			}
			$('.notSelected').hide();
		} else if (status == 'Error') {
			for ( var i = 0; i < detectedNodes.nodes.length; i++) {
				var rowId = detectedNodes.nodes[i][0].split('.').join('_');
				if ($('#node' + rowId).hasClass('error-row')) {
					$('#node' + rowId).addClass('selected');
				} else
					$('#node' + rowId).addClass('notSelected');
			}
			$('#nodeCheckHead').removeAttr('checked');
			$('.notSelected').hide();
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
				data.nodePorts[ip] = com.impetus.ankush.oClusterMonitoring.getIpPorts(this);
			}
		});
		data.clusterId=monitorClusterId;
		com.impetus.ankush.inspectNodesCall(data,id,'retrieveAddNodeButton');
	},
	getIpPorts : function(elem){
		var portArray = [];
		var isAdminChecked = $(elem).parent().next().next().children().first().is(':checked');
		if(isAdminChecked){
			var adminPort = $(elem).parent().next().next().children().next().text();
			portArray.push(adminPort);
		}
		var regPort = $("td:nth-child(8)", $(elem).parents('tr')).text();
		portArray.push(regPort);
		var haPort1 = $("td:nth-child(9)", $(elem).parents('tr')).text();
		var haPort2 = $("td:nth-child(10)", $(elem).parents('tr')).text();
		portArray.push(haPort1+'-'+haPort2);
		return portArray;
	},
	/* Function for populating add node child page detail */
	addNodeChildDetail : function(node) {
		if ($('#nodeCheck' + node).is(':disabled')) {
			$('#nodeStatusChild').html('').text('Unavailable');
		} else {
			$('#nodeStatusChild').html('').text('Available');
		}
		if ($('#adminCheckBox' + node).is(':checked')) {
			$('#adminStatusChild').html('').text("Yes");
		} else {
			$('#adminStatusChild').html('').text("No");
		}
		$('#nodeDetailHeadChild').html('').html(detectedNodes.nodes[node][0]);
		$('#nodeCapacityChild').html('').text($('#capacity' + node).text());
		$('#nodeCpuChild').html('').text($('#cpu' + node).text());
		$('#nodeMemoryChild').html('').text($('#memory' + node).text());
		$('#nodeRegPortChild').html('').text($('#registryPort' + node).text());
		$('#nodeHA1Child').html('').text($('#haPort1' + node).text());
		$('#nodeHA2Child').html('').text($('#haPort2' + node).text());
	},

	/* Function for client-side validations of add node */
	addNodeValidate : function() {
		var errorMsg = '';
		var errorCount = 0;
		if ($('#ipRangeAddNode').val() == '' && detectedNodes == null) {
			$('#validateError').show().empty().text('1 Error');
			var divId = 'ipRangeAddNode';
			errorMsg = 'Provide IP for node retriving.';
			$("#errorDivMainAddNode").show().empty().append(
							"<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.oClusterMonitoring.focusDiv(\""
									+ divId
									+ "\")' style='color: #5682C2;' >1. "
									+ errorMsg + "</a></div>");
			return;
		}
		if ($('#addNodeIpTable .nodeCheckBox:checked').length < 1) {
			errorMsg = 'Select atlest one node.';
			$('#validateError').show().empty().text('1 Error');
			$("#errorDivMainAddNode").empty().show().append(
							"<div class='errorLineDiv'><a href='#addNodeIpTable' style='color: #5682C2;' >1. "
									+ errorMsg + "</a></div>");
			return;
		}

		if ($('#addNodeIpTable .nodeCheckBox:checked').length > 0) {
			var count = 0;
			var adminPort = 0;
			var status = false;
			$('.nodeCheckBox').each(function() {
								status = false;
								if ($('#nodeCheck' + count).is(':checked')) {
									errorMsg = '';
									if ($('#adminCheckBox' + count).is(':checked')) {
										if (!com.impetus.ankush.validation.empty($('#adminPort' + count).text())) {
											errorMsg = errorMsg + ' Admin port field Empty';
											status = true;
										} else if (!com.impetus.ankush.validation.numeric($('#adminPort' + count).text())) {
											errorMsg = errorMsg + ' Admin port field must be Numeric';
											status = true;
										} else if (!com.impetus.ankush.validation.oPort($('#adminPort' + count).text())) {
											errorMsg = errorMsg + ' Admin Port field must be between 1024-65535';
											status = true;
										} else {
											adminPort = $('#adminPort' + count).text();
										}
									}
									if (!com.impetus.ankush.validation.empty($('#capacity' + count).text())) {
										errorMsg = errorMsg	+ ' Capacity field Empty';
										status = true;
									} else if (!com.impetus.ankush.validation.numeric($('#capacity' + count).text())) {
										errorMsg = errorMsg + ' Capacity field must be Numeric';
										status = true;
									}
									if (!com.impetus.ankush.validation.empty($('#cpu' + count).text())) {
										errorMsg = errorMsg + ' CPU field Empty';
										status = true;
									} else if (!com.impetus.ankush.validation.numeric($('#cpu' + count).text())) {
										errorMsg = errorMsg+ ' CPU field must be Numeric';
										status = true;
									}
									if (!com.impetus.ankush.validation.empty($('#memory' + count).text())) {
										errorMsg = errorMsg + ' Memory field Empty';
										status = true;
									} else if (!com.impetus.ankush.validation.numeric($('#memory' + count).text())) {
										errorMsg = errorMsg + ' Memory field must be Numeric';
										status = true;
									}
									if (!com.impetus.ankush.validation.empty($('#registryPort' + count).text())) {
										errorMsg = errorMsg + ' Registry Port field Empty';
										status = true;
									} else if (!com.impetus.ankush.validation.numeric($('#registryPort' + count).text())) {
										errorMsg = errorMsg + ' Registry Port field must be Numeric';
										status = true;
									} else if (!com.impetus.ankush.validation.oPort($('#registryPort' + count).text())) {
										errorMsg = errorMsg + ' Registry Port field must be between 1024-65535';
										status = true;
									}
									if (!com.impetus.ankush.validation.empty($('#haPort1' + count).text())) {
										errorMsg = errorMsg + ' HA Port Start field Empty';
										status = true;
									} else if (!com.impetus.ankush.validation.numeric($('#haPort1' + count).text())) {
										errorMsg = errorMsg + ' HA Port Start field must be Numeric';
										status = true;
									} else if (!com.impetus.ankush.validation.oPort($('#haPort1' + count).text())) {
										errorMsg = errorMsg + ' HA Port Start field must be between 1024-65535';
										status = true;
									}
									if (!com.impetus.ankush.validation.empty($('#haPort2' + count).text())) {
										errorMsg = errorMsg+ ' HA Port End field Empty';
										status = true;
									} else if (!com.impetus.ankush.validation.numeric($('#haPort2' + count).text())) {
										errorMsg = errorMsg+ ' HA Port End field must be Numeric';
										status = true;
									} else if (!com.impetus.ankush.validation.oPort($('#haPort2' + count).text())) {
										errorMsg = errorMsg+ ' HA Port End must be between 1024-65535';
										status = true;
									}
									if (parseInt($.trim($("#haPort2" + count).text())) < parseInt($.trim($("#haPort1" + count).text()))) {
										errorMsg = errorMsg+ ' HA Port Range End should be greater or equal to HA Port Range Start.';
										status = true;
									}
									var portStatus = com.impetus.ankush.validation.allPortCheck($("#registryPort" + count).text(), adminPort, $(
													"#haPort1" + count).text(),
													$("#haPort2" + count).text(), "");
									if (portStatus != 0) {
										if (portStatus == 1) {
											errorMsg = errorMsg+ " Registry Port and Admin Ports are same";
											status = true;
										}
										if (portStatus == 2) {
											errorMsg = errorMsg+ " Registry Port falls between HA Port Range";
											status = true;
										}
										if (portStatus == 3) {
											errorMsg = errorMsg+ " Admin Port falls between HA Port Range";
											status = true;
										}
									}
								}
								if (status) {
									errorCount++;
									var nodeIp = detectedNodes.nodes[count][0];
									$("#errorDivMainAddNode").append(
											"<div class='errorLineDiv'><a href='#addNodeIpTable' style='color: #5682C2;'>"
													+ errorCount + ".For "
													+ nodeIp + ": " + errorMsg
													+ "</a></div>");
								}
								count++;
							});
		}
		if (errorCount > 0) {
			$("#errorDivMainAddNode").show();
			$('#validateError').show().html(errorCount + ' Error');
		} else {
			com.impetus.ankush.oClusterMonitoring.nodeAdd();
		}
	},

	/* Function for node addition data prepration and post */
	nodeAdd : function() {
		var node = null;
		var adNodeData = {};
		adNodeData.newNodes = new Array();
		adNodeData.technology = 'Oracle NoSQL Database';
		adNodeData["@class"] = "com.impetus.ankush.oraclenosql.OracleNoSQLClusterConf";
		for ( var i = 0; i < detectedNodes.nodes.length; i++) {
			if ($('#nodeCheck' + i).is(':checked')) {
				if ($("#storageDirs" + i).text().trim() == 'Empty') {
					storageDirs = '';
				} else {
					storageDirs = $("#storageDirs" + i).text().trim();
				}
				if ($('#adminCheckBox' + i).is(':checked')) {
					node = {
						"publicIp" : detectedNodes.nodes[i][0],
						"privateIp" : detectedNodes.nodes[i][0],
						"admin" : true,
						"adminPort" : $("#adminPort" + i).text().trim(),
						"os" : detectedNodes.nodes[i][4],
						"registryPort" : $("#registryPort" + i).text().trim(),
						"haPortRangeStart" : $("#haPort1" + i).text().trim(),
						"haPortRangeEnd" : $("#haPort2" + i).text().trim(),
						"memoryMb" : $("#memory" + i).text().trim(),
						"cpuNum" : $("#cpu" + i).text().trim(),
						"capacity" : $("#capacity" + i).text().trim(),
						"storageDirs" : storageDirs,
					};
				} else
					node = {
						"publicIp" : detectedNodes.nodes[i][0],
						"privateIp" : detectedNodes.nodes[i][0],
						"admin" : false,
						"os" : detectedNodes.nodes[i][4],
						"registryPort" : $("#registryPort" + i).text().trim(),
						"haPortRangeStart" : $("#haPort1" + i).text().trim(),
						"haPortRangeEnd" : $("#haPort2" + i).text().trim(),
						"memoryMb" : $("#memory" + i).text().trim(),
						"cpuNum" : $("#cpu" + i).text().trim(),
						"capacity" : $("#capacity" + i).text().trim(),
						"storageDirs" : storageDirs,
					};

				adNodeData.newNodes.push(node);
			}
		}
		var nodeAddUrl = baseUrl + '/cluster/' + monitorClusterId + '/add';
		$.ajax({
			'type' : 'POST',
			'url' : nodeAddUrl,
			'contentType' : 'application/json',
			'dataType' : 'json',
			'data' : JSON.stringify(adNodeData),
			'async' : true,
			'success' : function(result) {
				if (result.output.status) {
					com.impetus.ankush.oClusterMonitoring.loadHomePage();
				} else {
					com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
				}
			},
			'error' : function(result) {
				com.impetus.ankush.oClusterMonitoring.loadHomePage();
			}
		});
	},

	/* Function for node addition status showing on add node table */
	nodeAddition : function(tileid) {
		if (nodeAddInterval != null) {
			nodeAddInterval = window.clearInterval(nodeAddInterval);
		}
		if (document.getElementById("nodeIpProgressTable") == null) {
			nodeAddInterval = window.clearInterval(nodeAddInterval);
			return;
		}
		addNodeChildInterval = window.clearInterval(addNodeChildInterval);
		addNodeChildProgressData = null;
		clusterDetailResult = null;
		nodeProgressDetailResult = null;
		deployedNodesCount = 0;
		var progressImage = loadingImage;
		var clusterDetailUrl = baseUrl + '/monitor/' + monitorClusterId	+ '/addnodes';
		com.impetus.ankush.placeAjaxCall(clusterDetailUrl,"GET",true,null,function(result) {
							if (!result.output.status) {
								return;
							}
							clusterDetailResult = result.output;
							lastLogAddNode = clusterDetailResult.lastLog;
							if (nodeIpProgressTable == null) {
								nodeIpProgressTable.dataTable();
							} else {
								nodeIpProgressTable.fnClearTable();
							}
							if (clusterDetailResult.state == 'deployed') {
								progressImage = okIcon;
							} else if (clusterDetailResult.state == 'error') {
								progressImage = crossIcon;
							} else {
								progressImage = loadingImage;
							}
							var tableObj = new Array();
							for ( var i = 0; i < clusterDetailResult.nodes.length; i++) {
								var addId = null;
								if (clusterDetailResult.nodes[i][2] == "Node deployed." && clusterDetailResult.nodes[i].length == 3) {
									deployedNodesCount++;
								}
								var rowObj = new Array();
								rowObj.push(progressImage);
								rowObj.push(clusterDetailResult.nodes[i].publicIp);
								rowObj.push('<a class="" id="nodeType' + i
										+ '">'
										+ clusterDetailResult.nodes[i].type
										+ '</a>');
								rowObj.push('<a class="" id="nodeStatus' + i
										+ '">'
										+ clusterDetailResult.nodes[i].message
										+ '</a>');
								rowObj.push('<a href="##" onclick="com.impetus.ankush.oClusterMonitoring.loadAddNodeProgressChild('
												+ i
												+ ');"><img id="navigationImg-'
												+ i
												+ '" src="'
												+ baseUrl
												+ '/public/images/icon-chevron-right.png" /></a>');
								tableObj.push(rowObj);
							}
							addId = nodeIpProgressTable.fnAddData(tableObj);
							$("#nodeIpProgressDatatable").html('').append(
											"<h4 class='section-heading'>Node List</h4>");
							$("#nodeDeployCount").html('').append(
									"<label style='margin:10px;margin-left:20px; ;color:black;font-size:14px;'>"
											+ deployedNodesCount
											+ " Added </label>");
							if (clusterDetailResult.state != 'Adding Nodes') {
								nodeAddInterval = window.clearInterval(nodeAddInterval);
								return;
							}
							if (clusterDetailResult.state == 'Adding Nodes') {
								com.impetus.ankush.oClusterMonitoring.initNodeAddition(tileid);
							}
						});
	},

	/* Function for initializing node addition auto-refresh function */
	initNodeAddition : function(tileid) {
		if (nodeProgressDetailResult != null) {
			lastLogAddNode = nodeProgressDetailResult.lastLog;
		}
		nodeAddInterval = setInterval(
				'com.impetus.ankush.oClusterMonitoring.nodeAdditionProgress('+ tileid + ');', logInterval);
	},

	/* Function to be called on auto-refresh of node addition for status update */
	nodeAdditionProgress : function(tileid) {
		if (document.getElementById("nodeIpProgressTable") == null) {
			nodeAddInterval = window.clearInterval(nodeAddInterval);
			return;
		}
		deployedNodesCount = 0;
		var progressNodeUrl = baseUrl + '/monitor/' + monitorClusterId+ '/addnodes';
		com.impetus.ankush.placeAjaxCall(progressNodeUrl,"GET",true,null,function(result) {
							nodeProgressDetailResult = result;
							if (nodeProgressDetailResult.output.status && undefined != nodeProgressDetailResult.nodes) {
								nodeProgressDetailResult = nodeProgressDetailResult.output;
								lastLogAddNode = nodeProgressDetailResult.lastLog;
								if (nodeIpProgressTable == null) {
									nodeIpProgressTable.dataTable();
								} else {
									nodeIpProgressTable.fnClearTable();
								}
								var tableObj = new Array();
								if (clusterDetailResult.state == 'deployed') {
									progressImage = okIcon;
								} else if (clusterDetailResult.state == 'error') {
									progressImage = crossIcon;
								} else {
									progressImage = loadingImage;
								}
								for ( var i = 0; i < nodeProgressDetailResult.nodes.length; i++) {
									var addId = null;
									if (nodeProgressDetailResult.nodes[i][2] == "Node deployed." && nodeProgressDetailResult.nodes[i].length == 3) {
										deployedNodesCount++;
									}
									var rowObj = new Array();
									rowObj.push(progressImage);
									rowObj.push(nodeProgressDetailResult.nodes[i].publicIp);
									rowObj.push('<a class="" id="nodeType'
													+ i
													+ '">'
													+ nodeProgressDetailResult.nodes[i].type
													+ '</a>');
									rowObj.push('<a class="" id="nodeStatus'
													+ i
													+ '">'
													+ nodeProgressDetailResult.nodes[i].message
													+ '</a>');
									rowObj.push('<a href="##" onclick="com.impetus.ankush.oClusterMonitoring.loadAddNodeProgressChild('
													+ i
													+ ');"><img id="navigationImg-'
													+ i
													+ '" src="'
													+ baseUrl
													+ '/public/images/icon-chevron-right.png" /></a>');
									tableObj.push(rowObj);
								}
								addId = nodeIpProgressTable.fnAddData(tableObj);

								$("#nodeDeployCount").html('').append(
										"<label style='margin:10px;margin-left:20px; ;color:black;font-size:14px;'>"
												+ deployedNodesCount
												+ " Added </label>");
								if (nodeProgressDetailResult.state != 'Adding Nodes') {
									nodeAddInterval = window.clearInterval(nodeAddInterval);
								}
							} else {
								nodeAddInterval = window.clearInterval(nodeAddInterval);
							}
						});

	},
	/* Function for loading add node child page progress */
	loadAddNodeProgressChild : function(i) {

		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl+ '/oClusterMonitoring/addNodeChildProgress',
							method : 'get',
							params : {},
							title : 'Add Node Child Detail',
							tooltipTitle : 'Add Node Progress',
							callback : function(data) {
								com.impetus.ankush.oClusterMonitoring.addNodeChildProgressDetail(data.i);
							},
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.nodeAddition("+ tileId + ")",
							callbackData : {
								i : i
							}
						});

	},
	/* Function for showing progress logs of individual node while adding node */
	addNodeChildProgressDetail : function(node) {
		if (nodeProgressDetailResult == null) {
			nodeProgressDetailResult = clusterDetailResult;
		}
		nodeAddInterval = window.clearInterval(nodeAddInterval);
		$('#addNodeProgressError').hide();
		ipAdd = nodeProgressDetailResult.nodes[node].publicIp;
		var addNodeChildUrl = baseUrl + '/monitor/' + monitorClusterId+ '/logs?ip=' + ipAdd;
		com.impetus.ankush.placeAjaxCall(addNodeChildUrl,"GET",true,null,function(result) {
							addNodeChildProgressData = result.output;
							$('#nodeDetailHead1').html('').text(ipAdd);
							for ( var key in nodeProgressDetailResult.nodes[node]) {
								var newKey = com.impetus.ankush.camelCaseKey(key);
								if (key != 'errors')
									$('#nodeDeploymentField').append(
													'<div class="row-fluid"><div class="span2"><label class=" text-right form-label">'
															+ newKey
															+ ':</label></div><div class="span10"><label class="form-label label-black">'
															+ nodeProgressDetailResult.nodes[node][key]
															+ '</label></div></div>');
							}
							if (addNodeChildProgressData.status) {
								$('#nodeDeployProgress').html('');
								for ( var i = 0; i < addNodeChildProgressData.logs.length; i++) {
									if (addNodeChildProgressData.logs[i].type == 'error') {
										$('#nodeDeployProgress').prepend(
														'<div style="color:red">'
																+ addNodeChildProgressData.logs[i].longMessage
																+ '</div>');
									} else {
										$('#nodeDeployProgress').prepend(
														'<div>'
																+ addNodeChildProgressData.logs[i].longMessage
																+ '</div>');
									}
								}

								if (nodeProgressDetailResult.state == 'error') {
									addNodeChildInterval = window.clearInterval(addNodeChildInterval);
									if (Object.keys(nodeProgressDetailResult.nodes[node].errors).length > 0) {
										$('#nodeErrorDiv').show();
									}
									$('#errorOnNodeDiv').css("margin-top","10px");
									for ( var key in nodeProgressDetailResult.nodes[node].errors) {
										$('#errorOnNodeDiv').append(
														'<label class="text-left" style="color: black;" id="'
																+ key
																+ '">'
																+ nodeProgressDetailResult.nodes[node].errors[key]
																+ '</label>');
									}
								}
								if (addNodeChildProgressData.state == 'deployed'|| addNodeChildProgressData.state == 'error') {
									addNodeChildInterval = window.clearInterval(addNodeChildInterval);
								} else {
									com.impetus.ankush.oClusterMonitoring.initAddNodeChildProgressDetail(node);
								}
							}
						});
	},

	/* Function to initialize add node logs retreiving function */
	initAddNodeChildProgressDetail : function(node) {
		addNodeChildInterval = setInterval('com.impetus.ankush.oClusterMonitoring.addNodeChildProgressRefresh('+ node + ');', logInterval);
	},

	/* Function to be called on auto-refresh of add node progress */
	addNodeChildProgressRefresh : function(node) {
		$('#addNodeProgressError').hide();
		if (nodeProgressDetailResult == null) {
			nodeProgressDetailResult = clusterDetailResult;
		}
		var addNodeChildUrl = baseUrl + '/monitor/' + monitorClusterId+ '/logs?ip=' + ipAdd + '&lastlog='+ addNodeChildProgressData.lastlog;
		com.impetus.ankush.placeAjaxCall(addNodeChildUrl,"GET",true,null,function(result) {
							addNodeChildProgressData = result;
							addNodeChildProgressData = addNodeChildProgressData.output;
							if (addNodeChildProgressData.status) {
								$('#nodeProgressStatusChild').text(addNodeChildProgressData.message);
								for ( var i = 0; i < addNodeChildProgressData.logs.length; i++) {
									if (addNodeChildProgressData.logs[i].type == 'error') {
										$('#nodeProgressLogsChild').prepend(
														'<div style="color:red">'
																+ addNodeChildProgressData.logs[i].longMessage
																+ '</div>');
									} else {
										$('#nodeProgressLogsChild').prepend(
														'<div>'
																+ addNodeChildProgressData.logs[i].longMessage
																+ '</div>');
									}
								}
								if (addNodeChildProgressData.state == 'deployed'|| addNodeChildProgressData.state == 'error') {
									addNodeChildInterval = window.clearInterval(addNodeChildInterval);
								}
							}
						});
	},
	/* Function for populating cluster actions logs */
	commonActionLogs : function(name) {
		$("#actionLogPageHeader").html('').html(name);
		var commonLogsUrl = baseUrl + '/monitor/' + monitorClusterId+ '/storelogs?lasttimestamp=' + commonActionId;
		com.impetus.ankush.placeAjaxCall(commonLogsUrl,"GET",true,null,function(result) {
							redistributeLogsResult = result;
							if (redistributeLogsResult.output.status) {
								commonActionTimeStamp = redistributeLogsResult.output.lasttimestamp;
								for ( var i = 0; i < redistributeLogsResult.output.logs.length; i++) {
									$('#commonProgressLog').prepend(
													'<div>'
															+ redistributeLogsResult.output.logs[i][0]
															+ '</div>');
								}
								if (redistributeLogsResult.output.state != 'deployed' && redistributeLogsResult.output.state != 'error') {
									com.impetus.ankush.oClusterMonitoring.initCommonActionLogs();
								} else {
									redistributeLogInterval = window.clearInterval(redistributeLogInterval);
									com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
								}
							}
						});

	},

	/* Function for initializing auto-refresh of action logs */
	initCommonActionLogs : function() {
		redistributeLogInterval = setInterval('com.impetus.ankush.oClusterMonitoring.commonActionLogsrefresh();',logInterval);
	},
	/*
	 * Function to be called on auto-refresh of common action logs for
	 * populating logs
	 */
	commonActionLogsrefresh : function() {
		var redistributeLogsUrl = baseUrl + '/monitor/' + monitorClusterId+ '/storelogs?lasttimestamp=' + commonActionTimeStamp;
		com.impetus.ankush.placeAjaxCall(redistributeLogsUrl,"GET",true,null,function(result) {
							var redistributeLogsResult = result;
							if (redistributeLogsResult.status) {
								commonActionTimeStamp = redistributeLogsResult.output.lasttimestamp;
								for ( var i = 0; i < redistributeLogsResult.output.logs.length; i++) {
									$('#commonProgressLog').prepend(
													'<div>'
															+ redistributeLogsResult.output.logs[i][0]
															+ '</div>');
								}
								if (redistributeLogsResult.output.state == 'deployed') {
									redistributeLogInterval = window.clearInterval(redistributeLogInterval);
									com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
								}
							} else {
								redistributeLogInterval = window.clearInterval(redistributeLogInterval);
							}
						});
	},

	/* Function for cluster rebalancing */
	rebalanceCluster : function() {
		url = baseUrl + '/manage/' + monitorClusterId + '/rebalance';
		var result = com.impetus.ankush.placeAjaxCall(url, "POST", false);
		if (result.output.status) {
			commonActionId = result.output.lasttimestamp;
			com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
		} else {
			com.impetus.ankush.oClusterMonitoring.commonActionError(result.output.error[0]);
		}

	},

	/* Function for cluster redistributing */
	redistributeCluster : function() {
		var redistributeUrl = baseUrl + '/manage/' + monitorClusterId+ '/redistribute';
		var redistributeResult = com.impetus.ankush.placeAjaxCall(redistributeUrl, "POST", false);
		if (redistributeResult.output.status) {
			commonActionId = redistributeResult.output.lasttimestamp;
			com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId);
		} else {
			com.impetus.ankush.oClusterMonitoring.commonActionError(redistributeResult.output.error[0]);
		}

	},

	/* Function for cluster verify call */
	verifyFunction : function() {
		var verifyUrl = baseUrl + '/monitor/' + monitorClusterId + '/verify';
		com.impetus.ankush.placeAjaxCall(verifyUrl, "GET", true, null,function(result) {
					$('#verifyProgressLog').empty().html(result.output.verify);
				});

	},

	/* Function for populating plan history table */
	planHistory : function() {
		var planUrl = baseUrl + '/monitor/' + monitorClusterId + '/planhistory';
		com.impetus.ankush.placeAjaxCall(planUrl,"GET",true,null,function(result) {
							var planResult = result;
							if (!planResult.output.status) {
								$('#planHistoryError').empty();
								var count = 0;
								for ( var i = 0; i < planResult.output.error.length; i++) {
									count++;
									$("#planHistoryError").append(
													"<div class='errorLineDiv'><a href='#' style='color: #5682C2;'>"
															+ count
															+ ". "
															+ planResult.output.error[i]
															+ "</a></div>");
								}
								$('#planHistoryButton').html('').text(count + ' Error');
								$('#planHistoryError').show();
								$('#planHistoryButton').show();
								return;
							}
							if (planHistoryTable == null) {
								planHistoryTable.dataTable();
							} else
								planHistoryTable.fnClearTable();

							var tableObj = new Array();
							//for ( var i in planResult.output.planHistory) {
							for ( var i=0;i<planResult.output.planHistory.length;i++) {
								var rowObj = new Array();
						var dropdown='<div class="btn-group" id="planActionDropDown'+i+'">'+
							' <button data-toggle="dropdown" id="planActionBtnGrp'+i+'" class="btn btn-default dropdown-toggle" type="button" style="height:22px;">Actions'+
							 '	<span class="caret"></span></button>'+
							 '<ul role="menu" class="dropdown-menu" id="planActionList'+i+'"></ul></div>';
								
								rowObj.push(planResult.output.planHistory[i][1]);
								rowObj.push(planResult.output.planHistory[i][2]);
								rowObj.push(planResult.output.planHistory[i][3]);
								rowObj.push(dropdown);
								rowObj.push(planResult.output.planHistory[i][4]);
								rowObj.push(planResult.output.planHistory[i][5]);
								rowObj.push(planResult.output.planHistory[i][6]);
								rowObj.push('<a href="#"><img onclick="com.impetus.ankush.oClusterMonitoring.loadPlanHistoryChildPage('
												+ planResult.output.planHistory[i][0]
												+ ');" src="'
												+ baseUrl
												+ '/public/images/icon-chevron-right.png" /></a>');
								tableObj.push(rowObj);
							}
							planHistoryTable.fnAddData(tableObj);
							
							for ( var k=0;k<planResult.output.planHistory.length;k++) {
								$('.dropdown-toggle').dropdown();
								if(planResult.output.planHistory[k][3]=='INTERRUPTED' || planResult.output.planHistory[k][3]=='INTERRUPT REQUESTED' ||planResult.output.planHistory[k][3]=='ERROR'){
									$('#planActionList'+k).append('<li><a role="menuitem" tabindex="-1" href="#"'+
									'class="text-left"	onclick="com.impetus.ankush.oClusterMonitoring.planAction('+planResult.output.planHistory[k][0]+',\'Retry\')">Retry</a></li>'+
									'<li><a role="menuitem" tabindex="-1" href="#"'+
										'class="text-left"	onclick="com.impetus.ankush.oClusterMonitoring.planAction('+planResult.output.planHistory[k][0]+',\'Cancel\')"">Cancel</a></li>');		
								}
								else if(planResult.output.planHistory[k][3]=='RUNNING'){
									$('#planActionList'+k).append('<li><a role="menuitem" tabindex="-1" href="#"'+
									'class="text-left"	onclick="com.impetus.ankush.oClusterMonitoring.planAction('+planResult.output.planHistory[k][0]+',\'Interrupt\')"">Intrerrupt</a></li>');		
								}else{
									$('#planActionBtnGrp'+k).attr('disabled','disabled');
								}
							}
				});
	},

	/* Function for populating values on plan history child page */
	planHistoryChild : function(planId) {
		var planChildUrl = baseUrl + '/monitor/' + monitorClusterId+ '/plan?id=' + planId;
		com.impetus.ankush.placeAjaxCall(planChildUrl,"GET",true,null,function(result) {
							var planChildResult = result;
							$('#planId').html('').html(planChildResult.output.Id);
							$('#planType').html('').html(planChildResult.output.Type);
							$('#planStatus').html('').html(planChildResult.output.State);
							$('#planAttempts').html('').html(planChildResult.output.ExecutionHistory[0].AttemptNumber);
							$('#planStart').html('').html(planChildResult.output.ExecutionHistory[0].StartTime);
							$('#planStop').html('').html(planChildResult.output.ExecutionHistory[0].EndTime);
							$('#planCreate').html('').html(planChildResult.output.CreateTime);
							for ( var key in planChildResult.output.PlanParameters) {
								$('#planChildTableBody').append(
												'<tr><td class="span8 text-right"><label class=" text-right">'
														+ key
														+ '</label></td><td class="span4"><label class="" style="color:black">'
														+ planChildResult.output.PlanParameters[key]
														+ '</label></td></tr>');
							}
						});
	},
	/* Function for Plan actions to be taken*/
	planAction:function(planId,planName){
		var planActionHash={
			'Interrupt' : baseUrl + '/manage/'+monitorClusterId+'/interruptplan?id='+planId,
			'Cancel' : baseUrl + '/manage/'+monitorClusterId+'/cancelplan?id='+planId,
			'Retry' : baseUrl + '/manage/'+monitorClusterId+'/executeandwaitplan?id='+planId,

		};
		$.ajax({
			'type' : 'POST',
			'url' : planActionHash[planName],
			'contentType' : 'application/json',
			'dataType' : 'json',
			'async' : true,
			'success' : function(result) {
				com.impetus.ankush.oClusterMonitoring.planHistory();
			},
			'error' : function() {
			}
		});
			
			
			
		
	},
	
	/* Function for populating values of store events */
	storeEventView : function() {
		
		var storeEventUrl = baseUrl + '/monitor/' + monitorClusterId+ '/storeevents';
		com.impetus.ankush.placeAjaxCall(storeEventUrl,"GET",true,null,function(result) {
							storeEventResult = result;
							if (!storeEventResult.output.status) {
								$('#storeEventError').empty();
								var count = 0;
								for ( var i = 0; i < storeEventResult.output.error.length; i++) {
									count++;
									$("#storeEventError").append(
													"<div class='errorLineDiv'><a href='#' style='color: #5682C2;'>"
															+ count
															+ ". "
															+ storeEventResult.output.error[i]
															+ "</a></div>");

								}
								$('#storeEventButton').html('').text(count + ' Error');
								$('#storeEventError').show();
								$('#storeEventButton').show();
								return;
							}else{
								if (storeEventTable == null) {
									storeEventTable = $("#storeEventTable").dataTable();
								} else {
									storeEventTable.fnClearTable();
								}
								storeEventTable.fnAddData(storeEventResult.output.events);
								storeEventTimeStamp = storeEventResult.output.lasttimestamp;
							}
							com.impetus.ankush.oClusterMonitoring.initStoreEventView();
						});

	},

	/* Function for initializing store event auto-refresh call */
	initStoreEventView : function() {
		storeEventInterval = setInterval('com.impetus.ankush.oClusterMonitoring.storeEventRefresh();',pageInterval);
	},

	/* Function to be called on auto-refresh of store event */
	storeEventRefresh : function() {
		if (document.getElementById("storeEventTable") != null) {
			var storeEventUrl = baseUrl + '/monitor/' + monitorClusterId+ '/storeevents?starttime=' + storeEventTimeStamp;
			com.impetus.ankush.placeAjaxCall(storeEventUrl,"GET",true,null,function(result) {
								storeEventResult = result;
								storeEventTimeStamp = storeEventResult.output.lasttimestamp;
							});
		} else {
			storeEventInterval = window.clearInterval(storeEventInterval);
		}
	},

	/* Function for populating events table */
	eventView : function() {
		if (eventTable != null) {
			eventTable.fnClearTable();
		}
		var eventUrl = baseUrl + '/monitor/' + monitorClusterId + '/events';
		com.impetus.ankush.placeAjaxCall(eventUrl,"GET",true,null,function(result) {
							eventResult = result.output;
							var tableObj = new Array();
							for ( var i = 0; i < eventResult.events.length; i++) {
								var rowObj = new Array();
								var tempDate = new Date(parseInt(eventResult.events[i].date));
								var date = $.format.date(tempDate,"dd/MM/yyyy hh:mm:ss");
								rowObj.push(eventResult.events[i].name);
								rowObj.push(eventResult.events[i].type);
								rowObj.push(eventResult.events[i].severity);
								rowObj.push(eventResult.events[i].currentValue);
								rowObj.push(date);
								rowObj.push('<a href="#"><img onclick="com.impetus.ankush.oClusterMonitoring.loadEventChildPage('
												+ i
												+ ');" src="'
												+ baseUrl
												+ '/public/images/icon-chevron-right.png" /></a>');
								tableObj.push(rowObj);
							}
							eventTable.fnAddData(tableObj);
						});
	},

	/* Function for populating event child page */
	eventViewChild : function(i) {
		var tempDate = new Date(parseInt(eventResult.events[i].date));
		var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
		$('#eventType').html('').html(eventResult.events[i].type);
		$('#eventSeverity').html('').html(eventResult.events[i].severity);
		$('#eventStatus').html('').html(eventResult.events[i].currentValue);
		$('#eventTime').html('').html(date);
		$('#eventIp').html('').html(eventResult.events[i].host);
		$('#eventDesc').html('').html(eventResult.events[i].description);
	},

	/* Function for populating policy parameters page */
	policyParam : function() {
		$('#policyParamDiv').html('');
		var policyParamUrl = baseUrl + '/monitor/' + monitorClusterId+ '/policyparams';
		com.impetus.ankush.placeAjaxCall(policyParamUrl,"GET",true,null,function(result) {
							policyParamResult = result;
							if (!policyParamResult.output.status) {
								$('#policyParamError').empty();
								var count = 0;
								for ( var i = 0; i < policyParamResult.output.error.length; i++) {
									count++;
									$("#policyParamError").append(
													"<div class='errorLineDiv'><a href='#' style='color: #5682C2;'>"
															+ count
															+ ". "
															+ policyParamResult.output.error[i]
															+ "</a></div>");

								}
								$('#policyParamButton').html('').text(count + ' Error');
								$('#policyParamError').show();
								$('#policyParamButton').show();
								return;
							}
							policyParamJson = policyParamResult.output;
							for ( var key in policyParamResult.output) {
								var labelVal = policyParamResult.output[key];
								var labelKey = camelCaseKey(key);
								$('#policyParamDiv').append(
												'<div class="row-fluid">'
														+ '<div class="span2 ">'
														+ '<label class=" text-right form-label policyKey">'
														+ labelKey
														+ ':</label>'
														+ '</div>'
														+ '<div class="span3">'
														+ '<div class="editOnClick policyVal" style="color:black" id="'
														+ key + '">' + labelVal
														+ '</div>' + '</div>'
														+ '</div>');

							}
							$('.editOnClick').textEditable({
								parentClass : 'editLabel',
							}).css('padding-top', '15px');
							$('.editLabel').css('pading-top', '-4px');
						});
	},

	/* Function for cluster delete */
	deleteCluster : function() {
		url = baseUrl + '/cluster/remove/' + monitorClusterId;
		monitorPageInterval = window.clearInterval(monitorPageInterval);
		if(redistributeLogInterval != null){
			redistributeLogInterval = window.clearInterval(redistributeLogInterval);
		}
		com.impetus.ankush.placeAjaxCall(url, "DELETE", false);
		com.impetus.ankush.removeChild(1);
	},

	/* Function will populate type drop-down on logs page */
	logViewByTypeNodeFile : function() {
		com.impetus.ankush.oClusterMonitoring.removeErrorClass();
		var logParamUrl = baseUrl + '/monitor/' + monitorClusterId+ '/logparams';
		com.impetus.ankush.placeAjaxCall(logParamUrl, "GET", true, null,
				function(result) {
					if (!result.output.status) {
						$('#logError').empty();
						var count = 0;
						for ( var i = 0; i < result.output.error.length; i++) {
							count++;
							$("#logError").append(
									"<div class='errorLineDiv'><a href='#' style='color: #5682C2;'>"
											+ count + ". "
											+ result.output.error[i]
											+ "</a></div>");
						}
						$('#logErrorButton').html('').text(count + 'Error');
						$('#logError').show();
						$('#logErrorButton').show();
						return;
					}
					if (result.output.status == true) {
						populate = result.output;
						for ( var key in populate) {
							if (key.toLowerCase() == "status")
								continue;
							$('#logFromType').append(
									'<option value="' + key + '">' + key
											+ '</option>');
						}
						com.impetus.ankush.oClusterMonitoring.onChangeNodePopulation();
					}
				});
	},

	/* This function will populate node drop down on logs page */
	onChangeNodePopulation : function() {
		com.impetus.ankush.oClusterMonitoring.removeErrorClass();
		var type = $('#logFromType').val();
		$('#logFromNode').empty();
		for ( var i = 0; i < populate[type].length; i++) {
			$('#logFromNode').append(
					'<option value="' + populate[type][i] + '">'
							+ populate[type][i] + '</option>');
		}
		com.impetus.ankush.oClusterMonitoring.onchangePopulateFiles();
	},

	/* This function will populate files drop down on logs page */
	onchangePopulateFiles : function() {
		com.impetus.ankush.oClusterMonitoring.removeErrorClass();
		$('#logFromFile').empty();
		var IP = $('#logFromNode').val();
		var Type = $('#logFromType').val();
		var url = baseUrl + '/monitor/' + monitorClusterId + '/logfiles?ip='+ IP + '&type=' + Type;
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			if (result.output.status == true) {
				for ( var i = 0; i < result.output.files.length; i++) {
					$('#logFromFile').append(
							'<option value="' + result.output.files[i] + '">'
									+ result.output.files[i] + '</option>');
				}
			}else{
				com.impetus.ankush.oClusterMonitoring.validateLogs(result,"nodeDown");
			}
		});
	},
	/*Function to show logs on logs page (this function is replaced by common movitoring logDisplay function. 
 * Once the below logDisplay function verified and tested this can be deleted.)
	viewLogsOnFieldBasis : function() {
		com.impetus.ankush.oClusterMonitoring.removeErrorClass();
		var getFile = $('#logFromFile').val();
		var getNode = $('#logFromNode').val();
		var logUrl = baseUrl + '/monitor/' + monitorClusterId + '/viewfile?ip='+ getNode + '&filename=' + getFile;
		com.impetus.ankush.placeAjaxCall(logUrl, "GET", true, null, function(result) {
			logResult = result;
			if (logResult.output.status == true) {
				$("#logsOnPre").text(logResult.output.content).show();
			} else {
				$("#logErrorButton").html('1 Error').show();
				$("#logError").html('').append(
						"<div class='errorLineDiv'><a href='#' style='color: #5682C2;' >1. "
								+ logResult.output.error[0] + "</a></div>").show();
			}
		});
	},*/
	// function used to display logs of the file selected. Data is loaded using lazy loading
	logDisplay : function() {
		com.impetus.ankush.oClusterMonitoring.removeErrorClass();
		readCount = 0;
		$(window).unbind("scroll");
		$(window).bind("scroll" ,function () {        	
			if ($(window).scrollTop() == ( $(document).height() - $(window).height())) {
				com.impetus.ankush.oClusterMonitoring.appendLog();
			}
		});

		var currentNodeIP = $("#logFromNode").val();
		var currentFileName = $("#logFromFile").val();
		var logFileURL = baseUrl + '/monitor/' + monitorClusterId + '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000';
		var logFileData = com.impetus.ankush.placeAjaxCall(logFileURL, 'GET',false);
		if (logFileData != null) {
			if(logFileData.output.status) {
				$("#logsOnPre").css("display", "block");
				$("#logsOnPre").empty();
				$("#logsOnPre").append(logFileData.output.content);
				readCount = logFileData.output.readCount;
			}else{
				com.impetus.ankush.oClusterMonitoring.validateLogs(logFileData,"filename");
			}
		} else {
			alert("Sorry, unable to retrieve the Log file");
		}
	},
	// function used to append log data, during lazy-load, at the end of the previously shown logs
	appendLog : function() {
		com.impetus.ankush.oClusterMonitoring.removeErrorClass();
		var currentNodeIP = $("#logFromNode").val();
		var currentFileName = $("#logFromFile").val();
		var logFileURL = baseUrl + '/monitor/' + monitorClusterId+ '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000';
		var logFileData = com.impetus.ankush.placeAjaxCall(logFileURL, 'GET',false);
		if (logFileData != null) {
			if(logFileData.output.status) {
				$("#logsOnDiv").css("display", "block");
				$("#logsOnPre").append(logFileData.output.content);
				readCount = logFileData.output.readCount;
			}
			else{
				$(window).unbind("scroll");
			}
		} else {
			alert("Sorry, unable to retrieve the Log file");
		}
	},
	// function used to validate whether a log file can be dowloaded or not
	validateLogs : function(urlData,errorType) {
		var focusDivId = null;
		if(errorType=="nodeDown"){
			focusDivId = "logFromNode";
		}else if(errorType=="filename"){
			focusDivId = "logFromFile";
		}
		$("#logError").empty();
		$("#logError").css("display", "none");
		$('#logErrorButton').text("");
		$('#logErrorButton').css("display", "none");
		com.impetus.ankush.validation.errorCount = 0;
		var i=0;
		$.each( urlData.output.error,function(index,value){
			i = index + 1;
			com.impetus.ankush.validation.addNewErrorToDiv(focusDivId,'logError',value,null);
		});
		if(com.impetus.ankush.validation.errorCount > 0) {
			com.impetus.ankush.validation.showErrorDiv('logError', 'logErrorButton');
			return;
		}
	},
	download : function(clusterId) {
		com.impetus.ankush.oClusterMonitoring.removeErrorClass();
		var currentNodeIP = $("#logFromNode").val();
		var currentFileName = $("#logFromFile").val();
		var downloadUrl = baseUrl + '/monitor/' + monitorClusterId+ '/downloadfile?ip=' + currentNodeIP + '&fileName='+ currentFileName;
		var downloadFileSize = logFileNameOutputCommon[currentFileName];
		if(downloadFileSize>5120){
			$("#fileSizeExceed").modal('show');
			$('.ui-dialog-titlebar').hide();
			$('.ui-dialog :button').blur();
			return;
		}
		var downloadUrlData = com.impetus.ankush.placeAjaxCall(downloadUrl,'GET', false);
		if (downloadUrlData.output.status == true) {
			var downloadFilePath = baseUrl + downloadUrlData.output.downloadPath;
			var hiddenIFrameID = 'hiddenDownloader', iframe = document.getElementById(hiddenIFrameID);
			if (iframe === null) {
				iframe = document.createElement('iframe');
				iframe.id = hiddenIFrameID;
				iframe.style.display = 'none';
				document.body.appendChild(iframe);
			}
			iframe.src = downloadFilePath;
		}else {
			com.impetus.ankush.oClusterMonitoring.validateLogs(downloadUrlData,"filename");
		}
	},
/*	 Function for downloading log files (this function is replaced by common movitoring download function. 
 * Once the above download function verified and tested this can be deleted. )
	downloadLogFiles : function() {
		var fileName = $('#logFromFile').val();
		var IP = $('#logFromNode').val();
		var url = baseUrl + '/monitor/' + monitorClusterId+ '/downloadfile?ip=' + IP + '&filename=' + fileName;
		com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
							if (result.output.status == true) {
								var downloadUrl = baseUrl
										+ result.output.downloadPath;
								var hiddenIFrameID = 'hiddenDownloader', iframe = document.getElementById(hiddenIFrameID);
								if (iframe === null) {
									iframe = document.createElement('iframe');
									iframe.id = hiddenIFrameID;
									iframe.style.display = 'none';
									document.body.appendChild(iframe);
								}
								iframe.src = downloadUrl;

							} else {
								$("#logErrorButton").html('1 Error').show();
								$("#logError").html('').append(
										"<div class='errorLineDiv'><a href='#' style='color: #5682C2;' >1. "
												+ result.output.error[0]
												+ "</a></div>").show();
							}
						});
	},*/

	// function used to remove error-class
	removeErrorClass : function() {
		$('#logFromNode').removeClass('error-box');
		$('#logFromFile').removeClass('error-box');
		$("#logError").css("display", "none");
		$('#logErrorButton').css("display", "none");
	},
	/* Function for showing alerts table values */
	clusterAlerts : function() {
		var alertUrl = baseUrl + '/monitor/' + monitorClusterId + '/alertsConf';
		com.impetus.ankush.placeAjaxCall(alertUrl,"GET",true,null,function(result) {
							alertResult = result;
							$("#refreshInterval").html('').text(alertResult.output.refreshInterval);
							if (alertResult.output.mailingList == '' || alertResult.output.mailingList == null) {
								$("#mailingList").val('');
							} else
								$("#mailingList").val(alertResult.output.mailingList);
							if (alertResult.output.informAllAdmins) {
								$("#mailingListCheck").attr('checked','checked');
							} else {
								$("#mailingListCheck").removeAttr('checked');
							}

							if (alertTable != null) {
								alertTable.fnClearTable();
							} else {
								alertTable.dataTable();
							}

							var tableObj = new Array();
							for ( var i = 0; i < alertResult.output.thresholds.length; i++) {
								var rowObj = new Array();
								rowObj.push(alertResult.output.thresholds[i].metricName);
								rowObj.push(alertResult.output.thresholds[i].warningLevel);
								rowObj.push(alertResult.output.thresholds[i].alertLevel);
								rowObj.push(i);
								tableObj.push(rowObj);
							}
							alertTable.fnAddData(tableObj);
							$('#alertTable td').css('line-height', '26px');
							$('.editableLabel').editable(
											{
												type : 'text',
												validate : function(value) {
													return com.impetus.ankush.oClusterMonitoring.checkUsageValue(this, value);
												}
											});
						});
	},

	/* Function to match value from a given pattern */
	validatePattern : function(value, pattern) {
		if (value.match(pattern) != null) {
			return true;
		}
		return false;
	},

	/* Function for validations in alert table values */
	checkUsageValue : function(elem, value) {
		if (!com.impetus.ankush.oClusterMonitoring.validatePattern(value,/^[\d]*$/)) {
			return "Invalid value.Should be numeric";
		} else if (!com.impetus.ankush.validation.between(value, '0', '100')) {
			return "Invalid value. Should be within 0-100";
		}
		var elementId = elem.id;
		var id = elementId.split('-');
		if (id[0] == "warningLevel") {
			if (parseInt(value, 10) >= parseInt($('#alertLevel-' + id[1]).text(), 10)) {
				return "Invalid value.Should be less than alert(%)";
			}
		} else if (id[0] == "alertLevel") {
			if (parseInt(value, 10) <= parseInt($('#warningLevel-' + id[1]).text(), 10)) {
				return "Invalid value.Should be greater than warning(%)";
			}
		}
	},

	/* Function for saving alerts value */
	saveAlerts : function() {
		$('#alertpageError').hide().html('');
		var data = {};
		var errorCount = 0;
		data.thresholds = new Array();
		var alertUrl = baseUrl + '/monitor/' + monitorClusterId + '/alertsConf';
		data.refreshInterval = $("#refreshInterval").text();
		if ($("#mailingList").val() == '') {
			data.mailingList = '';
		} else {
			data.mailingList = $("#mailingList").val();
		}
		if ($("#mailingListCheck").is(':checked')) {
			data.informAllAdmins = true;
		} else {
			data.informAllAdmins = false;
		}
		for ( var i = 0; i < alertResult.output.thresholds.length; i++) {
			var rowData = {
				"metricName" : $("#metricName" + i).text().trim(),
				"warningLevel" : $("#warningLevel-" + i).text().trim(),
				"alertLevel" : $("#alertLevel-" + i).text().trim()
			};
			data.thresholds.push(rowData);
			if (parseInt($("#warningLevel-" + i).text().trim()) >= parseInt($("#alertLevel-" + i).text().trim())) {
				errorCount++;
				$("#alertpageError").append(
								"<div class='errorLineDiv'><a href='#alertTable'  >"
										+ errorCount
										+ ". For "
										+ $("#metricName" + i).text()
										+ " Warning(%) must be less than Alert(%)</a></div>");
			}
		}
		var result = com.impetus.ankush.placeAjaxCall(alertUrl, "POST", false,data);
		com.impetus.ankush.removeChild(3);
	},

	/* Function for tree graph construction for storage node */
	plotStorageNodeTree : function(storageNodeId, divId) {
		result = overviewData;
		if (result.output.tree == null || $.isEmptyObject(result.output.tree)) {
			$("#" + divId).html('');
			return;
		}
		$("#" + divId).html('');
		if (result.output.status) {
			result = result.output.tree;
			var width = 450, height = 400, radius = 15;
			if (result.childCount) {
				height = result.childCount * 60 + 40;
			}
			var cluster = d3.layout.cluster().size([ height, width - 160 ]);
			var diagonal = d3.svg.diagonal().projection(function(d) {
				return [ d.y, d.x ];
			});

			var svg = d3.select("#" + divId).append("svg").attr("width", width).attr("height", height).append("g").attr("transform","translate(40,0)");

			var nodes = cluster.nodes(result), links = cluster.links(nodes);

			svg.selectAll(".link").data(links).enter().append("path").attr("style", "fill: none;stroke: #d8d7d7;stroke-width: 2px").attr("d", diagonal);

			var node = svg
					.selectAll(".node")
					.data(nodes)
					.enter()
					.append("g")
					.attr("class", "node")
					.attr("transform", function(d) {
						return "translate(" + d.y + "," + d.x + ")";
					}).on("click",function(d) {
								if (d.parent && d.children) {
									if (document.getElementById("shardNameHead") == null) {
										var gpId = d.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadShardDrillDown(gpId);
									}
									// Shards
								} else if (d.parent && !d.childern) {
									// RepNodes
									if (document.getElementById("labelHeader") == null) {
										var rnId = d.name.split('RN')[1];
										var gpId = d.parent.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadRepNodeChild(gpId, rnId,'Cluster Monitoring');
									}
								} else if (!d.parent) {
									// Root/Topology node
								}
							});
			node.append("circle").attr("r", radius).attr("fill", function(d) {
				if (d.sn && d.sn == storageNodeId) {
					return colorArray[d.active];
				} else {
					return colorArray[3];
				}
			}).append("title").text(function(d) {
				return d.name;
			});
			node.append("text").attr("dx", function(d) {
				return (d.children ? -10 : 20);
			}).attr("dy", function(d) {
				return (d.children ? -18 : 5);
			}).text(function(d) {
				return d.name;
			});
			d3.select(self.frameElement).style("height", height + "px");
		}
	},

	/* Function for tree graph construction for shard node */
	plotShardNodeTree : function(shardId, divId) {
		result = overviewData;
		if (result.output.tree == null || $.isEmptyObject(result.output.tree)) {
			$("#" + divId).html('');
			return;
		}

		$("#" + divId).html('');
		if (result.output.status) {
			result = result.output.tree;
			var width = 450, height = 400, radius = 15;
			if (result.childCount) {
				height = result.childCount * 60 + 40;
			}
			var cluster = d3.layout.cluster().size([ height, width - 160 ]);
			var diagonal = d3.svg.diagonal().projection(function(d) {
				return [ d.y, d.x ];
			});

			var svg = d3.select("#" + divId).append("svg").attr("width", width).attr("height", height).append("g").attr("transform","translate(40,0)");

			var nodes = cluster.nodes(result), links = cluster.links(nodes);

			svg.selectAll(".link").data(links).enter().append("path").attr("style", "fill: none;stroke: #d8d7d7;stroke-width: 2px").attr("d", diagonal);

			var node = svg
					.selectAll(".node")
					.data(nodes)
					.enter()
					.append("g")
					.attr("class", "node")
					.attr("transform", function(d) {
						return "translate(" + d.y + "," + d.x + ")";
					}).on("click",function(d) {
								if (d.parent && d.children) {
									if (document.getElementById("shardNameHead") == null) {
										var gpId = d.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadShardDrillDown(gpId);
									}
									// Shards

								} else if (d.parent && !d.childern) {
									// RepNodes

									if (document.getElementById("labelHeader") == null) {
										var rnId = d.name.split('RN')[1];
										var gpId = d.parent.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadRepNodeChild(gpId, rnId,'Cluster Monitoring');
									}
								} else if (!d.parent) {
									// Root/Topology node
								}
							});

			node.append("circle")
					.attr("r", radius)
					.attr("fill",function(d) {
								if ((d.shard && d.shard == shardId) || (d.parent && d.parent.shard && d.parent.shard == shardId)) {
									return colorArray[d.active];
								} else {
									return colorArray[3];
								}
							}).append("title").text(function(d) {
						return d.name;
					});
			node.append("text").attr("dx", function(d) {
				return (d.children ? -10 : 20);
			}).attr("dy", function(d) {
				return (d.children ? -18 : 5);
			}).text(function(d) {
				return d.name;
			});
			d3.select(self.frameElement).style("height", height + "px");
		}
	},

	/* Function for tree graph construction for replication node */
	plotRepNodeTree : function(shardId, repNodeId, divId) {
		result = overviewData;
		if (result.output.tree == null || $.isEmptyObject(result.output.tree)) {
			$("#" + divId).html('');
			return;
		}
		$("#" + divId).html('');
		if (result.output.status) {
			result = result.output.tree;
			var width = 450, height = 400, radius = 15;
			if (result.childCount) {
				height = result.childCount * 60 + 40;
			}
			var cluster = d3.layout.cluster().size([ height, width - 160 ]);
			var diagonal = d3.svg.diagonal().projection(function(d) {
				return [ d.y, d.x ];
			});

			var svg = d3.select("#" + divId).append("svg").attr("width", width).attr("height", height).append("g").attr("transform","translate(40,0)");

			var nodes = cluster.nodes(result), links = cluster.links(nodes);
			svg.selectAll(".link").data(links).enter().append("path").attr("style", "fill: none;stroke: #d8d7d7;stroke-width: 2px").attr("d", diagonal);

			var node = svg
					.selectAll(".node")
					.data(nodes)
					.enter()
					.append("g")
					.attr("class", "node")
					.attr("transform", function(d) {
						return "translate(" + d.y + "," + d.x + ")";
					}).on("click",function(d) {
								if (d.parent && d.children) {
									if (document.getElementById("shardNameHead") == null) {
										var gpId = d.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadShardDrillDown(gpId);
									}
									// Shards

								} else if (d.parent && !d.childern) {
									// RepNodes

									if (document.getElementById("labelHeader") == null) {
										var rnId = d.name.split('RN')[1];
										var gpId = d.parent.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadRepNodeChild(gpId, rnId,'Cluster Monitoring');
									}
								} else if (!d.parent) {
									// Root/Topology node
								}
							});

			node.append("circle").attr("r", radius).attr("fill",function(d) {
								if (d.rn && d.rn == repNodeId && d.parent
										&& d.parent.shard
										&& d.parent.shard == shardId) {
									return colorArray[d.active];
								} else {
									return colorArray[3];
								}
							}).append("title").text(function(d) {
						return d.name;
					});
			node.append("text").attr("dx", function(d) {
				return (d.children ? -10 : 20);
			}).attr("dy", function(d) {
				return (d.children ? -18 : 5);
			}).text(function(d) {
				return d.name;
			});
			d3.select(self.frameElement).style("height", height + "px");
		}
	},

	/* Function for making tree graph */
	plotTree : function(result, divId) {
		if (result.output.tree == null || $.isEmptyObject(result.output.tree)) {
			$("#" + divId).html('');
			return;
		}
		$("#" + divId).html('');
		if (result.output.status) {
			result = result.output.tree;
			var width = 450, height = 400, radius = 8;
			if (result.childCount) {
				height = result.childCount * 42 + 70;
			}

			var cluster = d3.layout.cluster().size([ height, width - 160 ]);

			var diagonal = d3.svg.diagonal().projection(function(d) {
				return [ d.y, d.x ];
			});

			var svg = d3.select("#" + divId).append("svg").attr("width", width).attr("height", height).append("g").attr("transform","translate(40,0)");

			var nodes = cluster.nodes(result), links = cluster.links(nodes);

			svg.selectAll(".link").data(links).enter().append("path").attr("style", "fill: none;stroke: #d8d7d7;stroke-width: 2px").attr("d", diagonal);

			var node = svg
					.selectAll(".node")
					.data(nodes)
					.enter()
					.append("g")
					.attr("class", "node")
					.attr("transform", function(d) {
						return "translate(" + d.y + "," + d.x + ")";
					}).on("click",function(d) {
								if (d.parent && d.children) {
									if (document.getElementById("shardNameHead") == null) {
										var gpId = d.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadShardDrillDown(gpId);
									}
									// Shards

								} else if (d.parent && !d.childern) {
									// RepNodes

									if (document.getElementById("labelHeader") == null) {
										var rnId = d.name.split('RN')[1];
										var gpId = d.parent.name.split('Shard')[1];
										com.impetus.ankush.oClusterMonitoring.loadRepNodeChild(gpId, rnId,'Cluster Monitoring');
									}
								} else if (!d.parent) {
									// Root/Topology node
								}
							});

			node.append("circle").attr("r", radius).attr("fill", function(d) {
				return colorArray[d.active];
			}).append("title").text(function(d) {
				return d.name;
			});
			node.append("text").attr("dx", function(d) {
				return (d.children ? -10 : -10);
			}).attr("dy", function(d) {
				return (d.children ? -12 : -12);
			}).text(function(d) {
				return d.name;
			});
			d3.select(self.frameElement).style("height", height + "px");
		}
	},
	/* Function for tree graph construction */
	shardGraph : function(clusterId, urlPart, divId) {
		url = baseUrl + '/monitor/' + clusterId + '/' + urlPart;
		com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			com.impetus.ankush.oClusterMonitoring.plotTree(result, divId);
		});
	},
	/* Function for auto-refresh- call of monitoring page */
	monitoringTiles : function(data) {
		//com.impetus.ankush.oClusterMonitoring.monitorPageRefresh(monitorClusterId,currentClusterName);
			com.impetus.ankush.oClusterMonitoring.monitorPageLoad(monitorClusterId,currentClusterName);
			com.impetus.ankush.oClusterMonitoring.initMonitorPageLoad(monitorClusterId,currentClusterName);
	},
	/* Function to remove dialog on page unload & to stop autorefresh call */
	unloadDetailPage : function() {
		$('#actionDialog').remove();
		$('#actionErrorDialog').remove();
		$('#migrateNodeDialog').remove();
		$('#increaseRepFactorDialog').remove();
		$('#nodeServiceDialog').remove();
		window.clearInterval(monitorPageInterval);
		monitorPageInterval = null;
	},
	loadUtilizationGraphPage : function() {
		$('#content-panel').sectionSlider('addChildPanel',{
							current : 'login-panel',
							url : baseUrl+ '/oClusterMonitoring/utilizationTrend',
							method : 'get',
							params : {},
							title : 'Utilization Trend',
							previousCallBack : "com.impetus.ankush.oClusterMonitoring.monitoringTiles("+ monitorClusterId + ")",
							callback : function() {
								com.impetus.ankush.oClusterMonitoring.loadClusterLevelGraphs('lasthour');
							},
							callbackData : {}
						});
	},

	// graph related function
	loadClusterLevelGraphs : function(startTime) {
		$('#pageHeadingUtilization').text(currentClusterName + '/Utilization Metrics');
		// $("#utilizationTrendGraphs").clear();
		$('#utilizationTrendGraphs').empty();
		// to get the json for cpu graph.
		com.impetus.ankush.oClusterMonitoring.loadGraph(startTime, 'cpu','cpu_.*\\.rrd');
		// to get the json for memory graph.
		com.impetus.ankush.oClusterMonitoring.loadGraph(startTime, 'memory','(mem|swap)_.*\\.rrd');
		// to get the json for network graph.
		com.impetus.ankush.oClusterMonitoring.loadGraph(startTime, 'network','bytes_.*\\.rrd');
		// to get the json for load graph.
		com.impetus.ankush.oClusterMonitoring.loadGraph(startTime, 'load','(load_.*\\.rrd|proc_run.rrd)');
		// to get the json for packed graphs.
		com.impetus.ankush.oClusterMonitoring.loadGraph(startTime, 'packet','pkts_.*\\.rrd');
	},
	loadGraph : function(startTime, key, value) {
		var graphUrl = baseUrl + '/monitor/' + monitorClusterId+ '/graphs?startTime=' + startTime + '&pattern=' + value;
		$("#utilizationTrendGraphs").append(
						'<div class="span6" style="margin-top:10px; margin-left:20px"><div class="row-fluid section-heading" style="margin-top:10px;">'
								+ key.toUpperCase()
								+ '</div><div class="row-fluid box infobox masonry-brick"><div id="graph_'
								+ key
								+ '" style="margin-top:10px;background-color:#FFFFFF" class="graphTile"><svg></svg></div></div></div>');
		$('#graph_' + key + ' svg').css({
			'height' : '300px',
			'width' : '450px',
		});
		com.impetus.ankush.placeAjaxCall(graphUrl,"GET",true,null,function(result) {
							if (result.output.status) {
								var formatString = '';
								switch (startTime) {
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
								var newResult = com.impetus.ankush.oClusterMonitoring.normalizeData(result.output.json);
								nv.addGraph(function() {
											var chart = nv.models.cumulativeLineChart().x(
															function(d) {
																return d[0];
															}).y(function(d) {
														return d[1];
													}).clipEdge(true);

											chart.showControls(false);
											chart.xAxis.tickFormat(function(d) {
																return d3.time.format(formatString)
																		(new Date(d * 1000));
															}).ticks(10);

											chart.yAxis.tickFormat(
													d3.format("f")).ticks(10);
											chart.forceY([ 0, 10 ]);
											var format = null;
											if (key == 'network' || key == 'memory') {
												chart.yAxis.ticks(10).tickFormat(
																function(d) {
																	var suffix = '';
																	var decimals = com.impetus.ankush.oClusterMonitoring.countDecimals(d);
																	if (decimals < 3) {
																		d = d;
																	} else if (decimals < 6) {
																		d = (Math.round(d / 10) / 100);
																		suffix = " kB";
																	} else if (decimals < 9) {
																		d = (Math.round(d / 10000) / 100);
																		suffix = " MB";
																	} else if (decimals < 12) {
																		d = (Math.round(d / 10000000) / 100);
																		suffix = " GB";
																	} else {
																		d = (Math.round(d / 10000000000) / 100);
																		suffix = " TB";
																	}
																	d = d3.format(
																					"f")
																			(d);
																	return d
																			+ suffix;
																});
											}
											// var legend = chart.legend();
											var svg = d3.select('#graph_' + key + ' svg')
													.datum(newResult)
													.transition().duration(0)
													.call(chart);
											nv.utils.windowResize(chart.update);
										});
							} else {
								$("#graph_" + key).empty();
								$("#graph_" + key).append("<div id=error_" + key + "></div>");
								$("#error_" + key).append(
										'<h2>Sorry, Unable to get ' + key
												+ ' graph.</h2>').css({
									'text-align' : 'center',
									'height' : '300px',
									'width' : '450px',
								});
								$('#graphButtonGroup_JobMonitoring').css("display", "none");
							}
						});
	},
	countDecimals : function(v) {
		var test = v, count = 0;
		while (test > 10) {
			test /= 10;
			count++;
		}
		return count;
	},
	// custom formatting functions:
	toTerra : function(d) {
		return (Math.round(d / 10000000000) / 100) + " T";
	},
	toGiga : function(d) {
		return (Math.round(d / 10000000) / 100) + " G";
	},
	toMega : function(d) {
		return (Math.round(d / 10000) / 100) + " M";
	},
	toKilo : function(d) {
		return (Math.round(d / 10) / 100) + " k";
	},

	// this function will create Node Utilization trend class
	nodeUtilizationTrend : function() {
		/*com.impetus.ankush.oClusterMonitoring.loadChild(baseUrl + '/oClusterMonitoring/nodeUtilizationTrend', 'get',
				'IP / Utilization Graphs', currentClusterName, null, "com.impetus.ankush.oClusterMonitoring.graphDraw(\'"+graphInitialTime+"\')");*/
		$('#content-panel').sectionSlider('addChildPanel', {
            url : baseUrl + '/oClusterMonitoring/nodeUtilizationTrend',
            method : 'get',
            title : 'IP / Utilization Graphs',
            tooltipTitle : currentClusterName,
            previousCallBack : "com.impetus.ankush.oClusterMonitoring.graphDraw(\'"+graphInitialTime+"\')",
            callback : function() {
            	
            },
            params : {
				clusterId : monitorClusterId,
				clusterTechnology : 'Oracle NoSQL',
			},
            callbackData : {
            }
        });	
	},
	// this will load child page
	loadChild : function(url, method, title, tooltipTitle, data,previousCallBack, callback, callbackData) {
		if (data) {
			$('#content-panel').sectionSlider('addChildPanel', {
				url : url,
				method : method,
				title : title,
				tooltipTitle : tooltipTitle,
				previousCallBack : previousCallBack,
				params : {
					clusterId : data.id,
					clusterName : data.name,
					clusterTechnology : data.technology,
					clusterEnvironment : data.env
				}
			});
		} else {
			$('#content-panel').sectionSlider('addChildPanel', {
				url : url,
				method : method,
				title : title,
				tooltipTitle : tooltipTitle,
				previousCallBack : previousCallBack,
			});
		}
	},
};
$(document).ready(function() {/*
	Code for bootstarp multi select box for shradtable columns
	$('.dropdown input, .dropdown label').click(function (event) {
	    event.stopPropagation();
	});
	$('#shardNodeTableColumns').multiselect({
		
		  maxHeight: false,
	      buttonText: function(options) {
	        if (options.length == 0) {
	          return 'No column selected <b class="caret"></b>';
	        }
	        else if (options.length > 3) {
	          return options.length + ' columns selected  <b class="caret"></b>';
	          
	        }
	        else {
	          //var selected = '';
	          //options.each(function() {
	            //selected += $(this).text() + ', ';
	          //});
	          //return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
	        	return options.length + ' columns selected  <b class="caret"></b>';
	        }
	      },
	    onChange:function(element, checked){
			//alert(element[0].value+'->'+checked);
			//if(checked===true)
			fnDataTableShowHide(element[0].value,'shardNodeTable');
		   // console.log(element);
		}
	});
	function fnDataTableShowHide( iCol,elmid )
	{
		 Get the DataTables object again - this is not a recreation, just a get of the object 
		
		var oTable = $('#shardNodeTable').dataTable();
		if(oTable.fnSettings().aoColumns[iCol]){
		var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
		oTable.fnSetColumnVis( iCol, bVis ? false : true );
		}
	}
	Code for bootstarp multi select box for shradtable columns*/
});
