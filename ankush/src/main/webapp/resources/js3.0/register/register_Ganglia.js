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
com.impetus.ankush.register_Ganglia = {	
		gangliaConfigPopulate : function() {
			$('#gmetadHost').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gmetadHost);
			$('#gmondConfPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gmondConfPath);
			$('#gmetadConfPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gmetadConfPath);
			$('#gangliaPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.gangliaPort);
			$('#registerLevel').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.gangliaClusterCreation.registerLevel);
			$('#registerLevel').html('');
			
			if (setupDetailData == null || undefined == setupDetailData.components.Ganglia) {
				
			var gangliaData = jsonDataHybrid.hybrid.Ganglia;
			for ( var key in gangliaData.Defaults.RegisterLevel){
				$("#registerLevel").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			if(undefined != gangliaObjReg.Defaults.gmetadHost){
					gangliaData.Defaults.register=gangliaObjReg.Defaults.register;
					gangliaData.Defaults.gmetadHost=gangliaObjReg.Defaults.gmetadHost;
					gangliaData.Defaults.gmondConfPath=gangliaObjReg.Defaults.gmondConfPath;
					gangliaData.Defaults.gmetadConfPath=gangliaObjReg.Defaults.gmetadConfPath;
					gangliaData.Defaults.gangliaPort=gangliaObjReg.Defaults.gangliaPort;
					$("#registerLevel").val(gangliaObjReg.Defaults.registerLevel);
				}
						$('#gmetadHost').val(gangliaData.Defaults.gmetadHost);
						$('#gmondConfPath').val(gangliaData.Defaults.gmondConfPath);
						$('#gmetadConfPath').val(gangliaData.Defaults.gmetadConfPath);
						$('#gangliaPort').val(gangliaData.Defaults.gangliaPort);
						
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
				for ( var key in jsonDataHybrid.hybrid.Ganglia.Defaults.RegisterLevel){
					$("#registerLevel").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				if (Object.keys(gangliaObjReg.Defaults).length==1) {
					gangliaObjReg = jsonDataHybrid.hybrid.Ganglia;
					com.impetus.ankush.register_Ganglia.redeployDataPrepare();
				}
				$('#gmetadHost').val(gangliaObjReg.Defaults.gmetadHost);
				$('#gmondConfPath').val(gangliaObjReg.Defaults.gmondConfPath);
				$('#gmetadConfPath').val(gangliaObjReg.Defaults.gmetadConfPath);
				$('#gangliaPort').val(gangliaObjReg.Defaults.gangliaPort);
				$("#registerLevel").val(gangliaObjReg.Defaults.registerLevel);
			}
		},	
		redeployDataPrepare : function() {
			if (undefined == gangliaObjReg.Defaults.register) {
				gangliaObjReg = jsonDataHybrid.hybrid.Ganglia;
			}
			gangliaObjReg.Defaults.register = setupDetailData.components.Ganglia.advanceConf.register;
			gangliaObjReg.Defaults.gmetadHost = setupDetailData.components.Ganglia.advanceConf.gmetadHost;
			gangliaObjReg.Defaults.gmondConfPath = setupDetailData.components.Ganglia.advanceConf.gmondConfPath;
			gangliaObjReg.Defaults.gmetadConfPath = setupDetailData.components.Ganglia.advanceConf.gmetadConfPath;
			gangliaObjReg.Defaults.gangliaPort = setupDetailData.components.Ganglia.advanceConf.gangliaPort;
			gangliaObjReg.Defaults.registerLevel=setupDetailData.components.Ganglia.advanceConf.registerLevel;
		},
		gangliaConfigValidate:function(){
			$('#validateErrorGanglia').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMainGanglia").html('').hide();
	        errorCount = 0;
				  if(!com.impetus.ankush.validation.empty($('#gmetadHost').val())){
		                errorCount++;
		                errorMsg = 'Grid Name field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('gmetadHost','Gmetad Host cannot be empty');
		                var divId='gmetadHost';
		                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainGanglia',divId,errorCount,errorMsg);
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('gmetadHost',com.impetus.ankush.tooltip.gangliaClusterCreation.gmetadHost);
		            }
				  if(!com.impetus.ankush.validation.empty($('#gmondConfPath').val())){
		                errorCount++;
		                errorMsg = 'Gmond Conf Path field empty.';
		                com.impetus.ankush.common.tooltipMsgChange('gmondConfPath','Gmond Conf Path cannot be empty');
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
		            }else{
		                com.impetus.ankush.common.tooltipOriginal('gmetadConfPath',com.impetus.ankush.tooltip.gangliaClusterCreation.gmetadConfPath);
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
				  if(errorCount>0 && errorMsg!=''){
					  $('#validateErrorGanglia').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
			        	  $("#errorDivMainGanglia").show();
			        }else{
			        	com.impetus.ankush.register_Ganglia.gangliaConfigApply();
			        }
		},
		gangliaConfigApply:function(){
			gangliaObjReg={
					Defaults:{},
			};
			  if($("#toggleRegisterBtnGanglia .active").data("value")==0){
				  gangliaObjReg.Defaults.register=false;
			}else{
				gangliaObjReg.Defaults.register=true;
			}
				gangliaObjReg.Defaults.gmetadHost=$('#gmetadHost').val();
				gangliaObjReg.Defaults.gmondConfPath=$('#gmondConfPath').val();
				gangliaObjReg.Defaults.gmetadConfPath=$('#gmetadConfPath').val();
				gangliaObjReg.Defaults.gangliaPort=$('#gangliaPort').val();
				gangliaObjReg.Defaults.registerLevel=$('#registerLevel').val();
			$('#confTypeGanglia').text('Custom');
			$("#confPageGanglia").removeClass("btn-danger");
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},
};
