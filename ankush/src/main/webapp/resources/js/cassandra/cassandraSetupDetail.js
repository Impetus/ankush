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

com.impetus.ankush.cassandraSetupDetail={
		/*Function for populating default values*/
		populateDefaultValues:function(result){
			defaultValue=result;
			/*if($('#clusterDeploy').val() !=''){
				return;
			}*/
			$('.section-body input:eq(0)').focus();
		//	$('#homePathJava').val(result.output.cassandra.javaHome);
			for ( var key in result.output.cassandra.vendors){
				$("#vendor-Cassandra").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in result.output.cassandra.vendors[$("#vendor-Cassandra").val()]){
				$("#version-Cassandra").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$('#downloadPath-Cassandra').val(result.output.cassandra.vendors[$("#vendor-Cassandra").val()][$("#version-Cassandra").val()].downloadUrl);
			for ( var key in result.output.cassandra.partitioners){
				$("#partitionerDropDown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in result.output.cassandra.snitch){
				$("#snitchDropDown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			$("#installationPath-Cassandra").val(result.output.cassandra.installationPath);
			$("#rpcPort").val(result.output.cassandra.rpcPort);
			$("#storagePort").val(result.output.cassandra.storagePort);
			$("#logDir").val(result.output.cassandra.logDir);
			$("#dataDir").val(result.output.cassandra.dataDir);
			$("#savedCachesDir").val(result.output.cassandra.savedCachesDir);
			$("#commitlogDir").val(result.output.cassandra.commitlogDir);
		},
		redeployPagePopulate:function(currentClusterId,clusterState){
			 var url = baseUrl + '/monitor/' + currentClusterId + '/details';
		       $.ajax({
		                   'type' : 'GET',
		                   'url' : url,
		                   'contentType' : 'application/json',
		                   'dataType' : 'json',
		                   'async' : false,
		                   'success' : function(result) {
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
		                 		  if(Object.keys(setupDetailData.errors).length > 0){
		                 			  	$("#errorDivMain").show();
		                 		   if(Object.keys(setupDetailData.errors).length > 1){
		                                $('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Errors');
		                           }
		                           else{
		                               $('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Error');
		                           }
		                 		  }
		                 	   } if(!setupDetailData.status){
		                 		  $("#errorDivMain").html('').append("<div class='errorLineDiv'><a href='#'  >1. "+setupDetailData.error[0]+"</a></div>");
		                 		  $('#errorDivMain').show();
		                 		  $('#validateError').html('').text('1 Error').show();
		                 		  $('#clusterDelete').removeAttr('disabled');
		                		   return;
		                	   }
		                	    $('#clusterName').val(setupDetailData.clusterName).removeAttr('title');
		                	    $('#inputClusterDesc').val(setupDetailData.description).removeAttr('title');
		                	    if(setupDetailData.javaConf.install==false){
		                	    	 $('#homePathJava').val(setupDetailData.javaConf.javaHomePath).removeAttr('title');
		                	    	 $('#chkInstallJava').removeAttr('checked');
		                	    	 $('#bundlePathJava').removeAttr("disabled");
			       					 $('#homePathJava').attr("disabled","disabled");
		                	    }else{
		                	    	 $('#chkInstallJava').attr('checked',true);
		                	    	 $('#bundlePathJava').val(setupDetailData.javaConf.javaBundle).removeAttr('title');
		                	    	 $('#homePathJava').removeAttr("disabled");
			       					 $('#bundlePathJava').attr("disabled","disabled"); 
		                	    }
		                	    $('#commonUserName').val(setupDetailData.username).removeAttr('title');
		                	    if(setupDetailData.password==null ||setupDetailData.password==''){
					          		   $('#authTypePassword').removeClass('active');
					          		   $('#authTypeSharedKey').addClass('active');
					          		   $('#sharedKeyFileUploadDiv').show();
					          		   $('#passwordAuthDiv').hide();
					               	 }else{
					          		   $('#commonUserPassword').val(setupDetailData.password).removeAttr('title');
					          		   $('#passwordAuthDiv').show();
					          		   $('#sharedKeyFileUploadDiv').hide();
					          		   $('#authTypeSharedKey').removeClass('active');
					          		   $('#authTypePassword').addClass('active');
					               	 } 
		                	    if(setupDetailData.ipPattern==''){
		                	    	   $('#pathIPFile').val(setupDetailData.patternFile);
			                		//   $('#modeIPFile').attr('checked','checked');
			                		   $('#modeIPRange').removeClass('active');
			                		   $('#modeIPFile').addClass('active');
			                		   $('#fileUploadDiv').show();
			                		   $('#ipRangeDiv').hide();
			                	   }else{
			                		   $('#ipRangeDiv').show();
			                		   $('#fileUploadDiv').hide();
			                		   $('#ipRange').val(setupDetailData.ipPattern).removeAttr('title');
			                		   $('#modeIPFile').removeClass('active');
			                		   $('#modeIPRange').addClass('active');
			                		  // $('#modeIPRange').attr('checked','checked');
			                	   }
		                	    if(setupDetailData.rackEnabled==true){
		                	    	$('#rackMapCheck').attr('checked','checked');
		                	    	$('#rackFilePath').removeAttr('disabled').css('background-color','white').val(setupDetailData.rackFileName);
		                	    }else{
		                	    	$('#rackMapCheck').removeAttr('checked');
		                	    }
		                	    $('#vendor-Cassandra').val(setupDetailData.cassandra.componentVendor);
		                	    $("#version-Cassandra").html('');
		                       	for ( var key in defaultValue.output.cassandra.vendors[$("#vendor-Cassandra").val()]){
		            				$("#version-Cassandra").append("<option value=\"" + key + "\">" + key + "</option>");
		            			}
		                	    $('#version-Cassandra').val(setupDetailData.cassandra.componentVersion);
		                	    if(setupDetailData.cassandra.tarballUrl=='' ||setupDetailData.cassandra.tarballUrl==null){
		                	    	   $('#localPath-Cassandra').val(setupDetailData.cassandra.localBinaryFile).removeAttr('title');
			                	//	   $('#bundleSourceLocalPath-Cassandra').attr('checked','checked');
		                	    	   $('#bundleSourceDownload-Cassandra').removeClass('active');
		                	    	   $('#bundleSourceLocalPath-Cassandra').addClass('active');
			                		   $('#localPathDiv').show();
			                		   $('#downloadPathDiv').hide();
			                	   }else{
			                		   $('#downloadPath-Cassandra').val(setupDetailData.cassandra.tarballUrl);
			                		   $('#downloadPathDiv').show();
			                		   $('#localPathDiv').hide();
			                		//   $('#bundleSourceDownload-Cassandra').attr('checked','checked');
			                		   $('#bundleSourceLocalPath-Cassandra').removeClass('active');
			                		   $('#bundleSourceDownload-Cassandra').addClass('active');
			                	   }
		                	    if(setupDetailData.cassandra.vNodeEnabled==true){
		                	    	$("#vNodeCheck").attr("checked","checked");
		                	    	oNodeTable.fnSetColumnVis( 10, true );
		                	    }else{
		                	    	$("#vNodeCheck").removeAttr("checked");
		                	    	oNodeTable.fnSetColumnVis( 10, false );
		                	    }
		                	    $('#installationPath-Cassandra').val(setupDetailData.cassandra.installationPath).removeAttr('title');
		                	    for(var key in defaultValue.output.cassandra.partitioners){
		                	    	if(setupDetailData.cassandra.partitioner == defaultValue.output.cassandra.partitioners[key]){
		                	    		  $('#partitionerDropDown').val(key);
		                	    		  break;
		                	    	}
		                	    }
		                	    $('#snitchDropDown').val(setupDetailData.cassandra.snitch);
		                	    $('#rpcPort').val(setupDetailData.cassandra.rpcPort).removeAttr('title');
		                	    $('#storagePort').val(setupDetailData.cassandra.storagePort).removeAttr('title');
		                	    $('#dataDir').val(setupDetailData.cassandra.dataDir).removeAttr('title');
		                	    $('#logDir').val(setupDetailData.cassandra.logDir).removeAttr('title');
		                	    $('#savedCachesDir').val(setupDetailData.cassandra.savedCachesDir).removeAttr('title');
		                	    $('#commitlogDir').val(setupDetailData.cassandra.commitlogDir).removeAttr('title');
		                	   /* $('#nodeSearch').hide();
		                 	    $('#toggleSelectButton').hide();*/
		                 	   nodeStatus={
			               	    		nodes:new Array(),
			               	    };
		                 	   for ( var i = 0; i < setupDetailData.nodes.length; i++) {
		            				var addId = null;
		            				  var nodeObj = new Array();
		            					addId = oNodeTable.fnAddData([
	        					                                 '<input type="checkbox" name="" value=""  id="nodeCheck'
	        	                                                 + i
	        	                                                 + '" class="nodeCheckBox" />',
	        	                                                 setupDetailData.nodes[i].publicIp,
		            	                                         '<input type="checkbox" name="" value=""  id="seedNodeCheck'
		            	                                         + i
		            	                                         + '" class="seedNodeCheckBox"/>',
		            	                                         setupDetailData.nodes[i].os,
		            	                                         '<a class="" id="datacenter'
		            												+ i + '">'+setupDetailData.nodes[i].datacenter+'</a>',
		            											'<a class="" id="rack'
		            											+ i + '">'+setupDetailData.nodes[i].rack+'</a>',
		            	                                         '<a class="" id="cpuCount'
		            												+ i + '">-</a>',
		            											'<a class="" id="diskCount'
		            												+ i + '">-</a>',
		            											'<a class="" id="diskSize'
		            												+ i + '">-</a>',
		            											'<a class="" id="ramSize'
		            												+ i + '">-</a>',
		            											'<a class="" id="vNodeCount'
		            	                                            + i + '">'+setupDetailData.nodes[i].vNodeCount+'</a>',
		            											'<label>Yes</label>',
		            	                                        '<a href="##" onclick="com.impetus.ankush.cassandraSetupDetail.loadNodeDetail('
	        	                                                 + i
	        	                                                 + ');"><img id="navigationImg-'
	        	                                                 + i
	        	                                                 + '" src="'
	        	                                                 + baseUrl
	        	                                                 + '/public/images/icon-chevron-right.png" /></a>' ]);
		            					nodeObj.push(setupDetailData.nodes[i].publicIp);
			           					nodeObj.push('true');
			           					nodeObj.push('true');
			           					nodeObj.push('true');
			           					nodeObj.push(setupDetailData.nodes[i].os);
			           					nodeObj.push($('#datacenter'+i).text());
			           					nodeObj.push($('#rack'+i).text());
			           					nodeObj.push('');
			           					nodeObj.push('');
			           					nodeObj.push('');
			           					nodeStatus.nodes.push(nodeObj);
		            				var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
		            				theNode.setAttribute('id', 'node'+ setupDetailData.nodes[i].publicIp.split('.').join('_'));
		            				if (Object.keys(setupDetailData.nodes[i].errors).length>0 ){
		            					rowId = setupDetailData.nodes[i].publicIp.split('.').join('_');
		            					$('td', $('#node'+rowId)).addClass('error-row');
		            					$('#node' + rowId).addClass('error-row');
		            				}
		            				 if(setupDetailData.nodes[i].seedNode==true){
		  	                 		   $('#seedNodeCheck'+i).attr('checked',true);
		  	                 	   }
		            			}
		                 	  if(clusterState!='error'){
		                 		 $('.nodeCheckBox').attr('disabled',true);
		                 		$('.seedNodeCheckBox').attr('disabled',true);
		                 	  }else{
		                 			$('#pathIPFile').val('');
		                 			$('#rackFilePath').val('');
		                 	  }
		                 	  $('.nodeCheckBox').attr('checked',true);
		                	   $('#nodeCheckHead').attr('checked',true);
		                 	  
		                   }
		       		});	
		},
/*Function for value population on setupdetail page*/	
		setupDetailValuePopulate:function(currentClusterId,clusterState){
			com.impetus.ankush.common.getDefaultValue('cassandra','cassandraSetupDetail.populateDefaultValues');
			if(clusterState=='error'){
				$('#clusterDeploy').text('Redeploy');
				$('#clusterDeploy').val(currentClusterId);
				$('#nodeListDiv').show();
			
				com.impetus.ankush.cassandraSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
				com.impetus.ankush.cassandraClusterCreation.tooltipInitialize();
				return;
			}
			com.impetus.ankush.common.pageStyleChange();
			$('#nodeListDiv').show();
			$('#shardNodeTableHeaderDiv').show();
		//	$('#javaCheckLabel').css('padding-top','6px');
			$('.add-on').css({
				'background-color':'white',
				'border':'none',
				'':'',
			});
			$('#bundlePathJava').css({
				'width':'80%',
			});
			$('.btnGrp').attr('disabled','disabled');
			$('#nodeList_filter').hide();
			$('#clusterDeploy').hide();
			$('#retrieveNodes').attr('disabled','disabled');
			$('#inspectNodeBtn').attr('disabled','disabled');
			$('#toggleSelectButton').attr('disabled','disabled');
			$('#clusterDelete').hide();
			
			if (oNodeTable != null) {
				oNodeTable.fnClearTable();
			}
			$('#clusterDeploy').val(currentClusterId);
			com.impetus.ankush.cassandraSetupDetail.redeployPagePopulate(currentClusterId,clusterState);	  
	    },	
/*Function for loading node detail page*/
	    loadNodeDetail: function(i) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/cassandra-cluster/retrievedNodeDetail',
				method : 'get',
				params : {},
				title : 'Node Detail',
				callback : function(data) {
					com.impetus.ankush.cassandraSetupDetail.nodeDetail(data.i);
				},
				callbackData : {
					i : i
				}
			});
		},
		
/*Function for populating node detail page*/		
		nodeDetail:function(node){
			if(setupDetailData==null || setupDetailData.nodes.length==0){
				$('#cassandraNodeHead').empty().html(nodeStatus.nodes[node][0]);
				if(nodeStatus.nodes[node][1]==false){
					$('#isAuthenticated').html('').text('No');
					$('#cassandraNodeStatus').html('').text('In Use');
				}else if(nodeStatus.nodes[node][2]==false){
					$('#isAuthenticated').html('').text('No');
					$('#cassandraNodeStatus').html('').text('Available');
				}else if(nodeStatus.nodes[node][3]==false){
					$('#isAuthenticated').html('').text('No');
					$('#cassandraNodeStatus').html('').text('Available');
				}else{
					$('#isAuthenticated').html('').text('Yes');
					$('#cassandraNodeStatus').html('').text('Available');
				}
				$('#cassandraOs').html('').text(nodeStatus.nodes[node][4]);
				$('#cassandraDatacenter').html(nodeStatus.nodes[node][5]);
				$('#cassandraRack').html(nodeStatus.nodes[node][6]);
				/*if(nodeStatus.nodes[node][6]==''){
					$('#cassandraCpu').html('-');
				}else{
					$('#cassandraCpu').html('').text(nodeStatus.nodes[node][6]);
				}
				*/
				$('#cassandraCpu').html('').text(nodeStatus.nodes[node][7]);
				/*if(nodeStatus.nodes[node][7]==''){
					$('#cassandraDiskCount').html('-');
				}else{
					$('#cassandraDiskCount').html('').text(nodeStatus.nodes[node][7]);
				}*/
				$('#cassandraDiskCount').html('').text(nodeStatus.nodes[node][8]);
				if(nodeStatus.nodes[node][9]==''){
					$('#cassandraDiskSize').html('');
				}else{
					$('#cassandraDiskSize').html('').text(nodeStatus.nodes[node][9]+' GB');
				}
				if(nodeStatus.nodes[node][10]==''){
					$('#cassandraRam').html('');
				}else{
					$('#cassandraRam').html('').text(nodeStatus.nodes[node][10]+' GB');
				}
				}else{
					$('#isAuth').hide();
					$('#cassandraNodeHead').empty().html(setupDetailData.nodes[node].publicIp);
					$('#cassandraNodeStatus').html('').text(setupDetailData.nodes[node].message);
					$('#cassandraOs').html('').text(setupDetailData.nodes[node].os);
					$('#cassandraDatacenter').html(setupDetailData.nodes[node].datacenter);
					$('#cassandraRack').html(setupDetailData.nodes[node].rack);
					$('#cassandraCpu').html('').text('-');
					$('#cassandraDiskCount').html('').text('-');
					$('#cassandraDiskSize').html('').text('-');
					$('#cassandraRam').html('').text('-');
				
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
