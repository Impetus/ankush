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
<!-- Fields showing Hadoop Cluster Node Authentication Details on Cluster Configuration Page  -->

<div id="configNodeAuthentication">
	<div class="row-fluid">
		<div class="span3 text-left">
			<label class="form-label section-heading">Node Authentication</label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 text-right">
			<label class="form-label text-right">Username:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="config_clusterUserName"></label>
		</div>
	</div>
	<!-- <div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label">Authentication Type:</label>
		</div>
		<div class="span10 text-left">
			<label class="radio inline text-left" style="padding-top: 0px; margin-top:5px;">
				<input type="radio" name="authentication" id="config_nodeauth_radio_password" 
				disabled="disabled" value="password" style="vertical-align: middle;float: none;"/>
				<span>&nbsp;Password</span>
			</label>
			<label class="radio inline text-left form-label" style="margin-left: 20px;padding-top: 0px; margin-top:5px;">
				<input type="radio" id="config_nodeauth_radio_sharedkey" disabled="disabled"  
				value="sharedKey" name="authentication" style="vertical-align: middle;float: none;"/>
				<span>&nbsp;Shared Key</span>
			</label>
			</div>
	</div> -->

	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Authentication Type:</label>
		</div>
		<div class="span10 ">
			<div class="btn-group" data-toggle="buttons-radio"
				id="ipModeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
				<button class="btn active btnGrp" data-value="password"
					id="config_nodeauth_radio_password" disabled="disabled">Password</button>
				<button class="btn btnGrp" data-value="sharedKey"
					id="config_nodeauth_radio_sharedkey" disabled="disabled">Shared Key</button>
			</div>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label" id="config_nodeauth_authType"></label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="config_clusterPassword"></label>
		</div>
	</div>
</div>
