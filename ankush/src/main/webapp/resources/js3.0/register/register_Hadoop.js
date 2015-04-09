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
com.impetus.ankush.register_Hadoop={
		tooltipInitialize:function(){
			$('#vendorDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.vendor);
			$('#versionDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.version);
			$('#namenode').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.namenode);
			$('#resourceManager').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.resourceManager);
			$('#jobTracker').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.jobTracker);
			$('#httpPortNameNode').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.httpPortNameNode);
			$('#registerLevel').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.registerLevel);
			$('#installationType').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.hadoopClusterCreation.installationType);
			},	
		divToggle:function(divId,type){
			if(type=='Enable'){
				$('#'+divId).show();
			}else{
				$('#'+divId).hide();
			}
		},
		/*Function for configuration page value population*/
		hadoopConfigPopulate:function(){
			com.impetus.ankush.register_Hadoop.tooltipInitialize();
			$('#vendorDropdown').html('');
			$('#versionDropdown').html('');
			$('#installationType').html('');
			$('#registerLevel').html('');
			if (setupDetailData == null || undefined == setupDetailData.components.Hadoop) {
				var hadoopData=jsonDataHybrid.hybrid.Hadoop;
				for ( var key in hadoopData.Registration.Vendors){
					$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				for ( var key in hadoopData.Registration.Vendors[$("#vendorDropdown").val()]){
					$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				for ( var key in hadoopData.Defaults.DeploymentType){
					$("#installationType").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				for ( var key in hadoopData.Defaults.RegisterLevel){
					$("#registerLevel").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				
				if(undefined != hadoopObjReg.vendor){
					hadoopData.Defaults.httpPortNameNode=hadoopObjReg.Defaults.httpPortNameNode;
					$("#vendorDropdown").val(hadoopObjReg.vendor);
					$('#versionDropdown').html('');
					for ( var key in hadoopData.Registration.Vendors[$("#vendorDropdown").val()]){
						$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
					}
					$("#versionDropdown").val(hadoopObjReg.version);
					$("#namenode").val(hadoopObjReg.Defaults.namenode);
					if(hadoopObjReg.vendor=="Apache" && hadoopObjReg.version.split(".")[0]=='1'){
						$("#jobTracker").val(hadoopObjReg.Defaults.jobTracker);
						$("#resourceManagerDiv").hide();
						$("#jobTrackerDiv").show();
					}else{
						$("#resourceManager").val(hadoopObjReg.Defaults.resourceManager);
						$("#resourceManagerDiv").show();
						$("#jobTrackerDiv").hide();
					}
					$("#installationType").val(hadoopObjReg.Defaults.installationType);
					$("#registerLevel").val(hadoopObjReg.Defaults.registerLevel);
				}
					$('#httpPortNameNode').val(hadoopData.Defaults.httpPortNameNode);
					
			}else{
				if(cluster_State!=com.impetus.ankush.constants.stateError){
					com.impetus.ankush.common.pageStyleChange();
					$('.btnGrp').attr('disabled', 'disabled');
					$('#reverthadoop').attr('disabled', 'disabled');
					$('#applyHadoop').attr('disabled', 'disabled');
				}
				//if (Object.keys(hadoopObjReg.Defaults).length<1 ){
				if(undefined == hadoopObjReg.vendor){
					hadoopObjReg=jsonDataHybrid.hybrid.Hadoop;
					com.impetus.ankush.register_Hadoop.redeployDataPrepare();
				}
				for ( var key in jsonDataHybrid.hybrid.Hadoop.Registration.Vendors){
					$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#vendorDropdown").val(hadoopObjReg.vendor);
				for ( var key in jsonDataHybrid.hybrid.Hadoop.Registration.Vendors[$("#vendorDropdown").val()]){
					$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#versionDropdown").val(hadoopObjReg.version);
				for ( var key in jsonDataHybrid.hybrid.Hadoop.Defaults.DeploymentType){
					$("#installationType").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				for ( var key in jsonDataHybrid.hybrid.Hadoop.Defaults.RegisterLevel){
					$("#registerLevel").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#namenode").val(hadoopObjReg.Defaults.namenode);
				if(hadoopObjReg.vendor=="Apache" && hadoopObjReg.version.split(".")[0]=='1'){
					$("#jobTracker").val(hadoopObjReg.Defaults.jobTracker);		
				}else{
					$("#resourceManager").val(hadoopObjReg.Defaults.resourceManager);
				}
				$("#installationType").val(hadoopObjReg.Defaults.installationType);
				$("#registerLevel").val(hadoopObjReg.Defaults.registerLevel);
				$("#httpPortNameNode").val(hadoopObjReg.Defaults.httpPortNameNode);
				
			}
		},
		/*Function for object value population during redeployment*/
		redeployDataPrepare: function(){
			if(undefined == hadoopObjReg.vendor){
				hadoopObjReg=jsonDataHybrid.hybrid.Hadoop;
			}       
			hadoopObjReg.vendor=setupDetailData.components.Hadoop.vendor;
			hadoopObjReg.version=setupDetailData.components.Hadoop.version;
			if(undefined !=setupDetailData.components.Hadoop.advanceConf && Object.keys(setupDetailData.components.Hadoop.advanceConf).length>0){
				hadoopObjReg.Defaults.namenode=setupDetailData.components.Hadoop.advanceConf.namenode;
				if(hadoopObjReg.vendor=="Apache" && hadoopObjReg.version.split(".")[0]=='1'){
					hadoopObjReg.Defaults.jobTracker=setupDetailData.components.Hadoop.advanceConf.jobTracker;	
				}else{
					hadoopObjReg.Defaults.resourceManager=setupDetailData.components.Hadoop.advanceConf.resourceManager;
				}
				hadoopObjReg.Defaults.httpPortNameNode=setupDetailData.components.Hadoop.advanceConf.httpPortNameNode;
				hadoopObjReg.Defaults.installationType=setupDetailData.components.Hadoop.advanceConf.installationType;
				hadoopObjReg.Defaults.registerLevel=setupDetailData.components.Hadoop.advanceConf.registerLevel;
			}
		},
/*Function for changing version and download path on change of Hadoop vendor dropdown*/
		vendorOnChangeHadoop:function(){
			$("#versionDropdown").html('');
			for ( var key in jsonDataHybrid.hybrid.Hadoop.Registration.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			if($("#versionDropdown").val().split(".")[0]==2){
				$("#resourceManagerDiv").show();
				$("#jobTrackerDiv").hide();
			}else{
				$("#resourceManagerDiv").hide();
				$("#jobTrackerDiv").show();
			}
		},
		/*Function for changing  download path on change of version Hadoop dropdown*/
		versionOnChangeHadoop:function(){
			if($("#versionDropdown").val().split(".")[0]==2){
				$("#resourceManagerDiv").show();
				$("#jobTrackerDiv").hide();
			}else{
				$("#resourceManagerDiv").hide();
				$("#jobTrackerDiv").show();
			}
		},
		mrFrameworkOnChange:function(){
			
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
	        if (!com.impetus.ankush.validation.empty($('#namenode').val())) {
  	            errorCount++;
  	            errorMsg = 'Namenode field Empty';
  	            com.impetus.ankush.common.tooltipMsgChange('namenode','Namenode  cannot be empty');
  	            var divId='namenode';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
  	        }else {
	            com.impetus.ankush.common.tooltipOriginal('namenode',com.impetus.ankush.tooltip.hadoopClusterCreation.namenode);
	        }
	        if($('#vendorDropdown').val()=='Apache' && $('#versionDropdown').val().split(".")[0]=='1'){
	        	if (!com.impetus.ankush.validation.empty($('#jobTracker').val())) {
	  	            errorCount++;
	  	            errorMsg = 'JobTracker field Empty';
	  	            com.impetus.ankush.common.tooltipMsgChange('jobTracker','JobTracker cannot be empty');
	  	            var divId='jobTracker';
	  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('jobTracker',com.impetus.ankush.tooltip.hadoopClusterCreation.jobTracker);
		        }
	        }else{
	        	if (!com.impetus.ankush.validation.empty($('#resourceManager').val())) {
	  	            errorCount++;
	  	            errorMsg = 'Resource Manager field Empty';
	  	            com.impetus.ankush.common.tooltipMsgChange('resourceManager','Resource Manager cannot be empty');
	  	            var divId='resourceManager';
	  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
	  	        }else {
		            com.impetus.ankush.common.tooltipOriginal('resourceManager',com.impetus.ankush.tooltip.hadoopClusterCreation.resourceManager);
		        } 
	        }
	        if (!com.impetus.ankush.validation.empty($('#httpPortNameNode').val())) {
  	            errorCount++;
  	            errorMsg = 'HTTP Port NameNode field Empty';
  	            com.impetus.ankush.common.tooltipMsgChange('httpPortNameNode','HTTP Port NameNode cannot be empty');
  	            var divId='httpPortNameNode';
  	          com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivMainHadoop',divId,errorCount,errorMsg);
  	        }else {
	            com.impetus.ankush.common.tooltipOriginal('httpPortNameNode',com.impetus.ankush.tooltip.hadoopClusterCreation.httpPortNameNode);
	        }
	        if(errorCount>0 && errorMsg!=''){
	        	$('#validateErrorHadoop').show().html('Error '+"<span class='badge'>"+errorCount+"</span>");
	        	  $("#errorDivMainHadoop").show();
	        }else{
	        	 com.impetus.ankush.register_Hadoop.hadoopConfigApply();
	        } 
		},
		hadoopConfigApply:function(){
			hadoopObjReg.vendor=$('#vendorDropdown').val();
			hadoopObjReg.version=$('#versionDropdown').val();
			hadoopObjReg.Defaults.registerLevel=$('#registerLevel').val();
			hadoopObjReg.Defaults.installationType=$('#installationType').val();
			hadoopObjReg.Defaults.namenode=$('#namenode').val();
			hadoopObjReg.Defaults.resourceManager=$('#resourceManager').val();
			hadoopObjReg.Defaults.jobTracker=$('#jobTracker').val();
			hadoopObjReg.Defaults.httpPortNameNode=$('#httpPortNameNode').val();
			$('#confTypeHadoop').text('Custom');
			hadoopObjReg.Defaults.register=true;
			$("#confPageHadoop").removeClass("btn-danger btn-primary");
			com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();
		},
		
		hadoopNodesPopulate:function(){
			
			if(hadoopNodeTable!=null){
				hadoopNodeTable.fnClearTable();
			}
			
			
			if(undefined == hadoopObjReg.vendor || hadoopObjReg.version.split(".")[0]=='2'){
				hadoopNodeTable.fnSetColumnVis(6, false);
			}
			if((undefined != hadoopObjReg.vendor && hadoopObjReg.version.split(".")[0]!='2')){
				hadoopNodeTable.fnSetColumnVis(7, false);
				hadoopNodeTable.fnSetColumnVis(8, false);
				hadoopNodeTable.fnSetColumnVis(9, false);
				hadoopNodeTable.fnSetColumnVis(10, false);
				hadoopNodeTable.fnSetColumnVis(11, false);
			}
			if(undefined != hadoopObjReg.vendor){
				if(!hadoopObjReg.Defaults.haEnabled){
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
													'<div><a href="##" onclick="com.impetus.ankush.hybrid_Hadoop.loadHadoopNodeDetail(this);"><img id="navigationImg-'
														+ i
														+ '" src="'
														+ baseUrl
														+ '/public/images/icon-chevron-right.png" /></a></div>' ]);
				var theNode = hadoopNodeTable.fnSettings().aoData[addId[0]].nTr;
				theNode.setAttribute('id', 'hadoop'+ nodeStatus.nodes[i][0].split('.').join('_'));
				if (nodeStatus.nodes[i][1] == false	|| nodeStatus.nodes[i][2] == false|| nodeStatus.nodes[i][3] == false) {
					rowId = nodeStatus.nodes[i][0].split('.').join('_');
					$('td', $('#hadoop'+rowId)).addClass('error-row');
					$('#hadoop' + rowId).addClass('error-row');
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
			if(undefined==hadoopObjReg.version || (hadoopObjReg.version.split(".")[0]=='2' && hadoopObjReg.Defaults.haEnabled)){
				$('.secNameNodeCheck').removeAttr("checked").attr('disabled', true);	
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
					if(undefined!=hadoopObjReg.version && hadoopObjReg.version.split(".")[0]!='2'){
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
					if(undefined!=hadoopObjReg.version && hadoopObjReg.version.split(".")[0]=='2' && hadoopObjReg.Defaults.haEnabled && $("#toggleRegisterBtnHadoop .active").data("value")==0){
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
					 if(undefined!=hadoopObjReg.version && hadoopObjReg.version.split(".")[0]=='2' && hadoopObjReg.Defaults.haEnabled && $("#toggleRegisterBtnHadoop .active").data("value")==0){
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
		hadoopNodesPopulateApplyValidate:function(){
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
		            	 if(undefined!=hadoopObjReg.version && hadoopObjReg.version.split('.')[0]==2){
		            		 if(isSameNode && hadoopObjReg.Defaults.haEnabled){
		            			 errorMsg = "NameNode and StandBy NameNode can't be same.";
			     	                errorCount++;
			     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
			            	 
		            		 }
	            			 if(hadoopObjReg.Defaults.webProxyEnabled && hadoopObjReg.Defaults.haEnabled){
	            				 if(!nn && !dn && !rmn && !jhn && !wpn && !sbn && !jn){
	 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
	 		     	                errorCount++;
	 		     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
	 		            		 }
	            			 }else if(hadoopObjReg.Defaults.webProxy){
	            				 if(!nn && !dn && !rmn && !jhn && !wpn){
		 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
		 		     	                errorCount++;
		 		     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
		 		            		 } 
	            			 }else if(hadoopObjReg.Defaults.haEnabled){
	            				 if(!nn && !dn && !rmn && !jhn && !sbn && !jn){
		 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
		 		     	                errorCount++;
		 		     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
		 		            		 }
	            			 }else if(hadoopObjReg.vendor=="Cloudera"){
	            				 if(hadoopObjReg.Defaults.mapreduceFramework == "classic") {
	            					 if(!jtn){
			 		            		errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
			 		     	            errorCount++;
			 		     	            $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
			 		            	}	 
	            				 }
	            			 }else{
	            				 if(!nn && !dn && !rmn && !jhn){
		 		            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ ' must be mapped to any one role.';
		 		     	                errorCount++;
		 		     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
		 		            		 }
	            			 }
	            		 }else{
	            			 if(!nn && !snn && !dn){
			            			errorMsg = 'Node '+$("td:nth-child(2)", $(this).parents('tr')).text()+ '  must be Namenode or Secondarynamenode or Datanode.';
			     	                errorCount++;
			     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
			            		 }	 
	            		 }
		            	 if(nameNode==0){
		            		 errorMsg = 'At least one node must be selected as Namenode.';
		     	                errorCount++;
		     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
		     	           
		            	 }
		            	 if(undefined != hadoopObjReg.version && hadoopObjReg.version.split('.')[0] == 1 && jtNode==0){
		            		 errorMsg = 'At least one node must be selected as JobTracker.';
		     	                errorCount++;
		     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
		     	           
		            	 }
		            	 if(dataNode==0){
		            		 errorMsg = 'At least one node must be selected as Datanode.';
		     	                errorCount++;
		     	                $("#errorDivHadoopNodes").append("<div class='errorLineDiv'><a href='#hadoopNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
		     	           
		            	 }
		            	 if((undefined==hadoopObjReg.version || hadoopObjReg.version.split('.')[0]==2) && $("#toggleRegisterBtnHadoop .active").data("value")==0){
		            		 if(rmNode==0){
			            		 errorMsg = 'At least one node must be selected as ResourceManger Node.';
			     	                errorCount++;
			     	               com.impetus.ankush.hybridClusterCreation.errorMessageShow('errorDivHadoopNodes','',errorCount,errorMsg,'hadoopNodeTable');
			     	           
			            	 }
		            	
	            			 if(undefined==hadoopObjReg.Defaults.haEnabled  || (undefined!=hadoopObjReg.Defaults.haEnabled && hadoopObjReg.Defaults.haEnabled )){
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
	    	                    $('#validateErrorHadoop').text(errorCount + " Errors");
	    	                else
	    	                    $('#validateErrorHadoop').text(errorCount + " Error");
	    	            $('#validateErrorHadoop').show().html(errorCount + ' Error');
	    	        } else {
	    	            $("#errorDivHadoopNodes").hide();
	    	            $('#validateErrorhadoop').hide();
	    	            com.impetus.ankush.register_Hadoop.hadoopNodesPopulateApply();
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
									if(undefined==hadoopObjReg.version || hadoopObjReg.version.split(".")[0]==2){
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
};
