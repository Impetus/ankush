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
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
   <head>
    <meta charset="utf-8">
   <title>Ankush:Login</title>
   <%@ include file="./layout/header.jsp"%> 
        <!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /><![endif]-->
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">
		<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
	<style>
		.popoverDashboard {
				    border : 1px solid rgba(0, 0, 0, 0.2) !important;
				    top: -25px !important;
		}
		.popover-content{
			font-weight: normal;
		}
		.popover.right .arrow:after{ 
			bottom: -50px  !important;
		}
	</style>
	<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.validate.js" type="text/javascript"></script>
	<script src="<c:out value='${baseUrl}' />/public/js/sliding.form.js" type="text/javascript"></script>
	<script type="text/javascript">
	less = {
			env: "development", // or "production"
			async: false,       // load imports async
			fileAsync: false,   // load imports async when in a page under 
			                    // a file protocol
			poll: 1000,         // when in watch mode, time in ms between polls
			functions: {},      // user functions, keyed by name
			dumpLineNumbers: "comments", // or "mediaQuery" or "all"
			relativeUrls: false,// whether to adjust url's to be relative
			                    // if false, url's are already relative to the
			                    // entry less file
			rootpath: "./"// a path to add on to the start of every url 
			                    //resource
		    };
		    function hello() {
 $("#login-box").hide("slow", function(){
    /* Replace First Div */
    $(this).replaceWith($('#forgot-username-box'));
  });		    
		    }
	function test() {
		$('#content-panel').sectionSlider({
	       current : 'login-content',
	       url : baseUrl + '/dashboardnew/home',
	       method : 'get',
	       title : 'Cluster Overview',
	       params : {}
     	});
		 tooltipInitialize();
 	return false;
	}
	 function test1() {
	   $('#content-panel').sectionSlider('addParentPanel', {
       current : 'login-content',
       url : 'responsive-parent.html',
       params : {}
       });
	   $('#j_username').focus();
	    		return false;
	    }
	 function tooltipInitialize(){
		 $('#dashboardIcon').tooltip({
			 'min-width' : '100px;'
		 });
		 $('#userProfile').tooltip({
			 'min-width' : '100px;'
		 });
		 $('#soIcon').tooltip({
			 'width' : '100px !important'
		 });
		 $('#configurationSelected').tooltip({
			 'min-width' : '100px;'
		 });
		
	 }
	 function loadDashboard(elem){
		 com.impetus.ankush.dashboard.removeChildDialog();
		 //if($(elem).hasClass('selected')) return false;
		 com.impetus.ankush.dashboard.clearAllAutorefreshInterval();
		 if($.data(document, "panels").children.length == 0){
			 var parent = document.getElementsByClassName('section-parent');
			 if(parent.length > 0){
				  $('#content-panel').sectionSlider('removeParentPanel',{
					 title : "Cluster Overview",
					 callback : function() {
					}
				  });
				     var key = 0;
					 for(var i = (Object.keys(autoRefreshCallsObject).length - 1) ; i >= key ; i--){
							for(var j = 0 ; j < autoRefreshCallsObject[i].length ; j++){
								if(i != 0)
									autoRefreshCallsObject[i][j].varName = window.clearInterval(autoRefreshCallsObject[i][j].varName);
								else
									autoRefreshCallsObject[i][j].varName = setInterval(autoRefreshCallsObject[i][j].callFunction,autoRefreshCallsObject[i][j].time);
							}
							if(i != 0)
								delete autoRefreshCallsObject[i];
				 }
			 }
		}
		 else
		  com.impetus.ankush.removeChild(1);
	 }

	 function loadDefault(elem){
		 var key = 0;
		 for(var i = (Object.keys(autoRefreshCallsObject).length - 1) ; i >= key ; i--){
				for(var j = 0 ; j < autoRefreshCallsObject[i].length ; j++){
						autoRefreshCallsObject[i][j].varName = window.clearInterval(autoRefreshCallsObject[i][j].varName);
				}
				if(i != 0)
					delete autoRefreshCallsObject[i];
		}
		/* Following code will stop all the autorefresh for the sparkline and heat map calls defined in Common*/
		for(var intNo=0; intNo<=intervalId.length; intNo++)
		{
			window.clearInterval(intervalId[intNo]);
		}
		com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
		/* Following code will stop all the autorefresh for the sparkline and heat map calls defined in Common*/
			
		 com.impetus.ankush.dashboard.removeChildDialog();
		 if($(elem).hasClass('selected')) return false;
		 com.impetus.ankush.dashboard.clearAllAutorefreshInterval();
		  
		  $('#content-panel').sectionSlider('addParentPanel', {
  		    url : baseUrl + '/dashboardnew/default',
		        method : 'get',
		        title : 'System Overview',
     			params : {
     			},
     			callback : function() {
     				com.impetus.ankush.dashboard.systemOverview();
  				},
  		});

	 }
	 function loadConfiguration(elem) {
		 var key = 0;
		 for(var i = (Object.keys(autoRefreshCallsObject).length - 1) ; i >= key ; i--){
				for(var j = 0 ; j < autoRefreshCallsObject[i].length ; j++){
						autoRefreshCallsObject[i][j].varName = window.clearInterval(autoRefreshCallsObject[i][j].varName);
				}
				if(i != 0)
					delete autoRefreshCallsObject[i];
		}
		 /* Following code will stop all the autorefresh for the sparkline and heat map calls defined in Common*/
			for(var intNo=0; intNo<=intervalId.length; intNo++)
			{
				window.clearInterval(intervalId[intNo]);
			}
			/* Following code will stop all the autorefresh for the sparkline and heat map calls defined in Common*/
		 	com.impetus.ankush.dashboard.removeChildDialog();
	    	if($(elem).hasClass('selected')) return false;
	    	com.impetus.ankush.dashboard.clearAllAutorefreshInterval();
	       $('#content-panel').sectionSlider('addParentPanel', {
    		    current : 'login-panel',
		        url : baseUrl + '/auth/config',
		        title : 'Configuration',
		        method : 'get',
       			params : {}
     		});
	 }
	 function goToLogin(){
		$('#forgotFieldSet').hide();
		$('#j_username').focus();
	 	$('a', $('#navigation ul li.selected').prev()).click();
	 	$('#get_passwd').attr("checked",false);
	 	$('#get_user_id').attr("checked",false);
	 	$('#userIdDiv').hide();
	 	$('#passwordDiv').hide();
	 	$('#email-error').empty();
	 	$('#UserId-error').empty();
	 	$("#continueButton").attr("disabled",true);
	 	$("#continueButton").css({"background-color" :"#79A9F6"});
	 	
	 }
	 function goToForgotPassword(){
		 $('#forgotFieldSet').show();
	 	$('a', $('#navigation ul li.selected').next()).click();
	 	$('#get_passwd').attr("checked",false);
	 	$('#get_user_id').attr("checked",false);
	 	$("#continueButton").attr("disabled",true);
	 	$("#continueButton").css({"background-color" :"#79A9F6"});
	 }
    function loadProfilePopOver(elem){
    	com.impetus.ankush.dashboard.removeChildDialog();
    	$('#userProfile').clickover({
			content: function() {	console.log('inside userprofile popover mehod');return $('#popover_content_wrapper').html();},
			template: '<div class="popover popoverDashboard"><div class="arrow"></div><div class="popover-inner"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div></div>'
		});
	 }
	 function loadUpdatePassword(){
		 //var docum = document.getElementById('sectionslider');
		 	//docum.click();
			 $('.popoverDashboard').hide();
		    com.impetus.ankush.dashboard.clearAllAutorefreshInterval();
	    	$('#content-panel').sectionSlider('addParentPanel', {
    		    current : 'login-panel',
    		    title : 'Update Password',
		        url : baseUrl + '/dashboardnew/getUpdatePassword',
		        method : 'get',
       			params : {},
       			callback : function(){
       				$('#currentPassword').focus();
       			}
       		});
	    	com.impetus.ankush.dashboard.makeBarSelected('userProfile');
	 }
	 function showUserIdDiv(elem){
		 if(elem.checked){
			 $('#userIdTextBox').val('');
			 $("#continueButton").attr("disabled",false);
			 $("#continueButton").css({"background-color" :"#498BF3"});
			 $('#userIdDiv').show();
			 $('#passwordDiv').hide();
			 $('#email-error').empty();
			 
		 }
	 }
	 function showPasswordDiv(elem){
		 if(elem.checked){
			 $('#emailTextBox').val('');
			 $("#continueButton").attr("disabled",false);
			 $("#continueButton").css({"background-color" :"#498BF3"});
			 $('#userIdDiv').hide();
			 $('#passwordDiv').show();
			 $('#UserId-error').empty();
		 }
	}
	function termsOfUse(){
		 $('#termsOfUse').modal('show');
	} 
	$(document).ready(function(){
		$('#signInButton').attr('disabled',false);
	});
    </script>
 	</head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->

    <!-- Add your site or application content here -->
    <div id="login-content" class="row-fluid">
	<div class="container" id="login-panel">
		<div id="login-box" class="box1" style="display: none;">
			<form id="login-form" name="login-form" style="display: inline;" onsubmit="return com.impetus.ankush.login.processLogin(this);">
				<fieldset class="step">
				<h1 class="heading">Ankush Cluster Manager</h1>
				<div class="line-height">
					<div class="text-label left">User ID:</div>
					<div class="input-box left"><input type="text" name="username" id="j_username" /></div>
					<div style="padding-left:165px;text-align:left;line-height:normal;" class="clear">
					</div>
					<div class="clear"></div>
				</div>
				<div class="line-height">
					<div class="text-label left">Password:</div>
					<div class="input-box left"><input type="password" name="password" id="j_password" /></div>
					<div style="padding-left:165px;text-align:left;line-height:normal;width:200px;" class="clear" id="login-error">
					</div>
					<div class="clear"></div>
				</div>
				<div class="line-height">
					<div class="text-label left">&nbsp;</div>
					<div  class="input-box-right left">
						<button id="signInButton" class="enable-btn-login" data-loading-text="Signing In..." form="login-form" value="Submit" type="submit">Sign In</button>
					</div>
					<div class="clear"></div>
				</div>
				<div class="button-box">
					<a href="##" onclick="goToForgotPassword();">Forgot ID or Password?</a>
				</div>
				</fieldset>
			</form>
			
				<fieldset id="forgotFieldSet" class="step" style="display:none;">
				<h1 class="heading">Ankush Cluster Manager</h1>
				<div class="line-height">
					<div class="text-label1 left"><input type="radio"  style="margin-right: 5px;" id="get_passwd" onclick="showUserIdDiv(this)" name="get_passwd"/>I don't know my password.</div>
					
					<div class="clear"></div>
				</div>
				<div class="line-height" id="userIdDiv" style="display:none;">
						<div class="text-label1 left">Enter your user ID and click Continue:</div>
						<div class="clear"></div>	
						<div class="text-label1 left"><input type="text" id="userIdTextBox" placeholder="user ID"/></div>
						<div style="padding-left:40px;text-align:left;line-height:normal;" class="clear" id="UserId-error"></div>
						<div class="text-label1 left">Check your email for password reset instructions.</div>
						<div class="clear"></div>
				</div>	
				<div class="line-height">
					<div class="text-label1 left"><input type="radio" style="margin-right: 5px;" id="get_user_id" onclick="showPasswordDiv(this)" name="get_passwd"/>I don't know my User ID.</div>
					<!-- <div class="input-box left"></div> -->
					<div class="clear"></div>
				</div>
				<div class="line-height" id="passwordDiv" style="display:none;">
						<div class="text-label1 left">Enter your email address and click Continue:</div>
						<div class="clear"></div>	
						<div class="text-label1 left"><input type="text" id="emailTextBox" placeholder="email Address"/></div>
						<div style="padding-left:40px;text-align:left;line-height:normal;" class="clear" id="email-error"></div>
						<div class="text-label1 left">Check your email for your user ID.</div>
						<div class="clear"></div>
				</div>	
				<!-- <div>
					<div style="text-align:left;padding-left:40px;color:#B9B9B9;display:none;" id="get_passwd_success">Check your email for further instructions.</div>
				</div> -->
				<div class="line-height">
					<div class="text-label left">&nbsp;</div>
					<div  class="input-box-right left">
						<button class="btn" onclick="goToLogin();">Cancel</button>
						<button class="enable-btn" id="continueButton" value="Go" style="margin-left:8px;background-color:#79a9f6;" disabled="disabled" onclick="com.impetus.ankush.login.getPassword()">Continue</button>
					</div>
					<div class="clear"></div>
				</div>
			</fieldset>
			
			
		</div>
	</div>
                <div id="navigation" style="display:none;">
                    <ul>
                        <li class="selected">
                            <a href="#">Forget Password</a>
                        </li>
                        <li>
                            <a href="#">Login</a>
                        </li>
                    </ul>
                </div>
                <div id="popover_content_wrapper"  style="display: none;">
                	<div style="width:160px;margin-bottom:-10px;height:70px;">
                		<div id="userId" style="font-family:'Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif;"></div>
                		<div style="margin-top:10px;"><a href="#" style="font-family:'Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif;" onclick="loadUpdatePassword();">Change Password...</a></div>
                		<div style="margin-top:4px;"><a href="#" style="font-family:'Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif;" onclick="com.impetus.ankush.login.signOut();">Sign Out</a></div>
                	</div>	
                	<!-- <ul style="display: block;width:150px;height:50px; ">
                		<li href="#" style="width:150px;">Change Password</li>
                		<li>Sign Out</li>
                	</ul> -->
                </div>
	
		<div class="clear;"></div>
		<div  class="footer_div">
			<div id="footerCanvas" style="display: none; text-align:center;">Copyright © 2013 Impetus Technologies. All rights reserved. <span><a href="#" onclick="termsOfUse()">Terms of Use</a></span></div>
			<div id="version" style="display: none; text-align: center;">Version : <%@include file="../../version.txt" %></div>
		</div>
	</div>
	

<div id="content-panel" style="display:none;">
   <div id="sectionslider">
    <div class="left-bar">
      <div class="content-body" style="position:relative;height:100%;">
        <div style="position:absolute;top:10px;">
          <ul class="left-nav">
            <li class="cluster" id="soIcon" title="System&nbsp;Overview" data-placement="right" data-toggle="tooltip" onclick="loadDefault(this);"></li>
            <li class="dashboard selected" id="dashboardIcon" title="Dashboard" data-placement="right" data-toggle="tooltip" onclick="loadDashboard(this);"></li>
          </ul>
        </div>
        <div style="position:absolute;bottom:0px;">
          <ul class="left-nav">
            <li class="settings"  id="configurationSelected" title="Configuration" data-toggle="tooltip" data-placement="right" onclick="loadConfiguration(this);"></li>
            <li class="user" id="userProfile" title="User&nbsp;Profile" data-placement="right" data-toggle="tooltip" onclick="loadProfilePopOver(this);" style="padding-bottom: 10px;"></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="section"></div>
   </div>
 </div>

<div class="modal-backdrop loadingCurtain" id="showLoading" style="display:none;"></div>
<%@ include file="termsAndPolicy.jsp"%>
    </body>
</html>
