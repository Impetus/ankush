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

var hClusterMapView = null;
var IsAutoRefreshON_heatMap = false;
var refreshTimeIntervalMaps = 90000;
var heatMapClusterId= null;
var heatMapType = null;
var heatMapFlag = false;
var typeCheck_global = null;

//function used to set an interval after which a function passed is called, unless clearinterval() for that function is called
function autoRefreshMaps(functionName) {
	hClusterMapView = setInterval(function() {
		eval(functionName);
	}, refreshTimeIntervalMaps);
}

//function used to stop heat map autorefresh call
function stopHeatMapAutoRefreshCalls() {
	IsAutoRefreshON_heatMap = false;
	hClusterMapView = window.clearInterval(hClusterMapView);
}

//unbinding resize function from window
//$(window).unbind('resize');

//function used to call getHeatMapChart() function once window get resized
$(window).bind('resize', function () {
	$('#heat_map').html('');
	getHeatMapChart(heatMapClusterId,'0');
	getCommonHeatMapChart(heatMapClusterId,'0');
	com.impetus.ankush.commonMonitoring.createCommonTiles(com.impetus.ankush.commonMonitoring.clusterId);
	com.impetus.ankush.commonMonitoring.createTilesForIndividualNode(com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh);
	if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
		if(com.impetus.ankush.commonMonitoring.hybridTechnology == 'ElasticSearch')
			com.impetus.ankush.hybridMonitoring_elasticSearch.createTiles();
});

//function used to define type of heat map to display, whether memory or cpu.
function heatMapTypeSelect(typeCheck){
	var setouttimeFlag = 1;
	if(typeCheck==typeCheck_global){
		return;
	}
	heatMapFlag = true;
	typeCheck_global =  typeCheck;
	getHeatMapChart(heatMapClusterId,setouttimeFlag);
}

//function used to show het map graphs
function getHeatMapChart(heatmap_clusterId,setouttimeFlag){
	if(heatMapFlag){
		if (!$('#cpuHeatMap').hasClass('active')) {
			heatMapType = 'cpuheatmap';
		} else {
			heatMapType = 'memoryheatmap';
		}
	}else{
		if ($('#cpuHeatMap').hasClass('active')) {
			heatMapType = 'cpuheatmap';
		} else {
			heatMapType = 'memoryheatmap';
		}
	}
	heatMapFlag = false;
	var heatMap_JSONUrl = baseUrl + "/monitor/" + heatmap_clusterId + "/"
	+ heatMapType;
	if(heatMapClusterId != null){
		if(heatmap_clusterId!=heatMapClusterId){
			heatMap_JSONUrl = baseUrl + "/monitor/" + heatMapClusterId + "/"
			+ heatMapType;
			stopHeatMapAutoRefreshCalls();
			IsAutoRefreshON_heatMap = true;
			initProgressHeatMap(heatMapClusterId);
		}
	}else{
		heatMapClusterId=heatmap_clusterId;
	}
	var usageType = null;
	if(heatMapType=="cpuheatmap"){
		usageType = "CPU";
	} else {
		usageType = "Memory";
	}
	var margin =140;
	var windowSize = $(window).innerWidth() - margin;
	var screenWidth=windowSize;
	if ($(window).innerWidth()<=windowSize)
	{
		screenWidth=$(window).innerWidth();
	}
	var width = screenWidth,
	cellSize = 28, // cell size
	boxCount = parseInt(width/cellSize)-1;
	var totalNodes = [],
	totalRack,
	ctr = 1 ,
	itr = 1; 
	if(document.getElementById('div_HadoopMonitoringDetails') == null) {
		stopHeatMapAutoRefreshCalls();
		return false;
	}
	var JSONData=null;
	var JSON=null;
	$.ajax({
		'type' : 'GET',
		'url' : heatMap_JSONUrl,
		'contentType' : 'application/json',
		"async" : true,
		'dataType' : 'json',
		'success' : function(result) {
			
			JSONData=result;

			JSON=result.output;
			if(JSON.rackInfo==null){
				return;
			}
			var totalNumberOfNode = JSON.nodeData.length;
			totalRack = JSON.totalRack;    
			var screenHeight = (parseInt(totalNumberOfNode/(boxCount*totalRack) )+3) * cellSize;
			if ($(window).innerHeight()<=screenHeight)
			{
				screenHeight=$(window).innerHeight();
			}
			var height = screenHeight;

			$('#heat_map').html('');

			var styleAttr = "height:"+height+";width:"+width + ";top:0px;";
			var card = d3.select("#heat_map").selectAll(".flip")
			.data(d3.range(1,totalRack+1))
			.enter().append("div")
			.attr("class", "flip")
			.attr("style", styleAttr)
			;

			// code for line break
			var rackCount1 = 0;
			card.append("br")
			.text(function() {
				rackCount1++;
				return "";
			});


			// code for rack name
			var rackCount = 0;
			var rackText = '';
			card.append("div")
			.attr("text-align", "left")  
			.attr("style","font-size:16px;text-decoration:underline;")
			.text(function(d) {
				rackText = JSON.rackInfo[rackCount].rackId;
				rackCount++;
				return rackText;
			});

			var div = d3.select("body").append("div")
			.attr("class", "tooltip")
			.style("opacity", 1e-6);

			//mouseover event for tool tip 
			function mouseover() {
				div.transition()
				.duration(30)
				.style("opacity", 1)
				.style("display", "block");  // remove 1e-6 to the 1
			}

			function toolTip(obj) {
				div
				.text(obj.find('.back').attr('ip'))
				.style("left", (d3.event.pageX+5 ) + "px")
				.style("top", (d3.event.pageY+5) + "px");
			}

			// Remove tool tip	
			function mouseout() {
				div.transition()
				.duration(30)
				.style("opacity", 1e-6)
				.style("display", "none");
			}



			var i = 0;
			JSON.rackInfo.forEach(function(d){
				totalNodes[i] = d.totalnode; 
				i++;       
			});    

			var parent = card.selectAll(".card")
			.data(function(d) { return d3.range(1,totalNodes[d-1]+1); })
			.enter().append("div")
			.attr("class", "card")
			.on("mouseover", function(){
				toolTip($(this));
				mouseover();
			})
			.on("mousemove", function(){
				toolTip($(this));
			})
			.on("mouseout", function(){
				mouseout();
			});

			var x = 0;
			var nCtr = 0; 
			var nodeClass = '';

			var rect = parent.selectAll(".card")
			.data([1,2])
			.enter().append("div")
			.attr("class", function(d){
				if(d==1){
					nodeClass = "face front " + JSON.nodeData[nCtr].status;
					return nodeClass;
				}
				if(d==2){
					nodeClass = "face back " + JSON.nodeData[nCtr].status;
					nCtr++;
					return nodeClass;
				}
			});

			// node display according to the conditions.

			rect.append("title")
			.text(function(d) { return d; });     

			// variables for storing status, nodeIp, value in rackorder
			var status = [], nodeIp =[], value = [];

			JSON.rackInfo.forEach(function(d){
				JSON.nodeData.forEach(function(d1){
					if(d1.rackId==d.rackId){
						status.push(d1.status);
						nodeIp.push(d1.nodeIp);
						value.push(d1.value);
					}
				})
			});

			var nodeIpLastOctet = [];
			for(var i=1;i<nodeIp.length;i++){
				nodeIpLastOctet.push(nodeIp[i].split('.')[3]);
			}

			var j = 0;
			var nodeText = '';
			var onclickFunction='';

			d3.selectAll('.back')
			.attr('ip', function(d) {   
				if(JSON.nodeData[j].status=="unavailable"){
					nodeText = "Node IP: " + nodeIp[j] + " " + " Status: Node Down";
				}else{
					nodeText = "Node IP: " + nodeIp[j] + "" + " " + usageType + " Usage: "+ value[j] + "%";
				}
				j++;
				return nodeText;

			});

			var timeout;

			if(setouttimeFlag==1){
				timeout = 10;

			}else{
				timeout = 1000;
			}
			
			setTimeout(function atest(){
				d3.selectAll(".card")
				.transition()
				.delay(function(d, i) {
					if(i==0){
						i=1;
					}
					return i * 30; 
				})
				.duration(5000)
				.attr("class","card flipped");

			}, timeout);

			var k=0;
			var nodeIp='';
			var nodeId = '';

			d3.selectAll('.back').attr("onclick", function(d){
				nodeIp = JSON.nodeData[k].nodeIp;
				nodeId = JSON.nodeData[k].id;
				k++;
				onclickFunction = "com.impetus.ankush.hadoopMonitoring.graphOnClick('"+nodeId+"','"+nodeIp+"');";
				return onclickFunction;
			});

			if(!IsAutoRefreshON_heatMap){
				stopHeatMapAutoRefreshCalls();
				IsAutoRefreshON_heatMap = true;
				initProgressHeatMap(heatmap_clusterId);
			}
		},
		'error' : function() {
		}
	});
}

//function used to initialize heat map autorefresh call
function initProgressHeatMap(heatmap_clusterId) {
	autoRefreshMaps("getHeatMapChart(\""
			+ heatmap_clusterId +"\",\"0\");");
}


//*********************************************************************************common Monitoring******************************************************************************//
function commonHeatMapTypeSelect(typeCheck){
	var setouttimeFlag = 1;
	if(typeCheck == typeCheck_global){
		return;
	}
	heatMapFlag = true;
	typeCheck_global =  typeCheck;
	getCommonHeatMapChart(com.impetus.ankush.commonMonitoring.clusterId,setouttimeFlag);
}



//heat map function for common monitoring


function getCommonHeatMapChart(heatmap_clusterId,setouttimeFlag){
	if(heatMapFlag){
		if (!$('#cpuHeatMap').hasClass('active')) {
			heatMapType = 'cpuheatmap';
		} else {
			heatMapType = 'memoryheatmap';
		}
	}else{
		if ($('#cpuHeatMap').hasClass('active')) {
			heatMapType = 'cpuheatmap';
		} else {
			heatMapType = 'memoryheatmap';
		}
	}
	heatMapFlag = false;
	var heatMap_JSONUrl = baseUrl + "/monitor/" + heatmap_clusterId + "/"
	+ heatMapType;
	if(heatMapClusterId != null){
		if(heatmap_clusterId!=heatMapClusterId){
			heatMap_JSONUrl = baseUrl + "/monitor/" + heatMapClusterId + "/"
			+ heatMapType;
			//stopHeatMapAutoRefreshCalls();
			//IsAutoRefreshON_heatMap = true;
			//initProgressHeatMap(heatMapClusterId);
		}
	}else{
		heatMapClusterId=heatmap_clusterId;
	}
	var usageType = null;
	if(heatMapType=="cpuheatmap"){
		usageType = "CPU";
	} else {
		usageType = "Memory";
	}
	var margin =140;
	var windowSize = $(window).innerWidth() - margin;
	var screenWidth=windowSize;
	if ($(window).innerWidth()<=windowSize)
	{
		screenWidth=$(window).innerWidth();
	}
	var width = screenWidth,
	cellSize = 28, // cell size
	boxCount = parseInt(width/cellSize)-1;
	var totalNodes = [],
	totalRack,
	ctr = 1 ,
	itr = 1; 
	/*if(document.getElementById('div_HadoopMonitoringDetails') == null) {
		stopHeatMapAutoRefreshCalls();
		return false;
	}*/
	var JSONData=null;
	var JSON=null;
	$.ajax({
		'type' : 'GET',
		'url' : heatMap_JSONUrl,
		'contentType' : 'application/json',
		"async" : true,
		'dataType' : 'json',
		'success' : function(result) {
			if(result.output.status == false){
				return ;
			}
			JSONData=result;

			JSON=result.output;
			
			JSON=JSONData.output;
			
			if((JSON.rackInfo==null) || (JSON.rackInfo.length == 0)){
				return;
			}
			var totalNumberOfNode = JSON.rackInfo[0].totalnode;
			totalRack = JSON.totalRack;
			var screenHeight = (parseInt(totalNumberOfNode/boxCount)+3) * cellSize;
			if ($(window).innerHeight()<=screenHeight)
			{
				screenHeight=$(window).innerHeight();
			}
			var height = screenHeight;
			$('#heat_map').html('');

			var styleAttr = "height:"+height+";width:"+width + ";top:0px;";
			var card = d3.select("#heat_map").selectAll(".flip")
			.data(d3.range(1,totalRack+1))
			.enter().append("div")
			.attr("class", "flip")
			.attr("style", styleAttr)
			;

			// code for rack name
			var rackCount = 0;
			var rackText = '';
			
			card = card.append("div").attr("class", "heat_tile_section").style("float", "left").style("display", "inline").style("padding-left", "100px");
			
			card.append("div")
			.attr("text-align", "left")  
			.attr("style","font-size:14px;text-decoration:underline;float:left;display:inline;width:100px;margin-left:-100px;margin-top:5px;")
			.text(function(d) {
				rackText = JSON.rackInfo[rackCount].rackId;
				rackCount++;
				return rackText;
			});

			var div = d3.select("body").append("div")
			.attr("class", "tooltip")
			.style("opacity", 1e-6);

			//mouseover event for tool tip 
			function mouseover() {
				div.transition()
				.duration(30)
				.style("opacity", 1)
				.style("display", "block");  // remove 1e-6 to the 1
			}

			function toolTip(obj) {
				div
				.text(obj.find('.back').attr('ip'))
				.style("left", (d3.event.pageX+5 ) + "px")
				.style("top", (d3.event.pageY+5) + "px");
			}

			// Remove tool tip	
			function mouseout() {
				div.transition()
				.duration(30)
				.style("opacity", 1e-6)
				.style("display", "none");
			}



			var i = 0;
			JSON.rackInfo.forEach(function(d){
				totalNodes[i] = d.totalnode; 
				i++;       
			});    
			
			//alert(JSON.toSource());
			
			var parent = card.selectAll(".card")
			.data(function(d) { return d3.range(1,totalNodes[d-1]+1); })
			.enter().append("div")
			.attr("class", "card")
			.attr("data-placement", "bottom")
			//obj.find('.back').attr('ip')
//			.on("mouseover", function(){
//				toolTip($(this));
//				mouseover();
//			})
//			.on("mousemove", function(){
//				toolTip($(this));
//			})
//			.on("mouseout", function(){
//				mouseout();
//			})
			;
			
			var j = 0;
			var nodeText = '';
			var onclickFunction='';
			d3.selectAll('.card')
			.attr('data-original-title', function(d) {   
				if(JSON.nodeData[j].status=="unavailable"){
					nodeText = "Node IP: " + JSON.nodeData[j].nodeIp + " " + " Status: Node Down";
				}else{
					nodeText = "Node IP: " + JSON.nodeData[j].nodeIp + "" + " " + usageType + " Usage: "+ JSON.nodeData[j].value + "%";
				}
				j++;
				return nodeText;

			});

			var x = 0;
			var nCtr2 = 0;
			var nodeClass = '';
			var rect = parent.selectAll(".card")
			.data([1,2])
			.enter().append("div")
			.attr("class", function(d){          

				if(d==1){
					nodeClass = 'face front '+JSON.nodeData[nCtr2].status;
					return nodeClass;
				}
				if(d==2)
				{
					nodeClass = 'face back '+JSON.nodeData[nCtr2].status;
					nCtr2++;	
					return nodeClass;
				}

			});

			$('.card').tooltip();
			// node display according to the conditions.

			rect.append("title")
			.text(function(d) { return d; });    

			// variables for storing status, nodeIp, value in rackorder
			var status = [], nodeIp =[], value = [];

			JSON.rackInfo.forEach(function(d){
				JSON.nodeData.forEach(function(d1){
					if(d1.rackId==d.rackId){
						status.push(d1.status);
						nodeIp.push(d1.nodeIp);
						value.push(d1.value);
					}
				})
			});


			var j = 0;
			var nodeText = '';
			var onclickFunction='';

			d3.selectAll('.back')
			.attr('ip', function(d) {   
				if(JSON.nodeData[j].status=="unavailable"){
					nodeText = "Node IP: " + JSON.nodeData[j].nodeIp + " " + " Status: Node Down";
				}else{
					nodeText = "Node IP: " + JSON.nodeData[j].nodeIp + " ," + " " + usageType + " Usage: "+ JSON.nodeData[j].value + "%";
				}
				j++;
				return nodeText;

			});

			var timeout;

			if(setouttimeFlag==1){
				timeout = 10;

			}else{
				timeout = 1000;
			}
			setTimeout(function atest(){
				
				d3.selectAll(".flip")
				.transition()
				.delay(function(d, i) {
					return i * 30; 
				})
				.duration(5000)
				.attr("a",function(){
					d3.select(this).selectAll(".card")
					.transition()
					.delay(function(d, i) {
						if(i==0){
							i=1;
						}
						return i * 30; 
					})
					.duration(5000)
					.attr("class","card flipped");
				});
				
				
				

			}, timeout);

			var k=0;
			var nodeIp='';
			var nodeId = '';

			d3.selectAll('.back').attr("onclick", function(d){
				nodeIp = JSON.nodeData[k].nodeIp;
				nodeId = JSON.nodeData[k].id;
				k++;
				onclickFunction = "com.impetus.ankush.commonMonitoring.graphOnClick('"+nodeIp+"');";
				return onclickFunction;
			});

			if(!IsAutoRefreshON_heatMap){
				stopHeatMapAutoRefreshCalls();
				IsAutoRefreshON_heatMap = true;
				initProgressHeatMap(heatmap_clusterId);
			}
		},
		'error' : function() {
		}
	});
}
