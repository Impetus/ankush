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
<!-- Page for retrieved node details -->
<%@ include file="../../layout/blankheader.jsp"%>
<script>
var ipNodeInspect = '';
$(document).ready(function(){
	setTimeout(function(){
	ipNodeInspect = $('#kafkaNodeHead').text();	
},0);
});
</script>
	<div class="section-header">
		<div class="row-fluid">
			<div class="span10">
				<h3>
					<a
						id="kafkaNodeHead"></a>
				</h3>
			</div>
		</div>
	</div>
	<div class="section-body">
	<div class="container-fluid">
	<br>
		<div class="row-fluid">
			<div class="span10 offset1">
				<h4 class="section-heading" >Node Details</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right" id="">Status:</label>
			</div>
			<div class="span10">
				<label class="text-left lbl-black"  id="kafkaNodeStatus"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right" id="">Zookeeper:</label>
			</div>
			<div class="span10">
				<label class="text-left lbl-black"  id="zookeeper"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">OS:</label>
			</div>
			<div class="span10">
				<label class="text-left lbl-black"  id="kafkaOs"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">CPU:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="kafkaCpu"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Disk Count:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;"
					id="kafkaDiskCount"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">Disk Size:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;"
					id="kafkaDiskSize"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right">RAM:</label>
			</div>
			<div class="span10">
				<label class="text-left" style="color: black;" id="kafkaRam"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid" id="isAuth">
			<div class="span2 ">
				<label class="text-right">Authenticated:</label>
			</div>
			<div class="span10">
				<label class="text-left lbl-black" id="isAuthenticated"></label>
			</div>
		</div>
		<%@ include file="../../common/validationTable.jsp"%>
	</div>
	</div>
