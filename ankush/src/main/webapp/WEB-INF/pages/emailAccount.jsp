<!------------------------------------------------------------------------------
-  ===========================================================
-  Ankush : Big Data Cluster Management Solution
-  ===========================================================
-  
-  (C) Copyright 2014, by Impetus Technologies
-  
-  This is free software; you can redistribute it and/or modify it under
-  the terms of the GNU Lesser General Public License (LGPL v3) as
-  published by the Free Software Foundation;
-  
-  This software is distributed in the hope that it will be useful, but
-  WITHOUT ANY WARRANTY; without even the implied warranty of
-  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-  See the GNU Lesser General Public License for more details.
-  
-  You should have received a copy of the GNU Lesser General Public License 
-  along with this software; if not, write to the Free Software Foundation, 
- Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->

<!-- Email Account Section on Common Configuration page -->

<div id="emailAccountDiv" class="mrgt5">
<div class="row">
		<div class="col-md-3" ><label class="form-label section-heading">
Email Account
</label></div>
	</div>
	<div class="row">
		<div class="col-md-2 text-right">
			<label class="form-label">Mail Server:</label>
		</div>
		<div class="col-md-10 col-lg-2 form-group text-left">
			<input type="text" data-toggle="tooltip"
				data-original-title="Please type your SMTP server name."
				title="Please type your SMTP server name."
				class="input-large form-control" data-placement="right"
				placeholder="Email Server" id="emailServer"></input>
		</div>
	</div>
	<div class="row">
		<div class="col-md-2 text-right"><label class="form-label">Mail Server Port:</label></div>
		<div class="col-md-10 col-lg-2 form-group text-left"><input data-placement="right" type="text" data-toggle="tooltip" class="input-large form-control" id="emailServerPort" placeholder="Email Server Port" title="The port number that is used for SMTP. (default is 25.)"></input></div>
		</div>
		<div class="row">
		<div class="col-md-2 text-right"><label class="form-label">Email:</label></div>
		<div class="col-md-10 col-lg-2 text-left form-group"><input data-placement="right"  data-toggle="tooltip" class="input-large form-control" type="text" placeholder="Email Account" title="email address" id="emailAccount"></input></div>	
	</div>
	<div class="row">
		<div class="col-md-2 text-right "><label class="form-label">Username:</label></div>
		<div class="col-md-10 col-lg-2 form-group text-left"><input data-placement="right"  data-toggle="tooltip" class="input-large form-control" type="text" placeholder="Username" title="Please provide username for the account from which mail is to be sent. In case of anonymous support, please leave it blank" id="userName"></input></div>	
	</div>
	<div class="row">
		<div class="col-md-2 text-right"><label class="form-label">Account Password:</label></div>
		<div class="col-md-10 col-lg-2  form-group text-left"><input  data-placement="right" data-toggle="tooltip" class="input-large form-control" type="password" title="Account Password" placeholder="Account Password" id="accountPassword"></input></div>	
	</div>
	<div class="row">
		<div class="col-md-2 text-right"><label class="form-label">&nbsp;</label></div>
		<div class="col-md-10 col-lg-2 form-group"><input id="useSSLCheck" type="checkbox" style="margin-top:-1px" data-placement="right" title="Please check if SSL is required for the port?"></input>
		<span style="font-size:14px;font-family:Franklin Gothic Medium','Arial Narrow Bold',Arial,sans-serif; padding-left:5px;"> Use SSL</span>
		</div>	
	</div>
	<div class="row">
	<div class="col-md-2 text-right"><label class="form-label">Email Verification:</label></div>
		<div class="col-md-10 col-lg-2 text-left form-group mrgt5" id="verificationText">
		</div>	
	</div>
	<div class="row">
	<div class="col-md-2 text-right"><label class="form-label">&nbsp;</label></div>
		<div class="col-md-10 col-lg-2 text-left form-group mrgt5">
			<button onclick="com.impetus.ankush.configurationNew.openTestMailDialog();" data-loading-text="Applying..." id="" class="btn btn-default">Send Test Mail</button>
		</div>	
	</div>
</div>	
