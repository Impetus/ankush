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
<!-- Page for showing verify function logs -->
<%@ include file="../layout/blankheader.jsp"%>

<script>

$(document).ready(function() {

	
});
</script>

<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span6">
			<h2 class="heading left">Verify</h2>
			<button class="btn-error" id="verifyErrorButton"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding: 0px 15px; left: 15px; position: relative;" onclick="com.impetus.ankush.oClusterMonitoring.focusError();"></button>
		</div>
		
		<!--  
		<div class="span3">
				<button class="span3 btn-error" id="verifyErrorButton"
					style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;" onclick="com.impetus.ankush.oClusterMonitoring.focusError();"></button>
			</div>
			
			-->
		<!-- <div class="span2 text-right" style="margin-top: 18px;">
				<button class="btn" style="margin-right: 100px;"
					onclick="com.impetus.ankush.oClusterSetup.stopVerify();">Stop</button>
			</div> -->
	</div>
</div>
<div class="section-body">
<div class="container-fluid">
<div id="verifyLogError" class="commonErrorDiv" style="display: none;"></div>
	<div class="row-fluid">
		<div class="span11 mrgt20">
		<pre id='verifyProgressLog' style="border: none;background-color: white;font-family: 'Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif;font-size: 14px;">

		</pre>
		
		</div>
	</div>
	</div>
</div>
