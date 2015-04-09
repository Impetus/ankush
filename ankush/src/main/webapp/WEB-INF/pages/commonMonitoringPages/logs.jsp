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
<%@ include file="../layout/header.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all"/>
<%@ include file="../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.fileDownload.js"
	type="text/javascript"></script>
	<script
	src="<c:out value='${baseUrl}' />/public/js3.0/ankush.validation.js"
	type="text/javascript"></script>
<script>

var jspPage = null;
$(document).ready(function(){
	$(window).scroll($.debounce(500,false,com.impetus.ankush.logs.appendLog));
	jspPage = '<c:out value='${page}'/>';
	hybridTechnology = '<c:out value='${hybridTechnology}'/>';
	$("#nodeType").tooltip();
	$("#nodeIP").tooltip();
	$("#fileName").tooltip();
	com.impetus.ankush.logs.getDefaultLogDownloadValue();
	
});
</script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
	<div class="row">
		<div class="col-md-2"><h1>logs</h1></div>	
	<div class="col-md-2"><button class="btn btn-danger element-hide mrgt20" id="error-header-button"></button></div>	
	
	</div>
	
   
</div>
<div class="page-body" id="main-content">
<%@ include file="../layout/breadcrumbs.jsp"%>
	<div class="container-fluid mrgnlft8">
		<div class="row">
			<div id="error-div" class="col-md-12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content"></span>
			</div>
		</div>
		<div id="fileSizeExceed" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Download Failed</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12 text-left font14">
						File cannot be downloaded as its size exceeds 5MB</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" id="divOkbtn_NodeDD" class="btn btn-default">OK</a>
			</div>
		</div>
		<div class="row">
			<div class="col-md-1">
				<label class="pull-right form-label">Type:</label>
			</div>
			<div class="col-md-11 col-lg-2 form-group text-left">
				<select id="nodeType"
					onchange="com.impetus.ankush.logs.fillNodeType(<c:out value='${clusterId}'/>);"
					data-toggle="tooltip" data-placement="right"
					title="Select Node Type" class="form-control"></select>
			</div>
		</div>
		<div class="row">
			<div class="col-md-1">
				<label class="pull-right form-label">Host Name:</label>
			</div>
			<div class="col-md-11 col-lg-2 form-group text-left">
				<select id="nodeIP"
					onchange="com.impetus.ankush.logs.nodeIPChange(<c:out value='${clusterId}'/>);"
					data-toggle="tooltip" data-placement="right" title="Select Node Host Name" class="form-control"></select>
			</div>
		</div>
		<div class="row">
			<div class="col-md-1">
				<label class="pull-right form-label">File Name:</label>
			</div>
			<div class="col-md-11  col-lg-3 form-group text-left">
				<select id="fileName" class="log_select form-control" data-toggle="tooltip"
					data-placement="right" title="Select File Name"
					onchange="com.impetus.ankush.logs.fileNameChange();"><option></option></select>
			</div>
		</div>
		<div class="row">
			<div class="col-md-1">
				<label class="pull-right form-label"></label>
			</div>
			<div class="col-md-11 text-left">
				<div class="text-left mrgt10">
					<button class="btn btn-default mrgr10" id="viewLogs"
						onclick="com.impetus.ankush.logs.logDisplay(<c:out value='${clusterId}'/>)">View</button>
						<button class="btn btn-default" id="downloadLogs"
						onclick="com.impetus.ankush.logs.download(<c:out value='${clusterId}'/>)">Download</button>
				</div>
				
			</div>
		</div>
		<br>
		<div id="div_Logs" style="display:none;" class="container-fluid">
			<div class="row">
				<div class="col-md-3 text-left">
					<label class="form-label section-heading mrgl30">Logs</label>
				</div>
			</div>
			<div class="row">
				<div class="data text-left mrgl10 font14">
					<pre id="logView">
					</pre>
				</div>
			</div>
		</div>
	</div>
</div>
</div>

<div class="modal-backdrop loadingPage element-hide" id="showLoading"
		style=""></div>

</body>
</html>
