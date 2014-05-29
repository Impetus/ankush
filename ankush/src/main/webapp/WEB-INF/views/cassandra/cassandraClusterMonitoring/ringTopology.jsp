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
<script
	src="<c:out value='${baseUrl}' />/public/js/cassandra/cassandraMonitoring.js"
	type="text/javascript"></script>
<script>
$(document).ready(function(){
		var obj1 = {};
		obj1.varName = 'is_autorefresh_monitoringCassandraRingTopology'; 
		obj1.callFunction = "com.impetus.ankush.cassandraMonitoring.ringTopology();";
		obj1.time = 30000;
		obj1.varName = setInterval(obj1.callFunction,obj1.time);
		autoRefreshCallsObject[$.data(document, "panels").children.length].push(obj1);
		com.impetus.ankush.cassandraMonitoring.ringTopology();
})
</script>
<div id="cassandra-ring-graph"
	style="display: none">
	<div class="text-left" id="ring_graph" style=""></div>
	<div class="clear"></div>
</div>



						
						
						
