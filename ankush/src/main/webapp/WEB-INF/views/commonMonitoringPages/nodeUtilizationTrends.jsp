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
<%@include file="../layout/blankheader.jsp"%>
<script
    src="<c:out value='${baseUrl}' />/public/js/commonMonitoring/nodeUtilizationTrend.js"
    type="text/javascript"></script>


<style>
.demo{
    clear: both;
    float: none;
    margin: 10px auto;
    width: auto;
    height : 300px;
}
.jstree.jstree-proton li {
    font-size: 14px;
    line-height: 32px;
}
.jstree ins{
	width : 15px !important;
}
.jstree.jstree-proton a > ins{
	width : 16px !important;
}
</style>
<script type="text/javascript">
function nodeUtilizationTrendAutoRefresh(){
    var obj1 = {};
    var autoRefreshArray = [];
    obj1.varName = 'is_autoRefresh_nodeUtilizationTrend';
    obj1.callFunction = "com.impetus.ankush.nodeUtilizationTrend.graphsViewport(true);";
    obj1.time = 50000;
    autoRefreshArray.push(obj1);
    com.impetus.ankush.addAutorefreshCall(autoRefreshArray,$.data(document, "panels").children.length);
}
var nodeLevelGraphsStartTime = 'lasthour';
var ipForNodeUtilization = null;
var clusterIdForNodeUtilization = null;
var clusterTechnologyForNodeUtilization = '<c:out value='${clusterTechnology}' />';
$(document).ready(
function() {
    if(clusterTechnologyForNodeUtilization == 'Oracle NoSQL'){
        ipForNodeUtilization = storageNodeIP;
        clusterIdForNodeUtilization = monitorClusterId;
    }else{
        ipForNodeUtilization = ipForNodeDrillDown;
        clusterIdForNodeUtilization = com.impetus.ankush.commonMonitoring.clusterId;
    }
    $(window).scroll($.debounce(500,false,com.impetus.ankush.nodeUtilizationTrend.graphsViewport));
    $("#pageHeadingNodeUtilization").text(ipForNodeUtilization+'/Utilization Metrics');
    //com.impetus.ankush.commonMonitoring.loadNodeLevelGraphsDivs(ipForNodeUtilization)
     $('#systemMetric').expandCollapse({
        content : 'show',
        callbackOpen : 'com.impetus.ankush.commonMonitoring.metricSection(\''+ipForNodeUtilization+'\')'
    });
    com.impetus.ankush.nodeUtilizationTrend.jstreeCall();   
    $("#jstreeNodeGraph").bind("change_state.jstree", function(e, d) {   
        $("#jstreeNodeGraph").unbind("loaded.jstree");
        if ((d.args[0].tagName == "A" || d.args[0].tagName == "INS") &&
                    (d.inst.data.core.refreshing != true && d.inst.data.core.refreshing != "undefined"))
            var divId = d.rslt.attr("id");
            var checked = $("#" + divId + ".jstree-checked").length!=0;   
            if(checked == false){
                com.impetus.ankush.nodeUtilizationTrend.removeGraphUsingId(divId);
                return;   
            }
            var pattern = divId.replace(/[\_]+/g,'.');
            com.impetus.ankush.nodeUtilizationTrend.getGraphUsingAjax(clusterIdForNodeUtilization,ipForNodeUtilization,nodeLevelGraphsStartTime,pattern+'(\\.|_).*');
    });   
    $("#jstreeNodeGraph").bind("loaded.jstree", function(e, d) {
        com.impetus.ankush.nodeUtilizationTrend.loadAllSavedGraphs();
    });
    nodeUtilizationTrendAutoRefresh();
});
    function slide(){
        var isVisible = $('#jstreeNodeGraph').is(':visible');
        if(isVisible){
        	$('#jstreeNodeGraph').hide('slow',function(){
        		$('#nodeUtilizationTrendGraphsDivs').removeClass('span9').addClass('span12');
        	});
        	$('#buttonGroup_tree').animate({opacity : 'hide'});
        }
        else{
        	$('#nodeUtilizationTrendGraphsDivs').removeClass('span12').addClass('span9');
        	$('#jstreeNodeGraph').show('slow',function(){});
        	$('#buttonGroup_tree').animate({opacity : 'show'});
        }
    }
</script>
<!-- This page will show detailed VIew of Utilization Trend Graphs -->
<div class="section-header">
    <div class="row-fluid mrgt20">
        <div class="span6">
            <h2 class="heading text-left" id= "pageHeadingNodeUtilization"></h2>
        </div>
        <div class="span6 text-right">
            <div class="form-image text-left btn-group"
                    id="graphButtonGroup_utilizationTrend" data-toggle="buttons-radio"
                    style="margin-top: -2px;">
                    <button class="btn" id="btnLastYear_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastyear');">1y</button>
                    <button class="btn" id="btnLastMonth_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastmonth');">1m</button>
                    <button class="btn" id="btnLastWeek_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastweek');">1w</button>
                    <button class="btn" id="btnLastDay_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lastday');">1d</button>
                    <button class="btn active" id="btnLastHour_HNDD" onclick="com.impetus.ankush.nodeUtilizationTrend.loadGraphsOnChangeStartTime('lasthour');">1h</button>
                </div>
        </div>
    </div>
</div>
<div class="section-body">
    <div class="container-fluid">
        <div class="mrgt20 boxToExpand">
        <div class="titleExpand">
            <a onclick ="slide()" href="#"><img src="<c:out value = '${baseUrl}'/>/public/images/list.png"/></a>
                    <div class="btn-group"
                    id="buttonGroup_tree" data-toggle="buttons-radio" style="margin-top:-4px;">
                    <button class="btn" id="btnLastDay_HNDD"
                        onclick="com.impetus.ankush.jstreeExpandAll('jstreeNodeGraph');" style="height:20px;">Expand All</button>
                    <button class="btn active" id="btnLastHour_HNDD"
                        onclick="com.impetus.ankush.jstreeCollapseAll('jstreeNodeGraph');" style="height:20px;">Collapse All</button>
                </div>
        </div>
        <div style="border: 1px solid #E6E6E6;" class="contentExpand row-fluid">
       
            <div id="jstreeNodeGraph" class="span3 pad10" style="height:100%;border-right: 1px solid #E6E6E6;"><ul> </ul></div>
            <div id="nodeUtilizationTrendGraphsDivs" class="span9" style=""></div>
        </div>
     </div>         
    </div>
</div>

