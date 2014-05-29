/*******************************************************************************
*===========================================================
*Ankush : Big Data Cluster Management Solution
*===========================================================
*
*(C) Copyright 2014, by Impetus Technologies
*
*This is free software; you can redistribute it and/or modify it under
*the terms of the GNU Lesser General Public License (LGPL v3) as
*published by the Free Software Foundation;
*
*This software is distributed in the hope that it will be useful, but
*WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*See the GNU Lesser General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License 
*along with this software; if not, write to the Free Software Foundation, 
*Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/

var cloudDetailsResult = null;
var hadoopData=null;
var hadoopCreate_EcosystemTable=null;
var addNodeHadoopTable=null;
var hadoopCertifiedData=null;
var nodeHadoopJSON=null;
var nodeTableLength=null;
var connectionPassword=null;
var connectionURL=null;
var connectionDriverName=null;
var connectionUserName=null;
var valComponentLocalPath = null;
var valComponentDownloadPath = null;
var clientPort=null;
var dataDir=null;
var syncLimit=null;
var initLimit=null;
var tickTime=null;
var compactionThreshold=null;
var cacheSize=null;
var filesize=null;
var caching=null;
var timeout=null;
var multiplier=null;
var majorcompaction=null;
var maxsize=null;
var flushSize=null;
var handlerCount=null;
var deployedClusterHadoop = false;
var hiveServer=null;
var regionServers={};
var zookeeperNodes={};
var zookeeperAdvancedConf = null;
var hiveAdvancedConf = null;
var hbaseAdvancedConf = null;
var hClusterDeployInterval = null;
var hSetupDeployInterval = null;
var deployFlagHadoop = false;
var inProgressStatus = true;
var deploymentData = null;
var deploymentLogs = null;
var deploymentNodeLogs = null;
var lastID;
var lastData = null;
var currentClusterId = null;
var refreshTimeInterval = 10000;
var nodeStatusDeployInterval = null;
var nodeIpStatusTable = null;
var IsAutoRefreshON = false;
var IsAutoRefreshON_NodeList = false;
var IsAutoRefreshON_NodeList_SetupDetail = false;
var IsAutoRefreshON_NodeLogs = false;

var autoRefreshInterval_NodeList = null;
var autoRefreshInterval_NodeList_SetupDetail = null;
var autoRefreshInterval_NodeLogs = null;

var hadoopDeployedData_Error = null;
var clusterErrorList =  new Array();
var clusterErrorCount = 0;
var componentList = new Array();
var sharedKey_ServerPath = null;
var fileIPAddress_ServerPath = null;
var jsonFileUploadString = null;
var fileRack_ServerPath = null;
var namenodeIP = null;
var datanodesList={};
var setUpDetailNodeTable = null;
var flagCloudDetailsAvailable = false;
var limit_hadoop_cluster_name = 20;
var limit_hadoop_cluster_desc = 50;
var inspectNodeData = {};
// Object Template for Ecosystem Component Advanced Settings
function echoSystemComponentsTemplate()
{
	this.name = '';
	this.version = '';
	this.vendor = '';
	//this.status=null;
	this.source=true;
	this.downloadUrl = '';
	this.localPath= '';
	this.installationPath='';
	this.certified=false;
	this.isSubmit=false;
}

// Object Template for Hadoop Advanced Settings
function hadoopValues()
{
	this.version = '';
	this.vendor = '';
	this.source=true;
	this.downloadUrl = '';
	this.localPath= '';
	this.installationPath='';
	this.mapRedTmpDir='';
	this.nameNodePath='';
	this.hadoopTmpDir='';
	this.includeMasterAsSlave='';
	this.includeDFSFilePath='';
	this.javaHome='';
	this.excludeDFSFilePath='';
	this.includeMRFilePath='';
	this.excludeMRFilePath='';
	this.dataDir='';
	this.replicationFactorDFS='';
};

// Function to start auto-refresh call during cluster deployment
function autoRefresh(functionName) {
	hClusterDeployInterval = setInterval(function() {
		eval(functionName);
	}, refreshTimeInterval);
}

// Javascript Class for Hadoop Cluster Creation
com.impetus.ankush.hadoopCluster = {
		
		echoSystem : new Array(),
		hadoopAdvanceData:new Array(),
		
		// Function to scroll to top of the page 
		scrollToTop:function(){
			$(window).scrollTop(0);
		},
		
		// Function to Jump to an anchor tag on the page
		goToAnchor : function(tagName) {
			window.location.hash = tagName;
		},
	
		// Function to Enable tool-tips on Page Load for cluster creation
		tooltip : function() {
			$(":text").tooltip();
			$("select:not([size])").tooltip();
		},
		
		// Function to Enable tool-tips on Page Load for Hadoop Adv. Settings
		tooltipHadoopAdvSettings : function(){
			com.impetus.ankush.hadoopCluster.tooltip();
		},
		
		// Function to Enable tool-tips on Page Load for Ecosystem Components Adv. Settings
		tooltipComponentAdvSettings : function(componentName) {
			com.impetus.ankush.hadoopCluster.tooltip();
		},
	
		// Function to Open Node Details Child Page during cluster creation 
		loadNodeDetailHadoop : function(id) {
			$('#content-panel').sectionSlider('addChildPanel', {
				url : baseUrl + '/hadoop-cluster/hadoopNodeDetails',
				method : 'get',
				title : nodeHadoopJSON[id][0],
				tooltipTitle : 'Hadoop Cluster Creation',
				params : {
				},
				title : 'Node Status',
				callback : function(data) {
					com.impetus.ankush.hadoopCluster.loadHadoopNodePage(data.id);
				},
				callbackData : {id :id}
			});
		},
		
		// Function to populate Node Details on Node Details Child Page during cluster creation
		loadHadoopNodePage:function(id) {
			$("#nodeIp-Hadoop").text(nodeHadoopJSON[id][0]);
			var nodeType = "";
			if($("#namNodeHadoop-"+id).attr("checked"))
				nodeType = "NameNode";
			if($("#dataNode-"+id).attr("checked")){
				if(nodeType.length == 0)
					nodeType = "DataNode";
				else
					nodeType += " / DataNode";
			}
			if($("#secNameNode-"+id).attr("checked")){
				if(nodeType.length == 0)
					nodeType = "SecondaryNameNode";
				else
					nodeType += " / SecondaryNameNode";
			}
			$("#nodeType-Hadoop").text(nodeType);
			if(nodeHadoopJSON[id][1])
				$("#nodeStatus-Hadoop").empty().append("Available");
			else
				$("#nodeStatus-Hadoop").empty().append("In Use");

			if(nodeHadoopJSON[id][2])
				$("#nodeReachable-Hadoop").empty().append("Yes");
			else
				$("#nodeReachable-Hadoop").empty().append("No");

			if(nodeHadoopJSON[id][3])
				$("#nodeAuthentication-Hadoop").empty().append('Yes');
			else
				$("#nodeAuthentication-Hadoop").empty().append('No');

			if(nodeHadoopJSON[id][4] != "")
				$("#OSName-Hadoop").empty().append(nodeHadoopJSON[id][4]);
			else
				$("#OSName-Hadoop").empty().append("--");
			
			$("#nodeRack-Hadoop").empty().append($('#rackInfo-'+id).text());
			$("#nodeDc-Hadoop").empty().append($('#dcInfo-'+id).text());
		},
		
		// Toggle Event for Java Installation Check-box
		enableJavaHome : function(){
			if($("#install_java_checkbox").attr("checked")){
				
				$("#java_bundle_path").attr("disabled",false);
				$("#java_home_hadoop").attr("disabled",true);
			}   
			else{
				$("#java_bundle_path").attr("disabled",true);
				$("#java_home_hadoop").attr("disabled",false);
			}
			
			com.impetus.ankush.common.tooltipOriginal('java_bundle_path' , 'Java Bundle Path Location');
			com.impetus.ankush.common.tooltipOriginal('java_home_hadoop' , 'Value of JAVA_HOME environment variable');
			$('#java_home_hadoop').removeClass('error-box');
			$('#java_bundle_path').removeClass('error-box');
		},   
		
		toggleJobHistoryServer : function() {
			if($('#checkboxJobHistoryServer').attr('checked')) {
				$('#jobHistoryServer').removeAttr('disabled');
				$('#jobHistoryServer').css('background', 'white');
			}
			else {
				$('#jobHistoryServer').css('cursor', 'default');
				$('#jobHistoryServer').attr('disabled', 'disabled');
				$('#jobHistoryServer').css('background', '#EEEEEE');	
			}
		},
		
		// Toggle Event for IP Address Type Selection during cluster creation
		divShowOnClickIPAddress : function(clickId) {
			$('#div_IPRange').attr('style','display:none');
			$('#div_IPFileUpload').attr('style','display:none;');
			$('#'+clickId).attr('style','display:block;');               
		},
		
		// Toggle Event for IP Address Type Selection during cluster creation
		divShowOnClickAuthenticationType : function(clickId,actionClass){
			$("#"+clickId+"-action").show();
			$("#"+clickId+"-action").removeClass(actionClass);
			$("."+actionClass).hide();
			$("#"+clickId+"-action").addClass(actionClass);
			$('#sharedkeyHadoop').removeClass('error-box');
			$('#passwordHadoop').removeClass('error-box');
			com.impetus.ankush.common.tooltipOriginal('sharedkeyHadoop', 'Upload Shared Key');
			com.impetus.ankush.common.tooltipOriginal('passwordHadoop', 'Enter Password');
		},
		
		// Ajax call to get cluster details during cluster deployment failure
		showErrorStateCluster : function(clusterId) {
			currentClusterId = clusterId;
			var hadoopDetailsUrl = baseUrl + '/monitor/' + currentClusterId + '/details';
			$.ajax({
				'type' : 'GET',
				'url' : hadoopDetailsUrl,
				'contentType' : 'application/json',
				'dataType' : 'json',
				'success' : function(result) {
					hadoopDeployedData_Error = result;
					com.impetus.ankush.hadoopCluster.fillData_ClusterError();
				}
			});
		},
		
		// Function to populate cluster details during cluster deployment failure
		fillData_ClusterError : function() {
			$("#hadoop_cluster_name").val(hadoopDeployedData_Error.output.clusterName);
			$("#hadoop_cluster_desc").val(hadoopDeployedData_Error.output.description);
			$("#usernameHadoop").val(hadoopDeployedData_Error.output.authConf.username);
			
			if(hadoopDeployedData_Error.output.authConf.type == "password") {
				$("#passwordHadoop").val(hadoopDeployedData_Error.output.authConf.password);
//				$('#throughPassword').attr("checked", "checked");
				$('#throughPassword').addClass('active');
				$('#throughSharedKey').removeClass('active');
				com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('throughPassword','passwordDivs');
			}
			else {
				$("#filePathDb").val(hadoopDeployedData_Error.output.authConf.password);
				$("#filePathDb").removeClass('input-large');
				$('#throughPassword').removeClass('active');
				$('#throughSharedKey').addClass('active');
				$('#uploadframesharekeyDbPackage').css("width", "100%");
				$('#fileBrowseDb').css("display", "none");
				$('#uploadframesharekeyDb').css("display", "none");
				com.impetus.ankush.hadoopCluster.divShowOnClickAuthenticationType('throughSharedKey','passwordDivs');
			}

			$('#install_java_checkbox').css('display', 'none');
			$('#hadoopJavaInstallationFields').removeClass('input-prepend');
			
			if(hadoopDeployedData_Error.output.javaConf.install) {
//				$('#install_java_checkbox').attr("checked", "checked");
				$('#java_bundle_path').val(hadoopDeployedData_Error.output.javaConf.javaBundle);
				$('#java_home_hadoop').val('');
			}
			else {
//				$('#install_java_checkbox').removeAttr("checked");
				$('#java_bundle_path').val('N/A');
				$('#java_home_hadoop').val(hadoopDeployedData_Error.output.javaConf.javaHomePath);
			}

			if(hadoopDeployedData_Error.output.environment == "In Premise") {
				$('#localEnv').attr("checked", "checked");
				$('#cloudEnv').removeAttr("checked");
				com.impetus.ankush.hadoopCluster.fillNodeDetails_ClusterError();
			}
			else {
				$('#cloudEnv').attr("checked", "checked");
				$('#localEnv').removeAttr("checked");
				$('#localEnv-action').css("display", "none");
				com.impetus.ankush.hadoopCluster.fillCloudDetails_ClusterError(hadoopDeployedData_Error.output.cloudConf);
			}
			
			if(hadoopDeployedData_Error.output.rackEnabled) {
				$('#filePath_RackFile').val(hadoopDeployedData_Error.output.rackFileName);
			}
			else {
				$('#filePath_RackFile').val('No Mapping available');
			}
			if(hadoopDeployedData_Error.output.components.Hadoop == null) {
				hadoopDeployedData_Error.output.components.Hadoop = hadoopDeployedData_Error.output.components.Hadoop2;
			}
			com.impetus.ankush.hadoopCluster.fillEcosystemDetails_ClusterError(hadoopDeployedData_Error.output.components);

			if(hadoopDeployedData_Error.output.state == 'error') {
				if(hadoopDeployedData_Error.output.errors != null) {
					$("#error-div-hadoop").css("display", "block");
					var arrErrors = [];
					for(var iCount = 0; iCount < (Object.keys(hadoopDeployedData_Error.output.errors)).length; iCount++) {
						arrErrors.push(hadoopDeployedData_Error.output.errors[(Object.keys(hadoopDeployedData_Error.output.errors))[iCount]]);					   
					}
					com.impetus.ankush.validation.showAjaxCallErrors(arrErrors, 'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
					com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
					$('#commonDeleteButtonHadoop').removeAttr('disabled');
				}	
			}
			$('#hadoop_cluster_name').attr('disabled', true);
			$('#hadoop_cluster_desc').attr('disabled', true);
			$('#usernameHadoop').attr('disabled', true);
			$('#passwordHadoop').attr('disabled', true);
			$('#throughSharedKey').attr('disabled', true);
			$('#throughPassword').attr('disabled', true);
			$('#filePathDb').attr('disabled', true);
			$('#install_java_checkbox').attr('disabled', true);
			$('#java_bundle_path').attr('disabled', true);
			$('#java_home_hadoop').attr('disabled', true);
			$('#localEnv').attr('disabled', true);
			$('#cloudEnv').attr('disabled', true);
			$('#hadoopClusterDeploy').attr('disabled', true);
			$('#filePath_RackFile').attr('disabled', true);
			$('#filePath_RackFile').css('cursor', 'default');
			
//			$('#hadoopClusterDeploy').css('display', 'none');
			
			$('#chkBxRackEnable').css('display', 'none');
			$('#fileBrowse_RackFile').css('display', 'none');
			$('#uploadframeRackFile').css('display', 'none');
			$('#divRackMapping').removeClass('input-prepend');
			
					
		},
		
		// Function to populate node details during cluster deployment failure
		fillNodeDetails_ClusterError : function() {
			var nodeList = "";
			clusterErrorCount = 0;
			$("#nodeTable").show();
			$("#btnAvailable_HSN").css('display', 'none');
			$("#btnSelected_HSN").css('display', 'none');
			$("#searchNodeTableHadoop").css('display', 'none');
			$("#HadoopCreate_NodeIPTable").removeClass('span7');
			$("#HadoopCreate_NodeIPTable").css('float', 'right');
			if(addNodeHadoopTable != null) {
				addNodeHadoopTable.fnClearTable();
			}
			$('#divNodeTableHadoop').css("display", "block");
			nodeHadoopJSON = hadoopDeployedData_Error.output.nodes;
			for(var i = 0; i <  hadoopDeployedData_Error.output.nodes.length; i++) {
				var node = hadoopDeployedData_Error.output.nodes[i];
				nodeList += node.publicIp + " ";
				var checkNodeHadoop= '<input type="checkbox" id="hadoopCheckBox-'+i+'" onclick="com.impetus.ankush.hadoopCluster.updateHeaderCheckBox(\'hadoopCheckBox-'+i+'\');"/>';
				var ipHadoop=node.publicIp;
				var nameNodeValue='<input id="namNodeHadoop-'+i+'" type="radio" name="namNodeHadoop"/>';
				var secNameNode='<input type="radio" id="secNameNode-'+i+'"/>';
				var dataNode='<input type="checkbox" id="dataNode-'+i+'"/>';
				var dcInfo='<span style="font-weight:bold;" id="dcInfo-'+i+'">'+node.datacenter+'</span>';
				var rackInfo='<span style="font-weight:bold;" id="rackInfo-'+i+'">'+node.rack+'</span>';
				var os='<span style="font-weight:bold;" id="os-'+i+'">'+node.os+'</span>';
				var navigationHadoop='<a href="#"><img id="navigationImgHadoop'+i+'" src="'+baseUrl+'/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.hadoopCluster.showNodeErrorDetails('+i+');"/></a>';
				addNodeHadoopTable.fnAddData([
				                              checkNodeHadoop,
				                              ipHadoop,
				                              nameNodeValue,
				                              secNameNode,
				                              dataNode,
				                              dcInfo,
				                              rackInfo,
				                              os,
				                              navigationHadoop                                 
				                              ]);
  
				$("#hadoopCheckBox-"+i).attr('checked', 'checked');
				$("#nodeCheckHead").attr('checked', 'checked');

				var nodeType = node.type;
				var hadoopObj = hadoopDeployedData_Error.output.components.Hadoop;
				
				if(hadoopObj == null) {
					hadoopObj = hadoopDeployedData_Error.output.components.Hadoop2;
				}
				if(hadoopObj.namenode.publicIp == node.publicIp) {
					$("#namNodeHadoop-"+i).attr('checked', 'checked');
				}
				
				if(nodeType.indexOf('DataNode') != -1) {
					$("#dataNode-"+i).attr('checked', 'checked');
				}
				if(nodeType.indexOf('SecondaryNameNode') != -1) {
					$("#secNameNode-"+i).attr('checked', 'checked');
				}
				
				$('#hadoopCheckBox-'+i).attr('disabled', true);
				$("#namNodeHadoop-"+i).attr('disabled', true);
				$("#secNameNode-"+i).attr('disabled', true);
				$("#dataNode-"+i).attr('disabled', true);
				if(node.errors != null && (Object.keys(node.errors)).length != 0){
					$('#rowId_' + i).addClass('error-row');
//					errorNodesCount++;
//					clusterErrorMessage = errorNodesCount + ". Deployment failed on node : " + node.publicIp;
//					$("#popover-content").append('<div class="errorLineDiv"><a style="color: #5682C2;" href="javascript:com.impetus.ankush.hadoopCluster.goToAnchor(\'nodeIpTableHadoop_Jump\');">'
//							+clusterErrorMessage+'</a></br></div>');
				}
			}
			if(hadoopDeployedData_Error.output.ipPattern != null) {
				//$('#ipRange').attr('checked', true);
				$('#ipFile').removeClass('active');
				$('#ipRange').addClass('active');
				$('#ipRangeHadoop').val(hadoopDeployedData_Error.output.ipPattern);
				$('#ipRangeHadoop').css("width", "100%");
				com.impetus.ankush.hadoopCluster.divShowOnClickIPAddress('div_IPRange');
			}
			else {
				//	$('#ipFile').attr('checked', true);
				$('#ipRange').removeClass('active');
				$('#ipFile').addClass('active');
				$('#filePath_IPAddressFile').val(hadoopDeployedData_Error.output.patternFile);
				$('#uploadframeIPAddressFile_Form').css("width", "100%");
				$('#fileBrowse_IPAddressFile').css("display", "none");
				$('#uploadframeIPAddressFile').css("display", "none");
				com.impetus.ankush.hadoopCluster.divShowOnClickIPAddress('div_IPFileUpload');
			}
			$('#ipRange').attr('disabled', true);
			$('#ipFile').attr('disabled', true);
			$('#ipRangeHadoop').attr('disabled', true);
			$('#filePath_IPAddressFile').attr('disabled', true);
			$("#nodeRetrieveHadoop").attr('disabled', true);
			$("#nodeCheckHead").attr('disabled', true);
			$("#inspectNodeBtn").attr('disabled', true);			
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
			data.username = $('#usernameHadoop').val();
			if($('#throughPassword').hasClass('active')){
				data.password=$('#passwordHadoop').val();
				data.privateKey = '';	
			}else{
				data.privateKey = sharedKey_ServerPath;
				data.password =   '' ;
			}
			com.impetus.ankush.inspectNodesCall(data,id,'nodeRetrieveHadoop');
		},
		// Function to Open Node Details Child Page during cluster deployment failure
		showNodeErrorDetails : function(i) {
			
			$('#content-panel').sectionSlider('addChildPanel', {
				url : baseUrl + '/hadoop-cluster/hadoopNodeDetails',
				method : 'get',
				title : hadoopDeployedData_Error.output.nodes[i].publicIp,
				tooltipTitle : 'Hadoop Node Details',
				params : {
				},
				callback : function() {
					com.impetus.ankush.hadoopCluster.fillNodeChildPage_ClusterError(hadoopDeployedData_Error.output.nodes[i]);
				},
			});
			
//			$('#content-panel').sectionSlider('addChildPanel', {
//				url : baseUrl + '/hadoop-cluster/hadoopErrorNodeDetails',
//				method : 'get',
//				params : {

//				},
//				title : 'Node Status',
//				callback : function() {
//					com.impetus.ankush.hadoopCluster.fillNodeChildPage_ClusterError(hadoopDeployedData_Error.output.nodes[i]);
//				},
//				callbackData : { }

//			});
		},
		
		// Function to populate Node Details on Node Details Child Page during cluster deployment failure
		fillNodeChildPage_ClusterError : function(nodeObject) {
			
			$("#nodeIp-Hadoop").text(nodeObject.publicIp);
			$("#nodeType-Hadoop").text(nodeObject.type);
			$("#nodeStatus-Hadoop").empty().append("Available");
			$("#nodeReachable-Hadoop").empty().append("Yes");
			$("#nodeAuthentication-Hadoop").empty().append('Yes');
			$("#OSName-Hadoop").empty().append(nodeObject.os);
			$("#nodeRack-Hadoop").empty().append(nodeObject.rack);
			$("#nodeDc-Hadoop").empty().append(nodeObject.datacenter);
			
//			$('#NSD_nodeDetailHead').html('').text(nodeObject.publicIp);
//			$('#NSD_nodePrivateIP').html('').text(nodeObject.privateIp);
//			$('#NSD_nodeStatus').html('').text(nodeObject.message);
//			$('#NSD_nodeType').html('').text(com.impetus.ankush.hadoopCluster.getNodeTypeValue(nodeObject));
//			$('#NSD_nodeRack').html('').text(nodeObject.rackInfo);
//			$('#NSD_nodeOS').html('').text(nodeObject.os);

//			$('#NSD_nodeErrorList').empty();
//			for(var i = 0; i < (Object.keys(nodeObject.errors)).length; i ++) {

//				$('#NSD_nodeErrorList').append('<div class="row-fluid"><div class="span3 "><label class="text-right form-label">'
//						+ (Object.keys(nodeObject.errors))[i]
//						+ ':</label></div><div class="span8"><label class="text-left data" style="color: black;">'
//						+ nodeObject.errors[(Object.keys(nodeObject.errors))[i]]
//						+'</label></div></div>');
//			}

//			$('#NSD_nodeDeploymentProgressDiv').empty();
//			for ( var i = nodeObject.logs.length-1; i >= 0; i--) {
//				if(nodeObject.logs[i].type == 'error'){
//					$('#NSD_nodeDeploymentProgressDiv').append('<div style="color:red">' +
//							nodeObject.logs[i].longMessage + '</div>');
//				}else{
//					$('#NSD_nodeDeploymentProgressDiv').append('<div>' +
//							nodeObject.logs[i].longMessage + '</div>');
//				}
//			}
		},
		
		// Function to populate Hadoop Ecosytem details during cluster deployment failure
		fillEcosystemDetails_ClusterError : function(data_Components) {
			$('#hadoopReplicationFactor').val(data_Components.Hadoop.advancedConf.replicationFactorDFS);
			$("#hadoopVendor").append("<option value='" + data_Components.Hadoop.componentVendor + "'>" + data_Components.Hadoop.componentVendor + "</option>");
			$('#hadoopVendor').val(data_Components.Hadoop.componentVendor);
			$("#hadoopVersion").append("<option value='" + data_Components.Hadoop.componentVersion + "'>" + data_Components.Hadoop.componentVersion + "</option>");
			$('#hadoopVersion').val(data_Components.Hadoop.componentVersion);
			$('#hadoopAdvanceSettings').attr('onclick', 'com.impetus.ankush.hadoopCluster.openHadoopAdvSettingsPage_ClusterError();');

			for(var componentName in data_Components) {
				var checkNodeHadoop='<input type="checkbox" checked="true" disabled="disabled" id="hadoopEcoSystemCheckBox-'+componentName+'" / >';
				var componentNameTable='<span style="font-weight:bold;" id="componentName-'+componentName+'">'+componentName+'</span>';
				var componentVendor='<select class="ecoSystemSelectVendor" disabled="disabled" id="componentVendor-'+componentName+'" onchange="com.impetus.ankush.hadoopCluster.componentVendorChange(this);"></select>';
				var componentVersion='<select class="ecoSystemSelect" disabled="disabled" id="componentVersion-'+componentName+'" onchange="com.impetus.ankush.hadoopCluster.componentVersionChange(this);"></select>';
				var status='<span style="font-weight:bold;" id="componentStatus-'+componentName+'">'+data_Components[componentName].certification+'</span>';
				var navigationHadoop='<div><a href="#" id="navigationImgHadoopComponent-'+componentName+'"><img  src="'+baseUrl+'/public/images/icon-chevron-right.png" /></a></div>';
				if(!(componentName == 'Hadoop' || componentName =='Hadoop2')){
					var addId_Ecosystem = hadoopCreate_EcosystemTable.fnAddData([
					                                                             checkNodeHadoop,
					                                                             componentNameTable,
					                                                             componentVendor,
					                                                             componentVersion,
					                                                             status,
					                                                             navigationHadoop
					                                                             ]);
					$("#componentVendor-"+componentName).append("<option value='"
							+ data_Components[componentName].componentVendor + "'>"
							+ data_Components[componentName].componentVendor + "</option>");
					$("#componentVersion-"+componentName).append("<option value='"
							+ data_Components[componentName].componentVersion + "'>"
							+ data_Components[componentName].componentVersion + "</option>");
					$("#componentVendor-"+componentName).val(data_Components[componentName].componentVendor);
					$("#componentVersion-"+componentName).val(data_Components[componentName].componentVersion);
//					var theNode = hadoopCreate_EcosystemTable.fnSettings().aoData[addId_Ecosystem[0]].nTr;
//					theNode.setAttribute('id', 'componentRow-'+ componentName);

					$("#navigationImgHadoopComponent-"+componentName).css({
						"cursor":"pointer"
					});

					$("#navigationImgHadoopComponent-"+componentName).attr('href','javascript:com.impetus.ankush.hadoopCluster.openComponentDetailsPage_ClusterError(\'' + componentName + '\');');
				}
			}
			$('#ecoSystemTableSearchBox').css('display', 'none');
			$('#nodeCheckHead_Ecosystem').attr('checked', true);
			$('#nodeCheckHead_Ecosystem').attr('disabled', true);
			$('#hadoopReplicationFactor').attr('disabled', true);
			$('#hadoopVendor').attr('disabled', true);
			$('#hadoopVersion').attr('disabled', true);
		},
	
		// Function to Open Hadoop Adv. Details Child Page during cluster deployment failure
		openHadoopAdvSettingsPage_ClusterError : function() {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster/HadoopDetails',
				method : 'get',
				params : {
				},
				title : 'Hadoop Advanced Settings',
				callback : function(data) {
					com.impetus.ankush.common.pageStyleChange();
					com.impetus.ankush.hadoopCluster.fillHadoopAdvSettings_ClusterError(hadoopDeployedData_Error.output.components.Hadoop);
					com.impetus.ankush.hadoopCluster.disableFields_HadoopAdvSettings();
				},
			});
		},
		
		// Function to Open Component Details Child Page during cluster deployment failure
		openComponentDetailsPage_ClusterError : function(componentName) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster/componentDetails/'+componentName,
				method : 'get',
				params : {
				},
				title : componentName + ' Advanced Settings',
				callback : function() {
					com.impetus.ankush.common.pageStyleChange();
					com.impetus.ankush.hadoopCluster.fillComponentsDetails_ClusterError(componentName);
					com.impetus.ankush.hadoopCluster.disableFields_ComponentsDetails(componentName);

				},
			});
		},
		
		// Function to populate Hadoop Adv. Details during cluster deployment failure
		fillHadoopAdvSettings_ClusterError : function(data_HadoopSettings) {

			if(data_HadoopSettings.advancedConf.includes3) {
				$("#s3Div").css("display", "block");
				$("#includeS3SupportHadoop").attr("checked", true);
				$("#acessKeyS3").val(data_HadoopSettings.advancedConf.s3AccessKey);
				$("#secretKeyS3").val(data_HadoopSettings.advancedConf.s3SecretKey);
			}
			else {
				$("#s3Div").css("display", "none");
			}
			if(data_HadoopSettings.advancedConf.includes3n) {
				$("#s3nDiv").css("display", "block");
				$("#includeS3nSupportHadoop").attr("checked", true);
				$("#acessKeyS3n").val(data_HadoopSettings.advancedConf.s3nAccessKey);           
				$("#secretKeys3n").val(data_HadoopSettings.advancedConf.s3nSecretKey);
			}
			else {
				$("#s3nDiv").css("display", "none");
			}
			com.impetus.ankush.hadoopCluster.fillCommonComponentsDetails_ClusterError('Hadoop');
			$("#dfsReplicationHadoop").val(data_HadoopSettings.advancedConf.replicationFactorDFS);
			$("#nameNodePathHadoop").val(data_HadoopSettings.advancedConf.nameNodePath);
			$("#dataNodePathHadoop").val(data_HadoopSettings.advancedConf.dataDir);
			$("#mapredTempPathHadoop").val(data_HadoopSettings.advancedConf.mapRedTmpDir);
			$("#tempPathHadoop").val(data_HadoopSettings.advancedConf.hadoopTmpDir);
			
			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected(data_HadoopSettings.componentVersion)) {
				$("#resourceManagerNode").append('<option value="'+data_HadoopSettings.resourceManagerNode.publicIp+'">'+data_HadoopSettings.resourceManagerNode.publicIp+'</option>');
				if(data_HadoopSettings.startJobHistoryServer) {
					$("#checkboxJobHistoryServer").attr("checked", true);
					$("#jobHistoryServer").append('<option value="'+data_HadoopSettings.jobHistoryServerNode.publicIp+'">'+data_HadoopSettings.jobHistoryServerNode.publicIp+'</option>');
				} else {
					$("#checkboxJobHistoryServer").removeAttr("checked");
				}
				
				if(data_HadoopSettings.webProxyEnabled) {
					
					$("#webAppProxyNode").append('<option value="'+data_HadoopSettings.webAppProxyNode.publicIp+'">'+data_HadoopSettings.webAppProxyNode.publicIp+'</option>');
					$("#webAppProxyNode").val(data_HadoopSettings.webAppProxyNode.publicIp);
					
					$("#webAppProxyPort").val(data_HadoopSettings.webAppProxyPort);
					$('#webAppProxyDiv').css('display', 'block');
				} else {
					$('#webAppProxyDiv').css('display', 'none');
				}
				
				if(data_HadoopSettings.advancedConf.isHAEnabled) {
					var obj = data_HadoopSettings.advancedConf;
					$("#nameserviceId").val(obj.nameserviceId);
					$("#nameNodeId1").val(obj.nameNodeId1);
					$("#nameNodeId2").val(obj.nameNodeId2);
					$("#journalNodeEditsDir").val(obj.journalNodeEditsDir);
					if(!obj.automaticFailoverEnabled) {
						$('#automaticFailoverEnabled').removeClass('active');
						$('#automaticFailoverDisabled').addClass('active');
					}
					$("#haStandByNode").append('<option value="'+obj.standByNamenode+'">'+obj.standByNamenode+'</option>');
					
					if(data_HadoopSettings.journalNodes != null) {
						for(var iCount = 0 ; iCount < data_HadoopSettings.journalNodes.length ; iCount++){
							$("#haJournalNodes").append(
										"<option value='"
										+ data_HadoopSettings.journalNodes[iCount].publicIp
										+ "' selected disabled='disabled'>"
										+ data_HadoopSettings.journalNodes[iCount].publicIp
										+ "</option>");
						}
						$("#haJournalNodes").multiselect({
							buttonClass: 'btn',
							maxHeight: '150',
							buttonText: function(options, select) {
								if (options.length == 0) {
									return 'None selected <b class="caret"></b>';
								}
								else if (options.length > 4) {
									return options.length + ' selected <b class="caret"></b>';
								}
								else {
									var selected = '';
									options.each(function() {
										selected += $(this).text() + ', ';
									});
										return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
									}
								},
						});
						//$(".multiselect").multiselect("disable");
						$(".multiselect").click(function(){
							$(".dropdown-menu").toggle('fast');
						});
						$(document).click(function(e){
							$(".dropdown-menu").hide();
						});
						$(".multiselect").click(function(e){
							e.stopPropagation();
						});	
						
					}
				}
				else {
					$('#hadoopHADiv').css('display', 'none');	
				}
			}
			else {
				$('#hadoop2Settings').css('display', 'none');
			}
		},
		
		// Function to populate Ecosystem Components during cluster deployment failure
		fillCommonComponentsDetails_ClusterError : function (componentName) {
			var componentDetails = new Object();
			componentDetails = hadoopDeployedData_Error.output.components[componentName];

			$('#vendor-'+componentName).append("<option value='" + componentDetails.componentVendor + "'>" + componentDetails.componentVendor + "</option>");
			$('#vendor-'+componentName).val(componentDetails.componentVendor);
			$('#version-'+componentName).append("<option value='" + componentDetails.componentVersion + "'>" + componentDetails.componentVersion + "</option>");
			$('#version-'+componentName).val(componentDetails.componentVersion);
			if(componentDetails.localBinaryFile == ""){
				$("#path-"+componentName).val(componentDetails.tarballUrl);
				$("#downloadRadio-"+componentName).addClass('active');
				$("#localPathRadio-"+componentName).removeClass('active');
			} else {
				$("#path-"+componentName).val(componentDetails.localBinaryFile);
				$("#localPathRadio-"+componentName).addClass('active');
				$("#downloadRadio-"+componentName).removeClass('active');
			}
			$("#installationPath-"+componentName).val(componentDetails.installationPath);
		},
		
		// Function to disable Hadoop Adv Settings during cluster deployment failure
		disableFields_HadoopAdvSettings : function() {
			com.impetus.ankush.hadoopCluster.disableFields_CommonAdvSettings('Hadoop');
			$('#dfsReplicationHadoop').attr('disabled', true);
			$('#nameNodePathHadoop').attr('disabled', true);
			$('#dataNodePathHadoop').attr('disabled', true);
			$('#mapredTempPathHadoop').attr('disabled', true);
			$('#tempPathHadoop').attr('disabled', true);
			$('#includeS3SupportHadoop').attr('disabled', true);
			$('#acessKeyS3').attr('disabled', true);
			$('#secretKeyS3').attr('disabled', true);
			$('#includeS3nSupportHadoop').attr('disabled', true);
			$('#acessKeyS3n').attr('disabled', true);
			$('#secretKeys3n').attr('disabled', true);
			
			$('#hadoopHAEnable').attr('disabled', true);
			$('#hadoopHADisable').attr('disabled', true);
			$('#haStandByNode').attr('disabled', true);
			$('#haJournalNodes').attr('disabled', true);
			$('#automaticFailoverEnabled').attr('disabled', true);
			$('#automaticFailoverDisabled').attr('disabled', true);
			$('#webAppProxyEnable').attr('disabled', true);
			$('#webAppProxyDisable').attr('disabled', true);
			
		},
		
		// Function to populate component details during cluster deployment failure
		fillComponentsDetails_ClusterError : function(componentName) {
			com.impetus.ankush.hadoopCluster.fillCommonComponentsDetails_ClusterError(componentName);
			com.impetus.ankush.hadoopCluster.showComponentAdvFields(componentName);
			com.impetus.ankush.hadoopCluster.fillComponentAdvDetails_ClusterError(componentName);
		},
		
		// Function to populate component advanced settings during cluster deployment failure
		fillComponentAdvDetails_ClusterError : function(componentName) {
			$('.tooltiptext').css('display', 'none');
			var componentAdvData = new Object();
			componentAdvData = hadoopDeployedData_Error.output.components[componentName].advancedConf;
			
//			var nodeCount = hadoopDeployedData_Error.output.components[componentName].nodes.length;
//			var componentNodes = hadoopDeployedData_Error.output.components[componentName].nodes;
			
			var nodeCount = hadoopDeployedData_Error.output.components[componentName].compNodes.length;
			var componentNodes = hadoopDeployedData_Error.output.components[componentName].compNodes;
			
			if(componentName=="Hive"){
				$("#hiveServerHadoop").empty();
				$("#connectionPassword").val(componentAdvData['javax.jdo.option.ConnectionPassword']);
				$("#connectionURL").val(componentAdvData['javax.jdo.option.ConnectionURL']);
				$("#connectionDriverName").val(componentAdvData['javax.jdo.option.ConnectionDriverName']);
				$("#connectionUserName").val(componentAdvData['javax.jdo.option.ConnectionUserName']);

				if(nodeCount != null){
					for(var i = 0 ; i < nodeCount ; i++){
						$("#hiveServerHadoop").append('<option value="'+componentNodes[i].publicIp+'">'+componentNodes[i].publicIp+'</option>');
					}
				}
			}
			if(componentName=="Zookeeper"){
				$("#zookeeperNodes").empty();
				$("#clientPort").val(componentAdvData['clientPort']);           
				$("#dataDir").val(componentAdvData['dataDir']);
				$("#syncLimit").val(componentAdvData['syncLimit'] + " miliseconds");
				$("#initLimit").val(componentAdvData['initLimit'] + " miliseconds");
				
				$("#tickTime").val(componentAdvData['tickTime'] + " miliseconds");

				if(nodeCount != null){
					for(var i = 0 ; i < nodeCount ; i++){
						$("#zookeeperNodes").append(
								"<option value='"
								+ componentNodes[i].publicIp
								+ "' selected disabled='disabled'>"
								+ componentNodes[i].publicIp
								+ "</option>");   
					}
					$("#zookeeperNodes").multiselect({
						buttonClass: 'btn',
						maxHeight: '150',
						buttonText:function(options, select) {
							if (options.length == 0) {
								return 'None selected <b class="caret"></b>';
							}
							else if (options.length > 4) {
								return options.length + ' selected <b class="caret"></b>';
							}
							else {
								var selected = '';
								options.each(function() {
									selected += $(this).text() + ', ';
								});
								return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';   
							}
						},
					});
					$(".multiselect").click(function(){
						$(".dropdown-menu").toggle('fast');
					});
					$(document).click(function(e){
						$(".dropdown-menu").hide();
					});
					$(".multiselect").click(function(e){
						e.stopPropagation();
					});
				}
			}
			if(componentName=="Hbase"){
				$("#hbaseMasterHadoop").empty();
				$("#compactionThreshold").val(componentAdvData['hbase.hstore.compactionThreshold']);
				$("#cacheSize").val(componentAdvData['hfile.block.cache.size'] + " %");
				$("#filesize").val(componentAdvData['hbase.hregion.max.filesize'] + " bytes");
				$("#caching").val(componentAdvData['hbase.client.scanner.caching']);
				$("#timeout").val(componentAdvData['zookeeper.session.timeout'] + " miliseconds");
				$("#multiplier").val(componentAdvData['hbase.hregion.memstore.block.multiplier']);
				$("#majorcompaction").val(componentAdvData['hbase.hregion.majorcompaction'] + " miliseconds");
				$("#maxsize").val(componentAdvData['hbase.client.keyvalue.maxsize'] + " bytes");
				$("#flushSize").val(componentAdvData['hbase.hregion.memstore.flush.size'] + " bytes");
				$("#handlerCount").val(componentAdvData['hbase.regionserver.handler.count']);

//				if(nodeCount != null){
					$("#hbaseMasterHadoop").append(
							"<option value='"
							+ hadoopDeployedData_Error.output.components[componentName].hbaseMasterNode.publicIp
							+ "' selected>"
							+ hadoopDeployedData_Error.output.components[componentName].hbaseMasterNode.publicIp
							+ "</option>");
					var hbaseRSCount = hadoopDeployedData_Error.output.components[componentName].hbaseRegionServerNodes.length;
					for(var i = 0 ; i < hbaseRSCount ; i++){
						$("#regionServersHadoop").append(
								"<option value='"
								+ hadoopDeployedData_Error.output.components[componentName].hbaseRegionServerNodes[i].publicIp
								+ "' selected disabled='disabled'>"
								+ hadoopDeployedData_Error.output.components[componentName].hbaseRegionServerNodes[i].publicIp
								+ "</option>");   
					}

					$("#regionServersHadoop").multiselect({
						buttonClass: 'btn',
						maxHeight: '150',
						buttonText:function(options, select) {
							if (options.length == 0) {
								return 'None selected <b class="caret"></b>';
							}
							else if (options.length > 4) {
								return options.length + ' selected <b class="caret"></b>';
							}
							else {
								var selected = '';
								options.each(function() {
									selected += $(this).text() + ', ';
								});
								return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';   
							}
						},
					});
					$(".multiselect").click(function(){
						$(".dropdown-menu").toggle('fast');
					});
					$(document).click(function(e){
						$(".dropdown-menu").hide();
					});
					$(".multiselect").click(function(e){
						e.stopPropagation();
					});
//				}
			}
		},
		
		// Function to disable Component details during cluster deployment failure
		disableFields_ComponentsDetails : function(componentName) {
			com.impetus.ankush.hadoopCluster.disableFields_CommonAdvSettings(componentName);
			com.impetus.ankush.hadoopCluster.disableFields_ComponentAdvSettings(componentName);
		},
		
		// Function to disable Component Common Settings during cluster deployment failure
		disableFields_CommonAdvSettings : function(componentName) {

			$('#revertAdv-'+componentName).attr('disabled', true);
			$('#applyAdv-'+componentName).attr('disabled', true);
			$('#vendor-'+componentName).attr('disabled', true);
			$('#version-'+componentName).attr('disabled', true);
			$('#path-'+componentName).attr('disabled', true);
			$('#downloadRadio-'+componentName).attr('disabled', true);
			$('#localPathRadio-'+componentName).attr('disabled', true);
			$("#installationPath-"+componentName).attr('disabled', true);
		},
		
		// Function to disable Component Advanced Settings during cluster deployment failure
		disableFields_ComponentAdvSettings : function(componentName) {
			if(componentName=="Hive"){
				$("#connectionPassword").attr("disabled", true);
				$("#connectionURL").attr("disabled", true);
				$("#connectionDriverName").attr("disabled", true);
				$("#connectionUserName").attr("disabled", true);
				$("#hiveServerHadoop").attr("disabled", true);
				$("#connectionPassword_PlainText").attr("disabled", true);

			}
			if(componentName=="Zookeeper"){
				$("#zookeeperNodes").attr("disabled", true);
				$("#clientPort").attr("disabled", true);           
				$("#dataDir").attr("disabled", true);
				$("#syncLimit").attr("disabled", true);
				$("#initLimit").attr("disabled", true);
				$("#tickTime").attr("disabled", true);
			}
			if(componentName=="Hbase"){
				$("#hbaseMasterHadoop").attr("disabled", true);
				$("#compactionThreshold").attr("disabled", true);
				$("#cacheSize").attr("disabled", true);
				$("#filesize").attr("disabled", true);
				$("#caching").attr("disabled", true);
				$("#timeout").attr("disabled", true);
				$("#multiplier").attr("disabled", true);
				$("#majorcompaction").attr("disabled", true);
				$("#maxsize").attr("disabled", true);
				$("#flushSize").attr("disabled", true);
				$("#handlerCount").attr("disabled", true);
			}
		},

		// Function to populate Cloud details during cluster deployment failure
		fillCloudDetails_ClusterError : function(data_cloudConf) {
			if(hadoopDeployedData_Error.output.environment == 'Cloud') {
				$('#clouddetails').show();
				$('#cloudEnv-action').show();   

				$("#cloud_service_provider").append("<option value='" + data_cloudConf.providerName + "'>" + data_cloudConf.providerName + "</option>");
				$('#cloud_service_provider').val(data_cloudConf.providerName);
				$('#cloudUsername').val(data_cloudConf.username);
				$('#cloudPassword').val(data_cloudConf.password);
				$('#cluster_size_cloud').val(data_cloudConf.clusterSize);

				$('#keyPairsSelect').append("<option value='" + data_cloudConf.keyPairs + "'>" + data_cloudConf.keyPairs + "</option>");
				$('#keyPairsSelect').val(data_cloudConf.keyPairs);

				$('#securityGroupsSelect').append("<option value='" + data_cloudConf.securityGroup + "'>" + data_cloudConf.securityGroup + "</option>");
				$('#securityGroupsSelect').val(data_cloudConf.securityGroup);

				$('#regionSelect').append("<option value='" + data_cloudConf.region + "'>" + data_cloudConf.region + "</option>");
				$('#regionSelect').val(data_cloudConf.region);

				$('#zoneSelect').append("<option value='" + data_cloudConf.zone + "'>" + data_cloudConf.zone + "</option>");
				$('#zoneSelect').val(data_cloudConf.zone);

				$('#instanceTypeSelect').append("<option value='" + data_cloudConf.instanceType + "'>" + data_cloudConf.instanceType + "</option>");
				$('#instanceTypeSelect').val(data_cloudConf.instanceType);

				$('#machine_image_hadoop').val(data_cloudConf.machineImage);

				if(data_cloudConf.autoTerminate) {
					$("#autoTerminateYes").attr("checked", true);
					$('#timeout_interval_hadoop').val(data_cloudConf.timeOutInterval);
					$("#autoTerminateYes-action").css('display', 'block');
				}
				else {
					$("#autoTerminateYes").attr("checked", false);
					$("#autoTerminateNo").attr("checked", true);
					$('#timeout_interval_hadoop').val('--');
					$("#autoTerminateYes-action").css('display', 'none');
				}

				$('#cloud_service_provider').attr("disabled", true);
				$('#cloudUsername').attr("disabled", true);
				$('#cloudPassword').attr("disabled", true);
				$('#cluster_size_cloud').attr("disabled", true);
				$('#keyPairsSelect').attr("disabled", true);
				$('#securityGroupsSelect').attr("disabled", true);
				$('#regionSelect').attr("disabled", true);
				$('#zoneSelect').attr("disabled", true);
				$('#instanceTypeSelect').attr("disabled", true);
				$('#machine_image_hadoop').attr("disabled", true);
				$('#autoTerminateYes').attr("disabled", true);
				$('#autoTerminateNo').attr("disabled", true);
				$('#timeout_interval_hadoop').attr("disabled", true);
				$('#btnGetCloudDetails_HadoopCreate').attr("disabled", true);
			}
		},
		
		// Function to initialize Data Tables used during Cluster deployment 
		initHadoop:function(){
			hadoopCreate_EcosystemTable = null;
			addNodeHadoopTable = null;
			       
			$("#hadoopCreate_hadoopEcoSystemTable").dataTable().fnDestroy();
			hadoopCreate_EcosystemTable = $("#hadoopCreate_hadoopEcoSystemTable").dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : true,
				"bInfo" : false,
				"aaSorting": [[ 1, "asc" ]],
				"aoColumns" : [
				               {'bSortable' : false,},
				               {},
				               {'bSortable' : false,},
				               {'bSortable' : false,},
				               {},
				               {'bSortable' : false,},
				               ],
			});
			$("#hadoopCreate_hadoopEcoSystemTable_filter").hide();
			hadoopCreate_hadoopEcoSystemTable = $('#hadoopCreate_hadoopEcoSystemTable').dataTable();
			$('#ecoSystemTableSearchBox').keyup(function() {
				hadoopCreate_hadoopEcoSystemTable.fnFilter($(this).val());
			});
			     
			$("#hadoopCreate_nodeIpTableHadoop").dataTable().fnDestroy();
			addNodeHadoopTable = $("#hadoopCreate_nodeIpTableHadoop").dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : false,
				"bFilter" : true,
				"bSort" : true,
				"bInfo" : false,
				"aoColumnDefs": [
				                 { 'bSortable': false, 'aTargets': [ 0,2,3,4,8 ] },
				                 { 'sType': "ip-address", 'aTargets': [1] }
				                 ],
				                 'fnRowCallback': function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
				                	 $(nRow).attr('id','rowId_'+iDisplayIndex);
				                 }
			});
			$('#hadoopCreate_nodeIpTableHadoop_filter').hide();
			$('#searchNodeTableHadoop').keyup(function(){
				$("#hadoopCreate_nodeIpTableHadoop").dataTable().fnFilter( $(this).val() );
			});
			return false;
		},
		
		// Function to handle Hadoop Nodes Table Header Check-box click event
		checkAllNode : function(elem) {

			if(addNodeHadoopTable.fnGetData().length!=0){
				for(var i=0;i<nodeHadoopJSON.length;i++) {
					var val = $('input:[name=nodeCheckHead]').is(':checked');
					if (val) {
						if(nodeHadoopJSON[i][1] && nodeHadoopJSON[i][2] && nodeHadoopJSON[i][3]){
							$("#hadoopCheckBox-"+i).attr('checked','checked');
							$("#dataNode-"+i).attr('checked','checked');
						}
					}
					else{
						$("#hadoopCheckBox-"+i).removeAttr('checked');
						$("#namNodeHadoop-"+i).removeAttr('checked');
						$("#secNameNode-"+i).removeAttr('checked');
						$("#dataNode-"+i).removeAttr('checked');
					}
				}
			}
		},
	
		// Function to handle Ecosystem Components Table Header Check-box click event
		checkAllNode_Ecosystem : function(elem) {

			if(hadoopCreate_EcosystemTable.fnGetData().length!=0){
				for(var componentName in hadoopData.output.hadoop) {
					if(componentName!='Hadoop'){
						var val = $('input:[name=nodeCheckHead_Ecosystem]').is(':checked');
						if (val) {
							$("#hadoopEcoSystemCheckBox-"+componentName).attr('checked','checked');
						}
						else
							$("#hadoopEcoSystemCheckBox-"+componentName).removeAttr('checked');
						com.impetus.ankush.hadoopCluster.activateComponentChildPage($("#hadoopEcoSystemCheckBox-"+componentName));
					}
				}
			}
		},
		
		// Function to update node type selection on any checkbox or radio button event for Node Table
		updateHeaderCheckBox : function(id){
			var rowId = (id.split('-'))[1];
			if(!$('#'+id).attr('checked')){
				$("#nodeCheckHead").removeAttr('checked');
				$("#dataNode-"+rowId).removeAttr('checked');
				$("#namNodeHadoop-"+rowId).removeAttr('checked');
				$("#secNameNode-"+rowId).removeAttr('checked');
			}
			else
				$("#dataNode-"+rowId).attr('checked',true);
		},
		
		// Function to update node selection checkbox for the Node Table
		updateNodeCheckBox : function(rowId){

			if(!($("#namNodeHadoop-"+rowId).attr('checked') || $("#secNameNode-"+rowId).attr('checked') ||  $("#dataNode-"+rowId).attr('checked')))
				$("#hadoopCheckBox-"+rowId).removeAttr('checked');
			else
				$("#hadoopCheckBox-"+rowId).attr('checked','checked');
		},
		
		// Function to validate Node Retrieve button click event 
		validateRetrieveEvent : function() {
			$("#popover-content").empty();
			$('#nodeRetrieveHadoop').removeClass('error-box');
			com.impetus.ankush.validation.errorCount = 0;
			
			com.impetus.ankush.validation.validateField('linuxUser', 'usernameHadoop', 'User Name', 'popover-content');
			
//			if($('#throughPassword').attr('checked'))
			if($('#throughPassword').hasClass('active'))
				com.impetus.ankush.validation.validateField('required', 'passwordHadoop', 'Password', 'popover-content');
			else
				com.impetus.ankush.validation.validateField('required', 'filePathDb', 'Shared Key File', 'popover-content');
			
			//	if($('#ipRange').attr('checked'))
			if($('#ipRange').hasClass('active'))
				com.impetus.ankush.validation.validateField('required', 'ipRangeHadoop', 'IP Range', 'popover-content');
			else
				com.impetus.ankush.validation.validateField('required', 'filePath_IPAddressFile', 'IP Address File', 'popover-content');
			com.impetus.ankush.hadoopCluster.validateRackMappingFileUpload();
		},
		
		validateRackMappingFileUpload : function() {
			if($('#chkBxRackEnable').attr('checked')) {
				if(fileRack_ServerPath == null) {
					var errMsg = 'Rack Mapping file not uploaded';
					var toolTipMsg = 'Upload rack mapping file';
					com.impetus.ankush.validation.addNewErrorToDiv('filePath_RackFile', 'popover-content', errMsg, toolTipMsg);
				}
				else {
					$('#filePath_RackFile').removeClass('error-box');
				}
			}
			else {
				$('#filePath_RackFile').removeClass('error-box');
				
			}
		},
		// Function to clear rack & node mapping 
		toggleRackMapping : function() {
			if($('#chkBxRackEnable').attr('checked')) {
				$('#filePath_RackFile').removeAttr('disabled');
				$('#filePath_RackFile').css('cursor', 'pointer');
				$('#filePath_RackFile').css('background', 'white');
			}
			else {
				$('#filePath_RackFile').css('cursor', 'default');
				$('#filePath_RackFile').attr('disabled', 'disabled');
				$('#filePath_RackFile').css('background', '#EEEEEE');
				fileRack_ServerPath = null;
				$('#filePath_RackFile').val('');
				com.impetus.ankush.hadoopCluster.clearRackMappingFromTable();	
			}
		},
		
		// Function to clear rack mapping from node list table 
		clearRackMappingFromTable : function() {
			if(addNodeHadoopTable == null) {
				return;
			}
			var nodesCount = addNodeHadoopTable.fnGetData().length;
			for(var i = 0; i < nodesCount; i++ ) {
				$('#rackInfo-'+i).text('--');
				$('#dcInfo-'+i).text('--');
			}
		},
		
		// Node Retrieve button click event to fetch node information based on the node ip pattern / file
		getNodes:function() {
			addNodeHadoopTable.fnClearTable();
			com.impetus.ankush.hadoopCluster.validateRetrieveEvent();
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			$("#nodeCheckHead").removeAttr("checked");
			$("#error-div-hadoop").css("display", "none");
			$('#errorBtnHadoop').text("");
			$('#errorBtnHadoop').css("display", "none");

			$('#divNodeTableHadoop').css("display", "block");
			$('#ipRangeHadoop').val($.trim($('#ipRangeHadoop').val()));
			$("#usernameHadoop").val($.trim($("#usernameHadoop").val()));
			if(sharedKey_ServerPath != null) {
				sharedKey_ServerPath = $.trim(sharedKey_ServerPath);   
			}
			if(fileIPAddress_ServerPath != null) {
				fileIPAddress_ServerPath = $.trim(fileIPAddress_ServerPath);   
			}

			var url=baseUrl+"/cluster/detectNodes";
			var nodeData={};
			nodeData.isFileUploaded=false;
			nodeData.userName=$('#usernameHadoop').val();
			nodeData.authTypePassword = true;
//			if($('#throughPassword').attr('checked'))
			if($('#throughPassword').hasClass('active'))
				nodeData.password=$('#passwordHadoop').val();
			else {
				nodeData.authTypePassword = false;
				nodeData.password =    sharedKey_ServerPath;
			}

			//if($('#ipFile').attr('checked')) {
			if($('#ipFile').hasClass('active')) {
				nodeData.isFileUploaded=true;
				nodeData.nodePattern = fileIPAddress_ServerPath;
			}
			else {
				nodeData.nodePattern=$('#ipRangeHadoop').val();
			}
			// Rack POST Data for Node Retrival
			nodeData.isRackEnabled = false;
			nodeData.filePathRackMap = "";
			
			if($('#chkBxRackEnable').attr('checked')) {
				nodeData.isRackEnabled = true;
				nodeData.filePathRackMap = fileRack_ServerPath;	
			}
			$('#nodeRetrieveHadoop').text('Retrieving...');
			$('#nodeRetrieveHadoop').attr('disabled', true);
			$('#hadoopClusterDeploy').attr('disabled', true);
			$('#btnClearRackMapping').attr('disabled', true);

			$('#btnAll_HSN').removeClass('active');
			$('#btnSelected_HSN').removeClass('active');
			$('#btnAvailable_HSN').removeClass('active');
			$('#btnError_HSN').removeClass('active');
			$('#btnAll_HSN').addClass('active');
			$("#nodeTable").show();
			$('#inspectNodeBtn').attr('disabled','disabled');
			$.ajax({
				'type' : 'POST',
				'url' : url,
				'contentType' : 'application/json',
				'data' : JSON.stringify(nodeData),
				"async": true,
				'dataType' : 'json',
				'success' : function(result) {
					$('#inspectNodeBtn').removeAttr('disabled');
					$('#nodeRetrieveHadoop').removeAttr('disabled');
					$('#btnClearRackMapping').removeAttr('disabled');
					$('#nodeRetrieveHadoop').text('Retrieve');
					$('#hadoopClusterDeploy').removeAttr('disabled');
					// IP pattern invalid error message
					if(result.output.error != null){
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
						return;
					}

					addNodeHadoopTable.fnClearTable();
					nodeTableLength=result.output.nodes.length;
					nodeHadoopJSON=result.output.nodes;
					var errorNodeCount = 0;
					for(var i=0;i<nodeTableLength;i++) {
						var dcText = '';
						if(nodeHadoopJSON[i][5] != undefined) {
							dcText = nodeHadoopJSON[i][5];
						}
						var rackText = '';
						if(nodeHadoopJSON[i][6] != undefined) {
							rackText = nodeHadoopJSON[i][6];
						}
						
						var checkNodeHadoop= '<input type="checkbox" id="hadoopCheckBox-'+i+'" class="inspect-node" onclick="com.impetus.ankush.hadoopCluster.updateHeaderCheckBox(\'hadoopCheckBox-'+i+'\');"/>';
						//var ipHadoop='<span style="font-weight:bold;" id="ipHadoop-'+i+'">'+nodeHadoopJSON[i][0]+'</span>';
						var ipHadoop=nodeHadoopJSON[i][0];
						var nameNodeValue='<input type="radio" name="namNodeHadoop" id="namNodeHadoop-'+i+'" onclick="com.impetus.ankush.hadoopCluster.updateNodeCheckBox(\''+i+'\');"/>';
						var secNameNode='<input type="radio" name="secNameNode" id="secNameNode-'+i+'" onclick="com.impetus.ankush.hadoopCluster.updateNodeCheckBox(\''+i+'\');"/>';
						var dataNode='<input type="checkbox" id="dataNode-'+i+'" onclick="com.impetus.ankush.hadoopCluster.updateNodeCheckBox(\''+i+'\');"/>';
						var dcInfo = '<span  id="dcInfo-' + i + '"/>'+dcText+'</span>';
						var rackInfo = '<span  id="rackInfo-' + i + '"/>'+rackText+'</span>';
						var os='<span style="font-weight:bold;" id="os-'+i+'">'+nodeHadoopJSON[i][4]+'</span>';
						var navigationHadoop='<a href="#"><img id="navigationImgHadoop'+i+'" src="'+baseUrl+'/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.hadoopCluster.loadNodeDetailHadoop('+i+');"/></a>';
						addNodeHadoopTable.fnAddData([
						                              checkNodeHadoop,
						                              ipHadoop,
						                              nameNodeValue,
						                              secNameNode,
						                              dataNode,
						                              dcInfo,
						                              rackInfo,
						                              os,
						                              navigationHadoop
						                            ]);

						if(!(nodeHadoopJSON[i][1] && nodeHadoopJSON[i][2] && nodeHadoopJSON[i][3])){
							$('#hadoopCheckBox-'+i).attr('disabled', true);
							$("#namNodeHadoop-"+i).attr('disabled', true);
							$("#secNameNode-"+i).attr('disabled', true);
							$("#dataNode-"+i).attr('disabled', true);
							$('#rowId_' + i).addClass('error-row');
							errorNodeCount++;
						}
						com.impetus.ankush.hadoopCluster.toggleSecNNColumn();
					}
				},
				'error' : function() {
					$('#inspectNodeBtn').removeAttr('disabled');
					$('#nodeRetrieveHadoop').removeAttr('disabled');
					$('#nodeRetrieveHadoop').text('Retrieve');
					$('#hadoopClusterDeploy').removeAttr('disabled');
				}
			});   

		},
	
		// Function to validate Get Cloud Details button click event 
		validateCloudDetailsEvent : function() {
			$("#popover-content").empty();
			com.impetus.ankush.validation.errorCount = 0;
			com.impetus.ankush.validation.validateField('required', 'cloud_service_provider', 'Cloud Service Provider', 'popover-content');
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'cloudUsername', 'Cloud User Name', 'popover-content');
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'cloudPassword', 'Cloud Password', 'popover-content');
		},
		
		// Get Cloud Details button click event to fetch cloud account information based on credentials entered		
		getCloudDetails : function(){

			com.impetus.ankush.hadoopCluster.validateCloudDetailsEvent();
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			$("#nodeCheckHead").removeAttr("checked");
			$("#error-div-hadoop").css("display", "none");
			$('#errorBtnHadoop').text("");
			$('#errorBtnHadoop').css("display", "none");

			$('#cloudUsername').val($.trim($('#cloudUsername').val()));

			var provider = $("#cloud_service_provider").val();
			var username = $('#cloudUsername').val();
			var password = $("#cloudPassword").val();
			var data = {};
			data.providerName = provider;
			data.username = username;
			data.password = password;

			$("#keyPairsSelect").empty();
			$("#securityGroupsSelect").empty();
			$("#regionSelect").empty();
			$("#instanceTypeSelect").empty();
			$("#btnGetCloudDetails_HadoopCreate").attr('disabled', true);
			$("#btnGetCloudDetails_HadoopCreate").text('Fetching...');
			$("#hadoopClusterDeploy").attr('disabled', true);

			var cloudDetailsUrl = baseUrl+"/cluster/cloudEnv";
			$.ajax({
				'type' : 'POST',
				'url' : cloudDetailsUrl,
				'contentType' : 'application/json',
				'data' : JSON.stringify(data),
				"async": true,
				'dataType' : 'json',
				'success' : function(result) {
					cloudDetailsResult = result;
					$("#btnGetCloudDetails_HadoopCreate").removeAttr('disabled');
					$("#btnGetCloudDetails_HadoopCreate").text('Get Cloud Details');
					$('#hadoopClusterDeploy').removeAttr('disabled');
					if((cloudDetailsResult.output.keyPairInfo != null) && (cloudDetailsResult.output.securityGroups != null) && (cloudDetailsResult.output.regions != null)){
						for(var i=0 ; i < cloudDetailsResult.output.hardwareInfo.length ; i++){
							$("#instanceTypeSelect")
							.append(
									"<option value='"
									+ cloudDetailsResult.output.hardwareInfo[i].name
									+ "'>"
									+ cloudDetailsResult.output.hardwareInfo[i].name
									+ "</option>");
						}
						for(var i=0 ; i < cloudDetailsResult.output.keyPairInfo.length ; i++){
							$("#keyPairsSelect")
							.append(
									"<option value='"
									+ cloudDetailsResult.output.keyPairInfo[i].keyName
									+ "'>"
									+ cloudDetailsResult.output.keyPairInfo[i].keyName
									+ "</option>");
						}
						for(var i=0 ; i < cloudDetailsResult.output.securityGroups.length ; i++){
							$("#securityGroupsSelect")
							.append(
									"<option value='"
									+ cloudDetailsResult.output.securityGroups[i].name
									+ "'>"
									+ cloudDetailsResult.output.securityGroups[i].name
									+ "</option>");
						}
						for(var i=0 ; i < cloudDetailsResult.output.regions.length ; i++){
							$("#regionSelect")
							.append(
									"<option value='"
									+ cloudDetailsResult.output.regions[i].regionName
									+ "'>"
									+ cloudDetailsResult.output.regions[i].regionName
									+ "</option>");
						}
						$("#clouddetails").show();
						flagCloudDetailsAvailable = true;
						com.impetus.ankush.hadoopCluster.zonePopulation();

					}else{
//						var errMsg = 'Invalid Credentials for Cloud Provider';
//						var toolTipMsg = 'Invalid Credentials for Cloud Provider';
//						
//						com.impetus.ankush.validation.addNewErrorToDiv('cloudUsername', 'popover-content', errMsg, toolTipMsg);
//						com.impetus.ankush.validation.addNewErrorToDiv('cloudPassword', 'popover-content', errMsg, toolTipMsg);
						
						alert("Wrong Credential!!");
					}
				},
				'error' : function() {
					$("#btnGetCloudDetails_HadoopCreate").removeAttr('disabled');
					$("#btnGetCloudDetails_HadoopCreate").text('Get Cloud Details');
					$('#hadoopClusterDeploy').removeAttr('disabled');
				}
			});
		},
	
		// Function to fill Zone details under cloud details during cluster creation
		zonePopulation : function(){
			$("#zoneSelect").empty();
			for(var i=0 ; i < cloudDetailsResult.output.zones.length ; i++){
				if(cloudDetailsResult.output.zones[i].region == $("#regionSelect").val()){
					$("#zoneSelect")
					.append(
							"<option value='"
							+ cloudDetailsResult.output.zones[i].zone
							+ "'>"
							+ cloudDetailsResult.output.zones[i].zone
							+ "</option>");
				}
			}
		},
		
		// Function to validate Apply button click event for Hadoop Advanced Settings
		validateApply_HadoopAdvSettings : function() {
			$("#popover-content-HadoopAdvSettings").empty();
			$("#errorBtnHadoop-HadoopAdvSettings").empty();
			var errorDivId = 'popover-content-HadoopAdvSettings';
			com.impetus.ankush.validation.errorCount = 0;
			com.impetus.ankush.validation.validateField('numeric', 'dfsReplicationHadoop', 'DFS Replication Factor', errorDivId);
			if(!$('#dfsReplicationHadoop').hasClass('error-box')) {
				if(parseInt($.trim($('#dfsReplicationHadoop').val())) <= 0) {
					var errMsg = 'Invalid DFS Replication Factor: Should be greater than Zero';
					var toolTipMsg = 'Should be greater than Zero';
					com.impetus.ankush.validation.addNewErrorToDiv('dfsReplicationHadoop', errorDivId, errMsg, toolTipMsg);		
				}	
			}
			
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'path-Hadoop', 'Download URL / Local Path for Hadoop Tarball', errorDivId);
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'nameNodePathHadoop', ' NameNode Directory', errorDivId);
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'dataNodePathHadoop', 'Datanode Directory', errorDivId);
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'mapredTempPathHadoop', 'Mapred Temporary Directory', errorDivId);
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'tempPathHadoop', 'Hadoop Temporary Directory', errorDivId);
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'installationPath-Hadoop', 'Installation Directory', errorDivId);
			if($('#includeS3SupportHadoop').attr('checked')) {
				com.impetus.ankush.validation.validateField('required', 'acessKeyS3', 'S3 Acess Key', errorDivId);
				com.impetus.ankush.validation.validateField('required', 'secretKeyS3', 'S3 Secret Key', errorDivId);
			}
			if($('#includeS3nSupportHadoop').attr('checked')) {
				com.impetus.ankush.validation.validateField('required', 'acessKeyS3n', 'S3n Acess Key', errorDivId);
				com.impetus.ankush.validation.validateField('required', 'secretKeys3n', 'S3n Secret Key', errorDivId);
			}
			
			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected($('#version-Hadoop').val())) {
				
				if($('#webAppProxyEnable').hasClass('active')) {
					com.impetus.ankush.validation.validateField('numeric', 'webAppProxyPort', 'Web Application Proxy Port', errorDivId);
				}
				
				if(!$('#hadoopHAEnable').hasClass('active')) {
					return;
				}
				
				com.impetus.ankush.validation.validateField('alphaNumericWithoutUnderScore', 'nameserviceId', 'Nameservice Id', errorDivId);
				if(!$('#nameserviceId').hasClass('error-box')) {
					var limit_ha_nameserviceId = 20;
					if($('#nameserviceId').val().length > limit_ha_nameserviceId ) {
						var errMsg = 'Invalid Nameservice Id: Only ' + limit_ha_nameserviceId + ' characters allowed.';
						var toolTipMsg = 'Only ' + limit_ha_nameserviceId + ' characters allowed.';
						com.impetus.ankush.validation.addNewErrorToDiv('nameserviceId', errorDivId, errMsg, toolTipMsg);		
					}	
				}
				
				var limit_ha_nameNodeId = 10;
				
				com.impetus.ankush.validation.validateField('alphaNumericWithoutUnderScore', 'nameNodeId1', 'NameNode Id 1', errorDivId);
				if(!$('#nameNodeId1').hasClass('error-box')) {
					if($('#nameNodeId1').val().length > limit_ha_nameNodeId ) {
						var errMsg = 'Invalid NameNode Id 1: Only ' + limit_ha_nameNodeId + ' characters allowed.';
						var toolTipMsg = 'Only ' + limit_ha_nameNodeId + ' characters allowed.';
						com.impetus.ankush.validation.addNewErrorToDiv('nameNodeId1', errorDivId, errMsg, toolTipMsg);		
					}	
				}
				
				com.impetus.ankush.validation.validateField('alphaNumericWithoutUnderScore', 'nameNodeId2', 'NameNode Id 2', errorDivId);
				if(!$('#nameNodeId2').hasClass('error-box')) {
					if($('#nameNodeId2').val().length > limit_ha_nameNodeId ) {
						var errMsg = 'Invalid NameNode Id 2: Only ' + limit_ha_nameNodeId + ' characters allowed.';
						var toolTipMsg = 'Only ' + limit_ha_nameNodeId + ' characters allowed.';
						com.impetus.ankush.validation.addNewErrorToDiv('nameNodeId2', errorDivId, errMsg, toolTipMsg);		
					}	
				}
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'journalNodeEditsDir', 'Journal Node Edits Dir', errorDivId);
			}
		},
		
		// Function to validate Apply button click event for Ecosystem Component Advanced Settings		
		validateApply_ComponentAdvSettings : function(componentName) {
			$("#popover-content-ComponentAdvSettings").empty();
			$("#errorBtnHadoop-ComponentAdvSettings").empty();
			var errorDivId = 'popover-content-ComponentAdvSettings';
			com.impetus.ankush.validation.errorCount = 0;
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'path-' + componentName, 'Download URL / Local Path for ' + componentName + ' Tarball', errorDivId);
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'installationPath-' + componentName, 'Installation Directory', errorDivId);
			if(componentName == 'Hive') {
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'connectionDriverName', 'Connection Driver Name', errorDivId);
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'connectionURL', 'Connection URL', errorDivId);
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'connectionUserName', 'Connection UserName', errorDivId);
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'connectionPassword', 'Connection Password', errorDivId);
			}
			else if(componentName == 'Hbase') {
				com.impetus.ankush.validation.validateField('numeric', 'filesize', 'File Size', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'compactionThreshold', 'Compaction Threshold', errorDivId);
				com.impetus.ankush.hadoopCluster.validateCahceSize();
				com.impetus.ankush.validation.validateField('numeric', 'caching', 'Caching', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'timeout', 'Timeout', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'multiplier', 'Multiplier', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'majorcompaction', 'Major Compaction', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'maxsize', 'Max Size', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'flushSize', 'Flush Size', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'handlerCount', 'Handler Count', errorDivId);
			}
			else if(componentName == 'Zookeeper') {
				com.impetus.ankush.validation.validateField('numeric', 'tickTime', 'Tick Time', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'clientPort', 'Client Port', errorDivId);
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'dataDir', 'dataDir', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'syncLimit', 'Sync Limit', errorDivId);
				com.impetus.ankush.validation.validateField('numeric', 'initLimit', 'Init Limit', errorDivId);
			}
		},
		
		// Function to validate field Cache Size for Hbase 
		validateCahceSize : function() {
		
			var fieldId = 'cacheSize';
			var fieldName = 'Cache Size';
			var divId = 'popover-content-ComponentAdvSettings';
			
			var isValid = com.impetus.ankush.validation.validateField('required', fieldId, fieldName, divId);
			if(!isValid) {
				return;
			}
			if(!com.impetus.ankush.validation.pattern($('#'+fieldId).val(),/^[\d]*.[\d]*$/)) {
				var errMsg = 'Invalid ' + fieldName + ': Should be between 0.0 & 1.0';
				var toolTipMsg = fieldName + ' should be a decimal value';
				com.impetus.ankush.validation.addNewErrorToDiv(fieldId, divId, errMsg, toolTipMsg);
			}
			else if(!com.impetus.ankush.validation.betweenFloat($('#'+fieldId).val(), '0.0', '1.0')) {
				var errMsg = 'Invalid ' + fieldName + ': Should be a decimal value.';
				var toolTipMsg = fieldName + ' should be between 0.0 & 1.0';
				com.impetus.ankush.validation.addNewErrorToDiv(fieldId, divId, errMsg, toolTipMsg);
			}
			else {
				$('#'+fieldId).removeClass('error-box');
				com.impetus.ankush.common.tooltipOriginal(fieldId, 'Enter ' + fieldName);
				$('#'+fieldId).tooltip();
			}
		},
		
		// Function to validdate Deploy Cluster button click event 
		validateFields : function() {
			$("#popover-content").empty();
			$("#error-div-hadoop").css('display', 'none');
			$("#errorBtnHadoop").css('display', 'none');
			
			$('#nodeRetrieveHadoop').removeClass('error-box');
			com.impetus.ankush.validation.errorCount = 0;
			
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'hadoop_cluster_name', 'Cluster Name', 'popover-content');
			if(!$('#hadoop_cluster_name').hasClass('error-box')) {
				com.impetus.ankush.validation.validateField('alphaNumericWithoutUnderScore', 'hadoop_cluster_name', 'Cluster Name', 'popover-content');
				if($('#hadoop_cluster_name').val().length > limit_hadoop_cluster_name ) {
					var errMsg = 'Invalid Cluster Name: Only ' + limit_hadoop_cluster_name + ' characters allowed.';
					var toolTipMsg = 'Only ' + limit_hadoop_cluster_name + ' characters allowed.';
					com.impetus.ankush.validation.addNewErrorToDiv('hadoop_cluster_name', 'popover-content', errMsg, toolTipMsg);		
				}
			}
			com.impetus.ankush.validation.validateField('required', 'hadoop_cluster_desc', 'Cluster Description', 'popover-content');
			if(!$('#hadoop_cluster_desc').hasClass('error-box')) {
				
				com.impetus.ankush.validation.validateField('alphaNumericWithSpace', 'hadoop_cluster_desc', 'Cluster Description', 'popover-content');
				if($('#hadoop_cluster_desc').val().length > limit_hadoop_cluster_desc ) {
					var errMsg = 'Invalid Cluster Description: Only ' + limit_hadoop_cluster_desc + ' characters allowed.';
					var toolTipMsg = 'Only ' + limit_hadoop_cluster_desc + ' characters allowed.';
					com.impetus.ankush.validation.addNewErrorToDiv('hadoop_cluster_desc', 'popover-content', errMsg, toolTipMsg);		
				}
			}

			if($('#install_java_checkbox').attr('checked'))
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'java_bundle_path', 'Java Bundle Path', 'popover-content');
			else
				com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'java_home_hadoop', 'Java Home Path', 'popover-content');

			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'usernameHadoop', 'User Name', 'popover-content');
//			if($('#throughPassword').attr('checked'))   
			if($('#throughPassword').hasClass('active'))
				com.impetus.ankush.validation.validateField('required', 'passwordHadoop', 'Password', 'popover-content');
			else
				com.impetus.ankush.validation.validateField('required', 'filePathDb', 'Shared Public Key', 'popover-content');

			com.impetus.ankush.validation.validateField('numeric', 'hadoopReplicationFactor', 'DFS Replication Factor', 'popover-content');
			if(!$('#hadoopReplicationFactor').hasClass('error-box')) {
				if(parseInt($.trim($('#hadoopReplicationFactor').val())) <= 0) {
					var errMsg = 'Invalid DFS Replication Factor: Should be greater than Zero';
					var toolTipMsg = 'Should be greater than Zero';
					com.impetus.ankush.validation.addNewErrorToDiv('hadoopReplicationFactor', 'popover-content', errMsg, toolTipMsg);		
				}	
			}
			
			if($('#localEnv').hasClass('active')){
				//if($('#ipRange').attr('checked'))
				if($('#ipRange').hasClass('active'))
					com.impetus.ankush.validation.validateField('required', 'ipRangeHadoop', 'IP Range', 'popover-content');
				else
					com.impetus.ankush.validation.validateField('required', 'filePath_IPAddressFile', 'IP Address File', 'popover-content');
				com.impetus.ankush.hadoopCluster.validateNodeSelection();
				com.impetus.ankush.hadoopCluster.validateRackMappingFileUpload();
				com.impetus.ankush.hadoopCluster.validateEcosystemComponents();
			}
			else
				com.impetus.ankush.hadoopCluster.validateCloudDetails();
			
			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected($('#hadoopVersion').val())) {
				if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'] != null) {
					var hadoopAdvData_Tmp = com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'];
					if(hadoopAdvData_Tmp.isHadoop2) {
						com.impetus.ankush.hadoopCluster.validateHadoop2_Node('Resource Manager', hadoopAdvData_Tmp.resourceManagerNode);
						if(hadoopAdvData_Tmp.startJobHistoryServer) {
							com.impetus.ankush.hadoopCluster.validateHadoop2_Node('Job History Server', hadoopAdvData_Tmp.jobHistoryServer);
						}
						
						if(hadoopAdvData_Tmp.isWebProxyEnabled) {
							com.impetus.ankush.hadoopCluster.validateHadoop2_Node('Web Application Proxy', 
									hadoopAdvData_Tmp.webAppProxyNode);
						}
						
						if(hadoopAdvData_Tmp.isHAEnabled) {
							if(hadoopAdvData_Tmp.automaticFailoverEnabled) {
								if(!$('#hadoopEcoSystemCheckBox-Zookeeper').attr('checked')) {
									var errMsg = 'Hadoop HA Settings: Please configure zookeeper with automatic failover enabled HA.';
									var toolTipMsg = 'Configure Zookeeper';
									com.impetus.ankush.validation.addNewErrorToDiv('hadoopEcoSystemCheckBox-Zookeeper', 'popover-content', errMsg, toolTipMsg);	
								}
							}			
						}
					}
				}
				else {
					if(!$('#hadoopEcoSystemCheckBox-Zookeeper').attr('checked')) {
						var errMsg = 'Invalid Operation: Please configure zookeeper with Hadoop HA.';
						var toolTipMsg = 'Configure Zookeeper';
						com.impetus.ankush.validation.addNewErrorToDiv('hadoopEcoSystemCheckBox-Zookeeper', 'popover-content', errMsg, toolTipMsg);	
					}
				}
			}
		},
		
		// Function to check if any of the node is selected for cluster deployment
		checkIfNodeIsSelected : function(nodeIp) {
			if(nodeTableLength != null) {
				for(var i = 0 ; i < nodeTableLength ; i++){
					if(nodeHadoopJSON[i][0] == nodeIp)
						return $("#hadoopCheckBox-"+i).attr("checked");
				}
			}
			return false;
		},
		
		// Function to validate HAdoop 2 node selections with actual nodes selected during cluster creation
		validateHadoop2_Node : function (nodeType, nodeIp) {
			var flag_Error = false;
			if(nodeIp == null) {
			flag_Error = true;
					}
					else if(nodeIp == '') {
						flag_Error = true;
					}
					if(flag_Error) {
						var errMsg = 'Hadoop 2 Settings: ' + nodeType + ' Field Empty';
						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
					}
					else if(!com.impetus.ankush.hadoopCluster.checkIfNodeIsSelected(nodeIp)){
						var errMsg = 'Hadoop 2 Settings: Invalid ' + nodeType + ' - ' + nodeIp;
						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
					}
		},
		
		// Function to validate Hive Node selection with actual nodes selected during cluster creation
		validateEcosystemComponent_Hive : function () {
			if(com.impetus.ankush.hadoopCluster.echoSystem['Hive'].isSubmit != null) {
				if(com.impetus.ankush.hadoopCluster.echoSystem['Hive'].isSubmit) {
					var flag_Error = false;
					if(hiveServer == null) {
						flag_Error = true;
					}
					else if(hiveServer == '') {
						flag_Error = true;
					}
					if(flag_Error) {
						var errMsg = 'Hive Settings: Hive Server Field Empty';
						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
					}
					else if(!com.impetus.ankush.hadoopCluster.checkIfNodeIsSelected(hiveServer)){
						var errMsg = 'Hive Settings: Invalid Hive Server node - ' + hiveServer;
						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
					}
				}
			}
		},
		
		// Function to validate HBase Node selection with actual nodes selected during cluster creation		
		validateEcosystemComponent_Hbase : function() {
			if(com.impetus.ankush.hadoopCluster.echoSystem['Hbase'].isSubmit != null) {
				if(com.impetus.ankush.hadoopCluster.echoSystem['Hbase'].isSubmit) {
					var flag_Error = false;
					
					// To be changed for HBase master selection validation
					
//					if(hbaseMaster == null)
//						flag_Error = true;
//					else if(hbaseMaster == '')
//						flag_Error = true;

//					if(flag_Error) {
//						var errMsg = 'Hbase Settings: Hbase Master Field Empty';
//						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
//					}
//					else if(!com.impetus.ankush.hadoopCluster.checkIfNodeIsSelected(hbaseMaster)){
//						var errMsg = 'Hbase Settings: Invalid Hbase Master node - ' + hbaseMaster;
//						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
//					}

					flag_Error = false;
					if(regionServers == null)
						flag_Error = true;
					else if(Object.keys(regionServers).length === 0)
						flag_Error = true;

					if(flag_Error) {
						var errMsg = 'Hbase Settings: Region Servers List Empty';
						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
					}
					else {
						var countRegionServers = Object.keys(regionServers).length;
						for(var i = 0; i < countRegionServers; i++) {
							if(!com.impetus.ankush.hadoopCluster.checkIfNodeIsSelected(Object.keys(regionServers)[i])) {
								var errMsg = 'Hbase Settings: Invalid Reqion Server node - ' + Object.keys(regionServers)[i];
								com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
							}
						}
					}
				}
			}
		},
		
		// Function to validate Zookeeper Node selection with actual nodes selected during cluster creation
		validateEcosystemComponent_Zookeeper : function() {
			if(com.impetus.ankush.hadoopCluster.echoSystem['Zookeeper'].isSubmit != null) {
				if(com.impetus.ankush.hadoopCluster.echoSystem['Zookeeper'].isSubmit) {
					var flag_Error = false;
					if(zookeeperNodes == null)
						flag_Error = true;
					else if(Object.keys(zookeeperNodes).length === 0)
						flag_Error = true;

					if(flag_Error) {
						var errMsg = 'Zookeeper Settings: Zookeeper Nodes List Empty';
						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
					}
					else {
						var countZookeeperNodes = Object.keys(zookeeperNodes).length;
						for(var i = 0; i < countZookeeperNodes; i++) {
							if(!com.impetus.ankush.hadoopCluster.checkIfNodeIsSelected(Object.keys(zookeeperNodes)[i])) {
								var errMsg = 'Zookeeper Settings: Invalid Zookeeper node - ' + Object.keys(zookeeperNodes)[i];
								com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
							}
						}
					}
				}
			}
		},
		
		// Function to validate Ecosystem Components Node selection with actual nodes selected during cluster creation
		validateEcosystemComponents : function() {
			if($('#hadoopEcoSystemCheckBox-Hive').attr('checked')) {
				com.impetus.ankush.hadoopCluster.validateEcosystemComponent_Hive();
			}
			if($('#hadoopEcoSystemCheckBox-Hbase').attr('checked')) {
				com.impetus.ankush.hadoopCluster.validateEcosystemComponent_Hbase();
			}
			if($('#hadoopEcoSystemCheckBox-Zookeeper').attr('checked')) {
				com.impetus.ankush.hadoopCluster.validateEcosystemComponent_Zookeeper();
			}
		},
		
		// Function to validate Node selection during cluster creation
		validateNodeSelection : function () {
			var flag_NodeCount = false;
			var flag_NamenodeCount = false;
			var flag_DatanodeCount = false;

			if(nodeTableLength == null || nodeTableLength == 0) {
				var errMsg = 'Select at-least one node for cluster';
				com.impetus.ankush.validation.addNewErrorToDivJumpToAnchor('hadoopNodeList', 'popover-content', errMsg, null);
			}
			else {
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($("#hadoopCheckBox-"+i).attr("checked"))
						flag_NodeCount = true;

					if($("#namNodeHadoop-"+i).attr("checked"))
						flag_NamenodeCount = true;

					if($("#dataNode-"+i).attr("checked"))
						flag_DatanodeCount = true;

					if(flag_NodeCount && flag_NamenodeCount && flag_DatanodeCount) {
						break;
					}
				}
				if(!(flag_NodeCount && flag_NamenodeCount && flag_DatanodeCount)) {

					if(!flag_NodeCount) {
						var errMsg = 'Select at-least one node for cluster';
						com.impetus.ankush.validation.addNewErrorToDivJumpToAnchor('hadoopNodeList', 'popover-content', errMsg, null);
					}
					if(!flag_NamenodeCount) {
						var errMsg = 'Select at-least one NameNode';
						com.impetus.ankush.validation.addNewErrorToDivJumpToAnchor('hadoopNodeList', 'popover-content', errMsg, null);
					}
					if(!flag_DatanodeCount) {
						var errMsg = 'Select at-least one DataNode';
						com.impetus.ankush.validation.addNewErrorToDivJumpToAnchor('hadoopNodeList', 'popover-content', errMsg, null);
					}
				}
				if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'] != null) {
					if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'].isHadoop2) {
						if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'].isHAEnabled) {
							com.impetus.ankush.hadoopCluster.validateHANodeSelection();			
						}
					}
				} else {
					
					if(com.impetus.ankush.hadoopCluster.isHadoop2Selected($("#hadoopVersion").val())) {
						var countNode = 0;
						for(var i = 0 ; i < nodeTableLength ; i++){
							if($("#hadoopCheckBox-"+i).attr("checked")){
								countNode++;
							}
						}
						if(countNode < 2) {
							var errMsg = 'Hadoop HA Settings: Invalid Node Selection - Please select minimum two nodes';
							com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
						}	
					}
				}
			}
		},
		
		validateHANodeSelection : function() {
			var obj = com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'];
			var flag_Error = false;
			
			if(obj.standByNamenode == null)
				flag_Error = true;
			else if(obj.standByNamenode == '')
				flag_Error = true;

			var activeNamenode = "";
			var countNode = 0;
			for(var i = 0 ; i < nodeTableLength ; i++){
				if($("#hadoopCheckBox-"+i).attr("checked")){
					if($("#namNodeHadoop-"+i).is(":checked")) {
						activeNamenode = nodeHadoopJSON[i][0];
					}
					countNode++;
				}
			}
			
			if(flag_Error) {
				var errMsg = 'Hadoop HA Settings: StandBy NameNode Field Empty';
				com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
			} else if(!com.impetus.ankush.hadoopCluster.checkIfNodeIsSelected(obj.standByNamenode)){
				var errMsg = 'Hadoop HA Settings: Invalid StandBy NameNode - ' + obj.standByNamenode;
				com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
			} else if(obj.standByNamenode == activeNamenode) {
				var errMsg = 'Hadoop HA Settings: StandBy NameNode can not be same as Active NameNode';
				com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
			}

			if(countNode < 2) {
				var errMsg = 'Hadoop HA Settings: Invalid Node Selection - Please select minimum two nodes';
				com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
			}
			
			flag_Error = false;
			if(obj.journalNodes == null)
				flag_Error = true;
			else if(Object.keys(obj.journalNodes).length === 0)
				flag_Error = true;

			if(flag_Error) {
				var errMsg = 'Hadoop HA Settings: Journal Nodes List Empty';
				com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
			}
			else {
				var countJournalNodes = Object.keys(obj.journalNodes).length;
				for(var i = 0; i < countJournalNodes; i++) {
					if(!com.impetus.ankush.hadoopCluster.checkIfNodeIsSelected(Object.keys(obj.journalNodes)[i])) {
						var errMsg = 'Hadoop HA Settings: Invalid Journal Node - ' + Object.keys(obj.journalNodes)[i];
						com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
					}
				}
			}
		},
		
		// Function to validate cloud details during cluster creation
		validateCloudDetails : function () {
			com.impetus.ankush.validation.validateField('requiredWithoutSpace', 'cloudUsername', 'User Name for Cloud Service', 'popover-content');
			com.impetus.ankush.validation.validateField('required', 'cloudPassword', 'Password for Cloud Service', 'popover-content');
			if(!flagCloudDetailsAvailable) {
				var errMsg = 'Invalid Flow: Cloud Details are required for this operation.';
				com.impetus.ankush.validation.addNewErrorToDiv(null, 'popover-content', errMsg, null);
			}
			else {

				com.impetus.ankush.validation.validateField('numeric', 'cluster_size_cloud', 'Cloud Cluster Size', 'popover-content');
				com.impetus.ankush.validation.validateField('required', 'machine_image_hadoop', 'Cloud Machine Image', 'popover-content');  
				
				if($("#autoTerminateYes").attr("checked")){
					com.impetus.ankush.validation.validateField('numeric', 'timeout_interval_hadoop', 'Timeout Interval', 'popover-content');
					if(!($('#timeout_interval_hadoop').hasClass('error-box'))) {
						var timeOut = parseInt($('#timeout_interval_hadoop').val(), 10);
						if(!(timeOut > 0)) {
							var errMsg = 'Invalid Auto-Terminate Interval: Should be greater than 0';
							var toolTipMsg = 'Interval should be greater than 0';
							com.impetus.ankush.validation.addNewErrorToDiv('timeout_interval_hadoop', 'popover-content', errMsg, toolTipMsg);
						}
					}
				}
			}
		},
		
		// Deploy Cluster button click event
		clusterCreation : function(){
			com.impetus.ankush.hadoopCluster.validateFields();
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
				return;
			}
			com.impetus.ankush.hadoopCluster.trimTextBoxFields();

			$('#validateError').hide();
			$('#error-div-hadoop').hide();

			deployedClusterHadoop = true;
			data = {};
			data.clusterName = $("#hadoop_cluster_name").val();
			data.description = $("#hadoop_cluster_desc").val();
			data.technology = "Hadoop";
			
			data.environment = $("#environmentTypeGroupBtn .active").data("value");
//			data.environment = $('input:radio[name="environment"]:checked').val();

			data.javaConf = {};
			if($("#install_java_checkbox").attr("checked"))
				data.javaConf.install = true;
			else
				data.javaConf.install = false;
			data.javaConf.javaBundle = $("#java_bundle_path").val();
			data.javaConf.javaHomePath = $("#java_home_hadoop").val();

			data.authConf = {};
			data.authConf.username = $('#usernameHadoop').val();
//			if($('#throughPassword').attr('checked')) {
			if($('#throughPassword').hasClass('active')) {
				data.authConf.password=$('#passwordHadoop').val();
			}
			else {
				data.authConf.password = sharedKey_ServerPath;
			}
			
			data = com.impetus.ankush.hadoopCluster.deployHadoopValues(data);
			for(var componentName in hadoopData.output.hadoop){
				if($("#hadoopEcoSystemCheckBox-"+componentName).attr("checked")){
					data.components[componentName] = {};
					componentData = com.impetus.ankush.hadoopCluster.deployEcoSystemComponents(componentName);
					data.components[componentName] = componentData;
				}
			}
			// Change Component key from Hadoop to Hadoop2 if Hadoop2 is Selected 
			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected(data.components.Hadoop.componentVersion)) {
				if(data.components.Hadoop.advancedConf.isHAEnabled) {
					if(data.components.Zookeeper != null) {
						data.components.Hadoop.advancedConf.zkClientPort = data.components.Zookeeper.advancedConf.clientPort;
						data.components.Hadoop.advancedConf.zkNodes = data.components.Zookeeper.advancedConf.nodes;	
					}
				}
				var Hadoop2 = new Object();
				Hadoop2 = data.components.Hadoop;
				delete data.components.Hadoop;
				
				var tmpComponents = new Object();
				for(var iCount = 0; iCount < (Object.keys(data.components)).length; iCount++) {
					tmpComponents[Object.keys(data.components)[iCount]] = data.components[Object.keys(data.components)[iCount]];
				}
				
				delete data.components;
				data.components = new Object();
				data.components.Hadoop2 = Hadoop2;
				for(var iCount = 0; iCount < Object.keys(tmpComponents).length; iCount++) {
					data.components[Object.keys(tmpComponents)[iCount]] = tmpComponents[Object.keys(tmpComponents)[iCount]];
				}
				delete tmpComponents;
			}
			
//			data.authConf.type = $('input:radio[name="authentication"]:checked').val();
			data.authConf.type = $("#authTypeGroupBtn .active").data("value");
			
			//if($('#ipRange').attr('checked'))
			if($('#ipRange').hasClass('active'))

				data.ipPattern = $('#ipRangeHadoop').val();
			else
				data.patternFile = fileIPAddress_ServerPath;

			data.rackEnabled = false;
			data.rackFileName = "";
			if($('#chkBxRackEnable').attr('checked')) {
				data.rackEnabled = true;
				data.rackFileName = $("#filePath_RackFile").val();
			}
			
			data.nodes = [];

			if(data.environment == "Cloud"){
				data.cloudConf= {};
				data.cloudConf.providerName = $("#cloud_service_provider").val();
				data.cloudConf.username = $("#cloudUsername").val();
				data.cloudConf.password = $("#cloudPassword").val();
				data.cloudConf.clusterSize = $("#cluster_size_cloud").val();
				data.cloudConf.keyPairs    = $("#keyPairsSelect").val();
				data.cloudConf.securityGroup = $("#securityGroupsSelect").val();
				data.cloudConf.region = $("#regionSelect").val();
				data.cloudConf.zone = $("#zoneSelect").val();
				data.cloudConf.instanceType = $("#instanceTypeSelect").val();
				data.cloudConf.machineImage = $("#machine_image_hadoop").val();

				if($("#autoTerminateYes").attr("checked")){
					data.cloudConf.autoTerminate = true;
					data.cloudConf.timeOutType = "userdefined";
					data.cloudConf.timeOutInterval = $("#timeout_interval_hadoop").val();
				}
				else {
					data.cloudConf.autoTerminate = false;
					data.cloudConf.timeOutType = "";
					data.cloudConf.timeOutInterval = "";
				}
			}
			else{
				var count = 0;
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($("#hadoopCheckBox-"+i).attr("checked")){
						var ip = nodeHadoopJSON[i][0];
						
						var nodeDcInfo = $("#dcInfo-"+i).text();
						if(nodeDcInfo == '') {
							nodeDcInfo = "default-dc";
						}
						
						var nodeRackInfo = $("#rackInfo-"+i).text();
						if(nodeRackInfo == '') {
							nodeRackInfo = "default-rack";
						}
						data.nodes[count] = {};
						data.nodes[count].publicIp = ip;
						data.nodes[count].privateIp = ip;
						data.nodes[count].nameNode = $("#namNodeHadoop-"+i).is(":checked");
						data.nodes[count].dataNode = $("#dataNode-"+i).is(":checked");
						data.nodes[count].secondaryNameNode = $("#secNameNode-"+i).is(":checked");
						data.nodes[count].datacenter = nodeDcInfo;
						data.nodes[count].rack = nodeRackInfo;
						data.nodes[count].os = $("#os-"+i).text();
						data.nodes[count].nodeState = "deploying";
						data.nodes[count].standByNameNode = false;
						if(data.components.Hadoop2 != null) {
							data.nodes[count].type = com.impetus.ankush.hadoopCluster.getNodeTypeForHadoop2(data.nodes[count]);
							if(data.components.Hadoop2.advancedConf.standByNamenode != null) {
								if(data.components.Hadoop2.advancedConf.standByNamenode == data.nodes[count].publicIp) {
									data.nodes[count].standByNameNode = true;
								}
							}	
						}
						else {
							data.nodes[count].type = com.impetus.ankush.hadoopCluster.getNodeTypeForHadoop(data.nodes[count]);
						}
						count++;
					}
				}
			}
			
			// To be changed later: Hbase Master selection as Cluster Namenode
//			if(data.components['Hbase'] != null) {
//				for(var i = 0 ; i < data.nodes.length ; i++){
//					if(data.nodes[i].nameNode) {
//						data.components['Hbase']['advancedConf']['hbaseMaster'] = data.nodes[i].publicIp;
//						data.nodes[i].type += "/HBaseMaster";
//					}
//					if(regionServers != null) {
//						if(regionServers.hasOwnProperty(data.nodes[i].publicIp)) {
//							data.nodes[i].type += "/RegionServer";
//						}	
//					}
//				}	
//			}
//			
//			if(data.components['Zookeeper'] != null) {
//				for(var i = 0 ; i < data.nodes.length ; i++){
//					if(zookeeperNodes != null) {
//						if(zookeeperNodes.hasOwnProperty(data.nodes[i].publicIp)) {
//							data.nodes[i].type += "/Zookeeper";
//						}	
//					}
//				}	
//			}
//			
//			if(data.components['Hive'] != null) {
//				for(var i = 0 ; i < data.nodes.length ; i++){
//					if(hiveServer != null) {
//						if(hiveServer == data.nodes[i].publicIp) {
//							data.nodes[i].type += "/HiveServer";
//						}	
//					}
//				}	
//			}
			$('#hadoopClusterDeploy').button();
	        $('#hadoopClusterDeploy').button('loading');
			clusterCreateUrl = baseUrl + '/cluster/create/Hadoop';
			data["@class"] = "com.impetus.ankush.hadoop.config.HadoopClusterConf";
		//	clusterCreationResult = com.impetus.ankush.placeAjaxCall(clusterCreateUrl,"POST",false,data);
			$.ajax({
				'type' : 'POST',
				'url' : clusterCreateUrl,
				'data' : JSON.stringify(data),
				'contentType' : 'application/json',
				'dataType' : 'json',
				'async' : true,
				'success' : function(result) {
					$('#hadoopClusterDeploy').button('reset');
					clusterCreationResult=result;
					if(!clusterCreationResult.output.status)
					{
						com.impetus.ankush.validation.showAjaxCallErrors(clusterCreationResult.output.error, 'popover-content',
								'error-div-hadoop', 'errorBtnHadoop');
					}
					else
						com.impetus.ankush.hadoopCluster.loadDashboardHadoop();
				},
				'error' : function() {
					$('#hadoopClusterDeploy').button('reset');
					com.impetus.ankush.oClusterSetup.loadDashboard();
				}
			});
			
			
		},
		
		// Function to get Hadoop Node Type for POST call for cluster creation
		getNodeTypeForHadoop:function(node) {
			var type = "";
			if(node.nameNode == true) {
				type = "NameNode/JobTracker/";
			}
			if(node.dataNode == true) {
				type += "DataNode/TaskTracker/";
			}
			if(node.secondaryNameNode == true) {
				type += "SecondaryNameNode/";
			}
			type = type.substring(0, type.length -1);
			return type;
		},
		
		// Function to get Hadoop Node Type for POST call for cluster creation
		getNodeTypeForHadoop2:function(node) {
			var type = "";
			if(data.components.Hadoop2 != null) {
				
//				if(data.components.Hadoop2.advancedConf.startJobHistoryServer) {
//					if(data.components.Hadoop2.advancedConf.jobHistoryServer == node.publicIp) {
//						type += "JobHistoryServer/";
//					}
//				}
				
				if(data.components.Hadoop2.advancedConf.resourceManagerNode != null) {
					if(data.components.Hadoop2.advancedConf.resourceManagerNode == node.publicIp) {
						type += "ResourceManager/";
					}
				}
				if(data.components.Hadoop2.advancedConf.isHAEnabled) {
					if(data.components.Hadoop2.advancedConf.standByNamenode != null) {
						if(data.components.Hadoop2.advancedConf.standByNamenode == node.publicIp) {
							type += "NameNode/";
							if(data.components.Hadoop2.advancedConf.automaticFailoverEnabled) {
								type += "zkfc/";
							}
						}
					}
					if(data.components.Hadoop2.advancedConf.journalNodes != null) {
						if(data.components.Hadoop2.advancedConf.journalNodes.hasOwnProperty(node.publicIp)) {
							type += "JournalNode/";
						}	
					}
				}		
			}
			if(node.nameNode == true) {
				type += "NameNode/";
				if(data.components.Hadoop2.advancedConf.isHAEnabled) {
					if(data.components.Hadoop2.advancedConf.automaticFailoverEnabled) {
						type += "zkfc/";
					}
				}
			}
			if(node.dataNode == true) {
				type += "DataNode/NodeManager/";
			}
			if(node.secondaryNameNode == true) {
				type += "SecondaryNameNode/";
			}
			type = type.substring(0, type.length -1);
			return type;
		},
		// Function to get Hadoop Configuration on page load event for cluster creation
		getDefaultHadoopJson:function() {
			var hadoopUrl=baseUrl+'/app/metadata/hadoop';
			hadoopData=com.impetus.ankush.placeAjaxCall(hadoopUrl,'GET',false);
			if(hadoopData!=null) {
				$("#hadoopVendor").empty();
				for(var vendor in hadoopData.output.hadoop.Hadoop.Vendors){
					$("#hadoopVendor").append("<option value='" + vendor + "'>" + vendor + "</option>");
				}
				com.impetus.ankush.hadoopCluster.fillHadoopVersion();
			}
		},
		
		// Function to populate Hadoop Version based on Hadoop Vendor selection during cluster creation
		fillHadoopVersion:function() {
			com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'] = null;
			$("#hadoopVersion").empty();
			var currentHadoopVendor=$("#hadoopVendor").val();
			for(var version in hadoopData.output.hadoop.Hadoop.Vendors[currentHadoopVendor]){
				$("#hadoopVersion").append("<option value='" + version + "'>" + version + "</option>");
			}
			com.impetus.ankush.hadoopCluster.fillComponentDataTable();
			com.impetus.ankush.hadoopCluster.hadoopVersionChange();
		},
		
		// Function to populate Ecosystem Component Data table during cluster creation
		fillComponentDataTable:function() {
			if(hadoopCreate_EcosystemTable.fnGetData().length==0){
				for(var componentName in hadoopData.output.hadoop) {
					var checkNodeHadoop='<input type="checkbox" id="hadoopEcoSystemCheckBox-'+componentName+'" onclick="com.impetus.ankush.hadoopCluster.activateComponentChildPage(this);" / >';
					var componentNameTable='<span style="font-weight:bold;" id="componentName-'+componentName+'">'+componentName+'</span>';
					var componentVendor='<select class="ecoSystemSelectVendor" disabled="disabled" id="componentVendor-'+componentName+'" onchange="com.impetus.ankush.hadoopCluster.componentVendorChange(this);"></select>';
					var componentVersion='<select class="ecoSystemSelect" disabled="disabled" id="componentVersion-'+componentName+'" onchange="com.impetus.ankush.hadoopCluster.componentVersionChange(this);"></select>';
					var status='<span style="font-weight:bold;" id="componentStatus-'+componentName+'">'+'Uncertified'+'</span>';
					var navigationHadoop='<div><a disabled="disabled" href="#" id="navigationImgHadoopComponent-'+componentName+'"><img  src="'+baseUrl+'/public/images/icon-chevron-right.png" /></a></div>';
					if( !(componentName =='Hadoop' || componentName =='Hadoop2') ){
						var addId_Ecosystem = hadoopCreate_EcosystemTable.fnAddData([
						                                                             checkNodeHadoop,
						                                                             componentNameTable,
						                                                             componentVendor,
						                                                             componentVersion,
						                                                             status,
						                                                             navigationHadoop
						                                                             ]);
						com.impetus.ankush.hadoopCluster.fillComponentVendor(componentName,'componentVendor-');
						com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'componentVendor-', 'componentVersion-');
						var theNode = hadoopCreate_EcosystemTable.fnSettings().aoData[addId_Ecosystem[0]].nTr;
						theNode.setAttribute('id', 'componentRow-'+ componentName);
					}

					$("#navigationImgHadoopComponent-"+componentName).removeAttr('href');
				}
			}
			return true;
		},
		
		// Function to enable setting for a component on component check box click event 
		activateComponentChildPage:function(elem) {
			var id = $(elem).attr('id').split('-');
			componentName = id[id.length-1];
			if($("#hadoopEcoSystemCheckBox-"+componentName).attr('checked')){
				$("#navigationImgHadoopComponent-"+componentName).css({
					"cursor":"pointer"
				});
				$("#componentVendor-" +componentName).removeAttr('disabled');
				$("#componentVersion-" +componentName).removeAttr('disabled');
				$("#navigationImgHadoopComponent-"+componentName).attr('href','javascript:com.impetus.ankush.hadoopCluster.hadoopComponentsDetail(\'' + componentName + '\');');
				com.impetus.ankush.hadoopCluster.componentCheckBoxCheck(componentName);
			}
			else {
				$("#navigationImgHadoopComponent-"+componentName).css({
					"cursor":"default"
				});
				$("#componentVendor-" +componentName).attr("disabled", true);
				$("#componentVersion-" +componentName).attr("disabled", true);
				$("#nodeCheckHead_Ecosystem").removeAttr('checked');
				$("#navigationImgHadoopComponent-"+componentName).removeAttr('href');
				if(com.impetus.ankush.hadoopCluster.echoSystem[componentName] != null)
					delete com.impetus.ankush.hadoopCluster.echoSystem[componentName];
			}
		},
		
		// Function to populate Ecosystem Component Vendor dropdown during cluster creation
		fillComponentVendor:function(componentName,compVendorId) {
			$("#"+compVendorId+componentName).empty();
			var iCount = 0;
			for(var vendor in hadoopData.output.hadoop[componentName]['Vendors']) {
				if(iCount == 0) {
					$("#"+compVendorId+componentName).append("<option selected='selected' value='" + vendor + "'>" + vendor + "</option>");
				}
				else {
					$("#"+compVendorId+componentName).append("<option value='" + vendor + "'>" + vendor + "</option>");	
				}
				iCount++;
			}
			//com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'componentVendor-','componentVersion-');
			return true;
		},
		
		// Function to populate Ecosystem Component Version based on its Vendor selection during cluster creation
		fillComponentVersion:function(componentName, compVendorId, compVersionId) {
			var componentVendor=$("#"+compVendorId+componentName).val();
			$("#"+compVersionId+componentName).empty();
			for(var version in hadoopData.output.hadoop[componentName]['Vendors'][componentVendor]) {
				$("#"+compVersionId+componentName).append("<option value='" + version + "'>" + version + "</option>");
			}
			com.impetus.ankush.hadoopCluster.certifiedData(componentName,'componentVendor-','componentVersion-','componentStatus-');
			return true;
		},
		
		// Function to populate Ecosystem Component certifification status based on its Vendor / Verseion  selection and Hadoop vendor / version selection during cluster creation
		certifiedData:function(componentName,compVendorId,compVersionId,statusId) {
			var currentHadoopVendor=$("#hadoopVendor").val();
			var currentHadoopVersion=$("#hadoopVersion").val();
			var currentComponentVendor=$("#"+compVendorId+componentName).val();
			var currentComponentVersion=$("#"+compVersionId+componentName).val();
			var versionCertification = hadoopData.output.hadoop.Hadoop.Vendors[currentHadoopVendor][currentHadoopVersion]['CertifiedData'];
			if(versionCertification!=undefined){
				if(versionCertification[componentName]!=undefined){
					if(versionCertification[componentName][currentComponentVendor]!=undefined) {
						var versionArray = versionCertification[componentName][currentComponentVendor];
						if(jQuery.inArray(currentComponentVersion,versionArray)!=-1) {
							$("#"+statusId+componentName).empty().append("Certified");
						}
						else{
							$("#"+statusId+componentName).empty().append(" Uncertified");
						}
					}
					else {
						$("#"+statusId+componentName).empty().append(" Uncertified");
					}
				}
				else {
					$("#"+statusId+componentName).empty().append(" Uncertified");
				}
			}
			else {
				$("#"+statusId+componentName).empty().append(" Uncertified");
			}
			return true;
		},
		
		// Function to create object for Ecosystem Component Settings on checkbox click event for component during cluster creation
		componentCheckBoxCheck :function(elem) {
			var isComponentExist = false;
			for (variable in com.impetus.ankush.hadoopCluster.echoSystem) {
				if(variable == componentName) {
					isComponentExist = true;
				}
			}
			if(!isComponentExist) {
				var obj = new echoSystemComponentsTemplate();
				com.impetus.ankush.hadoopCluster.echoSystem[componentName] = obj;
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].name =componentName ;
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor =$("#componentVendor-"+componentName).val();
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].version =$("#componentVersion-"+componentName).val() ;
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].certified =null;
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].source=0;
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].downloadUrl=hadoopData.output.hadoop[componentName]['Vendors'][     com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor][com.impetus.ankush.hadoopCluster.echoSystem[componentName].version];
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].localPath=null ;
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].installationPath=hadoopData.output.hadoop[componentName]['Defaults']['installationHomePath'];
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].isSubmit=false;
			}
		},
		
		// Function to clear hadoop advanced settings (if any) based on hadoop version change
		hadoopVersionChange:function(){
			if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'] != null) {
				if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'].isSubmit)
					com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'] = null;
			}
			for(var componentName in hadoopData.output.hadoop)
				com.impetus.ankush.hadoopCluster.certifiedData(componentName,'componentVendor-','componentVersion-','componentStatus-');
			com.impetus.ankush.hadoopCluster.toggleSecNNColumn();
			return true;
		},
		
		// Function to clear Ecosystem Component settings (if any) based on Ecosystem Component vendor change
		componentVendorChange:function(elem) {
			var id = $(elem).attr('id').split('-');
			componentName = id[id.length-1];
			if(com.impetus.ankush.hadoopCluster.echoSystem[componentName] != null) {
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].isSubmit = false;
			}
			com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'componentVendor-','componentVersion-');
			com.impetus.ankush.hadoopCluster.certifiedData(componentName,'componentVendor-','componentVersion-','componentStatus-');
			return true;
		},
		
		// Function to clear Ecosystem Component settings (if any) based on Ecosystem Component version change
		componentVersionChange:function(elem) {
			var id = $(elem).attr('id').split('-');
			componentName = id[id.length-1];
			if(com.impetus.ankush.hadoopCluster.echoSystem[componentName] != null) {
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].isSubmit = false;
			}
			com.impetus.ankush.hadoopCluster.certifiedData(componentName,'componentVendor-','componentVersion-','componentStatus-');
			return true;
		},

		// Function to open Ecosystem Component settings Page during cluster creation
		hadoopComponentsDetail:function(componentName) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster/componentDetails/'+componentName,
				method : 'get',
				params : {
				},
				title : componentName + ' Advanced Settings',
				tooltipTitle : 'Hadoop Cluster Creation',
				callback : function(data) {
					var version = $('#componentVersion-'+componentName).val();
					var vendor = $('#componentVendor-'+componentName).val();
					com.impetus.ankush.hadoopCluster.loadComponentDetails(data.componentName, vendor, version);
					com.impetus.ankush.hadoopCluster.showComponentAdvFields(data.componentName);
					//com.impetus.ankush.hadoopCluster.syncComponentTableData(data.componentName, vendor, version);
				},
				callbackData : {componentName :componentName}
			});

		},
		
		// Function to show Ecosystem Component Advanced settings (if any) during cluster creation
		showComponentAdvFields:function(componentName) {
			if(componentName=="Hive"||componentName=="Zookeeper"||componentName=="Hbase"){
				$(".component-"+componentName).show();
				if($('#localEnv').hasClass("active")) {
					$('#divNodes-'+componentName).css("display", "block");
				}
			}
			else {
				$(".advanceComponents").hide();
			}
		},
		
		// Function to sync Component Table Data to the Ecosystem Component settings during cluster creation
		syncComponentTableData:function(componentName, vendor, version){
			$("#vendor-"+componentName).val(vendor);
			$("#version-"+componentName).empty();
			com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'vendor-','version-');
			$("#version-"+componentName).val(version);
			$("#certified-"+componentName).text($("#componentStatus-"+componentName).text());
			com.impetus.ankush.hadoopCluster.downloadUrl(componentName);
		},
		
		// Function to sync Hadoop fields to Hadoop Advanced Settings during cluster creation		
		syncHadoopDetails : function() {
			var componentName = "Hadoop";
			var appliedVendor = null;
			var appliedVersion = null;
			if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName] != null){
				appliedVendor = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].vendor;
				appliedVersion = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].version;
			}

			if( ($("#hadoopVendor").val() != appliedVendor) || ($("#hadoopVersion").val() != appliedVersion)) {
				$("#vendor-"+componentName).val($("#hadoopVendor").val());
				$("#version-"+componentName).empty();   
				com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'vendor-','version-');
				$("#version-"+componentName).val($("#hadoopVersion").val());
				$("#path-"+componentName).val(hadoopData.output.hadoop[componentName]['Vendors'][$("#vendor-"+componentName).val()][$("#version-"+componentName).val()]['url']);
				$("#downloadRadio-"+componentName).addClass('active');
				$("#localPathRadio-"+componentName).removeClass('active');
			}
			$("#dfsReplicationHadoop").val($("#hadoopReplicationFactor").val());
			
			var currentVersion=$("#version-"+componentName).val();
			com.impetus.ankush.hadoopCluster.showHideHadoop2Settings(currentVersion);
		},
		
		// Function to open Hadoop Advanced settings Page during cluster creation
		hadoopAdvanceSettings:function() {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster/HadoopDetails',
				method : 'get',
				params : {
				},
				title : 'Hadoop Advanced Settings',
				tooltipTitle : 'Hadoop Cluster Creation',
				callback : function(data) {
					var componentName='Hadoop';
					if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName] != null)
						com.impetus.ankush.hadoopCluster.loadHadoopAppliedValues();
					else
						com.impetus.ankush.hadoopCluster.loadHadoopDefaultValues();
					com.impetus.ankush.hadoopCluster.syncHadoopDetails();
				},
			});
		},
		
		// Function to populate applied values for Hadoop Advanced settings during cluster creation
		loadHadoopAppliedValues : function () {
			var componentName='Hadoop';
			if(!com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].isSubmit){
				com.impetus.ankush.hadoopCluster.loadHadoopDefaultValues();
			}
			else{
				var obj = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName];
				$("#vendor-"+componentName).empty();
				$("#version-"+componentName).empty();

				$("#dfsReplicationHadoop").val(obj.replicationFactorDFS);
				com.impetus.ankush.hadoopCluster.fillComponentVendor(componentName,'vendor-');
				$("#vendor-"+componentName).val(obj.vendor);
				com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'vendor-','version-');
				$("#version-"+componentName).val(obj.version);

				if(obj.source==0) {
					$("#downloadRadio-"+componentName).addClass('active');
					$("#localPathRadio-"+componentName).removeClass('active');
					$("#path-"+componentName).val(obj.downloadUrl);
				}   
				else {

					$("#downloadRadio-"+componentName).removeClass('active');
					$("#localPathRadio-"+componentName).addClass('active');
					$("#path-"+componentName).val(obj.localPath) ;
				}
				$("#installationPath-"+componentName).val(obj.installationPath);
				$("#nameNodePathHadoop").val(obj.nameNodePath);
				$("#dataNodePathHadoop").val(obj.dataDir);
				$("#mapredTempPathHadoop").val(obj.mapRedTmpDir);
				$("#tempPathHadoop").val(obj.hadoopTmpDir);

				if(obj.includeS3SupportHadoop) {
					$("#s3Div").show();
					$("#includeS3SupportHadoop").attr('checked', true);
					obj.includeS3SupportHadoop=true;
					$('#acessKeyS3').val(obj.acessKeyS3);
					$('#secretKeyS3').val(obj.secretKeys3);
				}
				else {
					$("#s3Div").hide();
					$("#includeS3SupportHadoop").attr('checked', false);
				}

				if(obj.includeS3nSupportHadoop) {
					$("#s3nDiv").show();
					$("#includeS3nSupportHadoop").attr('checked', true);
					$('#acessKeyS3n').val(obj.acessKeyS3n);
					$('#secretKeys3n').val(obj.secretKeys3n);
				}
				else {
					$("#s3nDiv").hide();
					$("#includeS3nSupportHadoop").attr('checked', false);
				}
				if(obj.isHadoop2) {
					
					if(nodeTableLength != null){
						for(var i = 0 ; i < nodeTableLength ; i++){
							if($('#hadoopCheckBox-'+i).attr('checked')) {   
								$("#jobHistoryServer").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
								$("#resourceManagerNode").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
								$("#webAppProxyNode").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
							}
						}
						$("#resourceManagerNode").val(obj.resourceManagerNode);
						if(obj.startJobHistoryServer) {
							$("#checkboxJobHistoryServer").attr("checked", true);
							$("#jobHistoryServer").val(obj.jobHistoryServer);
							$("#jobHistoryServer").removeAttr('disabled');
						}
					}
					$("#webAppProxyEnable").removeClass("active");
					$("#webAppProxyDisable").removeClass("active");
					
					if(obj.isWebProxyEnabled) {
						$("#webAppProxyNode").val(obj.webAppProxyNode);
						$("#webAppProxyPort").val(obj.webAppProxyPort);
						$("#webAppProxyEnable").addClass("active");
						$('#webAppProxyDiv').css('display', 'block');
					} else {
						$("#webAppProxyDisable").addClass("active");
						$('#webAppProxyDiv').css('display', 'none');
						$("#webAppProxyPort").val(hadoopData.output.hadoop[componentName]['Defaults'].webAppProxyPort);
					}
					
					if(obj.isHAEnabled) {
						$("#nameserviceId").val(obj.nameserviceId);
						$("#nameNodeId1").val(obj.nameNodeId1);
						$("#nameNodeId2").val(obj.nameNodeId2);
						$("#journalNodeEditsDir").val(obj.journalNodeEditsDir);
						if(!obj.automaticFailoverEnabled) {
							$('#automaticFailoverEnabled').removeClass('active');
							$('#automaticFailoverDisabled').addClass('active');
						}
						if(nodeTableLength != null){
							for(var i = 0 ; i < nodeTableLength ; i++){
								if($('#hadoopCheckBox-'+i).attr('checked')){   
									$("#haStandByNode").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
								}
							}
							$("#haStandByNode").val(obj.standByNamenode);
							
							for(var i = 0 ; i < nodeTableLength ; i++){
								if($('#hadoopCheckBox-'+i).attr('checked')){
									if(!obj.journalNodes.hasOwnProperty(nodeHadoopJSON[i][0])) {
										$("#haJournalNodes").append(
												"<option value='"
												+ nodeHadoopJSON[i][0]
												+ "'>"
												+ nodeHadoopJSON[i][0]
												+ "</option>");   
									}
									else {
										$("#haJournalNodes").append(
												"<option value='"
												+ nodeHadoopJSON[i][0]
												+ "' selected>"
												+ nodeHadoopJSON[i][0]
												+ "</option>");
									}

								}
							}
							$("#haJournalNodes").multiselect({
								buttonClass: 'btn',
								maxHeight: '150',
								buttonText: function(options, select) {
									if (options.length == 0) {
										return 'None selected <b class="caret"></b>';
									}
									else if (options.length > 4) {
										return options.length + ' selected <b class="caret"></b>';
									}
									else {
										var selected = '';
										options.each(function() {
											selected += $(this).text() + ', ';
										});
										return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
									}
								},
							});
							$(".multiselect").click(function(){
								$(".dropdown-menu").toggle('fast');
							});
							$(document).click(function(e){
								$(".dropdown-menu").hide();
							});
							$(".multiselect").click(function(e){
								e.stopPropagation();
							});
						}
					}
					else {
						$('#hadoopHADiv').css('display', 'none');
						$('#hadoopHAEnable').removeClass('active');
						$('#hadoopHADisable').addClass('active');
						com.impetus.ankush.hadoopCluster.loadHadoopHADefaultValues(hadoopData.output.hadoop[componentName]['Defaults']);
					}
				}
				else {
					$('#hadoop2Settings').css('display', 'none');
					com.impetus.ankush.hadoopCluster.loadHadoop2DefualtValues(hadoopData.output.hadoop[componentName]['Defaults']);
				}
				
			}
		},
		
		loadHadoop2DefualtValues : function(hadoopDefault) {
			$("#resourceManagerNode").empty();
			$("#jobHistoryServer").empty();
			$("#webAppProxyNode").empty();
			if(nodeTableLength != null){
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($('#hadoopCheckBox-'+i).attr('checked')){
						$("#resourceManagerNode").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
						$("#jobHistoryServer").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
						$("#webAppProxyNode").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
					}
				}
			}
			$("#webAppProxyPort").val(hadoopDefault.webAppProxyPort);
			com.impetus.ankush.hadoopCluster.loadHadoopHADefaultValues(hadoopDefault);		
		},
		
		loadHadoopHADefaultValues : function(hadoopDefault) {
			$("#nameserviceId").val($('#hadoop_cluster_name').val());
			$("#nameNodeId1").val(hadoopDefault.nameNodeId1);
			$("#nameNodeId2").val(hadoopDefault.nameNodeId2);
			$("#journalNodeEditsDir").val(hadoopDefault.journalNodeEditsDir.split('${user}').join($("#usernameHadoop").val()));
			$("#haStandByNode").empty();
			$("#haJournalNodes").empty();
			if(nodeTableLength != null){
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($('#hadoopCheckBox-'+i).attr('checked')){   
						$("#haStandByNode").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
					}
				}
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($('#hadoopCheckBox-'+i).attr('checked')){
						$("#haJournalNodes").append(
							"<option value='"
							+ nodeHadoopJSON[i][0]
							+ "' selected>"
							+ nodeHadoopJSON[i][0]
							+ "</option>");   
						}
					}
					$("#haJournalNodes").multiselect({
						buttonClass: 'btn',
						maxHeight: '150',
						buttonText: function(options, select) {
							if (options.length == 0) {
								return 'None selected <b class="caret"></b>';
							}
							else if (options.length > 4) {
								return options.length + ' selected <b class="caret"></b>';
							}
							else {
								var selected = '';
								options.each(function() {
									selected += $(this).text() + ', ';
								});
								return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
							}
						},
					});
					$(".multiselect").click(function(){
						$(".dropdown-menu").toggle('fast');
					});
					$(document).click(function(e){
						$(".dropdown-menu").hide();
					});
					$(".multiselect").click(function(e){
						e.stopPropagation();
					});
			}
		},
		
		// Apply Button click event for Ecosystem Component settings during cluster creation
		applyComponent:function(componentName) {
			com.impetus.ankush.hadoopCluster.validateApply_ComponentAdvSettings(componentName);
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop-ComponentAdvSettings', 'errorBtnHadoop-ComponentAdvSettings');
				return;
			}
			if(com.impetus.ankush.hadoopCluster.echoSystem[componentName].isSubmit) {
				delete com.impetus.ankush.hadoopCluster.echoSystem[componentName];
				com.impetus.ankush.hadoopCluster.echoSystem[componentName] = new Object();
			}
			com.impetus.ankush.hadoopCluster.trimTextBoxFields();
			com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor = $("#vendor-"+componentName).val();
			com.impetus.ankush.hadoopCluster.echoSystem[componentName].version =$("#version-"+componentName).val() ;
			com.impetus.ankush.hadoopCluster.echoSystem[componentName].certified =$("#certified-"+componentName).text();

			// Change Component Configuration Values on Parent Page
			$("#componentVendor-"+componentName).val(com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor);
			$("#componentVersion-"+componentName).val(com.impetus.ankush.hadoopCluster.echoSystem[componentName].version);
			$("#componentStatus-"+componentName).text(com.impetus.ankush.hadoopCluster.echoSystem[componentName].certified);

			if($("#downloadRadio-"+componentName).hasClass('active')) {
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].source = '0';
			}
			else {
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].source = '1';
			}
			if(com.impetus.ankush.hadoopCluster.echoSystem[componentName].source=='0'){
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].localPath='';
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].downloadUrl=$("#path-"+componentName).val();
			}
			else{
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].downloadUrl='';
				com.impetus.ankush.hadoopCluster.echoSystem[componentName].localPath=$("#path-"+componentName).val() ;
			}
			com.impetus.ankush.hadoopCluster.echoSystem[componentName].installationPath=$("#installationPath-"+componentName).val();
			com.impetus.ankush.hadoopCluster.echoSystem[componentName].isSubmit=true;

			/**Hive Advance fields **/
			if(componentName == 'Hive'){
				connectionPassword=$("#connectionPassword").val();
				connectionURL=$("#connectionURL").val();
				connectionDriverName=$("#connectionDriverName").val();
				connectionUserName=$("#connectionUserName").val();
				hiveServer = $("#hiveServerHadoop").val();
			}

			/**Zookeeper Advance fields **/
			if(componentName == 'Zookeeper'){
				zookeeperNodes = new Object();
				clientPort=$("#clientPort").val();
				dataDir=$("#dataDir").val();
				syncLimit=$("#syncLimit").val();
				initLimit=$("#initLimit").val();
				tickTime=$("#tickTime").val();
				var tmp_zookeeperNodes = new Object();
				tmp_zookeeperNodes = $("#zookeeperNodes").val();
				if(tmp_zookeeperNodes != null) {
					for(var i = 0; i < tmp_zookeeperNodes.length; i++){
						zookeeperNodes[tmp_zookeeperNodes[i]] = tmp_zookeeperNodes[i];
					}
				}

			}

			/**Hbase Advance fields **/
			if(componentName == 'Hbase'){
				regionServers = new Object();
				hbaseMaster = $("#hbaseMasterHadoop").val();
				var tmp_regionServers = new Object();
				tmp_regionServers = $("#regionServersHadoop").val();
				if(tmp_regionServers != null) {
					for(var i = 0; i < tmp_regionServers.length; i++){
						regionServers[tmp_regionServers[i]] = tmp_regionServers[i];
					}
				}
				compactionThreshold=$("#compactionThreshold").val();
				cacheSize=$("#cacheSize").val();
				filesize=$("#filesize").val();
				caching=$("#caching").val();
				timeout=$("#timeout").val();
				multiplier=$("#multiplier").val();
				majorcompaction=$("#majorcompaction").val();
				maxsize=$("#maxsize").val();
				flushSize=$("#flushSize").val();
				handlerCount=$("#handlerCount").val();
			}

			$("#componentVendor-"+componentName).empty();
			$("#componentVendor-"+componentName).append("<option value='" +
					com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor  +
					"'>" + com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor  + "</option>");
			for(var vendor in hadoopData.output.hadoop[componentName]['Vendors']) {
				if(vendor!=com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor )
					$("#componentVendor-"+componentName).append("<option value='" + vendor + "'>" + vendor + "</option>");
			}
			$("#componentVersion-"+componentName).empty();
			$("#componentVersion-"+componentName).append("<option value='" + com.impetus.ankush.hadoopCluster.echoSystem[componentName].version  + "'>" + com.impetus.ankush.hadoopCluster.echoSystem[componentName].version  + "</option>");
			for(var version in hadoopData.output.hadoop[componentName]['Vendors'][com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor ]) {
				if(version!=com.impetus.ankush.hadoopCluster.echoSystem[componentName].version )
					$("#componentVersion-"+componentName).append("<option value='" + version + "'>" + version + "</option>");
			}
			$("#componentStatus-"+componentName).text(com.impetus.ankush.hadoopCluster.echoSystem[componentName].certified);

//			$('#content-panel').sectionSlider('removeChildPanel', {
//				childNo : 2
//			});
			com.impetus.ankush.removeChild(2);
			com.impetus.ankush.hadoopCluster.goToAnchor('hadoopEcosystem_Jump');

			//com.impetus.ankush.hadoopCluster.ecoSystemAdvanceValues(componentName);
			return true;
		},
		
		// Revert Button click event for Ecosystem Component settings during cluster creation
		revertComponent:function(componentName) {
			com.impetus.ankush.removeChild(2);
			com.impetus.ankush.hadoopCluster.goToAnchor('hadoopEcosystem_Jump');
			return true;
		},
		
		// Function to populate download path for Hadoop / Component settings during cluster creation
		downloadUrl:function(componentName) {
			
			if( $("#downloadRadio-"+componentName).hasClass('active')) {
				return;
			}
			valComponentLocalPath = $("#path-"+componentName).val(); 
			if(valComponentDownloadPath != null) {
				$("#path-"+componentName).val(valComponentDownloadPath);
				return;
			}
			com.impetus.ankush.hadoopCluster.fillDownloadPath(componentName);
		},
		
		// Function to clear local path field for Hadoop / Component settings during cluster creation		
		localUrl:function(componentName) {

			if( $("#localPathRadio-"+componentName).hasClass('active')) {
				return;
			}
			valComponentDownloadPath = $("#path-"+componentName).val();
			$("#path-"+componentName).val(valComponentLocalPath);
		},
		
		// Function to populate default values for Hadoop settings during cluster creation		
		loadHadoopDefaultValues:function() {
			var componentName='Hadoop';
			var hadoopDefault=hadoopData.output.hadoop[componentName]['Defaults'];
			com.impetus.ankush.hadoopCluster.fillComponentVendor(componentName,'vendor-');
			com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'vendor-','version-');
			
			$("#path-"+componentName).val(hadoopData.output.hadoop[componentName]['Vendors'][$("#vendor-"+componentName).val()][$("#version-"+componentName).val()]['url']);
			com.impetus.ankush.hadoopCluster.fillInstallationPath(componentName);
			$("#nameNodePathHadoop").val(hadoopDefault.nameNodePath.split('${user}').join($("#usernameHadoop").val()));
			$("#dataNodePathHadoop").val(hadoopDefault.dataDir.split('${user}').join($("#usernameHadoop").val()));
			$("#mapredTempPathHadoop").val(hadoopDefault.mapRedTmpDir.split('${user}').join($("#usernameHadoop").val()));
			$("#tempPathHadoop").val(hadoopDefault.hadoopTmpDir.split('${user}').join($("#usernameHadoop").val()));

			if(hadoopDefault.includeS3SupportProperties=='false') {
				$("#s3Div").hide();
			}
			else {
				$("#s3Div").show();
				$("#includeS3SupportHadoop").attr("checked",true);
			}
			if(hadoopDefault.includeS3nSupportProperties=='false') {
				$("#s3nDiv").hide();
			}
			else {
				$("#s3nDiv").show();
				$("#includeS3nSupportHadoop").attr("checked",true);
			}
			$("#dfsReplicationHadoop").val(hadoopDefault.replicationFactorDFS);
		
			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected($('#hadoopVersion').val())) {
				com.impetus.ankush.hadoopCluster.loadHadoop2DefualtValues(hadoopDefault);
			}
		},
		
		// Function to discard applied values for Hadoop / Component settings on version / vendor change
		discardComponentAppliedValues : function(discardComponentName) {
			if(discardComponentName == "Hadoop") {
				delete com.impetus.ankush.hadoopCluster.hadoopAdvanceData[discardComponentName];
				com.impetus.ankush.hadoopCluster.hadoopAdvanceData[discardComponentName] = new Object();
			}
			else {
				delete com.impetus.ankush.hadoopCluster.echoSystem[discardComponentName];
				com.impetus.ankush.hadoopCluster.echoSystem[discardComponentName] = new Object();
			}   
		},
		
		// Function to show / hide Hive Connection password value during cluster creation
		showHiveConnPswd : function() {
			if($('#hiveConnPswd_ShowPswd').attr('checked')) {
				$('#connectionPassword_PlainText').val($('#connectionPassword').val());
				$('#connectionPassword_PlainText').css('display', 'block');
				$('#connectionPassword').css('display', 'none');
			}
			else {
				$('#connectionPassword').val($('#connectionPassword_PlainText').val());
				$('#connectionPassword_PlainText').css('display', 'none');
				$('#connectionPassword').css('display', 'block');
			}
		},
		
		// Function to populate Ecosystem Component settings during cluster creation
		loadComponentDetails:function(componentName, vendor, version) {
			valComponentLocalPath = null;
			valComponentDownloadPath = null;
			$('#path-'+componentName).val('');
			if(com.impetus.ankush.hadoopCluster.echoSystem[componentName].isSubmit){
				com.impetus.ankush.hadoopCluster.loadAppliedValues(componentName);
				com.impetus.ankush.hadoopCluster.loadAdvancedFieldsSubmitted(componentName);
			}
			else{
				com.impetus.ankush.hadoopCluster.fillComponentVendor(componentName,'vendor-');
				$('#vendor-'+componentName).val(vendor);
				com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'vendor-','version-');
				$('#version-'+componentName).val(version);
				com.impetus.ankush.hadoopCluster.certifiedData(componentName,'vendor-','version-','certified-');
				com.impetus.ankush.hadoopCluster.fillDownloadPath(componentName);
				com.impetus.ankush.hadoopCluster.fillInstallationPath(componentName);
				com.impetus.ankush.hadoopCluster.loadAdvancedFields(componentName);
			}
			return true;
		},
		
		// Function to populate applied values for Ecosystem Component settings during cluster creation
		loadAppliedValues:function(componentName) {
			$("#vendor-"+componentName).empty();
			$("#vendor-"+componentName).append("<option selected value='" + com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor  + "'>" + com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor  + "</option>");
			for(var vendor in hadoopData.output.hadoop[componentName]['Vendors']) {
				if(vendor!=com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor)
					$("#vendor-"+componentName).append("<option value='" + vendor + "'>" + vendor + "</option>");
			}
			$("#version-"+componentName).empty();
			$("#version-"+componentName).append("<option value='" + com.impetus.ankush.hadoopCluster.echoSystem[componentName].version  + "'>" + com.impetus.ankush.hadoopCluster.echoSystem[componentName].version  + "</option>");
			for(var version in hadoopData.output.hadoop[componentName]['Vendors'][com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor ]) {
				if(version!=com.impetus.ankush.hadoopCluster.echoSystem[componentName].version )
					$("#version-"+componentName).append("<option value='" + version + "'>" + version + "</option>");
			}
			$("#certified-"+componentName).text(com.impetus.ankush.hadoopCluster.echoSystem[componentName].certified);
			
			if(com.impetus.ankush.hadoopCluster.echoSystem[componentName].source == "0"){
//				$("#downloadRadio-"+componentName).attr("checked",true);
				$("#downloadRadio-"+componentName).addClass('active');
				$("#localPathRadio-"+componentName).removeClass('active');
				$("#path-"+componentName).val(com.impetus.ankush.hadoopCluster.echoSystem[componentName].downloadUrl);
			}
			else {
				$("#downloadRadio-"+componentName).removeClass('active');
				$("#localPathRadio-"+componentName).addClass('active');
				$("#path-"+componentName).val(com.impetus.ankush.hadoopCluster.echoSystem[componentName].localPath);
			}
			$("#installationPath-"+componentName).val(com.impetus.ankush.hadoopCluster.echoSystem[componentName].installationPath);

		},
		
		// Function to populate default values for Ecosystem Component settings during cluster creation
		loadAdvancedFields:function(componentName) {
			if(componentName=="Hive"){
				$("#hiveServerHadoop").empty();
				$("#connectionPassword").val(hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionPassword"]);
				$("#connectionPassword_PlainText").val(hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionPassword"]);
				$("#connectionURL").val(hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionURL"]);
				$("#connectionDriverName").val(hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionDriverName"]);
				$("#connectionUserName").val(hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionUserName"]);
				if(nodeTableLength != null){
					for(var i = 0 ; i < nodeTableLength ; i++){
						if($('#hadoopCheckBox-'+i).attr('checked')){   
							$("#hiveServerHadoop").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
						}
					}
				}
			}
			if(componentName=="Zookeeper"){
				$("#zookeeperNodes").empty();
				$("#clientPort").val(hadoopData.output.hadoop[componentName]['Defaults']["clientPort"]);
				$("#dataDir").val(hadoopData.output.hadoop[componentName]['Defaults']["dataDir"].split('${user}').join($("#usernameHadoop").val()));
				$("#syncLimit").val(hadoopData.output.hadoop[componentName]['Defaults']["syncLimit"]);
				$("#initLimit").val(hadoopData.output.hadoop[componentName]['Defaults']["initLimit"]);
				$("#tickTime").val(hadoopData.output.hadoop[componentName]['Defaults']["tickTime"]);
				if(nodeTableLength != null){

					for(var i = 0 ; i < nodeTableLength ; i++){
						if($('#hadoopCheckBox-'+i).attr('checked')){
							$("#zookeeperNodes").append(
									"<option value='"
									+ nodeHadoopJSON[i][0]
									+ "' selected>"
									+ nodeHadoopJSON[i][0]
									+ "</option>");   
						}
					}
					$("#zookeeperNodes").multiselect({
						buttonClass: 'btn',
						maxHeight: '150',
						buttonText: function(options, select) {
							if (options.length == 0) {
								return 'None selected <b class="caret"></b>';
							}
							else if (options.length > 4) {
								return options.length + ' selected <b class="caret"></b>';
							}
							else {
								var selected = '';
								options.each(function() {
									selected += $(this).text() + ', ';
								});
								return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
							}
						},
					});
					$(".multiselect").click(function(){
						$(".dropdown-menu").toggle('fast');
					});
					$(document).click(function(e){
						$(".dropdown-menu").hide();
					});
					$(".multiselect").click(function(e){
						e.stopPropagation();
					});


				}
			}
			if(componentName=="Hbase"){
				$("#hbaseMasterHadoop").empty();
				$("#compactionThreshold").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.hstore.compactionThreshold"]);
				$("#cacheSize").val(hadoopData.output.hadoop[componentName]['Defaults']["hfile.block.cache.size"]);
				$("#filesize").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.max.filesize"]);
				$("#caching").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.client.scanner.caching"]);
				$("#timeout").val(hadoopData.output.hadoop[componentName]['Defaults']["zookeeper.session.timeout"]);
				$("#multiplier").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.memstore.block.multiplier"]);
				$("#majorcompaction").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.majorcompaction"]);
				$("#maxsize").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.client.keyvalue.maxsize"]);
				$("#flushSize").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.memstore.flush.size"]);
				$("#handlerCount").val(hadoopData.output.hadoop[componentName]['Defaults']["hbase.regionserver.handler.count"]);
				if(nodeTableLength != null){
					for(var i = 0 ; i < nodeTableLength ; i++){
						if($('#hadoopCheckBox-'+i).is(':checked')){
							$("#hbaseMasterHadoop").append(
									"<option value='"
									+ nodeHadoopJSON[i][0]
									+ "'>"
									+ nodeHadoopJSON[i][0]
									+ "</option>");
							$("#regionServersHadoop").append(
									"<option value='"
									+ nodeHadoopJSON[i][0]
									+ "' selected>"
									+ nodeHadoopJSON[i][0]
									+ "</option>");   
						}
					}
					$("#regionServersHadoop").multiselect({
						buttonClass: 'btn',
						maxHeight: '150',
						buttonText: function(options, select) {
							if (options.length == 0) {
								return 'None selected <b class="caret"></b>';
							}
							else if (options.length > 4) {
								return options.length + ' selected <b class="caret"></b>';
							}
							else {
								var selected = '';
								options.each(function() {
									selected += $(this).text() + ', ';
								});
								return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
							}
						},
					});
					$(".multiselect").click(function(){
						$(".dropdown-menu").toggle('fast');
					});
					$(document).click(function(e){
						$(".dropdown-menu").hide();
					});
					$(".multiselect").click(function(e){
						e.stopPropagation();
					});


				}
			}
		},
		
		// Function to populate applied values for Ecosystem Component Advanced settings during cluster creation
		loadAdvancedFieldsSubmitted:function(componentName) {
			if(componentName=="Hive"){
				$("#connectionPassword").val(connectionPassword);
				$("#connectionPassword_PlainText").val(connectionPassword);
				$("#connectionURL").val(connectionURL);
				$("#connectionDriverName").val(connectionDriverName);
				$("#connectionUserName").val(connectionUserName);
				if(nodeTableLength != null){
					for(var i = 0 ; i < nodeTableLength ; i++){
						if($('#hadoopCheckBox-'+i).attr('checked')){   
							$("#hiveServerHadoop").append('<option value="'+nodeHadoopJSON[i][0]+'">'+nodeHadoopJSON[i][0]+'</option>');
						}
					}
				}
				$("#hiveServerHadoop").val(hiveServer);
			}
			if(componentName=="Zookeeper"){
				$("#clientPort").val(clientPort);
				$("#dataDir").val(dataDir);
				$("#syncLimit").val(syncLimit);
				$("#initLimit").val(initLimit);
				$("#tickTime").val(tickTime);

				if(nodeTableLength != null){

					for(var i = 0 ; i < nodeTableLength ; i++){
						if($('#hadoopCheckBox-'+i).attr('checked')){
							if(!zookeeperNodes.hasOwnProperty(nodeHadoopJSON[i][0])) {
								$("#zookeeperNodes").append(
										"<option value='"
										+ nodeHadoopJSON[i][0]
										+ "'>"
										+ nodeHadoopJSON[i][0]
										+ "</option>");   
							}
							else {
								$("#zookeeperNodes").append(
										"<option value='"
										+ nodeHadoopJSON[i][0]
										+ "' selected>"
										+ nodeHadoopJSON[i][0]
										+ "</option>");
							}

						}
					}
					$("#zookeeperNodes").multiselect({
						buttonClass: 'btn',
						maxHeight: '150',
						buttonText: function(options, select) {
							if (options.length == 0) {
								return 'None selected <b class="caret"></b>';
							}
							else if (options.length > 4) {
								return options.length + ' selected <b class="caret"></b>';
							}
							else {
								var selected = '';
								options.each(function() {
									selected += $(this).text() + ', ';
								});
								return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
							}
						},
					});
					$(".multiselect").click(function(){
						$(".dropdown-menu").toggle('fast');
					});
					$(document).click(function(e){
						$(".dropdown-menu").hide();
					});
					$(".multiselect").click(function(e){
						e.stopPropagation();
					});
				}
			}
			if(componentName=="Hbase"){
				$("#compactionThreshold").val(compactionThreshold);
				$("#cacheSize").val( cacheSize);
				$("#filesize").val(filesize);
				$("#caching").val(caching);
				$("#timeout").val(timeout);
				$("#multiplier").val(multiplier);
				$("#majorcompaction").val(majorcompaction);
				$("#maxsize").val(maxsize);
				$("#flushSize").val(flushSize);
				$("#handlerCount").val(handlerCount);
				if(nodeTableLength != null){
					for(var i = 0 ; i < nodeTableLength ; i++){
						if($('#hadoopCheckBox-'+i).is(':checked')){
							$("#hbaseMasterHadoop").append(
									"<option value='"
									+ nodeHadoopJSON[i][0]
									+ "'>"
									+ nodeHadoopJSON[i][0]
									+ "</option>");
							if(!regionServers.hasOwnProperty(nodeHadoopJSON[i][0])) {
								$("#regionServersHadoop").append(
										"<option value='"
										+ nodeHadoopJSON[i][0]
										+ "'>"
										+ nodeHadoopJSON[i][0]
										+ "</option>");   
							}
							else {
								$("#regionServersHadoop").append(
										"<option value='"
										+ nodeHadoopJSON[i][0]
										+ "' selected>"
										+ nodeHadoopJSON[i][0]
										+ "</option>");
							}

						}
					}
					$("#hbaseMasterHadoop").val(hbaseMaster);
					$("#regionServersHadoop").multiselect({
						buttonClass: 'btn',
						maxHeight: '150',
						buttonText: function(options, select) {
							if (options.length == 0) {
								return 'None selected <b class="caret"></b>';
							}
							else if (options.length > 4) {
								return options.length + ' selected <b class="caret"></b>';
							}
							else {
								var selected = '';
								options.each(function() {
									selected += $(this).text() + ', ';
								});
								return selected.substr(0, selected.length -2) + ' <b class="caret"></b>';
							}
						},
					});
					$(".multiselect").click(function(){
						$(".dropdown-menu").toggle('fast');
					});
					$(document).click(function(e){
						$(".dropdown-menu").hide();
					});
					$(".multiselect").click(function(e){
						e.stopPropagation();
					});
				}
			}
		},
		
		// Function to populate component settings based on component vendor change  during cluster creation
		componentInternalVendorChange:function(componentName) {
			com.impetus.ankush.hadoopCluster.fillComponentVersion(componentName,'vendor-','version-');
			com.impetus.ankush.hadoopCluster.certifiedData(componentName,'vendor-','version-','certified-');
			com.impetus.ankush.hadoopCluster.fillDownloadPath(componentName);
			$("#localPathRadio-"+componentName).removeAttr("checked");
//			$("#downloadRadio-"+componentName).attr("checked", true);
			$("#downloadRadio-"+componentName).addClass('active');
			$("#localPathRadio-"+componentName).removeClass('active');
			return true;
		},
		
		// Function to populate component settings based on component version change  during cluster creation		
		componentInternalVersionChange:function(componentName) {
			com.impetus.ankush.hadoopCluster.certifiedData(componentName,'vendor-','version-','certified-');
			com.impetus.ankush.hadoopCluster.fillDownloadPath(componentName);
//			com.impetus.ankush.hadoopCluster.updateHadoopAdvFields();
//			$("#localPathRadio-"+componentName).removeAttr("checked");
//			$("#downloadRadio-"+componentName).attr("checked", true);
			$("#downloadRadio-"+componentName).addClass('active');
			$("#localPathRadio-"+componentName).removeClass('active');
			
			if(componentName == 'Hadoop') {
				var currentVersion=$("#version-"+componentName).val();
				if(com.impetus.ankush.hadoopCluster.isHadoop2Selected(currentVersion)) {
					$('#hadoop2Settings').css('display', 'block');
					com.impetus.ankush.hadoopCluster.loadHadoop2DefualtValues(hadoopData.output.hadoop[componentName]['Defaults']);
				} else {
					$('#hadoop2Settings').css('display', 'none');
				}
			}
			return true;
		},
		
		// Function to populate download url for component settings based on component version / version		
		fillDownloadPath:function(componentName) {
			var currentVendor=$("#vendor-"+componentName).val();
			var currentVersion=$("#version-"+componentName).val();
			$("#path-"+componentName).val(hadoopData.output.hadoop[componentName]['Vendors'][currentVendor][currentVersion].url);
		},
		
		// Function to populate installation path for component settings based on component version / version
		fillInstallationPath:function(componentName) {
			$("#installationPath-"+componentName).val(hadoopData.output.hadoop[componentName]['Defaults']['installationHomePath'].split('${user}').join($("#usernameHadoop").val()));
		},
		
		// Function to show / hide S3 fields under Hadoop Advanced settings during cluster creation
		s3Property:function() {
			if($("#includeS3SupportHadoop").attr('checked')){
				$("#s3Div").show();
			}
			else
				$("#s3Div").hide();
		},
		
		// Function to show / hide S3n fields under Hadoop Advanced settings during cluster creation
		s3nProperty:function() {
			if($("#includeS3nSupportHadoop").attr('checked')){
				$("#s3nDiv").show();
			}
			else
				$("#s3nDiv").hide();
		},
		
		// Function to trim text box fields before creating POST JSON for cluster deployment call
		trimTextBoxFields : function() {
			var temp_secretKeyS3 = $('#secretKeyS3').val();
			var temp_secretKeys3n = $('#secretKeys3n').val();
			var NodePassword =  $("#passwordHadoop").val();
			var cloudPassword = $("#cloudPassword").val();
			var hiveConnPass = $("#connectionPassword").val();

			$('input[type=text]').each(function() {
				$(this).val($.trim($(this).val()));
			});

			$('#secretKeyS3').val(temp_secretKeyS3);
			$('#secretKeys3n').val(temp_secretKeys3n);
			$("#passwordHadoop").val(NodePassword);
			$("#cloudPassword").val(cloudPassword);
			$("#connectionPassword").val(hiveConnPass);
		},
		
		// Apply Button click event for Hadoop Advanced settings during cluster creation
		applyHadoop:function() {

			com.impetus.ankush.hadoopCluster.validateApply_HadoopAdvSettings();
			
			if(com.impetus.ankush.validation.errorCount > 0) {
				com.impetus.ankush.validation.showErrorDiv('error-div-hadoop-HadoopAdvSettings', 'errorBtnHadoop-HadoopAdvSettings');
				return;
			}
			
			com.impetus.ankush.hadoopCluster.trimTextBoxFields();           
			var componentName='Hadoop';
			var obj = new hadoopValues();
			obj.replicationFactorDFS=$("#dfsReplicationHadoop").val();
			obj.vendor=$("#vendor-"+componentName).val();
			obj.version=$("#version-"+componentName).val();

			// Change Hadoop Configuration Values on Parent Page
			$("#hadoopReplicationFactor").val(obj.replicationFactorDFS);
			$("#hadoopVendor").val(obj.vendor);
			$("#hadoopVersion").empty();
			var currentHadoopVendor=$("#hadoopVendor").val();
			for(var version in hadoopData.output.hadoop.Hadoop.Vendors[currentHadoopVendor]){
				$("#hadoopVersion").append("<option value='" + version + "'>" + version + "</option>");
			}
			$("#hadoopVersion").val(obj.version);

			obj.source=$("#sourceTypeGroupBtn .active").data("value");
			if(obj.source==0){
				obj.downloadUrl=$("#path-"+componentName).val();
				obj.localPath='';
			}
			else{
				obj.downloadUrl='';
				obj.localPath=$("#path-"+componentName).val() ;
			}

			obj.mapRedTmpDir=$("#mapredTempPathHadoop").val();
			obj.nameNodePath=$("#nameNodePathHadoop").val();
			obj.hadoopTmpDir=$("#tempPathHadoop").val();
			obj.includeMasterAsSlave=$("#nameNodePathHadoop").val();
			obj.installationPath=$("#installationPath-"+componentName).val();

			if($("#includeS3SupportHadoop").attr('checked')) {
				obj.includeS3SupportHadoop=true;
				obj.acessKeyS3 = $('#acessKeyS3').val();
				obj.secretKeys3 = $('#secretKeyS3').val();
			}

			else {
				obj.includeS3SupportHadoop=false;
			}

			if($("#includeS3nSupportHadoop").attr('checked')) {
				obj.includeS3nSupportHadoop=true;
				obj.acessKeyS3n = $('#acessKeyS3n').val();
				obj.secretKeys3n = $('#secretKeys3n').val();
			}
			else
				obj.includeS3nSupportHadoop=false;
			obj.dataDir=$("#dataNodePathHadoop").val();

			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected(obj.version)) {
				obj.isHadoop2 = true;
				obj.startJobHistoryServer = false;  // Based On Checkbox selection
				if($("#checkboxJobHistoryServer").attr("checked")) {
					obj.jobHistoryServer = $('#jobHistoryServer').val();
					obj.startJobHistoryServer = true;
				} else {
					obj.jobHistoryServer = null;
				}
				
				if($('#webAppProxyEnable').hasClass('active')) {
					obj.isWebProxyEnabled = true;
					obj.webAppProxyNode = $('#webAppProxyNode').val();
					obj.webAppProxyPort = $("#webAppProxyPort").val();
				} else {
					obj.isWebProxyEnabled = false;
					obj.webAppProxyNode = null;
					obj.webAppProxyPort = null;
				}
				
				obj.resourceManagerNode = $('#resourceManagerNode').val();
				if($('#hadoopHAEnable').hasClass('active')) {
					obj.isHAEnabled = true;
					obj.nameserviceId = $("#nameserviceId").val();
					obj.standByNamenode = $("#haStandByNode").val();
					obj.nameNodeId1 = $("#nameNodeId1").val();
					obj.nameNodeId2 = $("#nameNodeId2").val();
					obj.journalNodeEditsDir = $("#journalNodeEditsDir").val();
					obj.journalNodes = new Object();
					var tmp_journalNodes = new Object();
					tmp_journalNodes = $("#haJournalNodes").val();
					if(tmp_journalNodes != null) {
						for(var i = 0; i < tmp_journalNodes.length; i++){
							obj.journalNodes[tmp_journalNodes[i]] = tmp_journalNodes[i];
						}
					}
					if($('#automaticFailoverEnabled').hasClass('active')) {
						obj.automaticFailoverEnabled = true;
					}
					else {
						obj.automaticFailoverEnabled = false;
					}
				}
				else {
					obj.isHAEnabled = false;
				}
			}
			else {
				obj.isHadoop2 = false;
			}
			obj.isSubmit=true;
			com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName] = obj;
			com.impetus.ankush.hadoopCluster.toggleSecNNColumn();
			com.impetus.ankush.removeChild(2);
			com.impetus.ankush.hadoopCluster.goToAnchor('hadoopDetails_Jump');
		},
		
		// Revert Button click event for Hadoop Advanced settings during cluster creation
		revertHadoop:function() {
			com.impetus.ankush.removeChild(2);
			com.impetus.ankush.hadoopCluster.goToAnchor('hadoopDetails_Jump');
		},
		
		// Function to return the component POST JSON object for cluster deployment call
		deployEcoSystemComponents:function(componentName) {
			var componentData={};
			var advancedConf=null;
			if($("#hadoopEcoSystemCheckBox-"+componentName).attr("checked")){
				if(com.impetus.ankush.hadoopCluster.echoSystem[componentName].isSubmit) {
					advancedConf = com.impetus.ankush.hadoopCluster.deployAdvanceComponentsSubmit(componentName);
					componentData = {
							"componentVendor":com.impetus.ankush.hadoopCluster.echoSystem[componentName].vendor,
							"componentVersion":com.impetus.ankush.hadoopCluster.echoSystem[componentName].version,
							"certification":com.impetus.ankush.hadoopCluster.echoSystem[componentName].certified,
							"installationPath":com.impetus.ankush.hadoopCluster.echoSystem[componentName].installationPath,
							"localBinaryFile":com.impetus.ankush.hadoopCluster.echoSystem[componentName].localPath,
							"tarballUrl":com.impetus.ankush.hadoopCluster.echoSystem[componentName].downloadUrl,
							"advancedConf":advancedConf
					};
				}
				else {
					advancedConf=com.impetus.ankush.hadoopCluster.deployAdvanceComponentsDefault(componentName);
					componentData = {
							"componentVendor":$("#componentVendor-"+componentName).val(),
							"componentVersion":$("#componentVersion-"+componentName).val(),
							"certification":$("#componentStatus-"+componentName).text(),
							"installationPath":hadoopData.output.hadoop[componentName]['Defaults']['installationHomePath'].split('${user}').join($("#usernameHadoop").val()),
							"localBinaryFile":'',
							"tarballUrl":hadoopData.output.hadoop[componentName]['Vendors'][$("#componentVendor-"+componentName).val()][$("#componentVersion-"+componentName).val()]['url'],
							"advancedConf":advancedConf
					};
				}
				return componentData;   
			}
		},
		
		// Function to return the component applied advanced settings POST JSON object for cluster deployment call		
		deployAdvanceComponentsSubmit : function(componentName) {
			var advancedConf={};
			if(componentName=='Hive'){
				if($('#cloudEnv').attr('checked')) {
					hiveServer = "";
				}
				advancedConf={
						"hiveServer":hiveServer,   
						"javax.jdo.option.ConnectionPassword":connectionPassword,
						"javax.jdo.option.ConnectionURL":connectionURL,
						"javax.jdo.option.ConnectionDriverName":connectionDriverName,
						"javax.jdo.option.ConnectionUserName":connectionUserName
				};
			}
			if(componentName=='Zookeeper'){
				if($('#cloudEnv').attr('checked')) {
					zookeeperNodes = new Object();
				}
				advancedConf={
						"nodes":zookeeperNodes,
						"clientPort":clientPort,
						"dataDir":dataDir,
						"syncLimit":syncLimit,
						"initLimit":initLimit,
						"tickTime":tickTime

				};
			}
			if(componentName=='Hbase'){
				if($('#cloudEnv').attr('checked')) {
					hbaseMaster = "";
					regionServers = new Object();
				}
				advancedConf={
						"hbaseMaster":hbaseMaster,
						"regionServers":regionServers,
						"hbase.hstore.compactionThreshold":compactionThreshold,
						"hfile.block.cache.size":cacheSize,
						"hbase.hregion.max.filesize":filesize,
						"hbase.client.scanner.caching":caching,
						"zookeeper.session.timeout":timeout,
						"hbase.hregion.memstore.block.multiplier":multiplier,
						"hbase.hregion.majorcompaction":majorcompaction,
						"hbase.client.keyvalue.maxsize":maxsize,
						"hbase.hregion.memstore.flush.size":flushSize,
						"hbase.regionserver.handler.count":handlerCount

				};
			}
			return advancedConf;
		},
		
		// Function to return the component default advanced settings POST JSON object for cluster deployment call
		deployAdvanceComponentsDefault:function(componentName) {
			var advancedConf = {};
			com.impetus.ankush.hadoopCluster.getNamenodeIp();
			com.impetus.ankush.hadoopCluster.getDatanodesList();
			if(componentName == 'Hive'){
				if($('#cloudEnv').attr('checked')) {
					namenodeIp = "";
				}
				advancedConf={
						"hiveServer": namenodeIp,
						"javax.jdo.option.ConnectionPassword":hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionPassword"],
						"javax.jdo.option.ConnectionURL":hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionURL"],
						"javax.jdo.option.ConnectionDriverName":hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionDriverName"],
						"javax.jdo.option.ConnectionUserName":hadoopData.output.hadoop[componentName]['Defaults']["javax.jdo.option.ConnectionUserName"]

				};
			}
			if(componentName=='Zookeeper'){
				if($('#cloudEnv').attr('checked')) {
					zookeeperNodes = new Object();
				}
				else {
					for(var i = 0; i < datanodeList.length; i++){
						zookeeperNodes[datanodeList[i]] = datanodeList[i];
					}
				}

				advancedConf={
						"clientPort":hadoopData.output.hadoop[componentName]['Defaults']["clientPort"],
						"dataDir":hadoopData.output.hadoop[componentName]['Defaults']["dataDir"].split('${user}').join($("#usernameHadoop").val()),
						"syncLimit":hadoopData.output.hadoop[componentName]['Defaults']["syncLimit"],
						"initLimit":hadoopData.output.hadoop[componentName]['Defaults']["initLimit"],
						"tickTime":hadoopData.output.hadoop[componentName]['Defaults']["tickTime"],
						"nodes":zookeeperNodes
				};
			}
			if(componentName=='Hbase'){
				if($('#cloudEnv').attr('checked')) {
					namenodeIp = "";
					regionServers = new Object();
				}
				else {
					for(var i = 0; i < datanodeList.length; i++){
						regionServers[datanodeList[i]] = datanodeList[i];
					}
				}

				advancedConf={
						"hbaseMaster":namenodeIp,
						"regionServers":regionServers,
						"hbase.hstore.compactionThreshold":hadoopData.output.hadoop[componentName]['Defaults']["hbase.hstore.compactionThreshold"],
						"hfile.block.cache.size":hadoopData.output.hadoop[componentName]['Defaults']["hfile.block.cache.size"],
						"hbase.hregion.max.filesize":hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.max.filesize"],
						"hbase.client.scanner.caching":hadoopData.output.hadoop[componentName]['Defaults']["hbase.client.scanner.caching"],
						"zookeeper.session.timeout":hadoopData.output.hadoop[componentName]['Defaults']["zookeeper.session.timeout"],
						"hbase.hregion.memstore.block.multiplier":hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.memstore.block.multiplier"],
						"hbase.hregion.majorcompaction":hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.majorcompaction"],
						"hbase.client.keyvalue.maxsize":hadoopData.output.hadoop[componentName]['Defaults']["hbase.client.keyvalue.maxsize"],
						"hbase.hregion.memstore.flush.size":hadoopData.output.hadoop[componentName]['Defaults']["hbase.hregion.memstore.flush.size"],
						"hbase.regionserver.handler.count":hadoopData.output.hadoop[componentName]['Defaults']["hbase.regionserver.handler.count"]
				};
			}
			return advancedConf;
		},
		
		// Function to return the hadoop default / applied advanced settings POST JSON object for cluster deployment call
		deployHadoopValues:function(data) {
			var componentName = 'Hadoop';
			if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName] == null) {
				data.components = {};
				data.components.Hadoop = {};
				data.components.Hadoop.advancedConf = {};
				var hadoopDefault=hadoopData.output.hadoop[componentName]['Defaults'];
				
				data.components.Hadoop.advancedConf.replicationFactorDFS = $("#hadoopReplicationFactor").val();
				data.components.Hadoop.componentVendor = $("#hadoopVendor").val();
				data.components.Hadoop.componentVersion = $("#hadoopVersion").val();

				var currentVendor = $("#hadoopVendor").val();
				var currentVersion = $("#hadoopVersion").val();
				data.components.Hadoop.tarballUrl = hadoopData.output.hadoop[componentName]['Vendors'][currentVendor][currentVersion].url;
				data.components.Hadoop.localBinaryFile = "";

				data.components.Hadoop.installationPath = hadoopDefault['installationHomePath'].split('${user}').join($("#usernameHadoop").val());
				data.components.Hadoop.advancedConf.mapRedTmpDir = hadoopDefault.mapRedTmpDir.split('${user}').join($("#usernameHadoop").val());
				data.components.Hadoop.advancedConf.nameNodePath = hadoopDefault.nameNodePath.split('${user}').join($("#usernameHadoop").val());
				data.components.Hadoop.advancedConf.hadoopTmpDir = hadoopDefault.hadoopTmpDir.split('${user}').join($("#usernameHadoop").val());
				data.components.Hadoop.advancedConf.formatNameNode = true;
				data.components.Hadoop.advancedConf.startServices = true;
				data.components.Hadoop.advancedConf.includes3 = false;
				data.components.Hadoop.advancedConf.includes3n = false;
				data.components.Hadoop.advancedConf.s3AccessKey = "";
				data.components.Hadoop.advancedConf.s3SecretKey = "";
				data.components.Hadoop.advancedConf.s3nAccessKey = "";
				data.components.Hadoop.advancedConf.s3nSecretKey = "";
				data.components.Hadoop.advancedConf.dataDir = hadoopDefault.dataDir.split('${user}').join($("#usernameHadoop").val());
				data.components.Hadoop.advancedConf.isHAEnabled = false;
				if(com.impetus.ankush.hadoopCluster.isHadoop2Selected(data.components.Hadoop.componentVersion)) {
					data.components.Hadoop.advancedConf.isHAEnabled = true;
					data.components.Hadoop.advancedConf.nameserviceId = $('#hadoop_cluster_name').val();
					data.components.Hadoop.advancedConf.nameNodeId1 = hadoopDefault.nameNodeId1;
					data.components.Hadoop.advancedConf.nameNodeId2 = hadoopDefault.nameNodeId2;
					data.components.Hadoop.advancedConf.journalNodeEditsDir = hadoopDefault.journalNodeEditsDir.split('${user}').join($("#usernameHadoop").val());
					data.components.Hadoop.advancedConf.automaticFailoverEnabled = true;
					data.components.Hadoop.advancedConf.activeNamenode = "";
					data.components.Hadoop.advancedConf.standByNamenode = "";
					data.components.Hadoop.advancedConf.journalNodes = new Object();
					data.components.Hadoop.advancedConf.startJobHistoryServer = true;
					data.components.Hadoop.advancedConf.isWebProxyEnabled = true;
					data.components.Hadoop.advancedConf.webAppProxyPort = hadoopDefault.webAppProxyPort;
					
					if(nodeTableLength != null) {
						var isStandByNNSelected = false;
						for(var i = 0 ; i < nodeTableLength ; i++){
							if($("#hadoopCheckBox-"+i).attr("checked")){
								var ip = nodeHadoopJSON[i][0];
								if($("#namNodeHadoop-"+i).is(":checked")) {
									data.components.Hadoop.advancedConf.activeNamenode = ip;
									data.components.Hadoop.advancedConf.resourceManagerNode = ip;
									data.components.Hadoop.advancedConf.jobHistoryServer = ip;
									data.components.Hadoop.advancedConf.webAppProxyNode = ip;
									data.components.Hadoop.advancedConf.journalNodes[ip] = ip;
								} else if(!isStandByNNSelected){
								 	isStandByNNSelected = true;
								 	data.components.Hadoop.advancedConf.standByNamenode = ip;
								}
							}
						}
					}
				}
			}
			else if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].isSubmit){
				data.components = {};
				data.components.Hadoop = {};
				data.components.Hadoop.componentVendor = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].vendor;
				data.components.Hadoop.componentVersion = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].version;
				data.components.Hadoop.tarballUrl = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].downloadUrl;
				data.components.Hadoop.localBinaryFile = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].localPath;
				data.components.Hadoop.installationPath =     com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].installationPath;
				data.components.Hadoop.advancedConf = {};
				data.components.Hadoop.advancedConf.mapRedTmpDir = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].mapRedTmpDir;
				data.components.Hadoop.advancedConf.nameNodePath = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].nameNodePath;
				data.components.Hadoop.advancedConf.hadoopTmpDir = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].hadoopTmpDir;
				data.components.Hadoop.advancedConf.startServices = true;
				data.components.Hadoop.advancedConf.includes3 = false;
				data.components.Hadoop.advancedConf.includes3n = false;
				data.components.Hadoop.advancedConf.s3AccessKey = "";
				data.components.Hadoop.advancedConf.s3SecretKey = "";
				data.components.Hadoop.advancedConf.s3nAccessKey = "";
				data.components.Hadoop.advancedConf.s3nSecretKey = "";

				if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].includeS3SupportHadoop){
					data.components.Hadoop.advancedConf.includes3 = true;
					data.components.Hadoop.advancedConf.s3AccessKey = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].acessKeyS3;
					data.components.Hadoop.advancedConf.s3SecretKey = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].secretKeys3;
				}

				if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].includeS3nSupportHadoop){
					data.components.Hadoop.advancedConf.includes3n = true;
					data.components.Hadoop.advancedConf.s3nAccessKey = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].acessKeyS3n;
					data.components.Hadoop.advancedConf.s3nSecretKey = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].secretKeys3n;
				}

				data.components.Hadoop.advancedConf.dataDir = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].dataDir;
				data.components.Hadoop.advancedConf.replicationFactorDFS = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName].replicationFactorDFS;
				data.components.Hadoop.advancedConf.formatNameNode = true;
				
				var objAdvancedData = com.impetus.ankush.hadoopCluster.hadoopAdvanceData[componentName]; 
				data.components.Hadoop.advancedConf.isHAEnabled = false;
				if(objAdvancedData.isHadoop2) {
					data.components.Hadoop.advancedConf.resourceManagerNode = objAdvancedData.resourceManagerNode;
					if(objAdvancedData.startJobHistoryServer) {
						data.components.Hadoop.advancedConf.startJobHistoryServer = true;
						data.components.Hadoop.advancedConf.jobHistoryServer = objAdvancedData.jobHistoryServer;	
					}
					else {
						data.components.Hadoop.advancedConf.startJobHistoryServer = false;
						data.components.Hadoop.advancedConf.jobHistoryServer = null;	
					}
					
					if(objAdvancedData.isWebProxyEnabled) {
						data.components.Hadoop.advancedConf.isWebProxyEnabled = true;
						data.components.Hadoop.advancedConf.webAppProxyNode = objAdvancedData.webAppProxyNode;
						data.components.Hadoop.advancedConf.webAppProxyPort = objAdvancedData.webAppProxyPort;
					}
					else {
						data.components.Hadoop.advancedConf.isWebProxyEnabled = false;
						data.components.Hadoop.advancedConf.webAppProxyNode = null;
						data.components.Hadoop.advancedConf.webAppProxyPort = null;	
					}
					
					if(objAdvancedData.isHAEnabled) {
						data.components.Hadoop.advancedConf.isHAEnabled = true;
						data.components.Hadoop.advancedConf.nameserviceId = objAdvancedData.nameserviceId;
						data.components.Hadoop.advancedConf.nameNodeId1 = objAdvancedData.nameNodeId1;
						data.components.Hadoop.advancedConf.nameNodeId2 = objAdvancedData.nameNodeId2;
						data.components.Hadoop.advancedConf.journalNodeEditsDir = objAdvancedData.journalNodeEditsDir;
						data.components.Hadoop.advancedConf.automaticFailoverEnabled = objAdvancedData.automaticFailoverEnabled;
						data.components.Hadoop.advancedConf.standByNamenode = objAdvancedData.standByNamenode;
						data.components.Hadoop.advancedConf.journalNodes = objAdvancedData.journalNodes;
						data.components.Hadoop.advancedConf.activeNamenode = "";
						if(nodeTableLength != null) {
							for(var i = 0 ; i < nodeTableLength ; i++){
								if($("#hadoopCheckBox-"+i).attr("checked")){
									var ip = nodeHadoopJSON[i][0];
									if($("#namNodeHadoop-"+i).is(":checked")) {
										data.components.Hadoop.advancedConf.activeNamenode = ip;
										break;
									}
								}
							}
						}	
					}
				}
			}
			return data;   
		},
		
		// Function to set NameNode Ip selected during cluster creation
		getNamenodeIp : function() {
			if(nodeTableLength != null) {
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($("#hadoopCheckBox-"+i).attr("checked")){
						if($("#namNodeHadoop-"+i).is(":checked")) {
							namenodeIp = nodeHadoopJSON[i][0];
							break;
						}
					}
				}
			}
		},
		
		// Function to set list of Data Nodes selected during cluster creation
		getDatanodesList : function() {
			datanodeList = new Array();
			if(nodeTableLength != null) {
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($("#hadoopCheckBox-"+i).attr("checked")){
						if($("#dataNode-"+i).is(":checked")) {
							datanodeList.push(nodeHadoopJSON[i][0]);
						}
					}
				}
			}
		},
		
		// Function to filter node table data based on the filter selection
		toggleDatatable : function(status){
			$('.notSelected').show();
			$('.notSelected').removeClass('notSelected');
			$('.selected').removeClass('selected');
			if(!$('#nodeRetrieveHadoop').attr('disabled'))
				$('#nodeCheckHead').removeAttr('disabled');
			nodeTableLength = addNodeHadoopTable.fnGetData().length;
			if(status == 'All'){
				$('.notSelected').show();
				$('.notSelected').removeClass('notSelected');
				$('.selected').removeClass('selected');
			}
			else if(status == 'Selected') {
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($('#hadoopCheckBox-'+i).attr('checked')){
						$('#rowId_'+i).addClass('selected');
					}
					else
						$('#rowId_'+i).addClass('notSelected');
				}
				$('#nodeCheckHead').attr('disabled', true);
				$('.notSelected').hide();
			}
			else if(status == 'Available'){
				for(var i = 0 ; i < nodeTableLength ; i++){
					if(!$('#rowId_'+i).hasClass('error-row')){
						$('#rowId_'+i).addClass('selected');
					}
					else
						$('#rowId_'+i).addClass('notSelected');
				}
				$('.notSelected').hide();
			}
			else if(status == 'Error'){
				for(var i = 0 ; i < nodeTableLength ; i++){
					if($('#rowId_'+i).hasClass('error-row')){
						$('#rowId_'+i).addClass('selected');
					}
					else
						$('#rowId_'+i).addClass('notSelected');
				}
				$('#nodeCheckHead').attr('disabled', true);
				$('#nodeCheckHead').removeAttr('checked');
				$('.notSelected').hide();
			}
			if($('#nodeRetrieveHadoop').attr('disabled'))
				$('#nodeCheckHead').attr('checked', true);
		},

		// Function to open Cluster Setup Details Page during cluster deployment Progress
		loadSetupDetailHadoop : function(i) {
			setUpDetailNodeTable = null;
			$('#content-panel').sectionSlider(
					'addChildPanel',
					{
						current : 'login-panel',
						url : baseUrl + '/hadoop-cluster/hSetupDetails',
						method : 'get',
						params : {

						},
						title : 'Setup Details',
						callback : function() {
							com.impetus.ankush.hadoopCluster.setupDetailHadoop(currentClusterId);
						}
					});
		},
		
		// Function to initialise Node Table on Cluster Setup Details Page during cluster deployment Progress
		initSetupDetailNodeTable : function() {
			setUpDetailNodeTable = $("#setUpDetailNodeTable").dataTable({
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
			});
			$("#setUpDetailNodeTable_filter").css({
				'text-align' : 'right'
			});
			$("#setUpDetailNodeTable_filter")
			.prepend('<div style="float:left;margin-top:15px;" id="setupHadoopDatatable"></div>');
			$("#setupHadoopDatatable").append(
			"<h4 class='section-heading'>Node List</h4>");   
		},
		setupDetailValuePopulate : function(currentClusterId){
	    	com.impetus.ankush.common.pageStyleChange();
	    	var deploymentDetailUrl = baseUrl + '/monitor/' + currentClusterId
			+ '/details';
			$.ajax({
				'type' : 'GET',
				'url' : deploymentDetailUrl,
				'contentType' : 'application/json',
				'dataType' : 'json',
				'success' : function(result) {
					hadoopDeployedData_Error = result;
					if(hadoopDeployedData_Error == null){
						return;
					}
					com.impetus.ankush.hadoopCluster.fillData_ClusterError();
				},
				'error' : function() {

				}
			});
			
			
		},
		
		// Function to populate Cluster Setup Details during cluster deployment Progress
		setupDetailHadoop : function(iClusterId) {
			com.impetus.ankush.hadoopCluster.initSetupDetailNodeTable();
			com.impetus.ankush.hadoopCluster.fillSetupDetailNodeTable(iClusterId);

			$('#lbl_HSD_ClusterName').html('').text(deploymentData.output.clusterName);
			$('#lbl_HSD_ClusterDesc').html('').text(deploymentData.output.description);
			$('#lbl_HSD_ClusterEnv').html('').text(deploymentData.output.environment);

			if(deploymentData.output.environment == 'Cloud') {
				$('#clouddetails').show();
				$('#cloudEnv-action').show();

				$('#lbl_HSD_CA_ServiceProvider').html('').text(deploymentData.output.cloudConf.providerName);
				$('#lbl_HSD_CA_Username').html('').text(deploymentData.output.cloudConf.username);
				$('#lbl_HSD_CA_Password').html('').text(deploymentData.output.cloudConf.password);
				$('#lbl_HSD_CA_ClusterSize').html('').text(deploymentData.output.cloudConf.clusterSize);
				$('#lbl_HSD_CA_KeyPairs').html('').text(deploymentData.output.cloudConf.keyPairs);
				$('#lbl_HSD_CA_SecurityGroup').html('').text(deploymentData.output.cloudConf.securityGroup);
				$('#lbl_HSD_CA_Region').html('').text(deploymentData.output.cloudConf.region);
				$('#lbl_HSD_CA_Zone').html('').text(deploymentData.output.cloudConf.zone);
				$('#lbl_HSD_CA_InstanceType').html('').text(deploymentData.output.cloudConf.instanceType);
				$('#lbl_HSD_CA_MachineImage').html('').text(deploymentData.output.cloudConf.machineImage);

				if(deploymentData.output.cloudConf.autoTerminate) {
					$("#autoTerminateYes").attr("checked", true);
					$("#autoTerminateYes-action").css('display', 'block');
					$('#lbl_HSD_CA_TimeoutInterval').html('').text(deploymentData.output.cloudConf.timeOutInterval);
				}
				else {
					$("#autoTerminateYes-action").css('display', 'none');
					$('#lbl_HSD_CA_TimeoutInterval').html('').text("--");
					$("#autoTerminateYes").attr("checked", false);
					$("#autoTerminateNo").attr("checked", true);
				}
			}

			if(deploymentData.output.javaConf.install) {
				$('#chkbx_HSD_InstallJava').attr("checked", "checked");
				$('#lbl_HSD_JavaBundle').html('').text(deploymentData.output.javaConf.javaBundle);
				$('#lbl_HSD_JavaHomePath').html('').text('N/A');
			}
			else {
				$('#lbl_HSD_JavaBundle').html('').text('N/A');
				$('#lbl_HSD_JavaHomePath').html('').text(deploymentData.output.javaConf.javaHomePath);
			}

			$('#lbl_HSD_UserName').html('').text(deploymentData.output.authConf.username);
			if(deploymentData.output.authConf.type == "password")
				$('#radio_HSD_Password').attr("checked", "checked");
			else
				$('#radio_HSD_SharedKey').attr("checked", "checked");
			$('#txtBx_HSD_Password').html('').val(deploymentData.output.authConf.password);   

			com.impetus.ankush.hadoopCluster.deploymentPrg_loadHadoopDetails();
		},
		
		// Function to populate Node list / progress table during cluster deployment Progress
		fillSetupDetailNodeTable : function(iClusterId){

			if(document.getElementById('setUpDetailNodeTable') == null) {
				autoRefreshInterval_NodeList_SetupDetail = window.clearInterval(autoRefreshInterval_NodeList_SetupDetail);
				return;
			}

			currentClusterId = iClusterId;

			if(deploymentData == null)
			{
				var deploymentDetailUrl = baseUrl + '/monitor/' + currentClusterId
				+ '/details';
				$.ajax({
					'type' : 'GET',
					'url' : deploymentDetailUrl,
					'contentType' : 'application/json',
					'dataType' : 'json',
					'success' : function(result) {
						deploymentData = result;
					},
					'error' : function() {

					}
				});
			}
			if(deploymentData == null){
				return;
			}
			var tableData = [];

			for ( var i = 0; i < deploymentData.output.nodes.length; i++) {
				var rowData = [];
				var col_1 = deploymentData.output.nodes[i].publicIp;
				var col_2 = '<a class=""><label id="nodeIpType' + i + '">'
				+ com.impetus.ankush.hadoopCluster.getNodeTypeValue(deploymentData.output.nodes[i])
				+ '</label></a>';
				var col_3 = '<a class=""><label id="nodeIpStatus' + i
				+ '">'
				+ deploymentData.output.nodes[i].message
				+ '</label></a>';
				var col_4 = '<a href="#" onclick="com.impetus.ankush.hadoopCluster.loadNodeSetupDetail(\''
					+ i
					+ '\');" ><img id="navigationImg'
					+ i
					+ '" src="'
					+ baseUrl
					+ '/public/images/icon-chevron-right.png"/></a>';

				rowData.push(col_1);
				rowData.push(col_2);
				rowData.push(col_3);
				rowData.push(col_4);
				tableData.push(rowData);
			}
			if(setUpDetailNodeTable != null) {
				setUpDetailNodeTable.fnClearTable();
			}
			setUpDetailNodeTable.fnAddData(tableData);
			if (!deployFlagHadoop) {
				if(!IsAutoRefreshON_NodeList_SetupDetail){
					IsAutoRefreshON_NodeList_SetupDetail = true;
					com.impetus.ankush.hadoopCluster.initProgressSetupDetailNodeTable(currentClusterId);
				}
			}
			else
				IsAutoRefreshON_NodeList_SetupDetail = false;
		},
		
		// Function to open Node Setup Details page during cluster deployment Progress		
		loadNodeSetupDetail : function(nodeRowId) {
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster/hNodeSetupDetail',
				method : 'get',
				params : {

				},
				title : 'Node Setup Details',
				callback : function() {
					com.impetus.ankush.hadoopCluster.deploymentPrg_loadNodeSetupDetail(nodeRowId);
				},
				callbackData : {

				}
			});
		},
		
		// Function to populate Node Setup Details (Node details & Progress Logs) during cluster deployment Progress		
		deploymentPrg_loadNodeSetupDetail : function(nodeRowId) {

			if(document.getElementById('NSD_nodeDetailHead') == null) {
				autoRefreshInterval_NodeLogs = window.clearInterval(autoRefreshInterval_NodeLogs);
				return;
			}
			$('#NSD_nodeDetailHead').html('').text(deploymentData.output.nodes[nodeRowId].publicIp);
			$('#NSD_nodePrivateIP').html('').text(deploymentData.output.nodes[nodeRowId].privateIp);
			$('#NSD_nodeStatus').html('').text(deploymentData.output.nodes[nodeRowId].message);
			$('#NSD_nodeType').html('').text(com.impetus.ankush.hadoopCluster.getNodeTypeValue(deploymentData.output.nodes[nodeRowId]));
			$('#NSD_nodeOS').html('').text(deploymentData.output.nodes[nodeRowId].os);
			com.impetus.ankush.hadoopCluster.deploymentPrg_loadNodeSetupLogs(nodeRowId);
		},
		
		// Function to populate Node Progress Logs on Node Details page during cluster deployment Progress
		deploymentPrg_loadNodeSetupLogs : function(nodeRowId) {
			$('#NSD_nodeDeploymentProgressDiv').empty();
			for ( var i = deploymentData.output.nodes[nodeRowId].logs.length-1; i >= 0 ; i--) {
				if(deploymentData.output.nodes[nodeRowId].logs[i].type == 'error'){
					$('#NSD_nodeDeploymentProgressDiv').append('<div style="color:red">' +
							deploymentData.output.nodes[nodeRowId].logs[i].longMessage + '</div>');
				}else{
					$('#NSD_nodeDeploymentProgressDiv').append('<div>' +
							deploymentData.output.nodes[nodeRowId].logs[i].longMessage + '</div>');
				}
			}
			if (deploymentData.output.state == 'deployed' || deploymentData.output.state == 'error') {
				deployFlagHadoop = true;
				IsAutoRefreshON = false;
				hClusterDeployInterval = window.clearInterval(hClusterDeployInterval);
				return;
			}
			if (!deployFlagHadoop) {
				if(!IsAutoRefreshON_NodeLogs){
					IsAutoRefreshON_NodeLogs = true;
					com.impetus.ankush.hadoopCluster.initProgressNodeSetupLogs(nodeRowId);
				}
			}
			else {
				hClusterDeployInterval = window.clearInterval(hClusterDeployInterval);
				IsAutoRefreshON_NodeLogs = false;
				return;
			}
		},
		
		// Function to start auto-refresh call for Node list / progress table during cluster deployment Progress		
		initProgressSetupDetailNodeTable : function(clusterId) {
			var functionName = "com.impetus.ankush.hadoopCluster.fillSetupDetailNodeTable(\"" + currentClusterId + "\");";
			autoRefreshInterval_NodeList_SetupDetail = setInterval(function() {
				eval(functionName);
			}, refreshTimeInterval);
		},
		
		// Function to start auto-refresh call for Node list / progress table on Setup Details during cluster deployment Progress		
		initProgressNodeSetupLogs : function(nodeRowId) {
			var functionName = "com.impetus.ankush.hadoopCluster.deploymentPrg_loadNodeSetupDetail(\"" + nodeRowId + "\");";
			autoRefreshInterval_NodeLogs = setInterval(function() {
				eval(functionName);
			}, refreshTimeInterval);
		},
		
		// Function to open Node List page during cluster deployment Progress
		loadNodeStatusHadoop : function(iClusterId) {
			nodeIpStatusTable = null;
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hadoop-cluster/hNodeStatus',
				method : 'get',
				params : {

				},
				title : 'Node Status',
				callback : function() {
					com.impetus.ankush.hadoopCluster.nodeStatus(iClusterId);
				},
				callbackData : {

				}
			});
		},
		
		// Function to populate Node List table during cluster deployment Progress		
		nodeStatus : function(clusterId) {
			if(document.getElementById('nodeIpStatusTable') == null) {
				autoRefreshInterval_NodeList = window.clearInterval(autoRefreshInterval_NodeList);
				return;
			}

			currentClusterId = clusterId;

			if(deploymentData == null)
			{
				var deploymentDetailUrl = baseUrl + '/monitor/' + currentClusterId
				+ '/details';
				$.ajax({
					'type' : 'GET',
					'url' : deploymentDetailUrl,
					'contentType' : 'application/json',
					'dataType' : 'json',
					'success' : function(result) {
						deploymentData = result;
					},
					'error' : function() {
						return;
					}
				});
			}
			if(deploymentData == null){
				return;
			}
			var tableData = [];
			for ( var i = 0; i < deploymentData.output.nodes.length; i++) {
				var rowData = [];
				var col1 = deploymentData.output.nodes[i].publicIp;
				var col2 = '<a class=""><label id="nodeIpType' + i + '">'
				+ com.impetus.ankush.hadoopCluster.getNodeTypeValue(deploymentData.output.nodes[i])
				+ '</label></a>';
				var col3 = '<a class=""><label id="nodeIpStatus' + i
				+ '">'
				+ deploymentData.output.nodes[i].message
				+ '</label></a>';
				var col4 = '<a href="#" onclick="com.impetus.ankush.hadoopCluster.loadNodeSetupDetail(\''
					+ i
					+ '\');" ><img id="navigationImg'
					+ i
					+ '" src="'
					+ baseUrl
					+ '/public/images/icon-chevron-right.png"/></a>';
				rowData.push(col1);
				rowData.push(col2);
				rowData.push(col3);
				rowData.push(col4);
				tableData.push(rowData);
			}
			if (nodeIpStatusTable != null) {
				nodeIpStatusTable.fnClearTable();
			} else {
				nodeIpStatusTable = $("#nodeIpStatusTable").dataTable({
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
				});
				$("#nodeIpStatusTable_filter").css({
					'text-align' : 'right'
				});
				$("#nodeIpStatusTable_filter")
				.prepend(
				'<div style="float:left;margin-top:15px;" id="nodeIpHadoopDatatable"></div>');
				$("#nodeIpHadoopDatatable").append(
				"<h4 class='section-heading'>Node List</h4>");
			}
			nodeIpStatusTable.fnAddData(tableData);

			if (!deployFlagHadoop) {
				if(!IsAutoRefreshON_NodeList){
					IsAutoRefreshON_NodeList = true;
					com.impetus.ankush.hadoopCluster.initProgressNodeStatusNodeTable(clusterId);
				}
			}
			else
				IsAutoRefreshON_NodeList = false;
		},
		
		// Function to return Node type (NameNode / DataNode / SecondaryNameNode) during cluster deployment Progress
		getNodeTypeValue : function(nodeObject){
			var nodeType = "";
			if(nodeObject.nameNode)
				nodeType = "NameNode";
			if(nodeObject.dataNode){
				if(nodeType.length == 0)
					nodeType = "DataNode";
				else
					nodeType += " / DataNode";
			}
			if(nodeObject.secondaryNameNode){
				if(nodeType.length == 0)
					nodeType = "SecondaryNameNode";
				else
					nodeType += " / SecondaryNameNode";
			}
			return nodeType;
		},
		
		// Function to start auto-refresh call for Node list / progress table on Node Details Page during cluster deployment Progress
		initProgressNodeStatusNodeTable : function(currentClusterId) {
			var functionName = "com.impetus.ankush.hadoopCluster.nodeStatus(\"" + currentClusterId + "\");";
			autoRefreshInterval_NodeList = setInterval(function() {
				eval(functionName);
			}, refreshTimeInterval);   
		},
		
		// Function to open Dashboard on Deploy / Delete Cluster button click events
		loadDashboardHadoop : function(i) {
			com.impetus.ankush.removeChild(1);
		},
		
		// Function to show Delete Cluster Dialog box 
		initDeleteClusterDialog : function() {
			$("#deleteClusterDialogHadoop").modal('show');
			$('.ui-dialog-titlebar').hide();
		},

		// Function to show Delete Cluster Dialog box		
		deleteDialogHadoop : function() {
			com.impetus.ankush.hadoopCluster.initDeleteClusterDialog();
		},
		
		// Function to hide Delete Cluster Dialog box
		closeDeleteDialog : function(){
			$('#deleteClusterDialogHadoop').modal('hide');
		},
		
		// Function to send cluster Delete request to the Ankush Server 
		deleteClusterHadoop : function() {

			$('#deleteClusterDialogHadoop').modal('hide');
			$('#deleteClusterDialogHadoop').remove();
			if (currentClusterId == null) {
				com.impetus.ankush.hadoopCluster.loadDashboardHadoop();
				return;
			} else {
				var deleteUrl = baseUrl + '/cluster/remove/'+ currentClusterId;
				$.ajax({
					'type' : 'DELETE',
					'url' : deleteUrl,
					'contentType' : 'application/json',
					'dataType' : 'json',
					'success' : function(result) {
						if (result.output.status){
							com.impetus.ankush.hadoopCluster.loadDashboardHadoop();

						}
						else
							alert(result.output.error[0]);           
					},
					error : function(data) {

					}
				});
			}
		},
		
		// Function to set the current cluster Id variable
		setCurrentClusterId : function(clusterId) {
			currentClusterId = clusterId;
		},
		
		// Function to remove fake path entry for Chrome & Safari during file upload functionality 
		removeFakePathFromPath : function(fieldId) {
			var value = $('#' + fieldId).val().replace('C:\\fakepath\\', '');
			$('#' + fieldId).val(value);
		},

		// Function to upload IP Address pattern file 
		hadoopIPAddressFileUpload : function() {
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_IPAddressFile').click();
			$('#fileBrowse_IPAddressFile').change(
					function() {
						$('#filePath_IPAddressFile').val(document.forms['uploadframeIPAddressFile_Form'].elements['fileBrowse_IPAddressFile'].value);
						com.impetus.ankush.hadoopCluster.uploadFile(uploadUrl,"fileBrowse_IPAddressFile", "fileIPAddress");
						com.impetus.ankush.validation.removeFakePathFromPath('filePath_IPAddressFile');
					});
		},
		
		// Function to upload IP Address & Rack mapping file		
		hadoopRackFileUpload : function() {
			fileRack_ServerPath = null;
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowse_RackFile').click();
			$('#fileBrowse_RackFile').change(
					function() {
						$('#filePath_RackFile').val(document.forms['uploadframeRackFile_Form'].elements['fileBrowse_RackFile'].value);
						com.impetus.ankush.hadoopCluster.uploadFile(uploadUrl,"fileBrowse_RackFile", "fileRack");
						com.impetus.ankush.validation.removeFakePathFromPath('filePath_RackFile');
					});
		},
		
		// Function to upload Shared key file for node authentication
		hadoopSharedKeyUpload : function() {
			var uploadUrl = baseUrl + '/uploadFile';
			$('#fileBrowseDb').click();
			$('#fileBrowseDb').change(
					function() {
						$('#filePathDb').val(document.forms['uploadframesharekeyDbPackage'].elements['fileBrowseDb'].value);
						com.impetus.ankush.hadoopCluster.uploadFile(uploadUrl,"fileBrowseDb", 'sharedKey');
						com.impetus.ankush.validation.removeFakePathFromPath('filePathDb');
					});
		},

		// Function to upload a file to the Ankush Server
		uploadFile : function(uploadUrl, fileId, uploadType, callback, context) {
			jsonFileUploadString = null;
			$.ajaxFileUpload({
				url : uploadUrl,
				secureuri : false,
				fileElementId : fileId,
				dataType : 'text',
				accept: "application/png",
				success : function(result) {
					if (callback)
						callback(result, context);
					
					var htmlObject = $(result);
					var resultJSON = new Object();
					eval("resultJSON  = " + htmlObject.text());
					if(uploadType == "sharedKey") {
						sharedKey_ServerPath = resultJSON.output;
					}
					else if(uploadType == "fileIPAddress") {
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
		
		isHadoop2Selected : function(currentVersion) {
			if(currentVersion.indexOf("2.") != 0) {
				return false;
			}
			else {
				return true;
			}
		},
		
		toggleSecNNColumn : function() {
			
			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected($('#hadoopVersion').val())) {
				if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'] != null) {
					if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'].isHadoop2) {
						if(com.impetus.ankush.hadoopCluster.hadoopAdvanceData['Hadoop'].isHAEnabled) {
							if(nodeTableLength != null){
								for(var i = 0 ; i < nodeTableLength ; i++){
									$('#secNameNode-'+i).removeAttr('checked');   
									$('#secNameNode-'+i).attr('disabled', 'disabled');
								}
							}						
						} else {
							if(nodeTableLength != null){
								for(var i = 0 ; i < nodeTableLength ; i++){
									if(!$('#hadoopCheckBox-'+i).attr("disabled")) {
										$('#secNameNode-'+i).removeAttr('disabled');	
									}
								}
							}		
						}
					}
				}
				else {
					if(nodeTableLength != null){
						for(var i = 0 ; i < nodeTableLength ; i++){
							$('#secNameNode-'+i).removeAttr('checked');   
							$('#secNameNode-'+i).attr('disabled', 'disabled');
						}
					}
				}
			}
			else {
				if(nodeTableLength != null){
					
					for(var i = 0 ; i < nodeTableLength ; i++){
						if(!$('#hadoopCheckBox-'+i).attr("disabled")) {
							$('#secNameNode-'+i).removeAttr('disabled');	
						}
					}
				}
			}
			
			
		},
		
		// Functions Related to Hadoop 2
		showHideHadoop2Settings : function(currentVersion) {
			if(com.impetus.ankush.hadoopCluster.isHadoop2Selected(currentVersion)) {
				$('#hadoop2Settings').css('display', 'block');
			}
			else {
				$('#hadoop2Settings').css('display', 'none');
			}
		},
		hadoopHADivShow : function() {
			$('#hadoopHADiv').css('display', 'block');
		},
		hadoopHADivHide : function() {
			$('#hadoopHADiv').css('display', 'none');
		},
		webAppProxyDivShow : function() {
			$('#webAppProxyDiv').css('display', 'block');
		},
		webAppProxyDivHide : function() {
			$('#webAppProxyDiv').css('display', 'none');
		}
};
