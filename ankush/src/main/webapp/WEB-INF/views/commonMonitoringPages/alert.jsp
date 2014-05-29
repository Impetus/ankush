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
<%@ include file="../layout/blankheader.jsp"%>
<style>
.edit{
background-color: 'red';
}
</style>
<script>
function autoRefreshAlertPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = ''; 
	obj1.callFunction = "";
	obj1.time = 0;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	$(document).ready(function(){
		$.fn.editable.defaults.mode = 'inline';
		$('.editOnClick').textEditable({
			cssClass : 'edit',
		});	
		$("#mailingList").tooltip();
		$('.editOnClick').mouseover(function(){
				$(this).attr('data-original-title', 'Click to edit');
				$(this).attr('data-placement', 'mouse');
				$(this).tooltip();
		});
		configAlertsUsageTable=$('#configAlertsUsageTable').dataTable({
			"bJQueryUI" : false,
			"bPaginate" : false,
			"bLengthChange" : false,
			"bFilter" : false,
			"bSort" : false,
			"bInfo" : false,
			"bAutoWidth" : false,
		});
		/* This method will fill the default values on Alert page */
 		com.impetus.ankush.commonMonitoring.getDefaultAlertValues();
 		autoRefreshAlertPage();
	});
</script>

<!--This page will show alert page  -->
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Alerts</h2>
		</div>
		<div class="span8 text-right">
			<button class="btn mrgr10"
				onclick="com.impetus.ankush.removeChild(3)">Cancel</button>
			<button class="btn"
				onclick="com.impetus.ankush.commonMonitoring.saveAlerts()">Save</button>
		</div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
	<div class="row-fluid">
			<div id="error-div-alert" class="span12 error-div-hadoop"
				style="display: none;">
		<span id="popover-content-alert" style="color: red"></span>
	</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Refresh Interval:</label>
			</div>
			<div class="span4 ">
				<div class="editOnClick" id="refreshInterval"
					style="padding-top: 15px; width: 40px;"></div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Mailing List:</label>
			</div>
			<div class="span5 ">
				<input type="checkbox" class="mrgt15 left" id="alertInform" style="margin-right:10px"/><label class="text-left  form-label left"
					style="color: black;">Inform All
					Administrators</label>
			</div>
			
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Email:</label>
			</div>
			<div class="span3 ">
				<input type="text" id="mailingList" title="Enter mail list" data-placement="right" placeholder="Email"/>
			</div>
		</div>
		<br />
		<div class="row-fluid" id="configAlertsTable">
			<div class="span5">
				<table class="table table-striped table-border"
					id="configAlertsUsageTable">
					<thead style="text-align: left;">
						<tr>
							<th>Property</th>
							<th>Warn(%)</th>
							<th>Alert(%)</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
