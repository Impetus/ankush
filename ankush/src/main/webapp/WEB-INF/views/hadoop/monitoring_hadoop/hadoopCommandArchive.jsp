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
<!-- Hadoop Archive Command Page  -->

<%@ include file="../../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js/ankush.validation.js"
	type="text/javascript"></script>
<script>
	$(document).ready(function() {
		$('#command_archive').tooltip();
		$('#command_archive_parentPath').tooltip();
		$('#command_archive_src').tooltip();
		$('#command_archive_dest').tooltip();
	});
</script>
<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h2 class="heading text-left left">Archive</h2>
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
				onclick="com.impetus.ankush.removeChild(3)">Cancel</button>
			<button class="btn" style="margin-right: 50px;"
				onclick="com.impetus.ankush.hadoopMonitoring.commandsData('archive')">Run</button>
		</div>
	</div>
</div>
<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">

		<div class="row-fluid">
			<div id="error-div-hadoop" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content" style="color: red;"></span>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Archive:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large" id="command_archive"
					placeholder="Archive" data-toggle="tooltip" data-placement="right"
					title=""></input>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Parent Path:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large" id="command_archive_parentPath"
					placeholder="Parent Path" data-toggle="tooltip" data-placement="right"
					title=""></input>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Source Directory:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large" id="command_archive_src"
					placeholder="Source" data-toggle="tooltip" data-placement="right"
					title=""></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Destination Directory:</label>
			</div>
			<div class="span9 text-left">
				<input type="text" class="input-large" id="command_archive_dest"
					placeholder="Destination" data-toggle="tooltip"
					data-placement="right" title=""></input>
			</div>
		</div>
	</div>
</div>
