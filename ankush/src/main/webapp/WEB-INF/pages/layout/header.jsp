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

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String baseUrl =request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort() + request.getContextPath();
pageContext.setAttribute("baseUrl",baseUrl);
%>
		<link rel="shortcut icon" href="<c:out value="'${baseUrl}'" />/public/images/newUI-Icons/ankushFAV_1.ico"/>
		<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/libcss3.0/jquery-ui.css" media="all"/>
		<link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/bootstrap-3.2.0-dist/css/bootstrap.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/bootstrap-3.2.0-dist/css/bootstrap-theme.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/libcss3.0/bootstrap-toggle-buttons.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/font-awesome-4.1.0/css/font-awesome.min.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/ankush.expandable.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/ankush.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/theme.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/libcss3.0/nv.d3.css"/>
        <link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/libcss3.0/bootstrap-multiselect.css"/>
        <link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/libcss3.0/bootstrap-editable.css"/>
        <link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/libcss3.0/jquery.navgoco.css"/>
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/dashboard.less" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css3.0/ladda-themeless.min.css" media="all"/>
        
        <script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery-2.1.1.min.js" type="text/javascript"></script>
        <script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery-ui.js" type="text/javascript"></script>
        <script src="<c:out value='${baseUrl}' />/public/bootstrap-3.2.0-dist/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="<c:out value='${baseUrl}' />/public/libjs3.0/less-1.3.3.min.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js3.0/ankush.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js3.0/ankush.constants.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js3.0/ankush.validation.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js3.0/ankush.common.js" type="text/javascript"></script> 
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.toggle.buttons.js" type="text/javascript"></script>	
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/bootstrap-button.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.dataTables.min.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.dataTables.rowGrouping.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.navgoco.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/bootstrap-multiselect.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/d3.v3.min.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/d3.v2.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/nv.d3.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/pie.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/masonry.pkgd.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/ajaxfileupload.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/bootstrap-editable.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/amplify.min.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.withinViewport.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/withinViewport.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.ba-throttle-debounce.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.jstree.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/spin.min.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libjs3.0/ladda.min.js" type="text/javascript"></script>
		<script type="text/javascript">
            var baseUrl = '<c:out value='${baseUrl}' />';
        </script>
        <script type="text/javascript" >
		var clusterId = null;	
    	var clusterName = null;
    	var clusterTechnology = null;
    	var hybridTechnology = null;
    	var jspPage = null;
    	var decisionVar = '';
    	var autorefreshArray = [];
    	var pageLevelAutorefreshArray = [];
        $(document).ready(function(){
        	/* These variables will be set throught the common monitoring pages */
        	clusterId = '<c:out value='${clusterId}'/>';
			clusterName = '<c:out value='${clusterName}'/>';
			clusterTechnology = "Hybrid";//'<c:out value='${clusterTechnology}'/>';
			hybridTechnology = '<c:out value='${hybridTechnology}'/>';
			jspPage = '<c:out value='${page}'/>';
			decisionVar = '<c:out value='${decisionVar}'/>';
			var jsLink = $("<script type='text/javascript' src='<c:out value="${baseUrl}" />/public/js3.0/techListsAndActions.js'>");
		    $("head").append(jsLink); 
		    $("head").append('<title>'+jspPage+'</title>'); 
		    com.impetus.ankush.navigation.populateMenu();
		    com.impetus.ankush.navigation.populateAlerts();
		    com.impetus.ankush.navigation.populateOperations();
        });
        var projectEnvVariable = 'Ankush';
        var buildManager = {
        			'Ankush' : {
        				'Display' : 'Ankush'
        			},
        			
        };
        var shortcutIcon = $('<link rel="shortcut icon" href="<c:out value="${baseUrl}" />/public/images/newUI-Icons/ankushFAV_1.ico" type="image/x-icon" />');;
        $("head").append(shortcutIcon); 
        </script>
 		<c:if test="${cssFiles != null}">
            <c:forEach var="cssFile" items='${cssFiles}' >
            <link rel="stylesheet" href="<c:out value='${baseUrl}' />/public/css3.0/<c:out value="${cssFile}"/>.css" type="text/css" media="all" />
            </c:forEach>
        </c:if>
        <c:if test="${jsFiles != null}">
            <c:forEach var="jsFile" items='${jsFiles}' >
            <script src="<c:out value='${baseUrl}' />/public/js3.0/<c:out value="${jsFile}"/>.js" type="text/javascript"></script>
            </c:forEach>
        </c:if>    
<style>
.table-striped tbody > tr:nth-child(odd) > td,
.table-striped tbody > tr:nth-child(odd) > th {
  background-color: #fff;
}
.table-striped tbody > tr:hover > td,
.table-striped tbody > tr:hover > th {
  background-color: #f9f9f9 ! important;
}
/* .group{
	background-color: #f9f9f9 ! important;
} */
</style>
<script src="<c:out value='${baseUrl}' />/public/js3.0/navigation.js" type="text/javascript"></script>

       