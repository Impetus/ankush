/*******************************************************************************
*  ===========================================================
*  Ankush : Big Data Cluster Management Solution
*  ===========================================================
*  
*  (C) Copyright 2014, by Impetus Technologies
*  
*  This is free software; you can redistribute it and/or modify it under
*  the terms of the GNU Lesser General Public License (LGPL v3) as
*  published by the Free Software Foundation;
*  
*  This software is distributed in the hope that it will be useful, but
*  WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*  See the GNU Lesser General Public License for more details.
*  
*  You should have received a copy of the GNU Lesser General Public License 
*  along with this software; if not, write to the Free Software Foundation, 
* Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*******************************************************************************/
var zookeeperMonitoringNodeListData = null;
com.impetus.ankush.zookeeperMonitoring = {
		//this function will populate node list and will be called from monitoringZookeeper.jsp 
		nodeListPopulate : function(){
			var nodeListUrl =  baseUrl+'/monitor/'+clusterId+'/zookeepernodes?component='+hybridTechnology;
			if(zookeeperNodeListTables != null)
				zookeeperNodeListTables.fnClearTable();
			$("#showLoading").show();
			 com.impetus.ankush.placeAjaxCall(nodeListUrl, "GET", true, null, function(result) {
				 $("#showLoading").hide();
					if(result.output.status == true){
						if((result.output.zookeeperNodeInfo == undefined) || (result.output.zookeeperNodeInfo == null)){
							zookeeperNodeListTables.fnSettings().oLanguage.sEmptyTable = "No data Found.";
							zookeeperNodeListTables.fnClearTable();
							return;
						}
						zookeeperMonitoringNodeListData = result.output.zookeeperNodeInfo;
						for(var j = 0 ; j < result.output.zookeeperNodeInfo.length ; j++){
							zookeeperNodeListTables.fnAddData([
							                           	result.output.zookeeperNodeInfo[j].nodeIp,
							                           	result.output.zookeeperNodeInfo[j].serverId,
							                           	result.output.zookeeperNodeInfo[j].serverType,
							                           ]);
						}
					}else{
						zookeeperNodeListTables.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						zookeeperNodeListTables.fnClearTable();
					}
				
				
			},function(){
				$("#showLoading").hide();
				zookeeperNodeListTables.fnSettings().oLanguage.sEmptyTable = "No data available.";
				zookeeperNodeListTables.fnClearTable();
			});
		},
		loadRunCommandPage : function(){
			var url = baseUrl + '/hybrid-monitoring/'+clusterName+'/'+hybridTechnology+'/runCommand/C-D/'+clusterId+'/'+clusterTechnology;
			$(location).attr('href',(url));	
		},
		populateCommand : function(){
			var commandListUrl =  baseUrl+'/monitor/'+clusterId+'/commandlist?component='+hybridTechnology;
			 com.impetus.ankush.placeAjaxCall(commandListUrl, "GET", true, null, function(result) {
					if(result.output.status == true){
						if((result.output.command == undefined) || (result.output.command == null)){
							$('#zookeeperCommandDropDown')
							.append('<option>No Command Found</option>')
							.addClass('error-box');
							return;
						}
						for(var j = 0 ; j < result.output.command.length ; j++){
							$('#zookeeperCommandDropDown').append('<option value="'+result.output.command[j]+'">'+result.output.command[j]+'</option>');
						}
					}else{
						
					}
			});
		},
		populateIp : function (){
			var nodeListUrl =  baseUrl+'/monitor/'+clusterId+'/zookeepernodes?component='+hybridTechnology;
			 com.impetus.ankush.placeAjaxCall(nodeListUrl, "GET", false, null, function(result) {
				 if(result.output.status == true){
						if((result.output.zookeeperNodeInfo == undefined) || (result.output.zookeeperNodeInfo == null))
							return;
						zookeeperMonitoringNodeListData = result.output.zookeeperNodeInfo;
						if((zookeeperMonitoringNodeListData == null) || (zookeeperMonitoringNodeListData == undefined)){
							$('#zookeeperIpDropDown')
							.append('<option>No IP Found</option>')
							.addClass('error-box');
							return;
						}
						for(var j = 0 ; j < zookeeperMonitoringNodeListData.length ; j++){
							$('#zookeeperIpDropDown').append('<option value="'+zookeeperMonitoringNodeListData[j].nodeIp+'">'+zookeeperMonitoringNodeListData[j].nodeIp+'</option>');
						}
						
					}
			 });
			
		},
		runCommand : function(){
			var host = $('#zookeeperIpDropDown').val();
			var command = $('#zookeeperCommandDropDown').val();
			var runCommandUrl =  baseUrl+'/monitor/'+clusterId+'/runFourLetterCommand?component='+hybridTechnology+'&host='+host+'&command='+command; 
			 com.impetus.ankush.placeAjaxCall(runCommandUrl, "GET", true, null, function(result) {
					if(result.output.status == true){
						if((result.output.out == undefined) || (result.output.out == null)){
							$("#error-zookeeper-command").show();
							$("#error-zookeeper-command").text("Output is undefined.");
							return;
						}
						$("#zookeeperCommandOutput").show();
						$("#zookeeperCommandOutputHead").show();
						$("#zookeeperCommandOutput").html(result.output.out);
					}else{
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,'popover-content', 'error-div', '');
					}
			});
		}
};
