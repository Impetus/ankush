<!------------------------------------------------------------------------------
-  ===========================================================
-  Ankush : Big Data Cluster Management Solution
-  ===========================================================
-  
-  (C) Copyright 2014, by Impetus Technologies
-  
-  This is free software; you can redistribute it and/or modify it under
-  the terms of the GNU Lesser General Public License (LGPL v3) as
-  published by the Free Software Foundation;
-  
-  This software is distributed in the hope that it will be useful, but
-  WITHOUT ANY WARRANTY; without even the implied warranty of
-  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-  See the GNU Lesser General Public License for more details.
-  
-  You should have received a copy of the GNU Lesser General Public License 
-  along with this software; if not, write to the Free Software Foundation, 
- Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->




<html>
<head>
<%@ include file="../../layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all"/>
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/hadoop/hadoopJobs.js"
	type="text/javascript"></script>
	<script>
	$(document).ready(
			function() {
				$('#submitJob_Job').tooltip();
				$('#filePath_JarFile').tooltip();
				com.impetus.ankush.hadoopJobs.getJobTrackerStatus('JobSubmission', <c:out value="${clusterId}"/>);
				com.impetus.ankush.hadoopJobs.initTables_SubmitJob();
				var url = baseUrl+'/monitor/'+clusterId+'/hadoopversion?component=Hadoop';
				var submission = 'Job';
				com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
					if(result.output.isHadoop2)
						submission = 'Application';
					$('#headerSubmitJob').text('Submit '+submission);
					$('#labelSubmitJob').text(submission+':');	
					$('#labelJobArgument').text('Arguments:');
					$('#dialogSuccess').text(submission+' submission request placed successfully.');
					$('#dialogHeading').text('Submit '+submission);
				});
				
					
			});
</script>
	
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
  				<h1 id="headerSubmitJob" class="left"></h1>
				<button class="btn btn-danger element-hide left"
						id="error-header-button"></button>
						<button class="btn btn-default pull-right" id="btnSubmitJob_Submit"
							onclick="com.impetus.ankush.hadoopJobs.submitJobs('<c:out value="${clusterId}"/>');">Submit</button>
			</div>
<div class="page-body" id="main-content">
<%@ include file="../../layout/breadcrumbs.jsp"%>
	<div class="container-fluid ">
	<div class="row">
		<div id="error-div-hadoop" class="col-md-12 error-div-hadoop"
			style="display: none;">
			<span id="popover-content"
				style="color: red;"></span>
		</div>
	</div>
	<div class="row">
		<div class="col-md-1 text-right">
			<label class="form-label" id="labelSubmitJob"></label>
		</div>
		<div class="col-md-11 col-md-2 text-left">
			<input type="text" class="input-large form-control" id="submitJob_Job"
				placeholder="Class with main method" data-toggle="tooltip"
				data-placement="right" title="Job Name"></input>
		</div>
	</div>
	<div class="row">
		<div class="col-md-1 text-right mrgt20">
			<label class="form-label">JAR Path:</label>
		</div>
		<div class="col-md-11 col-md-2 text-left">
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
					data-placement="right" class="input-large form-control" style="cursor: pointer;float:left;background:white"
					onclick="com.impetus.ankush.hadoopJobs.hadoopJarFileUpload();"></input>
			</form>
		</div>
	</div>
	<div class="row mrgt10">
		<div class="col-md-1 text-right">
			<label class="form-label" id="labelJobArgument"></label>
		</div>
		<div class="col-md-11 col-md-2 form-image mrg10"
			style="vertical-align: middle; float: left;">
			<a href='#' id="submitJob_AddJobArg"
				onclick="javascript:com.impetus.ankush.hadoopJobs.addJobArgumentRow();"><input
				type='image'
				style='width: 20px; height: 20px; vertical-align: middle;'
				src="<c:out value='${baseUrl}' />/public/images/newUI-Icons/circle-plus.png"></input></a>
		</div>
	</div>
	<div class="row">
		<div class="col-md-1 ">
			<label class="text-right form-label"></label>
		</div>
		<div class="col-md-11	 text-left">
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
	
	
	<div class="modal fade" id="div_RequestSuccess" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 id="dialogHeading">Submit Job</h4>
					</div>
					<div class="modal-body">
						<div class="row">
				<div class="col-md-12" style="text-align: left; font-size: 14px;" id="dialogSuccess">
					</div>
			</div>
		
					</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal"  id="divOkbtn" class="btn btn-default">OK</a>
					</div>
				</div>
			</div>
	</div>
</div>
</div>
</div>
<script>
$(document).ready(function(){
	
	
});
</script>
</body>
</html>
