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

var idPattern = [];
var com = {
		impetus : {
			ankush : {
				tooltip:{},
			   init:function(){
				   $("#login-form").validate({
                       errorElement : "error",
                       rules : {
                           username : {
                               required : true,
                           }
                       },
                       messages : {
                           username : {
                               required : "Enter your user ID.",
                           }
                       },
                       errorPlacement : function(error, element) {
                           error.appendTo(element.parent().next());
                       }
                   });
				   $("#password-form").validate({
                       errorElement : "error",
                       rules : {
                           email : {
                               required : true,
                           }
                       },
                       messages : {
                           email : {
                               required : "Please provide your email",
                           }
                       },
                       errorPlacement : function(error, element) {
                           error.appendTo(element.parent().next());
                       }
                   });
			   },
			  
			   uploadFile : function(uploadUrl, fileId, callback, context) {
				  	$.ajaxFileUpload({
				    	url : uploadUrl,
				    	secureuri : false,
				    	fileElementId : fileId,
				    	dataType : 'text',
				    	success : function(data) {
				    		if(callback)
				    			callback(data, context);
				    	},
				  		error : function() {
				    		alert('error');
				        }
				    });
 				},
 				 uploadFileNew : function(uploadUrl, fileId,object,callback, context) {
 					 if(object != null){
 						$('#'+object.uploadBtn).button();
 	                    $('#'+object.uploadBtn).button('loading');
 	                   if(object.otherBtn!=null){
		            		$('#'+object.otherBtn).attr('disabled','disabled');
	            	}
 					 }
 					 $.ajaxFileUpload({
 			            url : uploadUrl,
 			            secureuri : false,
 			            fileElementId : fileId,
 			            dataType : 'text',
 			            success : function(data) {
 			            	if(object != null){
	 			            	$('#'+object.uploadBtn).button('reset');
	 			            	if(object.otherBtn!=null){
	 			            		$('#'+object.otherBtn).removeAttr('disabled');
 			            	}
 			            	 }
 			                if (callback)
 			                    callback(data, context);
 			            	
 			            },
 			            error : function() {
 			            	if(object != null){
 			            	$('#'+object.uploadBtn).button('reset');
 			            	if(object.otherBtn!=null){
 			            		$('#'+object.otherBtn).removeAttr('disabled');
 			            	}
 			            	var btnId=object.uploadBtn;
 			            	$('#'+btnId).button('reset');
 			            	}
 			            }
 			        });
  				},
  				 //this function will remove child of given child no from slider
 			   removeChild : function(childNo){
 				   document.getElementsByClassName('nav-bar-'+childNo)[0].click();
 				},
 				//this function will remove current child of slider
 				removeCurrentChild : function(){
 					var childNo = $.data(document, "panels").children.length;
 					document.getElementsByClassName('nav-bar-'+childNo)[0].click();
 				},
 				//this function will place order of tiles in order of red, yellow, green
 				tileReordring : function(data){
 					if(data == undefined)
 						return;
 					var mainArray = [];
 					var arrayForWarning = [];
 					var arrayForNormal = [];
 					for(var i = 0 ; i < data.length ; i++){
 						switch(data[i].status){
 							case 'Error' : 
 								mainArray.push(data[i]);
 								break;
 							case 'Critical' : 
 								mainArray.push(data[i]);
 								break;
 							case 'Warning' :
 								arrayForWarning.push(data[i]);
 								break;
 							case 'Normal' :	
 								arrayForNormal.push(data[i]);
 								break;
 						}
 					}
 					for(var i = 0 ; i < arrayForWarning.length ; i++)
 						mainArray.push(arrayForWarning[i]);
 					for(var i = 0 ; i < arrayForNormal.length ; i++)
 						mainArray.push(arrayForNormal[i]);
 				return mainArray ;
 				},
  				//this will checked unchecked all check boxes of data table on header check box click
  				checked_unchecked_all : function(className,headerElement){
  					if($(headerElement).is(':checked')){
  						$('.'+className).each(function(){
  							if(!$(this).is(":disabled"))
  								$(this).attr('checked',true)
  						});
  					}
  					else
  						$('.'+className).attr('checked',false);
  				},
  				//this will checked unchecked all checkboxes of data table on header checkbox click
  				headerCheckedOrNot : function(className,headerId){
  					var flag = true;
  					$('.'+className).each(function(){
  						if(!$(this).is(':checked')){
  							flag = false;
  							return;
  						}
  					});
  					if(flag == true){
  						$('#'+headerId).attr('checked',true);
  					}else{
  						$('#'+headerId).attr('checked',false);
  					}
  				},
  				//this will populate jquery jstree
  				jstreePopulate : function(id,data){
  					for(var key in data){
  						idPattern.push(key);
  						$('#'+id+'> ul').append('<li id="'+idPattern.join('_').replace(/ /g,"_")+'" class="treeTooltip" title="'+key+'" data-placement="right"><a href="#">'+key+'</a></li>');
						$('#'+idPattern.join('_')).append('<ul></ul>');
  					    if(data[key].length != undefined){
  					    	for(var i = 0 ; i < data[key].length ; i++){
								if(data[key][i].length == undefined){
									com.impetus.ankush.jstreePopulate(idPattern.join('_'),data[key][i]);
								}
								else{
									var stringId = idPattern.join('_');
									idPattern.push(data[key][i]);
									$('#'+stringId+'>ul').append('<li id="'+idPattern.join('_').replace(/ /g,"_")+'" class="treeTooltip" title="'+data[key][i]+'" data-placement="right"><a href="#">'+data[key][i]+'</a></li>');
									idPattern.pop();
								}
							}
						}else{
							com.impetus.ankush.jstreePopulate(idPattern.join('_').replace(/ /g,"_"),data[key]);
  						}
  					  idPattern.pop();
  					}
  					$('.treeTooltip').tooltip();
  				},
  				//this will expand jquery jstree
  				jstreeExpandAll : function(id){
  					$('#'+id).jstree('open_all');
  				},
  				//this will collapse jquery jstree
  				jstreeCollapseAll : function(id){
  					$('#'+id).jstree('close_all');
  				},
  				//tile creation tileVar is an object(pass by refrence to change css for tiles) i is index of tile, tile is html element of parent div anidForTile is id
  				createTyleUsingType : function(tileVar,i,clusterTiles,tile,idForTile){
  					if(tile == null)
  						return;
  					var linesFun = function(){
  					var lineString = '';
  					if((Object.keys(clusterTiles[i].data).length != 0) && (clusterTiles[i].data != null)){
  						for(var lineIndex = 0 ; lineIndex < clusterTiles[i].data.tileLines.length ; lineIndex++)
  							lineString += '<div class="row-fluid"><div class="text-right span6">'+clusterTiles[i].data.tileLines[lineIndex].text[0]+'</div><div class="text-left span6">'+clusterTiles[i].data.tileLines[lineIndex].text[1]+'</div></div>';
					return 	lineString;
  					};
  					};
  					var status = {
  							'Error' : 'errorbox',
  							'Critical' : 'errorbox',
  							'Warning' : 'warningbox',
  							'Normal' : 'infobox'
  					};
  					var colorClass = {
  							'Error' : 'redTitle',
  							'Critical' : 'redTitle',
  							'Warning' : 'yellowTitle',
  							'Normal' : 'greenTitle'
  					};
  					var tileId = 0;
  					if(clusterTiles[i].tileType == undefined){
						if (clusterTiles[i].data != null) {
							tileId = clusterTiles[i].data.tileid;
						}
  					}
					var tileAction = {};
					if((clusterTiles[i].tileType == 'small_text') || (clusterTiles[i].tileType == undefined)){
						if(clusterTiles[i].tileType == undefined){
							tileAction.actionName = clusterTiles[i].line1;
							tileAction.action =  clusterTiles[i].url;
							tileAction.data = tileId;
							tileAction.line3 = clusterTiles[i].line3;
							if(clusterTiles[i].line3 == null)
								clusterTiles[i].line3 = '';
						}else{
							tileAction.actionName = clusterTiles[i].data.line1;
							tileAction.action =  clusterTiles[i].url;
							tileAction.data = tileId;
							tileAction.line3 = clusterTiles[i].data.line3;
							if(clusterTiles[i].data.line3 == null)
								clusterTiles[i].data.line3 = '';
						}
					}
					switch(tileVar.tyleType){
						case 'smallTileOdd' :
							if(clusterTiles[i].tileType == undefined){
								tile.innerHTML += '<div class="item grid-1c2text '+status[clusterTiles[i].status]+'" id="'+idForTile+'-'+i+'" onclick="com.impetus.ankush.actionFunction_ClusterMonitoringTiles(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\', \''+tileAction.line3+'\');" style="left:'+tileVar.leftCss+'px;top:'+tileVar.topCss+'px;position:absolute;">'+
								'<div class="tile-1c2text thumbnail" onclick="">'+
								'<div class="'+colorClass[clusterTiles[i].status]+'">'+
								'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
								'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
							}
							else{
								tile.innerHTML += '<div class="item grid-1c2text '+status[clusterTiles[i].status]+'" id="'+idForTile+'-'+i+'" onclick="com.impetus.ankush.actionFunction_ClusterMonitoringTiles(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\', \''+tileAction.line3+'\');" style="left:'+tileVar.leftCss+'px;top:'+tileVar.topCss+'px;position:absolute;">'+
								'<div class="tile-1c2text thumbnail" onclick="">'+
								'<div class="'+colorClass[clusterTiles[i].status]+'">'+
								'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].data.line1+'" data-placement="bottom">'+clusterTiles[i].data.line1+'</div></div>'+
								'<div class="descTitle"><span>'+clusterTiles[i].data.line2+'</span><br/><span>'+clusterTiles[i].data.line3+'</span></div></div></div>';
							}
							if(clusterTiles[i+1] != undefined){
								if(clusterTiles[i+1].tileType == 'big_text'){
									tileVar.leftCss += 200; 
									tileVar.tyleType = 'bigTile';
								}
								else if((clusterTiles[i+1].tileType == 'small_text') || (clusterTiles[i+1].tileType == undefined)){
									tileVar.topCss += 100; 
									tileVar.tyleType = 'smallTileEven';
								}
							}
							break;
						case 'smallTileEven' :
							if(clusterTiles[i].tileType == undefined){
							tile.innerHTML += '<div class="item grid-1c2text '+status[clusterTiles[i].status]+'" id="'+idForTile+'-'+i+'" onclick="com.impetus.ankush.actionFunction_ClusterMonitoringTiles(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\', \''+tileAction.line3+'\');" style="left:'+tileVar.leftCss+'px;top:'+tileVar.topCss+'px;position:absolute;">'+
								'<div class="tile-1c2text thumbnail" onclick="">'+
								'<div class="'+colorClass[clusterTiles[i].status]+'">'+
								'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
								'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
							}
							else{
								tile.innerHTML += '<div class="item grid-1c2text '+status[clusterTiles[i].status]+'" id="'+idForTile+'-'+i+'" onclick="com.impetus.ankush.actionFunction_ClusterMonitoringTiles(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\', \''+tileAction.line3+'\');" style="left:'+tileVar.leftCss+'px;top:'+tileVar.topCss+'px;position:absolute;">'+
								'<div class="tile-1c2text thumbnail" onclick="">'+
								'<div class="'+colorClass[clusterTiles[i].status]+'">'+
								'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].data.line1+'" data-placement="bottom">'+clusterTiles[i].data.line1+'</div></div>'+
								'<div class="descTitle"><span>'+clusterTiles[i].data.line2+'</span><br/><span>'+clusterTiles[i].data.line3+'</span></div></div></div>';
							}
							if(clusterTiles[i+1] != undefined){
								if(clusterTiles[i+1].tileType == 'big_text'){
									tileVar.leftCss += 200; 
									tileVar.topCss -= 100;
									tileVar.tyleType = 'bigTile';
								}
								else if((clusterTiles[i+1].tileType == 'small_text') || (clusterTiles[i+1].tileType == undefined)){
									tileVar.topCss -= 100; 
									tileVar.leftCss += 200; 
									tileVar.tyleType = 'smallTileOdd';
								}
							}
							break;	
						case 'bigTile' :
							tile.innerHTML += '<div class="item grid-2c2text '+status[clusterTiles[i].status]+'" id="'+idForTile+'-'+i+'" onclick="com.impetus.ankush.actionFunction_ClusterMonitoringTiles();" style="left:'+tileVar.leftCss+'px;top:'+tileVar.topCss+'px;position:absolute;">'+
							'<div class="tile-2c2text thumbnail" onclick="">'+
							'<div class="'+colorClass[clusterTiles[i].status]+'">'+
							'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].data.header+'" data-placement="bottom">'+clusterTiles[i].data.header+'</div></div>'+
							'<div class="descTitle">'+linesFun()+'</div></div></div>';
							tileVar.leftCss += 200; 
							if(clusterTiles[i+1] != undefined){
								if((clusterTiles[i+1].tileType == 'small_text') || (clusterTiles[i+1].tileType == undefined)){
									tileVar.tyleType = 'smallTileOdd';
								}
							}
							break;	
					}
					if(tileAction.action != null) {
						var tileClickCursor = document.getElementById(idForTile+'-'+i);
						tileClickCursor.style.cursor = "pointer";
					}
  				},
  			//this function will inspect data node
  				inspectNodesCall : function(data,btnId,retrieveNodeBtn){
					if(Object.keys(data.nodePorts).length == 0){
						return ;
					}
  					$('#'+btnId).button();
					$('#'+btnId).button('loading');
  					var statusClass = {
  							'Critical' : 'error',
  							'Warning' : 'warning',
  							'Ok' : 'success'
  					};
  					$('#'+retrieveNodeBtn).attr('disabled',true);
  					inspectNodeUrl = baseUrl+'/cluster/validate';
  					com.impetus.ankush.placeAjaxCall(inspectNodeUrl,'POST',true,data,function(result){
  						$('#'+retrieveNodeBtn).attr('disabled',false);
  						inspectNodeData = result.output;
  						if(result.output.status == true){
	  						$('#'+btnId).button('reset');
	  						if(Object.keys(result.output).length != 0){
		  						$('.inspect-node-ok').each(function(){
		  							$(this).parent().parent().removeClass('error success warning');
		  							var ip = $(this).parent().next().text();
		  							$(this).parent().parent().addClass(statusClass[result.output[ip].status.status]);
		  							$(this).removeClass('inspect-node-ok');
		  						});
	  						}
  						}
  					});
  				},
  				//this will add autorefresh as and when page loads
  				//autorefresharray is array of object consisting autorefreshcallVariable,function and time
  				addAutorefreshCall : function(autoRefreshArray,key){
  					autoRefreshCallTestVariable++;
  					autoRefreshCallsObject[key] = [];
  					autoRefreshCallsObject[key] = autoRefreshArray;
  					for(var i = 0 ; i < autoRefreshCallsObject[key-1].length ; i++)
  						autoRefreshCallsObject[key-1][i].varName = window.clearInterval(autoRefreshCallsObject[key-1][i].varName);
  					for(var i = 0 ; i < autoRefreshCallsObject[key].length ; i++){
  						autoRefreshCallsObject[key][i].varName = setInterval(autoRefreshCallsObject[key][i].callFunction,autoRefreshCallsObject[key][i].time);
  					}
  					console.log(autoRefreshCallsObject);
  				},
  				//this function will remove autorefresh call of slider page which is removed
  				removeAutorefreshCall : function(key,parent){
  					for(var i = (Object.keys(autoRefreshCallsObject).length - 1) ; i >= key ; i--){
  						for(var j = 0 ; j < autoRefreshCallsObject[i].length ; j++){
  	  						autoRefreshCallsObject[i][j].varName = window.clearInterval(autoRefreshCallsObject[i][j].varName);
  						}
  						delete autoRefreshCallsObject[i];
  					}
  					if(parent != 'parent'){
	  					for(var i = 0 ; i < autoRefreshCallsObject[key-1].length ; i++){
	  						autoRefreshCallsObject[key-1][i].varName = setInterval(autoRefreshCallsObject[key-1][i].callFunction,autoRefreshCallsObject[key-1][i].time);
		  				}
  					}
  					console.log(autoRefreshCallsObject);
  				},
  				actionFunction_ClusterMonitoringTiles : function(name, key, data, line3) {
  					if (key == '' || key == null) {
  						return;
  					}
  					else {
  						if (typeof String.prototype.startsWith != 'function') {
  							// see below for better implementation!
  							String.prototype.startsWith = function (str){
  								return this.indexOf(str) == 0;
  							};
  						}
  						if ((key == 'Adding Nodes|progress') || (key == 'Adding Nodes|error')) {
  							com.impetus.ankush.commonMonitoring.loadAddNodeProgress();
  						} else if (key == 'Adding Nodes|success') {
  							var clusterDetailUrl = baseUrl + '/monitor/'+ currentClusterId + '/nodeprogress';
  							com.impetus.ankush.commonMonitoring.loadAddNodeProgress();
  						}
  						else if(key == 'Node List') {
  							com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/nodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
  						}
  						else if(key == 'Ecosystem') {
  							com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/configurations/hadoopEcosystem','get','Hadoop Ecosystem',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);','com.impetus.ankush.hadoopMonitoring.loadHadoopEcosystemInfo('+com.impetus.ankush.commonMonitoring.clusterId+')');
  						}
  						else if(key == 'Job Monitoring') {
  							com.impetus.ankush.hadoopMonitoring.jobMonitoringPage(com.impetus.ankush.commonMonitoring.clusterId);
  						}
  						else if(key.startsWith('NodeDrillDown|')) {
  							var nodeId = (key.split('|'))[1];
  							if(nodeId == null)
  								return;
  							var nodeIp = line3;
  							com.impetus.ankush.commonMonitoring.graphOnClick(nodeId, nodeIp);
  						}
  						else if(key == 'Keyspace') {
  							com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/keyspaces','get','Keyspaces',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
  						}
  						
  					}
  				},
				placeAjaxCall : function(url, method, async, data, callback, errorCallback) {
			    	var result = $.ajax({
                        'type' : method,
                        'url' : url,
                        'contentType' : 'application/json',
                        'dataType' : 'json',
                        'data' : JSON.stringify(data),
                        'async' : async,
                        'success' : function(data) {
                        	 //$("#content").hideLoading(); 
			    			if (callback)
			    				callback(data);
                         },
                         error : function(data) {
                        	if (errorCallback)
                        		errorCallback(data);
                         }
                    });
			    	result = $.parseJSON(result.responseText);
			    	return result;
			    },
			    validate : {
			    	numeric : function(data) {
			    		var patt= /^[\d]*$/ ;
			    		if(!patt.test($.trim(data))) {
			    			return false;
			    		}
			    		return true;
			    			
			    	},
			    	email:function(data){
			    		var patt=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
			    		if(!patt.test($.trim(data))) {
			    			return false;
			    		}
			    			return true;
			    	},
			    	between :function (x, min, max) {
			    		x = parseInt(x);
			    		min = parseInt(min);
			    		max = parseInt(max);
			    		return x >= min && x <= max;
			    	},
			    	alphaNumeric : function(data) {
			    		var patt= /^[\w]*$/ ;
			    		if(!patt.test($.trim(data))) {
			    			//alert("Only alpha numeric values are allowed for " + context + " ..!");
			    			return false;
			    		}
			    		return true;
			    			
			    	},
			    	aplhabets :function(data){
			    		var patt = /^[a-zA-Z]+$/;
			    		if(!patt.test($.trim(data))) {
			    			//alert("Only alphabets values are allowed for " + context + " ..!");
			    			return false;
			    		}
			    		return true;
			    	},
			    	username:function(data,context){
			    		var patt=/^[\w\.]+$/;
			    		if(!patt.test($.trim(data))) {
			    			//alert("Only alphabets,period and underscore values are allowed for " + context + " ..!");
			    			return false;
			    		}
			    		return true;
			    	},
			    	ipAddressOrHost : function(data){
			    		var patt= /^([a-zA-Z0-9_\.\-]*)$/ ;
			    		if(!patt.test($.trim(data))) {
			    			//alert("Special character not allowed for " + context + " ..!");
			    			return false;
			    		}
			    		return true;
			    	},
			    	email:function(data){
			    		var patt=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
			    		if(!patt.test($.trim(data))) {
			    			return false;
			    		}
			    			return true;
			    	},
			    	empty : function(data) {
			    		if($.trim(data).length == 0) {
			    			//alert("Enter " + context + " ..!");
			    			return false;
			    		}
			    		return true;
			    			
			    	},
			    	range : function(data1, lowerLimit, upperLimit) {
			    		if(!com.impetus.ankush.validate.between($.trim(data1), lowerLimit, upperLimit)) {
			    			return false;
			    		}
			    		return true;
			    	},
			    	oPort: function(data, context){
			    		if(!com.impetus.ankush.validate.numeric(data, context))
			    			return false;
			    		
			    		if(!com.impetus.ankush.validate.empty(data, context))
			    			return false;
			    		if(!com.impetus.ankush.validate.validPort(data, context))
			    			return false;
			    		if($.trim(data) <= 0) {
			    			alert("Enter " + context + " ..!");
			    			return false;
			    		}		    		
			    		if(!com.impetus.ankush.validate.range(data, 1025, 65535, context))
			    			return false;
			    		
			    		return true;	
			    	},
			    	validPort:function(data, context){
			    		if($.trim(data) < 1024) {
			    			alert(context + " must be greater than 1024..!");
			    			return false;
			    		}
			    		return true;
			    	},
			    	port : function(data) {
			    		if(!com.impetus.ankush.validate.range(data, 0, 65535))
			    			return false;
			    		
			    		return true;	
			    	},
			    	nameValidation:function(data){
			    		var firstChar=$.trim(data).charAt(0);
			    		var value=com.impetus.ankush.validate.numeric(firstChar);
			    		if(firstChar == '.')
			    			value = true;
			    		if(value==true)
			    			return false;
			    		else
			    			return true;
			    	},		    	
			    	stormSlotPort : function(data, context) {			    				    		
			    		var slotPortLength = 0;
			    		var slotPorts = null;
			    		var port1 = null;
			    		var port2 = null;
			    		slotPorts = data.split(",");
			    		slotPortLength = slotPorts.length;
			    		if(slotPortLength != 2)
			    		{
			    			alert("Enter comma separated port value for" + context + " ..!");
			    			return false;
			    		}
			    		port1 = slotPorts[0];
			    		port2 = slotPorts[1];
			    		if(!com.impetus.ankush.validate.port(port1, context))
			    			return false;
			    		if(!com.impetus.ankush.validate.port(port2, context))
			    			return false;	  
			    		return true;	
			    	},
			    	
			    	portCheck : function(regPort, adminPort, haPortRangeStart, haPortRangeEnd, context) {
			    		regPort = parseInt($.trim(regPort));
			    		adminPort = parseInt($.trim(adminPort));
			    		haPortRangeStart = parseInt($.trim(haPortRangeStart));
			    		haPortRangeEnd = parseInt($.trim(haPortRangeEnd));
			    		if(regPort == adminPort){
			    			alert("Registry Port and Admin Ports are same" + context + ".");
			    			return false;
			    		}
			    		if(com.impetus.ankush.validate.between(regPort, haPortRangeStart, haPortRangeEnd)){
			    			alert("Registry Port falls between HA Port Range" + context + ".");
			    			return false;
			    		}
			    		if(com.impetus.ankush.validate.between(adminPort, haPortRangeStart, haPortRangeEnd)){
			    			alert("Admin Port falls between HA Port Range" + context + ".");
			    			return false;
			    		}
			    		return true;
			    	},
			    	confirmPassword:function(pass1,pass2){
			    		if(pass1==pass2) {
			    			return true;
			    		}
			    		return false;
			    	},
			    	maxValue:function(data,maxLength){
			    		if($.trim(data).length<maxLength)
			    			return true;
			    		else
			    			return false;
			    	}
			    },
			    
			    blockUI : function(message, timeout){
					message = '<h1>'+(message || 'Please wait...')+'</h1>';
					timeout = timeout || 0;
					$.blockUI({
						css : {
							border : 'none',
							padding : '15px',
							backgroundColor : '#000',
							'-webkit-border-radius' : '10px',
							'-moz-border-radius' : '10px',
							opacity : .5,
							color : '#fff',

						},
						message : message,
						timeout : timeout
					});
				},
				unblockUI : function(){
					$(document).ajaxStop($.unblockUI); 
				},
				getMemory: function(memoryInBytes, memoryUnit) 
				{
					switch(memoryUnit)
					{
						case 0:	// Bytes
							return memoryInBytes;
						case 1: // KB
							return (memoryInBytes/(1024)).toFixed(2);
						case 2:	// MB
							return (memoryInBytes/(1024*1024)).toFixed(2);
						case 3: // GB
							return (memoryInBytes/(1024*1024*1024)).toFixed(2);
					}
				},
				decimal: function(number,dec){
					var result = Math.round(number*Math.pow(10,dec))/Math.pow(10,dec);
					return result;
				},
				camelCaseKey: function(key){
					 return key.split(/(?=[A-Z])/).map(
							function(p) {
								return p.charAt(0).toUpperCase()
										+ p.slice(1);
							}).join(' ');
				},
				getDateTime: function(txtDate){
					dateTime = 0;
					if($(txtDate).val().trim().length > 0) {
						var strDate = $(txtDate).val();
						strDate = strDate.split("-").join("/");
						dateTime = new Date(strDate).getTime();
					}
					return dateTime;
				},
				formattedDate:function(date){
					var formattedDate = $.format.date(date, "dd-MM-yyyy");
					return formattedDate;
				},
				closeDialog : function(dialogId){
					$("#"+dialogId).dialog('destroy');
				},
				addpanel : function(addpannel,type,newUrl){
					$('#content-panel').sectionSlider(addpannel, {
		    		    current : 'login-panel',
				        url : baseUrl+"/"+newUrl,
				        method : type,
		       			params : {
		       			}
		     		});
				},
				nodeRetrieve:function(userName,password,uploadFilePath,ipPattern,radioVal,uploadPathSharedKey,radioAuth,functionCall,buttonId,rackCheck,uploadRackFilePath,inspectNodeBtn){
					var nodeData = {};
					var clusterId=$('#clusterDeploy').val();
					if(clusterId !=''){
						nodeData.clusterId = clusterId;
					}
					$('#'+inspectNodeBtn).attr('disabled','disabled');
					nodeData.userName = userName;
					nodeData.password = password;
			        nodeData.isRackEnabled=false;
			    	nodeData.authTypePassword=true;
			        	 if(rackCheck==1){
			                 nodeData.isRackEnabled=true;
			                 nodeData.filePathRackMap = uploadRackFilePath;
			            }
		            	 if(radioAuth==1){
			                 nodeData.authTypePassword=false;
			                 nodeData.password = uploadPathSharedKey;
			            }
					if(radioVal==1){
						nodeData.isFileUploaded = true;
						nodeData.nodePattern=uploadFilePath;
						
					}else{
						nodeData.nodePattern =ipPattern;
						nodeData.isFileUploaded = false;
					}
					var nodeUrl = baseUrl + '/cluster/detectNodes';
					$.ajax({
						'type' : 'POST',
						'url' : nodeUrl,
						'contentType' : 'application/json',
						'data' : JSON.stringify(nodeData),
						'async' : true,
						'dataType' : 'json',
						'success' : function(result) {
							$('#'+inspectNodeBtn).removeAttr('disabled');
							$('#'+buttonId).button('reset');
							functionCall.call(this,result);
						},
						error : function(result) {
							$('#'+inspectNodeBtn).removeAttr('disabled');
							$('#'+buttonId).button('reset');
						}
					});
				},
				
				
			}
		
		},
};

com.impetus.ankush.login = {
	processLogin : function(elem) {
		$('#j_username').removeClass('error-box');
		$('#j_password').removeClass('error-box');
		if(!$(elem).valid())
			return false;
		$('#signInButton').button('loading');
		$.ajax({
            type:"POST",
            dataType:"text",
            beforeSend: function (request)
            {
                request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            },
            url: baseUrl + '/j_spring_security_check',
            data: "j_username=" + encodeURIComponent($('#j_username').val()) + "&j_password=" + encodeURIComponent($('#j_password').val()),
            processData: false,
            success: function(data) {
            	data = JSON.parse(data);
            	if(data.success == true) {
            		if(data.target != undefined){
            			configureState = data.target; 
            			if(data.target == 'configure')
            				$('#content-panel').sectionSlider('addRootConfigPanel');
            		}
            		else
            			test();
            	}
            	else {
            		if(data.reason == 'AccountDisabled') {
            			$('#login-error').html('<error for="j_password" generated="true" class="error">Your account is disabled. Please contact Ankush administrator.</error>');
	            		$('#j_username').addClass('error-box');
	            		$('#j_password').addClass('error-box');
	            		$('#j_username').val('');
	            		$('#j_password').val('');
	            		$('#signInButton').button('reset');
            		}
            		else if(data.reason == 'InvalidCredentials'){
	            		$('#login-error').html('<error for="j_password" generated="true" class="error">The user ID or password you entered is incorrect.</error>');
	            		$('#j_username').addClass('error-box');
	            		$('#j_password').addClass('error-box');
	            		$('#j_username').val('');
	            		$('#j_password').val('');
	            		$('#signInButton').button('reset');
            		}
            	}
            	
            },
			error : function(data) {
				$('#login-error').html('<error generated="true" class="error">We are unable to process your request, Please try again later.</error>');
        		$('#signInButton').button('reset');
			}
		});
		return false;
	},
	getPassword : function() {
		var url = null;
		var data = {};
		if($('#get_user_id').is(':checked')) {
			console.log($("#emailTextBox").val());
			if(!com.impetus.ankush.validate.empty($("#emailTextBox").val())){
				$('#email-error').html('<error generated="true" class="error">Enter your Email</error>');
				return;
			}
			data={
					"mail": ($('#emailTextBox').val())
			};
			url = baseUrl + '/user/forgotuserid';
		}else if($('#get_passwd').is(':checked')){
			if(!com.impetus.ankush.validate.empty($("#userIdTextBox").val())){
				$('#UserId-error').html('<error generated="true" class="error">Enter your user ID</error>');
				return;
			}	
			userId = $('#userIdTextBox').val();
			data={
					 'username' : userId 
			 };
			url = baseUrl + '/user/forgotpassword';
		}else{
			return;
		}
		$.ajax({
            type:"POST",
			contentType : 'application/json',
            dataType:"json",
            url: url,
            data: JSON.stringify(data),
            success: function(data) {
            	goToLogin();
        		
            },
            error:function (xhr){
                switch (xhr.status) {
                        case 400: {
                    	var jsonObject = JSON.parse( xhr.responseText );
                    	if($('#get_user_id').is(':checked')) 
                    		$('#email-error').html('<error generated="true" class="error">'+jsonObject.message+'</error>');
                    	else
                    		$('#UserId-error').html('<error generated="true" class="error">'+jsonObject.message+'</error>');
                    	};
                }
            } 
		});
		return false;
	},
	signOut : function(){
		url = baseUrl + '/auth/logout';
		$.ajax({
            type:"GET",
			contentType : 'application/json',
            dataType:"json",
            url: url,
            success: function(data) {
            	if(data.success == true) {
            		url = baseUrl + '/auth/login';
            		$(location).attr('href',url);
            	}
            	else {
            		
            	}
        		
            },
           error : function(data) {
        	
			}
		});
		return false;
		
	}
};

$(document).ready(function() { 
	///alert("test");
	$(document).bind('ajaxError',function(event,request,settings){
		// if status is 200 in error event that means page is redirected to some valid page
	    if (request.status == 200){
	       window.location = baseUrl;
	    };
	});
	// all three events tab event , enter and click  event for editable data
	/*$('input.input-medium').live("change", function(){
        var editedValue = $( this ).val();
        $(this).parents('td').find('a.editable').text(editedValue);
           $('a.editable').click(function(){
               var thisText = $(this).text();
               $(this).parents('td').find('input.input-medium').val(thisText);
       });
           
	
  });
	$('input.input-medium').live("blur", function(){
        $(this).hide();
        $(this).parents('td').find('a.editable').show();
           // set update value in hidden field
           var statusTxt = $(this).parents('tr').find("a.status").text();
           //alert(statusTxt);
	       if(statusTxt == 'Add'){}  
	       else if (statusTxt == 'None'){
	   var cdx =    $(this).parents('tr').find("a.status").text();
	  // alert(cdx);
	       }
	       var paramStatusTxt = $(this).parents('tr').find("a.delete").text();
	       if (paramStatusTxt == 'none'){ 
		       $(this).parents('tr').find("a.delete").text('edit');
		     }
	       
	       
    });
	 $('input.input-medium').live("keypress", function(e){
		    if (e.keyCode == '13') {
		        $(this).hide();
		        $(this).parents('td').find('a.editable').show();
		        }
		    
	});             */
    com.impetus.ankush.init();
});
Array.prototype.remove = function() {
    var what, a = arguments, L = a.length, ax;
    while (L && this.length) {
        what = a[--L];
        while ((ax = this.indexOf(what)) !== -1) {
            this.splice(ax, 1);
        }
    }
    return this;
};
