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
						com.impetus.ankush.keyspaceDrilldown.tileCreateColumnFamilies();
					}else{
						columnFamiliesTables.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						columnFamiliesTables.fnClearTable();
					}
			},function(){
				$("#showLoading").addClass('element-hide');
			});
		},
		//this function will populate tiles in keyspace drilldown page
		tileCreateColumnFamilies : function(){
			var clusterTiles = {};
			clusterTiles = com.impetus.ankush.tileReordring(columnFamilyDetailData.output.tiles);
			$('#tilesColumnFamilies').empty();
			var tile = '';
			tile = document.getElementById('tilesColumnFamilies');
			for(var i = 0 ; i < clusterTiles.length ; i++){
				switch (clusterTiles[i].status) {
				case 'Error':
					tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="redTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Critical':
					tile.innerHTML += '<div class="item grid-1c2text errorbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="redTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Warning':
					tile.innerHTML += '<div class="item grid-1c2text warningbox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="yellowTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle"><span>'+clusterTiles[i].line2+'</span><br/><span>'+clusterTiles[i].line3+'</span></div></div></div>';
					break;
				case 'Normal':
					tile.innerHTML += '<div class="item grid-1c2text infobox">'+
					'<div class="tile-1c2text thumbnail">'+
					'<div class="greenTitle">'+
					'<div class="clip tile-innerdiv" data-original-title="'+clusterTiles[i].line1+'" data-placement="bottom">'+clusterTiles[i].line1+'</div></div>'+
					'<div class="descTitle">'+clusterTiles[i].line2+'</div></div></div>';
					break;
				}
				
			}
			var tileId = '';
			tileId = 'tilesColumnFamilies';
			$('.clip').tooltip();
			$('#'+tileId).masonry({ itemSelector : '.item',
				columnWidth : 100 });
	},
};
