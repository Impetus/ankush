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

var topicListData = null;
var is_autorefresh_monitoringKafkaBrokerTableHybrid = null;
var is_autorefresh_monitoringKafkaTilesHybrid = null;
var ipForKafkaNode = null;
com.impetus.ankush.kafkaMonitoring = {
	//this function will populate broker table and topic table both on monitoring page and individual monitoring page of Hybrid	
	brokerListTable : function(){
		var brokerListUrl = null;
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			brokerListUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/brokers?technology=Kafka';
		else
			brokerListUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/brokers';
		$.ajax({
			'type' : 'GET',
			'url' : brokerListUrl,
			'contentType' : 'application/json',
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				if(brokerListTable != null)
					brokerListTable.fnClearTable();
				if(result.output.status == false){
					brokerListTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
					brokerListTable.fnClearTable();
					topicListTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
					topicListTable.fnClearTable();
					return;
				}
				if(result.output.status == true){
					if(result.output.brokers == null)
						return ;
					else{
						topicListData = result.output.topics;
					}
					for(var i = 0 ; i < result.output.brokers.length ; i++){
						/*var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.kafkaMonitoring.brokerDrillDown(\'' + (result.output.brokers[i].brokerIP) + '\');"><img id="navigationImgTopologyDrillDown'
						+ i
						+ '" src="'
						+ baseUrl
						+ '/public/images/icon-chevron-right.png"/></a>';*/
						brokerListTable.fnAddData([
						                           	result.output.brokers[i].brokerId,
						                           	result.output.brokers[i].brokerIP,
						                           	result.output.brokers[i].leaderCount,
						                           	result.output.brokers[i].followerCount,
						                           	/*navigationStormTopology*/
						                           ]);
					}
					if(result.output.topics == null){
						topicListTable.fnSettings().oLanguage.sEmptyTable = result.output.message;
						topicListTable.fnClearTable();
						return;
					}
					if(topicListTable !=null)
						topicListTable.fnClearTable();
					for(var i = 0 ; i < result.output.topics.length ; i++){
						var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.kafkaMonitoring.topicDrillDown(' + (i) + ');"><img id="navigationImgTopologyDrillDown'
						+ i
						+ '" src="'
						+ baseUrl
						+ '/public/images/icon-chevron-right.png"/></a>';
						topicListTable.fnAddData([
						                           	result.output.topics[i].topicName,
						                           	result.output.topics[i].partitionCount,
						                           	result.output.topics[i].replicas,
						                           	navigationStormTopology
						                           ]);
					}
					
				}
			},
			'error' : function(){
			}
		});
	},
	topicDrillDown : function(topicIndex){
		$('#content-panel').sectionSlider('addChildPanel', {
            url : baseUrl + '/kafkaMonitoring/topicDrillDown',
            method : 'get',
            title : '',
            tooltipTitle : com.impetus.ankush.commonMonitoring.clusterName,
            previousCallBack : "com.impetus.ankush.kafkaMonitoring.topicDrillDownPrePage();",
            callback : function() {
            	com.impetus.ankush.kafkaMonitoring.topicDrillDownDetailTable(topicIndex);
            	$('#topicDrillDown').text(topicListData[topicIndex].topicName);
            },
            params : {
            	topicIndex : topicIndex
            },
            callbackData : {
            }
        });	
		
	},
	brokerDrillDownDetailTable : function(ip){
		ipForKafkaNode = ip;
		var dataUrl = null;
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
			dataUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/brokerData?ip='+ip+'&technology='+com.impetus.ankush.commonMonitoring.hybridTechnology;
		}else{
			dataUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/brokerData?ip='+ip;
		}
		com.impetus.ankush.placeAjaxCall(dataUrl, "GET", true, null, function(result){
			com.impetus.ankush.kafkaMonitoring.brokerTableBox(result);
			com.impetus.ankush.kafkaMonitoring.logTableBox(result);
			com.impetus.ankush.kafkaMonitoring.topicTableBox(result);
		});
	},
	brokerTableBox : function(result){
		if(brokerDrillDownDetailTable != null)
			brokerDrillDownDetailTable.fnClearTable();
		if(result.output.status == true){
			if((result.output.brokerData == undefined) || (result.output.brokerData == null)){
				brokerDrillDownDetailTable.fnSettings().oLanguage.sEmptyTable = "No records found !";
				brokerDrillDownDetailTable.fnClearTable();
				return ;
			}
			if((result.output.brokerData.allTopicsMsgIn == undefined) || (result.output.brokerData.allTopicsMsgIn == null)){
				brokerDrillDownDetailTable.fnSettings().oLanguage.sEmptyTable = "No records found !";
				brokerDrillDownDetailTable.fnClearTable();
				return ;
			}
			var navigationBrokerTopology = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;<a href='#' onclick='com.impetus.ankush.kafkaMonitoring.brokerDetailPageOnDrillDown("+JSON.stringify(result)+");'><img src='"
			+ baseUrl
			+ "/public/images/icon-chevron-right.png'/></a>";
			$("#broker-drilldown-arrow").html(navigationBrokerTopology);
			var keyForTable = {
					0 : 'MeanRate',
					1 : 'OneMinuteRate'
			};
			
			for(var key in result.output.brokerData){
				if(result.output.brokerData[key] == null)
					continue;
				if((key != 'logFlushDetails') && (key != 'topicsData')){
					brokerDrillDownDetailTable.fnAddData([
					                                      	key,
					                                      	result.output.brokerData[key].Count
					                                      ]);
					for(var i = 0; i < 2 ; i++){
						brokerDrillDownDetailTable.fnAddData([
						                                      	keyForTable[i],
						                                      	result.output.brokerData[key][keyForTable[i]]
						                                      ]);
					}
				}
			}
		}else{
			$("#error-broker-drilldown").show();
			$("#error-broker-drilldown").html(result.output.error[0]);
			brokerDrillDownDetailTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
			brokerDrillDownDetailTable.fnClearTable();
		}
	},
	brokerDetailPageOnDrillDown : function(result){
		$('#content-panel').sectionSlider('addChildPanel', {
            url : baseUrl + '/kafkaMonitoring/brokerBoxDetail',
            method : 'get',
            title : "Broker Details",
            tooltipTitle : ipForKafkaNode,
            previousCallBack : "com.impetus.ankush.kafkaMonitoring.brokerLogTopicPageRemoval()",
            callback : function() {
            	com.impetus.ankush.kafkaMonitoring.brokerDetailPopulateOnDrillDown(result);
            },
            callbackData : {
            }
        });	
	},
	brokerDetailPopulateOnDrillDown : function(result){
		if(brokerBoxDetailTable != null)
			brokerBoxDetailTable.fnClearTable();
		if((result.output.brokerData == undefined) || (result.output.brokerData == null))
			return ;
		if(result.output.status == true){
			for(var key in result.output.brokerData){
				if((result.output.brokerData[key] == null))
					continue;
					if((key != 'logFlushDetails') && (key != 'topicsData')){
						for(var prop in result.output.brokerData[key]){
							brokerBoxDetailTable.fnAddData([
							                                key,'',
							                                prop,
							                                result.output.brokerData[key][prop]
							                                ]);
						}
					}
			}
		}else{
			brokerBoxDetailTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
			brokerBoxDetailTable.fnClearTable();
		}
	},
	logDetailPageOnDrillDown : function(result){
		$('#content-panel').sectionSlider('addChildPanel', {
            url : baseUrl + '/kafkaMonitoring/logBoxDetail',
            method : 'get',
            title : "Log Details",
            tooltipTitle : ipForKafkaNode,
            previousCallBack : "com.impetus.ankush.kafkaMonitoring.brokerLogTopicPageRemoval()",
            callback : function() {
            	com.impetus.ankush.kafkaMonitoring.logDetailPopulateOnDrillDown(result);
            },
            callbackData : {
            }
        });	
	},
	logDetailPopulateOnDrillDown : function(result){
		if(logBoxDetailTable != null)
			logBoxDetailTable.fnClearTable();
		if((result.output.brokerData == undefined) || (result.output.brokerData == null))
			return ;
		
		if(result.output.status == true){
			if((result.output.brokerData.logFlushDetails == undefined) || (result.output.brokerData.logFlushDetails == null)){
				logBoxDetailTable.fnSettings().oLanguage.sEmptyTable = "No records found !";
				logBoxDetailTable.fnClearTable();
				return ;
			}
			for(var key in result.output.brokerData.logFlushDetails){
					logBoxDetailTable.fnAddData([
						                                key,
						                                result.output.brokerData.logFlushDetails[key],
						                                ]);
		
			}
		}else{
			logBoxDetailTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
			logBoxDetailTable.fnClearTable();
		}
	},
	logTableBox : function(result){
		if(logFlushDetailTable != null)
			logFlushDetailTable.fnClearTable();
		if(result.output.status == true){
			if((result.output.brokerData == undefined) || (result.output.brokerData == null))
				return ;
			if((result.output.brokerData.logFlushDetails == undefined) || (result.output.brokerData.logFlushDetails == null)){
				logFlushDetailTable.fnSettings().oLanguage.sEmptyTable = "No records found !";
				logFlushDetailTable.fnClearTable();
				return ;
			}
			var keyForTable = {
					'Count' : result.output.brokerData.logFlushDetails.Count,
					'MeanRate' : result.output.brokerData.logFlushDetails.MeanRate+" Calls/Sec",
					'OneMinuteRate' : result.output.brokerData.logFlushDetails.OneMinuteRate+" Calls/Sec",
					'Max Latency' : result.output.brokerData.logFlushDetails.Max+" MILLISECONDS",
					'Min Latency' : result.output.brokerData.logFlushDetails.Min+" MILLISECONDS",
					'Mean Latency' : result.output.brokerData.logFlushDetails.Mean+" MILLISECONDS",
			};
			var navigationLogTopology = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;<a href='#' onclick='com.impetus.ankush.kafkaMonitoring.logDetailPageOnDrillDown("+JSON.stringify(result)+");'><img src='"
			+ baseUrl
			+ "/public/images/icon-chevron-right.png'/></a>";
			$("#log-drilldown-arrow").html(navigationLogTopology);
			for(var key in keyForTable){
				logFlushDetailTable.fnAddData([
				                                      	key,
				                                      	keyForTable[key]
				                                      ]);
			}
			
		}else{
			$("#error-broker-drilldown").show();
			$("#error-broker-drilldown").html(result.output.error[0]);
			logFlushDetailTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
			logFlushDetailTable.fnClearTable();
		}
	},
	topicTableBox : function(result){
		if(topicNameListTable != null)
			topicNameListTable.fnClearTable();
		if(result.output.status == true){
			if((result.output.brokerData == undefined) || (result.output.brokerData == null))
				return ;
			if((result.output.brokerData.topicsData == undefined) || (result.output.brokerData.topicsData == null)){
				topicNameListTable.fnSettings().oLanguage.sEmptyTable = "No records found !";
				topicNameListTable.fnClearTable();
				return ;
			}
			/*var navigationTopicTopology = '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;<a href="#"><img src="'
			+ baseUrl
			+ '/public/images/icon-chevron-right.png"/></a>';
			$("#topic-drilldown-arrow").html(navigationTopicTopology);*/
			for(var key in result.output.brokerData.topicsData){
				var navigationTopology = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;<a href='#' onclick='com.impetus.ankush.kafkaMonitoring.topicDetailPageOnDrillDown("+JSON.stringify(result)+",\""+key+"\");'><img src='"
				+ baseUrl
				+ "/public/images/icon-chevron-right.png'/></a>";
				topicNameListTable.fnAddData([
				                                      	key,
				                                      	navigationTopology
				                                      ]);
			}
			
		}else{
			$("#error-broker-drilldown").show();
			$("#error-broker-drilldown").html(result.output.error[0]);
			topicNameListTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
			topicNameListTable.fnClearTable();
		}
	},
	topicDetailPageOnDrillDown : function(result,topicName){
		$('#content-panel').sectionSlider('addChildPanel', {
            url : baseUrl + '/kafkaMonitoring/topicMoreDetailDrillDown',
            method : 'get',
            title : topicName,
            tooltipTitle : ipForKafkaNode,
            previousCallBack : "com.impetus.ankush.kafkaMonitoring.brokerLogTopicPageRemoval()",
            callback : function() {
            	com.impetus.ankush.kafkaMonitoring.topicDetailPopulateOnDrillDown(result,topicName);
            	$("#topicMoreDrillDown").html(topicName);
            },
            callbackData : {
            }
        });	
	},
	topicDetailPopulateOnDrillDown : function(result,topicName){
		if(topicMoreDetailDrillTable != null)
			topicMoreDetailDrillTable.fnClearTable();
		if((result.output.brokerData == undefined) || (result.output.brokerData == null))
			return ;
		if(result.output.status == true){
			if((result.output.brokerData.topicsData[topicName] == undefined) || (result.output.brokerData.topicsData[topicName] == null)){
				topicMoreDetailDrillTable.fnSettings().oLanguage.sEmptyTable = "No records found !";
				topicMoreDetailDrillTable.fnClearTable();
				return ;
			}
			for(var key in result.output.brokerData.topicsData[topicName]){
				if(result.output.brokerData.topicsData[topicName]['msgIn'] == null){
					topicMoreDetailDrillTable.fnSettings().oLanguage.sEmptyTable = "No records found !";
					topicMoreDetailDrillTable.fnClearTable();
				}
				if(key != "partitionData"){
					for(var prop in result.output.brokerData.topicsData[topicName][key]){
					topicMoreDetailDrillTable.fnAddData([
							                                key,'',
							                                prop,
							                                result.output.brokerData.topicsData[topicName][key][prop],
							                                ]);
					
					}
				}else{
					for(var key1 in result.output.brokerData.topicsData[topicName][key]){
						for(var prop in result.output.brokerData.topicsData[topicName][key][key1]){
							partitionDetail.fnAddData([
								                                key1,'',
								                                prop,
								                                result.output.brokerData.topicsData[topicName][key][key1][prop],
								                                ]);
				
						}
					}
				}
			}
		}else{
			topicMoreDetailDrillTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
			topicMoreDetailDrillTable.fnClearTable();
			partitionDetail.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
			partitionDetail.fnClearTable();
		}
	},
	topicMoreDetailDrillDownPopulate : function(data){
		data = JSON.parse(data);
		for(var key in data){
			if(key != 'partitionData'){
				for(var prop in data[key]){
					topicMoreDetailDrillTable.fnAddData([
					                                        key,'',
					                                        prop,
					                                        data[key][prop]
					                                        ]);
				}		
			}
		}
	},
	topicDrillDownDetailTable : function(topicIndex){
		var topicListUrl = null;
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			topicListUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/topicDetail?technology=Kafka';
		else
			topicListUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/topicDetail';
		var data = {};
		data.topicName = topicListData[topicIndex].topicName;
		$.ajax({
			'type' : 'POST',
			'url' : topicListUrl,
			'contentType' : 'application/json',
			'data' : JSON.stringify(data),
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				if(topicDrillDownDetailTable != null)
					topicDrillDownDetailTable.fnClearTable();
				if(result.output.status == true){
					for(var i = 0 ; i < result.output.topicDetail.length ; i++){
						topicDrillDownDetailTable.fnAddData([
						                           	result.output.topicDetail[i].partition,
						                           	result.output.topicDetail[i].leader,
						                           	result.output.topicDetail[i].replicas,
						                           	result.output.topicDetail[i].isr
						                           ]);
					}
				}else{
					topicDrillDownDetailTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
					topicDrillDownDetailTable.fnClearTable();
				}
			},
			'error' : function(){
			}
			
		});
	},
	createTopicValidate:function(){
		$('#validateErrorTopicCreate').html('').hide();
        var errorMsg = '';
        $("#errorDivTopicCreate").html('').hide();
       var errorCount = 0;
        if (!com.impetus.ankush.validation.empty($('#topicNameKafka').val())) {
	            errorCount++;
	            errorMsg = 'Topic Name field empty.';
	            com.impetus.ankush.common.tooltipMsgChange('topicNameKafka','Topic name cannot be empty');
	            var divId='topicNameKafka';
	            $("#errorDivTopicCreate").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        }else {
	        com.impetus.ankush.common.tooltipOriginal('topicNameKafka','Enter Topic Name');
	        $('#topicNameKafka').removeClass('error-box');
        }
        if(!com.impetus.ankush.validation.empty($('#partitionCountKafka').val())){
            errorCount++;
            errorMsg = 'Partition count field empty.';
            com.impetus.ankush.common.tooltipMsgChange('partitionCountKafka','Partition Count cannot be empty');
            var divId='partitionCountKafka';
            $("#errorDivTopicCreate").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
        }else if (!com.impetus.ankush.validation.numeric($('#partitionCountKafka').val())) {
            errorCount++;
            errorMsg = 'Partition count field must be numeric.';
            com.impetus.ankush.common.tooltipMsgChange('partitionCountKafka','Partition count must be numeric');
            var divId='partitionCountKafka';
            $("#errorDivTopicCreate").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        }else if ($('#partitionCountKafka').val()<1) {
            errorCount++;
            errorMsg = 'Partition count field must be greater than 0';
            com.impetus.ankush.common.tooltipMsgChange('partitionCountKafka','Partition count must be greater than 0');
            var divId='partitionCountKafka';
            $("#errorDivTopicCreate").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        } else{
            com.impetus.ankush.common.tooltipOriginal('partitionCountKafka','Enter Partition Count');
            $('#partitionCountKafka').removeClass('error-box');
        }
        if(!com.impetus.ankush.validation.empty($('#replicasKafka').val())){
            errorCount++;
            errorMsg = 'Replicas field empty.';
            com.impetus.ankush.common.tooltipMsgChange('replicasKafka','Replicas cannot be empty');
            var divId='replicasKafka';
            $("#errorDivTopicCreate").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
        }else if (!com.impetus.ankush.validation.numeric($('#replicasKafka').val())) {
            errorCount++;
            errorMsg = 'Replicas field must be numeric.';
            com.impetus.ankush.common.tooltipMsgChange('partitionCountKafka','Replicas must be numeric');
            var divId='replicasKafka';
            $("#errorDivTopicCreate").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        }else if ($('#replicasKafka').val()<1) {
            errorCount++;
            errorMsg = 'Replicas field must be greater than 0';
            com.impetus.ankush.common.tooltipMsgChange('replicasKafka','Replicas must be greater than 0');
            flag = true;
            var divId='replicasKafka';
            $("#errorDivTopicCreate").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        }else{
            com.impetus.ankush.common.tooltipOriginal('replicasKafka','Enter Replicas');
            $('#replicasKafka').removeClass('error-box');
        }
        if(errorCount>0 && errorMsg!=''){
        	 $('#validateErrorTopicCreate').show().html(errorCount + ' Error');
        	  $("#errorDivTopicCreate").show();
        }else{
        	com.impetus.ankush.kafkaMonitoring.createTopic();
        }
		
	},
	createTopic : function(topicIndex){
		var url= baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/createTopic';
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
			 url= baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/createTopic?technology=Kafka';
		}
		var data = {
				topicMetadata:{},
		};
		data.topicMetadata.topicName=$('#topicNameKafka').val();
		data.topicMetadata.partitionCount=$('#partitionCountKafka').val();
		data.topicMetadata.replicas=$('#replicasKafka').val();
		$('#errorDivTopicCreate').hide().html('');
		$('#kafkaTopicCreateBtn').button();
		$('#kafkaTopicCreateBtn').button('loading');
		$.ajax({
			'type' : 'POST',
			'url' : url,
			'contentType' : 'application/json',
			'data' : JSON.stringify(data),
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				if(result.output.status){
					com.impetus.ankush.removeChild($.data(document, "panels").children.length);
					com.impetus.ankush.kafkaMonitoring.brokerListTable();
				}else{
					for(var i=0;i<result.output.error.length;i++){
						$('#errorDivTopicCreate').append("<div class='errorLineDiv'><a href='#' >"+(i+1)+". "+result.output.error[i]+"</a></div>");	
					}
					$('#errorDivTopicCreate').show();
				}
				$('#kafkaTopicCreateBtn').button('reset');
			},
			'error' : function(){
				$('#kafkaTopicCreateBtn').button('reset');
			}
			
		});
	},
	 removeChildPage:function(){
		 com.impetus.ankush.removeChild($.data(document, "panels").children.length);
	 },
	createTiles : function(ip){
		currentClusterId = com.impetus.ankush.commonMonitoring.clusterId;
		if(undefined == currentClusterId)
			return;
		var tileUrl = baseUrl + '/monitor/' + currentClusterId + '/brokerTiles?brokerIP='+ip;
		com.impetus.ankush.placeAjaxCall(tileUrl, "GET", true, null, function(tileData){
			if(tileData.output.status == false){
				return ;
			}
			$('#tilesBrokerDrillDown').empty();
			var $wrapperAll = $('#tilesBrokerDrillDown'), $boxes, innerHtml, boxMaker = {}, tileClass, line1Class;
			var tileAction;
			var tiles = [], runningTiles = [], errorTiles = [], warningTiles = [];;
			var clustorTiles, tile;
			$wrapperAll.masonry({
				itemSelector : '.box'
			});
			
			boxMaker.makeBoxes = function(innerHtml, boxClass, tileAction) {
				var boxes = [];
				var box = document.createElement('div');
				if(tileAction.action != null) {
					box.style.cursor = "pointer";
					box.onclick = function() {
						com.impetus.ankush.commonMonitoring
						.actionFunction_ClusterMonitoringTiles(
								tileAction.actionName, tileAction.action,
								tileAction.data, tileAction.line3);
					};
				}
				box.className = 'box ' + boxClass;
				box.innerHTML = innerHtml;
				boxes.push(box);
				return boxes;
			};
			prependAppendTiles();
			function prependAppendTiles() {
				if (tileData.output == null) {
					return;
				}
				var clusterTilesInfo = tileData.output;
				for ( var i = 0; i < clusterTilesInfo.tiles.length; i++) {
					var d = clusterTilesInfo.tiles[i];
					switch (d.status) {
					case 'Error':
						errorTiles.push(d);
						break;
					case 'Critical':
						errorTiles.push(d);
						break;
					case 'Normal':
						runningTiles.push(d);
						break;
					case 'Warning':
						warningTiles.push(d);
						break;
					}
				}
				tiles = [ runningTiles, warningTiles , errorTiles ];
				//var flagAddNode_Disable = false;
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
									/*innerHtml += '<div class="' + line1Class + '">'
									+ tile.line1 + '</div>'; old structure*/
									innerHtml += '<div class="'
										+ line1Class
										+ '">'
										+ '<div data-placement="bottom" data-original-title="'
										+ tile.line1
										+ '" class="clip tile-innerdiv" >'
										+ tile.line1 + '</div>' + '</div>';
									
									
									
								}
								if (tile.line2) {
									innerHtml += '<div class="descTitle">'
										+ tile.line2 + '</div>';
								}
								var tileId = 0;
								if (tile.data != null) {
									tileId = tile.data.tileid;
								}
								tileAction = {

										'actionName' : tile.line1,
										'action' : tile.url,
										'data' : tileId,
										'line3' : tile.line3,
								};
								break;
							case 'Error':
								tileClass = 'span2 errorbox';
    							line1Class = 'redTitle';
    							if (tile.line1) {
    								/*innerHtml += '<div class="'+line1Class+'">'
    										+ tile.line1; old div structure*/
    								innerHtml += '<div class="'
										+ line1Class
										+ '">'
										+ '<div data-placement="bottom" data-original-title="'
										+ tile.line1
										+ '" class="clip tile-innerdiv" >'
										+ tile.line1 + '</div>';
    								
    								
    								if (tile.line2 || tile.line3) {
    									 innerHtml += '<div class="descStyle" ><span>'+tile.line2 +'</span> <span>'+ tile.line3 +'</span></div>' ;
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
										'line3' : tile.line3,
								};
								break;
							case 'Critical':
								tileClass = 'span2 errorbox';
    							line1Class = 'redTitle';
    							if (tile.line1) {
    								/*innerHtml += '<div class="'+line1Class+'">'
    										+ tile.line1; old structure */
    								innerHtml += '<div class="'
										+ line1Class
										+ '">'
										+ '<div data-placement="bottom" data-original-title="'
										+ tile.line1
										+ '" class="clip tile-innerdiv" >'
										+ tile.line1 + '</div>';

    								if (tile.line2 || tile.line3) {
    									 innerHtml += '<div class="descStyle" ><span>'+tile.line2 +'</span> <span>'+ tile.line3 +'</span></div>' ;
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
										'line3' : tile.line3,
								};
								break;
							case 'Warning':
								tileClass = 'span2 warningbox';
        						line1Class = 'yellowTitle';
        						if (tile.line1) {
        							/*innerHtml += '<div class="'+line1Class+'">'
        									+ tile.line1; old div structure*/
        							innerHtml += '<div class="'
										+ line1Class
										+ '">'
										+ '<div data-placement="bottom" data-original-title="'
										+ tile.line1
										+ '" class="clip tile-innerdiv" >'
										+ tile.line1 + '</div>';
        							if (tile.line2 || tile.line3) {
        								 innerHtml += '<div class="descStyle" ><span>'+tile.line2 +'</span> <span>'+ tile.line3 +'</span></div>' ;
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
										'line3' : tile.line3,
								};
								break;
							}
							innerHtml += '</div>';
							$boxes = $(boxMaker.makeBoxes(innerHtml, tileClass, tileAction));
							$wrapperAll.prepend($boxes).masonry();
							/*if (tile.url == "Adding Nodes|progress") {
								flagAddNode_Disable = true;
							}*/
						}
						/*if (flagAddNode_Disable) {
							//showDialog_InvalidAddNode
							$('#lbl_invalidAddNodeOperation').empty();
							$('#lbl_invalidAddNodeOperation').text('Node Addition is already in progress. A new request can be submitted once the current operation completes.');
							$('#hadoopAddNodes').attr("onclick",
									"com.impetus.ankush.hadoopMonitoring.showDialog_InvalidAddNode()");
						} else {
							if(!($('#clusterEnv').text() == 'CLOUD')) {
								$('#hadoopAddNodes').attr("onclick",
										"com.impetus.ankush.hadoopMonitoring.addNodes("
										+ currentClusterId + ")");
							}
						}*/
					}
				}
				$('.clip').tooltip();
			}

		});

		
				
	},
	kafkaMonitoringPageAutorefresh : function(){
		/*is_autorefresh_monitoringKafkaBrokerTableHybrid = setInterval("com.impetus.ankush.kafkaMonitoring.brokerListTable();",30000);
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			is_autorefresh_monitoringKafkaTilesHybrid = setInterval("com.impetus.ankush.hybridMonitoring_kafka.createTiles();",30000);*/
	},
	topicDrillDownPrePage : function(){
		//com.impetus.ankush.kafkaMonitoring.brokerListTable();
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			com.impetus.ankush.hybridMonitoring_kafka.createTiles();
		else
			com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();
	},
	//this function willbecalled on page removal of btoker log and topic detail page
	brokerLogTopicPageRemoval : function(){
		com.impetus.ankush.commonMonitoring.pageLoadFunction_NodeDrillDown(nodeIndexVar);
	},
	removeKafkaMonitoringPageAutorefresh : function(){
		/*is_autorefresh_monitoringKafkaBrokerTableHybrid = window.clearInterval(is_autorefresh_monitoringKafkaBrokerTableHybrid);
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			is_autorefresh_monitoringKafkaTilesHybrid = window.clearInterval(is_autorefresh_monitoringKafkaTilesHybrid);*/
	}
};
