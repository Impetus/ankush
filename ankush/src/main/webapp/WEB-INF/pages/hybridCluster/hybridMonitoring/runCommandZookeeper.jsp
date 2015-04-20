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
<%@ include file="../../layout/header.jsp"%>
<%@ include file="../../layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/zookeeper/zookeeperMonitoring.js"
	type="text/javascript"></script>
<script>
	$(document).ready(function() {
		com.impetus.ankush.zookeeperMonitoring.populateCommand();
		com.impetus.ankush.zookeeperMonitoring.populateIp();
	});
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">

			<h1 class="left">Run Command</h1>
			<button class="btn btn-default pull-right"
				data-loading-text="Running..."
				onclick="com.impetus.ankush.zookeeperMonitoring.runCommand();">Run</button>

		</div>
		<div class="page-body" id="main-content">
			<%@ include file="../../layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
				<div class="row">
					<div id="error-div" class="col-md-12 error-div-hadoop"
						style="display: none;">
						<span id="popover-content"></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right">
						<label class="form-label">Select Command :</label>
					</div>
					<div class="col-md-10 col-lg-2 form-group">
						<select id="zookeeperCommandDropDown"
							title="Select Zookeeper Command" data-placement="right"
							class="form-control">
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2 text-right ">
						<label class="form-label">Select Host :</label>
					</div>
					<div class="col-md-10 col-lg-2 form-group">
						<select id="zookeeperIpDropDown" title="Select Zookeeper IP"
							data-placement="right" class="form-control">
						</select>
					</div>
				</div>
				<div class="row element-hide" id="zookeeperCommandOutputHead">
					<div class="col-md-2 text-left">
						<h4 class="section-heading" style="text-align: left;">Output:</h4>
					</div>
				</div>
				<div class="row">
					<pre id="zookeeperCommandOutput" class="element-hide"></pre>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
