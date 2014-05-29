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
<!-- Hadoop Cluster Ecosystem Components Details Page -->
<script>
$(document).ready(function(){
	hadoopClusterEcosystemTable_HM = $('#configHadoopEcoSystemTable').dataTable({
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
	
});
</script>
<div class="row-fluid">
	<div class="span10 text-left">
		<h4 class="section-heading" style="text-align: left;">Hadoop
			Ecosystem</h4>
	</div>
</div>
<div class="row-fluid">
	<div class="span12 text-left">
		<table class="table table-striped" id="configHadoopEcoSystemTable"
			width="100%" style="border: 1px solid; border-color: #CCCCCC">
			<thead style="text-align: left;">
				<tr>
					<th>Component</th>
					<th>Vendor</th>
					<th>Version</th>
					<th>Status</th>
					<th></th>
				</tr>
			</thead>
			<tbody style="text-align: left;">

			</tbody>
		</table>
	</div>
</div>
