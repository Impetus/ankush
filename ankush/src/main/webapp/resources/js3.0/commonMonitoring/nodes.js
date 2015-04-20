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
var is_autoRefresh_nodeTilesAndDetails = null;
var nodeDetailsTable = null;
var nodeDeleteDataCommon = null;
com.impetus.ankush.nodes = {
		nodeDetails :  function() {
			//com.impetus.ankush.commonMonitoring.nodesTileCreate();
			var nodesUrl = baseUrl + '/monitor/' + clusterId + '/deployednodes';
			com.impetus.ankush.placeAjaxCall(nodesUrl, 'GET', true, null, function(data){
				if(!data.output.status){
					com.impetus.ankush.validation
					.showAjaxCallErrors(
							data.output.error,
							'popover-content', 'error-div',
							'');
					return;
				}
				$("#nodesDetailTable_filter").css({
					'display' : 'none'
				});
				$('#searchNodeDetailTable').keyup(function() {
					$("#nodesDetailTable").dataTable().fnFilter($(this).val());
				});
				$("#popover-content-hadoopNodesDetail").empty();
				$("#popover-content-hadoopNodesDetail").css('display', 'none');
				$("#error-div-hadoopNodesDetail").css('display', 'none');
				$("#errorBtn-hadoopNodesDetail").css('display', 'none');
				if(nodeDetailsTable != null)
					nodeDetailsTable.fnClearTable();
				
				$("#nodeListCommonTech").attr("checked", false);
				var nodesData = data;
				var flag_DeleteProgress = false;
				var mainList = [];
				var count = 0;
				// iterating the over the nodes list.
				if(nodesData.output.status == false){
					return ;
				}
				if((clusterTechnology == 'Hybrid'))
					$('#btnNodeList_DeleteNodes').hide();
				$.each(nodesData.output.nodelist,function(index, node) {
							var dataList = [];
							var	checkboxNode = '<input type="checkbox" id="checkboxNode-'
									+ index + '" onclick="com.impetus.ankush.headerCheckedOrNot(\'checkboxoption\',\'nodeListCommonTech\')" class="checkboxoption"/ >';
							var navigationLink = '<a href="'+baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+node.publicHost+'" class="fa fa-chevron-right"></a>';
							if(node.state == com.impetus.ankush.constants.stateRemoving) {
								flag_DeleteProgress = true;
								checkboxNode = '<input type="checkbox" id="checkboxNode-'
									+ index + '" disabled="disabled"/ >';
								navigationLink = '<a href="'+baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+node.publicHost+'" class="fa fa-chevron-right"></a>';
								$("#btnNodeList_DeleteNodes").addClass('disabled');
								$("#btnNodeList_DeleteNodes").removeAttr("onclick");
							}
							
							var labelsToShow = '';
							mainList.length = 0;
							dataList.push(checkboxNode);
							dataList.push(node.publicHost);
							var isAgent = true;
							for(var i = 0 ; i < node.params.serviceStatus.length ; i++){
								if(node.params.serviceStatus[i].service === 'Agent'){
									isAgent = node.params.serviceStatus[i].status;
									break;
								}
							}
							for(var i = 0 ; i < node.params.serviceStatus.length ; i++){
								var className = '';
								if(!isAgent){
									className = "label-default";
									if(node.params.serviceStatus[i].service === 'Agent'){
										className = "label-danger";
									}
								}else{
									className = "label-success";
									if(!node.params.serviceStatus[i].status){
										className = "label-danger";
									}
								}
								labelsToShow += '<span class="label label-service '+className + '">'+node.params.serviceStatus[i].service+'</span>&nbsp;';
							}
							// commeted code because no roles in desired format
							var cpuUsage = node.params.health ? node.params.health.cpuUsage : '';
							var totalMemory = node.params.health ? node.params.health.totalMemory : '';
							var usedMemory = node.params.health ? node.params.health.usedMemory : '';
							var memoryPer = '';
							var memoryUsage = 'NA';
							if(cpuUsage != 'NA'){
								var colorCPU = 'green';
								if(cpuUsage >= nodesData.output.alertsConf.CPU.alertLevel){
									colorCPU = '#b94a48';
								}
								else if(cpuUsage >= nodesData.output.alertsConf.CPU.warningLevel){
									colorCPU = '#c09853';
								}
								cpuUsage = '<svg width="100" height="16" id="cpuBar-'+index+'" style="margin-top:5px;"><rect x="0" y="0" width="'+cpuUsage+'" height="50" fill="'+colorCPU+'"/><rect x="'+cpuUsage+'" y="0" width="'+(100-(cpuUsage))+'" height="50" fill="grey" /></svg>';
							}
							if(node.params.health){
								if(!((node.params.health.usedMemory == 'NA') || (node.params.health.totalMemory == 'NA'))){
									memoryPer = (node.params.health.usedMemory*100)/node.params.health.totalMemory;
									var colorMemory = 'green';
									if(memoryPer >= nodesData.output.alertsConf.Memory.alertLevel){
										colorMemory = '#b94a48';
									}
									else if(memoryPer >= nodesData.output.alertsConf.Memory.warningLevel){
										colorMemory = '#c09853';
									}
									memoryUsage = '<svg width="100" height="16" id="memoryBar-'+index+'" style="margin-top:5px;"><rect x="0" y="0" width="'+memoryPer+'" height="50" fill="'+colorMemory+'"/><rect x="'+memoryPer+'" y="0" width="'+(100-memoryPer)+'" height="50" fill="grey" /></svg>';
								}
							}
							/*if(node.params.serviceStatus.Ankush.Agent === false) {
								labelsToShow = '';
								for(var labelIndex = 0 ; labelIndex < typeArray.length ; labelIndex++) {
									var className = "label-default";
									labelsToShow += '<span class="label '+className + '">'+typeArray[labelIndex]+'</span>&nbsp;';
								}
								labelsToShow += '<span class="label label-danger">Agent</span>&nbsp;';
								memoryUsage = 'NA';
								cpuUsage	 = 'NA';
							}*/
							dataList.push(labelsToShow);
							if(node.state === com.impetus.ankush.constants.stateDeployed){
								if(node.status){
									dataList.push('<svg height="16" width="16" style="margin-top:2px;float:left;"><circle cx="8" cy="8" r="6" style="stroke:#77933C ! important;fill:#77933C;"></circle></svg><div style="color:#77933C;margin-left:5px;float:left">Running</div');
								}else{
									dataList.push('<svg height="16" width="16" style="margin-top:2px;float:left;"><circle cx="8" cy="8" r="6" style="stroke:#94000C ! important;fill:#94000C;"/></svg><div style="color:#94000C;margin-left:5px;float:left">Down</div>');
								}
							}else{
								dataList.push('<svg height="16" width="16" style="margin-top:2px;float:left;"><circle cx="8" cy="8" r="6" style="stroke:#94000C ! important;fill:#94000C;"/></svg><div style="color:#94000C;margin-left:5px;float:left">Error</div>');
							}
							dataList.push(cpuUsage);
							dataList.push(memoryUsage);
							dataList.push(navigationLink);
							mainList.push(dataList);
							if(nodeDetailsTable != null)
								nodeDetailsTable.fnAddData(mainList);
							/*if(cpuUsage != 'NA'){
								s1 = [node.cpuUsage];
								plot1 = $.jqplot('meterGaugeCpu-'+index,[s1],{
									 seriesDefaults: {
								           renderer: $.jqplot.MeterGaugeRenderer,	
								           rendererOptions: {
								        	   ringWidth:0.0001,
								        	   label : node.cpuUsage+'&nbsp;%',
								        	   labelPosition : 'inside',
								        	   min : 0,
								        	   max : 100,
								        	   padding: 0, 
								        	   background : 'white',
								        	   intervalInnerRadius : 15,
								        	   intervalOuterRadius : 25,
								        	   hubRadius : 2,
								        	   needlePad : 0,
								        	   needleThickness:2,
								        	   showTickLabels: false,
								        	   showTicks: false,
								        	   intervals:[30, 60, 100],
								               intervalColors:['#66cc66', '#E7E658', '#cc6666'],
								           },
								       }
								   });
							}
							 $('.jqplot-target').css('height','30px');
							 $('.jqplot-series-canvas').css({'top':0,'left':80});
							 $('.jqplot-meterGauge-label').css({'top':6,'left':60});
							 $('.jqplot-series-canvas').css('margin-top','1px');*/
							d3.select('#memoryBar-'+index)
							.append('text')
							.attr('dx',17)
							.attr('dy',12)
							.text((usedMemory+'/'+totalMemory+' GB'))
							.style("fill",'white');
							d3.select('#cpuBar-'+index)
							.append('text')
							.attr('dx',27)
							.attr('dy',12)
							.text((cpuUsage = node.params.health ? node.params.health.cpuUsage : 'undefined'+' %'))
							.style("fill",'white');
						   
							if(!node.serviceStatus){
								$('td', $('#rowIdNode-' + index)).addClass('error-row');
							}
							count++;
						});
				if(flag_DeleteProgress) {
					var nodeCount = nodeDetailsTable.fnGetData().length;
					$("#btnNodeList_DeleteNodes").addClass('disabled');
					for(var i = 0; i < nodeCount; i++) {
						$("#checkboxNode-"+i).attr('disabled', true);
					}
				}
			});
		},
		checked_unchecked_all : function(ckeckbox,elem){
			if($(elem).is(":checked")){
				$("#btnNodeList_DeleteNodes").removeClass('disabled');
				$("#btnNodeList_DeleteNodes").attr("onclick","com.impetus.ankush.commonMonitoring.sendDeleteNodesRequest()");
			}else{
				$("#btnNodeList_DeleteNodes").addClass('disabled');
			}
			com.impetus.ankush.checked_unchecked_all(ckeckbox,elem);
		},
		canDeleteNodeCheck : function(){
			var url = baseUrl + '/monitor/'+clusterId+'/canNodesBeDeleted';
			var data = {};
			data.nodes = [];
			$(".checkboxoption").each(function(){
				if($(this).is(":checked")){
					var node = $(this).parent().next().text();
					data.nodes.push(node);
				}
			});
			if(data.nodes.length == 0){
				com.impetus.ankush.validation.showAjaxCallErrors(["Select atleast one node."],'popover-content', 'error-div', '');
				return;
			}
			com.impetus.ankush.placeAjaxCall(url, 'POST', true, data, function(result){
				if(result.output.status){
					$("#passForDelete").val('');
		            $("#passForDeleteError").text('');
		            $("#popover-content").empty();
		    	  	$("#passForDelete").removeClass('error-box');
					$("#deleteNodesDialog").appendTo('body').modal('show');
					$("#deleteNodes").bind('click',function(){
						com.impetus.ankush.nodes.deleteNodes(data);
					});
					setTimeout(function() {
		            	$("#passForDelete").focus();
		            }, 500);
				}else{
					com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,'popover-content', 'error-div', '');
				}
			});
		},
		deleteNodes : function(data){
			data.password = $("#passForDelete").val();
			if(!com.impetus.ankush.validate.empty(data.password)){
				$("#passForDeleteError").text("Password must not be empty.").addClass('text-error');
				return;
			}
			var url = baseUrl+'/cluster/'+clusterId+'/removenode';
			com.impetus.ankush.placeAjaxCall(url, 'POST', true, data, function(result){
				if(result.output.status){
					$("#deleteNodesDialog").modal('hide');
				}else{
					$("#passForDeleteError").text(result.output.error[0]).addClass('text-error');
				}
			});
		}
};

	
			
