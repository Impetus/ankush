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
com.impetus.ankush.hybrid_Zookeeper = {
	zookeeperConfigPopulate : function(key) {
		$("#zookeeperConfigHeader").text(key+'/Configuration');
		if(key=="Zookeeper"){
			key="Zookeeper_default";
		}
		var techName="";
		if(undefined !=key){
			 techName=key;
		}
		if (setupDetailData == null
				|| undefined == setupDetailData.components[key]) {
			userName = '/home/${user}';
			$('#zookeeperVendorDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.vendor);
			$('#zookeeperVersionDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.version);
			$('#zookeeperDownloadPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.downloadPath);
			$('#zookeeperLocalPath').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.localPath);
			$('#installationPathZookeeper').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.installationPath);
			$('#dataDirZookeeper').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.dataDir);
			$('#clientPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.clientPort);
			$('#jmxPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.jmxPort);
			$('#syncLimit').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.syncLimit);
			$('#initLimit').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.initLimit);
			$('#tickTime').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.tickTime);
			$('#zookeeperVendorDropdown').html('').attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.vendor);
			$('#zookeeperVersionDropdown').html('').attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.vendor);
			if(undefined != zookeeperObjMap[techName]){
				zookeeperObj=zookeeperObjMap[techName].zookeeperObj;
			}else{
				zookeeperObj=null;
			}
			var zookeeperData = jsonDataHybrid.hybrid.Zookeeper_default;
			for ( var key in zookeeperData.Vendors) {
				$("#zookeeperVendorDropdown").append(
						"<option value=\"" + key + "\">" + key + "</option>");
			}
			if (zookeeperObj != null) {
				$("#zookeeperVendorDropdown").val(zookeeperObj.vendor);
			}
			for ( var key in zookeeperData.Vendors[$("#zookeeperVendorDropdown")
					.val()]) {
				$("#zookeeperVersionDropdown").append(
						"<option value=\"" + key + "\">" + key + "</option>");
			}
			if (zookeeperObj != null) {
				$("#zookeeperVersionDropdown").val(zookeeperObj.version);
			}
			$('#zookeeperDownloadPath').val(zookeeperData.Vendors[$("#zookeeperVendorDropdown").val()][$("#zookeeperVersionDropdown").val()].url);
			if (zookeeperObj != null) {
				zookeeperData.Defaults = zookeeperObj.Defaults;

				if (zookeeperObj.sourceType == "DOWNLOAD") {
					$('#zookeeperDownloadPathDiv').show();
					$('#zookeeperLocalPathDiv').hide();
					$('#zookeeperLocalRadio').removeClass('active');
					$('#zookeeperDownloadRadio').addClass('active');
					$('#zookeeperDownloadPath').val(zookeeperObj.path);
				} else {
					$('#zookeeperLocalPathDiv').show();
					$('#zookeeperDownloadPathDiv').hide();
					$('#zookeeperDownloadRadio').removeClass('active');
					$('#zookeeperLocalRadio').addClass('active');
					$('#zookeeperLocalPath').val(zookeeperObj.path);
				}
			}
			$('#installationPathZookeeper').val(zookeeperData.Defaults.installationHomePath);
			$('#dataDirZookeeper').val(zookeeperData.Defaults.dataDir);
			$('#clientPort').val(zookeeperData.Defaults.clientPort);
			$('#jmxPort').val(zookeeperData.Defaults.jmxPort);
			$('#syncLimit').val(zookeeperData.Defaults.syncLimit);
			$('#initLimit').val(zookeeperData.Defaults.initLimit);
			$('#tickTime').val(zookeeperData.Defaults.tickTime);
			var user = $.trim($('#inputUserName').val());
			if (user == '') {
				return;
			}
			var installationPathZookeeper = $.trim(
					$('#installationPathZookeeper').val()).split(userName)
					.join('/home/' + user);
			var dataDirZookeeper = $.trim($('#dataDirZookeeper').val()).split(
					userName).join('/home/' + user);
			userName = '/home/' + user;
			$('#installationPathZookeeper').empty().val(
					installationPathZookeeper);
			$('#dataDirZookeeper').empty().val(dataDirZookeeper);
		} else {
			if (cluster_State != com.impetus.ankush.constants.stateError) {
				com.impetus.ankush.common.pageStyleChange();
				$('.btnGrp').attr('disabled', 'disabled');
				$('#revertZookeeper').attr('disabled', 'disabled');
				$('#applyZookeeper').attr('disabled', 'disabled');
			}
			if(undefined == zookeeperObjMap[techName]){
				zookeeperObjMap[techName]=jsonDataHybrid.hybrid.Zookeeper_default;
				com.impetus.ankush.hybrid_Zookeeper.redeployDataPrepare(techName);
			}
			zookeeperObj=zookeeperObjMap[techName].zookeeperObj;
			$("#zookeeperVendorDropdown").html('');
			$("#zookeeperVersionDropdown").html('');
			for ( var key in jsonDataHybrid.hybrid.Zookeeper_default.Vendors) {
				$("#zookeeperVendorDropdown").append(
						"<option value=\"" + key + "\">" + key + "</option>");
			}
			$("#zookeeperVendorDropdown").val(zookeeperObj.vendor);
			for ( var key in jsonDataHybrid.hybrid.Zookeeper_default.Vendors[$(
					"#zookeeperVendorDropdown").val()]) {
				$("#zookeeperVersionDropdown").append(
						"<option value=\"" + key + "\">" + key + "</option>");
			}
			$("#zookeeperVersionDropdown").val(zookeeperObj.version);
			if (zookeeperObj.sourceType == "DOWNLOAD") {
				$('#zookeeperDownloadPathDiv').show();
				$('#zookeeperLocalPathDiv').hide();
				$('#zookeeperLocalRadio').removeClass('active');
				$('#zookeeperDownloadRadio').addClass('active');
				$('#zookeeperDownloadPath').val(zookeeperObj.path);
			} else {
				$('#zookeeperLocalPathDiv').show();
				$('#zookeeperDownloadPathDiv').hide();
				$('#zookeeperDownloadRadio').removeClass('active');
				$('#zookeeperLocalRadio').addClass('active');
				$('#zookeeperLocalPath').val(zookeeperObj.path);
			}
			$('#installationPathZookeeper').val(
					zookeeperObj.Defaults.installationHomePath);
			$('#dataDirZookeeper').val(zookeeperObj.Defaults.dataDir);
			$('#clientPort').val(zookeeperObj.Defaults.clientPort);
			$('#jmxPort').val(zookeeperObj.Defaults.jmxPort);
			$('#syncLimit').val(zookeeperObj.Defaults.syncLimit);
			$('#initLimit').val(zookeeperObj.Defaults.initLimit);
			$('#tickTime').val(zookeeperObj.Defaults.tickTime);
		}
	},

	redeployDataPrepare : function(key) {
		var zookeeperObj={
				Defaults:{}
		};
		if(undefined==zookeeperObjMap[key]){
			zookeeperObjMap[key]={
					zookeeperObj:{},	
			};
			zookeeperObjMap[key].zookeeperObj=jsonDataHybrid.hybrid.Zookeeper_default;
		}
		zookeeperObj.vendor = setupDetailData.components[key].vendor;
		zookeeperObj.version = setupDetailData.components[key].version;
		zookeeperObj.path = setupDetailData.components[key].source;
		zookeeperObj.sourceType = setupDetailData.components[key].sourceType;
		zookeeperObj.Defaults.installationHomePath = setupDetailData.components[key].installPath;
		zookeeperObj.Defaults.dataDir = setupDetailData.components[key].advanceConf.dataDirectory;
		zookeeperObj.Defaults.syncLimit = setupDetailData.components[key].advanceConf.syncLimit;
		zookeeperObj.Defaults.initLimit = setupDetailData.components[key].advanceConf.initLimit;
		zookeeperObj.Defaults.clientPort = setupDetailData.components[key].advanceConf.clientPort;
		zookeeperObj.Defaults.jmxPort = setupDetailData.components[key].advanceConf.jmxPort;
		zookeeperObj.Defaults.tickTime = setupDetailData.components[key].advanceConf.tickTime;
		zookeeperObjMap[key].zookeeperObj=zookeeperObj;
	},
	/*
	 * Function for changing version and download path on change of zookeeper
	 * vendor dropdown
	 */
	vendorOnChangeZookeeper : function() {
		$("#zookeeperVersionDropdown").html('');
		for ( var key in jsonDataHybrid.hybrid.Zookeeper_default.Vendors[$(
				"#zookeeperVendorDropdown").val()]) {
			$("#zookeeperVersionDropdown").append(
					"<option value=\"" + key + "\">" + key + "</option>");
		}
		$('#zookeeperDownloadPath').val(
				jsonDataHybrid.hybrid.Zookeeper_default.Vendors[$(
						"#zookeeperVendorDropdown").val()][$(
						"#zookeeperVersionDropdown").val()].url);

	},
	/*
	 * Function for changing download path on change of version zookeeper
	 * dropdown
	 */
	versionChangeZookeeper : function() {
		$("#zookeeperDownloadPath").html('');
		$('#zookeeperDownloadPath').val(
				jsonDataHybrid.hybrid.Zookeeper_default.Vendors[$(
						"#zookeeperVendorDropdown").val()][$(
						"#zookeeperVersionDropdown").val()].url);

	},
		
		zookeeperConfigValidate:function(){
			$('#validateErrorZookeeper').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMainZookeeper").html('').hide();
	        errorCount = 0;
	        if (!com.impetus.ankush.validation.empty($('#zookeeperVendorDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No zookeeper vendor selected';
  	            var divId='zookeeperVendorDropdown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
  	        }else {
  	          com.impetus.ankush.common.tooltipOriginal('zookeeperVendorDropdown',com.impetus.ankush.tooltip.zookeeperClusterCreation.vendor);
	        }
	        if (!com.impetus.ankush.validation.empty($('#zookeeperVersionDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No zookeeper version selected';
  	            var divId='zookeeperVersionDropdown';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
  	        }else {
  	          com.impetus.ankush.common.tooltipOriginal('zookeeperVersionDropdown',com.impetus.ankush.tooltip.zookeeperClusterCreation.version);
	        }
	        if($("#sourcePathBtnGrp .active").data("value")==0){
	            if(!com.impetus.ankush.validation.empty($('#zookeeperDownloadPath').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Download Path field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('zookeeperDownloadPath','Download path cannot be empty');
	                var divId='zookeeperDownloadPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
	            }else if(!com.impetus.ankush.validation.withoutSpace($('#zookeeperDownloadPath').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Download Path contains space.';
	                com.impetus.ankush.common.tooltipMsgChange('zookeeperDownloadPath','Download path cannot contain space');
	                var divId='zookeeperDownloadPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
	            }else{
	                com.impetus.ankush.common.tooltipOriginal('zookeeperDownloadPath',com.impetus.ankush.tooltip.zookeeperClusterCreation.downloadPath);
	            }
	        }else{
	        	if(!com.impetus.ankush.validation.empty($('#zookeeperLocalPath').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Local path field empty.';
	                com.impetus.ankush.common.tooltipMsgChange('zookeeperLocalPath','Zookeeper Local path cannot be empty');
	                var divId='zookeeperLocalPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
	            }else if(!com.impetus.ankush.validation.withoutSpace($('#zookeeperLocalPath').val())){
	                errorCount++;
	                errorMsg = 'Zookeeper Local path field contains space.';
	                com.impetus.ankush.common.tooltipMsgChange('zookeeperLocalPath','Zookeeper Local path cannot contain space');
	                var divId='zookeeperLocalPath';
	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('zookeeperLocalPath',com.impetus.ankush.tooltip.zookeeperClusterCreation.localPath);
	            }
	        }
	        if(!com.impetus.ankush.validation.empty($('#installationPathZookeeper').val())){
                errorCount++;
                errorMsg = 'Zookeeper Installation Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('installationPathZookeeper','Installation path cannot be empty');
                var divId='installationPathZookeeper';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if(!com.impetus.ankush.validation.withoutSpace($('#installationPathZookeeper').val())){
                errorCount++;
                errorMsg = 'Zookeeper Installation Path field contains space.';
                com.impetus.ankush.common.tooltipMsgChange('installationPathZookeeper','Installation path cannot contain space');
                var divId='installationPathZookeeper';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('installationPathZookeeper',com.impetus.ankush.tooltip.zookeeperClusterCreation.installationPath);
            }
	        if(!com.impetus.ankush.validation.empty($('#dataDirZookeeper').val())){
                errorCount++;
                errorMsg = 'Zookeeper Data Path field empty.';
                com.impetus.ankush.common.tooltipMsgChange('dataDirZookeeper','Data path cannot be empty');
                var divId='dataDirZookeeper';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if(!com.impetus.ankush.validation.withoutSpace($('#dataDirZookeeper').val())){
                errorCount++;
                errorMsg = 'Zookeeper Data Path field contain space.';
                com.impetus.ankush.common.tooltipMsgChange('dataDirZookeeper','Data path cannot contain space');
                var divId='dataDirZookeeper';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('dataDirZookeeper',com.impetus.ankush.tooltip.zookeeperClusterCreation.dataDir);
            }
	        if(!com.impetus.ankush.validation.empty($('#clientPort').val())){
                errorCount++;
                errorMsg = 'Client Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('clientPort','Client port cannot be empty');
                var divId='clientPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#clientPort').val())) {
                errorCount++;
                errorMsg = 'Client Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('clientPort','Client Port must be numeric');
                var divId='clientPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            } else if (!com.impetus.ankush.validation.oPort($('#clientPort').val())) {
                errorCount++;
                errorMsg = 'Client Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('clientPort','Client Port must be between 1024-65535');
                var divId='clientPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            } else{
                com.impetus.ankush.common.tooltipOriginal('clientPort',com.impetus.ankush.tooltip.zookeeperClusterCreation.clientPort);
            }
	        if(!com.impetus.ankush.validation.empty($('#jmxPort').val())){
                errorCount++;
                errorMsg = 'JMX Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX port cannot be empty');
                var divId='jmxPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be numeric');
                var divId='jmxPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            } else if (!com.impetus.ankush.validation.oPort($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be between 1024-65535');
                var divId='jmxPort';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            } else{
                com.impetus.ankush.common.tooltipOriginal('jmxPort',com.impetus.ankush.tooltip.zookeeperClusterCreation.jmxPort);
            }
	        if(!com.impetus.ankush.validation.empty($('#syncLimit').val())){
                errorCount++;
                errorMsg = 'Sync Limit field empty.';
                com.impetus.ankush.common.tooltipMsgChange('syncLimit','Sync limit cannot be empty');
                var divId='syncLimit';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#syncLimit').val())) {
                errorCount++;
                errorMsg = 'Sync limit field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('syncLimit','Sync limit must be numeric');
                var divId='syncLimit';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if($('#syncLimit').val()<1) {
                errorCount++;
                errorMsg = 'Sync limit '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
                com.impetus.ankush.common.tooltipMsgChange('syncLimit',errorMsg);
                var divId='syncLimit';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('syncLimit',com.impetus.ankush.tooltip.zookeeperClusterCreation.syncLimit);
            }
	        if(!com.impetus.ankush.validation.empty($('#initLimit').val())){
                errorCount++;
                errorMsg = 'Init Limit field empty.';
                com.impetus.ankush.common.tooltipMsgChange('initLimit','Init limit cannot be empty');
                var divId='initLimit';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#initLimit').val())) {
                errorCount++;
                errorMsg = 'Init limit field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('initLimit','Init limit must be numeric');
                var divId='initLimit';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if ($('#initLimit').val()<1) {
                errorCount++;
                errorMsg = 'Init limit '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
                com.impetus.ankush.common.tooltipMsgChange('initLimit',errorMsg);
                var divId='initLimit';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('initLimit',com.impetus.ankush.tooltip.zookeeperClusterCreation.initLimit);
            }
	        if(!com.impetus.ankush.validation.empty($('#tickTime').val())){
                errorCount++;
                errorMsg = 'Ticktime field empty.';
                com.impetus.ankush.common.tooltipMsgChange('tickTime','Ticktime cannot be empty');
                var divId='tickTime';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if (!com.impetus.ankush.validation.numeric($('#tickTime').val())) {
                errorCount++;
                errorMsg = 'Ticktime field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('tickTime','Ticktime must be numeric');
                var divId='tickTime';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else if ($('#tickTime').val()<1) {
                errorCount++;
                errorMsg = 'Ticktime '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
                com.impetus.ankush.common.tooltipMsgChange('tickTime',errorMsg);
                var divId='tickTime';
                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainZookeeper',divId,errorCount,errorMsg);
            }else{
                com.impetus.ankush.common.tooltipOriginal('tickTime',com.impetus.ankush.tooltip.zookeeperClusterCreation.tickTime);
            }
	        if(errorCount>0 && errorMsg!=''){
	        	$('#validateErrorZookeeper').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
	        	$("#errorDivMainZookeeper").show();
	        }else{
	        	 com.impetus.ankush.hybrid_Zookeeper.zookeeperConfigApply();
	        }
	        
		},
		zookeeperConfigApply:function(){
			zookeeperObj1={
					Defaults:{},
			};
			var techName=$("#zookeeperConfigHeader").text().split('/')[0];
			if(undefined==zookeeperObjMap[techName]){
				zookeeperObjMap[techName]={};	
			}
			zookeeperObj1.vendor=$('#zookeeperVendorDropdown').val();
			zookeeperObj1.version=$('#zookeeperVersionDropdown').val();
			 if($("#sourcePathBtnGrp .active").data("value")==1){
				 zookeeperObj1.sourceType="LOCAL";
				 zookeeperObj1.path=$('#zookeeperLocalPath').val();
				}else{
					zookeeperObj1.sourceType="DOWNLOAD";
					zookeeperObj1.path=$('#zookeeperDownloadPath').val();
				}
			zookeeperObj1.Defaults.installationHomePath=$('#installationPathZookeeper').val();
			zookeeperObj1.Defaults.dataDir=$('#dataDirZookeeper').val();
			zookeeperObj1.Defaults.syncLimit=$('#syncLimit').val();
			zookeeperObj1.Defaults.initLimit=$('#initLimit').val();
			zookeeperObj1.Defaults.tickTime=$('#tickTime').val();
			zookeeperObj1.Defaults.clientPort=$('#clientPort').val();
			zookeeperObj1.Defaults.jmxPort=$('#jmxPort').val();
			zookeeperObjMap[techName].zookeeperObj=zookeeperObj1;
			$('#confType'+techName).text('Custom');
			$("#confPage"+techName).removeClass("btn-primary");
			$("#confPage"+techName).removeClass("open");
			$("#row"+techName).remove();
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},
		
		zookeeperNodesPopulate:function(){
			
			var techName=$("#zookeeperNodeHeader").text().split('/')[0];
			$("#zookeeperNodeHeader1").text(techName.split('_')[0]+'/Node Mapping');
			if(zookeeperNodeTable!=null){
				zookeeperNodeTable.fnClearTable();
			}
			if(techName=="Zookeeper"){
				techName="Zookeeper_default";
			}
			if(nodeStatus==null){
				return;
			}
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var addId = null;
					addId = zookeeperNodeTable.fnAddData([
											nodeStatus.nodes[i][0],
											'<span class="" id="nodeRoleZookeeper'+i+'">'+$('#nodeRole' + i).text()+'</span>',
											'<input id="zookeeperCheckBox'
												+ i
												+ '" class="zookeeperCheck" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.hybrid_Zookeeper.zookeeperCheckBox('
												+ i + ',\'zookeeperNodeTable\',\'zookeeperCheck\',\'headCheckZookeeper\');">',
											nodeStatus.nodes[i][4],
											'']);
				var theNode = zookeeperNodeTable.fnSettings().aoData[addId[0]].nTr;
				theNode.setAttribute('id', 'zookeeper'+ nodeStatus.nodes[i][0].split('.').join('_'));
				var isSudo=false;
				if(sudoFlag){	
					if($("#nodeRight"+i).text()=="Sudo"){
					}else{
						isSudo=true;
					}
				}
				if (nodeStatus.nodes[i][1] == false	|| nodeStatus.nodes[i][2] == false|| nodeStatus.nodes[i][3] == false || isSudo) {
					rowId = nodeStatus.nodes[i][0].split('.').join('_');
					$('td', $('#zookeeper'+rowId)).addClass('alert-danger');
					$('#zookeeper' + rowId).addClass('alert-danger');
					$('#zookeeperCheckBox' + i).attr('disabled', true);
				} else {
					$('#zookeeperCheckBox' + i).removeAttr('disabled');
				}
			}
			for ( var j = 0; j < nodeStatus.nodes.length; j++) {
				if(undefined!= zookeeperObjMap[techName] && undefined!= zookeeperObjMap[techName].zookeeperNodesMap){
					if (zookeeperObjMap[techName].zookeeperNodesMap[nodeStatus.nodes[j][0]] != undefined) {
						$('#zookeeperCheckBox' + j).attr('checked', true);
					}
				}
				
			}
			
			if(cluster_State == 'deploying' || cluster_State == 'deployed'){
				$('#zookeeperNodesRevert').attr('disabled', true);
				$('#zookeeperNodesApply').attr('disabled', true);
				$('#headCheckZookeeper').attr('disabled', true);
				$('.zookeeperCheck').attr('disabled', true);
			}
			$('#headCheckZookeeper').attr('checked',true);
				  
		},
		
		/*Function for check/uncheck head check box on click of Zookeeper checkbox*/
		 zookeeperCheckBox : function(i,table,nodeCheck,checkHead) {
		        if ($("#"+table+" ."+nodeCheck+":checked").length == $("#"+table+" ."+nodeCheck).length
		                - disableNodeCount) {
		            $("#"+checkHead).prop("checked", true);
		        } else {
		            $("#"+checkHead).removeAttr("checked");
		        }
		    },
		    zookeeperNodesPopulateApplyValidate:function(){
		    	errorCount=0;
		    	errorMsg='';
		    	$("#errorDivZookeeperNodes").html('');
		        $('#validateErrorZookeeper').html('');
		    	if(nodeStatus!=null){
	    	            if ($('#zookeeperNodeTable .zookeeperCheck:checked').length < 1) {
	    	            	errorMsg = 'Select at least one node as zookeeper.';
	    	                errorCount++;
	    	                flag = true;
	    	                com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivZookeeperNodes',divId,errorCount,errorMsg);
	    	            }	
	    	           
		    		 if (errorCount > 0) {
		    	            $("#errorDivZookeeperNodes").show();
		    	               if(errorCount > 1)
		    	            	   $('#validateErrorZookeeper').show().html('Errors '+"<span class='badge'>"+errorCount+"</span>");
		    	                else
		    	                	$('#validateErrorZookeeper').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
		    	        } else {
		    	            $("#errorDivZookeeper").hide();
		    	            $('#validateErrorZookeeper').hide();
		    	            com.impetus.ankush.hybrid_Zookeeper.zookeeperNodesPopulateApply();
		    	        }
		    	}
		    },
		zookeeperNodesPopulateApply:function(){
			var techName=$("#zookeeperNodeHeader").text().split('/')[0];
			zookeeperNodeObj=new Array();
			zookeeperNodes=new Array();
			zookeeperNodesMap ={};
		
			for ( var k = 0; k < nodeRoleArray.length; k++) {
				if(techName=="Zookeeper"){
					nodeRoleArray[k].roles["Zookeeper_default"]=new Array();
				}else{
					nodeRoleArray[k].roles[techName]=new Array();	
				}
				
				if(Object.keys(zookeeperObjMap)>0 && undefined != zookeeperObjMap[techName] &&  undefined != zookeeperObjMap[techName].zookeeperNodesMap.nodeRoleArray[k].ip){
					nodeRoleArray[k].role.Zookeeper=0;
				}
			}
			if(cluster_State!=com.impetus.ankush.constants.stateError){
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var zkNode={};
				if($("#zookeeperCheckBox"+i).is(':checked')) {
					zkNode.publicIp=nodeStatus.nodes[i][0];
					zkNode.privateIp=nodeStatus.nodes[i][0];
					zkNode.os=nodeStatus.nodes[i][4];
					zookeeperNodeObj.push(zkNode);
					zookeeperNodes.push(nodeStatus.nodes[i][0]);
				}
			}
			}else{
				  $(".zookeeperCheck").each(function(elem) {
					var  count=$(this).attr("id").split("zookeeperCheckBox")[1];
					  var rowIndex = zookeeperNodeTable.fnGetPosition($("#zookeeperCheckBox"+ count).closest('tr')[0]);
					  var aData= zookeeperNodeTable.fnGetData(rowIndex);
						var zkNode={};
						if($(this).is(':checked')) {
							zkNode.publicIp=aData[0];
							zkNode.privateIp=aData[0];
							zkNode.os=aData[aData.length-2];
							zookeeperNodeObj.push(zkNode);
							zookeeperNodes.push(aData[0]);
						}
				  });
			}
			
			 
			$.each(zookeeperNodeObj, function(i, znode) {
				zookeeperNodesMap[znode.publicIp] = {};;
			});
			if(undefined==zookeeperObjMap[techName]){
				zookeeperObjMap[techName]={};	
			}
			zookeeperObjMap[techName].zookeeperNodeObj=zookeeperNodeObj;
			zookeeperObjMap[techName].zookeeperNodesMap=zookeeperNodesMap;
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				 var temp=false;
				 var newArr=new Array();
				 for(var key in zookeeperObjMap){
					 if(undefined!= zookeeperObjMap[key].zookeeperNodesMap && undefined != zookeeperObjMap[key].zookeeperNodesMap[nodeStatus.nodes[i][0]]){
						 temp=true;
					 }
				 }
				 if(!temp){
					 nodeRoleArray[i].role.Zookeeper=0;	 
				 }
				 if(zookeeperNodes.indexOf(nodeStatus.nodes[i][0]) !=-1){
					 newArr.push("Zookeeper");
					 nodeRoleArray[i].role.Zookeeper=1;
					 if(techName=="Zookeeper"){
						 nodeRoleArray[i].roles["Zookeeper_default"]=newArr;
					 }else{
						 nodeRoleArray[i].roles[techName]=newArr;
					 }
				 }
			}
			$('#nodeMap'+techName).removeClass('btn-danger');
			$('#nodeCount'+techName).text(Object.keys(zookeeperNodesMap).length);
			com.impetus.ankush.hybridClusterCreation.nodeRoleMap('nodeRole');
			$('#rowZookeeper').remove();
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},	
};
