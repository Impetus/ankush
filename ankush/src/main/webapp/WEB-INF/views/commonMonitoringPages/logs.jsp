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
<!-- Hadoop Cluster Logs Page, showing log file content, and a link for Downloading a file  -->

<%@ include file="../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/jquery.fileDownload.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/jquery.jscroll.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/jquery.jscroll.min.js"
	type="text/javascript"></script>
	<script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript"></script>
<script>
$(document).ready(function(){
	var clusterId = "<c:out value='${clusterId}'/>";
	$("#nodeType").tooltip();
	$("#nodeIP").tooltip();
	$("#fileName").tooltip();
	$("#logView").jscroll(
			{
		loadingHtml: '<img alt="Loading..." />',
		debug : true,
		padding: 20,
		autoTrigger: true,
		nextSelector: 'a:last',
		contentSelector: ''
	});
	//com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
	com.impetus.ankush.commonMonitoring.getDefaultLogDownloadValue();
});
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left left">Logs</h2>
			<button class="btn-error" id="errorBtnHadoopLogs"
				onclick="com.impetus.ankush.commonMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>
		
		<!--  
		
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnHadoopLogs"
				onclick="com.impetus.ankush.commonMonitoring.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		-->
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="error-div-hadoopLogs" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content-hadoopLogs" style="color: red;"></span>
			</div>
		</div>
		<div id="fileSizeExceed" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Download Failed</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12 text-left font14">
						File cannot be downloaded as its size exceeds 5MB</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" id="divOkbtn_NodeDD" class="btn">OK</a>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Type:</label>
			</div>
			<div class="span10 text-left">
				<select id="nodeType"
					onchange="com.impetus.ankush.commonMonitoring.fillNodeType(<c:out value='${clusterId}'/>);"
					data-toggle="tooltip" data-placement="right"
					title="Select Node Type"></select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">IP:</label>
			</div>
			<div class="span10 text-left">
				<select id="nodeIP"
					onchange="com.impetus.ankush.commonMonitoring.nodeIPChange(<c:out value='${clusterId}'/>);"
					data-toggle="tooltip" data-placement="right" title="Select Node IP"></select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">File Name:</label>
			</div>
			<div class="span10 text-left">
				<select id="fileName" class="log_select" data-toggle="tooltip"
					data-placement="right" title="Select File Name"
					onchange="com.impetus.ankush.commonMonitoring.fileNameChange();"><option></option></select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label"></label>
			</div>
			<div class="span10 text-left">
				<div class="text-left mrgt10">
					<button class="btn mrgr10" id="viewLogs"
						onclick="com.impetus.ankush.commonMonitoring.logDisplay(<c:out value='${clusterId}'/>)">View</button>
						<button class="btn" id="downloadLogs"
						onclick="com.impetus.ankush.commonMonitoring.download(<c:out value='${clusterId}'/>)">Download</button>
				</div>
				
			</div>
		</div>
		<br>
		<div id="div_Logs" style="display: none;" class="container-fluid">
			<div class="row-fluid">
				<div class="span3 text-left">
					<label class="form-label section-heading mrgl30">Logs</label>
				</div>
			</div>
			<div class="row-fluid">
				<div class="data text-left mrgl10 font14">
					<pre id="logView">
					</pre>
				</div>
			</div>
		</div>
	</div>
</div>
