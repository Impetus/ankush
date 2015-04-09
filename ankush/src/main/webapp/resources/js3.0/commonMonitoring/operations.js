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
com.impetus.ankush.operations = {
//this function will populate operation list
		populateOperations : function(){
			var eventsUrl = baseUrl + '/monitor/' + clusterId + "/operations";
			com.impetus.ankush.placeAjaxCall(eventsUrl, 'GET', true,null,function(result){
				if(operationsTable != null)
					operationsTable.fnClearTable();
				if(!result.output.status){
					operationsTable.fnSettings().oLanguage.sEmptyTable = '<span class="text-danger">'+result.output.error[0]+'</span>';
					operationsTable.fnClearTable();
					return;
				}else if (result.output.operations && result.output.operations.length !== 0) {
					var operationsLength = result.output.operations.length;
					var operationsOutput = result.output.operations;
					var mainList = [];
					for ( var i = 0; i < operationsLength; i++) {
						var dataList = [];
						var opName = operationsOutput[i].opName;
						var opStartedBy = operationsOutput[i].startedBy;
						var opStartedAt =$.format.date(new Date(parseInt(operationsOutput[i].startedAt)), "dd/MM/yyyy hh:mm:ss");
						var opCompletedAt =$.format.date(new Date(parseInt(operationsOutput[i].completedAt)), "dd/MM/yyyy hh:mm:ss");
						var opStatus = operationsOutput[i].status;
						
						if(operationsOutput[i].status === com.impetus.ankush.constants.statusInProgress){
							opStatus = '<span class="fa fa-circle-o-notch fa-spin text-primary"></span>&nbsp;&nbsp;'+operationsOutput[i].status;
						}else if(operationsOutput[i].status === com.impetus.ankush.constants.statusCompleted){
							opStatus = '<span class="fa fa-check-circle text-success"></span>&nbsp;&nbsp;'+operationsOutput[i].status;
						}
						else if(operationsOutput[i].status === com.impetus.ankush.constants.typeError){
							opStatus = '<span class="fa fa-times-circle-o text-danger"></span>&nbsp;&nbsp;'+operationsOutput[i].status;
						}
						var progressUrl = baseUrl+'/commonMonitoring/'+clusterName+'/operations/'+operationsOutput[i].operationId+'/C-D/'+clusterId+'/'+clusterTechnology;
						var navigation = '<a href="'+progressUrl+'"><span class="fa fa-chevron-right"></span></a>';
						//com.impetus.ankush.commonMonitoring.eventRows[i] = eventsOutput[i];
						if(operationsOutput[i].status === 'INPROGRESS')
							opCompletedAt = '-';
						dataList.push(opName);
						dataList.push(opStatus);
						dataList.push(opStartedBy);
						dataList.push(opStartedAt);
						dataList.push(opCompletedAt);
						
						dataList.push(navigation);
						mainList.push(dataList);
					}
					operationsTable.fnAddData(mainList);
				}else{
					operationsTable.fnSettings().oLanguage.sEmptyTable = "No data Available";
					operationsTable.fnClearTable();
				}
				
			});
		}
};
