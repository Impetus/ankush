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
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/js/hadoop/hadoopJobs.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				$('#submitJob_Job').tooltip();
				$('#filePath_JarFile').tooltip();
				com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
				com.impetus.ankush.hadoopJobs.getJobTrackerStatus('JobSubmission', <c:out value="${clusterId}"/>);
				if((com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hadoop') || (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')){
					$('#headerSubmitJob').text('Submit Job');
					$('#labelSubmitJob').text('Job:');
					$('#labelJobArgument').text('Job Arguments:');
					$('#dialogSuccess').text('Job submission request placed successfully.');
					$('#dialogHeading').text('Submit Job');
				}
				else if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hadoop2'){
					$('#headerSubmitJob').text('Submit Application');	
					$('#labelSubmitJob').text('Application:');
					$('#labelJobArgument').text('Application Arguments:');
					$('#dialogSuccess').text('Application submission request placed successfully.');
					$('#dialogHeading').text('Submit Application');
				}
			});
</script>

<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h2 class="heading text-left left" id="headerSubmitJob"></h2>
			<button class="btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
			
		</div>
		
		<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		-->
		
		<div class="span5 text-right">
			<button class="btn" style="margin-right: 8px;"
				onclick="com.impetus.ankush.removeChild(2);"
				id="btnSubmitJob_Cancel">Cancel</button>
			<button class="btn" id="btnSubmitJob_Submit"
				style="margin-right: 20px;"
				onclick="com.impetus.ankush.hadoopJobs.submitJobs('<c:out value="${clusterId}"/>');">Submit</button>
		</div>
	</div>
</div>

<div class="section-body content-body mrgnlft8">
<div class="container-fluid ">
	<div class="row-fluid">
		<div id="error-div-hadoop" class="span12 error-div-hadoop"
			style="display: none;">
			<span id="popover-content"
				style="color: red;"></span>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label" id="labelSubmitJob"></label>
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
				id="uploadframeJarFile" name="uploadframeJarFile"
				style="float:left;"></iframe>
			<form action="" id="uploadframeJarFile_Form"
				target="uploadframeJarFile" enctype="multipart/form-data"
				method="post" style="float: left; margin: 0px;">
				<input style='visibility: hidden;float: right;' id='fileBrowse_JarFile' type='file'
					class="input-large" name='file'></input><input type="text-large"
					id="filePath_JarFile" data-toggle="tooltip" readonly="readonly"
					placeholder="Upload Jar File" title="Jar file for Hadoop Job"
					data-placement="right" width="220px" class="input-large" style="cursor: pointer;float:left;background:white"
					onclick="com.impetus.ankush.hadoopJobs.hadoopJarFileUpload();"></input>
			</form>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label" id="labelJobArgument"></label>
		</div>
		<div class="span10 form-image"
			style="vertical-align: middle; float: left;">
			<a href='#' id="submitJob_AddJobArg"
				onclick="javascript:com.impetus.ankush.hadoopJobs.addJobArgumentRow();"><input
				type='image'
				style='width: 20px; height: 20px; vertical-align: middle;'
				src="<c:out value='${baseUrl}' />/public/images/newUI-Icons/circle-plus.png"></input></a>
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
			<h4 id="dialogHeading">Submit Job</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;" id="dialogSuccess">
					</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal"  id="divOkbtn" class="btn">OK</a>
		</div>
	</div>
	</div>
</div>
