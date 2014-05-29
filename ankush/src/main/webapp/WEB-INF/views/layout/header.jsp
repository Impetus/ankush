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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String baseUrl =request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort() + request.getContextPath();
pageContext.setAttribute("baseUrl",baseUrl);
%>

 		<link rel="shortcut icon" href="<c:out value="${baseUrl}" />/public/images/newUI-Icons/ankushFAV_1.ico" type="image/x-icon" />
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/bootstrap.css" media="all"/>
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/bootstrap-responsive.css" media="all"/>
        <%-- <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/bootstrap3.css" media="all"/>
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/ankush.bootstrap.css" media="all"/> --%>
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/bootstrap-toggle-buttons.css" media="all"/>
        <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/css/ankush.less" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/css/ankush.expandable.css" media="all"/>
	    <link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/css/ankush.login.less" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/css/jquery.sectionslider.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:out value='${baseUrl}' />/public/css/styles.less" media="all"/>
		<link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/css/dashboard.less" media="all"/>
		<link rel="stylesheet/less" type="text/css" href="<c:out value="${baseUrl}" />/public/css/default.less" media="all"/>
	    <link rel="stylesheet/less" type="text/css" href="<c:out value='${baseUrl}' />/public/libCss/datepicker.css"/>
	    <link rel="stylesheet/less" type="text/css" href="<c:out value='${baseUrl}' />/public/libCss/datepicker.less"/>
	    <link rel="stylesheet/less" type="text/css" href="<c:out value='${baseUrl}' />/public/libCss/nv.d3.css"/>
	    <link rel="stylesheet" type="text/css" href="<c:out value="${baseUrl}" />/public/libCss/_jquery-ui-1.8.20.custom.css" media="all"/>
	  	<link rel="stylesheet" type="text/less" href="<c:out value="${baseUrl}" />/public/css/custom.less" media="all"/>
		<%-- <script src="<c:out value='${baseUrl}' />/public/libJs/jquery.js" type="text/javascript"></script> --%>
		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery-1.7.2.min.js" type="text/javascript"></script>
	 	<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-modal.js" type="text/javascript"></script>
	 	<script src="<c:out value='${baseUrl}' />/public/libJs/jquery-ui-1.10.0.custom.js" type="text/javascript"></script>
	 	<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.toggle.buttons.js" type="text/javascript"></script>
	 	<script src="<c:out value='${baseUrl}' />/public/libJs/less-1.3.3.min.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap.js" type="text/javascript"></script>
		<%-- <script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap3.js" type="text/javascript"></script> --%>
		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.switchButton.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js/jquery.sectionslider.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js/jquery.textEditable.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js/jquery.expandable.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js/ankush.js" type="text/javascript"></script> 
   		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dataTables.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dataTables.min.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dataTables.rowGrouping.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-multiselect.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-editable.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-tooltip.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-popover.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-clickover.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap.file-input.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootbox.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.dateFormat-1.0.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.fileDownload.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/ajaxfileupload.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-datepicker.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-popover.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/d3.v3.min.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/pie.js" type="text/javascript"></script>
   		<script src="<c:out value='${baseUrl}' />/public/libJs/masonry.pkgd.js" type="text/javascript"></script>
   		<%-- <script src="<c:out value='${baseUrl}' />/public/libJs/modernizr-transitions.js" type="text/javascript"></script> --%>
   		<%-- <script src="<c:out value='${baseUrl}' />/public/libJs/jquery.masonry.js" type="text/javascript"></script> --%>
		<script src="<c:out value='${baseUrl}' />/public/libJs/d3.v2.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/nv.d3.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.hotkeys.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.jstree.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/stackedArea.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-button.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/dataTables.IpSort.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/js/error.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.withinViewport.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/withinViewport.js" type="text/javascript"></script>
		<script src="<c:out value='${baseUrl}' />/public/libJs/jquery.ba-throttle-debounce.js" type="text/javascript"></script>

 		<script type="text/javascript">
            var baseUrl = '<c:out value='${baseUrl}' />';
        </script>
        <script type="text/javascript" >
        $(document).ready(function(){
        	$.get(baseUrl+'/user/userid', function(data) {
        		if(data.status == '200'){
        			if(data.output.target != null){
            			if(data.output.	target == 'configure')
            				$('#content-panel').sectionSlider('addRootConfigPanel');
            		}
            		else{
            			test();
            		}
        		}else{
        			$("#login-box").show();
        			$('#j_username').focus();
        			$("#footerCanvas").show();
        			$("#version").show();
        		}
        	});
        	$('ul.left-nav li').click(function(){
        	if($(this).attr('id') != 'userProfile'){	
			  $('ul.left-nav li').each(function(){
				  $(this).removeClass('selected');  
			  });
			  $(this).addClass('selected');
        	} 
			});
        	
		});
        var autoRefreshCallsObject = {};
        var autoRefreshCallTestVariable = 0;
		</script>
        
   
