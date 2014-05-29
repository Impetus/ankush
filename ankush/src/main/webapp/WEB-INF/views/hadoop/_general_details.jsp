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
<!-- Fields related to General Cluster Details (Name, Description & Environment) during Cluster Creation   -->
<div id="generalDetails">
	<div class="row-fluid">
		<div class="span3 text-left">
			<label class="form-label section-heading">General Details</label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Cluster:</label>
		</div>
		<div class="span10 text-left">
			<input type="text" class="input-large" id="hadoop_cluster_name"
				placeholder="Cluster Name" data-toggle="tooltip"
				data-placement="right" title="Provide a unique alphanumeric name to the Cluster"></input>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Description:</label>
		</div>
		<div class="span10 text-left">
			<input type="text" class="input-large" id="hadoop_cluster_desc"
				placeholder="Description" data-toggle="tooltip"
				data-placement="right" title="Describe the Cluster "></input>
		</div>
	</div>
	
	<div class="row-fluid" style="display: none;">
			<div class="span2">
				<label class="text-right form-label">Environment:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="environmentTypeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="In Premise"
						id="localEnv"
						onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('localEnv','envDivs');">In
					Premise</button>
					<button class="btn btnGrp" data-value="Cloud" id="cloudEnv"
						onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('cloudEnv','envDivs');">Cloud</button>
				</div>
			</div>
		</div>
	
	<br />
</div>
