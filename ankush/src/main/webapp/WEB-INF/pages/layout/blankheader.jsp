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

		<c:if test="${cssFiles != null}">
            <c:forEach var="cssFile" items='${cssFiles}' >
            <link rel="stylesheet" href="<c:out value='${baseUrl}' />/public/css/<c:out value="${cssFile}"/>.css" type="text/css" media="all" />
            </c:forEach>
        </c:if>
        <c:if test="${jsFiles != null}">
            <c:forEach var="jsFile" items='${jsFiles}' >
            <script src="<c:out value='${baseUrl}' />/public/js3.0/<c:out value="${jsFile}"/>.js" type="text/javascript"></script>
            </c:forEach>
        </c:if>
 	      
		<script type="text/javascript">
            var baseUrl = '<c:out value='${baseUrl}' />';
            var username = null;
        </script>
              
