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
<!-- Fields showing Services status with links to start/stop services on node Details Page  -->

<script>
	$(document).ready(function(){
		var clusterId = "<c:out value='${clusterId}'/>";
		var nodeId = "<c:out value='${nodeId}'/>";
		var nodeIp = "<c:out value='${nodeIp}'/>";
		$('.dropdown-toggle').dropdown();
	});
</script>
<div id="hadoopNodeServiceDetails">
	<div class="row-fluid span12">
		<div class="text-left">
			<label style="float:left" class="form-label section-heading">Services</label>
		</div>
		<div class="row-fluid span5 text-left">
			<div class="btn-group" style="margin-top: 10px">
  				<button class="btn" style="height: 25px" onclick="com.impetus.ankush.hadoopNodeDrillDown.hadoopServiceAction('start');">Start</button>
  				<button class="btn dropdown-toggle" data-toggle="dropdown" style="height: 25px">
    				<span class="caret"></span>
  				</button>
  				<ul class="dropdown-menu">
					<li><a href="#"
						onclick="com.impetus.ankush.hadoopNodeDrillDown.hadoopServiceAction('start');">Start</a></li>
					<li><a href="#" onclick="com.impetus.ankush.hadoopNodeDrillDown.hadoopServiceAction('start all');">Start All</a></li>
  				</ul>
			</div>
			<div class="btn-group" style="margin-top: 10px;">
  				<button class="btn" style="height: 25px" onclick="com.impetus.ankush.hadoopNodeDrillDown.hadoopServiceAction('stop');">
  				Stop</button>
  				<button class="btn dropdown-toggle" style="height: 25px" data-toggle="dropdown">
    				<span class="caret"></span>
  				</button>
  				<ul class="dropdown-menu">
					<li><a href="#"
						onclick="com.impetus.ankush.hadoopNodeDrillDown.hadoopServiceAction('stop');">Stop</a></li>
					<li><a href="#" onclick="com.impetus.ankush.hadoopNodeDrillDown.hadoopServiceAction('stop all');">
						Stop All</a></li>
  				</ul>
			</div>
		</div>
	</div>
	<div class="row-fluid span12">
		<div class="span5 text-left">
		<table class="table" id="hadoopNodeServiceDetail" 
		style="border:1px solid;border-color: #CCCCCC;">
			<thead style="text-align: left;">
				<tr>
					<th><input type='checkbox' id='checkHead_NodeService'
								onclick="com.impetus.ankush.hadoopNodeDrillDown.checkAll_NodeService()"></th>
					<th>Service</th>
					<th>Status</th>
				</tr>
			</thead>
			<tbody style="text-align: left;">
			</tbody>
		</table>
		</div>
	</div>
</div>
