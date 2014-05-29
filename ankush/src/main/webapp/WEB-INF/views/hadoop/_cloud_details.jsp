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
<!-- Fields related to Cloud Cluster Details during Cluster Creation   -->
<div id="clouddetails" class="envDivs" Style="display:none;">
        <div class="row-fluid">
            <div class="span3 text-left">
                <label class="form-label section-heading">Cloud Details
                 </label>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2 ">
                <label class="text-right form-label">Cluster Size:</label>
            </div>
            <div class="span10 text-left">
                <input type="text" class="input-large" style="width: 50px;" id="cluster_size_cloud"
                    placeholder="#" data-toggle="tooltip" data-placement="right" title="Size of the cluster in the number of Nodes"></input>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2">
                <label class="text-right form-label">Key Pairs:</label>
            </div>
            <div class="span10 text-left">
                <select style="width: 300px;" id="keyPairsSelect" data-toggle="tooltip" data-placement="right" title="">
                <option>Key Pairs</option>
                </select>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2 ">
                <label class="text-right form-label">Security Group:</label>
            </div>
            <div class="span10 text-left">
                <select style="width: 300px;" id="securityGroupsSelect" data-toggle="tooltip" data-placement="right" title="">
                <option>Security Group</option>
                </select>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2">
                <label class="text-right form-label">Region:</label>
            </div>
            <div class="span10 text-left">
                <select style="width: 300px;" id="regionSelect" onchange="com.impetus.ankush.hadoopCluster.zonePopulation();"
                data-toggle="tooltip" data-placement="right" title="">
                <option>Region</option>
                </select>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2">
                <label class="text-right form-label">Zone:</label>
            </div>
            <div class="span10 text-left">
                <select style="width: 300px;" id="zoneSelect" data-toggle="tooltip" data-placement="right" title="">
                <option  >Zone</option>
                </select>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2 ">
                <label class="text-right form-label">Instance Type:</label>
            </div>
            <div class="span10 text-left">
                <select style="width: 300px;" id="instanceTypeSelect" data-toggle="tooltip" data-placement="right" title="">
                <option>Instance Type</option>
                </select>
            </div>
        </div>
        <div class="row-fluid" style="display:none;">
            <div class="span2">
                <label class="text-right form-label">OS:</label>
            </div>
            <div class="span10 text-left">
                <select style="width: 300px;"id="os_hadoop" >
                <option>OS</option>
                </select>
            </div>
        </div>
        <div class="row-fluid" style="display: none;">
            <div class="span2">
                <label class="text-right form-label">Architecture:</label>
            </div>
            <div class="span10 text-left radioDiv">
                <div class="span2"><input type="radio" name="architecture" /><span>&nbsp;&nbsp;32 bit</span></div>
                <div class="span2"><input type="radio" name="architecture" checked="checked"/><span>&nbsp;&nbsp;64 bit</span></div>   
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2">
                <label class="text-right form-label">Machine Image:</label>
            </div>
            <div class="span10 text-left">
                <input type="text" class="input-large" id="machine_image_hadoop" data-toggle="tooltip" data-placement="right"
                    title="Image ID to create / instantiate a machine" placeholder="Machine Image"></input>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span2">
                <label class="text-right form-label">Auto-Terminate:</label>
            </div>
            <div class="span10 text-left radioDiv">
                <div class="span2"><input type="radio" id="autoTerminateYes" name="autoTerminate"  onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('autoTerminateYes','autoTerminateDiv');"/><span>&nbsp;&nbsp;Yes</span></div>
                <div class="span2"><input type="radio" id="autoTerminateNo" checked="checked" name="autoTerminate" checked="checked" onclick="com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('autoTerminateNo','autoTerminateDiv');"/><span>&nbsp;&nbsp;No</span></div>   
            </div>
        </div>
        <div id="autoTerminateYes-action" class="autoTerminateDiv"  style="display: none;">
            <div class="row-fluid ">
                <div class="span2 ">
                    <label class="text-right form-label">Auto-Terminate Interval:</label>
                </div>
                <div class="span10 text-left">
                    <input type="text" class="input-large" id="timeout_interval_hadoop" value="10" data-toggle="tooltip" data-placement="right"
                    title="Timeout Interval(in mins)" style="width: 50px;" placeholder="Interval"></input>   
                </div>
            </div>
        </div>
</div>   
