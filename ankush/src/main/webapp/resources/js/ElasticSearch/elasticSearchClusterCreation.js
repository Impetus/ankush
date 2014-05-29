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
var uploadFilePath=null;
var uploadShareKeyFilePath=null;
var getNodeFlag=null;
var userName='/home/${user}';
var nodeStatus=null;
var errorCount=0;
var setupDetailData=null;
var uploadRackFilePath=null;
var  inspectNodeData = {};
com.impetus.ankush.elasticSearchClusterCreation = {
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
			$('#vendor-ElasticSearch').tooltip();
			$('#version-ElasticSearch').tooltip();
			$('#downloadPath-ElasticSearch').tooltip();
			$('#sourcePath-ElasticSearch').tooltip();
			$('#installationPath-ElasticSearch').tooltip();
			$('#dataPath-ElasticSearch').tooltip();
			$('#heapSize-ElasticSearch').tooltip();
			$('#localStorageNodes-ElasticSearch').tooltip();
			$('#shardIndex-ElasticSearch').tooltip();
			$('#replicaIndex-ElasticSearch').tooltip();
			$('#httpPort-ElasticSearch').tooltip();
			$('#tcpPort-ElasticSearch').tooltip();
		},

/*Function for changing version and download path on change of elasticSearch vendor dropdown*/
		vendorOnChangeElasticSearch:function(){
			$("#version-ElasticSearch").html('');
			for ( var key in defaultVlaue.output.elasticSearch.ElasticSearch.Vendors[$("#vendor-ElasticSearch").val()]){
				$("#version-ElasticSearch").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath-ElasticSearch').val(defaultVlaue.output.elasticSearch.ElasticSearch.Vendors[$("#vendor-ElasticSearch").val()][$("#version-ElasticSearch").val()].url);
		},
/*Function for changing  download path on change of version elasticSearch dropdown*/
		versionOnChangeElasticSearch:function(){
			$('#downloadPath-ElasticSearch').val(defaultVlaue.output.elasticSearch.ElasticSearch.Vendors[$("#vendor-ElasticSearch").val()][$("#version-ElasticSearch").val()].url);
		},
/*Function for changing installation and datadir path on change of user*/
		pathChange: function(){
			var user=$.trim($('#commonUserName').val());
			var installationPathElasticSearch=$.trim($('#installationPath-ElasticSearch').val()).split(userName).join('/home/'+user);
			var dataPathElasticSearch=$.trim($('#dataPath-ElasticSearch').val()).split(userName).join('/home/'+user);
			userName='/home/'+user;
			$('#installationPath-ElasticSearch').empty().val(installationPathElasticSearch);
			$('#dataPath-ElasticSearch').empty().val(dataPathElasticSearch);
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
		/*Function for browse & upload rack file*/
		rackFilepload : function() {
	        uploadRackFlag=true;
	        uploadRackFilePath=null;
	        var uploadUrl = baseUrl + '/uploadFile';
	        $('#rackFileBrowse').click();
	        $('#rackFileBrowse').change(function(){
		        $('#rackFilePath').val($('#rackFileBrowse').val());
		        com.impetus.ankush.uploadFileNew(uploadUrl,"rackFileBrowse",null,function(data){
		        var htmlObject = $(data);
		        var jsonData = JSON.parse(htmlObject.text());
		        uploadRackFilePath=jsonData.output;
		        });
		    });
	    },
	    /*Function for toggling prepend textbox on checkbox click*/
		inputPrependToggle:function(checkBox,inputBox){
			if($('#'+checkBox).is(':checked')){
				$('#'+inputBox).removeAttr('disabled').css({
					'background-color' : 'white',
				});
			}else{
				$('#'+inputBox).attr('disabled',true);	
			}
		},
/*Function for client side validations on retrieve button click*/
		validateNode:function(){
			var userName = $.trim($('#commonUserName').val());
			var password = $('#commonUserPassword').val();	
			var ipPattern= $.trim($('#ipRange').val());
			var radioVal=0;
			var radioAuth=0;
			var rackCheck=0;
			if($("#ipModeGroupBtn .active").data("value")==1){
				radioVal=1;
			}
			 if($("#authGroupBtn .active").data("value")==1){
				radioAuth=1;
			}
			 if($('#rackMapCheck').is(':checked')){
				 rackCheck=1; 
			 }
			 errorCount = 0;
		        $("#errorDivMain").html('').hide();
		        $('#validateError').html('').hide();
		     
		        if(!com.impetus.ankush.validation.empty($('#commonUserName').val())){
		            errorCount++;
		            errorMsg = 'commonUserName '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		            com.impetus.ankush.common.tooltipMsgChange('commonUserName','Username cannot be empty');
		            divId='commonUserName';
		            errorId='lblErr_commonUserName_Empty';
		            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a  id='lblErr_commonUserName_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");          
		        }else{
		            com.impetus.ankush.common.tooltipOriginal('commonUserName',' Enter Username.');
		            $('#commonUserName').removeClass('error-box');
		        }
		        if($("#authGroupBtn .active").data("value")==0){
			        if (!com.impetus.ankush.validation.empty($('#commonUserPassword').val())) {
			            errorCount++;
			            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			            com.impetus.ankush.common.tooltipMsgChange('commonUserPassword','Password cannot be empty');
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
		                com.impetus.ankush.common.tooltipMsgChange('ipRange','IP range cannot be empty');
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
		                errorMsg = 'File Path '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
		                com.impetus.ankush.common.tooltipMsgChange('pathIPFile','File path cannot be empty');
		                var divId='pathIPFile';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_FilePath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            } else {
		                com.impetus.ankush.common.tooltipOriginal('pathIPFile','Browse File.');
		                $('#pathIPFile').removeClass('error-box');
		            }
		        }
		        if($('#rackMapCheck').is(':checked')){
		        	  if (uploadRackFilePath==null) {
			                errorCount++;
			                errorMsg = 'Rack File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
			                com.impetus.ankush.common.tooltipMsgChange('rackFilePath',errorMsg);
			                var divId='rackFilePath';
			                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
			            } else {
			                com.impetus.ankush.common.tooltipOriginal('rackFilePath','Browse File.');
			                $('#rackFilePath').removeClass('error-box');
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
						com.impetus.ankush.elasticSearchClusterCreation.getNodes(result);
					};
					com.impetus.ankush.nodeRetrieve(userName,password,uploadFilePath,ipPattern,radioVal,uploadShareKeyFilePath,radioAuth,functionCall,'retrieveNodes',rackCheck,uploadRackFilePath,'inspectNodeBtn');
		        }
		},
		/*Function for populating retrieved nodes in IP table*/
		getNodes : function(result) {
			var errorNodeCount=0;
			getNodeFlag = true;
			disableNodeCount = 0;
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
												'<input id="elasticSearchCheckBox'
													+ i
													+ '" class="elasticSearchCheck inspect-node"  style="margin-right:10px;" type="checkbox">',
													nodeStatus.nodes[i][0],
													nodeStatus.nodes[i][4],
													 '<a class="" id="datacenter'
													+ i + '">'+ nodeStatus.nodes[i][5]+'</a>',
												'<a class="" id="rack'
												+ i + '">'+ nodeStatus.nodes[i][6]+'</a>',
												 '<a class="" id="cpuCount'
												+ i + '">'+nodeStatus.nodes[i][7]+'</a>',
												'<a class="" id="diskCount'
												+ i + '">'+nodeStatus.nodes[i][8]+'</a>',
												'<a class="" id="diskSize'
												+ i + '">'+nodeStatus.nodes[i][9]+' GB</a>',
												'<a class="" id="ramSize'
												+ i + '">'+nodeStatus.nodes[i][10]+' GB</a>',
												'<div><a href="##" onclick="com.impetus.ankush.elasticSearchSetupDetail.loadNodeDetailelasticSearch('
													+ i
													+ ');"><img id="navigationImg-'
													+ i
													+ '" src="'
													+ baseUrl
													+ '/public/images/icon-chevron-right.png" /></a></div>']);
										
					var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
				theNode.setAttribute('id', 'node'+ nodeStatus.nodes[i][0].split('.').join('_'));
				if (nodeStatus.nodes[i][1] != true
						|| nodeStatus.nodes[i][2] != true
						|| nodeStatus.nodes[i][3] != true) {
					rowId = nodeStatus.nodes[i][0].split('.').join('_');
					$('td', $('#node'+rowId)).addClass('error-row');
					$('#node' + rowId).addClass('error-row');
					$('#elasticSearchCheckBox' + i).attr('disabled', true);
					 $('#diskSize' + i).text('');
  					$('#ramSize' + i).text('');
					errorNodeCount++;
					disableNodeCount++;
				}else{
					$('#elasticSearchCheckBox'+i).prop("checked", true);
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
			if(disableNodeCount != nodeTableLength){
				$('#nodeCheckHead').prop("checked", true);
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
			data.username = $('#commonUserName').val();
			if($("#authGroupBtn .active").data("value")==0){
				data.password=$.trim($('#commonUserPassword').val());
				data.privateKey='';
			}else{
				data.password='';
				data.privateKey=uploadShareKeyFilePath;
			}
			com.impetus.ankush.inspectNodesCall(data,id,'retrieveNodes');
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
			            com.impetus.ankush.common.tooltipMsgChange('clusterName','Cluster Name cannot be empty');
			            var divId='clusterName';
			            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ClusterName_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			        } else if ($('#clusterName').val().trim().split(' ').length > 1) {
			            errorCount++;
			            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameBlankSpace'];
			            com.impetus.ankush.common.tooltipMsgChange('clusterName','Cluster Name cannot contain blank spaces');
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
			        	  if (!com.impetus.ankush.validation.empty($('#bundlePathJava').val())) {
			  	            errorCount++;
			  	            errorMsg = 'Java Bundle Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			  	            com.impetus.ankush.common.tooltipMsgChange('bundlePathJava','Java Bundle Path cannot be empty');
			  	            var divId='bundlePathJava';
			  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_BundlePathJava_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			  	        }else {
				            com.impetus.ankush.common.tooltipOriginal('bundlePathJava','Enter Bundle Path.');
				            $('#bundlePathJava').removeClass('error-box');
				        }
			        }else{
			        	if (!com.impetus.ankush.validation.empty($('#homePathJava').val())) {
			  	            errorCount++;
			  	            errorMsg = 'Java home Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			  	            com.impetus.ankush.common.tooltipMsgChange('homePathJava','Java home path cannot be empty');
			  	            var divId='homePathJava';
			  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_HomePathJava_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			  	        }else {
				            com.impetus.ankush.common.tooltipOriginal('homePathJava','Enter Java Home Path.');
				            $('#homePathJava').removeClass('error-box');
				        }
			        }
			        if (!com.impetus.ankush.validation.empty($('#commonUserName').val())) {
		  	            errorCount++;
		  	            errorMsg = 'User Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		  	            com.impetus.ankush.common.tooltipMsgChange('commonUserName','User name cannot be empty');
		  	            var divId='commonUserName';
		  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_UserName_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		  	        }else {
			            com.impetus.ankush.common.tooltipOriginal('commonUserName','Enter user name.');
			            $('#commonUserName').removeClass('error-box');
			        }
			        if($("#authGroupBtn .active").data("value")==0){
				        if (!com.impetus.ankush.validation.empty($('#commonUserPassword').val())) {
				            errorCount++;
				            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
				            com.impetus.ankush.common.tooltipMsgChange('commonUserPassword','Password cannot be empty');
				            var divId='commonUserPassword';
				            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_Password_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
				      
				        }else{
				            com.impetus.ankush.common.tooltipOriginal('commonUserPassword','Enter password.');
				            $('#commonUserPassword').removeClass('error-box');
				        }
			        }else{
			        	  if (uploadShareKeyFilePath==null ){
			        		  errorCount++;
			                  errorMsg = 'Share Key '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload']; 
			                  var divId='pathSharedKey';
			                  $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ShareKeyFile_NotUploaded' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			        	  }else{
			                  com.impetus.ankush.common.tooltipOriginal('pathSharedKey','Upload File.');
			                  $('#pathSharedKey').removeClass('error-box');
			        	  }
			        }
			        if($('#rackMapCheck').is(':checked')){
			        	  if (uploadRackFilePath==null && nodeStatus!=null && nodeStatus.nodes.length==0) {
				                errorCount++;
				                errorMsg = 'Rack File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
				                com.impetus.ankush.common.tooltipMsgChange('rackFilePath',errorMsg);
				                var divId='rackFilePath';
				                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
				            } else {
				                com.impetus.ankush.common.tooltipOriginal('rackFilePath','Browse File.');
				                $('#rackFilePath').removeClass('error-box');
				            }
			        }else{
			        	  com.impetus.ankush.common.tooltipOriginal('rackFilePath','Browse File.');
			                $('#rackFilePath').removeClass('error-box');
			        }
			        if($("#ipModeGroupBtn .active").data("value")==0){
			            if(!com.impetus.ankush.validation.empty($('#ipRange').val())){
			                errorCount++;
			                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			                com.impetus.ankush.common.tooltipMsgChange('ipRange','IP range cannot be empty');
			                var divId='ipRange';
			                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_IpRange_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			            }else{
			                com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP range.');
			                $('#ipRange').removeClass('error-box');
			            }
			        }else{
			        	if (uploadFilePath == null && nodeStatus!=null && nodeStatus.nodes.length==0) {
			                errorCount++;
			                errorMsg = 'IP '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
			                com.impetus.ankush.common.tooltipMsgChange('pathIPFile','File Not Uploaded');
			                var divId='pathIPFile';
			                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_IpFile_NotUploaded' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			            } else {
			                com.impetus.ankush.common.tooltipOriginal('pathIPFile','Upload File');
			                $('#pathIPFile').removeClass('error-box');
			            }
			        }
			        if (!com.impetus.ankush.validation.empty($('#vendor-ElasticSearch').val())) {
		  	            errorCount++;
		  	            errorMsg = "ElasticSearch vendor"+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		  	            var divId='vendor-ElasticSearch';
		  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ElasticSearchVendor_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		  	        }else {
			            $('#vendor-ElasticSearch').removeClass('error-box');
			        }
			        if (!com.impetus.ankush.validation.empty($('#version-ElasticSearch').val())) {
		  	            errorCount++;
		  	          errorMsg = "ElasticSearch version"+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		  	            var divId='version-ElasticSearch';
		  	            $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ElasticSearchVersion_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		  	        }else {
			            $('#version-ElasticSearch').removeClass('error-box');
			        }
			        //if($('input[name=stormRadio]:checked').val()==0){
			        if($("#sourcePathBtnGrp .active").data("value")==0){
			            if(!com.impetus.ankush.validation.empty($('#downloadPath-ElasticSearch').val())){
			                errorCount++;
			                errorMsg = 'Download Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			                com.impetus.ankush.common.tooltipMsgChange('downloadPath-ElasticSearch','Download path cannot be empty');
			                var divId='downloadPath-ElasticSearch';
			                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_DownloadPathElasticSearch_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			            }else{
			                com.impetus.ankush.common.tooltipOriginal('downloadPath-ElasticSearch','Enter download Path');
			                $('#downloadPath-ElasticSearch').removeClass('error-box');
			            }
			        }else{
			        	if(!com.impetus.ankush.validation.empty($('#sourcePath-ElasticSearch').val())){
			                errorCount++;
			                errorMsg = 'Local path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
			                com.impetus.ankush.common.tooltipMsgChange('sourcePath-ElasticSearch','Local path cannot be empty');
			                var divId='sourcePath-ElasticSearch';
			                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_LocalPathElasticSearch_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			            } else {
			                com.impetus.ankush.common.tooltipOriginal('sourcePath-ElasticSearch','Local Path');
			                $('#sourcePath-ElasticSearch').removeClass('error-box');
			            }
			        }
			        if(!com.impetus.ankush.validation.empty($('#installationPath-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'ElasticSearch Installation Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('installationPath-ElasticSearch','Installation path cannot be empty');
		                var divId='installationPath-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ElasticSearchInstallation_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('installationPath-ElasticSearch','Enter installation  path.');
		                $('#installationPath-ElasticSearch').removeClass('error-box');
		            }
			        if(!com.impetus.ankush.validation.empty($('#dataPath-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'ElasticSearch Data Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('dataPath-ElasticSearch','Data path cannot be empty');
		                var divId='dataPath-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ElasticSearchDataPath_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('dataPath-ElasticSearch','Enter data  path.');
		                $('#dataPath-ElasticSearch').removeClass('error-box');
		            }
			        if(!com.impetus.ankush.validation.empty($('#heapSize-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'Heap size'+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('heapSize-ElasticSearch','Heap size cannot be empty');
		                var divId='heapSize-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_HeapSize_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            } else{
		                com.impetus.ankush.common.tooltipOriginal('heapSize-ElasticSearch','Enter heap size .');
		                $('#heapSize-ElasticSearch').removeClass('error-box');
		            }
			        if(!com.impetus.ankush.validation.empty($('#localStorageNodes-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'Local storage  nodes'+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('localStorageNodes-ElasticSearch','Local storage nodes cannot be empty');
		                var divId='localStorageNodes-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_LocalStorageNodes_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else if (!com.impetus.ankush.validation.numeric($('#localStorageNodes-ElasticSearch').val())) {
		                errorCount++;
		                errorMsg = 'Local Storage Nodes '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
		                com.impetus.ankush.common.tooltipMsgChange('localStorageNodes-ElasticSearch','Local storge nodes must be numeric');
		                var divId='localStorageNodes-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_LocalStorageNodes_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('localStorageNodes-ElasticSearch','Enter local storage nodes.');
		                $('#localStorageNodes-ElasticSearch').removeClass('error-box');
		            }
			        if(!com.impetus.ankush.validation.empty($('#shardIndex-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'Shard Index '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('shardIndex-ElasticSearch','Shard Index cannot be empty');
		                var divId='shardIndex-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ShardIndex_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else if (!com.impetus.ankush.validation.numeric($('#shardIndex-ElasticSearch').val())) {
		                errorCount++;
		                errorMsg = 'Shard Index '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
		                com.impetus.ankush.common.tooltipMsgChange('shardIndex-ElasticSearch','Shard Index must be numeric');
		                var divId='shardIndex-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ShardIndex_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('shardIndex-ElasticSearch','Enter shard index.');
		                $('#shardIndex-ElasticSearch').removeClass('error-box');
		            }
			        if(!com.impetus.ankush.validation.empty($('#replicaIndex-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'Replica Index  nodes'+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('replicaIndex-ElasticSearch','Replica Index cannot be empty');
		                var divId='replicaIndex-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ReplicaIndex_Empty' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else if (!com.impetus.ankush.validation.numeric($('#replicaIndex-ElasticSearch').val())) {
		                errorCount++;
		                errorMsg = 'Replica Index Nodes '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
		                com.impetus.ankush.common.tooltipMsgChange('replicaIndex-ElasticSearch','Replica Index must be numeric');
		                var divId='replicaIndex-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_ReplicaIndex_Numeric' href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('replicaIndex-ElasticSearch','Enter replica index.');
		                $('#replicaIndex-ElasticSearch').removeClass('error-box');
		            }
			        
			        if(!com.impetus.ankush.validation.empty($('#httpPort-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'HTTP port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('httpPort-ElasticSearch',errorMsg);
		                var divId='httpPort-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            }else if (!com.impetus.ankush.validation.numeric($('#httpPort-ElasticSearch').val())) {
		                errorCount++;
		                errorMsg = 'HTTP port'+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
		                com.impetus.ankush.common.tooltipMsgChange('httpPort-ElasticSearch',errorMsg);
		                var divId='httpPort-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		            } else if (!com.impetus.ankush.validation.oPort($('#httpPort-ElasticSearch').val())) {
		                errorCount++;
		                errorMsg = 'HTTP Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
		                com.impetus.ankush.common.tooltipMsgChange('httpPort-ElasticSearch',errorMsg);
		                var divId='httpPort-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		            } else{
		                com.impetus.ankush.common.tooltipOriginal('httpPort-ElasticSearch','Enter HTTP port.');
		                $('#httpPort-ElasticSearch').removeClass('error-box');
		            }
			        if(!com.impetus.ankush.validation.empty($('#tcpPort-ElasticSearch').val())){
		                errorCount++;
		                errorMsg = 'TCP port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		                com.impetus.ankush.common.tooltipMsgChange('tcpPort-ElasticSearch',errorMsg);
		                var divId='tcpPort-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            }else if (!com.impetus.ankush.validation.numeric($('#tcpPort-ElasticSearch').val())) {
		                errorCount++;
		                errorMsg = 'TCP port'+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
		                com.impetus.ankush.common.tooltipMsgChange('tcpPort-ElasticSearch',errorMsg);
		                var divId='tcpPort-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		            } else if (!com.impetus.ankush.validation.oPort($('#tcpPort-ElasticSearch').val())) {
		                errorCount++;
		                errorMsg = 'TCP Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
		                com.impetus.ankush.common.tooltipMsgChange('tcpPort-ElasticSearch',errorMsg);
		                var divId='tcpPort-ElasticSearch';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		            } else{
		                com.impetus.ankush.common.tooltipOriginal('tcpPort-ElasticSearch','Enter TCP port.');
		                $('#tcpPort-ElasticSearch').removeClass('error-box');
		            }
			        if($('#clusterDeploy').val()!=''){
			    		getNodeFlag=true;
			    	}
			        if (!getNodeFlag) {
			            errorMsg = com.impetus.ankush.errorMsg.errorHash['RetrieveNode'];
			                var divId='ipRange';
			                    errorCount++;
			                $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeRetrieve' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			            }
			        else{ 
			        	if($('#nodeList .elasticSearchCheck:checked').length==0){
		            		errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectNode'];
		                    errorCount++;
		                    var divId='nodeTableDiv';
		                    $("#errorDivMain").append("<div class='errorLineDiv'>"+errorCount+". <a id='lblErr_NodeSelect' href='javascript:com.impetus.ankush.common.focusTableDiv(\""+divId+"\")'  >"+errorMsg+"</a></div>");
			        		}
			            }
			        
			        if(errorCount>0 && errorMsg!=''){
			        	 $('#validateError').show().html(errorCount + ' Error');
			        	  $("#errorDivMain").show();
			        }else{
			        	com.impetus.ankush.elasticSearchClusterCreation.clusterDeploy();
			        }
				
				
			},
		
/*Function for cluster deployment data post*/  
		   clusterDeploy:function(){
			   var data={
					   esConf:{
						  nodes:new Array(),
					   },
			   };
				data["@class"]= "com.impetus.ankush.elasticsearch.ElasticSearchClusterConf";
				data.username=$.trim($('#commonUserName').val());
				if($("#authGroupBtn .active").data("value")==0){
					data.password=$.trim($('#commonUserPassword').val());
				}else{
					data.privateKey=uploadShareKeyFilePath;
				}
				data.technology='ElasticSearch';
				data.environment="In Premise";
				data.clusterName=$.trim($('#clusterName').val());
				data.environment="In Premise";
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
				if($("#ipModeGroupBtn .active").data("value")==0){
					data.ipPattern=$.trim($('#ipRange').val());
					data.patternFile='';
				}else{
					data.patternFile=$.trim($('#pathIPFile').val());
					data.ipPattern='';
				}
				data.rackEnabled = false;
				data.rackFileName = "";
				data.esConf.componentVendor=$.trim($('#vendor-ElasticSearch').val());
				data.esConf.componentVersion=$.trim($('#version-ElasticSearch').val());
				data.esConf.installationPath=$.trim($('#installationPath-ElasticSearch').val());
				
				if($("#sourcePathBtnGrp .active").data("value")==0){
					data.esConf.tarballUrl=$.trim($('#downloadPath-ElasticSearch').val()); 	
				}else{
					data.esConf.localBinaryFile=$.trim($('#sourcePath-ElasticSearch').val()); 
				}
				data.esConf.dataPath=$.trim($('#dataPath-ElasticSearch').val());
				if($('#actionAutoCreateIndex').is(':checked')){
					data.esConf.actionAutoCreateIndex=true;	
				}else{
					data.esConf.actionAutoCreateIndex=false;
				}
					if($('#bootstrapMlockall').is(':checked')){
						data.esConf.bootstrapMlockall=true;	
					}else{
						data.esConf.bootstrapMlockall=false;
					}
				
				data.esConf.heapSize=$.trim($('#heapSize-ElasticSearch').val());
				data.esConf.nodeMaxLocalStorageNodes=$.trim($('#localStorageNodes-ElasticSearch').val());
				data.esConf.indexNumberOfShards=$.trim($('#shardIndex-ElasticSearch').val());
				data.esConf.indexNumberOfReplicas=$.trim($('#replicaIndex-ElasticSearch').val());
				data.esConf.httpPort=$.trim($('#httpPort-ElasticSearch').val());
				data.esConf.transportTcpPort=$.trim($('#tcpPort-ElasticSearch').val());
				if ($("#rackMapCheck").is(':checked')) {
					data.rackEnabled=true;
					data.rackFileName = $.trim($('#rackFilePath').val());
				}
				for ( var i = 0; i < nodeStatus.nodes.length; i++) {
					var node={};
					if ($("#elasticSearchCheckBox" + i).is(':checked')) {
						node.publicIp=nodeStatus.nodes[i][0];
						node.privateIp=nodeStatus.nodes[i][0];
						node.os=nodeStatus.nodes[i][4];
						node.datacenter=nodeStatus.nodes[i][5];
						node.rack=nodeStatus.nodes[i][6];
						node.nodeState='deploying';
						node.type='ElasticSearch';
						data.esConf.nodes.push(node);
					}
				}
				var clusterId=$('#clusterDeploy').val();
				var url = baseUrl + '/cluster/create/ElasticSearch';
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
};
