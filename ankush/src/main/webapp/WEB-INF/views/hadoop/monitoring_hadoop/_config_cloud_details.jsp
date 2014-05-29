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
<!-- Fields showing Cloud Cluster Details during Cluster Monitoring   -->

<div id="clouddetails" class="envDivs" Style="display: none;">
	<div class="row-fluid">
		<div class="span3">
			<label class="form-label section-heading" style="margin-left:30px;">Cloud
				Details</label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Cluster Size:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_hCloud_ClusterSize"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Key Pairs:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_hCloud_KeyPairs"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Security Group:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_hCloud_SecurityGroup"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Region:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_hCloud_Region"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Zone:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_hCloud_Zone"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2 ">
			<label class="text-right form-label">Instance Type:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_hloud_InstanceType"></label>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Machine Image:</label>
		</div>
		<div class="span10 text-left">
			<label class="data text-left" id="lbl_hCloud_MachineImage"></label>
		</div>
	</div>
 	<div class="row-fluid">
            <div class="span2">
                <label class="text-right form-label">Auto-Terminate:</label>
            </div>
            <div class="span10 text-left radioDiv">
                <div class="span2"><input type="radio" id="autoTerminateYes" disabled="disabled" name="autoTerminate"/><span>&nbsp;&nbsp;Yes</span></div>
                <div class="span2"><input type="radio" id="autoTerminateNo"  disabled="disabled" name="autoTerminate"/><span>&nbsp;&nbsp;No</span></div>   
            </div>
        </div>
        <div id="autoTerminateYes-action" class="autoTerminateDiv"  style="display: none;">
            <div class="row-fluid ">
                <div class="span2 ">
                    <label class="text-right form-label">Auto-Terminate Interval:</label>
                </div>
                <div class="span10 text-left">
                    <label class="data text-left" id="lbl_hCloud_TimeoutInterval"></label>   
                </div>
            </div>
        </div>
</div>
