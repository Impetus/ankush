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

<%@ include file="../layout/blankheader.jsp"%>
<script src="<c:out value='${baseUrl}'/>/public/js3.0/tooltip/cassandraClusterCreationTooltip.js" type="text/javascript"></script>
<!-- header section -->
<div class="section-header">
	<div class="row-fluid headermargin">
		<div class="span7">
			<h2 class="heading text-left left">${technology}/Configuration</h2>
			<button class="btn-error header_errmsg" id="validateErrorCassandra"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none; padding: 0 15px; left: 15px; position: relative"></button>
		</div>
		<div class="span5 text-right">
			<button id="cassandraRevertBtn" class="btn headerright-setting"
				onclick="com.impetus.ankush.register_Cassandra.cassandraConfigPopulate('error');">Revert</button>
			<button class="btn" id="cassandraApplyBtn"
				onclick="com.impetus.ankush.register_Cassandra.cassandraConfigValidate();">Apply</button>
		</div>
	</div>
</div>
<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="errorDivMainCassandra" class="span12 errorDiv"
				style="display: none;"></div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblVendor-Cassandra" class="text-right form-label">Vendor:</label>
			</div>
			<div class="span10">
				<select id="vendorDropdown" title="Select Cassandra Vendor"
					data-placement="right"
					onchange="com.impetus.ankush.register_Cassandra.vendorOnChangeCassandra();">

				</select>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label id="lblVersion-Cassandra" class="text-right form-label">Version:</label>
			</div>
			<div class="span10">
				<select id="versionDropdown" title="Select Cassandra Vendor"
					data-placement="right">
				</select>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span2">
				<label id="lblinstallationPath-Casandra"
					class="text-right form-label">Component Home:</label>
			</div>
			<div class="span10">
				<input type="text" value="" name="inputInstallionPath"
					id="componentHome" class="input-xlarge"
					placeholder="Component Home" title="Component Home"
					data-placement="right">
			</div>
		</div>
	
		<div class="row-fluid">
			<div class="span2">
				<label id="lblJMXPort" class="text-right form-label">JMX
					Port:</label>
			</div>
			<div class="span10">
				<input type="text" id="jmxPort" class="input-mini"
					placeholder="JMX Port" title="JMX Port" data-placement="right">
			</div>
		</div>

	</div>
</div>
