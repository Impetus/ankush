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
<!-- Fields related to Node Authentication / User Details during Cluster Creation   -->
<div id="nodeAuthentication">
	<div class="row-fluid">
		<div class="span12">
			<label class="form-label section-heading">Node Authentication</label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Username:</label>
		</div>
		<div class="span10">
			<input type="text" class="input-large" id="usernameHadoop"
				placeholder="Username" data-toggle="tooltip" data-placement="right"
				title="A common username for authenticating nodes. User must have administrative rights on the nodes"></input>
		</div>
	</div>
<!-- 	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Authentication Type:</label>
		</div>
		<div class="span10 text-left" style="margin-top: 5px;">
			<label class="radio inline text-left" style="padding-top: 0px;">
				<input type="radio" name="authentication" id="throughPassword"
				value="password" checked="checked"
				style="vertical-align: middle; float: none;"
				onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('throughPassword','passwordDivs');" />
				<span>&nbsp;&nbsp;Password</span>
			</label> <label class="radio inline text-left"
				style="margin-left: 20px; padding-top: 0px;"> <input
				type="radio" id="throughSharedKey" value="sharedKey"
				name="authentication" style="vertical-align: middle; float: none;"
				onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('throughSharedKey','passwordDivs');" /><span>&nbsp;&nbsp;Shared
					Key</span>
			</label>
		</div>
	</div> -->
	
	<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Authentication Type:</label>
			</div>
			<div class="span10 ">
				<div class="btn-group" data-toggle="buttons-radio"
					id="authTypeGroupBtn" style="margin-top: 8px; margin-bottom: 15px">
					<button class="btn active btnGrp" data-value="password"
						id="throughPassword"
						onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('throughPassword','passwordDivs');">Password</button>
					<button class="btn btnGrp" data-value="sharedKey" id="throughSharedKey"
						onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('throughSharedKey','passwordDivs');">Shared Key</button>
				</div>
			</div>
		</div>
	
	<div class="row-fluid passwordDivs" id="throughPassword-action">
		<div class="span2">
			<label class="text-right form-label">Password:</label>
		</div>
		<div class="span10">
			<input type="password" class="input-large" placeholder="Password"
				id="passwordHadoop" data-toggle="tooltip" data-placement="right"
				title="The password to authenticate the nodes"></input>
		</div>
	</div>
	<div class="row-fluid passwordDivs" id="throughSharedKey-action"
		style="display: none;">
		<div class="span2">
			<label class="text-right form-label">File Upload: </label>
		</div>
		<div class="span10">
			<iframe style="width: 1px; height: 1px; border: 0px;"
				id="uploadframesharekeyDb" name="uploadframesharekeyDb"
				style="float:left;"></iframe>
			<form action="" id="uploadframesharekeyDbPackage" name="uploadframesharekeyDbPackage"
				target="uploadframesharekeyDb" enctype="multipart/form-data"
				method="post" style="float: left; margin: 0px;">
				<input id='fileBrowseDb' type='file' class='' style="visibility : hidden;float:right"
					name='file'></input><input type="text" id="filePathDb"
					data-toggle="tooltip" placeholder="Upload File" title="File Path"
					data-placement="right" readonly="readonly" style="cursor: pointer;background:white"
					onclick="com.impetus.ankush.hadoopCluster.hadoopSharedKeyUpload();" class="input-large"></input>
			</form>
		</div>
	</div>
</div>
