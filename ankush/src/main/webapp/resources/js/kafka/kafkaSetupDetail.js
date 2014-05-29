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

com.impetus.ankush.kafkaSetupDetail={
		/*Function for populating default values of storm cluster*/ 
		populateDefaultValues:function(result){
		//	result={"output":{"kafka":{"Kafka":{"Defaults":{"installationPath":"/home/${user}/kafka/","port":9092,"jmxPort":9999,"logDir":"/home/${user}/kafka/kafka_logs","replicationFactor":1,"numOfNetworkThreads":3,"numOfIOThreads":8,"queuedMaxRequests":500,"numPartitions":1,"logRetentionHours":168,"logRetentitionBytes":1073741824,"logCleanupIntervalMins":10,"logFlushIntervalMessage":10000,"logFlushSchedularIntervalMs":3000,"logFlushIntervalMs":3000,"controlledShutdownEnable":false,"controlledShutdownMaxRetries":3,"advancedConf":{"logLevel":"INFO,requestAppender,Error","loggerKafkaController":"INFO,controllerAppender","stateChangeLogger":"INFO,stateChangeAppender","loggerRequestChannel":"INFO, requestAppender"}},"Vendors":{"Apache":{"2.8.0-0.8.0":{"url":"http://archive.apache.org/dist/kafka/0.8.0/kafka_2.8.0-0.8.0.tar.gz"}}}},"Zookeeper":{"Defaults":{"installationHomePath":"/home/${user}/zookeeper/","clientPort":2181,"jmxPort":"12345","dataDir":"/home/${user}/zookeeper/zk_data_dir/","syncLimit":2,"initLimit":5,"tickTime":2000},"Vendors":{"Apache":{"3.4.5":{"url":"http://archive.apache.org/dist/zookeeper/zookeeper-3.4.5/zookeeper-3.4.5.tar.gz"},"3.3.5":{"url":"http://mirror.tcpdiag.net/apache/zookeeper/zookeeper-3.3.5/zookeeper-3.3.5.tar.gz"}},"Cloudera":{"3.4.5-cdh4.4.0":{"url":"http://archive.cloudera.com/cdh4/cdh/4/zookeeper-3.4.5-cdh4.4.0.tar.gz"},"3.3.3-cdh3u1":{"url":"http://archive.cloudera.com/cdh/3/zookeeper-3.3.3-cdh3u1.tar.gz"}}}}}},"status":"200","description":"Get application configuration.","errors":null};
			$('.section-body input:eq(0)').focus();
			 $('#clusterDeploy').removeAttr('disabled');
			defaultVlaue=result;
						for ( var key in result.output.kafka.Kafka.Vendors){
				$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in result.output.kafka.Kafka.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			var logLevelVal=result.output.kafka.Kafka.Defaults.advancedConf.logLevel.split(',');
			for ( var i=0;i<logLevelVal.length ;i++){
				$("#logLevelDropdown").append("<option value=\"" + logLevelVal[i] + "\">" + logLevelVal[i]+ "</option>");
			}
			$('#downloadPath').val(result.output.kafka.Kafka.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].url);
			$('#logDirectory').val(result.output.kafka.Kafka.Defaults.logDir);
			$('#installationPathKafka').val(result.output.kafka.Kafka.Defaults.installationPath);
			$('#replicationFactor').val(result.output.kafka.Kafka.Defaults.replicationFactor);
			$('#port').val(result.output.kafka.Kafka.Defaults.port);
			$('#jmxPort_kafka').val(result.output.kafka.Kafka.Defaults.jmxPort);
			$('#numOfNetworkThreads').val(result.output.kafka.Kafka.Defaults.numOfNetworkThreads);
			$('#numOfIOThreads').val(result.output.kafka.Kafka.Defaults.numOfIOThreads);
			$('#queuedMaxRequests').val(result.output.kafka.Kafka.Defaults.queuedMaxRequests);
			$('#numPartitions').val(result.output.kafka.Kafka.Defaults.numPartitions);
			$('#logRetentionHours').val(result.output.kafka.Kafka.Defaults.logRetentionHours);
			$('#logRetentitionBytes').val(result.output.kafka.Kafka.Defaults.logRetentitionBytes);
			$('#logCleanupIntervalMins').val(result.output.kafka.Kafka.Defaults.logCleanupIntervalMins);
			$('#logFlushIntervalMessage').val(result.output.kafka.Kafka.Defaults.logFlushIntervalMessage);
			$('#logFlushSchedularIntervalMs').val(result.output.kafka.Kafka.Defaults.logFlushSchedularIntervalMs);
			$('#logFlushIntervalMs').val(result.output.kafka.Kafka.Defaults.logFlushIntervalMs);
			$('#controlledShutdownMaxRetries').val(result.output.kafka.Kafka.Defaults.controlledShutdownMaxRetries);
			$('#requestLogger').val(result.output.kafka.Kafka.Defaults.advancedConf.requestLogger);
			$('#loggerKafkaController').val(result.output.kafka.Kafka.Defaults.advancedConf.loggerKafkaController);
			$('#stateChangeLogger').val(result.output.kafka.Kafka.Defaults.advancedConf.stateChangeLogger);
			$('#loggerRequestChannel').val(result.output.kafka.Kafka.Defaults.advancedConf.loggerRequestChannel);
			if(result.output.kafka.Kafka.Defaults.controlledShutdownEnable){
				$('#controlledShutdownEnable').attr('checked','checked');
			}else{
				$('#controlledShutdownEnable').removeAttr('checked');
			}
			$('#installationPathZookeeper').val(result.output.kafka.Zookeeper.Defaults.installationHomePath);
			$('#dataDirZookeeper').val(result.output.kafka.Zookeeper.Defaults.dataDir);
			for ( var key in result.output.kafka.Zookeeper.Vendors){
				$("#zookeeperVendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in result.output.kafka.Zookeeper.Vendors[$("#zookeeperVendorDropdown").val()]){
				$("#zookeeperVersionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#zookeeperDownloadPath').val(result.output.kafka.Zookeeper.Vendors[$("#zookeeperVendorDropdown").val()][$("#zookeeperVersionDropdown").val()].url);
			$('#clientPort').val(result.output.kafka.Zookeeper.Defaults.clientPort);
			$('#jmxPort_Zookeeper').val(result.output.kafka.Zookeeper.Defaults.jmxPort);
			$('#syncLimit').val(result.output.kafka.Zookeeper.Defaults.syncLimit);
			$('#initLimit').val(result.output.kafka.Zookeeper.Defaults.initLimit);
			$('#tickTime').val(result.output.kafka.Zookeeper.Defaults.tickTime);
			if($("#versionDropdown").val().split('0.8.').length>1){
				$("#replicationFactorDiv").show();
			}else{
				$("#replicationFactorDiv").hide();
			}
		},
		redeployPagePopulate:function(currentClusterId,clusterState){
			  var url = baseUrl + '/monitor/' + currentClusterId + '/details';
			   $.ajax({
                  'type' : 'GET',
                  'url' : url,
                  'contentType' : 'application/json',
                  'dataType' : 'json',
                  'async' : true,
                  'success' : function(result) {
               	    setupDetailData=result.output;
               	    if(setupDetailData.state=='error'){
                		   $('#commonDeleteButton').removeAttr('disabled');
                		   $('#errorDivMain').html('');
                		   $('#validateError').html('');
                		   var count=0;
                		   for(var key in setupDetailData.errors){
                			   count++;
                			   $("#errorDivMain").append("<div class='errorLineDiv'><a href='#inputIpRange'>"+count+". "+setupDetailData.errors[key]+"</a></div>");   
                		   }
                		  if(Object.keys(setupDetailData.errors).length > 0){
                			  	$("#errorDivMain").show();
                		   if(Object.keys(setupDetailData.errors).length > 1){
                               $('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Errors');
                          }
                          else{
                              $('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Error');
                          }
                		  }
                	   } if(!setupDetailData.status){
                		  $("#errorDivMain").html('').append("<div class='errorLineDiv'><a href='#'  >1. "+setupDetailData.error[0]+"</a></div>");
                		  $('#errorDivMain').show();
                		  $('#validateError').html('').text('1 Error').show();
                		  $('#commonDeleteButton').removeAttr('disabled');
               		   return;
               	   }
               	    $('#inputCluster').val(setupDetailData.clusterName).removeAttr('title');
	               	 if(setupDetailData.javaConf.install==false){
	           	    	 $('#inputJavaHome').val(setupDetailData.javaConf.javaHomePath).removeAttr('title');
	           	    	 $('#installJavaCheck').removeAttr('checked');
	           	    	 $('#inputJavaHome').removeAttr("disabled");
	   					 $('#inputBundlePath').attr("disabled","disabled"); 
	           	    }else{
	           	    	 $('#installJavaCheck').attr('checked',true);
	           	    	 $('#inputBundlePath').val(setupDetailData.javaConf.javaBundle).removeAttr('title');
	           	    	 $('#inputBundlePath').removeAttr("disabled");
	   					 $('#inputJavaHome').attr("disabled","disabled")
	           	    }
	               	    
               	    $('#inputUserName').val(setupDetailData.username).removeAttr('title');
               	 if(setupDetailData.password==null ||setupDetailData.password==''){
	          		   $('#authTypePassword').removeClass('active');
	          		   $('#authTypeSharedKey').addClass('active');
	          		   $('#sharedKeyFileUploadDiv').show();
	          		   $('#passwordAuthDiv').hide();
	               	 }else{
	          		   $('#inputPassword').val(setupDetailData.password).removeAttr('title');
	          		   $('#passwordAuthDiv').show();
	          		   $('#sharedKeyFileUploadDiv').hide();
	          		   $('#authTypeSharedKey').removeClass('active');
	          		   $('#authTypePassword').addClass('active');
	               	 } 
               	    if(setupDetailData.ipPattern=='' ||setupDetailData.ipPattern==null){
               	    	if(clusterState !='error'){
               	    		$('#filePath').val(setupDetailData.patternFile);
               	    	}
	                		//   $('#fileUploadRadio').attr('checked','checked');
	                		   $('#ipRangeRadio').removeClass('active');
	                		   $('#fileUploadRadio').addClass('active');
	                		   $('#fileUploadDiv').show();
	                		   $('#ipRangeDiv').hide();
	                	   }else{
	                		   $('#ipRangeDiv').show();
	                		   $('#fileUploadDiv').hide();
	                		   $('#inputIpRange').val(setupDetailData.ipPattern).removeAttr('title');
	                		   $('#fileUploadRadio').removeClass('active');
	                		   $('#ipRangeRadio').addClass('active');
	                		 //  $('#ipRangeRadio').attr('checked','checked');
	                	   }
               	    $('#vendorDropdown').val(setupDetailData.kafka.componentVendor);
               	    $('#versionDropdown').val(setupDetailData.kafka.componentVersion);
               	    $('#logLevelDropdown').val(setupDetailData.kafka.advancedConf.logLevel);
               	    if(setupDetailData.kafka.tarballUrl==''||setupDetailData.kafka.tarballUrl==null){
               	    	   $('#localPath').val(setupDetailData.kafka.localBinaryFile).removeAttr('title');
	                		  // $('#localRadio').attr('checked','checked');
               	    	   $('#downloadRadio').removeClass('active');
               	    	   $('#localRadio').addClass('active');
	                		   $('#localPathDiv').show();
	                		   $('#downloadPathDiv').hide();
	                	   }else{
	                		   $('#downloadPath').val(setupDetailData.kafka.tarballUrl);
	                		   $('#downloadPathDiv').show();
	                		   $('#localPathDiv').hide();
	                		 //  $('#downloadRadio').attr('checked','checked');
	                		   $('#localRadio').removeClass('active');
	                		   $('#downloadRadio').addClass('active');
	                	   }
               	    if(setupDetailData.kafka.componentVersion.split('0.8.').length>1){
               	    	 $('#replicationFactorDiv').show().val(setupDetailData.kafka.replicationFactor).removeAttr('title');
               	    	 $('#replicationFactor').val(setupDetailData.kafka.replicationFactor).removeAttr('title');
       				}else{
       					$('#replicationFactorDiv').hide();
       				}
               	    $('#installationPathKafka').val(setupDetailData.kafka.installationPath).removeAttr('title');
               	    $('#port').val(setupDetailData.kafka.port).removeAttr('title');
               	    $('#jmxPort_kafka').val(setupDetailData.kafka.jmxPort).removeAttr('title');
               	    $('#logDirectory').val(setupDetailData.kafka.logDir).removeAttr('title');
               	    
               	    
               	$('#numOfNetworkThreads').val(setupDetailData.kafka.numOfNetworkThreads).removeAttr('title');
               	$('#numOfIOThreads').val(setupDetailData.kafka.numOfIOThreads).removeAttr('title');
               	$('#queuedMaxRequests').val(setupDetailData.kafka.queuedMaxRequests).removeAttr('title');
               	$('#numPartitions').val(setupDetailData.kafka.numPartitions).removeAttr('title');
               	$('#logRetentionHours').val(setupDetailData.kafka.logRetentionHours).removeAttr('title');
               	$('#logRetentitionBytes').val(setupDetailData.kafka.logRetentitionBytes).removeAttr('title');
               	$('#logCleanupIntervalMins').val(setupDetailData.kafka.logCleanupIntervalMins).removeAttr('title');
               	$('#logFlushIntervalMessage').val(setupDetailData.kafka.logFlushIntervalMessage).removeAttr('title');
               	$('#logFlushSchedularIntervalMs').val(setupDetailData.kafka.logFlushSchedularIntervalMs).removeAttr('title');
               	$('#logFlushIntervalMs').val(setupDetailData.kafka.logFlushIntervalMs).removeAttr('title');
               	$('#controlledShutdownMaxRetries').val(setupDetailData.kafka.controlledShutdownMaxRetries).removeAttr('title');
               	
               	$('#requestLogger').val(setupDetailData.kafka.advancedConf.requestLogger).removeAttr('title');
               	$('#loggerKafkaController').val(setupDetailData.kafka.advancedConf.loggerKafkaController).removeAttr('title');
               	$('#stateChangeLogger').val(setupDetailData.kafka.advancedConf.stateChangeLogger).removeAttr('title');
               	$('#loggerRequestChannel').val(setupDetailData.kafka.advancedConf.loggerRequestChannel).removeAttr('title');
               	if(setupDetailData.kafka.controlledShutdownEnable){
               		$('#controlledShutdownEnable').attr('checked','checked');
               	}else{
               		$('#controlledShutdownEnable').removeAttr('checked');
               	}
            	    nodeStatus={
            	    		nodes:new Array(),
            	    };
                	   for ( var i = 0; i < setupDetailData.nodes.length; i++) {
                		   var nodeObj = new Array();
           				var addId = null;
           					addId = oNodeTable.fnAddData([
           					                              '<input type="checkbox" name="" value=""  id="nodeCheck'
        	                                                 + i
        	                                                 + '" class="nodeCheckBox" />',
        	                                                setupDetailData.nodes[i].publicIp,
        	                                         '<input type="checkbox" name="" value=""  id="zookeeperNodeCheck'
        	                                         + i
        	                                         + '" class="zookeeperNodeCheckBox"/>',
        	                                        setupDetailData.nodes[i].os,
        	                                        
        	                                       '<a class="" id="cpuCount'
        											+ i + '">-</a>',
        											'<a class="" id="diskCount'
        											+ i + '">-</a>',
        											'<a class="" id="diskSize'
        											+ i + '">-</a>',
        											'<a class="" id="ramSize'
        											+ i + '">-</a>',
        											
        	                                         '<a href="##" onclick="com.impetus.ankush.kafkaSetupDetail.loadNodeDetail('
        	                                                 + i
        	                                                 + ');"><img id="navigationImg-'
        	                                                 + i
        	                                                 + '" src="'
        	                                                 + baseUrl
        	                                                 + '/public/images/icon-chevron-right.png" /></a>' ]);
           					nodeObj.push(setupDetailData.nodes[i].publicIp);
           					nodeObj.push('true');
           					nodeObj.push('true');
           					nodeObj.push('true');
           					nodeObj.push(setupDetailData.nodes[i].os);
           					nodeObj.push('');
           					nodeObj.push('');
           					nodeObj.push('');
           					nodeObj.push('');
           					nodeObj.push('');
           					nodeStatus.nodes.push(nodeObj);
           				var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
           				theNode.setAttribute('id', 'node'+ setupDetailData.nodes[i].publicIp.split('.').join('_'));
           				if (Object.keys(setupDetailData.nodes[i].errors).length>0 ){
           					rowId = setupDetailData.nodes[i].publicIp.split('.').join('_');
           					$('td', $('#node'+rowId)).addClass('error-row');
           					$('#node' + rowId).addClass('error-row');
           				}
           			}
                	  for(var j=0;j<setupDetailData.nodes.length;j++){
               		   for(var i=0;i<setupDetailData.zookeeper.nodes.length;i++){
               			   if(setupDetailData.zookeeper.nodes[i].publicIp==setupDetailData.nodes[j].publicIp)
               				   $('#zookeeperNodeCheck'+j).attr('checked',true);
               		   }
               	}
	        	if(clusterState!='error'){
	        		  $('.zookeeperNodeCheckBox').attr('disabled','disabled');
	             	  $('.nodeCheckBox').attr('checked',true).attr('disabled','disabled');
	        	}
                $('.nodeCheckBox').attr('checked',true);
               	$('#nodeCheckHead').attr('checked',true);
               	$("#zookeeperVendorDropdown").val(setupDetailData.zookeeper.componentVendor);
               	$("#zookeeperVersionDropdown").html('');
               	for ( var key in defaultVlaue.output.kafka.Zookeeper.Vendors[$("#zookeeperVendorDropdown").val()]){
    				$("#zookeeperVersionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
    			}
               	   $("#zookeeperVersionDropdown").val(setupDetailData.zookeeper.componentVersion);
               	   if(setupDetailData.zookeeper.localBinaryFile=='' || setupDetailData.zookeeper.localBinaryFile==null){
               		   //$('#zookeeperDownloadRadio').attr('checked','checked');
               		   $('#zookeeperLocalRadio').removeClass('active');
       					$('#zookeeperDownloadRadio').addClass('active');
               		   $('#zookeeperDownloadPathDiv').show();
               		   $('#zookeeperLocalPathDiv').hide();
               	   }else{
               		   $('#zookeeperLocalPathDiv').show();
               		   $('#zookeeperDownloadPathDiv').hide();
               		 //  $('#zookeeperLocalRadio').attr('checked','checked');
               			$('#zookeeperLocalRadio').addClass('active');
       					$('#zookeeperDownloadRadio').removeClass('active');
               	   }$('#zookeeperDownloadPath').val(setupDetailData.zookeeper.tarballUrl).removeAttr('title');
               	   $('#zookeeperLocalPath').val(setupDetailData.zookeeper.localBinaryFile).removeAttr('title');
               	   $('#installationPathZookeeper').val(setupDetailData.zookeeper.installationPath).removeAttr('title');
               	   $('#dataDirZookeeper').val(setupDetailData.zookeeper.dataDirectory).removeAttr('title');
               	   $('#clientPort').val(setupDetailData.zookeeper.clientPort).removeAttr('title');
               	   $('#jmxPort_Zookeeper').val(setupDetailData.zookeeper.jmxPort).removeAttr('title');
               	   if(clusterState=='error'){
               		 $('#syncLimit').val(setupDetailData.zookeeper.syncLimit);
                 	   $('#initLimit').val(setupDetailData.zookeeper.initLimit);
                 	   $('#tickTime').val(setupDetailData.zookeeper.tickTime);
                 	 
               	   }else{
               		 $('#syncLimit').val(setupDetailData.zookeeper.syncLimit + " milliseconds" ).removeAttr('title');
                 	   $('#initLimit').val(setupDetailData.zookeeper.initLimit + " milliseconds").removeAttr('title');
                 	   $('#tickTime').val(setupDetailData.zookeeper.tickTime + " milliseconds").removeAttr('title'); 
                 	  $('.tooltiptext').css('display', 'none');
               	   }
                  }
      		});
		},
		setupDetailValuePopulate:function(currentClusterId,clusterState){
			com.impetus.ankush.common.getDefaultValue('kafka','kafkaSetupDetail.populateDefaultValues');
			if(clusterState=='error'){
				$('#clusterDeploy').text('Redeploy');
				$('#clusterDeploy').val(currentClusterId);
				$('#nodeListDiv').show();
				com.impetus.ankush.kafkaSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
				 $('#filePath').val('');
				com.impetus.ankush.kafkaClusterCreation.tooltipInitialize();
				return;
			}
			com.impetus.ankush.common.pageStyleChange();
			$('#nodeListDiv').show();
			$('#nodeSearchBox').hide();
         	$('#toggleSelectButton').hide();
	//		$('#javaCheckLabel').css('padding-top','6px');
			$('.add-on').css({
				'background-color':'white',
				'border':'none',
				'':'',
			});
			$('#inputBundlePath').css({
				'width':'80%',
			});
			$('#inputBundlePath').css('width','85%');
			$('.btnGrp').attr('disabled','disabled');
			$('#nodeIpTable_filter').hide();
			$('#clusterDeploy').hide();
			$('#nodeRetrieveBtn').attr('disabled','disabled');
			$('#inspectNodeBtn').attr('disabled','disabled');
			$('#commonDeleteButton').hide();
			if (oNodeTable != null) {
				oNodeTable.fnClearTable();
			}
			com.impetus.ankush.kafkaSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
		},
		
		/*Function for loading node detail page*/
	    loadNodeDetail: function(i) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/kafka-cluster/retrievedNodeDetail',
				method : 'get',
				params : {},
				title : 'Node Detail',
				callback : function(data) {
					com.impetus.ankush.kafkaSetupDetail.nodeDetail(data.i);
				},
				callbackData : {
					i : i
				}
			});
		},	
		/*Function for populating node detail page*/
		nodeDetail:function(node){
			if(setupDetailData==null || setupDetailData.nodes.length==0){
				$('#kafkaNodeHead').empty().html(nodeStatus.nodes[node][0]);
				if(nodeStatus.nodes[node][1]==false){
					$('#isAuthenticated').html('').text('No');
					$('#kafkaNodeStatus').html('').text('Unavailable');
				}else if(nodeStatus.nodes[node][2]==false){
					$('#isAuthenticated').html('').text('No');
					$('#kafkaNodeStatus').html('').text('Unreachable');
				}else if(nodeStatus.nodes[node][3]==false){
					$('#isAuthenticated').html('').text('No');
					$('#kafkaNodeStatus').html('').text('Unauthenticated');
				}else{
					$('#isAuthenticated').html('').text('Yes');
					$('#kafkaNodeStatus').html('').text('Available');
				}
				if($('#zookeeperNodeCheck'+node).is(':checked')){
					$('#zookeeper').text('Yes');
				}else{
					$('#zookeeper').text('No');
				}
				$('#kafkaOs').html('').text(nodeStatus.nodes[node][4]);
				$('#kafkaCpu').html('').text(nodeStatus.nodes[node][7]);
				$('#kafkaDiskCount').html('').text(nodeStatus.nodes[node][8]);
				if(nodeStatus.nodes[node][9]==''){
					$('#kafkaDiskSize').html('');
				}else{
					$('#kafkaDiskSize').html('').text(nodeStatus.nodes[node][9]+' GB');
				}
				if(nodeStatus.nodes[node][10]==''){
					$('#kafkaRam').html('');
				}else{
					$('#kafkaRam').html('').text(nodeStatus.nodes[node][10]+' GB');
				}
				}else{
					$('#isAuth').hide();
					$('#kafkaNodeHead').empty().html(setupDetailData.nodes[node].publicIp);
					$('#kafkaNodeStatus').html('').text(setupDetailData.nodes[node].message);
					$('#kafkaOs').html('').text(setupDetailData.nodes[node].os);
					$('#kafkaCpu').html('').text('-');
					$('#kafkaDiskCount').html('').text('-');
					$('#kafkaDiskSize').html('').text('-');
					$('#kafkaRam').html('').text('-');
					$('#isAuthenticated').text('Yes');
					if($('#zookeeperNodeCheck'+node).is(':checked')){
						$('#zookeeper').text('Yes');
					}else{
						$('#zookeeper').text('No');
					}
				}
		},
		
};
