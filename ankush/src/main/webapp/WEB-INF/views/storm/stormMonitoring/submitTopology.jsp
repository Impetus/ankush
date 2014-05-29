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
<!-- Hadoop Job submit page -->

<%@ include file="../../layout/blankheader.jsp"%>

<script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript">
</script>
<script type="text/javascript">
var tblJobArguments = null;
	$(document).ready(
			function() {
				if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
					com.impetus.ankush.stormMonitoring.removeStormMonitoringPageAutorefresh();
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
			});
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading text-left left">Submit Topology</h2>
			
			<button class="btn-error" id="errorBtnHadoop-submitTopo"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important; padding:0 15px; left:15px; position:relative"></button>
		</div>
		<!--  <div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		-->
		<div class="span5 text-right">
			<button class="btn mrgr10"	onclick="com.impetus.ankush.removeCurrentChild();"
				id="btnSubmitJob_Cancel">Cancel</button>
				<button id="btnSubmitJob_Submit" onclick="com.impetus.ankush.stormMonitoring.submitTopology();" data-loading-text="Submitting..." class="btn">Submit</button>
		</div>
	</div>
</div>

<div class="section-body content-body mrgnlft8">
<div class="container-fluid">
	<div class="row-fluid">
		<div id="error-div-submitTopo" class="span12 error-div-hadoop"
			style="display: none;">
			<span id="popover-content-submitTopo"
				style="color: red;"></span>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Main Class:</label>
		</div>
		<div class="span10 text-left">
			<input type="text" class="input-large" id="submitJob_Job"
				placeholder="Class with main method" data-toggle="tooltip"
				data-placement="right" title="Job Name"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">JAR Path:</label>
		</div>
		<div class="span10 text-left">
			<iframe style="width: 1px; height: 1px; border: 0px;"
				id="uploadframeJarFile" name="uploadframeJarFile" class="text-left"></iframe>
			<form action="" id="uploadframeJarFile_Form"
				target="uploadframeJarFile" enctype="multipart/form-data"
				method="post" class="left mrg0">
				<input style='visibility: hidden; display:none' id='fileBrowse_JarFile' type='file'
					class="input-large right" name='file'></input><input type="text-large"
					id="filePath_JarFile" data-toggle="tooltip" readonly="readonly"
					placeholder="Upload Jar File" title="Jar file for topology"
					data-placement="right" width="220px" class="input-large upload_jar_input"
					onclick="com.impetus.ankush.stormMonitoring.JarFileUpload();"></input>
			</form>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Topology Arguments:</label>
		</div>
		<div class="span10 form-image left">
			<a href='#' id="submitJob_AddJobArg"
				onclick="javascript:com.impetus.ankush.stormMonitoring.addJobArgumentRow();"><input
				type='image' class="add_icon" src="<c:out value='${baseUrl}' />/public/images/newUI-Icons/circle-plus.png"></input></a>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label"></label>
		</div>
		<div class="span10 text-left">
			<table id="tblJobArguments"
				style="border: 0px none; vertical-align: middle;">
				<thead style="display: none">
					<tr>
						<th style="width: 100px;">Argument</th>
						<th style="width: 100px; vertical-align: middle;">Delete</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div id="div_RequestSuccess" class="modal hide fade"
		style="display: none;">
		<div class="modal-header text-center">
			<h4>Submit Job</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12 text-left font14">
					Topology submission
				request placed successfully.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal"  id="divOkbtn" class="btn">OK</a>
		</div>
	</div>
	</div>
</div>
