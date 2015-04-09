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
var cluster_State='';
com.impetus.ankush.hybridSetupDetail={
/*Function for deleting data on template loading*/
		flushTemplateData:function(){
			$('#errorDivMain').hide();
    		$('#validateError').hide();
    		$('.nodeCheckBoxTech').removeAttr('checked');
    		$('.techNodeCount').text('0');
			$('.nodeRoleIp').text('');
			$('#nodeListDiv').show();
			cassandraObj.Defaults={};
			cassandraObj.Defaults.advanceConf={};
			//com.impetus.ankush.hybridClusterCreation.defaultValue();
		},
/*Function for template loading*/
		loadTemplate:function(loadTemplateName){
			var templateName=$('#selectTemplate').val();
			if(loadTemplateName!=undefined){
				templateName=loadTemplateName;
			}
			if(templateName==''){
				return;				
			}
			com.impetus.ankush.hybridSetupDetail.flushTemplateData();
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
			clusterState=com.impetus.ankush.constants.stateError;
			var url = baseUrl + '/cluster/template?name='+templateName;
			 com.impetus.ankush.placeAjaxCall(url, "GET", false,null, function(result){
				 setupDetailData=result.output.data;
				 setupDetailData.status=result.output.status;
				 $('.confTech').removeClass('defaultBtn errorBtn').attr('disabled','disabled');
				 $('.nodeMap').removeClass('errorBtn').attr('disabled','disabled');
				 if(!result.output.status && result.output.data.error[0]==null){
					 return;
				 }
				 com.impetus.ankush.hybridSetupDetail.loadPageData();
			 });
		},
	
/*Function for template data loading on UI*/
		loadPageData:function(currentClusterId,clusterState){
			if(undefined==setupDetailData.state){
				 cluster_State=com.impetus.ankush.constants.stateError;
				 setupDetailData.errors={};
			}else{
				cluster_State=setupDetailData.state;	
			}
			com.impetus.ankush.hybridClusterCreation.techTableUpdate();
			nodeRoleArray=new Array();
     	    if(cluster_State==com.impetus.ankush.constants.stateError){
      		   $('#errorDivMain').html('');
      		   $('#validateError').html('');
      		   var count=0;
      		   for(var key in setupDetailData.errors){
      			   count++;
      			   com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',count,setupDetailData.errors[key],'nodeIpTable');
      		   }
      		  if(Object.keys(setupDetailData.errors).length > 0){
      			  	$("#errorDivMain").show();
      		   if(Object.keys(setupDetailData.errors).length > 1){
      			 $('#validateError').show().html('Errors '+"<span class='badge'>"+Object.keys(setupDetailData.errors).length+"</span>");
                }
                else{
                	$('#validateError').show().html('Error '+"<span class='badge'>"+Object.keys(setupDetailData.errors).length+"</span>");
                }
      		  }
      	   }else{
      		$('#inspectNodeBtn').attr('disabled',true);
      		 $('#clusterDeploy').hide();
      	   }
     		
     	   if(!setupDetailData.status){
     	    com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','','1',setupDetailData.error[0],'');
      		 $('#errorDivMain').show();
      		$('#validateError').show().html('Error '+"<span class='badge'>1</span>");
      		$('#clusterDeploy').removeAttr('disabled');
      		  return;
     	  }
     	  com.impetus.ankush.hybridClusterCreation.populateOracleJava();
      	  $('#inputClusterName').val(setupDetailData.name).removeAttr('title');
      	 if(!setupDetailData.javaConf.register){
      		 $('#oracleBundleCheck').attr('checked',true);
      		 $('#oracleBundleFilePath').val(setupDetailData.javaConf.source).removeAttr('title');
      		 $('#inputJavaHome').val('').attr('disabled',true);
      	 }else{
      		 if(cluster_State==com.impetus.ankush.constants.stateError){
       			$('#inputJavaHome').attr('disabled',false).val(setupDetailData.javaConf.homeDir).removeAttr('title');
       		 }else{
       			$('#inputJavaHome').val(setupDetailData.javaConf.homeDir).removeAttr('title');
       		 }
	   		 $('#oracleBundleCheck').removeAttr('checked');  
      	 }
      	
      	 $('#inputUserName').val(setupDetailData.authConf.username).removeAttr('title');
      	 com.impetus.ankush.hybridClusterCreation.userNameOnChange();
      	 if(undefined==setupDetailData.authConf.privateKey || setupDetailData.authConf.privateKey==null ||setupDetailData.authConf.privateKey==''){
      		$('#passwordAuthDiv').show();
      		 $('#sharedKeyFileUploadDiv').hide();
      		 $('#btnSharedKey').removeClass('active');
     		 $('#btnPassword').addClass('active');
      		 $('#inputPassword').val(setupDetailData.authConf.password).removeAttr('title');
      	 }else{
      		 $('#sharedKeyFileUploadDiv').show();
      		 $('#passwordAuthDiv').hide();
      		 $('#btnPassword').removeClass('active');
     		 $('#btnSharedKey').addClass('active');
      	 }
      	 if(setupDetailData.installationType=="SUDO"){
      		sudoFlag=true;
      		$("#btnSudoNode").addClass('active');
      		$("#btnNonSudoNode").removeClass('active');
      	 }else{
      		com.impetus.ankush.hybridClusterCreation.nodeRightToggle('');  
      		sudoFlag=false;
      		$("#btnSudoNode").removeClass('active');
      		$("#btnNonSudoNode").addClass('active');
      	 }
      	 if(currentClusterId==null){
      		 com.impetus.ankush.hybridClusterCreation.userNameOnChange();  
      	 }
     	$('#agentInstallDir').val(setupDetailData.components.Agent.installPath).removeAttr('title');
      	 if(setupDetailData.hostFile==null ||setupDetailData.hostFile==''){
      	   $('#fileUploadBtn').removeClass('active');
 		   $('#ipRangeBtn').addClass('active');
 		   $('#ipRangeDiv').show();
 		   $('#inputIpRange').val(setupDetailData.hosts).removeAttr('title');
     	   }else{
     		  $('#ipRangeBtn').removeClass('active');
    		   $('#fileUploadBtn').addClass('active');
    		   $('#fileUploadDiv').show();
    		   $('#filePath').val(setupDetailData.hostFile).removeAttr('title');
    		   $('#ipRangeDiv').hide();
     		   $('#fileUploadDiv').hide();
     	   }
      	  if(nodesTable!=null){
      		 nodesTable.fnClearTable();
      	  }
      	enabledNodes=new Array();
      	   nodeStatus={
      	    		nodes:new Array(),
      	    };
      	 if(!(cluster_State==com.impetus.ankush.constants.stateError)){
      		 nodesTable.fnSetColumnVis(3, false);
      		 nodesTable.fnSetColumnVis(4, false);
      		 nodesTable.fnSetColumnVis(5, false);
      		 nodesTable.fnSetColumnVis(6, false);
      		 nodesTable.fnSetColumnVis(8, false);	
      		 nodesTable.fnSetColumnVis(9, false);
      		 nodesTable.fnSetColumnVis(10, false);	
      	   }
      	   var i=0;
      	 if(undefined!=setupDetailData.nodes){
      		for ( var key in setupDetailData.nodes) {
 				var addId = null;
 				 var nodeObj = new Array();
 				 nodeRole={
			        		'ip':'',
			        		'role':{},
			        		'roles':{},
			        };
 					nodeObj.push(key);
   					nodeObj.push('true');
   					nodeObj.push('true');
   					nodeObj.push('true');
   					nodeObj.push(setupDetailData.nodes[key].os);
   					nodeObj.push('');
   					nodeObj.push('');
   					nodeObj.push('');
   					nodeObj.push('');
   					nodeObj.push('');
   					nodeObj.push('');
   					nodeObj.push(sudoFlag);
   					nodeStatus.nodes.push(nodeObj);
   					nodeRole.ip=key;
   			com.impetus.ankush.hybridClusterCreation.nodeRoleInitialize();
   			nodeRoleArray.push(nodeRole);
   			enabledNodes.push(key);
   			var nodeRoleString="";
   			nodeRoleString=com.impetus.ankush.hybridClusterCreation.roleStringConvert(setupDetailData.nodes[key].roles);
   			var nodeRight="Non-Sudo";
			if(setupDetailData.installationType=="SUDO"){
				nodeRight="Sudo";
			}
   			addId = nodesTable.fnAddData([
					                        '<input type="checkbox" class="hide-div inspect-node" style="display:none">',
											key,
											'<a class="" id="osName'
												+ i + '">'+setupDetailData.nodes[key].os+'</a>',
											'<a class="" id="cpuCount'
    											+ i + '">-</a>',
											'<a class="" id="diskCount'
    											+ i + '">-</a>',
											'<a class="" id="diskSize'
    											+ i + '">-</a>',
											'<a class="" id="ramSize'
    											+ i + '">-</a>',
											'<a class="" id="nodeRole'+i+'">'+nodeRoleString+'</a>',
											'<span class="nodeRight" id="nodeRight'+i+'">'+nodeRight+'</span>',
											'<a class="" >-</a>',
											'<div class="text-center"><a href="##" onclick="com.impetus.ankush.hybridSetupDetail.pageDetailValue('
												+ i
												+ ',this);"><img id="navigationImg-'
												+ i
												+ '" src="'
												+ baseUrl
												+ '/public/images/icon-chevron-right.png" /></a></div>' ]);
   				com.impetus.ankush.hybridSetupDetail.nodeRoleMapSetupDetailPage(setupDetailData.nodes[key]);
				var theNode = nodesTable.fnSettings().aoData[addId[0]].nTr;
				theNode.setAttribute('id', 'node'+ key.split('.').join('_'));
				if (Object.keys(setupDetailData.nodes[key].errors).length>0 ){
					rowId = key.split('.').join('_');
					$('td', $('#node'+rowId)).addClass('error-row');
					$('#node' + rowId).addClass('error-row');
				}
				i++;
 			}
      		nodesTable.fnSetColumnVis(10, false);	
		}
      	com.impetus.ankush.hybridSetupDetail.nodeObjCreateSetupDetailPage(setupDetailData.nodes);
      	 $('#techNodeHead').prop('checked',false);
      	 var i=0;
      	$(".nodeCheckBoxTech").removeAttr("checked");
      	$(".confTech").removeClass("btn-primary btn-danger").attr("disabled","disabled");
      	$(".nodeMap").removeClass("btn-primary btn-danger").attr("disabled","disabled");;
      	 $(".registerBtn").attr("disabled","disabled");	
		 $(".deployBtn").attr("disabled","disabled");
      	 for(var key in setupDetailData.components){
      		 var tech=key;
      		if(key=='Agent'){
      			continue;
      		}
				 if(key=='Hadoop2'){
					 tech="Hadoop";
				 }
				 $('#nodeCount'+tech).text(setupDetailData.components[key].count);
				 $('#nodeCheck'+tech).prop('checked',true);
				 $("#confPage"+tech).removeAttr("disabled");
				 $("#nodeMap"+tech).removeAttr("disabled");
				 if(!setupDetailData.components[key].register){
					 $("#deployToggle"+tech).removeAttr("disabled").addClass("active");	
					 $("#registerToggle"+tech).removeAttr("disabled").removeClass("active");
					 $("#nodeMap"+tech).removeAttr("disabled");
				 }else{
					 $("#registerToggle"+tech).removeAttr("disabled").addClass("active");
					 $("#deployToggle"+tech).removeAttr("disabled").removeClass("active");	
					 $("#nodeMap"+tech).attr("disabled","disabled");
					
				 }
			     $("#confType"+tech).text(setupDetailData.components[key].confState);
			     if(undefined == setupDetailData.components[key].count || setupDetailData.components[key].count==0){
			    	 $("#nodeMap"+tech).addClass("btn-danger"); 
			     }else{
			    	 $('#nodeMap'+tech).removeClass('btn-danger');
			     }
			     if(setupDetailData.components[key].confState=='-'){
			    	 $("#confPage"+tech).addClass("btn-danger");		
					}else if(setupDetailData.components[key].confState=='Default'){
						 $("#confPage"+tech).removeClass('btn-danger').addClass("btn-primary");	
					}
					else{
						 $("#confPage"+tech).removeClass('btn-danger btn-primary');
					}
				 i++;
			 }
      	 if(i==$('.nodeCheckBoxTech').length){
      		 $('#techNodeHead').prop('checked',true);
      	 }
      	if(cluster_State !=com.impetus.ankush.constants.stateError){
      		$('.deployBtn').attr('disabled',true);
			$('.registerBtn').attr('disabled',true);
      		$('#techNodeHead').attr('disabled','disabled');
          	$('.nodeCheckBoxTech').attr('disabled','disabled');
      	}
		com.impetus.ankush.hybridSetupDetail.redeployDataFunctionMap();
		},
		
		redeployPagePopulate:function(currentClusterId,clusterState){
			cluster_State=clusterState;
			$("#showLoading").removeClass("element-hide");
			var url = baseUrl + '/monitor/' + currentClusterId + '/details';
			 com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
				 $("#showLoading").addClass("element-hide");
                	setupDetailData=result.output;
                	com.impetus.ankush.hybridSetupDetail.loadPageData();
			   });
		},
	
		redeployDataFunctionMap:function(){
			
			for ( var key in setupDetailData.components) {
				if(key=='Agent'){
	      			continue;
	      		}
				if(key.split("_")[0]=="Zookeeper"){
					if(!setupDetailData.components[key].register){
						com.impetus.ankush.hybrid_Zookeeper.redeployDataPrepare(key);
					}else{
						com.impetus.ankush.register_Zookeeper.redeployDataPrepare(key);
					}
				}
			}
		
			if(undefined !=setupDetailData.components.Cassandra){
				if(!setupDetailData.components.Cassandra.register){
					com.impetus.ankush.hybrid_Cassandra.redeployDataPrepare();
				}else{
					com.impetus.ankush.register_Cassandra.redeployDataPrepare();
				}
			}
			
			if(undefined !=setupDetailData.components.Hadoop){
				if(!setupDetailData.components.Hadoop.register){
					com.impetus.ankush.hybrid_Hadoop.redeployDataPrepare();	
				}else{
					com.impetus.ankush.register_Hadoop.redeployDataPrepare();	
				}
			}
			
			if(undefined !=setupDetailData.components.Ganglia){
				if(!setupDetailData.components.Ganglia.register){
					com.impetus.ankush.hybrid_Ganglia.redeployDataPrepare();
				}else{
					com.impetus.ankush.register_Ganglia.redeployDataPrepare();
				}
			}
		
			
		},
		
		nodeObjCreateSetupDetailPage:function(techData){
			zookeeperNodesMap = {};
			zookeeperNodes=new Array();
			zookeeperNodeObj=new Array();
			gangliaNodesObj.gmetadNode={};
			gangliaNodeMap = {};
			hadoopNodesObj.NameNode={};
			hadoopNodesObj.SecondaryNameNode={};
			hadoopNodesObj.ResourceManager={};
			hadoopNodesObj.JobHistoryServer={};
			hadoopNodesObj.slaves=new Array();
			hadoopNodesObj.WebAppProxyServer={};
			hadoopNodesObj.standByNamenode={};
			hadoopNodesObj.JournalNode=new Array();
			cassandraNodesObj.nodes=new Array();
			cassandraNodeMap={};
			for(var host in techData){
				for(var techName in techData[host].roles){
					var tech=techName.split('_')[0];
					switch (techName) {
			    
					
					
					case 'Cassandra':
						for(var a=0;a<techData[host].roles[tech].length;a++){
							var cassandraNode = {};
							if(techData[host].roles[tech][a].indexOf("CassandraSeed")!=-1){
								cassandraNode.publicIp = techData[host].host;
								cassandraNode.privateIp = techData[host].host;
								cassandraNode.os = techData[host].os;
								cassandraNode.vNodeCount = setupDetailData.components.Cassandra.nodes[techData[host].host].vNodeCount;
								cassandraNodesObj.nodes.push(cassandraNode);
							}
							if(techData[host].roles[tech][a].indexOf("CassandraNonSeed")!=-1){
								cassandraNode.publicIp = techData[host].host;
								cassandraNode.privateIp = techData[host].host;
								cassandraNode.os = techData[host].os;
								cassandraNode.vNodeCount = setupDetailData.components.Cassandra.nodes[techData[host].host].vNodeCount;
								cassandraNodesObj.nodes.push(cassandraNode);
							}
							cassandraNodeMap[cassandraNode.publicIp] = cassandraNode;
						}
						setupDetailData.components[tech].count = Object.keys(cassandraNodeMap).length;
						break;
					
					case 'Ganglia':
						for(var a=0;a<techData[host].roles[tech].length;a++){
							if(techData[host].roles[tech][a].indexOf("GangliaMaster")!=-1){
								gangliaNodesObj.gmetadNode.publicIp = techData[host].host;
								gangliaNodesObj.gmetadNode.privateIp = techData[host].host;
								gangliaNodesObj.gmetadNode.os = techData[host].os;
								gangliaNodeMap[gangliaNodesObj.gmetadNode.publicIp] = gangliaNodesObj.gmetadNode;
							}
						}
						setupDetailData.components[tech].count = Object.keys(gangliaNodeMap).length;
						break;
						
				
					case 'Hadoop':
						if(techData[host].roles[tech].length>0){
								if(techData[host].roles[tech].indexOf("NameNode")!=-1){
									if(undefined != setupDetailData.components.Hadoop.advanceConf.standByNamenode && setupDetailData.components["Hadoop"].advanceConf.standByNamenode==techData[host].host){
										hadoopNodesObj.standByNamenode.publicIp =  techData[host].host;
										hadoopNodesObj.standByNamenode.privateIp =  techData[host].host;
										hadoopNodesObj.standByNamenode.os = techData[host].os;
										hadoopNodeMap[hadoopNodesObj.standByNamenode.publicIp]={};
									}else{
										hadoopNodesObj.NameNode.publicIp = techData[host].host;
										hadoopNodesObj.NameNode.privateIp = techData[host].host;
										hadoopNodesObj.NameNode.os = techData[host].os;
										if (hadoopNodesObj.NameNode != null
												&& hadoopNodesObj.NameNode.publicIp != undefined
												&& hadoopNodesObj.NameNode.publicIp != null) {
											hadoopNodeMap[hadoopNodesObj.NameNode.publicIp] = {};
										}
									}
								}
								if(techData[host].roles[tech].indexOf("SecondaryNameNode")!=-1){
									hadoopNodesObj.SecondaryNameNode.publicIp = techData[host].host;
									hadoopNodesObj.SecondaryNameNode.privateIp = techData[host].host;
									hadoopNodesObj.SecondaryNameNode.os = techData[host].os;
									hadoopNodeMap[hadoopNodesObj.SecondaryNameNode.publicIp] = {};
								}
								if(techData[host].roles[tech].indexOf("JobTracker")!=-1){
									hadoopNodesObj.JobTracker.publicIp = techData[host].host;
									hadoopNodesObj.JobTracker.privateIp = techData[host].host;
									hadoopNodesObj.JobTracker.os = techData[host].os;
									hadoopNodeMap[hadoopNodesObj.JobTracker.publicIp] =  {};
								}
								if(techData[host].roles[tech].indexOf("ResourceManager")!=-1){
									hadoopNodesObj.ResourceManager.publicIp = techData[host].host;
									hadoopNodesObj.ResourceManager.privateIp = techData[host].host;
									hadoopNodesObj.ResourceManager.os = techData[host].os;
									hadoopNodeMap[hadoopNodesObj.ResourceManager.publicIp] = {};
								}
								if(techData[host].roles[tech].indexOf("JobHistoryServer")!=-1){
									hadoopNodesObj.JobHistoryServer.publicIp =  techData[host].host;
									hadoopNodesObj.JobHistoryServer.privateIp =  techData[host].host;
									hadoopNodesObj.JobHistoryServer.os = techData[host].os;
									hadoopNodeMap[hadoopNodesObj.JobHistoryServer.publicIp] = {};
								}
								if(techData[host].roles[tech].indexOf("WebAppProxyServer")!=-1){
									hadoopNodesObj.WebAppProxyServer.publicIp =  techData[host].host;
									hadoopNodesObj.WebAppProxyServer.privateIp = techData[host].host;
									hadoopNodesObj.WebAppProxyServer.os = techData[host].os;
									hadoopNodeMap[hadoopNodesObj.WebAppProxyServer.publicIp] =  {};
								}
								if(techData[host].roles[tech].indexOf("JournalNode")!=-1){
									var journalNode = {};
									journalNode.publicIp = techData[host].host;
									journalNode.privateIp = techData[host].host;
									journalNode.os = techData[host].os;
									hadoopNodesObj.JournalNode.push(journalNode);
									hadoopNodeMap[journalNode.publicIp] =  {};
								}
								if(techData[host].roles[tech].indexOf("DataNode")!=-1){
									var slaveNode = {};
									slaveNode.publicIp = techData[host].host;
									slaveNode.privateIp = techData[host].host;
									slaveNode.os = techData[host].os;
									hadoopNodesObj.slaves.push(slaveNode);
									hadoopNodeMap[slaveNode.publicIp] = {};
									}
							setupDetailData.components[tech].count = Object.keys(hadoopNodeMap).length;
						}
					break;
					
					default:
							if(techName.split("_").length>1){
								zookeeperNodesMap1 = {};
								zookeeperNodes1=new Array();
								zookeeperNodeObj1=new Array();
								if(techData[host].roles[techName].length>0){
										if(techData[host].roles[techName].indexOf("Zookeeper")!=-1){
											var zkNode = {};
											zkNode.publicIp = techData[host].host;
											zkNode.privateIp = techData[host].host;
											zkNode.os =  techData[host].os;
											zookeeperNodeObj1.push(zkNode);
											zookeeperNodes1.push(techData[host].host);
											zookeeperNodesMap1[techData[host].host] = {};	
											if(undefined==zookeeperObjMap[techName]){
												zookeeperObjMap[techName]={
														zookeeperNodeObj:new Array(),
														zookeeperNodesMap:{},
												};
											}
											zookeeperObjMap[techName].zookeeperNodeObj=zookeeperNodeObj1;
											zookeeperObjMap[techName].zookeeperNodesMap[techData[host].host]={};
											setupDetailData.components[techName].count = zookeeperNodes1.length;
										}
								}
								
							}
						}
					}
			}
	},
	//function to modify nodes object(removing all keys from unused node object)
		hadoopDataReprepare:function(techData){
			if(techData.componentVersion.split(".")[0]!='2'){
				setupDetailData.components.Hadoop2.ResourceManager={};
			}
			if(!techData.webAppProxyServerEnabled){
				setupDetailData.components.Hadoop2.WebAppProxyServer={};
			}
			if(!techData.jobHistoryServerEnabled){
				setupDetailData.components.Hadoop2.JobHistoryServer={};
			}
			if(!techData.haEnabled){
				setupDetailData.components.Hadoop2.standByNamenode={};
			}
		},
		nodeRoleMapSetupDetailPage:function(nodeObj){
			if (nodeObj==null){
				return;
			}
			for(var key in nodeObj.roles){
				var roleArr=new Array();
				roleArr= nodeObj.roles[key];
				nodeRole.roles[key]=roleArr;
				for(var i=0;i<nodeObj.roles[key].length;i++){
					switch (nodeObj.roles[key][i]) {
					case 'Zookeeper':
						nodeRole.role.Zookeeper=1;
						break;
					case 'GangliaMaster':
						nodeRole.role.GangliaMaster=1;
						break;
					case 'CassandraSeed':
						nodeRole.role.CassandraSeed=1;
						break;
					case  'CassandraNonSeed':
						nodeRole.role.CassandraNonSeed=1;
						break;
					case 'NameNode':
						nodeRole.role.NameNode=1;
						break;
					case 'SecondaryNameNode':
						nodeRole.role.SecondaryNameNode=1;
						break;
					case 'ResourceManager':
						nodeRole.role.ResourceManager=1;
						break;
					case 'JobHistoryServer':
						nodeRole.role.JobHistoryServer=1;
						break;
					case 'WebAppProxyServer':
						nodeRole.role.WebAppProxyServer=1;
						break;
					case 'JournalNode':
						nodeRole.role.JournalNode=1;
						break;
					case 'DataNode':
						nodeRole.role.DataNode=1;
						break;
					case 'Hadoop':
						nodeRole.role.Hadoop=1;
				
					default:
						break;
					}
				}
			}
		},
		
		setupDetailValuePopulate:function(currentClusterId,clusterState){
			if(clusterState==com.impetus.ankush.constants.stateError){
				$('#selectTemplate').attr('disabled','disabled');
				$('#clusterDeploy').text('Redeploy');
				$('#clusterDeploy').val(currentClusterId);
				$('#commonDeleteButton').removeAttr('disabled');
				$('#nodeListDiv').show();
				com.impetus.ankush.hybridSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
				com.impetus.ankush.hybridClusterCreation.tooltipInitialize();
				return;	
			}else{
				 $('#clusterDeploy').removeAttr('disabled');
			}
			com.impetus.ankush.common.pageStyleChange();
			$('#clusterDeploy').attr('disabled',true);   
			$('#btnPassword').attr('disabled',true);
			$('#btnSharedKey').attr('disabled',true);
			$('#ipRangeBtn').attr('disabled',true);
			$('#btnSudoNode').attr('disabled',true);
			$('#btnNonSudoNode').attr('disabled',true);
			$('#fileUploadBtn').attr('disabled',true);
			$('.envRadio').attr('disabled',true);
			$('#deployToggle').attr('disabled',true);
			$('#registerToggle').attr('disabled',true);
			$('#nodeRetrieveBtn').attr('disabled','disabled');
			$("#nodeListDiv").show();
			if (nodesTable != null) {
				nodesTable.fnClearTable();
			}
			com.impetus.ankush.hybridSetupDetail.redeployPagePopulate(currentClusterId,clusterState); 
		},
	
		pageDetailValue:function(node,elem){
			$(".navigationImg").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-right");
			var nodeIp=$("td:nth-child(2)", $(elem).parents('tr')).text();
			var nodeIpNew=nodeIp.split('.').join('_');
			if(!$("#node_"+nodeIpNew).hasClass("open")){
				$("#navigationImg-"+node).removeClass("glyphicon-chevron-right").addClass("glyphicon-chevron-down");
				var page='/ankush/hybrid-cluster/retrievedNodeDetail/'+nodeIpNew;
				$(".nodeDiv").remove();
				$("#node"+nodeIpNew).after('<tr class="nodeDiv"><td id="node_'+nodeIpNew+'" class="open" colspan="9" style="height:auto !important"></td></tr>');
				jQuery("#node_"+nodeIpNew).load(page, function() {
					$('#nodeChildPage').text(nodeIp);
					com.impetus.ankush.hybridSetupDetail.inspectNodeFunction();
					$('#os').html('').text($("td:nth-child(3)", $(elem).parents('tr')).text());
					$('#hybridCpu').html('').text($("td:nth-child(4)", $(elem).parents('tr')).text());
					$('#hybridDiskCount').html('').text($("td:nth-child(5)", $(elem).parents('tr')).text());
					$('#hybridDiskSize').html('').text($("td:nth-child(6)", $(elem).parents('tr')).text());
					$('#hybridRam').html('').text($("td:nth-child(7)", $(elem).parents('tr')).text());
					if(undefined==setupDetailData || setupDetailData==null || setupDetailData.nodes.length==0){
						$('#isAuthenticated').html('').text('No');
						$('#nodeStatus').html('').text(nodeStatus.nodes[node][11]);
						if(nodeStatus.nodes[node][1] && nodeStatus.nodes[node][2] && nodeStatus.nodes[node][3]){
							$('#isAuthenticated').html('').text('Yes');
						}
					}else{
						node=$("td:nth-child(2)", $(elem).parents('tr')).text();
						$('#isAuthDiv').hide();
						$('#nodeStatus').html('').text(setupDetailData.nodes[node].message);
						if(cluster_State == com.impetus.ankush.constants.stateError){
							if(Object.keys(setupDetailData.nodes[node].errors).length>0){
								$('#errorNodeDiv').show();
		          		 }
		          			for(var key in setupDetailData.nodes[node].errors){
		          				$('#nodeDeploymentError').append('<label class="text-left" style="color: black;" id="'+key+'">'+setupDetailData.nodes[node].errors[key]+'</label>');
		          	        }
						}
					}
				});
			}else{
				$(".nodeDiv").remove();
			}
			
			
		},
		inspectNodeFunction:function(){
			$('.validation-inspect-node').hide();
			validationTable = $('#validationTable').dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : true,
				"bFilter" : true,
				"bSort" : false,
				"bInfo" : false,
				"bAutoWidth" : false,
				"sPaginationType" : "full_numbers",
				"bAutoWidth" : false,
				"bRetrieve" : true,
				"oLanguage" : {
					"sEmptyTable" : 'Loading...',
				}
			});
			$("#validationTable_filter").css({
				'display' : 'none'
			});
			$('#searchvalidationTable').keyup(
					function() {
						$("#validationTable").dataTable().fnFilter(
								$(this).val());
					});
			if (Object.keys(inspectNodeData).length != 0) {
				$('.validation-inspect-node').show();
				var ip = $('#nodeChildPage').text();
				if (undefined == inspectNodeData[ip]) {
					$('.validation-inspect-node').hide();
					return;
				}
				var classInspect = {
					"Ok" : 'text-success',
					"Critical" : 'text-error',
					"Warning" : 'text-error'
				};
				if (inspectNodeData.status == true) {
					for ( var key in inspectNodeData[ip]) {
						validationTable
								.fnAddData([
										inspectNodeData[ip][key].label,
										'<span class="'+classInspect[inspectNodeData[ip][key].status]+'">'
												+ inspectNodeData[ip][key].status
												+ '</span>',
										inspectNodeData[ip][key].message ]);
					}
				} else {
					validationTable.fnSettings().oLanguage.sEmptyTable = inspectNodeData.error[0];
					validationTable.fnClearTable();
				}
			}
		},
		/*Function for loading node detail page*/
		 loadConfigPage: function(technology, key) {
		    	if(undefined==key){
		    	key=technology;
		    	}
	
	    	var tech=key.split(' ').join('_');
	    
	    	 com.impetus.ankush.hybridClusterCreation.dynamicRowCreate(technology,key);
		  
			
		},
		 loadMapNodePage: function(technology,key) {
			
				if(undefined==key){
			    	key=technology;
			    	}
		    
		    	
		    	var tech=key.split(' ').join('_');
		    	if(!$('#nodeCheck'+tech).is(':checked')){
		    		return;
		    	}
		    	 com.impetus.ankush.hybridClusterCreation.dynamicRowCreateNodes(technology,key);
			},
};
