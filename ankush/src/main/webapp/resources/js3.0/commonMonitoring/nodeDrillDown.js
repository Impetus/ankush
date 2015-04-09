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
//auto refresh variable for tiles
//var is_autorefresh_individualNode = null;
//auto refresh variable for services
//var is_autorefresh_services = null;
//auto refresh variable for graphs
//var is_autorefresh_graphs = null;
// data table variable for services datatable
var nodeServiceDetail = null;
var utilizationTrend = null;
var preserve_autorefresh_time = 'lasthour';
var lineChartToReDraw = null;
com.impetus.ankush.nodeDrillDown = {
		//this function will load node drill down page
		pageLoadFunction_NodeDrillDown : function(hostName) {
			com.impetus.ankush.nodeDrillDown.drawGraph_JobMonitoring('lasthour',hostName);
			//com.impetus.ankush.nodeDrillDown.createTilesForIndividualNode(hostName);
			com.impetus.ankush.nodeDrillDown.initServiceStatus(hostName);
		},
		drawGraph_JobMonitoring : function(startTime,hostName){
			var width = $('#node-drill-down-graph').width();
			currentStartTime = startTime;
			if(lineChartToReDraw == null){
				var url = baseUrl + '/monitor/' + clusterId + '/nodegraph?host='+ hostName+'&startTime='+startTime;
				com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result){			
					$('#node-drill-down-graph').removeClass('loadingDiv');
					if(result.output.status){
						result = com.impetus.ankush.nodeDrillDown.normalizeData(result.output.json);
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
					$('#node-drill-down-graph svg').css({
						'width': width+'px',
					});
						nv.addGraph(function() {
							lineChartToReDraw = nv.models.lineChart()
								.x(function(d) { return d[0]; }) 
							    .y(function(d) { return d[1]; })
							    .clipEdge(true);
							lineChartToReDraw.margin().left = 60;
							lineChartToReDraw.margin().right = 40;
							var maxValue = 0;
							$.each(result, function(key, value){
								var keyMaxVal = Math.max.apply(Math, value.yvalues);
								if(keyMaxVal > maxValue) {
									maxValue = keyMaxVal;
								}
							});
							var maxDecimals = com.impetus.ankush.nodeDrillDown.countDecimals(maxValue);
							lineChartToReDraw.xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
							lineChartToReDraw.yAxis.ticks(10).tickFormat(function(d){return d3.format(".1f")(d);});
							var svg = d3.select('#nodeDrillDown_Graph svg')
								.attr('height',300)
								.data([result])
								.transition().duration(1000).call(lineChartToReDraw);
							svg.attr('width','90%');
							svg.attr('display','block');
							nv.utils.windowResize(lineChartToReDraw.update);
					   });
				}
					else {
						$('#nodeDrillDown_Graph').empty();
						$('#nodeDrillDown_Graph').append('Sorry, Unable to fetch Graph information for node');
					} 
			});	
		}else{
			com.impetus.ankush.nodeDrillDown.drawGraph_JobMonitoring_autorefresh(currentStartTime,hostName);
		}
	  },
	  drawGraph_JobMonitoring_autorefresh : function(startTime,hostName){
		  var width = $('#node-drill-down-graph').width();
		  var url = baseUrl + '/monitor/' + clusterId + '/nodegraph?host='+ hostName+'&startTime='+currentStartTime;
			com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result){	
				if(result.output.status){
					result = com.impetus.ankush.nodeDrillDown.normalizeData(result.output.json);
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
					$('#node-drill-down-graph svg').css({
						'width': width+'px',
					});
				var maxValue = 0;
				$.each(result, function(key, value){
					var keyMaxVal = Math.max.apply(Math, value.yvalues);
					if(keyMaxVal > maxValue) {
						maxValue = keyMaxVal;
					}
				});
				var maxDecimals = com.impetus.ankush.nodeDrillDown.countDecimals(maxValue);
				lineChartToReDraw.xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
				lineChartToReDraw.yAxis.ticks(10).tickFormat(function(d){return d3.format(".1f")(d);});
				var svg = d3.select('#nodeDrillDown_Graph svg')
							.attr('height',300)
							.data([result])
							.transition().duration(1000).call(lineChartToReDraw);
						svg.attr('width','90%');
						svg.attr('display','block');
						nv.utils.windowResize(lineChartToReDraw.update);
			}
				else {
					$('#nodeDrillDown_Graph').empty();
					$('#nodeDrillDown_Graph').append('Sorry, Unable to fetch Graph information for node');
					$('#graphButtonGroup_JobMonitoring').css("display", "none");
				} 
		});	
	  },
	  
	    initServiceStatus : function(nodeIndex){
		    $("#errorBtn_NodeDrillDown").text("");
			$('#errorBtn_NodeDrillDown').css("display", "none");
			$("#error-div-NodeDrillDown").css("display", "none");
			$("#popover-content_NodeDrillDown").text("");
			$("#popover-content_NodeDrillDown").css("display", "none");
			com.impetus.ankush.nodeDrillDown.fillNodeServiceStatus(nodeIndex);
	  },
	  // this function will fill service data table in individual node page
	 fillNodeServiceStatus : function(nodeIndex){
		 // $("#nodeServiceDetail").dataTable().fnDestroy();
		var serviceStatusUrl=baseUrl+'/monitor/' +clusterId +'/services?host=' + hostName;
		com.impetus.ankush.placeAjaxCall(serviceStatusUrl,'GET',true,null,function(serviceStatusData){
			if(serviceStatusData.output.status == true){
				if(nodeServiceDetail != null)
					nodeServiceDetail.fnClearTable(); 
				var tableData = [];
				var iIndex=0;
					for(var i = 0 ; i < serviceStatusData.output.services.length ; i++ ){
							var componentName = '<span style="" id="componentName-'+ iIndex +'">'+serviceStatusData.output.services[i].component+'</span>';
							if(serviceStatusData.output.services[i].component.indexOf("Zookeeper") > -1){
								componentName = '<span style="" id="componentName-'+ iIndex +'">Zookeeper</span>';
							}
							var serviceName = '<span style="" id="serviceName-'+ iIndex +'">'+serviceStatusData.output.services[i].service+'</span>';
							var status = '<div id="status'+iIndex+'"></div>';
							var checkboxes = "<input type='checkbox' id='serviceCheckboxes"+iIndex+"' class='checkedServices' onclick='com.impetus.ankush.headerCheckedOrNot(\"checkedServices\",\"servicesHeadCheckbox\")'/>";
							var actions = '<div id="actions'+iIndex+'"></div>';
							nodeServiceDetail.fnAddData([
							                             checkboxes,
							                             componentName,
							                             serviceName,
							                             status,
							                             actions
							                             ]);
							if(serviceStatusData.output.services[i].status == true){
								$('#status'+iIndex).html('<svg height="16" width="16" style="margin-top:2px;float:left;"><circle cx="8" cy="8" r="6" style="stroke:#77933C ! important;fill:#77933C;"></circle></svg><div style="color:#77933C;margin-left:5px;float:left">Running</div' );
								$('#actions'+iIndex).html('<button class="btn btn-default" id="serviceButton'+iIndex+'" onclick="com.impetus.ankush.nodeDrillDown.serviceAction(\'stop\',\''+serviceStatusData.output.services[i].component+'\',\''+serviceStatusData.output.services[i].service+'\');" style="height:20px;">Stop</button>');
							}
							else{
								$('#status'+iIndex).html('<svg height="16" width="16" style="margin-top:2px;float:left;"><circle cx="8" cy="8" r="6" style="stroke:#94000C ! important;fill:#94000C;"/></svg><div style="color:#94000C;margin-left:5px;float:left">Stopped</div>' );
								$('#actions'+iIndex).html('<button class="btn btn-default" id="serviceButton'+iIndex+'" onclick="com.impetus.ankush.nodeDrillDown.serviceAction(\'start\',\''+serviceStatusData.output.services[i].component+'\',\''+serviceStatusData.output.services[i].service+'\');" style="height:20px;background-color:#85B71E;background-image:none;">Start</button>');
							}
							if(serviceStatusData.output.services[i].registrationType === 'LEVEL2'){
								$("#serviceCheckboxes"+iIndex).prop('disabled',true);
								$("#serviceButton"+iIndex).prop('disabled',true);
							}
							iIndex++;
						}	
					
				
				if($("#servicesHeadCheckbox").is(':checked'))
					$('.checkedServices').attr('checked',true);
			}else{
				nodeServiceDetail.fnSettings().oLanguage.sEmptyTable = serviceStatusData.output.error[0];
				nodeServiceDetail.fnClearTable();
			}
		});
		
	  },
		 serviceAction : function(action,component,service){
			 $('#error-div').hide();
			 	$("#error-div-NodeDrillDown").css("display", "none");
				$('#errorBtn_NodeDrillDown').css("display", "none");
			 	var serviceData={};
			 	if((action == 'startChecked') || (action == 'stopChecked')){
			 		var checkedServiceFlag = false;
			 		$('.checkedServices').each(function(){
			 			if($(this).is(':checked')){
			 				checkedServiceFlag = true;
				 			var componentItrate = $(this).parent().next().text();
				 			serviceData[componentItrate] = [];
				 		}
			 		});
			 		if(!checkedServiceFlag){
			 			com.impetus.ankush.validation.showAjaxCallErrors(['Select atleast one service.'],'popover-content', 'error-div', '');
			 			return;
			 		}
			 		$('.checkedServices').each(function(){
			 			if($(this).is(':checked')){
				 			var componentItrate = $(this).parent().next().text();
				 			var serviceItrate = $(this).parent().next().next().text();
				 			serviceData[componentItrate].push(serviceItrate);
			 			}
			 		});
			 	}
			 	else{
			 		if(component)
			 			serviceData[component]=[service];
			 	}
			 	action = action.split('Checked')[0];
			 	var url = baseUrl+'/service/'+clusterId+'/'+action+'/'+hostName;
			 	$("#showLoading").removeClass('element-hide');
			    com.impetus.ankush.placeAjaxCall(url,'POST',true,serviceData,function(result){
			    	$("#showLoading").addClass('element-hide');
			    	$("#div_RequestSuccess_NodeServices").appendTo('body').modal('show');
					$('.ui-dialog-titlebar').hide();
			    	if (result.output.status){
						$("#service-message").css('color','#6f7971').html(result.output.message);
					}else{
						$("#service-message").css('color','red').html(result.output.error[0]);
					}
			    },function(){
			    	$("#showLoading").addClass('element-hide');
			    });
		},
		
		servicePostRequest : function(serviceData){
			var serviceurl=baseUrl+"/manage/" + clusterId + "/manage";
			if(serviceData.services.length > 0) {
				com.impetus.ankush.placeAjaxCall(serviceurl, "POST", false, serviceData, function(result) {
						if (result.output.status){
							$("#div_RequestSuccess_NodeServices").appendTo('body').modal('show');
							$('.ui-dialog-titlebar').hide();
						}
						else {
							com.impetus.ankush.nodeDrillDown.errorCount_NodeDrillDown = 0;
							$("#error-div-NodeDrillDown").empty();
							$("#popover-content_NodeDrillDown").empty();
							
							for(var i = 0; i < result.output.error.length; i++) {
								com.impetus.ankush.nodeDrillDown.errorCount_NodeDrillDown++;
								$("#error-div-NodeDrillDown").append("<div class='errorLineDiv'><a href='#'>"+com.impetus.ankush.nodeDrillDown.errorCount_NodeDrillDown+". "+result.output.error[i]+".</a></div>");
								$("#popover-content_NodeDrillDown").append('<a href="#">'+result.output.error[i]+'</a></br>');
							}
							$('#errorBtn_NodeDrillDown').text(com.impetus.ankush.nodeDrillDown.errorCount_NodeDrillDown + " Error");
							$("#error-div-NodeDrillDown").css("display", "block");
							$('#errorBtn_NodeDrillDown').css("display", "block");
							$("#popover-content_NodeDrillDown").css("display", "block");
						}
				});	
			}
			else{
				com.impetus.ankush.nodeDrillDown.errorCount_NodeDrillDown = 1;
				$("#error-div-NodeDrillDown").empty();
				$('#errorBtn_NodeDrillDown').text(com.impetus.ankush.nodeDrillDown.errorCount_NodeDrillDown + " Error");
				$("#error-div-NodeDrillDown").append("<div class='errorLineDiv'><a href='#'>1. Select atleast one service.</a></div>");
				$('#error-div-NodeDrillDown').css("display", "block");
				$('#errorBtn_NodeDrillDown').css("display", "block");
				$("#popover-content_NodeDrillDown").css("display", "block");
			}
		},
		// this function will select all services to stop or start
		checkAll_NodeService : function() {
			var serviceCount = nodeServiceDetail.fnGetData().length;
			var flag = false;
			if($("#checkHead_NodeService").attr("checked"))
				flag = true;
			for(var i = 0; i < serviceCount; i++)
				$("#checkboxService-"+i).attr("checked", flag);
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
            result[legendIndex].values.push([Number(data.t) , Number(data.v[legendIndex])]);
            result[legendIndex].yvalues.push(Number(data.v[legendIndex]));
          });         
        });     
        return result;
      },
      countDecimals : function(v) {
          var test = v, count = 0;
          while(test > 10) {
              test /= 10;
              count++;
          }
          return count;
      },
};
		
		
	
		

	
