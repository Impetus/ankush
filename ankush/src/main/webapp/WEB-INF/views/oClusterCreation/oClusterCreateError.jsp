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
<!-- Page for oracle cluster in case of deployment Error -->
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->

<head>
<%@ include file="../layout/header.jsp"%>
<link rel="stylesheet/less" type="text/css"
	href="<c:out value='${baseUrl}' />/public/css/styles.less" />
<script src="<c:out value='${baseUrl}' />/public/libJs/less.js"
	type="text/javascript"></script>
<script
	src="<c:out value='${baseUrl}' />/public/libJs/bootstrap-editable.js"
	type="text/javascript"></script>
<link rel="stylesheet/less" type="text/css"
	href="<c:out value='${baseUrl}' />/public/css/oClusterSetup.less" />
<link rel="stylesheet" type="text/css"
	href="<c:out value='${baseUrl}' />/public/css/ankush.dialog.css" />
<%-- <script
	src="<c:out value='${baseUrl}' />/public/js/oClusterSetup.js"
	type="text/javascript"></script> --%>
<script>
	$(document).ready(function() {
		less = {
			env : "development", // or "production"
			async : false, // load imports async
			fileAsync : false, // load imports async when in a page under 
			// a file protocol
			poll : 1000, // when in watch mode, time in ms between polls
			functions : {}, // user functions, keyed by name
			dumpLineNumbers : "comments", // or "mediaQuery" or "all"
			relativeUrls : false,// whether to adjust url's to be relative
			// if false, url's are already relative to the
			// entry less file
			rootpath : ":/a.com/"// a path to add on to the start of every url 
		//resource
		};
		
		/* oTable.$('td').editable( '../examples_support/editable_ajax.php', {
		    "callback": function( sValue, y ) {
		        var aPos = oTable.fnGetPosition( this );
		        oTable.fnUpdate( sValue, aPos[0], aPos[1] );
		    },
		    "submitdata": function ( value, settings ) {
		        return {
		            "row_id": this.parentNode.getAttribute('id'),
		            "column": oTable.fnGetPosition( this )[2]
		        };
		    },
		    "height": "14px",
		    "width": "100%"
		} ); */
	});
	$('#example_filter').css('text-align', 'right');
</script>

</head>
<body style="background: none;">
	<div class="head" style="">
		<div class="row">
			<div class="span4 offset1">
				<h3 style="margin-top: 0px;">Oracle NoSQL Database v2</h3>
			</div>
			<div class="span offset ">
				<button class=" btn-large"
					style="height: 29px; padding: 0 10px; min-width: 29px; color: white; border: none; background-color: #EF3024">2
					Errors</button>
			</div>
			<div class="span offset4 ">
				<input class="btn  btn-large " type="submit" value="Delete"
					style="color: #848584">
			</div>
			<div class="span offset ">
				<input class="btn span btn-large " type="submit" value="Validate"
					style="color: #848584">
			</div>
			<div class="span offset ">
				<input class="btn span btn-large " type="submit" value="Deploy"
					style="color: #848584">
			</div>
		</div>
		<hr>
	</div>
	
		<div class="row">
			<div class="span4 offset1">
				<h4 style="text-align: left;">General Details</h4>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2 ">
				<label class="text-right form-label">Store:</label>
			</div>
			<div class="span ">
				<input type="text" value="oracle"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class="text-right form-label">Datacenter:</label>
			</div>
			<div class="span ">
				<input type="text" value="dc"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label form-label">Topology:</label>
			</div>
			<div class="span ">
				<input type="text" value="topology"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Replication Factor:</label>
			</div>
			<div class="span ">
				<input type="text" class="span1" style="width: 70px;" value="1"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Partitions:</label>
			</div>
			<div class="span ">
				<input type="text" class="span1" style="width: 70px;" value="20"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Registry Port:</label>
			</div>
			<div class="span ">
				<input type="text" style="width: 110px;" value="5000"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">HA Port Range:</label>
			</div>
			<div class="span ">
				<input type="text" class="span2" style="width: 110px;" value="5010"></input>
			</div>
			<div class="span ">
				<input type="text" class="span2" style="width: 110px;" value="5020"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Base Directory:</label>
			</div>
			<div class="span ">
				<input type="text" class="span8" style="width: 400px;" value="C:/"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Installation Path:</label>
			</div>
			<div class="span ">
				<input type="text" class="span5" style="width: 400px;"
					value="C:/KVHOME"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Data Path:</label>
			</div>
			<div class="span ">
				<input type="text" class="span5" style="width: 400px;"
					value="C:/KVROOT"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">NTP Server:</label>
			</div>
			<div class="span ">
				<input class='error' type="text" style="border: 1px solid red;"
					value="192.168.100.63" title="Invalid NTP server"></input>
			</div>
		</div>
		<br />
		<div class="row">
			<div class="span4 offset1">
				<h4 style="text-align: left;">Node Authentication</h4>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Username:</label>
			</div>
			<div class="span ">
				<input class='error' type="text"
					style="width: 200px; border: 1px solid red;"
					title="Invalid Username" value="admin"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">Password:</label>
			</div>
			<div class="span ">
				<input class='error' type="password"
					style="width: 200px; border: 1px solid red;"
					title="Invalid Password" value="*********"></input>
			</div>
		</div>
		<br />
		<div class="row">
			<div class="span4 offset1">
				<h4 style="text-align: left;">Search and Select Nodes</h4>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">IP Range:</label>
			</div>
			<div class="span ">
				<input type="text" class="span4" style="width: 300px;"></input>
			</div>
		</div>
		<div class="row">
			<div class="span2 offset2">
				<label class=" text-right form-label">File Path:</label>
			</div>
			<div class="span ">
				<input type="text" class="span5" style="width: 400px;"></input>
			</div>
		</div>
		<div class="row">
			<div class="">
				<button class="align-left btn span2 btn-large offset4"
					onclick="com.impetus.ankush.oClusterSetup.getNodes();"
					style="color: #848584">Retrieve</button>
			</div>
		</div>
		<br />
		<div class="row" id="nodeListDiv">
			<div class="span4 offset1">
				<h4 style="text-align: left;">Node List</h4>
			</div>
		</div>
	<div class="row">
		<div class="span12 offset1" style="width: 1100px;">
			<table class="table table-striped table-bordered" id=""
				style="width: 100%;">
				<thead>
					<tr>
						<th><input type='checkbox'></th>
						<th>IP</th>
						<th>Admin</th>
						<th>Capacity</th>
						<th>CPUs</th>
						<th>Memory</th>
						<th>Registry Port</th>
						<th>HA Port</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<tr class="odd gradeX" style="border-bottom: 2px solid #c4c4c4">
						<td><input type='checkbox'></td>
						<td>192.168.145.180</td>
						<td><input type='checkbox'></td>
						<td>Win 95+</td>
						<td class="center">4</td>
						<td class="center">X</td>
						<td>5000</td>
						<td>5010-5012</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
					<tr class="odd gradeX"
						style="border-bottom: 2px solid #c4c4c4; background-color: #f2dcdb;">
						<td><input type='checkbox'></td>
						<td>192.165.241.22</td>
						<td><input type='checkbox'></td>
						<td>Win 95+</td>
						<td class="center">4</td>
						<td class="center">4</td>
						<td>5000</td>
						<td>5010-5012</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
					<tr class="odd gradeX">
						<td><input type='checkbox'></td>
						<td>192.168.145.175</td>
						<td><input type='checkbox'></td>
						<td>Win 95+</td>
						<td class="center">4</td>
						<td class="center">X</td>
						<td>5000</td>
						<td>5010-5012</td>
						<td><a
							href="<c:out value='${baseUrl}'/>/oClusterCreate/oNodeDetail"><img
								src="<%=baseUrl%>/public/images/icon-chevron-right.png" /></a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
