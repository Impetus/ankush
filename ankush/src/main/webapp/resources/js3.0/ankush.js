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
var idPattern = [];
var intervalId =[];
var com = {
		impetus : {
			ankush : {
				tooltip:{},
				init : function(){},
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
  				
 				//this function will place order of tiles in order of red, yellow, green
 				tileReordring : function(data){
 					if(data == undefined)
 						return;
 					var mainArray = [];
 					var arrayForWarning = [];
 					var arrayForNormal = [];
 					for(var i = 0 ; i < data.length ; i++){
 						switch(data[i].status){
 							case 'ERROR' : 
 								mainArray.push(data[i]);
 								break;
 							case 'CRITICAL' : 
 								mainArray.push(data[i]);
 								break;
 							case 'WARNING' :
 								arrayForWarning.push(data[i]);
 								break;
 							case 'NORMAL' :	
 								arrayForNormal.push(data[i]);
 								break;
 						}
 					}
 					for(var i = 0 ; i < arrayForWarning.length ; i++)
 						mainArray.push(arrayForWarning[i]);
 					for(var i = 0 ; i < arrayForNormal.length ; i++)
 						mainArray.push(arrayForNormal[i]);
 				 return com.impetus.ankush.orderEvenSmallTiles(mainArray);
 				},
 				//this function will order even no of small tiles before big tiles
 				orderEvenSmallTiles : function(tileData){
 					var deciMake = 0;
 					var mainArray = [];
 					var tempArray = [];
 					for(var i = 0 ; i < tileData.length ; i++){
 						if(tileData[i].tileType != 'big_text'){
 							mainArray.push(tileData[i]);
 							if(deciMake == 0)
 								deciMake = 1;
 							else
 								deciMake = 0;
 							if((tempArray.length != 0) && (deciMake == 0)){
 								for(var j = 0 ; j < tempArray.length ; j++)
 									mainArray.push(tempArray[j]);
 								tempArray = [];
 							}
 						}else{
 							if(deciMake == 1){
 								tempArray.push(tileData[i]);
 							}else{
 								mainArray.push(tileData[i]);
 							}
 						}
 					}
 					for(var j = 0 ; j < tempArray.length ; j++)
						mainArray.push(tempArray[j]);
 					return mainArray;
 				},
 				//this function will change % width in pixels of a div @Zubair
 				changeWidth : function(id,pixel){
 		        	var widthInPx = $('#'+id).width();
 		        	$('#'+id).css('width', (widthInPx+pixel)+'px');
 		        },
  				//this will checked unchecked all check boxes of data table on header check box click @Zubair
  				checked_unchecked_all : function(className,headerElement){
  					if($(headerElement).is(':checked')){
  						$('.'+className).each(function(){
  							if(!$(this).is(":disabled"))
  								$(this).prop('checked',true)
  						});
  					}
  					else
  						$('.'+className).prop('checked',false);
  				},
  				//this will checked unchecked all checkboxes of data table on header checkbox click @Zubair
  				headerCheckedOrNot : function(className,headerId){
  					var flag = true;
  					$('.'+className).each(function(){
  						if(!$(this).is(':checked')){
  							flag = false;
  							return;
  						}
  					});
  					if(flag === true){
  						$('#'+headerId).prop('checked',true);
  					}else{
  						$('#'+headerId).prop('checked',false);
  					}
  				},
  				//this will populate jquery jstree @Zubair
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
  				//this will expand jquery jstree @Zubair
  				jstreeExpandAll : function(id){
  					$('#'+id).jstree('open_all');
  					com.impetus.ankush.nodeUtilizationTrend.changeBorder();
  				},
  				//this will collapse jquery jstree @Zubair
  				jstreeCollapseAll : function(id){
  					$('#'+id).jstree('close_all');
  					com.impetus.ankush.nodeUtilizationTrend.changeBorder();
  				},
  			//this will create common tiles for common monitoring page
  		        createCommonTiles : function(lastUrl){
  		        	if(lastUrl === undefined){
  		        		lastUrl = '';
  		        	}
  		        	
  		        	$('#tilesAnkush').parent().show();
  		        	var tileUrl = baseUrl + '/monitor/' + clusterId + '/alerts'+lastUrl;
  		        	com.impetus.ankush.placeAjaxCall(tileUrl, "GET", true, null, function(result){
  		        		$("#tilesAnkush").empty();
  		        		var events = com.impetus.ankush.clubSameTile(result.output.events);
  		        		for(var key in events){
  		        			if(events.hasOwnProperty(key)){
  		        				var borderClass = 'border-danger text-danger';
  		        				var borderColor = '#a94442';
  		        				if(events[key].status === com.impetus.ankush.constants.stateWarning){
  		        					borderClass = 'border-warning text-warning';
  		        					borderColor = '#8a6d3b';
  		        				}
  		        				var obj = {};
  		        				obj.borderClass = borderClass;
  		        				obj.borderColor = borderColor;
  		        				obj.name = events[key].name;
  		        				obj.value = events[key].value;
  		        				obj.iconClass = 'fa-bar-chart-o';
  		        				obj.sizeClass = 'col-md-3';
  		        				if(jspPage === "monitoringPage"){
  		        					obj.sizeClass = 'col-md-4';
  		        				}
  		        				if(events[key].type === 'SERVICE'){
  		        					obj.iconClass = 'fa-gears';
  		        				}
  		        				if(events[key].count > 1){
	        							obj.value += ' '+events[key].count+' Nodes';
	        							obj.url = baseUrl+'/commonMonitoring/'+clusterName+'/nodes/C-D/'+clusterId+'/'+clusterTechnology;
  		        				}
	        					else if(events[key].count === 1){
	        							obj.value += ' '+events[key].host[0];
	        							obj.url = baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+events[key].host[0];
	        					}
  		        				$("#tilesAnkush").append(com.impetus.ankush.tileTemplate(obj));
  		        			}
  		        		}
  		        	});
  		           
  		        },
  		        //this will change alert object to tile object
  		        tileTemplate : function(obj){
  		        	var template = '<div class="'+obj.sizeClass+' mrgb10"><a href="'+obj.url+'" style="text-decoration:none;">';
  				    template += '<div class="progressTile '+obj.borderClass+'">';
  				    template += '<div class="progressTile-body">';
  				    template += '<div class="current-stats" id="'+obj.id+'">';
  				    template += '<h4>'+obj.name+'</h4>';
  				    template += '<p>'+obj.value+'</p>';
  				    template += '<div class="type" style="border-right:1px solid '+obj.borderColor+'">';
  				    template += '<span class="fa '+obj.iconClass+'" data-icon="" style="font-size:35px;" aria-hidden="true"></span>';
  				    template += '</div>';
  				    template += '</div>';
  				    template += '</div>';
				    template += '</div>';
				    template += '</div>';
  				    template += '</div>';
  				    template += '</a></div>';
  					return template;
  		        },
  				//this will change alert object to tile object
  				createTileObject : function(json){
  					if(json.output.status){
  						var tiles = com.impetus.ankush.convertAlertToTile(json.output.events);
  						delete json.output.events;
  						json.output.tiles = {};
  						json.output.tiles = tiles;
  					}
  					return json;
  				},
  				//this function will increment count for same tile
  				clubSameTile : function(events){
  					var orderBy = '';
  					var tiles = [];
  					var count = 1;
  					index = 0;
  					for(var i = 0 ; i < events.length ; i++){
  						var tempTile = {};
  						tempTile.host = [];
  						if(i === 0){
  							tempTile.name = events[i].name;
  	  						tempTile.value = events[i].type == 'SERVICE'?events[i].value:events[i].thresholdValue;
  	  						if(events[i].type === 'USAGE'){
  	  							tempTile.value = Math.floor(tempTile.value)+"% above";
  	  						}
  	  						if(tempTile.value === null){
  	  							tempTile.value = '';
  	  						}
  	  						tempTile.count = count;
  	  						tempTile.type = events[i].type;
  	  						tempTile.category = events[i].category;
  	  						tempTile.status = events[i].severity;
  	  						tempTile.host.push(events[i].host);
  	  						tiles.push(tempTile);
  						}else{
  							orderBy = events[i].category+''+events[i].name+''+events[i].severity;
  							var orderByTile = tiles[index].category+''+tiles[index].name+''+tiles[index].status;
  							if(orderBy == orderByTile){
  								tiles[index].count = tiles[index].count+1;
  								tiles[index].host.push(events[i].host[0]);
  							}else{
  								tempTile.name = events[i].name;
  	  	  						tempTile.value = events[i].type == 'SERVICE'?events[i].value:events[i].thresholdValue;
	  	  	  					if(events[i].type === 'USAGE'){
	  	  							tempTile.value = Math.floor(tempTile.value)+"% above";
	  	  						}
	  	  	  					if(tempTile.value === null){
	  	  							tempTile.value = '';
	  	  						}
	  	  	  					tempTile.type = events[i].type;
	  	  	  					tempTile.count = count;
	  	  	  					tempTile.category = events[i].category;
  	  	  						tempTile.status = events[i].severity;
  	  	  						tempTile.host.push(events[i].host);
  	  	  						tiles.push(tempTile);
  	  	  						index++;
  							}
  						}
  					}
  					return tiles;
  				},
  				convertAlertToTile : function(alertArray){
  					var orderBy = '';
  					var tiles = [];
  					var count = 1;
  					index = 0;
  					for(var i = 0 ; i < alertArray.length ; i++){
  						var tempTile = {};
  						if(i === 0){
  							tempTile.line1 = alertArray[i].name;
  	  						tempTile.line2 = alertArray[i].type == 'SERVICE'?alertArray[i].value:alertArray[i].thresholdValue;
  	  						if(alertArray[i].type === 'USAGE'){
  	  							tempTile.line2 = Math.floor(tempTile.line2)+"% above";
  	  						}
  	  						if(tempTile.line2 === null){
  	  							tempTile.line2 = '';
  	  						}
  	  						tempTile.line3 = count;
  	  						tempTile.line4 = alertArray[i].category;
  	  						tempTile.status = alertArray[i].severity;
  	  						tiles.push(tempTile);
  						}else{
  							orderBy = alertArray[i].category+''+alertArray[i].name+''+alertArray[i].severity;
  							var orderByTile = tiles[index].line4+''+tiles[index].line1+''+tiles[index].status;
  							if(orderBy == orderByTile){
  								tiles[index].line3 = tiles[index].line3+1;
  							}else{
  								tempTile.line1 = alertArray[i].name;
  	  	  						tempTile.line2 = alertArray[i].type == 'SERVICE'?alertArray[i].value:alertArray[i].thresholdValue;
	  	  	  					if(alertArray[i].type === 'USAGE'){
	  	  							tempTile.line2 = Math.floor(tempTile.line2)+"% above";
	  	  						}
	  	  	  					if(tempTile.line2 === null){
	  	  							tempTile.line2 = '';
	  	  						}
	  	  	  					tempTile.line3 = count;
	  	  	  					tempTile.line4 = alertArray[i].category;
  	  	  						tempTile.status = alertArray[i].severity
  	  	  						tiles.push(tempTile);
  	  	  						index++;
  							}
  						}
  					}
  					return tiles;
  				},
  				 //this will create common tiles for common monitoring page @zubair
  		        ankushTiles : function(currentClusterId){
  		        	$('#tileSection').append('<div class="masonry col-md-12" id="tilesAnkush"></div>');
  		        	if(undefined == currentClusterId)
  		                return;
  		            var tileUrl = baseUrl + '/monitor/' + currentClusterId + '/clustertiles';
  		            if((hybridTechnology !== undefined) && (hybridTechnology !== ''))
  		            	tileUrl = baseUrl + '/monitor/' + currentClusterId + '/technologyTiles?component='+hybridTechnology;
  		            var clusterTiles = {};
  		            com.impetus.ankush.placeAjaxCall(tileUrl, "GET", true, null, function(tileData){
  		            	
  		            	//this object variable will set css passing by refrence
  		                var tileVar = {
  		                        'leftCss' : 0,
  		                        'topCss' : 0,
  		                        'lineCounter' : 1,
  		                        'tyleType' : 'bigTile',
  		                };
  		                $('#tilesAnkush').empty();
  		                if(tileData.output.status == false){
  		                	$('#tilesAnkush').parent().hide();
  		                    return ;
  		                }
  		                else{
  		                	if((tileData.output.tiles == null) || (tileData.output.tiles.length == 0)){
  		                		$('#tilesAnkush').parent().hide();
  		                		return;
  		                	}
  		                    clusterTiles = com.impetus.ankush.tileReordring(tileData.output.tiles);
  		                }
  		                if(clusterTiles === null || clusterTiles.length === 0){
  		                	$('#tilesAnkush').parent().hide();
  		                	return;
  		                }
  		                $('#ankushTiles').parent().show();
  		                //this var will set whether tile is 2*2 or 1*2
  		                var tileFlag = false;
  		                for(var j = 0 ; j < clusterTiles.length ; j++){
  		                    if(clusterTiles[j].tileType == 'big_text'){
  		                        tileFlag = true;
  		                        break;
  		                    }
  		                }
  		                var tile = document.getElementById('tilesAnkush');
  		                for(var i = 0 ; i < clusterTiles.length ; i++){
  		                    if((tileVar.leftCss+200) > ($('#tilesAnkush').width())){
  		                        tileVar.leftCss = 0;
  		                        tileVar.topCss = tileVar.lineCounter*200;
  		                        tileVar.lineCounter++;
  		                    }
  		                    // && ((clusterTiles[i].tileType == 'small_text') && (clusterTiles[i].tileType == undefined))
  		                    if((i == 0) && ((clusterTiles[i].tileType == 'small_text') || (clusterTiles[i].tileType == undefined)))
  		                        tileVar.tyleType = 'smallTileOdd';
  		                    com.impetus.ankush.createTyleUsingType(tileVar,i,clusterTiles,tile,'common-id-for-tile');
  		                }
  		                if(tileFlag == false){
  		                    $('#tilesAnkush').masonry('destroy');
  		                    $('#tilesAnkush').masonry({
  		                        itemSelector : '.item',
  		                        columnWidth : 100,
  		                        isAnimated : true
  		                    });
  		                }else
  		                    setTimeout(function(){$('#tilesAnkush').css('height',(tileVar.lineCounter)*200+'px');},50);
  		                $('.clip').tooltip();
  		            });
  		           
  		        },
  				//tile creation tileVar is an object(pass by refrence to change css for tiles) i is index of tile, tile is html element of parent div anidForTile is id @Zubair
  				createTyleUsingType : function(tileVar,i,clusterTiles,tile,idForTile){
  					if(tile == null)
  						return;
  					var linesFun = function(){
  					var lineString = '';
  					if((Object.keys(clusterTiles[i].data).length != 0) && (clusterTiles[i].data != null)){
  						for(var lineIndex = 0 ; lineIndex < clusterTiles[i].data.tileLines.length ; lineIndex++){
  								lineString += '<div class="row-fluid"><div class="text-left span6" style="word-wrap:break-word;">'+clusterTiles[i].data.tileLines[lineIndex].text[0]+'</div><div class="text-left span6" style="word-wrap:break-word;">'+clusterTiles[i].data.tileLines[lineIndex].text[1]+'</div></div>';
  						}
					return 	lineString;
  					};
  					};
  					var status = {
  							'ERROR' : 'errorbox',
  							'CRITICAL' : 'errorbox',
  							'WARNING' : 'warningbox',
  							'NORMAL' : 'infobox'
  					};
  					var colorClass = {
  							'ERROR' : 'redTitle',
  							'CRITICAL' : 'redTitle',
  							'WARNING' : 'yellowTitle',
  							'NORMAL' : 'greenTitle'
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
								'<div class="descTitle"><span>'+(clusterTiles[i].line2).split('Hybrid').join('Delve')+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
							}
							else{
								tile.innerHTML += '<div class="item grid-1c2text '+status[clusterTiles[i].status]+'" id="'+idForTile+'-'+i+'" onclick="com.impetus.ankush.actionFunction_ClusterMonitoringTiles(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\', \''+tileAction.line3+'\');" style="left:'+tileVar.leftCss+'px;top:'+tileVar.topCss+'px;position:absolute;">'+
								'<div class="tile-1c2text thumbnail" onclick="">'+
								'<div class="'+colorClass[clusterTiles[i].status]+'">'+
								'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].data.line1+'" data-placement="bottom">'+clusterTiles[i].data.line1+'</div></div>'+
								'<div class="descTitle"><span>'+(clusterTiles[i].line2).split('Hybrid').join('Delve')+'</span><br/><span>'+clusterTiles[i].data.line3+'</span></div></div></div>';
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
								'<div class="descTitle"><span>'+(clusterTiles[i].line2).split('Hybrid').join('Delve')+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
							}
							else{
								tile.innerHTML += '<div class="item grid-1c2text '+status[clusterTiles[i].status]+'" id="'+idForTile+'-'+i+'" onclick="com.impetus.ankush.actionFunction_ClusterMonitoringTiles(\''+tileAction.actionName+'\', \''+tileAction.action+'\',\''+tileAction.data+'\', \''+tileAction.line3+'\');" style="left:'+tileVar.leftCss+'px;top:'+tileVar.topCss+'px;position:absolute;">'+
								'<div class="tile-1c2text thumbnail" onclick="">'+
								'<div class="'+colorClass[clusterTiles[i].status]+'">'+
								'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].data.line1+'" data-placement="bottom">'+clusterTiles[i].data.line1+'</div></div>'+
								'<div class="descTitle"><span>'+(clusterTiles[i].line2).split('Hybrid').join('Delve')+'</span><br/><span>'+clusterTiles[i].data.line3+'</span></div></div></div>';
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
  			//this function will initialize all types of datatable @Zubair
  				datatableInitialize : function(tableId,newOptions){
  					var preOptions = {
  							"bJQueryUI" : false,
  							"bPaginate" : false,
  							"bLengthChange" : true,
  							"bFilter" : true,
  							"bSort" : false,
  							"bInfo" : false,
  							"bAutoWidth" : false,
  							"sPaginationType" : "full_numbers",
  							"bAutoWidth" : false,
  							"bRetrieve" : true,
  							"bRetrieve" : true,
  							"aaSorting": [[0,'asc']],
  							"oLanguage": {
  								"sEmptyTable": 'Loading...',
  						    }
  					  };
  					$.extend(preOptions, newOptions);
  					if(("#"+tableId).dataTable == null){
  						$("#"+tableId).dataTable(preOptions);
  						$("#"+tableId+"_filter").css({
  							'display' : 'none'
  						});
  						$('#'+tableId+'Search').keyup(function() {
  							$("#"+tableId).dataTable().fnFilter($(this).val());
  						});
  					}else{
  						$("#"+tableId).dataTable().fnClearTable();
  					}
  					
  				},
  				//this will convert sentence case to camel case   @zubair
  				toCamelCase : function(sentenceCase){
  					var convertedCase = "";
  				    sentenceCase.split(" ").forEach(function (el, idx) {
  				      var add = el.toLowerCase();
  				      convertedCase += (idx === 0 ? add : add[0].toUpperCase() + add.slice(1));
  				    });
  				    return convertedCase;
  				},
  				//this will convert camel case to sentence case   @zubair
  				toSentenceCase : function(camelCase){
  					camelCase = camelCase.replace( /([A-Z])/g, " $1" );
  					camelCase = camelCase.charAt(0).toUpperCase()+camelCase.slice(1);
  					return camelCase;
  				},
  			//this function will inspect data node
  				inspectNodesCall : function(data,btnId,retrieveNodeBtn){
					if(Object.keys(data.nodePorts).length == 0){
						return ;
					}
					var l = Ladda.create( document.querySelector( '#'+btnId ) );
				 	l.start();
				 //	$('#'+btnId).button();
  					/*
					$('#'+btnId).button('loading');*/
  					var statusClass = {
  							'Critical' : 'alert-danger',
  							'Warning' : 'alert-warning',
  							'Ok' : 'alert-success'
  					};
  					$('#'+retrieveNodeBtn).attr('disabled',true);
  					inspectNodeUrl = baseUrl+'/cluster/validate';
  					com.impetus.ankush.placeAjaxCall(inspectNodeUrl,'POST',true,data,function(result){
  						$('#'+retrieveNodeBtn).attr('disabled',false);
  						inspectNodeData = result.output;
  						if(result.output.status == true){
	  					/*	$('#'+btnId).button('reset');*/
  							l.stop();
  						//	$("#"+tableName).dataTable().fnSetColumnVis(colIndex,true);
	  						if(Object.keys(result.output).length != 0){
		  						$('.inspect-node-ok').each(function(){
		  							$(this).parent().parent().find('td').removeClass('alert-danger alert-success alert-warning');
		  							$(this).parent().parent().removeClass('alert-danger alert-success alert-warning');
		  							var ip = $(this).parent().next().text();
		  							$(this).parent().parent().find('td').addClass(statusClass[result.output[ip].status.status]);
		  							$(this).removeClass('inspect-node-ok');
		  						});
	  						}
  						}
  					});
  				},
  				createAutorefresh : function(arrayOfAutorefreshFunction){
  					defaults = {
  							time : 30000,
  							varName : "is_autorefresh_"+jspPage
  					};
  					for(var i = 0 ; i < arrayOfAutorefreshFunction.length ; i++){
  						$.extend(arrayOfAutorefreshFunction[i], defaults);
  						autorefreshArray.push(arrayOfAutorefreshFunction[i]);
  					}
  					com.impetus.ankush.autorefreshHandler(autorefreshArray);
  				},
  				//this function will start all autorefresh call @zubair
  				autorefreshHandler : function(autorefreshArray){
  					for(var i = 0 ; i < autorefreshArray.length ; i++){
  						autorefreshArray[i].varName = setInterval(autorefreshArray[i].method,autorefreshArray[i].time);
  					}
  				},
  				//this function will remove all autorefresh call @zubair
  				autorefreshremover : function(autorefreshArray){
  					for(var i = 0 ; i < autorefreshArray.length ; i++){
  						autorefreshArray[i].varName = window.clearInterval(autorefreshArray[i].varName);
  					}
  				},
  				//this will add autorefresh as and when page loads
  				//autorefresharray is array of object consisting autorefreshcallVariable,function and time
  				//for Slider @Zubair
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
  				// for Slider @Zubair
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
  				},
  				//this function will be called on slide out
  				previousCallbackCall : function(key){
  					var objLength = (Object.keys(prePagePopulateCallsObject).length);
  					for(var i = 0 ; i < prePagePopulateCallsObject[key].length ; i++)
  						eval(prePagePopulateCallsObject[key][i]);
  					for(var j = key ; j < (objLength -1) ; j++){
  						delete prePagePopulateCallsObject[j+1];
  					}
  					console.log(prePagePopulateCallsObject);
  				},
  				//add previous call back function
  				addPreviousCallbackCall : function(arrayOfFunction){
  					prePagePopulateCallsTestvar++;
  					prePagePopulateCallsObject[$.data(document, "panels").children.length] = arrayOfFunction;
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
  						else if(key == 'AutoProvision') {
  							com.impetus.ankush.hadoopMonitoring.configurationAutoProvision(com.impetus.ankush.commonMonitoring.clusterId);
  						}
  						else if(key == 'Keyspace') {
  							com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/keyspaces','get','Keyspaces',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
  						}
  						
  					}
  				},
  				isHadoop2 : function(clusterId){
  					var flag = false;
  					var url = baseUrl+'/monitor/'+clusterId+'/hadoopversion?component=Hadoop';
  					com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
  						if(result.output.isHadoop2){
  							flag = true;
  						}
  					});
  					return flag;
  				},
				placeAjaxCall : function(url, method, async, data, callback, errorCallback) {
					$.ajax({
                        'type' : method,
                        'url' : url,
                        'contentType' : 'application/json',
                        'datatype' : 'json',
                        'data' : JSON.stringify(data),
                        'async' : async,
                        'success' : function(data) {
                        	
                        	if (callback)
			    				callback(data);
                         },
                         error: function (responseData) {
                        	 if (errorCallback)
                         		errorCallback(data);
                         }
                    });

			    	/*result = $.parseJSON(result.responseText);
			    	return result;*/
			    },
			    validate : {
			    	numeric : function(data) {
			    		var patt= /^[\d]*$/ ;
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
			    		var patt=/^[_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*(\.[a-zA-Z]{2,4})$/;
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
			    	positive : function(data){
			    		if(!(+$.trim(data) > 0))
			    			return false;
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
					com.impetus.ankush.placeAjaxCall(nodeUrl,'POST',true,nodeData,function(result){
						
							$('#'+inspectNodeBtn).removeAttr('disabled');
							$('#'+buttonId).button('reset');
							functionCall.call(this,result);
					},function(){
						$('#'+inspectNodeBtn).removeAttr('disabled');
						$('#'+buttonId).button('reset');
					});
				},
				
				
			}
		
		},
};

com.impetus.ankush.login = {
	processLogin : function(elem) {
		$('.formv-group').removeClass('has-error');
		/*if(!$(elem).valid())
			return false;*/
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
            			if(data.target === 'configure'){
            				$(location).attr('href',(baseUrl + '/dashboard/configuration/C-D/configure'));
            			}
            			if(data.target === 'changepassword'){
            				$(location).attr('href',(baseUrl + '/dashboard/changePassword/C-D/enforce-password'));
            			}
            		}
            		else{
            			$(location).attr('href',(baseUrl + '/dashboard'));
            		}
            	}
            	else {
            		if(data.reason == 'AccountDisabled') {
            			$('#login-error').html('<error for="j_password" generated="true" class="">Your account is disabled. Please contact Ankush administrator.</error>');
	            		$('.formv-group').addClass('has-error')
	            		$('#j_username').val('');
	            		$('#j_password').val('');
	            		$('#signInButton').button('reset');
            		}
            		else if(data.reason == 'InvalidCredentials'){
	            		$('#login-error').html('<error for="j_password" generated="true" class="">The user ID or password you entered is incorrect.</error>');
	            		$('.formv-group').addClass('has-error')
	            		$('#j_username').val('');
	            		$('#j_password').val('');
	            		$('#signInButton').button('reset');
            		}
            	}
            	
            },
			error : function(data) {
				$('#login-error').html('<error generated="true" class="alert fade in">We are unable to process your request, Please try again later.</error>');
        		$('#signInButton').button('reset');
			}
		});
		return false;
	},
	getPassword : function() {
		var url = null;
		var data = {};
		if($('#get_user_id').is(':checked')) {
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
		com.impetus.ankush.placeAjaxCall(url,'POST',true,data,function(data){
            	goToLogin();
        },function(xhr){
			switch (xhr.errorId) {
            case "400": {
            if($('#get_user_id').is(':checked')) 
        		$('#email-error').html('<error generated="true" class="error">'+xhr.message+'</error>');
        	else
        		$('#UserId-error').html('<error generated="true" class="error">'+xhr.message+'</error>');
        	};
    }
		});
		return false;
	},
	signOut : function(){
		url = baseUrl + '/auth/logout';
		com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(data){
            	if(data.success == true) {
            		var url1 = baseUrl + '/auth/login';
            		$(location).attr('href',url1);
            	}
            	else {
            		
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
