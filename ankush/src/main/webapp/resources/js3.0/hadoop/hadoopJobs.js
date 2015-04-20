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
var tblHadoopParameters = null;
var tblJobArguments = null;
var tblJobMonitoring = null;
var jobMonitoringData = null;
var refreshTimeInterval_JobMonitoring = 60000;
var refreshInterval_JobMonitoring = null;
var IsAutoRefreshON_JobMonitoring = false;
var tilesInfo_JobMonitoring = null;
var clusterId_JobMonitor = null;
var clusterTilesInfo = null;
var currentJobState = 'ALL';
var fileUpload_JobJarPath = null;

com.impetus.ankush.hadoopJobs={

		// Function to initialise data tables used in Job Submission Page 
		initTables_SubmitJob:function(){
			if(tblJobArguments == null){		
				tblJobArguments = $("#tblJobArguments").dataTable({
					"bJQueryUI" : false,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : false,
					"bSort" : false,
					"bInfo" : false,
					"oLanguage": {
						"sEmptyTable": ' ',
					}
				});
			}	
			$("#div_RequestSuccess").appendTo('body');
		},
		
		// Function to get job tracker status on page load event of Job Submission Page 
		getJobTrackerStatus : function(page, clusterId) {
			var statusUrl = baseUrl + '/monitor/' + clusterId + '/validatejobsubmission?component=Hadoop';
			
			com.impetus.ankush.placeAjaxCall(statusUrl, "GET", true, null, function(result) {
					if (!result.output.AllowJobSubmission){
						com.impetus.ankush.hadoopJobs.disableFields_SubmitJob();
						com.impetus.ankush.hadoopJobs.showErrorOnPageLoad(result.output.error, 'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
					}			
					else {
						if(page == 'JobMonitoring') {
							com.impetus.ankush.hadoopJobs.pageLoad_JobMonitoring(clusterId);
						}
					}
			});
		},

		// Function to disable fields on error during page load event (if any) of Job Submission Page
		disableFields_SubmitJob : function() {
			$('#btnSubmitJob_Submit').attr('disabled', true);
			$('#submitJob_Job').attr('disabled', true);
			$('#filePath_JarFile').attr('disabled', true);
			$('#submitJob_AddJobArg').removeAttr('onclick');
		},
		
		// Function to display errors on page load event (if any) of Job Submission Page
		showErrorOnPageLoad : function(arrError, popoverContentId, errorDivId, errorBtnId) {
			com.impetus.ankush.validation.showAjaxCallErrors(arrError, popoverContentId, errorDivId, errorBtnId);
			return;
		},
		
		// Function to Add an Argument Row to the Job Argument List  
		addJobArgumentRow : function() {
			var tableData = [];
			var index_JobArguments = tblJobArguments.fnGetData().length;
			var jobArgumentRow = [];
			var colArgument = '<div class="mrgt10"><input type="text" class="form-control" data-toggle="tooltip" title="Job Argument-'+(index_JobArguments+1)+'" data-placement="right" class="input-large" id="jobArgument-'+(index_JobArguments+1)+'" placeholder="Argument" />';
			var colArgumentDelete = '<a href="#" class="mrgt10"><img id="deleteJobArgument-'+(index_JobArguments+1)+'" src="'+baseUrl
			+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.hadoopJobs.deleteJobArgumentRow('
			+(index_JobArguments+1)+');" style="margin-left:15px;"/></a></div>';
			jobArgumentRow.push(colArgument);
			jobArgumentRow.push(colArgumentDelete);
			tableData.push(jobArgumentRow);
			tblJobArguments.fnAddData(tableData);
			$("#jobArgument-"+(index_JobArguments+1)).tooltip();
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

		// Function to validate Submit Job Button click event				
		validateFields : function() {
			$("#popover-content").empty();
			$("#error-div-hadoop").css('display', 'none');
			$("#errorBtnHadoop").css('display', 'none');
			
			com.impetus.ankush.validation.errorCount = 0;
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'submitJob_Job', 'Job Name', 'popover-content');
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'filePath_JarFile', 'JAR File Path', 'popover-content');
			
			com.impetus.ankush.hadoopJobs.validateJarExtn();
			var count_JobArg = tblJobArguments.fnGetData().length;
			for(var i = 1; i <= count_JobArg; i++) {
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'jobArgument-'+i, 'Job Argument-'+i, 'popover-content');
			}
		},
		
		// Function to validate the uploaded JAR file		
		validateJarExtn : function(){
			if($('#filePath_JarFile').val() == '') {
				return;
			}
			var ext = $('#filePath_JarFile').val().split('.').pop().toLowerCase();
			var fileName = $('#filePath_JarFile').val().split('/').pop();
			if(ext != 'jar') {
				errorMsg = fileName + ': Invalid File, please upload a valid jar file';
				tooltipMsg = 'Invalid JAR File';
				com.impetus.ankush.validation.addNewErrorToDiv('filePath_JarFile','popover-content',errorMsg,tooltipMsg);
			}
		},

		// Submit Job Button Click Event
		submitJobs : function(clusterId_JobSubmit) {
			com.impetus.ankush.hadoopJobs.validateFields();
			
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			
			data = {};
			data.job = $.trim($('#submitJob_Job').val());
			data.jarPath = fileUpload_JobJarPath;
			data.jobArgs = new Array();
			var count_JobArg = tblJobArguments.fnGetData().length;
			for(var i = 1; i <= count_JobArg; i++) {
				data.jobArgs.push($.trim($("#jobArgument-"+i).val()));
			}
			//submitJobUrl = baseUrl + '/monitor/' + clusterId_JobSubmit + '/submitjob';
			var submitJobUrl = null;
			submitJobUrl = baseUrl + '/monitor/' + clusterId_JobSubmit + '/submitjob?component=Hadoop';
			

			com.impetus.ankush.placeAjaxCall(submitJobUrl, "POST", true, data, function(result) {
					if (result.output.status){
						$("#div_RequestSuccess").modal('show');
						$('.ui-dialog-titlebar').hide();
						$("#divOkbtn").click(function() {
							$('#div_RequestSuccess').modal('hide');
							com.impetus.ankush.hadoopJobs.clearPageFields();
						});
					}else {
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
						return;
					}
				
			});
		},

		// Function to Clear Page fields after job submission 
		clearPageFields : function() {
			$('#submitJob_Job').val("");
			$('#filePath_JarFile').val("");
			tblJobArguments.fnClearTable();
		},
		
		// Function to remove the Fake Path text in Chrome & Safari browsers during file upload
		removeFakePathFromPath : function(fieldId) {
			var value = $('#' + fieldId).val().replace('C:\\fakepath\\', '');
			$('#' + fieldId).val(value);
		},
	
		// Function to initialise the JAR file upload functionality
		hadoopJarFileUpload : function() {
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_JarFile').click();
			$('#fileBrowse_JarFile').change(
				function() {
					$('#filePath_JarFile').val(document.forms['uploadframeJarFile_Form'].elements['fileBrowse_JarFile'].value);
					com.impetus.ankush.hadoopJobs.uploadFile(uploadUrl,"fileBrowse_JarFile");
					com.impetus.ankush.hadoopJobs.removeFakePathFromPath('filePath_JarFile');
				});
		},
		
		// Function to upload the JAR file to the Ankush Server		
		uploadFile : function(uploadUrl, fileId, callback, context) {
			jsonFileUploadString = null;
			$.ajaxFileUpload({
				url : uploadUrl,
				secureuri : false,
				fileElementId : fileId,
				dataType : 'text',
				success : function(result) {
					if (callback)
						callback(result, context);
					var htmlObject = $(result);
					var resultData = new Object();
					eval("resultData  = " + htmlObject.text());
					fileUpload_JobJarPath = resultData.output;
				},
				error : function() {
					alert('Unable to upload the JAR file');
				}
			});
		},

		// Function to call page load event for Job Monitoring Page
		pageLoad_JobMonitoring : function(clusterId) {
			clusterId_JobMonitor = clusterId;
			com.impetus.ankush.hadoopJobs.initTables_JobMonitoring();
			com.impetus.ankush.hadoopJobs.getMonitoringPageContent(clusterId_JobMonitor);
		},
		
		/* Hadoop2 application level monitoring*/
		// Function to call page load event for Job Monitoring Page
		pageLoad_AppMonitoring : function(clusterId) {
			clusterId_JobMonitor = clusterId;
			com.impetus.ankush.hadoopJobs.initTables_JobMonitoring();
			com.impetus.ankush.hadoopJobs.getApplicationMonitoringPageContent(clusterId_JobMonitor);
		},
		//this will filter hadoop2 application table
		filterHadoop2App : function(val){
			$('#searchJobsTableHadoop').unbind('keyup');
			$("#tblJobMonitoring").dataTable().fnFilter(val);
		},
		getApplicationMonitoringPageContent : function(clusterId){
			clusterId_JobMonitor = clusterId;
			var jobMonitorUrl = null;
			jobMonitorUrl =  baseUrl + "/monitor/" + clusterId_JobMonitor + "/applist?component=Hadoop";
			com.impetus.ankush.placeAjaxCall(jobMonitorUrl,'GET',true,null,function(result){
				tblJobMonitoring.fnClearTable();
				if(result.output.status == true){
					if(result.output.hadoop2applist == undefined){
						tblJobMonitoring.fnSettings().oLanguage.sEmptyTable = "No Records Found";
						tblJobMonitoring.fnClearTable();
						return;
					}
						for(var i = 0 ; i < result.output.hadoop2applist.length ; i++){
							var navigationTopology = "<a href='"+baseUrl+"/hadoop-cluster-monitoring/appMonitoringDrillDown/"+clusterId+"/"+clusterName+"/"+clusterTechnology+"/"+hybridTechnology+"/"+result.output.hadoop2applist[i].id+"' class='fa fa-chevron-right' onclick='com.impetus.ankush.hadoopJobs.appMonitorDrillDownPage(\""+result.output.hadoop2applist[i].id+"\",\""+result.output.hadoop2applist[i].applicationType+"\");'></a>";
							tblJobMonitoring.fnAddData([
							                            result.output.hadoop2applist[i].id,
							                            result.output.hadoop2applist[i].user,
							                            result.output.hadoop2applist[i].name,	
							                            result.output.hadoop2applist[i].queue,
							                            result.output.hadoop2applist[i].state,
							                            result.output.hadoop2applist[i].finalStatus,
							                            result.output.hadoop2applist[i].applicationType,
							                            result.output.hadoop2applist[i].progress,

							                            result.output.hadoop2applist[i].startedTime,

							                            result.output.hadoop2applist[i].elapsedTime,
							                            navigationTopology
							                            ]);
						}
					
				}else{
					tblJobMonitoring.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
					tblJobMonitoring.fnClearTable();
					com.impetus.ankush.hadoopJobs.showErrorOnPageLoad(result.output.error, 'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
				}
			});
		},
		//
		appMonitorDrillDownPage : function(appid,applicationType){
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/appMonitoringDrillDown',
				method : 'get',
				title : 'Application Details',
				tooltipTitle : 'Application Monitoring',
				previousCallBack : "",
				callback : function(){
					com.impetus.ankush.hadoopJobs.appMonitorDrillDownLoad(appid,applicationType);
				},
				params : {
				}
			});
		},
		appMonitorDrillDownLoad : function(appid,applicationType){
			var clusterId_JobMonitor = clusterId;
			var appMonitorUrl = null;
			appMonitorUrl =  baseUrl + "/monitor/" + clusterId_JobMonitor + "/appdetails?appid="+appid+"&component=Hadoop";
			com.impetus.ankush.placeAjaxCall(appMonitorUrl,'GET',true,null,function(result){
				if(result.output.status == true){
					if(result.output.appattempts == undefined){
						appMonitorDrillDown.fnSettings().oLanguage.sEmptyTable = "No Records Found";
						appMonitorDrillDown.fnClearTable();
						return;
					}
						for(var i = 0 ; i < result.output.appattempts.length ; i++){
							appMonitorDrillDown.fnAddData([
							                            result.output.appattempts[i].id,
							                            new Date(result.output.appattempts[i].startTime).toUTCString()	,
							                            result.output.appattempts[i].nodeId,	
							                            result.output.appattempts[i].logsLink,
							                            result.output.appattempts[i].logsLink,
							                            result.output.appattempts[i].containerId,
							                            ]);
						}
						if(result.output.appdetails == undefined){
							return;
						}
						for(var key  in  result.output.appdetails){
							var text = key;
							text = text.replace( /([A-Z])/g, " $1" );
							text = text.charAt(0).toUpperCase()+text.slice(1);
							$("#all_tiles_appInfo").prev().children().children().text("Application Details");
							$("#all_tiles_appInfo").append('<div class="row"><div class="col-md-1 text-right">'+text+'</div><div class="col-md-11 text-left" style="color:#000000">'+result.output.appdetails[key]+'</div></div>')
						}
				}else{
					appMonitorDrillDown.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
					appMonitorDrillDown.fnClearTable();
					com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,
							'popover-content-appMonitoring', 'error-div-appMonitoring', 'errorBtnAppMonitoring');
				}		
			});
//			if(applicationType == 'MAPREDUCE')
//				com.impetus.ankush.hadoopJobs.appMonitorDrillDownLoad_jobDetail(appid,applicationType);
		},
		appMonitorDrillDownLoad_jobDetail : function(appid,applicationType){
			var appMonitorUrl = null;
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				appMonitorUrl =  baseUrl + "/monitor/" + clusterId_JobMonitor + "/appjobinfo?appid="+appid+"&technology=Hadoop";
			else
				appMonitorUrl = baseUrl + "/monitor/" + clusterId_JobMonitor + "/appjobinfo?appid="+appid;
			com.impetus.ankush.placeAjaxCall(appMonitorUrl,'GET',true,null,function(result){
				if(result.output.status == true){
					if(result.output.jobinfo == undefined){
						return;
					}
					for(var key  in  result.output.jobinfo){
						var text = key;
						text = text.replace( /([A-Z])/g, " $1" );
						text = text.charAt(0).toUpperCase()+text.slice(1);
						$("#all_tiles_JobInfo").prev().children().children().text("Job Details");
						$("#all_tiles_JobInfo").append('<div class="row-fluid"><div class="span2 text-right">'+text+'</div><div class="span2 text-left" style="color:#000000">'+result.output.jobinfo[key]+'</div></div>')
					}
				}else{
					com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,
							'popover-content-appMonitoring', 'error-div-appMonitoring', 'errorBtnAppMonitoring');
				}
			});
		},
		// Function to initialise data tables used in Job Monitoring Page		
		initTables_JobMonitoring : function() {
			if(tblJobMonitoring == null) {
				tblJobMonitoring = $("#tblJobMonitoring").dataTable({
					"bJQueryUI" : false,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : false,
					"bInfo" : false,
				});
				$('#tblJobMonitoring_filter').hide();
				$('#searchJobsTableHadoop').keyup(function(){
					$("#tblJobMonitoring").dataTable().fnFilter($(this).val());
				});
				$('#div_Request_JobMonitoring').appendTo('body');
			};
		},
		
		// Function to get job Monitoring Page Content (Tiles & Job List) 
		getMonitoringPageContent : function(clusterId) {
			clusterId_JobMonitor = clusterId;
			var jobMonitorUrl = baseUrl + '/monitor/' + clusterId_JobMonitor + '/hadoopjobs?component=Hadoop';
			jobMonitoringData = null;
			com.impetus.ankush.placeAjaxCall(jobMonitorUrl, "GET", true, null, function(result) {
					if(result.output.status){
						jobMonitoringData = result;
						com.impetus.ankush.hadoopJobs.loadJobsMonitoringPage();	
					}
					else {
						com.impetus.ankush.hadoopJobs.showErrorOnPageLoad(result.output.error, 'popover-content-hadoop', 'error-div-hadoop', 'errorBtnHadoop');
					}
			});
		},
		
		// Function to start auto-refresh call for Job Monitoring Page
		autoRefresh_JobMonitoring : function(functionName) {
			refreshInterval_JobMonitoring = setInterval(function() {
				eval(functionName);
			}, refreshTimeInterval_JobMonitoring);
		},

		// Function to stop auto-refresh call for Job Monitoring Page
		stopAutoRefresh_JobMonitoring : function() {
			IsAutoRefreshON_JobMonitoring = false;
			refreshInterval_JobMonitoring = window.clearInterval(refreshInterval_JobMonitoring);
		},
		
		// Function to implement Header Check-box click event functionality for Hadoop Jobs List table 
		checkAllNode_JobMonitoring : function() {
			if(tblJobMonitoring.fnGetData().length!=null) {
				var flag = false;
				if($('#checkHead_JobMonitoring').attr('checked')) {
					flag = true;
				}
				var jobCount = tblJobMonitoring.fnGetData().length;
				for(var i = 0; i < jobCount; i++) {
					if(!$('#checkboxJob-'+ i).attr('disabled')) {
						$('#checkboxJob-'+ i).attr('checked', flag);
						com.impetus.ankush.hadoopJobs.enableJobMonitorRow(i);	
					}
				}
			}
		},
		
		// Function to submit Kill Job request 
		killJobs : function(clusterId_JobMonitor) {
			if(tblJobMonitoring.fnGetData().length!=null) {
				var jobCount = tblJobMonitoring.fnGetData().length;
				data = {};
				data.jobs = new Array();
				for(var i = 0; i < jobCount; i++) {
					if($('#checkboxJob-'+ i).attr('checked')) {
						if( ($('#jobState-'+ i).text() == "RUNNING") || ($('#jobState-'+ i).text() == "PREP")) 
							data.jobs.push($('#jobId-'+ i).text());	
					}
				}

				if((Object.keys(data.jobs)).length == 0) {
					com.impetus.ankush.validation.showAjaxCallErrors(['Select atleast one running job to kill.'],
							'popover-content-jobDetail', 'error-div-jobDetail', 'errorBtnJobDetail');
					return;
				}
				else {
					var killJobsUrl = null;
					if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
						killJobsUrl = baseUrl + '/monitor/' + clusterId_JobMonitor + '/killjob?technology=Hadoop';
					else
						killJobsUrl = baseUrl+"/monitor/" + clusterId_JobMonitor + "/killjob";
					$('#checkHead_JobMonitoring').attr('checked', false);
					com.impetus.ankush.hadoopJobs.checkAllNode_JobMonitoring();
					com.impetus.ankush.placeAjaxCall(killJobsUrl, "POST", false, data, function(result) {
							if (result.output.status){
								$("#div_Request_JobMonitoring").modal('show');
								$('.ui-dialog-titlebar').hide();
								$("#lblJobRequestMessage").text('Kill Job request placed successfully');
								$("#divOkbtn_JobMonitoring").click(function() {
									$('#div_Request_JobMonitoring').modal('hide');
								});
							}
					});	
				}
			}
		},
	
		// Function to close request dialog box for job monitoring 
		closeDialog_Request_JobMonitoring : function() {
			$('#div_Request_JobMonitoring').modal('hide');
		},
		
		// Function to submit Update Job Priority request
		updateJobsPriority : function(clusterId_JobMonitor) {
			if(tblJobMonitoring.fnGetData().length!=null) {
				var jobCount = tblJobMonitoring.fnGetData().length;
				data = {};
				for(var i = 0; i < jobCount; i++) {
					if($('#checkboxJob-'+ i).attr('checked')) {
						if( ($('#jobState-'+ i).text() == "RUNNING") || ($('#jobState-'+ i).text() == "PREP")) 
							data[$('#jobId-'+ i).text()] = $('#jobPriority-'+ i).val();
					}
				}
				if((Object.keys(data)).length == 0) {
					com.impetus.ankush.validation.showAjaxCallErrors(['Select atleast one running job to update priority.'],
							'popover-content-jobDetail', 'error-div-jobDetail', 'errorBtnJobDetail');
					return;
				}
				else {
					var updateJobsPriorityUrl = null;
					if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
						updateJobsPriorityUrl = baseUrl + '/monitor/' + clusterId_JobMonitor + '/updatejobpriority?technology=Hadoop';
					else
						updateJobsPriorityUrl = baseUrl+"/monitor/" + clusterId_JobMonitor + "/updatejobpriority";
					$('#checkHead_JobMonitoring').attr('checked', false);
					com.impetus.ankush.hadoopJobs.checkAllNode_JobMonitoring();
					com.impetus.ankush.placeAjaxCall(updateJobsPriorityUrl, "POST", false, data, function(result) {
							if (result.output.status){
								$("#div_Request_JobMonitoring").modal('show');
								$('.ui-dialog-titlebar').hide();
								$("#lblJobRequestMessage").text('Update job priority request placed successfully');
							}
					});	
				}
			}
		},
		
		// Function to submit Update Job Priority request
		loadJobsMonitoringPage : function() { 
			//com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Tiles();
			com.impetus.ankush.hadoopJobs.loadJobsMonitoring_Table(currentJobState);
		},
		
		// Function to submit Update Job Priority request
		loadJobsMonitoring_Tiles : function() {
			$('#all_tiles_JobMonitoring').empty();
			var clusterTiles = com.impetus.ankush.tileReordring(jobMonitoringData.output.tiles);
			var tile = document.getElementById('all_tiles_JobMonitoring');
			for(var i = 0 ; i < clusterTiles.length ; i++){
				switch (clusterTiles[i].status) {
				case 'Error':
					tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="redTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Critical':
					tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="redTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Warning':
					tile.innerHTML += '<div class="item grid-1c2text warningbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="yellowTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Normal':
					tile.innerHTML += '<div class="item grid-1c2text infobox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="greenTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle">'+clusterTiles[i].line2+'</div></div></div>';
					break;
				}
				
			}
			$('.clip').tooltip();
			$('#all_tiles_JobMonitoring').masonry({ itemSelector : '.item',
				columnWidth : 100 });
		},
	
		// Function to get Priority Data for Job Priority dropdown
		getPriorityData : function(i, strPriority) {
			var priorityData = '';
			switch(strPriority){
			case 'VERY_HIGH':
				priorityData =  '<select disabled="disabled" id="jobPriority-'+ i + '" style="width: 105px;margin-top: 0px;margin-bottom: 0px;padding: 0px 0px;height: 25px">' +
				'<option value="VERY_HIGH" selected>VERY_HIGH</option>' +
				'<option value="HIGH">HIGH</option>' +
				'<option value="NORMAL">NORMAL</option>' +
				'<option value="LOW">LOW</option>' +
				'<option value="VERY_LOW">VERY_LOW</option>' +
				'</select>';
				break;
			case 'HIGH':
				priorityData =  '<select disabled="disabled" id="jobPriority-'+ i + '" style="width: 105px;margin-top: 0px;margin-bottom: 0px;padding: 0px 0px;height: 25px">' +
				'<option value="VERY_HIGH">VERY_HIGH</option>' +
				'<option value="HIGH" selected>HIGH</option>' +
				'<option value="NORMAL">NORMAL</option>' +
				'<option value="LOW">LOW</option>' +
				'<option value="VERY_LOW">VERY_LOW</option>' +
				'</select>';
				break;
			case 'NORMAL':
				priorityData =  '<select disabled="disabled" id="jobPriority-'+ i + '" style="width: 105px;margin-top: 0px;margin-bottom: 0px;padding: 0px 0px;height: 25px">' +
				'<option value="VERY_HIGH">VERY_HIGH</option>' +
				'<option value="HIGH">HIGH</option>' +
				'<option value="NORMAL" selected>NORMAL</option>' +
				'<option value="LOW">LOW</option>' +
				'<option value="VERY_LOW">VERY_LOW</option>' +
				'</select>';
				break;
			case 'LOW':
				priorityData =  '<select disabled="disabled" id="jobPriority-'+ i + '" style="width: 105px;margin-top: 0px;margin-bottom: 0px;padding: 0px 0px;height: 25px">' +
				'<option value="VERY_HIGH">VERY_HIGH</option>' +
				'<option value="HIGH">HIGH</option>' +
				'<option value="NORMAL">NORMAL</option>' +
				'<option value="LOW" selected>LOW</option>' +
				'<option value="VERY_LOW">VERY_LOW</option>' +
				'</select>';
				break;
			case 'VERY_LOW':
				priorityData =  '<select disabled="disabled" id="jobPriority-'+ i + '" style="width: 105px;margin-top: 0px;margin-bottom: 0px;padding: 0px 0px;height: 25px">' +
				'<option value="VERY_HIGH">VERY_HIGH</option>' +
				'<option value="HIGH">HIGH</option>' +
				'<option value="NORMAL">NORMAL</option>' +
				'<option value="LOW">LOW</option>' +
				'<option value="VERY_LOW" selected>VERY_LOW</option>' +
				'</select>';
				break;
			}
			return priorityData;
		},
		
		// Function to load jobs list table data
		loadJobsMonitoring_Table : function(strJobState) {
			currentJobState = strJobState;
			
			if((jobMonitoringData == null) || (jobMonitoringData.output.jobs == null))
				return;
			var jobCount_JSON = jobMonitoringData.output.jobs.length;
			var jobListData = jobMonitoringData.output.jobs;
			var tableData = [];
			var jobCount_Table = 0;
			for(var i = 0; i < jobCount_JSON; i++) {
				if(jobListData[i].jobState == currentJobState || currentJobState == 'ALL') {
					var jobListRow = [];
					var checkboxJob ='<input disabled="disabled" onclick="com.impetus.ankush.hadoopJobs.enableJobMonitorRow('+ jobCount_Table +');" type="checkbox" id="checkboxJob-'+ jobCount_Table +'" / >';
					if(jobListData[i].jobState == 'RUNNING') {
						checkboxJob ='<input onclick="com.impetus.ankush.hadoopJobs.enableJobMonitorRow('+ jobCount_Table +');" type="checkbox" id="checkboxJob-'+ jobCount_Table +'" / >';
					}
					var colJobId = '<span style="" id="jobId-'+jobCount_Table+'">'+jobListData[i].jobId+'</span>';
					var colJobName = '<span style="" id="jobName-'+jobCount_Table+'">'+jobListData[i].jobName+'</span>';
					var priorityData =  com.impetus.ankush.hadoopJobs.getPriorityData(jobCount_Table, jobListData[i].jobPriority);
					var colJobPriority = priorityData;
					var colMapProgress = '<span style="" id="mapProgress-'+jobCount_Table+'">'+jobListData[i].mapProgress+'</span>';
					var colReduceProgress = '<span style="" id="reduceProgress-'+jobCount_Table+'">'+jobListData[i].reduceProgress+'</span>';
					var colJobState = '<span style="" id="jobState-'+jobCount_Table+'">'+jobListData[i].jobState+'</span>';

					var navigationJob = '<div><a href="javascript:com.impetus.ankush.hadoopJobs.loadJobChildPage(\'' + jobListData[i].jobId + '\')" id="navigationImgJob-'+ i 
					+ '"><img  src="'+baseUrl+'/public/images/icon-chevron-right.png" /></a></div>';

					jobListRow.push(checkboxJob);
					jobListRow.push(colJobId);
					jobListRow.push(colJobName);
					jobListRow.push(colJobPriority);
					jobListRow.push(colMapProgress);
					jobListRow.push(colReduceProgress);
					jobListRow.push(colJobState);
					
					tableData.push(jobListRow);
					jobCount_Table++;	
				}
			}
			tblJobMonitoring.fnClearTable();
			tblJobMonitoring.fnAddData(tableData);
		},

		// Function to enable Job check box based on its state
		enableJobMonitorRow : function(rowId) {
			if($("#checkboxJob-"+rowId).attr("checked")) {
				$("#jobPriority-"+rowId).attr("disabled", false);
			}
			else {
				$("#jobPriority-"+rowId).attr("disabled", true);
			}
		},
		
		// Function to open Job details page
		loadJobChildPage : function(jobId) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/jobDetails/'+jobId,
				method : 'get',
				title : 'Job Details',
				tooltipTitle : 'Job Monitoring',
				previousCallBack : "com.impetus.ankush.hadoopJobs.previousCallback_JobDetaills();",
				params : {
				}
			});
		},

		// Previous Callback Function for Job details page to stop auto-refresh calls (if any) and refresh Job Monitoring data
		previousCallback_JobDetaills : function() {
			//com.impetus.ankush.hadoopJobs.stopAutoRefresh_JobDetails();
			com.impetus.ankush.hadoopJobs.loadJobsMonitoringPage();
		},

		// Function to get Job Details on page load event of Job Details page
		loadJobDetails : function(jobId) {
			
			var jobMonitorUrl = null;
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
				jobMonitorUrl = baseUrl + "/monitor/" + clusterId_JobMonitor + "/hadoopjobs?technology=Hadoop";
			}
			else
				jobMonitorUrl = baseUrl + "/monitor/" + clusterId_JobMonitor + "/hadoopjobs";
			jobMonitoringData = null;
			com.impetus.ankush.placeAjaxCall(jobMonitorUrl, "GET", true, null, function(result) {
					if(result.output.status) {
						jobMonitoringData = result;
						
						var jobCount_JSON = jobMonitoringData.output.jobs.length;
						var jobListData = jobMonitoringData.output.jobs;
						var jobObject = null;
						for(var i = 0; i < jobCount_JSON; i++) {
							if(jobListData[i].jobId == jobId){
								jobObject = jobListData[i];
								break;
							}
						}
						if(jobObject == null) {
							var arrErrors = new Array();
							arrErrors.push("Error: Unable to fetch job data");
							com.impetus.ankush.hadoopJobs.showErrorOnPageLoad(arrErrors, 'popover-content_JobDetails', 'error-div-hadoop_JobDetails', 'errorBtnHadoop_JobDetails');							
						}
						else {
							com.impetus.ankush.hadoopJobs.loadJobDetails_Content(jobObject);
						}	
					}
					else {
						com.impetus.ankush.hadoopJobs.showErrorOnPageLoad(result.output.error, 'popover-content_JobDetails', 'error-div-hadoop_JobDetails', 'errorBtnHadoop_JobDetails');
					}
			});
		},
		

		// Function to populate Job Details on page load event of Job Details page		
		loadJobDetails_Content : function(jobObject) {
			if(document.getElementById('jobDetails_JobId') == null) {
				//com.impetus.ankush.hadoopJobs.stopAutoRefresh_JobDetails();
				return;				
			}
			$('#jobDetails_JobId').text(jobObject.jobId);
			$('#jobDetails_jobName').text(jobObject.jobName);
			$('#jobDetails_jobState').text(jobObject.jobState);
			$('#jobDetails_userName').text(jobObject.userName);
			$('#jobDetails_jobPriority').text(jobObject.jobPriority);
			$('#jobDetails_jobStartTime').text($.format.date(jobObject.jobStartTime, "dd/MM/yyyy hh:mm:ss"));
			$('#jobDetails_schedulingInfo').text(jobObject.schedulingInfo);
			$('#jobDetails_jobComplete').text(jobObject.jobComplete);
			$('#jobDetails_mapProgress').text(jobObject.mapProgress);
			$('#jobDetails_mapTotal').text(jobObject.mapTotal);
			$('#jobDetails_mapCompleted').text(jobObject.mapCompleted);
			$('#jobDetails_reduceProgress').text(jobObject.reduceProgress);
			$('#jobDetails_reduceTotal').text(jobObject.reduceTotal);
			$('#jobDetails_reduceCompleted').text(jobObject.reduceCompleted);

		},
};
