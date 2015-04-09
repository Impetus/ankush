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

var zookeeperObjReg=null;
com.impetus.ankush.register_Zookeeper = {
		//function for populating configuration page
	zookeeperConfigPopulate : function(key) {
		$("#zookeeperConfigHeader1").text(key.split('_')[0]+'/Configuration');
		$("#zookeeperConfigHeader").text(key+'/Configuration');
		if(key=="Zookeeper"){
			key="Zookeeper_default";
		}
		var techName="";
		if(undefined !=key){
			 techName=key;
		}
			$('#zookeeperVendorDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.vendor);
			$('#zookeeperVersionDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.version);
			$('#host').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.host);
			$('#clientPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.clientPort);
			$('#jmxPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.jmxPort);
			$('#registerLevel').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.zookeeperClusterCreation.registerLevel);
			if (setupDetailData == null || undefined == setupDetailData.components[key]) {
				if(undefined != zookeeperObjMap[techName]){
					zookeeperObjReg=zookeeperObjMap[techName].zookeeperObjReg;
				}else{
					zookeeperObjReg=null;
				}
				var zookeeperData = jsonDataHybrid.hybrid.Zookeeper_default;
				for ( var key in zookeeperData.Vendors) {
					$("#zookeeperVendorDropdown").append(
							"<option value=\"" + key + "\">" + key + "</option>");
				}
				if (zookeeperObjReg != null) {
					$("#zookeeperVendorDropdown").val(zookeeperObjReg.vendor);
				}
				for ( var key in zookeeperData.Vendors[$("#zookeeperVendorDropdown").val()]) {
					$("#zookeeperVersionDropdown").append(
							"<option value=\"" + key + "\">" + key + "</option>");
				}
				for ( var key in zookeeperData.Defaults.RegisterLevel){
					$("#registerLevel").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				if (zookeeperObjReg != null) {
					$("#zookeeperVersionDropdown").val(zookeeperObjReg.version);
				}
				if (zookeeperObjReg != null) {
					zookeeperData.Defaults.clientPort = zookeeperObjReg.Defaults.clientPort;
					zookeeperData.Defaults.jmxPort = zookeeperObjReg.Defaults.jmxPort;
					$('#host').val(zookeeperObjReg.Defaults.host);
					$("#registerLevel").val(zookeeperObjReg.Defaults.registerLevel);
				}
				
				$('#clientPort').val(zookeeperData.Defaults.clientPort);
				$('#jmxPort').val(zookeeperData.Defaults.jmxPort);
			}else{
				if (cluster_State != com.impetus.ankush.constants.stateError) {
					com.impetus.ankush.common.pageStyleChange();
					$('.btnGrp').attr('disabled', 'disabled');
					$('#revertZookeeper').attr('disabled', 'disabled');
					$('#applyZookeeper').attr('disabled', 'disabled');
				}
				if(undefined == zookeeperObjMap[techName] && undefined == zookeeperObjMap[techName].zookeeperObjReg){
					zookeeperObjMap[techName]=jsonDataHybrid.hybrid.Zookeeper_default;
					com.impetus.ankush.register_Zookeeper.redeployDataPrepare(techName);
				}
				zookeeperObjReg=zookeeperObjMap[techName].zookeeperObjReg;
				$("#zookeeperVendorDropdown").html('');
				$("#zookeeperVersionDropdown").html('');
				for ( var key in jsonDataHybrid.hybrid.Zookeeper_default.Vendors) {
					$("#zookeeperVendorDropdown").append(
							"<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#zookeeperVendorDropdown").val(zookeeperObjReg.vendor);
				for ( var key in jsonDataHybrid.hybrid.Zookeeper_default.Vendors[$(
						"#zookeeperVendorDropdown").val()]) {
					$("#zookeeperVersionDropdown").append(
							"<option value=\"" + key + "\">" + key + "</option>");
				}
				for ( var key in jsonDataHybrid.hybrid.Zookeeper_default.Defaults.RegisterLevel){
					$("#registerLevel").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#zookeeperVersionDropdown").val(zookeeperObjReg.version);
				$('#host').val(zookeeperObjReg.Defaults.host);
				$('#clientPort').val(zookeeperObjReg.Defaults.clientPort);
				$('#jmxPort').val(zookeeperObjReg.Defaults.jmxPort);
				$("#registerLevel").val(zookeeperObjReg.Defaults.registerLevel);
			}
	},
	redeployDataPrepare : function(key) {
		var zookeeperObjReg={
				Defaults:{}
		};
		if(undefined==zookeeperObjMap[key]){
			zookeeperObjMap[key]={
					zookeeperObjReg:{},	
			};
			zookeeperObjMap[key].zookeeperObjReg=jsonDataHybrid.hybrid.Zookeeper_default;
		}
		
		zookeeperObjReg.vendor = setupDetailData.components[key].vendor;
		zookeeperObjReg.version = setupDetailData.components[key].version;
		zookeeperObjReg.Defaults.host = setupDetailData.components[key].advanceConf.host;
		zookeeperObjReg.Defaults.clientPort = setupDetailData.components[key].advanceConf.clientPort;
		zookeeperObjReg.Defaults.jmxPort = setupDetailData.components[key].advanceConf.jmxPort;
		zookeeperObjReg.Defaults.registerLevel=setupDetailData.components[key].advanceConf.registerLevel;
		zookeeperObjMap[key].zookeeperObjReg=zookeeperObjReg;
	},
	
	/*
	 * Function for changing version and download path on change of zookeeper
	 * vendor dropdown
	 */
	vendorOnChangeZookeeper : function() {
		$("#zookeeperVersionDropdown").html('');
		for ( var key in jsonDataHybrid.hybrid.Zookeeper_default.Vendors[$("#zookeeperVendorDropdown").val()]) {
			$("#zookeeperVersionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
		}
	},
	//function for client validation of configuration page 
		zookeeperConfigValidate:function(){
			$('#validateErrorZookeeper').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMainZookeeper").html('').hide();
	        errorCount = 0;
	        if (!com.impetus.ankush.validation.empty($('#zookeeperVendorDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No zookeeper vendor selected';
  	            var divId='zookeeperVendorDropdown';
  	            $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	          com.impetus.ankush.common.tooltipOriginal('zookeeperVendorDropdown',com.impetus.ankush.tooltip.zookeeperClusterCreation.vendor);
	        }
	        if (!com.impetus.ankush.validation.empty($('#zookeeperVersionDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No zookeeper version selected';
  	            var divId='zookeeperVersionDropdown';
  	            $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
  	          com.impetus.ankush.common.tooltipOriginal('zookeeperVersionDropdown',com.impetus.ankush.tooltip.zookeeperClusterCreation.version);
	        }
	       
	        if(!com.impetus.ankush.validation.empty($('#host').val())){
                errorCount++;
                errorMsg = 'Zookeeper Host field empty.';
                com.impetus.ankush.common.tooltipMsgChange('host','Host cannot be empty');
                var divId='host';
                $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('host',com.impetus.ankush.tooltip.zookeeperClusterCreation.host);
            }
	       
	        if(!com.impetus.ankush.validation.empty($('#clientPort').val())){
                errorCount++;
                errorMsg = 'Client Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('clientPort','Client port cannot be empty');
                var divId='clientPort';
                $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#clientPort').val())) {
                errorCount++;
                errorMsg = 'Client Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('clientPort','Client Port must be numeric');
                var divId='clientPort';
                $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else if (!com.impetus.ankush.validation.oPort($('#clientPort').val())) {
                errorCount++;
                errorMsg = 'Client Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('clientPort','Client Port must be between 1024-65535');
                var divId='clientPort';
                $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else{
                com.impetus.ankush.common.tooltipOriginal('clientPort',com.impetus.ankush.tooltip.zookeeperClusterCreation.clientPort);
            }
	        
	        if(!com.impetus.ankush.validation.empty($('#jmxPort').val())){
                errorCount++;
                errorMsg = 'JMX Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port cannot be empty');
                var divId='jmxPort';
                $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be numeric');
                var divId='jmxPort';
                $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else if (!com.impetus.ankush.validation.oPort($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be between 1024-65535');
                var divId='jmxPort';
                $("#errorDivMainZookeeper").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else{
                com.impetus.ankush.common.tooltipOriginal('jmxPort',com.impetus.ankush.tooltip.zookeeperClusterCreation.jmxPort);
            }
	        if(errorCount>0 && errorMsg!=''){
	        	 $('#validateErrorZookeeper').show().html(errorCount + ' Error');
	        	  $("#errorDivMainZookeeper").show();
	        }else{
	        	 com.impetus.ankush.register_Zookeeper.zookeeperConfigApply();
	        }
	        
		},
		//function to be called onapply button click for data formation
		zookeeperConfigApply:function(){
			zookeeperObjReg1={
					Defaults:{},
			};
			var techName=$("#zookeeperConfigHeader").text().split('/')[0];
			if(undefined==zookeeperObjMap[techName]){
				zookeeperObjMap[techName]={};	
			}
			zookeeperObjReg1.vendor=$('#zookeeperVendorDropdown').val();
			zookeeperObjReg1.version=$('#zookeeperVersionDropdown').val();
			zookeeperObjReg1.Defaults.host=$('#host').val();
			zookeeperObjReg1.Defaults.clientPort=$('#clientPort').val();
			zookeeperObjReg1.Defaults.jmxPort=$('#jmxPort').val();
			zookeeperObjReg1.Defaults.registerLevel=$('#registerLevel').val();
			zookeeperObjReg1.Defaults.register=true;
			zookeeperObjMap[techName].zookeeperObjReg=zookeeperObjReg1;
			$('#confType'+techName).text('Custom');
			$("#confPage"+techName).removeClass("btn-primary btn-danger");
			$("#confPage"+techName).removeClass("open");
			$("#row"+techName).remove();
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
			
		},
};
