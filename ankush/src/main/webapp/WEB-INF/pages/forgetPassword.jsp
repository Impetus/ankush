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
<script>
	function showUserIdDiv(elem) {
		if (elem.checked) {
			$('#userIdTextBox').val('');
			$("#continueButton").attr("disabled", false);
			$('#userIdDiv').show();
			$('#passwordDiv').hide();
			$('#email-error').empty();

		}
	}
	function showPasswordDiv(elem) {
		if (elem.checked) {
			$('#emailTextBox').val('');
			$("#continueButton").attr("disabled", false);
			$('#userIdDiv').hide();
			$('#passwordDiv').show();
			$('#UserId-error').empty();
		}
	}
</script>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1>Forgot Password</h1>
		</div>
		<div class="page-body" id="main-content">
			<div class="row">
				<div class="col-md-10 col-lg-2 form-group text-left">
					<input type="radio" style="margin-right: 5px;" id="get_passwd"
						onclick="showUserIdDiv(this)" name="get_passwd" />I don't know my
					password.
				</div>
			</div>
			<div id="userIdDiv" class="element-hide">
				<div class="row">
					<div class="col-md-10 col-lg-2 form-group text-left">Enter
						your user ID and click Continue:</div>
				</div>
				<div class="row">
					<div class="col-md-10 col-lg-2 text-left form-group">
						<input type="text" id="userIdTextBox" class="input-large form-control" placeholder="user ID" />
						<div  id="UserId-error"></div>
					</div>
					
				</div>
				<div class="row">
					<div class="col-md-10 col-lg-3 form-group text-left">Check
						your email for password reset instructions. <a href="<c:out value="${baseUrl}" />/auth/login">Click to Login</a></div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-10 col-lg-2 form-group text-left">
					<input type="radio" style="margin-right: 5px;" id="get_user_id"
						onclick="showPasswordDiv(this)" name="get_passwd" />I don't know
					my User ID.
				</div>
			</div>
			<div id="passwordDiv" class="element-hide">
				<div class="row">
					<div class="col-md-10 col-lg-2 form-group text-left">Enter
						your email address and click Continue:</div>
				</div>
				<div class="row">
					<div class="col-md-10 col-lg-2 text-left form-group">
						<input type="text" id="emailTextBox" class="input-large form-control" placeholder="Email Address" />
						<div  id="email-error"></div>
						
					</div>
				</div>
				<div class="row">
					<div class="col-md-10 col-lg-3 form-group text-left">Check
						your email for your user ID. <a href="<c:out value="${baseUrl}" />/auth/login">Click to Login</a></div>
				</div>
			</div>
			<div class="row">
				<a href="<c:out value="${baseUrl}" />/auth/login" class="btn btn-default mrgl10">Cancel</a>
				<button class="btn btn-primary mrgl10" id="continueButton" value="Go" disabled="disabled"
					onclick="com.impetus.ankush.login.getPassword()">Continue</button>
			</div>



</div>
</div>


			<script>
				$(document).ready(function() {

				});
			</script>
</body>
</html>
