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
				$('#indexIdElasticSearch').tooltip();
				$('#shardsElasticSearch').tooltip();
				$('#replicasElasticSearch').tooltip();
			});
</script>

<!-- header section -->
<div class="section-header">
	<div class="row-fluid headermargin">
		<div class="span7">
			<h2 class="heading text-left left">Index Create</h2>
			<button class="btn-error header_errmsg"
				id="validateErrorIndexCreate"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;padding:0 15px; left:15px; position:relative"></button>
		</div>

		<div class="span5 text-right">
			<button id="" class="btn headerright-setting" onclick="com.impetus.ankush.removeCurrentChild()">Cancel</button>
			<button class="btn" id="createIndexBtn" data-loading-text="Creating..." onclick="com.impetus.ankush.elasticSearchMonitoring.createIndex();">Create</button>
		</div>
	</div>
</div>
<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="errorDivIndexCreate" class="span12 errorDiv"
				style="display: none;"></div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Index ID:</label>
			</div>
			<div class="span10">
				<input type="text" id="indexIdElasticSearch" class="input-large"
					placeholder="Index Id" title="Enter ElasticSearch Index Id"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label"># Shards:</label>
			</div>
			<div class="span10">
				<input type="text" id="shardsElasticSearch" class="input-small	"
					placeholder="No of Shards"
					title="Enter ElasticSearch Shards" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label"># Replicas:</label>
			</div>
			<div class="span10">
				<input type="text" id="replicasElasticSearch" class="input-small"
					placeholder="No of Replicas" title="Enter ElasticSearch Replicas"
					data-placement="right">
			</div>
		</div>
		<div id="div_create_index_dialog" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Index Create</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Index create request placed successfully.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn" onclick="com.impetus.ankush.removeCurrentChild();">Ok</a>
		</div>
	</div>	
	</div>
</div>
