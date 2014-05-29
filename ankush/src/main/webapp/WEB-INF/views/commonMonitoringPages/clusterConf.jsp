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
<script>
function autoRefreshClusterConfigurationPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = ''; 
	obj1.callFunction = "";
	obj1.time = 0;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function(){
	var url = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/details';
	var result = com.impetus.ankush.placeAjaxCall(url,'get',false);
	$("#clusterConfiguration_clusterName").append(result.output.clusterName);
	$("#genDesc").remove();
	$("#clusterConfiguration_environment").append(result.output.environment);
	if(!result.output.javaConf.install){
		$('#config_java_checkbox').attr('checked',false);
	}
	$('#config_javaHomePath').text(result.output.javaConf.javaHomePath);
	$('#config_clusterUserName').text(result.output.username);
	if(result.output.password){
		$('#config_nodeauth_authType').text('Password');
		$('#config_clusterPassword').text('******');
		$('#config_nodeauth_radio_password').attr('checked',true);
	}
	else{
		$('#config_nodeauth_radio_sharedkey').attr('checked',true);
	}
	autoRefreshClusterConfigurationPage();
});

</script>
<%@ include file="../layout/blankheader.jsp"%>
<div class="section-header">
	<div  class="row-fluid mrgt20">
	  	<div class="span4"><h2 class="heading text-left">Cluster Configurations</h2></div>
		<div class="span8 text-right" style="display: none;">
		  	<button class="btn" style="margin-right:48px;" onclick="com.impetus.ankush.removeChild(3);">Cancel</button>
		 </div>
	</div>
	
</div>
<div class="section-body">
	<div class="container-fluid mrgnlft8">
			<%@ include file="../hadoop/monitoring_hadoop/_config_general_details.jsp"%>
			<%@ include file="../hadoop/monitoring_hadoop/_config_java_installation.jsp"%>
			<%@ include file="../hadoop/monitoring_hadoop/_config_node_authentication.jsp"%>
			<%-- <%@ include file="../cassandra/cassandraClusterMonitoring/clusterConfiguration.jsp"%> --%>
	</div>	
</div>
