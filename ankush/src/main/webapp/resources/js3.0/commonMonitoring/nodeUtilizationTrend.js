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
var chartObj = {};
var patternDataForGraphs = null;
		//var graphCounter = 0;
		var is_autoRefresh_nodeUtilizationTrend = null;
		var graphLoadedCounter=0;
		com.impetus.ankush.nodeUtilizationTrend = {
				//this function will hit rest url for jstree
				jstreeCall : function(){
					var jstreeUrl = baseUrl + '/monitor/' + clusterId + '/graphtree?ip='+hostName;
					com.impetus.ankush.placeAjaxCall(jstreeUrl, "GET", true, null, function(jstreeData){
						if(jstreeData.output.status){
							com.impetus.ankush.jstreePopulate("jstreeNodeGraph",jstreeData.output.tree);
							 $("#jstreeNodeGraph").jstree({
									// the `plugins` array allows you to configure the active plugins on this instance
									"plugins" : ["themes","html_data","ui","checkbox"],
									// each plugin you have included can have its own config object
									"core" : {
										"animation" : 100,
										//"initially_open" : [ "phtml_1" ]
									},
									// set a theme
									"themes" : {
										"icons" : false,
							            "theme" : "proton",
							            "dots" : true
							        },
							    });
							 $('#jstreeNodeGraph').show(500);
							 setTimeout(function(){
								 $("#nodeUtilizationContainer").removeClass('loadingDiv');
								 com.impetus.ankush.nodeUtilizationTrend.changeBorder();
								 if($("#nodeUtilizationTrendGraphsDivs").is(':empty')){
										$("#nodeUtilizationTrendGraphsDivs").append("<div style='margin-left:10px;font-size:16px;'id='emptyGraphsDiv'>No Graph Selected.</div>");
									}
									else{
										$("#emptyGraphsDiv").remove();
									}
							},500);
							 
						}else{
							com.impetus.ankush.validation.showAjaxCallErrors(jstreeData.output.error,'popover-content', 'error-div', '');
							$("#nodeUtilizationContainer").removeClass('loadingDiv');
						}
					});
				},
				//This function will make graph boxes using legends array from back-end call
				getGraphUsingAjax : function(clusterId,nodeIp,startTime,pattern,checkAutorefresh){
					$("#showLoading").show();
					var graphUrl = baseUrl + '/monitor/'+clusterId+'/legends?ip='+nodeIp+'&pattern='+pattern;
					com.impetus.ankush.placeAjaxCall(graphUrl,'GET',true,null,function(graphResult){
						if(graphResult.output.status == false){
							$("#showLoading").hide();
							return;
						}
						var jsonArray =  graphResult.output.legends;
						com.impetus.ankush.nodeUtilizationTrend.makeBoxes(jsonArray);
						com.impetus.ankush.nodeUtilizationTrend.graphsViewport(false);
						com.impetus.ankush.nodeUtilizationTrend.changeBorder();
						$("#showLoading").hide();
						if($("#nodeUtilizationTrendGraphsDivs").is(':empty')){
							$("#nodeUtilizationTrendGraphsDivs").append("<div style='margin-left:10px;font-size:16px;'id='emptyGraphsDiv'>No Graph Selected.</div>");
						}
						else{
							$("#emptyGraphsDiv").remove();
						}
					});	
				},
				//this function will make boxes
				makeBoxes : function(jsonArray){
					for(var i = 0 ; i < jsonArray.length ; i++){
						var idUsingKey = jsonArray[i].replace(/[\.]+/g,'_');
						idUsingKey = idUsingKey.replace(/ /g, '_');
						$("#tree_"+idUsingKey).hide();
						$("#tree_"+idUsingKey).remove();
						$("#nodeUtilizationTrendGraphsDivs")
							.append('<div style="margin-top:10px;margin-left:10px;width:430px;height:300px;"  id="tree_'+idUsingKey+'" class="left graphTile">'+
										'<div id="graph-'+idUsingKey+'_'+i+'" class="'+nodeLevelGraphsStartTime+'">'+
											'<svg></svg>'+
										'</div>'+
									'</div>');
						
						
					}
				},
				//this function will load all saved graphs 
				loadAllSavedGraphs : function(){
					var graphUrl = baseUrl + '/monitor/'+clusterId+'/graphviews?ip='+hostName;
					com.impetus.ankush.placeAjaxCall(graphUrl,'GET',true,null,function(graphResult){
						if(graphResult.output.status == false)
							return;
						var jsonArray =  graphResult.output.list;
						for(var i = 0 ; i < jsonArray.length ; i++){
							var idUsingKey = jsonArray[i].replace(/[\.]+/g,'_').replace(/ /g,"_");
							$('#'+idUsingKey).removeClass('jstree-unchecked').addClass('jstree-checked');
							var parents = $('#'+idUsingKey).parents('li');
							for(var j = 0 ; j < parents.length ; j++){
								$('#'+parents[j].id).removeClass('jstree-unchecked').addClass('jstree-undetermined');
							}
						}
						setTimeout(function(){
							com.impetus.ankush.nodeUtilizationTrend.makeBoxes(jsonArray);
							com.impetus.ankush.nodeUtilizationTrend.graphsViewport(false);
						},500);
					});
				},
				//this function will check whether graphs are in a view-port
				graphsViewport : function(isAutorefresh){
					$(".graphTile").withinViewport().each(function() {
						var id = ($(this).attr('id')).split('tree_')[1];
						var pattern = id.replace(/[\_]+/g,'.');
						var svgId = $(this).children().first().attr('id');
						if(isAutorefresh != true){
							if($("#"+svgId+" > svg").is(':empty')){
								com.impetus.ankush.nodeUtilizationTrend.fillGraphDataViewport(clusterId,hostName,nodeLevelGraphsStartTime,pattern+'(\\.|_).*',svgId,isAutorefresh);
							}else{
								var preClass = $('#'+svgId).attr('class');
								if((preClass != nodeLevelGraphsStartTime)){
									$('#'+svgId).removeClass(preClass).addClass(nodeLevelGraphsStartTime);
									com.impetus.ankush.nodeUtilizationTrend.fillGraphDataViewport(clusterId,hostName,nodeLevelGraphsStartTime,pattern+'(\\.|_).*',svgId,isAutorefresh);
								}
							}
						}else{
							com.impetus.ankush.nodeUtilizationTrend.fillGraphDataViewport(clusterId,hostName,nodeLevelGraphsStartTime,pattern+'(\\.|_).*',svgId,isAutorefresh);
						}
					});
				},
				//This function will make an ajax call using pattern and populate graphdata using callchart function
				fillGraphDataViewport : function(clusterId,nodeIp,startTime,pattern,svgId,isAutorefresh){
					if(isAutorefresh != true)
						$('#'+svgId).parent().addClass('loadingDiv');
					var graphUrl = baseUrl + '/monitor/'+clusterId+'/graphs?ip='+nodeIp+'&startTime='+startTime+'&pattern='+pattern;
					com.impetus.ankush.placeAjaxCall(graphUrl,'GET',true,null,function(graphResult){
						$('#'+svgId).parent().removeClass('loadingDiv');
						if(graphResult.output.status == false){
							$('#'+svgId).text("No data available for "+$('#'+svgId).parent().attr('id').split('tree_')[1]);
							return;
						}
						var jsonArray =  com.impetus.ankush.nodeUtilizationTrend.normalizeData(graphResult.output.json);	
						var arr = jsonArray;
						com.impetus.ankush.nodeUtilizationTrend.callChart(arr,svgId,startTime);
					});
				},
				removeGraphUsingId : function(idStartPattern){
					if(idStartPattern == undefined)
						return;
					var pattern = idStartPattern.replace(/[\_]+/g,'.');
					var saveRemovedGraphUrl = baseUrl + '/monitor/'+clusterId+'/removeviews?ip='+hostName+'&pattern='+pattern+'(\\.|_).*';
					com.impetus.ankush.placeAjaxCall(saveRemovedGraphUrl,'GET',true,null,function(removeResult){
						
					});
					$("div[id^=tree_"+idStartPattern+"]").hide();
					$("div[id^=tree_"+idStartPattern+"]").remove();
					com.impetus.ankush.nodeUtilizationTrend.changeBorder();
					if($("#nodeUtilizationTrendGraphsDivs").is(':empty')){
						$("#nodeUtilizationTrendGraphsDivs").append("<div style='margin-left:10px;font-size:16px;'id='emptyGraphsDiv'>No Graph Selected.</div>");
					}
					else{
						$("#emptyGraphsDiv").remove();
					}
				},
				//this function will be called when time level buttons will be clicked
				loadGraphsOnChangeStartTime : function(startTime){
					nodeLevelGraphsStartTime = startTime;
					com.impetus.ankush.nodeUtilizationTrend.graphsViewport({});
				},
				//this function will populate data using data and id of an element having svg element as first children
				callChart : function(data,id,startTime) {
					if(data.length == 0)
						return;
					if(chartObj[id] == undefined){
						var unit = data[1];
						data = [data[0]];
						var formatString = '';
						switch(startTime){
							case 'lastday':
								formatString = '%H:%M';
								break;
							case 'lastweek':
								formatString = '%a %H:%M';
								break;
							case 'lastmonth':
								formatString = '%d/%m';
								break;
							case 'lastyear':
								formatString = '%b';
								break;
							default:
								formatString = '%H:%M:%S';
							}
						$('#'+id+' svg').css({
							'height': '300px',
							'width': '430px',
						});
							nv.addGraph(function() {
								chartObj[id] = nv.models.lineChart()
									.x(function(d) { return d[0]; }) 
								    .y(function(d) { return d[1]; })
								    .clipEdge(true);
								chartObj[id].margin().left = 77;
								chartObj[id].margin().right = 40;
								var maxValue = 0;
								$.each(data, function(key, value){
									var keyMaxVal = Math.max.apply(Math, value.yvalues);
									if(keyMaxVal > maxValue) {
										maxValue = keyMaxVal;
									}
								});
								if(maxValue == 0)
									maxValue = 1;
								chartObj[id].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
								var dataArray = [0,maxValue/6,maxValue/3,maxValue/2,2*(maxValue/3),5*(maxValue/6),maxValue];
								var format = null;
									chartObj[id].yAxis.ticks(10).tickFormat(function(d,i){
											var suffix = '';
												if (maxValue >= 1000000000){
													d = d/1000000000;
													suffix = ' G';
												} 
												else if (maxValue >= 1000000){
													d = d/1000000;
													suffix = ' M';
												} 
												else if (maxValue >= 1000){
													d = d/1000;
													suffix = ' K';
												} else if (maxValue > 0 && maxValue < 1) {
													d = d*1000;
													suffix = ' m';
												}
						                    d = d3.format(".2f")(d);
						                    return d + suffix;
										});
								chartObj[id].yAxis.scale().domain([0,maxValue]);
								chartObj[id].yAxis.axisLabel(unit);
								var svg = d3.select('#'+id+' svg')
								    .data([data])
								    .transition().duration(1000).call(chartObj[id]);
								d3.selectAll('.graphTile  .nv-series').on('click',null);
								nv.utils.windowResize(chartObj[id].update);
							});
				}else
					com.impetus.ankush.nodeUtilizationTrend.callChartRedraw(data,id,startTime);
			},
			callChartRedraw : function(data,id,startTime) {
				if(data.length == 0)
					return;
				var unit = data[1];
				data = [data[0]];
				var formatString = '';
				switch(startTime){
					case 'lastday':
						formatString = '%H:%M';
						break;
					case 'lastweek':
						formatString = '%a %H:%M';
						break;
					case 'lastmonth':
						formatString = '%d/%m';
						break;
					case 'lastyear':
						formatString = '%b';
						break;
					default:
						formatString = '%H:%M:%S';
					}
				$('#'+id+' svg').css({
					'height': '300px',
					'width': '430px',
				});
				var maxValue = 0;
						$.each(data, function(key, value){
							var keyMaxVal = Math.max.apply(Math, value.yvalues);
							if(keyMaxVal > maxValue) {
								maxValue = keyMaxVal;
							}
						});
							if(maxValue == 0)
								maxValue = 1;
						chartObj[id].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
						var format = null;
						var dataArray = [0,maxValue/6,maxValue/3,maxValue/2,2*(maxValue/3),5*(maxValue/6),maxValue];
							chartObj[id].yAxis.ticks(10).tickFormat(function(d){
									var suffix = '';
				                    	if (maxValue >= 1000000000){
											d = d/1000000000;
											suffix = ' G';
										} 
										else if (maxValue >= 1000000){
											d = d/1000000;
											suffix = ' M';
										} 
										else if (maxValue >= 1000){
											d = d/1000;
											suffix = ' K';
										} else if (maxValue > 0 && maxValue < 1) {
											d = d*1000;
											suffix = ' m';
										}
									d = d3.format(".2f")(d);
									return d + suffix;
								});
						chartObj[id].yAxis.axisLabel(unit);
						var svg = d3.select('#'+id+' svg')
						    .data([data])
						    .transition().duration(1000).call(chartObj[id]);
						d3.selectAll('.graphTile  .nv-series').on('click',null);
						nv.utils.windowResize(chartObj[id].update);
					
		},
		//this function will change border according to longer div
		changeBorder : function(){
			var leftHeight = $("#jstreeNodeGraph").height(); 
		    var rightHeight = $("#nodeUtilizationTrendGraphsDivs").height();
		    if(leftHeight >= rightHeight){
		    	$("#jstreeNodeGraph").css('border-right', '1px solid #E6E6E6');
		    	$("#nodeUtilizationTrendGraphsDivs").css('border-left', '0');
		    }
		    else{
		    	$("#nodeUtilizationTrendGraphsDivs").css('border-left', '1px solid #E6E6E6');
		    	$("#jstreeNodeGraph").css('border-right', '0');
		    }
		},
			 normalizeData: function (rrdData)
		      {
				if(rrdData == null){
		            return [];
		        }
		        var meta = rrdData.meta;
		        var data = rrdData.data;
		        var legends = meta.legend;
		        var result = [];

		        legends.forEach(function(legend, index)
		        {
		            result.push({key: legend, values: [], yvalues : []});
		        });
		        data.forEach(function(data, dataIndex)
		        {
		          legends.forEach(function(legend, legendIndex)
		          {
		            result[legendIndex].values.push([Number(data.t) , Number(data.v)]);
		            result[legendIndex].yvalues.push(Number(data.v));
		          });         
		        });  
		        result.push(rrdData.unit);
		        return result;
		      },
		};
		
	
		
