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
var hadoopObjReg={
		Defaults:{},
};
var gangliaObjReg={
		Defaults:{},
};

com.impetus.ankush.registerCluster={
		validateCluster:function(errorCount,errorMsg){
			if($("input.nodeCheckBoxTech:checked").length == 0){
				errorCount++;
                errorMsg ="Select atleast one technology.";
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
			}
			if ($("#nodeCheckganglia").is(':checked') && undefined != gangliaObj.Defaults.register) {
				if( !com.impetus.ankush.validation.empty(gangliaObj.Defaults.gangliaClustername)){
	                errorCount++;
	                errorMsg = 'Ganglia Cluster Name field empty.';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
	            } else if (gangliaObj.Defaults.gangliaClustername.trim().split(' ').length > 1) {
		            errorCount++;
		            errorMsg = "Ganglia Cluster Name can't contain blank spaces.";
		            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
		        }
		        else if (!com.impetus.ankush.validation.alphaNumericCharWithoutHyphen(gangliaObj.Defaults.gangliaClustername)) {
		            errorCount++;
		            errorMsg = "Ganglia Cluster Name can contain only alphabets, numeric, dot and underscore";
		            com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
		        }
			  else{
	                com.impetus.ankush.common.tooltipOriginal('gangliaClustername',com.impetus.ankush.tooltip.gangliaClusterCreation.gangliaClustername);
	            }
			}
			if ($("#nodeCheckHadoop").is(':checked')) {
				if($("#registerToggleHadoop").hasClass("active")){
				}else{
			        	if(undefined!=hadoopObj.version && hadoopObj.version.split(".")[0]=='2' && hadoopObj.Defaults.haEnabled){
			        		
			        		if(Object.keys(hadoopNodesObj.standByNamenode).length==0){
			        			errorCount++;
				                errorMsg ="Map Nodes for Hadoop Standby NameNode role is invalid.";
				                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
			        		}
			        		if(hadoopNodesObj.standByNamenode.publicIp == hadoopNodesObj.NameNode.publicIp){
			        			errorCount++;
				                errorMsg ="Invalid map nodes for Hadoop2, Standby NameNode cannot be same as NameNode.";
				                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
			        		}
			        		if(hadoopNodesObj.JournalNode.length==0){
			        			errorCount++;
				                errorMsg ="Map Nodes for Hadoop Journal Node role is invalid.";
				                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
			        		}
			        		if(hadoopObj.version.split(".")[0]=='2'  && Object.keys(hadoopNodesObj.ResourceManager).length==0) {
			        			errorCount++;
				                errorMsg ="Map Nodes for Hadoop ResourceManager role is invalid.";
				                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
			        		}
			        		
			        		if(hadoopObj.version.split(".")[0]=='2' && hadoopObj.Defaults.webAppProxyServerEnabled){
				        		if(Object.keys(hadoopNodesObj.WebAppProxyServer).length==0){
				        			errorCount++;
					                errorMsg ="Map Nodes for Hadoop Webapp Proxy Server role is invalid.";
					                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
				        		}
				        	}
			        	}
				}
	        }
			if(undefined!=hadoopObj.version && $('#nodeCheckHadoop').is(':checked') && !$('#nodeCheckZookeeper_default').is(':checked') && hadoopObj.version.split('.')[0]==2 && $("#deployToggleHadoop").hasClass("active") &&
					hadoopObj.Defaults.haEnabled && hadoopObj.Defaults.automaticFailoverEnabled && !$('#nodeCheckZookeeper_default').is(':checked') && !$('#deployToggleZookeeper').hasClass('active') &&
					zookeeperNodeObj.length==0){
		        	errorCount++;
	                errorMsg ="Hadoop2 with HA Automatic Failover enabled, can't be deployed without Zookeeper.";
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
		        }
	        if(undefined!=hadoopObjReg.version && $('#nodeCheckHadoop').is(':checked') && hadoopObjReg.version.split('.')[0]==2 && $("#deployToggleHadoop").hasClass("active") &&
	        		hadoopObjReg.Defaults.haEnabled && hadoopObjReg.Defaults.automaticFailoverEnabled && !$('#nodeCheckZookeeper_default').is(':checked') && 
	        		!$('#registerToggleZookeeper').hasClass('active') &&
	        		zookeeperNodeObj.length==0){
		        	errorCount++;
		        	errorMsg ="Hadoop2 with HA Automatic Failover enabled, can't be deployed without Zookeeper.";
		        	com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',errorCount,errorMsg,'accordion');
		        }
			if(errorCount>0 && errorMsg!=''){
				 $('#validateError').show().html('Error&nbsp;&nbsp;'+"<span class='badge'>"+errorCount+"</span>");
	        	  $("#errorDivMain").show();
	        }else{
	        	com.impetus.ankush.hybridClusterCreation.dataPrepare();
	        	com.impetus.ankush.registerCluster.registerCluster();
	        	
	        }
		},
		registerCluster:function(){
			var clusterId=$('#clusterDeploy').val();
			var url = baseUrl + '/cluster/create';
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
							$('#validateError').show().html('Errors '+"<span class='badge'>"+result.output.error.length+"</span>");
						}
						else{
							$('#validateError').show().html('Error '+"<span class='badge'>"+result.output.error.length+"</span>");
						}
						for ( var i = 0; i < result.output.error.length; i++) {
							com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMain','',i+1,result.output.error[i],'');
						}
					}else{
						$("#errorDivMain").hide();
						$('#validateError').hide();
						$(location).attr('href',(baseUrl + '/dashboard'));
					}
			});	
		}
};
