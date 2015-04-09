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
var hClusterMapView = null;
var IsAutoRefreshON_heatMap = false;
var refreshTimeIntervalMaps = 90000;
var heatMapClusterId= null;
var heatMapType = null;
var heatMapFlag = false;
var typeCheck_global = null;

//unbinding resize function from window
//$(window).unbind('resize');

//function used to call getHeatMapChart() function once window get resized


function initProgressHeatMap(heatmap_clusterId) {
	autoRefreshMaps("getCommonHeatMapChart(\""
			+ heatmap_clusterId +"\",\"0\");");
}
function goToNodeList(){
	$(location).attr('href',( baseUrl+'/commonMonitoring/'+clusterName+'/nodes/C-D/'+clusterId+'/'+clusterTechnology));
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
	var JSONData=null;
	var JSON=null;
	com.impetus.ankush.placeAjaxCall(heatMap_JSONUrl,'GET',true,null,function(result){
			if(result.output.status == false){
				return ;
			}
			JSONData=result;

			JSON=result.output;
			
			JSON=JSONData.output;
			if((JSON.rackInfo==null) || (JSON.rackInfo.length == 0)){
				return;
			}
			var index = 0;
			JSON.nodeData = [];
			for(var i = 0 ; i < JSON.rackInfo.length ; i++){
				for(var j = 0 ; j < JSON.rackInfo[i].nodeData.length ; j++){
					JSON.nodeData[index] = JSON.rackInfo[i].nodeData[j];
					if(JSON.rackInfo[i].rackId == "")
						JSON.rackInfo[i].rackId = "Nodes";
					JSON.nodeData[index].rackId = JSON.rackInfo[i].rackId;
					index++;
				}
			}
			var totalNumberOfNode = JSON.rackInfo[0].totalnodes;
			totalRack = JSON.totalRacks;
			var screenHeight = (parseInt(totalNumberOfNode/boxCount)+3) * cellSize;
			if ($(window).innerHeight()<=screenHeight)
			{
				screenHeight=$(window).innerHeight();
			}
			var height = screenHeight;
			$('#heat_map').html('');

			var styleAttr = "height:"+height+";width:"+	 + ";top:0px;";
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
			.attr("style","font-size:14px;text-decoration:underline;float:left;display:inline;width:100px;margin-left:-100px;margin-top:5px;cursor: pointer;")
			.on('click',function(){goToNodeList()})
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
				totalNodes[i] = d.totalNodes; 
				i++;       
			});
			//alert(JSON.toSource());
			
			var parent = card.selectAll(".card")
			.data(function(d) {return d3.range(1,totalNodes[d-1]+1); })
			.enter().append("div")
			.attr("class", "card")
			.attr("data-placement", "top")
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
			var j = 0;
			var nodeText = '';
			var onclickFunction='';
			d3.selectAll('.back')
			.attr('ip', function(d) {   
				if(JSON.nodeData[j].status == "unavailable"){
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

		
	});
};

	

