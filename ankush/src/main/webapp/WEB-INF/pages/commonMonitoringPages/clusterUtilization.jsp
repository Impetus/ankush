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
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/clusterUtilization.js"
	type="text/javascript"></script>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">	
   <h1 id="pageHeadingUtilization"></h1>
</div>
<div class="page-body" id="main-content">
<%@ include file="../layout/breadcrumbs.jsp"%>
	<div class="container-fluid">
		<div id="utilizationTrendGraphs">
		</div>
	</div>
</div>
</div>
<script>
$(document).ready(function(){
	$("#pageHeadingUtilization").text(clusterName+'/Utilization Metrics')
	var clusterUtilization = {};
	clusterUtilization.method = function(){
		com.impetus.ankush.clusterUtilization.loadClusterLevelGraphs('lasthour',true);
	};
	com.impetus.ankush.createAutorefresh([clusterUtilization]);
	com.impetus.ankush.clusterUtilization.loadClusterLevelGraphs('lasthour');
});
</script>
</body>
</html>				
