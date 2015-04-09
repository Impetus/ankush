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
com.impetus.ankush.columnFamilyDrilldown = {
		//this function will populate columnfamily drill down page and related action
		columnFamilyDrillDownPopulate : function(keyspaceName,columnFamilyName){
			var url = null;
            if(clusterTechnology == 'Hybrid'){
            	url = baseUrl + '/monitor/'+clusterId+'/columnfamilydetails?keyspace='+keyspaceName+'&columnfamily='+columnFamilyName+'&component='+hybridTechnology;
            }
            else
            	url = baseUrl + '/monitor/'+clusterId+'/columnfamilydetails?keyspace='+keyspaceName+'&columnfamily='+columnFamilyName;
            $("#showLoading").removeClass('element-hide');
            com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result){
            	$("#showLoading").addClass('element-hide');
	            if(columnFamiliesDrillDownTable != null)
					columnFamiliesDrillDownTable.fnClearTable();
						if(result.output.status == true){
							if((result.output.ColumnFamilyDetails == undefined) || (result.output.ColumnFamilyDetails == null)){
								columnFamiliesDrillDownTable.fnSettings().oLanguage.sEmptyTable = "No data available.";
								columnFamiliesDrillDownTable.fnClearTable();
								columnMetaData.fnSettings().oLanguage.sEmptyTable = "No data available.";
								columnMetaData.fnClearTable();
								return;
							}
							for(var key in result.output.ColumnFamilyDetails){
								if(typeof result.output.ColumnFamilyDetails[key] === 'object'){
										for(var prop in result.output.ColumnFamilyDetails[key]){
											var text = key;
											text = text.replace( /([A-Z])/g, " $1" );
											text = text.charAt(0).toUpperCase()+text.slice(1);
											var property = prop;
											property = property.replace( /([A-Z])/g, " $1" );
											property = property.charAt(0).toUpperCase()+property.slice(1);
											columnFamiliesDrillDownTable.fnAddData([
											                                        '<ins class="left"></ins><div class="left">&nbsp;&nbsp;&nbsp;&nbsp;'+text+'</div>','',
											                                        property,
											                                        result.output.ColumnFamilyDetails[key][prop]
											                                        ]);
										}
								}else{
									if(key == "columnMetadata"){
										if(result.output.ColumnFamilyDetails[key] == "")
											$("#columnMetaData").html("No Records found.");
										else
											$("#columnMetaData").html(result.output.ColumnFamilyDetails[key]);
									}
								}
							}
						}else{
							columnFamiliesDrillDownTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							columnFamiliesDrillDownTable.fnClearTable();
							columnMetaData.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							columnMetaData.fnClearTable();
						}
            },function(){
            	$("#showLoading").addClass('element-hide');
            });
		},
		
		
};
