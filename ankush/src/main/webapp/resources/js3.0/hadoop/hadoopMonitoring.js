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
var hadoopClusterData = null;
var clusterConfigurationDetail = null;
var addNodeHadoopClusterTable = null;
var addNodeProgressTable = null;
var hadoopClusterEcosystemTable_HM = null;
var hadoopServiceStatusTable = null;
var nodeHadoopJSON_AddNodes = null;
var nodeTableLength_AddNodes = null;
/*var eventsData = null;
var eventsOutput = null;
var logData = null;*/
var autoProvisionData = null;
var refreshTimeInterval = 5000;
var refreshTimeIntervalTiles = 90000;
var clusterDetailResult = null;
var deployFlagHadoop = false;
var IsAutoRefreshON = false;
var IsAutoRefreshON_NodeLogs = false;
var IsAutoRefreshON_NodeList = false;
var refreshTimeInterval_NodeList = 60000;
var IsAutoRefreshON_AddNodeProgress = false;
var hNodeListInterval = null;
/*var currentClusterId = null;*/
/*var auditTrailRows = [];*/
/*var eventsRows = [];*/
var fileRack_ServerPath = null;
var successText = "Request has been placed successfully";
var editedFields = [];
var fileIPAddress_ServerPath = null;
var hAddNodeProgressInterval = null;
var autoprovisionStatus = false;
var autoProvisioningEnable = null;
var autoProvisioningDisable = null;
var IsAutoRefreshON_clusterTile = false;
var hClusterTilesView = null;
var nodesData = null;
var nodeDetailsTable = null;
var readCount = null;
var logFileNameOutput = null;
var nodeDeleteData = null;
var isClusterRackEnabled = false;
var isHadoop2Cluster = false;
var inspectNodeData = {};
var StatusOnHead_nodeProgress={
		'Adding Nodes' : 'Node Addition in progress',
		'deployed' : 'Node Addition successful',
};



com.impetus.ankush.hadoopMonitoring = {
		compNameHadoop : 'Hadoop',
        //function will load data tables in hadoop monitoring page COMMON MONITORING 
		

		// function used to focus on element having invalid data
		focusError : function(id) {
			$('#' + id).focus();
		},

		// function used to validate whether node Ip address is provided or not for node retrieval during add node operation
		validateRetrieveEvent : function() {
			$("#popover-content").empty();
			com.impetus.ankush.validation.errorCount = 0;
//			if ($('#ipRange').attr('checked')){
			if($('#ipRange').hasClass('active')){
				com.impetus.ankush.validation.validateField('required', 'ipRangeHadoop', 'IP Range', 'popover-content');
			}
			else{
				com.impetus.ankush.validation.validateField('required', 'filePath_IPAddressFile', 'IP Address File', 'popover-content');
			}
			com.impetus.ankush.hadoopMonitoring.validateRackMappingFileUpload();
		},
	
		//this function will inspect nodes
		inspectNodesObject : function(id){
			var data = {};
			data.nodePorts = {};
			$('.inspect-node').each(function(){
				if($(this).is(':checked')){
					$(this).addClass('inspect-node-ok');
					var ip = $(this).parent().next().text();
					data.nodePorts[ip] = [];
				}
			});
			data.clusterId = com.impetus.ankush.commonMonitoring.clusterId;
			com.impetus.ankush.inspectNodesCall(data,id,'retrieve');
			
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

		// function used to upload a file containing nodes IP to be added to cluster
		hadoopIPAddressFileUpload : function() {
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_IPAddressFile').click();
			$('#fileBrowse_IPAddressFile').change(
					function() {
						$('#filePath_IPAddressFile').val(
								$('#fileBrowse_IPAddressFile').val());
						com.impetus.ankush.hadoopMonitoring.uploadFile(uploadUrl,
								"fileBrowse_IPAddressFile", "fileIPAddress");
					});
		},

		// function used to upload a file
		uploadFile : function(uploadUrl, fileId, uploadType, callback, context) {
			jsonFileUploadString = null;
			$.ajaxFileUpload({
				url : uploadUrl,
				secureuri : false,
				fileElementId : fileId,
				dataType : 'text',
				accept : "application/png",
				success : function(result) {
					if (callback)
						callback(result, context);
					var htmlObject = $(result);
					var resultJSON = new Object();
					eval("resultJSON  = " + htmlObject.text());
					if (uploadType == "fileIPAddress") {
						fileIPAddress_ServerPath = resultJSON.output;
					}
					else if(uploadType == "fileRack") {
						fileRack_ServerPath = resultJSON.output;
					}
				},
				error : function() {
					alert('Unable to upload the file');
				}
			});
		},


		// function used to set a global variable to a current cluster id
		setCurrentClusterId : function(clusterId) {
			currentClusterId = clusterId;
		},

		// function used to stop add nodes progress autorefresh call
		stopAutoRefreshCall_NodeAddition : function() {
			deployFlagHadoop = false;
			IsAutoRefreshON_AddNodeProgress = false;
			hAddNodeProgressInterval = window.clearInterval(hAddNodeProgressInterval);
		},

		// function used to load data of node addtion progress in a table
		nodeAddition : function(clusterId) {
			if (document.getElementById('addNodeProgressTable') == null) {
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}

			if($("#status_AddNodeProgress").hasClass('label-success')) {
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}
			var clusterDetailUrl = baseUrl + '/monitor/' + clusterId
			+ '/nodeprogress';
			clusterDetail = com.impetus.ankush.placeAjaxCall(clusterDetailUrl,
					"GET", false);
			clusterDetailResult = clusterDetail.output;
			if (addNodeProgressTable == null) {
				addNodeProgressTable = $("#addNodeProgressTable").dataTable(
						{
							"bJQueryUI" : true,
							"bPaginate" : false,
							"bLengthChange" : false,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"aoColumnDefs": [
							                 { 'bSortable': false, 'aTargets': [ 3 ] },
							                 { 'sType': "ip-address", 'aTargets': [0] }
							                 ], 							
							                 'fnRowCallback' : function(nRow, aData, iDisplayIndex,
							                		 iDisplayIndexFull) {
							                	 $(nRow).attr('id',
							                			 'rowNodeProgress-' + iDisplayIndex);
							                	 $(nRow).addClass('selected');
							                 }
						});
				$('#searchAddNodeTableHadoop').keyup(function(){
					$("#addNodeProgressTable").dataTable().fnFilter( $(this).val() );
				});
				$("#addNodeProgressTable_filter").css({
					'display' : 'none'
				});

			} 
			if (clusterDetailResult.ipPattern != "") {
//				$('#ipRange').attr('checked', true);
				$('#ipRange').addClass('active');
				$('#ipFile').removeClass('active');
				$('#ipRangeHadoop').val(clusterDetailResult.ipPattern);
				com.impetus.ankush.hadoopMonitoring
				.divShowOnClickIPAddress('div_IPRange');
			} else {
//				$('#ipFile').attr('checked', true);
				$('#ipFile').addClass('active');
				$('#ipRange').removeClass('active');
				$('#filePath_IPAddressFile').val(clusterDetailResult.patternFile);
				com.impetus.ankush.hadoopMonitoring
				.divShowOnClickIPAddress('div_IPFileUpload');
			}
			$('#ipRange').attr('disabled', true);
			$('#ipRangeHadoop').attr('disabled', true);
			$('#ipFile').attr('disabled', true);
			$('#filePath_IPAddressFile').attr('disabled', true);
			$('#retrieveAddNodeButton').attr('disabled', true);

			var tableData = [];
			var flag_ErrorNodeProgress = false;  
			for ( var i = 0; i < clusterDetailResult.nodes.length; i++) {
				var nodeType = null;
				if(clusterDetailResult.nodes[i].nodeState == 'error')
					flag_ErrorNodeProgress = true;	
				var rowData = [];

				if (clusterDetailResult.nodes[i].nameNode) {
					nodeType = "NameNode";
				}
				if (clusterDetailResult.nodes[i].dataNode) {
					if (nodeType != null) {
						nodeType = nodeType + "/DataNode";
					} else
						nodeType = "DataNode";
				}
				if (clusterDetailResult.nodes[i].secondaryNameNode) {
					if (nodeType != null) {
						nodeType = nodeType + "/SecondaryNameNode";
					} else
						nodeType = "SecondaryNameNode";
				}

				var col_PublicIp = clusterDetailResult.nodes[i].publicIp;
				var col_NodeType = '<a class="" id="nodeType-' + i + '">'
				+ nodeType + '</a>';
				var col_Message = '<a class="" id="nodeStatus-' + i + '">'
				+ clusterDetailResult.nodes[i].message + '</a>';
				var col_Image = '<a href="##" onclick="com.impetus.ankush.hadoopMonitoring.loadAddNodeProgressChild('
					+ i
					+ ');"><img id="navigationImg-'
					+ i
					+ '" src="'
					+ baseUrl
					+ '/public/images/icon-chevron-right.png" /></a>';

				rowData.push(col_PublicIp);
				rowData.push(col_NodeType);
				rowData.push(col_Message);
				rowData.push(col_Image);
				tableData.push(rowData);
			}
			addNodeProgressTable.fnClearTable();
			addNodeProgressTable.fnAddData(tableData);
			$("#status_AddNodeProgress").removeClass('label-success');
			$("#div_status_AddNodeProgress").css("display", "block");

			if (flag_ErrorNodeProgress) {
				var errNodesCount = 0;
				for ( var i = 0; i < clusterDetailResult.nodes.length; i++) {
					if (clusterDetailResult.nodes[i].nodeState == 'error') {
						$('#rowNodeProgress-' + i).addClass('error');
						errNodesCount++;
						clusterErrorMessage = errNodesCount
						+ ". Node addition failed : "
						+ clusterDetailResult.nodes[i].publicIp;
						$("#popover-content").append(
								'<div class="errorLineDiv"><a style="color: #5682C2;" href="#">' + clusterErrorMessage
								+ '</a></br></div>');
					}
				}
				$("#error-div-hadoop").css("display", "block");
				if (errNodesCount > 1)
					$('#errorBtnHadoop').text(errNodesCount + " Errors");
				else
					$('#errorBtnHadoop').text(errNodesCount + " Error");
				$('#errorBtnHadoop').css("display", "block");
				$("#div_status_AddNodeProgress").css("display", "none");
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}
			else if(clusterDetailResult.state == 'Adding Nodes'){
				$('#status_AddNodeProgress').text(StatusOnHead_nodeProgress['Adding Nodes']);
			}
			else {
				deployFlagHadoop = true;
				$("#div_status_AddNodeProgress").css("display", "block");
				$("#status_AddNodeProgress").addClass('label-success');
				$("#status_AddNodeProgress").html('').text(StatusOnHead_nodeProgress['deployed']);
				com.impetus.ankush.hadoopMonitoring.stopAutoRefreshCall_NodeAddition();
				return;
			}
			if (!deployFlagHadoop) {
				if (!IsAutoRefreshON_AddNodeProgress) {
					IsAutoRefreshON_AddNodeProgress = true;
					com.impetus.ankush.hadoopMonitoring
					.initProgressNodeStatusNodeTable(clusterId);
				}
			}
		},


		// function used to select/ deselect node type during checking/unchecking of node
		updateHeaderCheckBox : function(id) {
			var rowId = (id.split('-'))[1];
			if (!$('#' + id).attr('checked')) {
				$("#addNodeCheckHead").removeAttr('checked');
				$("#addDataNode-" + rowId).removeAttr('checked');
//				$("#addSecNameNode-" + rowId).removeAttr('checked');
			} else
				$("#addDataNode-" + rowId).attr('checked', true);
			com.impetus.ankush.headerCheckedOrNot('checkedElement','addNodeCheckHead');
		},

		// function used to check/uncheck node header check box during checking/unchecking of datanode type
		updateDataNodeCheckBox : function(id) {
			var rowId = (id.split('-'))[1];
			if (!$('#' + id).attr('checked')) {
					$("#addNodeCheckHead").removeAttr('checked');
					$("#hadoopAddNodeCheckBox-" + rowId).removeAttr('checked');
				
//				if (!$('#addSecNameNode-' + rowId).attr('checked')) {
//					$("#addNodeCheckHead").removeAttr('checked');
//					$("#hadoopAddNodeCheckBox-" + rowId).removeAttr('checked');
//				}
			} else {
				$("#hadoopAddNodeCheckBox-" + rowId).attr('checked', true);
			}
		},

		// function used to check/uncheck node header check box during checking/unchecking of secondary namenode type
		updateSecNameNodeCheckBox : function(id) {
			var rowId = (id.split('-'))[1];
			if (!$('#' + id).attr('checked')) {
				if (!$("#addDataNode-" + rowId).attr('checked')) {
					$("#addNodeCheckHead").removeAttr('checked');
					$("#hadoopAddNodeCheckBox-" + rowId).removeAttr('checked');
				}
			} else {
				$("#hadoopAddNodeCheckBox-" + rowId).attr('checked', true);
			}
		},

		// function used to load hadoop cluster monitoring data
		loadClusterData : function(clusterId) {
			currentClusterId = clusterId;
			com.impetus.ankush.hadoopMonitoring.heatMapToggle();
			com.impetus.ankush.hadoopMonitoring.loadClusterEcosystem(clusterId);
		},

		// function used to show heat map graphs
		heatMapToggle : function() {
			$('#heat_map').empty();
			getHeatMapChart(currentClusterId,'0');
		},


		// function used to define tooltip title on the slider of hadoop Ecosystem components details page
		toolTipDecide : function(pageId){
			var toolTipTitle = null;
			if (pageId == '1'){
				toolTipTitle = hadoopClusterData.output.clusterName;
			}else{
				toolTipTitle = 'Hadoop Ecosystem';
			}
			return toolTipTitle;
		},


		// function used to remove error-class
		removeErrorClass : function() {
			$('#nodeIP').removeClass('error-box');
			$('#fileName').removeClass('error-box');
			$("#error-div-hadoopLogs").css("display", "none");
			$('#errorBtnHadoopLogs').css("display", "none");
		},

		

		

		
		// this function will populate tiletable for index drill down and node drill
		// down
		populateTileTables : function(tableName) {
			var url = baseUrl + '/monitor/'+clusterId+'/'+tableName;;
			if(hybridTechnology !== null)
				url = baseUrl + '/monitor/'+clusterId+'/'+tableName+'?component='+hybridTechnology;
				com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
					if (eval(tableName+'Table') != null)
						eval(tableName+'Table').fnClearTable();
					if(result.output.status == true){
						for ( var dataKey in result.output.data) {
							var secondColumn = result.output.data[dataKey];
							if(result.output.data[dataKey].indexOf('http://') !== -1){
								secondColumn = '<a href="'+result.output.data[dataKey]+'" target="newTab">'+result.output.data[dataKey]+'</a>';
							}
							eval(tableName+'Table').fnAddData([
							                           dataKey,
							                           secondColumn 
							                          ]);
						}
					}else{
						eval(tableName+'Table').fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						eval(tableName+'Table').fnClearTable();
					}
				});
		},
		populateProcessSummaryTables : function(tableName,process) {
			$("."+process).html(process);
				url = baseUrl + '/monitor/'+clusterId+'/'+tableName+'?component='+hybridTechnology+'&process='+process;
				com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
					if (eval(tableName+'Table'+process) != null)
						eval(tableName+'Table'+process).fnClearTable();
					if(result.output.status == true){
						for ( var dataKey in result.output.data) {
							eval(tableName+'Table'+process).fnAddData([
							                           dataKey,
							                           result.output.data[dataKey] 
							                          ]);
						}
					}else{
						eval(tableName+'Table'+process).fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						eval(tableName+'Table'+process).fnClearTable();
					}
				});
		},


		isHadoop2 : function(){
			var flag = false;
			var url = baseUrl+'/monitor/'+clusterId+'/hadoopversion?component=Hadoop';
			com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
				if(result.output.isHadoop2){
					flag = true;
				}
			});
			return flag;
		},
		
		

};
