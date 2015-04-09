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
com.impetus.ankush.hybridMonitoring_common = {
	//this function will show tiles on elastic search monitoring drill down page	
	createTiles : function(technology){
		var currentClusterId = clusterId;
		if(undefined == currentClusterId)
			return;
		var tileUrl = baseUrl + '/monitor/' + currentClusterId + '/technologyTiles?component='+technology;
		var clusterTiles = {};
		com.impetus.ankush.placeAjaxCall(tileUrl, "GET", true, null, function(tileData){
			//this object variable will set css passing by refrence
			var tileVar = {
					'leftCss' : 0,
					'topCss' : 0,
					'lineCounter' : 1,
					'tyleType' : 'bigTile',
			}; 
			if(tileData.output.status == false){
				return ;
			}
			else{
				if((tileData.output.tiles == undefined) || (tileData.output.tiles == null) || (tileData.output.tiles.length == 0))
					return;
				clusterTiles = com.impetus.ankush.tileReordring(tileData.output.tiles);
			}
			//this var will set whether tile is 2*2 or 1*2
			var tileFlag = false;
			for(var j = 0 ; j < clusterTiles.length ; j++){
				if(clusterTiles[j].tileType == 'big_text'){
					tileFlag = true;
					break;
				}
			}
			$('#allTilesCluster'+technology).empty();
			var tile = document.getElementById('allTilesCluster'+technology);
			for(var i = 0 ; i < clusterTiles.length ; i++){
				if((tileVar.leftCss+200) > ($('#allTilesCluster'+technology).width())){
					tileVar.leftCss = 0;
					tileVar.topCss = tileVar.lineCounter*200;
					tileVar.lineCounter++;
				}
				// && ((clusterTiles[i].tileType == 'small_text') && (clusterTiles[i].tileType == undefined))
				if((i == 0) && ((clusterTiles[i].tileType == 'small_text') || (clusterTiles[i].tileType == undefined)))
					tileVar.tyleType = 'smallTileOdd';
				com.impetus.ankush.createTyleUsingType(tileVar,i,clusterTiles,tile,'common-id-for-tile');
			}
			if(tileFlag == false){
				$('#allTilesCluster'+technology).masonry('destroy');
				$('#allTilesCluster'+technology).masonry({ 
					itemSelector : '.item',
					columnWidth : 100,
					isAnimated : true
				});
			}else
				setTimeout(function(){$('#allTilesCluster'+technology).css('height',(tileVar.lineCounter)*200+'px');},50);
			$('.clip').tooltip();
		});
	}
};
