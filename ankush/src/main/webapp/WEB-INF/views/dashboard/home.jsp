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
<%@ include file="../layout/blankheader.jsp"%>


<div class="section-header">
<input id="dashboardJsonVariable" style="display:none"/>
	<div class="row-fluid mrgt20">
		<div class="span12">
			<h2 class="heading">Cluster Overview</h2>
		</div>
	</div>



</div>
<div class="section-body">
	
	<div class="padt15">
		<!-- <div class="row-fluid transitions-enabled" id="wrapper_error"></div>
		 <br style="clear: both;" /> tile will break into two line 
		<div class="row-fluid transitions-enabled" id="wrapper_other"></div>-->
		  
		<div class="row-fluid transitions-enabled" id="allTile"></div>
		
		
		
		
	</div>
	<div id="technologyDialogBox" class="modal hide fade" style="display: none;" data-keyboard="true" data-backdrop="static">
		<div class="modal-header text-center">
			<h4 id="headingTechDialog"></h4>
		</div>
		<div class="row-fluid modal-body" id="technoloigyList"></div>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn">Cancel</a>
		</div>
	</div>
</div>


<script>
$(document).ready(function(){
				com.impetus.ankush.dashboard.createTile();
				com.impetus.ankush.dashboard.getUserId();
				autoRefreshCallsObject['0'] = [];
				var obj = {};
				obj.varName = 'autoRefreshCallInterval';
				obj.callFunction = 'com.impetus.ankush.dashboard.createTile()';
				obj.time = 15000;
				autoRefreshCallsObject['0'].push(obj);
				autoRefreshCallsObject['0'][0].varName = setInterval(autoRefreshCallsObject['0'][0].callFunction,autoRefreshCallsObject['0'][0].time);
			});
			
$(document).ready(function(){
	
	$(".section-root .section-header").css('width', '100%').css('width', '-=90px');
	$(".section-children .section-header").css('width', '100%').css('width', '-=110px');
	$(".section-children-1 .section-header").css('width', '100%').css('width', '-=130px');
	$(".section-children-2 .section-header").css('width', '100%').css('width', '-=150px');
 

});
</script>
