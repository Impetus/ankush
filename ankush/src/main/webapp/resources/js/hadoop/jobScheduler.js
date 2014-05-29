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

var capacityQueueTable = null;
var jobSchedulerClusterId  = null;
var successText = "Update Job Scheduler request has been sent successfully";
var jobSchedulerUrl = null;
var jobSchedulerPostData = {};
var jobResult = null;
var deleteText = "Are you sure you want to delete queue?";
var deletedQueueRowId = [];
var deletedQueueRowData =[];
var jsonObjFairScheduler = null;
var tblFS_Users = null;
var tblFS_Pools = null;

function queueTemplate()
{
	this.queue = '';
	this.capacity = '';
	this.maxCapacity = '';
	this.minUserLimit='';
	this.userLimit = '';
	this.prioritySupport= false;
	this.maxActiveTasks='';
	this.maxActiveTasksUser='';
	this.jobCount='';
}

function template_FairCommon() {
	this.pool = '';
	this.preemption = '';
	this.allocationFile = '';
	this.sizeBasedWeight = 'false';
	this.preemptionOnlyLog = 'false';
	this.updateInterval = 500;
	this.preemptionInterval = 15000;
}

function template_FairDefault() {
	this.poolMaxJobsDefault = 5;
	this.userMaxJobsDefault = 5;
	this.defaultMinSharePreemptionTimeout = 600;
	this.fairSharePreemptionTimeout = 600;
	this.defaultPoolSchedulingMode = 'fair';
}

function template_FairUser() {
	this.name = '';
	this.maxRunningJobs = 5;
}

function template_FairPool() {
	this.name = '';
	this.minMaps = 0;
	this.minReduces = 0;
	this.maxMaps = 5;
	this.maxReduces = 5;
	this.maxRunningJobs= $('#jobFS_poolMaxJobsDefault').val();
	this.weight = '1.0';
}

com.impetus.ankush.jobScheduler={
		queueDetails : new Array(),
		
		// function used to append queue delete dialog to page body
		appendModalPopUp : function() {
			$("#deleteQueueDialog").appendTo('body');
			$("#div_RequestPlaced_JobScheduler").appendTo('body');
		},

		// function used to call validate function depending on the type of scheduler is to be configured
		validateJobScheduler : function() {

			$("#popover-content").empty();
			$("#error-div-hadoop").css('display', 'none');
			$("#errorBtnHadoop").css('display', 'none');
			
			// for fair scheduler
			com.impetus.ankush.validation.errorCount = 0;
			if($('#jobScheduler_capacity').attr("checked")) {
				com.impetus.ankush.jobScheduler.validateCapacityScheduler();
			}
			else if($('#jobScheduler_fair').attr("checked")) {
				com.impetus.ankush.jobScheduler.validateFairScheduler();
			}
		},

		// function used to validate capacity scheduler
		validateCapacityScheduler : function() {
			com.impetus.ankush.validation.validateField('numeric', 'jobSchedulerCapacity_pollingInterval', 'Polling Interval', 'popover-content');
			com.impetus.ankush.validation.validateField('numeric', 'jobSchedulerCapacity_workerThreads', 'Worker Threads', 'popover-content');
			com.impetus.ankush.validation.validateField('numeric', 'jobSchedulerCapacity_maxsystemJobs', 'Max. system Jobs', 'popover-content');
			com.impetus.ankush.validation.validateField('numeric', 'jobSchedulerCapacity_maxActivetasks_queue', 'Max. Active Tasks/Queue', 'popover-content');
			com.impetus.ankush.validation.validateField('numeric', 'jobSchedulerCapacity_maxActivetasks_user', 'Max. Active Tasks/User', 'popover-content');
			com.impetus.ankush.validation.validateField('numeric', 'jobSchedulerCapacity_jobCount', 'Job Count', 'popover-content');

			if($('#jobSchedulerCapacity_minUserLimit').val()!=""){
				com.impetus.ankush.validation.validateField('between_O_100', 'jobSchedulerCapacity_minUserLimit', 'Min. User Limit', 'popover-content');
			}
			if(jobResult.output.hasOwnProperty('queues')){
				if(jobResult.output.queues.length<1){
					errorMsg = 'There must be atleast 1 queue to use capacity scheduler';
					tooltipMsg = 'Add at least One Queue';
					com.impetus.ankush.validation.addNewErrorToDiv('divCapacityQueueTable','popover-content',errorMsg,tooltipMsg);
				}
				else {
					var queueName = null;
					var totalCapacity = 0;
					for(var a=0;a<jobResult.output.queues.length;a++){
						queueName=jobResult.output.queues[a].name;
						if(queueName == 'Empty') {
							com.impetus.ankush.jobScheduler.addErrorQueueDetailEmpty('Queue Name', (parseInt(a, 10)+1));
						}
						if($('#capacity-'+queueName).text() == 'Empty') {
							com.impetus.ankush.jobScheduler.addErrorQueueDetailEmpty('Capacity', (parseInt(a, 10)+1));
						}
						if($('#maxCapacity-'+queueName).text() == 'Empty') {
							com.impetus.ankush.jobScheduler.addErrorQueueDetailEmpty('Maximum Capacity', (parseInt(a, 10)+1));
						}
						if($('#minUserLimit-'+queueName).text() == 'Empty') {
							com.impetus.ankush.jobScheduler.addErrorQueueDetailEmpty('Minimum User Limit', (parseInt(a, 10)+1));
						}
						var temp_Capacity = parseInt($('#capacity-'+queueName).text(), 10);

						if ( !isNaN(temp_Capacity) )
							totalCapacity += temp_Capacity;
					}
					if(totalCapacity != 100) {
						errorMsg = 'Sum of capacities for all queues should be 100%';
						tooltipMsg = 'Invalid Total Capacity for Queues';
						com.impetus.ankush.validation.addNewErrorToDiv('divCapacityQueueTable','popover-content',errorMsg,tooltipMsg);
					}
				}
			}else {
				errorMsg = 'There must be atleast 1 queue to use capacity scheduler';
				tooltipMsg = 'There must be atleast 1 queue to use capacity scheduler';
				com.impetus.ankush.validation.addNewErrorToDiv('divCapacityQueueTable','popover-content',errorMsg,tooltipMsg);
			}
		},
		
		// function used to validate whether queue added has any of element empty
		addErrorQueueDetailEmpty : function(field, rowIndex) {
			errorMsg = 'Invalid Queue: ' + field + ' cannot be empty - Row ' + rowIndex;
			com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
		},

		// function used to validate capacity scheduler queues
		validateCapacityQueue : function(i) {
			$("#error-div-queue").css("display", "none");
			$('#errorBtnQueue').css("display", "none");
			$("#popover-content-queue").empty();
			com.impetus.ankush.validation.errorCount = 0;
			com.impetus.ankush.validation.validateField('required', 'capacitySchedulerQueue-'+i, 'Queue Name', 'popover-content-queue');
			com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerCapacity-'+i, 'Queue Capacity', 'popover-content-queue');
			com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerMaxCapacity-'+i, 'Maximum Capacity', 'popover-content-queue');
			if($('#capacitySchedulerMaxCapacity-'+i).val() != '-1') {
				com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerMaxCapacity-'+i, 'Maximum Capacity', 'popover-content-queue');
			}
			com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerMinUserLimit-'+i, 'Minimum User Limit','popover-content-queue');
			com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerUserLimit-'+i, 'User Limit', 'popover-content-queue');
			com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerMaxActivityTasks-'+i, 'Maximum Active Tasks/Queue', 'popover-content-queue');
			com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerMaxActivityTasksVal-'+i, 'Maximum Active Tasks/User', 'popover-content-queue');
			com.impetus.ankush.validation.validateField('numeric', 'capacitySchedulerJobCount-'+i, 'Job Count', 'popover-content-queue');
			if($('#capacitySchedulerCapacity-'+i).val()!=""){
				com.impetus.ankush.validation.validateField('between_O_100', 'capacitySchedulerCapacity-'+i, 'Queue Capacity', 'popover-content-queue');
				if($('#capacitySchedulerMaxCapacity-'+i).val() != '-1'){

					if (parseInt($('#capacitySchedulerCapacity-'+i).val(), 10) > parseInt($('#capacitySchedulerMaxCapacity-'+i).val(), 10)) {
						errorMsg = 'Queue Capacity should be less than its Maximum Capacity';
						tooltipMsg = 'Invalid Queue Capacity';
						com.impetus.ankush.validation.addNewErrorToDiv('capacitySchedulerCapacity-'+i,'popover-content-queue',errorMsg,tooltipMsg);
					}	
				}
			}
			if($('#capacitySchedulerMaxActivityTasks-'+i).val()!=""){
				com.impetus.ankush.validation.validateField('between_O_100', 'capacitySchedulerMaxActivityTasks-'+i, 'Maximum Active Tasks', 'popover-content-queue');
			}
			if(jobResult.output.queues!=null){
				if($('#capacitySchedulerQueue-'+i).val()!=i){
					for(var a=0;a<jobResult.output.queues.length;a++){
						if(jobResult.output.queues[a].name==$('#capacitySchedulerQueue-'+i).val()){
							errorMsg = 'Queue with queue name '+$('#capacitySchedulerQueue-'+i).val()+ ' already exists';
							tooltipMsg = 'Queue with queue name '+$('#capacitySchedulerQueue-'+i).val()+ ' already exists';
							com.impetus.ankush.validation.addNewErrorToDiv('capacitySchedulerQueue-'+i,'popover-content-queue',errorMsg,tooltipMsg);
						}
					}
				}
			}
		},


		// function used to show default scheduler details
		showDefaultScheduler : function() {
			$("#div_capacityScheduler").hide();
			$("#div_fairScheduler").hide();
			$("#error-div-hadoop").css("display", "none");
		},

		// function used to show capacity scheduler details
		showCapacityScheduler : function() {
			$("#div_capacityScheduler").show();
			$("#div_fairScheduler").hide();
		},

		// function used to show fair scheduler details
		showFairScheduler : function() {
			$("#div_capacityScheduler").hide();
			$("#error-div-hadoop").css("display", "none");
			$("#div_fairScheduler").show();
		},

		// function used to called functions to validate fair-scheduler
		validateFairScheduler : function() {
			com.impetus.ankush.jobScheduler.validateCommonValues();
			com.impetus.ankush.jobScheduler.validateDefaultValues();
			com.impetus.ankush.jobScheduler.validateUsers();
			com.impetus.ankush.jobScheduler.validatePools();
		},

		// function used to validate common values in fair-scheduler
		validateCommonValues : function() {
			var poolName = $('#jobFS_pool').val();
			if(poolName == 'Select Pool') {
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'jobFS_poolNameProperty', 'Pool Name Property', 'popover-content');
			}
			else {
				$('#jobFS_poolNameProperty').removeClass('error-box');
				$('#jobFS_poolNameProperty').tooltip();
				com.impetus.ankush.common.tooltipOriginal('jobFS_poolNameProperty', 'Jobconf property to determine the pool that a job belongs in');
			}
			
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'jobFS_allocationFile', 'Allocation File', 'popover-content');
			com.impetus.ankush.validation.validateField('greaterThan_0', 'jobFS_updateInterval', 'Update Interval', 'popover-content');
			com.impetus.ankush.validation.validateField('greaterThan_0', 'jobFS_preemptionInterval', 'Preemption Interval', 'popover-content');
		},

		// function used to validate override values in fair-scheduler
		validateDefaultValues : function() {
			com.impetus.ankush.validation.validateField('greaterThan_0', 'jobFS_poolMaxJobsDefault', 'Maximum running jobs allowed per pool', 'popover-content');
			com.impetus.ankush.validation.validateField('greaterThan_0', 'jobFS_userMaxJobsDefault', 'Maximum running jobs allowed per user', 'popover-content');
			com.impetus.ankush.validation.validateField('greaterThan_0', 'jobFS_defaultMinSharePreemptionTimeout', 'Minimum Share Preemption Timeout', 'popover-content');
			com.impetus.ankush.validation.validateField('greaterThan_0', 'jobFS_fairSharePreemptionTimeout', 'Fair Share Preemption Timeout', 'popover-content');
		},

		// function used to validate users in fair-scheduler
		validateUsers : function() {
			var count_Users = tblFS_Users.fnGetData().length;
			for(var i = 0; i < count_Users; i++) {
				if($('#user_name-' + i).hasClass('editable-empty')) {
					errorMsg = "Invalid User: User Name cannot be empty - Row " + (i+1) + ".";
					com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
				}
				if($('#user_maxRunningJobs-' + i).hasClass('editable-empty')) {
					errorMsg = "Invalid User: Max Running Job cannot be empty - Row " + (i+1) + ".";
					com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
				}
			}
		},

		// function used to validate pools in fair-scheduler
		validatePools : function() {
			var count_Pools = tblFS_Pools.fnGetData().length;
			for(var i = 0; i < count_Pools; i++) {
				if($('#name-' + i).hasClass('editable-empty')) {
					com.impetus.ankush.jobScheduler.updateErrorBoxForPool("Pool Name", i);
				}
				if($('#minMaps-' + i).hasClass('editable-empty')) {
					com.impetus.ankush.jobScheduler.updateErrorBoxForPool('Minimum Maps', i);
				}
				if($('#minReduces-' + i).hasClass('editable-empty')) {
					com.impetus.ankush.jobScheduler.updateErrorBoxForPool('Minimum Reduces', i);
				}
				if($('#maxMaps-' + i).hasClass('editable-empty')) {
					com.impetus.ankush.jobScheduler.updateErrorBoxForPool('Maximum Maps', i);
				}
				if($('#maxReduces-' + i).hasClass('editable-empty')) {
					com.impetus.ankush.jobScheduler.updateErrorBoxForPool('Maximum Reduces', i);
				}
				if($('#maxRunningJobs-' + i).hasClass('editable-empty')) {
					com.impetus.ankush.jobScheduler.updateErrorBoxForPool('Maximum Running Jobs', i);
				}
				if($('#weight-' + i).hasClass('editable-empty')) {
					com.impetus.ankush.jobScheduler.updateErrorBoxForPool('Weight', i);
				}
			}
		},

		// function used to validate pool values
		updateErrorBoxForPool : function(fieldName, rowId) {
			errorMsg = "Invalid Pool: " + fieldName + " cannot be empty - Row " + (rowId+1) + ".";
			com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
		},

		// function used to call functions to show fair-scheduler values
		fillFairSchedulerContents : function(jsonObjFS) {
			com.impetus.ankush.jobScheduler.fillFairSchedulerCommonsValues(jsonObjFS.commons);
			com.impetus.ankush.jobScheduler.fillFairSchedulerDefaultsValues(jsonObjFS.defaults);
			com.impetus.ankush.jobScheduler.fillFairSchedulerUsers(jsonObjFS.users);
			com.impetus.ankush.jobScheduler.fillFairSchedulerPools(jsonObjFS.pools);
			if(document.getElementById("poolName_" + jsonObjFS.commons.pool) != null) {
				$('#poolName_' + jsonObjFS.commons.pool).attr('selected', true);	
			}
		},

		// function used to show fair-scheduler common values
		fillFairSchedulerCommonsValues : function(jsonObjFS_Common) {
			$('#jobFS_pool').val(jsonObjFS_Common.pool);
			$('#jobFS_poolNameProperty').val(jsonObjFS_Common.poolnameProperty);
			$('#jobFS_allocationFile').val(jsonObjFS_Common.allocationFile);
			$('#jobFS_updateInterval').val(jsonObjFS_Common.updateInterval);
			$('#jobFS_preemptionInterval').val(jsonObjFS_Common.preemptionInterval);

			if(jsonObjFS_Common.preemption) {
				$('#jobFS_disablePreemption').removeClass('active');
				$('#jobFS_enablePreemption').addClass('active');
			}
			else {
				$('#jobFS_enablePreemption').removeClass('active');
				$('#jobFS_disablePreemption').addClass('active');
			}

			if(jsonObjFS_Common.sizeBasedWeight) {
				$('#jobFS_disableSizeBasedWeigth').removeClass('active');
				$('#jobFS_enableSizeBasedWeigth').addClass('active');
			}
			else {
				$('#jobFS_enableSizeBasedWeigth').removeClass('active');
				$('#jobFS_disableSizeBasedWeigth').addClass('active');
			}

			if(jsonObjFS_Common.preemptionOnlyLog) {
				$('#jobFS_disablePreemptionOnlyLog').removeClass('active');
				$('#jobFS_enablePreemptionOnlyLog').addClass('active');
			}
			else {
				$('#jobFS_enablePreemptionOnlyLog').removeClass('active');
				$('#jobFS_disablePreemptionOnlyLog').addClass('active');
			}
		},

		// function used to show fair-scheduler default values
		fillFairSchedulerDefaultsValues : function(jsonObjFS_Default) {
			$('#jobFS_poolMaxJobsDefault').val(jsonObjFS_Default.poolMaxJobsDefault);
			$('#jobFS_userMaxJobsDefault').val(jsonObjFS_Default.userMaxJobsDefault);
			$('#jobFS_defaultMinSharePreemptionTimeout').val(jsonObjFS_Default.defaultMinSharePreemptionTimeout);
			$('#jobFS_fairSharePreemptionTimeout').val(jsonObjFS_Default.fairSharePreemptionTimeout);

			if(jsonObjFS_Default.defaultPoolSchedulingMode == "fair") {
				$('#jobFS_defaultPoolSchedulingMode_Fifo').removeClass('active');
				$('#jobFS_defaultPoolSchedulingMode_Fair').addClass('active');
			}
			else {
				$('#jobFS_defaultPoolSchedulingMode_Fair').removeClass('active');
				$('#jobFS_defaultPoolSchedulingMode_Fifo').addClass('active');
			}
		},

		// function used to show fair-scheduler users
		fillFairSchedulerUsers : function(jsonObjFS_Users) {
			var count_Users = jsonObjFS_Users.length;
			tblFS_Users.fnClearTable();
			for(var i = 0; i < count_Users; i++) {
				com.impetus.ankush.jobScheduler.addNewUserToTable(jsonObjFS_Users[i], i);
			}
		},

		// function used to show fair-scheduler pools
		fillFairSchedulerPools : function(jsonObjFS_Pools) {
			var count_Pools = jsonObjFS_Pools.length;
			tblFS_Pools.fnClearTable();
			for(var i = 0; i < count_Pools; i++) {
				com.impetus.ankush.jobScheduler.addNewPoolToTable(jsonObjFS_Pools[i], i);
			}
		},

		// function used to initialize users table in fair-scheduler
		addNewUserToTable : function(objUser, i) {
			var tableData = [];
			var rowData = [];
			var col1_name = '<span  class="editableLabel_jobFSUser" style="font-weight:bold;" id="user_name-' + i + '"/>'+objUser.name+'</span>';
			var col2_maxRunningJobs = '<span  class="editableLabel_jobFSUser" style="font-weight:bold;" id="user_maxRunningJobs-' + i + '"/>'+objUser.maxRunningJobs+'</span>';
			var col3_DeleteUser ='<a href="#"><img id="deleteUser-' + i +'"' 
			+ ' src="'+baseUrl +'/public/images/newUI-Icons/circle-minus.png"'
			+ ' onclick="com.impetus.ankush.jobScheduler.DeleteUser(' + i + ');"/></a>';

			rowData.push(col1_name);
			rowData.push(col2_maxRunningJobs);
			rowData.push(col3_DeleteUser);
			tableData.push(rowData);
			tblFS_Users.fnAddData(tableData);
			$('.editableLabel_jobFSUser').editable({
				type : 'text',
				validate :function(value){
					return com.impetus.ankush.jobScheduler.saveUserTableValues(this, value);}
			});
		},

		// function used to save users value in fair-scheduler
		saveUserTableValues : function(elem, newValue) {
			var propertyName = (elem.id.split('-')[0]).substring(5);
			var rowIndex = elem.id.split('-')[1];
			if(propertyName == 'name') {
				if(!com.impetus.ankush.validation.pattern(newValue,/^[a-z][-a-z0-9_]*$/)) {
					return 'Invalid Name';
				}
				var countUsers = jsonObjFairScheduler.users.length;
				for(var i = 0; i < countUsers; i++) {
					if(rowIndex != i) {
						if(jsonObjFairScheduler.users[i][propertyName] == newValue) {
							return 'Already Exists';
						}
					}
				}
			}
			else {
				if(!com.impetus.ankush.validation.pattern(newValue,/^[\d]*$/)) {
					return 'Invalid Value';
				}
			}
			jsonObjFairScheduler.users[rowIndex][propertyName] = newValue;
			return false;
		},

		// function used to add a user to a user table in fair-scheduler
		AddNewUser : function() {
			var objUser = new template_FairUser();
			if(jsonObjFairScheduler == null) {
				com.impetus.ankush.jobScheduler.initializeFairJSONObject();
			}
			var userIndex = tblFS_Users.fnGetData().length;
			jsonObjFairScheduler.users.push(objUser);
			com.impetus.ankush.jobScheduler.addNewUserToTable(objUser, userIndex);
		},

		// function used to delete user in fair-scheduler
		DeleteUser : function(rowId) {
			var jsonObjUsers_tmp = jsonObjFairScheduler.users;
			delete jsonObjFairScheduler.users;
			jsonObjFairScheduler.users = new Array();

			var count_Users = tblFS_Users.fnGetData().length;

			for(var i = 0; i < count_Users; i++) {
				if(i == rowId) {
					tblFS_Users.fnDeleteRow(i);
				}
				else{
					var rowIndex = jsonObjFairScheduler.users.length;
					jsonObjFairScheduler.users.push(jsonObjUsers_tmp[i]);
					$('#deleteUser-' + i).removeAttr('onclick');
					$('#deleteUser-' + i).attr('onclick', 'com.impetus.ankush.jobScheduler.DeleteUser(' + (rowIndex) + ');');
					$('#deleteUser-' + i).attr('id' , 'deleteUser-' + rowIndex);
					$('#user_name-' + i).attr('id' , 'user_name-' + rowIndex);
					$('#user_maxRunningJobs-' + i).attr('id' , 'user_maxRunningJobs-' + rowIndex);
				}

			}
			Window.location.hash = "hidden_divFairUserTable";
		},

		// function used to add a pool in fair-scheduler
		addNewPoolToTable : function(objPool, i) {
			var tableData = [];
			var rowData = [];
			var col1_name = '<span  class="editableLabel_jobFSPool" style="font-weight:bold;" id="name-' + i + '"/>'+objPool.name+'</span>';
			var col2_minMaps = '<span  class="editableLabel_jobFSPool" style="font-weight:bold;" id="minMaps-' + i + '"/>'+objPool.minMaps+'</span>';
			var col3_minReduces = '<span  class="editableLabel_jobFSPool" style="font-weight:bold;" id="minReduces-' + i + '"/>'+objPool.minReduces+'</span>';
			var col4_maxMaps = '<span  class="editableLabel_jobFSPool" style="font-weight:bold;" id="maxMaps-' + i + '"/>'+objPool.maxMaps+'</span>';
			var col5_maxReduces = '<span  class="editableLabel_jobFSPool" style="font-weight:bold;" id="maxReduces-' + i + '"/>'+objPool.maxReduces+'</span>';
			var col6_maxRunningJobs = '<span  class="editableLabel_jobFSPool" style="font-weight:bold;" id="maxRunningJobs-' + i + '"/>'+objPool.maxRunningJobs+'</span>';
			var col7_Weight = '<span  class="editableLabel_jobFSPool" style="font-weight:bold;" id="weight-' + i + '"/>'+objPool.weight+'</span>';
			var col8_DeletePool ='<a href="#"><img id="deletePool-' + i +'"' 
			+ ' src="'+baseUrl +'/public/images/newUI-Icons/circle-minus.png"'
			+ ' onclick="com.impetus.ankush.jobScheduler.DeletePool(' + i + ');"/></a>';

			rowData.push(col1_name);
			rowData.push(col2_minMaps);
			rowData.push(col3_minReduces);
			rowData.push(col4_maxMaps);
			rowData.push(col5_maxReduces);
			rowData.push(col6_maxRunningJobs);
			rowData.push(col7_Weight);
			rowData.push(col8_DeletePool);
			tableData.push(rowData);

			tblFS_Pools.fnAddData(tableData);
			$('.editableLabel_jobFSPool').editable({
				type : 'text',
				validate :function(value){
					return com.impetus.ankush.jobScheduler.savePoolTableValues(this, value);
				}
			});
			if(objPool.name != '')
				$('#jobFS_pool').append('<option id="poolName_'+ objPool.name + '" value="' + objPool.name + '">' + objPool.name + '</option>');
		},

		// function used to save pools value in fair-scheduler
		savePoolTableValues : function(elem, newValue) {
			var propertyName = elem.id.split('-')[0];
			var rowIndex = elem.id.split('-')[1];
			var poolList = document.getElementById('jobFS_pool').options;
			var flag_Valid = true;
			if(propertyName == 'name') {
				if(!com.impetus.ankush.validation.pattern(newValue,/^[a-zA-Z0-9_-]*$/)) {
					return 'Invalid Name';
				}
				for(var i = 1; i < poolList.length; i++) {
					if(poolList[i].value.toUpperCase() == newValue.toUpperCase()) {
						if(!((rowIndex+1) == i)) {
							flag_Valid = false;
							break;	
						}
					}
				}
				if(flag_Valid) {
					var flag_Added = false;
					for(var i = 1; i < poolList.length; i++) {
						if(poolList[i].value.toUpperCase() == elem.innerHTML.toUpperCase()) {
							var optionPoolName = document.getElementById('poolName_'+elem.innerHTML);
							optionPoolName.selected = false;
							optionPoolName.innerHTML = newValue;
							optionPoolName.value = newValue;
							flag_Added = true;
							break;
						}
					}
					if(!flag_Added) {
						$('#jobFS_pool').append('<option id="poolName_'+ newValue + '" value="' + newValue + '">' + newValue + '</option>');
					}
				}
				else {
					return 'Already Exists';
				}
			}
			else if(propertyName == 'weight') {
				if(!com.impetus.ankush.validation.pattern(newValue,/^[\d]*.[\d]*$/)) {
					return 'Invalid Weight';
				}
			}
			else {
				if(!com.impetus.ankush.validation.pattern(newValue,/^[\d]*$/)) {
					return 'Invalid Value';
				}
			}
			jsonObjFairScheduler.pools[rowIndex][propertyName] = newValue;
			return false;
		},
		
		// function used to initialize users & pools table in fair-scheduler 
		initTables_FairScheduler : function() {
			if(tblFS_Users == null){		
				tblFS_Users = $("#FairUserTable").dataTable({
					"bJQueryUI" : false,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : false,
					"bInfo" : false,
				});
				$('#FairUserTable_filter').hide();
				$('#jobFS_searchUsers').keyup(function(){
					tblFS_Users.fnFilter( $('#jobFS_searchUsers').val());
				});
			}
			if(tblFS_Pools == null){		

				tblFS_Pools = $("#FairPoolTable").dataTable({
					"bJQueryUI" : false,
					"bPaginate" : false,
					"bLengthChange" : false,
					"bFilter" : true,
					"bSort" : false,
					"bInfo" : false,
					'fnRowCallback': function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
						$(nRow).attr('id','rowId_jobFSPool_'+iDisplayIndex);
						$(nRow).addClass('selectedPool');
					}
				});
				$('#FairPoolTable_filter').hide();
				$('#jobFS_searchPools').keyup(function(){
					tblFS_Pools.fnFilter( $('#jobFS_searchPools').val());
				});
			}
		},
		
		// function used to jump to pools table in fair-scheduler
		goToPoolsTable : function() {
			window.location.hash = 'jump_divFairPoolTable';
		},
		
		// function used to jump to users table in fair-scheduler
		goToUsersTable : function() {
			window.location.hash = 'jump_divFairUserTable';
		},
		
		
		// function used to add a pool in fair-scheduler
		AddNewPool : function() {
			var objPool = new template_FairPool();
			if(jsonObjFairScheduler == null) {
				com.impetus.ankush.jobScheduler.initializeFairJSONObject();
			}
			var poolIndex = tblFS_Pools.fnGetData().length;
			jsonObjFairScheduler.pools.push(objPool);
			com.impetus.ankush.jobScheduler.addNewPoolToTable(objPool, poolIndex);
		},

		// function used to delete a pool in fair-scheduler 
		DeletePool : function(rowId) {
			var jsonObjPools_tmp = jsonObjFairScheduler.pools;
			delete jsonObjFairScheduler.pools;
			jsonObjFairScheduler.pools = [];

			var count_Pools = tblFS_Pools.fnGetData().length;
			for(var i = 0; i < count_Pools; i++) {
				if(i == rowId) {
					var poolList = document.getElementById('jobFS_pool').options;
					var delIndex = null;
					for(var iCount = 1; iCount < poolList.length; iCount++) {
						if(poolList[iCount].value == $('#name-'+i).text()) {
							var optionPoolName = document.getElementById("poolName_"+$('#name-'+i).text());
							optionPoolName.selected = false;
							delIndex = iCount;
							break;
						}
					}
					if(delIndex != null)
						document.getElementById('jobFS_pool').remove(delIndex);
					tblFS_Pools.fnDeleteRow(i);
				}
				else{
					var rowIndex = jsonObjFairScheduler.pools.length;
					jsonObjFairScheduler.pools.push(jsonObjPools_tmp[i]);
					$('#deletePool-' + i).removeAttr('onclick');
					$('#deletePool-' + i).attr('onclick', 'com.impetus.ankush.jobScheduler.DeletePool(' + (rowIndex) + ');');
					$('#deletePool-' + i).attr('id' , 'deletePool-' + rowIndex);

					$('#name-' + i).attr('id' , 'name-' + rowIndex);
					$('#minMaps-' + i).attr('id' , 'minMaps-' + rowIndex);
					$('#minReduces-' + i).attr('id' , 'minReduces-' + rowIndex);
					$('#maxMaps-' + i).attr('id' , 'maxMaps-' + rowIndex);
					$('#maxReduces-' + i).attr('id' , 'maxReduces-' + rowIndex);
					$('#maxRunningJobs-' + i).attr('id' , 'maxRunningJobs-' + rowIndex);
					$('#weight-' + i).attr('id' , 'weight-' + rowIndex);					
				}
			}
		},
		
		// function used to call functions to update fair-scheduler post json
		updateFairSchedulerPostJson : function() {
			com.impetus.ankush.jobScheduler.updateFSCommonsPostJSON();
			com.impetus.ankush.jobScheduler.updateFSDefaultsPostJSON();
		},
		
		// function used to update common data in fair-scheduler post json
		updateFSCommonsPostJSON : function() {
			jsonObjFS_Common = new Object();
			if($('#jobFS_pool').val() == "Select Pool") {
				jsonObjFS_Common.pool = "";
			} else {
				jsonObjFS_Common.pool = $('#jobFS_pool').val();
			}
			
			jsonObjFS_Common.poolnameProperty = $('#jobFS_poolNameProperty').val();
			jsonObjFS_Common.allocationFile = $('#jobFS_allocationFile').val();
			jsonObjFS_Common.updateInterval = $('#jobFS_updateInterval').val();
			jsonObjFS_Common.preemptionInterval = $('#jobFS_preemptionInterval').val();

			if($('#jobFS_enablePreemption').hasClass('active')) {
				jsonObjFS_Common.preemption = true;
			}
			else {
				jsonObjFS_Common.preemption = false;
			}

			if($('#jobFS_enableSizeBasedWeigth').hasClass('active')) {
				jsonObjFS_Common.sizeBasedWeight = true;				
			}
			else {
				jsonObjFS_Common.sizeBasedWeight = false;
			}

			if($('#jobFS_enablePreemptionOnlyLog').hasClass('active')) {
				jsonObjFS_Common.preemptionOnlyLog = true;
			}
			else {
				jsonObjFS_Common.preemptionOnlyLog = false;
			}
			jsonObjFairScheduler.commons = new Object();
			jsonObjFairScheduler.commons = jsonObjFS_Common; 
		},
		
		// function used to update defaults data in fair-scheduler post json
		updateFSDefaultsPostJSON : function() {
			jsonObjFS_Default = new Object();
			jsonObjFS_Default.poolMaxJobsDefault = $('#jobFS_poolMaxJobsDefault').val();
			jsonObjFS_Default.userMaxJobsDefault = $('#jobFS_userMaxJobsDefault').val();
			jsonObjFS_Default.defaultMinSharePreemptionTimeout = $('#jobFS_defaultMinSharePreemptionTimeout').val();
			jsonObjFS_Default.fairSharePreemptionTimeout = $('#jobFS_fairSharePreemptionTimeout').val();

			if($('#jobFS_defaultPoolSchedulingMode_Fair').hasClass('active')) {
				jsonObjFS_Default.defaultPoolSchedulingMode = "fair";
			}
			else {
				jsonObjFS_Default.defaultPoolSchedulingMode = "fifo";
			}
			jsonObjFairScheduler.defaults = new Object();
			jsonObjFairScheduler.defaults = jsonObjFS_Default; 
		},

		// function used to initialize fair scheduler Json object 
		initializeFairJSONObject : function() {
			jsonObjFairScheduler = new Object();
			jsonObjFairScheduler.schedulerClassName = "org.apache.hadoop.mapred.FairScheduler";
			jsonObjFairScheduler.commons = new template_FairCommon();
			jsonObjFairScheduler.defaults = new template_FairDefault();
			jsonObjFairScheduler.pools = new Array();
			jsonObjFairScheduler.users = new Array();
		},

		// function used to show default values when job-scheduler page opens
		getDefaultJobSchedulerValues : function(clusterId) {
			jobSchedulerClusterId = clusterId;
			jobSchedulerUrl=baseUrl+'/job/' + jobSchedulerClusterId + '/scheduler'; 
			$('#jobScheduler_default').attr('disabled', true);
			$('#jobScheduler_capacity').attr('disabled', true);
			$('#jobScheduler_fair').attr('disabled', true);
			$('#saveSchedulerButton').attr('disabled', true);

			$.ajax({
				'type' : 'GET',
				'url' : jobSchedulerUrl,
				'contentType' : 'application/json',
				"async": true,
				'dataType' : 'json',
				'success' : function(result) {
					jobResult = result;
					$('#jobScheduler_default').removeAttr('disabled');
					$('#jobScheduler_capacity').removeAttr('disabled');
					$('#jobScheduler_fair').removeAttr('disabled');
					$('#saveSchedulerButton').removeAttr('disabled');
					if(!jobResult.output.status) {
						$('#saveSchedulerButton').attr('disabled', true);
						$('#jobScheduler_default').attr('disabled', true);
						$('#jobScheduler_capacity').attr('disabled', true);
						$('#jobScheduler_fair').attr('disabled', true);
						com.impetus.ankush.validation.errorCount = 0;
						if(jobResult.output.error == null) {
							errorMsg = 'Sorry, unable to get Scheduler Information, please try later.';
							com.impetus.ankush.validation.addNewErrorToDiv(null,'popover-content',errorMsg,null);
						}
						else {
							com.impetus.ankush.validation.showAjaxCallErrors(jobResult.output.error, 'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
							return;
						}
					}
					if(jobResult.output.schedulerClassName=="default"){
						$('#jobScheduler_default').attr('checked', true);
						delete jobResult.output.status;
						delete jobResult.output.error;
					} else if(jobResult.output.schedulerClassName == "org.apache.hadoop.mapred.FairScheduler"){
						$('#jobScheduler_fair').attr('checked', true);
						com.impetus.ankush.jobScheduler.showFairScheduler();
						jsonObjFairScheduler = jobResult.output;
						delete jsonObjFairScheduler.status;
						delete jsonObjFairScheduler.error;
						com.impetus.ankush.jobScheduler.fillFairSchedulerContents(jsonObjFairScheduler);
					} else if(jobResult.output.schedulerClassName == "org.apache.hadoop.mapred.CapacityTaskScheduler"){

						com.impetus.ankush.jobScheduler.showCapacityScheduler();
						delete jobResult.output.status;
						delete jobResult.output.error;
						$('#jobScheduler_capacity').attr('checked', true);
						$('#jobSchedulerCapacity_pollingInterval').val(jobResult.output.commons.pollInterval);
						$('#jobSchedulerCapacity_workerThreads').val(jobResult.output.commons.workerThreads);
						var enableAccess = document.getElementById("enableAccessControl");
						var disableAccess = document.getElementById("disableAccessControl");
						if(jobResult.output.commons.accessControlList){

							enableAccess.classList.add("active");
							disableAccess.classList.remove("active");
						}else{
							enableAccess.classList.remove("active");
							disableAccess.classList.add("active");
						}
						$('#jobSchedulerCapacity_maxsystemJobs').val(jobResult.output.defaults.maxSystemJobs);
						$('#jobSchedulerCapacity_minUserLimit').val(jobResult.output.defaults.minUserLimit);
						$('#jobSchedulerCapacity_maxActivetasks_queue').val(jobResult.output.defaults.maxActiveTasksPerQueue);
						$('#jobSchedulerCapacity_maxActivetasks_user').val(jobResult.output.defaults.maxActiveTasksPerUser);
						$('#jobSchedulerCapacity_jobCount').val(jobResult.output.defaults.jobCount);
						var enablePriority = document.getElementById("prioritySupportON");
						var disablePriority = document.getElementById("prioritySupportOFF");
						if(jobResult.output.defaults.prioritySupport){
							enablePriority.classList.add("active");
							disablePriority.classList.remove("active");
						}else{
							enablePriority.classList.remove("active");
							disablePriority.classList.add("active");
						}
						com.impetus.ankush.jobScheduler.loadQueueTableData();
					}

				},
				'error' : function() {
					$('#jobScheduler_default').removeAttr('disabled');
					$('#jobScheduler_capacity').removeAttr('disabled');
					$('#jobScheduler_fair').removeAttr('disabled');
					$('#saveSchedulerButton').removeAttr('disabled');
				}
			});	
		},

		// function used to load queueus in queue table in capacity scheduler
		loadQueueTableData : function() {
			var mainList=[];
			var queueId = null;
			for(var i=0; i< jobResult.output.queues.length;i++){
				queueId = jobResult.output.queues[i].name;
				var dataList=[];
				var queueName= '<span  class="editableLabel" style="font-weight:bold;" id="queueName-'+queueId+'" onclick=""/>'+jobResult.output.queues[i].name+'</span>';
				var queueCapacity= '<span class="editableLabel" style="font-weight:bold;" id="capacity-'+queueId+'"/>'+jobResult.output.queues[i].capacity+'</span>';
				var queueMaxCapacity= '<span  class="editableLabel" style="font-weight:bold;" id="maxCapacity-'+queueId+'"/>'+jobResult.output.queues[i].maxCapacity+'</span>';
				var queueMinUserLimit= '<span class="editableLabel" style="font-weight:bold;" id="minUserLimit-'+queueId+'"/>'+jobResult.output.queues[i].minUserLimit+'</span>';
				var deleteImgQueue = '<a href="#"><img class="deleteQueue"  id="deleteImgQueue-'+ queueId +'" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.jobScheduler.deleteQueue(\''+queueId+'\');"/></a>';
				var navigationImgQueue = '<a href="#"><img id="navigationImgQueue-'+ queueId +'" src="'+baseUrl+'/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.jobScheduler.capacityQueueChild(\''+queueId+'\');"/></a>'; 
				dataList.push(queueName);
				dataList.push(queueCapacity);
				dataList.push(queueMaxCapacity);
				dataList.push(queueMinUserLimit);
				dataList.push(deleteImgQueue);
				dataList.push(navigationImgQueue);
				mainList.push(dataList);
				com.impetus.ankush.jobScheduler.createNewObject(queueId,i);
			}
			capacityQueueTable.fnAddData(mainList);
			$('.editableLabel').editable({
				type : 'text',
				validate :function(value){
					return com.impetus.ankush.jobScheduler.updatedExistingQueueId(this,value);}
			});
		},

		// function used to initialize queue table
		initializeQueueTable : function() {
			capacityQueueTable=$('#capacityQueueTable').dataTable({
				"bJQueryUI" : false,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : false,
				"bInfo" : false,
				"bAutoWidth" : false,
			});
			$('#capacityQueueTable_filter').hide();
			$('#searchQueues').keyup(function(){
				$("#capacityQueueTable").dataTable().fnFilter( $(this).val() );
			});
		},

		// function called when any existing queue detail in queue table is edited
		updatedExistingQueueId : function(elem,value) {
			var id=elem.id;
			id = id.split('-');
			var k=id.length-1;
			var addId = "";
			for(var i=1;i<id.length;i++){
				addId += id[i];
				if(i!=k){
					addId+='-';
				}
			}
			if(id[0]=='queueName'){
				var c= $('#queueName-'+addId).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();

				var nameFlag = true;
				for(var j=0;j<jobResult.output.queues.length;j++){
					if(c==jobResult.output.queues[j].name){
						if(jobResult.output.queues[j].name!=$('#queueName-'+addId).text())
							nameFlag = false;
					}
				}
				var newQueue = null;
				if(!com.impetus.ankush.validation.pattern(c,/^[a-zA-Z0-9_-]*$/)) {
					return 'Invalid Name';
				}
				if(!nameFlag){
					return "Already exists";
				}
				if($('#queueName-'+addId).text()!=c){
					var previousQueueName = $('#queueName-'+addId).text();
					for(var a=0;a<jobResult.output.queues.length;a++){
						if(previousQueueName==jobResult.output.queues[a].name){
							jobResult.output.queues.splice(a,1);
						}
					}

					com.impetus.ankush.jobScheduler.queueDetails[c] = new queueTemplate();
					com.impetus.ankush.jobScheduler.queueDetails[c].queue = c;
					com.impetus.ankush.jobScheduler.queueDetails[c].capacity = $('#capacity-'+addId).text();
					com.impetus.ankush.jobScheduler.queueDetails[c].maxCapacity = $('#maxCapacity-'+addId).text();
					com.impetus.ankush.jobScheduler.queueDetails[c].minUserLimit = $('#minUserLimit-'+addId).text();
					com.impetus.ankush.jobScheduler.queueDetails[c].userLimit =com.impetus.ankush.jobScheduler.queueDetails[addId].userLimit;
					com.impetus.ankush.jobScheduler.queueDetails[c].prioritySupport = com.impetus.ankush.jobScheduler.queueDetails[addId].prioritySupport;
					com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasks = com.impetus.ankush.jobScheduler.queueDetails[addId].maxActiveTasks;
					com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasksUser= com.impetus.ankush.jobScheduler.queueDetails[addId].maxActiveTasksUser;
					com.impetus.ankush.jobScheduler.queueDetails[c].jobCount=com.impetus.ankush.jobScheduler.queueDetails[addId].jobCount;
					delete com.impetus.ankush.jobScheduler.queueDetails[addId];

					newQueue = {
							"name": c,
							"capacity": com.impetus.ankush.jobScheduler.queueDetails[c].capacity,
							"maxCapacity": com.impetus.ankush.jobScheduler.queueDetails[c].maxCapacity,
							"minUserLimit": com.impetus.ankush.jobScheduler.queueDetails[c].minUserLimit,
							"userLimit": com.impetus.ankush.jobScheduler.queueDetails[c].userLimit,
							"prioritySupport": com.impetus.ankush.jobScheduler.queueDetails[c].prioritySupport,
							"maxActiveTasks": com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasks,
							"maxActiveTasksPerUser": com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasksUser,
							"jobCount": com.impetus.ankush.jobScheduler.queueDetails[c].jobCount	
					};
					jobResult.output.queues.push(newQueue);
					console.log(jobResult);
					nRow=$('#queueName-'+addId).parents('tr')[0];
					$(nRow).hide();
					$(nRow).html("");
					capacityQueueTable.fnAddData([
					                              '<span  class="editableLabel" style="font-weight:bold;" id="queueName-'+c+'" onclick=""/>'+c+'</span>',
					                              '<span class="editableLabel" style="font-weight:bold;" id="capacity-'+c+'"/>'+com.impetus.ankush.jobScheduler.queueDetails[c].capacity+'</span>',
					                              '<span  class="editableLabel" style="font-weight:bold;" id="maxCapacity-'+c+'"/>'+com.impetus.ankush.jobScheduler.queueDetails[c].maxCapacity+'</span>',
					                              '<span class="editableLabel" style="font-weight:bold;" id="minUserLimit-'+c+'"/>'+com.impetus.ankush.jobScheduler.queueDetails[c].minUserLimit+'</span>',
					                              '<a href="#"><img class="deleteQueue"  id="deleteImgQueue-'+ c +'" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.jobScheduler.deleteQueue(\''+c+'\');"/></a>',
					                              '<a href="#"><img id="navigationImgQueue-'+ c +'" src="'+baseUrl+'/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.jobScheduler.capacityQueueChild(\''+c+'\');"/></a>'                        									
					                              ]);
					$('.editableLabel').editable({
						type : 'text',
						validate :function(){
							return com.impetus.ankush.jobScheduler.updatedExistingQueueId(this);}
					});
				}
			}else if(id[0]=='maxCapacity'){

				var d = $('#'+elem.id).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();

				if(d!='-1'){
					if($('#capacity-'+addId).text()!=''){
						if (parseInt($('#capacity-'+addId).text(),10) > parseInt(d,10)) {
							return "Invalid value";
						}
					}
					if(!com.impetus.ankush.validation.pattern(d,/^[\d]*$/)) {
						return "Invalid value";
					}
				}
			}else if(id[0]=='capacity'){

				var d = $('#'+elem.id).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();

				if($('#maxCapacity-'+addId).text()!=''){
					if (parseInt(d,10) > parseInt($('#maxCapacity-'+addId).text(),10)) {
						return "Invalid value";
					}
				}
				if(!com.impetus.ankush.validation.pattern(d,/^[\d]*$/)) {
					return "Invalid value";
				}				
			}else {
				var e = $('#'+elem.id).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();

				if(!com.impetus.ankush.validation.pattern(e,/^[\d]*$/)) {
					return "Invalid value";
				}
			}
		},

		// function used to create a new queue object
		createNewObject : function(queueId,i) {
			if(com.impetus.ankush.jobScheduler.queueDetails[queueId] == null){
				var obj = new queueTemplate();
				com.impetus.ankush.jobScheduler.queueDetails[queueId] = obj;
			}
			com.impetus.ankush.jobScheduler.queueDetails[queueId].queue = jobResult.output.queues[i].name;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].capacity = jobResult.output.queues[i].capacity;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].maxCapacity = jobResult.output.queues[i].maxCapacity;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].minUserLimit = jobResult.output.queues[i].minUserLimit;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].userLimit =jobResult.output.queues[i].userLimit;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].prioritySupport = jobResult.output.queues[i].prioritySupport;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].maxActiveTasks = jobResult.output.queues[i].maxActiveTasks;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].maxActiveTasksUser= jobResult.output.queues[i].maxActiveTasksPerUser;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].jobCount=jobResult.output.queues[i].jobCount;

		},

		// function used to check whether a queue added has queue name empty
		getEmptyQueueName : function() {
			var flag = true;
			if(document.getElementById('queueName-Empty') != null) {
				flag = false;
			}
			return flag;
		},

		// function used to add queue to a table
		addQueue : function() {
			if(jobResult.output.schedulerClassName=="org.apache.hadoop.mapred.fairScheduler"||jobResult.output.schedulerClassName=="Default"||com.impetus.ankush.jobScheduler.getEmptyQueueName()){
				var newQueueId = 'Empty';
				var tableData = [];
				var queueRow = [];
				var column1 = '<span class="editableLabel" style="font-weight:bold;" id="queueName-' + newQueueId + '"  placeholder="Queue Name" onclick=""></span>';
				var column2 = '<span class="editableLabel" style="font-weight:bold;" id="capacity-'+ newQueueId +'"  placeholder="%"></span>';
				var column3 = '<span class="editableLabel" style="font-weight:bold;" id="maxCapacity-'+ newQueueId +'"  placeholder="%"></span>';
				var column4 = '<span class="editableLabel" style="font-weight:bold;" id="minUserLimit-'+ newQueueId +'"  placeholder="%"></span>';
				var column5 = '<a href="#"><img class="deleteQueue"  id="deleteImgQueue-'+ newQueueId +'" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.jobScheduler.deleteQueue(\''+newQueueId+'\');"/></a>';
				var column6 = '<a href="#"><img id="navigationImgQueue-'+ newQueueId +'" src="'+baseUrl+'/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.jobScheduler.capacityQueueChild(\''+newQueueId+'\');"/></a>'; 
				queueRow.push(column1);
				queueRow.push(column2);
				queueRow.push(column3);
				queueRow.push(column4);
				queueRow.push(column5);
				queueRow.push(column6);
				tableData.push(queueRow);
				if(capacityQueueTable==null){
					capacityQueueTable=$('#capacityQueueTable').dataTable({
						"bJQueryUI" : false,
						"bPaginate" : false,
						"bLengthChange" : false,
						"bFilter" : true,
						"bSort" : false,
						"bInfo" : false,
						"bAutoWidth" : false,
					});

					$('#capacityQueueTable_filter').hide();
					$('#searchQueues').keyup(function(){
						$("#capacityQueueTable").dataTable().fnFilter( $(this).val() );
					});
				}
				capacityQueueTable.fnAddData(tableData);

				var obj = new queueTemplate();
				com.impetus.ankush.jobScheduler.queueDetails[newQueueId] = obj;
				$('.editableLabel').editable({
					type : 'text',
					validate :function(value){
						return com.impetus.ankush.jobScheduler.updateQueueId(this,value);}
				});
			}
		},

		// function called when details of any new queue added in queue table is edited
		updateQueueId : function(elem,value) {
			var id=elem.id;
			id = id.split('-');
			var k=id.length-1;
			var addId = "";
			for(var i=1;i<id.length;i++){
				addId += id[i];
				if(i!=k){
					addId+='-';
				}
			}
			if(id[0]=="queueName"){
				var c= $('#queueName-'+addId).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();
				if(!com.impetus.ankush.validation.pattern(c,/^[a-zA-Z0-9_-]*$/)) {
					return 'Invalid Name';
				}
				if(jobResult.output.queues!=null){
					var nameFlag = true;
					for(var j=0;j<jobResult.output.queues.length;j++){
						if(c==jobResult.output.queues[j].name){
							nameFlag = false;
						}
					}
					if(!nameFlag){
						return 'Already exists';
					}
				}
				com.impetus.ankush.jobScheduler.queueDetails[c] = new queueTemplate();
				com.impetus.ankush.jobScheduler.queueDetails[c].queue = c;
				com.impetus.ankush.jobScheduler.queueDetails[c].capacity = $('#capacity-'+addId).text();
				com.impetus.ankush.jobScheduler.queueDetails[c].maxCapacity = $('#maxCapacity-'+addId).text();
				com.impetus.ankush.jobScheduler.queueDetails[c].minUserLimit = $('#minUserLimit-'+addId).text();
				com.impetus.ankush.jobScheduler.queueDetails[c].userLimit ="";
				com.impetus.ankush.jobScheduler.queueDetails[c].prioritySupport = false;
				com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasks = "";
				com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasksUser= "";
				com.impetus.ankush.jobScheduler.queueDetails[c].jobCount="";
				delete com.impetus.ankush.jobScheduler.queueDetails[addId];

				var newQueue = null;
				newQueue = {
						"name": c,
						"capacity": com.impetus.ankush.jobScheduler.queueDetails[c].capacity,
						"maxCapacity": com.impetus.ankush.jobScheduler.queueDetails[c].maxCapacity,
						"minUserLimit": com.impetus.ankush.jobScheduler.queueDetails[c].minUserLimit,
						"userLimit": com.impetus.ankush.jobScheduler.queueDetails[c].userLimit,
						"prioritySupport": com.impetus.ankush.jobScheduler.queueDetails[c].prioritySupport,
						"maxActiveTasks": com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasks,
						"maxActiveTasksPerUser": com.impetus.ankush.jobScheduler.queueDetails[c].maxActiveTasksUser,
						"jobCount": com.impetus.ankush.jobScheduler.queueDetails[c].jobCount	
				};

				if(jobResult.output.queues==null){
					com.impetus.ankush.jobScheduler.initializeJobResult();

				}
				jobResult.output.queues.push(newQueue);
				nRow=$('#queueName-'+addId).parents('tr')[0];
				$(nRow).hide();
				$(nRow).html("");
				capacityQueueTable.fnAddData([
				                              '<span  class="editableLabel" style="font-weight:bold;" id="queueName-'+c+'" onclick=""/>'+c+'</span>',
				                              '<span class="editableLabel" style="font-weight:bold;" id="capacity-'+c+'"/>'+com.impetus.ankush.jobScheduler.queueDetails[c].capacity+'</span>',
				                              '<span  class="editableLabel" style="font-weight:bold;" id="maxCapacity-'+c+'"/>'+com.impetus.ankush.jobScheduler.queueDetails[c].maxCapacity+'</span>',
				                              '<span class="editableLabel" style="font-weight:bold;" id="minUserLimit-'+c+'"/>'+com.impetus.ankush.jobScheduler.queueDetails[c].minUserLimit+'</span>',
				                              '<a href="#"><img class="deleteQueue"  id="deleteImgQueue-'+ c +'" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.jobScheduler.deleteQueue(\''+c+'\');"/></a>',
				                              '<a href="#"><img id="navigationImgQueue-'+ c +'" src="'+baseUrl+'/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.jobScheduler.capacityQueueChild(\''+c+'\');"/></a>'                        									
				                              ]);
				$('.editableLabel').editable({
					type : 'text',
					validate :function(){
						return com.impetus.ankush.jobScheduler.updatedExistingQueueId(this);}
				});

			}else if(id[0]=='maxCapacity'){
				var d = $('#'+elem.id).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();
				if(d!='-1'){
					if($('#capacity-'+addId).text()!=''){
						if (parseInt($('#capacity-'+addId).text(),10) > parseInt(d,10)) {
							return "Invalid value";
						}
					}
					if(!com.impetus.ankush.validation.pattern(d,/^[\d]*$/)) {
						return "Invalid value";
					}
				}
			}else if(id[0]=='capacity'){
				var d = $('#'+elem.id).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();

				if($('#maxCapacity-'+addId).text()!=''){
					if (parseInt(d,10) > parseInt($('#maxCapacity-'+addId).text(),10)) {
						return "Invalid value";
					}
				}
				if(!com.impetus.ankush.validation.pattern(d,/^[\d]*$/)) {
					return "Invalid value";
				}
			}else {
				var d = $('#'+elem.id).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first().val();

				if(!com.impetus.ankush.validation.pattern(d,/^[\d]*$/)) {
					return "Invalid value";
				}
			}
		},

		// function used to initialize post Json used in capacity scheduler
		initializeJobResult : function() {
			jobResult.output = {};
			jobResult.output.schedulerClassName = "org.apache.hadoop.mapred.CapacityTaskScheduler";
			jobResult.output.commons = {};
			jobResult.output.commons.pollInterval = $('#jobSchedulerCapacity_pollingInterval').val();
			jobResult.output.commons.workerThreads = $('#jobSchedulerCapacity_workerThreads').val();
			if($('#enableAccessControl').hasClass('active')){
				jobResult.output.commons.accessControlList = true;
			} else{
				jobResult.output.commons.accessControlList = false;
			}
			jobResult.output.defaults = {};
			jobResult.output.defaults.maxSystemJobs = $('#jobSchedulerCapacity_maxsystemJobs').val();
			jobResult.output.defaults.minUserLimit = $('#jobSchedulerCapacity_minUserLimit').val();
			jobResult.output.defaults.maxActiveTasksPerQueue = $('#jobSchedulerCapacity_maxActivetasks_queue').val();
			jobResult.output.defaults.maxActiveTasksPerUser = $('#jobSchedulerCapacity_maxActivetasks_user').val();
			jobResult.output.defaults.jobCount = $('#jobSchedulerCapacity_jobCount').val();

			if($('#prioritySupportON').hasClass('active')){
				jobResult.output.defaults.prioritySupport = true;
			} else{
				jobResult.output.defaults.prioritySupport = false;
			}
			jobResult.output.queues = new Array();
		},

		// function used to open a capacity scheduler queue details page
		capacityQueueChild : function(newQueueId) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster-monitoring/capacityQueueChild/' + newQueueId,
				method : 'get',
				params : {},
				title : 'Add New Queue',
				tooltipTitle : 'Job Scheduler',
				callback : function(data) {
					com.impetus.ankush.jobScheduler.syncQueueData(data.newQueueId);
				},
				callbackData : { newQueueId:newQueueId }
			});
		},

		// function used to synchronize changes made to queue details in table to be reflected in its details child page
		syncQueueData : function(newQueueId) {
			var enablePriority = document.getElementById("queuePrioritySupportON-"+newQueueId);
			var disablePriority = document.getElementById("queuePrioritySupportOFF-"+newQueueId);
			if(newQueueId == 'Empty'){
				$('#capacitySchedulerQueue-' + newQueueId).val('');
				$('#capacitySchedulerCapacity-' + newQueueId).val('');
				$('#capacitySchedulerMaxCapacity-' + newQueueId).val('');
				$('#capacitySchedulerMinUserLimit-' + newQueueId).val($('#jobSchedulerCapacity_minUserLimit').val());
				$('#capacitySchedulerUserLimit-'+newQueueId).val($('#jobSchedulerCapacity_minUserLimit').val());
				$('#capacitySchedulerMaxActivityTasks-'+newQueueId).val($('#jobSchedulerCapacity_maxActivetasks_queue').val());
				$('#capacitySchedulerMaxActivityTasksVal-'+newQueueId).val($('#jobSchedulerCapacity_maxActivetasks_user').val());
				$('#capacitySchedulerJobCount-'+newQueueId).val($('#jobSchedulerCapacity_jobCount').val());

				if($('#prioritySupportON').hasClass('active')){
					enablePriority.classList.add("active");
					disablePriority.classList.remove("active");
				}else{
					enablePriority.classList.remove("active");
					disablePriority.classList.add("active");
				}
			}else{
				var k=null;
				for(var i=0;i<jobResult.output.queues.length;i++){
					if(newQueueId==jobResult.output.queues[i].name){
						k=i;
					}
				}
				if(! (($('#queueName-'+newQueueId).hasClass('editable-empty')) || ($('#queueName-'+newQueueId).text() == 'Empty'))) {
					$('#capacitySchedulerQueue-' + newQueueId).val($('#queueName-'+newQueueId).text());	
				}
				if(! (($('#capacity-'+newQueueId).hasClass('editable-empty')) || ($('#capacity-'+newQueueId).text() == 'Empty'))) {
					$('#capacitySchedulerCapacity-' + newQueueId).val($('#capacity-'+newQueueId).text());	
				}
				if(! (($('#maxCapacity-'+newQueueId).hasClass('editable-empty')) || ($('#maxCapacity-'+newQueueId).text() == 'Empty'))) {
					$('#capacitySchedulerMaxCapacity-' + newQueueId).val($('#maxCapacity-'+newQueueId).text());	
				}
				if(! (($('#minUserLimit-'+newQueueId).hasClass('editable-empty')) || ($('#minUserLimit-'+newQueueId).text() == 'Empty'))) {
					$('#capacitySchedulerMinUserLimit-' + newQueueId).val($('#minUserLimit-'+newQueueId).text());	
				}else {
					$('#capacitySchedulerMinUserLimit-' + newQueueId).val($('#jobSchedulerCapacity_minUserLimit').val());
				}
				if(jobResult.output.queues[k].userLimit==""){
					$('#capacitySchedulerUserLimit-'+newQueueId).val($('#jobSchedulerCapacity_minUserLimit').val());
					$('#capacitySchedulerMaxActivityTasks-'+newQueueId).val($('#jobSchedulerCapacity_maxActivetasks_queue').val());
					$('#capacitySchedulerMaxActivityTasksVal-'+newQueueId).val($('#jobSchedulerCapacity_maxActivetasks_user').val());
					$('#capacitySchedulerJobCount-'+newQueueId).val($('#jobSchedulerCapacity_jobCount').val());

					if($('#prioritySupportON').hasClass('active')){
						enablePriority.classList.add("active");
						disablePriority.classList.remove("active");
					}else{
						enablePriority.classList.remove("active");
						disablePriority.classList.add("active");
					}
				}else{
					$('#capacitySchedulerUserLimit-'+newQueueId).val(jobResult.output.queues[k].userLimit);
					$('#capacitySchedulerMaxActivityTasks-'+newQueueId).val(jobResult.output.queues[k].maxActiveTasks);
					$('#capacitySchedulerMaxActivityTasksVal-'+newQueueId).val(jobResult.output.queues[k].maxActiveTasksPerUser);
					$('#capacitySchedulerJobCount-'+newQueueId).val(jobResult.output.queues[k].jobCount);
					if(jobResult.output.queues[k].prioritySupport){
						enablePriority.classList.add("active");
						disablePriority.classList.remove("active");
					}else{
						enablePriority.classList.remove("active");
						disablePriority.classList.add("active");
					}
				}
			}
		},

		// function used to synchronize changes made to queue details page to be reflected table
		syncQueueTable : function(i) {
			com.impetus.ankush.jobScheduler.validateCapacityQueue(i);
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-queue', 'errorBtnQueue');
				return;
			}
			$('input[type=text]').each(function() {
				$(this).val($.trim($(this).val()));
			});
			com.impetus.ankush.jobScheduler.updateQueueTable(i);
		},

		// function used to update queue table to the changes made in its details page 
		updateQueueTable : function(queueId) {
			var previousQueueId = queueId;
			var prioritysupport = null;
			if($('#queuePrioritySupportON-'+queueId).hasClass('active')){
				prioritysupport = true;
			}else{
				prioritysupport = false;
			}
			if($('#capacitySchedulerQueue-'+queueId).val()!=$('#queueName-'+queueId).text()){

				var newQueueId= $('#capacitySchedulerQueue-'+queueId).val();
				var previousQueueName = $('#queueName-'+queueId).text();
				if(jobResult.output.schedulerClassName=="org.apache.hadoop.mapred.CapacityTaskScheduler"){
					for(var a=0;a<jobResult.output.queues.length;a++){
						if(previousQueueName==jobResult.output.queues[a].name){
							jobResult.output.queues.splice(a,1);
						}
					}
				}
				var newQueue = null;
				newQueue = {
						"name": newQueueId,
						"capacity": $('#capacitySchedulerCapacity-'+queueId).val(),
						"maxCapacity": $('#capacitySchedulerMaxCapacity-'+queueId).val(),
						"minUserLimit": $('#capacitySchedulerMinUserLimit-'+queueId).val(),
						"userLimit": $('#capacitySchedulerUserLimit-'+queueId).val(),
						"prioritySupport": prioritysupport,
						"maxActiveTasks": $('#capacitySchedulerMaxActivityTasks-'+queueId).val(),
						"maxActiveTasksPerUser": $('#capacitySchedulerMaxActivityTasksVal-'+queueId).val(),
						"jobCount": $('#capacitySchedulerJobCount-'+queueId).val()	
				};
				if(jobResult.output.schedulerClassName!="org.apache.hadoop.mapred.CapacityTaskScheduler"){
					com.impetus.ankush.jobScheduler.initializeJobResult();
				}
				jobResult.output.queues.push(newQueue);
				nRow=$('#queueName-'+queueId).parents('tr')[0];
				$(nRow).hide();
				$(nRow).html("");
				capacityQueueTable.fnAddData([
				                              '<span  class="editableLabel" style="font-weight:bold;" id="queueName-'+newQueueId+'" onclick=""/>'+newQueueId+'</span>',
				                              '<span class="editableLabel" style="font-weight:bold;" id="capacity-'+newQueueId+'"/>'+$('#capacitySchedulerCapacity-'+queueId).val()+'</span>',
				                              '<span  class="editableLabel" style="font-weight:bold;" id="maxCapacity-'+newQueueId+'"/>'+$('#capacitySchedulerMaxCapacity-'+queueId).val()+'</span>',
				                              '<span class="editableLabel" style="font-weight:bold;" id="minUserLimit-'+newQueueId+'"/>'+$('#capacitySchedulerMinUserLimit-'+queueId).val()+'</span>',
				                              '<a href="#"><img class="deleteQueue"  id="deleteImgQueue-'+ newQueueId +'" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.jobScheduler.deleteQueue(\''+newQueueId+'\');"/></a>',
				                              '<a href="#"><img id="navigationImgQueue-'+ newQueueId +'" src="'+baseUrl+'/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.jobScheduler.capacityQueueChild(\''+newQueueId+'\');"/></a>'                        									
				                              ]);
				$('.editableLabel').editable({
					type : 'text',
					validate :function(){
						return com.impetus.ankush.jobScheduler.updatedExistingQueueId(this);}
				});
				queueId= newQueueId;
			}
			delete com.impetus.ankush.jobScheduler.queueDetails[previousQueueId];
			com.impetus.ankush.jobScheduler.queueDetails[queueId] = new queueTemplate();
			com.impetus.ankush.jobScheduler.queueDetails[queueId].queue = queueId;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].capacity = $('#capacitySchedulerCapacity-'+previousQueueId).val();
			com.impetus.ankush.jobScheduler.queueDetails[queueId].maxCapacity = $('#capacitySchedulerMaxCapacity-'+previousQueueId).val();
			com.impetus.ankush.jobScheduler.queueDetails[queueId].minUserLimit = $('#capacitySchedulerMinUserLimit-'+previousQueueId).val();
			com.impetus.ankush.jobScheduler.queueDetails[queueId].userLimit =$('#capacitySchedulerUserLimit-'+previousQueueId).val();
			com.impetus.ankush.jobScheduler.queueDetails[queueId].prioritySupport = prioritysupport;
			com.impetus.ankush.jobScheduler.queueDetails[queueId].maxActiveTasks = $('#capacitySchedulerMaxActivityTasks-'+previousQueueId).val();
			com.impetus.ankush.jobScheduler.queueDetails[queueId].maxActiveTasksUser= $('#capacitySchedulerMaxActivityTasksVal-'+previousQueueId).val();
			com.impetus.ankush.jobScheduler.queueDetails[queueId].jobCount=$('#capacitySchedulerJobCount-'+previousQueueId).val();
			com.impetus.ankush.removeChild(4);
			$('#queueName-'+queueId).empty();
			$('#queueName-'+queueId).text(com.impetus.ankush.jobScheduler.queueDetails[queueId].queue);
			$('#capacity-'+queueId).empty();
			$('#capacity-'+queueId).text(com.impetus.ankush.jobScheduler.queueDetails[queueId].capacity);
			$('#maxCapacity-'+queueId).empty();
			$('#maxCapacity-'+queueId).text(com.impetus.ankush.jobScheduler.queueDetails[queueId].maxCapacity);
			$('#minUserLimit-'+queueId).empty();
			$('#minUserLimit-'+queueId).text(com.impetus.ankush.jobScheduler.queueDetails[queueId].minUserLimit);
			if(previousQueueId==queueId){
				for(var b=0;b<jobResult.output.queues.length;b++){
					if(jobResult.output.queues[b].name==queueId){
						break;
					}
				}
				jobResult.output.queues[b].capacity = com.impetus.ankush.jobScheduler.queueDetails[queueId].capacity;
				jobResult.output.queues[b].maxCapacity = com.impetus.ankush.jobScheduler.queueDetails[queueId].maxCapacity;
				jobResult.output.queues[b].minUserLimit = com.impetus.ankush.jobScheduler.queueDetails[queueId].minUserLimit;
				jobResult.output.queues[b].userLimit = com.impetus.ankush.jobScheduler.queueDetails[queueId].userLimit;
				jobResult.output.queues[b].prioritySupport = com.impetus.ankush.jobScheduler.queueDetails[queueId].prioritySupport;
				jobResult.output.queues[b].maxActiveTasks = com.impetus.ankush.jobScheduler.queueDetails[queueId].maxActiveTasks;
				jobResult.output.queues[b].maxActiveTasksPerUser = com.impetus.ankush.jobScheduler.queueDetails[queueId].maxActiveTasksUser;
				jobResult.output.queues[b].jobCount = com.impetus.ankush.jobScheduler.queueDetails[queueId].jobCount;

			}
		},

		// function used to show delete queue dialog 
		deleteQueue : function(queueId) {
			$('#capacityQueueTable .deleteQueue').live('click', function (e) {
				nRow= $(this).parents('tr')[0];
			} );
			$("#deleteQueueDialog").modal('show');
			$('.ui-dialog-titlebar').hide();
			$("#confirmDeleteButtonQueue").click(function() {
				com.impetus.ankush.jobScheduler.deleteCapacityQueue(nRow,queueId);
				$('#deleteQueueDialog').modal('hide');
			});
			$("#cancelDeleteQueue").click(function() {
				$('#deleteQueueDialog').modal('hide');
			});
		},

		// function used to delete queue in capacity scheduler
		deleteCapacityQueue : function(nRow,queueId) {
			$(nRow).hide();
			$(nRow).html("");
			for(var i=0;i<jobResult.output.queues.length;i++) {
				if(jobResult.output.queues[i].name==queueId){
					jobResult.output.queues.splice(i,1);
				}
			}
		},

		// function used to post job scheduler data
		postJobScheduler : function() {
			var data = new Object(); 
			com.impetus.ankush.jobScheduler.validateJobScheduler();

			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}

			$('input[type=text]').each(function() {
				$(this).val($.trim($(this).val()));
			});

			if($('#jobScheduler_capacity').attr("checked")) {
				var jobPostData = jobResult.output;
				if(!jobPostData.hasOwnProperty('queues')){
					jobPostData.commons = {};
				}
				if(!jobPostData.hasOwnProperty('queues')){
					jobPostData.defaults = {};
				}
				if($('#enableAccessControl').hasClass('active')){
					jobPostData.commons.accessControlList = true;
				} else{
					jobPostData.commons.accessControlList = false;
				}

				if($('#prioritySupportON').hasClass('active')){
					jobPostData.defaults.prioritySupport = true;
				} else{
					jobPostData.defaults.prioritySupport = false;
				}
				jobPostData.commons.pollInterval = $('#jobSchedulerCapacity_pollingInterval').val();
				jobPostData.commons.workerThreads = $('#jobSchedulerCapacity_workerThreads').val();
				jobPostData.defaults.jobCount = $('#jobSchedulerCapacity_jobCount').val();
				jobPostData.defaults.maxActiveTasksPerQueue = $('#jobSchedulerCapacity_maxActivetasks_queue').val();
				jobPostData.defaults.maxActiveTasksPerUser = $('#jobSchedulerCapacity_maxActivetasks_user').val();
				jobPostData.defaults.maxSystemJobs = $('#jobSchedulerCapacity_maxsystemJobs').val();
				jobPostData.defaults.minUserLimit = $('#jobSchedulerCapacity_minUserLimit').val();

				var userLimit, prioritySupport,maxActiveTasks,maxActiveTasksPerUser,jobCount;
				var newQueue = null;
				var queueName = null;
				for(var a=0;a<jobPostData.queues.length;a++){
					queueName=jobPostData.queues[0].name;
					jobPostData.queues.splice(0,1);

					console.log(com.impetus.ankush.jobScheduler.queueDetails[queueName].userLimit);
					if(com.impetus.ankush.jobScheduler.queueDetails[queueName].userLimit==""){
						userLimit = $('#jobSchedulerCapacity_minUserLimit').val();
						if($('#prioritySupportON').hasClass('active')){
							prioritySupport = true;
						}else{
							prioritySupport = false;
						}
						maxActiveTasks = $('#jobSchedulerCapacity_maxActivetasks_queue').val();
						maxActiveTasksPerUser=$('#jobSchedulerCapacity_maxActivetasks_user').val();
						jobCount=$('#jobSchedulerCapacity_jobCount').val();
					}else{
						userLimit = com.impetus.ankush.jobScheduler.queueDetails[queueName].userLimit;
						prioritySupport = com.impetus.ankush.jobScheduler.queueDetails[queueName].prioritySupport;
						maxActiveTasks = com.impetus.ankush.jobScheduler.queueDetails[queueName].maxActiveTasks;
						maxActiveTasksPerUser=com.impetus.ankush.jobScheduler.queueDetails[queueName].maxActiveTasksUser;
						jobCount=com.impetus.ankush.jobScheduler.queueDetails[queueName].jobCount;
					}
					newQueue = {
							"name": com.impetus.ankush.jobScheduler.queueDetails[queueName].queue,
							"capacity": $('#capacity-'+queueName).text(),
							"maxCapacity": $('#maxCapacity-'+queueName).text(),
							"minUserLimit": $('#minUserLimit-'+queueName).text(),
							"userLimit": userLimit,
							"prioritySupport": prioritySupport,
							"maxActiveTasks": maxActiveTasks,
							"maxActiveTasksPerUser": maxActiveTasksPerUser,
							"jobCount": jobCount
					};
					jobPostData.queues.push(newQueue);
				}
				data = jobPostData;
			}
			else if($('#jobScheduler_fair').attr("checked")) {
				com.impetus.ankush.jobScheduler.updateFairSchedulerPostJson();
				data = jsonObjFairScheduler;
			}
			else {
				data.schedulerClassName = "default";
			}
			window.scrollTo(0,0);
			$("#div_RequestPlaced_JobScheduler").modal('show');

			$('.ui-dialog-titlebar').hide();

			$.ajax({
				'type' : 'POST',
				'url' : jobSchedulerUrl,
				'contentType' : 'application/json',
				'dataType' : 'json',
				'data':JSON.stringify(data),
				'success' : function(result){
					$("body").css({ overflow: 'inherit' });
					$('#div_RequestPlaced_JobScheduler').modal('hide');
					com.impetus.ankush.removeChild(3);
				},
				'error' : function(){
					$('#div_RequestPlaced_JobScheduler').modal('hide');
					$("#div_RequestError_JobScheduler").modal('show');;
					$('.ui-dialog-titlebar').hide();
					$("body").css({ overflow: 'inherit' });
					$("#div_RequestError_JobScheduler_OK").click(function() {
						$('#div_RequestError_JobScheduler').modal('hide');
					});
				}
			});			
		},

		// function used to initialize tooltip on the specified elements 
		tooltip_FairScheduler : function() {
			$('#jobFS_pool').tooltip();
			$('#jobFS_poolNameProperty').tooltip();
			$('#jobFS_allocationFile').tooltip();
			$('#jobFS_updateInterval').tooltip();
			$('#jobFS_preemptionInterval').tooltip();
			$('#jobFS_weightAdjuster').tooltip();
			$('#jobFS_loadManager').tooltip();
			$('#jobFS_taskSelector').tooltip();
			$('#jobFS_poolMaxJobsDefault').tooltip();
			$('#jobFS_userMaxJobsDefault').tooltip();
			$('#jobFS_defaultMinSharePreemptionTimeout').tooltip();
			$('#jobFS_fairSharePreemptionTimeout').tooltip();
		} 
};
