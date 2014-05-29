/*******************************************************************************
*===========================================================
*Ankush : Big Data Cluster Management Solution
*===========================================================
*
*(C) Copyright 2014, by Impetus Technologies
*
*This is free software; you can redistribute it and/or modify it under
*the terms of the GNU Lesser General Public License (LGPL v3) as
*published by the Free Software Foundation;
*
*This software is distributed in the hope that it will be useful, but
*WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*See the GNU Lesser General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License 
*along with this software; if not, write to the Free Software Foundation, 
*Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/

var zookeeperMonitoringNodeListData = null;
com.impetus.ankush.zookeeperMonitoring = {
		//this function will populate node list and will be called from monitoringZookeeper.jsp 
		nodeListPopulate : function(){
			var nodeListUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/nodelist?technology=Zookeeper';
			if(zookeeperNodeListTables != null)
				zookeeperNodeListTables.fnClearTable();
			$.ajax({
				'type' : 'GET',
				'url' : nodeListUrl,
				'contentType' : 'application/json',
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
					if(result.output.status == true){
						if((result.output.zookeeperNodeInfo == undefined) || (result.output.zookeeperNodeInfo == null))
							return;
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
				},
				'error' : function(){
				}
				
			});
		},
		loadRunCommandPage : function(){
			$('#content-panel').sectionSlider('addChildPanel', {
	            url : baseUrl + '/hybrid-monitoring/zookeeper/runCommand',
	            method : 'get',
	            title : 'Run Command',
	            tooltipTitle : com.impetus.ankush.commonMonitoring.clusterName+'/Zookeeper',
	            previousCallBack : "",
	            callback : function() {
	            	com.impetus.ankush.zookeeperMonitoring.populateCommand();
	            	com.impetus.ankush.zookeeperMonitoring.populateIp();
	            },
	            callbackData : {
	            }
	        });
		},
		populateCommand : function(){
			var commandListUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/commandlist?technology=Zookeeper';
			$.ajax({
				'type' : 'GET',
				'url' : commandListUrl,
				'contentType' : 'application/json',
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
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
				},
				'error' : function(){
				}
				
			});
		},
		populateIp : function (){
			if((zookeeperMonitoringNodeListData == null) || (zookeeperMonitoringNodeListData == undefined)){
				$('#zookeeperIpDropDown')
				.append('<option>No IP Found</option>')
				.addClass('error-box');
				return;
			}
			for(var j = 0 ; j < zookeeperMonitoringNodeListData.length ; j++){
				$('#zookeeperIpDropDown').append('<option value="'+zookeeperMonitoringNodeListData[j].nodeIp+'">'+zookeeperMonitoringNodeListData[j].nodeIp+'</option>');
			}
		},
		runCommand : function(){
			var runCommandUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/runFourLetterCommand?technology=Zookeeper'; 
			var data = {};
			data.ip = $('#zookeeperIpDropDown').val();
			data.command = $('#zookeeperCommandDropDown').val();
			$.ajax({
				'type' : 'POST',
				'url' : runCommandUrl,
				'contentType' : 'application/json',
				'data' : JSON.stringify(data),
				'async' : true,
				'dataType' : 'json',
				'success' : function(result) {
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
						
					}
				},
				'error' : function(){
				}
				
			});
		}
};
