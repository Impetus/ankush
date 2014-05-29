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
<!-- Hadoop Cluster Ecosystem components Details Page containing its configuration details during Cluster Creation  -->

<div class="section-header">
	<div  class="row-fluid mrgt20">
	  	<div class="span4"><h2 class="heading text-left">${componentName}</h2></div>
	</div>
</div>
<div class="section-body">

<div class="container-fluid mrgnlft8">
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Vendor:</label></div>
		<div class="span10"><label class="data text-left" id="vendor-${componentName}"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Version:</label></div>
		<div class="span10"><label class="data text-left" id="version-${componentName}"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Status:</label></div>
		<div class="span10" style="padding-top: 12px;"><label><span class="text-data" id="certified-${componentName}"></span></label></div>
	</div>
	<%-- <div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Source:</label></div>
		
		<div class="span10" style="padding-top: 8px;">
			<label class="radio inline" style="padding-top: 0px;vertical-align: middle;">
				<input type="radio" id="downloadRadio-${componentName}" name="componentPathRadio-${componentName}" checked="checked" 
				style="float: none;" value="0" disabled="disabled"><span class="text-data" >&nbsp;&nbsp;Download</span>
			</label>
			<label class="radio inline" style="padding-top: 0px;vertical-align: middle;">
				<input type="radio" id="localPathRadio-${componentName}" 
				name="componentPathRadio-${componentName}" style="float: none;margin-left: 12px;"  
				value="1" disabled="disabled"><span class="text-data">&nbsp;&nbsp;Local Path</span>
			</label>
		</div>
	</div> --%>
	
	<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Source:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="sourceTypeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="0"
						id="downloadRadio-${componentName}" disabled="disabled">Download</button>
					<button class="btn btnGrp" data-value="1" id="localPathRadio-<c:out value='${componentName}'/>" disabled="disabled">Local Path</button>
				</div>
			</div>
	</div>
	
	<div class="row-fluid">
	<div class="span2">	&nbsp;</div>
		<div class="span10"><label class="text-data" class="input-large" data-placement="left" id="path-${componentName}"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Installation Path:</label></div>
		<div class="span10"><label class="text-data" class="input-large" data-placement="left" id="installationPath-${componentName}"></label></div>
	</div>
	<div class="component-Hive" class="advanceComponents" style="display: none;">
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Hive Server:</label></div>
			<div class="span10"><label class="data text-left" id="hiveServerHadoop"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Connection Password:</label></div>
			<div class="span10"><label class="data text-left" id="hiveConnectionPassword"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Connection URL:</label></div>
			<div class="span10"><label class="data text-left" id="hiveConnectionURL"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Connection Driver Name:</label></div>
			<div class="span10"><label class="data text-left" id="hiveConnectionDriverName"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Connection User Name:</label></div>
			<div class="span10"><label class="data text-left" id="hiveConnectionUserName"  class="input-large"></label></div>
		</div>
	</div>
	<div class="component-Zookeeper" class="advanceComponents" style="display: none;">
	
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Zookeeper Nodes:</label></div>
			<div class="span10"><label class="data text-left" id="zookeeperNodes"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Client Port:</label></div>
			<div class="span10"><label class="data text-left" id="zookeeperClientPort"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Data Dir:</label></div>
			<div class="span10"><label class="data text-left" id="zookeeperDataDir"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Sync Limit:</label></div>
			<div class="span10"><label class="data text-left" id="zookeeperSyncLimit"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Init Limit:</label></div>
			<div class="span10"><label class="data text-left" id="zookeeperInitLimit"  class="input-large"></label></div>
		</div>
				<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Tick Time:</label></div>
			<div class="span10"><label class="data text-left" id="zookeeperTickTime"  class="input-large"></label></div>
		</div>
	</div>
	<div class="component-Hbase" class="advanceComponents" style="display: none;">
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Hbase Master:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseMasterHadoop"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Region Servers:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseRegionServersHadoop"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Compaction Threshold:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseCompactionThreshold"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Cache Size:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseCacheSize"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">File Size:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseFilesize"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Caching:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseCaching"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Timeout:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseTimeout"  class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Multiplier:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseMultiplier" class="input-large"></label></div>
		</div>
		
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Major Compaction:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseMajorcompaction" class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Max Size:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseMaxsize" class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Flush Size:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseFlushSize" class="input-large"></label></div>
		</div>
		<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Handler Count:</label></div>
			<div class="span10"><label class="data text-left" id="hbaseHandlerCount" class="input-large"></label></div>
		</div>
	</div>
	
	</div>
</div>	
