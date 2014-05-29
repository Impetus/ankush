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
<!-- Fields showing Hadoop Cluster Details & link to its Advanced settings  -->

<script>
	$('.editOnClick').textEditable({
		cssClass : 'edit',
	});
	
	$('#configHadoopDetailsTable').dataTable({
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : false,
		"bSort" : false,
		"bInfo" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
	});
	
</script>
<div id="configHadoopDetails">
	<div class="row-fluid">
		<div class="span3 text-left">
			<label class="form-label section-heading">Hadoop Details</label>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="configHadoopDetailsTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC">
				<thead style="text-align: left;">
					<tr>
						<th>Vendor</th>
						<th>Version</th>
						<th>DFS Replication</th>
						<th></th>
					</tr>
				</thead>
				<tbody style="text-align: left;">
					<tr> 
						<td> 
							<label class="text" id="configHadoopVendor"></label>
						</td>
						<td> 
							<label class="text" id="configHadoopVersion"></label>
						</td>
						<td> 
							<label class="text" id="configHadoopDfsReplication"></label>
						</td>
						<td> 
							<a style="margin-left: 20px;" href="##" id="lnk_HadoopAdvSettings_img_1"
							onclick="com.impetus.ankush.hadoopMonitoring.hadoopEcosystemAdvanceSettings('3');"><img
							src="<c:out value='${baseUrl}' />/public/images/icon-chevron-right.png" /></a>
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
</div>

