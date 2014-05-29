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
<!-- Page for storage node child node parameters -->
<%@ include file="../layout/blankheader.jsp" %>

<script>
	var storageNodeId = '<c:out value="${storageNodeId}"/>';
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span10"><h2 class="heading text-left">Parameters</h2></div>
		<div class="span2 text-right"><button class="btn" onclick="com.impetus.ankush.oClusterMonitoring.storageNodeChildParamCancel(storageNodeId)">Cancel</button>
		<button class="btn mrgl10" onclick="com.impetus.ankush.oClusterMonitoring.saveStorageNodeChildParam(storageNodeId)">Save</button></div>
	</div>
</div>

<div class="section-body">
<div class="container-fluid"  id="storageNodeChildParamBody"></div>

</div>
