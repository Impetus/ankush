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
<!-- Common Configuration  before login page -->

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<title>Ankush:Configuration</title>
<%
String baseUrl =request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort() + request.getContextPath();
pageContext.setAttribute("baseUrl",baseUrl);
%>
		<link rel="shortcut icon" href="<c:out value="${baseUrl}" />/public/images/newUI-Icons/ankushFAV_1.ico" type="image/x-icon" />
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/bootstrap.css" media="all"/>
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/bootstrap-toggle-buttons.css" media="all"/>
		<link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/css/styles.less" media="all"/>
		<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/_jquery-ui-1.8.20.custom.css" media="all"/>	
        <script src="<c:out value='${baseUrl}' />/public/libJs/jquery.js" type="text/javascript"></script>
        <script src="<c:out value='${baseUrl}' />/public/libJs/jquery-1.7.2.min.js" type="text/javascript"></script>
	 	<script src="<c:out value='${baseUrl}' />/public/libJs/jquery-ui-1.10.0.custom.js" type="text/javascript"></script>
	 	<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.toggle.buttons.js" type="text/javascript"></script>
	 	<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.validate.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/less.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js/jquery.sectionslider.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js/ankush.js" type="text/javascript"></script> 
		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dataTables.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dataTables.min.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-dropdown.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-editable.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootbox.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-tooltip.js" type="text/javascript"></script>
   				
		<script type="text/javascript">
            var baseUrl = '<c:out value='${baseUrl}' />';
            var username = null;
        </script>
   		<script src="<c:out value='${baseUrl}' />/public/js/configurationNew.js" type="text/javascript"></script>
   		<script>	
   			var confUrl = '<c:out value='${url}'/>'
   			$(document).ready(function(){
   				com.impetus.ankush.configurationNew.initConfig(baseUrl,confUrl);
   			});
   			
   		</script>
   		<style> 
		.left-bar {
			background: url('../public/images/sectionslider/left-bar.png');
			background-repeat: repeat-y;
			display: table-cell;
			min-width: 50px; /* real browsers */
			height: 100%;
			margin: 0px;
			padding: 0px;
			position: fixed;
			text-align: center;
			vertical-align: top;
			width: 50px;
		}
		ul.left-nav li {
		    height: 32px;
		    list-style: none outside none;
		    margin-left: -16px;
		    padding-bottom: 20px;
		    width: 32px;
		    opacity:0.4;
		    filter:alpha(opacity=40);
		    cursor: pointer;
		}
		ul.left-nav li.selected {
		    opacity:1;
		    filter:alpha(opacity=100);
		}
		ul.left-nav li.dashboard {
		    background: url("../public/images/81-dashboard@2x.png") no-repeat scroll 0 0 transparent;
		}
		ul.left-nav li.cluster {
		    background: url("../public/images/000-cluster@2x.png") no-repeat scroll 0 0 transparent;
		}
		ul.left-nav li.settings {
		    background: url("../public/images/19-gear@2x.png") no-repeat scroll 0 0 transparent;
		}
		ul.left-nav li.user {
		    background: url("../public/images/111-user@2x.png") no-repeat scroll 0 0 transparent;
		}
		.editable-input .input-medium{
			height:25px;
		}
		.fontFamilyClass{
			font-family: 'Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif;
		}
		.popoverTechnology{
			border : 1px solid rgba(0, 0, 0, 0.2) !important;
		}
		
</style>
</head>

<div>
	<div class="left-bar">
	      <div style="position:relative;height:100%;">
	        <div style="position:absolute;top:10px;">
	          <ul class="left-nav">
	            <li class="dashboard selected"></li>
	            <li class="cluster"></li>
	          </ul>
	        </div>
	        <div style="position:absolute;bottom:0px;">
	          <ul class="left-nav">
	            <li class="settings"></li>
	            <li id="userProfile" class="user" style="padding-bottom: 10px;"></li>
	         </ul>
	        </div>
		</div>
	</div>
	
</div>


<div style="margin-left: 50px;font-family:'Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif;">
	<div class="section-header" style="border-bottom:5px solid #7B7C7B;width:92.5%;position: fixed;z-index: 101;background-color: #FFFFFF;">
		<div  class="row-fluid">
			  	<div class="span3"><h2 class="heading text-left" style="padding-top: 20px;">Configuration</h2></div>
			  	<div class="span2 text-center" style="padding:15px;"><button class="btn btnAlert"  id="errorBtn" style="margin-right:8px;display:none;">Error</button></div>
			  	<div class="span4 offset3" id="header" style="padding: 15px;">
			  		<div class="row-fluid">
			  			<div class="span10 text-right">
				  			<button class="btn" onclick="com.impetus.ankush.configurationNew.revertConfiguration();">Revert</button>
			  			</div>
			  			<div class="span2 text-left">
			  				<button class="btn" id="applyConfiguration" data-loading-text="Applying..." onclick="com.impetus.ankush.configurationNew.applyConfiguration(confUrl);">Apply</button>
				  		</div>
				  	</div>		
			    </div>
		</div>
	</div>	
	<div class="section-body" style="margin-left:8px;padding-top:67px;z-index: 100;">
		<div class="container-fluid">
			<div id="error-div" class="row-fluid" style="display:none;">
				<div id="popover-content" class="commonErrorDiv"></div>
			</div>
			<div class="row-fluid">
				<div class="span3">
	    			<div class="span6"> <label class="form-label section-heading">Admin Account</label></div>
	    			<div style="padding-top:7px;" class="span6 text-left"><button style="width:60px;" class="btn" onclick="com.impetus.ankush.configurationNew.addNewRowInUserTable();">Add</button></div>
				</div>	
				<div class="span3 offset6 text-right"><input type="text" placeholder="Search" id="userTableSearchBox"></div>
			</div>
			<div class="row-fluid" style="margin-top: 10px;">
			  	<div class="span12">
				  	<table class="table table-striped" id="userDatatable" style="border:1px solid;border-color: #CCCCCC">
				  		<thead style="color:#6F7971;font-size:12px;">
					    	<tr>
					    		<th style="width:0px;" style="display:none;"></th>
					    		<th style="width:140px;">First Name</th>
					    		<th style="width:140px;">Last Name</th>
					    		<th style="width:140px;">User ID</th>
					    		<th style="width:140px;">Email</th>
								<th style="width:140px;" id="account_status">Account Status</th>
					    		<th style="width:0px;"></th>
					    		<th style="width:0px;" style="display:none;"></th>
					    	</tr>
					    </thead>
					    <tbody style="font-size:12px;"></tbody>
					 </table>
				</div>
			</div>
		<div id="deleteUserConfirmDialog" class="modal hide fade" style="display:none;" data-keyboard="true" data-backdrop="static">
			<div class="modal-header text-center">
				<h4>User Delete</h4>
			</div>
			<div class="modal-body">
				<div class="row-fluid"><div class="span12">User has been marked for Delete. Changes will be applicable on Apply.</div></div>
			</div>
			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn">OK</a>
			</div>
		</div>
<!------------- Email Account Section----------------------------------------->			
			<%@ include file="emailAccount.jsp" %>
<!--------------------Server Section-------------------->			
			<%@ include file="serverHosting.jsp" %>
		</div>
	</div> 
</div>		
<div id="goToLoginOrDashboard" style="display:none;"></div> 
