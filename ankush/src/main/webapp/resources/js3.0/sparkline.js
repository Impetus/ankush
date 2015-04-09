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
		/*init : function(autorefreshFlag){
			context = cubism.context()
			.step(1.5e4)
		    .size(240) // 1 hour
			.serverDelay(0);
		    //.stop();
			var result = {"output":{"times":["1401103920","1401103935","1401103950","1401103965","1401103980","1401103995","1401104010","1401104025","1401104040","1401104055","1401104070","1401104085","1401104100","1401104115","1401104130","1401104145","1401104160","1401104175","1401104190","1401104205","1401104220","1401104235","1401104250","1401104265","1401104280","1401104295","1401104310","1401104325","1401104340","1401104355","1401104370","1401104385","1401104400","1401104415","1401104430","1401104445","1401104460","1401104475","1401104490","1401104505","1401104520","1401104535","1401104550","1401104565","1401104580","1401104595","1401104610","1401104625","1401104640","1401104655","1401104670","1401104685","1401104700","1401104715","1401104730","1401104745","1401104760","1401104775","1401104790","1401104805","1401104820","1401104835","1401104850","1401104865","1401104880","1401104895","1401104910","1401104925","1401104940","1401104955","1401104970","1401104985","1401105000","1401105015","1401105030","1401105045","1401105060","1401105075","1401105090","1401105105","1401105120","1401105135","1401105150","1401105165","1401105180","1401105195","1401105210","1401105225","1401105240","1401105255","1401105270","1401105285","1401105300","1401105315","1401105330","1401105345","1401105360","1401105375","1401105390","1401105405","1401105420","1401105435","1401105450","1401105465","1401105480","1401105495","1401105510","1401105525","1401105540","1401105555","1401105570","1401105585","1401105600","1401105615","1401105630","1401105645","1401105660","1401105675","1401105690","1401105705","1401105720","1401105735","1401105750","1401105765","1401105780","1401105795","1401105810","1401105825","1401105840","1401105855","1401105870","1401105885","1401105900","1401105915","1401105930","1401105945","1401105960","1401105975","1401105990","1401106005","1401106020","1401106035","1401106050","1401106065","1401106080","1401106095","1401106110","1401106125","1401106140","1401106155","1401106170","1401106185","1401106200","1401106215","1401106230","1401106245","1401106260","1401106275","1401106290","1401106305","1401106320","1401106335","1401106350","1401106365","1401106380","1401106395","1401106410","1401106425","1401106440","1401106455","1401106470","1401106485","1401106500","1401106515","1401106530","1401106545","1401106560","1401106575","1401106590","1401106605","1401106620","1401106635","1401106650","1401106665","1401106680","1401106695","1401106710","1401106725","1401106740","1401106755","1401106770","1401106785","1401106800","1401106815","1401106830","1401106845","1401106860","1401106875","1401106890","1401106905","1401106920","1401106935","1401106950","1401106965","1401106980","1401106995","1401107010","1401107025","1401107040","1401107055","1401107070","1401107085","1401107100","1401107115","1401107130","1401107145","1401107160","1401107175","1401107190","1401107205","1401107220","1401107235","1401107250","1401107265","1401107280","1401107295","1401107310","1401107325","1401107340","1401107355","1401107370","1401107385","1401107400","1401107415","1401107430","1401107445","1401107460","1401107475","1401107490","1401107505"],"status":true,"sparklineData":[{"id":"Memory","unit":"%","values":[25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,10,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25],"label":"Memory","delay":500,"maxValue":100},{"id":"CPU","unit":"%","values":[18,14,14,11,14,6,8,15,15,14,12,14,19,13,10,17,18,16,14,18,14,10,9,16,16,11,10,16,16,11,10,16,18,12,7,10,15,13,11,14,15,12,13,17,17,11,10,16,18,14,13,17,18,11,11,16,16,13,12,16,18,12,10,15,16,10,6,9,10,14,15,14,15,9,12,17,18,15,12,14,18,11,11,16,18,10,11,16,18,10,6,6,6,11,14,18,18,15,14,14,12,9,12,16,17,10,12,14,14,9,14,15,17,12,13,18,17,15,11,13,17,9,13,17,18,13,13,16,17,12,12,17,18,10,13,16,16,16,12,10,17,7,12,14,18,7,14,15,18,7,13,14,18,6,6,6,13,9,13,13,17,8,13,14,18,7,14,14,14,9,7,6,10,10,15,16,16,10,6,13,18,8,12,13,18,7,14,15,18,7,7,15,15,10,7,14,14,9,6,13,14,8,7,14,18,7,14,15,18,12,13,14,16,9,12,14,19,8,13,14,18,14,12,11,17,9,12,14,18,9,14,17,18,9,6,6,12,16,13,13],"label":"CPU","delay":500,"maxValue":100},{"id":"Load","unit":"%","values":[31,31,31,34,49,49,49,49,49,44,34,34,34,34,34,35,36,36,36,36,36,38,41,41,41,41,41,41,42,42,42,42,42,43,45,45,45,45,45,42,39,39,39,39,39,36,33,33,33,33,33,35,38,38,38,38,38,35,34,34,34,34,34,37,40,40,40,40,40,39,38,38,38,38,38,32,30,31,31,31,31,25,25,27,27,27,27,27,29,30,30,30,30,31,27,25,25,25,25,30,36,38,38,38,38,29,21,19,19,19,19,19,17,17,17,17,17,14,11,10,10,10,10,9,12,14,14,14,14,14,12,12,12,12,12,11,9,8,8,8,8,8,6,6,6,6,6,6,6,7,7,7,7,7,17,18,18,18,18,18,6,6,6,6,6,6,1,1,1,1,1,1,8,10,10,10,10,10,15,16,16,16,16,16,6,4,4,4,4,4,5,5,5,5,5,5,5,6,6,6,6,6,10,10,10,10,10,10,14,15,15,15,15,15,6,4,4,4,4,4,1,1,1,1,1,3,3,3,3,3,3,1,1,2,2,2,2,9,12,12],"label":"Load","delay":500,"maxValue":100},{"id":"Network","unit":"MB/s","values":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],"label":"Network","delay":500,"maxValue":100}]},"status":"200","description":"Cluster details.","errors":null}
				var array = {};
				var timeArray = [];
				array['Network'] = [];
				array['Memory'] = [];
				array['CPU'] = [];
				array['Load'] = [];
				timeArray = result.output.times;
				for(var key = 0 ; key < result.output.sparklineData.length ; key++){
					array[result.output.sparklineData[key].id] = result.output.sparklineData[key].values;
					arrayToDrawGraph[result.output.sparklineData[key].id] = arrayToDrawGraph[result.output.sparklineData[key].id].concat(array[result.output.sparklineData[key].id]);
					arrayToDrawGraph[result.output.sparklineData[key].id] = arrayToDrawGraph[result.output.sparklineData[key].id].slice(arrayToDrawGraph[result.output.sparklineData[key].id].length - context.size());
				}
			d3.select("#sparkline_container").selectAll(".axis")
		    .data(["top","bottom"])
		  .enter().append("div")
		    .attr("class", function(d) {return d + " axis"; })
		    .each(function(d) {d3.select(this).call(context.axis().ticks(2).orient(d)); });

		d3.select("#sparkline_container").append("div")
		    .attr("class", "rule")
		    .call(context.rule());
		

		d3.select("#sparkline_container").selectAll(".horizon")
		    .data(["Network","Memory","CPU","Load"].map(metric))
		  .enter().insert("div", ".bottom")
    .attr("class", "horizon")
    .call(context.horizon().extent([-10, 10]));

		context.on("focus", function(i) {
		  d3.selectAll(".value").style("right", i == null ? null : context.size() - i + "px");
		});
		var interval = setInterval( increment, 10000);	 
	}*/
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
/*function metric(name) {
 return context.metric(function(start, stop, step, callback) {
	 start = +start, stop = +stop;
	 //console.log(arrayToDrawGraph[name]);
callback(null, arrayToDrawGraph[name] = arrayToDrawGraph[name].slice((start-stop)/step));
 }, name);
}*/


/*function increment(){
	for(var key in arrayToDrawGraph){
		var a = ++(arrayToDrawGraph[key][0]);
		(arrayToDrawGraph[key]).push(a);
		//console.log(arrayToDrawGraph[key]);
	}
}*/
