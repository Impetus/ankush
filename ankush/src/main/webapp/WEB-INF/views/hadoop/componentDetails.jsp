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
<!-- Fields related to Component Deployment Details during Cluster Creation   -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
$(document).ready(function(){
	com.impetus.ankush.hadoopCluster.tooltipComponentAdvSettings("<c:out value='${componentName}'/>");
});
</script>
<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h2 class="heading text-left left">
				<c:out value='${componentName}' />
			</h2>
			
			<button id="errorBtnHadoop-ComponentAdvSettings"
				class="btn-error"
				onclick="com.impetus.ankush.hadoopCluster.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>
		
		<!--  
		<div class="span3 minhgt0">
			<button id="errorBtnHadoop-ComponentAdvSettings"
				class="span3 btn-error"
				onclick="com.impetus.ankush.hadoopCluster.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		-->
		<div class="span5 text-right">
			<button class="btn" style="margin-right: 8px;"
				onclick="com.impetus.ankush.hadoopCluster.revertComponent();"
				id="revertAdv-<c:out value='${componentName}'/>">Revert</button>
			<button class="btn" style="margin-right: 20px;"
				id="applyAdv-<c:out value='${componentName}'/>"
				onclick="com.impetus.ankush.hadoopCluster.applyComponent('<c:out value='${componentName}'/>');">Apply</button>
		</div>
	</div>
</div>
<div class="section-body content-body mrgnlft8">
<div class="container-fluid">
	<div class="row-fluid">
		<div id="error-div-hadoop-ComponentAdvSettings"
			class="span12 error-div-hadoop" style="display: none;">
			<span id="popover-content-ComponentAdvSettings" style="color: red;"></span>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Vendor:</label>
		</div>
		<div class="span10">
			<select id="vendor-<c:out value='${componentName}'/>"
				data-toggle="tooltip" data-placement="right"
				title="Select <c:out value='${componentName}'/> Vendor"
				onchange="com.impetus.ankush.hadoopCluster.componentInternalVendorChange('<c:out value='${componentName}'/>');"></select>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Version:</label>
		</div>
		<div class="span10">
			<select id="version-<c:out value='${componentName}'/>"
				data-toggle="tooltip" data-placement="right"
				title="Select <c:out value='${componentName}'/> Version"
				onchange="com.impetus.ankush.hadoopCluster.componentInternalVersionChange('<c:out value='${componentName}'/>');"></select>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Status:</label>
		</div>
		<div class="span10" style="padding-top: 12px;">
			<label><span class="text-data"
				id="certified-<c:out value='${componentName}'/>">Certified</span></label>
		</div>
	</div>
	
	<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Source:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="sourceTypeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="0"
						id="downloadRadio-<c:out value='${componentName}'/>"
						onclick="com.impetus.ankush.hadoopCluster.downloadUrl('<c:out value='${componentName}'/>');">Download</button>
					<button class="btn btnGrp" data-value="1" id="localPathRadio-<c:out value='${componentName}'/>"
						onclick="com.impetus.ankush.hadoopCluster.localUrl('<c:out value='${componentName}'/>');">Local Path</button>
				</div>
			</div>
	</div>
	
	<div class="row-fluid">
	
	<div class="span2">	&nbsp;</div>
		<div class="span10">
			<input type="text" data-toggle="tooltip" style="width: 300px;"
				title="Download URL / Local Path for <c:out value='${componentName}'/> Tarball"
				data-placement="right" id="path-<c:out value='${componentName}'/>"
				placeholder="Download URL/Local Path">
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Installation Path:</label>
		</div>
		<div class="span10">
			<input type="text" data-toggle="tooltip" style="width: 300px;"
				title="Location of Installation Directory for <c:out value='${componentName}'/>"
				data-placement="right" placeholder="Installation Path"
				id="installationPath-<c:out value='${componentName}'/>">
		</div>
	</div>
	<div class="component-Hive" class="advanceComponents"
		style="display: none;">
		<div id="divNodes-Hive" style="display: none;">
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Hive Server:</label>
				</div>
				<div class="span10">
					<select id="hiveServerHadoop" name="hiveServerSelect"
						data-toggle="tooltip" data-placement="right"
						title="Select a node for Hive Server"></select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Connection Driver Name:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 300px;"
					title="JDBC Connection Driver"
					data-placement="right" placeholder="Connection Driver Name"
					id="connectionDriverName">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Connection URL:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 300px;"
					title="JDBC Connection String"
					data-placement="right" placeholder="Connection URL"
					id="connectionURL">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Connection User Name:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" class="input-large"
					title="JDBC user credentials"
					data-placement="right" placeholder="Connection User Name"
					id="connectionUserName">
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Connection Password:</label>
			</div>
			<div class="span10">
				<input type="password" data-toggle="tooltip" class="input-large"
					title="Password for connecting to Hive Metastore"
					data-placement="right" placeholder="Connection Password"
					id="connectionPassword"> <input type="text"
					data-toggle="tooltip" class="input-large"
					title="JDBC authentication Password"
					style="display: none;" data-placement="right"
					placeholder="Connection Password" id="connectionPassword_PlainText">
			</div>
		</div>
		
	</div>
	<div class="component-Zookeeper" class="advanceComponents"
		style="display: none;">
		<div id="divNodes-Zookeeper" style="display: none;">
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Zookeeper Nodes:</label>
				</div>
				<div class="span10">
					<select class="multiSelect" id="zookeeperNodes"
						data-toggle="tooltip" data-placement="right"
						title="List of Zookeeper Nodes"
						data-bind="multiselect: true, options: Options, selectedOptions: SelectedOptions, optionsValue: $data"
						multiple="multiple"></select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Tick Time:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
					title="The basic time unit in milliseconds used by ZooKeeper. It is used to do heartbeats and the minimum session timeout will be twice the tickTime."
					data-placement="right" placeholder="Tick Time" id="tickTime">
				<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">milliseconds</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Client Port:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px"
					title="The port to listen for client connections"
					data-placement="right" placeholder="Client Port" id="clientPort">
			</div>
		</div>
<!-- 		<div class="row-fluid"> -->
<!-- 			<div class=" span2 "> -->
<!-- 				<label class="text-right form-label">Jmx Port:</label> -->
<!-- 			</div> -->
<!-- 			<div class="span10"> -->
<!-- 				<input id="jmxPort_Zookeeper" type="text" data-toggle="tooltip" -->
<!-- 					class="input-mini" placeholder="Zookeeper Jmx Port" -->
<!-- 					title="Enter Zookeeper Jmx port" data-placement="right"></input> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Data Dir:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 300px;"
					title="The location to store the in-memory database snapshots and, unless specified otherwise, the transaction log of updates to the database."
					data-placement="right" placeholder="Data Dir" id="dataDir">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Sync Limit:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
					title="Time (in ticks) that limits how far out of date a server can be from a leader"
					data-placement="right" placeholder="Sync Limit" id="syncLimit">
				<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">milliseconds</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Init Limit:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
					title="Time (in ticks) that limit the length of time the ZooKeeper servers in quorum have to connect to a leader"
					data-placement="right" placeholder="Init Limit" id="initLimit">
				<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">milliseconds</label>
			</div>
		</div>
	</div>
	<div class="component-Hbase" class="advanceComponents"
		style="display: none;">
		<div id="divNodes-Hbase" style="display: none;">
			<div class="row-fluid" style="display: none;">
				<div class="span2">
					<label class="text-right form-label">Hbase Master:</label>
				</div>
				<div class="span10">
					<select id="hbaseMasterHadoop" name="hbaseMasterSelect"
						data-placement="right" data-toggle="tooltip"
						title="Select a node for HBase Master"></select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span2">
					<label class="text-right form-label">Region Servers:</label>
				</div>
				<div class="span10">
					<select class="multiSelect" id="regionServersHadoop"
						data-placement="right" data-toggle="tooltip"
						title="Select nodes for HRegion Servers"
						data-bind="multiselect: true, options: Options, selectedOptions: SelectedOptions, optionsValue: $data"
						multiple="multiple"></select>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">File Size:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
					title="Maximum HStoreFile size. If any one of a column families' HStoreFiles has grown to exceed this value, the hosting HRegion is split in two. Default: 10G" data-placement="right"
					placeholder="File Size" id="filesize">
					<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">bytes</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Compaction Threshold:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px"
					title="Minimum number of StoreFiles per Store to be selected for a compaction to occur"
					data-placement="right" placeholder="Compaction Threshold"
					id="compactionThreshold">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Cache Size:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
					title="Percentage of maximum heap to allocate to block cache. Default of 0.25 means allocate 25%. Set to 0 to disable but it's not recommended."
					data-placement="right" placeholder="Cache Size" id="cacheSize">
					<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">%</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Caching:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px"
					title="Number of rows that will be fetched when calling next on a scanner if it is not served from (local, client) memory."
					data-placement="right" placeholder="Caching" id="caching">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Timeout:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
					title="ZooKeeper session timeout. HBase passes this to the zk quorum as suggested maximum time for a session"
					data-placement="right" placeholder="Timeout" id="timeout">
					<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">milliseconds</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Multiplier:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px"
					title="Useful in preventing runaway memstore during spikes in update traffic. Block updates if memstore has hbase.hregion.block.memstore time hbase.hregion.flush.size bytes."
					data-placement="right" placeholder="Multiplier" id="multiplier">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Major Compaction:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float:left;"
					title="The time (in miliseconds) between 'major' compactions of all HStoreFiles in a region."
					data-placement="right" placeholder="Major Compaction"
					id="majorcompaction">
					<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">milliseconds</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Max Size:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float: left;"
					title="The maximum combined value in a multi-level block index beyond which the block is written out and a new block is started"
					data-placement="right" placeholder="Max Size" id="maxsize">
					<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">bytes</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Flush Size:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px;float: left;"
					title="The Memstore size in Bytes beyond which it is flushed to disk"
					data-placement="right" placeholder="Flush Size" id="flushSize">
					<label style="margin-left: 10px;float:left" class="text-left tooltiptext form-label">bytes</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Handler Count:</label>
			</div>
			<div class="span10">
				<input type="text" data-toggle="tooltip" style="width: 100px"
					title="Count of RPC Listener instances spun up on RegionServers"
					data-placement="right" placeholder="Handler Count"
					id="handlerCount">
			</div>
		</div>
	</div>
	</div>
</div>
