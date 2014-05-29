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
<!-- Node Details Page during Cluster Creation  -->
<script>
var ipNodeInspect = '';
$(document).ready(function(){
	setTimeout(function(){
	ipNodeInspect = $('#nodeIp-Hadoop').text();
},0);
});
</script>
<div class="section-header ">
	<div  class="row-fluid"   style="margin-top:20px;">
	  	<div class="span4"><h2 class="heading text-left"><span id="nodeIp-Hadoop" ></span></h2></div>
	</div>
</div>
<div class="section-body content-body">
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span3" ><label class="form-label section-heading">Node Details</label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Status:</label></div>
		<div class="span10"><label class="text-data" id="nodeStatus-Hadoop"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Type:</label></div>
		<div class="span10"><label class="text-data" id="nodeType-Hadoop"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Reachable:</label></div>
		<div class="span10"><label class="text-data" id="nodeReachable-Hadoop"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Datacenter:</label></div>
		<div class="span10"><label class="text-data" id="nodeDc-Hadoop"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Rack:</label></div>
		<div class="span10"><label class="text-data" id="nodeRack-Hadoop"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">OS:</label></div>
		<div class="span10"><label class="text-data" id="OSName-Hadoop"></label></div>
	</div>

	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Authenticated:</label></div>
		<div class="span10"><label class="text-data" id="nodeAuthentication-Hadoop"></label></div>
	</div>
	<%@ include file="../common/validationTable.jsp"%>
	</div>
	 
</div>	
