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
<!-- Hadoop Cluster Advanced Settings Details Page during Cluster Monitoring  -->

<div class="section-header">
	<div class="row-fluid mrgt20">
	  	<div class="span4"><h2 class="heading text-left">Hadoop Advance Settings</h2></div>
		<!-- <div class="span2 text-center"><button class="btn btnAlert"  id="errorBtn" style="margin-right:8px;display:none;">Error</button></div> -->
		<div class="span8 text-right minhgt0">
		  	<button class="btn mrgr10" style="display:none;" onclick="">Revert</button>
		  	<button class="btn" style="display:none;"onclick="">Apply</button>
		</div>
	</div>
</div>
<div class="section-body">

<div class="container-fluid mrgnlft8">
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">DFS Replication:</label></div>
		<div class="span10"><label class="text-left data" id="hadoopMonitoringAdvancedSettings_dfsReplication"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label" >Vendor:</label></div>
		<div class="span10"><label class="text-left data" id="hadoopMonitoringAdvancedSettings_vendor"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Version:</label></div>
		<div class="span10"><label class="text-left data" id="hadoopMonitoringAdvancedSettings_version"></label></div>
	</div>
	
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Source:</label>
		</div>
		<div class="span10 ">
			<div class="btn-group" data-toggle="buttons-radio"
				id="SourceModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
				<button class="btn active btnGrp" data-value="0"
					id="hadoopRadioSourceDownload" disabled="disabled">Download</button>
				<button class="btn btnGrp" data-value="1"
					id="hadoopRadioSourceLocal" disabled="disabled">Local Path</button>
			</div>
		</div>
	</div>

	<div class="row-fluid">
	
	<div class="span2">&nbsp;</div>
		<div class="span10">
			<label class="text-left data" id="hadoopSourcePathView"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Name Node Path:</label></div>
		<div class="span10">
			<label class="text-left data" id="hadoopMonitoringAdvancedSettings_nameNodePath"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Data Node Path:</label></div>
		<div class="span10">
			<label class="text-left data" id="hadoopMonitoringAdvancedSettings_dataNodePath"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Mapred Temp Path:</label></div>
		<div class="span10">
			<label class="text-left data" id="hadoopMonitoringAdvancedSettings_mapredTempPath"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2"><label class="text-right form-label">Hadoop Temp Path:</label></div>
		<div class="span10">
			<label class="text-left data" id="hadoopMonitoringAdvancedSettings_hadoopTempPath"></label>
		</div>
	</div>

	<div class="row-fluid">
			<div class="span2"><label class="text-right form-label">Installation Path:</label></div>
		<div class="span10">
				<label class="text-left data" id="hadoopMonitoringAdvancedSettings_installationPath"></label>
		</div>
	</div>
	<div class="row-fluid">
	
	<div class="span2">&nbsp;</div>
		<div class="span10">
			<label  class="checkbox inline">
				<input type="checkbox" id="hadoopMonitoringAdvancedSettings_includeS3SupportHadoop"><span style="display: block; margin-top: 4px;">Include S3 Support</span>
			</label>
		</div>
	</div>
	<div id="s3Div" style="display: none;">
		<div class="row-fluid">
				<div class="span2"><label class="text-right form-label">Acess Key:</label></div>
			<div class="span10">
					<label class="text-left data" id="hadoopMonitoringAdvancedSettings_acessKeyS3"></label>
			</div>
		</div>
		<div class="row-fluid">
				<div class="span2"><label class="text-right form-label">Secret Key:</label></div>
			<div class="span10">
				<label class="text-left data" id="hadoopMonitoringAdvancedSettings_secretKeyS3"></label>
			</div>
		</div>
	</div>
	<div class="row-fluid">
	<div class="span2">&nbsp;</div>
		<div class="span10">
			<label  class="checkbox inline">
				<input type="checkbox" id="hadoopMonitoringAdvancedSettings_includeS3nSupportHadoop"><span style="display: block; margin-top: 4px;">Include S3n Support</span>
			</label>
		</div>
	</div>
	<div id="s3nDiv" style="display: none;">
		<div class="row-fluid">
				<div class="span2"><label class="text-right form-label">Acess Key:</label></div>
			<div class="span10">
					<label class="text-left data" id="hadoopMonitoringAdvancedSettings_acessKeyS3n"></label>
			</div>
		</div>
		<div class="row-fluid">
				<div class="span2"><label class="text-right form-label">Secret Key:</label></div>
			<div class="span10">
				<label class="text-left data" id="hadoopMonitoringAdvancedSettings_secretKeyS3n"></label>
			</div>
		</div>
	</div>
	
	</div>
</div>
