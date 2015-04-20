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
var clusterUtilizationTrend = {};
com.impetus.ankush.clusterUtilization = {
		// graph related function
        loadClusterLevelGraphs : function(startTime,autorefresh) {
        	if(!autorefresh){
                loadClusterLevelGraphsStartTime = startTime;
                
    //            $("#utilizationTrendGraphs").clear();
                $('#utilizationTrendGraphs').empty();
                // to get the json for cpu graph.
                com.impetus.ankush.clusterUtilization.loadGraph(startTime, 'cpu', 'cpu_.*\\.rrd');
                // to get the json for memory graph.
                com.impetus.ankush.clusterUtilization.loadGraph(startTime, 'memory', '(mem|swap)_.*\\.rrd');
                // to get the json for network graph.
                com.impetus.ankush.clusterUtilization.loadGraph(startTime, 'network', 'bytes_.*\\.rrd');
                // to get the json for load graph.
                com.impetus.ankush.clusterUtilization.loadGraph(startTime, 'load', '(load_.*\\.rrd|proc_run.rrd)');
                // to get the json for packed graphs.
                com.impetus.ankush.clusterUtilization.loadGraph(startTime, 'packet', 'pkts_.*\\.rrd');
            }
            else{
                // to get the json for cpu graph.
                com.impetus.ankush.clusterUtilization.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'cpu', 'cpu_.*\\.rrd');
                // to get the json for memory graph.
                com.impetus.ankush.clusterUtilization.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'memory', '(mem|swap)_.*\\.rrd');
                // to get the json for network graph.
                com.impetus.ankush.clusterUtilization.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'network', 'bytes_.*\\.rrd');
                // to get the json for load graph.
                com.impetus.ankush.clusterUtilization.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'load', '(load_.*\\.rrd|proc_run.rrd)');
                // to get the json for packed graphs.
                com.impetus.ankush.clusterUtilization.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'packet', 'pkts_.*\\.rrd');
            }
        },
        //this function will populate graph
        loadGraph : function(startTime, key, value,autorefresh) {
            var graphUrl =  baseUrl + '/monitor/'+clusterId+'/graphs?startTime='+startTime+'&pattern=' + value;
            if(autorefresh != 'autorefresh')
                $("#utilizationTrendGraphs").append('<div class="col-md-6"><div class="panel"><div class="panel-heading"><h3 class="panel-title">'+key.toUpperCase()+'</h3></div><div class="panel-body" id="node-drill-down-graph"><div id="graph_'+key+'"class="loadingDiv"><svg></svg></div></div></div>');
            var graphWidth = $("#graph_"+key).width();
            
            $('#graph_'+key+' svg').css({
                'height': '250px',
                'width': graphWidth+'px',
            });
            com.impetus.ankush.placeAjaxCall(graphUrl, "GET", true, null, function(result) {
            	if(result.output.status) {
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
                        var newResult = com.impetus.ankush.clusterUtilization.normalizeData(result.output.json);
                    nv.addGraph(function() {
                    	clusterUtilizationTrend[key] = nv.models.lineChart()
                            .x(function(d) { return d[0]; })
                            .y(function(d) { return d[1]; })
                            .clipEdge(true);
                        clusterUtilizationTrend[key].margin().left = 85;
                        clusterUtilizationTrend[key].margin().right = 40;
                        clusterUtilizationTrend[key].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
                        var format = null;
                        var maxValue = 0;
                       
                        $.each(newResult, function(key, mapvalue){
                            var keyMaxVal = Math.max.apply(Math, mapvalue.yvalues);
                            if(keyMaxVal > maxValue) {
                                maxValue = keyMaxVal;
                            }
                        });
                        var maxDecimals = com.impetus.ankush.clusterUtilization.countDecimals(maxValue);
                        if(key == 'network' || key == 'memory' || key == 'packet') {
                        	clusterUtilizationTrend[key].yAxis.ticks(10).
                            tickFormat(function (d) {
                                var suffix = ' B';
                                if(maxDecimals < 3 ) {
                                    d = d;
                                } else if(maxDecimals < 6 ) {
                                    d = d/1024;
                                    suffix = " K";
                                } else if(maxDecimals < 9 ){
                                    d = (d/1024)/1024;
                                    suffix = " M";
                                } else if(maxDecimals < 12){
                                    d = (d/1024)/(1024*1024);
                                    suffix = " G";
                                } else {
                                    d = (d/1024)/(1024*1024*1024);
                                    suffix = " T";
                                }
                                d = d3.format(".2f")(d);
                                return d + suffix;});
                        }else{
                        	clusterUtilizationTrend[key].yAxis.tickFormat(d3.format(".2f")).ticks(10);
                        }
//                        var legend = chart.legend();
                        clusterUtilizationTrend[key].yAxis.axisLabel(result.output.json.unit);
                        var svg = d3.select('#graph_'+key+' svg')
                            .datum(newResult)
                            .transition().duration(0).call(clusterUtilizationTrend[key]);
                        nv.utils.windowResize(clusterUtilizationTrend[key].update);
                        $('#graph_'+key).removeClass('loadingDiv');
                   });
                }
                else {
                    $("#graph_"+key).empty();
                    $("#graph_"+key).append("<div id=error_"+key+"></div>");
                    $("#error_"+key).append('<h2>Sorry, Unable to get '+key+' graph.</h2>').css({
                        'text-align' : 'center',
                        'height': '250px',
                        'width': '450px',
                    });
                    $('#graphButtonGroup_JobMonitoring').css("display", "none");
                    $('#graph_'+key).removeClass('loadingDiv');
                }
            	
            });   
        },
        //this function will populate graph on autorefresh
        loadGraphRedraw : function(startTime, key, value) {
        	 var graphUrl =  baseUrl + '/monitor/'+clusterId+'/graphs?startTime='+startTime+'&pattern=' + value;
        	 var graphWidth = $("#graph_"+key).width();
        	 $('#graph_'+key+' svg').css({
                 'height': '250px',
                 'width': graphWidth+'px',
             });
             com.impetus.ankush.placeAjaxCall(graphUrl, "GET", true, null, function(result) {
                 if(result.output.status) {
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
                         var newResult = com.impetus.ankush.clusterUtilization.normalizeData(result.output.json);
                         clusterUtilizationTrend[key].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
                         var format = null;
                         var maxValue = 0;
                        
                         $.each(newResult, function(key, mapvalue){
                             var keyMaxVal = Math.max.apply(Math, mapvalue.yvalues);
                             if(keyMaxVal > maxValue) {
                                 maxValue = keyMaxVal;
                             }
                         });
                         var maxDecimals = com.impetus.ankush.clusterUtilization.countDecimals(maxValue);
                         if(key == 'network' || key == 'memory' || key == 'packet') {
                         	clusterUtilizationTrend[key].yAxis.ticks(10).
                             tickFormat(function (d) {
                            	 var suffix = ' B';
                                 if(maxDecimals < 3 ) {
                                     d = d;
                                 } else if(maxDecimals < 6 ) {
                                     d = d/1024;
                                     suffix = " K";
                                 } else if(maxDecimals < 9 ){
                                     d = (d/1024)/1024;
                                     suffix = " M";
                                 } else if(maxDecimals < 12){
                                     d = (d/1024)/(1024*1024);
                                     suffix = " G";
                                 } else {
                                     d = (d/1024)/(1024*1024*1024);
                                     suffix = " T";
                                 }
                                 d = d3.format(".2f")(d);
                                 return d + suffix;});
                         }else{
                         	clusterUtilizationTrend[key].yAxis.tickFormat(d3.format(".2f")).ticks(10);
                         }
                         clusterUtilizationTrend[key].yAxis.axisLabel(result.output.json.unit);
//                         var legend = chart.legend();
                         var svg = d3.select('#graph_'+key+' svg')
                             .datum(newResult)
                             .transition().duration(0).call(clusterUtilizationTrend[key]);
                         nv.utils.windowResize(clusterUtilizationTrend[key].update);
                    
                 }
                 else {
                     $("#graph_"+key).empty();
                     $("#graph_"+key).append("<div id=error_"+key+"></div>");
                     $("#error_"+key).append('<h2>Sorry, Unable to get '+key+' graph.</h2>').css({
                         'text-align' : 'center',
                         'height': '250px',
                         'width': '450px',
                     });
                     $('#graphButtonGroup_JobMonitoring').css("display", "none");
                 }
             });    
        },
        countDecimals : function(v) {
            var test = v, count = 0;
            while(test > 10) {
                test /= 10;
                count++;
            }
            return count;
        },
        // custom formatting functions:
        toTerra :function (d) { return (Math.round(d/10000000000)/100) + " T"; },
        toGiga : function(d)  { return (Math.round(d/10000000)/100) + " G"; },
        toMega :function(d)  { return (Math.round(d/10000)/100) + " M"; },
        toKilo: function(d)  { return (Math.round(d/10)/100) + " k"; },
        // create tiles on node deatail page
        // will draw graph on individual node detail page
      //normalize graph data , will be called from drawGraph_JobMonitoring function
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
		    result[legendIndex].values.push([Number(data.t) , Number(data.v[legendIndex])]);
		    result[legendIndex].yvalues.push(Number(data.v[legendIndex]));
		  });         
		});     
		return result;
		},
};
