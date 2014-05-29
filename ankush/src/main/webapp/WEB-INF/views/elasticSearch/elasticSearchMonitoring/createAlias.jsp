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
<script type="text/javascript">
	$(document).ready(
			function() {
				$('#aliasElasticSearch').tooltip();
				$('#index_routingElasticSearch').tooltip();
				$('#search_routingElasticSearch').tooltip();
			});
</script>

<!-- header section -->
<div class="section-header">
	<div class="row-fluid headermargin">
		<div class="span7">
			<h2 class="heading text-left left">Alias Create</h2>
			<button class="btn-error header_errmsg"
				id="validateErrorAliasCreate"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;padding:0 15px; left:15px; position:relative"></button>
		</div>

		<div class="span5 text-right">
			<button id="" class="btn headerright-setting" onclick="com.impetus.ankush.removeCurrentChild()">Cancel</button>
			<button class="btn" id="createAliasBtn" data-loading-text="Creating..." onclick="com.impetus.ankush.elasticSearchMonitoring.createAlias();">Create</button>
		</div>
	</div>
</div>
<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="errorDivAliasCreate" class="span12 errorDiv"
				style="display: none;"></div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Alias Id:</label>
			</div>
			<div class="span10">
				<input type="text" id="aliasElasticSearch" class="input-large"
					placeholder="Alias Id" title="Enter Alias Id"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Index Routing:</label>
			</div>
			<div class="span10">
				<input type="text" id="index_routingElasticSearch" class="input-large	"
					placeholder="Index Routing"
					title="Enter Index Routing" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Search Routing:</label>
			</div>
			<div class="span10">
				<input type="text" id="search_routingElasticSearch" class="input-large"
					placeholder="Search Routing" title="Enter Search Routing"
					data-placement="right">
			</div>
		</div>
		<div id="div_create_alias_dialog" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Alias Create</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Alias create request placed successfully.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn" onclick="com.impetus.ankush.removeCurrentChild();">Ok</a>
		</div>
	</div>
	</div>
</div>
