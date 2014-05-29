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
<style>
 button {
    width: 120px;
    height: 25px ! important;
}
</style>
<script>
$(document).ready(function() {
	
});
</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h2 class="heading text-left left" id="indexNameElasticSearchAdministration"></h2>
			<button class="btn-error header_errmsg"
				id="validateErrorAdministration"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none;padding:0 15px; left:15px; position:relative"></button>
		</div>
</div>
</div>
<div class="section-body">
<div class="container-fluid">
<div class="row-fluid">
			<div class="row-fluid">
			<div id="errorDivAdministration" class="span12 errorDiv"
				style="display: none;"></div>
		</div>
		</div>
		
			<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="aliasElasticSearchTable"
							width="100%" style="border: 1px solid; border-color: #CCCCCC">
							<thead style="text-align: left;display:none">
								<tr>
									<th></th>
									<th></th>
									
								</tr>
							</thead>
							<tbody style="text-align: left;">
								<tr>
									<td><button class="btn mrgr10"
				onclick="com.impetus.ankush.elasticSearchMonitoring.administrationAction('flush');">Flush Index</button></td>
									<td>The flush process of an index frees memory from the index by flushing data to the index storage and clearing the internal transaction log. By default, ElasticSearch uses memory heuristics in order to automatically trigger flush operations as required in order to clear memory.</td>
								</tr>
								<tr>
									<td><button class="btn mrgr10"
				onclick="com.impetus.ankush.elasticSearchMonitoring.administrationAction('clearcache');">Clear Cache</button></td>
									<td>Clears the cache on all indices.</td>
								</tr>
								<tr>
									<td><button class="btn mrgr10"
				onclick="com.impetus.ankush.elasticSearchMonitoring.administrationAction('optimize');">Optimize Index</button></td>
									<td>The optimize process basically optimizes the index for faster search operations (and relates to the number of segments a lucene index holds within each shard). the optimize operation allows to reduce the number of segments by merging them.</td>
								</tr>
								<tr>
									<td><button class="btn mrgr10"
				onclick="com.impetus.ankush.elasticSearchMonitoring.administrationAction('refresh');">Refresh Index</button></td>
									<td>Refresh the index, making all operations performed since the last refresh available for search. The (near) real-time capabilities depend on the index engine used. For example, the internal one requires refresh to be called, but by default a refresh is scheduled periodically.</td>
								</tr>
								<!-- <tr>
									<td><button class="btn mrgr10"
				onclick="com.impetus.ankush.elasticSearchMonitoring.administrationAction('close');">Close Index</button></td>
									<td>the open and close index commands allow to close an index, and later on opening it. A closed index has almost no overhead on the cluster (except for maintaining its metadata), and is blocked for read/write operations. A closed index can be opened which will then go tdrough the normal recovery process.</td>
								</tr> -->
								<tr>
									<td><button class="btn mrgr10"
				onclick="com.impetus.ankush.elasticSearchMonitoring.administrationAction('delete',true);">Delete Index</button></td>
									<td><strong>Delete index will destroy this index and all documents associated with this index.This action cannot be undone.</strong></td>
								</tr>
							</tbody>
						</table>
					</div>	
				
</div>
<div id="div_RequestSuccess_administrationAction" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Administration Action</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;" id="administrationDialogText">
					Action update request has been placed successfully.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" id="administrationOperations" class="btn">Ok</a>
		</div>
	</div>	
</div>
<div id="div_RequestSuccess_administrationDelete" class="modal hide fade" style="display: none;">
		<div class="modal-header text-center">
			<h4>Administration Action</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					Do you want to delete Index ?</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn">Cancel</a>
			<a href="#" data-dismiss="modal" class="btn" onclick="com.impetus.ankush.elasticSearchMonitoring.administrationAction('delete');">Confirm</a>
		</div>
	</div>	
</div>
</div>
</div>
