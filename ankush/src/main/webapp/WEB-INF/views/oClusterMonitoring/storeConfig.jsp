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
<!-- Page for store configurations -->
<%@ include file="../layout/blankheader.jsp"%>
</head>
<body style="background: none;">
	<div class="section-header">
		<div class="row-fluid">
			<div class="span4">
				<h3>Store Configuration</h3>
		</div>
	</div></div>
	<div class="section-body">
	<div class="container-fluid  mrgnlft8">
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
				<label class=" form-label label-black" id="storeLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Datacenter1:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="dataCenterLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label form-label">Topology:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="topologyLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Replication Factor:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="repFactorLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Partitions:</label>
			</div>
			<div class="span10 ">
				<label class="form-label label-black" id="partitionLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Registry Port:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="regPortLable" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">HA Port Range:</label>
			</div>
			<div class="span1">
				<label class=" form-label label-black" id="haPortRange" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Base Directory:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="baseDirLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Installation Path:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="installationDirLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">Data Path:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="dataDirLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class=" text-right form-label">NTP Server:</label>
			</div>
			<div class="span10">
				<label class=" form-label label-black" id="ntpLabel" ></label>
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
				<label class=" form-label label-black" id="userNameLabel" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class=" text-right form-label">Password:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="passwordLabel" ></label>
			</div>
		</div>
		</div>
	</div>
