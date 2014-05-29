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

var is_autoRefresh_nodeTilesAndDetails = null;
var nodeDetailsTable = null;
var nodeDeleteDataCommon = null;
//This function will load node details page
	com.impetus.ankush.commonMonitoring.nodeDetails =  function() {
		com.impetus.ankush.commonMonitoring.nodesTileCreate();
		var nodesUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/nodes';
		com.impetus.ankush.placeAjaxCall(nodesUrl, 'GET', true, null, function(data){
			com.impetus.ankush.commonMonitoring.nodesData = data;
			$("#nodesDetailTable_filter").css({
				'display' : 'none'
			});
			$('#searchNodeDetailTable').keyup(function() {
				$("#nodesDetailTable").dataTable().fnFilter($(this).val());
			});
			$("#popover-content-hadoopNodesDetail").empty();
			$("#popover-content-hadoopNodesDetail").css('display', 'none');
			$("#error-div-hadoopNodesDetail").css('display', 'none');
			$("#errorBtn-hadoopNodesDetail").css('display', 'none');
			if(nodeDetailsTable != null)
				nodeDetailsTable.fnClearTable();

			$("#nodeListCommonTech").attr("checked", false);
			var nodesData = com.impetus.ankush.commonMonitoring.nodesData;
			var flag_DeleteProgress = false;
			var mainList = [];
			var count = 0;
			// iterating the over the nodes list.
			if(nodesData.output.status == false){
				return ;
			}
			if((com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid') || (com.impetus.ankush.commonMonitoring.clusterEnv == 'Cloud'))
				$('#btnNodeList_DeleteNodes').hide();
			$.each(nodesData.output.nodes,function(index, node) {
						var dataList = [];
						var checkboxNode = '<input type="checkbox" id="checkboxNode-'
							+ index + '" onclick="com.impetus.ankush.commonMonitoring.enableDeleteButton(\'checkboxNode-'
							+ index + '\')" class="checkboxoption"/ >';
						
//						if(node.nameNode || node.secondaryNameNode || node.standByNameNode){
//							checkboxNode = '<input type="checkbox" id="checkboxNode-'
//								+ index + '" disabled="disabled" onclick="com.impetus.ankush.commonMonitoring.enableDeleteButton(\'checkboxNode-'
//								+ index + '\')" class="checkboxoption"/ >';
//						}else{
//							checkboxNode = '<input type="checkbox" id="checkboxNode-'
//								+ index + '" onclick="com.impetus.ankush.commonMonitoring.enableDeleteButton(\'checkboxNode-'
//								+ index + '\')" class="checkboxoption"/ >';
//						}
						var navigationLink = '<a href="#"><img id="nodeNavigation-'
							+ index
							+ '"'
							+ 'src="'
							+ baseUrl
							+ '/public/images/icon-chevron-right.png"'
							+ ' onclick="com.impetus.ankush.commonMonitoring.nodeDrillDown('+count+');"/></a>';
						if(node.nodeState == 'removing') {
							flag_DeleteProgress = true;
							checkboxNode = '<input type="checkbox" id="checkboxNode-'
								+ index + '" disabled="disabled"/ >';
							navigationLink = '<a href="#"><img id="nodeNavigation-'
								+ index
								+ '"'
								+ 'src="'
								+ baseUrl
								+ '/public/images/icon-chevron-right.png"/></a>';
							$("#btnNodeList_DeleteNodes").addClass('disabled');
							$("#btnNodeList_DeleteNodes").removeAttr("onclick");
						}
						mainList.length = 0;
						dataList.push(checkboxNode);
						dataList.push(node.publicIp);
						dataList.push(node.type);
						if(node.nodeState=='deployed'){
							if(node.serviceStatus){
								dataList.push('Running');
							}else{
								dataList.push('Down');
							}
						}else{
							dataList.push(node.nodeState);
						}
						var cpuUsage = node.cpuUsage;
						var totalMemory = node.totalMemory;
						var usedMemory = node.usedMemory;
						if(cpuUsage != 'NA')
							cpuUsage = cpuUsage+' %';
						if(totalMemory != 'NA')
							totalMemory = totalMemory+' GB';
						if(usedMemory != 'NA')
							usedMemory = usedMemory+' GB';
						dataList.push(cpuUsage);
						dataList.push(totalMemory);
						dataList.push(usedMemory);
						dataList.push(node.os);
						dataList.push(navigationLink);
						mainList.push(dataList);
						if(nodeDetailsTable != null)
							nodeDetailsTable.fnAddData(mainList);
						if(com.impetus.ankush.commonMonitoring.clusterTechnology=="Storm"){
							var masterNode = node.type.split('Nimbus');
							 if(masterNode.length > 1){
                                    $('#checkboxNode-'+index).attr('disabled','disabled');
                                }
						}
						if(!node.serviceStatus){
							$('td', $('#rowIdNode-' + index)).addClass('error-row');
						}
						count++;
					});
			if(flag_DeleteProgress) {
				var nodeCount = nodeDetailsTable.fnGetData().length;
				$("#btnNodeList_DeleteNodes").addClass('disabled');
				for(var i = 0; i < nodeCount; i++) {
					$("#checkboxNode-"+i).attr('disabled', true);
				}
			}
			if((com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid') || (com.impetus.ankush.commonMonitoring.clusterEnv == 'Cloud')){
				$('.checkboxoption').remove();
				$('#nodeListCommonTech').remove();
			}
		});
				
	};
	//this function will create tiles on node details page
	com.impetus.ankush.commonMonitoring.nodesTileCreate =  function() {
		var tileData = com.impetus.ankush.commonMonitoring.nodesData;
		var clusterTiles = {};
		if(tileData.output.status == false){
			return ;
		}
		else{
			clusterTiles = com.impetus.ankush.tileReordring(tileData.output.tiles);
		}	
		$('#tilesNodesPage').empty();
		var tile = document.getElementById('tilesNodesPage');
		if(tile == null)
			return;
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
		$('#tilesNodesPage').masonry({ itemSelector : '.item',
			columnWidth : 100 });

	};
	//function to send delete node request
	com.impetus.ankush.commonMonitoring.sendDeleteNodesRequest = function(){
		var hashForClass = {
				'Storm' : "com.impetus.ankush.storm.StormClusterConf",
				'Kafka' : "com.impetus.ankush.kafka.KafkaClusterConf",
				'Hadoop' : "com.impetus.ankush.hadoop.config.HadoopClusterConf",
				'Hadoop2' : "com.impetus.ankush.hadoop.config.HadoopClusterConf",
				'Cassandra' : "com.impetus.ankush.cassandra.CassandraClusterConf",
				'ElasticSearch' : "com.impetus.ankush.elasticsearch.ElasticSearchClusterConf",
			};
		var nodeCount = nodeDetailsTable.fnGetData().length;
	    nodeDeleteDataCommon = new Object();
		nodeDeleteDataCommon["@class"] = hashForClass[com.impetus.ankush.commonMonitoring.clusterTechnology];
		nodeDeleteDataCommon.newNodes = [];
		for ( var i = 0; i < nodeCount; i++) {
			if ($("#checkboxNode-" + i).attr("checked")) {
				var node = new Object();
				node.publicIp = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[i].publicIp;
				nodeDeleteDataCommon.newNodes.push(node);
			}
		}
		if (nodeDeleteDataCommon.newNodes.length > 0) {
			$('#deleteNodeDialogCommon').appendTo('body').modal('show');
		} else {
			$("#invalidRequestMsg_ND").text('Select at least one node for this action.');
			$("#div_RequestInvalid_NodeDetails").dialog({
				modal : true,
				resizable : false,
				dialogClass : 'alert',
				width : 400,
				draggable : true,
				modal : true,
				position : 'center',
				close : function(ev, ui) {
					$(this).dialog("destroy");
				},
			});
			$('.ui-dialog-titlebar').hide();
			$('.ui-dialog :button').blur();
		}
		$("#checkHead_NodeList").attr("checked", false);
		//com.impetus.ankush.hadoopMonitoring.checkAll_NodeList();

	};
	//function will delete nodes
	com.impetus.ankush.commonMonitoring.deleteNodeCommon = function() {
		$("#deleteNodeDialogCommon").modal('hide');
		var deleteNodesUrl = baseUrl + '/cluster/' + com.impetus.ankush.commonMonitoring.clusterId + '/remove';
		$.ajax({
			'type' : 'POST',
			'url' : deleteNodesUrl,
			'contentType' : 'application/json',
			'data' : JSON.stringify(nodeDeleteDataCommon),
			'dataType' : 'json',
			'success' : function(result) {
				if (result.output.status) {
					com.impetus.ankush.commonMonitoring.nodeDetails();
					com.impetus.ankush.removeChild(2);
				}
				else {
					com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content-hadoopNodesDetail', 'error-div-hadoopNodesDetail', 'errorBtn-hadoopNodesDetail');
					return;
				}
			},
		});
	};
	com.impetus.ankush.commonMonitoring.checked_unchecked_all = function(ckeckbox,elem){
		if($(elem).is(":checked")){
			$("#btnNodeList_DeleteNodes").removeClass('disabled');
			$("#btnNodeList_DeleteNodes").attr("onclick","com.impetus.ankush.commonMonitoring.sendDeleteNodesRequest()");
		}else{
			$("#btnNodeList_DeleteNodes").addClass('disabled');
		}
		com.impetus.ankush.checked_unchecked_all(ckeckbox,elem);
	};
	// this function will start auto refresh of nodes page will be called on jsp load
	com.impetus.ankush.commonMonitoring.nodesPageAutorefresh = function(){
		//is_autoRefresh_nodeTilesAndDetails = setInterval("com.impetus.ankush.commonMonitoring.nodeDetails();",30000);
	};
	//this function will remove auto refresh of nodes page will be called on slider child out
	com.impetus.ankush.commonMonitoring.removeNodesPageAutoRefresh = function(){
		//is_autoRefresh_nodeTilesAndDetails = window.clearInterval(is_autoRefresh_nodeTilesAndDetails);
	};
	//this function will do the task necessary on slider child out
	com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad = function(removeDialogFlag){
		if(removeDialogFlag == 1)
			$("#deleteNodeDialogCommon").remove();
		//com.impetus.ankush.commonMonitoring.removeNodesPageAutoRefresh();
		com.impetus.ankush.commonMonitoring.createCommonTiles(com.impetus.ankush.commonMonitoring.clusterId);
		com.impetus.ankush.commonMonitoring.commonHeatMaps(com.impetus.ankush.commonMonitoring.clusterId);
		//com.impetus.ankush.commonMonitoring.monitoringPageAutorefresh();
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Kafka'){
			//com.impetus.ankush.kafkaMonitoring.kafkaMonitoringPageAutorefresh();
			com.impetus.ankush.kafkaMonitoring.brokerListTable();
		}
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Storm'){
			//com.impetus.ankush.stormMonitoring.stormMonitoringPageAutorefresh();
			com.impetus.ankush.stormMonitoring.createTopologySummaryTable();
		}
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'ElasticSearch'){
			//com.impetus.ankush.stormMonitoring.stormMonitoringPageAutorefresh();
			com.impetus.ankush.elasticSearchMonitoring.indicesListTable();
		}
		if((com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hadoop') || (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hadoop2')){
			//com.impetus.ankush.stormMonitoring.stormMonitoringPageAutorefresh();
			com.impetus.ankush.hadoopMonitoring.populateTileTables('hadoopNodesSummary');
			com.impetus.ankush.hadoopMonitoring.populateTileTables('hdfsMonitoringUsage');
			com.impetus.ankush.hadoopMonitoring.populateTileTables('mapredMonitoringJobs');
		}
	};
			
