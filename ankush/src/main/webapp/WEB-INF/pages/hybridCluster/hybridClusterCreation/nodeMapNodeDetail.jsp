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

<!-- Page for retrieved node detail -->

<div class="" id="main-content">
	<!-- <h3>
		<a id="nodeHead"></a>
	</h3> -->
	<div class="container-fluid  mrgnlft8">
		<br>
		<div class="row">
			<div class="col-md-2 ">
				<label class="text-right" id="">Status:</label>
			</div>
			<div class="col-md-10">
				<label class="text-left" style="color: black;" id="nodeStatus"></label>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2 ">
				<label class="text-right">Type:</label>
			</div>
			<div class="col-md-10">
				<label class="text-left " style="color: black;" id="nodeType"></label>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2 ">
				<label class="text-right">OS:</label>
			</div>
			<div class="col-md-10">
				<label class="text-left" style="color: black;" id="os"></label>
			</div>
		</div>
		<div class="row" id="errorNodeDiv" style="display: none;">
			<div class="col-md-2">
				<label class=" text-right">Error:</label>
			</div>
			<div class="col-md-10" id="nodeDeploymentError"></div>
		</div>
	</div>
</div>
