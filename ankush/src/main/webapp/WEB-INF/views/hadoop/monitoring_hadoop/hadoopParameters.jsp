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
<!-- Hadoop Parameters page, containing link for parameter addition ,edition and deletion  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js/hadoop/hadoopParameters.js"
	type="text/javascript"></script>
	<script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript"></script>
<style>

td.group {
	padding-left: 90px;
}
td.expanded-group {
	padding-left: 60px !important;
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.expanded-group {
	padding-left: 60px !important;
	background: url("../public/images/newUI-Icons/circle-minus.png")
		no-repeat scroll left center #c0e1ff !important;
}
td.collapsed-group {
	padding-left: 60px !important;
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center transparent;
}
tr:hover td.collapsed-group {
	padding-left: 60px !important;
	background: url("../public/images/newUI-Icons/circle-plus.png")
		no-repeat scroll left center #c0e1ff !important;
}
</style>
<script>
	$(document).ready(
			function() {
				$.fn.editable.defaults.mode = 'inline';
				$('.dropdown-toggle').dropdown();
				var clusterId = "<c:out value='${clusterId}'/>";
				com.impetus.ankush.hadoopParameters
						.initializeParameterTable(clusterId);
			});

	$('#newParameterName').tooltip();
	$('#newParameterValue').tooltip();
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Hadoop Parameters</h2>
			<button class="btn-error" id="errorBtnHadoopParameters"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
			
		</div>


<!-- 
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnHadoopParameters"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
	 -->	
		
		<div class="span5 text-right">
			<button class="btn mrgr10"
				onclick="com.impetus.ankush.removeCurrentChild();">Revert</button>
			<button class="btn" id="applyParameters"
				onclick="com.impetus.ankush.hadoopParameters.postParameterData();">Apply</button>
		</div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="error-div-hadoopParameters" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content-hadoopParameters" style="color: red;"></span>
			</div>
		</div>
		<div id="deleteParameterDialogHadoop" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Parameter Delete</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12 font14 text-left">
						Parameter has been marked for delete. Changes will be applicable
						on Apply.</div>
				</div>
			</div>
			<br>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">Cancel</a> <a href="#"
					id="confirmDeleteParameterHadoop" onclick="" class="btn">Delete</a>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<button class="btn mrgt15" id="addParameter"
				onclick="com.impetus.ankush.hadoopParameters.addParameterDialog();">Add Parameter</button>

			<div class="text-right" style="float: right">
				<input id="searchParametersTableHadoop" type="text"
					placeholder="Search">
			</div>
		</div>
		<div class="row-fluid">
			<div id="parameterTableDiv">
				<table class="table table-striped table-border" id="parameterTable">
					<thead>
						<tr>
							<th style="margin-left: 200px; width: 180px;">File</th>
							<th>File</th>
							<th style="width: 350px;">Parameter</th>
							<th style="width: 350px;">Value</th>
							<th style="width: 10px;"></th>
							<th style="display: none;">Status</th>
						</tr>
					</thead>

				</table>
			</div>
		</div>
		<div id="div_addParameter" class="modal hide fade"
			style="display: none;width: 58%">
			<div class="modal-header text-center">
				<h4>Add Parameter</h4>
			</div>
			<div class="modal-body" style="overflow : hidden;">
				<div class="row-fluid">
					<div class="span4">
						<label class="text-right form-label">File name:</label>
					</div>
					<div class="span6 text-left" style="margin-left: 23px">
						<select id="fileNameValue" onchange="" data-placement="right"></select>
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
					onclick="com.impetus.ankush.hadoopParameters.addData();"
					class="btn">Add</a>
			</div>
		</div>
	</div>
</div>
