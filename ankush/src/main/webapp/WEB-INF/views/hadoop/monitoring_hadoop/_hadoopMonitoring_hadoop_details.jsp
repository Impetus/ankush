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
$(document).ready(function(){
	$('#hadoopMonitoring_HadoopDetailsTable').dataTable({
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
	
	var url = baseUrl + "/monitor/" + com.impetus.ankush.commonMonitoring.clusterId + "/ecosystem";
	hadoopClusterData = com.impetus.ankush.placeAjaxCall(url, 'GET', false);
	com.impetus.ankush.hadoopMonitoring.loadHadoopMonitoringDatatables(com.impetus.ankush.commonMonitoring.clusterId);
	
});
</script>

<div id="monitoringHadoopDetails" style="margin-top:10px;">
	<div class="row-fluid">
		<div class="text-left">
			<h4 class="section-heading" style="text-align: left;">Hadoop
				Details</h4>
		</div>
	</div>
	
	
	<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped" id="hadoopMonitoring_HadoopDetailsTable"
				width="100%" style="border: 1px solid; border-color: #CCCCCC; margin-bottom:10px;">
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
							<label class="text" id="hadoopMonitoring_vendor"></label>
						</td>
						<td> 
							<label class="text" id="hadoopMonitoring_version"></label>
						</td>
						<td> 
							<label class="text" id="hadoopMonitoring_dfsReplication"></label>
						</td>
						<td> 
							<a style="margin-left: 20px;" href="##" id="lnk_HadoopAdvSettings_img"
							onclick="com.impetus.ankush.hadoopMonitoring.hadoopEcosystemAdvanceSettings('1');"><img
							src="<c:out value='${baseUrl}' />/public/images/icon-chevron-right.png" /></a>
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
</div>
