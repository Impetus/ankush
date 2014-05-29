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
 <%@ include file="layout/blankheader.jsp"%>
 <input id="SystemOverviewJsonVariable" style="display:none"/>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading">System Overview</h2>
		</div>
	</div>

	

</div>
<div class="section-body common-tooltip">
<br style="clear: both;"/>

<div  id="systemOverviewContainer">
<div class="row-fluid"><div id="allTilesSystemOverview" class="masonry"></div></div>
	<!-- <div class="row-fluid transitions-enabled" id="error_tiles"></div>
	<br style="clear: both;"/>
	<div class="row-fluid transitions-enabled" id="running_tiles"></div> -->
</div>
</div>
<script>
 $(document).ready(function(){
	 
	autoRefreshCallSystemOverview = setInterval("com.impetus.ankush.dashboard.systemOverview()",25000);
	
}); 

</script>
