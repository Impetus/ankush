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
<!-- Alerts & Warning percentage levels Configuration Page  -->

<%@ include file="../../layout/blankheader.jsp"%>
<style>
.edit{
background-color: 'red';
}
</style>
<script>
	$(document).ready(function(){
		var clusterId = "<c:out value='${clusterId}'/>";
		$.fn.editable.defaults.mode = 'inline';
		
		$('.editOnClick').textEditable({
			cssClass : 'edit',
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
 		com.impetus.ankush.hadoopMonitoring.getDefaultAlertValues(clusterId);
	});
</script>
<div class="section-header">
	<div  class="row-fluid mrgt20">
	  	<div class="span4"><h2 class="heading text-left">Alerts</h2></div>
		<div class="span8 text-right">
		  	<button class="btn mrgr10" onclick="com.impetus.ankush.removeChild(3)">Cancel</button>
		  	<button class="btn"onclick="com.impetus.ankush.hadoopMonitoring.saveAlerts(<c:out value='${clusterId}'/>)">Save</button>
		 </div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Refresh Interval:</label>
			</div>
			<div class="span4 ">
				<div class="editOnClick padt15" id="refreshInterval"></div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Mailing List:</label>
			</div>
			
			<div class="span4 ">
				<label class="text-left  form-label" style="color:black;"><input type="checkbox" id="alertInform"  style="margin-top:-1px"/> Inform All Administrators</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
			
			</div>
			<div class="span4 ">
				<div class="editOnClick padt15" id="mailingList">&nbsp;</div>
			</div>
		</div>
		<br/>
		<div class="row-fluid"  id="configAlertsTable">
			<div class="span10" >
				<table class="table table-striped table-border" id="configAlertsUsageTable" style="width: 50%; margin-left:50px;">
					<thead style="text-align: left;">
						<tr>
							<th>Property</th>
							<th>Warn(%)</th>
							<th>Alert(%)</th>
						</tr>
					</thead>
					<tbody style="text-align: center; padding-left: 90px;">
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
