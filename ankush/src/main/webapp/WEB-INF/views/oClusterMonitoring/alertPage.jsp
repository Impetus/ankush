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
<!-- Page for cluster alerts -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
$(document).ready(function(){
	$.fn.editable.defaults.mode = 'inline';
	$('.editOnClick').textEditable({
		cssClass : 'edit',
	});
	alertTable=$("#alertTable").dataTable({
	"bJQueryUI" : false,
	"bPaginate" : false,
	"bLengthChange" : false,
	"bFilter" : false,
	"bSort" : false,
	"bInfo" : false,
	"bAutoWidth" : false,
	"aoColumns" : [
					{
						"fnRender" : function(
								oObj) {
							return '<a class="" id="metricName' + oObj.aData[3] + '" >'
							+ oObj.aData[0]
							+ '</a>';
						}
					},
					{
						"fnRender" : function(
								oObj) {
							return '<a class="editableLabel" id="warningLevel-' + oObj.aData[3]
							+ '" style="width:20px;">'
							+ oObj.aData[1]
							+ '</a>';
						}
					},
					{
						"fnRender" : function(
								oObj) {
							return '<a class="editableLabel" id="alertLevel-' + oObj.aData[3] + '">'
							+ oObj.aData[2]
							+ '</a>';
						}
					},
					 ],
});
});
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span6">
			<h2 class="heading left">Alerts</h2>
			<button class="btn-error" id="alertErrorButton"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative" onclick="com.impetus.ankush.oClusterMonitoring.focusError()"></button>
		</div>
		
		<!--  
			<div class="span3 minhgt0">
				<button class="span3 btn-error" id="alertErrorButton"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;" onclick="com.impetus.ankush.oClusterMonitoring.focusError()"></button>
			</div>
			-->
		<div class="span6 text-right">
				<button class="btn"
					onclick="com.impetus.ankush.oClusterMonitoring.cancelParam('alertParam')">Cancel</button>
					
					<button class="btn mrgl10"
					onclick="com.impetus.ankush.oClusterMonitoring.saveAlerts()">Save</button>
			</div>
			
		
	</div>
</div>
<div class="section-body">
<div class="container-fluid mrgnlft8">
<div id="alertpageError" class="commonErrorDiv" style="display: none;margin-top: 20px;"></div>
	<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Refresh Interval:</label>
			</div>
			<div class="span4 ">
				<div class="editOnClick label-black  padt15" id="refreshInterval"></div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Mailing List:</label>
			</div>
			
			<div class="span3 ">
				<label class="text-left label-black form-label"><input  type="checkbox" id="mailingListCheck"  style="margin-top:-1px"/> Inform all Administrators</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				
			</div>
			<div class="span4 ">
				<input class="input-large" type="text" id="mailingList"></<input>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span6 ">
				<table class="table table-striped" id="alertTable"
					style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4;">
					<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
						<tr>
							<th style="width: 40%">Property</th>
							<th style="width: 30%">Warn(%)</th>
							<th style="width: 30%">Alert(%)</th>
						</tr>
					</thead>
					<tbody style="color: black;">
						
					</tbody>
				</table>
			</div>
		</div>
		</div>
</div>
