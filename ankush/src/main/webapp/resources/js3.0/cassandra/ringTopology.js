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
/*js to populate ring topology page*/
com.impetus.ankush.ringTopology = {
	ringTopology : function(){
					var width = 761,
					height = 500;
					var NodeColorClass ;        
					var ringUrl = null;
					if(clusterTechnology == 'Hybrid'){
						ringUrl = baseUrl + '/monitor/'+clusterId+'/clusteroverview?component=Cassandra';
					}else{
						ringUrl = baseUrl + '/monitor/'+clusterId+'/clusteroverview';
					}
	                $("#ring_graph").empty();
	                $("#showLoading").removeClass('element-hide');
	                com.impetus.ankush.placeAjaxCall(ringUrl, "GET", true, null, function(result) {
	                	$("#showLoading").addClass('element-hide');
	                	if(result.output.status == true){
	                		com.impetus.ankush.ringTopology.clusterSummary(result.output.clusterSummary);
				    		if((result.output.datacenters == undefined) || (result.output.datacenters == null)){
				    			return;
				    		}
					    	datacenters = result.output.datacenters;
			                for(var i = 0; i < datacenters.length; i++){
			                	var node , diffInNodes , datacenters , totalNodes = 0;        
			    				var cy = 70; 
			    				var node = [];
			    				var nodeCtr = 0;	
			    				var minCy = 45;
			    				var g_xaxis = 90;
			                	for(var j = 0; j < datacenters[i].racks.length; j++){
			                        totalNodes += datacenters[i].racks[j].nodes.length;                        
			                        for(var k = 0; k < datacenters[i].racks[j].nodes.length; k++){                      
			                            node[nodeCtr] = datacenters[i].racks[j].nodes[k];
			                            nodeCtr++;
			                        }
			                    }
			                	diffInNodes = 360/totalNodes;
			                    width = cy + g_xaxis + 12;
			                    height = cy + g_xaxis + 10;
			                    var svgContainer = d3.select("#ring_graph").append("svg")
			                    .attr("width", width)
			                    .attr("height", height)                             
			                    .append("g")
			                    .attr("transform", "translate("+g_xaxis+",10)")
			                    .append("g");
			                    svgContainer.append('text')
			                    .attr("dy",cy)
			                    .attr("dx",-6-((datacenters[i].datacenterName).length)*2)
			                    .text(datacenters[i].datacenterName)
			                    svgContainer.attr("transform", "translate(0,0)")
			                    .attr("id","dataparent"+i)
			                    
			                    .append("circle")
			                    .attr("cy",cy)
			                    .attr("r",cy)
			                    
			                    
			                    
			                    d3.select("#dataparent"+i).selectAll(".dataCircle")
			                    .data(node)
			                    .enter()
			                    .append("circle")
			                    .attr("class","dataCircle")
			                    
			              
			                                                                      
			                    .attr("r",9)
			                    .attr("transform", function(d,i) {                             
			                        a = i*diffInNodes;                               
			                        return "rotate(" + [a, 0,cy] + ")"
			                    }) 
			                    .attr("class",function(d){		                			                	
			                        switch(d.state){
			                            case  "NORMAL":
			                                NodeColorClass="green";
			                                break;
			                            case  "Moving":
			                                NodeColorClass="blue";
			                                break;
			                            case  "Leaving":
			                                NodeColorClass="purple";
			                                break;
			                            case  "Joining":
			                                NodeColorClass="black";
			                                break;
			                            case  "Down":
			                                NodeColorClass="orange";
			                                break;
			                            case  "Error":
			                                NodeColorClass="red";
			                                break;
			                        }
			                        if(d.status == "DOWN"){
			                        	NodeColorClass="red";
			                        }
			                        return "bead " +NodeColorClass;
			                    })
			                    .on("mouseover", function(d){tooltip.style("display", "block");
			                    tooltipInner.html("Host : " + d.host + "<br/> Token count : " + d.tokenCount + "<br/> Status : " + d.status + "<br/> State : " + d.state + "<br/> Ownership : " + d.ownership + "<br/> Datacenter : " 
			                    		+ d.dataCenter + "<br/> Rack : " + d.rack + "<br/> Load : " + d.load + "<br/> HostId : " + d.hostId )})
			                    .on("mousemove", function(){return tooltip.style("top",(d3.event.pageY-58)+"px").style("left",(d3.event.pageX-45)+"px");})
			                    .on("mouseout", function(){return tooltip.style("display", "none");});                       
			                
			                                                
			                       var tooltip = d3.select("#ring_graph")
			                                    .append("div")
			                                    .attr("class","tooltip fade right in")
			                                    .attr("id","crTooltip"+i)
			                                    .style("display","none")    ;
	
			                       var tooltipArrow = d3.select("#crTooltip"+i)
			                                        .append("div")
			                                        .attr("class","tooltip-arrow");
	
			                       var tooltipInner = d3.select("#crTooltip"+i)
			                                        .append("div")
			                                        .attr("class","tooltip-inner"); 
			                } 
			               
				    	}
				    	else{
				    		$("#ring_graph").text(result.output.error[0]);
				    		clusterSummaryTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							clusterSummaryTable.fnClearTable();
				    	}
				    });
			},	
			clusterSummary : function(clusterSummaryData){
				if((clusterSummaryData == null) || (Object.keys(clusterSummaryData).length == 0)){
					clusterSummaryTable.fnSettings().oLanguage.sEmptyTable = "No records found.";
					clusterSummaryTable.fnClearTable();
				}
				if (clusterSummaryTable != null)
					clusterSummaryTable.fnClearTable();
				
				for ( var dataKey in clusterSummaryData) {
					clusterSummaryTable.fnAddData([
					                           dataKey,
					                           clusterSummaryData[dataKey] 
					                          ]);
				}
	},
};
