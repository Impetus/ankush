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
/*var context = null;
var arrayToDrawGraph = {};
arrayToDrawGraph['Network'] = [];
arrayToDrawGraph['Memory'] = [];
arrayToDrawGraph['CPU'] = [];
arrayToDrawGraph['Load'] = [];*/
var sparkObj = {};

com.impetus.ankush.sparkline = {
		
		//this function will call for all four metric
		graphsLoad : function(){
			com.impetus.ankush.sparkline.callAjaxForSparkLine(com.impetus.ankush.commonMonitoring.clusterId,'cpu','lasthour');
			com.impetus.ankush.sparkline.callAjaxForSparkLine(com.impetus.ankush.commonMonitoring.clusterId,'memory','lasthour');
			com.impetus.ankush.sparkline.callAjaxForSparkLine(com.impetus.ankush.commonMonitoring.clusterId,'network','lasthour');
			com.impetus.ankush.sparkline.callAjaxForSparkLine(com.impetus.ankush.commonMonitoring.clusterId,'load','lasthour');
		},
		//this function will hit ajax call for all four metric
		callAjaxForSparkLine : function(clusterId,type,startTime){
			var url = baseUrl+'/monitor/'+clusterId+'/clustergraph?startTime='+startTime+'&type='+type;
			com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(graphResult){
				if(graphResult.output.status){
					if(graphResult.output.json == undefined){
						$("#"+type+'Spark').parent().removeClass('loadingDiv');
						$("#"+type+'Spark').parent().parent().parent().html('<span class="mrg10">Unable to fetch graphs.</span>');
					}else{
						var jsonArray =  com.impetus.ankush.sparkline.normalizeData(graphResult.output.json);	
						var arr = jsonArray;
						com.impetus.ankush.sparkline.callChart(arr,type+'Spark',startTime);
					}
				}else{
					$("#"+type+'Spark').parent().removeClass('loadingDiv');
					$("#"+type+'Spark').parent().parent().parent().html('<span class="mrg10">Unable to fetch graphs.</span>');
				}
			});
		},
		//this function will make chart for all four metric
		callChart : function(data,id,startTime) {
			$("#"+id).parent().removeClass('loadingDiv');
			var sparkWidth = $('#'+id).parent().width()+40;
			var sparkHeight = $('#'+id).parent().height();
			if(data.length == 0)
				return;
			if(sparkObj[id] == undefined){
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
					'height': sparkHeight+'px',
					'width': sparkWidth+'px',
				});
					nv.addGraph(function() {
						sparkObj[id] = nv.models.lineChart()
							.x(function(d) { return d[0]; }) 
						    .y(function(d) { return d[1]; })
						    .clipEdge(true);
						sparkObj[id].margin().left = 48;
						sparkObj[id].margin().right = 40;
						var maxValue = 0;
						$.each(data, function(key, value){
							var keyMaxVal = Math.max.apply(Math, value.yvalues);
							if(keyMaxVal > maxValue) {
								maxValue = keyMaxVal;
							}
						});
						if(maxValue == 0)
							maxValue = 1;
						sparkObj[id].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
						var dataArray = [0,maxValue/6,maxValue/3,maxValue/2,2*(maxValue/3),5*(maxValue/6),maxValue];
						var format = null;
							sparkObj[id].yAxis.ticks(10).tickFormat(function(d,i){
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
						sparkObj[id].yAxis.scale().domain([0,maxValue]);
						sparkObj[id].yAxis.axisLabel(unit);
						var svg = d3.select('#'+id+' svg')
						    .data([data])
						    .transition().duration(1000).call(sparkObj[id]);
						d3.selectAll('.graphTile  .nv-series').on('click',null);
						nv.utils.windowResize(sparkObj[id].update);
					});
		}else{
			com.impetus.ankush.sparkline.callChartRedraw(data,id,startTime);
		}
	},
	callChartRedraw : function(data,id,startTime,width,height) {
		var sparkWidth = $('#'+id).parent().width()+40;
		var sparkHeight = $('#'+id).parent().height();
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
			'height': sparkHeight+'px',
			'width': sparkWidth+'px',
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
				sparkObj[id].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
				var format = null;
				var dataArray = [0,maxValue/6,maxValue/3,maxValue/2,2*(maxValue/3),5*(maxValue/6),maxValue];
					sparkObj[id].yAxis.ticks(10).tickFormat(function(d){
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
				/*sparkObj[id].yAxis.axisLabel(unit);*/
				var svg = d3.select('#'+id+' svg')
				    .data([data])
				    .transition().duration(1000).call(sparkObj[id]);
				d3.selectAll('.graphTile  .nv-series').on('click',null);
				nv.utils.windowResize(sparkObj[id].update);
			
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

