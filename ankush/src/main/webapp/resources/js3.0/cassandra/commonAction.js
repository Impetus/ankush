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
/*js to populate column family drilldown page*/
com.impetus.ankush.commonAction = {
		postNodeActionServiceDialog : function(action,keyspace,columnfamily,dialogName){
			$('#'+dialogName+'-message').html(action+' operation will be done.');
			$('#confirmationDialogs'+dialogName).appendTo('body').modal('show');
			$('#confirmationDialogsOK'+dialogName).bind('click',function(){ com.impetus.ankush.commonAction.postNodeActionService(action,keyspace,columnfamily,dialogName); });
		},
		postNodeActionService : function(action,keyspace,columnfamily,dialogName){
			$('#confirmationDialogsOK'+dialogName).unbind('click');
			action = action.replace(' ','').toLowerCase();
			if((keyspace == '') && (columnfamily == '')){
				if(action == 'decommission'){
					var deleteNodeUrl = baseUrl + '/cluster/' + clusterId + '/remove';
					var deleteNodePost = {};
					deleteNodePost['@class'] = "com.impetus.ankush.cassandra.CassandraClusterConf";
					deleteNodePost.newNodes = [];
					deleteNodePost.newNodes[0] = {};
					deleteNodePost.newNodes[0].publicIp = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp;
					com.impetus.ankush.placeAjaxCall(deleteNodeUrl, 'POST', true, deleteNodePost, function(result){
						$('#error-div-'+dialogName+'DrillDown').hide();
						if(result.output.status){
							$('#confirmationDialogs'+dialogName).appendTo('body').modal('hide');
						}
						else{
							$('#confirmationDialogs'+dialogName).modal('hide');
							$('#error-div-'+dialogName+'DrillDown').show();
							$('#error-div-'+dialogName+'DrillDown').html(result.output.error[0]);
						}
					});
					return;
				}
			}
			var postUrl = null;
			if(clusterTechnology === 'Hybrid'){
				postUrl = baseUrl + '/monitor/'+clusterId+'/action?component=Cassandra';
			}else{
				postUrl = baseUrl + '/monitor/'+clusterId+'/action';
			}
			var data = {};
			data.action = action;
			if(keyspace == '')
				data.ip = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp;
			data.keyspace = keyspace;
			data.columnfamily = columnfamily;
			$('#confirmationDialogs'+dialogName).modal('hide');
			 com.impetus.ankush.placeAjaxCall(postUrl, "POST", true, data, function(result) {
					$('#error-div-'+dialogName+'DrillDown').hide();
					if(result.output.status){
					
					}
					else{
						$('#confirmationDialogs'+dialogName).appendTo('body').modal('hide');
						$('#error-div-'+dialogName+'DrillDown').show();
						$('#error-div-'+dialogName+'DrillDown').html(result.output.error[0]);
					}
			});
		},
}
