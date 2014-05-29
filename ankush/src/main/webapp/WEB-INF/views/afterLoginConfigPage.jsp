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
<!-- Common Configuration  after login page -->

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String baseUrl =request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort() + request.getContextPath();
pageContext.setAttribute("baseUrl",baseUrl);
%>
   		<script src="<c:out value='${baseUrl}' />/public/js/configurationNew.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js" type="text/javascript"></script>
   		<script>	
   		var confUrl = '<c:out value='${url}'/>'
   			$(document).ready(function(){
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
   		</style>
   		
<div class="section-header">
	<div  class="row-fluid mrgt20">
	  	<div class="span6">
	  	<h2 class="heading text-left left">Configuration</h2>
	  	<button class="btn-error"  id="errorBtn" style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important; position:relative; padding:0 15px; left:15px">Error</button>
	  	
	  	</div>
	  	
	  	<!--  
	  	
	  	<div class="span2 text-center minhgt0"><button class="btn btnAlert"  id="errorBtn" style="margin-right:8px;display:none;">Error</button></div>
	  	-->
	  	<div class="span6 text-right" id="header">
	  		
		  	<button class="btn" style="margin-right:8px;" onclick="com.impetus.ankush.configurationNew.revertConfiguration();">Revert</button>
		  	<button class="btn"  id="applyConfiguration" data-loading-text="Applying..." onclick="com.impetus.ankush.configurationNew.applyConfiguration(confUrl);">Apply</button>
	    </div>
	</div>
</div>
<div class="section-body mrgr20 common-tooltip">
	<div class="mrgnlft8">
		<div id="error-div" class="row-fluid" style="display:none;">
			<div id="popover-content" class="error-div-hadoop"></div>
		</div>
		<div class="row-fluid">
			<div class="span3">
    			<div class="span6"> <label class="form-label section-heading">Admin Account</label></div>
    			<div style="padding-top:7px;" class="span6 text-left"><button style="width:60px;" class="btn" onclick="com.impetus.ankush.configurationNew.addNewRowInUserTable();">Add</button></div>
			</div>	
			<div class="span3 offset6 text-right"><input type="text" placeholder="Search" id="userTableSearchBox"></div>
		</div>
		<div class="row-fluid mrgt10">
		  	<div class="span12">
			  	<table class="table table-striped" id="userDatatable" style="border:1px solid;border-color: #CCCCCC">
			  		<thead>
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

