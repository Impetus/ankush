/*******************************************************************************
*  ===========================================================
*  Ankush : Big Data Cluster Management Solution
*  ===========================================================
*  
*  (C) Copyright 2014, by Impetus Technologies
*  
*  This is free software; you can redistribute it and/or modify it under
*  the terms of the GNU Lesser General Public License (LGPL v3) as
*  published by the Free Software Foundation;
*  
*  This software is distributed in the hope that it will be useful, but
*  WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*  See the GNU Lesser General Public License for more details.
*  
*  You should have received a copy of the GNU Lesser General Public License 
*  along with this software; if not, write to the Free Software Foundation, 
* Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*******************************************************************************/
var nodeRoleArray=new Array();
var data={
		components:{
		}
};
var nodeRole={
		'ip':'',
		'role':{},
		'roles':{},
};
var clusterTechnology="";
var technologyTable=null;
var uploadPathSharedKey=null;
var jsonDataHybrid=null;
var userName='/home/${user}';
var agentPath='/home/${user}';
var techArray=new Array();
var errorCount=0;
var nodeStatus=null;
var nodesTable=null;
var	zookeeperObj=null;
var zookeeperNodeObj=new Array();
var zookeeperNodesMap = {};
var zookeeperNodes=new Array();
var zookeeperNodeTable=null;
var	zookeeperObjReg=null;
var cassandraNodeTable=null;
var cassandraNodeMap={};
var cassandraNodesObjReg={};
var cassandraNodesObj={
		nodes:new Array(),
	};
var cassandraObj={
		Defaults:{},
};
var cassandraObjReg={
		Defaults:{},
};
var gangliaNodeTable=null;
var gangliaNodeMap={};
var gangliaNodesObj={
		gmetadNode:{},
};
var gangliaObj={
		Defaults:{},
};
var gangliaObjReg={
		Defaults:{
		},
};
var hadoopNodeMap={};
var hadoopNodesObj={
		NameNode:{},
		SecondaryNameNode:{},
		slaves:new Array(),
		clusterNodeConfs:new Array(),
		ResourceManager:{},
		JobTracker:{},
		JobHistoryServer:{},
		WebAppProxyServer:{},
		standByNamenode:{},
		JournalNode:new Array,
};
var hadoopObj={
		Defaults:{},
};
var disableNodeCount = 0;
var ensembleIdMap={};
ensembleIdMap.Zookeeper_default="default";
var zookeeperId=0;
var zookeeperObjMap={};
var setupDetailData=null;
var  inspectNodeData = {};
var enabledNodes=new Array();
var uploadFilePath=null;
var saModule=new Array();
var repoPath="";
var sudoFlag=true;
com.impetus.ankush.hybridClusterCreation={
		tooltipInitialize:function(){
			// tooltip
			$('#inputClusterName').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.clusterName);
			$('#inputJavaHome').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.javaHome);
			$('#inputUserName').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.userName);
			$('#inputPassword').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.password);
			$('#agentInstallDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.agentInstallDir);
			$('#inputIpRange').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
			$('#filePath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
			$('#filePath_Rackfile').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.rackFile);
			$('#filePathShareKey').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.commonClusterCreation.sharedKey);
			$('.section-body input:eq(0)').focus();
			com.impetus.ankush.hybridClusterCreation.populateOracleJava();
		},
		/*Function for uploading Oracle java and populate it to dropdown*/
		javaDbPackageUpload : function() {  
			var uploadUrl = baseUrl + '/uploadFile?category=bundle';
			$('#fileBrowseDbJava').click();
			$('#fileBrowseDbJava').change(
			function(){
				var obj={
					'uploadBtn': 'filePathDbJava',
					'otherBtn' : 'deployButton'
				};
				com.impetus.ankush.uploadFileNew(uploadUrl,"fileBrowseDbJava",obj,function(abcd){
					var selectFile=$("#fileBrowseDbJava").val();
				com.impetus.ankush.hybridClusterCreation.populateOracleJava();
				$("#oracleBundleFilePath").val(selectFile);
				});
			});
		},
/*Function for populating oracle java in selectbox*/
		populateOracleJava : function() {
			$('#oracleBundleFilePath').html('');
			var url = baseUrl + '/list/files';
			var data = {
				"category" : "repo",
				"pattern" : "jdk-.*"
			};
			 com.impetus.ankush.placeAjaxCall(url, "POST", false, data, function(result) {
					repoPath=result.output.path;
					for ( var i = 0; i < result.output.files.length; i++)
						$("#oracleBundleFilePath").append("<option value=\"" + result.output.files[i] + "\">" + result.output.files[i] + "</option>");
			});
		},
		
		checkBoxToggle:function(id,div1,div2){
			if($('#'+id).is(':checked')){
				$('#'+div1).removeAttr('disabled');
				$('#'+div2).attr('disabled','disabled');
			}else{
				$('#'+div1).attr('disabled','disabled');
				$('#'+div2).removeAttr('disabled');
			}
		},
		buttonClick:function(div1,div2){
			$('#'+div2).hide();
			$('#'+div1).show();
		},
		 deleteClusterDialog : function(){
			 	$("#deleteClusterDialogDeploy").appendTo('body').modal('show');
	            $("#passForDelete").val('');
	            $("#passForDeleteError").text('');
	    	  	$("#passForDelete").removeClass('error-box');
	            $('.ui-dialog-titlebar').hide();
	            $('.ui-dialog :button').blur();
	            $('#deleteClusterDialogDeploy').on('shown.bs.modal', function () {
						$('input:password:visible:first', this).focus();
					 });
	        },
		 deleteCluster : function() {
	    	  	if(!com.impetus.ankush.validate.empty($("#passForDelete").val())){
	    	  		$("#passForDeleteError").text("Password must not be empty.").addClass('text-error');
	            	$("#passForDelete").addClass('error-box');
	            	return;
	    	  	}
	    	  	currentClusterId=$("#clusterDeploy").val();
	            if (currentClusterId == null || currentClusterId == "") {
	                return;
	            }else {
	            	$("#confirmDeleteButtonDeployDialog").button('loading');
	                var dataParam = {
	                		"password" : $("#passForDelete").val()
	                };
	                var deleteUrl = baseUrl + '/cluster/removemixcluster/' + currentClusterId;
	                com.impetus.ankush.placeAjaxCall(deleteUrl,'DELETE',true,dataParam,function(result){
	                		$("#confirmDeleteButtonDeployDialog").button('reset');
	                		if (result.output.status) {
	                			$('#deleteClusterDialogDeploy').modal('hide');
	                			$(location).attr('href',(baseUrl + '/dashboard'));
	                        } else{
	                        	$("#passForDeleteError").text(result.output.error).addClass('text-error');
	                        	$("#passForDelete").addClass('error-box');
	                        }
	               });
	            }
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

		roleStringConvert:function(roleArray){
			var string="";
			for (var key in roleArray){
				if(roleArray[key].length>0){
					if(string==""){
						string=roleArray[key].join('/');
					}else{
						string=string+'/'+roleArray[key].join('/');		
					}
				}
			}
			return string;
		},
	    nodeDataClean: function(){
	    	gangliaNodesObj.gmetadNode={};
			gangliaNodeMap={};
			cassandraNodeMap={};
			hadoopNodeMap={};
			zookeeperNodesMap = {};
			zookeeperNodeObj=new Array();
			cassandraNodesObj.nodes=new Array();
			cassandraNodesObjReg={};
			hadoopNodesObj.NameNode={};
			hadoopNodesObj.SecondaryNameNode={};
			hadoopNodesObj.slaves=new Array();
			hadoopNodesObj.ResourceManager={};
			hadoopNodesObj.JobTracker={};
			hadoopNodesObj.JobHistoryServer={};
			hadoopNodesObj.WebAppProxyServer={};
			hadoopNodesObj.standByNamenode={};
			hadoopNodesObj.JournalNode=new Array();
			hadoopNodesObj.clusterNodeConfs=new Array();
			enabledNodes=new Array();
			$('.techNodeCount').text('0');
			$('.nodeMap').each(function(){
				var btnId=this.id;
				btn=btnId.split("nodeMap").join("nodeCheck");
				if($("#"+btn).is(':checked')){
					$("#"+btnId).addClass('btn-danger');
				}
			});
			$('.nodeRoleIp').text('');
			if(setupDetailData!=null){
				
				for ( var key in setupDetailData.components) {
					if(key.split("_")[0]=="Zookeeper"){
						if(undefined != setupDetailData.components[key]){
							setupDetailData.components[key].nodes=new Array();		
						}
					}
				}
				if(undefined != setupDetailData.components.Cassandra){
					setupDetailData.components.Cassandra.nodes=new Array();
				}
				
				if(undefined != setupDetailData.components.Ganglia){
					setupDetailData.components.Ganglia.gmetadNode={};
				}
				if(undefined != setupDetailData.components.Hadoop){
					setupDetailData.components.Hadoop.NameNode={};
					setupDetailData.components.Hadoop.SecondaryNameNode={};
					setupDetailData.components.Hadoop.ResourceManager={};
					setupDetailData.components.Hadoop.JobTracker={};
					setupDetailData.components.Hadoop.JobHistoryServer={};
					setupDetailData.components.Hadoop.slaves=new Array();
					setupDetailData.components.Hadoop.WebAppProxyServer={};
					setupDetailData.components.Hadoop.standByNamenode="";
					setupDetailData.components.Hadoop.JournalNode=new Array();
				}
			}
	    },
		//function for cleaning data
		flushDetectNodesData:function(){
			nodeRoleArray=new Array();
			com.impetus.ankush.hybridClusterCreation.nodeDataClean();
			
		},
		/*Function for client side validations on retrieve button click*/
		validateNode:function(key){
			var userName = $.trim($('#inputUserName').val());
			var password = $('#inputPassword').val();	
			var ipPattern= $.trim($('#inputIpRange').val());
			var agentpath=$.trim($('#agentInstallDir').val());
			 errorCount = 0;
		        $("#errorDivMain").html('').hide();
		        $('#validateError').html('').hide();
		        if(!com.impetus.ankush.validation.empty(userName)){
		            errorCount++;
		            errorMsg = 'Username field empty.';
		            com.impetus.ankush.common.tooltipMsgChange('inputUserName','Username cannot be empty');
		            var divId='inputUserName';
		            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        }else{
		            com.impetus.ankush.common.tooltipOriginal('inputUserName',com.impetus.ankush.tooltip.commonClusterCreation.userName);
		        }
		        if($("#authGroupBtn .active").data("value")==0){
			        if (!com.impetus.ankush.validation.empty(password)) {
			            errorCount++;
			            errorMsg = 'Password field empty.';
			            com.impetus.ankush.common.tooltipMsgChange('inputPassword','Password cannot be empty');
			            var divId='inputPassword';
			            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
			      
			        }else{
			            com.impetus.ankush.common.tooltipOriginal('inputPassword',com.impetus.ankush.tooltip.commonClusterCreation.password);
			        }
		        }else{
		        	  if (uploadPathSharedKey==null){
		        		  errorCount++;
		                  errorMsg = 'Shared Key field empty.'; 
		                  var divId='filePathShareKey';
		                  com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        	  }else{
		                  com.impetus.ankush.common.tooltipOriginal('filePathShareKey',com.impetus.ankush.tooltip.commonClusterCreation.sharedKey);
		        	  }
		        }
		        if($("#ipModeGroupBtn .active").data("value")==0){
		            if(!com.impetus.ankush.validation.empty(ipPattern)){
		                errorCount++;
		                errorMsg = 'Host Name field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('inputIpRange','Host Name cannot be empty');
		                var divId='inputIpRange';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('inputIpRange',com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
		            }
		        }
		        if($("#ipModeGroupBtn .active").data("value")==1){
		            if (!com.impetus.ankush.validation.empty($('#filePath').val())) {
		                errorCount++;
		                errorMsg = 'File path field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('filePath','File path cannot be empty');
		                var divId='filePath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		            } else {
		                com.impetus.ankush.common.tooltipOriginal('filePath',com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
		            }
		        }
		        if(key=="inspectNodeBtn"){
			        if(!com.impetus.ankush.validation.empty(agentpath)){
			            errorCount++;
			            errorMsg = 'Agent Install Dir field empty.';
			            com.impetus.ankush.common.tooltipMsgChange('agentInstallDir','Agent Install Dir cannot be empty');
			            var divId='agentInstallDir';
			            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
			        }else{
			        	  com.impetus.ankush.common.tooltipOriginal('agentInstallDir',com.impetus.ankush.tooltip.commonClusterCreation.agentInstallDir);
			        }
		        }
		        if(errorCount>0 && errorMsg!=''){
		        	 $('#validateError').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
		        	  $("#errorDivMain").show();
		        }else{
		        	if(key=="inspectNodeBtn"){
		        		com.impetus.ankush.hybridClusterCreation.inspectNodesObject(key);
		        	}else{
		        		com.impetus.ankush.hybridClusterCreation.getNodes();
		        	}
		        }
		},
		nodeRoleInitialize:function(){
			nodeRole.roles={
					"Cassandra":new Array(),
					"Ganglia":new Array(),
					"Hadoop":new Array(),
			};
			nodeRole.role={
					"Zookeeper":0,
					"CassandraSeed":0,
					"CassandraNonSeed":0,
					"NameNode":0,
					"DataNode":0,
					"ResourceManager":0,
					"NodeManager":0,
					"zkfc":0,
					"JournalNode":0,
					"JobTracker":0,
					"TaskTracker":0,
					"SecondaryNameNode":0,
					"JobHistoryServer":0,
					"WebAppProxyServer":0,
					"GangliaMaster":0,
			};
		},
		//function for node retrieving
		getNodes:function(){
				$("#errorDivMain").hide().html('');
				$('#inspectNodeBtn').attr('disabled','disabled');
				var errorNodeCount=0;
				disableNodeCount = 0;
				var tooltipMsg;
				var divId;
			 	var user= $.trim($('#inputUserName').val());
		        var password = $('#inputPassword').val();
		        var nodeData = {};
		        inspectNodeData = {};
		        enabledNodes=new Array();
		        nodeData.userName = user;
	            nodeData.password = password;
	            nodeData.nodePattern=$.trim($('#inputIpRange').val());
		        nodeData.isFileUploaded = false;
		        nodeData.authTypePassword=true;
		        if($("#ipModeGroupBtn .active").data("value")==1){
		            nodeData.isFileUploaded = true;
		            nodeData.nodePattern=uploadFilePath;
		        }
		        if($("#authGroupBtn .active").data("value")==1){
		        	  nodeData.authTypePassword=false;
		        	  nodeData.password = uploadPathSharedKey;
		        }
		        if($('#rackMapCheck').is(':checked')){
		        }
		        
		        var clusterId=$('#clusterDeploy').val();
				if(clusterId !=''){
					nodeData.clusterId = clusterId;
				}
		        var url = baseUrl + '/cluster/detectNodes';
		        com.impetus.ankush.hybridClusterCreation.flushDetectNodesData();
		        com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		        if(setupDetailData!=null){
		        	setupDetailData.nodes=new Array();
		        }
		        var l = Ladda.create(document.querySelector('#nodeRetrieveBtn'));
			 	l.start();
		        com.impetus.ankush.placeAjaxCall(url, "POST", true, nodeData, function(result) {
		        		l.stop();
                    	$('#inspectNodeBtn').removeAttr('disabled');
	                	if (nodesTable != null) {
	                         nodesTable.fnClearTable();
	                    }
                    	nodeStatus=result.output;
                    	$("#nodeListDiv").show();
                    	if($("#ipModeGroupBtn .active").data("value")==1){
            				divId='filePath';
            				tooltipMsg='Host Name is not valid in file';							
            			}else{
            				 divId='inputIpRange';
            				tooltipMsg='Host Name is not valid.';
            			}
            			if(null != nodeStatus.error && nodeStatus.error.length > 0){
            				errorMsg = nodeStatus.error[0];
            				flag = true;
            				com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,'1',errorMsg);
            				com.impetus.ankush.common.tooltipMsgChange(divId,tooltipMsg);
            				$("#errorDivMain").show();
            				 $('#validateError').show().html('Error '+"<span class='badge'>1</span>");
            			}else{
            				if($("#ipModeGroupBtn .active").data("value")==1){
            					com.impetus.ankush.common.tooltipOriginal('filePath',com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
            				}else{
            					com.impetus.ankush.common.tooltipOriginal('inputIpRange',com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
            				}
            				$("#errorDivMain").hide();
            				$('#validateError').hide();	
            				}
                    	for ( var i = 0; i < nodeStatus.nodes.length; i++) {
		    				var addId = null;
		    				nodeRole={
					        		'ip':'',
					        		'role':{},
					        		'roles':{},
					        };
		    				var nodeRight="Non-Sudo";
		    				if(nodeStatus.nodes[i][12]){
		    					nodeRight="Sudo";
		    				}
        					addId = nodesTable.fnAddData([
				                        '<input type="checkbox" class="inspect-node" style="display:none">',
										nodeStatus.nodes[i][0],
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
										'<span class="nodeRoleIp" id="nodeRole'+i+'"></span>',
										'<span class="nodeRight" id="nodeRight'+i+'">'+nodeRight+'</span>',
										nodeStatus.nodes[i][11],
										'<div class="text-center"><a href="##" onclick="com.impetus.ankush.hybridSetupDetail.pageDetailValue('
											+ i
											+ ',this);"><i id="navigationImg-'+i+'" class=" navigationImg glyphicon glyphicon-chevron-right"></i></a></div>' ]);
            				nodeRole.ip=nodeStatus.nodes[i][0];
            				com.impetus.ankush.hybridClusterCreation.nodeRoleInitialize();
            				nodeRoleArray.push(nodeRole);
            				var theNode = nodesTable.fnSettings().aoData[addId[0]].nTr;
            				theNode.setAttribute('id', 'node'+ nodeStatus.nodes[i][0].split('.').join('_'));
            				sudoFlag=true;
            				if($("#btnNonSudoNode").hasClass("active")){
            					sudoFlag=false;
            				}
            				if (nodeStatus.nodes[i][1] != true
            						|| nodeStatus.nodes[i][2] != true
            						|| nodeStatus.nodes[i][3] != true || (sudoFlag && !nodeStatus.nodes[i][12])) {
            					rowId = nodeStatus.nodes[i][0].split('.').join('_');
            					$('td', $('#node'+rowId)).addClass('alert-danger');
            					$('#node' + rowId).addClass('alert-danger');
            					$('#diskSize' + i).text('');
            					$('#ramSize' + i).text('');
            					errorNodeCount++;
            					disableNodeCount++;
            				}else{
            					enabledNodes.push(nodeStatus.nodes[i][0]);
            				}
                    	}
                    	nodesTable.fnSetColumnVis(10, false);	
		        });
		},
		nodeRightDialogOpen:function(value){
			if(nodeStatus !=null){
				$('#nodeRightChangeDialog').modal('show');	
			}else{
				com.impetus.ankush.hybridClusterCreation.nodeRightToggle(value);
			}
		},
		nodeRightDialogCancel:function(){
			if($("#btnSudoNode").hasClass("active")){
				$("#btnNonSudoNode").addClass("active");
				$("#btnSudoNode").removeClass("active");
			}else{
				$("#btnNonSudoNode").removeClass("active");
				$("#btnSudoNode").addClass("active");
			}
			$('#nodeRightChangeDialog').modal('hide');	
		},
		nodeRightToggle:function(value){
			$("#nodeRightWarning").html('');
			var warningMessage="<strong>Non-sudo deployment will effect following:</strong>" +
					"<br>Ganglia cannot be deployed.";
			
			if(undefined== value){
				if($("#btnSudoNode").hasClass("active")){
					value='Sudo';
				}else{
					value="";
				}
			}
			if(undefined==value || value==""){
				$("#nodeRightWarning").append('<div class="alert alert-warning" role="alert">'
	            		+'<span class="warning-link" style="color:black" href="" >'+warningMessage+'</span></div>');
			}
			$('#nodeRightChangeDialog').modal('hide');
			if(value=="Sudo"){
				sudoFlag=true;
				if(null !=nodeStatus){
					for ( var i = 0; i < nodeStatus.nodes.length; i++) {
						rowId = nodeStatus.nodes[i][0].split('.').join('_');
						$('td', $('#node'+rowId)).removeClass('alert-danger');
						if (nodeStatus.nodes[i][1] != true
	    						|| nodeStatus.nodes[i][2] != true
	    						|| nodeStatus.nodes[i][3] != true || !nodeStatus.nodes[i][12]) {
							$('td', $('#node'+rowId)).addClass('alert-danger');
						}
					}
				}
			}else{
				sudoFlag=false;
				if(null !=nodeStatus){
					for ( var i = 0; i < nodeStatus.nodes.length; i++) {
						rowId = nodeStatus.nodes[i][0].split('.').join('_');
						$('td', $('#node'+rowId)).removeClass('alert-danger');
						if (nodeStatus.nodes[i][1] != true
	    						|| nodeStatus.nodes[i][2] != true
	    						|| nodeStatus.nodes[i][3] != true) {
							$('td', $('#node'+rowId)).addClass('alert-danger');
						}
					}
					
				}
				$("#nodeCheckGanglia").removeAttr("checked");
				$("#deployToggleGanglia").attr("disabled","disabled").removeClass("active");
				$("#registerToggleGanglia").attr("disabled","disabled");
				$("#nodeMapGanglia").attr("disabled","disabled").removeClass("btn-danger");
				$("#confPageGanglia").attr("disabled","disabled").removeClass("btn-danger");
				
			}
			com.impetus.ankush.hybridClusterCreation.nodeDataClean();
		},
		dynamicRowNodetable:function(nodeIp){
			$(".nodeDiv").remove();
			$("#node"+nodeIp).after('<tr class="nodeDiv"><td id="node_'+nodeIp+'" colspan="9" style="height:auto !important"></td></tr>');
		},
		
		//function for inspectnode data
		inspectNodesObject : function(id){
			var data = {};
			data.nodePorts = {};
			$('.inspect-node').each(function(){
					$(this).addClass('inspect-node-ok');
					var ip = $(this).parent().next().text();
					data.nodePorts[ip] = [];
			});
			data.agentInstallDir = $("#agentInstallDir").val();
			data.username = $("#inputUserName").val();
			if($("#authGroupBtn .active").data("value")==1){
				 data.privateKey = uploadPathSharedKey;
					data.password = '';
	        }else{
	        	data.privateKey = '';
	        	data.password = $("#inputPassword").val();
	        }
			com.impetus.ankush.inspectNodesCall(data,id,'nodeRetrieveBtn');
			nodesTable.fnSetColumnVis(10,true);
		},
		
		//function for mapping node roles and showing on node rows
		nodeRoleMap:function(colId){
			var role='';
			for ( var j = 0; j < nodeRoleArray.length; j++) {
				for(var key in nodeRoleArray[j].role){
					if(nodeRoleArray[j].role[key]==1){
						
						if(role==''){
							role=key;
						}else{
							role=role+'/'+key;
						}
					}
				}
				var ip=nodeRoleArray[j].ip.split('.').join('_');
				var rowIndex = nodesTable.fnGetPosition( $('#node'+ip).closest('tr')[0] );
				var nodeData='<span class="nodeRoleIp" id="nodeRole'+j+'">'+role+'</span>';
				nodesTable.fnUpdate( nodeData, rowIndex , 7);
				role='';
			}
		
		},
	//function for loading template data
		getTemplateData:function(techName){
			clusterTechnology=techName;
			var url = baseUrl + '/cluster/templates?technology='+clusterTechnology;
			 com.impetus.ankush.placeAjaxCall(url, "GET", false,null, function(result){
				 $('#selectTemplate').html('').append("<option value=''>Select Template</option>");
				 for(var i=0;i<result.output.length ;i++){
					 $('#selectTemplate').append('<option value="' + result.output[i].name + '">'+result.output[i].name+ '</option>');	 
				 }
			 });
		},
		getSAModule:function(techName){
			if(undefined != techName){
				com.impetus.ankush.hybridClusterCreation.getDefaultValue(techName);
				return;
			}
				com.impetus.ankush.hybridClusterCreation.getDefaultValue();
		},
		
		//function for loading default data
		getDefaultValue : function(technology,functionName) {
			jsonDataHybrid={};
				 var url = baseUrl + '/app/metadata/hybrid';
				 com.impetus.ankush.placeAjaxCall(url, "GET", false,null, function(result){
						 jsonDataHybrid.hybrid = result.output.hybrid;
						 if(undefined != technology){
							 com.impetus.ankush.hybridClusterCreation.customJsonForTechnology(technology);
						}
						 com.impetus.ankush.hybridClusterCreation.techTableCreateNew();
					  });
				 $("#agentInstallDir").val(jsonDataHybrid.hybrid.Agent.agentInstallDir);	 
		},
		customJsonForTechnology:function(technology){
			var jsonData={};
			switch (technology) {
			
			case "Hadoop":
				jsonData.Ganglia=jsonDataHybrid.hybrid.Ganglia;
				jsonData.Agent=jsonDataHybrid.hybrid.Agent;
				jsonData.Hadoop=jsonDataHybrid.hybrid.Hadoop;
				jsonData.Zookeeper_default=jsonDataHybrid.hybrid.Zookeeper_default;
				jsonDataHybrid.hybrid={};
				jsonDataHybrid.hybrid=jsonData;
				break;
			case "Cassandra":
				jsonData.Ganglia=jsonDataHybrid.hybrid.Ganglia;
				jsonData.Agent=jsonDataHybrid.hybrid.Agent;
				jsonData.Cassandra=jsonDataHybrid.hybrid.Cassandra;
				jsonDataHybrid.hybrid={};
				jsonDataHybrid.hybrid=jsonData;
				break;
		
			default:
				break;
			}
			
		},
		
		techTableCreateNew:function(){
			var result={};
			 result.output=jsonDataHybrid;	
			 var i=0;
			 $("#accordion").html("");
			 $("#accordion").append("<div class='panel panel-default' style='border:-moz-mac-accentdarkestshadow'><div class='panel-heading' style='border-bottom:none !important;content:none !important;padding-top:3px;padding-bottom:0px;' role='tab' id=''><h4 class='panel-title'>"+
					 "<div class='row'><div class='col-xs-1 col-sm-1 col-md-1'><input type='checkbox' name='' id='techNodeHead' onclick='com.impetus.ankush.hybridClusterCreation.checkAllNodes(\"techNodeHead\",\"nodeCheckBoxTech\");'/></div>" +
					 "<div class='col-md-5'><div class='col-xs-5 col-sm-3 col-md-6'>Technology</div>"+
					 "<div class='col-xs-4 col-sm-3 col-md-6'>Action</div></div>"+
					 "<div class='col-md-5'><div class='col-xs-6 col-sm-4 col-md-6'>Configuration</div>"+
                     "<div class='col-xs-6 col-sm-3 col-md-6'>Nodes</div></div></div>"+
							"	</h4></div>");
			 for(var key in result.output.hybrid){
				 if(key=="Agent"){
					 continue;
				 }
				 var tech=key;
				 var deployText="deployToggle";
				 var registerText="registerToggle";
				 btnGrpId="toggleRegisterBtn"+tech;
				 deployBtnId="deployToggle"+tech;
				 registerBtnId="registerToggle"+tech;
				 var btnGrp="<div style='padding:0px;width:150px;' data-toggle='buttons-radio' id='"+btnGrpId+"'"+
						"class='btn-group ' >"+
					"<button id='"+deployBtnId+"' type='button' class='btn btn-default active deployBtn' disabled='disabled' style='height:23px;padding-top:0px;'"+
						"data-value='1'"+
						"data-toggle='collapse' data-parent='#accordion'"+
							"	href='#collapseOne"+tech+"' aria-expanded='	'"+
							"	aria-controls='collapseOne"+tech+"' "+
						"onclick='com.impetus.ankush.hybridClusterCreation.selectToggle(\""+deployText+"\",\""+tech+"\");'>Deploy</button>"+
					"<button id='"+registerBtnId+"' type='button' class='btn btn-default registerBtn' disabled='disabled' style='height:23px;padding-top:0px;'"+
						"data-value='0'"+
						"data-toggle='collapse' data-parent='#accordion'"+
							"	href='#collapseOne"+tech+"' aria-expanded='	'"+
							"	aria-controls='collapseOne"+tech+"' "+
						"onclick='com.impetus.ankush.hybridClusterCreation.selectToggle(\""+registerText+"\",\""+tech+"\");'>Register</button>"+
				"</div>";
				 $("#accordion").append("<div class='panel panel-default' style='border:-moz-mac-accentdarkestshadow'><div class='panel-heading' style='border-bottom:none !important;content:none !important;padding-top:3px;padding-bottom:0px;' role='tab' id='headingOne"+tech+"'><h4 class='panel-title'>"+
						 "<div class='row'><div class='col-xs-1 col-sm-1 col-md-1'><input type='checkbox' name='' value='"+key+"'  id='nodeCheck"+tech+"' class='nodeCheckBoxTech' onclick='com.impetus.ankush.hybridClusterCreation.nodeCheckBox("+ i + ",\"accordion\",\"nodeCheckBoxTech\",\"techNodeHead\",\""+tech+"\")'/></div>" +
						 "<div class='col-md-5'><div class='col-xs-5 col-sm-3 col-md-6'><label id='techLabel"+key+"'>"+tech+"</label></div>"+
						 "<div class='col-xs-4 col-sm-3 col-md-6'>"+btnGrp+"</div></div>"+
						 "<div class='col-md-5'><div class='col-xs-6 col-sm-4 col-md-6'><label id='confType"+tech+"' class='' style='min-width:60px;'>"+result.output.hybrid[key].Defaults.confState+"</label><button data-toggle='collapse' data-parent='#accordion'"+
								"	href='#collapseOne"+tech+"' aria-expanded='	'"+
								"	aria-controls='collapseOne"+tech+"'  style='height:23px;padding-top:0px;' disabled='disabled' class='btn btn-default confTech'  id='confPage"+tech+"' href='###' onclick='com.impetus.ankush.hybridSetupDetail.loadConfigPage(\""+key+"\")'>Configure</button></div>"+
                         "<div class='col-xs-6 col-sm-3 col-md-6'><span class='techNodeCount mrgr10' id='nodeCount"+tech+"'>0</span><button   data-toggle='collapse' data-parent='#accordion'"+
								"	href='#collapseOne"+tech+"' aria-expanded='	'"+
								"	aria-controls='collapseOne"+tech+"' style='height:23px;padding-top:0px;' disabled='disabled' class='btn btn-default nodeMap' id='nodeMap"+tech+"' href='###' onclick='com.impetus.ankush.hybridSetupDetail.loadMapNodePage(\""+key+"\")'>Map Nodes</button></div></div></div>	"+
								"	</h4></div><div id='collapseOne"+tech+"' style=';' class='panel-collapse collapse pageDiv'"+
						"role='tabpanel' aria-labelledby='headingOne"+tech+"'>"+
						"<div class='panel-body pageDiv' id='techPage"+tech+"'></div></div>");
				 $("#registerToggleCassandra").remove();
				 if(key=='Zookeeper_default'){
					 $("#techLabelZookeeper_default").text("Zookeeper");
				 }
				 if(key=='Ganglia'){
					 $("#nodeCheckGanglia").attr("checked","checked");
					 $("#confPageGanglia").removeAttr("disabled").addClass("btn-danger");
					 $("#nodeMapGanglia").removeAttr("disabled").addClass("btn-danger");
					 $("#registerToggleGanglia").removeAttr("disabled");
					 $("#deployToggleGanglia").removeAttr("disabled");
				 }
			 }
			
		},
	
		techTableUpdate:function(){
			var result={};
			result.output={
					hybrid:{}
			};
			 var zookeeperData=new Array();
			for ( var key in setupDetailData.components) {
				if(key.split("_")[0]=="Zookeeper"){
					setupDetailData.components[key].ensembleId=key;
					zookeeperData.push(setupDetailData.components[key]);
				}
			}
			for(var list=0;list<zookeeperData.length;list++){
				 ensembleIdMap[zookeeperData[list].ensembleId]=zookeeperData[list].ensembleId.split("Zookeeper_")[1];
				if(zookeeperData[list].ensembleId!="Zookeeper_default"){
					result.output.hybrid[zookeeperData[list].ensembleId]=zookeeperData[list];	
				}
			}
			for(var key in ensembleIdMap){
				zookeeperId=ensembleIdMap[key];
			}
			var i = $('#technologyTable tbody tr').length;
			var tech="Zookeeper_"+zookeeperId;
			var count=0;
			var deployText="deployToggle";
			 var registerText="registerToggle";
			 btnGrpId="toggleRegisterBtn"+tech;
			 deployBtnId="deployToggle"+tech;
			 registerBtnId="registerToggle"+tech;
			 
			 var btnGrp="<div data-toggle='buttons-radio' id='"+btnGrpId+"'"+
					"class='btn-group ' style='padding:5px;'>"+
				"<button id='"+deployBtnId+"' type='button' class='btn btn-default active deployBtn' disabled='disabled' style='height:25px;'"+
					"data-value='1'"+
					"onclick='com.impetus.ankush.hybridClusterCreation.selectToggle(\""+deployText+"\",\""+tech+"\");'>Deploy</button>"+
				"<button id='"+registerBtnId+"' type='button' class='btn btn-default registerBtn' disabled='disabled' style='height:25px;'"+
					"data-value='0'"+
					"onclick='com.impetus.ankush.hybridClusterCreation.selectToggle(\""+registerText+"\",\""+tech+"\");'>Register</button>"+
			"</div>";
			 for(var key in result.output.hybrid){
				 tech=key;
				 var zooTech="Zookeeper";
				 $("#accordion").append("<div class='panel panel-default' style='border:-moz-mac-accentdarkestshadow'><div class='panel-heading' style='border-bottom:none !important;content:none !important;padding-top:3px;padding-bottom:0px;' role='tab' id='headingOne"+tech+"'><h4 class='panel-title'>"+
						 "<div class='row'><div class='col-xs-1 col-sm-1 col-md-1'><input type='checkbox' name='' value='"+tech+"'  id='nodeCheck"+tech+"' class='nodeCheckBoxTech"+zooTech+"' onclick=''/></div>" +
						 "<div class='col-md-5'><div class='col-xs-5 col-sm-3 col-md-6'><a>"+tech+"</a></div>"+
						 "<div class='col-xs-4 col-sm-3 col-md-6'>"+btnGrp+"</div></div>"+
						 "<div class='col-md-5'><div class='col-xs-6 col-sm-4 col-md-6'><label id='confType"+tech+"' class='' style='min-width:60px;'>"+jsonDataHybrid.hybrid.Zookeeper_default.Defaults.confState+"</label><button data-toggle='collapse' data-parent='#accordion'"+
								"	href='#collapseOne"+tech+"' aria-expanded='	'"+
								"	aria-controls='collapseOne"+tech+"'  style='height:23px;padding-top:0px;' class='btn btn-default confTech'  id='confPage"+tech+"' href='###' onclick='com.impetus.ankush.hybridSetupDetail.loadConfigPage(\""+zooTech+"\")'>Configure</button></div>"+
                         "<div class='col-xs-6 col-sm-3 col-md-6'><span class='techNodeCount mrgr10' id='nodeCount"+tech+"'>0</span><button   data-toggle='collapse' data-parent='#accordion'"+
								"	href='#collapseOne"+tech+"' aria-expanded='	'"+
								"	aria-controls='collapseOne"+tech+"' style='height:23px;padding-top:0px;' class='btn btn-danger nodeMap' id='nodeMap"+tech+"' href='###' onclick='com.impetus.ankush.hybridSetupDetail.loadMapNodePage(\""+zooTech+"\",\""+tech+"\")'>Map Nodes</button></div></div></div>"+
								"	</h4></div><div id='collapseOne"+tech+"' style=';' class='panel-collapse collapse pageDiv'"+
						"role='tabpanel' aria-labelledby='headingOne"+tech+"'>"+
						"<div class='panel-body pageDiv' id='techPage"+tech+"'></div></div>");
				 count++;
				 i++;
			 }
		},
		dynamicRowCreate:function(technology,tech){
			if(undefined==tech){
				tech=technology;
		    }
			key=technology;
			 if($("#collapseOne"+tech).hasClass("in") && !($("#collapseOne"+tech).hasClass("NodeMap"))){
			
			 }else{
				 com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
				$("#collapseOne"+tech).removeClass("NodeMap");
				 var id="toggleRegisterBtn"+tech;
				 var page="";
				 var pageTech=tech;
				 if(pageTech.indexOf('Zookeeper')>-1){
					 pageTech='zookeeper';
				 }
					if($("#"+id+" .active").data("value")==1){
						 page="/ankush/hybrid-cluster/"+pageTech.toLowerCase()+"Config/"+tech;	
					}else{
						page="/ankush/register-cluster/"+pageTech.toLowerCase()+"Config/"+tech;	
					}
					jQuery("#collapseOne"+tech).load(page);
					$("#collapseOne"+tech).addClass("Config");
					var target = $("#accordion");
					$('html, body').animate({
			            scrollTop: target.offset().top
			        }, 1000);
			 }
		},
		
		dynamicRowRemove:function(){
			$(".pageDiv").removeClass("in").html('');
		},
		dynamicRowCreateNodes:function(technology,tech){
			if(undefined==tech){
				tech=technology;
		    }
			key=technology; 
			
			 if($("#collapseOne"+tech).hasClass("in") && !($("#collapseOne"+tech).hasClass("Config"))){
			 }else{
				 com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
					$("#collapseOne"+tech).removeClass("Config");
				 var id="toggleRegisterBtn"+tech;
				 var page="";
				 var pageTech=tech;
				 if(pageTech.indexOf('Zookeeper')>-1){
					 pageTech='zookeeper';
				 }
					if($("#"+id+" .active").data("value")==1){
						page="/ankush/hybrid-cluster/"+pageTech.toLowerCase()+"Nodes/"+tech;	
					}else{
						page="/ankush/register-cluster/"+pageTech.toLowerCase()+"Nodes/"+tech;	
					}
					
					jQuery("#collapseOne"+tech).load(page);
					$("#collapseOne"+tech).addClass("NodeMap");
					var target = $("#accordion");
					$('html, body').animate({
			            scrollTop: target.offset().top
			        }, 1000);
			 }
		},
	
		//function for taking actions on register/deploy toggle button 
		selectToggle:function(btnVal,tech){
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
			$(".pageDiv").removeClass("in");
			setTimeout(function(){ $(".pageDiv").removeClass("in"); }, 500);
			switch (tech) {
			case "Ganglia":
				if ($("#nodeCheckGanglia").is(':checked') && btnVal=="registerToggle") {
					$("#nodeMapGanglia").attr("disabled","disabled");
					if(undefined==gangliaObj.Defaults.register && undefined== gangliaObjReg.Defaults.gmetadHost){
						$("#confPageGanglia").removeClass("btn-primary").addClass("btn-danger");
						$("#confTypeGanglia").text("-");
					}else{
						$("#confPageGanglia").removeClass("btn-primary");
						$("#confTypeGanglia").text("Custom");
					}
				}else{
					$("#nodeMapGanglia").removeAttr("disabled");
					if(undefined==gangliaObj.Defaults.register){
						$("#confPageGanglia").addClass("btn-primary").removeClass("btn-danger");
						$("#confTypeGanglia").text("Default");
					}else{
						$("#confPageGanglia").removeClass("btn-primary btn-danger");
						$("#confTypeGanglia").text("Custom");
					}
				}
				break;
			case "Hadoop":
				if ($("#nodeCheckHadoop").is(':checked') && btnVal=="registerToggle") {
					$("#nodeMapHadoop").attr("disabled","disabled");
					if(undefined==hadoopObj.Defaults.register && undefined== hadoopObjReg.Defaults.homeDir){
						$("#confPageHadoop").removeClass("btn-primary").addClass("btn-danger");
						$("#confTypeHadoop").text("-");
					}else{
						$("#confPageHadoop").removeClass("btn-primary");
						$("#confTypeHadoop").text("Custom");
					}
				}else{
					$("#nodeMapHadoop").removeAttr("disabled");
					if(undefined==hadoopObj.Defaults.register){
						$("#confPageHadoop").addClass("btn-primary").removeClass("btn-danger");
						$("#confTypeHadoop").text("Default");
					}else{
						$("#confPageHadoop").removeClass("btn-primary btn-danger");
						$("#confTypeHadoop").text("Custom");
					}
				}
				break;
			case "Zookeeper":
				if ($("#nodeCheckZookeeper").is(':checked') && btnVal=="registerToggle") {
					$("#nodeMapZookeeper").attr("disabled","disabled");
					if(null==zookeeperObjReg){
						$("#confPageZookeeper").removeClass("btn-primary").addClass("btn-danger");
						$("#confTypeZookeeper").text("-");
					}else{
						$("#confPageZookeeper").removeClass("btn-primary ");
						$("#confTypeZookeeper").text("Custom");
					}
				}else{
					$("#nodeMapZookeeper").removeAttr("disabled");
					if(null==zookeeperObj){
						$("#confPageZookeeper").addClass("btn-primary");
						$("#confTypeZookeeper").text("Default");
					}else{
						$("#confPageZookeeper").removeClass("btn-primary btn-danger");
						$("#confTypeZookeeper").text("Custom");
					}
				}
				break;
			
			default:
				if ($("#nodeCheck"+tech).is(':checked') && btnVal=="registerToggle") {
					$("#nodeMap"+tech).attr("disabled","disabled");
					if(undefined==zookeeperObjMap[tech] || undefined== zookeeperObjMap[tech].zookeeperObjReg){
						$("#confPage"+tech).removeClass("btn-primary").addClass("btn-danger");
						$("#confType"+tech).text("-");
					}else{
						$("#confPage"+tech).removeClass("btn-primary");
						$("#confType"+tech).text("Custom");
					}
				}else{
					$("#nodeMap"+tech).removeAttr("disabled");
					if(undefined==zookeeperObjMap[tech] || undefined== zookeeperObjMap[tech].zookeeperObj){
						$("#confPage"+tech).removeClass("btn-danger").addClass("btn-primary");
						$("#confType"+tech).text("Default");
					}else{
						$("#confPage"+tech).removeClass("btn-primary btn-danger");
						$("#confType"+tech).text("Custom");
					}
				}

				break;
			}
			
		},
		
		
		agentPathOnChange:function(){
			var agent=$.trim($('#agentInstallDir').val());
			var gmondConfPath=jsonDataHybrid.hybrid.Ganglia.Defaults.gmondConfPath.split(agentPath).join(agent);
			var gmetadConfPath=jsonDataHybrid.hybrid.Ganglia.Defaults.gmetadConfPath.split(agentPath).join(agent);
			var rrdFilePath=jsonDataHybrid.hybrid.Ganglia.Defaults.rrdFilePath.split(agentPath).join(agent);
			jsonDataHybrid.hybrid.Ganglia.Defaults.gmondConfPath=gmondConfPath;
			jsonDataHybrid.hybrid.Ganglia.Defaults.gmetadConfPath=gmetadConfPath;
			jsonDataHybrid.hybrid.Ganglia.Defaults.rrdFilePath=rrdFilePath;
			agentPath=agent;
		},
		//function for changing username from path directories 
		userNameOnChange:function(){
			var user=$.trim($('#inputUserName').val());
			var agentInstallDir=jsonDataHybrid.hybrid.Agent.agentInstallDir.split(userName).join('/home/'+user);
			jsonDataHybrid.hybrid.Agent.agentInstallDir=agentInstallDir;
			 $("#agentInstallDir").val(jsonDataHybrid.hybrid.Agent.agentInstallDir);
			
			if(jsonDataHybrid.hybrid.Zookeeper_default != undefined){
				var installationPathZookeeper=jsonDataHybrid.hybrid.Zookeeper_default.Defaults.installationHomePath.split(userName).join('/home/'+user);
				var dataDirZookeeper=jsonDataHybrid.hybrid.Zookeeper_default.Defaults.dataDir.split(userName).join('/home/'+user);
				jsonDataHybrid.hybrid.Zookeeper_default.Defaults.installationHomePath=installationPathZookeeper;
				jsonDataHybrid.hybrid.Zookeeper_default.Defaults.dataDir=dataDirZookeeper;
			}
			if(jsonDataHybrid.hybrid.Cassandra != undefined){
				var installationPathCassandra=jsonDataHybrid.hybrid.Cassandra.Defaults.installationPath.split(userName).join('/home/'+user);
				var logDir=jsonDataHybrid.hybrid.Cassandra.Defaults.logDir.split(userName).join('/home/'+user);
				var dataDir=jsonDataHybrid.hybrid.Cassandra.Defaults.dataDir.split(userName).join('/home/'+user);
				var savedCachesDir=jsonDataHybrid.hybrid.Cassandra.Defaults.savedCachesDir.split(userName).join('/home/'+user);
				var commitlogDir=jsonDataHybrid.hybrid.Cassandra.Defaults.commitlogDir.split(userName).join('/home/'+user);
				jsonDataHybrid.hybrid.Cassandra.Defaults.installationPath=installationPathCassandra;
				jsonDataHybrid.hybrid.Cassandra.Defaults.logDir=logDir;
				jsonDataHybrid.hybrid.Cassandra.Defaults.dataDir=dataDir;
				jsonDataHybrid.hybrid.Cassandra.Defaults.savedCachesDir=savedCachesDir;
				jsonDataHybrid.hybrid.Cassandra.Defaults.commitlogDir=commitlogDir;
			}
			
			if(jsonDataHybrid.hybrid.Hadoop != undefined){
				var installationPath=jsonDataHybrid.hybrid.Hadoop.Defaults.installationPath.split(userName).join('/home/'+user);
				var dfsNameDir=jsonDataHybrid.hybrid.Hadoop.Defaults.dfsNameDir.split(userName).join('/home/'+user);
				var dfsDataDir=jsonDataHybrid.hybrid.Hadoop.Defaults.dfsDataDir.split(userName).join('/home/'+user);
				var mapRedTmpDir=jsonDataHybrid.hybrid.Hadoop.Defaults.mapRedTmpDir.split(userName).join('/home/'+user);
				var hadoopTmpDir=jsonDataHybrid.hybrid.Hadoop.Defaults.hadoopTmpDir.split(userName).join('/home/'+user);
				var journalNodeEditsDir=jsonDataHybrid.hybrid.Hadoop.Defaults.journalNodeEditsDir.split(userName).join('/home/'+user);
				
				jsonDataHybrid.hybrid.Hadoop.Defaults.installationPath=installationPath;
				jsonDataHybrid.hybrid.Hadoop.Defaults.dfsNameDir=dfsNameDir;
				jsonDataHybrid.hybrid.Hadoop.Defaults.dfsDataDir=dfsDataDir;
				jsonDataHybrid.hybrid.Hadoop.Defaults.mapRedTmpDir=mapRedTmpDir;
				jsonDataHybrid.hybrid.Hadoop.Defaults.hadoopTmpDir=hadoopTmpDir;
				jsonDataHybrid.hybrid.Hadoop.Defaults.journalNodeEditsDir=journalNodeEditsDir;
			}
			if(jsonDataHybrid.hybrid.Ganglia != undefined){
				var gmondConfPath=jsonDataHybrid.hybrid.Ganglia.Defaults.gmondConfPath.split(userName).join('/home/'+user);
				var gmetadConfPath=jsonDataHybrid.hybrid.Ganglia.Defaults.gmetadConfPath.split(userName).join('/home/'+user);
				var rrdFilePath=jsonDataHybrid.hybrid.Ganglia.Defaults.rrdFilePath.split(userName).join('/home/'+user);
				jsonDataHybrid.hybrid.Ganglia.Defaults.gmondConfPath=gmondConfPath;
				jsonDataHybrid.hybrid.Ganglia.Defaults.gmetadConfPath=gmetadConfPath;
				jsonDataHybrid.hybrid.Ganglia.Defaults.rrdFilePath=rrdFilePath;
			}
			userName='/home/'+user;
			com.impetus.ankush.hybridClusterCreation.agentPathOnChange();
			agentPath=userName;
		},
		/*Function for check/uncheck check boxes on click on header check box*/
		checkAllNodes : function(id,nodeClass) {
			if ($('#' + id).is(':checked')) {
				$("."+nodeClass).each(function() {
						if($(this).is(':disabled')){
							$(this).removeAttr('checked');
						}else{
							$(this).prop('checked', true);
						}
					var idComp=$(this).attr("id").split("nodeCheck")[1];
					if($("#nodeCount"+idComp).text()!='0'){
						$("#nodeMap"+idComp).removeClass('disabled btn-danger');	
					}else{
						$("#nodeMap"+idComp).addClass("btn-danger").removeClass('disabled');
					}
					if($("#confType"+idComp).text()=='-'){
						$("#confPage"+idComp).addClass("btn-danger").removeClass('disabled');		
					}else if($("#confType"+idComp).text()=='Default'){
						$("#confPage"+idComp).removeClass('disabled btn-danger').addClass("btn-primary");	
					}else{
						$("#confPage"+idComp).removeClass('disabled btn-danger btn-primary');
					}
				});
				$(".deployBtn").removeAttr("disabled");
				$(".registerBtn	").removeAttr("disabled");
				$(".confTech").removeAttr("disabled");
			    $(".nodeMap").removeAttr("disabled");
			   
			}else{
					$(".confTech").attr("disabled",true).removeClass('btn-primary btn-danger');
				    $(".nodeMap").attr("disabled",true).removeClass('btn-danger');
				    $(".deployBtn").attr("disabled",true);
				    $(".registerBtn").attr("disabled",true);
				    $('.' + nodeClass).attr('checked', false);
			}
		},
		
/*Function for check/uncheck head check box on click of node checkbox*/
		 nodeCheckBox : function(i,table,nodeCheck,checkHead,value) {
		        if ($("#"+table+" ."+nodeCheck+":checked").length == $("#"+table+" ."+nodeCheck).length) {
		            $("#"+checkHead).prop("checked", true);
		        } else {
		            $("#"+checkHead).removeAttr("checked");
		        }
		        if($('#nodeCheck'+value).is(':checked')){
		        	if(value=="Ganglia" && $("#btnNonSudoNode").hasClass("active")){
		        		  $("#confPage"+value).removeAttr("disabled");
					      $("#nodeMap"+value).attr("disabled","disabled");
					      $("#registerToggle"+value).removeAttr("disabled");
					      if($("#confType"+value).text()=='-'){
					    	  $("#confPage"+value).addClass("btn-danger");  
					      }else if($("#confType"+value).text()=='Default'){
					    	  $("#confPage"+value).addClass("btn-primary"); 
					      }
					      else{
					    	  $("#confPage"+value).removeClass("btn-danger btn-primary");
					      }
		        	}else{
		        		$("#confPage"+value).removeAttr("disabled");
					      $("#nodeMap"+value).removeAttr("disabled");
					      $("#deployToggle"+value).removeAttr("disabled");
					      $("#registerToggle"+value).removeAttr("disabled");
					      if($("#confType"+value).text()=='-'){
					    	  $("#confPage"+value).addClass("btn-danger");  
					      }else if($("#confType"+value).text()=='Default'){
					    	  $("#confPage"+value).addClass("btn-primary"); 
					      }
					      else{
					    	  $("#confPage"+value).removeClass("btn-danger btn-primary");
					      }
					      if($("#nodeCount"+value).text()=='0'){
					    	  $("#nodeMap"+value).addClass("btn-danger");  
					      }else{
					    	  $("#nodeMap"+value).removeClass("btn-danger");
					      }
		        	}
		        	  
		        }else{
		        	$("#deployToggle"+value).attr("disabled",true);
		        	$("#registerToggle"+value).attr("disabled",true);
		        	$("#confPage"+value).attr("disabled",true);
				    $("#nodeMap"+value).attr("disabled",true);
				    $("#nodeMap"+value).removeClass("btn-danger");
				    $("#confPage"+value).removeClass("btn-danger btn-primary");
				    $('td', $('#nodeCheck'+value).parents('tr')).removeClass('alert-danger');
		        }
		    
		    },
		    errorMessageShow:function(errDivId,divId,errorCount,errorMsg,tableId){
		    	if(divId != ''){
		    	 	$("#"+errDivId).append('<div class="alert alert-danger alert-dismissible" role="alert">'
		            		+'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
		            		+'<a class="alert-link" href="javascript:com.impetus.ankush.common.focusDiv(\''+divId+'\')" >'+errorMsg+'</a></div>');
		    	}else{
		    	 	$("#"+errDivId).append('<div class="alert alert-danger alert-dismissible" role="alert">'
		            		+'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
		            		+'<a class="alert-link" href="javascript:com.impetus.ankush.common.focusTableDiv(\''+tableId+'\')" >'+errorMsg+'</a></div>');
		    	}
		    },
//function for cluster deployment validations
			validateCluster:function(){
				$('#validateError').html('').hide();
		        var errorMsg = '';
		        $("#errorDivMain").html('').hide();
		        errorCount = 0;
		        if (!com.impetus.ankush.validation.empty($('#inputClusterName').val())) {
		            errorCount++;
		            errorMsg = 'Cluster Name field empty.';
		            com.impetus.ankush.common.tooltipMsgChange('inputClusterName','Cluster Name cannot be empty');
		            var divId='inputClusterName';
		            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        } else if ($('#inputClusterName').val().trim().split(' ').length > 1) {
		            errorCount++;
		            errorMsg = "Cluster Name can't contain blank spaces.";
		            com.impetus.ankush.common.tooltipMsgChange('inputClusterName','Cluster Name cannot contain blank spaces');
		            var divId='inputClusterName';
		            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        }else if ($('#inputClusterName').val().length > 20) {
		            errorCount++;
		            errorMsg = "Cluster Name must be at max. 20 characters long.";
		            com.impetus.ankush.common.tooltipMsgChange('inputClusterName',errorMsg);
		            var divId='inputClusterName';
		            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        }
		        else if (!com.impetus.ankush.validation.alphaNumericCharWithoutHyphen($('#inputClusterName').val())) {
		            errorCount++;
		            errorMsg = "Cluster Name can contain only alphabets, numeric, dot and underscore";
		            com.impetus.ankush.common.tooltipMsgChange('inputClusterName',errorMsg);
		            var divId='inputClusterName';
		            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        }
		        else {
		            com.impetus.ankush.common.tooltipOriginal('inputClusterName',com.impetus.ankush.tooltip.commonClusterCreation.clusterName);
		        }
		        if($('#oracleBundleCheck').is(':checked') &&  !$('.registerBtn').hasClass("active")){
		        	  if (!com.impetus.ankush.validation.empty($('#oracleBundleFilePath').val())) {
		  	            errorCount++;
		  	            errorMsg = 'Java bundle path empty';
		  	            com.impetus.ankush.common.tooltipMsgChange('oracleBundleFilePath','Java Bundle Path cannot be empty');
		  	            var divId='oracleBundleFilePath';
		  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		  	        }else {
			            com.impetus.ankush.common.tooltipOriginal('oracleBundleFilePath',com.impetus.ankush.tooltip.commonClusterCreation.oracleHome);
			        }
		        }else if($('#oracleBundleCheck').is(':checked') && $('.registerBtn').hasClass("active")){
		        	errorCount++;
	  	            errorMsg = 'Uncheck install Java as Java cannot be installed if any technology is selected as register';
	  	            com.impetus.ankush.common.tooltipMsgChange('oracleBundleFilePath','Uncheck install Java as Java cannot be installed if any technology is selected as register');
	  	            var divId='oracleBundleFilePath';
	  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        }
		        else{
		        	if (!com.impetus.ankush.validation.empty($('#inputJavaHome').val())) {
		  	            errorCount++;
		  	            errorMsg = 'Java library path field empty';
		  	            com.impetus.ankush.common.tooltipMsgChange('inputJavaHome','Java Path cannot be empty');
		  	            var divId='inputJavaHome';
		  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		  	        }else {
			            com.impetus.ankush.common.tooltipOriginal('inputJavaHome',com.impetus.ankush.tooltip.commonClusterCreation.javaHome);
			        }
		        }
		        if (!com.impetus.ankush.validation.empty($('#inputUserName').val())) {
	  	            errorCount++;
	  	            errorMsg = 'User name field empty';
	  	            com.impetus.ankush.common.tooltipMsgChange('inputUserName','User name cannot be empty');
	  	            var divId='inputUserName';
	  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
	  	        }else if (!com.impetus.ankush.validation.withoutSpace($('#inputUserName').val())) {
	  	            errorCount++;
	  	            errorMsg = 'User name field contains space';
	  	            com.impetus.ankush.common.tooltipMsgChange('inputUserName','User name cannot contain space');
	  	            var divId='inputUserName';
	  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('inputUserName',com.impetus.ankush.tooltip.commonClusterCreation.userName);
		        }
		        if($("#authGroupBtn .active").data("value")==0){
			        if (!com.impetus.ankush.validation.empty($('#inputPassword').val())) {
			            errorCount++;
			            errorMsg = 'Password field empty.';
			            com.impetus.ankush.common.tooltipMsgChange('inputPassword','Password cannot be empty');
			            var divId='inputPassword';
			            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
			      
			        }else{
			            com.impetus.ankush.common.tooltipOriginal('inputPassword',com.impetus.ankush.tooltip.commonClusterCreation.password);
			        }
		        }else{
		        	  if (uploadPathSharedKey==null){
		        		  errorCount++;
		                  errorMsg = 'Share key file not uploaded.'; 
		                  var divId='filePathShareKey';
		                  com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        	  }else{
		                  com.impetus.ankush.common.tooltipOriginal('filePathShareKey',com.impetus.ankush.tooltip.commonClusterCreation.sharedKey);
		        	  }
		        }
		        if (!com.impetus.ankush.validation.empty($('#agentInstallDir').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Agent Install Dir field empty';
	  	            com.impetus.ankush.common.tooltipMsgChange('agentInstallDir','Agent Install Dir cannot be empty');
	  	            var divId='agentInstallDir';
	  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
	  	        }else if (!com.impetus.ankush.validation.withoutSpace($('#agentInstallDir').val())) {
	  	            errorCount++;
	  	            errorMsg = 'User name field contains space';
	  	            com.impetus.ankush.common.tooltipMsgChange('agentInstallDir','User name cannot contain space');
	  	            var divId='agentInstallDir';
	  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('agentInstallDir',com.impetus.ankush.tooltip.commonClusterCreation.agentInstallDir);
		        }
		        if($("#ipModeGroupBtn .active").data("value")==0){
		            if(!com.impetus.ankush.validation.empty($('#inputIpRange').val())){
		                errorCount++;
		                errorMsg = 'Host Name field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('inputIpRange','Host Name cannot be empty');
		                var divId='inputIpRange';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('inputIpRange',com.impetus.ankush.tooltip.commonClusterCreation.ipRange);
		            }
		        }else{
	        		if (uploadFilePath == null) {
		                errorCount++;
		                errorMsg = 'File not uploaded.';
		                com.impetus.ankush.common.tooltipMsgChange('filePath','File Not Uploaded');
		                var divId='filePath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
		        	}else {
		                com.impetus.ankush.common.tooltipOriginal('filePath',com.impetus.ankush.tooltip.commonClusterCreation.ipFile);
		            }
		        }
		        $(".nodeCheckBoxTech").each(function() {
		        	var checkBoxVal=$(this).val();
		        	
		        	 if($(this).is(':checked') && $('#confType'+checkBoxVal).text()=='-'){	
			    			errorCount++;
			                errorMsg ='For '+ $(this).val()+' configurations not set.';
			                var divId='confPage'+checkBoxVal;
			                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
			                $('td', $(this).parents('tr')).addClass('alert-danger');
				        }else{
				        	$('td', $(this).parents('tr')).removeClass('alert-danger');
				        }
		    		if($(this).is(':checked') && $('#nodeCount'+checkBoxVal).text()==0){
		    			if(!(((checkBoxVal == "Hadoop") && $("#toggleRegisterBtnHadoop .active").data("value")==0) || 
		    					((checkBoxVal == "Ganglia") && $("#toggleRegisterBtnGanglia .active").data("value")==0) || 
		    					((checkBoxVal.split("_")[0] == "Zookeeper") && $("#toggleRegisterBtn"+checkBoxVal+" .active").data("value")==0))){
		    				errorCount++;
			                errorMsg ='For '+ $(this).val().split("_")[0]+' no node is selected.';
			                var divId='nodeMap'+checkBoxVal;
			                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain',divId,errorCount,errorMsg);
			                $('td', $(this).parents('tr')).addClass('alert-danger');
		    			}
		    			
			        }else{
			        	$('td', $(this).parents('tr')).removeClass('alert-danger');
			        }
		    	});
		           	com.impetus.ankush.registerCluster.validateCluster(errorCount, errorMsg);
			},
			
			cancelDialog:function(dialogId){
				$('#'+dialogId).modal('hide');
			},
			openTemplateDialog:function(){
				if($("#selectTemplate").val()!=""){
					com.impetus.ankush.hybridClusterCreation.updateTemplateData();
				}else{
					com.impetus.ankush.hybridClusterCreation.templateDialogCreate();
				}
			},
			
			//function for template dialog creation
			templateDialogCreate:function(){
				$('#templateName').val('');
				$('#errorLabel').html('');
				$('#templateCreate').modal('show');	
				$('#templateCreate').on('shown.bs.modal', function () {
					$('input:text:visible:first', this).focus();
				 });
			},
			
			//function for save template
			saveTemplate:function(){
				$('#errorLabel').html('');
				var tempName=$.trim($('#templateName').val());
				if(tempName==''){
					$('#errorLabel').append('Enter template name.');
					return;
				}else if(!com.impetus.ankush.validation.alphaNumericCharWithUnderscore(tempName)){
					$('#errorLabel').append('Template name should be alphanumeric without any spaces and special characters except underscore.');
					return;
					
				}else if(!com.impetus.ankush.validation.length(tempName,30)){
					$('#errorLabel').append('Template name should be at max 30 characters long.');
					return;
				}else{
					$('#errorLabel').html('');
				}
				var url = baseUrl + '/cluster/template';
				com.impetus.ankush.hybridClusterCreation.dataPrepare();
				var saveData={};
				saveData.technology=clusterTechnology;
				saveData.name=tempName;
				saveData.data=data;
				  com.impetus.ankush.placeAjaxCall(url, "POST", true, saveData, function(result) {
							com.impetus.ankush.hybridClusterCreation.cancelDialog('templateCreate');
							$("#errorDivMain").hide();
							$('#validateError').hide();
							if(result.output.status){
								com.impetus.ankush.hybridClusterCreation.getTemplateData(clusterTechnology);
								$("#selectTemplate").val(tempName);
								if(cluster_State=="error"){
									$('#selectTemplate').html('').append("<option value=''>"+tempName+"</option>");
								}
							}else{
								if(result.output.data.error[0]=='Already exists'){
									 $('#confirmTemplate').modal('show');
								}
								else{
									$("#errorDivMain").show().html('');
									if(result.output.data.error.length > 1){
										 $('#validateError').show().html('Errors '+"<span class='badge'>"+result.output.data.error.length+"</span>");
									}
									else{
										$('#validateError').show().html('Error '+"<span class='badge'>"+result.output.data.error.length+"</span>");
									}
									for ( var i = 0; i < result.output.data.error.length; i++) {
										  com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',i+1,result.output.data.error[i]);
									}	
								}
							}
					});
			},
			
			//function for updating template
			updateTemplateData:function(){
				var url = baseUrl + '/cluster/template?update=true';
				com.impetus.ankush.hybridClusterCreation.dataPrepare();
				var saveData={};
				saveData.technology=clusterTechnology;
				saveData.name=$.trim($('#templateName').val());
				if(saveData.name==""){
					saveData.name=$.trim($('#selectTemplate').val());
				}
				saveData.data=data;
				  com.impetus.ankush.placeAjaxCall(url, "POST", true, saveData, function(result) {
							com.impetus.ankush.hybridClusterCreation.cancelDialog('confirmTemplate');
							if(result.output.status){
								$("#errorDivMain").hide();
								$('#validateError').hide();
							}else{
									$("#errorDivMain").show().html('');
									if(result.output.data.error.length > 1){
										$('#validateError').show().html('Errors '+"<span class='badge'>"+result.output.data.error.length+"</span>");
									}
									else{
										$('#validateError').show().html('Error '+"<span class='badge'>"+result.output.data.error.length+"</span>");
									}
									for ( var i = 0; i < result.output.data.error.length; i++) {
										  com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',i+1,result.output.data.error[i]);
									}	
								}
						
					});
			},

			
			//function for dpeloyment data prepration
			dataPrepare:function(){
				 data.components={};
				 var dataObj = nodesTable.fnGetData();
					var nodes={};
					if(nodeStatus!=null){
						for ( var i = 0; i < nodeRoleArray.length; i++) {
							
						var nodeObj={};
						nodeObj.roles={};
							if($("#nodeRole"+i).text()!=""){
								nodeObj.host=dataObj[i][1];
								nodeObj.publicHost=dataObj[i][1];
								nodeObj.os=$("#osName"+i).text();
								for ( var key in nodeRoleArray[i].roles) {
									if(key=="Hadoop"){
										var index=nodeRoleArray[i].roles[key].indexOf("standByNamenode");
										if(index != -1){
											nodeRoleArray[i].roles[key][index]="NameNode";	
										}
									}
									nodeObj.roles[key]=nodeRoleArray[i].roles[key];
									
								}
								nodes[nodeObj.host]=nodeObj;
								if(nodes[nodeObj.host].roles["Ganglia"].indexOf("gmond") ==-1){
									nodes[nodeObj.host].roles["Ganglia"].push("gmond");	
								}
								if(undefined==hadoopObj.version || hadoopObj.version.split(".")[0] == '2' ){
									var index=nodes[nodeObj.host].roles["Hadoop"].indexOf("TaskTracker");
									if(index !=-1){
										nodes[nodeObj.host].roles["Hadoop"][index]="NodeManager";	
									}
								}
								if($("#nodeCheckHadoop").is(":checked") && $("#toggleRegisterBtnHadoop.active").data("value")==1){
									if( undefined==hadoopObj.version || (hadoopObj.version.split(".")[0] == '2' && 
											hadoopObj.Defaults.haEnabled &&  
											hadoopObj.Defaults.automaticFailoverEnabled && 
											nodeObj.host==hadoopNodesObj.standByNamenode.publicIp)){
										if(nodes[nodeObj.host].roles["Hadoop"].indexOf("zkfc") == -1){
											nodes[nodeObj.host].roles["Hadoop"].push("zkfc");	
										}
									}
									if(undefined==hadoopObj.version || (hadoopObj.version.split(".")[0] == '2' && 
											hadoopObj.Defaults.haEnabled &&  hadoopObj.Defaults.automaticFailoverEnabled && 
											nodeObj.host==hadoopNodesObj.NameNode.publicIp)){
										if(nodes[nodeObj.host].roles["Hadoop"].indexOf("zkfc") == -1){
											nodes[nodeObj.host].roles["Hadoop"].push("zkfc");	
										}
									}
								}
							}
								
						}
					}
					//for removing empty role array
				for ( var key in nodes) {
					for ( var roleTech in nodes[key].roles) {
						if(nodes[key].roles[roleTech].length==0 || !$('#nodeCheck'+roleTech).is(':checked')){
							delete nodes[key].roles[roleTech];
						}
					}
					if(Object.keys(nodes[key].roles).length==0){
						delete nodes[key];
					}
				}
				data.nodes=nodes;
				var javaConf={};
				if ($("#oracleBundleCheck").is(':checked')) {
					javaConf.register=false;
						javaConf.source=$.trim($('#oracleBundleFilePath').val());	
				}else{
					javaConf.register=true;
					javaConf.homeDir=$.trim($('#inputJavaHome').val());
				}
				data.javaConf=javaConf;
				if($("#ipModeGroupBtn .active").data("value")==0){
					data.hosts=$.trim($('#inputIpRange').val());
					data.hostFile='';
				}else{
					data.hostFile=$.trim($('#filePath').val());
					data.hosts='';
				}
				data.name=$.trim($('#inputClusterName').val());
				data.authConf={};
				data.authConf.username=$.trim($('#inputUserName').val());
				if($("#authGroupBtn .active").data("value")==0){
					data.authConf.usingPassword=true;
					data.authConf.password=$.trim($('#inputPassword').val());
				}else{
					data.authConf.usingPassword=false;
					data.authConf.privateKey=uploadPathSharedKey;
				}
				data.technology=clusterTechnology;
				if(sudoFlag){
					data.installationType="SUDO";
				}else{
					data.installationType="NONSUDO";
				}
				
				data.components.Agent={};
				data.components.Agent.installPath=$("#agentInstallDir").val();
				//Cassandra Data
				if ($("#nodeCheckCassandra").is(':checked')) {
					if($("#toggleRegisterBtnCassandra .active").data("value")==1){
						data.components.Cassandra={};
						data.components.Cassandra.advanceConf={};
						 if(undefined ==cassandraObj.Defaults.installationPath){
							 var jsonData=jsonDataHybrid.hybrid.Cassandra;
							 cassandraObj=jsonData;
							 for(var key in jsonData.Vendors){
								 cassandraObj.vendor=key;
								 break;
							 }
							 cassandraObj.version=Object.keys(jsonData.Vendors[cassandraObj.vendor])[0];
							 cassandraObj.path=jsonData.Vendors[cassandraObj.vendor][cassandraObj.version].downloadUrl;
							 cassandraObj.sourceType="DOWNLOAD";
							 for(var key in jsonData.Defaults.Partitioner){
								 cassandraObj.Defaults.partitioner=jsonData.Defaults.Partitioner[key];
								 break;
							 }
							 for(var key in jsonData.Defaults.Snitch){
								 cassandraObj.Defaults.snitch=key;
								 break;
							 }
							cassandraObj.Defaults.vNodeEnabled=false;
						 }
						data.components.Cassandra.vendor=cassandraObj.vendor;
						data.components.Cassandra.version=cassandraObj.version;
						data.components.Cassandra.installPath=cassandraObj.Defaults.installationPath;
						data.components.Cassandra.source=cassandraObj.path;	
						data.components.Cassandra.sourceType=cassandraObj.sourceType;	
						data.components.Cassandra.advanceConf.partitioner=cassandraObj.Defaults.partitioner;
						data.components.Cassandra.advanceConf.snitch=cassandraObj.Defaults.snitch;
						data.components.Cassandra.advanceConf.rpcPort=+cassandraObj.Defaults.rpcPort;
						data.components.Cassandra.advanceConf.jmxPort=+cassandraObj.Defaults.jmxPort;
						data.components.Cassandra.advanceConf.storagePort=+cassandraObj.Defaults.storagePort;
						data.components.Cassandra.advanceConf.dataDir=cassandraObj.Defaults.dataDir;
						data.components.Cassandra.advanceConf.logDir=cassandraObj.Defaults.logDir;
						data.components.Cassandra.advanceConf.savedCachesDir=cassandraObj.Defaults.savedCachesDir;
						data.components.Cassandra.advanceConf.vNodeEnabled=cassandraObj.Defaults.vNodeEnabled;
						data.components.Cassandra.advanceConf.commitlogDir=cassandraObj.Defaults.commitlogDir;
						data.components.Cassandra.nodes=cassandraNodeMap;	
						 data.components.Cassandra.register=false;
						data.components.Cassandra.confState=$('#confTypeCassandra').text();
					}else{
						data.components.Cassandra={
								nodes:new Array(),
							};
							data.components.Cassandra.componentVendor=cassandraObjReg.vendor;
							data.components.Cassandra.componentVersion=cassandraObjReg.version;
							data.components.Cassandra.componentHome=cassandraObjReg.Defaults.componentHome;
							data.components.Cassandra.nodes.push(cassandraNodesObjReg);
							data.components.Cassandra.jmxPort=+cassandraObjReg.Defaults.jmxPort;
							data.components.Cassandra.register=true;
							data.components.Cassandra.confState=$('#confTypeCassandra').text();
					}
				}
				
				
				//Ganglia Data
				if ($("#nodeCheckGanglia").is(':checked')){
					if($("#toggleRegisterBtnGanglia .active").data("value")==1){
					 if(undefined ==gangliaObj.Defaults.register){
						 var jsonData=jsonDataHybrid.hybrid.Ganglia;
						 gangliaObj=jsonData;
					}
					data.components.Ganglia={};
					data.components.Ganglia.advanceConf={};
						data.components.Ganglia.vendor="ganglia";
						data.components.Ganglia.version="3.0.7";
						data.components.Ganglia.advanceConf.gridName=$.trim(gangliaObj.Defaults.gridName);
						data.components.Ganglia.advanceConf.gangliaClustername=$.trim(gangliaObj.Defaults.gangliaClustername);
						data.components.Ganglia.advanceConf.gmondConfPath=$.trim(gangliaObj.Defaults.gmondConfPath);
						data.components.Ganglia.advanceConf.gmetadConfPath=$.trim(gangliaObj.Defaults.gmetadConfPath);
						data.components.Ganglia.advanceConf.gangliaPort=+gangliaObj.Defaults.gangliaPort;
						data.components.Ganglia.advanceConf.rrdFilePath=$.trim(gangliaObj.Defaults.rrdFilePath);
						if($("#toggleRegisterBtnGanglia .active").data("value")==1){
							data.components.Ganglia.advanceConf.pollingInterval=+gangliaObj.Defaults.pollingInterval;
							data.components.Ganglia.register=false;
						}else{
							data.components.Ganglia.register=true;
						}
						data.components.Ganglia.confState=$('#confTypeGanglia').text();
					}else{
						data.components.Ganglia={};
						data.components.Ganglia.advanceConf={};
						if(undefined!=gangliaObjReg.Defaults.gmetadHost){
							data.components.Ganglia.register=true;
							data.components.Ganglia.advanceConf.gmetadHost=gangliaObjReg.Defaults.gmetadHost;
							data.components.Ganglia.advanceConf.gmondConfPath=gangliaObjReg.Defaults.gmondConfPath;
							data.components.Ganglia.advanceConf.gmetadConfPath=gangliaObjReg.Defaults.gmetadConfPath;
							data.components.Ganglia.advanceConf.gangliaPort=+gangliaObjReg.Defaults.gangliaPort;
							data.components.Ganglia.advanceConf.registerLevel=gangliaObjReg.Defaults.registerLevel;
						}
						data.components.Ganglia.confState=$('#confTypeGanglia').text();
					}
				}
				
				//Hadoop Data
				if ($("#nodeCheckHadoop").is(':checked')) {
					if($("#toggleRegisterBtnHadoop .active").data("value")==1){
						data.components.Hadoop={};
						data.components.Hadoop.advanceConf={};
						 if(undefined == hadoopObj.version){
							 var jsonData=jsonDataHybrid.hybrid.Hadoop;
							 hadoopObj=jsonData;
							 for(var key in jsonData.Vendors){
								 hadoopObj.vendor=key;
								 break;
							 }
							 hadoopObj.version=Object.keys(jsonData.Vendors[hadoopObj.vendor])[0];
							 hadoopObj.path=jsonData.Vendors[hadoopObj.vendor][hadoopObj.version].url;
							 hadoopObj.sourceType="DOWNLOAD";
							 
						 }
						data.components.Hadoop.vendor=hadoopObj.vendor;
						data.components.Hadoop.version=hadoopObj.version;
						data.components.Hadoop.installPath=$.trim(hadoopObj.Defaults.installationPath);
						data.components.Hadoop.sourceType=hadoopObj.sourceType;
						data.components.Hadoop.source=$.trim(hadoopObj.path);	
						
						data.components.Hadoop.advanceConf.dfsReplicationFactor=hadoopObj.Defaults.dfsReplicationFactor;
						data.components.Hadoop.advanceConf.dfsDataDir=$.trim(hadoopObj.Defaults.dfsDataDir);
						data.components.Hadoop.advanceConf.dfsNameDir=$.trim(hadoopObj.Defaults.dfsNameDir);
						data.components.Hadoop.advanceConf.hadoopTmpDir=$.trim(hadoopObj.Defaults.hadoopTmpDir);
						data.components.Hadoop.advanceConf.mapRedTmpDir=$.trim(hadoopObj.Defaults.mapRedTmpDir);
						data.components.Hadoop.register=false;
						data.components.Hadoop.confState=$('#confTypeHadoop').text();
						data.components.Hadoop.nodes=hadoopNodeMap;
						if(undefined==hadoopObj.version || hadoopObj.version.split(".")[0]=='2'){
							data.components.Hadoop.advanceConf.jobHistoryServerEnabled=false;
							if(Object.keys(hadoopNodesObj.JobHistoryServer).length>0){
								data.components.Hadoop.advanceConf.jobHistoryServerEnabled=true;
							}
							data.components.Hadoop.advanceConf.webAppProxyServerEnabled=false;
							if(Object.keys(hadoopNodesObj.WebAppProxyServer).length>0){
								data.components.Hadoop.advanceConf.webAppProxyServerEnabled=true;
							}
							data.components.Hadoop.advanceConf.nameserviceId="";
							data.components.Hadoop.advanceConf.standByNamenode="";
							data.components.Hadoop.advanceConf.nameNodeId1="";
							data.components.Hadoop.advanceConf.nameNodeId2="";
							data.components.Hadoop.advanceConf.journalNodeEditsDir="";
							data.components.Hadoop.advanceConf.automaticFailoverEnabled=false;
							data.components.Hadoop.advanceConf.activeNamenode="";
							data.components.Hadoop.advanceConf.zkClientPort="";
							data.components.Hadoop.advanceConf.haEnabled=hadoopObj.Defaults.haEnabled;
							if(hadoopObj.Defaults.haEnabled){
								data.components.Hadoop.advanceConf.nameserviceId=$.trim(hadoopObj.Defaults.nameserviceId);
								data.components.Hadoop.advanceConf.standByNamenode=hadoopNodesObj.standByNamenode.publicIp;
								data.components.Hadoop.advanceConf.activeNamenode=hadoopNodesObj.NameNode.publicIp;
								data.components.Hadoop.advanceConf.nameNodeId1=$.trim(hadoopObj.Defaults.nameNodeId1);
								data.components.Hadoop.advanceConf.nameNodeId2=$.trim(hadoopObj.Defaults.nameNodeId2);
								data.components.Hadoop.advanceConf.journalNodeEditsDir=$.trim(hadoopObj.Defaults.journalNodeEditsDir);
								data.components.Hadoop.advanceConf.automaticFailoverEnabled=hadoopObj.Defaults.automaticFailoverEnabled;
								if(hadoopObj.Defaults.automaticFailoverEnabled){
									data.components.Hadoop.advanceConf.ensembleId="default";
								}
							}
						}
					}else{
						data.components.Hadoop={};
						data.components.Hadoop.advanceConf={};
						if(undefined!=hadoopObjReg.version){
							data.components.Hadoop.vendor=hadoopObjReg.vendor;
							data.components.Hadoop.version=hadoopObjReg.version;
							data.components.Hadoop.register=true;
							data.components.Hadoop.advanceConf.namenode=$.trim(hadoopObjReg.Defaults.namenode);
							data.components.Hadoop.advanceConf.httpPortNameNode=hadoopObjReg.Defaults.httpPortNameNode;
							data.components.Hadoop.advanceConf.installationType=$.trim(hadoopObjReg.Defaults.installationType);
							data.components.Hadoop.advanceConf.registerLevel=hadoopObjReg.Defaults.registerLevel;
							if(hadoopObjReg.version=="Apache" && hadoopObjReg.version.split(".")[0]=='1'){
								data.components.Hadoop.advanceConf.jobTracker=hadoopObjReg.Defaults.jobTracker;
						}else{
							data.components.Hadoop.advanceConf.resourceManager=hadoopObjReg.Defaults.resourceManager;
						}
					}
						data.components.Hadoop.confState=$('#confTypeHadoop').text();
					}
				}
				
					$('.nodeCheckBoxTech').each(function(){
						if($(this).val().split('_')[0]=="Zookeeper"){
							var techName=$(this).val();
							if ($("#nodeCheck"+techName).is(':checked')) {
								var btnId="toggleRegisterBtn"+techName;
								if($("#"+btnId+" .active").data("value")==1){
									 var zookeeperObj1={};
									 data.components[techName]={};
									 data.components[techName].advanceConf={};
									 if(undefined == zookeeperObjMap[techName]){
										 zookeeperObjMap[techName]={};
									 }
									 if(undefined == zookeeperObjMap[techName].zookeeperObj){
										 zookeeperObjMap[techName].zookeeperObj={};
										var jsonData=jsonDataHybrid.hybrid.Zookeeper_default;
												 zookeeperObj1=jsonData;
												 for(var key in zookeeperObj1.Vendors){
													 zookeeperObj1.vendor=key;
													 break;
												 }
												 zookeeperObj1.version=Object.keys(zookeeperObj1.Vendors[zookeeperObj1.vendor])[0];
												 zookeeperObj1.path=zookeeperObj1.Vendors[zookeeperObj1.vendor][zookeeperObj1.version].url;
												 zookeeperObj1.sourceType="DOWNLOAD";
												 zookeeperObjMap[techName].zookeeperObj=zookeeperObj1;
									 }
									 
									 if(undefined != zookeeperObjMap[techName]){
											zookeeperObj1= zookeeperObjMap[techName].zookeeperObj; 
									 }
									 if(null!=zookeeperObj1){
										 	data.components[techName].vendor=zookeeperObj1.vendor;
											data.components[techName].version=zookeeperObj1.version;
											data.components[techName].installPath=$.trim(zookeeperObj1.Defaults.installationHomePath);
											data.components[techName].sourceType=zookeeperObj1.sourceType;
											data.components[techName].source=$.trim(zookeeperObj1.path);
											data.components[techName].register=false;
											data.components[techName].advanceConf.dataDirectory=$.trim(zookeeperObj1.Defaults.dataDir);
											data.components[techName].advanceConf.syncLimit=+zookeeperObj1.Defaults.syncLimit;
											data.components[techName].advanceConf.initLimit=+zookeeperObj1.Defaults.initLimit;
											data.components[techName].advanceConf.clientPort=+zookeeperObj1.Defaults.clientPort;
											data.components[techName].advanceConf.jmxPort=+zookeeperObj1.Defaults.jmxPort;
											data.components[techName].advanceConf.tickTime=+zookeeperObj1.Defaults.tickTime;
											data.components[techName].confState=$('#confType'+techName).text();
									 }
									 if(undefined != zookeeperObjMap[techName]){
										
										 data.components[techName].nodes= zookeeperObjMap[techName].zookeeperNodesMap;
									 }
									
								}else{
									 var zookeeperObj1={};
									 data.components[techName]={};
									 data.components[techName].advanceConf={};
									 if(undefined == zookeeperObjMap[techName]){
										 zookeeperObjMap[techName]={};
									 }
									 if(undefined == zookeeperObjMap[techName].zookeeperObjReg){
										 zookeeperObjMap[techName].zookeeperObjReg={};
										var jsonData=jsonDataHybrid.hybrid.Zookeeper_default;
												 zookeeperObj1=jsonData;
												 for(var key in zookeeperObj1.Vendors){
													 zookeeperObj1.vendor=key;
													 break;
												 }
												 zookeeperObj1.version=Object.keys(zookeeperObj1.Vendors[zookeeperObj1.vendor])[0];
												 zookeeperObjMap[techName].zookeeperObjReg=zookeeperObj1;
									 }
									 
									 if(undefined != zookeeperObjMap[techName]){
											zookeeperObj1= zookeeperObjMap[techName].zookeeperObjReg; 
									 }
									 if(null!=zookeeperObj1){
										 	data.components[techName].vendor=zookeeperObj1.vendor;
											data.components[techName].version=zookeeperObj1.version;
											data.components[techName].register=true;
											data.components[techName].advanceConf.host=zookeeperObj1.Defaults.host;
											data.components[techName].advanceConf.registerLevel=zookeeperObj1.Defaults.registerLevel;
											data.components[techName].advanceConf.clientPort=+zookeeperObj1.Defaults.clientPort;
											data.components[techName].advanceConf.jmxPort=+zookeeperObj1.Defaults.jmxPort;
											data.components[techName].confState=$('#confType'+techName).text();
									 }
								}
							}
						}
					});
			},
			
			//function for cluster deployment call
			deployCluster:function(){
				delete data.components;
				var clusterId=$('#clusterDeploy').val();
				var url = baseUrl + '/cluster/create/Hybrid';
				if(clusterId!=''){
					 url = baseUrl + '/cluster/redeploy/'+clusterId;
				}
				$('#clusterDeploy').button();
				$('#clusterDeploy').button('loading');
				  com.impetus.ankush.placeAjaxCall(url, "POST", true, data, function(result) {
						$('#clusterDeploy').button('reset');
						if (!result.output.status) {
							$("#errorDivMain").show().html('');
							if(result.output.error.length > 1){
								$('#validateError').show().html('Errors '+"<span class='badge'>"+result.output.data.error.length+"</span>");
							}
							else{
								$('#validateError').show().html('Error '+"<span class='badge'>"+result.output.data.error.length+"</span>");
							}
							for ( var i = 0; i < result.output.error.length; i++) {
								 com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',i+1,result.output.error[i],"nodeTable");
							}
						}else{
							$("#errorDivMain").hide();
							$('#validateError').hide();
							com.impetus.ankush.common.loadDashboard();
						}
				});
			},
};