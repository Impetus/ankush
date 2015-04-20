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
<%@ include file="layout/header.jsp"%>
<%@ include file="layout/navigation.jsp"%>
<script
	src="<c:out value='${baseUrl}' />/public/js3.0/ankush.password.js"
	type="text/javascript"></script>
<script>
	$(document)
			.ready(
					function() {
						$('#existingPassword').focus();
						if (decisionVar === "enforce-password") {
							$(".menuIcon").click(function(e) {
								e.preventDefault();
							});
							$("#breadcrumb-ankush a").attr("href", "#");
							var menuShow = $('#menu-show').unbind('click');
							menuShow.css('cursor', 'default');
							$('head')
									.append(
											'<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/menu-disable.css" media="all"/>');
						}
					});
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 class="left">Change Password</h1>
			<button id="changePasswordButton"
				onclick="com.impetus.ankush.password.changePassword();"
				class="btn btn-default pull-right mrgt5" form="changePasswordForm"
				type="submit" data-loading-text="Applying..." class="btn">Apply</button>
		</div>
		<div class="page-body" id="main-content">
			<%@ include file="layout/breadcrumbs.jsp"%>
			<div class="container-fluid mrgnlft8">
				<div>
					<div id="error-div" style="display: none;">
						<div id="popover-content" class="error-div-hadoop mrgb10"></div>
					</div>

					<div class="row">
						<div class="col-md-1">
							<label class="pull-right form-label">Existing:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group text-left">
							<input data-placement="right" data-toggle="tooltip"
								class="form-control" title="Existing Password"
								id="existingPassword" type="password"
								placeholder="Existing Password" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-1">
							<label class="pull-right form-label">New:</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group text-left">
							<input id="newPassword" type="password" class="form-control"
								placeholder="New Password" data-placement="right"
								title="New Password" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-1">
							<label class="pull-right form-label">&nbsp;</label>
						</div>
						<div class="col-md-10 col-lg-2 form-group text-left">
							<input id="confirmPassword" type="password" class="form-control"
								placeholder="Confirm Password" data-placement="right"
								title="Confirm Password" />
						</div>
					</div>




				</div>
			</div>
		</div>
	</div>
</body>
</html>
