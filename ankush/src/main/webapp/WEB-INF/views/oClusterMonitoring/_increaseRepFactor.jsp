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
<!-- Replication Increasing dialog box -->
<div id="increaseRepFactorDialog" class="modal hide fade" style="display:none;">
	
	<div class="modal-header text-center">
				<h4 class="dialog-heading">Increase Replication Factor</h4>
			</div>
	<!-- <div class="row-fluid">
		<div class="span12 text-center">
			<p class="dialog-heading">Increase Replication Factor</p>
		</div>
	</div> -->
	<div class="modal-body">
	<div class="row-fluid">
		<div class="span4 offset1 text-right"><label class="form-label">Nodes In Use:</label></div>
		<div class="span7 text-left"><label class="form-label" id="nodesInUseRepDialog"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span4 offset1 text-right"><label class="form-label">Shards:</label></div>
		<div class="span7 text-left"><label class="form-label" id="shardsRepDialog"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span4 offset1 text-right"><label class="form-label">Available Storage Nodes:</label></div>
		<div class="span7 text-left"><label class="form-label" id="availableStorageNodeRepDialog"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span4 offset1 text-right"><label class="form-label">Available Capacity:</label></div>
		<div class="span7 text-left"><label class="form-label" id="availableCapacityeRepDialog"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span4 offset1 text-right"><label class="form-label">Replication Factor:</label></div>
		<div class="span7 text-left"><label class="form-label" id="repFactorRepDialog"></label></div>
	</div>
	<div class="row-fluid">
		<div class="span4 offset1 text-right"><label class="form-label">New Replication Factor:</label></div>
		<div class="span7 text-left"><input type="text" style="width:70px;height:20px;"placeholder="#" id="repFactorToIncrease"/></div>
	</div>
	</div>
	
	<div class="modal-footer">
				 <a href="#" data-dismiss="modal" id="cancelBtnIncRep" onclick="com.impetus.ankush.oClusterMonitoring.cancelActionDialog('cancelBtnIncRep')" class="btn">Cancel</a>
				<a href="#" id="increaseBtnIncRep"
					onclick="com.impetus.ankush.oClusterMonitoring.increaseReplicationFactor();" class="btn">Increase</a>
					
			</div>
	
	
	<!-- <div class="row-fluid">
		<div class="span10 text-right"><button id="cancelBtnIncRep" class="btn" onclick="com.impetus.ankush.oClusterMonitoring.closeIncreaseRepFactorDialog();">Cancel</button></div>
		<div class="span1 text-right"><button id="increaseBtnIncRep" class="btn" onclick="com.impetus.ankush.oClusterMonitoring.increaseReplicationFactor();">Increase</button></div>
	</div> -->
	
</div>
