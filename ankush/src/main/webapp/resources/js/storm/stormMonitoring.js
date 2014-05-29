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

var topologiesSavedArray = [];
var nimbusConfigObject = {};
var supervisorsSavedArray = [];
var jsonFileUploadString = null;
var is_autorefresh_monitoringStormTableHybrid = null;
var is_autorefresh_monitoringStormTilesHybridHybrid = null;
com.impetus.ankush.stormMonitoring = {
		createTopologySummaryTable : function(){
			var topologyUrl = null;
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				topologyUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/topology?technology=Storm';
			else
				topologyUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/topology';
			$.ajax({
				'type' : 'GET',
				'url' : topologyUrl,
				'contentType' : 'application/json',
				'async' : false,
				'dataType' : 'json',
				'success' : function(result) {
					if(topologySummaryTable != null)
						topologySummaryTable.fnClearTable();
					if(result.output.status){
						if(result.output.topology != undefined){
							topologiesSavedArray = result.output.topology.topologies;
							nimbusConfigObject = result.output.topology.nimbusConf;
							supervisorsSavedArray = result.output.topology.supervisors;
							if((result.output.topology.topologies.length == 0) || (result.output.topology.topologies == null))
								return ;
						}
						else
							return;
						for(var i = 0 ; i < result.output.topology.topologies.length ; i++){
							var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.stormMonitoring.topologyTableDrillDown(' + (i) + ',\''+ result.output.topology.topologies[i].status+'\');"><img id="navigationImgTopologyDrillDown'
								+ i
								+ '" src="'
								+ baseUrl
								+ '/public/images/icon-chevron-right.png"/></a>';
							topologySummaryTable.fnAddData([
							                                '<span>'+result.output.topology.topologies[i].topolgoyId+'</span>',
							                                '<span id="name-'+i+'">'+result.output.topology.topologies[i].name+'</span>',
							                                result.output.topology.topologies[i].status,
							                                result.output.topology.topologies[i].uptime,
							                                result.output.topology.topologies[i].numberOfWorkers,
							                                result.output.topology.topologies[i].numberOfExecutors,
							                                result.output.topology.topologies[i].numberOfTasks,
							                                navigationStormTopology
							                                ]);
						}
					}
					else
						return;
				},
				'error' : function(){
				
				}
			});

		},
		supervisorsTable : function(){
			if((supervisorsSavedArray.length == 0) || (supervisorsSavedArray == null))
				return ;
			if(supervisorSummaryTable != null)
				supervisorSummaryTable.fnClearTable();
			for(var i = 0 ; i < supervisorsSavedArray.length ; i++){
				supervisorSummaryTable.fnAddData([
				                                supervisorsSavedArray[i].host,
				                                supervisorsSavedArray[i].uptime,
				                                supervisorsSavedArray[i].numberOfWorkers,
				                                supervisorsSavedArray[i].numberOfUsedWorkers
				                                ]);
			}
		},
		topologyTableDrillDown : function(topologyIndex,status){
			$('#content-panel').sectionSlider('addChildPanel', {
	            url : baseUrl + '/stormMonitoring/topologyDrillDown',
	            method : 'get',
	            title : topologiesSavedArray[topologyIndex].name,
	            tooltipTitle : com.impetus.ankush.commonMonitoring.clusterName,
	            previousCallBack : "com.impetus.ankush.stormMonitoring.topologyDrillDownPrevious()",
	            callback : function() {
	            	com.impetus.ankush.stormMonitoring.btnGrpValidate(status);
	            	com.impetus.ankush.stormMonitoring.boltTable(topologyIndex);
	            	com.impetus.ankush.stormMonitoring.spoutsTable(topologyIndex);
	            	$('#topologyDrillDown').text(topologiesSavedArray[topologyIndex].name);
	            },
	            callbackData : {
	            }
	        });	
		},
		
		btnGrpValidate:function(status){
			$('.actionBtn').attr('disabled',false);
			if(status=='ACTIVE'){
				$('#btnLastYear_HNDD').attr('disabled',true);
			
			}		else if(status=='INACTIVE'){
				$('#btnLastMonth_HNDD').attr('disabled',true);
				
			}else if(status=='REBALANCING'){
				$('#btnLastYear_HNDD').attr('disabled',true);
				$('#btnLastMonth_HNDD').attr('disabled',true);
				$('#btnLastWeek_HNDD').attr('disabled',true);
			}else{
				$('.actionBtn').attr('disabled',true);
			}
		},
		boltTable : function(topologyIndex){
			if((topologiesSavedArray[topologyIndex].bolts.length == 0) || (topologiesSavedArray[topologyIndex].bolts == null))
				return ;
			if(topologyBoltsTables != null)
				topologyBoltsTables.fnClearTable();
			for(var i = 0 ; i < topologiesSavedArray[topologyIndex].bolts.length ; i++){
				var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.stormMonitoring.x(' + (i) + ');"><img id="navigationImgTopologyDrillDown'
					+ i
					+ '" src="'
					+ baseUrl
					+ '/public/images/icon-chevron-right.png"/></a>';
				topologyBoltsTables.fnAddData([
				                                topologiesSavedArray[topologyIndex].bolts[i].id,
				                                topologiesSavedArray[topologyIndex].bolts[i].numberOfExecutors,
				                                topologiesSavedArray[topologyIndex].bolts[i].numberOfTasks,
				                                topologiesSavedArray[topologyIndex].bolts[i].parallelism,
				                                Math.round(topologiesSavedArray[topologyIndex].bolts[i].executeLatency * 100) / 100,
				                                Math.round(topologiesSavedArray[topologyIndex].bolts[i].processLatency * 100) / 100,
				                                topologiesSavedArray[topologyIndex].bolts[i].emitted,
				                                topologiesSavedArray[topologyIndex].bolts[i].transferred,
				                                topologiesSavedArray[topologyIndex].bolts[i].executed,
				                                topologiesSavedArray[topologyIndex].bolts[i].acked,
				                                topologiesSavedArray[topologyIndex].bolts[i].failed,
				                                topologiesSavedArray[topologyIndex].bolts[i].lastError,
				                                //navigationStormTopology
				                                ]);
			}
		},
		spoutsTable : function(topologyIndex){
			if((topologiesSavedArray[topologyIndex].spouts.length == 0) || (topologiesSavedArray[topologyIndex].spouts == null))
				return ;
			if(topologySpoutsTables != null)
				topologySpoutsTables.fnClearTable();
			for(var i = 0 ; i < topologiesSavedArray[topologyIndex].spouts.length ; i++){
				var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.stormMonitoring.x(' + (i) + ');"><img id="navigationImgTopologyDrillDown'
					+ i
					+ '" src="'
					+ baseUrl
					+ '/public/images/icon-chevron-right.png"/></a>';
				topologySpoutsTables.fnAddData([
				                                topologiesSavedArray[topologyIndex].spouts[i].id,
				                                topologiesSavedArray[topologyIndex].spouts[i].numberOfExecutors,
				                                topologiesSavedArray[topologyIndex].spouts[i].numberOfTasks,
				                                topologiesSavedArray[topologyIndex].spouts[i].parallelism,
				                                Math.round(topologiesSavedArray[topologyIndex].spouts[i].completeLatency * 100) / 100,
				                                topologiesSavedArray[topologyIndex].spouts[i].emitted,
				                                topologiesSavedArray[topologyIndex].spouts[i].transferred,
				                                topologiesSavedArray[topologyIndex].spouts[i].acked,
				                                topologiesSavedArray[topologyIndex].spouts[i].failed,
				                                topologiesSavedArray[topologyIndex].spouts[i].lastError,
				                                //navigationStormTopology
				                                ]);
			}
		},
		JarFileUpload : function(){
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_JarFile').click();
			$('#fileBrowse_JarFile').change(
				function() {
					$('#filePath_JarFile').val(document.forms['uploadframeJarFile_Form'].elements['fileBrowse_JarFile'].value);
					com.impetus.ankush.stormMonitoring.uploadFile(uploadUrl,"fileBrowse_JarFile");
					com.impetus.ankush.stormMonitoring.removeFakePathFromPath('filePath_JarFile');
				});
		},
		uploadFile : function(uploadUrl, fileId, callback, context){
			jsonFileUploadString = null;
			$.ajaxFileUpload({
				url : uploadUrl,
				secureuri : false,
				fileElementId : fileId,
				dataType : 'text',
				async : false,
				success : function(result) {
					if (callback)
						callback(result, context);
					var htmlObject = $(result);
					var resultData = new Object();
					eval("resultData  = " + htmlObject.text());
					jsonFileUploadString = resultData.output;
				},
				error : function() {
					alert('Unable to upload the JAR file');
				}
			});
		},
		removeFakePathFromPath : function(fieldId){
			var value = $('#' + fieldId).val().replace('C:\\fakepath\\', '');
			$('#' + fieldId).val(value);
		},
		submitTopology : function(){
			var data = {};
			//$('#btnSubmitJob_Submit').button('Submitting...');
			$('#btnSubmitJob_Submit').button();
			$('#btnSubmitJob_Submit').button('loading');
			data.topology = $('#submitJob_Job').val();
			data.jarPath = jsonFileUploadString;
			console.log("jarpath.."+jsonFileUploadString);
			var validationError = [];
			var havingSpace = (data.topology).split(" ");
			if((!com.impetus.ankush.validate.empty(data.topology)) || (havingSpace.length > 1)){
				$('#submitJob_Job').addClass('error-box');
				validationError.push("Topology Main class name should be non empty and without having spaces.");
			}else{
				$('#submitJob_Job').removeClass('error-box');
			}
			if(data.jarPath == null){
				$("#filePath_JarFile").addClass('error-box');
				validationError.push("Upload JAR.");
			}
			else{
				$("#filePath_JarFile").removeClass('error-box');
			}
			$(".jobArgumentClass").each(function(){
				if(!com.impetus.ankush.validate.empty($(this).val())){
					validationError.push("Topology Arguments can not be blank.");
					$(this).addClass('error-box');
					return;
				}else{
					$(this).removeClass('error-box');
				}
			});
			if(validationError.length != 0){
				com.impetus.ankush.validation.showAjaxCallErrors(validationError,'popover-content-submitTopo', 'error-div-submitTopo', 'errorBtnHadoop-submitTopo');
				$('#btnSubmitJob_Submit').button('reset');
				return;
			}else{
				$("#popover-content-submitTopo").empty();
				$("#error-div-submitTopo").hide();
				$("#errorBtnHadoop-submitTopo").empty().hide();
			}
			data.topologyArgs = [];
			var submitTopoUrl =  null;
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				submitTopoUrl =  baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/submitTopology?technology=Storm';
			else
				submitTopoUrl =  baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/submitTopology';
			data.topologyArgs = new Array();
			var count_JobArg = tblJobArguments.fnGetData().length;
			for(var i = 1; i <= count_JobArg; i++) {
				data.topologyArgs.push($.trim($("#jobArgument-"+i).val()));
			}
			$.ajax({
				'type' : 'POST',
				'url' : submitTopoUrl,
				'contentType' : 'application/json',
				'data' : JSON.stringify(data),
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
					$('#btnSubmitJob_Submit').button('reset');
					if (result.output.status){
						$("#div_RequestSuccess").modal('show');
						$('.ui-dialog-titlebar').hide();
						$('#errorBtnHadoop').empty().hide();
						$('#error-div-hadoop').empty().hide();
						$("#divOkbtn").click(function() {
							$('#div_RequestSuccess').modal('hide');
						//	com.impetus.ankush.removeCurrentChild();
							com.impetus.ankush.stormMonitoring.clearSubmitJobData();
						});
					}else {
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content-submitTopo', 'error-div-submitTopo', 'errorBtnHadoop-submitTopo');
						return;
					}

				},
				'error' : function(){
					$('#btnSubmitJob_Submit').button('reset');
				}
			});
		},
		clearSubmitJobData:function(){
			jsonFileUploadString=null;
			$("#submitJob_Job").val('');
			$("#filePath_JarFile").val('');
			tblJobArguments.fnClearTable();
		},
		// Function to Add an Argument Row to the Job Argument List  
		addJobArgumentRow : function() {
			var tableData = [];
			var index_JobArguments = tblJobArguments.fnGetData().length;
			var jobArgumentRow = [];
			var colArgument = '<input type="text" data-toggle="tooltip" title="Topology Argument-'+(index_JobArguments+1)+'" data-placement="right" class="input-large jobArgumentClass" id="jobArgument-'+(index_JobArguments+1)+'" placeholder="Topology Argument" />';
			var colArgumentDelete = '<a href="#"><img id="deleteJobArgument-'+(index_JobArguments+1)+'" src="'+baseUrl
			+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.stormMonitoring.deleteJobArgumentRow('
			+(index_JobArguments+1)+');" style="margin-left:15px;"/></a>';
			jobArgumentRow.push(colArgument);
			jobArgumentRow.push(colArgumentDelete);
			tableData.push(jobArgumentRow);
			tblJobArguments.fnAddData(tableData);
		},
		// Function to Delete an Argument Row from the Job Argument List
		deleteJobArgumentRow : function(index_DeleteArgument) {
			var data_JobArgs = [];
			var count_JobArg = tblJobArguments.fnGetData().length;
			for(var i = 1; i <= count_JobArg; i++) {
				if(i != index_DeleteArgument)
					data_JobArgs.push($("#jobArgument-"+i).val());
			}
			tblJobArguments.fnClearTable();
			count_JobArg = data_JobArgs.length;
			for(var i = 1; i <= count_JobArg; i++) {
				com.impetus.ankush.hadoopJobs.addJobArgumentRow();
				$("#jobArgument-"+i).val(data_JobArgs[i-1]);
			}
		},
		actionTopology : function(action){
			var data = {};
			var manageTopoUrl='';
			data.topologyNames = [];
			if(topologiesSavedArray.length != 0){
						data.topologyNames.push($('#topologyDrillDown').text());
				}
			data.action = action;
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				manageTopoUrl =   baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/manageTopology?technology=Storm';
			else
				manageTopoUrl =   baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/manageTopology';
			
			
			$.ajax({
				'type' : 'POST',
				'url' : manageTopoUrl,
				'contentType' : 'application/json',
				'async' : true,
				'dataType' : 'json',
				'data' : JSON.stringify(data),
				'success' : function(result) {
				
				},
				'error' : function(){
						
				}
			});	
			com.impetus.ankush.removeChild($.data(document, "panels").children.length);	
		},
		nimbusConfTable : function(){
			console.log(nimbusConfigObject);
			if(nimbusConfigTable != null)
				nimbusConfigTable.fnClearTable();
			if((Object.keys(nimbusConfigObject)).length != 0){
				for(var key in nimbusConfigObject){
					nimbusConfigTable.fnAddData([
					                                key,
					                                nimbusConfigObject[key]
					                                ]);
				}
			}
		},
		topologyDrillDownPrevious : function(){
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
				com.impetus.ankush.stormMonitoring.stormMonitoringPageAutorefresh();
				com.impetus.ankush.stormMonitoring.createTopologySummaryTable();
				com.impetus.ankush.hybridMonitoring_storm.createTiles();
			}
			else
				com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);
		},
		stormMonitoringPageAutorefresh : function(){
			is_autorefresh_monitoringStormTableHybrid = setInterval("com.impetus.ankush.stormMonitoring.createTopologySummaryTable();",30000);
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				is_autorefresh_monitoringStormTilesHybridHybrid = setInterval("com.impetus.ankush.hybridMonitoring_storm.createTiles();",30000);
		},
		removeStormMonitoringPageAutorefresh : function(){
			is_autorefresh_monitoringStormTableHybrid = window.clearInterval(is_autorefresh_monitoringStormTableHybrid);
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				is_autorefresh_monitoringStormTilesHybridHybrid = window.clearInterval(is_autorefresh_monitoringStormTilesHybridHybrid);
		}
		
};
