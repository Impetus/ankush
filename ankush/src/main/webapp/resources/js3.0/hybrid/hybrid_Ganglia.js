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
com.impetus.ankush.hybrid_Ganglia = {	
		gangliaConfigPopulate : function() {
			$('#gridName').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gridName);
			$('#gangliaClustername').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gangliaClustername);
			$('#gmondConfPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gmondConfPath);
			$('#gmetadConfPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gmetadConfPath);
			$('#pollingInterval').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.pollingInterval);
			$('#gangliaPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gangliaPort);
			$('#rrdFilePath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.rrdFilePath);
			if (setupDetailData == null || undefined == setupDetailData.components.Ganglia) {
			var gangliaData = jsonDataHybrid.hybrid.Ganglia;
				if (Object.keys(gangliaObj.Defaults).length > 1) {
					gangliaData.Defaults.register=gangliaObj.Defaults.register;
					gangliaData.Defaults.gridName=gangliaObj.Defaults.gridName;
					gangliaData.Defaults.gangliaClustername=gangliaObj.Defaults.gangliaClustername;
					gangliaData.Defaults.gmondConfPath=gangliaObj.Defaults.gmondConfPath;
					gangliaData.Defaults.gmetadConfPath=gangliaObj.Defaults.gmetadConfPath;
					gangliaData.Defaults.pollingInterval=gangliaObj.Defaults.pollingInterval;
					gangliaData.Defaults.gangliaPort=gangliaObj.Defaults.gangliaPort;
					gangliaData.Defaults.rrdFilePath=gangliaObj.Defaults.rrdFilePath;
				}
				
						$('#gridName').val(gangliaData.Defaults.gridName);
						$('#gangliaClustername').val(gangliaData.Defaults.gangliaClustername);
						if(gangliaData.Defaults.gangliaClustername==""){
							$('#gangliaClustername').val($('#inputClusterName').val());
						}
						$('#gmondConfPath').val(gangliaData.Defaults.gmondConfPath);
						$('#gmetadConfPath').val(gangliaData.Defaults.gmetadConfPath);
						$('#pollingInterval').val(gangliaData.Defaults.pollingInterval);
						$('#gangliaPort').val(gangliaData.Defaults.gangliaPort);
						$('#rrdFilePath').val(gangliaData.Defaults.rrdFilePath);
						var user = $.trim($('#inputUserName').val());
						if (user == '') {
							return;
						}
						var gmondConfPath = $.trim($('#gmondConfPath').val()).split(userName).join('/home/' + user);
						var gmetadConfPath = $.trim($('#gmetadConfPath').val()).split(userName).join('/home/' + user);
						var rrdFilePath = $.trim($('#rrdFilePath').val()).split(userName).join('/home/' + user);
						userName = '/home/' + user;
						$('#gmondConfPath').empty().val(gmondConfPath);
						$('#gmetadConfPath').empty().val(gmetadConfPath);
						$('#rrdFilePath').empty().val(rrdFilePath);
			}else {
				if (cluster_State != com.impetus.ankush.constants.stateError) {
					com.impetus.ankush.common.pageStyleChange();
					$('.btnGrp').attr('disabled', 'disabled');
					$('#revertGanglia').attr('disabled', 'disabled');
					$('#applyGanglia').attr('disabled', 'disabled');
				}
				if (Object.keys(gangliaObj.Defaults).length==1) {
					gangliaObj = jsonDataHybrid.hybrid.Ganglia;
					com.impetus.ankush.hybrid_Ganglia.redeployDataPrepare();
				}
				$('#gridName').val(gangliaObj.Defaults.gridName);
				$('#gangliaClustername').val(gangliaObj.Defaults.gangliaClustername);
				$('#gmondConfPath').val(gangliaObj.Defaults.gmondConfPath);
				$('#gmetadConfPath').val(gangliaObj.Defaults.gmetadConfPath);
				$('#pollingInterval').val(gangliaObj.Defaults.pollingInterval);
				$('#gangliaPort').val(gangliaObj.Defaults.gangliaPort);
				$('#rrdFilePath').val(gangliaObj.Defaults.rrdFilePath);
			}
		},	
		redeployDataPrepare : function() {
			if (undefined == gangliaObj.Defaults.register) {
				gangliaObj = jsonDataHybrid.hybrid.Ganglia;
			}
			gangliaObj.Defaults.register = setupDetailData.components.Ganglia.advanceConf.register;
			gangliaObj.Defaults.gridName = setupDetailData.components.Ganglia.advanceConf.gridName;
			gangliaObj.Defaults.gangliaClustername = setupDetailData.components.Ganglia.advanceConf.gangliaClustername;
			gangliaObj.Defaults.gmondConfPath = setupDetailData.components.Ganglia.advanceConf.gmondConfPath;
			gangliaObj.Defaults.gmetadConfPath = setupDetailData.components.Ganglia.advanceConf.gmetadConfPath;
			gangliaObj.Defaults.pollingInterval = setupDetailData.components.Ganglia.advanceConf.pollingInterval;
			gangliaObj.Defaults.gangliaPort = setupDetailData.components.Ganglia.advanceConf.gangliaPort;
			gangliaObj.Defaults.rrdFilePath = setupDetailData.components.Ganglia.advanceConf.rrdFilePath;
		},
		gangliaConfigValidate:function(){
			$('#validateErrorGanglia').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMainGanglia").html('').hide();
	        errorCount = 0;
				  if(!com.impetus.ankush.validation.empty($('#gridName').val())){
		                errorCount++;
		                errorMsg = 'Grid Name field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('gridName','Grid Name cannot be empty');
		                var divId='gridName';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('gridName',com.impetus.ankush.tooltip.gangliaClusterCreation.gridName);
		            }
				  if(!com.impetus.ankush.validation.empty($('#gangliaClustername').val())){
		                errorCount++;
		                errorMsg = 'Ganglia Cluster Name field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('gangliaClustername','Ganglia Cluster Name cannot be empty');
		                var divId='gangliaClustername';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            } else if ($('#gangliaClustername').val().trim().split(' ').length > 1) {
			            errorCount++;
			            errorMsg = "Ganglia Cluster Name can't contain blank spaces.";
			            com.impetus.ankush.common.tooltipMsgChange('gangliaClustername','Ganglia Cluster Name cannot contain blank spaces');
			            var divId='gangliaClustername';
			            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
			        }else if (!com.impetus.ankush.validation.alphaNumericCharWithoutHyphen($('#gangliaClustername').val())) {
			            errorCount++;
			            errorMsg = "Ganglia Cluster Name can contain only alphabets, numeric, dot and underscore";
			            com.impetus.ankush.common.tooltipMsgChange('gangliaClustername',errorMsg);
			            var divId='gangliaClustername';
			            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
			        }
				  else{
		                com.impetus.ankush.common.tooltipOriginal('gangliaClustername',com.impetus.ankush.tooltip.gangliaClusterCreation.gangliaClustername);
		            }
				  if(!com.impetus.ankush.validation.empty($('#gmondConfPath').val())){
		                errorCount++;
		                errorMsg = 'Gmond Conf Path field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('gmondConfPath','Gmond Conf Path cannot be empty');
		                var divId='gmondConfPath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else  if(!com.impetus.ankush.validation.withoutSpace($('#gmondConfPath').val())){
		                errorCount++;
		                errorMsg = 'Gmond Conf Path field contains space.';
		                com.impetus.ankush.common.tooltipMsgChange('gmondConfPath','Gmond Conf Path cannot contain space');
		                var divId='gmondConfPath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('gmondConfPath',com.impetus.ankush.tooltip.gangliaClusterCreation.gmondConfPath);
		            }
				  if(!com.impetus.ankush.validation.empty($('#gmetadConfPath').val())){
		                errorCount++;
		                errorMsg = 'Gmetad Conf Path field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('gmetadConfPath','Gmetad Conf Path cannot be empty');
		                var divId='gmetadConfPath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else  if(!com.impetus.ankush.validation.withoutSpace($('#gmetadConfPath').val())){
		                errorCount++;
		                errorMsg = 'Gmetad Conf Path field contains space.';
		                com.impetus.ankush.common.tooltipMsgChange('gmetadConfPath','Gmetad Conf Path cannot contain space');
		                var divId='gmetadConfPath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('gmetadConfPath',com.impetus.ankush.tooltip.gangliaClusterCreation.gmetadConfPath);
		            }
				  if(!com.impetus.ankush.validation.empty($('#pollingInterval').val())){
		                errorCount++;
		                errorMsg = 'Polling Interval field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('pollingInterval','Polling Interval cannot be empty');
		                var divId='pollingInterval';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else if (!com.impetus.ankush.validation.numeric($('#pollingInterval').val())) {
		                errorCount++;
		                errorMsg = 'Polling Interval field must be numeric';
		                com.impetus.ankush.common.tooltipMsgChange('pollingInterval','Polling Interval must be numeric');
		                var divId='pollingInterval';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('pollingInterval',com.impetus.ankush.tooltip.gangliaClusterCreation.pollingInterval);
		            }
				  if(!com.impetus.ankush.validation.empty($('#gangliaPort').val())){
		                errorCount++;
		                errorMsg = 'Ganglia Port field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('gangliaPort','Ganglia Port cannot be empty');
		                var divId='gangliaPort';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else if (!com.impetus.ankush.validation.numeric($('#gangliaPort').val())) {
		                errorCount++;
		                errorMsg = 'Ganglia Port field must be numeric';
		                com.impetus.ankush.common.tooltipMsgChange('gangliaPort','Ganglia Port must be numeric');
		                var divId='gangliaPort';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            } else if (!com.impetus.ankush.validation.oPort($('#gangliaPort').val())) {
		                errorCount++;
		                errorMsg = 'Ganglia Port field must be between 1024-65535';
		                com.impetus.ankush.common.tooltipMsgChange('gangliaPort','Ganglia Port must be between 1024-65535');
		                var divId='gangliaPort';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            } else{
		                com.impetus.ankush.common.tooltipOriginal('gangliaPort',com.impetus.ankush.tooltip.gangliaClusterCreation.Port);
		            }
				  if(!com.impetus.ankush.validation.empty($('#rrdFilePath').val())){
		                errorCount++;
		                errorMsg = 'RRD File Path field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('rrdFilePath','RRD File Path cannot be empty');
		                var divId='rrdFilePath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else if(!com.impetus.ankush.validation.withoutSpace($('#rrdFilePath').val())){
		                errorCount++;
		                errorMsg = 'RRD File Path field contains space.';
		                com.impetus.ankush.common.tooltipMsgChange('rrdFilePath','RRD File Path cannot contain space');
		                var divId='rrdFilePath';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('rrdFilePath',com.impetus.ankush.tooltip.gangliaClusterCreation.rrdFilePath);
		            }
				  if(errorCount>0 && errorMsg!=''){
					  $('#validateErrorGanglia').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
			        	  $("#errorDivMainGanglia").show();
			        }else{
			        	com.impetus.ankush.hybrid_Ganglia.gangliaConfigApply();
			        }
		},
		gangliaConfigApply:function(){
			gangliaObj={
					Defaults:{},
			};
			  if($("#toggleRegisterBtnGanglia .active").data("value")==0){
				  gangliaObj.Defaults.register=false;
			}else{
				gangliaObj.Defaults.register=true;
			}
				gangliaObj.Defaults.gridName=$('#gridName').val();
				gangliaObj.Defaults.gangliaClustername=$('#gangliaClustername').val();
				gangliaObj.Defaults.gmondConfPath=$('#gmondConfPath').val();
				gangliaObj.Defaults.gmetadConfPath=$('#gmetadConfPath').val();
				gangliaObj.Defaults.pollingInterval=$('#pollingInterval').val();
				gangliaObj.Defaults.gangliaPort=$('#gangliaPort').val();
				gangliaObj.Defaults.rrdFilePath=$('#rrdFilePath').val();
			$('#confTypeGanglia').text('Custom');
			$("#confPageGanglia").removeClass("btn-danger");
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},
		toggleSelect:function(btnId){
			
		},
		
		gangliaNodesPopulate:function(){
			if(gangliaNodeTable!=null){
				gangliaNodeTable.fnClearTable();
			}
			if(nodeStatus==null){
				return;
			}
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var addId = null;
					addId = gangliaNodeTable.fnAddData([
											nodeStatus.nodes[i][0],
											'<span class="" id="nodeRoleGanglia'+i+'">'+$('#nodeRole' + i).text()+'</span>',
											'<input id="gmetadNodeCheckBox'
												+ i
												+ '" class="gmetadNodeCheck" name="gmetadNodeRadio" style="margin-right:10px;" type="radio" onclick="">',
											nodeStatus.nodes[i][4],
									'' ]);
				var theNode = gangliaNodeTable.fnSettings().aoData[addId[0]].nTr;
				theNode.setAttribute('id', 'ganglia'+ nodeStatus.nodes[i][0].split('.').join('_'));
				var isSudo=false;
				if(sudoFlag){	
					if($("#nodeRight"+i).text()=="Sudo"){
					}else{
						isSudo=true;
					}
				}
				if (nodeStatus.nodes[i][1] == false	|| nodeStatus.nodes[i][2] == false|| nodeStatus.nodes[i][3] == false || isSudo) {
					rowId = nodeStatus.nodes[i][0].split('.').join('_');
					$('td', $('#ganglia'+rowId)).addClass('alert-danger');
					$('#ganglia' + rowId).addClass('alert-danger');
					$('#gmetadNodeCheckBox' + i).attr('disabled', true);
				}
			}
			if(setupDetailData==null || undefined==setupDetailData.components.Ganglia){
			if(nodeStatus.nodes.length>0 && Object.keys(gangliaNodesObj.gmetadNode).length==0){
				
			}else{
				for(var j=0;j<nodeStatus.nodes.length;j++){
	     			   if(gangliaNodesObj.gmetadNode.publicIp==nodeStatus.nodes[j][0]){
	     				   $('#gmetadNodeCheckBox'+j).attr('checked',true); 
	     				   break;
	     			   }
					}
			}
			
		}else{
			if(Object.keys(gangliaNodeMap).length>0){
				 for (var i = 0; i < nodeRoleArray.length; i++) {
	         			   if(gangliaNodesObj.gmetadNode.publicIp==nodeRoleArray[i].ip){
	         				   $('#gmetadNodeCheckBox'+i).attr('checked',true); 
	         			   }
				}
			}
			  if(cluster_State!=com.impetus.ankush.constants.stateError){
					$('#gangliaNodesApply').attr('disabled', true);
					 $('#gangliaNodesRevert').attr('disabled', true);
					  $('.gmetadNodeCheck').attr('disabled', true);
				}
			}
		},
		  loadGangliaNodeDetail:function(elem){
				$('#content-panel').sectionSlider('addChildPanel', {
					current : 'login-panel',
					url : baseUrl + '/hybrid-cluster/nodeMapNodeDetail',
					method : 'get',
					title : 'Node Detail',
					callback : function(data) {
						com.impetus.ankush.hybrid_Ganglia.gangliaNodeDetail(elem);
					},
					callbackData : {
					}
				});
			},
			gangliaNodeDetail:function(elem){
				$('#nodeHead').html($('td:first', $(elem).parents('tr')).text());
				$('#nodeType').html($("td:nth-child(2)", $(elem).parents('tr')).text());
				$('#os').html($("td:nth-child(4)", $(elem).parents('tr')).text());
				var rowIndex=$(elem).closest('td').parent()[0].sectionRowIndex;
				if(nodeStatus.nodes[rowIndex][1]==false){
					$('#nodeStatus').html('').text('Unavailable');
				}else if(nodeStatus.nodes[rowIndex][2]==false){
					$('#nodeStatus').html('').text('Unreachable');
				}else if(nodeStatus.nodes[rowIndex][3]==false){
					$('#nodeStatus').html('').text('Unauthenticated');
				}else{
					$('#nodeStatus').html('').text('Available');
				}
				if(Object.keys(gangliaNodeMap).length > 0){
					var gangliaNodeDetails = gangliaNodeMap[$('td:first', $(elem).parents('tr')).text()];
					if(gangliaNodeDetails.message !=undefined){
						$('#nodeStatus').html('').text(gangliaNodeDetails.message);	
					}
					if(gangliaNodeDetails.errors!=undefined && Object.keys(gangliaNodeDetails.errors).length>0){
						for(var key in gangliaNodeDetails.errors){
	          				$('#nodeDeploymentError').append('<label class="text-left" style="color: black;" id="'+key+'">'+gangliaNodeDetails.errors[key]+'</label>');
	          	        }
						$('#errorNodeDiv').show();
					}
				} 
			},
			
			gangliaNodesPopulateApplyValidate:function(){
				errorCount=0;
		    	errorMsg='';
		    	 $("#errorDivGangliaNodes").html('');
		            $('#validateErrorGanglia').html('');
		    	if(nodeStatus!=null){
	    	           
	    	            if ($('#gangliaNodeTable .gmetadNodeCheck:checked').length < 1) {
	    	            	errorMsg = 'Select at least one node as Master Node.';
	    	                errorCount++;
	    	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivGangliaNodes','',errorCount,errorMsg,'gangliaNodeTable');
	    	            }
		    		 if (errorCount > 0) {
		    	            $("#errorDivGangliaNodes").show();
		    	               if(errorCount > 1)
		    	            	   $('#validateErrorGanglia').show().html('Errors '+"<span class='badge'>"+errorCount+"</span>");
		    	            	   
		    	                else
		    	                	 $('#validateErrorGanglia').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
		    	        } else {
		    	            $("#errorDivGangliaNodes").hide();
		    	            $('#validateErrorGanglia').hide();
		    	            com.impetus.ankush.hybrid_Ganglia.gangliaNodesPopulateApply();
		    	        }
		    	}
				},
				
				gangliaNodesPopulateApply:function(){
					var nodeCount=0;
					gangliaNodeMap={};
					gangliaNodesObj.gmetadNode={};
					for ( var k = 0; k < nodeRoleArray.length; k++) {
						nodeRoleArray[k].role.GangliaMaster=0;
					}
					if(cluster_State!=com.impetus.ankush.constants.stateError){
						for ( var i = 0; i < nodeStatus.nodes.length; i++) {
							if($('#gmetadNodeCheckBox'+i).is(':checked')){
								nodeCount++;
								gangliaNodesObj.gmetadNode.publicIp=nodeStatus.nodes[i][0];
								gangliaNodesObj.gmetadNode.privateIp=nodeStatus.nodes[i][0];
								gangliaNodesObj.gmetadNode.os=nodeStatus.nodes[i][4];
								gangliaNodesObj.gmetadNode.type='GangliaMaster';
							}
						}
					}else{
						 $(".gmetadNodeCheck").each(function(elem) {
							 var  count=$(this).attr("id").split("gmetadNodeCheckBox")[1];
							  var rowIndex = gangliaNodeTable.fnGetPosition($("#gmetadNodeCheckBox"+ count).closest('tr')[0]);
							  var aData= gangliaNodeTable.fnGetData(rowIndex);
							 
								if($(this).is(':checked')) {
									gangliaNodesObj.gmetadNode.publicIp=aData[0];
									gangliaNodesObj.gmetadNode.privateIp=aData[0];
									gangliaNodesObj.gmetadNode.os=aData[aData.length-2];
									gangliaNodesObj.gmetadNode.type='GangliaMaster';
									console.log(gangliaNodesObj);
								}
						  });
						
					}
					com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
					for ( var i = 0; i < nodeStatus.nodes.length; i++) {
						var newArr=new Array();
						if(gangliaNodesObj.gmetadNode.publicIp==nodeStatus.nodes[i][0]){
							newArr.push("GangliaMaster");
							nodeRoleArray[i].role.GangliaMaster=1;
							nodeRoleArray[i].roles.Ganglia=newArr;
							gangliaNodeMap[gangliaNodesObj.gmetadNode.publicIp]=gangliaNodesObj.gmetadNode;
						}
					}
					$('#nodeMapGanglia').removeClass('btn-danger');
					$('#nodeCountGanglia').text(Object.keys(gangliaNodeMap).length);
				com.impetus.ankush.hybridClusterCreation.nodeRoleMap('nodeRole');
				},	
};
