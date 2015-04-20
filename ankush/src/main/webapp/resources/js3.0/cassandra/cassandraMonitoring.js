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
var keyspacesData = null;
var columnFamilyDetailData = null;
var enabledNodes = 0;
com.impetus.ankush.cassandraMonitoring = {
		//this will populate 
	  	nodeListPopulate : function(){
			var nodeListUrl =  baseUrl+'/monitor/'+clusterId+'/nodelist?component='+hybridTechnology;
			if(cassandraNodeListTables != null)
				cassandraNodeListTables.fnClearTable();
			 com.impetus.ankush.placeAjaxCall(nodeListUrl, "GET", false, null, function(result) {
					if(result.output.status == true){
						if((result.output.nodes == undefined) || (result.output.nodes == null))
							return;
						for(var j = 0 ; j < result.output.nodes.length ; j++){
							var nodeListUrl = '<span><a href="'+baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+result.output.nodes[j].host+'" class="fa fa-chevron-right"></a></span>'
							cassandraNodeListTables.fnAddData([
							                           	result.output.nodes[j].host,
							                           	result.output.nodes[j].roles,
							                           	nodeListUrl
							                           ]);
						}
					}else{
						cassandraNodeListTables.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						cassandraNodeListTables.fnClearTable();
					}
				
				
			});
		},
				
};
