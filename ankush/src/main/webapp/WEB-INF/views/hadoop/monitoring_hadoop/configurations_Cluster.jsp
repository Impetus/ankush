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
<!-- Cluster Configuration Page containing Cluster General Details, Java Details, Cloud Configuration Details & Node Authentication Details  -->

<%@ include file="../../layout/blankheader.jsp"%>
<div class="section-header">
	<div  class="row-fluid mrgt20">
	  	<div class="span4"><h2 class="heading text-left">Cluster Configurations</h2></div>
		<div class="span8 text-right" style="display: none;">
		  	<button class="btn" style="margin-right:48px;" onclick="com.impetus.ankush.removeChild(3);">Cancel</button>
		 </div>
	</div>
	
</div>
<div class="section-body">
	<div class="container-fluid">
			<%@ include file="_config_general_details.jsp"%>
			<%@ include file="_config_java_installation.jsp"%>
			<%@ include file="_config_cloudEnv_details.jsp"%>
			<%@ include file="_config_cloud_details.jsp"%>
			<%@ include file="_config_node_authentication.jsp"%>
	</div>	
</div>
