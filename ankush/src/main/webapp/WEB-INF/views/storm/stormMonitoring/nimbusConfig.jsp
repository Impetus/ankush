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
<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->
<script>
var nimbusConfigTable = null;
$(document).ready(function(){
	nimbusConfigTable=	$('#nimbusConfigTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : false,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
	});
	com.impetus.ankush.stormMonitoring.nimbusConfTable();
});


</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left" id="nimbusConfig">Nimbus Configuration</h2>
		</div>
</div>
</div>
<div class="section-body">

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="nimbusConfigTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC;margin-top:20px;">
				<thead style="text-align: left;display:none;">
					<tr>
						<th style="width:30%"></th>
						<th style="width:60%"></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	</div>
</div>
