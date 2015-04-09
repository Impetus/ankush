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
com.impetus.ankush.hybrid_Cassandra={
		tooltipInitialize:function(){
			$('#vendorDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.vendor);
			$('#versionDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.version);
			$('#downloadPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.downloadPath);
			$('#localPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.localPath);
			$('#installationPathCassandra').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.installationPath);
			$('#partitionerDropDown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.partitioner);
			$('#snitchDropDown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.snitch);
			$('#rpcPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.rpcPort);
			$('#jmxPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.jmxPort);
			$('#storagePort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.storagePort);
			$('#dataDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.dataDirectory);
			$('#logDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.logDirectory);
			$('#savedCachesDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.savedCacheDirectory);
			$('#commitlogDir').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.commitLogDirectory);
		},
		cassandraConfigPopulate:function(){
			com.impetus.ankush.hybrid_Cassandra.tooltipInitialize();
			if(setupDetailData==null || undefined==setupDetailData.components.Cassandra){
			$('#vendorDropdown').html('');
			$('#versionDropdown').html('');
			$('#partitionerDropDown').html('');
			$('#snitchDropDown').html('');
			var cassandraData=jsonDataHybrid.hybrid.Cassandra;
			for ( var key in cassandraData.Vendors){
				$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in cassandraData.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in jsonDataHybrid.hybrid.Cassandra.Defaults.Partitioner){
				$("#partitionerDropDown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in jsonDataHybrid.hybrid.Cassandra.Defaults.Snitch){
				$("#snitchDropDown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			console.log(cassandraObj);
			$('#downloadPath').val(cassandraData.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].downloadUrl);
			if(Object.keys(cassandraObj.Defaults).length > 1){
				cassandraData.Defaults.installationPath=cassandraObj.Defaults.installationPath;
				cassandraData.Defaults.rpcPort=cassandraObj.Defaults.rpcPort;
				cassandraData.Defaults.jmxPort=cassandraObj.Defaults.jmxPort;
				cassandraData.Defaults.storagePort=cassandraObj.Defaults.storagePort;
				cassandraData.Defaults.logDir=cassandraObj.Defaults.logDir;
				cassandraData.Defaults.savedCachesDir=cassandraObj.Defaults.savedCachesDir;
				cassandraData.Defaults.commitlogDir=cassandraObj.Defaults.commitlogDir;
				cassandraData.Defaults.dataDir=cassandraObj.Defaults.dataDir;
				cassandraData.Defaults.vNodeEnabled=cassandraObj.Defaults.vNodeEnabled;
				$("#vendorDropdown").val(cassandraObj.vendor);
				$('#versionDropdown').html('');
				for ( var key in cassandraData.Vendors[$("#vendorDropdown").val()]){
					$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				if(cassandraObj.Defaults.vNodeEnabled){
					$('#vNodeCheck').prop('checked',true);
				}else{
					$('#vNodeCheck').prop('checked',false);
				}
				$("#versionDropdown").val(cassandraObj.version);
				$("#partitionerDropDown").val(cassandraObj.Defaults.partitioner);
				$("#snitchDropDown").val(cassandraObj.Defaults.snitch);
				if(cassandraObj.sourceType=="DOWNLOAD"){
					$('#downloadPathDiv').show();
					$('#localPathDiv').hide();
					$('#bundleSourceLocalPath-Cassandra').removeClass('active');
					$('#bundleSourceDownload-Cassandra').addClass('active');
					$('#downloadPath').val(cassandraObj.path);
				}else{
					$('#localPathDiv').show();
					$('#downloadPathDiv').hide();
					$('#bundleSourceDownload-Cassandra').removeClass('active');
					$('#bundleSourceLocalPath-Cassandra').addClass('active');
					$('#localPath').val(cassandraObj.path);
				}
			}
			$('#installationPathCassandra').val(cassandraData.Defaults.installationPath);
			$("#rpcPort").val(cassandraData.Defaults.rpcPort);
			$("#jmxPort").val(cassandraData.Defaults.jmxPort);
			$("#storagePort").val(cassandraData.Defaults.storagePort);
			$("#dataDir").val(cassandraData.Defaults.dataDir);
			$("#logDir").val(cassandraData.Defaults.logDir);
			$("#savedCachesDir").val(cassandraData.Defaults.savedCachesDir);
			$("#commitlogDir").val(cassandraData.Defaults.commitlogDir);
			
			var user=$.trim($('#inputUserName').val());
			if(user==''){
				return;
			}
			var installationPathCassandra=$.trim($('#installationPathCassandra').val()).split(userName).join('/home/'+user);
			var logDir=$.trim($('#logDir').val()).split(userName).join('/home/'+user);
			var dataDir=$.trim($('#dataDir').val()).split(userName).join('/home/'+user);
			var savedCachesDir=$.trim($('#savedCachesDir').val()).split(userName).join('/home/'+user);
			var commitlogDir=$.trim($('#commitlogDir').val()).split(userName).join('/home/'+user);
			userName='/home/'+user;
			$('#installationPathCassandra').empty().val(installationPathCassandra);
			$('#logDir').empty().val(logDir);
			$('#dataDir').empty().val(dataDir);
			$('#savedCachesDir').empty().val(savedCachesDir);
			$('#commitlogDir').empty().val(commitlogDir);
			}else{
				if(cluster_State!=com.impetus.ankush.constants.stateError){
					com.impetus.ankush.common.pageStyleChange();
					$('.btnGrp').attr('disabled','disabled');
					$('#cassandraRevertBtn').attr('disabled','disabled');
					$('#cassandraApplyBtn').attr('disabled','disabled');	
				}
					$("#vendorDropdown").html('');
					$("#versionDropdown").html('');
					$("#partitionerDropDown").html('');
					$("#snitchDropDown").html('');
					for ( var key in jsonDataHybrid.hybrid.Cassandra.Vendors){
						$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
					}
					for ( var key in jsonDataHybrid.hybrid.Cassandra.Vendors[$("#vendorDropdown").val()]){
						$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
					}
					for ( var key in jsonDataHybrid.hybrid.Cassandra.Defaults.Partitioner){
						$("#partitionerDropDown").append("<option value=\"" + key + "\">" + key + "</option>");
					}
					for ( var key in jsonDataHybrid.hybrid.Cassandra.Defaults.Snitch){
						$("#snitchDropDown").append("<option value=\"" + key + "\">" + key + "</option>");
					}
				
				if (Object.keys(cassandraObj.Defaults).length==1 ){
					cassandraObj=jsonDataHybrid.hybrid.Cassandra;
					com.impetus.ankush.hybrid_Cassandra.redeployDataPrepare();
				}
				$("#vendorDropdown").val(cassandraObj.vendor);
				$("#versionDropdown").val(cassandraObj.version);
				if(cassandraObj.sourceType=="DOWNLOAD"){
					$('#downloadPathDiv').show();
					$('#localPathDiv').hide();
					$('#bundleSourceLocalPath-Cassandra').removeClass('active');
					 $('#bundleSourceDownload-Cassandra').addClass('active');
					$('#downloadPath').val(cassandraObj.path);
				}else{
					$('#localPathDiv').show();
					$('#downloadPathDiv').hide();
					$('#bundleSourceDownload-Cassandra').removeClass('active');
					$('#bundleSourceLocalPath-Cassandra').addClass('active');
					$('#localPath').val(cassandraObj.path);
				}
				if(cassandraObj.Defaults.vNodeEnabled){
					$('#vNodeCheck').prop('checked',true);
				}else{
					$('#vNodeCheck').prop('checked',false);
				}
				$("#partitionerDropDown").val(cassandraObj.Defaults.partitioner);
				$("#snitchDropDown").val(cassandraObj.Defaults.snitch);
				$('#installationPathCassandra').val(cassandraObj.Defaults.installationPath);
				$("#rpcPort").val(cassandraObj.Defaults.rpcPort);
				$("#jmxPort").val(cassandraObj.Defaults.jmxPort);
				$("#storagePort").val(cassandraObj.Defaults.storagePort);
				$("#dataDir").val(cassandraObj.Defaults.dataDir);
				$("#logDir").val(cassandraObj.Defaults.logDir);
				$("#savedCachesDir").val(cassandraObj.Defaults.savedCachesDir);
				$("#commitlogDir").val(cassandraObj.Defaults.commitlogDir);
			}
		},
		
		redeployDataPrepare: function(){
			if (Object.keys(cassandraObj.Defaults).length==1 ){
				cassandraObj=jsonDataHybrid.hybrid.Cassandra;
			}
			cassandraObj.vendor=setupDetailData.components.Cassandra.vendor;
			cassandraObj.version=setupDetailData.components.Cassandra.version;
			cassandraObj.path=setupDetailData.components.Cassandra.source;
			cassandraObj.sourceType=setupDetailData.components.Cassandra.sourceType;
			cassandraObj.Defaults.installationPath=setupDetailData.components.Cassandra.installPath;
			cassandraObj.Defaults.rpcPort=setupDetailData.components.Cassandra.advanceConf.rpcPort;
			cassandraObj.Defaults.jmxPort=setupDetailData.components.Cassandra.advanceConf.jmxPort;
			cassandraObj.Defaults.storagePort=setupDetailData.components.Cassandra.advanceConf.storagePort;
			cassandraObj.Defaults.dataDir=setupDetailData.components.Cassandra.advanceConf.dataDir;
			cassandraObj.Defaults.vNodeEnabled=setupDetailData.components.Cassandra.advanceConf.vNodeEnabled;
			cassandraObj.Defaults.logDir=setupDetailData.components.Cassandra.advanceConf.logDir;
			cassandraObj.Defaults.savedCachesDir=setupDetailData.components.Cassandra.advanceConf.savedCachesDir;
			cassandraObj.Defaults.commitlogDir=setupDetailData.components.Cassandra.advanceConf.commitlogDir;
			cassandraObj.Defaults.partitioner=setupDetailData.components.Cassandra.advanceConf.partitioner;
			cassandraObj.Defaults.snitch=setupDetailData.components.Cassandra.advanceConf.snitch;
			cassandraObj.Defaults.confState=setupDetailData.components.Cassandra.confState;
		},
/*Function for changing version and download path on change of Cassandra vendor dropdown*/
		vendorOnChangeCassandra:function(){
			$("#versionDropdown").html('');
			for ( var key in jsonDataHybrid.hybrid.Cassandra.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath').val(jsonDataHybrid.hybrid.Cassandra.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].downloadUrl);
		},
/*Function for changing  download path on change of version Cassandra dropdown*/
		versionOnChangeCassandra:function(){
			$('#downloadPath').val(jsonDataHybrid.hybrid.Cassandra.Vendors[$("#vendorDropdown").val()][$("#versionDropdown").val()].downloadUrl);
		},
		cassandraConfigValidate:function(){
			$('#validateErrorCassandra').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMainCassandra").html('').hide();
	        errorCount = 0;
	        if (!com.impetus.ankush.validation.empty($('#vendorDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No cassandra vendor selected';
  	            var divId='vendorDropdown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
  	        }else {
	            $('#vendorDropdown').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#versionDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No cassandra version selected';
  	            var divId='versionDropdown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
  	        }else {
	            $('#versionDropdown').removeClass('error-box');
	        }
	        if($("#sourcePathBtnGrp .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#downloadPath').val())){
	                errorCount++;
	                errorMsg = 'Download Path field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('downloadPath','Download path cannot be empty');
	                var divId='downloadPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('downloadPath',com.impetus.ankush.tooltip.cassandraClusterCreation.downloadPath);
	            }
	        }else{
	        	if(!com.impetus.ankush.validation.empty($('#localPath').val())){
	                errorCount++;
	                errorMsg = 'Local path field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('localPath','Local path cannot be empty');
	                var divId='localPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('localPath',com.impetus.ankush.tooltip.cassandraClusterCreation.localPath);
	            }
	        }
	        if (!com.impetus.ankush.validation.empty($('#installationPathCassandra').val())) {
  	            errorCount++;
  	            errorMsg = 'Installation Path field Empty';
  	            com.impetus.ankush.common.tooltipMsgChange('installationPathCassandra','Installation Path cannot be empty');
  	            var divId='installationPathCassandra';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
  	        }else {
	            com.impetus.ankush.common.tooltipOriginal('installationPathCassandra',com.impetus.ankush.tooltip.cassandraClusterCreation.installationPath);
	        }
	        if (!com.impetus.ankush.validation.empty($('#partitionerDropDown').val())) {
  	            errorCount++;
  	            errorMsg = 'No partitioner selected';
  	            var divId='partitionerDropDown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
  	        }else {
	            $('#partitionerDropDown').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#snitchDropDown').val())) {
  	            errorCount++;
  	            errorMsg = 'No snitch selected';
  	            var divId='snitchDropDown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
  	        }else {
	            $('#snitchDropDown').removeClass('error-box');
	        }
	        
	        if(!com.impetus.ankush.validation.empty($('#rpcPort').val())){
                errorCount++;
                errorMsg = 'RPC Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('rpcPort','RPC Port cannot be empty');
                var divId='rpcPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#rpcPort').val())) {
                errorCount++;
                errorMsg = 'RPC Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('rpcPort','RPC Port must be numeric');
                var divId='rpcPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            } else if (!com.impetus.ankush.validation.oPort($('#rpcPort').val())) {
                errorCount++;
                errorMsg = 'RPC Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('rpcPort','RPC Port must be between 1024-65535');
                var divId='rpcPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            } else{
                com.impetus.ankush.common.tooltipOriginal('rpcPort',com.impetus.ankush.tooltip.cassandraClusterCreation.rpcPort);
            }
	        if(!com.impetus.ankush.validation.empty($('#jmxPort').val())){
                errorCount++;
                errorMsg = 'JMX Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port cannot be empty');
                var divId='jmxPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be numeric');
                var divId='jmxPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            } else if (!com.impetus.ankush.validation.oPort($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be between 1024-65535');
                var divId='jmxPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            } else{
                com.impetus.ankush.common.tooltipOriginal('jmxPort',com.impetus.ankush.tooltip.cassandraClusterCreation.jmxPort);
            }
	        if(!com.impetus.ankush.validation.empty($('#storagePort').val())){
                errorCount++;
                errorMsg = 'Storage Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('storagePort','Storage Port cannot be empty');
                var divId='storagePort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#storagePort').val())) {
                errorCount++;
                errorMsg = 'Storage Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('storagePort','Storage Port must be numeric');
                var divId='storagePort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            } else if (!com.impetus.ankush.validation.oPort($('#storagePort').val())) {
                errorCount++;
                errorMsg = 'Storage Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('storagePort','Storage Port must be between 1024-65535');
                var divId='storagePort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            } else{
                com.impetus.ankush.common.tooltipOriginal('storagePort',com.impetus.ankush.tooltip.cassandraClusterCreation.storagePort);
            }
	        
	        if(!com.impetus.ankush.validation.empty($('#dataDir').val())){
                errorCount++;
                errorMsg = 'Data Directory Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('dataDir','Data directory path cannot be empty');
                var divId='dataDir';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('dataDir',com.impetus.ankush.tooltip.cassandraClusterCreation.dataDirectory);
            }
	        if(!com.impetus.ankush.validation.empty($('#logDir').val())){
                errorCount++;
                errorMsg = 'Log Directory Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('logDir','Log directory path cannot be empty');
                var divId='logDir';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('logDir',com.impetus.ankush.tooltip.cassandraClusterCreation.logDirectory);
            }
	        if(!com.impetus.ankush.validation.empty($('#savedCachesDir').val())){
                errorCount++;
                errorMsg = 'Saved Cache Directory Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('savedCachesDir','Saved cache directory path cannot be empty');
                var divId='savedCachesDir';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('savedCachesDir',com.impetus.ankush.tooltip.cassandraClusterCreation.savedCacheDirectory);
            }
	        if(!com.impetus.ankush.validation.empty($('#commitlogDir').val())){
                errorCount++;
                errorMsg = 'Commit Log Directory Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('commitlogDir','Commit log directory path cannot be empty');
                var divId='commitlogDir';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainCassandra',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('commitlogDir',com.impetus.ankush.tooltip.cassandraClusterCreation.commitLogDirectory);
            }
	        if(errorCount>0 && errorMsg!=''){
	        	$('#validateErrorCassandra').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
	        	  $("#errorDivMainCassandra").show();
	        }else{
	        	 com.impetus.ankush.hybrid_Cassandra.cassandraConfigApply();
	        } 
		},
		cassandraConfigApply:function(){
			cassandraObj.vendor=$('#vendorDropdown').val();
			cassandraObj.version=$('#versionDropdown').val();
			  if($("#sourcePathBtnGrp .active").data("value")==1){
				  cassandraObj.sourceType="LOCAL";
				  cassandraObj.path=$('#localPath').val();
			}else{
				cassandraObj.sourceType="DOWNLOAD";
				cassandraObj.path=$('#downloadPath').val();
			}
			  cassandraObj.Defaults.installationPath=$('#installationPathCassandra').val();
			  cassandraObj.Defaults.partitioner=$('#partitionerDropDown').val();
			  cassandraObj.Defaults.snitch=$('#snitchDropDown').val();
			  cassandraObj.Defaults.rpcPort=$('#rpcPort').val();
			  cassandraObj.Defaults.jmxPort=$('#jmxPort').val();
			  cassandraObj.Defaults.storagePort=$('#storagePort').val();
			  cassandraObj.Defaults.dataDir=$('#dataDir').val();
			  cassandraObj.Defaults.logDir=$('#logDir').val();
			  cassandraObj.Defaults.savedCachesDir=$('#savedCachesDir').val();
			  cassandraObj.Defaults.commitlogDir=$('#commitlogDir').val();
			  cassandraObj.Defaults.vNodeEnabled=false;
			  if ($("#vNodeCheck").is(':checked')) {
				  cassandraObj.Defaults.vNodeEnabled=true;
			  }
			 $('#confTypeCassandra').text('Custom');
			 $("#confPageCassandra").removeClass("btn-danger btn-primary");
			 com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},
		
		cassandraNodesPopulate:function(){
			if(cassandraNodeTable!=null){
				cassandraNodeTable.fnClearTable();
			}
			if(nodeStatus==null){
				return;
			}
				 for (var i = 0; i < nodeStatus.nodes.length; i++){
	           	  var addId=null;
	                    addId = cassandraNodeTable.fnAddData([
	                                      '<input type="checkbox" name="" value="" onclick="com.impetus.ankush.hybrid_Cassandra.cassandraCheckBox('
	  									+ i + ',\'cassandraNodeTable\',\'cassandraNodeCheckBox\',\'nodeCheckCassandraHead\');" id="cassandraNodeCheck'
	                                      + i
	                                      + '" class="cassandraNodeCheckBox"/>',
	                                    nodeStatus.nodes[i][0],
	                                    '<span class="" id="nodeRoleCassandra'+i+'">'+$('#nodeRole' + i).text()+'</span>',
	                                    '<input type="checkbox" name="" value="" id="seedNodeCheck'
	                                    + i
	                                    + '" class="seedNodeCheckBox" onclick="com.impetus.ankush.hybrid_Cassandra.seedNodeCheck('+i+');"/>',
	                                    nodeStatus.nodes[i][4],
	                                    '<a class="editableLabel vNodeCount" id="vNodeCount'
	                                    + i + '">'+jsonDataHybrid.hybrid.Cassandra.Defaults.vNodeCount+'</a>',
	                                    '<a href="##" onclick="com.impetus.ankush.hybrid_Cassandra.loadCassandraNodeDetail(this);"><img id="navigationImg-'
	                                            + i
	                                            + '" src="'
	                                            + baseUrl
	                                            + '/public/images/icon-chevron-right.png" /></a>' ]);
	                var theNode = cassandraNodeTable.fnSettings().aoData[addId[0]].nTr;
	                theNode.setAttribute('id', 'cassandra'+ nodeStatus.nodes[i][0].split('.').join('_'));
	                if (nodeStatus.nodes[i][1] == false || nodeStatus.nodes[i][2] == false || nodeStatus.nodes[i][3] == false){
	                    rowId = nodeStatus.nodes[i][0].split('.').join('_');
	                    $('td', $('#cassandra'+rowId)).addClass('error-row');
	                    $('#cassandra' + rowId).addClass('error-row');
	                    $('#cassandraNodeCheck' + i).attr('disabled', true);
	                    $('#seedNodeCheck' + i).attr('disabled', true);
	                }
	                }
			if(setupDetailData==null || undefined==setupDetailData.components.Cassandra){
			 if(cassandraNodesObj.nodes.length>0){
				 for(var i=0;i<cassandraNodesObj.nodes.length;i++){
					 for(var j=0;j<nodeStatus.nodes.length;j++){
						 $('#cassandraNodeCheck' + j).removeAttr('disabled');
						 $('#seedNodeCheck' + j).removeAttr('disabled');
						 if(cassandraNodesObj.nodes[i].publicIp==nodeStatus.nodes[j][0]){
		     				 $('#vNodeCount'+j).text(cassandraNodesObj.nodes[i].vNodeCount);
		     				 if(cassandraNodesObj.nodes[i].seedNode){
		     					 $('#seedNodeCheck'+j).attr('checked',true); 
			     			 }
		     				 $('#cassandraNodeCheck'+j).attr('checked',true); 
		     				 break;
	     			   }
					}
				}
			}
		}else{
				 if(Object.keys(cassandraNodeMap).length>0){
					 for (var i = 0; i < nodeRoleArray.length; i++) {
							 for(var j=0;j<cassandraNodesObj.nodes.length;j++){
								 if(cassandraNodesObj.nodes[j].publicIp==nodeRoleArray[i].ip){
									 $('#cassandraNodeCheck'+i).attr('checked',true); 
									 $('#vNodeCount'+i).text(cassandraNodesObj.nodes[j].vNodeCount);
								 if(nodeRoleArray[i].role.CassandraSeed=="1"){
									 $('#seedNodeCheck'+i).prop('checked',true);
								 }else{
									 $('#seedNodeCheck'+i).prop('checked',false);
								 }
							}
						}
					 }
				}
				 if(cluster_State!=com.impetus.ankush.constants.stateError){
						$('.seedNodeCheckBox').attr('disabled', true);
						$('.cassandraNodeCheckBox').attr('disabled', true);
						$('#cassandraNodesApply').attr('disabled', true);
						 $('#cassandraNodesRevert').attr('disabled', true);
						 $('#nodeCheckCassandraHead').attr('disabled', true);
					}else{
						 $('.vNodeCount').removeClass('editableLable');
					}
			}
			 $('.editableLabel').editable({
                 type : 'text',
             });
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
				$('.seedNodeCheckBox').attr('checked', false);
			}
		},
		seedNodeCheck : function(i){
			if($('#seedNodeCheck'+i).is(':checked')){
				$('#cassandraNodeCheck'+i).attr('checked','checked');
			}
			if ($("#cassandraNodeTable .cassandraNodeCheckBox:checked").length == $("#cassandraNodeTable .cassandraNodeCheckBox").length
		               - disableNodeCount) {
		        $("#nodeCheckCassandraHead").prop("checked", true);
		     } 
		},
		 cassandraCheckBox : function(i,table,nodeCheck,checkHead) {
		        if ($("#"+table+" ."+nodeCheck+":checked").length == $("#"+table+" ."+nodeCheck).length
		                - disableNodeCount) {
		            $("#"+checkHead).prop("checked", true);
		        } else {
		            $("#"+checkHead).removeAttr("checked");
		        }
		        if(!$('#cassandraNodeCheck'+i).is(':checked')){
		        	$('#seedNodeCheck'+i).removeAttr('checked');
				}
		    },
			loadCassandraNodeDetail:function(elem){
				$('#content-panel').sectionSlider('addChildPanel', {
					current : 'login-panel',
					url : baseUrl + '/hybrid-cluster/nodeMapNodeDetail',
					method : 'get',
					title : 'Node Detail',
					callback : function(data) {
						com.impetus.ankush.hybrid_Cassandra.cassandraNodeDetail(elem);
					},
					callbackData : {
					}
				});
		},
		cassandraNodeDetail:function(elem){
			$('#nodeHead').html($("td:nth-child(2)", $(elem).parents('tr')).text());
			$('#nodeType').html($("td:nth-child(3)", $(elem).parents('tr')).text());
			$('#os').html($("td:nth-child(5)", $(elem).parents('tr')).text());
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
			if(Object.keys(cassandraNodeMap).length > 0){
				var cassandraNodeDetails = cassandraNodeMap[$('td:first', $(elem).parents('tr')).text()];
				if(cassandraNodeDetails.message !=undefined){
					$('#nodeStatus').html('').text(cassandraNodeDetails.message);	
				}
				if(cassandraNodeDetails.errors!=undefined && Object.keys(cassandraNodeDetails.errors).length>0){
					for(var key in cassandraNodeDetails.errors){
          				$('#nodeDeploymentError').append('<label class="text-left" style="color: black;" id="'+key+'">'+cassandraNodeDetails.errors[key]+'</label>');
          	        }
					$('#errorNodeDiv').show();
				}
			} 
		},
		cassandraNodesPopulateApplyValidate:function(){
	    	errorCount=0;
	    	errorMsg='';
	    	var seedNodecount=0;
	    	var nodeCount=0;
	    	var status=false;
	    	 $("#errorDivCassandraNodes").html('');
	            $('#validateErrorCassandra').html('');
	    	if(nodeStatus!=null){
    	            if ($('#cassandraNodeTable .cassandraNodeCheckBox:checked').length < 1) {
    	            	errorMsg = 'Select at least one node.';
    	                errorCount++;
    	                $("#errorDivCassandraNodes").append("<div class='errorLineDiv'><a href='#cassandraNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
    	            }else{
    	            	   $('.cassandraNodeCheckBox').each(function(){
    	            		   status= false;
    	    	            	if($('#cassandraNodeCheck'+nodeCount).is(':checked')){
    	    	            		   errorMsg='';
    		    	            	if($('#seedNodeCheck'+nodeCount).is(':checked')){
    		    	            		seedNodecount++;
    		    	            	}
    		    	            	 if (!com.impetus.ankush.validation.empty($('#vNodeCount'+nodeCount).text())|| $('#vNodeCount'+nodeCount).text()=='Empty') {                  
    		    	                        errorMsg =errorMsg+ ' vNodeCount field Empty';
    		    	                        status= true;
    		    	                        } else if (!com.impetus.ankush.validation.numeric($('#vNodeCount'+nodeCount).text())) {              
    		    	                            errorMsg = errorMsg+' vNodeCount field must be numeric';
    		    	                            status= true;
    		    	                        }
    	    	            	}
    	    	                if(status){
    		                        errorCount++;
    		                        var nodeIp=nodeStatus.nodes[nodeCount][0];
    		                       $("#errorDivCassandraNodes").append("<div class='errorLineDiv'><a href='#cassandraNodeTable' style='color: #5682C2;'>"+errorCount+".For "+nodeIp+": "+errorMsg+"</a></div>");
    		                    }
    	    	                nodeCount++;
    	    	            });
    	    	            if(seedNodecount==0){
    	    	            	errorMsg = 'Select at least one node as seedNode from selected nodes.';
    	    	                errorCount++;
    	    	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivCassandraNodes',divId,errorCount,errorMsg);
    	    	            }	
    	            }
	    		 if (errorCount > 0) {
	    	            $("#errorDivCassandraNodes").show();
	    	               if(errorCount > 1)
	    	                    $('#validateErrorCassandra').text(errorCount + " Errors");
	    	                else
	    	                    $('#validateErrorCassandra').text(errorCount + " Error");
	    	            $('#validateErrorCassandra').show().html(errorCount + ' Error');
	    	        } else {
	    	            $("#errorDivCassandraNodes").hide();
	    	            $('#validateErrorCassandra').hide();
	    	            com.impetus.ankush.hybrid_Cassandra.cassandraNodesPopulateApply();
	    	        }
	    	}
	    },
		
		cassandraNodesPopulateApply:function(){
			cassandraNodeMap={}; 	
			cassandraNodesObj.nodes=new Array();
			for ( var k = 0; k < nodeRoleArray.length; k++) {
				nodeRoleArray[k].role.CassandraSeed=0;
				nodeRoleArray[k].role.CassandraNonSeed=0;
			}
			if(cluster_State!=com.impetus.ankush.constants.stateError){
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var cassandraNode={};
				if($("#cassandraNodeCheck"+i).attr('checked')) {
					cassandraNode.vNodeCount=parseInt($('#vNodeCount'+i).text());
					cassandraNode.type='CassandraNonSeed';
					cassandraNode.seedNode=false;
					cassandraNode.publicIp=nodeStatus.nodes[i][0];
					cassandraNode.privateIp=nodeStatus.nodes[i][0];
					cassandraNode.os=nodeStatus.nodes[i][4];
					cassandraNode.nodeState='deploying';
					if($("#seedNodeCheck"+i).is(':checked')) {
						cassandraNode.seedNode=true;
						cassandraNode.type='CassandraSeed';
					}
					cassandraNodesObj.nodes.push(cassandraNode);
				}
			}
			}else{
				var nodeCount=0;
				  $(".cassandraNodeCheckBox").each(function(elem) {
					  var cassandraNode={};
						if($(this).is(':checked')) {
							cassandraNode.vNodeCount=parseInt($("td:nth-child(6)", $(this).parents('tr')).text());
							cassandraNode.type='CassandraNonSeed';
							cassandraNode.seedNode=false;
							cassandraNode.publicIp=$("td:nth-child(2)", $(this).parents('tr')).text();
							cassandraNode.privateIp=$("td:nth-child(2)", $(this).parents('tr')).text();
							cassandraNode.os=$("td:nth-child(5)", $(this).parents('tr')).text();
							cassandraNode.nodeState='deploying';
							if($(this).parent().next().next().next().children().is(":checked")){
								cassandraNode.type='CassandraSeed';
								cassandraNode.seedNode=true;
							}
							cassandraNodesObj.nodes.push(cassandraNode);
						}
						nodeCount++;
				  });
			}
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				for ( var j = 0; j < cassandraNodesObj.nodes.length; j++) {
					if(cassandraNodesObj.nodes[j].publicIp==nodeStatus.nodes[i][0]){
						var newObj={};
						var newArr=new Array();
						if(cassandraNodesObj.nodes[j].seedNode){
							newArr.push("CassandraSeed");
							nodeRoleArray[i].role.CassandraSeed=1;
							nodeRoleArray[i].roles.Cassandra=newArr;
						}else{
							newArr.push("CassandraNonSeed");
							nodeRoleArray[i].role.CassandraNonSeed=1;
							nodeRoleArray[i].roles.Cassandra=newArr;
						}
						newObj.vNodeCount=parseInt(cassandraNodesObj.nodes[j].vNodeCount);
						cassandraNodeMap[cassandraNodesObj.nodes[j].publicIp]=newObj;
					}
				}
			}
			$("#nodeMapCassandra").removeClass("btn-danger");
			$('#nodeCountCassandra').text(Object.keys(cassandraNodeMap).length);
			com.impetus.ankush.hybridClusterCreation.nodeRoleMap('nodeRole');
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},	
		
};
