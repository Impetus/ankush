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
		<script>	
   		var confUrl = '<c:out value='${url}'/>';
   			$(document).ready(function(){
   				if(decisionVar === "configure"){
   					$(".menuIcon").click(function(e) {
   						   e.preventDefault();
   						});
   					$("#breadcrumb-ankush a").attr("href", "#");
   					var menuShow = $('#menu-show').unbind('click');
   					menuShow.css('cursor','default');
   					$('head').append('<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/menu-disable.css" media="all"/>');
   				}
   				com.impetus.ankush.configurationNew.initConfig();
   			});
   			
   		</script>
   		<style>
   		.fontFamilyClass{
				font-family: 'Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif;
		}
		.popoverTechnology{
			border : 1px solid rgba(0, 0, 0, 0.2) !important;
		}
		.deleteUser > ins{
			background-position: -25px -93px;
		}
   		</style>
</head>

<body>
<div class="page-wrapper">
<div class="page-header heading">
					<h1 class="left">Configuration</h1>
				
				<div class="col-md-2 left">
					<button class="btn btn-danger element-hide mrgt5"
						id="error-header-button"></button>
				</div>
					<div class="pull-right">
						<button class="btn btn-default mrgr10"
							onclick="com.impetus.ankush.configurationNew.revertConfiguration();">Revert</button>
						<button class="btn btn-default" id="applyParameters"
							id="applyConfiguration" data-loading-text="Applying..."
							onclick="com.impetus.ankush.configurationNew.applyConfiguration();">Apply</button>
					</div>
		</div>
<div class="page-body" id="main-content">
<%@ include file="layout/breadcrumbs.jsp"%>
<div class="container-fluid">
		<div id="error-div"style="display:none;">
			<div id="popover-content" class="error-div-hadoop mrgb10"></div>
		</div>
	
		<div class="panel">
		<div class="panel-heading">
			<h3 class="panel-title left mrgt10 mrgl5">Admin Account</h3>
			 <button class="btn btn-default mrgl10" onclick="com.impetus.ankush.configurationNew.addNewRowInUserTable();">Add</button>
			<div class="pull-right panelSearch">
				<input id="userTableSearchBox" class="search-datatable search-datatable-button" type="text"
					placeholder="Search">
			</div>
		</div>
		<div class="row panel-body">
			<div class="col-md-12 text-left">
				<table class="table" id="userDatatable">
					<thead style="text-align: left;">
						<tr>
				    		<th style="width:1%;"></th>
				    		<th style="width:19%;">First Name</th>
				    		<th style="width:19%;">Last Name</th>
				    		<th style="width:19%;">User ID</th>
				    		<th style="width:19%;">Email</th>
				    		<th style="width:19%px;" id="account_status">Account Status</th>
				    		<th style="width:1%;"></th>
				    		<th style="width:1%;"></th>
				    	</tr>
					</thead>
				</table>
			</div>
		</div>

	</div>
	<div class="modal fade" id="sendMail" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">Verify Email Settings</h4>
					</div>
					<div class="modal-body">
								<div class="row">
									<div class="col-md-3 text-right">
										<label class="form-label">Send Email to:</label>
									</div>
									<div class="col-md-5 col-lg-5  form-group text-left">
										<input type="text" data-placement="right"
											data-toggle="tooltip" class="input-large form-control"
											title="Enter Email" placeholder="Enter Email" id="mailtoTest"></input>
									</div>
								</div>
								<div class="row">
									<div class="col-md-3 text-right">
										&nbsp;
									</div>
									<div class="col-md-9 form-group text-left text-default" id="validEmailText">
										An email will be sent to above email Id.
									</div>
								</div>
							</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal"  class="btn btn-default">Cancel</a>
                        <a href="#" class="btn btn-default" onclick="com.impetus.ankush.configurationNew.validationMail();">Send</a>
					</div>
				</div>
			</div>
		</div>	
		
		<div class="modal fade" id="deleteUserConfirmDialog" tabindex="-1"
			role="dialog" aria-labelledby="" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">User Delete</h4>
					</div>
					<div class="modal-body">
						<div class="row"><div class="col-md-12">User has been marked for Delete. Changes will be applicable on Apply.</div></div>
					</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn btn-default">OK</a>
					</div>
				</div>
			</div>
		</div>
		<!------------- Email Account Section----------------------------------------->			
			<%@ include file="emailAccount.jsp" %>
		<!--------------------Server Section-------------------->			
			<%@ include file="serverHosting.jsp" %>
			
	
	
	
	
	
	
</div>
</div>
</div>
</body>
</html>

