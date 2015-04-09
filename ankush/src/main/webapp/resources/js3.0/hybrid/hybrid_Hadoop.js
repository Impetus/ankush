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
com.impetus.ankush.hybrid_Hadoop={
		
		tooltipInitialize:function(){
			$('#vendorDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.vendor);
			$('#versionDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.version);
			$('#downloadPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.downloadPath);
			$('#localPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.localPath);
			$('#installationPathHadoop').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.installationPath);
			$('#dfsReplication').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.dfsReplicationFactor);
			$('#nameNodePath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.dfsNameDir);
			$('#dataNodePath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.dfsDataDir);
			$('#mapredTempPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.mapRedTmpDir);
			$('#hadoopTempPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.hadoopTmpDir);
			$('#nameserviceId').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.nameserviceId);
			$('#nameNodeId1').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.nameNodeId1);
			$('#nameNodeId2').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.nameNodeId2);
			$('#journalNodesDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.journalNodesDir);
		},
		checkBoxClick: function(chkId,divId){
			if($('#'+chkId).is(':checked')){
				$('#'+divId).show();
			}else{
				$('#'+divId).hide();
			}
		},
		divToggle:function(divId,type){
			if(type=='Enable'){
				$('#'+divId).show();
			}else{
				$('#'+divId).hide();
			}
		},
/*Function for changing version and download path on change of Hadoop vendor dropdown*/
		vendorOnChangeHadoop:function(){
			$("#versionDropdown").html('');
			for ( var key in jsonDataHybrid.hybrid.Hadoop.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath').val(jsonDataHybrid.hybrid.Hadoop.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].url);
		},
/*Function for changing  download path on change of version Hadoop dropdown*/
		versionOnChangeHadoop:function(){
			$('#downloadPath').val(jsonDataHybrid.hybrid.Hadoop.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].url);
			if($("#versionDropdown").val().split('.')[0]==2){
				$('#hadoop2Config').show();
			}else{
				$('#hadoop2Config').hide();
			}
		},
/*Function for configuration page value population*/
		hadoopConfigPopulate:function(){
			com.impetus.ankush.hybrid_Hadoop.tooltipInitialize();
			$('#vendorDropdown').html('');
			$('#versionDropdown').html('');
			$('#zookeeperensembleIdHadoop').html('');
			if(undefined == hadoopObj.version){
				$("#hadoop2Config").show();
			}else if(hadoopObj.version.split(".")[0]=='2'){
				$("#hadoop2Config").show();
				
				if(hadoopObj.Defaults.haEnabled){
					$("#haPropDiv").show();
					$('#haBtnDisable').removeClass('active');
					$('#haBtnEnable').addClass('active');
				}else if(hadoopObj.Defaults.haEnabled==false){
					$("#haPropDiv").hide();
					$('#haBtnEnable').removeClass('active');
					$('#haBtnDisable').addClass('active');
				}
			}else{
				$("#hadoop2Config").hide();
			}
			for ( var key in ensembleIdMap){
				$("#zookeeperensembleIdHadoop").append("<option value=\"" + ensembleIdMap[key] + "\">" + ensembleIdMap[key]+ "</option>");
			}
			if(setupDetailData==null || undefined==setupDetailData.components.Hadoop){
				var hadoopData=jsonDataHybrid.hybrid.Hadoop;
				for ( var key in hadoopData.Vendors){
					$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				for ( var key in hadoopData.Vendors[$("#vendorDropdown").val()]){
					$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$('#downloadPath').val(hadoopData.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].url);
				if(undefined != hadoopObj.vendor){
					if(hadoopObj.version.split(".")[0]!='2'){
						$("hadoop2Config").hide();
					}else{
						$("hadoop2Config").show();
					
						if(hadoopObj.Defaults.haEnabled){
							$("haPropDiv").show();
						}else{
							$("haPropDiv").hide();
						}
					}
					
					hadoopData.Defaults.installationPath=hadoopObj.Defaults.installationPath;
					hadoopData.Defaults.dfsReplicationFactor=hadoopObj.Defaults.dfsReplicationFactor;
					hadoopData.Defaults.dfsNameDir=hadoopObj.Defaults.dfsNameDir;
					hadoopData.Defaults.dfsDataDir=hadoopObj.Defaults.dfsDataDir;
					hadoopData.Defaults.mapRedTmpDir=hadoopObj.Defaults.mapRedTmpDir;
					hadoopData.Defaults.hadoopTmpDir=hadoopObj.Defaults.hadoopTmpDir;
					hadoopData.Defaults.includes3=hadoopObj.Defaults.includes3;
					hadoopData.Defaults.includes3n=hadoopObj.Defaults.includes3n;
					hadoopData.Defaults.nameserviceId=hadoopObj.Defaults.nameserviceId;
					hadoopData.Defaults.nameNodeId1=hadoopObj.Defaults.nameNodeId1;
					hadoopData.Defaults.nameNodeId2=hadoopObj.Defaults.nameNodeId2;
					hadoopData.Defaults.journalNodeEditsDir=hadoopObj.Defaults.journalNodeEditsDir;
					hadoopData.Defaults.automaticFailoverEnabled=hadoopObj.Defaults.automaticFailoverEnabled;
					$("#vendorDropdown").val(hadoopObj.vendor);
					$('#versionDropdown').html('');
					for ( var key in hadoopData.Vendors[$("#vendorDropdown").val()]){
						$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
					}
					$("#versionDropdown").val(hadoopObj.version);
				
					if(hadoopObj.sourceType=="DOWNLOAD"){
						$('#downloadPathDiv').show();
						$('#localPathDiv').hide();
						$('#localRadio').removeClass('active');
						$('#downloadRadio').addClass('active');
						$('#downloadPath').val(hadoopObj.path);
					}else{
						$('#localPathDiv').show();
						$('#downloadPathDiv').hide();
						$('#downloadRadio').removeClass('active');
						$('#localRadio').addClass('active');
						$('#localPath').val(hadoopObj.path);
					}
					if(hadoopData.Defaults.automaticFailoverEnabled){
						$('#autoFailDisable').removeClass('active');
						 $('#autoFailEnable').addClass('active');
					}else{
						$('#autoFailEnable').removeClass('active');
						$('#autoFailDisable').addClass('active');
					}
				}
					$('#installationPathHadoop').val(hadoopData.Defaults.installationPath);
					$("#dfsReplication").val(hadoopData.Defaults.dfsReplicationFactor);
					$("#nameNodePath").val(hadoopData.Defaults.dfsNameDir);
					$("#dataNodePath").val(hadoopData.Defaults.dfsDataDir);
					$("#mapredTempPath").val(hadoopData.Defaults.mapRedTmpDir);
					$("#hadoopTempPath").val(hadoopData.Defaults.hadoopTmpDir);
			
					$('#nameserviceId').val(hadoopData.Defaults.nameserviceId);
					$('#nameNodeId1').val(hadoopData.Defaults.nameNodeId1);
					$('#nameNodeId2').val(hadoopData.Defaults.nameNodeId2);
					$("#journalNodesDir").val(hadoopData.Defaults.journalNodeEditsDir);
					var user=$.trim($('#inputUserName').val());
					if(user==''){
						return;
					}
					var installationPath=$.trim($('#installationPathHadoop').val()).split(userName).join('/home/'+user);
					var dfsNameDir=$.trim($('#nameNodePath').val()).split(userName).join('/home/'+user);
					var dfsDataDir=$.trim($('#dataDir').val()).split(userName).join('/home/'+user);
					var mapRedTmpDir=$.trim($('#mapRedTmpDir').val()).split(userName).join('/home/'+user);
					var hadoopTmpDir=$.trim($('#hadoopTmpDir').val()).split(userName).join('/home/'+user);
					var journalNodeEditsDir=$.trim($('#journalNodesDir').val()).split(userName).join('/home/'+user);
					userName='/home/'+user;
					$('#installationPathHadoop').empty().val(installationPath);
					$('#nameNodePath').empty().val(dfsNameDir);
					$('#dataDir').empty().val(dfsDataDir);
					$('#mapRedTmpDir').empty().val(mapRedTmpDir);
					$('#hadoopTmpDir').empty().val(hadoopTmpDir);
					$('#journalNodesDir').empty().val(journalNodeEditsDir);
			}else{
				if(cluster_State!=com.impetus.ankush.constants.stateError){
					com.impetus.ankush.common.pageStyleChange();
					$('.btnGrp').attr('disabled','disabled');
					$('#hadoopRevertBtn').attr('disabled','disabled');
					$('#hadoopApplyBtn').attr('disabled','disabled');	
				}
				if(undefined == hadoopObj.vendor){
					hadoopObj=jsonDataHybrid.hybrid.Hadoop;
					com.impetus.ankush.hybrid_Hadoop.redeployDataPrepare();
				}
				for ( var key in jsonDataHybrid.hybrid.Hadoop.Vendors){
					$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#vendorDropdown").val(hadoopObj.vendor);
				for ( var key in jsonDataHybrid.hybrid.Hadoop.Vendors[$("#vendorDropdown").val()]){
					$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#versionDropdown").val(hadoopObj.version);
				if(hadoopObj.sourceType=="DOWNLOAD"){
					$('#downloadPathDiv').show();
					$('#localPathDiv').hide();
					$('#localRadio').removeClass('active');
					 $('#downloadRadio').addClass('active');
					$('#downloadPath').val(hadoopObj.path);
				}else{
					$('#localPathDiv').show();
					$('#downloadPathDiv').hide();
					$('#downloadRadio').removeClass('active');
					$('#localRadio').addClass('active');
					$('#localPath').val(hadoopObj.path);
				}
				$('#installationPathHadoop').val(hadoopObj.Defaults.installationPath);
				$("#dfsReplication").val(hadoopObj.Defaults.dfsReplicationFactor);
				$("#nameNodePath").val(hadoopObj.Defaults.dfsNameDir);
				$("#dataNodePath").val(hadoopObj.Defaults.dfsDataDir);
				$("#mapredTempPath").val(hadoopObj.Defaults.mapRedTmpDir);
				$("#hadoopTempPath").val(hadoopObj.Defaults.hadoopTmpDir);
				if(hadoopObj.Defaults.haEnabled){
					$('#haPropDiv').show();
					$('#haBtnDisable').removeClass('active');
					 $('#haBtnEnable').addClass('active');
				}else{
					$('#haPropDiv').hide();
					$('#haBtnEnable').removeClass('active');
					$('#haBtnDisable').addClass('active');
				}
				$('#nameserviceId').val(hadoopObj.Defaults.nameserviceId);
				$("#nameNodeId1").val(hadoopObj.Defaults.nameNodeId1);
				$("#nameNodeId2").val(hadoopObj.Defaults.nameNodeId2);
				$("#journalNodesDir").val(hadoopObj.Defaults.journalNodeEditsDir);
				if(hadoopObj.Defaults.automaticFailoverEnabled){
					$('#autoFailDisable').removeClass('active');
					 $('#autoFailEnable').addClass('active');
				}else{
					$('#autoFailEnable').removeClass('active');
					$('#autoFailDisable').addClass('active');
				}
			}
		},
		
		/*Function for object value population during redeployment*/
		redeployDataPrepare: function(){
			if(undefined == hadoopObj.vendor){
				hadoopObj=jsonDataHybrid.hybrid.Hadoop;
			}       
		
			hadoopObj.vendor=setupDetailData.components.Hadoop.vendor;
			hadoopObj.version=setupDetailData.components.Hadoop.version;
			hadoopObj.path=setupDetailData.components.Hadoop.source;	
			hadoopObj.sourceType=setupDetailData.components.Hadoop.sourceType;
			hadoopObj.Defaults.installationPath=setupDetailData.components.Hadoop.installPath;
			hadoopObj.Defaults.dfsReplicationFactor=setupDetailData.components.Hadoop.advanceConf.dfsReplicationFactor;
			hadoopObj.Defaults.dfsNameDir=setupDetailData.components.Hadoop.advanceConf.dfsNameDir;
			hadoopObj.Defaults.dfsDataDir=setupDetailData.components.Hadoop.advanceConf.dfsDataDir;
			hadoopObj.Defaults.mapRedTmpDir=setupDetailData.components.Hadoop.advanceConf.mapRedTmpDir;
			hadoopObj.Defaults.hadoopTmpDir=setupDetailData.components.Hadoop.advanceConf.hadoopTmpDir;
			hadoopObj.Defaults.jobHistoryServerEnabled=setupDetailData.components.Hadoop.advanceConf.jobHistoryServerEnabled;
			hadoopObj.Defaults.webAppProxyServerEnabled=setupDetailData.components.Hadoop.advanceConf.webAppProxyServerEnabled;
			hadoopObj.Defaults.haEnabled=setupDetailData.components.Hadoop.advanceConf.haEnabled;
			hadoopObj.Defaults.nameserviceId=setupDetailData.components.Hadoop.advanceConf.nameserviceId;
			hadoopObj.Defaults.nameNodeId1=setupDetailData.components.Hadoop.advanceConf.nameNodeId1;
			hadoopObj.Defaults.nameNodeId2=setupDetailData.components.Hadoop.advanceConf.nameNodeId2;
			hadoopObj.Defaults.automaticFailoverEnabled=setupDetailData.components.Hadoop.advanceConf.automaticFailoverEnabled;
			com.impetus.ankush.hybrid_Hadoop.clusterConfCreation();
		},
		hadoopConfigValidate:function(){
			$('#validateErrorHadoop').html('').hide();
	        var errorMsg = '';
	        errorCount = 0;
	        $("#errorDivMainHadoop").html('').hide();
	        if (!com.impetus.ankush.validation.empty($('#vendorDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No hadoop vendor selected';
  	            var divId='vendorDropdown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
  	        }else {
	            $('#vendorDropdown').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#versionDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No hadoop version selected';
  	            var divId='versionDropdown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
  	        }else {
	            $('#versionDropdown').removeClass('error-box');
	        }
	        if($("#sourcePathBtnGrp .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#downloadPath').val())){
	                errorCount++;
	                errorMsg = 'Download Path field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('downloadPath','Download path cannot be empty');
	                var divId='downloadPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	            }else if(!com.impetus.ankush.validation.withoutSpace($('#downloadPath').val())){
	                errorCount++;
	                errorMsg = 'Download Path field contains space.';
	                com.impetus.ankush.common.tooltipMsgChange('downloadPath','Download path cannot conatin space');
	                var divId='downloadPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('downloadPath',com.impetus.ankush.tooltip.hadoopClusterCreation.downloadPath);
	            }
	        }else{
	        	if(!com.impetus.ankush.validation.empty($('#localPath').val())){
	                errorCount++;
	                errorMsg = 'Local path field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('localPath','Local path cannot be empty');
	                var divId='localPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	            }else if(!com.impetus.ankush.validation.withoutSpace($('#localPath').val())){
	                errorCount++;
	                errorMsg = 'Local path field contains space.';
	                com.impetus.ankush.common.tooltipMsgChange('localPath','Local path cannot contains space');
	                var divId='localPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('localPath',com.impetus.ankush.tooltip.hadoopClusterCreation.localPath);
	            }
	        }
	        if (!com.impetus.ankush.validation.empty($('#installationPathHadoop').val())) {
  	            errorCount++;
  	            errorMsg = 'Installation Path field Empty';
  	            com.impetus.ankush.common.tooltipMsgChange('installationPathHadoop','Installation Path cannot be empty');
  	            var divId='installationPathHadoop';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
  	        }else if (!com.impetus.ankush.validation.withoutSpace($('#installationPathHadoop').val())) {
  	            errorCount++;
  	            errorMsg = 'Installation Path field contains space';
  	            com.impetus.ankush.common.tooltipMsgChange('installationPathHadoop','Installation Path cannot contain space');
  	            var divId='installationPathHadoop';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
  	        } else {
	            com.impetus.ankush.common.tooltipOriginal('installationPathHadoop',com.impetus.ankush.tooltip.hadoopClusterCreation.installationPath);
	        }
	        if(!com.impetus.ankush.validation.empty($('#dfsReplication').val())){
                errorCount++;
                errorMsg = 'DFS replication field empty.';
                com.impetus.ankush.common.tooltipMsgChange('dfsReplication','DFS replication cannot be empty');
                var divId='dfsReplication';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#dfsReplication').val())) {
                errorCount++;
                errorMsg = 'DFS replicaion field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('dfsReplication','DFS replication Port must be numeric');
                var divId='dfsReplication';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }  else{
                com.impetus.ankush.common.tooltipOriginal('dfsReplication',com.impetus.ankush.tooltip.hadoopClusterCreation.dfsReplicationFactor);
            }
	        if(!com.impetus.ankush.validation.empty($('#nameNodePath').val())){
                errorCount++;
                errorMsg = 'Namenode Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('nameNodePath','Namenode path cannot be empty');
                var divId='nameNodePath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else if(!com.impetus.ankush.validation.withoutSpace($('#nameNodePath').val())){
                errorCount++;
                errorMsg = 'Namenode Path field contains space.';
                com.impetus.ankush.common.tooltipMsgChange('nameNodePath','Namenode path cannot contain space');
                var divId='nameNodePath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('nameNodePath',com.impetus.ankush.tooltip.hadoopClusterCreation.dfsNameDir);
            }
	        if(!com.impetus.ankush.validation.empty($('#dataNodePath').val())){
                errorCount++;
                errorMsg = 'Datanode Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('dataNodePath','Datanode path cannot be empty');
                var divId='dataNodePath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else if(!com.impetus.ankush.validation.withoutSpace($('#dataNodePath').val())){
                errorCount++;
                errorMsg = 'Datanode Path field conatains space.';
                com.impetus.ankush.common.tooltipMsgChange('dataNodePath','Datanode path cannot contain space');
                var divId='dataNodePath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('dataNodePath',com.impetus.ankush.tooltip.hadoopClusterCreation.dfsDataDir);
            }
	        if(!com.impetus.ankush.validation.empty($('#mapredTempPath').val())){
                errorCount++;
                errorMsg = 'Mapred temp Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('mapredTempPath','Mapred temp path cannot be empty');
                var divId='mapredTempPath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else if(!com.impetus.ankush.validation.withoutSpace($('#mapredTempPath').val())){
                errorCount++;
                errorMsg = 'Mapred temp Path field contains space.';
                com.impetus.ankush.common.tooltipMsgChange('mapredTempPath','Mapred temp path cannot conatin space');
                var divId='mapredTempPath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('mapredTempPath',com.impetus.ankush.tooltip.hadoopClusterCreation.mapRedTmpDir);
            }
	        if(!com.impetus.ankush.validation.empty($('#hadoopTempPath').val())){
                errorCount++;
                errorMsg = 'Hadoop temp Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('hadoopTempPath','Hadoop temp path cannot be empty');
                var divId='hadoopTempPath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else if(!com.impetus.ankush.validation.withoutSpace($('#hadoopTempPath').val())){
                errorCount++;
                errorMsg = 'Hadoop temp Path field conatins space.';
                com.impetus.ankush.common.tooltipMsgChange('hadoopTempPath','Hadoop temp path cannot contain space');
                var divId='hadoopTempPath';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('hadoopTempPath',com.impetus.ankush.tooltip.hadoopClusterCreation.hadoopTmpDir);
            }
	        if($("#versionDropdown").val().split('.')[0]=='2'){
	        	
	        	 if($("#haBtnGrp .active").data("value")==0){
	        		  if(!com.impetus.ankush.validation.empty($('#nameserviceId').val())){
	                      errorCount++;
	                      errorMsg = 'NameService ID field empty.';
	                      com.impetus.ankush.common.tooltipMsgChange('nameserviceId','NameService ID cannot be empty');
	                      var divId='nameserviceId';
	                      com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
		        	 }else if(!com.impetus.ankush.validation.alphaNumericWithoutUnderScore($('#nameserviceId').val())){
	                     errorCount++;
	                     errorMsg = 'NameService ID field must be alphanumeric.';
	                     com.impetus.ankush.common.tooltipMsgChange('nameserviceId','NameService ID cannot contain special characters/underscore');
	                     var divId='nameserviceId';
	                     com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
		        	 }
	        		  else{
	                      com.impetus.ankush.common.tooltipOriginal('nameserviceId',com.impetus.ankush.tooltip.hadoopClusterCreation.nameserviceId);
	                  }
	        		  if(!com.impetus.ankush.validation.empty($('#nameNodeId1').val())){
	                      errorCount++;
	                      errorMsg = 'NameNode ID 1 field empty.';
	                      com.impetus.ankush.common.tooltipMsgChange('nameNodeId1','NameNode ID 1  cannot be empty');
	                      var divId='nameNodeId1';
	                      com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	                  }else{
	                      com.impetus.ankush.common.tooltipOriginal('nameNodeId1',com.impetus.ankush.tooltip.hadoopClusterCreation.nameNodeId1);
	                  }
	        		  if(!com.impetus.ankush.validation.empty($('#nameNodeId2').val())){
	                      errorCount++;
	                      errorMsg = 'NameNode ID 2 Path field empty.';
	                      com.impetus.ankush.common.tooltipMsgChange('nameNodeId2','NameNode ID 2 cannot be empty');
	                      var divId='nameNodeId2';
	                      com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	                  }else{
	                      com.impetus.ankush.common.tooltipOriginal('nameNodeId2',com.impetus.ankush.tooltip.hadoopClusterCreation.nameNodeId2);
	                  }
	        		  if(!com.impetus.ankush.validation.empty($('#journalNodesDir').val())){
	                      errorCount++;
	                      errorMsg = 'Journal Nodes Dir field empty.';
	                      com.impetus.ankush.common.tooltipMsgChange('journalNodesDir','Journal Nodes Dir cannot be empty');
	                      var divId='journalNodesDir';
	                      com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	                  }else if(!com.impetus.ankush.validation.withoutSpace($('#journalNodesDir').val())){
	                      errorCount++;
	                      errorMsg = 'Journal Nodes Dir field contains space.';
	                      com.impetus.ankush.common.tooltipMsgChange('journalNodesDir','Journal Nodes Dir cannot contain space');
	                      var divId='journalNodesDir';
	                      com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	                  }else{
	                      com.impetus.ankush.common.tooltipOriginal('journalNodesDir',com.impetus.ankush.tooltip.hadoopClusterCreation.journalNodesDir);
	                  }
	        	 }
	        }
	        if(errorCount>0 && errorMsg!=''){
	        	$('#validateErrorHadoop').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
	        	  $("#errorDivMainHadoop").show();
	        }else{
	        	 com.impetus.ankush.hybrid_Hadoop.hadoopConfigApply();
	        } 
		},
		hadoopConfigApply:function(){
			hadoopObj.vendor=$('#vendorDropdown').val();
			hadoopObj.version=$('#versionDropdown').val();
			 if($("#sourcePathBtnGrp .active").data("value")==1){
				hadoopObj.sourceType="LOCAL";
				hadoopObj.path=$('#localPath').val();
			}else{
				hadoopObj.sourceType="DOWNLOAD";
				hadoopObj.path=$('#downloadPath').val();
			}
			hadoopObj.Defaults.ensembleId=$('#zookeeperensembleIdHadoop').val();
			hadoopObj.Defaults.installationPath=$('#installationPathHadoop').val();
			hadoopObj.Defaults.dfsReplicationFactor=$('#dfsReplication').val();
			hadoopObj.Defaults.dfsNameDir=$('#nameNodePath').val();
			hadoopObj.Defaults.dfsDataDir=$('#dataNodePath').val();
			hadoopObj.Defaults.mapRedTmpDir=$('#mapredTempPath').val();
			hadoopObj.Defaults.hadoopTmpDir=$('#hadoopTempPath').val();
			hadoopObj.Defaults.nameserviceId=$('#nameserviceId').val();
			hadoopObj.Defaults.nameNodeId1=$('#nameNodeId1').val();
			hadoopObj.Defaults.nameNodeId2=$('#nameNodeId2').val();
			hadoopObj.Defaults.journalNodeEditsDir=$('#journalNodesDir').val();
			hadoopObj.Defaults.jobHistoryServerEnabled=false;
	
			 if($("#haBtnGrp .active").data("value")==0){
					hadoopObj.Defaults.haEnabled=true;
				}else{
					hadoopObj.Defaults.haEnabled=false;
				}
				hadoopObj.Defaults.nameserviceId=$('#nameserviceId').val();
				hadoopObj.Defaults.nameNodeId1=$('#nameNodeId1').val();
				hadoopObj.Defaults.nameNodeId2=$('#nameNodeId2').val();
				hadoopObj.Defaults.journalNodeEditsDir=$('#journalNodesDir').val();
				if($("#autoFailBtnGrp .active").data("value")==0){
					hadoopObj.Defaults.automaticFailoverEnabled=true;
				}else{
					hadoopObj.Defaults.automaticFailoverEnabled=false;
				}
			$('#confTypeHadoop').text('Custom');
			$("#confPageHadoop").removeClass("btn-danger btn-primary");
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},
		hadoopNodesPopulate:function(){
			if($("#toggleRegisterBtnHadoop .active").data("value")==0){
				com.impetus.ankush.register_Hadoop.hadoopNodesPopulate();
				return;
			}
	
			if(hadoopNodeTable!=null){
				hadoopNodeTable.fnClearTable();
			}
			if(undefined == hadoopObj.vendor || hadoopObj.version.split(".")[0]=='2'){
				hadoopNodeTable.fnSetColumnVis(6, false);
			}
			if((undefined != hadoopObj.vendor && hadoopObj.version.split(".")[0]!='2')||($("#toggleRegisterBtnHadoop .active").data("value")==0)){
				hadoopNodeTable.fnSetColumnVis(7, false);
				hadoopNodeTable.fnSetColumnVis(8, false);
				hadoopNodeTable.fnSetColumnVis(9, false);
				hadoopNodeTable.fnSetColumnVis(10, false);
				hadoopNodeTable.fnSetColumnVis(11, false);
			}
			if(undefined != hadoopObj.vendor){
			
				if(!hadoopObj.Defaults.haEnabled){
					hadoopNodeTable.fnSetColumnVis(10, false);
					hadoopNodeTable.fnSetColumnVis(11, false);
				}
			}
			
			if(nodeStatus==null){
				return;
			}
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var addId = null;
					addId = hadoopNodeTable.fnAddData([
					                               	'<input id="hadoopCheckBox'
					                               		+ i
					                               		+ '" class="hadoopCheck" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.hybrid_Hadoop.hadoopNodeCheck('+i+')" >',
													nodeStatus.nodes[i][0],
													'<span class="" id="nodeRoleHadoop'+i+'">'+$('#nodeRole' + i).text()+'</span>',
													'<input id="nameNodeCheckBox'
														+ i
														+ '" class="nameNodeCheck" name="nameNodeRadio" style="margin-right:10px;" type="radio" onclick="com.impetus.ankush.hybrid_Hadoop.nameNodeCheck('+i+');">',
													'<input id="secNameNodeCheckBox'
														+ i
														+ '" class="secNameNodeCheck" name="secNameNodeRadio" style="margin-right:10px;" type="radio" onclick="com.impetus.ankush.hybrid_Hadoop.secNameNodeCheck('+i+');">',
													'<input id="dataNodeCheckBox'
														+ i
														+ '" class="dataNodeCheck" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.hybrid_Hadoop.dataNodeCheck('+i+');">',
													'<input id="jtCheckBox'
														+ i
														+ '" class="jtCheck" name="rmRadio" style="margin-right:10px;" type="radio" onclick="com.impetus.ankush.hybrid_Hadoop.radioCheck('+i+',\'jtCheckBox\');">',
													
													'<input id="rmCheckBox'
														+ i
														+ '" class="rmCheck" name="rmRadio" style="margin-right:10px;" type="radio" onclick="com.impetus.ankush.hybrid_Hadoop.radioCheck('+i+',\'rmCheckBox\');">',
													
													'<input id="jobHistoryCheckBox'
														+ i
														+ '" class="radioCheck jobHistoryCheck" value="1" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.hybrid_Hadoop.radioCheck('+i+',\'jobHistoryCheckBox\');">',
													
													'<input id="webAppCheckBox'
														+ i
														+ '" class="radioCheckWeb webAppCheck" value="1"  style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.hybrid_Hadoop.radioCheck('+i+',\'webAppCheckBox\');">',
													'<input id="standbyCheckBox'
														+ i
														+ '" class="standbyCheck" name="standbyRadio" style="margin-right:10px;" type="radio" onclick="com.impetus.ankush.hybrid_Hadoop.radioCheck('+i+',\'standbyCheckBox\');">',
													'<input id="journalCheckBox'
														+ i
														+ '" class="journalCheck" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.hybrid_Hadoop.radioCheck('+i+',\'journalCheckBox\');">',
													nodeStatus.nodes[i][4],
													'' ]);
				var theNode = hadoopNodeTable.fnSettings().aoData[addId[0]].nTr;
				theNode.setAttribute('id', 'hadoop'+ nodeStatus.nodes[i][0].split('.').join('_'));
				var isSudo=false;
				if(sudoFlag){	
					if($("#nodeRight"+i).text()=="Sudo"){
					}else{
						isSudo=true;
					}
				}
				
				if (nodeStatus.nodes[i][1] == false	|| nodeStatus.nodes[i][2] == false|| nodeStatus.nodes[i][3] == false  || isSudo) {
					rowId = nodeStatus.nodes[i][0].split('.').join('_');
					$('td', $('#hadoop'+rowId)).addClass('alert-danger');
					$('#hadoop' + rowId).addClass('alert-danger');
					$('#hadoopCheckBox' + i).attr('disabled', true);
					$('#nameNodeCheckBox' + i).attr('disabled', true);
					$('#jtCheckBox' + i).attr('disabled', true);
					$('#secNameNodeCheckBox' + i).attr('disabled', true);
					$('#dataNodeCheckBox' + i).attr('disabled', true);
					$('#rmCheckBox' + i).attr('disabled', true);
					$('#jobHistoryCheckBox' + i).attr('disabled', true);
					$('#webAppCheckBox' + i).attr('disabled', true);
					$('#standbyCheckBox' + i).attr('disabled', true);
					$('#journalCheckBox' + i).attr('disabled', true);
					
				}else{
					$('#hadoopCheckBox' + i).removeAttr('disabled', true);
					$('#nameNodeCheckBox' + i).removeAttr('disabled', true);
					$('#jtCheckBox' + i).removeAttr('disabled', true);
					$('#secNameNodeCheckBox' + i).removeAttr('disabled', true);
					$('#dataNodeCheckBox' + i).removeAttr('disabled', true);
					$('#rmCheckBox' + i).removeAttr('disabled', true);
					$('#jobHistoryCheckBox' + i).removeAttr('disabled', true);
					$('#webAppCheckBox' + i).removeAttr('disabled', true);
					$('#standbyCheckBox' + i).removeAttr('disabled', true);
					$('#journalCheckBox' + i).removeAttr('disabled', true);
				};
			}
			if(undefined==hadoopObj.version){
				$('.secNameNodeCheck').removeAttr("checked").attr('disabled', true);	
			}else if(hadoopObj.version.split(".")[0]=='2' && hadoopObj.Defaults.haEnabled && $("#toggleRegisterBtnHadoop .active").data("value")==1){
				$('.secNameNodeCheck').removeAttr("checked").attr('disabled', true);
			}else{
				$('.secNameNodeCheck').removeAttr("disabled");
			}
			//to make jobhistory checkbox work like a radio.
			$("#hadoopNodeTable input:checkbox.radioCheck").click(function() {
				 var id=jQuery(this).attr("id");
				 if($(this).is(":checked")){
					 $("#hadoopNodeTable input:checkbox.radioCheck").attr("checked", false);	 
				 }else{
					 $("#hadoopNodeTable input:checkbox.radioCheck").attr("checked", false);
					 $(this).prop("checked", true); 
				 }
			    if($('#'+id).is(":checked")){
			    	$(this).removeAttr("checked");	
			    }else{
			    	$(this).prop("checked", true);
			    }
			});
			$("#hadoopNodeTable input:checkbox.radioCheckWeb").click(function() {
				 var id=jQuery(this).attr("id");
				 if($(this).is(":checked")){
					 $("#hadoopNodeTable input:checkbox.radioCheckWeb").attr("checked", false);	 
				 }else{
					 $("#hadoopNodeTable input:checkbox.radioCheckWeb").attr("checked", false);
					 $(this).prop("checked", true); 
				 }
			    if($('#'+id).is(":checked")){
			    	$(this).removeAttr("checked");	
			    }else{
			    	$(this).prop("checked", true);
			    }
			});
			
			if(setupDetailData==null ||undefined==setupDetailData.components.Hadoop){
				if(nodeStatus.nodes.length>0 && Object.keys(hadoopNodesObj.NameNode).length==0){
					
				}else{
					$('.hadoopCheck').removeAttr('disabled');
					$('.nameNodeCheck').removeAttr('disabled');
					$('.jtCheck').removeAttr('disabled');
					if(undefined!=hadoopObj.version && hadoopObj.version.split(".")[0]!='2'){
						$('.secNameNodeCheck').removeAttr('disabled');
					}
					$('.dataNodeCheck').removeAttr('disabled');
					$('.rmCheck').removeAttr('disabled');
					$('.jobHistoryCheck').removeAttr('disabled');
					$('.webAppCheck').removeAttr('disabled');
					$('.standbyCheck').removeAttr('disabled');
					$('.journalCheck').removeAttr('disabled');
					for(var j=0;j<nodeStatus.nodes.length;j++){
		     			   if(hadoopNodesObj.NameNode.publicIp==nodeStatus.nodes[j][0]){
		     				   $('#nameNodeCheckBox'+j).attr('checked',true);
		     				  $('#hadoopCheckBox'+j).attr('checked',true);
		     			   }
		     			  if(hadoopNodesObj.SecondaryNameNode.publicIp==nodeStatus.nodes[j][0]){
		     				   $('#secNameNodeCheckBox'+j).attr('checked',true); 
		     				  $('#hadoopCheckBox'+j).attr('checked',true);
		     			   }
		     			 if(hadoopNodesObj.JobTracker.publicIp==nodeStatus.nodes[j][0]){
		     				   $('#jtCheckBox'+j).attr('checked',true);
		     				  $('#hadoopCheckBox'+j).attr('checked',true);
		     			   }
		     			 if(hadoopNodesObj.ResourceManager.publicIp==nodeStatus.nodes[j][0]){
		     				   $('#rmCheckBox'+j).attr('checked',true); 
		     				  $('#hadoopCheckBox'+j).attr('checked',true);
		     			   }
		     			if(hadoopNodesObj.JobHistoryServer.publicIp==nodeStatus.nodes[j][0]){
		     				   $('#jobHistoryCheckBox'+j).attr('checked',true); 
		     				  $('#hadoopCheckBox'+j).attr('checked',true);
		     			   }
		     			if(hadoopNodesObj.WebAppProxyServer.publicIp==nodeStatus.nodes[j][0]){
		     				   $('#webAppCheckBox'+j).attr('checked',true); 
		     				  $('#hadoopCheckBox'+j).attr('checked',true);
		     			   }
		     			if(hadoopNodesObj.standByNamenode.publicIp==nodeStatus.nodes[j][0]){
		     				   $('#standbyCheckBox'+j).attr('checked',true); 
		     				  $('#hadoopCheckBox'+j).attr('checked',true);
		     			   }
		     			if(hadoopNodesObj.JournalNode.length>0){	
			   				 for(var i=0;i<hadoopNodesObj.JournalNode.length;i++){
			   	     			   if(hadoopNodesObj.JournalNode[i].publicIp==nodeStatus.nodes[j][0]){
			   	     				   $('#journalCheckBox'+j).attr('checked',true); 
			   	     				  $('#hadoopCheckBox'+j).attr('checked',true);
			   	     				   break;
			   					}
			   				 }
		   				}
					}
					if(undefined!=hadoopObj.version && hadoopObj.version.split(".")[0]=='2' && hadoopObj.Defaults.haEnabled && $("#toggleRegisterBtnHadoop .active").data("value")==1){
						$('.secNameNodeCheck').removeAttr("checked").attr('disabled', true);
					}
				}
				if(hadoopNodesObj.slaves.length>0){
				 for(var i=0;i<hadoopNodesObj.slaves.length;i++){
					 for(var j=0;j<nodeStatus.nodes.length;j++){
	     			   if(hadoopNodesObj.slaves[i].publicIp==nodeStatus.nodes[j][0]){
	     				   $('#dataNodeCheckBox'+j).attr('checked',true); 
	     				  $('#hadoopCheckBox'+j).attr('checked',true);
	     				   break;
	     			   }
					}
				 }
				}
			}else{
				 if(Object.keys(hadoopNodeMap).length>0){
					 for (var i = 0; i < nodeRoleArray.length; i++) {
		         			   if(hadoopNodesObj.NameNode.publicIp==nodeRoleArray[i].ip){
		         				   $('#nameNodeCheckBox'+i).attr('checked',true);
		         				  $('#hadoopCheckBox'+i).attr('checked',true);
		         			   }
		         			   if(hadoopNodesObj.SecondaryNameNode.publicIp==nodeRoleArray[i].ip){
		         				   $('#secNameNodeCheckBox'+i).attr('checked',true); 
		         				  $('#hadoopCheckBox'+i).attr('checked',true);
		         			   	}
		         			  if(hadoopNodesObj.JobTracker.publicIp==nodeRoleArray[i].ip){
		         				   $('#jtCheckBox'+i).attr('checked',true);
		         				  $('#hadoopCheckBox'+i).attr('checked',true);
		         			   }
		         			   if(hadoopNodesObj.ResourceManager.publicIp==nodeRoleArray[i].ip){
		         				   $('#rmCheckBox'+i).attr('checked',true); 
		         				  $('#hadoopCheckBox'+i).attr('checked',true);
		         			   	}
		         			   if(hadoopNodesObj.JobHistoryServer.publicIp==nodeRoleArray[i].ip){
		         				   $('#jobHistoryCheckBox'+i).attr('checked',true); 
		         				  $('#hadoopCheckBox'+i).attr('checked',true);
		         			   	}
		         			   for(var j=0;j<hadoopNodesObj.slaves.length;j++){
			         			   if(hadoopNodesObj.slaves[j].publicIp==nodeRoleArray[i].ip){
			         				   $('#dataNodeCheckBox'+i).attr('checked',true); 
			         				  $('#hadoopCheckBox'+i).attr('checked',true);
			         				   break;
			         			   }
		         			   }
		         			   if(hadoopNodesObj.WebAppProxyServer.publicIp==nodeRoleArray[i].ip){
		         				   $('#webAppCheckBox'+i).attr('checked',true); 
		         				   $('#hadoopCheckBox'+i).attr('checked',true);
		         			   }
		         			   if(hadoopNodesObj.standByNamenode.publicIp==nodeRoleArray[i].ip){
		         				   $('#standbyCheckBox'+i).attr('checked',true); 
		         				   $('#hadoopCheckBox'+i).attr('checked',true);
		         			   }
		         			   for(var k=0;k<hadoopNodesObj.JournalNode.length;k++){
		         				   if(hadoopNodesObj.JournalNode[k].publicIp==nodeRoleArray[i].ip){
		         					   	$('#journalCheckBox'+i).attr('checked',true); 
		         					   	$('#hadoopCheckBox'+i).attr('checked',true);
		         					   	break;
		         				   }
		         			   }
					 	}
					 if(undefined!=hadoopObj.version && hadoopObj.version.split(".")[0]=='2' && hadoopObj.Defaults.haEnabled && $("#toggleRegisterBtnHadoop .active").data("value")==1){
							$('.secNameNodeCheck').removeAttr("checked").attr('disabled', true);	
						}
				}
				  if(cluster_State!=com.impetus.ankush.constants.stateError){
						$('.hadoopCheck,.nameNodeCheck,.secNameNodeCheck,.dataNodeCheck,.jtCheck, .rmCheck, .jobHistoryCheck, .webAppCheck, .standbyCheck, .journalCheck').attr('disabled', true);
						$('#nodeCheckHadoopHead').attr('disabled', true);
						$('#hadoopNodesApply').attr('disabled', true);
						 $('#hadoopNodesRevert').attr('disabled', true);
					}
			};
		},
		toggleJobHistoryServer:function(id,divId){
			if($('#'+id).is(':checked')){
				$('#'+divId).removeAttr('disabled');
			}else{
				$('#'+divId).attr('disabled','disabled');
			};
		},
		radioCheck:function(i,id){
			if($('#'+id+''+i).is(':checked')){
				$('#hadoopCheckBox'+i).attr('checked','checked');
			}
			if ($("#hadoopNodeTable .hadoopCheck:checked").length == $("#hadoopNodeTable .hadoopCheck").length
		               - disableNodeCount) {
		        $("#nodeCheckHadoopHead").prop("checked", true);
		     }; 	
		},
		nameNodeCheck : function(i){
			if($('#nameNodeCheckBox'+i).is(':checked')){
				$('#hadoopCheckBox'+i).attr('checked','checked');
			}
			if ($("#hadoopNodeTable .hadoopCheck:checked").length == $("#hadoopNodeTable .hadoopCheck").length
		               - disableNodeCount) {
		        $("#nodeCheckHadoopHead").prop("checked", true);
		     } ;
		},
		secNameNodeCheck : function(i){
			if($('#secNameNodeCheckBox'+i).is(':checked')){
				$('#hadoopCheckBox'+i).attr('checked','checked');
			}
			if ($("#hadoopNodeTable .hadoopCheck:checked").length == $("#hadoopNodeTable .hadoopCheck").length
		               - disableNodeCount) {
		        $("#nodeCheckHadoopHead").prop("checked", true);
		     } ;
		},
		dataNodeCheck : function(i){
			if($('#dataNodeCheckBox'+i).is(':checked')){
				$('#hadoopCheckBox'+i).attr('checked','checked');
			}
			if ($("#hadoopNodeTable .hadoopCheck:checked").length == $("#hadoopNodeTable .hadoopCheck").length
		               - disableNodeCount) {
		        $("#nodeCheckHadoopHead").prop("checked", true);
		     } ;
		},
		
		hadoopHeadNodeCheck:function(){
			if($('#nodeCheckHadoopHead').is(':checked')){
				var count=0;
				 $('.hadoopCheck').each(function(){
					 if($('#hadoopCheckBox'+count).is(':disabled')==false){
						 $('#hadoopCheckBox'+count).attr('checked','checked');
						 $('#dataNodeCheckBox'+count).attr('checked','checked');
					 }
					 count++;
				 });
			}else{
				 $('.hadoopCheck').removeAttr('checked');
				 $('.nameNodeCheck').removeAttr('checked');
				 $('.jtCheck').removeAttr('checked');
				 $('.secNameNodeCheck').removeAttr('checked');
				 $('.dataNodeCheck').removeAttr('checked');
				 $('.rmCheck').removeAttr('checked');
				 $('.jobHistoryCheck').removeAttr('checked');
				 $('.webAppCheck').removeAttr('checked');
				 $('.standbyCheck').removeAttr('checked');
				 $('.journalCheck').removeAttr('checked');
				 $('.mrCheck').removeAttr('checked');
				 
			};
		},
		
		hadoopNodeCheck:function(node){
			var ipArray=new Array();
			if($('#hadoopCheckBox'+node).is(':checked')){
				$('#dataNodeCheckBox'+node).attr('checked','checked');
			}else{
				$('#nameNodeCheckBox'+node).removeAttr('checked');
				$('#jtCheckBox'+node).removeAttr('checked');
				$('#secNameNodeCheckBox'+node).removeAttr('checked');
				$('#dataNodeCheckBox'+node).removeAttr('checked');
				$('#rmCheckBox'+node).removeAttr('checked');
				$('#jobHistoryCheckBox' +node).removeAttr('checked');
				$('#webAppCheckBox' +node).removeAttr('checked');
				$('#standbyCheckBox' +node).removeAttr('checked');
				$('#journalCheckBox' +node).removeAttr('checked');
				$('#mrCheckBox' +node).removeAttr('checked');
			}
			var count=0;
			var disabledNodeCount=0;
			var checkedNodeCount=0;
			 $(".hadoopCheck").each(function(elem) {
				 if($(this).is(':checked')){
					 ipArray.push(nodeStatus.nodes[count][0]);
					 checkedNodeCount++;
				 }
				 if($(this).is(':disabled')){
					 disabledNodeCount++;
				 }
				 count++;
			 });
			
				if((checkedNodeCount==nodeStatus.nodes.length) || (nodeStatus.nodes.length==(checkedNodeCount+disabledNodeCount))){
					$('#nodeCheckHadoopHead').attr('checked','checked');
				}else{
					$('#nodeCheckHadoopHead').removeAttr('checked');
				}
			
		},
		loadHadoopNodeDetail:function(elem){
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hybrid-cluster/nodeMapNodeDetail',
				method : 'get',
				title : 'Node Detail',
				callback : function(data) {
					com.impetus.ankush.hybrid_Hadoop.hadoopNodeDetail(elem);
				},
				callbackData : {
				}
			});
	},
	hadoopNodeDetail:function(elem){
		var rowIndex=$(elem).closest('td').parent()[0].sectionRowIndex;
		var aData= hadoopNodeTable.fnGetData(rowIndex);
		$('#nodeHead').html($('td:nth-child(2)', $(elem).parents('tr')).text());
		$('#nodeType').html($("td:nth-child(3)", $(elem).parents('tr')).text());
		$('#os').html(aData[aData.length-2]);
		if(nodeStatus.nodes[rowIndex][1]==false){
			$('#nodeStatus').html('').text('Unavailable');
		}else if(nodeStatus.nodes[rowIndex][2]==false){
			$('#nodeStatus').html('').text('Unreachable');
		}else if(nodeStatus.nodes[rowIndex][3]==false){
			$('#nodeStatus').html('').text('Unauthenticated');
		}else{
			$('#nodeStatus').html('').text('Available');
		}
		if(Object.keys(hadoopNodeMap).length > 0){
			var hadoopNodeDetails = hadoopNodeMap[aData[1]];
			if(undefined!=hadoopNodeDetails.message){
				$('#nodeStatus').html('').text(hadoopNodeDetails.message);	
			}
			if(hadoopNodeDetails.errors!=undefined && Object.keys(hadoopNodeDetails.errors).length>0){
				for(var key in hadoopNodeDetails.errors){
      				$('#nodeDeploymentError').append('<label class="text-left" style="color: black;" id="'+key+'">'+hadoopNodeDetails.errors[key]+'</label>');
      	        }
				$('#errorNodeDiv').show();
			}
		} 
	},
	hadoopNodesPopulateApplyValidate:function(){
		if($("#toggleRegisterBtnHadoop .active").data("value")==0){
			 com.impetus.ankush.register_Hadoop.hadoopNodesPopulateApplyValidate();
			return;
		}
		
		errorCount=0;
    	errorMsg='';
    	 $("#errorDivHadoopNodes").html('');
            $('#validateErrorHadoop').html('');
    	if(nodeStatus!=null){
	            if ($('#hadoopNodeTable .hadoopCheck:checked').length < 1) {
	            	errorMsg = 'Select at least one node.';
	                errorCount++;
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
	            }else{
	            	 var count=0;
	            	 var nameNode=0;
	            	 var jtNode=0;
	            	 var secNameNode=0;
	            	 var dataNode=0;
	            	 var rmNode=0;
	            	 var jhNode=0;
	            	 var waNode=0;
	            	 var snNode=0;
	            	 var jnNode=0;
	            	 var nn=false;
	            	 var jt=false;
	            	 var snn=false;
	            	 var dn=false;
	            	 var rmn=false;
	            	 var jhn=false;
	            	 var wpn=false;
	            	 var sbn=false;
	            	 var jn=false;
	            	 var isSameNode=false;
	            	 $(".hadoopCheck").each(function(elem) {
		            	 count=$(this).attr('id').split('hadoopCheckBox')[1];
	            		 if($('#hadoopCheckBox'+count).is(':checked')){
		            		 if($('#nameNodeCheckBox'+count).is(':checked')){
		            			 nameNode++;	 
		            			 nn=true;
		            		 }
		            		 if($('#secNameNodeCheckBox'+count).is(':checked')){
		            			 secNameNode++;
		            			 snn=true;
		            		 }
		            		 if($('#dataNodeCheckBox'+count).is(':checked')){
		            			 dataNode++;
		            			 dn=true;
		            		 }
		            		 if($('#jtCheckBox'+count).is(':checked')){
		            			 jtNode++;	 
		            			 jt=true;
		            		 }
		            		 if($('#rmCheckBox'+count).is(':checked')){
		            			 rmNode++;
		            			 rmn=true;
		            		 }
		            		 if($('#jobHistoryCheckBox'+count).is(':checked')){
		            			 jhNode++;
		            			 jhn=true;
		            		 }
		            		 if($('#webAppCheckBox'+count).is(':checked')){
		            			 waNode++;
		            			 wpn=true;
		            		 }
		            		 if($('#standbyCheckBox'+count).is(':checked')){
		            			 snNode++;
		            			 sbn=true;
		            		 }
		            		 if($('#journalCheckBox'+count).is(':checked')){
		            			 jnNode++;
		            			 jn=true;
		            		 }
		            		 if($('#nameNodeCheckBox'+count).is(':checked') && $('#standbyCheckBox'+count).is(':checked')){
		            			 isSameNode=true;
		            		 }
		            	
	            		 }
	            	 });
	            	 if((undefined==hadoopObj.version || hadoopObj.version.split('.')[0]==2) && $("#toggleRegisterBtnHadoop .active").data("value")==1){
	            		 if(isSameNode && (undefined==hadoopObj.Defaults.haEnabled  || (undefined!=hadoopObj.Defaults.haEnabled && hadoopObj.Defaults.haEnabled ))){
	            			 errorMsg = "NameNode and StandBy NameNode can't be same.";
		     	                errorCount++;
		     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
		            	 
	            		 }
            			 if(hadoopObj.Defaults.webAppProxyServerEnabled && hadoopObj.Defaults.haEnabled){
            				 if(!nn && !dn && !rmn && !jhn && !wpn && !sbn && !jn){
 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
 		     	                errorCount++;
 		     	              com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
 		            		 }
            			 }else if(hadoopObj.Defaults.webProxy){
            				 if(!nn && !dn && !rmn && !jhn && !wpn){
	 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
	 		     	                errorCount++;
	 		     	              com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
	 		            		 } 
            			 }else if(hadoopObj.Defaults.haEnabled){
            				 if(!nn && !dn && !rmn && !jhn && !sbn && !jn){
	 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
	 		     	                errorCount++;
	 		     	              com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
	 		            		 }
            			 }else{
            				 if(!nn && !dn && !rmn && !jhn){
	 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
	 		     	                errorCount++;
	 		     	              com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
	 		            		 }
            			 }
            		 }else{
            			 if(!nn && !snn && !dn && !jt){
		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ '  must be Namenode or Secondarynamenode or Datanode or Job Tracker Node.';
		     	                errorCount++;
		     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
		            		 }	 
            		 }
	            	 if(nameNode==0){
	            		 errorMsg = 'At least one node must be selected as Namenode.';
	     	                errorCount++;
	     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
	     	           
	            	 }
	            	 if(undefined != hadoopObj.version && hadoopObj.version.split('.')[0] == 1 && jtNode==0){
	            		 errorMsg = 'At least one node must be selected as JobTracker.';
	     	                errorCount++;
	     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
	     	           
	            	 }
	            	 if(dataNode==0){
	            		 errorMsg = 'At least one node must be selected as Datanode.';
	     	                errorCount++;
	     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
	     	           
	            	 }
	            	 if((undefined==hadoopObj.version || hadoopObj.version.split('.')[0]==2) && $("#toggleRegisterBtnHadoop .active").data("value")==1){
	            		 if(rmNode==0){
		            		 errorMsg = 'At least one node must be selected as ResourceManger Node.';
		     	                errorCount++;
		     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
		     	           
		            	 }
	            		
            			 if(undefined==hadoopObj.Defaults.haEnabled  || (undefined!=hadoopObj.Defaults.haEnabled && hadoopObj.Defaults.haEnabled )){
	            			 if(snNode==0){
			            		 errorMsg = 'At least one node must be selected as StandBy NameNode.';
			     	                errorCount++;
			     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
			     	           
			            	 } 
	            			 if(jnNode==0){
			            		 errorMsg = 'At least one node must be selected as Journal Node.';
			     	                errorCount++;
			     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
			     	           
			            	 } 
            			 }
	            	 }
	            }
	          
    		 if (errorCount > 0) {
    	            $("#errorDivHadoopNodes").show();
    	               if(errorCount > 1)
    	            	   $('#validateErrorHadoop').show().html('Errors '+"<span class='badge'>"+errorCount+"</span>");
    	                else
    	                	$('#validateErrorHadoop').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
    	        } else {
    	            $("#errorDivHadoopNodes").hide();
    	            $('#validateErrorHadoop').hide();
    	            com.impetus.ankush.hybrid_Hadoop.hadoopNodesPopulateApply();
    	        }
    	}
		},
		hadoopNodesPopulateApply:function(){
			hadoopNodeMap={}; 	
			var nodeCount=0;
			hadoopNodesObj.NameNode={};
			hadoopNodesObj.SecondaryNameNode={};
			hadoopNodesObj.ResourceManager={};
			hadoopNodesObj.JobTracker={};
			hadoopNodesObj.JobHistoryServer={};
			hadoopNodesObj.slaves=new Array();
			hadoopNodesObj.WebAppProxyServer={};
			hadoopNodesObj.standByNamenode={};
			hadoopNodesObj.JournalNode=new Array();
			hadoopNodesObj.clusterNodeConfs=new Array();
			for ( var k = 0; k < nodeRoleArray.length; k++) {
				nodeRoleArray[k].roles.Hadoop=new Array();
				nodeRoleArray[k].role.NameNode=0;
				nodeRoleArray[k].role.DataNode=0;
				nodeRoleArray[k].role.ResourceManager=0;
				nodeRoleArray[k].role.NodeManager=0;
				nodeRoleArray[k].role.zkfc=0;
				nodeRoleArray[k].role.JournalNode=0;
				nodeRoleArray[k].role.JobTracker=0;
				nodeRoleArray[k].role.TaskTracker=0;
				nodeRoleArray[k].role.SecondaryNameNode=0;
				nodeRoleArray[k].role.JobHistoryServer=0;
				nodeRoleArray[k].role.WebAppProxyServer=0;
			}
			if(cluster_State!=com.impetus.ankush.constants.stateError){
				for ( var i = 0; i < nodeStatus.nodes.length; i++) {
					if($('#hadoopCheckBox'+i).is(':checked')){
						if($('#hadoopCheckBox'+i).parent().next().next().next().children().is(":checked")){
							nodeCount++;
							hadoopNodesObj.NameNode.publicIp=nodeStatus.nodes[i][0];
							hadoopNodesObj.NameNode.privateIp=nodeStatus.nodes[i][0];
							hadoopNodesObj.NameNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.NameNode.type='NameNode';
							hadoopNodesObj.NameNode.nodeState='deploying';
						 	hadoopNodesObj.NameNode.datacenter='';
							hadoopNodesObj.NameNode.rack='';
							hadoopNodesObj.NameNode.nameNode=true;
							hadoopNodesObj.NameNode.dataNode=false;
							hadoopNodesObj.NameNode.secondaryNameNode=false;
						}
						if($('#hadoopCheckBox'+i).parent().next().next().next().next().children().is(":checked")){
							nodeCount++;
							hadoopNodesObj.SecondaryNameNode.publicIp=nodeStatus.nodes[i][0];
							hadoopNodesObj.SecondaryNameNode.privateIp=nodeStatus.nodes[i][0];
							hadoopNodesObj.SecondaryNameNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.SecondaryNameNode.type='SecondaryNameNode';
							hadoopNodesObj.SecondaryNameNode.nodeState='deploying';
							hadoopNodesObj.SecondaryNameNode.datacenter='';
							hadoopNodesObj.SecondaryNameNode.rack='';
							hadoopNodesObj.SecondaryNameNode.nameNode=false;
							hadoopNodesObj.SecondaryNameNode.dataNode=false;
							hadoopNodesObj.SecondaryNameNode.secondaryNameNode=true;
							if($('#hadoopCheckBox'+i).parent().next().next().next().children().is(":checked")){
								hadoopNodesObj.NameNode.secondaryNameNode=true;
								hadoopNodesObj.SecondaryNameNode.nameNode=true;	
								hadoopNodesObj.NameNode.type='NameNode/SecondaryNameNode';
								hadoopNodesObj.SecondaryNameNode.type='NameNode/SecondaryNameNode';
							}
						}
						if($('#hadoopCheckBox'+i).parent().next().next().next().next().next().children().is(":checked")){
						var slaveNode={};
						var nameNode=false;
						var secNameNode=false;
							slaveNode.publicIp=nodeStatus.nodes[i][0];
							slaveNode.privateIp=nodeStatus.nodes[i][0];
							slaveNode.os=nodeStatus.nodes[i][4];
							slaveNode.type='DataNode/TaskTracker';
							slaveNode.nodeState='deploying';
							slaveNode.datacenter='';
							slaveNode.rack='';
							slaveNode.nameNode=false;
							slaveNode.dataNode=true;
							slaveNode.secondaryNameNode=false;
							if($('#hadoopCheckBox'+i).parent().next().next().next().children().is(":checked")){
								nameNode=true;
								slaveNode.nameNode=true;
								slaveNode.type='NameNode/DataNode/TaskTracker';
								hadoopNodesObj.NameNode.dataNode=true;
								hadoopNodesObj.NameNode.type='NameNode/DataNode/TaskTracker';
							}
							if($('#hadoopCheckBox'+i).parent().next().next().next().next().children().is(":checked")){
								secNameNode=true;
								slaveNode.secondaryNameNode=true;	
								slaveNode.type='SecondaryNameNode/DataNode/TaskTracker';
								hadoopNodesObj.SecondaryNameNode.dataNode=true;
								hadoopNodesObj.SecondaryNameNode.type='SecondaryNameNode/DataNode/TaskTracker';
							}
							if(nameNode && secNameNode){
								hadoopNodesObj.NameNode.type='NameNode/SecondaryNameNode/DataNode/TaskTracker';
								hadoopNodesObj.SecondaryNameNode.type='NameNode/SecondaryNameNode/DataNode/TaskTracker';
								slaveNode.type='NameNode/SecondaryNameNode/DataNode/TaskTracker';
							}
							hadoopNodesObj.slaves.push(slaveNode);
						}
						if($('#jtCheckBox'+i).is(":checked")){
							var jtNode={};
							jtNode.publicIp=nodeStatus.nodes[i][0];
							jtNode.privateIp=nodeStatus.nodes[i][0];
							jtNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.JobTracker=jtNode;
							}
						if($('#rmCheckBox'+i).is(":checked")){
							var rmNode={};
							rmNode.publicIp=nodeStatus.nodes[i][0];
							rmNode.privateIp=nodeStatus.nodes[i][0];
							rmNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.ResourceManager=rmNode;
							}
						if($('#jobHistoryCheckBox'+i).is(":checked")){
							var jhNode={};
							jhNode.publicIp=nodeStatus.nodes[i][0];
							jhNode.privateIp=nodeStatus.nodes[i][0];
							jhNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.JobHistoryServer=jhNode;
							}
						if($('#webAppCheckBox'+i).is(":checked")){
							var webNode={};
							webNode.publicIp=nodeStatus.nodes[i][0];
							webNode.privateIp=nodeStatus.nodes[i][0];
							webNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.WebAppProxyServer=webNode;
							}
						if($('#standbyCheckBox'+i).is(":checked")){
							var snnNode={};
							snnNode.publicIp=nodeStatus.nodes[i][0];
							snnNode.privateIp=nodeStatus.nodes[i][0];
							snnNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.standByNamenode=snnNode;
							}
						if($('#journalCheckBox'+i).is(":checked")){
							var jnNode={};
							jnNode.publicIp=nodeStatus.nodes[i][0];
							jnNode.privateIp=nodeStatus.nodes[i][0];
							jnNode.os=nodeStatus.nodes[i][4];
							hadoopNodesObj.JournalNode.push(jnNode);
							}
					}
				} 
				com.impetus.ankush.hybrid_Hadoop.clusterConfCreation();
				$('#nodeCountHadoop').text(hadoopNodesObj.clusterNodeConfs.length);
			}else{
				 var count=0;
				  $(".hadoopCheck").each(function(elem) {
					  count=$(this).attr("id").split("hadoopCheckBox")[1];
					  var rowIndex = hadoopNodeTable.fnGetPosition($("#hadoopCheckBox"+ count).closest('tr')[0]);
					  var aData= hadoopNodeTable.fnGetData(rowIndex);
					  if($(this).parent().next().next().next().children().is(":checked")){
							hadoopNodesObj.NameNode.publicIp=aData[1];
							hadoopNodesObj.NameNode.privateIp=aData[1];
							hadoopNodesObj.NameNode.os=aData[aData.length-2];
							hadoopNodesObj.NameNode.type='NameNode';
							hadoopNodesObj.NameNode.nodeState='deploying';
							hadoopNodesObj.NameNode.datacenter='';
							hadoopNodesObj.NameNode.rack='';
							hadoopNodesObj.NameNode.nameNode=true;
							hadoopNodesObj.NameNode.dataNode=false;
							hadoopNodesObj.NameNode.secondaryNameNode=false;
						}
					  if($(this).parent().next().next().next().next().children().is(":checked")){
							hadoopNodesObj.SecondaryNameNode.publicIp=aData[1];
							hadoopNodesObj.SecondaryNameNode.privateIp=aData[1];
							hadoopNodesObj.SecondaryNameNode.os=aData[aData.length-2];
							hadoopNodesObj.SecondaryNameNode.type='SecondaryNameNode';
							hadoopNodesObj.SecondaryNameNode.nodeState='deploying';
							hadoopNodesObj.SecondaryNameNode.datacenter='';
							hadoopNodesObj.SecondaryNameNode.rack='';
							hadoopNodesObj.SecondaryNameNode.nameNode=false;
							hadoopNodesObj.SecondaryNameNode.dataNode=false;
							hadoopNodesObj.SecondaryNameNode.secondaryNameNode=true;
							 if($(this).parent().next().next().next().children().is(":checked")){
								hadoopNodesObj.SecondaryNameNode.nameNode=true;
								hadoopNodesObj.NameNode.secondaryNameNode=true;
								hadoopNodesObj.NameNode.type='NameNode/SecondaryNameNode';
								hadoopNodesObj.SecondaryNameNode.type='NameNode/SecondaryNameNode';
							}
						}
						var nameNode=false;
						var secNameNode=false;
						if($(this).parent().next().next().next().next().next().children().is(":checked")){
							var slaveNode={};
							slaveNode.publicIp=aData[1];
							slaveNode.privateIp=aData[1];
							slaveNode.os=aData[aData.length-2];
							slaveNode.type='DataNode/TaskTracker';
							slaveNode.nodeState='deploying';
							slaveNode.datacenter='';
							slaveNode.rack='';
							slaveNode.nameNode=false;
							slaveNode.dataNode=true;
							slaveNode.secondaryNameNode=false;
							 if($(this).parent().next().next().next().children().is(":checked")){
								nameNode=true;
								slaveNode.nameNode=true;
								slaveNode.type='NameNode/DataNode/TaskTracker';
								hadoopNodesObj.NameNode.dataNode=true;
								hadoopNodesObj.NameNode.type='NameNode/DataNode/TaskTracker';
							}
							 if($(this).parent().next().next().next().next().children().is(":checked")){
								secNameNode=true;
								slaveNode.secondaryNameNode=true;	
								slaveNode.type='SecondaryNameNode/DataNode/TaskTracker';
								hadoopNodesObj.SecondaryNameNode.dataNode=true;
								hadoopNodesObj.SecondaryNameNode.type='SecondaryNameNode/DataNode/TaskTracker';
							}
							if(nameNode && secNameNode){
								hadoopNodesObj.NameNode.type='NameNode/SecondaryNameNode/DataNode/TaskTracker';
								hadoopNodesObj.SecondaryNameNode.type='NameNode/SecondaryNameNode/DataNode/TaskTracker';
								slaveNode.type='NameNode/SecondaryNameNode/DataNode/TaskTracker';
							}
							hadoopNodesObj.slaves.push(slaveNode);
						}
						if($('#jtCheckBox'+count).is(':checked')){
							hadoopNodesObj.JobTracker.publicIp=aData[1];
							hadoopNodesObj.JobTracker.privateIp=aData[1];
							hadoopNodesObj.JobTracker.os=aData[aData.length-2];
							hadoopNodesObj.JobTracker.nodeState='deploying';
						}
						if($('#rmCheckBox'+count).is(':checked')){
							hadoopNodesObj.ResourceManager.publicIp=aData[1];
							hadoopNodesObj.ResourceManager.privateIp=aData[1];
							hadoopNodesObj.ResourceManager.os=aData[aData.length-2];
							hadoopNodesObj.ResourceManager.nodeState='deploying';
						}
						if($('#jobHistoryCheckBox'+count).is(':checked')){
							hadoopNodesObj.JobHistoryServer.publicIp=aData[1];
							hadoopNodesObj.JobHistoryServer.privateIp=aData[1];
							hadoopNodesObj.JobHistoryServer.os=aData[aData.length-2];
							hadoopNodesObj.JobHistoryServer.nodeState='deploying';
						}
						if($('#webAppCheckBox'+count).is(':checked')){
							hadoopNodesObj.WebAppProxyServer.publicIp=aData[1];
							hadoopNodesObj.WebAppProxyServer.privateIp=aData[1];
							hadoopNodesObj.WebAppProxyServer.os=aData[aData.length-2];
							hadoopNodesObj.WebAppProxyServer.nodeState='deploying';
						}
						if($('#standbyCheckBox'+count).is(':checked')){
							hadoopNodesObj.standByNamenode.publicIp=aData[1];
							hadoopNodesObj.standByNamenode.privateIp=aData[1];
							hadoopNodesObj.standByNamenode.os=aData[aData.length-2];
							hadoopNodesObj.standByNamenode.nodeState='deploying';
						}
						if($('#journalCheckBox'+count).is(':checked')){
							var jnNode={};
							jnNode.publicIp=aData[1];
							jnNode.privateIp=aData[1];
							jnNode.os=aData[aData.length-2];
							jnNode.nodeState='deploying';
							hadoopNodesObj.JournalNode.push(jnNode);
						}
				  });
				  com.impetus.ankush.hybrid_Hadoop.clusterConfCreation();
			}
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				for ( var j = 0; j < hadoopNodesObj.clusterNodeConfs.length; j++) {
					if(hadoopNodesObj.clusterNodeConfs[j].publicIp==nodeStatus.nodes[i][0]){
						var newArr=new Array();
						if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.NameNode.publicIp){
							newArr.push("NameNode");
							nodeRoleArray[i].role.NameNode=1;
							nodeRoleArray[i].roles.Hadoop=newArr;
						}
						if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.SecondaryNameNode.publicIp){
							newArr.push("SecondaryNameNode");
							nodeRoleArray[i].role.SecondaryNameNode=1;
							nodeRoleArray[i].roles.Hadoop=newArr;
						}
						if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.ResourceManager.publicIp){
							newArr.push("ResourceManager");
							nodeRoleArray[i].role.ResourceManager=1;
							nodeRoleArray[i].roles.Hadoop=newArr;
						}
						for ( var a = 0; a < hadoopNodesObj.slaves.length; a++) {
							if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.slaves[a].publicIp){
								newArr.push("DataNode");
								newArr.push("TaskTracker");
								nodeRoleArray[i].role.DataNode=1;
								nodeRoleArray[i].role.TaskTracker=1;
								if(undefined==hadoopObj.version || hadoopObj.version.split(".")[0]==2){
								nodeRoleArray[i].role.TaskTracker=0;
								var index=newArr.indexOf("TaskTracker");
								newArr[index]="NodeManager";
								nodeRoleArray[i].role.NodeManager=1;
								}
								nodeRoleArray[i].roles.Hadoop=newArr;
							}
						}
						if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.WebAppProxyServer.publicIp){
							newArr.push("WebAppProxyServer");
							nodeRoleArray[i].role.WebAppProxyServer=1;
							nodeRoleArray[i].roles.Hadoop=newArr;
						}
						if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.JobTracker.publicIp){
							newArr.push("JobTracker");
							nodeRoleArray[i].role.JobTracker=1;
							nodeRoleArray[i].roles.Hadoop=newArr;
						}
						if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.JobHistoryServer.publicIp){
							newArr.push("JobHistoryServer");
							nodeRoleArray[i].role.JobHistoryServer=1;
							nodeRoleArray[i].roles.Hadoop=newArr;
						}
						if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.standByNamenode.publicIp){
							newArr.push("standByNamenode");
							nodeRoleArray[i].role.standByNamenode=1;
							nodeRoleArray[i].roles.Hadoop=newArr;
						}
						for ( var a = 0; a < hadoopNodesObj.JournalNode.length; a++) {
							if(hadoopNodesObj.clusterNodeConfs[j].publicIp==hadoopNodesObj.JournalNode[a].publicIp){
								newArr.push("JournalNode");
								nodeRoleArray[i].role.JournalNode=1;
								nodeRoleArray[i].roles.Hadoop=newArr;
							}
						}
						hadoopNodeMap[hadoopNodesObj.clusterNodeConfs[j].publicIp]={};
					}
				}
			}
			$("#nodeMapHadoop").removeClass("btn-danger");
			$('#nodeCountHadoop').text(Object.keys(hadoopNodeMap).length);
			com.impetus.ankush.hybridClusterCreation.nodeRoleMap('nodeRole');
		},	
		
		clusterConfCreation: function(){
			var snn=false;
			var nn=false;
			var jt=false;
			var nodeConfMap={};
			for(var a=0;a<hadoopNodesObj.slaves.length;a++){
				hadoopNodesObj.clusterNodeConfs.push(hadoopNodesObj.slaves[a]);
			}
			if(Object.keys(hadoopNodesObj.SecondaryNameNode).length>0){
				for(var a=0;a<hadoopNodesObj.clusterNodeConfs.length;a++){
					if(hadoopNodesObj.clusterNodeConfs[a].publicIp == hadoopNodesObj.SecondaryNameNode.publicIp){
						hadoopNodesObj.clusterNodeConfs[a].secondaryNameNode=true;
						snn=true;
					}
				}
			}
			if(Object.keys(hadoopNodesObj.SecondaryNameNode).length>0 && snn==false){
				hadoopNodesObj.clusterNodeConfs.push(hadoopNodesObj.SecondaryNameNode);
			}
			for(var a=0;a<hadoopNodesObj.clusterNodeConfs.length;a++){
				if(hadoopNodesObj.clusterNodeConfs[a].publicIp == hadoopNodesObj.NameNode.publicIp){
					hadoopNodesObj.clusterNodeConfs[a].nameNode=true;
					nn=true;
				}
				if(hadoopNodesObj.clusterNodeConfs[a].publicIp == hadoopNodesObj.JobTracker.publicIp){
					jt=true;
				}
			}
			if(!nn){
				hadoopNodesObj.clusterNodeConfs.push(hadoopNodesObj.NameNode);
			}
			if(!jt){
				hadoopNodesObj.clusterNodeConfs.push(hadoopNodesObj.JobTracker);
			}
			nodeConfMap[hadoopNodesObj.ResourceManager.publicIp]=hadoopNodesObj.ResourceManager;
			nodeConfMap[hadoopNodesObj.JobHistoryServer.publicIp]=hadoopNodesObj.JobHistoryServer;
			nodeConfMap[hadoopNodesObj.WebAppProxyServer.publicIp]=hadoopNodesObj.WebAppProxyServer;
			nodeConfMap[hadoopNodesObj.standByNamenode.publicIp]=hadoopNodesObj.standByNamenode;
			for(var k=0;k<hadoopNodesObj.JournalNode.length;k++){
				nodeConfMap[hadoopNodesObj.JournalNode.publicIp]=hadoopNodesObj.JournalNode[k];	
			}
			for(var k=0;k<hadoopNodesObj.clusterNodeConfs.length;k++){
				nodeConfMap[hadoopNodesObj.clusterNodeConfs[k].publicIp]=hadoopNodesObj.clusterNodeConfs[k];	
			}
			
			for(var key in nodeConfMap){
				if(Object.keys(nodeConfMap[key]).length>0){
					hadoopNodesObj.clusterNodeConfs.push(nodeConfMap[key]);
				}
			}
		},
};
