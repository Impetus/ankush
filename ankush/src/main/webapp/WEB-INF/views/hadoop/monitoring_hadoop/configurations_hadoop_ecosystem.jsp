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
<!-- Hadoop Cluster & ist Components details page during Cluster Monitoring -->

<%@ include file="../../layout/blankheader.jsp"%>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span4 row-fluid">
			<h2 class="heading text-left">Hadoop Ecosystem</h2>
		</div>
		<div class="span8 text-right" style="display: none;">
			<button class="btn" style="margin-right: 8px;" onclick="com.impetus.ankush.removeChild(3);">Cancel</button>
			<button class="btn" style="margin-right: 40px;" disabled="disabled">Save</button>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="container-fluid">
		<%@ include file="_config_hadoop_details.jsp"%>

		<div class="row-fluid">
			<div class="span10">
				<label class="form-label section-heading text-left">Hadoop EcoSystem</label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12 text-left">
				<table class="table table-striped" id="HadoopEcoSystemTable_ManageConfig" 
				style="border:1px solid;border-color: #CCCCCC" width="100%">
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
	</div>
</div>
