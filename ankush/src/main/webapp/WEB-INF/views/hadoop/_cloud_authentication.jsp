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
<!-- Fields related to Cloud Service Provider Authentication during Cluster Creation   -->
<div id="cloudEnv-action" class="envDivs" style="display:none;">
		<div class="row-fluid">
			<div class="span3 text-left">
				<label class="form-label section-heading">Cloud Authentication
				 </label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Service Provider:</label>
			</div>
			<div class="span10 text-left">
				<select id="cloud_service_provider" data-toggle="tooltip" data-placement="right" title="A Cloud service Provider">
				<option>Amazon EC2</option>
				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Username:</label>
			</div>
			<div class="span10 text-left">
				<input type="text" class="input-large" id="cloudUsername"
					placeholder="Username" data-toggle="tooltip" data-placement="right" title="Username for Cloud Service Provider"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Password:</label>
			</div>
			<div class="span10 text-left">
				<input type="password" class="input-large" id="cloudPassword" title="Cloud Password"
					placeholder="Password" data-toggle="tooltip" data-placement="right" title="Password to authenticate Cloud Service Provider"></input>
			</div>
		</div>
		<div class="row-fluid" style="display:none">
			<div class="span2">
				<label class="text-right form-label"></label>
			</div>
			<div class="span10 text-left">
				<div class="span2 radioDiv"><input type="checkbox" id="cloudRememberDetails" checked="checked" /><span>&nbsp;&nbsp;Remember details</span></div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label"></label>
			</div>
			<div class="span10 text-left">
				<button class="btn" id="btnGetCloudDetails_HadoopCreate" onclick="com.impetus.ankush.hadoopCluster.getCloudDetails();">Get Cloud Details</button>
			</div>
		</div>
<br/>		
</div>		
