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
<!-- Setup detail page for oracle cluster creation -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	
	var clusterId = '<c:out value="${clusterId}"/>';
	setUpDetailNodeTable = $("#setUpDetailNodeTable").dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : false,
		"bFilter" : true,
		"bSort" : true,
		"bInfo" : false,
		"bAutoWidth" : false,
		"aoColumnDefs": [
		                 { 'sType': "ip-address", 'aTargets': [0] },
		           
		                  { 'bSortable': false, 'aTargets': [1,2,3] }
		               ],
	});
	$("#setUpDetailNodeTable_filter").css({
		'text-align' : 'right'
	});
	$("#setUpDetailNodeTable_filter")
			.prepend(
					'<div style="float:left;margin-top:15px;" id="setupOracleDatatable"></div>');
	$("#setupOracleDatatable").append(
			"<h4 class='section-heading'>Node List</h4>");
	$('#example_filter').css('text-align', 'right');
</script>
</head>
<body style="background: none;">
	<div class="section-header" style="">
		<div class="row-fluid">
			<div class="span4">
				<h3>Setup Details</h3>
			</div>

		</div>
	</div>
	<div class="section-body">
		<div class="row-fluid">
			<div class="span4">
				<h4 class="section-heading" style="text-align: left;">General
					Details</h4>
			</div>
		</div>
			
		<div class="row-fluid">
			<div class="span2  ">
				<label class="text-right form-label">Store:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="storeLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Datacenter:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="dataCenterLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label form-label">Topology:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="topologyLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Replication Factor:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="repFactorLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Partitions:</label>
			</div>
			<div class="span10 ">
				<label class="form-label" id="partitionLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Registry Port:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="regPortLable"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">HA Port Range:</label>
			</div>
			<div class="span1">
				<label class=" form-label" id="haPort1Label"></label>
			</div>
			<div class="span1">
				<label class=" form-label" id="haPort2Label"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Base Directory:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="baseDirLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Installation Path:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="installationDirLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Data Path:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="dataDirLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class=" span2 ">
				<label class="text-right form-label">DB Package Path:</label>
			</div>
			<div class="span3">
				<select id="selectDBPackageLabel"
					style="cursor: default;"
					data-placement="right" disabled="disabled"></select>
			</div>
			<div class="span5">
				<input id="" type="button" value="Upload DB Package" class="btn"
					style="margin-top: 10px;cursor: default;" disabled="disabled"
					data-placement="right"></input>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">NTP Server:</label>
			</div>
			<div class="span10">
				<label class=" form-label" id="ntpLabel"></label>
			</div>
		</div>
		<br />
		<div class="row-fluid">
			<div class="span4">
				<h4 class="section-heading" style="text-align: left;">Node
					Authentication</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class=" text-right form-label">Username:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="userNameLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class=" text-right form-label">Password:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="passwordLabel"></label>
			</div>
		</div>
		<br />
		<div class="row-fluid">
			<div class="span4 ">
				<h4 class="section-heading" style="text-align: left;">Search
					and Select Nodes</h4>
			</div>
		</div>
		<div class="row-fluid text-right radioDiv">
				<div class="span2"><input type="radio" disabled="disabled" name="authentication" id="throughPasswordLabel" value="0"/><span>&nbsp;&nbsp;IP Range</span></div>
				<div class="span4"><input type="radio" disabled="disabled" id="throughSharedKeyLabel" value="1" name="authentication"/><span>&nbsp;&nbsp;File Upload</span></div>	
			</div>
		
		
		<div class="row-fluid" id="ipRangeLabelDiv" style="display: none;">
			<div class="span2">
				<label class=" text-right form-label" >IP Range:</label>
			</div>
			<div class="span10">
				<label class=" form-label" id="ipRangeLabel"></label>
			</div>
		</div>
		<div class="row-fluid" id="filePathLabel" style="display: none;">
			<div class="span2">
				<label class=" text-right form-label">File Path:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label" id="filePathLabel"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2"></div>
			<div class="span10 ">
				<button class="align-left btn btn-large"
					onclick="com.impetus.ankush.oClusterSetup.getNodes();"
					style="color: #848584;cursor: default;" disabled="disabled">Retrieve</button>
			</div>
		</div>
		<br />
		<!-- <div class="row-fluid" id="nodeListDiv">
			<div class="span4 ">
				<h4 class="section-heading" class="section-heading"
					style="text-align: left;">Node List</h4>
			</div>
		</div> -->

		<div class="row-fluid">
			<div class="span12 ">
				<table class="table table-striped" id="setUpDetailNodeTable"
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
						<%-- <tr class="odd gradeX" style="border-bottom: 2px solid #c4c4c4">
						<td>192.168.145.180</td>
						<td>Admin</td>
						<td>Deploying...</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
					<tr class="odd gradeX" style="border-bottom: 2px solid #c4c4c4">
						<td>192.168.145.180</td>
						<td>Storage</td>
						<td>Copying Bundle file...</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
					<tr class="odd gradeX">
						<td>192.168.145.180</td>
						<td>Storage</td>
						<td>Done</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr> --%>
					</tbody>
				</table>
			</div>
		</div>
	</div>
