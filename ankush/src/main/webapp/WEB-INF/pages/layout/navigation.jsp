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


<style>
body{
	background-color: none ! important;
}
</style>

</head>

<div class="navbar main-menu nav-condensed" id="nav-main" style="">

	<ul class="nav" id="nav-main-ul">

		<li class="nav-dropdown menulist"><a id="menu-show"> <i
				class="fa fa-lg fa-fw fa-bars"></i>Menu List
		</a></li>
		<li class="nav-dropdown" id="dashboard" ><a title="Cluster Overview"
			href="<c:out value="${baseUrl}" />/dashboard" class="menuIcon"> <i
				class="fa fa-lg fa-fw fa-cubes"></i> Dashboard
		</a></li>
		<li class="nav-dropdown" id="dashboard-listTemplate"><a title="Template"
			href="<c:out value="${baseUrl}" />/dashboard/listTemplate" class="menuIcon"><i
				class="fa fa-lg fa-fw fa-file-text-o"></i> Template
		</a></li>
		<li class="nav-dropdown" id="dashboard-configuration"><a title="Configuration Settings"
			href="<c:out value="${baseUrl}" />/dashboard/configuration/C-D/configured" class="menuIcon"> <i
				class="fa fa-lg fa-fw fa-cog"></i> Configuration Settings
		</a></li>
		<li class="nav-dropdown"><a title="Users" href="#"> <i
				class="fa fa-lg fa-fw fa-user"></i> User Profile
		</a>
			<ul class="nav-sub" data-index="1" style="display: none;">
				<li id="dashboard-changePassword"><a title="Change Password"
					href="<c:out value="${baseUrl}" />/dashboard/changePassword/C-D/password">
						<i class="fa fa-fw fa-caret-right"></i> Change Password
				</a></li>
				<li><a title="Sign Out" href="#"
					onclick="com.impetus.ankush.login.signOut();"> <i
						class="fa fa-fw fa-caret-right"></i> Sign Out
				</a></li>
			</ul></li>

	</ul>
</div>

<script>
var jspPage = null;
$(document).ready(function(){
	 if(localStorage.getItem("open-menu")){
	  	  $("#nav-main").removeClass('nav-condensed');
	  	  $(".page-wrapper").css("margin-left","250px");
	  	  $(".page-header > .pull-right").css("margin-right","255px");
	  };
	  $("#nav-main-ul").navgoco({accordion: true});
	  $(".nav-condensed .nav > li").bind("hover",function(){
			$(".nav-condensed .nav").css("overflow","visible");
	  });
	com.impetus.ankush.navigation.getBreadCrumb();
	com.impetus.ankush.navigation.getAlertsAndWarnings();
	$('#menu-show').click(
			   function () {
				  if($("#nav-main").hasClass('nav-condensed')){
					  $("#nav-main").removeClass('nav-condensed');
					  $(".page-wrapper").css("margin-left","250px");
			    	  $(".page-header > .pull-right").css("margin-right","255px");
			    	  localStorage.setItem("open-menu", true);
			    	}			   
			      else{
			    	  $("#nav-main").addClass('nav-condensed');
			    	  $(".page-wrapper").css("margin-left","45px");
			    	  $(".page-header > .pull-right").css("margin-right","50px");
			    	  localStorage.removeItem("open-menu");
			      }
	});
	var menuPopulation = {};	
	menuPopulation.method = function(){
		com.impetus.ankush.navigation.getAlertsAndWarnings();
	};
	var alertPopulation = {};	
	alertPopulation.method = function(){
		com.impetus.ankush.navigation.populateAlerts();
	};
	var operationPopulation = {};	
	operationPopulation.method = function(){
		com.impetus.ankush.navigation.populateOperations();
	};
	pageLevelAutorefreshArray.push(menuPopulation);
	pageLevelAutorefreshArray.push(alertPopulation);
	pageLevelAutorefreshArray.push(operationPopulation);
});
</script>
<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.dateFormat-1.0.js"type="text/javascript"></script>
</html>
