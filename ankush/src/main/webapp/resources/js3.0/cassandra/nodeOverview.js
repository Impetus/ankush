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
com.impetus.ankush.nodeOverview = {
		//this will create a table at node drilldown page
		nodeOverviewPopulate : function(){
			var nodeOverviewUrl = baseUrl+'/monitor/'+clusterId+'/nodeoverview?component=Cassandra&host='+hostName;
			 com.impetus.ankush.placeAjaxCall(nodeOverviewUrl, "GET", true, null, function(result) {
					if(nodeOverviewTable != null)
						nodeOverviewTable.fnClearTable();
					if(result.output.status == true){
						if(result.output.nodeData == undefined){
							nodeOverviewTable.fnSettings().oLanguage.sEmptyTable = "No data Available.";
							nodeOverviewTable.fnClearTable();
							return;
						}
						for(var key in result.output.nodeData){
							var firstColumn = key;
							firstColumn = firstColumn.replace( /([A-Z])/g, " $1" );
							firstColumn = firstColumn.charAt(0).toUpperCase()+firstColumn.slice(1);
							var secondColumn = result.output.nodeData[key];
							var thirdColumn = '';
							if(typeof result.output.nodeData[key] === 'object'){
								secondColumn = '';
								for(var prop in result.output.nodeData[key]){
									var text = prop;
									text = text.replace( /([A-Z])/g, " $1" );
									text = text.charAt(0).toUpperCase()+text.slice(1);
									secondColumn += '<div style="float:left;text-wrap:suppress;" class="col-md-4">'+text+'&nbsp;&nbsp;&nbsp;&nbsp;</div><div style="float:left" class="col-md-4">'+result.output.nodeData[key][prop]+'</div><div style="clear:both"/>';
								}
								if(typeof result.output.nodeData[key].length === 'number'){
									continue;
								}
							}
							if(key == 'Token Count'){
								thirdColumn = '<a href="#"><img src="'
									+ baseUrl
									+ '/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.cassandraMonitoring.loadTokenList(\''+result.output.nodeData['tokens']+'\');"/></a>';
							}
							nodeOverviewTable.fnAddData([
							                            firstColumn,
							                            secondColumn,
							                            thirdColumn
							                           ]);
						}
					}else{
						nodeOverviewTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						nodeOverviewTable.fnClearTable();
					}
			});
		},
		loadTokenList : function(tokenList){
			/*$('#content-panel').sectionSlider('addChildPanel', {
	            url : baseUrl + '/cassandraMonitoring/tokenList',
	            method : 'get',
	            title : 'Token List',
	            tooltipTitle : com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp,
	            previousCallBack : "com.impetus.ankush.commonMonitoring.removeChildPreviousNodeUtilizationTrendPageLoad();",
	            callback : function() {
	            	$('#tokenListPreTag').append(tokenList);
	            },
	            callbackData : {
	            }
	        });*/
		},
};
