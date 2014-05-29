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
<%@ include file="../layout/blankheader.jsp"%>
<script>
function autoRefreshConfigurationPage(){
	var obj1 = {};
	var autoRefreshArray = [];
	obj1.varName = ''; 
	obj1.callFunction = "";
	obj1.time = 0;
	autoRefreshArray.push(obj1);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function(){
	var monitoringClusterTechnology = com.impetus.ankush.commonMonitoring.clusterTechnology;
	if(com.impetus.ankush.commonMonitoring.clusterEnv == 'Cloud')
		delete com.impetus.ankush.commonMonitoring[monitoringClusterTechnology].links.Configuration.confLinks['Auto-provision'];
	else if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hadoop2')
		delete com.impetus.ankush.commonMonitoring[monitoringClusterTechnology].links.Configuration.confLinks['Auto-provision'];
	if((monitoringClusterTechnology != 'Hybrid') || (com.impetus.ankush.commonMonitoring.hybridTechnology == null)){
		if((undefined == com.impetus.ankush.commonMonitoring[monitoringClusterTechnology]) || (monitoringClusterTechnology == null)){
			monitoringClusterTechnology = "Default";
		}
		/* This page will populate links on configuration child page*/
		for(var key in com.impetus.ankush.commonMonitoring[monitoringClusterTechnology].links.Configuration.confLinks)
			$("#commonLinksConfiguration").append(' <div class="row-fluid"><div class="span8 text-left" id="conflink_'+key.split(' ').join('_')+'"><h4 class="section-heading"><a href="#" onclick="com.impetus.ankush.commonMonitoring[\''+
			                                                                                               monitoringClusterTechnology+'\'].links.Configuration.confLinks[\''+
			                                                                                                            key+'\'].methodCall()">'+
			                                                                                                                         key+'&nbsp;&nbsp;&nbsp;<img src="'+
			                                                                                                                                 baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>'); 
	}
	else{
		monitoringClusterTechnology = com.impetus.ankush.commonMonitoring.hybridTechnology;
		var key = 'Parameters';
			$("#commonLinksConfiguration").append(' <div class="row-fluid"><div class="span8 text-left" id="conflink_'+key.split(' ').join('_')+'"><h4 class="section-heading"><a href="#" onclick="com.impetus.ankush.commonMonitoring[\''+
			                                                                                               monitoringClusterTechnology+'\'].links.Configuration.confLinks[\''+
			                                                                                                            key+'\'].methodCall()">'+
			                                                                                                                         key+'&nbsp;&nbsp;&nbsp;<img src="'+
			                                                                                                                                 baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
		if(monitoringClusterTechnology == 'Hadoop'){
			key = 'Job Scheduler';
			$("#commonLinksConfiguration").append(' <div class="row-fluid"><div class="span8 text-left" id="conflink_'+key.split(' ').join('_')+'"><h4 class="section-heading"><a href="#" onclick="com.impetus.ankush.commonMonitoring[\''+
                    monitoringClusterTechnology+'\'].links.Configuration.confLinks[\''+
                                 key+'\'].methodCall()">'+
                                              key+'&nbsp;&nbsp;&nbsp;<img src="'+
                                                      baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
		}
	}
	autoRefreshConfigurationPage();
	//com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
		                                                              
});
</script>

<!-- This page will show configuration link child page and show links -->
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4">
			<h2 class="heading text-left">Configurations</h2>
		</div>
	</div>
</div>

<div class="section-body">
	<div class="container-fluid" id="commonLinksConfiguration">
		
	</div>
</div>
