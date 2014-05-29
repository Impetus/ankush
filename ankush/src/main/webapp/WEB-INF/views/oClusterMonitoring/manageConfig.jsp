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
<!-- Page for cluster configurations -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	$(document).ready(function() {

	});
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span12">
			<h2 id="manageConfigPage" class="heading">Configuration</h2>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span6 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadStorePage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="##">Store</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
			
		</div>
		<br>
		<div class="row-fluid">
			<div class="span6 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadAdminParamPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="##">Admin Parameters</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span6 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadPolicyParamPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="##">Policy&nbsp;Parameters</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span6 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadRepNodeParamPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="##">Replication&nbsp;Node&nbsp;Parameters</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
		<br>
		<div class="row-fluid">
			<div class="span6 "
				onclick="com.impetus.ankush.oClusterMonitoring.loadAlertPage();">
				<div style="float: left;">
					<h4 class="section-heading" style="color: black; text-align: left;">
						<a style="color: black;" href="##">Alerts</a>
					</h4>
				</div>
				<div style="float: left;">
					<a style="line-height: 40px; margin-left: 20px;" href="##"><img
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a>
				</div>
			</div>
		</div>
	</div>
</div>
