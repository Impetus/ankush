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
<!-- Page for cluster logs(showing & downloading logs). -->
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span7">
			<h2 class="heading  text-left left">Logs</h2>
			
			<button class="btn-error" id="logErrorButton" 
				style="display:none;height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"
				onclick="com.impetus.ankush.oClusterMonitoring.focusError();">
			</button>
		</div>
		
		<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="logErrorButton" 
				style="display:none;height: 29px; color: white; border: none; background-color: #EF3024 !important;"
				onclick="com.impetus.ankush.oClusterMonitoring.focusError();">
			</button>
		</div>
		
		-->
	</div>
</div>

<div class="section-body">
	<div class="container-fluid  mrgnlft8">
		<div id="logError" class="commonErrorDiv" style="display: none;"></div>
		<div id="fileSizeExceed" class="modal hide fade"
			style="display: none;">
			<div class="modal-header text-center">
				<h4>Download Failed</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid">
					<div class="span12" style="text-align: left; font-size: 14px;">
						File cannot be downloaded as its size exceeds 5MB</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" id="divOkbtn_NodeDD" class="btn">OK</a>
			</div>
		</div>
		<div>
			<div class="row-fluid">
				
					<div class="span2 text-right">
						<label class="form-label">Type:</label>
					</div>
					<div class="span10 text-left">
						<select id="logFromType"
							onchange="com.impetus.ankush.oClusterMonitoring.onChangeNodePopulation();"></select>
					</div>
				
			</div>
			<div class="row-fluid">
				
					<div class="span2 text-right">
						<label class="form-label">Node:</label>
					</div>
					<div class="span10 text-left">
						<select id="logFromNode"
							onchange="com.impetus.ankush.oClusterMonitoring.onchangePopulateFiles();"></select>
					</div>
			</div>
			<div class="row-fluid">
					<div class="span2 text-right">
						<label class="form-label">File:</label>
					</div>
					<div class="span10 text-left">
						<select id="logFromFile"></select>
					</div>
			</div>
			<div class="row-fluid">
					<div class="span2 text-right">
						<label class="form-label"></label>
					</div>
				<div class="span10">
						<button class="btn"
							onclick="com.impetus.ankush.oClusterMonitoring.logDisplay();">View</button><button class="btn mrgl20"
							onclick="com.impetus.ankush.oClusterMonitoring.download();">Download</button>
					
				</div>
			</div>
			<div class="row-fluid">
				<div class="span10 offset1" id="logsOnDiv"
					style="font-size: 14px; padding-top: 20px;">
					<pre id="logsOnPre" style="display: none; background-color: white;border: none;"></pre>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		
	});

	/* $(document).on("click", "a.fileDownloadSimpleRichExperience", function () {
	 $.fileDownload($(this).attr('href'), {
	 preparingMessageHtml: "We are preparing your report, please wait...",
	 failMessageHtml: "There was a problem generating your report, please try again."
	 });
	 return false; //this is critical to stop the click event which will trigger a normal file download!
	 }); */
</script>
