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
<!-- Fields related to Node Selection during In Premise Cluster Creation   -->
<div id="localEnv-action" class="envDivs">
	<a name="hadoopNodeList" />
		<div class="row-fluid">
	
			<div class="span3">
				<label class="form-label section-heading text-left">Search and Select Nodes</label>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Mode :</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="ipModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn nodeListRadio active btnGrp" data-value="Range"
						id="ipRange"
						onclick="com.impetus.ankush.hadoopCluster.divShowOnClickIPAddress('div_IPRange');">IP
						Range</button>
					<button class="btn nodeListRadio btnGrp" data-value="Upload File" id="ipFile"
						onclick="com.impetus.ankush.hadoopCluster.divShowOnClickIPAddress('div_IPFileUpload');">File
						Upload</button>
				</div>
			</div>
		</div>
		<div class="row-fluid" id="div_IPRange">
			<div class="span2">
				<label class="text-right form-label" >IP Range:</label>
			</div>
			<div class="span10">
				<input type="text" class="input-large" id="ipRangeHadoop"
					placeholder="IP Range" value="" data-toggle="tooltip" data-placement="right" 
					title="IPs for a node or set of nodes. E.g. 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;cluster-test"></input>
			</div>
		</div>
		<div class="row-fluid" id="div_IPFileUpload" style="display:none;">
		<div class="span2">
				<label class="text-right form-label">File Upload: </label>
			</div>
			<div class="span10">
				<iframe style="width: 1px; height: 1px; border: 0px;"
					id="uploadframeIPAddressFile" name="uploadframeIPAddressFile"
					style="float:left;"></iframe>
				<form action="" id="uploadframeIPAddressFile_Form"
					target="uploadframeIPAddressFile" enctype="multipart/form-data"
					method="post" style="float: left; margin: 0px;">
					<input id='fileBrowse_IPAddressFile' type='file' style="visibility : hidden;float:right"
						class='' name='file'></input><input type="text" id="filePath_IPAddressFile"
						data-toggle="tooltip" placeholder="Upload File" title="A file that contains a node or set of nodes specified in the form of 192.168.100-101.10-50 or 192.168.100,105.10,20 or 192.168.10.2,5;cluster-test. Each unique pattern / IP should be in a new line."
						data-placement="right" readonly="readonly" style="cursor: pointer;background:white"
						onclick="com.impetus.ankush.hadoopCluster.hadoopIPAddressFileUpload();" class="input-large"></input>
				</form>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Rack Mapping: </label>
			</div>
			<div class="span10 text-left">
			<div id="divRackMapping" class="input-prepend">
				<span class="add-on" style="float:left;height:28px;margin-top:5px">
					<input type="checkbox"  class="inputSelect" id="chkBxRackEnable"
					onclick="com.impetus.ankush.hadoopCluster.toggleRackMapping();">
				</span>
				<iframe style="width: 1px; height: 1px; border: 0px;"
					id="uploadframeRackFile" name="uploadframeRackFile"
					style="float:left;"></iframe>
				<form action="" id="uploadframeRackFile_Form"
					target="uploadframeRackFile" enctype="multipart/form-data"
					method="post" style="margin: 0px;">
				<input id='fileBrowse_RackFile' type='file' style="visibility : hidden;float:right"
					class='' name='file'></input><input type="text" id="filePath_RackFile" disabled="disabled"
					data-toggle="tooltip" placeholder="Upload File" title="File with Rack & IP Address Mapping"
					data-placement="right" readonly="readonly" style="cursor:default;"
					onclick="com.impetus.ankush.hadoopCluster.hadoopRackFileUpload();"></input>		
				</form>
			</div>
        	
			</div>
		</div>
			
		<div class="row-fluid">
		<div class="span2">&nbsp;</div>
		
			<div class="span10 text-left">
				<button id="nodeRetrieveHadoop" class="btn" onclick="com.impetus.ankush.hadoopCluster.getNodes();">Retrieve</button>
			</div>
		</div>
		
		<div class="row-fluid" id="divNodeTableHadoop" style="display: none;">
		
		<div class="span12">
			<div class="text-left" style="padding-top: 20px;float:left">
				<label class='section-heading text-left left'>Node List</label>
				<button type="button" class="btn left mrgl10 mrgtn5" id="inspectNodeBtn" data-loading-text="Inspecting Nodes..." onclick="com.impetus.ankush.hadoopCluster.inspectNodesObject('inspectNodeBtn');">Inspect Nodes</button>
			</div>
			<div class="span7 form-image text-right btn-group" id="HadoopCreate_NodeIPTable" data-toggle="buttons-radio">
    			<button type="button" id="btnAll_HSN" class="btn active" onclick="com.impetus.ankush.hadoopCluster.toggleDatatable('All');">All</button>
    			<button type="button" id="btnSelected_HSN" class="btn" onclick="com.impetus.ankush.hadoopCluster.toggleDatatable('Selected');">Selected</button>
    			<button type="button" id="btnAvailable_HSN" class="btn" onclick="com.impetus.ankush.hadoopCluster.toggleDatatable('Available');">Available</button>
    			<button type="button" id="btnError_HSN" class="btn" onclick="com.impetus.ankush.hadoopCluster.toggleDatatable('Error');">Error</button>
    		</div>
			<div class="text-right">
				<input id="searchNodeTableHadoop" type="text" placeholder="Search" style="width: 200px;">
			</div>
		</div>
		
		
		<div class="row-fluid">
			<div class="span12 text-left">

				<table class="table table-border" id="hadoopCreate_nodeIpTableHadoop" width= "100%">
					<thead class="text-center">
						<tr>
							<th><input type='checkbox' id='nodeCheckHead'
								name="nodeCheckHead"
								onclick="com.impetus.ankush.hadoopCluster.checkAllNode(this)"></th>
							<th>IP</th>
							<th>NameNode</th>
							<th>Sec NameNode</th>
							<th>DataNode</th>
							<th>DataCenter</th>
							<th>Rack</th>
							<th>OS</th>
							<th></th>
						</tr>
					</thead>
					<tbody style="text-align: left;">

					</tbody>
				</table>
			</div>
		</div>
		</div>
</div>
