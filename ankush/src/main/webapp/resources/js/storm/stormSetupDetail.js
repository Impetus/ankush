/*******************************************************************************
*===========================================================
*Ankush : Big Data Cluster Management Solution
*===========================================================
*
*(C) Copyright 2014, by Impetus Technologies
*
*This is free software; you can redistribute it and/or modify it under
*the terms of the GNU Lesser General Public License (LGPL v3) as
*published by the Free Software Foundation;
*
*This software is distributed in the hope that it will be useful, but
*WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*See the GNU Lesser General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License 
*along with this software; if not, write to the Free Software Foundation, 
*Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/

com.impetus.ankush.stormSetupDetail={
		/*Function for populating default values of storm cluster*/ 
		populateDefaultValues:function(result){
			$('.section-body input:eq(0)').focus();
			 $('#clusterDeploy').removeAttr('disabled');
			defaultVlaue=result;
	//		$('#javaLibPath').val(result.output.storm.Storm.Defaults.javaLibraryPath);
			var slotPorts='';
			for(var i=0;i<result.output.storm.Storm.Defaults.slotsPorts.length ;i++){
				if(i!=result.output.storm.Storm.Defaults.slotsPorts.length-1){
					slotPorts=slotPorts+result.output.storm.Storm.Defaults.slotsPorts[i]+',';	
				}else{
					slotPorts=slotPorts+result.output.storm.Storm.Defaults.slotsPorts[i];
				}
			}
			$('#supervisorPorts').val(slotPorts);
			$('#uiPorts').val(result.output.storm.Storm.Defaults.uiPort);
			$('#jmxPort-Nimbus').val(result.output.storm.Storm.Defaults.jmxPort_Nimbus);
			$('#jmxPort-Supervisor').val(result.output.storm.Storm.Defaults.jmxPort_Supervisor);			
			$('#localDirStorm').val(result.output.storm.Storm.Defaults.localDir);
			$('#installationPath-Storm').val(result.output.storm.Storm.Defaults.installationPath);
			for ( var key in result.output.storm.Storm.Vendors){
				$("#vendor-Storm").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in result.output.storm.Storm.Vendors[$("#vendor-Storm").val()]){
				$("#version-Storm").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath-Storm').val(result.output.storm.Storm.Vendors[$("#vendor-Storm").val()][$("#version-Storm").val()].url);
			$('#installationPath-Zookeeper').val(result.output.storm.Zookeeper.Defaults.installationHomePath);
			$('#dataPath-Zookeeper').val(result.output.storm.Zookeeper.Defaults.dataDir);
			for ( var key in result.output.storm.Zookeeper.Vendors){
				$("#vendor-Zookeeper").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in result.output.storm.Zookeeper.Vendors[$("#vendor-Zookeeper").val()]){
				$("#version-Zookeeper").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath-Zookeeper').val(result.output.storm.Zookeeper.Vendors[$("#vendor-Zookeeper").val()][$("#version-Zookeeper").val()].url);
			$('#jmxPort-Zookeeper').val(result.output.storm.Zookeeper.Defaults.jmxPort);
			$('#clientPort-Zookeeper').val(result.output.storm.Zookeeper.Defaults.clientPort);
			$('#syncLimit').val(result.output.storm.Zookeeper.Defaults.syncLimit);
			$('#initLimit').val(result.output.storm.Zookeeper.Defaults.initLimit);
			$('#tickTime').val(result.output.storm.Zookeeper.Defaults.tickTime);
		},	
		redeployPagePopulate:function(currentClusterId,clusterState){
			var url = baseUrl + '/monitor/' + currentClusterId + '/details';
			$.ajax({
	                   'type' : 'GET',
	                   'url' : url,
	                   'contentType' : 'application/json',
	                   'dataType' : 'json',
	                   'async' : true,
	                   'success' : function(result) {
	                	   if(result.output.status){
	                	   setupDetailData=result.output;
	                	   if(setupDetailData.state=='error'){
	                		   $('#clusterDelete').removeAttr('disabled');
	                		   $('#errorDivMain').html('');
	                		   $('#validateError').html('');
	                		   var count=0;
	                		   for(var key in setupDetailData.errors){
	                			   count++;
	                			   $("#errorDivMain").append("<div class='errorLineDiv'><a href='#'  >"+count+". "+setupDetailData.errors[key]+"</a></div>");   
	                		   }
	                		   $('#errorDivMain').show();
	                		   if(Object.keys(setupDetailData.errors).length > 1){
	                               $('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Errors');
	                          }
	                          else{
	                              $('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Error');
	                          }
	                	   }
	                	   if(!setupDetailData.status){
	                		  $("#errorDivMain").html('').append("<div class='errorLineDiv'><a href='#'  >1. "+setupDetailData.error[0]+"</a></div>");
	                 		  $('#errorDivMain').show();
	                 		  $('#validateError').html('').text('1 Error').show();
	                		  $('#clusterDelete').removeAttr('disabled');
	                		   return;
	                	   }
	                	   $('#clusterName').val(setupDetailData.clusterName).removeAttr('title');
	                	   if(setupDetailData.javaConf.install){
	                		   $('#chkInstallJava').attr('checked',true);   
	                		   $('#bundlePathJava').val(setupDetailData.javaConf.javaBundle).removeAttr('title');   
	                		   $('#bundlePathJava').removeAttr("disabled");
	       						$('#homePathJava').attr("disabled","disabled");
	                	   }else{
	                		   $('#homePathJava').val(setupDetailData.javaConf.javaHomePath).removeAttr('title');
	                		   $('#chkInstallJava').removeAttr('checked'); 
	                		   $('#homePathJava').removeAttr("disabled");
	       						$('#bundlePathJava').attr("disabled","disabled"); 
	                	   }
	                	
	                //	   $('#javaLibPath').val(setupDetailData.storm.javaLibraryPath).removeAttr('title');
	                	   $('#commonUserName').val(setupDetailData.username).removeAttr('title');
	                	   $('#commonUserPassword').val(setupDetailData.password).removeAttr('title');
	                	   if(setupDetailData.ipPattern=='' ||setupDetailData.ipPattern==null){
	                		   $('#modeIPRange').removeClass('active');
	                		   $('#modeIPFile').addClass('active');
	                		   $('#fileUploadDiv').show();
	                		   $('#ipRangeDiv').hide();
	                	   }else{
	                		   $('#ipRangeDiv').show();
	                		   $('#fileUploadDiv').hide();
	                		   $('#modeIPFile').removeClass('active');
	                		   $('#modeIPRange').addClass('active');
	                	   }
	                	   $('#ipRange').val(setupDetailData.ipPattern).removeAttr('title');
	                	   $('#pathIPFile').val(setupDetailData.patternFile).removeAttr('title');
	                	   $("#vendor-Storm").val(setupDetailData.storm.componentVendor);
	                	   $("#version-Storm").val(setupDetailData.storm.componentVersion);
	                	   if(setupDetailData.storm.localBinaryFile=='' ||setupDetailData.storm.localBinaryFile==null){
	                		 //  $('#bundleSourceDownload-Storm').attr('checked','checked');
	                		   $('#bundleSourceLocalPath-Storm').removeClass('active');
	       					   $('#bundleSourceDownload-Storm').addClass('active');
	                		   $('#stormDownloadPathDiv').show();
	                		   $('#stormLocalPathDiv').hide();
	                	   }else{
	                		   $('#stormLocalPathDiv').show();
	                		   $('#stormDownloadPathDiv').hide();
	                		   $('#bundleSourceDownload-Storm').removeClass('active');
	                		   $('#bundleSourceLocalPath-Storm').addClass('active');
	                		  // $('#bundleSourceLocalPath-Storm').attr('checked','checked');
	                	   }
	                	   $('#downloadPath-Storm').val(setupDetailData.storm.tarballUrl).removeAttr('title');
	                	   $('#localPath-Storm').val(setupDetailData.storm.localBinaryFile).removeAttr('title');
	                	   var slotPort='';
	                	   for(var i=0;i<setupDetailData.storm.slotsPorts.length;i++){
	                		   if(slotPort=='')
	                			   slotPort=slotPort+setupDetailData.storm.slotsPorts[i];
	                		   else
	                		   slotPort=slotPort+','+setupDetailData.storm.slotsPorts[i];
	                	   }
	                	   $('#supervisorPorts').val(slotPort).removeAttr('title');
	                	   $('#uiPorts').val(setupDetailData.storm.uiPort).removeAttr('title');
	                	   $('#jmxPort-Nimbus').val(setupDetailData.storm.jmxPort_Nimbus).removeAttr('title');
	                	   $('#jmxPort-Supervisor').val(setupDetailData.storm.jmxPort_Supervisor).removeAttr('title');
	                	   $('#localDirStorm').val(setupDetailData.storm.localDir).removeAttr('title');
	                	   $('#installationPath-Storm').val(setupDetailData.storm.installationPath).removeAttr('title');
	                	   $("#vendor-Zookeeper").val(setupDetailData.zookeeper.componentVendor);
	                	   $("#version-Zookeeper").html('');
	                       	for ( var key in defaultVlaue.output.storm.Zookeeper.Vendors[$("#vendor-Zookeeper").val()]){
	            				$("#version-Zookeeper").append("<option value=\"" + key + "\">" + key + "</option>");
	            			}
	                	   $("#version-Zookeeper").val(setupDetailData.zookeeper.componentVersion);
	                	   if(setupDetailData.zookeeper.localBinaryFile=='' || setupDetailData.zookeeper.localBinaryFile==null){
	                		//   $('#bundleSourceDownload-Zookeeper').attr('checked','checked');
	                			$('#bundleSourceLocalPath-Zookeeper').removeClass('active');
	        					$('#bundleSourceDownload-Zookeeper').addClass('active');
	                		   $('#zookeeperDownloadPathDiv').show();
	                		   $('#zookeeperLocalPathDiv').hide();
	                	   }else{
	                		   $('#zookeeperLocalPathDiv').show();
	                		   $('#zookeeperDownloadPathDiv').hide();
	                		 //  $('#bundleSourceLocalPath-Zookeeper').attr('checked','checked');
	                			$('#bundleSourceLocalPath-Zookeeper').addClass('active');
	        					$('#bundleSourceDownload-Zookeeper').removeClass('active');
	                	   }$('#downloadPath-Zookeeper').val(setupDetailData.zookeeper.tarballUrl).removeAttr('title');
	                	   $('#localPath-Zookeeper').val(setupDetailData.zookeeper.localBinaryFile).removeAttr('title');
	                	   $('#installationPath-Zookeeper').val(setupDetailData.zookeeper.installationPath).removeAttr('title');
	                	   $('#dataPath-Zookeeper').val(setupDetailData.zookeeper.dataDirectory).removeAttr('title');
	                	   $('#clientPort-Zookeeper').val(setupDetailData.zookeeper.clientPort).removeAttr('title');
	                	   $('#jmxPort-Zookeeper').val(setupDetailData.zookeeper.jmxPort).removeAttr('title');
	                	   if(clusterState=='error'){
	                		   $('#syncLimit').val(setupDetailData.zookeeper.syncLimit);
	                       	   $('#initLimit').val(setupDetailData.zookeeper.initLimit);
	                       	   $('#tickTime').val(setupDetailData.zookeeper.tickTime);
	                     	 }else{
	                     		  $('#syncLimit').val(setupDetailData.zookeeper.syncLimit + " milliseconds").removeAttr('title');
	   	                	   $('#initLimit').val(setupDetailData.zookeeper.initLimit + " milliseconds").removeAttr('title');
	   	                	   $('#tickTime').val(setupDetailData.zookeeper.tickTime + " milliseconds").removeAttr('title');
	   	                	   $('.tooltiptext').css('display', 'none');
	                     	   }
	                	   nodeStatus={
	               	    		nodes:new Array(),
	               	    };
	                	   for ( var i = 0; i < setupDetailData.nodes.length; i++) {
                		   var nodeObj = new Array();
	           				var addId = null;
	           					addId = oNodeTable.fnAddData([
	           									'<input type="checkbox" name="" value=""  id="nodeCheck'
	           											+ i
	           											+ '" class="nodeCheckBox"  onclick="com.impetus.ankush.stormClusterCreation.nodeCheckBox('
	           											+ i + ',\'nodeList\',\'nodeCheckBox\',\'nodeCheckHead\');"/>',
	           											setupDetailData.nodes[i].publicIp,
	           									'<input id="nimbusCheckBox'
	           											+ i
	           											+ '" class="nimbusCheck"  name="nimbusRadio" style="margin-right:10px;" type="radio" onclick="com.impetus.ankush.stormClusterCreation.nimbusCheck()">',
	           											'<input id="zookeeperCheckBox'
	           											+ i
	           											+ '" class="zookeeperCheck" style="margin-right:10px;" type="checkbox" >',
	           											'<input id="supervisorCheckBox'
	           											+ i
	           											+ '" class="supervisorCheck" style="margin-right:10px;" type="checkbox" onclick="com.impetus.ankush.stormClusterCreation.supervisorCheckBox('
	           											+ i + ',\'nodeList\',\'supervisorCheck\',\'headCheckSup\');" >',
	           									'<a class="" id="osName'
	           											+ i + '">'+setupDetailData.nodes[i].os+'</a>',
	           											
	           											
	           											'<a class="" id="cpuCount'
	        											+ i + '">-</a>',
	        											'<a class="" id="diskCount'
	        											+ i + '">-</a>',
	        											'<a class="" id="diskSize'
	        											+ i + '">-</a>',
	        											'<a class="" id="ramSize'
	        											+ i + '">-</a>',
	           									'<div><a href="##" onclick="com.impetus.ankush.stormSetupDetail.loadNodeDetailStorm('
	           											+ i
	           											+ ');"><img id="navigationImg-'
	           											+ i
	           											+ '" src="'
	           											+ baseUrl
	           											+ '/public/images/icon-chevron-right.png" /></a></div>' ]);
	           					nodeObj.push(setupDetailData.nodes[i].publicIp);
	           					nodeObj.push('true');
	           					nodeObj.push('true');
	           					nodeObj.push('true');
	           					nodeObj.push(setupDetailData.nodes[i].os);
	           					nodeObj.push('');
	           					nodeObj.push('');
	           					nodeObj.push('');
	           					nodeObj.push('');
	           					nodeObj.push('');
	           					nodeStatus.nodes.push(nodeObj);
	           				$('#nodeCheck'+i).attr('checked',true);	
	           				var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
	           				theNode.setAttribute('id', 'node'+ setupDetailData.nodes[i].publicIp.split('.').join('_'));
	           				if (Object.keys(setupDetailData.nodes[i].errors).length>0 ){
	           					rowId = setupDetailData.nodes[i].publicIp.split('.').join('_');
	           					$('td', $('#node'+rowId)).addClass('error-row');
	           					$('#node' + rowId).addClass('error-row');
	           				}
	           				if(setupDetailData.nodes[i].publicIp==setupDetailData.storm.nimbus.publicIp){
	           					$('#nimbusCheckBox'+i).attr('checked',true);
	           				}
	           			}
	                	   if(clusterState!='error'){
	                 		  $('.nimbusCheck').attr('disabled','disabled');
	                      	  $('.nodeCheckBox').attr('disabled','disabled');
	                      	 $('.zookeeperCheck').attr('disabled','disabled');
	                      	 $('.supervisorCheck').attr('disabled','disabled');
	                 	  }
	                	   $('#nodeCheckHead').attr('checked',true);
	                	   for(var j=0;j<setupDetailData.nodes.length;j++){
	                	   for(var i=0;i<setupDetailData.storm.zkNodes.length;i++){
	                			   if(setupDetailData.storm.zkNodes[i]==setupDetailData.nodes[j].publicIp){
	                				   $('#zookeeperCheckBox'+j).attr('checked',true); 
	                			   }
	                		   }
	                	   }
	                	   for(var j=0;j<setupDetailData.nodes.length;j++){
	                		   for(var i=0;i<setupDetailData.storm.supervisors.length;i++){
	                			   if(setupDetailData.storm.supervisors[i].publicIp==setupDetailData.nodes[j].publicIp)
	                				   $('#supervisorCheckBox'+j).attr('checked',true);
	                		   }
	                	   }
	                   }
	                   },
	                   error : function() {
	                   }
			});
		},
		/*Function for populating setupdetail page*/
		setupDetailValuePopulate:function(currentClusterId,clusterState){
			com.impetus.ankush.common.getDefaultValue('storm','stormSetupDetail.populateDefaultValues');
			$('#nodeTableDiv').show();
			$('#shardNodeTableHeaderDiv').show();
			$('#nodeList_filter').hide();
			if(clusterState=='error'){
				$('#clusterDeploy').text('Redeploy');
				$('#clusterDeploy').val(currentClusterId);
				$('#nodeTableDiv').show();
				com.impetus.ankush.stormSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
				 $('#filePath').val('');
				com.impetus.ankush.stormClusterCreation.tooltipInitialize();
				return;
			}
			com.impetus.ankush.common.pageStyleChange();
			//$('#nodeTableDiv').show();
			//$('#javaCheckLabel').css('margin-top','4px');
			$('.add-on').css({
				'background-color':'white',
				'border':'none',
			});
			$('#bundlePathJava').css({
				'width':'80%',
			});
			$('#nodeSearch').hide();
      	   	$('#toggleSelectButton').hide();
			$('.btnGrp').attr('disabled','disabled');
			$('#clusterDeploy').hide();
			$('#clusterDelete').hide();
	  	   	$('#retrieveNodes').attr('disabled','disabled');
	  	   	$('#inspectNodeBtn').attr('disabled','disabled');
			if (oNodeTable != null) {
				oNodeTable.fnClearTable();
			}
			com.impetus.ankush.stormSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
		},
		
		/*Function for loading retrieved node detail page*/
		loadNodeDetailStorm: function(i) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/storm-cluster/retrievedNodeDetail',
				method : 'get',
				params : {},
				title : 'Node Detail',
				callback : function(data) {
					com.impetus.ankush.stormSetupDetail.nodeDetail(data.i);
				},
				callbackData : {
					i : i
				}
			});
		},
/*Function for populating values on node detail page*/
		nodeDetail:function(node){
			var nodeType='';
			if(setupDetailData==null ){
				$('#stormNodeHead').empty().html(nodeStatus.nodes[node][0]);
				if(nodeStatus.nodes[node][1]==false){
					$('#isAuthenticated').html('').text('No');
					$('#stormNodeStatus').html('').text('Unavailable');
				}else if(nodeStatus.nodes[node][2]==false){
					$('#isAuthenticated').html('').text('No');
					$('#stormNodeStatus').html('').text('Unreachable');
				}else if(nodeStatus.nodes[node][3]==false){
					$('#isAuthenticated').html('').text('No');
					$('#stormNodeStatus').html('').text('Unauthenticated');
				}else{
					$('#isAuthenticated').html('').text('Yes');
					$('#stormNodeStatus').html('').text('Available');
				}
				$('#stormOs').html('').text(nodeStatus.nodes[node][4]);
				$('#stormCpu').html('').text(nodeStatus.nodes[node][7]);
				$('#stormDiskCount').html('').text(nodeStatus.nodes[node][8]);
				if(nodeStatus.nodes[node][9]==''){
					$('#stormDiskSize').html('');
				}else{
					$('#stormDiskSize').html('').text(nodeStatus.nodes[node][9]+' GB');
				}
				if(nodeStatus.nodes[node][10]==''){
					$('#stormRam').html('');
				}else{
					$('#stormRam').html('').text(nodeStatus.nodes[node][10]+' GB');
				}
				
				if($('#nimbusCheckBox'+node).is(':checked')){
					nodeType='Nimbus';
				}
				if($('#zookeeperCheckBox'+node).is(':checked')){
					if(nodeType=='')
						nodeType='Zookeeper';
					else
					nodeType=nodeType+'/Zookeeper';
				}
				if($('#supervisorCheckBox'+node).is(':checked')){
					if(nodeType=='')
						nodeType='Zookeeper';
					else
					nodeType=nodeType+'/Supervisor';
				}
				$('#nodeType').html('').text(nodeType);
				}else{
					$('#isAuthDiv').hide();
						$('#stormNodeHead').empty().html(setupDetailData.nodes[node].publicIp);
						$('#isAuthenticated').html('').text('No');
						$('#stormNodeStatus').html('').text(setupDetailData.nodes[node].message);
						$('#stormOs').html('').text(setupDetailData.nodes[node].os);
						
						$('#stormCpu').html('').text('-');
						$('#stormDiskCount').html('').text('-');
						$('#stormDiskSize').html('').text('-');
						$('#stormRam').html('').text('-');
						
						$('#nodeType').html('').text(setupDetailData.nodes[node].type);
						if(setupDetailData.state =='error'){
							if(Object.keys(setupDetailData.nodes[node].errors).length>0){
								$('#errorNodeDiv').show();
		          		 }
		          		$('#nodeDeploymentError').css("margin-top","10px");
		          			for(var key in setupDetailData.nodes[node].errors){
		          				$('#nodeDeploymentError').append('<label class="text-left" style="color: black;" id="'+key+'">'+setupDetailData.nodes[node].errors[key]+'</label>');
		          	        }
		          	  }
				}
		},
		
		
};
