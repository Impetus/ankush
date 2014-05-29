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
<!-- Capacity Queue Configuration Page -->

<%@ include file="../../layout/blankheader.jsp"%>
<script>
$('.dropdown-toggle').dropdown();
</script>
<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left">Add Queue</h2>
		</div>
		<div class="span3">
			<button class="span3 btn-error" id="errorBtnQueue"
				style="display: none; height: 29px; color: white; border: none; cursor: text; background-color: #EF3024 !important;"></button>
		</div>
		<div class="span5 text-right">
			<button class="btn" style="margin-right: 8px;"
				onclick="com.impetus.ankush.removeChild(4)">Cancel</button>
			<button class="btn" style="margin-right: 70px;"
				onclick="com.impetus.ankush.jobScheduler.syncQueueTable('<c:out value='${newQueueId}'/>');">Save</button>
		</div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="error-div-queue" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content-queue" style="color: red;"></span>
			</div>
		</div>
		<br>
		<div id="error-div-hadoop" class="span12 error-div-hadoop"
			style="display: none;">
			<span id="popover-content" style="color: red;"></span>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Queue:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large"
					id="capacitySchedulerQueue-<c:out value='${newQueueId}'/>"
					data-toggle="tooltip" data-placement="right" title=""></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Capacity:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large"
					id="capacitySchedulerCapacity-<c:out value='${newQueueId}'/>"
					placeholder="%" data-toggle="tooltip" data-placement="right"
					title="" style="width: 100px"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Max. Capacity:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large"
					id="capacitySchedulerMaxCapacity-<c:out value='${newQueueId}'/>"
					placeholder="%" data-toggle="tooltip" data-placement="right"
					title="" style="width: 100px"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Min. User Limit:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large" placeholder="Numeric Value"
					id="capacitySchedulerMinUserLimit-<c:out value='${newQueueId}'/>"
					data-toggle="tooltip" data-placement="right" title=""
					style="width: 100px"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">User Limit:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large" placeholder="Numeric Value"
					id="capacitySchedulerUserLimit-<c:out value='${newQueueId}'/>"
					data-toggle="tooltip" data-placement="right" title=""
					style="width: 100px"></input>
			</div>
		</div>
		<div class="row-fluid" id="div_CSChild_PrioritySupport">
			<div class="span2 ">
				<label class="text-right form-label">Priority Support: </label>
			</div>
			<div class="span9 form-image text-left btn-group" id=""
				data-toggle="buttons-radio">
				<button type="button" class="btn active"
					id="queuePrioritySupportON-<c:out value='${newQueueId}'/>">ON</button>
				<button type="button" class="btn"
					id="queuePrioritySupportOFF-<c:out value='${newQueueId}'/>">OFF</button>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Max. Active Tasks:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large"
					id="capacitySchedulerMaxActivityTasks-<c:out value='${newQueueId}'/>"
					placeholder="Numeric Value" data-toggle="tooltip"
					data-placement="right" title="" style="width: 100px"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Max. Active Tasks/User:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large"
					id="capacitySchedulerMaxActivityTasksVal-<c:out value='${newQueueId}'/>"
					placeholder="Numeric Value" data-toggle="tooltip"
					data-placement="right" title="" style="width: 100px"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Job Count:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large" placeholder="Numeric Value"
					id="capacitySchedulerJobCount-<c:out value='${newQueueId}'/>"
					value="10" data-toggle="tooltip" data-placement="right" title=""
					style="width: 100px"></input>
			</div>
		</div>
	</div>
</div>
