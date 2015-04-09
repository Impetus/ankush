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

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html>
<!--<![endif]-->
<head>
<meta charset="utf-8">
<!-- <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE11"/>  -->
<meta content="/images/ankushFAV_1.ico" itemprop="image"><title>Login</title>
<%@ include file="./layout/header.jsp"%>

<!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /><![endif]-->
<meta name="description" content="">
<meta name="viewport" content="width=device-width">

<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

<script type="text/javascript">
	$(document).ready(function(){
	    $('#termsOfUse').appendTo('body');
	    $.get(baseUrl+'/user/userid', function(data) {
    		if(data.status == '200'){
    			if(data.output.target != null){
        			if(data.output.target == 'configure'){
        				$("#loginbox").hide();
        				$(location).attr('href',(baseUrl + '/dashboard/configuration/C-D/configure'));
        			}
        			else if(data.output.target == 'changepassword'){
        				$("#loginbox").hide();
        				$(location).attr('href',(baseUrl + '/dashboard/changePassword/C-D/enforce-password'));
        			}
        		}
        		else{
        			var url = window.location.href;
        			if(url.indexOf('/auth/login') === -1);
        			else{
        				$("#loginbox").hide();
        				$(location).attr('href',(baseUrl + '/dashboard'));
        			}
        		}
    		}else{
    			$('#j_username').focus();
    		}
    	});
	});
	function termsOfUse() {
		$('#termsOfUse').modal('show');
	}
	
</script>
<style>
.panel-info{
	border: none !important	;
}
.panel-heading{
	background-image: none !important;
	/* border-color : #888 ! important; */
	background: transparent ! important;
}
.container {
    width: 750px !important;
}

body{
/* background-color: #D2D2CE ! important; */
}
</style>
</head>
<body>
<div class="page-wrapper">
	<div class="container">
		<div class="mainbox login-wrap"
			style="margin-top: 20%;" id="loginbox">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title text-center" style="font-size:24px;color:#606060;">Ankush <strong>Cluster Manager</strong></div>
					<div
						style="font-size: 80%; position: relative; top: -10px"></div>
				</div>
<form role="form" class="form-horizontal" id="login-form" onsubmit="return com.impetus.ankush.login.processLogin(this);" name="login-form">
				<div class="panel-body" style="padding-top: 30px">
					
						<div class="form-group">
									<label class="col-sm-3 control-label" for="j_username">User Name</label>
									<div class="col-sm-9">										
										<input type="text" placeholder="username" value="" name="username" class="form-control" id="j_username">									
									</div>
								</div>
						<div class="form-group">
									<label class="col-sm-3 control-label" for="j_password">Password</label>
									<div class="col-sm-9">										
										<input type="password" placeholder="password" name="password" class="form-control" id="j_password">								
									</div>
								</div>		
						<div class="form-group">
									<label class="col-sm-3 control-label"></label>
									<div class="col-sm-9">										
										<div id="login-error" style="color:#a94442" class="text-left"></div>								
									</div>
								</div>	
					



				</div>
				 <div class="panel-footer">
				<button id="signInButton" class="btn btn-primary" data-loading-text="Signing In..." form="login-form" value="Submit" type="submit">Sign In</button>
				<a href="<c:out value="${baseUrl}" />/auth/forget_password" class="pull-right padt5">Forgot password?</a>
				 </div>
				 </form>
			</div>
		</div>

	</div>
	</div>
			<%-- <div class="text-center">
				Copyright © 2014 Impetus Technologies. All rights reserved. <span><a href="#" onclick="termsOfUse()">Terms of Use</a></span>
			</div>
			<div id="version" style="display: none; text-align: center;">
				Version :
				<%@include file="../../version.txt"%></div> --%>
<%@ include file="termsAndPolicy.jsp"%>	
<div class="login-footer">
		
<div id="footer" class="footer" style="display: block;">
	<span>Copyright © 2015 Impetus Technologies. All rights reserved.</span>

	<span><a href="#" onclick="termsOfUse()">Terms of Use</a></span>
	<span id="version" style=" text-align: center;">
				Version :
				<%@include file="../../version.txt"%></span>
	<a href="http://www.impetus.com/" target="_blank" class="impetus-logo">
		<img border="0" src="<c:out value="${baseUrl}"/>/public/images/impetus-logo.png" title="Impetus" alt="alt">
	</a>
</div>
	</div>
</body>
</html>
