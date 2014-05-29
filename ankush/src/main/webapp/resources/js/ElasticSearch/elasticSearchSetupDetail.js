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

com.impetus.ankush.elasticSearchSetupDetail={
		/*Function for populating default values of elasticSearch cluster*/ 
		populateDefaultValues:function(result){
			$('.section-body input:eq(0)').focus();
			 $('#clusterDeploy').removeAttr('disabled');
			defaultVlaue=result;
			for ( var key in result.output.elasticSearch.ElasticSearch.Vendors){
				$("#vendor-ElasticSearch").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in result.output.elasticSearch.ElasticSearch.Vendors[$("#vendor-ElasticSearch").val()]){
				$("#version-ElasticSearch").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			if(result.output.elasticSearch.ElasticSearch.Defaults.actionAutoCreateIndex){
				$('#actionAutoCreateIndex').attr('checked','checked');
			}else{
				$('#actionAutoCreateIndex').removeAttr('checked');
			}
			if(result.output.elasticSearch.ElasticSearch.Defaults.bootstrapMlockall){
				$('#bootstrapMlockall').attr('checked','checked');
			}else{
				$('#bootstrapMlockall').removeAttr('checked');
			}
			$('#downloadPath-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Vendors[$("#vendor-ElasticSearch").val()][$("#version-ElasticSearch").val()].url);
			$('#installationPath-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.installationPath);
			$('#dataPath-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.dataPath);
			$('#heapSize-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.heapSize);
			$('#localStorageNodes-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.nodeMaxLocalStorageNodes);
			$('#shardIndex-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.indexNumberOfShards);
			$('#replicaIndex-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.indexNumberOfReplicas);
			$('#httpPort-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.httpPort);
			$('#tcpPort-ElasticSearch').val(result.output.elasticSearch.ElasticSearch.Defaults.transportTcpPort);
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
	                	   $('#commonUserName').val(setupDetailData.username).removeAttr('title');
	                	   if(setupDetailData.password==null ||setupDetailData.password==''){
	                		   $('#authTypePassword').removeClass('active');
	                		   $('#authTypeSharedKey').addClass('active');
	                		   $('#sharedKeyDivAuth').show();
	                		   $('#passwordDivAuth').hide();
	                	   }else{
	                		   $('#commonUserPassword').val(setupDetailData.password).removeAttr('title');
	                		   $('#passwordDivAuth').show();
	                		   $('#sharedKeyDivAuth').hide();
	                		   $('#authTypeSharedKey').removeClass('active');
	                		   $('#authTypePassword').addClass('active');
	                	   } 
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
	                	   $("#vendor-ElasticSearch").html('').append("<option value=\"" +setupDetailData.esConf.componentVendor + "\">" + setupDetailData.esConf.componentVendor + "</option>");
	                	   $("#version-ElasticSearch").html('').append("<option value=\"" +setupDetailData.esConf.componentVersion + "\">" + setupDetailData.esConf.componentVersion + "</option>");
	                	
	                	   if(setupDetailData.esConf.localBinaryFile=='' ||setupDetailData.esConf.localBinaryFile==null){
	                		   $('#bundleSourceLocalPath-ElasticSearch').removeClass('active');
	       					   $('#bundleSourceDownload-ElasticSearch').addClass('active');
	                		   $('#elasticSearchDownloadPathDiv').show();
	                		   $('#elasticSearchLocalPathDiv').hide();
	                	   }else{
	                		   $('#elasticSearchLocalPathDiv').show();
	                		   $('#elasticSearchDownloadPathDiv').hide();
	                		   $('#bundleSourceDownload-ElasticSearch').removeClass('active');
	                		   $('#bundleSourceLocalPath-ElasticSearch').addClass('active');
	                	   }
	                	   $('#downloadPath-ElasticSearch').val(setupDetailData.esConf.tarballUrl).removeAttr('title');
	                	   $('#sourcePath-ElasticSearch').val(setupDetailData.esConf.localBinaryFile).removeAttr('title');
	                	   $('#installationPath-ElasticSearch').val(setupDetailData.esConf.installationPath).removeAttr('title');
	                	   $('#dataPath-ElasticSearch').val(setupDetailData.esConf.dataPath).removeAttr('title');
	                	   $('#heapSize-ElasticSearch').val(setupDetailData.esConf.heapSize).removeAttr('title');
	                	   $('#localStorageNodes-ElasticSearch').val(setupDetailData.esConf.nodeMaxLocalStorageNodes).removeAttr('title');
	                	   $('#shardIndex-ElasticSearch').val(setupDetailData.esConf.indexNumberOfShards).removeAttr('title');
	                	   $('#replicaIndex-ElasticSearch').val(setupDetailData.esConf.indexNumberOfReplicas).removeAttr('title');
	                	   $('#httpPort-ElasticSearch').val(setupDetailData.esConf.httpPort).removeAttr('title');
	                	   $('#tcpPort-ElasticSearch').val(setupDetailData.esConf.transportTcpPort).removeAttr('title');
	                	   if(setupDetailData.esConf.actionAutoCreateIndex){
	                		   $('#actionAutoCreateIndex').attr('checked','checked');
	                	   }
	                	   if(setupDetailData.esConf.bootstrapMlockall){
	                		   $('#bootstrapMlockall').attr('checked','checked');
	                	   }
	                	 
	                	   $('.tooltiptext').css('display', 'none');
	                	   $('#nodeSearch').hide();
	                	   $('#toggleSelectButton').hide();
	                	   nodeStatus={
		               	    		nodes:new Array(),
		               	    };
	                	   for ( var i = 0; i < setupDetailData.nodes.length; i++) {
	                		   var addId = null;
	                		   var nodeObj = new Array();
	                		   addId = oNodeTable.fnAddData([
	      												'<input id="elasticSearchCheckBox'
	      													+ i
	      													+ '" class="elasticSearchCheck inspect-node" style="margin-right:10px;" type="checkbox">',
	      													setupDetailData.nodes[i].publicIp,
	      													setupDetailData.nodes[i].os,
	      													 '<a class="" id="datacenter'
	      													+ i + '">'+ setupDetailData.nodes[i].datacenter+'</a>',
	      												'<a class="" id="rack'
	      												+ i + '">'+ setupDetailData.nodes[i].rack+'</a>',
	      												 '<a class="" id="cpuCount'
        												+ i + '">-</a>',
        											'<a class="" id="diskCount'
        												+ i + '">-</a>',
        											'<a class="" id="diskSize'
        												+ i + '">-</a>',
        											'<a class="" id="ramSize'
        												+ i + '">-</a>',
	      												'<div><a href="##" onclick="com.impetus.ankush.elasticSearchSetupDetail.loadNodeDetailelasticSearch('
	      													+ i
	      													+ ');"><img id="navigationImg-'
	      													+ i
	      													+ '" src="'
	      													+ baseUrl
	      													+ '/public/images/icon-chevron-right.png" /></a></div>']);
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
	           			} 
	                	   $('.elasticSearchCheck').attr('checked',true).attr('disabled',true);
	                	   if(clusterState=='error'){
	                		   $('#pathIPFile').val('');
		                 		  $('.elasticSearchCheck').removeAttr('disabled');
		                 	  }
	                	   }
	                   },
	                   error : function() {
	                   }
			});
		},
		/*Function for populating setupdetail page*/
		setupDetailValuePopulate:function(currentClusterId,clusterState){
			$('#nodeList_filter').hide();
			$('#nodeTableDiv').show();
			$('#shardNodeTableHeaderDiv').show();
			if(clusterState=='error'){
				$('#clusterDeploy').text('Redeploy');
				$('#clusterDeploy').val(currentClusterId);
				com.impetus.ankush.elasticSearchSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
				
				com.impetus.ankush.elasticSearchClusterCreation.tooltipInitialize();
				return;
			}
			com.impetus.ankush.common.pageStyleChange();
		
			
			//$('#javaCheckLabel').css('margin-top','4px');
			$('.add-on').css({
				'background-color':'white',
				'border':'none',
				'':'',
			});
			$('#bundlePathJava').css({
				'width':'80%',
			});
			$('.btnGrp').attr('disabled','disabled');
			$('#clusterDeploy').hide();
			$('#clusterDelete').hide();
	  	   	$('#retrieveNodes').attr('disabled','disabled');
	  		$('#inspectNodeBtn').attr('disabled','disabled');
	  	   	$('#elasticSearchDownloadPathDiv').removeClass('span5').addClass('span10');
	  	 	$('#elasticSearchLocalPathDiv').removeClass('span5').addClass('span10');
			if (oNodeTable != null) {
				oNodeTable.fnClearTable();
			}
			com.impetus.ankush.elasticSearchSetupDetail.redeployPagePopulate(currentClusterId,clusterState);
		},
		
		/*Function for loading retrieved node detail page*/
		loadNodeDetailelasticSearch: function(i) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/elasticSearch-cluster/retrievedNodeDetail',
				method : 'get',
				params : {},
				title : 'Node Detail',
				callback : function(data) {
					com.impetus.ankush.elasticSearchSetupDetail.nodeDetail(data.i);
				},
				callbackData : {
					i : i
				}
			});
		},
/*Function for populating values on node detail page*/
		nodeDetail:function(node){
			if(setupDetailData==null || setupDetailData.nodes.length==0){
				$('#elasticSearchNodeHead').empty().html(nodeStatus.nodes[node][0]);
				if(nodeStatus.nodes[node][1]==false){
					$('#isAuthenticated').html('').text('No');
					$('#elasticSearchNodeStatus').html('').text('Unavailable');
				}else if(nodeStatus.nodes[node][2]==false){
					$('#isAuthenticated').html('').text('No');
					$('#elasticSearchNodeStatus').html('').text('Unreachable');
				}else if(nodeStatus.nodes[node][3]==false){
					$('#isAuthenticated').html('').text('No');
					$('#elasticSearchNodeStatus').html('').text('Unauthenticated');
				}else{
					$('#isAuthenticated').html('').text('Yes');
					$('#elasticSearchNodeStatus').html('').text('Available');
				}
				$('#elasticSearchOs').html('').text(nodeStatus.nodes[node][4]);
				$('#elasticSearchDatacenter').html(nodeStatus.nodes[node][5]);
				$('#elasticSearchRack').html(nodeStatus.nodes[node][6]);
				$('#elasticSearchCpu').html('').text(nodeStatus.nodes[node][7]);
				$('#elasticSearchDiskCount').html('').text(nodeStatus.nodes[node][8]);
				if(nodeStatus.nodes[node][9]==''){
					$('#elasticSearchDiskSize').html('');
				}else{
					$('#elasticSearchDiskSize').html('').text(nodeStatus.nodes[node][9]+' GB');
				}
				if(nodeStatus.nodes[node][10]==''){
					$('#elasticSearchRam').html('');
				}else{
					$('#elasticSearchRam').html('').text(nodeStatus.nodes[node][10]+' GB');
				}
				if($('#elasticSearchCheckBox'+node).is(':checked')){
					$('#nodeType').html('').text('ElasticSearch');
				}else{
					$('#nodeType').html('');
				}
				}else{
					$('#isAuthDiv').hide();
						$('#elasticSearchNodeHead').empty().html(setupDetailData.nodes[node].publicIp);
						$('#isAuthenticated').html('').text('No');
						$('#elasticSearchNodeStatus').html('').text(setupDetailData.nodes[node].message);
						$('#elasticSearchOs').html('').text(setupDetailData.nodes[node].os);
						$('#elasticSearchDatacenter').html(setupDetailData.nodes[node].datacenter);
						$('#elasticSearchRack').html(setupDetailData.nodes[node].rack);
						$('#elasticSearchCpu').html('').text('-');
						$('#elasticSearchDiskCount').html('').text('-');
						$('#elasticSearchDiskSize').html('').text('-');
						$('#elasticSearchRam').html('').text('-');
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
