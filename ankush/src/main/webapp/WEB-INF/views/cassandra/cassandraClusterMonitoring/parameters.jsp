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
<style>

td.group {
	padding-left: 100px;
}
td.expanded-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.expanded-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center #c0e1ff !important;
}
td.collapsed-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.collapsed-group {
	padding-left: 30px !important;
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center #c0e1ff !important;
}
</style>
<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js/cassandra/configurationParam.js"
	type="text/javascript"></script>
<script>
var cassandraParameterTable = null;
$(document).ready(function(){
	//row grouping datatable for cassandra parameters
	cassandraParameterTable=$('#cassandraParameterTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
		}
	}).rowGrouping({
		bExpandableGrouping : true,
        iGroupingColumnIndex : 0});
	$("#cassandraParameterTable_filter").css({
		'display' : 'none'
	});
	$('#searchcassandraParameterTable').keyup(function() {
		$("#cassandraParameterTable").dataTable().fnFilter($(this).val());
	});
	com.impetus.ankush.configurationParam.confParameterPopulate();
});


</script>
<div class="section-header">
<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Configuration Parameters</h2>
			<button class="btn-error" id="errorBtnCassandraParameters"
				onclick=""
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important; position:relative; padding:0 15px; left:15px;"></button>
		</div>
		<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnCassandraParameters"
				onclick=""
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		-->
		<div class="span5 text-right">
			<button class="btn mrgr10"
				onclick="com.impetus.ankush.removeCurrentChild();">Revert</button>
			<button class="btn" id="applyParameters" data-loading-text="Applying..."
				onclick="com.impetus.ankush.configurationParam.applyChangesPostParamObj();">Apply</button>
		</div>
</div>
</div>
<div class="section-body">

<div class="container-fluid">
<div class="row-fluid">
			<div id="error-div-cassandraParameters" class="span12 errorDiv" style="display: none;">
				<span id="popover-content-cassandraParameters" style="color: red;"></span>
			</div>
		</div>
<div class="row-fluid mrgt20">
		<div class="span6 text-left">
			<button class="btn" id="addParameters"
				onclick="com.impetus.ankush.configurationParam.addParamDialog();"
				style="margin-top: 10px;">Add Parameter</button>
		</div>
		<div class="text-right span6">
					<input id="searchcassandraParameterTable" type="text"
						placeholder="Search">
		</div>
	</div>
	
	  <div id="deleteParameterDialogCassandra" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Parameter Delete</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;">
						Parameter has been marked on delete click. Changes will be applicable
						on Apply.</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					id="confirmDeleteParameterCassandra" class="btn">Delete</a>
			</div>
		</div>
		<div id="div_addParameter" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Add Parameter</h4>
			</div>
			<div class="modal-body" style="overflow : hidden;">
				<div class="row-fluid">
					<div class="span4">
						<label class="text-right form-label">File name:</label>
					</div>
					<div class="span6 text-left" style="margin-left: 23px">
						<select id="fileNameValueAddParam" data-placement="right"></select>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span4">
						<label class="text-right form-label">Parameter name:</label>
					</div>
					<div class="span6 text-left">
						<input type="text" class="input-large" id="newParameterName"
							style="margin-left: 13px" data-toggle="tooltip"
							data-placement="right" title="Enter Parameter Name"></input>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span4">
						<label class="text-right form-label">Parameter value:</label>
					</div>
					<div class="span6 text-left">
						<input type="text" class="input-large" id="newParameterValue"
							style="margin-left: 13px" data-toggle="tooltip"
							data-placement="right" title="Enter Parameter Value"></input>
					</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					id = "addParamCassandra"
					class="btn">Add</a>
			</div>
		</div>
	<div class="row-fluid mrgt10">
		<div class="span12 text-left">
			<table class="table table-striped" id="cassandraParameterTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>File</th>
						<th>File</th>
						<th>Parameter</th>
						<th>Value</th>
						<th></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	</div>
</div>
