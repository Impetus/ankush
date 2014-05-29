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
<!-- Page for add node table node drill down -->
<%@ include file="../layout/blankheader.jsp"%>
<<script type="text/javascript">
<!--

//-->
var ipNodeInspect = '';
$(document).ready(function() {
	setTimeout(function(){
		ipNodeInspect = $('#nodeDetailHeadChild').text();
	},0);
});
</script>
<div class="section-header">
	<div class="row-fluid">
		<div class="span10">
			<h3>
				<a href="##" id="nodeDetailHeadChild"></a>
			</h3>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="container-fluid  mrgnlft8">
		<br>
		<div class="row-fluid">
			<div class="span10">
				<h4 class="section-heading">Node Details</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right" id="">Status:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeStatusChild"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Admin:</label>
			</div>
			<div class="span10">
				<label class="text-left " style="color: black;"
					id="adminStatusChild"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Capacity:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;"
					id="nodeCapacityChild"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">CPUs:</label>
			</div>
			<div class=" span10">
				<label class="text-left" style="color: black;" id="nodeCpuChild"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Memory:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeMemoryChild"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Registry Port:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="nodeRegPortChild"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">HA Port Range:</label>
			</div>
			<div class="span1">
				<label class="text-left" style="color: black;" id="nodeHA1Child"></label>
			</div>
			<div class="span1">
				<label class="text-left" style="color: black;" id="nodeHA2Child"></label>
			</div>
		</div>
			<%@ include file="../common/validationTable.jsp"%>
	</div>
	<br>
</div>
