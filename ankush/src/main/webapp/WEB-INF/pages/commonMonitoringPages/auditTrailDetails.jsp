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

<script>
function autoRefreshAuditTrailDetailPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = ''; 
	obj1.callFunction = "";
	obj1.time = 0;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
	$(document).ready(function() {
		autoRefreshAuditTrailDetailPage();
	});
</script>
<!-- This page will show audit trail child page for audit trail details  -->
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Audit Details</h2>
		</div>
	</div>
	
</div>
<div class="section-body">
	<div class="container-fluid mrgnlft8 ">
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">File :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="commonSource"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Type :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="commonType"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right" >Property Name :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="commonPropertyName"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Property Value :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="commonPropertyValue"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">User :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="commonUserName"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Host :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="commonHost"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 text-right">
				<label class="form-label text-right">Time :</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="commonDate"></label>
			</div>
		</div>
	</div>
</div>
