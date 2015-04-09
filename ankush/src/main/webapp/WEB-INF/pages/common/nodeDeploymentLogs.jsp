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

<!-- Page for node level deployment logs -->
<head>
<%@ include file="../layout/header.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<c:out value="${baseUrl}" />/public/css3.0/main.css" media="all" />
<%@ include file="../layout/navigation.jsp"%>
</head>
<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 id="nodeDetailHead1"></h1>
		</div>
		<div class="page-body" id="main-content">
		<%@ include file="../layout/breadcrumbs.jsp"%>
			<div class="container-fluid  mrgnlft8">
				<div class="row">
					<div class="col-md-4 ">
						<h4 class="section-heading" style="color: black;">Node
							Details</h4>
					</div>
				</div>
				<div id="nodeDeploymentField"></div>
				<div class="row" id="nodeErrorDiv" style="display: none;">
					<div class="col-md-2 text-right">
						<label class="form-label">Error:</label>
					</div>
					<div class="col-md-10" id="errorOnNodeDiv"></div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<h4 class="section-heading"
							style="color: black; text-align: left;">Node Deployment</h4>
					</div>
					<div class="row">
						<div id="nodeDeployProgress" class="col-md-10 mrgl10"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			var clusterId = '<c:out value="${clusterId}"/>';
			var hostName = '<c:out value="${hostName}"/>';
			com.impetus.ankush.common.nodeLogs(clusterId,hostName);
			
		});
	</script>
</body>
