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
var uploadPathSharedKey=null;
var uploadRackFilePath=null;
var nodeStatus = null;
var disableNodeCount = 0;
var defaultValue = null;
var errorCount=0;
var getNodeFlag=false;
var setupDetailData=null;
var userName='/home/${user}';
var  inspectNodeData = {};
com.impetus.ankush.cassandraClusterCreation={
/*Function for tooltip initialization*/
		tooltipInitialize:function(){
			// tooltip
			$('#clusterName').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.clusterName);
			$('#bundlePathJava').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.oracleHome);
			$('#homePathJava').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.javaHome);
			$('#commonUserName').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.userName);
			$('#commonUserPassword').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.password);
			$('#ipRange').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
		//	$('#inputFilePath').tooltip();
			$('#vendor-Cassandra').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.vendor);
			$('#version-Cassandra').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.version);
			$('#downloadPath-Cassandra').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.downloadPath);
			$('#localPath-Cassandra').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.localPath);
			$('#installationPath-Cassandra').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.installationPath);
			$('#rpcPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.rpcPort);
			$('#pathIPFile').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
			$('#inputClusterDesc').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.clusterDescription);
			$('#storagePort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.storagePort);
			$('#pathSharedKey').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.sharedKey);
		//	$('#inputPath').tooltip();
		//	$('#inputInstallionPath').tooltip();
			$('#logDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.logDirectory);
			$('#partitionerDropDown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.partitioner);
			$('#snitchDropDown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.snitch);
			$('#dataDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.dataDirectory);
			$('#savedCachesDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.savedCacheDirectory);
			$('#commitlogDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.commitLogDirectory);
			$("[rel=tooltip]").tooltip();
		},
		

/*Function for changing version on change of vendor*/		
		vendorOnChangeCassandra:function(){
			$("#version-Cassandra").html('');
			for (var key in defaultValue.output.cassandra.vendors[$("#vendor-Cassandra").val()]){
				$("#version-Cassandra").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath-Cassandra').val(defaultValue.output.cassandra.vendors[$("#vendor-Cassandra").val()][$("#version-Cassandra").val()].downloadUrl);
		},
/*Function for changing download path on change of version*/
		versionOnChangeCassandra:function(){
			$('#downloadPath-Cassandra').val(defaultValue.output.cassandra.vendors[$("#vendor-Cassandra").val()][$("#version-Cassandra").val()].downloadUrl);
			
		},
/*Function for changing path according to the user*/
		pathChange: function(){
			var user=$.trim($('#commonUserName').val());
			var installationPath=$.trim($('#installationPath-Cassandra').val()).split(userName).join('/home/'+user);
			var logDir=$.trim($('#logDir').val()).split(userName).join('/home/'+user);
			var dataDir=$.trim($('#dataDir').val()).split(userName).join('/home/'+user);
			var savedCachesDir=$.trim($('#savedCachesDir').val()).split(userName).join('/home/'+user);
			var commitlogDir=$.trim($('#commitlogDir').val()).split(userName).join('/home/'+user);
			userName='/home/'+user;
			$('#installationPath-Cassandra').empty().val(installationPath);
			$('#logDir').empty().val(logDir);
			$('#dataDir').empty().val(dataDir);
			$('#savedCachesDir').empty().val(savedCachesDir);
			$('#commitlogDir').empty().val(commitlogDir);
		},

		
/*Function for toggling prepend textbox on checkbox click*/
		inputPrependToggle:function(checkBox,inputBox){
			if($('#'+checkBox).is(':checked')){
				$('#'+inputBox).removeAttr('disabled').css({
					'background-color' : 'white',
				});
			}else{
				$('#'+inputBox).attr('disabled',true).css({
					'background-color' : 'rgb(238, 238, 238)',
				});
			}
		},
		
/*Function for toggling between divs on radio button click*/
    toggleAuthenticate:function(radioName,div1,div2){
        if($('input:radio[name='+radioName+']:checked').val()==0){
            $('#'+div1).show();
            $('#'+div2).hide();
        }else{
            $('#'+div2).show();
            $('#'+div1).hide();
        }
    },
/*Function for browse & upload file*/
    getNodesUpload : function() {
        uploadFileFlag=true;
        uploadFilePath=null;
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
/*Function to Upload file*/
	shareKeyFileUpload : function() {
		uploadPathSharedKey=null;
		var uploadUrl = baseUrl + '/uploadFile';
		$('#fileBrowseShareKey').click();
		$('#fileBrowseShareKey').change(
		function(){
			//$('#pathSharedKey').val(document.getElementById("fileUploadSharedKey").files[0].name);
			$('#pathSharedKey').val($('#fileBrowseShareKey').val());
			com.impetus.ankush.uploadFileNew(uploadUrl,"fileBrowseShareKey",null,function(data){
				var htmlObject = $(data);
		        var jsonData = JSON.parse(htmlObject.text());
				uploadPathSharedKey= jsonData.output;
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
/*Function for client-side validation on retrieve node button*/
	validateNode:function(){
		
		var userName = $.trim($('#commonUserName').val());
		var password = $('#commonUserPassword').val();	
		var ipPattern= $.trim($('#ipRange').val());
		var radioVal=0;
		var radioAuth=0;
		var rackCheck=0;
		$('#nodeList_filter').hide();
		  if($("#ipModeGroupBtn .active").data("value")==1){
			radioVal=1;
		}
		//if($('input[name=authenticationType]:checked').val()==1){
		  if($("#authGroupBtn .active").data("value")==1){
			radioAuth=1;
		}
		 if($('#rackMapCheck').is(':checked')){
			 rackCheck=1; 
		 }
		 errorCount = 0;
	        $("#errorDivMain").html('').hide();
	        $('#validateError').html('').hide();
	        var flag = false;
	        if(!com.impetus.ankush.validation.empty($('#commonUserName').val())){
	            errorCount++;
	            errorMsg = 'Username '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	            com.impetus.ankush.common.tooltipMsgChange('commonUserName',errorMsg);
	            flag = true;
	            var divId='commonUserName';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");          
	        }else{
	        	 com.impetus.ankush.common.tooltipOriginal('commonUserName',com.impetus.ankush.tooltip.commonClusterCreation.userName);
	        }
	      //  if($('input[name=authenticationType]:checked').val()==0){
	        if($("#authGroupBtn .active").data("value")==0){
		        if (!com.impetus.ankush.validation.empty($('#commonUserPassword').val())) {
		            errorCount++;
		            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		            com.impetus.ankush.common.tooltipMsgChange('commonUserPassword',errorMsg);
		            flag = true;
		            var divId='commonUserPassword';
		            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		      
		        }else{
		        	  com.impetus.ankush.common.tooltipOriginal('commonUserPassword',com.impetus.ankush.tooltip.commonClusterCreation.password);
		        }
	        }else{
	        	  if (uploadPathSharedKey==null){
	        		  errorCount++;
	        		  errorMsg = 'Shared Key '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload']; 
	                  var divId='pathSharedKey';
	                  $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	        	  }else{
	        		  com.impetus.ankush.common.tooltipOriginal('pathSharedKey',com.impetus.ankush.tooltip.commonClusterCreation.sharedKey);
	        	  }
	        }
	        if($("#ipModeGroupBtn .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#ipRange').val())){
	                errorCount++;
	                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('ipRange',errorMsg);
	                flag = true;
	                var divId='ipRange';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	 com.impetus.ankush.common.tooltipOriginal('ipRange',com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
	            }
	        }
	        if($("#ipModeGroupBtn .active").data("value")==1){
	            if (uploadFilePath==null) {
	                errorCount++;
	                errorMsg = 'IP File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
	                com.impetus.ankush.common.tooltipMsgChange('pathIPFile',errorMsg);
	                flag = true;
	                var divId='pathIPFile';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            } else {
	            	  com.impetus.ankush.common.tooltipOriginal('pathIPFile',com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
	            }
	        }
	        
	        if($('#rackMapCheck').is(':checked')){
	        	  if (uploadRackFilePath==null) {
		                errorCount++;
		                errorMsg = 'Rack File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
		                com.impetus.ankush.common.tooltipMsgChange('rackFilePath',errorMsg);
		                flag = true;
		                var divId='rackFilePath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            } else {
		            	 com.impetus.ankush.common.tooltipOriginal('rackFilePath',com.impetus.ankush.tooltip.commonClusterCreation.rackFile);
		            }
	        }
	        if(errorCount>0 && errorMsg!=''){
	        	 $('#validateError').show().html(errorCount + ' Error');
	        	  $("#errorDivMain").show();
	        }else{
	        	$('#retrieveNodes').button();
	            $('#retrieveNodes').button('loading');
	            $("#nodeListDiv").show();
	            nodeStatus=null;
	            if(null!= setupDetailData){
	 	            setupDetailData.nodes=new Array();	
	            }
	        	var functionCall=function(result) {
					com.impetus.ankush.cassandraClusterCreation.getNodes(result);
				};
				com.impetus.ankush.nodeRetrieve(userName,password,uploadFilePath,ipPattern,radioVal,uploadPathSharedKey,radioAuth,functionCall,'retrieveNodes',rackCheck,uploadRackFilePath,'inspectNodeBtn');
	       
	        }
		
	}, 
/*Function for retriving nodes and populate on node table*/
    getNodes : function(result) {
    	getNodeFlag=false;
    	$('#errorDivMain').html('');
    	 var userName = $.trim($('#commonUserName').val());
         var password = $('#commonUserPassword').val();
         var nodeData = {};
         inspectNodeData = {};
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
                     flag = true;
                     $("#errorDivMain").append("<div class='errorLineDiv'><a href="+herfFunction+" >1. "+errorMsg+"</a></div>");                      
                     com.impetus.ankush.common.tooltipMsgChange(divId,tooltipMsg);
                     $("#errorDivMain").show();
                     $('#validateError').show().html('1 Error');
                 }else{
                	  if($("#ipModeGroupBtn .active").data("value")==1){
                		  com.impetus.ankush.common.tooltipOriginal('pathIPFile',com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
                     }else{
                    	   com.impetus.ankush.common.tooltipOriginal('ipRange',com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
                     }
                     $("#errorDivMain").hide();
                     $('#validateError').hide();  
                     }
                 for (var i = 0; i < nodeStatus.nodes.length; i++) {
                	  var addId=null;
                	  var nodeState="Yes";
                		  if(nodeStatus.nodes[i][3]==false){
                			  nodeState="No";
                		  }
                         addId = oNodeTable.fnAddData([
                                         '<input type="checkbox" name="" value=""  id="nodeCheck'
                                                 + i
                                                 + '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.cassandraClusterCreation.nodeCheckBox('+i+')" />',
                                         nodeStatus.nodes[i][0],
                                         '<input type="checkbox" name="" value=""  id="seedNodeCheck'
                                         + i
                                         + '" class="seedNodeCheckBox"/>',
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
											'<a class="editableLabel" id="vNodeCount'
                                            + i + '">'+defaultValue.output.cassandra.vNodeCount+'</a>',
										   nodeState,
                                         '<a href="##" onclick="com.impetus.ankush.cassandraSetupDetail.loadNodeDetail('
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
                       //  $('#vNodeCount'+i).removeClass('editableLabel');
                         $('#node' + rowId).addClass('error-row');
                        $('#diskSize' + i).text('');
     					$('#ramSize' + i).text('');
                         $('#nodeCheck' + i).attr('disabled', true);
                         $('#seedNodeCheck' + i).attr('disabled', true);
                         errorNodeCount++;
                         disableNodeCount++;
                     }else{
                         $('#nodeCheck'+i).prop("checked", true);
                     }
                     if(nodeStatus.nodes[i][1] != true){
                         var status='Unavailable';
                     $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeList'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
                     }else if(nodeStatus.nodes[i][2] != true){
                         var status='Unreachable';
                         $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeList'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
                     }else if(nodeStatus.nodes[i][3] != true){
                         var status='Unauthenticated';
                         $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeList'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");  
                     }
                     }
                     if(disableNodeCount != nodeTableLength){
                         $('#nodeCheckHead').prop("checked", true);
                     }
                     $('.editableLabel').editable({
                         type : 'text',
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
			data.password='';
			data.privateKey=uploadPathSharedKey;
		}
		com.impetus.ankush.inspectNodesCall(data,id,'retrieveNodes');
	},
    
    vNodeCheckToggle:function(){
    	if( $("#vNodeCheck").is(":checked")){
    		oNodeTable.fnSetColumnVis( 10, true );
    		  $('.editableLabel').editable({
                  type : 'text',
              });
    	}else{
    		oNodeTable.fnSetColumnVis( 10, false );
    	}
    },
    /*Function for check/uncheck check boxes on click on header check box*/
	checkAllNodes : function(id,nodeClass, nodeClassNext) {
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
			$('.' + nodeClassNext).attr('checked', false);
		}
	},
    /*Function for check/uncheck header check boxe according to node check box of a table & change properties accordingly*/ 
    nodeCheckBox : function(i) {
        if ($("#nodeList .nodeCheckBox:checked").length == $("#nodeList .nodeCheckBox").length - disableNodeCount) {
            $("#nodeCheckHead").prop("checked", true);
        } else {
            $("#nodeCheckHead").removeAttr("checked");
        }
        if(!$('#nodeCheck'+i).is(':checked')){
        	$("#seedNodeCheck"+i).removeAttr("checked");
        }
    },
	
/*Function for client side validations*/	
	validatePage:function(){
		  $('#validateError').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMain").html('').hide();
	        errorCount = 0;
	        if (!com.impetus.ankush.validation.empty($('#clusterName').val())) {
	            errorCount++;
	            errorMsg = 'Cluster Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	        } else if ($('#clusterName').val().trim().split(' ').length > 1) {
	            errorCount++;
	            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameBlankSpace'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	        }else if ($('#clusterName').val().length > 20) {
	            errorCount++;
	            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameLength'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	        }
	        else if (!com.impetus.ankush.validation.alphaNumericChar($('#clusterName').val())) {
	            errorCount++;
	            errorMsg = "Cluster Name "+com.impetus.ankush.errorMsg.errorHash['ClusterNameSpecialChar'];
	            com.impetus.ankush.common.tooltipMsgChange('clusterName',errorMsg);
	            var divId='clusterName';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	        }
	        else {
	        	com.impetus.ankush.common.tooltipOriginal('clusterName',com.impetus.ankush.tooltip.commonClusterCreation.clusterName);
	        }
	        if (!com.impetus.ankush.validation.empty($('#inputClusterDesc').val())) {
	            errorCount++;
	            errorMsg = 'Cluster description '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	            com.impetus.ankush.common.tooltipMsgChange('inputClusterDesc',errorMsg);
	            var divId='inputClusterDesc';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	        } else {
	        	com.impetus.ankush.common.tooltipOriginal('inputClusterDesc',com.impetus.ankush.tooltip.commonClusterCreation.clusterDescription);
	        }
	        if($('#chkInstallJava').is(':checked')){
	        	  if (!com.impetus.ankush.validation.empty($('#bundlePathJava').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Java bundle '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            com.impetus.ankush.common.tooltipMsgChange('bundlePathJava',errorMsg);
	  	            var divId='bundlePathJava';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
	  	          com.impetus.ankush.common.tooltipOriginal('bundlePathJava',com.impetus.ankush.tooltip.commonClusterCreation.oracleHome);
		        }
	        }else{
	        	if (!com.impetus.ankush.validation.empty($('#homePathJava').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Java Home '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	  	            com.impetus.ankush.common.tooltipMsgChange('homePathJava',errorMsg);
	  	            var divId='homePathJava';
	  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	  	        }else {
	  	          com.impetus.ankush.common.tooltipOriginal('homePathJava',com.impetus.ankush.tooltip.commonClusterCreation.javaHome);
		        }
	        }
	        if (!com.impetus.ankush.validation.empty($('#commonUserName').val())) {
  	            errorCount++;
  	            errorMsg = 'User name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            com.impetus.ankush.common.tooltipMsgChange('commonUserName',errorMsg);
  	            var divId='commonUserName';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	        	 com.impetus.ankush.common.tooltipOriginal('commonUserName',com.impetus.ankush.tooltip.commonClusterCreation.userName);
	        }
	    //    if($('input[name=authenticationType]:checked').val()==0){
	        if($("#authGroupBtn .active").data("value")==0){
		        if (!com.impetus.ankush.validation.empty($('#commonUserPassword').val())) {
		            errorCount++;
		            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
		            com.impetus.ankush.common.tooltipMsgChange('commonUserPassword',errorMsg);
		            var divId='commonUserPassword';
		            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
		      
		        }else{
		        	  com.impetus.ankush.common.tooltipOriginal('commonUserPassword',com.impetus.ankush.tooltip.commonClusterCreation.password);
		        }
	        }else{
	        	  /*if (uploadFilePath==null){
	        		  errorCount++;
	                  errorMsg = 'Share key file not uploaded.'; 
	                  var divId='pathIPFile';
	                  $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	        	  }else{
	                  com.impetus.ankush.common.tooltipOriginal('pathSharedKey','Upload File.');
	                  $('#pathIPFile').removeClass('error-box');
	        	  }*/
	        }
	        
	        if($("#ipModeGroupBtn .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#ipRange').val())){
	                errorCount++;
	                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('ipRange',errorMsg);
	                var divId='ipRange';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	com.impetus.ankush.common.tooltipOriginal('ipRange',com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
	            }
	        }else{
	        	if (uploadFilePath == null && nodeStatus.nodes.length==0) {
	                errorCount++;
	                errorMsg = 'IP File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
	                com.impetus.ankush.common.tooltipMsgChange('pathIPFile',errorMsg);
	                var divId='pathIPFile';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            } else {
	            	 com.impetus.ankush.common.tooltipOriginal('pathIPFile',com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
	            }
	        }
	        if($('#rackMapCheck').is(':checked')){
	        	  if (uploadRackFilePath==null && nodeStatus.nodes.length==0) {
		                errorCount++;
		                errorMsg = 'Rack File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
		                com.impetus.ankush.common.tooltipMsgChange('rackFilePath',errorMsg);
		                var divId='rackFilePath';
		                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
		            } else {
		            	 com.impetus.ankush.common.tooltipOriginal('rackFilePath',com.impetus.ankush.tooltip.commonClusterCreation.rackFile);
		            }
	        }else{
	        	 com.impetus.ankush.common.tooltipOriginal('rackFilePath',com.impetus.ankush.tooltip.commonClusterCreation.rackFile);
	        }
	        if (!com.impetus.ankush.validation.empty($('#vendor-Cassandra').val())) {
  	            errorCount++;
  	            errorMsg = "Cassandra vendor"+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='vendor-Cassandra';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
	            $('#vendor-Cassandra').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#version-Cassandra').val())) {
  	            errorCount++;
  	            errorMsg = "Cassandra version"+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='version-Cassandra';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
	            $('#version-Cassandra').removeClass('error-box');
	        }
	      //  if($('input[name=bundlePathRadio]:checked').val()==0){
	        if($("#sourcePathBtnGrp .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#downloadPath-Cassandra').val())){
	                errorCount++;
	                errorMsg = 'Download Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('downloadPath-Cassandra',errorMsg);
	                var divId='downloadPath-Cassandra';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	 com.impetus.ankush.common.tooltipOriginal('downloadPath-Cassandra',com.impetus.ankush.tooltip.cassandraClusterCreation.downloadPath);
	            }
	        }else{
	        	if(!com.impetus.ankush.validation.empty($('#localPath-Cassandra').val())){
	                errorCount++;
	                errorMsg = 'Local path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	                com.impetus.ankush.common.tooltipMsgChange('localPath-Cassandra',errorMsg);
	                var divId='localPath-Cassandra';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('localPath-Cassandra',com.impetus.ankush.tooltip.cassandraClusterCreation.localPath);
	            }
	        }
	        if (!com.impetus.ankush.validation.empty($('#installationPath-Cassandra').val())) {
  	            errorCount++;
  	            errorMsg = 'Installation path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            com.impetus.ankush.common.tooltipMsgChange('installationPath-Cassandra',errorMsg);
  	            var divId='installationPath-Cassandra';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('installationPath-Cassandra',com.impetus.ankush.tooltip.cassandraClusterCreation.installationPath);
	        }
	        if (!com.impetus.ankush.validation.empty($('#partitionerDropDown').val())) {
  	            errorCount++;
  	            errorMsg = 'Partitioner'+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='partitionerDropDown';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
	            $('#partitionerDropDown').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#snitchDropDown').val())) {
  	            errorCount++;
  	            errorMsg = 'Snitch '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            var divId='snitchDropDown';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
	            $('#snitchDropDown').removeClass('error-box');
	        }
	        if(!com.impetus.ankush.validation.empty($('#rpcPort').val())){
                errorCount++;
                errorMsg = 'RPC port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('rpcPort',errorMsg);
                var divId='rpcPort';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#rpcPort').val())) {
                errorCount++;
                errorMsg = 'RPC port'+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('rpcPort',errorMsg);
                var divId='rpcPort';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else if (!com.impetus.ankush.validation.oPort($('#rpcPort').val())) {
                errorCount++;
                errorMsg = 'RPC Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                com.impetus.ankush.common.tooltipMsgChange('rpcPort',errorMsg);
                var divId='rpcPort';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else{
            	 com.impetus.ankush.common.tooltipOriginal('rpcPort',com.impetus.ankush.tooltip.cassandraClusterCreation.rpcPort);
            }
	        if(!com.impetus.ankush.validation.empty($('#storagePort').val())){
                errorCount++;
                errorMsg = 'Storage port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('storagePort',errorMsg);
                var divId='storagePort';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#storagePort').val())) {
                errorCount++;
                errorMsg = 'Storage port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                com.impetus.ankush.common.tooltipMsgChange('storagePort',errorMsg);
                var divId='storagePort';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else if (!com.impetus.ankush.validation.oPort($('#storagePort').val())) {
                errorCount++;
                errorMsg = 'Storage '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                com.impetus.ankush.common.tooltipMsgChange('storagePort',errorMsg);
                var divId='storagePort';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else{
            	 com.impetus.ankush.common.tooltipOriginal('storagePort',com.impetus.ankush.tooltip.cassandraClusterCreation.storagePort);
            }
	        if (!com.impetus.ankush.validation.empty($('#dataDir').val())) {
  	            errorCount++;
  	            errorMsg = 'Data directory '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            com.impetus.ankush.common.tooltipMsgChange('dataDir',errorMsg);
  	            var divId='dataDir';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('dataDir',com.impetus.ankush.tooltip.cassandraClusterCreation.dataDirectory);
	        }
	        if (!com.impetus.ankush.validation.empty($('#logDir').val())) {
  	            errorCount++;
  	            errorMsg = 'Log directory '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            com.impetus.ankush.common.tooltipMsgChange('logDir',errorMsg);
  	            var divId='logDir';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('logDir',com.impetus.ankush.tooltip.cassandraClusterCreation.logDirectory);
	        }
	        
	        if (!com.impetus.ankush.validation.empty($('#savedCachesDir').val())) {
  	            errorCount++;
  	            errorMsg = 'Saved cache directory '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            com.impetus.ankush.common.tooltipMsgChange('savedCachesDir',errorMsg);
  	            var divId='savedCachesDir';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('savedCachesDir',com.impetus.ankush.tooltip.cassandraClusterCreation.savedCacheDirectory);
	        }
	        if (!com.impetus.ankush.validation.empty($('#commitlogDir').val())) {
  	            errorCount++;
  	            errorMsg = 'Commit log directory '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
  	            com.impetus.ankush.common.tooltipMsgChange('commitlogDir',errorMsg);
  	            var divId='commitlogDir';
  	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	        	com.impetus.ankush.common.tooltipOriginal('commitlogDir',com.impetus.ankush.tooltip.cassandraClusterCreation.commitLogDirectory);
	            $('#commitlogDir').removeClass('error-box');
	        }
	        if($('#clusterDeploy').val()!=''){
	    		getNodeFlag=true;
	    	}
	        var seedNode=0;
	        if (!getNodeFlag) {
	            errorMsg = com.impetus.ankush.errorMsg.errorHash['RetrieveNode'];
	               var divId='ipRange';
	               errorCount++;
	               $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            }else{
	            	for ( var i = 0; i < nodeStatus.nodes.length; i++) {
		        		if($("#nodeCheck" + i).is(':checked')){
		        			if($("#seedNodeCheck" + i).is(':checked') ){
		        				seedNode++;
		        			}
		        		}
		        	}
	            	if($('#nodeList .nodeCheckBox:checked').length==0){
	            		errorMsg =  com.impetus.ankush.errorMsg.errorHash['SelectNode'];
	                    errorCount++;
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeList' >"+errorCount+". "+errorMsg+"</a></div>");
	            	}else if(seedNode==0){
	            		errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectOneNode']+'as seed node.';
	            		errorCount++;
	            		$("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeList' >"+errorCount+". "+errorMsg+"</a></div>");
	            	}
            }
	    
        	
	        if(errorCount>0 && errorMsg!=''){
	        	 $('#validateError').show().html(errorCount + ' Error');
	        	  $("#errorDivMain").show();
	        }else{
	        	 com.impetus.ankush.cassandraClusterCreation.clusterDeploy();
	        }
	},
/*Function for deployment data post*/
    clusterDeploy:function(){
    	var data={
    			cassandra:{
    				nodes:new Array(),
    			}
    	};
    	var confJava={};
    	data.clusterName=$.trim($('#clusterName').val());
    	data.description=$.trim($('#inputClusterDesc').val());
		data.technology='Cassandra';
		data.environment='In Premise';
		data.username=$.trim($('#commonUserName').val());
		data.password='';
		data.privateKey='';
		if($("#authGroupBtn .active").data("value")==0){
			data.password=$.trim($('#commonUserPassword').val());
		}else{
			data.privateKey=uploadPathSharedKey;
		}
		  if($("#ipModeGroupBtn .active").data("value")==0){
			data.ipPattern=$.trim($('#ipRange').val());
			data.patternFile='';
		}else{
			data.ipPattern='';
			data.patternFile=$.trim($('#pathIPFile').val());
		}
		data.cassandra.installationPath=$.trim($('#installationPath-Cassandra').val());
		data.cassandra.componentVendor=$('#vendor-Cassandra').val();
		data.cassandra.componentVersion=$('#version-Cassandra').val();
		if ($("#chkInstallJava").is(':checked')) {
			confJava.install=true;
			confJava.javaBundle=$.trim($('#bundlePathJava').val());	    			
		}else{
			confJava.install=false;
			confJava.javaHomePath=$.trim($('#homePathJava').val());	
		}
		data.javaConf=confJava;
		data.cassandra.serverTarballLocation='';
	//	if($('input:radio[name=bundlePathRadio]:checked').val()==0){
		  if($("#sourcePathBtnGrp .active").data("value")==0){
			data.cassandra.tarballUrl=$.trim($('#downloadPath-Cassandra').val());
		}else{
			data.cassandra.localBinaryFile=$.trim($('#localPath-Cassandra').val());
		}
		data["@class"]="com.impetus.ankush.cassandra.CassandraClusterConf";
		data.cassandra.partitioner=defaultValue.output.cassandra.partitioners[$('#partitionerDropDown').val()];
		data.cassandra.snitch=$('#snitchDropDown').val();
		data.cassandra.rpcPort=$.trim($('#rpcPort').val());
		data.cassandra.storagePort=$.trim($('#storagePort').val());
		data.cassandra.dataDir=$.trim($('#dataDir').val());
		data.cassandra.logDir=$.trim($('#logDir').val());
		data.cassandra.savedCachesDir=$.trim($('#savedCachesDir').val());
		data.cassandra.commitlogDir=$.trim($('#commitlogDir').val());
		data.rackEnabled = false;
		data.rackFileName = "";
		data.cassandra.vNodeEnabled = false;
		if ($("#vNodeCheck").is(':checked')) {
			data.cassandra.vNodeEnabled = true;
		}
		if ($("#rackMapCheck").is(':checked')) {
			data.rackEnabled=true;
			data.rackFileName = $.trim($('#rackFilePath').val());
		}
		for ( var i = 0; i < nodeStatus.nodes.length; i++) {
			var nodeData={};
			if ($("#nodeCheck" + i).is(':checked')) {
				nodeData.vNodeCount=0;
				if ($("#vNodeCheck").is(':checked')) {
					nodeData.vNodeCount=$('#vNodeCount'+i).text().trim();
				}
				nodeData.os=nodeStatus.nodes[i][4];
		    	nodeData.privateIp=nodeStatus.nodes[i][0];
		    	nodeData.publicIp=nodeStatus.nodes[i][0];
		    	nodeData.datacenter=nodeStatus.nodes[i][5];
		    	nodeData.rack=nodeStatus.nodes[i][6];
				if ($('#seedNodeCheck'+i).prop("checked")) {
			    	nodeData.seedNode=true;
			    	nodeData.type='CassandraSeed';
				}else{
			    	nodeData.seedNode=false;
			    	nodeData.type='CassandraNonSeed';
				}
				data.cassandra.nodes.push(nodeData);
			}
		}
		var clusterId=$('#clusterDeploy').val();
		var url = baseUrl + '/cluster/create/Cassandra';
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
		

