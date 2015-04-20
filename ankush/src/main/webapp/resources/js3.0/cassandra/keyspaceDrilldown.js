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
/*js to populate keyspace drilldown page*/
com.impetus.ankush.keyspaceDrilldown = {
		//this function will populate table in keyspace drilldown page
		keyspaceDrillDownPopulate : function(keyspaceName){
			var keyspaceUrl =  null;
			if (clusterTechnology == 'Hybrid')
				keyspaceUrl = baseUrl+'/monitor/'+clusterId+'/columnfamilies?keyspace='+keyspaceName+'&component='+ hybridTechnology;
			else
				keyspaceUrl = baseUrl+'/monitor/'+clusterId+'/columnfamilies?keyspace='+keyspaceName;
			$("#showLoading").removeClass('element-hide');
			 com.impetus.ankush.placeAjaxCall(keyspaceUrl, "GET", true, null, function(result) {
				 $("#showLoading").addClass('element-hide');
					if(columnFamiliesTables != null)
						columnFamiliesTables.fnClearTable();
					columnFamilyDetailData = result;
					if(result.output.status == true){
						if((result.output.ColumnFamilies == undefined) || (result.output.ColumnFamilies == null)){
							columnFamiliesTables.fnSettings().oLanguage.sEmptyTable = "No data available.";
							columnFamiliesTables.fnClearTable();
							return;
						}
						for(var j = 0 ; j < result.output.ColumnFamilies.length ; j++){
							var columnFamilyDrilldownUrl = baseUrl+'/cassandraMonitoring/'+clusterName+'/'+hybridTechnology+'/keyspaces/'+keyspaceName+'/'+result.output.ColumnFamilies[j].columnFamilyName+'/C-D/'+clusterId+'/'+clusterTechnology;
							var navigation = '<a href="'+columnFamilyDrilldownUrl+'"><img id="navigationImgTopologyDrillDown'
							+ j
							+ '" src="'
							+ baseUrl
							+ '/public/images/icon-chevron-right.png"/></a>';
							columnFamiliesTables.fnAddData([
							                           	result.output.ColumnFamilies[j].columnFamilyName,
							                           	result.output.ColumnFamilies[j].liveSSTableCount,
							                           	result.output.ColumnFamilies[j].readLatency,
							                           	result.output.ColumnFamilies[j].writelatency,
							                           	result.output.ColumnFamilies[j].pendingTasks,
							                           	navigation
							                           ]);
						}
					}else{
						columnFamiliesTables.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						columnFamiliesTables.fnClearTable();
					}
			},function(){
				$("#showLoading").addClass('element-hide');
			});
		}
};
