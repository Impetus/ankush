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

var oNodeTable=null;
var nodeStatus=null;
var defaultVlaue=null;
var uploadFilePath=null;
var uploadShareKeyFilePath=null;
var getNodeFlag=null;
var userName='/home/${user}';
var errorCount=0;
var setupDetailData=null;
var  inspectNodeData = {};
var disableNodeCount=0;
com.impetus.ankush.stormClusterCreation = {
/*Function for tooltip initialization*/
		tooltipInitialize:function(){
			$('#clusterName').tooltip();
			$('#homePathJava').tooltip();
			$('#bundlePathJava').tooltip();
			$('#commonUserName').tooltip();
			$('#commonUserPassword').tooltip();
			$('#ipRange').tooltip();
			$('#pathIPFile').tooltip();
			$('#pathSharedKey').tooltip();
			$('#vendor-Storm').tooltip();
			$('#version-Storm').tooltip();
			$('#downloadPath-Storm').tooltip();
		//	$('#javaLibPath').tooltip();
			$('#localPath-Storm').tooltip();
			$('#supervisorPorts').tooltip();
			$('#uiPorts').tooltip();
			$('#localDirStorm').tooltip();
			$('#installationPath-Storm').tooltip();
			$('#vendor-Zookeeper').tooltip();
			$('#version-Zookeeper').tooltip();
			$('#downloadPath-Zookeeper').tooltip();
			$('#localPath-Zookeeper').tooltip();
			$('#installationPath-Zookeeper').tooltip();
			$('#dataPath-Zookeeper').tooltip();
			$('#clientPort-Zookeeper').tooltip();
			$('#syncLimit').tooltip();
			$('#initLimit').tooltip();
			$('#tickTime').tooltip();
			
			$('#jmxPort-Nimbus').tooltip();
			$('#jmxPort-Supervisor').tooltip();
			$('#jmxPort-Zookeeper').tooltip();
			
		},

/*Function for changing version and download path on change of zookeeper vendor dropdown*/
		vendorOnChangeZookeeper:function(){
			$("#version-Zookeeper").html('');
			for ( var key in defaultVlaue.output.storm.Zookeeper.Vendors[$("#vendor-Zookeeper").val()]){
				$("#version-Zookeeper").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath-Zookeeper').val(defaultVlaue.output.storm.Zookeeper.Vendors[$("#vendor-Zookeeper").val()][$("#version-Zookeeper").val()].url);
			
		},
/*Function for changing  download path on change of version zookeeper dropdown*/
		versionChangeZookeeper:function(){
			$('#downloadPath-Zookeeper').val(defaultVlaue.output.storm.Zookeeper.Vendors[$("#vendor-Zookeeper").val()][$("#version-Zookeeper").val()].url);
			
		},
/*Function for changing version and download path on change of storm vendor dropdown*/
		vendorOnChangeStorm:function(btnType){
			$("#version-Storm").html('');
			for ( var key in defaultVlaue.output.storm.Storm.Vendors[$("#vendor-Storm").val()]){
				$("#version-Storm").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath-Storm').val(defaultVlaue.output.storm.Storm.Vendors[$("#vendor-Storm").val()][$("#version-Storm").val()].url);
		},
/*Function for changing  download path on change of version storm dropdown*/
		versionChangeStorm:function(btnType){
			$('#downloadPath-Storm').val(defaultVlaue.output.storm.Storm.Vendors[$("#vendor-Storm").val()][$("#version-Storm").val()].url);
		},

/*Function for changing installation and datadir path on change of user*/
		pathChange: function(){
			var user=$.trim($('#commonUserName').val());
			var installationPathStorm=$.trim($('#installationPath-Storm').val()).split(userName).join('/home/'+user);
			var installationPathZookeeper=$.trim($('#installationPath-Zookeeper').val()).split(userName).join('/home/'+user);
			var dataPathZookeeper=$.trim($('#dataPath-Zookeeper').val()).split(userName).join('/home/'+user);
			var localDirStorm=$.trim($('#localDirStorm').val()).split(userName).join('/home/'+user);
			userName='/home/'+user;
			$('#installationPath-Storm').empty().val(installationPathStorm);
			$('#installationPath-Zookeeper').empty().val(installationPathZookeeper);
			$('#dataPath-Zookeeper').empty().val(dataPathZookeeper);
			$('#localDirStorm').empty().val(localDirStorm);
		},
/*Function for uploading share key file*/
		uploadShareKeyFile : function(id1,id2) {
			uploadShareKeyFilePath=null;
			var uploadUrl = baseUrl + '/uploadFile';
			$('#'+id1).click();
			$('#'+id1).change(
					function() {
					$('#'+id2).val($('#'+id1).val());
					com.impetus.ankush.uploadFile(uploadUrl,id1,function(data){
						var htmlObject = $(data);
				        var jsonData = JSON.parse(htmlObject.text());
				        uploadShareKeyFilePath= jsonData.output;
						});
					});
		},

/*Function for uploading IP file*/
		getNodesUpload : function() {
			uploadFileFlag=true;
		     var uploadUrl = baseUrl + '/uploadFile';
		        $('#fileUploadIPFile').click();
		        $('#fileUploadIPFile').change(function(){
			        $('#pathIPFile').val($('#fileUploadIPFile').val());
			        com.impetus.ankush.uploadFileNew(uploadUrl,"fileUploadIPFile",null,function(data){
			        var htmlObject = $(data);
			        var jsonData = JSON.parse(htmlObject.text());
			        uploadFilePath=jsonData.output;
			        });
			    });
		},
		
/*Function for client side validations on retrieve button click*/
		validateNode:function(){
			var userName = $.trim($('#commonUserName').val());
			var password = $('#commonUserPassword').val();	
			var ipPattern= $.trim($('#ipRange').val());
			var radioVal=0;
			var radioAuth=0;
			
			if($("#ipModeGroupBtn .active").data("value")==1){
				radioVal=1;
			}
			//if($('input[name=authenticationRadio]:checked').val()==1){
			 if($("#authGroupBtn .active").data("value")==1){
				radioAuth=1;
			}
			 errorCount = 0;
		        $("#errorDivMain").html('').hide();
		        $('#validateError').html('').hide();
		        if(!com.impetus.ankush.validation.empty($('#commonUserName').val())){
		            errorCount++;
		            errorMsg = 'commonUserName '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		            com.impetus.ankush.common.tooltipMsgChange('commonUserName',errorMsg);
		            divId='commonUserName';
		            errorId='lblErr_commonUserName_Empty';
		            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a  id='lblErr_commonUserName_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");          
		        }else{
		            com.impetus.ankush.common.tooltipOriginal('commonUserName',' Enter Username.');
		            $('#commonUserName').removeClass('error-box');
		        }
		       // if($('input[name=authenticationRadio]:checked').val()==0){
		        if($("#authGroupBtn .active").data("value")==0){
		      //  if($('.authRadio[class*="active"]').val()==0){
			        if (!com.impetus.ankush.validation.empty($('#commonUserPassword').val())) {
			            errorCount++;
			            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			            com.impetus.ankush.common.tooltipMsgChange('commonUserPassword',errorMsg);
			            var divId='commonUserPassword';
			            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_commonUserPassword_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			      
			        }else{
			            com.impetus.ankush.common.tooltipOriginal('commonUserPassword','Enter password.');
			            $('#commonUserPassword').removeClass('error-box');
			        }
		        }else{
		        	  if (uploadShareKeyFilePath==null){
		        		  errorCount++;
		                  errorMsg = 'Shared Key '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload']; 
		                  var divId='pathSharedKey';
		                  $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_File_NotUploaded' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		        	  }else{
		                  com.impetus.ankush.common.tooltipOriginal('pathSharedKey','Upload File.');
		                  $('#pathSharedKey').removeClass('error-box');
		        	  }
		        }
		        if($("#ipModeGroupBtn .active").data("value")==0){
		            if(!com.impetus.ankush.validation.empty($('#ipRange').val())){
		                errorCount++;
		                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('ipRange',errorMsg);
		                var divId='ipRange';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_IPRange_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP range.');
		                $('#ipRange').removeClass('error-box');
		            }
		        }
		        if($("#ipModeGroupBtn .active").data("value")==1){
		        	 if (uploadFilePath==null){
		                errorCount++;
		                errorMsg = 'File path '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
		                com.impetus.ankush.common.tooltipMsgChange('pathIPFile',errorMsg);
		                var divId='pathIPFile';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_FilePath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            } else {
		                com.impetus.ankush.common.tooltipOriginal('pathIPFile','Browse File.');
		                $('#pathIPFile').removeClass('error-box');
		            }
		        }
		        if(errorCount>0 && errorMsg!=''){
		        	 $('#validateError').show().html(errorCount + ' Error');
		        	  $("#errorDivMain").show();
		        }else{
		        	$('#retrieveNodes').button();
					$('#retrieveNodes').button('loading');
					$('#nodeList_filter').hide();
					$("#nodeTableDiv").show();
					$("#shardNodeTableHeaderDiv").show();
					 nodeStatus=null;
			            if(null!= setupDetailData){
			 	            setupDetailData.nodes=new Array();	
			            }
					var functionCall=function(result) {
						com.impetus.ankush.stormClusterCreation.getNodes(result);
					};
					com.impetus.ankush.nodeRetrieve(userName,password,uploadFilePath,ipPattern,radioVal,uploadShareKeyFilePath,radioAuth,functionCall,'retrieveNodes','','','inspectNodeBtn');
		        }
			
		},
/*Function for populating retrieved nodes in IP table*/
		getNodes : function(result) {
			var errorNodeCount=0;
			getNodeFlag = true;
			disableNodeCount = 0;
			var adminNodeCount=0;
			var herfFunction;
			var tooltipMsg;
			var divId;
			 inspectNodeData = {};
			$('#retrieveNodes').button('reset');
			uploadFileFlag=false;
			nodeStatus = result.output;
			$('#nodeList tbody tr').css('border-bottom',
					'1px solid #E1E3E4"');
			if (oNodeTable != null) {
				oNodeTable.fnClearTable();
			}
			nodeTableLength = nodeStatus.nodes.length;
			if($("#ipModeGroupBtn .active").data("value")==1){
				 divId='pathIPFile';
				tooltipMsg='IP pattern is not valid in file';							
				herfFunction="javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")";
				
			}else{
				 divId='ipRange';
				tooltipMsg='IP pattern is not valid.';
				herfFunction="javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")";
			}
			if(nodeTableLength == 0){
				errorMsg = nodeStatus.error;
				$("#errorDivMain").append("<div class='errorLineDiv'><a href="+herfFunction+" >1. "+errorMsg+"</a></div>");						
				com.impetus.ankush.common.tooltipMsgChange(divId,tooltipMsg);
				$("#errorDivMain").show();
				$('#validateError').show().html('1 Error');
			}else{
				if($("#ipModeGroupBtn .active").data("value")==1){
				com.impetus.ankush.common.tooltipOriginal('pathIPFile','Browse File.');
				$('#pathIPFile').removeClass('error-box');
				}else{
				com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP pattern.');
				$('#ipRange').removeClass('error-box');
				}
				$("#errorDivMain").hide();
				$('#validateError').hide();	
				}
			oNodeTable = $('#nodeList').dataTable();
			$('#nodeSearch').keyup(function() {
				oNodeTable.fnFilter($(this).val());
			});
			
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
			
				var addId = null;
					addId = oNodeTable.fnAddData([
									'<input type="checkbox" name="" value=""  id="nodeCheck'
											+ i
											+ '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.stormClusterCreation.nodeCheckBox('
											+ i + ',\'nodeList\',\'nodeCheckBox\',\'nodeCheckHead\');"/>',
											nodeStatus.nodes[i][0],
									'<input id="nimbusCheckBox'
											+ i
											+ '" class="nimbusCheck" name="nimbusRadio" style="margin-right:10px;" type="radio" onclick="com.impetus.ankush.stormClusterCreation.nimbusCheck()">',
											'<input id="zookeeperCheckBox'
											+ i
											+ '" class="zookeeperCheck" style="margin-right:10px;" type="checkbox" >',
											'<input id="supervisorCheckBox'
											+ i
											+ '" class="supervisorCheck" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.stormClusterCreation.supervisorCheckBox('
											+ i + ',\'nodeList\',\'supervisorCheck\',\'headCheckSup\');" >',
									'<a class="" id="osName'
											+ i + '">'+nodeStatus.nodes[i][4]+'</a>',
											
											'<a class="" id="cpuCount'
											+ i + '">'+nodeStatus.nodes[i][7]+'</a>',
											'<a class="" id="diskCount'
											+ i + '">'+nodeStatus.nodes[i][8]+'</a>',
											'<a class="" id="diskSize'
											+ i + '">'+nodeStatus.nodes[i][9]+' GB</a>',
											'<a class="" id="ramSize'
											+ i + '">'+nodeStatus.nodes[i][10]+' GB</a>',				
									'<div><a href="##" onclick="com.impetus.ankush.stormSetupDetail.loadNodeDetailStorm('
											+ i
											+ ');"><img id="navigationImg-'
											+ i
											+ '" src="'
											+ baseUrl
											+ '/public/images/icon-chevron-right.png" /></a></div>' ]);
										
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
					$('#nimbusCheckBox' + i).attr('disabled', true);
					$('#zookeeperCheckBox' + i).attr('disabled', true);
					$('#supervisorCheckBox' + i).attr('disabled', true);
					errorNodeCount++;
					disableNodeCount++;
				}else{
					$('#nodeCheck'+i).prop("checked", true);
					if(adminNodeCount<3){
					$('#adminCheckBox'+i).prop("checked", true);	
					adminNodeCount++;
					}
				}
				if(nodeStatus.nodes[i][1] != true){
					var status='Unavailable';
				$("#errorDivMain").append("<div class='errorLineDiv'>"+(errorCount+errorNodeCount)+". <a href='#nodeList'  >Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
			}else if(nodeStatus.nodes[i][2] != true){
				var status='Unreachable';
				$("#errorDivMain").append("<div class='errorLineDiv'>"+(errorCount+errorNodeCount)+". <a href='#nodeList'  >Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
			}else if(nodeStatus.nodes[i][3] != true){
				var status='Unauthenticated';
				$("#errorDivMain").append("<div class='errorLineDiv'>"+(errorCount+errorNodeCount)+". <a href='#nodeList'  >Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");	
			}
			}
			if(nodeStatus.nodes.length>0){
				for(var i=0;i<nodeStatus.nodes.length;i++){
					if($('#nodeCheck'+i).is(':checked')){
						$('#nimbusCheckBox'+i).prop("checked", true);
						break;
					}
				}
			}
			if(disableNodeCount != nodeTableLength){
				$('#nodeCheckHead').prop("checked", true);
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
				$('.nimbusCheck').removeAttr('checked');
	        	$('.zookeeperCheck').removeAttr('checked');
	        	$('.supervisorCheck').removeAttr('checked');
	        	$('#headCheckSup').removeAttr('checked'); 	
			}
		},
/*Function for check/uncheck head check box on click of node checkbox*/
		 nodeCheckBox : function(i,table,nodeCheck,checkHead) {
		        if ($("#"+table+" ."+nodeCheck+":checked").length == $("#"+table+" ."+nodeCheck).length
		                - disableNodeCount) {
		            $("#"+checkHead).prop("checked", true);
		        } else {
		            $("#"+checkHead).removeAttr("checked");
		        }
		      
		        if(!$("#nodeCheck"+i).is(':checked')){
		        	$('#nimbusCheckBox'+i).removeAttr('checked');
		        	$('#zookeeperCheckBox'+i).removeAttr('checked');
		        	$('#supervisorCheckBox'+i).removeAttr('checked');
		        }
		    },
/*Function for check/uncheck head check box on click of supervisor checkbox*/
			 supervisorCheckBox : function(i,table,nodeCheck,checkHead) {
			        if ($("#"+table+" ."+nodeCheck+":checked").length == $("#"+table+" ."+nodeCheck).length
			                - disableNodeCount) {
			            $("#"+checkHead).prop("checked", true);
			        } else {
			            $("#"+checkHead).removeAttr("checked");
			        }
			    },
/*Function for disabling supervisor check box on click of corresponding nibus check*/
		    nimbusCheck:function(){
		    	/*for ( var i = 0; i < nodeStatus.nodes.length; i++) {
		    		if ($('#nimbusCheckBox'+i).is(':checked')) {
			    		  $('#supervisorCheckBox'+i).attr('checked', false);
			    	}
		    	}*/
		    	
		    },
		    
/*Function for client side validations for cluster deployment*/
	validateCluster:function(){
		  $('#validateError').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMain").html('').hide();
	        errorCount = 0;
	        if (!com.impetus.ankush.validation.empty($('#clusterName').val())) {
	            errorCount++;
	            errorMsg = 'Cluster Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ClusterName_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	        } else if ($('#clusterName').val().trim().split(' ').length > 1) {
	            errorCount++;
	            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameBlankSpace'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a  id='lblErr_ClusterName_BlankSpace' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	        }else if ($('#clusterName').val().length > 20) {
	            errorCount++;
	            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameLength'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ClusterName_Length' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	        }
	        else if (!com.impetus.ankush.validation.alphaNumericChar($('#clusterName').val())) {
	            errorCount++;
	            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameSpecialChar'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ClusterName_SpecialChar' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	        }
	        else {
	            com.impetus.ankush.common.tooltipOriginal('clusterName','Enter Cluster Name.');
	            $('#clusterName').removeClass('error-box');
	        }
	        if($('#chkInstallJava').is(':checked')){
	        	com.impetus.ankush.common.tooltipOriginal('homePathJava','Enter default java home.');
	            $('#homePathJava').removeClass('error-box');
	        	  if (!com.impetus.ankush.validation.empty($('#bundlePathJava').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Java Bundle path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            com.impetus.ankush.common.tooltipMsgChange('bundlePathJava',errorMsg);
	  	            var divId='bundlePathJava';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_BundlePathJava_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('bundlePathJava','Enter Bundle path.');
		            $('#bundlePathJava').removeClass('error-box');
		        }
	        }else{
	        	 com.impetus.ankush.common.tooltipOriginal('bundlePathJava','Enter Bundle path.');
		            $('#bundlePathJava').removeClass('error-box');
	        	if (!com.impetus.ankush.validation.empty($('#homePathJava').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Java home path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            com.impetus.ankush.common.tooltipMsgChange('homePathJava',errorMsg);
	  	            var divId='homePathJava';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_HomePathJava_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('homePathJava','Enter default java home.');
		            $('#homePathJava').removeClass('error-box');
		        }
	        }
	        if (!com.impetus.ankush.validation.empty($('#commonUserName').val())) {
  	            errorCount++;
  	            errorMsg = 'User Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            com.impetus.ankush.common.tooltipMsgChange('commonUserName',errorMsg);
  	            var divId='commonUserName';
  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_UserName_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
  	        }else {
	            com.impetus.ankush.common.tooltipOriginal('commonUserName','A common username for authenticating nodes. User must have administrative rights on the nodes.');
	            $('#commonUserName').removeClass('error-box');
	        }
	        if($("#authGroupBtn .active").data("value")==0){
		        if (!com.impetus.ankush.validation.empty($('#commonUserPassword').val())) {
		            errorCount++;
		            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		            com.impetus.ankush.common.tooltipMsgChange('commonUserPassword',errorMsg);
		            var divId='commonUserPassword';
		            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_Password_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		      
		        }else{
		            com.impetus.ankush.common.tooltipOriginal('commonUserPassword','The password to authenticate the nodes.');
		            $('#commonUserPassword').removeClass('error-box');
		        }
	        }else{
	        	  if (uploadShareKeyFilePath==null){
	        		  errorCount++;
	                  errorMsg = 'Share Key '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload']; 
	                  var divId='pathSharedKey';
	                  $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ShareKeyFile_NotUploaded' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	        	  }else{
	                  com.impetus.ankush.common.tooltipOriginal('pathSharedKey','Upload the file.');
	                  $('#pathSharedKey').removeClass('error-box');
	        	  }
	        }
	        if($("#ipModeGroupBtn .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#ipRange').val())){
	                errorCount++;
	                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('ipRange',errorMsg);
	                var divId='ipRange';
	                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_IpRange_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('ipRange','IPs for a node or set of nodes. E.g. 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. For more options refer the User Guide.');
	                $('#ipRange').removeClass('error-box');
	            }
	        }else{
	        	if (uploadFilePath == null) {
	                errorCount++;
	                errorMsg = 'IP '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
	                com.impetus.ankush.common.tooltipMsgChange('pathIPFile',errorMsg);
	                var divId='pathIPFile';
	                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_IpFile_NotUploaded' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('pathIPFile','A text file (*.txt) that contains a node or set of nodes specified in the form of 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;. Each unique pattern / IP should be in a new line. For more options refer the User Guide.');
	                $('#pathIPFile').removeClass('error-box');
	            }
	        }
	        if (!com.impetus.ankush.validation.empty($('#vendor-Storm').val())) {
  	            errorCount++;
  	            errorMsg = "Storm vendor"+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='vendor-Storm';
  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_StormVendor_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('vendor-Storm','Select Storm vendor.');
	            $('#vendor-Storm').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#version-Storm').val())) {
  	            errorCount++;
  	          errorMsg = "Storm version"+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='version-Storm';
  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_StormVersion_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('version-Storm','Select Storm version.');
	            $('#version-Storm').removeClass('error-box');
	        }
	        //if($('input[name=stormRadio]:checked').val()==0){
	        if($("#sourcePathBtnGrp .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#downloadPath-Storm').val())){
	                errorCount++;
	                errorMsg = 'Storm Download path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('downloadPath-Storm',errorMsg);
	                var divId='downloadPath-Storm';
	                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_StormDownloadPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('downloadPath-Storm','Enter download path of Storm binary file.');
	                $('#downloadPath-Storm').removeClass('error-box');
	            }
	        }else{
	        	if(!com.impetus.ankush.validation.empty($('#localPath-Storm').val())){
	                errorCount++;
	                errorMsg = 'Storm Local path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('localPath-Storm',errorMsg);
	                var divId='localPath-Storm';
	                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_StormLocalPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('localPath-Storm','Enter local path of Storm binary file.');
	                $('#localPath-Storm').removeClass('error-box');
	            }
	        }
	
	       var supPort=$('#supervisorPorts').val().split(',');
	       var supFlag=false;
	       var supMsg='';
	       for(var i=0;i<supPort.length;i++){
	    	   if(i==0){
	    		   if (supPort[0]==''){
		    		   supFlag=true;  
		    		   supMsg='Supervisor port value cannot start with comma.';  
	    	   }
	    	   }
	    		   if(supFlag)
		    		   break;
	    		  
	    	   if (!com.impetus.ankush.validation.numeric(supPort[i])){
	    		   supFlag=true;  
	    		   supMsg='Each Supervisor port must be numeric.';
	    	   }else if (!com.impetus.ankush.validation.oPort(supPort[i])){
	    		   supFlag=true;  
	    		   supMsg='Each Supervisor port must be between 1024-65535.';
	    	   }
	    	   if(supFlag)
	    		   break;
	       }
	        if(!com.impetus.ankush.validation.empty($('#supervisorPorts').val())){
                errorCount++;
                errorMsg = 'Supervisor port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('supervisorPorts',errorMsg);
                var divId='supervisorPorts';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_SupervisorPort_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if(supFlag) {
            	  errorCount++;
            	  errorMsg = supMsg;
                  var divId='supervisorPorts';
                  $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_SupervisorPort_ValidPort' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            	  com.impetus.ankush.common.tooltipMsgChange('supervisorPorts',errorMsg);
            }
            else{
                com.impetus.ankush.common.tooltipOriginal('supervisorPorts','Enter comma seperated slot ports.');
                $('#supervisorPorts').removeClass('error-box');
            }
	        if(!com.impetus.ankush.validation.empty($('#uiPorts').val())){
                errorCount++;
                errorMsg = 'Storm UI port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('uiPorts',errorMsg);
                var divId='uiPorts';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_UiPort_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#uiPorts').val())) {
                errorCount++;
                errorMsg = 'Storm UI port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('uiPorts',errorMsg);
                var divId='uiPorts';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_UiPort_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            } else if (!com.impetus.ankush.validation.oPort($('#uiPorts').val())) {
                errorCount++;
                errorMsg = 'Storm UI port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                com.impetus.ankush.common.tooltipMsgChange('uiPorts',errorMsg);
                var divId='uiPorts';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_UiPort_Range' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            } else{
                com.impetus.ankush.common.tooltipOriginal('uiPorts','Enter Storm UI port.');
                $('#uiPorts').removeClass('error-box');
            }
	        
	        if(!com.impetus.ankush.validation.empty($('#jmxPort-Nimbus').val())){
                errorCount++;
                errorMsg = 'Nimbus JMX port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Nimbus',errorMsg);
                var divId='jmxPort-Nimbus';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NimbusJmxPort_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort-Nimbus').val())) {
                errorCount++;
                errorMsg = 'Nimbus JMX port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Nimbus',errorMsg);
                var divId='jmxPort-Nimbus';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NimbusJmxPort_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            } 
            else if (!com.impetus.ankush.validation.oPort($('#jmxPort-Nimbus').val())) {
                errorCount++;
                errorMsg = 'Nimbus JMX port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Nimbus',errorMsg);
                var divId='jmxPort-Nimbus';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NimbusJmxPort_Range' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('jmxPort-Nimbus','Enter Nimbus JMX port.');
                $('#jmxPort-Nimbus').removeClass('error-box');
            }
	        if(!com.impetus.ankush.validation.empty($('#jmxPort-Supervisor').val())){
                errorCount++;
                errorMsg = 'Supervisor JMX port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Supervisor',errorMsg);
                var divId='jmxPort-Supervisor';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_SupervisorJmxPort_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort-Supervisor').val())) {
                errorCount++;
                errorMsg = 'Supervisor JMX port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Supervisor',errorMsg);
                var divId='jmxPort-Supervisor';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_SupervisorJmxPort_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.oPort($('#jmxPort-Supervisor').val())) {
                errorCount++;
                errorMsg = 'Supervisor JMX port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Supervisor',errorMsg);
                var divId='jmxPort-Supervisor';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_SupervisorJmxPort_Range' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            } else{
                com.impetus.ankush.common.tooltipOriginal('jmxPort-Supervisor','Enter Supervisor JMX port.');
                $('#jmxPort-Supervisor').removeClass('error-box');
            }
	        
	        if(!com.impetus.ankush.validation.empty($('#localDirStorm').val())){
                errorCount++;
                errorMsg = 'Local Directory path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('localDirStorm',errorMsg);
                var divId='localDirStorm';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_LocalDir_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('localDirStorm','Enter directory path for Storm.');
                $('#localDirStorm').removeClass('error-box');
            }
	        if(!com.impetus.ankush.validation.empty($('#installationPath-Storm').val())){
                errorCount++;
                errorMsg = 'Storm Installation path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('installationPath-Storm',errorMsg);
                var divId='installationPath-Storm';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_StormInstallationPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('installationPath-Storm','Enter installation path for Storm.');
                $('#installationPath-Storm').removeClass('error-box');
            }
	        if (!com.impetus.ankush.validation.empty($('#vendor-Zookeeper').val())) {
  	            errorCount++;
  	            errorMsg = 'Zookeeper vendor '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='vendor-Zookeeper';
  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperVendor_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('vendor-Zookeeper','Select Zookeeper vendor.');
	            $('#vendor-Zookeeper').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#version-Zookeeper').val())) {
  	            errorCount++;
  	            errorMsg = 'Zookeeper version '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='version-Zookeeper';
  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperVersion_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('version-Zookeeper','Select Zookeeper version.');
	            $('#version-Zookeeper').removeClass('error-box');
	        }
	        if($("#zookeeperPathBtnGrp .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#downloadPath-Zookeeper').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Download path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('downloadPath-Zookeeper',errorMsg);
	                var divId='downloadPath-Zookeeper';
	                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperDownloadPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('downloadPath-Zookeeper','Enter download path of Zookeeper binary file.');
	                $('#downloadPath-Zookeeper').removeClass('error-box');
	            }
	        }else{
	        	if(!com.impetus.ankush.validation.empty($('#localPath-Zookeeper').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Local path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('localPath-Zookeeper',errorMsg);
	                var divId='localPath-Zookeeper';
	                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperLocalPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('localPath-Zookeeper','Enter local path of Zookeeper binary file.');
	                $('#localPath-Zookeeper').removeClass('error-box');
	            }
	        }
	        if(!com.impetus.ankush.validation.empty($('#installationPath-Zookeeper').val())){
                errorCount++;
                errorMsg = 'Zookeeper Installation path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('installationPath-Zookeeper',errorMsg);
                var divId='installationPath-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperInstallationPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('installationPath-Zookeeper','Enter installation path for Zookeeper.');
                $('#installationPath-Zookeeper').removeClass('error-box');
            }
	        if(!com.impetus.ankush.validation.empty($('#dataPath-Zookeeper').val())){
                errorCount++;
                errorMsg = 'Zookeeper Data path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('dataPath-Zookeeper',errorMsg);
                var divId='dataPath-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperDataPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('dataPath-Zookeeper','Enter data directory path for Zookeeper.');
                $('#dataPath-Zookeeper').removeClass('error-box');
            }
	        if(!com.impetus.ankush.validation.empty($('#clientPort-Zookeeper').val())){
                errorCount++;
                errorMsg = 'Client port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('clientPort-Zookeeper',errorMsg);
                var divId='clientPort-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ClientPort_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#clientPort-Zookeeper').val())) {
                errorCount++;
                errorMsg = 'Client port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('clientPort-Zookeeper',errorMsg);
                var divId='clientPort-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ClientPort_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            } else if (!com.impetus.ankush.validation.oPort($('#clientPort-Zookeeper').val())) {
                errorCount++;
                errorMsg = 'Client port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                com.impetus.ankush.common.tooltipMsgChange('clientPort-Zookeeper',errorMsg);
                var divId='clientPort-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ClientPort_Range' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            } else{
                com.impetus.ankush.common.tooltipOriginal('clientPort-Zookeeper','Enter Client port.');
                $('#clientPort-Zookeeper').removeClass('error-box');
            }
	        
	        if(!com.impetus.ankush.validation.empty($('#jmxPort-Zookeeper').val())){
                errorCount++;
                errorMsg = 'Zookeeper JMX port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Zookeeper',errorMsg);
                var divId='jmxPort-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperJmxPort_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort-Zookeeper').val())) {
                errorCount++;
                errorMsg = 'Zookeeper JMX port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Zookeeper',errorMsg);
                var divId='jmxPort-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperJmxPort_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.oPort($('#jmxPort-Zookeeper').val())) {
                errorCount++;
                errorMsg = 'Zookeeper JMX port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                com.impetus.ankush.common.tooltipMsgChange('jmxPort-Zookeeper',errorMsg);
                var divId='jmxPort-Zookeeper';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperJmxPort_Range' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }  else{
                com.impetus.ankush.common.tooltipOriginal('jmxPort-Zookeeper','Enter Zookeeper JMX port.');
                $('#jmxPort-Zookeeper').removeClass('error-box');
            }
	        
	        if(!com.impetus.ankush.validation.empty($('#syncLimit').val())){
                errorCount++;
                errorMsg = 'Sync Limit '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('syncLimit',errorMsg);
                var divId='syncLimit';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_SyncLimit_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#syncLimit').val())) {
                errorCount++;
                errorMsg = 'Sync limit '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('syncLimit',errorMsg);
                var divId='syncLimit';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_SyncLimit_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('syncLimit','Enter sync limit.');
                $('#syncLimit').removeClass('error-box');
            }
	        if(!com.impetus.ankush.validation.empty($('#initLimit').val())){
                errorCount++;
                errorMsg = 'Init Limit '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('initLimit',errorMsg);
                var divId='initLimit';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_InitLimit_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#initLimit').val())) {
                errorCount++;
                errorMsg = 'Init limit '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('initLimit',errorMsg);
                var divId='initLimit';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_InitLimit_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('initLimit','Enter init limit.');
                $('#initLimit').removeClass('error-box');
            }
	        if(!com.impetus.ankush.validation.empty($('#tickTime').val())){
                errorCount++;
                errorMsg = 'Ticktime '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('tickTime',errorMsg);
                var divId='tickTime';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_Ticktime_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#tickTime').val())) {
                errorCount++;
                errorMsg = 'Ticktime '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('tickTime',errorMsg);
                var divId='tickTime';
                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_Ticktime_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('tickTime','Enter ticktime.');
                $('#tickTime').removeClass('error-box');
            }
	        if($('#clusterDeploy').val()!=''){
	    		getNodeFlag=true;
	    	}
	        if (!getNodeFlag) {
	            errorMsg = com.impetus.ankush.errorMsg.errorHash['RetrieveNode'];
	                var divId='ipRange';
	                    errorCount++;
	                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeRetrieve' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	            }
	        else{
	        	var nimbus=0;
	        	var count=0;
	            $('.nodeCheckBox').each(function(){
	            	 if($('#nodeCheck'+count).is(':checked')){
	            		 if($('#nimbusCheckBox'+count).is(':checked')){
	            			 nimbus++;
	            		 }
	            	 }
	            	 count++;
	            });
	        
	        	if($('#nodeList .nodeCheckBox:checked').length==0){
            		errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectNode'];
                    errorCount++;
                    var divId='nodeTableDiv';
                    $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
	        	}else{
	        		if(nimbus==0){
		        		errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectNodeAs']+' Nimbus node.';
	                    errorCount++;
	                    var divId='nodeTableDiv';
	                    $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		        	}
		        	 if($('#nodeList .supervisorCheck:checked').length==0){
		            		errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectNodeAs']+' Supervisor node.';
		                    errorCount++;
		                    var divId='nodeTableDiv';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect_AsSupervisor' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            	}
		        	 if($('#nodeList .zookeeperCheck:checked').length==0){
		            		errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectNodeAs']+' Zookeeper node.';
		                    errorCount++;
		                    var divId='nodeTableDiv';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect_AsZookeeper' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            	}
		        	 if(nimbus!=0 && $('#nodeList .supervisorCheck:checked').length!=0 && $('#nodeList .zookeeperCheck:checked').length!=0){
		        		 var zookeeperFlag=false;
			            	var nodeFlag=false;
			            	for ( var i = 0; i < nodeStatus.nodes.length; i++) {
			            		if($("#nodeCheck" + i).is(':checked')){
			            			if(!($("#supervisorCheckBox" + i).is(':checked') || $("#nimbusCheckBox" + i).is(':checked') ||$("#zookeeperCheckBox" + i).is(':checked'))){
			            				nodeFlag=true;	
			            				errorMsg='Mark node '+nodeStatus.nodes[i][0]+' as either Nimbus or Supervisor or Zookeeper.';
			            			}
			            		}
			            		if(nodeFlag){
				                    errorCount++;
				                    nodeFlag=false;
				                    var divId='nodeTableDiv';
				                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect_Type' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
				            	}
			            	}
			            	for ( var i = 0; i < nodeStatus.nodes.length; i++) {
			            		if($("#nodeCheck" + i).is(':checked')){
			            			if(!($("#supervisorCheckBox" + i).is(':checked') || $("#nimbusCheckBox" + i).is(':checked') ||$("#zookeeperCheckBox" + i).is(':checked'))){
			            				nodeFlag=true;	
			            			}
			            		}
			        			if ($("#nodeCheck" + i).is(':checked') && $("#zookeeperCheckBox" + i).is(':checked')) {
			        				if($("#supervisorCheckBox" + i).is(':checked') || $("#nimbusCheckBox" + i).is(':checked')){
			        					zookeeperFlag=false;
			        				}else{
			        					zookeeperFlag=true;
			        				}
			        			}
			        			if(zookeeperFlag)
			        				break;
			        		}
			            	if(zookeeperFlag){
			            		errorMsg = 'Zookeeper node must be either Nimbus or Supervisor';
			                    errorCount++;
			                    var divId='nodeTableDiv';
			                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ZookeeperNodeSelect_Type' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			            	}
			            	var zookeeperNode=0;
			            	var supNode=0;
			            	for ( var i = 0; i < nodeStatus.nodes.length; i++) {
			            		if($("#nodeCheck" + i).is(':checked')){
			            			if($("#supervisorCheckBox" + i).is(':checked') ){
			            				supNode++;
			            			}
			            			if($("#zookeeperCheckBox" + i).is(':checked') ){
			            				zookeeperNode++;
			            			}
			            		}
			            	}
			            	if(zookeeperNode==0){
			            		 errorCount++;
			                    var divId='nodeTableDiv';
			                    errorMsg=com.impetus.ankush.errorMsg.errorHash['SelectOneNode']+" as Zookeeper node.";
				                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect_AsZookeeper' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
				            	
			            }
			            	if(supNode==0){
			            		 errorCount++;
				                    var divId='nodeTableDiv';
				                    errorMsg=com.impetus.ankush.errorMsg.errorHash['SelectOneNode']+" as Supervisor node.";
				                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect_AsSupervisor' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			            	} 
		        	 	}
	            	}
	            }
	        if(errorCount>0 && errorMsg!=''){
	        	if(errorCount==1){
	        		 $('#validateError').show().html(errorCount + ' Error');
	        	}else{
	        		 $('#validateError').show().html(errorCount + ' Errors');	
	        	}
	        	  $("#errorDivMain").show();
	        }else{
	        	 com.impetus.ankush.stormClusterCreation.clusterDeploy();
	        }
	},    
/*Function for cluster deployment data post*/  
   clusterDeploy:function(){
		var data={};
		data.storm = {
			nimbus : {},
			zkNodes : new Array(),
			supervisors : new Array(),
			slotsPorts : new Array(),
		};
		data.zookeeper={
				nodes : new Array(),
		};
		var slotPort=$.trim($('#supervisorPorts').val()).split(',');
		for(var i=0;i<slotPort.length;i++){
			data.storm.slotsPorts.push($.trim(slotPort[i]));
		}
	
		data["@class"]="com.impetus.ankush.storm.StormClusterConf";
		data.username=$.trim($('#commonUserName').val());
		data.password='';
		data.privateKey='';
	//	if($('input:radio[name=authenticationRadio]:checked').val()==0){
		if($("#authGroupBtn .active").data("value")==0){
			data.password=$.trim($('#commonUserPassword').val());
		}else{
			data.privateKey=uploadShareKeyFilePath;
		}
		data.clusterName=$.trim($('#clusterName').val());
		data.environment="In Premise";
		if($("#ipModeGroupBtn .active").data("value")==0){
			data.ipPattern=$.trim($('#ipRange').val());
			data.patternFile='';
		}else{
			data.patternFile=$.trim($('#pathIPFile').val());
			data.ipPattern='';
		}
		data.technology="Storm";
		var confJava={};
		if ($("#chkInstallJava").is(':checked')) {
			confJava.install=true;
			confJava.javaBundle=$.trim($('#bundlePathJava').val());	
			confJava.javaHomePath='';
		}else{
			confJava.install=false;
			confJava.javaBundle='';
			confJava.javaHomePath=$.trim($('#homePathJava').val());
		}
		data.javaConf=confJava;
		data.storm.componentVendor=$.trim($('#vendor-Storm').val());
		data.storm.componentVersion=$.trim($('#version-Storm').val());
		data.storm.installationPath=$.trim($('#installationPath-Storm').val());
		data.storm.zookeeperPort=$.trim($('#clientPort-Zookeeper').val());
		//if($('input:radio[name=stormRadio]:checked').val()==0){
		if($("#sourcePathBtnGrp .active").data("value")==0){
			data.storm.tarballUrl=$.trim($('#downloadPath-Storm').val()); 
		}else{
			data.storm.localBinaryFile=$.trim($('#localPath-Storm').val());
		}
		for ( var i = 0; i < nodeStatus.nodes.length; i++) {
			var supNode={};
			var zNodes={};
			if ($("#nodeCheck" + i).is(':checked')) {
				if ($('#nimbusCheckBox'+i).prop("checked")) {
					data.storm.nimbus.publicIp=nodeStatus.nodes[i][0];
					data.storm.nimbus.privateIp=nodeStatus.nodes[i][0];
					data.storm.nimbus.os=nodeStatus.nodes[i][4];
					data.storm.nimbus.nodeState='deploying';
					data.storm.nimbus.type='Nimbus';
				}
				if ($('#supervisorCheckBox'+i).prop("checked")) {
					supNode.publicIp=nodeStatus.nodes[i][0];
					supNode.privateIp=nodeStatus.nodes[i][0];
					supNode.os=nodeStatus.nodes[i][4];
					supNode.nodeState='deploying';
					supNode.type='Supervisor';
					data.storm.supervisors.push(supNode);
				}
				
				if ($('#zookeeperCheckBox'+i).prop("checked")) {
					zNodes.publicIp=nodeStatus.nodes[i][0];
					zNodes.privateIp=nodeStatus.nodes[i][0];
					zNodes.os=nodeStatus.nodes[i][4];
					zNodes.nodeState='deploying';
					zNodes.type='Zookeeper';
					data.storm.zkNodes.push(nodeStatus.nodes[i][0]);
					data.zookeeper.nodes.push(zNodes);
				}
			}
		}
		data.storm.localDir=$.trim($('#localDirStorm').val());
		data.storm.uiPort=$.trim($('#uiPorts').val());
		data.storm.jmxPort_Nimbus=$.trim($('#jmxPort-Nimbus').val());
		data.storm.jmxPort_Supervisor=$.trim($('#jmxPort-Supervisor').val());
		data.zookeeper.componentVendor=$.trim($('#vendor-Zookeeper').val());
		data.zookeeper.componentVersion=$.trim($('#version-Zookeeper').val());
		data.zookeeper.installationPath=$.trim($('#installationPath-Zookeeper').val());
		//if($('input:radio[name=zookeeperRadio]:checked').val()==0){
	      if($("#zookeeperPathBtnGrp .active").data("value")==0){
			data.zookeeper.tarballUrl=$.trim($('#downloadPath-Zookeeper').val()); 
		}else{
			data.zookeeper.localBinaryFile=$.trim($('#localPath-Zookeeper').val());
		}
		data.zookeeper.dataDirectory=$.trim($('#dataPath-Zookeeper').val());
		data.zookeeper.syncLimit=$.trim($('#syncLimit').val());
		data.zookeeper.initLimit=$.trim($('#initLimit').val());
		data.zookeeper.tickTime=$.trim($('#tickTime').val());
		data.zookeeper.clientPort=$.trim($('#clientPort-Zookeeper').val());
		data.zookeeper.jmxPort=$.trim($('#jmxPort-Zookeeper').val());
		var clusterId=$('#clusterDeploy').val();
		var url = baseUrl + '/cluster/create/Storm';
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
						$("#errorDivMain").append("<div class='errorLineDiv'>"+(i+1)+". <a href='#' >"+result.output.error[i]+"</a></div>");
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
		data.username = $('#commonUserName').val();
		if($("#authGroupBtn .active").data("value")==0){
			data.password=$.trim($('#commonUserPassword').val());
			data.privateKey='';
		}else{
			data.privateKey=uploadShareKeyFilePath;
			data.password='';
		}
		com.impetus.ankush.inspectNodesCall(data,id,'retrieveNodes');
	},

};
