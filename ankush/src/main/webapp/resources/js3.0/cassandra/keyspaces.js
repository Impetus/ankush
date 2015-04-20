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
com.impetus.ankush.keyspaces = {
		keyspacePopulate : function(){
			var keyspaceUrl =  null;
			if (clusterTechnology == 'Hybrid')
				keyspaceUrl = baseUrl+'/monitor/'+clusterId+'/keyspaces?component='+ hybridTechnology;
			else
				keyspaceUrl = baseUrl+'/monitor/'+clusterId+'/keyspaces';
			$("#showLoading").removeClass('element-hide');
			 com.impetus.ankush.placeAjaxCall(keyspaceUrl, "GET", true, null, function(result) {
				 $("#showLoading").addClass('element-hide');
					if(keyspaceTable != null)
						keyspaceTable.fnClearTable();
					keyspacesData = result.output;
					if(result.output.status == true){
						if((result.output.Keyspaces == undefined) || (result.output.Keyspaces == null) || (result.output.Keyspaces.length == 0)){
							keyspaceTable.fnSettings().oLanguage.sEmptyTable = "No data available.";
							keyspaceTable.fnClearTable();
							return;
						}	
						for(var i = 0 ; i < result.output.Keyspaces.length ; i++){
							var url = baseUrl+'/cassandraMonitoring/'+clusterName+'/'+hybridTechnology+'/keyspaces/'+result.output.Keyspaces[i].keyspaceName+'/C-D/'+clusterId+'/'+clusterTechnology;
							var navigation = '<a href="'+url+'"><img id="navigationImgTopologyDrillDown'
							+ i
							+ '" src="'
							+ baseUrl
							+ '/public/images/icon-chevron-right.png"/></a>';
							keyspaceTable.fnAddData([
							                           	result.output.Keyspaces[i].keyspaceName,
							                           	result.output.Keyspaces[i].strategyOptions,
							                           	result.output.Keyspaces[i].replicationStrategy,
							                           	result.output.Keyspaces[i].cfCount,
							                           	navigation,
							                           ]);
							com.impetus.ankush.keyspaces.tileCreateKeyspaces();	
						}
					}else{
						keyspaceTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						keyspaceTable.fnClearTable();
					}
			},function(){
				$("#showLoading").addClass('element-hide');
			});
		}
};
