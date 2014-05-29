<!------------------------------------------------------------------------------
-===========================================================
-Ankush : Big Data Cluster Management Solution
-===========================================================
-
-(C) Copyright 2014, by Impetus Technologies
-
-This is free software; you can redistribute it and/or modify it under
-the terms of the GNU Lesser General Public License (LGPL v3) as
-published by the Free Software Foundation;
-
-This software is distributed in the hope that it will be useful, but
-WITHOUT ANY WARRANTY; without even the implied warranty of
-MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-See the GNU Lesser General Public License for more details.
-
-You should have received a copy of the GNU Lesser General Public License 
-along with this software; if not, write to the Free Software Foundation, 
-Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->
<!-- Fair Scheduler Configuration Page  -->

<div id="div_fairScheduler" style="display: none;">
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Pool Name:</label>
		</div>
		<div class="span9 text-left">
			<div class="row-fluid">
				<select class="input-large"
				id="jobFS_pool" data-toggle="tooltip"  style="float: left"
				data-placement="right" title="Default Pool name for jobs">
					<option value="Select Pool">Select Pool</option>
				</select>
				<button class="btn" style="margin-top:10px;margin-left: 10px" onclick="com.impetus.ankush.jobScheduler.goToPoolsTable();">Add Pool</button>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Pool Name Property:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobFS_poolNameProperty" data-toggle="tooltip"
				data-placement="right" title="Jobconf property to determine the pool that a job belongs in"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Job Preemption:</label>
		</div>
		<div class="span9 form-image text-left btn-group" id="div_jobFS_Preemption"
			data-toggle="buttons-radio">
			<button type="button" class="btn" id="jobFS_enablePreemption">ON</button>
			<button type="button" class="btn active" id="jobFS_disablePreemption">OFF</button>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Allocation File:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobFS_allocationFile" data-toggle="tooltip"
				data-placement="right" title="Absolute Path to the allocation file, fair-schedule.xml"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Size Based Weight:</label>
		</div>
		<div class="span9 form-image text-left btn-group" id="div_jobFS_SizeBasedWeigth"
			data-toggle="buttons-radio">
			<button type="button" class="btn" id="jobFS_enableSizeBasedWeigth">ON</button>
			<button type="button" class="btn active" id="jobFS_disableSizeBasedWeigth">OFF</button>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Preemption Only Log:</label>
		</div>
		<div class="span9 form-image text-left btn-group" id="div_jobFS_PreemptionOnlyLog"
			data-toggle="buttons-radio">
			<button type="button" class="btn" id="jobFS_enablePreemptionOnlyLog">ON</button>
			<button type="button" class="btn active" id="jobFS_disablePreemptionOnlyLog">OFF</button>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Update Interval:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large" style="width: 100px"
				id="jobFS_updateInterval" data-toggle="tooltip"
				data-placement="right" title="Interval at which to update fair share calculations, Default: 500(ms)"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Preemption Interval:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large" style="width: 100px"
				id="jobFS_preemptionInterval" data-toggle="tooltip"
				data-placement="right" title="Interval at which to check for tasks to preempt, Default: 15000(ms)"></input>
		</div>
	</div>
	<div class="row-fluid" style="display: none;">
		<div class="span2">
			<label class="text-right form-label">Weight Adjuster Class:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobFS_weightAdjuster" data-toggle="tooltip"
				data-placement="right" title="A class to adjust the weights of running jobs"></input>
		</div>
	</div>
	<div class="row-fluid" style="display: none;">
		<div class="span2">
			<label class="text-right form-label">Load Manager Class:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobFS_loadManager" data-toggle="tooltip"
				data-placement="right" title="A class that determines how many maps and reduces can run on a given TaskTracker"></input>
		</div>
	</div>
	<div class="row-fluid" style="display: none;">
		<div class="span2">
			<label class="text-right form-label">Task Selector Class:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobFS_taskSelector" data-toggle="tooltip"
				data-placement="right" title="A class that determines which task from within a job to launch on a given tracker"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span3 text-left">
			<label class="form-label section-heading" style="margin-left: 30px;">Override
				Values </label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Max. Running Jobs/Pool:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large" style="width: 100px"
				id="jobFS_poolMaxJobsDefault" data-toggle="tooltip"
				data-placement="right" title="Maximum running jobs allowed per pool"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Max. Running Jobs/User:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobFS_userMaxJobsDefault" data-toggle="tooltip" style="width: 100px"
				data-placement="right" title="Maximum running jobs allowed per user"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Min. Share Preemption Timeout:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large" style="width: 100px"
				id="jobFS_defaultMinSharePreemptionTimeout" data-toggle="tooltip"
				data-placement="right" title="The time in milliseconds that the pool will wait before killing other pools' tasks"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Fair Share Preemption Timeout:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large" style="width: 100px"
				id="jobFS_fairSharePreemptionTimeout" data-toggle="tooltip"
				data-placement="right" title=" Preemption timeout used when jobs are below half their fair share"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Pool Scheduling Mode:</label>
		</div>
		<div class="span9 form-image text-left btn-group" id="div_jobFS_DefaultPoolSchedulingMode"
			data-toggle="buttons-radio">
			<button type="button" class="btn active" id="jobFS_defaultPoolSchedulingMode_Fair">Fair</button>
			<button type="button" class="btn" id="jobFS_defaultPoolSchedulingMode_Fifo">FIFO</button>
		</div>
	</div>
	<div class="row-fluid">
		<a name="jump_divFairUserTable"></a>
	</div>
	<div class="row-fluid">
		<div class="span6">
			<div class="text-left" style="padding-top: 20px;float: left;">
				<label class='section-heading text-left'>Hadoop Users</label>
			</div>
			<div class="span2 text-left" style="margin-top: 10px;margin-left: 10px;">
  				<button class="btn" style="height: 25px;margin-top:6px" onclick="com.impetus.ankush.jobScheduler.AddNewUser();">Add</button>
			</div>
			
			<div class="text-right">
				<input id="jobFS_searchUsers" type="text" placeholder="Search" style="width: 200px;">
			</div>
		</div>
	</div>
	<div class="row-fluid " id="divFairUserTable">
		<div class="span6">
			<table class="table table-stripped" id="FairUserTable"
				data-toggle="tooltip" data-placement="right" width="100%"
				style="border: 1px solid; border-color: #CCCCCC;">
				<thead style="text-align: left;">
					<tr>
						<th>User Name</th>
						<th>Max. Running Jobs</th>
						<th></th>
					</tr>
				</thead>
				<tbody>

				</tbody>
			</table>
		</div>
	</div>
	<div class="row-fluid">
		<a name="jump_divFairPoolTable"></a>
	</div>
	<div class="row-fluid">
		<div class="span12">
			<div class="text-left" style="padding-top: 20px; float: left">
				<label class='section-heading text-left'>Pools</label>
			</div>

			<div class="span2 text-left" style="margin-top: 10px;margin-left: 10px;">
  				<button id="btnJobScheduler_Fair_AddPools" class="btn" style="height: 25px;margin-top:6px" onclick="com.impetus.ankush.jobScheduler.AddNewPool();">Add</button>
			</div>
			
			<div class="text-right">
				<input id="jobFS_searchPools" type="text" placeholder="Search" style="width: 200px;">
			</div>
		</div>
	</div>
	<div class="row-fluid" id="divFairPoolTable">
		<div class="span12">
			<table class="table table-striped" id="FairPoolTable"
				data-toggle="tooltip" data-placement="right" width="100%"
				style="border: 1px solid; border-color: #CCCCCC;">
				<thead style="text-align: left;">
					<tr>
						<th>Pool Name</th>
						<th>Min. Maps</th>
						<th>Min. Reduces</th>
						<th>Max. Maps</th>
						<th>Max. Reduces</th>
						<th>Max. Running Jobs</th>
						<th>Weight</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
</div>
