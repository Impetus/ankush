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
<!-- Cluster Setup Details Page during Cluster Deployment Progress  -->

<%@ include file="../../layout/blankheader.jsp"%>
<div class="section-header" style="">
	<div  class="row-fluid mrgt20">
	  	<div class="span4"><h2 class="heading text-left">Setup Details</h2></div>
	    </div>
</div>

<div class="section-body content-body">
	<div class="row-fluid">
		<div class="span3">
			<h4 class="section-heading" style="text-align: left;">General
				Details</h4>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 text-right">
			<label class="form-label text-right">Cluster:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_ClusterName"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label">Description:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_ClusterDesc"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label">Environment:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_ClusterEnv" ></label>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="span3">
			<h4 class="section-heading" style="text-align: left;">Java</h4>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 text-right">
			<label class="form-label">Oracle Java JDK:</label>
		</div>
		
		<div class="span10 text-left">
			<div style="float: left;margin-top:6px">
				<label  class="inline"><input type="checkbox" disabled="disabled" id="chkbx_HSD_InstallJava" >&nbsp;&nbsp;Install</label>
			</div>
			<div class="span8 text-left">
				<label class="form-label text-left" id="lbl_HSD_JavaBundle"></label>
			</div>	
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label">Java Home:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_JavaHomePath"></label>
		</div>
	</div>
	
	<div id="cloudEnv-action" class="envDivs" style="display:none;">
	<div class="row-fluid">
		<div class="span3">
			<h4 class="section-heading" style="text-align: left;">Cloud Authentication</h4>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 text-right">
			<label class="form-label text-right">Service Provider:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_CA_ServiceProvider"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label">Username:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_CA_Username"></label>
		</div>
	</div>
	<div class="row-fluid" style="display: none;">
		<div class="span2  text-right">
			<label class="form-label">Password:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_CA_Password" ></label>
		</div>
	</div>
	</div>
	<div id="clouddetails" class="envDivs" Style="display:none;">
		<div class="row-fluid">
			<div class="span3">
				<h4 class="section-heading" style="text-align: left;">Cloud Details</h4>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Cluster Size:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="lbl_HSD_CA_ClusterSize"></label>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Key Pairs:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="lbl_HSD_CA_KeyPairs"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Security Group:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="lbl_HSD_CA_SecurityGroup"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Region:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="lbl_HSD_CA_Region"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Zone:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="lbl_HSD_CA_Zone"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Instance Type:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="lbl_HSD_CA_InstanceType"></label>
			</div>
		</div>
		<div class="row-fluid" style="display: none;">
			<div class="span2">
				<label class="text-right form-label">Architecture:</label>
			</div>
			<div class="span10 text-left radioDiv">
				<div class="span2"><input type="radio" name="architecture" checked="checked"/><span>&nbsp;&nbsp;32 bit</span></div>
				<div class="span2"><input type="radio" name="architecture"/><span>&nbsp;&nbsp;64 bit</span></div>	
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Machine Image:</label>
			</div>
			<div class="span10 text-left">
				<label class="data text-left" id="lbl_HSD_CA_MachineImage"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Auto-Terminate:</label>
			</div>
			<div class="span10 text-left radioDiv">
				<div class="span2"><input type="radio" id="autoTerminateYes" disabled="disabled" name="autoTerminate" checked="checked" onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('autoTerminateYes','autoTerminateDiv');"/><span>&nbsp;&nbsp;Yes</span></div>
				<div class="span2"><input type="radio" id="autoTerminateNo" disabled="disabled" name="autoTerminate" onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('autoTerminateNo','autoTerminateDiv');"/><span>&nbsp;&nbsp;No</span></div>	
			</div>
		</div>
		<div id="autoTerminateYes-action" class="autoTerminateDiv" style="display: none;">
			<div class="row-fluid ">
				<div class="span2 ">
					<label class="text-right form-label">Timeout Interval:</label>
				</div>
				<div class="span10 text-left">
						<label class="data text-left" id="lbl_HSD_CA_TimeoutInterval"></label>
				</div>
			</div>
		</div>
	</div>		

	<div class="row-fluid">
		<div class="span3 text-left">
			<h4 class="section-heading" style="text-align: left;">Node Authentication</h4>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 text-right">
			<label class="form-label text-right">Username:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_HSD_UserName"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2  text-right">
			<label class="form-label">Authentication Type:</label>
		</div>
		
		<div class="span10 text-left" style="margin-top: 10px;">
			<label class="radio inline text-left" style="padding-top: 0px;">
			<input type="radio" class="data" id="radio_HSD_Password" disabled="disabled"
			style="vertical-align: middle;float: none;"/>
			<span>&nbsp;<b>Password</b></span>
			</label>
			<label class="radio inline text-left" style="margin-left: 20px;padding-top: 0px;">
			<input type="radio" class="data" id="radio_HSD_SharedKey" disabled="disabled" 
			style="vertical-align: middle;float: none;margin-left:5px;"/>
			<span>&nbsp;&nbsp;Shared Key</span>
				
			</label>
				
		</div>

	</div>
	
	<div class="row-fluid">
		<div class="span12 ">
			<table class="table table-striped" id="setUpDetailNodeTable" width="100%"
				style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4">
				<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">
					<tr>
						<th>IP</th>
						<th>Type</th>
						<th>Status</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	
	<%@ include file="../monitoring_hadoop/_hadoopMonitoring_hadoop_details.jsp"%>
		
	<%@ include file="../monitoring_hadoop/_config_hadoop_ecosystem.jsp"%>
</div>


