<!------------------------------------------------------------------------------
-===========================================================
-Ankush : Big Data Cluster Management Solution
-===========================================================
-
-(C) Copyright 2014, by Impetus Technologies
-
-This is free software; you can redistribute it and/or modify it under
-the terms of the GNU Lesser General Public License (LGPL v3) as
-published by the Free Software Foundation;
-
-This software is distributed in the hope that it will be useful, but
-WITHOUT ANY WARRANTY; without even the implied warranty of
-MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-See the GNU Lesser General Public License for more details.
-
-You should have received a copy of the GNU Lesser General Public License 
-along with this software; if not, write to the Free Software Foundation, 
-Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->
<script>
function autoRefreshIndexDrillDownPage(){
	var indexName = $('#indexNameElasticSearch').text();
	var obj1 = {};
	var obj2 = {};
	var autoRefreshArray = [];
	obj1.varName = 'is_autoRefresh_indexDrillDownTilesTable'; 
	obj1.callFunction = "com.impetus.ankush.elasticSearchMonitoring.populateTileTables('index','indexdetail', \""+indexName+"\",'indexInfo');";
	obj1.time = 30000;
	obj2.varName = 'is_autoRefresh_indexTiles'; 
	obj2.callFunction = "com.impetus.ankush.elasticSearchMonitoring.createIndexTiles(\""+indexName+"\");";
	obj2.time = 30000;
	autoRefreshArray.push(obj1);
	autoRefreshArray.push(obj2);
	com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
$(document).ready(function() {
	//$(window).unbind('resize');
	$('#tilesindexDrillDownFamilies').masonry({ 
		itemSelector : '.item',
		columnWidth : 100,
		isAnimated : true
	});
	$("#indexLinks")
	.append(
			' <div class="row-fluid"><div class="span8 text-left"><h4 class="section-heading" ><a href="#" onclick="com.impetus.ankush.elasticSearchMonitoring.load_shard_page()">Shards&nbsp;&nbsp;&nbsp;<img src="'+
                    baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
	$("#indexLinks")
	.append(
			' <div class="row-fluid"><div class="span8 text-left"><h4 class="section-heading" ><a href="#" onclick="com.impetus.ankush.elasticSearchMonitoring.load_alias_page()">Aliases&nbsp;&nbsp;&nbsp;<img src="'+
                    baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
	$("#indexLinks")
	.append(
			' <div class="row-fluid"><div class="span8 text-left"><h4 class="section-heading" ><a href="#" onclick="com.impetus.ankush.elasticSearchMonitoring.load_administration_page()">Administration&nbsp;&nbsp;&nbsp;<img src="'+
                    baseUrl+'/public/images/icon-chevron-right.png" /></a></h4></div></div>');
	var action = 'Create Index...';
	$("#actionsDropDownIndexCreate")
			.append(
					' <li><a tabindex="-1" href="#" class="text-left" onclick="com.impetus.ankush.elasticSearchMonitoring.createAliasPageLoad()">Create Alias...</a></li>');
	$('.dropdown-toggle').dropdown();
	setTimeout(function(){autoRefreshIndexDrillDownPage();},3000);
	
});
</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span6">
			<h2 class="heading text-left" id="indexNameElasticSearch"></h2>
		</div>
		<div class="span6 text-right">
			<div class="btn-group">
				<a class="btn dropdown-toggle hgt25" data-toggle="dropdown" href="#">Actions <span class="caret"></span></a>
				<ul class="dropdown-menu pull-right" id="actionsDropDownIndexCreate">
				</ul>
			</div>
		</div>
</div>
</div>
<div class="section-body">
<div class="container-fluid">
<div class="row-fluid">
			<div id="error-div-KeyspaceDrillDown" class="span12 error-div-hadoop" style="display: none;">
				<span id="popover-content-cassandraParameters" style="color: red;"></span>
			</div>
		</div>
<div class="row-fluid">
<div class="row-fluid">
			<div class="masonry mrgt10" id="tilesindexDrillDownFamilies"></div>
		</div>
	<%@ include file="indexTileTables.jsp"%>		
</div>
<div id="indexLinks"></div>
</div>
</div>
