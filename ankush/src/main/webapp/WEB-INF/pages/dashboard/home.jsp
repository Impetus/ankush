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
<html>
<head>
<%@ include file="../layout/header.jsp"%>
<%@ include file="../layout/navigation.jsp"%>
<script src="<c:out value='${baseUrl}' />/public/js3.0/ankush.dashboard.js" type="text/javascript"></script>
<script src="<c:out value='${baseUrl}' />/public/js3.0/ankush.techlist.js" type="text/javascript"></script>
<title></title>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1>Cluster Overview</h1>
</div>
<div class="page-body" id="main-content">
<%@ include file="../layout/breadcrumbs.jsp"%>
	<div class="transitions-enabled" id="allTile">
	</div>	
</div>
<div class="modal fade" id="technologyDialogBox" tabindex="-1"
	role="dialog" aria-labelledby="" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4>Select technology</h4>
			</div>
			<div class="modal-body">
				<div class="row" id="technoloigyList" style="padding-left: 45px;"></div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn btn-default">Cancel</a>
			</div>
		</div>
	</div>
</div>
</div>
<script>
$(document).ready(function(){
	var createTiles = {};
	createTiles.method = function(){
		com.impetus.ankush.dashboard.createTile();
	};
	pageLevelAutorefreshArray.push(createTiles);
	com.impetus.ankush.createAutorefresh(pageLevelAutorefreshArray);
	com.impetus.ankush.dashboard.createTile();
});
</script>
</body>
</html>