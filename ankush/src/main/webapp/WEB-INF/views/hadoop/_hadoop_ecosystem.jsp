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
<!-- Fields related to Hadoop & its Ecosystem Components during Cluster Creation   -->
<div id="hadoop-ecosystem">
	<a name="hadoopDetails_Jump"></a>
	<div class="row-fluid">
		<div class="span12">
			<label class="form-label section-heading text-left">Hadoop
				Details</label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">DFS Replication:</label>
		</div>
		<div class="span10">
			<input type="text" id="hadoopReplicationFactor" style="width: 100px"
				placeholder="DFS Replication" data-toggle="tooltip"
				data-placement="right"
				title="This is the Replication Factor for each block. It can be updated later. The default value at is set to 3" value="3"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Vendor:</label>
		</div>
		<div class="span10">
			<select id="hadoopVendor"
				onchange="com.impetus.ankush.hadoopCluster.fillHadoopVersion();"
				data-toggle="tooltip" data-placement="right"
				title="Select the Vendor for Hadoop Installation. Ecosystems are dependent on this selection"></select>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Version:</label>
		</div>
		<div class="span10">
			<select id="hadoopVersion"
				onchange="com.impetus.ankush.hadoopCluster.hadoopVersionChange();"
				data-toggle="tooltip" data-placement="right"
				title=" Select the Version for Hadoop Installation. Ecosystems are dependent on this selection"></select>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span5">
			<a name="hadoopEcosystem_Jump"></a> <a href="##"
				id="hadoopAdvanceSettings"
				onclick="com.impetus.ankush.hadoopCluster.hadoopAdvanceSettings();">
				<div class="span8">
					<label class="text-right form-label">Advanced Settings</label>
				</div>
				<div class="text-left">
					<img style="margin-top: 15px; margin-left: 15px;"
						src="<c:out value='${baseUrl}' />/public/images/icon-chevron-right.png">
				</div>
			</a>
		</div>
	</div>
	<div class="row-fluid">
		<div class="row-fluid">
			<div class="span2">
				<label class="form-label section-heading text-left">Hadoop
					Ecosystem</label>
			</div>
			<div class="text-right">
				<input type="text" id="ecoSystemTableSearchBox" placeholder="Search" />
			</div>
		</div>
	</div>
	<div class="row-fluid" id="ecoSystemTable">
		<div class="span12">
			<table class="table table-striped"
				id="hadoopCreate_hadoopEcoSystemTable"
				style="border: 1px solid; border-color: #CCCCCC" width="100%">
				<thead style="text-align: left;">
					<tr>
						<th><input type='checkbox' id='nodeCheckHead_Ecosystem'
							name="nodeCheckHead_Ecosystem"
							onclick="com.impetus.ankush.hadoopCluster.checkAllNode_Ecosystem(this)"></th>
						<th style="width: 160px;">Component</th>
						<th>Vendor</th>
						<th>Version</th>
						<th>Status</th>
						<th></th>
					</tr>
				</thead>
				<tbody style="text-align: left;">
				</tbody>
			</table>
		</div>
	</div>
</div>
