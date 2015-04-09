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
com.impetus.ankush.operationProgress = {
		operationsTiles : function(){
			var eventsUrl = baseUrl + '/monitor/' + clusterId + "/operations?operationId="+operationId;
			com.impetus.ankush.placeAjaxCall(eventsUrl, 'GET', true,null,function(result){
				if(result.output.status){
					var operations = result.output.operations;
					$("#startedBy > h4").text("Started By");
					$("#startedBy > p").text(operations[0].startedBy);
					$("#operationName > h4").text(operations[0].opName);
					$("#startedAt  h4:first").text("Started");
					$("#startedAt  p:first").text($.format.date(new Date(parseInt(operations[0].startedAt)), "dd/MM/yyyy hh:mm:ss"));
					$("#operationStatus > h4").text(operations[0].status);
					$(".progressTile").addClass('border-default');
					if(operations[0].status === com.impetus.ankush.constants.statusCompleted){
						$("#operationStatus").addClass('text-success');
						$("#operationStatus > .type").css('border-right','1px solid #3c763d');
						$("#operationStatus").parent().parent().removeClass('border-success border-default border-danger border-primary').addClass('border-success');
						$("#operationStatus  p:first").text($.format.date(new Date(parseInt(operations[0].completedAt)), "dd/MM/yyyy hh:mm:ss"));
					}else if(operations[0].status === com.impetus.ankush.constants.typeError){
						$("#operationStatus").addClass('text-danger');
						$("#operationStatus").parent().parent().removeClass('border-success border-default border-danger border-primary').addClass('border-danger');
						$("#operationStatus > .type").css('border-right','1px solid #a94442');
					}else if(operations[0].status === com.impetus.ankush.constants.statusInProgress){
						$("#operationStatus").addClass('text-primary');
						$("#operationStatus > .type").css('border-right','1px solid #428bca');
						$("#operationStatus").parent().parent().removeClass('border-success border-default border-danger border-primary').addClass('border-primary');
					}
					com.impetus.ankush.operationProgress.createOperationTable(operations[0]);
				}
			});
		},
		progressLogs : function(){
			com.impetus.ankush.common.deploymentProgress(clusterId,clusterTechnology,operationId);
		},
		createOperationTable : function(data){
			var opName = data.opName;
			var nth_child = 1;
			var totalComponent = 0;
			if(opName === 'DEPLOY'){
				$("#operationTableSection").show();
				data = data.data.input.nodes;
				for(var node in data){
					var totalRow = 0;
					for(var component in data[node].roles){
						var label = '';
						for(var service in data[node].roles[component]){
							if(data[node].roles[component].hasOwnProperty(service))
								label = label+ '<span class="label label-default">'+data[node].roles[component][service]+'</span>&nbsp;';
						}
						
								$("#operationTable").append('<tr>'
													+'<td>'+(component.indexOf("Zookeeper") > -1 ? "Zookeeper" : component)+'</td>'
													+'<td>'+label+'</td>'
													+'</tr>');
								totalComponent++;
								totalRow++;
							
					}
					$("#operationTable  tr:nth-child("+nth_child+")").prepend('<td rowspan="'+totalRow+'">'+node+'</td>');
					nth_child = totalComponent+1;
				}
			}else if(opName === 'START_SERVICES' || opName === 'STOP_SERVICES'){
				$("#operationTableSection").show();
				var host = data.data.input.host;
				var data = data.data.input.services;
				for(var component in data){
					for(var service in data[component]){
						if(data[component].hasOwnProperty(service)){
							if(service == 0){
								$("#operationTable").append('<tr>'
										+'<td rowspan="'+data[component].length+'">'+(component.indexOf("Zookeeper") > -1 ? "Zookeeper" : component)+'</td>'
										+'<td>'+data[component][service]+'</td>'
										+'</tr>');
							}else{
								$("#operationTable").append('<tr>'
										+'<td>'+data[component][service]+'</td>'
										+'</tr>');
							}totalComponent++;
						}
					}
				}
				$("#operationTable  tr:nth-child("+nth_child+")").prepend('<td rowspan="'+totalComponent+'">'+host+'</td>');
				nth_child = totalComponent+1;
			}
		}
};
