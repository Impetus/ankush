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
<!-- Page for node detail in case of error -->
	<div class="section-header">
		<div class="row-fluid">
			<div class="span11">
				<h3>
					<a
						href="##"
						id="nodeDetailHead"></a>
				</h3>
			</div>
		</div>
	</div>
	<div class="section-body">
	<br>
		<div class="row-fluid">
			<div class="span11">
				<h4 class="section-heading" >Node Details</h4>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label" id="">Status:</label>
			</div>
			<div class="span10">
				<label class="text-left form-label" style="color: black;" id="nodeStatus"></label>
			</div>
		</div>
		<!-- <br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Admin:</label>
			</div>
			<div class="span10">
				<label class="text-left " style="color: black;" id="adminStatus"></label>
			</div>
		</div> -->
		
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Capacity:</label>
			</div>
			<div class="span10">
				<label class="text-left form-label" style="color: black;" id="nodeCapacity"></label>
			</div>
		</div>
	
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">CPUs:</label>
			</div>
			<div class=" span10">
				<label class="text-left form-label" style="color: black;" id="nodeCpu"></label>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Memory:</label>
			</div>
			<div class="span10">
				<label class="text-left form-label" style="color: black;" id="nodeMemory"></label>
			</div>
		</div>
	
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Registry Port:</label>
			</div>
			<div class="span10">
				<label class="text-left form-label" style="color: black;" id="nodeRegPort"></label>
			</div>
		</div>
	
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">HA Port Range:</label>
			</div>
			<div class="span1">
				<label class="text-left form-label" style="color: black;" id="nodeHA1"></label>
			</div>
			<div class="span1">
				<label class="text-left form-label" style="color: black;" id="nodeHA2"></label>
			</div>
		</div>
		
		<div class="row-fluid" id="errorDiv" style="display: none;">
			<div class="span2 " >
				<label class="text-right form-label">Node Error:</label>
			</div>
			<div id="nodeError" class="span10" style="padding-top: 15px;" >
			</div>
			
		</div>
		<div class="row-fluid">
			<div class="span2 " >
				<label class="text-right form-label">Node Logs:</label>
			</div>
			<div id="nodeDetailLogs" class="span10" style="margin-top: 15px;">
			</div>
			
		</div>
	</div>
