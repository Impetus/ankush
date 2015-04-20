/*******************************************************************************
*  ===========================================================
*  Ankush : Big Data Cluster Management Solution
*  ===========================================================
*  
*  (C) Copyright 2014, by Impetus Technologies
*  
*  This is free software; you can redistribute it and/or modify it under
*  the terms of the GNU Lesser General Public License (LGPL v3) as
*  published by the Free Software Foundation;
*  
*  This software is distributed in the hope that it will be useful, but
*  WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*  See the GNU Lesser General Public License for more details.
*  
*  You should have received a copy of the GNU Lesser General Public License 
*  along with this software; if not, write to the Free Software Foundation, 
* Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*******************************************************************************/
var loadClusterLevelGraphsStartTime = 'lasthour';
var monitorClusterTechnology="";
com.impetus.ankush.commonMonitoring = {
        nodeIndexForAutoRefresh : null,
        commonHeatMaps : function(currentClusterId){
            getCommonHeatMapChart(currentClusterId,'0');
        },
       // create tiles on node deatail page
                // will draw graph on individual node detail page
              //normalize graph data , will be called from drawGraph_JobMonitoring function
      normalizeData: function (rrdData)
      {
        if(rrdData == null){
            return [];
        }
        var meta = rrdData.meta;
        var data = rrdData.data;
        var legends = meta.legend;
        var result = [];

        legends.forEach(function(legend, index)
        {
            result.push({key: legend, values: [], yvalues : []});
        });
        data.forEach(function(data, dataIndex)
        {
          legends.forEach(function(legend, legendIndex)
          {
            result[legendIndex].values.push([Number(data.t) , Number(data.v[legendIndex])]);
            result[legendIndex].yvalues.push(Number(data.v[legendIndex]));
          });         
        });     
        return result;
      },
           //this function will take node page on heatmap individual node click
      graphOnClick : function(nodeIp) {
    	  var url = baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+nodeIp;
    	  $(location).attr('href',url);
        },
        //this will send delete request for cluster
      deleteCluster : function() {
    	  	if(!com.impetus.ankush.validate.empty($("#passForDelete").val())){
    	  		$("#passForDeleteError").text("Password must not be empty.").addClass('text-error');
            	$("#passForDelete").addClass('error-box');
            	return;
    	  	}
    	  	else if (com.impetus.ankush.commonMonitoring.clusterId == null) {
    	  		$("#passForDelete").addClass('error-box').text("Invalid ClusterId.").addClass('text-error');;
                return;
            } else {
            	$("#confirmDeleteButtonHadoopMonitor").button('loading');
                var dataParam = {
                		"password" : $("#passForDelete").val()
                };
                var deleteUrl = baseUrl + '/cluster/removemixcluster/' + com.impetus.ankush.commonMonitoring.clusterId;
	            com.impetus.ankush.placeAjaxCall(deleteUrl,'DELETE',true,dataParam,function(result){
	            		$("#confirmDeleteButtonHadoopMonitor").button('reset');
	            		if (result.output.status) {
	            			$('#deleteClusterDialogcommonMonitor').modal('hide');
	            			$(location).attr('href',(baseUrl + '/dashboard'));
	                    } else{
	                    	$("#passForDeleteError").text(result.output.error).addClass('text-error');
	                    	$("#passForDelete").addClass('error-box');
	                    }
	           });
            }
        },
        
       //this function will validate pattern
        validatePattern : function(value, pattern) {
            if(value.match(pattern) != null) {
                return true;
            }
            return false;
        },
        //this will check whether percentage lie or not
        percentageCheck : function(x, min, max) {
            x = parseInt(x);
            min = parseInt(min);
            max = parseInt(max);
            return x >= min && x <= max;
        },

        // function used to remove error-class
        removeErrorClass : function() {
            $('#nodeIP').removeClass('error-box');
            $('#fileName').removeClass('error-box');
            $("#error-div-hadoopLogs").css("display", "none");
            $('#errorBtnHadoopLogs').css("display", "none");
        },
        //this function will open confirmation dialog for restart cluster
        startStopClusterDialog : function(event){
        	$("#passForStartStopError").empty();
    		$("#passForStartStop").val("");
        	$("#confirmStartStopCluster").appendTo('body').modal('show');
            $('.ui-dialog-titlebar').hide();
            $('.ui-dialog :button').blur();
            var headingButton = event;
			headingButton = headingButton.replace( /([A-Z])/g, " $1" );
			headingButton = headingButton.charAt(0).toUpperCase()+headingButton.slice(1);
            $("#startStopButton").unbind('click');
			$("#startStopButton").text(headingButton);
			$("#startStopDialogHeading").text(headingButton);
			$("#startStopButton").bind('click',function(){
				com.impetus.ankush.commonMonitoring.startStopCluster(event);
			});
			setTimeout(function() {
            	$("#passForStartStop").focus();
            }, 500);
		},
        //Delete cluster dialog
        deleteClusterDialog : function(){
            $("#deleteClusterDialogcommonMonitor").appendTo('body').modal('show');
            $("#passForDelete").val('');
            $("#passForDeleteError").text('');
    	  	$("#passForDelete").removeClass('error-box');
            $('.ui-dialog-titlebar').hide();
            $('.ui-dialog :button').blur();
            $("#deleteDiv").empty();
            var deleted = [];
            for(var key in clusterComponentsResponseObject){
            		deleted.push(key.indexOf("Zookeeper") > -1 ? "Zookeeper" : key);
            }
            if(deleted.length != 0){
        		$("#deleteDiv").append(deleted.join(', ')+" will be deleted.");
        	}
            setTimeout(function() {
            	$("#passForDelete").focus();
            }, 500);
        },
      
       
        //this fuction will start/stop cluster
        startStopCluster : function(event){
        	var eventName = event.split('Cluster')[0];
        	var url = baseUrl+'/service/'+clusterId+'/'+eventName;
        	/*var progressState = com.impetus.ankush.commonMonitoring.startStopProgressCheck();
        	if(progressState == "InProgress"){
        		return;
        	}*/
        	var data = {
        			"password" : $("#passForStartStop").val()
        	};
        	com.impetus.ankush.placeAjaxCall(url,'POST',true,data,function(result){
        		com.impetus.ankush.createCommonTiles();
        		if(result.output.status === false){
        			$("#passForStartStopError").text(result.output.error[0]);
        		}else
        			$("#confirmStartStopCluster").modal('hide');
        	});
        },
         // function used to show information on tooltip of node IP details
        divShowOnClickIPAddress : function(clickId) {
            $('#ipRangeHadoop').removeClass('error-box');
            $('#ipRangeHadoop').tooltip();
            com.impetus.ankush.common.tooltipOriginal(
                    'ipRangeHadoop', 'Enter IP Address Range');

            $('#filePath_IPAddressFile').removeClass('error-box');
            $('#filePath_IPAddressFile').tooltip();
            com.impetus.ankush.common.tooltipOriginal(
                    'filePath_IPAddressFile', 'Upload IP Address File');

            $('#div_IPRange').attr('style', 'display:none');
            $('#div_IPFileUpload').attr('style', 'display:none;');
            $('#' + clickId).attr('style', 'display:block;');
        },
       
      
        
        
};


