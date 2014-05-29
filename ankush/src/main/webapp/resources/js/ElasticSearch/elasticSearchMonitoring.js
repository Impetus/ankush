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

com.impetus.ankush.elasticSearchMonitoring = {
	// this function will populate indices table on elastic search monitoring
	// page and hybrid individual elastic monitoring page.
	indicesListTable : function() {
		var indicesListUrl = null;
		if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			indicesListUrl = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indices?technology=ElasticSearch';
		else
			indicesListUrl = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indices';
		$
				.ajax({
					'type' : 'GET',
					'url' : indicesListUrl,
					'contentType' : 'application/json',
					'async' : true,
					'dataType' : 'json',
					'success' : function(result) {
						if (indicesListTable != null)
							indicesListTable.fnClearTable();
						if (result.output.status == false) {
							indicesListTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							indicesListTable.fnClearTable();
							return;
						}
						if (result.output.status == true) {
							if ((result.output.indices == null)
									|| (result.output.indices.length == 0)) {
								indicesListTable.fnSettings().oLanguage.sEmptyTable = 'No Records Found';
								indicesListTable.fnClearTable();
								return;
							}
							for ( var i = 0; i < result.output.indices.length; i++) {
								var navigationStormTopology = '<a href="#" onclick="com.impetus.ankush.elasticSearchMonitoring.indexDrillDown(\''
										+ result.output.indices[i].index
										+ '\');"><img id="navigationImgTopologyDrillDown'
										+ i
										+ '" src="'
										+ baseUrl
										+ '/public/images/icon-chevron-right.png"/></a>';
								indicesListTable.fnAddData([
										result.output.indices[i].index,
										result.output.indices[i].noOfDocs,
										result.output.indices[i].primarySize,
										result.output.indices[i].noOfShards,
										result.output.indices[i].noOfReplicas,
										result.output.indices[i].status,
										navigationStormTopology ]);
							}

						}
					},
					'error' : function() {
					}
				});
	},
	// this function will load index drilldown page
	indexDrillDown : function(indexName) {
		$('#content-panel')
				.sectionSlider(
						'addChildPanel',
						{
							url : baseUrl
									+ '/elasticSearchMonitoring/indexDrillDown',
							method : 'get',
							title : '',
							tooltipTitle : com.impetus.ankush.commonMonitoring.clusterName,
							previousCallBack : "com.impetus.ankush.elasticSearchMonitoring.removeIndexDrilldown()",
							callback : function() {
								com.impetus.ankush.elasticSearchMonitoring
										.populateTileTables('index',
												'indexdetail', indexName,
												'indexInfo');
								com.impetus.ankush.elasticSearchMonitoring
										.createIndexTiles(indexName);
								$('#indexNameElasticSearch').text(indexName);
							},
							callbackData : {}
						});
	},
	// this function will populate tiletable for index drill down and node drill
	// down
	populateTileTables : function(param, paramString, paramVal, keyInfo) {
		// this hashmap will decide which table will be populated
		// param(ip or index)
		// paramString is a word for backend call in case of index it is
		// indexDetail
		// paramVal(ipValue or index name)
		// keyInfo is key just within data.output in each call whether ip or
		// index
		var hashmap = {
			'ip' : {
				'process' : 'processDetailTable',
				'os' : 'osListTable',
				'threadPool' : 'threadPoolTable',
				'fs' : 'fileSystemTable',
				'indices' : 'indicesNodeTable',
				'jvm' : 'jvmTable',
				'network' : 'networkTable'
			},
			"index" : {
				'indexingTotal' : 'indexingTotalTable',
				'searchTotal' : 'searchTotalTable',
				'mergeActivity' : 'mergeActivityTable',
				'documents' : 'documentsTable',
				'getTotal' : 'getTotalTable',
				'operations' : 'operationsTable',
				'health' : 'indexingHealthTable'
			}
		};
		var url = null;
		if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			url = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId + '/'
					+ paramString + '?' + param + '=' + paramVal
					+ '&technology='
					+ com.impetus.ankush.commonMonitoring.hybridTechnology;
		else
			url = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId + '/'
					+ paramString + '?' + param + '=' + paramVal;
		com.impetus.ankush
				.placeAjaxCall(
						url,
						'GET',
						true,
						null,
						function(result) {
							// this loop will clear all tables
							for ( var caseKey in hashmap[param]) {
								if (eval(hashmap[param][caseKey]) != null)
									eval(hashmap[param][caseKey])
											.fnClearTable();
							}
							if (result.output.status == false) {
								// this loop will populate error after removing
								// loading text
								for ( var caseKey in hashmap[param]) {
									eval(hashmap[param][caseKey]).fnSettings().oLanguage.sEmptyTable = result.output.error[0];
									eval(hashmap[param][caseKey])
											.fnClearTable();
								}
								return;
							}
							if (result.output.status == true) {
								// through this loop all tables of elastic
								// search on nodedrilldownpage will be populated
								for ( var caseKey in result.output[keyInfo]) {
									if ((result.output[keyInfo][caseKey] == null)
											|| (Object
													.keys(result.output[keyInfo][caseKey]).length == 0))
										continue;
									for ( var key in result.output[keyInfo][caseKey]) {
										eval(hashmap[param][caseKey])
												.fnAddData(
														[
																key,
																result.output[keyInfo][caseKey][key] ]);
									}
								}
							}
						});
	},
	// this function will create tiles on index drill down page
	createIndexTiles : function(indexName) {
		var tileUrl = null;
		if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			tileUrl = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indextiles?index=' + indexName + '&technology='
					+ com.impetus.ankush.commonMonitoring.hybridTechnology;
		else
			tileUrl = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indextiles?index=' + indexName;
		com.impetus.ankush
					.placeAjaxCall(
						tileUrl,
						"GET",
						true,
						null,
						function(tileData) {
							var clusterTiles = {};
							if (tileData.output.status == false)
								return;
							else {
								clusterTiles = com.impetus.ankush
										.tileReordring(tileData.output.tiles);
							}
							// this var will set whether tile is 2*2 or 1*2
							var tileFlag = false;
							for ( var j = 0; j < clusterTiles.length; j++) {
								if (clusterTiles[j].tileType == 'big_text') {
									tileFlag = true;
									break;
								}
							}
							$('#tilesindexDrillDownFamilies').empty();
							var tileVar = {
								'leftCss' : 0,
								'topCss' : 0,
								'lineCounter' : 1,
								'tyleType' : 'bigTile',
							};
							var tile = document
									.getElementById('tilesindexDrillDownFamilies');
							for ( var i = 0; i < clusterTiles.length; i++) {
								if ((tileVar.leftCss + 200) > ($('#tilesindexDrillDownFamilies')
										.width())) {
									tileVar.leftCss = 0;
									tileVar.topCss = tileVar.lineCounter * 200;
									tileVar.lineCounter++;
								}
								if ((i == 0)
										&& ((clusterTiles[i].tileType == 'small_text') || (clusterTiles[i].tileType == undefined)))
									tileVar.tyleType = 'smallTileOdd';
								com.impetus.ankush.createTyleUsingType(tileVar,
										i, clusterTiles, tile,
										'node-id-for-tile');
							}
							if (tileFlag == false) {
								$('#tilesindexDrillDownFamilies').masonry(
										'destroy');
								$('#tilesindexDrillDownFamilies').masonry({
									itemSelector : '.item',
									columnWidth : 100,
									isAnimated : true
								});
							} else
								setTimeout(
										function() {
											$('#tilesNodes_NDD').css(
													'height',
													(tileVar.lineCounter) * 200
															+ 'px');
										}, 50);
							$('.clip').tooltip();
						});
	},
	// this function will load shard page
	load_shard_page : function() {
		$('#content-panel').sectionSlider(
				'addChildPanel',
				{
					url : baseUrl + '/elasticSearchMonitoring/shards',
					method : 'get',
					title : 'shards',
					tooltipTitle : 'index detail',
					previousCallBack : "com.impetus.ankush.elasticSearchMonitoring.removeCreateAliasPage()",
					callback : function() {
						com.impetus.ankush.elasticSearchMonitoring
								.populateShardsTable();
						$('#indexNameElasticSearchShards').text(
								$('#indexNameElasticSearch').text()+'/Shards');
					},
					callbackData : {}
				});
	},
	// this function will populate shard table
	populateShardsTable : function() {
		var indexName = $('#indexNameElasticSearch').text();
		var shardListUrl = null;
		if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			shardListUrl = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indexshards?index=' + indexName + '&technology='
					+ com.impetus.ankush.commonMonitoring.hybridTechnology;
		else
			shardListUrl = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indexshards?index=' + indexName;
		if (shardsElasticSearchTable != null)
			shardsElasticSearchTable.fnClearTable();
		$
				.ajax({
					'type' : 'GET',
					'url' : shardListUrl,
					'contentType' : 'application/json',
					'async' : true,
					'dataType' : 'json',
					'success' : function(result) {
						if (result.output.status == true) {
							for ( var i = 0; i < result.output.shards.length; i++) {
								shardsElasticSearchTable.fnAddData([
										result.output.shards[i].State,
										result.output.shards[i].Node,
										result.output.shards[i]['Primary?'],
										result.output.shards[i]['# Docs'],
										result.output.shards[i].Shard,
										result.output.shards[i].Size, ]);
							}
						} else {
							shardsElasticSearchTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							shardsElasticSearchTable.fnClearTable();
						}
					},
					'error' : function() {
					}

				});
	},
	// this function will create Index
	createIndex : function() {
		$('#createIndexBtn').button('loading');
		$('#errorDivIndexCreate').empty().hide();
		$('#validateErrorIndexCreate').empty().hide();
		var data = {
			"index" : {
				"indexId" : $('#indexIdElasticSearch').val(),
				"shards" : $('#shardsElasticSearch').val(),
				"replicas" : $('#replicasElasticSearch').val()
			}
		};
		var flag = com.impetus.ankush.elasticSearchMonitoring
				.indexValidate(data.index);
		if (flag == true) {
			var url = null;
			if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				url = baseUrl + '/monitor/'+ com.impetus.ankush.commonMonitoring.clusterId+ '/createindex?technology='+ com.impetus.ankush.commonMonitoring.hybridTechnology;
			else
				url = baseUrl + '/monitor/'+ com.impetus.ankush.commonMonitoring.clusterId+ '/createindex';
			com.impetus.ankush.placeAjaxCall(url, 'POST', true, data, function(
					result) {
				$('#createIndexBtn').button('reset');
				if (result.output.status == true) {
					$('#div_create_index_dialog').appendTo('body').modal();
				}
				else{
					$('#errorDivIndexCreate').append("<div class='errorLineDiv'><a href='#'>1 . " + result.output.error[0] + ".</a></div>").show();
					$('#validateErrorIndexCreate').text('1 Errors').show();
				}
			});
		} else {
			$('#createIndexBtn').button('reset');
			return;
		}
		
	},
	// this function will valiadet data of create Index page
	indexValidate : function(data) {
		$('#errorDivIndexCreate').empty().hide();
		$('#validateErrorIndexCreate').empty().hide();
		var flag = true;
		var errorCount = 0;
		for ( var key in data) {
			$('#' + key + 'ElasticSearch').removeClass('error-box');
			if (!com.impetus.ankush.validate.empty(data[key])) {
				errorCount++;
				$('#errorDivIndexCreate').append(
						"<div class='errorLineDiv'><a href='#'>" + errorCount
								+ ". " + key + " can not be empty.</a></div>");
				$('#' + key + 'ElasticSearch').addClass('error-box');
			} else if (!com.impetus.ankush.validate.numeric(data[key])) {
				if (key != 'indexId') {
					errorCount++;
					$('#errorDivIndexCreate').append(
							"<div class='errorLineDiv'><a href='#'>"
									+ errorCount + ". " + key
									+ " must be numeric.</a></div>");
					$('#' + key + 'ElasticSearch').addClass('error-box');
				}
			}

		}
		if (errorCount != 0) {
			$('#validateErrorIndexCreate').show();
			$('#validateErrorIndexCreate').text(errorCount + ' Errors');
			$('#errorDivIndexCreate').show();
			flag = false;
		}
		return flag;
	},
	// this function will load alias page
	createAliasPageLoad : function() {
		$('#content-panel').sectionSlider(
				'addChildPanel',
				{
					url : baseUrl + '/elasticSearchMonitoring/createAlias',
					method : 'get',
					title : 'Create Alias',
					tooltipTitle : 'index detail',
					previousCallBack : "com.impetus.ankush.elasticSearchMonitoring.removeCreateAliasPage()",
					callback : function() {
					},
					callbackData : {}
				});
	},
	// this function will create Alias
	createAlias : function() {
		$('#createAliasBtn').button('loading');
		$('#errorDivAliasCreate').empty().hide();
		$('#validateErrorAliasCreate').empty().hide();
		var data = {
			"alias" : {
				"index" : $('#indexNameElasticSearch').text(),
				"alias" : $('#aliasElasticSearch').val(),
				"index_routing" : $('#index_routingElasticSearch').val(),
				"search_routing" : $('#search_routingElasticSearch').val()
			}
		};
		var flag = com.impetus.ankush.elasticSearchMonitoring
				.aliasValidate(data.alias);
		if (flag == true) {
			var url = null;
			if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				url = baseUrl + '/monitor/'+ com.impetus.ankush.commonMonitoring.clusterId+ '/createalias?technology='+ com.impetus.ankush.commonMonitoring.hybridTechnology;
			else
				url = baseUrl + '/monitor/'+ com.impetus.ankush.commonMonitoring.clusterId+ '/createalias';;
			com.impetus.ankush.placeAjaxCall(url, 'POST', true, data, function(
					result) {
				$('#createAliasBtn').button('reset');
				if (result.output.status == true) {
					$('#div_create_alias_dialog').appendTo('body').modal();
				}
				else{
					$('#errorDivAliasCreate').append("<div class='errorLineDiv'><a href='#'>1 . " + result.output.error[0] + ".</a></div>").show();
					$('#validateErrorAliasCreate').text('1 Errors').show();
				}
			});
		} else {
			$('#createAliasBtn').button('reset');
			return;
		}
	},
	// this function will valiadet data of create Index page
	aliasValidate : function(data) {
		$('#errorDivAliasCreate').empty().hide();
		$('#validateErrorAliasCreate').empty().hide();
		var flag = true;
		var errorCount = 0;
		for ( var key in data) {
			$('#' + key + 'ElasticSearch').removeClass('error-box');
			if (!com.impetus.ankush.validate.empty(data[key])) {
				errorCount++;
				$('#errorDivAliasCreate').append(
						"<div class='errorLineDiv'><a href='#'>" + errorCount
								+ ". " + key + " can not be empty.</a></div>");
				$('#' + key + 'ElasticSearch').addClass('error-box');
			} 
}
		if (errorCount != 0) {
			$('#validateErrorAliasCreate').show();
			$('#validateErrorAliasCreate').text(errorCount + ' Errors');
			$('#errorDivAliasCreate').show();
			flag = false;
		}
		return flag;
	},
	// this function will load alias page
	load_alias_page : function() {
		$('#content-panel').sectionSlider(
				'addChildPanel',
				{
					url : baseUrl + '/elasticSearchMonitoring/aliasList',
					method : 'get',
					title : 'Alias List',
					tooltipTitle : 'index detail',
					previousCallBack : "com.impetus.ankush.elasticSearchMonitoring.removeAliasPage()",
					callback : function() {
						com.impetus.ankush.elasticSearchMonitoring
								.populateAliasTable();
						$('#indexNameElasticSearchAlias').text(
								$('#indexNameElasticSearch').text()+'/Alias');
					},
					callbackData : {}
				});
	},
	populateAliasTable : function(){
		var indexName = $('#indexNameElasticSearch').text();
		var aliasListUrl = null;
		if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			aliasListUrl = baseUrl + '/monitor/'+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indexaliases?index=' + indexName + '&technology='
					+ com.impetus.ankush.commonMonitoring.hybridTechnology;
		else
			aliasListUrl = baseUrl + '/monitor/'
					+ com.impetus.ankush.commonMonitoring.clusterId
					+ '/indexaliases?index=' + indexName;
		if (aliasElasticSearchTable != null)
			aliasElasticSearchTable.fnClearTable();
		$
				.ajax({
					'type' : 'GET',
					'url' : aliasListUrl,
					'contentType' : 'application/json',
					'async' : true,
					'dataType' : 'json',
					'success' : function(result) {
						if (result.output.status == true) {
							if(Object.keys(result.output.aliases).length != 0){
								for ( var key in result.output.aliases) {
									var deleteAlias = '<a href="#" onclick="com.impetus.ankush.elasticSearchMonitoring.openDialog(\'' + (key) + '\');"><img id="navigationImgTopologyDrillDown'
									+ key
									+ '" src="'
									+ baseUrl
									+ '/public/images/newUI-Icons/circle-minus.png"/></a>';
									aliasElasticSearchTable.fnAddData([
											key,
											result.output.aliases[key].index_routing,
											result.output.aliases[key].search_routing,
											deleteAlias ]);
								}
							}
							else{
								aliasElasticSearchTable.fnSettings().oLanguage.sEmptyTable = "No aliases associated with this index.";
								aliasElasticSearchTable.fnClearTable();
							}
						} else {
							aliasElasticSearchTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
							aliasElasticSearchTable.fnClearTable();
						}
					},
					'error' : function() {
					}

				});
	},
	//this function will open dialog
	openDialog : function(aliasName){
		$('#div_RequestSuccess_deleteAlias').appendTo('body').modal();
		$('#deleteAliasButton').bind('click',function(){
			com.impetus.ankush.elasticSearchMonitoring.deleteAlias(aliasName);
			});
	},
	//this function will delete alias
	deleteAlias : function(aliasName){
		$('#errorDivAliasList').empty().hide();
		$('#validateErrorAliasList').empty().hide();
		var indexName = $('#indexNameElasticSearch').text();
		var url = null;
		if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			url = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/removealias?index='+indexName+'&alias='+aliasName+'&technology='+ com.impetus.ankush.commonMonitoring.hybridTechnology;
		else
			url = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/removealias?index='+indexName+'&alias='+aliasName;
		com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
			if(result.output.status == true){
				$('#deleteAliasButton').unbind('click');
				$('#div_RequestSuccess_deleteAliasConfirm').appendTo('body').modal();
			}else{
				$('#errorDivAliasList').append("<div class='errorLineDiv'><a href='#'>1. " + result.output.error[0] + ".</a></div>").show();
				$('#validateErrorAliasList').text('1 Error').show();
				$('#div_RequestSuccess_deleteAlias').modal('hide');
				$('#deleteAliasButton').unbind('click');	
			}
		});
	},
	
	// this function will load administration page
	load_administration_page : function() {
		var indexName = $('#indexNameElasticSearch').text();
		$('#content-panel').sectionSlider(
				'addChildPanel',
				{
					url : baseUrl + '/elasticSearchMonitoring/administration',
					method : 'get',
					title : 'Alias List',
					tooltipTitle : 'index detail',
					previousCallBack : "com.impetus.ankush.elasticSearchMonitoring.removeAdministrationPage()",
					callback : function() {
						$('#indexNameElasticSearchAdministration').text(
								indexName+'/Administration');
					},
					callbackData : {}
				});
	},
	administrationAction : function(action,deleteFlag){
		$('#errorDivAdministration').empty().hide();
		$('#validateErrorAdministration').empty().hide();
		var indexName = $('#indexNameElasticSearch').text();
		if(deleteFlag == true){
			$('#div_RequestSuccess_administrationDelete').appendTo('body').modal();
			return;
		}
		var url = null;
		if (com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			url = baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/administerindex?index='+indexName+'&action='+action+'&technology='+ com.impetus.ankush.commonMonitoring.hybridTechnology;
		else
			url = baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/administerindex?index='+indexName+'&action='+action;
		com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
			if(result.output.status == true){
				if(action != 'delete'){
					$('#div_RequestSuccess_administrationAction').appendTo('body').modal();
					$('#administrationDialogText').text(result.output.output);
				}else{
					$('#div_RequestSuccess_administrationAction').appendTo('body').modal();
					$('#administrationDialogText').text('Index delete request placed successfully.');
					$('#administrationOperations').bind('click',function(){
						com.impetus.ankush.removeCurrentChild();
						com.impetus.ankush.removeCurrentChild();
					});
				}
			}else{
				$('#errorDivAdministration').append("<div class='errorLineDiv'><a href='#'>1. " + result.output.error[0] + ".</a></div>").show();
				$('#validateErrorAdministration').text('1 Error').show();
			}
		});
	},
	//this function will do neccessary operation an alias page removal
	removeAliasPage : function(){
		$('#div_RequestSuccess_deleteAlias').modal('hide');
		$('#div_RequestSuccess_deleteAlias').remove();
		$('#div_RequestSuccess_deleteAliasConfirm').modal('hide');
		$('#div_RequestSuccess_deleteAliasConfirm').remove();
		var indexName = $('#indexNameElasticSearch').text();
		com.impetus.ankush.elasticSearchMonitoring.createIndexTiles(indexName);
	}, 
	//this function will do neccessary operation an administration page removal
	removeAdministrationPage : function(){
		$('#div_RequestSuccess_administrationAction').modal('hide');
		$('#div_RequestSuccess_administrationAction').remove();
		var indexName = $('#indexNameElasticSearch').text();
		com.impetus.ankush.elasticSearchMonitoring.createIndexTiles(indexName);
	},
	removeIndexDrilldown : function(){
		if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
			com.impetus.ankush.hybridMonitoring_elasticSearch.createTiles();
		com.impetus.ankush.elasticSearchMonitoring.indicesListTable();
		$('#div_create_index_dialog').modal('hide');
		$('#div_create_index_dialog').remove();
	},
	removeCreateAliasPage : function(){
		$('#div_create_alias_dialog').modal('hide');
		$('#div_create_alias_dialog').remove();
		var indexName = $('#indexNameElasticSearch').text();
		com.impetus.ankush.elasticSearchMonitoring.createIndexTiles(indexName);
	}
};
