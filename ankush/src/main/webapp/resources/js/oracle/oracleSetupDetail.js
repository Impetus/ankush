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

com.impetus.ankush.oracleSetupDetail={

		redeployPagePopulate:function(currentClusterId,clusterState){
			 var url = baseUrl + '/monitor/' + currentClusterId + '/details';
		      $.ajax({
	              'type' : 'GET',
	              'url' : url,
	              'contentType' : 'application/json',
	              'dataType' : 'json',
	              'async' : true,
	              'success' : function(result) {
	            	  setupDetailData=result.output;
	            	  if(setupDetailData.state=='error'){
	            		  
	            		   $('#commonDeleteButton').removeAttr('disabled');
	            		   $('#errorDivMain').html('');
	            		   $('#validateError').html('');
	            		   var count=0;
	            		   for(var key in setupDetailData.errors){
	            			   if(key=='ntpServer' ||key=='validation'){
	            				   count++;
	                			   $("#errorDivMain").append("<div class='errorLineDiv'><a href='#'  >"+count+". "+setupDetailData.errors[key]+"</a></div>");   
	            			   }
	            			   else{
	            				   count++;
	                			   $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+count+". "+setupDetailData.errors[key]+"</a></div>");   
	            			   }
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
	            		  $("#errorDivMain").html('').append("<div class='errorLineDiv'><a href='#'  >1. "+setupDetailData.errors[0]+"</a></div>");
	            		  $('#errorDivMain').show();
	            		  $('#validateError').html('').text('1 Error').show();
	            		  $('#commonDeleteButton').removeAttr('disabled');
	           		   return;
	           	   }
	            	   $('#storeName').val(setupDetailData.clusterName).removeAttr('title');
	            	   $('#dataCenter').val(setupDetailData.oracleNoSQLConf.datacenterName).removeAttr('title');
	            	   $('#topology').val(setupDetailData.oracleNoSQLConf.topologyName).removeAttr('title');
	            	   $('#repFactor').val(setupDetailData.oracleNoSQLConf.replicationFactor).removeAttr('title');
	            	   $('#partitions').val(setupDetailData.oracleNoSQLConf.partitionCount).removeAttr('title');
	            	   $('#registryPort').val(setupDetailData.oracleNoSQLConf.registryPort).removeAttr('title');
	            	   $('#haPort1').val(setupDetailData.oracleNoSQLConf.haPortRangeStart).removeAttr('title');
	            	   $('#haPort2').val(setupDetailData.oracleNoSQLConf.haPortRangeEnd).removeAttr('title');
	            	   $('#baseDir').val(setupDetailData.oracleNoSQLConf.basePath).removeAttr('title');
	            	   $('#installationPath').val(setupDetailData.oracleNoSQLConf.installationPath).removeAttr('title');
	            	   $('#dataPath').val(setupDetailData.oracleNoSQLConf.dataPath).removeAttr('title');
	            	   var dbPackage=setupDetailData.oracleNoSQLConf.serverTarballLocation.split('repo/')[1];
	            	   if($('#clusterDeploy').val()==''){
	            		   $("#selectDBPackage").html('').append(
		                           "<option value=\"" + dbPackage+ "\">"
		                                   + dbPackage + "</option>");
	            	   }
	            	   else{
	            		   $("#selectDBPackage").val(dbPackage); 
	            	   }
	            	
	            	   $('#ntpServer').val(setupDetailData.oracleNoSQLConf.ntpServer).removeAttr('title');
	            	   $('#userName').val(setupDetailData.username).removeAttr('title');
	            	   if(setupDetailData.password==null ||setupDetailData.password==''){
		          		   $('#passwordAuthRadio').removeClass('active');
		          		   $('#sharedKeyAuthRadio').addClass('active');
		          		   $('#sharedKeyFileUploadDiv').show();
		          		   $('#passwordAuthDiv').hide();
		               	 }else{
		          		   $('#password').val(setupDetailData.password).removeAttr('title');
		          		   $('#passwordAuthDiv').show();
		          		   $('#sharedKeyFileUploadDiv').hide();
		          		   $('#sharedKeyAuthRadio').removeClass('active');
		          		   $('#passwordAuthRadio').addClass('active');
		               	 } 
	            	   if(setupDetailData.ipPattern==''){
	        	    	   $('#filePath').val(setupDetailData.patternFile);
	            		   $('#throughPassword').removeClass('active');
	            		   $('#throughSharedKey').addClass('active');
	            		   $('#fileUploadDiv').show();
	            		   $('#ipRangeDiv').hide();
	            	   }else{
	            		   $('#ipRangeDiv').show();
	            		   $('#fileUploadDiv').hide();
	            		   $('#ipRange').val(setupDetailData.ipPattern).removeAttr('title');
	            		   $('#throughSharedKey').removeClass('active');
	            		   $('#throughPassword').addClass('active');
	            	   }
	            	   nodeStatus={
	               	    		nodes:new Array(),
	               	    };
	            	   for ( var i = 0; i < setupDetailData.nodes.length; i++) {
	                       var addId = null;
	                       var adminPort='';
	                       var nodeObj = new Array();
	                        adminPort=setupDetailData.nodes[i].adminPort;
	                       if(adminPort==null){
	                    	   adminPort='';
	                       }
	                       addId = oNodeTable.fnAddData([
	                                                     '<input type="checkbox" name="" value=""  id="nodeCheck'
	                                                             + i
	                                                             + '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.oClusterSetup.nodeCheckBox('
	                                                             + i + ');"/>',
	                                                             setupDetailData.nodes[i].publicIp,
	                                                     '<input id="adminCheckBox'
	                                                             + i
	                                                             + '" class="adminCheck" style="margin-right:10px;" type="checkbox" ><a style=margin:left:5px;" href="#" class="editableLabel" id="adminPort'
	                                                             + i + '">'+adminPort+'</a>',
	                                                             '<a class="editableLabel" id="storageDirs'
	           		                                          + i
	           		                                          + '">'
	           		                                          + setupDetailData.nodes[i].storageDirs
	           		                                          + '</a>',
	           		                                       '<a class="" id="datacenter'
	                                                          + i + '">'+ setupDetailData.nodes[i].datacenter+'</a>',
	                                                     '<a class="editableLabel" id="capacity'
	                                                             + i
	                                                             + '">'
	                                                             + setupDetailData.nodes[i].capacity
	                                                             + '</a>',
	                                                     '<a class="" id="cpu'
	                                                             + i + '">'
	                                                             + setupDetailData.nodes[i].cpuNum
	                                                             + '</a>',
	                                                     '<a class="" id="memory'
	                                                             + i + '">'
	                                                             + setupDetailData.nodes[i].memoryMb
	                                                             + '</a>',
	                                                     '<a class="" id="registryPort'
	                                                             + i
	                                                             + '">'
	                                                             + setupDetailData.nodes[i].registryPort
	                                                             + '</a>',
	                                                     '<div><div style="float:left"><a class="" id="haPort1'
	                                                             + i
	                                                             + '">'
	                                                             + setupDetailData.nodes[i].haPortRangeStart
	                                                             + '</a></div>','<div style="float:left"><a class="" id="haPort2'
	                                                             + i
	                                                             + '">'
	                                                             + setupDetailData.nodes[i].haPortRangeEnd
	                                                             + '</a></div></div>',
	                                                     '<div><a href="##" onclick="com.impetus.ankush.oracleSetupDetail.loadNodeDetailOracle('
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
	                       var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
	          				theNode.setAttribute('id', 'node'+ setupDetailData.nodes[i].publicIp.split('.').join('_'));
	          				if (Object.keys(setupDetailData.nodes[i].errors).length>0 ){
	          					rowId = setupDetailData.nodes[i].publicIp.split('.').join('_');
	          					$('td', $('#node'+rowId)).addClass('error-row');
	          					$('#node' + rowId).addClass('error-row');
	          				}
	                    if(setupDetailData.nodes[i].admin){
	                    $('#adminCheckBox'+i).attr('checked',true);
	                    }else{
	                    	 $('#adminCheckBox'+i).attr('checked',false);
	                    }
	                  }
	            	   $('.nodeCheckBox').attr('checked',true);
                 	    $('#nodeCheckHead').attr('checked',true);
	            	   if(clusterState!='error'){
	            		   $('.adminCheck').attr('disabled','disabled');
	            		   $('.nodeCheckBox').attr('disabled','disabled');
	                 	  }
	              }
		      });	
		},
		
		
setupDetailValuePopulate:function(currentClusterId,clusterState){
	 $('#filePath').val('');
	 $('#nodeTable').show();
		if(clusterState=='error'){
			com.impetus.ankush.oClusterSetup.getDefaultValue();
			com.impetus.ankush.oClusterSetup.populateOracleDBPackage();
			$('#clusterDeploy').text('Redeploy');
			$('#clusterDeploy').val(currentClusterId);
			com.impetus.ankush.oracleSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
			com.impetus.ankush.oClusterSetup.tooltip();
			return;
		}
	com.impetus.ankush.common.pageStyleChange();
	$('#haPort1').css('width','50%');
	$('#haPort2').css('width','50%');
	$('#clusterDeploy').hide();
	$('#commonDeleteButton').hide();
	$('#inspectNodeBtn').attr('disabled',true);
	$('.nodeCheckBox').attr('disabled','disabled');
	$('.adminCheckBox').attr('disabled','disabled');
	$('#retrieveNodeButton').attr('disabled','disabled');
	$('#inspectNodeBtn').attr('disabled','disabled');
	$('#filePathDb').attr('disabled','disabled');
	$('#toggleButtonOracle').hide();
	$('#divRackMapping span').css({
		'border' : 'none',
		'background' : 'none'
	});
	$('.btnGrp').attr('disabled','disabled');
	if (oNodeTable != null) {
		oNodeTable.fnClearTable();
	}
	com.impetus.ankush.oracleSetupDetail.redeployPagePopulate(currentClusterId,clusterState);  
},


loadNodeDetailOracle:function(i) {
    $('#content-panel').sectionSlider('addChildPanel', {
        current : 'login-panel',
        url : baseUrl + '/oracle-cluster/oNodeDetail',
        method : 'get',
        params : {},
        title : 'Node Detail',
        tooltipTitle : 'Cluster Setup Detail',
        callback : function(data) {
            com.impetus.ankush.oracleSetupDetail.nodeDetail(data.i);
        },
        callbackData : {
            i : i
        }
    });
},
nodeDetail : function(node) {
	if(setupDetailData==null || setupDetailData.nodes.length==0){
    if(nodeStatus.nodes[node][1]==false){
        $('#nodeStatus').html('').text('Unavailable');
    }else if(nodeStatus.nodes[node][2]==false){
        $('#nodeStatus').html('').text('Unreachable');
    }else if(nodeStatus.nodes[node][3]==false){
        $('#nodeStatus').html('').text('Unauthenticated');
    }else{
        $('#nodeStatus').html('').text('Available');
    }
    if ($('#adminCheckBox' + node).is(':checked')) {
        $('#adminStatus').html('').text("Yes");
    } else {
        $('#adminStatus').html('').text("No");
    }
    $('#nodeDetailHead').html('').html(nodeStatus.nodes[node][0]);
    $('#nodeDatacenter').html('').text($('#datacenter' + node).text());
    $('#nodeCapacity').html('').text($('#capacity' + node).text());
    $('#nodeCpu').html('').text($('#cpu' + node).text());
    $('#nodeMemory').html('').text($('#memory' + node).text());
    $('#nodeRegPort').html('').text($('#registryPort' + node).text());
    $('#nodeHA1').html('').text($('#haPort1' + node).text());
    $('#nodeHA2').html('').text($('#haPort2' + node).text());
    }else{
    	 $('#nodeDetailHead').html('').html(setupDetailData.nodes[node].publicIp);
    	  if ($('#adminCheckBox' + node).is(':checked')) {
              $('#adminStatus').html('').text("Yes");
          } else {
              $('#adminStatus').html('').text("No");
          }
    	 $('#nodeStatus').html('').text('Available');
    	 $('#nodeDatacenter').html('').text($('#datacenter' + node).text());
         $('#nodeCapacity').html('').text($('#capacity' + node).text());
         $('#nodeCpu').html('').text($('#cpu' + node).text());
         $('#nodeMemory').html('').text($('#memory' + node).text());
         $('#nodeRegPort').html('').text($('#registryPort' + node).text());
         $('#nodeHA1').html('').text($('#haPort1' + node).text());
         $('#nodeHA2').html('').text($('#haPort2' + node).text());
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
