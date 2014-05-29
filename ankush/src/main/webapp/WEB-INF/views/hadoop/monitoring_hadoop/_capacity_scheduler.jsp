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
<!-- Capacity Scheduler configuration Page  -->

<script>
	$.fn.editable.defaults.mode = 'inline';
	$('#jobSchedulerCapacity_pollingInterval').tooltip();
	$('#jobSchedulerCapacity_accessControlList').tooltip();
	$('#jobSchedulerCapacity_workerThreads').tooltip();
	$('#jobSchedulerCapacity_maxsystemJobs').tooltip();
	$('#jobSchedulerCapacity_workerThreads').tooltip();
	$('#jobSchedulerCapacity_minUserLimit').tooltip();
	$('#jobSchedulerCapacity_maxActivetasks_queue').tooltip();
	$('#jobSchedulerCapacity_maxActivetasks_user').tooltip();
	$('#jobSchedulerCapacity_jobCount').tooltip();
	$('#divCapacityQueueTable').tooltip();
</script>
<div id="div_capacityScheduler" style="display: none;">
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Polling Interval:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobSchedulerCapacity_pollingInterval" data-toggle="tooltip"
				data-placement="right"
				title="Amount of time in miliseconds which is used to poll the job queues for jobs to initialize"></input>
		</div>
	</div>
	<div class="row-fluid" id="div_CS_AccessControlList" style="display: none;">
		<div class="span2">
			<label class="text-right form-label">Access Control List:</label>
		</div>
		<div class="span9 form-image text-left btn-group"
			data-toggle="buttons-radio">
			<button type="button" class="btn active" id="enableAccessControl">Enable</button>
			<button type="button" class="btn" id="disableAccessControl">Disable</button>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Worker Threads:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobSchedulerCapacity_workerThreads" data-toggle="tooltip"
				data-placement="right"
				title="Number of worker threads which would be used by Initialization poller to initialize jobs in a set of queue"></input>
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
			<label class="text-right form-label">Max. system Jobs:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobSchedulerCapacity_maxsystemJobs" data-toggle="tooltip"
				data-placement="right" title=""></input>
		</div>
	</div>
	<div class="row-fluid" id="div_CS_PrioritySupport">
		<div class="span2">
			<label class="text-right form-label">Priority Support:</label>
		</div>
		<div class="span9 form-image text-left btn-group"
			data-toggle="buttons-radio">
			<button type="button" class="btn active" id="prioritySupportON">ON</button>
			<button type="button" class="btn" id="prioritySupportOFF">OFF</button>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Min. User Limit:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobSchedulerCapacity_minUserLimit" placeholder="%"
				data-toggle="tooltip" data-placement="right"
				title="Minimum user limit"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Max. Active Tasks/Queue
				:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobSchedulerCapacity_maxActivetasks_queue" data-toggle="tooltip"
				data-placement="right" title="Maximium active tasks/queue"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Max. Active Tasks/User:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobSchedulerCapacity_maxActivetasks_user" data-toggle="tooltip"
				data-placement="right" title="Max. Active Tasks/User"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Job Count:</label>
		</div>
		<div class="span9 text-left">
			<input type="text" class="input-large"
				id="jobSchedulerCapacity_jobCount" value="10" data-toggle="tooltip"
				data-placement="right" title="Job count"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span12">
			<div class="span1 text-left" style="padding-top: 20px; float: left">
				<label class='section-heading text-left'>Queues</label>
			</div>
			<div class="span2">
				<button class="btn" id="btnAddCapacityQueue"
					onclick="com.impetus.ankush.jobScheduler.addQueue();"
					style="margin-right: 50px; margin-top: 15px;">Add</button>
			</div>
			<div class="text-right">
				<input id="searchQueues" type="text" placeholder="Search">
			</div>
		</div>
	</div>
	<div id="deleteQueueDialog" class="modal hide fade"
		style="display: none;">
		<div class="modal-header text-center">
			<h4>Queue Delete</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					The Queue will be permanently deleted. Once deleted, data cannot be
					recovered.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" id="confirmDeleteButtonQueue"
				onclick=""
				class="btn">Delete</a> <a href="#" data-dismiss="modal" class="btn" id="cancelDeleteQueue">Cancel</a>
		</div>
	</div>
	<div class="row-fluid" id="divCapacityQueueTable" data-toggle="tooltip"
		title="">
		<div class="span12">
			<table class="table table-striped" id="capacityQueueTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Queue</th>
						<th>Capacity</th>
						<th>Max. Capacity</th>
						<th>Min. User Limit</th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<tbody style="text-align: left;">
				</tbody>
			</table>
		</div>
	</div>
</div>
