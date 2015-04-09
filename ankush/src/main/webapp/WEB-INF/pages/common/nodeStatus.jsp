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

<head>
<%@ include file="../layout/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all" />
<%@ include file="../layout/navigation.jsp"%>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 class="left">Deployment Logs</h1>
				<div class="col-md-8 pull-right mrgt5">
				<h4>
					<span class="label label-default" id="deploymentStatusLabel"></span>
				</h4>
			</div>
		</div>
		<div class="page-body" id="main-content">
		<%@ include file="../layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-10" id='deploymentProgressDiv'></div>
				</div>

			</div>
		</div>
	</div>
	<script>
		var clusterId = '<c:out value="${clusterId}"/>';
		var clusterTechnology = '<c:out value="${clusterTechnology}"/>';
		$(document).ready(
				function() {
					com.impetus.ankush.common.deploymentProgress(clusterId,
							clusterTechnology);
				});
	</script>
</body>
