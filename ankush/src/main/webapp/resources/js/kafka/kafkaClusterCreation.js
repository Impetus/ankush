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

var disableNodeCount = 0;
var uploadFilePath=null;
var uploadPathSharedKey=null;
var userName='/home/${user}';
var nodeStatus=null;
var getNodeFlag=false;
var setupDetailData=null;
var  inspectNodeData = {};
com.impetus.ankush.kafkaClusterCreation={
/*Function for tooltip initialization*/
		tooltipInitialize:function(){
			$('#inputCluster').tooltip();
			$('#inputJavaHome').tooltip();
			$('#inputBundlePath').tooltip();
			$('#inputUserName').tooltip();
			$('#inputPassword').tooltip();
			$('#inputIpRange').tooltip();
			$('#filePathShareKey').tooltip();
			$('#filePath').tooltip();
			$('#vendorDropdown').tooltip();
			$('#versionDropdown').tooltip();
			$('#downloadPath').tooltip();
			$('#localPath').tooltip();
			$('#installationPathKafka').tooltip();
			$('#replicationFactor').tooltip();
			$('#jmxPort_kafka').tooltip();
			$('#port').tooltip();
			$('#partitions').tooltip();
			$('#logDirectory').tooltip();
			$('#numOfNetworkThreads').tooltip();
			$('#numOfIOThreads').tooltip();
			$('#queuedMaxRequests').tooltip();
			$('#numPartitions').tooltip();
			$('#logRetentionHours').tooltip();
			$('#logRetentitionBytes').tooltip();
			$('#logCleanupIntervalMins').tooltip();
			$('#logCleanupIntervalMins').tooltip();
			$('#logFlushIntervalMessage').tooltip();
			$('#logFlushSchedularIntervalMs').tooltip();
			$('#logFlushIntervalMs').tooltip();
			$('#controlledShutdownMaxRetries').tooltip();
			$('#logLevelDropdown').tooltip();
			/*$('#requestLogger').tooltip();
			$('#loggerKafkaController').tooltip();
			$('#stateChangeLogger').tooltip();
			$('#loggerRequestChannel').tooltip();*/
			$('#zookeeperVendorDropdown').tooltip();
			$('#zookeeperVersionDropdown').tooltip();
			$('#zookeeperDownloadPath').tooltip();
			$('#zookeeperLocalPath').tooltip();
			$('#installationPathZookeeper').tooltip();
			$('#dataDirZookeeper').tooltip();
			$('#clientPort').tooltip();
			$('#syncLimit').tooltip();
			$('#initLimit').tooltip();
			$('#tickTime').tooltip();
			$('#jmxPort_Zookeeper').tooltip();
		},
/*Function for changing version and download path on change of kafka vendor dropdown*/
		vendorOnChangeKafka:function(){
			$("#versionDropdown").html('');
			for ( var key in defaultVlaue.output.kafka.Kafka.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath').val(defaultVlaue.output.kafka.Kafka.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].url);
		},
/*Function for changing  download path on change of version kafka dropdown*/
		versionOnChangeKafka:function(){
			$('#downloadPath').val(defaultVlaue.output.kafka.Kafka.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].url);
			if($("#versionDropdown").val().split('0.8.').length>1){
				$("#replicationFactorDiv").show();
			}else{
				$("#replicationFactorDiv").hide();
			}
		},
/*Function for changing version and download path on change of zookeeper vendor dropdown*/
		vendorOnChangeZookeeper:function(){
			$("#zookeeperVersionDropdown").html('');
			for ( var key in defaultVlaue.output.kafka.Zookeeper.Vendors[$("#zookeeperVendorDropdown").val()]){
				$("#zookeeperVersionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#zookeeperDownloadPath').val(defaultVlaue.output.kafka.Zookeeper.Vendors[$("#zookeeperVendorDropdown").val()][$("#zookeeperVersionDropdown").val()].url);
		},
/*Function for changing  download path on change of version zookeeper dropdown*/
		versionOnChangeZookeeper:function(){
			$('#zookeeperDownloadPath').val(defaultVlaue.output.kafka.Zookeeper.Vendors[$("#zookeeperVendorDropdown").val()][$("#zookeeperVersionDropdown").val()].url);
		},
/*Function for changing path according to the user*/
		pathChange: function(){
			var user=$.trim($('#inputUserName').val());
			var installationPathKafka=$.trim($('#installationPathKafka').val()).split(userName).join('/home/'+user);
			var installationPathZookeeper=$.trim($('#installationPathZookeeper').val()).split(userName).join('/home/'+user);
			var dataDirZookeeper=$.trim($('#dataDirZookeeper').val()).split(userName).join('/home/'+user);
			var logDir=$.trim($('#logDirectory').val()).split(userName).join('/home/'+user);
			userName='/home/'+user;
			$('#installationPathKafka').empty().val(installationPathKafka);
			$('#installationPathZookeeper').empty().val(installationPathZookeeper);
			$('#dataDirZookeeper').empty().val(dataDirZookeeper);
			$('#logDirectory').empty().val(logDir);
		},
/*Function for browse & upload file*/
	    getNodesUpload : function() {
	        uploadFileFlag=true;
	        var uploadUrl = baseUrl + '/uploadFile';
	        $('#fileBrowse').click();
	        $('#fileBrowse').change(function(){
		        $('#filePath').val($('#fileBrowse').val());
		        com.impetus.ankush.uploadFileNew(uploadUrl,"fileBrowse",null,function(data){
		        var htmlObject = $(data);
		        var jsonData = JSON.parse(htmlObject.text());
		        uploadFilePath=jsonData.output;
		        });
		    });
	    },
/*Function to Upload file*/
		shareKeyFileUpload : function() {
			uploadPathSharedKey=null;
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowseShareKey').click();
			$('#fileBrowseShareKey').change(
			function(){
				$('#filePathShareKey').val($('#fileBrowseShareKey').val());
				com.impetus.ankush.uploadFileNew(uploadUrl,"fileBrowseShareKey",null,function(data){
					var htmlObject = $(data);
			        var jsonData = JSON.parse(htmlObject.text());
					uploadPathSharedKey= jsonData.output;
				});
			});
		},
		
/*Function for client-side validation on retrieve node button*/
		validateNode:function(){
			var userName = $.trim($('#inputUserName').val());
			var password = $('#inputPassword').val();	
			var ipPattern= $.trim($('#inputIpRange').val());
			var radioVal=0;
			var radioAuth=0;
			  if($("#ipModeGroupBtn .active").data("value")==1){
				radioVal=1;
			}
			if($("#authGroupBtn .active").data("value")==1){
				radioAuth=1;
			}
			 errorCount = 0;
		        $("#errorDivMain").html('').hide();
		        $('#validateError').html('').hide();
		        var flag = false;
		        if(!com.impetus.ankush.validation.empty($('#inputUserName').val())){
		            errorCount++;
		            errorMsg ='User Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		            com.impetus.ankush.common.tooltipMsgChange('inputUserName','Username cannot be empty');
		            flag = true;
		            var divId='inputUserName';
		            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");          
		        }else{
		            com.impetus.ankush.common.tooltipOriginal('inputUserName',' Enter Username.');
		            $('#inputUserName').removeClass('error-box');
		        }
		        if($("#authGroupBtn .active").data("value")==0){
			        if (!com.impetus.ankush.validation.empty($('#inputPassword').val())) {
			            errorCount++;
			            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			            com.impetus.ankush.common.tooltipMsgChange('inputPassword','Password cannot be empty');
			            flag = true;
			            var divId='inputPassword';
			            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
			      
			        }else{
			            com.impetus.ankush.common.tooltipOriginal('inputPassword','Enter password.');
			            $('#inputPassword').removeClass('error-box');
			        }
		        }else{
		        	  if (uploadPathSharedKey==null){
		        		  errorCount++;
		                  errorMsg = 'Shared Key '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty']; 
		                  var divId='filePathShareKey';
		                  $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		        	  }else{
		                  com.impetus.ankush.common.tooltipOriginal('filePathShareKey','Upload File.');
		                  $('#filePathShareKey').removeClass('error-box');
		        	  }
		        }
		        if($("#ipModeGroupBtn .active").data("value")==0){
		            if(!com.impetus.ankush.validation.empty($('#inputIpRange').val())){
		                errorCount++;
		                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('inputIpRange','IP range cannot be empty');
		                flag = true;
		                var divId='inputIpRange';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('inputIpRange','Enter IP range.');
		                $('#inputIpRange').removeClass('error-box');
		            }
		        }
		        if($("#ipModeGroupBtn .active").data("value")==1){
		            if (!com.impetus.ankush.validation.empty($('#filePath').val())) {
		                errorCount++;
		                errorMsg = 'File path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('filePath','File path cannot be empty');
		                flag = true;
		                var divId='filePath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            } else {
		                com.impetus.ankush.common.tooltipOriginal('filePath','Browse File.');
		                $('#filePath').removeClass('error-box');
		            }
		        }
		        if(errorCount>0 && errorMsg!=''){
		        	 $('#validateError').show().html(errorCount + ' Error');
		        	  $("#errorDivMain").show();
		        }else{
		        	$('#nodeRetrieveBtn').button();
		            $('#nodeRetrieveBtn').button('loading');
		            $("#nodeListDiv").show();
		            nodeStatus=null;
		            if(null!= setupDetailData){
		 	            setupDetailData.nodes=new Array();	
		            }
		        	var functionCall=function(result) {
						com.impetus.ankush.kafkaClusterCreation.getNodes(result);
					};
					com.impetus.ankush.nodeRetrieve(userName,password,uploadFilePath,ipPattern,radioVal,uploadPathSharedKey,radioAuth,functionCall,'nodeRetrieveBtn','','','inspectNodeBtn');
		        }
			
		}, 	
		/*Function for retriving nodes and populate on node table*/
	    getNodes : function(result) {
	    	getNodeFlag=false;
	    	 inspectNodeData = {};
	    	$('#errorDivMain').html('');
	         var errorNodeCount=0;
	         if (oNodeTable != null) {
	             oNodeTable.fnClearTable();
	         }else{
	        	
	         }
	                 getNodeFlag=true;
	                 uploadFileFlag=false;
	                 nodeStatus = result.output;
	                 nodeTableLength = nodeStatus.nodes.length;
	                 if($("#ipModeGroupBtn .active").data("value")==1){
	                     divId='filePath';
	                     tooltipMsg='IP pattern is not valid in file';                          
	                     herfFunction="javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")";
	                   
	                 }else{
	                     divId='ipRange';
	                     tooltipMsg='IP pattern is not valid.';
	                     herfFunction="javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")";
	                 }
	                 if(nodeTableLength == 0){
	                     errorMsg = nodeStatus.error;
	                     flag = true;
	                     $("#errorDivMain").append("<div class='errorLineDiv'><a href="+herfFunction+" >1. "+errorMsg+"</a></div>");                      
	                     com.impetus.ankush.common.tooltipMsgChange(divId,tooltipMsg);
	                     $("#errorDivMain").show();
	                     $('#validateError').show().html('1 Error');
	                 }else{
	                	  if($("#ipModeGroupBtn .active").data("value")==1){
	                     com.impetus.ankush.common.tooltipOriginal('filePath','Browse File.');
	                     $('#filePath').removeClass('error-box');
	                     }else{
	                     com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP pattern.');
	                     $('#ipRange').removeClass('error-box');
	                     }
	                     $("#errorDivMain").hide();
	                     $('#validateError').hide();  
	                     }
	                 for (var i = 0; i < nodeStatus.nodes.length; i++) {
	                	  var addId=null;
	                         addId = oNodeTable.fnAddData([
	                                         '<input type="checkbox" name="" value=""  id="nodeCheck'
	                                                 + i
	                                                 + '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.kafkaClusterCreation.nodeCheckBox('+i+')"  />',
	                                         nodeStatus.nodes[i][0],
	                                         '<input type="checkbox" name="" value=""  id="zookeeperNodeCheck'
	                                         + i
	                                         + '" class="zookeeperNodeCheckBox"/>',
                                             nodeStatus.nodes[i][4],
                                             
                                             '<a class="" id="cpuCount'
 											+ i + '">'+nodeStatus.nodes[i][7]+'</a>',
 											'<a class="" id="diskCount'
 											+ i + '">'+nodeStatus.nodes[i][8]+'</a>',
 											'<a class="" id="diskSize'
 											+ i + '">'+nodeStatus.nodes[i][9]+' GB</a>',
 											'<a class="" id="ramSize'
 											+ i + '">'+nodeStatus.nodes[i][10]+' GB</a>',
	                                         '<a href="##" onclick="com.impetus.ankush.kafkaSetupDetail.loadNodeDetail('
	                                                 + i
	                                                 + ');"><img id="navigationImg-'
	                                                 + i
	                                                 + '" src="'
	                                                 + baseUrl
	                                                 + '/public/images/icon-chevron-right.png" /></a>' ]);
	                                              
	                     var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
	                     theNode.setAttribute('id', 'node'+ nodeStatus.nodes[i][0].split('.').join('_'));
	                     if (nodeStatus.nodes[i][1] != true
	                             || nodeStatus.nodes[i][2] != true
	                             || nodeStatus.nodes[i][3] != true) {
	                         rowId = nodeStatus.nodes[i][0].split('.').join('_');
	                         $('td', $('#node'+rowId)).addClass('error-row');
	                         $('#node' + rowId).addClass('error-row');
	                     	$('#diskSize' + i).text('');
	    					$('#ramSize' + i).text('');
	                         $('#nodeCheck' + i).attr('disabled', true);
	                         $('#zookeeperNodeCheck' + i).attr('disabled', true);
	                         errorNodeCount++;
	                         disableNodeCount++;
	                     }else{
	                         $('#nodeCheck'+i).prop("checked", true);
	                     }
	                     if(nodeStatus.nodes[i][1] != true){
	                         var status='Unavailable';
	                     $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
	                     }else if(nodeStatus.nodes[i][2] != true){
	                         var status='Unreachable';
	                         $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
	                     }else if(nodeStatus.nodes[i][3] != true){
	                         var status='Unauthenticated';
	                         $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");  
	                     }
	                     }
	                     if(disableNodeCount != nodeTableLength){
	                         $('#nodeCheckHead').prop("checked", true);
	                     }
	                     $('.editableLabel').editable({
	                         type : 'text',
	                     });
	    },
/*Function for check/uncheck header check boxe according to node check box of a table & change properties accordingly*/ 
	    nodeCheckBox : function(i) {
	        if ($("#nodeIpTable .nodeCheckBox:checked").length == $("#nodeIpTable .nodeCheckBox").length
	                - disableNodeCount) {
	            $("#nodeCheckHead").prop("checked", true);
	        } else {
	            $("#nodeCheckHead").removeAttr("checked");
	        }
	    },

/*Function for client side validations for cluster deployment*/
		validateCluster:function(){
			  $('#validateError').html('').hide();
		        var errorMsg = '';
		        $("#errorDivMain").html('').hide();
		        errorCount = 0;
		        var flag = false;
		        if (!com.impetus.ankush.validation.empty($('#inputCluster').val())) {
		            errorCount++;
		            errorMsg = 'Cluster Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		            com.impetus.ankush.common.tooltipMsgChange('inputCluster',errorMsg);
		            flag = true;
		            var divId='inputCluster';
		            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		        } else if ($('#inputCluster').val().trim().split(' ').length > 1) {
		            errorCount++;
		            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameBlankSpace'];
		            com.impetus.ankush.common.tooltipMsgChange('inputCluster',errorMsg);
		            flag = true;
		            var divId='inputCluster';
		            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		        }else if ($('#inputCluster').val().length > 20) {
		            errorCount++;
		            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameLength'];
		            com.impetus.ankush.common.tooltipMsgChange('inputCluster',errorMsg);
		            flag = true;
		            var divId='inputCluster';
		            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		        }
		        else if (!com.impetus.ankush.validation.alphaNumericChar($('#inputCluster').val())) {
		            errorCount++;
		            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameSpecialChar'];
		            com.impetus.ankush.common.tooltipMsgChange('inputCluster',errorMsg);
		            flag = true;
		            var divId='inputCluster';
		            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		        }
		        else {
		            com.impetus.ankush.common.tooltipOriginal('inputCluster','Enter Cluster Name.');
		            $('#inputCluster').removeClass('error-box');
		        }
		        if($('#installJavaCheck').is(':checked')){
		        	  if (!com.impetus.ankush.validation.empty($('#inputBundlePath').val())) {
		  	            errorCount++;
		  	            errorMsg = 'Java Bundle Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		  	            com.impetus.ankush.common.tooltipMsgChange('inputBundlePath',errorMsg);
		  	            flag = true;
		  	            var divId='inputBundlePath';
		  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		  	        }else {
			            com.impetus.ankush.common.tooltipOriginal('inputBundlePath','Enter Bundle Path.');
			            $('#inputBundlePath').removeClass('error-box');
			        }
		        }else{
		        	if (!com.impetus.ankush.validation.empty($('#inputJavaHome').val())) {
		  	            errorCount++;
		  	            errorMsg = 'Java home Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		  	            com.impetus.ankush.common.tooltipMsgChange('inputJavaHome',errorMsg);
		  	            flag = true;
		  	            var divId='inputJavaHome';
		  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		  	        }else {
			            com.impetus.ankush.common.tooltipOriginal('inputJavaHome','Enter Java Home Path.');
			            $('#inputJavaHome').removeClass('error-box');
			        }
		        }
		        if (!com.impetus.ankush.validation.empty($('#inputUserName').val())) {
	  	            errorCount++;
	  	            errorMsg = 'User Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            com.impetus.ankush.common.tooltipMsgChange('inputUserName',errorMsg);
	  	            flag = true;
	  	            var divId='inputUserName';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('inputUserName','Enter user name.');
		            $('#inputUserName').removeClass('error-box');
		        }
		        if($("#authGroupBtn .active").data("value")==0){
			        if (!com.impetus.ankush.validation.empty($('#inputPassword').val())) {
			            errorCount++;
			            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			            com.impetus.ankush.common.tooltipMsgChange('inputPassword',errorMsg);
			            flag = true;
			            var divId='inputPassword';
			            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
			      
			        }else{
			            com.impetus.ankush.common.tooltipOriginal('inputPassword','Enter password.');
			            $('#inputPassword').removeClass('error-box');
			        }
		        }else{
		        	  if (uploadPathSharedKey==null){
		        		  errorCount++;
		                  errorMsg = 'Share Key '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload']; 
		                  var divId='filePathShareKey';
		                  $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		        	  }else{
		                  com.impetus.ankush.common.tooltipOriginal('filePathShareKey','Upload File.');
		                  $('#filePathShareKey').removeClass('error-box');
		        	  }
		        }
		        if($("#ipModeGroupBtn .active").data("value")==0){
		            if(!com.impetus.ankush.validation.empty($('#inputIpRange').val())){
		                errorCount++;
		                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('inputIpRange',errorMsg);
		                flag = true;
		                var divId='inputIpRange';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('inputIpRange','Enter IP range.');
		                $('#inputIpRange').removeClass('error-box');
		            }
		        }else{
		        	if (uploadFilePath == null) {
		                errorCount++;
		                errorMsg = com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
		                com.impetus.ankush.common.tooltipMsgChange('filePath',errorMsg);
		                flag = true;
		                var divId='filePath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            } else {
		                com.impetus.ankush.common.tooltipOriginal('filePath','Upload File');
		                $('#filePath').removeClass('error-box');
		            }
		        }
		        if (!com.impetus.ankush.validation.empty($('#vendorDropdown').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Kafka vendor'+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            flag = true;
	  	            var divId='vendorDropdown';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
		            $('#vendorDropdown').removeClass('error-box');
		        }
		        if (!com.impetus.ankush.validation.empty($('#versionDropdown').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Kafka version'+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            flag = true;
	  	            var divId='versionDropdown';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
		            $('#versionDropdown').removeClass('error-box');
		        }
		        if($("#sourcePathBtnGrp .active").data("value")==0){
		            if(!com.impetus.ankush.validation.empty($('#downloadPath').val())){
		                errorCount++;
		                errorMsg = 'Download Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('downloadPath',errorMsg);
		                flag = true;
		                var divId='downloadPath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('downloadPath','Enter download Path');
		                $('#downloadPath').removeClass('error-box');
		            }
		        }else{
		        	if(!com.impetus.ankush.validation.empty($('#localPath').val())){
		                errorCount++;
		                errorMsg = 'Local path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('localPath',errorMsg);
		                flag = true;
		                var divId='localPath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            } else {
		                com.impetus.ankush.common.tooltipOriginal('localPath','Local Path');
		                $('#localPath').removeClass('error-box');
		            }
		        }
		        if (!com.impetus.ankush.validation.empty($('#installationPathKafka').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Installation Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            com.impetus.ankush.common.tooltipMsgChange('installationPathKafka',errorMsg);
	  	            flag = true;
	  	            var divId='installationPathKafka';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('installationPathKafka','Enter Installation Path.');
		            $('#installationPathKafka').removeClass('error-box');
		        }
		        if($('#versionDropdown').val().split('0.8.').length>1){
			        if (!com.impetus.ankush.validation.empty($('#replicationFactor').val())) {
			            errorCount++;
			            errorMsg = 'Replication Factor '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			            com.impetus.ankush.common.tooltipMsgChange('replicationFactor',errorMsg);
			            flag = true;
			            var divId='replicationFactor';
			            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
			        } else if (!com.impetus.ankush.validation.numeric($('#replicationFactor').val())) {
			            errorCount++;
			            errorMsg = 'Replication Factor '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
			            com.impetus.ankush.common.tooltipMsgChange('replicationFactor',errorMsg);
			            flag = true;
			            var divId='replicationFactor';
			            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
			        }
			         else if ($('#replicationFactor').val()<1) {
			                errorCount++;
			                errorMsg = 'Replication Factor '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
			                com.impetus.ankush.common.tooltipMsgChange('replicationFactor',errorMsg);
			                flag = true;
			                var divId='replicationFactor';
			                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		            }else if ($('#nodeIpTable .nodeCheckBox:checked').length < $('#replicationFactor').val()) {
		                errorMsg = com.impetus.ankush.errorMsg.errorHash['ReplicationFactorVal'];
		                if(getNodeFlag){
		                    errorCount++;
		                    flag = true;
		                    $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+errorCount+". "+errorMsg+"</a></div>");
		                }else{
		                	   com.impetus.ankush.common.tooltipOriginal('replicationFactor','Enter Replication Factor');
		                }
		            } else {
			            com.impetus.ankush.common.tooltipOriginal('replicationFactor','Enter Replication Factor');
			        }
		        }
		        if(!com.impetus.ankush.validation.empty($('#port').val())){
	                errorCount++;
	                errorMsg = 'Port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('port',errorMsg);
	                flag = true;
	                var divId='port';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#port').val())) {
	                errorCount++;
	                errorMsg = 'Port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
	                com.impetus.ankush.common.tooltipMsgChange('port',errorMsg);
	                flag = true;
	                var divId='port';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else if (!com.impetus.ankush.validation.oPort($('#port').val())) {
	                errorCount++;
	                errorMsg = 'Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
	                com.impetus.ankush.common.tooltipMsgChange('port',errorMsg);
	                flag = true;
	                var divId='port';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	                com.impetus.ankush.common.tooltipOriginal('port','Enter port.');
	                $('#port').removeClass('error-box');
	            }
		        
		        if(!com.impetus.ankush.validation.empty($('#jmxPort_kafka').val())){
	                errorCount++;
	                errorMsg = 'Kafka Jmx Port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('jmxPort_kafka',errorMsg);
	                flag = true;
	                var divId='jmxPort_kafka';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort_kafka').val())) {
	                errorCount++;
	                errorMsg = 'Kafka Jmx Port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
	                com.impetus.ankush.common.tooltipMsgChange('jmxPort_kafka',errorMsg);
	                flag = true;
	                var divId='jmxPort_kafka';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	                com.impetus.ankush.common.tooltipOriginal('jmxPort_kafka','Enter Kafka Jmx port.');
	                $('#jmxPort_kafka').removeClass('error-box');
	            }
		        if(!com.impetus.ankush.validation.empty($('#logDirectory').val())){
	                errorCount++;
	                errorMsg = 'Log Directory Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('logDirectory',errorMsg);
	                flag = true;
	                var divId='logDirectory';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('logDirectory','Enter log directory path.');
	                $('#logDirectory').removeClass('error-box');
	            }
		        if(!com.impetus.ankush.validation.empty($('#numPartitions').val())){
	                errorCount++;
	                errorMsg = 'Partitions field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('numPartitions','Partitions cannot be empty');
	                var divId='numPartitions';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#numPartitions').val())) {
	                errorCount++;
	                errorMsg = 'Partitions field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('numPartitions',' Partitions must be numeric');
	                var divId='numPartitions';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('numPartitions','Enter Partitions.');
	                
	            } if(!com.impetus.ankush.validation.empty($('#numOfNetworkThreads').val())){
	                errorCount++;
	                errorMsg = 'Network Threads field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('numOfNetworkThreads','Network Threads cannot be empty');
	                var divId='numOfNetworkThreads';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#numOfNetworkThreads').val())) {
	                errorCount++;
	                errorMsg = 'Network Threads field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('numOfNetworkThreads',' Network Threads must be numeric');
	                var divId='numOfNetworkThreads';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('numOfNetworkThreads','Enter lNetwork Threads.');
	                
	            } 
	            if(!com.impetus.ankush.validation.empty($('#numOfIOThreads').val())){
	                errorCount++;
	                errorMsg = 'IO Threads field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('numOfIOThreads','IO Threads cannot be empty');
	                var divId='numOfIOThreads';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#numOfIOThreads').val())) {
	                errorCount++;
	                errorMsg = 'IO Threads field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('numOfIOThreads',' IO Threads must be numeric');
	                var divId='numOfIOThreads';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('numOfIOThreads','Enter IO Threads.');
	                
	            } if(!com.impetus.ankush.validation.empty($('#queuedMaxRequests').val())){
	                errorCount++;
	                errorMsg = 'Queued Max. Requests field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('queuedMaxRequests','Queued Max. Requests cannot be empty');
	                var divId='queuedMaxRequests';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#queuedMaxRequests').val())) {
	                errorCount++;
	                errorMsg = 'Queued Max. Requests field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('queuedMaxRequests',' Queued Max. Requests must be numeric');
	                var divId='queuedMaxRequests';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('queuedMaxRequests','Enter Queued Max. Requests.');
	                
	            } if(!com.impetus.ankush.validation.empty($('#logRetentionHours').val())){
	                errorCount++;
	                errorMsg = 'Log Retention Hours field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('logRetentionHours','Log Retention Hours cannot be empty');
	                var divId='logRetentionHours';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#logRetentionHours').val())) {
	                errorCount++;
	                errorMsg = 'Log Retention Hours field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('logRetentionHours',' Log Retention Hours must be numeric');
	                var divId='logRetentionHours';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('logRetentionHours','Enter Log Retention Hours .');
	                
	            } if(!com.impetus.ankush.validation.empty($('#logRetentitionBytes').val())){
	                errorCount++;
	                errorMsg = 'Log Retention Bytes field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('logRetentitionBytes','Log Retention Bytes cannot be empty');
	                var divId='logRetentitionBytes';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#logRetentitionBytes').val())) {
	                errorCount++;
	                errorMsg = 'Log Retention Bytes field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('logRetentitionBytes',' Log Retention Bytes must be numeric');
	                var divId='logRetentitionBytes';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('logRetentitionBytes','Enter Log Retention Bytes.');
	                
	            } if(!com.impetus.ankush.validation.empty($('#logCleanupIntervalMins').val())){
	                errorCount++;
	                errorMsg = 'Log Cleanup Interval Mins field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('logCleanupIntervalMins','Log Cleanup Interval Mins. cannot be empty');
	                var divId='logCleanupIntervalMins';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#logCleanupIntervalMins').val())) {
	                errorCount++;
	                errorMsg = 'Log Cleanup Interval Mins field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('logCleanupIntervalMins',' Log Cleanup Interval Mins must be numeric');
	                var divId='logCleanupIntervalMins';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('logCleanupIntervalMins','Enter Log Cleanup Interval Mins.');
	                
	            } if(!com.impetus.ankush.validation.empty($('#logFlushIntervalMessage').val())){
	                errorCount++;
	                errorMsg = 'Log Flush Interval Message field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('logFlushIntervalMessage','Log Flush Interval Message cannot be empty');
	                var divId='logFlushIntervalMessage';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#logFlushIntervalMessage').val())) {
	                errorCount++;
	                errorMsg = 'Log Flush Interval Message field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('logFlushIntervalMessage',' Log Flush Interval Message must be numeric');
	                var divId='logFlushIntervalMessage';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('logFlushIntervalMessage','Enter Log Flush Interval Message.');
	                
	            } if(!com.impetus.ankush.validation.empty($('#logFlushSchedularIntervalMs').val())){
	                errorCount++;
	                errorMsg = 'Log Flush Schedular Interval field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('logFlushSchedularIntervalMs','Log Flush Schedular Interval cannot be empty');
	                var divId='logFlushSchedularIntervalMs';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#logFlushSchedularIntervalMs').val())) {
	                errorCount++;
	                errorMsg = 'Log Flush Schedular Interval field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('logFlushSchedularIntervalMs',' Log Flush Schedular Interval must be numeric');
	                var divId='logFlushSchedularIntervalMs';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('logFlushSchedularIntervalMs','Enter Log Flush Schedular Interval.');
	            } 
	            
	            if(!com.impetus.ankush.validation.empty($('#logFlushIntervalMs').val())){
	                errorCount++;
	                errorMsg = 'Log Flush Interval field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('logFlushIntervalMs','Log Flush Interval cannot be empty');
	                var divId='logFlushIntervalMs';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#logFlushIntervalMs').val())) {
	                errorCount++;
	                errorMsg = 'Log Flush Interval field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('logFlushIntervalMs',' Log Flush Interval must be numeric');
	                var divId='logFlushIntervalMs';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('logFlushIntervalMs','Enter Log Flush Interval.');
	            } 
	            
	            if(!com.impetus.ankush.validation.empty($('#controlledShutdownMaxRetries').val())){
	                errorCount++;
	                errorMsg = 'Controlled Shutdown Max Retries field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('controlledShutdownMaxRetries','Controlled Shutdown Max Retries cannot be empty');
	                var divId='controlledShutdownMaxRetries';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#controlledShutdownMaxRetries').val())) {
	                errorCount++;
	                errorMsg = 'Controlled Shutdown Max Retries field must be numeric';
	                com.impetus.ankush.common.tooltipMsgChange('controlledShutdownMaxRetries',' Controlled Shutdown Max Retries must be numeric');
	                var divId='controlledShutdownMaxRetries';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	            	com.impetus.ankush.common.tooltipOriginal('controlledShutdownMaxRetries','Enter Controlled Shutdown Max Retries .');
	            } 
	            
	           /* if(!com.impetus.ankush.validation.empty($('#requestLogger').val())){
	                errorCount++;
	                errorMsg = 'Request Logger field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('requestLogger','Request Logger cannot be empty');
	                var divId='requestLogger';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	com.impetus.ankush.common.tooltipOriginal('requestLogger','Enter Request Logger.');
	            }
	            
	            if(!com.impetus.ankush.validation.empty($('#loggerKafkaController').val())){
	                errorCount++;
	                errorMsg = 'Logger Kafka Controller field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('loggerKafkaController','Logger Kafka Controller cannot be empty');
	                var divId='loggerKafkaController';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	com.impetus.ankush.common.tooltipOriginal('loggerKafkaController','Enter Logger Kafka Controller.');
	            } 
	            
	            if(!com.impetus.ankush.validation.empty($('#stateChangeLogger').val())){
	                errorCount++;
	                errorMsg = 'State Change Logger field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('stateChangeLogger','State Change Logger cannot be empty');
	                var divId='stateChangeLogger';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	com.impetus.ankush.common.tooltipOriginal('stateChangeLogger','Enter State Change Logger.');
	            } 
	            
	            if(!com.impetus.ankush.validation.empty($('#loggerRequestChannel').val())){
	                errorCount++;
	                errorMsg = 'Logger Request Channel field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('loggerRequestChannel','Logger Request Channel cannot be empty');
	                var divId='loggerRequestChannel';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	com.impetus.ankush.common.tooltipOriginal('loggerRequestChannel','Enter Logger Request Channel .');
	            }
		        */
	            if(!com.impetus.ankush.validation.empty($('#logLevelDropdown').val())){
	                errorCount++;
	                errorMsg = 'Log Level field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('logLevelDropdown',errorMsg);
	                var divId='logLevelDropdown';
	                 $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	com.impetus.ankush.common.tooltipOriginal('logLevelDropdown','Enter Log Level.');
	            } 
		        
		        if (!com.impetus.ankush.validation.empty($('#zookeeperVendorDropdown').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Zookeeper vendor'+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            flag = true;
	  	            var divId='zookeeperVendorDropdown';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
		            $('#zookeeperVendorDropdown').removeClass('error-box');
		        }
		        if (!com.impetus.ankush.validation.empty($('#zookeeperVersionDropdown').val())) {
	  	            errorCount++;
	  	            errorMsg ='Zookeeper version'+ com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            flag = true;
	  	            var divId='zookeeperVersiondropDown';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
		            $('#zookeeperVersionDropdown').removeClass('error-box');
		        }
		        if($("#zookeeperPathBtnGrp .active").data("value")==0){
		            if(!com.impetus.ankush.validation.empty($('#zookeeperDownloadPath').val())){
		                errorCount++;
		                errorMsg = 'Zookeeper Download Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('zookeeperDownloadPath',errorMsg);
		                flag = true;
		                var divId='zookeeperDownloadPath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('zookeeperDownloadPath','Enter download Path');
		                $('#zookeeperDownloadPath').removeClass('error-box');
		            }
		        }else{
		        	if(!com.impetus.ankush.validation.empty($('#zookeeperLocalPath').val())){
		                errorCount++;
		                errorMsg = 'Zookeeper Local path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('zookeeperLocalPath',errorMsg);
		                flag = true;
		                var divId='zookeeperLocalPath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            } else {
		                com.impetus.ankush.common.tooltipOriginal('zookeeperLocalPath','Local Path');
		                $('#zookeeperLocalPath').removeClass('error-box');
		            }
		        }
		        if(!com.impetus.ankush.validation.empty($('#installationPathZookeeper').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Installation Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('installationPathZookeeper',errorMsg);
	                flag = true;
	                var divId='installationPathZookeeper';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('installationPathZookeeper','Enter installation  path.');
	                $('#installationPathZookeeper').removeClass('error-box');
	            }
		        if(!com.impetus.ankush.validation.empty($('#dataDirZookeeper').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Data Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('dataDirZookeeper',errorMsg);
	                flag = true;
	                var divId='dataDirZookeeper';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('dataDirZookeeper','Enter data  path.');
	                $('#dataDirZookeeper').removeClass('error-box');
	            }
		        if(!com.impetus.ankush.validation.empty($('#clientPort').val())){
	                errorCount++;
	                errorMsg = 'Client Port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('clientPort',errorMsg);
	                flag = true;
	                var divId='clientPort';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#clientPort').val())) {
	                errorCount++;
	                errorMsg = 'Client Port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
	                com.impetus.ankush.common.tooltipMsgChange('clientPort',errorMsg);
	                flag = true;
	                var divId='clientPort';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else if (!com.impetus.ankush.validation.oPort($('#clientPort').val())) {
	                errorCount++;
	                errorMsg = 'Client Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
	                com.impetus.ankush.common.tooltipMsgChange('clientPort',errorMsg);
	                flag = true;
	                var divId='clientPort';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	                com.impetus.ankush.common.tooltipOriginal('clientPort','Enter client port.');
	                $('#clientPort').removeClass('error-box');
	            }
		        
		        if(!com.impetus.ankush.validation.empty($('#jmxPort_Zookeeper').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Jmx Port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('jmxPort_Zookeeper',errorMsg);
	                flag = true;
	                var divId='jmxPort_Zookeeper';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort_Zookeeper').val())) {
	                errorCount++;
	                errorMsg = 'Zookeeper Jmx Port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
	                com.impetus.ankush.common.tooltipMsgChange('jmxPort_Zookeeper',errorMsg);
	                flag = true;
	                var divId='jmxPort_Zookeeper';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            } else{
	                com.impetus.ankush.common.tooltipOriginal('jmxPort_Zookeeper','Enter Zookeeper Jmx port.');
	                $('#jmxPort_Zookeeper').removeClass('error-box');
	            }
		        if(!com.impetus.ankush.validation.empty($('#syncLimit').val())){
	                errorCount++;
	                errorMsg = 'Sync Limit '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('syncLimit',errorMsg);
	                flag = true;
	                var divId='syncLimit';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#syncLimit').val())) {
	                errorCount++;
	                errorMsg = 'Sync limit '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
	                com.impetus.ankush.common.tooltipMsgChange('syncLimit',errorMsg);
	                flag = true;
	                var divId='syncLimit';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if($('#syncLimit').val()<1) {
	                errorCount++;
	                errorMsg = 'Sync limit '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
	                com.impetus.ankush.common.tooltipMsgChange('syncLimit',errorMsg);
	                flag = true;
	                var divId='syncLimit';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('syncLimit','Enter sync limit.');
	                $('#syncLimit').removeClass('error-box');
	            }
		        if(!com.impetus.ankush.validation.empty($('#initLimit').val())){
	                errorCount++;
	                errorMsg = 'Init Limit '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('initLimit',errorMsg);
	                flag = true;
	                var divId='initLimit';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#initLimit').val())) {
	                errorCount++;
	                errorMsg = 'Init limit '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
	                com.impetus.ankush.common.tooltipMsgChange('initLimit',errorMsg);
	                flag = true;
	                var divId='initLimit';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if ($('#initLimit').val()<1) {
	                errorCount++;
	                errorMsg = 'Init limit '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
	                com.impetus.ankush.common.tooltipMsgChange('initLimit',errorMsg);
	                flag = true;
	                var divId='initLimit';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('Init','Enter init limit.');
	                $('#initLimit').removeClass('error-box');
	            }
		        if(!com.impetus.ankush.validation.empty($('#tickTime').val())){
	                errorCount++;
	                errorMsg = 'Ticktime '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('tickTime',errorMsg);
	                flag = true;
	                var divId='tickTime';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if (!com.impetus.ankush.validation.numeric($('#tickTime').val())) {
	                errorCount++;
	                errorMsg = 'Ticktime '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
	                com.impetus.ankush.common.tooltipMsgChange('tickTime',errorMsg);
	                flag = true;
	                var divId='tickTime';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            }else if ($('#tickTime').val()<1) {
	                errorCount++;
	                errorMsg = 'Ticktime '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
	                com.impetus.ankush.common.tooltipMsgChange('tickTime',errorMsg);
	                flag = true;
	                var divId='tickTime';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('tickTime','Enter Ticktime.');
	                $('#tickTime').removeClass('error-box');
	            }
		    	if($('#clusterDeploy').val()!=''){
		    		getNodeFlag=true;
		    	}
		        if (!getNodeFlag) {
		            errorMsg = com.impetus.ankush.errorMsg.errorHash['RetrieveNode'];
		                var divId='inputIpRange';
		                    errorCount++;
		                    flag = true;
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            }else{
		            	if($('#nodeIpTable .nodeCheckBox:checked').length <1)
		            	{
		            	errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectNode'];
		                    errorCount++;
		                    flag = true;
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable' >"+errorCount+". "+errorMsg+"</a></div>");
		            	}else if($('#nodeIpTable .zookeeperNodeCheckBox:checked').length==0){
		            		errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectOneNode']+'as Zookeeper node.';
		                    errorCount++;
		                    flag = true;
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable' >"+errorCount+". "+errorMsg+"</a></div>");
		            	}else{
		            	var zookeeperNode=0;
		            	for ( var i = 0; i < nodeStatus.nodes.length; i++) {
		            		if($("#nodeCheck" + i).is(':checked')){
		            			if($("#zookeeperNodeCheck" + i).is(':checked') ){
		            				zookeeperNode++;
		            			}
		            		}
		            	}
		            	if(zookeeperNode==0){
		            		 errorCount++;
		                    flag = true;
		                    errorMsg=com.impetus.ankush.errorMsg.errorHash['SelectOneNode']+' as Zookeeper';
			                $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable' >"+errorCount+". "+errorMsg+"</a></div>");
		            	}
		            }
		            }
		        if(errorCount>0 && errorMsg!=''){
		        	 $('#validateError').show().html(errorCount + ' Error');
		        	  $("#errorDivMain").show();
		        }else{
		        	 com.impetus.ankush.kafkaClusterCreation.clusterDeploy();
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
					data.nodePorts[ip] = [];
				}
			});
			data.username = $('#inputUserName').val();
			  if($("#authGroupBtn .active").data("value")==0){
					data.password=$.trim($('#inputPassword').val());
					data.privateKey='';
				}else{
					data.password='';
					data.privateKey=uploadPathSharedKey;
				}
			com.impetus.ankush.inspectNodesCall(data,id,'nodeRetrieveBtn');
		},
		
/*Function for cluster deployment data post*/  
		   clusterDeploy:function(){
				var data={};
				data.kafka = {
						advancedConf:{},
					zkNodesPort : {},
					nodes : new Array(),
				};
				data.zookeeper={
						nodes : new Array(),
				};
				var confJava={};
				data["@class"]="com.impetus.ankush.kafka.KafkaClusterConf";
				
				data.clusterName=$.trim($('#inputCluster').val());
				data.technology="Kafka";
				data.environment="In Premise";
				data.username=$.trim($('#inputUserName').val());
				data.password='';
				data.privateKey='';
				  if($("#authGroupBtn .active").data("value")==0){
					data.password=$.trim($('#inputPassword').val());
				}else{
					data.privateKey=uploadPathSharedKey;
				}
				if ($("#installJavaCheck").is(':checked')) {
					confJava.install=true;
					confJava.javaBundle=$.trim($('#inputBundlePath').val());	  
					confJava.javaHomePath='';
				}else{
					confJava.install=false;
					confJava.javaBundle='';
					confJava.javaHomePath=$.trim($('#inputJavaHome').val());	 
				}
				data.javaConf=confJava;
				  if($("#ipModeGroupBtn .active").data("value")==0){
					  data.ipPattern=$.trim($('#inputIpRange').val());
				  }else{
					  data.patternFile=$.trim($('#filePath').val());
				  }
				data.kafka.componentVendor=$.trim($('#vendorDropdown').val());
				data.kafka.componentVersion=$.trim($('#versionDropdown').val());
				data.kafka.installationPath=$.trim($('#installationPathKafka').val());
				if($("#versionDropdown").val().split('0.8.').length>1){
					data.kafka.replicationFactor=$.trim($('#replicationFactor').val());
				}else{
					data.kafka.replicationFactor=-1;
				}
				data.kafka.port=$.trim($('#port').val());
				data.kafka.jmxPort=$.trim($('#jmxPort_kafka').val());
				  if($("#sourcePathBtnGrp .active").data("value")==0){
					data.kafka.tarballUrl=$.trim($('#downloadPath').val()); 
				}else{
					data.kafka.localBinaryFile=$.trim($('#localPath').val());
				}
				data.kafka.serverTarballLocation='';
				data.kafka.logDir=$.trim($('#logDirectory').val());
				
				data.kafka.numOfNetworkThreads=$.trim($('#numOfNetworkThreads').val());
				data.kafka.numOfIOThreads=$.trim($('#numOfIOThreads').val());
				data.kafka.queuedMaxRequests=$.trim($('#queuedMaxRequests').val());
				data.kafka.numPartitions=$.trim($('#numPartitions').val());
				data.kafka.logRetentionHours=$.trim($('#logRetentionHours').val());
				data.kafka.logRetentitionBytes=$.trim($('#logRetentitionBytes').val());
				data.kafka.logCleanupIntervalMins=$.trim($('#logCleanupIntervalMins').val());
				data.kafka.logFlushIntervalMessage=$.trim($('#logFlushIntervalMessage').val());
				data.kafka.logFlushSchedularIntervalMs=$.trim($('#logFlushSchedularIntervalMs').val());
				data.kafka.logFlushIntervalMs=$.trim($('#logFlushIntervalMs').val());
				if($('#controlledShutdownEnable').is(':checked')){
					data.kafka.controlledShutdownEnable=true;
				}else{
					data.kafka.controlledShutdownEnable=false;
				}
				data.kafka.controlledShutdownMaxRetries=$.trim($('#controlledShutdownMaxRetries').val());
				data.kafka.advancedConf.logLevel=$.trim($('#logLevelDropdown').val());
				/*data.kafka.advancedConf.requestLogger=$.trim($('#requestLogger').val());
				data.kafka.advancedConf.loggerKafkaController=$.trim($('#loggerKafkaController').val());
				data.kafka.advancedConf.stateChangeLogger=$.trim($('#stateChangeLogger').val());
				data.kafka.advancedConf.loggerRequestChannel=$.trim($('#loggerRequestChannel').val());*/
				var zookeeperNodes=new Array();
				for ( var i = 0; i < nodeStatus.nodes.length; i++) {
					var nodeObj={};
					var zookeeperObj={};
					if ($("#nodeCheck" + i).is(':checked')) {
							nodeObj.publicIp=nodeStatus.nodes[i][0];
							nodeObj.privateIp=nodeStatus.nodes[i][0];
							nodeObj.os=nodeStatus.nodes[i][4];
							nodeObj.nodeState='deploying';
							nodeObj.type='Kafka';
							data.kafka.nodes.push(nodeObj);
						if ($('#zookeeperNodeCheck'+i).prop("checked")) {
							zookeeperNodes.push(nodeStatus.nodes[i][0]);
							zookeeperObj.publicIp=nodeStatus.nodes[i][0];
							zookeeperObj.privateIp=nodeStatus.nodes[i][0];
							zookeeperObj.os=nodeStatus.nodes[i][4];
							zookeeperObj.nodeState='deploying';
							zookeeperObj.type='Zookeeper';
							data.zookeeper.nodes.push(zookeeperObj);
						}
					}
				}
				data.kafka.zkNodesPort.zkNodes=zookeeperNodes;
				data.kafka.zkNodesPort.zkPort=$.trim($('#clientPort').val());
				data.zookeeper.componentVendor=$.trim($('#zookeeperVendorDropdown').val());
				data.zookeeper.componentVersion=$.trim($('#zookeeperVersionDropdown').val());
				data.zookeeper.installationPath=$.trim($('#installationPathZookeeper').val());
				 if($("#zookeeperPathBtnGrp .active").data("value")==0){
					data.zookeeper.tarballUrl=$.trim($('#zookeeperDownloadPath').val()); 
				}else{
					data.zookeeper.localBinaryFile=$.trim($('#zookeeperLocalPath').val());
				}
				data.zookeeper.dataDirectory=$.trim($('#dataDirZookeeper').val());
				data.zookeeper.syncLimit=$.trim($('#syncLimit').val());
				data.zookeeper.initLimit=$.trim($('#initLimit').val());
				data.zookeeper.tickTime=$.trim($('#tickTime').val());
				data.zookeeper.clientPort=$.trim($('#clientPort').val());
				data.zookeeper.jmxPort=$.trim($('#jmxPort_Zookeeper').val());
				var clusterId=$('#clusterDeploy').val();
				var url = baseUrl + '/cluster/create/Kafka';
				if(clusterId!=''){
					 url = baseUrl + '/cluster/redeploy/'+clusterId;
				}
				$('#clusterDeploy').button();
				$('#clusterDeploy').button('loading');
				$.ajax({
					'type' : 'POST',
					'url' : url,
					'data' : JSON.stringify(data),
					'contentType' : 'application/json',
					'dataType' : 'json',
					'async' : true,
					'success' : function(result) {
						$('#clusterDeploy').button('reset');
						if (!result.output.status) {
							$("#errorDivMain").show().html('');
							if(result.output.error.length > 1){
								$('#validateError').show().html(result.output.error.length+ ' Errors');
							}
							else{
								$('#validateError').show().html(result.output.error.length+ ' Error');
							}
							for ( var i = 0; i < result.output.error.length; i++) {
								$("#errorDivMain").append("<div class='errorLineDiv'><a href='#' >"+(i+1)+". "+result.output.error[i]+"</a></div>");
							}
						}else{
							$("#errorDivMain").hide();
							$('#validateError').hide();
							com.impetus.ankush.common.loadDashboard();
						}
					},
					'error' : function() {
						$('#clusterDeploy').button('reset');
						com.impetus.ankush.common.loadDashboard();
					}
				});
			},
};
