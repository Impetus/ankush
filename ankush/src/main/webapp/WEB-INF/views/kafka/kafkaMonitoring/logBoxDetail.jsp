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
<style>
 .table-striped tbody > tr:nth-child(2n) > td, .table-striped tbody > tr:nth-child(2n) > th {
    background-color: #FFFFFF;
}
.table-striped tbody > tr:nth-child(2n+1) > td, .table-striped tbody > tr:nth-child(2n+1) > th {
    background-color: #F9F9F9;
}
</style>
<script>
var logBoxDetailTable = null;
$(document).ready(function(){
	logBoxDetailTable  =	$('#logBoxDetailTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
	    }
	});
	$("#logBoxDetailTable_filter").css({
		'display' : 'none'
	});
	$('#searchlogBoxDetailTable').keyup(function() {
		$("#logBoxDetailTable").dataTable().fnFilter($(this).val());
	});
});


</script>
<div class="section-header">
<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left">Log Details</h2>
		</div>
</div>
</div>
<div class="section-body">
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span6 text-left">
			<h4 class="section-heading">Log Details</h4>
		</div>
		<div class="text-right span6">
					<input id="searchlogBoxDetailTable" type="text"
						placeholder="Search">
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id=logBoxDetailTable
				width="100%">
				<thead class="text-left">
					<tr>
						<th>Parameter</th>
						<th>value</th>
						<!-- <th>Topic Count</th> -->
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
</div>	
	
</div>
