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

var oNodeTable = null;
//var dataCenterMapTable=null;
var defaultValues = null;
var nodeTableLength = null;
var nodeStatus = null;
var errorDetail = null;
var getNodeFlag = false;
var disableNodeCount = 0;
var setUpDetailNodeTable = null;
var nodeIpStatusTable = null;
var deploymentData=null;
var deploymentLogs = null;
var nodeStatusArray = new Array();
var clusterDeployInterval = null;
var inProgressStatus = true;
var refreshTimeInterval = 5000;
var setupDeployInterval = null;
var nodeStatusDeployInterval = null;
var deployFlag = false;
var lastID;
var lastData = null;
var currentClusterId = null;
var nodeDeployLogData = null;
var deployedCluster = false;
var errorNodeDetail=null;
var uploadFilePath=null;
var uploadFileFlag=false;
var errorCount=0;
var ipAddress;
var lastLogDeploy;
var uploadPathSharedKey=null;
var setupDetailData=null;
var repoPath=null;
var uploadRackFilePath=null;
var inspectNodeData = {};
//var dataCenterObj={};
var deploymentStatusOnHead={
        'deploying' : 'Cluster deployment in progress',
        'deployed' : 'Cluster deployment successful',
        'error' :  'Error in cluster deployment'
};

com.impetus.ankush.oClusterSetup = {

/*Function for loading node drill down page from node table*/ 
  /*  loadNodeDetailOracle : function(i) {
        $('#content-panel').sectionSlider('addChildPanel', {
            current : 'login-panel',
            url : baseUrl + '/oracle-cluster/oNodeDetail',
            method : 'get',
            params : {},
            title : 'Node Detail',
            tooltipTitle : 'Cluster Setup Detail',
            callback : function(data) {
                com.impetus.ankush.oClusterSetup.nodeDetail(data.i);
            },
            callbackData : {
                i : i
            }
        });
    },*/
    
/*Function for loading dashboard by removing child page*/
	loadDashboard : function(i) {
		com.impetus.ankush.dashboard.createTile();
		com.impetus.ankush.removeChild(1);    
	},
    
/*Function for initializing tooltip*/
    tooltip : function() {
        $("#storeName").tooltip();
        $("#dataCenter").tooltip();
        $("#topology").tooltip();
        $("#repFactor").tooltip();
        $("#partitions").tooltip();
        $("#registryPort").tooltip();
        $("#haPort1").tooltip();
        $("#haPort2").tooltip();
        $("#baseDir").tooltip();
        $("#installationPath").tooltip();
        $("#dataPath").tooltip();
        $("#ntpServer").tooltip();
        $("#userName").tooltip();
        $("#password").tooltip();
        $("#ipRange").tooltip();
        $("#filePath").tooltip();
        $("#selectDBPackage").tooltip();
    },
  
/*Function for uploading oracke DB package file and populate it to dropdown*/
	oracleDbPackageUpload : function() {  
		var uploadUrl = baseUrl + '/uploadFile?category=bundle';
		$('#fileBrowseDb').click();
		$('#fileBrowseDb').change(
		function(){
			var obj={
				'uploadBtn': 'filePathDb',
				'otherBtn' : 'clusterDeploy'
			};
			/*if(!$('#fileBrowseDb').val().match('kv-ee-*')){
				alert('')
				return;
			}*/
			com.impetus.ankush.uploadFileNew(uploadUrl,"fileBrowseDb",obj,function(abcd){
			com.impetus.ankush.oClusterSetup.populateOracleDBPackage();
			});
		});
	},
  
/*Function for opening delete cluster dialog box*/
    deleteDialog : function() {
        $("#deleteClusterDialog").appendTo('body').modal('show');
        $('.ui-dialog-titlebar').hide();
        $('.ui-dialog :button').blur();
    },
    
/*Function for deleting cluster*/
	deleteCluster : function() {
		$('#deleteClusterDialog').modal('hide');
		if (deployedCluster == false) {
			deployedCluster=false;
			com.impetus.ankush.oClusterSetup.loadDashboard();
		}else{
			var deleteUrl = baseUrl + '/cluster/remove/'+ currentClusterId;
			$.ajax({
				'type' : 'DELETE',
				'url' : deleteUrl,
				'contentType' : 'application/json',
				'dataType' : 'json',
				'success' : function(result) {
					if (result.output.status == true) {
					}
					if (result.output.status == false) {
						alert(result.output.error[0]);
					}
					deployedCluster=false;
					com.impetus.ankush.oClusterSetup.loadDashboard();
				},
				error : function(data) {
				}
			});
		}
	},

/*Function for populating default values on cluster creation page*/
	getDefaultValue : function() {
		var getDefaultUrl = baseUrl + '/app/metadata/Oracle NoSQL Database';
		$.ajax({
			'type' : 'GET',
			'url' : getDefaultUrl,
			'contentType' : 'application/json',
			'dataType' : 'json',
			'async' : true,
			'success' : function(result) {
				defaultValues = result.output['Oracle NoSQL Database'];
				if($('#clusterDeploy').val() !=''){
					return;
				}
				if (defaultValues != '') {
					$('#repFactor').html('').val(defaultValues.replicationFactor);
					$('#registryPort').html('').val(defaultValues.registryPort);
					$('#baseDir').html('').val(defaultValues.baseDir);
					$('#installationPath').html('').val(defaultValues.installationDir);
					$('#dataPath').html('').val(defaultValues.dataDir);
					$('#haPort1').html('').val(defaultValues.haPortRangeStart);
					$('#haPort2').html('').val(defaultValues.haPortRangeEnd);
				}
			},
			error : function(data) {
			},
			cache : false
		});
	},
  
/*Function for changing installation & data path according to the value of basePath*/
    changePath : function(){
    var homePath=$.trim($('#baseDir').val());
    $('#installationPath').html('').val(homePath+'/kv');
    $('#dataPath').html('').val(homePath+'/kv/kvroot');
    },
    
/*Function for populating oracle DB package in selectbox*/
	populateOracleDBPackage : function() {
		$('#selectDBPackage').html('');
		var url = baseUrl + '/list/files';
		var data = {
			"category" : "repo",
			"pattern" : "kv-ee-2.*"
		};
		$.ajax({
			'type' : 'POST',
			'url' : url,
			'contentType' : 'application/json',
			'dataType' : 'json',
			'async' : true,
			'data' : JSON.stringify(data),
			'success' : function(result) {
				repoPath=result.output.path;
				for ( var i = 0; i < result.output.files.length; i++)
					$("#selectDBPackage").append("<option value=\"" + result.output.files[i] + "\">" + result.output.files[i] + "</option>");
			},
			'error' : function(result) {
			}
		});
	},
  
/*Function to render page according to the status of cluster*/
    
  /*Function to remove dialog on page unload*/
    unloadErrorPage:function(){
        $('#deleteClusterDialog').remove();
    },
    
  
/*Function to Upload file*/
	shareKeyFileUpload : function() {
		uploadPathSharedKey=null;
		var uploadUrl = baseUrl + '/uploadFile';
		$('#fileBrowseShareKey').click();
		$('#fileBrowseShareKey').change(
		function(){
			$('#filePathShareKey').val($('#fileBrowseShareKey').val());
			com.impetus.ankush.uploadFileNew(uploadUrl,"fileBrowseShareKey",null,function(data){
				var htmlObject = $(data);
		        var jsonData = JSON.parse(htmlObject.text());
				uploadPathSharedKey= jsonData.output;
			});
		});
	},
	
	/*Function for browse & upload rack file*/
	rackFilepload : function() {
        uploadRackFlag=true;
        uploadRackFilePath=null;
        var uploadUrl = baseUrl + '/uploadFile';
        $('#rackFileBrowse').click();
        $('#rackFileBrowse').change(function(){
	        $('#rackFilePath').val($('#rackFileBrowse').val());
	        com.impetus.ankush.uploadFileNew(uploadUrl,"rackFileBrowse",null,function(data){
	        var htmlObject = $(data);
	        var jsonData = JSON.parse(htmlObject.text());
	        uploadRackFilePath=jsonData.output;
	        });
	    });
    },
/*Function for clientSide validations for node retrieval*/
    validateAuthenticate : function() {
        var userName = $.trim($('#userName').val());
        var password = $('#password').val();
        var errorMsg = '';
        errorCount = 0;
        $("#errorDivMain").html('').hide();
        $('#validateError').html('').hide();
        var flag = false;
        if(!com.impetus.ankush.validation.empty(userName)){
            errorCount++;
            errorMsg = 'Username '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('userName',errorMsg);
            flag = true;
            var divId='userName';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");          
        }else{
            com.impetus.ankush.common.tooltipOriginal('userName',' Enter Username.');
            $('#userName').removeClass('error-box');
        }
        if($("#authGroupBtn .active").data("value")==0){
	        if (!com.impetus.ankush.validation.empty(password)) {
	            errorCount++;
	            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	            com.impetus.ankush.common.tooltipMsgChange('password',errorMsg);
	            flag = true;
	            var divId='password';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
	      
	        }else{
	            com.impetus.ankush.common.tooltipOriginal('password','Enter password.');
	            $('#password').removeClass('error-box');
	        }
        }else{
        	  if (uploadPathSharedKey==null){
        		  errorCount++;
        		  errorMsg = 'Shared Key '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload']; 
                  var divId='filePathShareKey';
                  $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        	  }else{
                  com.impetus.ankush.common.tooltipOriginal('filePathShareKey','Upload File.');
                  $('#filePathShareKey').removeClass('error-box');
        	  }
        }
        if($("#ipModeGroupBtn .active").data("value")==0){
            if(!com.impetus.ankush.validation.empty($('#ipRange').val())){
                errorCount++;
               // errorMsg = 'Provide IP range or Upload an IP file.';
                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('ipRange',errorMsg);
                flag = true;
                var divId='ipRange';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            }else{
                com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP range.');
                $('#ipRange').removeClass('error-box');
            }
        }
        if($("#ipModeGroupBtn .active").data("value")==1){
            if (!com.impetus.ankush.validation.empty($('#filePath').val())) {
                errorCount++;
                errorMsg = 'IP File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
                com.impetus.ankush.common.tooltipMsgChange('filePath',errorMsg);
                flag = true;
                var divId='filePath';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            } else {
                com.impetus.ankush.common.tooltipOriginal('filePath','Browse File.');
                $('#filePath').removeClass('error-box');
            }
        }
        
        if($('#rackMapCheck').is(':checked')){
      	  if (uploadRackFilePath==null) {
	                errorCount++;
	                errorMsg = 'Rack File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
	                com.impetus.ankush.common.tooltipMsgChange('rackFilePath',errorMsg);
	                flag = true;
	                var divId='rackFilePath';
	                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
	            } else {
	                com.impetus.ankush.common.tooltipOriginal('rackFilePath','Browse File.');
	                $('#rackFilePath').removeClass('error-box');
	            }
      }
        if(errorCount>0 && errorMsg!=''){
        	 $('#validateError').show().html(errorCount + ' Error');
        	  $("#errorDivMain").show();
        }else{
        	 com.impetus.ankush.oClusterSetup.getNodes();
        }
    },

/*Function for browse & upload file*/
    getNodesUpload : function() {
        uploadFileFlag=true;
        var uploadUrl = baseUrl + '/uploadFile';
        $('#fileBrowse').click();
        $('#fileBrowse').change(function(){
	        $('#filePath').val($('#fileBrowse').val());
	        com.impetus.ankush.uploadFileNew(uploadUrl,"fileBrowse",null,function(data){
	        var htmlObject = $(data);
	        var jsonData = JSON.parse(htmlObject.text());
	        uploadFilePath=jsonData.output;
	        });
	    });
    },
    
  /*Function for retriving nodes and populate on node table*/
    getNodes : function(nodeData) {
    	 nodeStatus=null;
         if(null!= setupDetailData){
	            setupDetailData.nodes=new Array();	
         }
         inspectNodeData = {};
        $('#retrieveNodeButton').button();
        $('#retrieveNodeButton').button('loading');
        var errorNodeCount=0;
        var userName = $.trim($('#userName').val());
        var password = $('#password').val();
        var nodeData = {};
        nodeData.userName = userName;
        nodeData.password = password;
        if($("#authGroupBtn .active").data("value")==0){
        	nodeData.authTypePassword=true;
        }else{
             nodeData.authTypePassword=false;
             nodeData.password = uploadPathSharedKey;
        }
        if($("#ipModeGroupBtn .active").data("value")==1){
            nodeData.isFileUploaded = true;
            nodeData.nodePattern=uploadFilePath;
           
        }else{
            nodeData.nodePattern =$.trim($('#ipRange').val());
            nodeData.isFileUploaded = false;
        }
        if(!$('#rackMapCheck').is(':checked')){
        	nodeData.isRackEnabled=false;
        }else{
             nodeData.isRackEnabled=true;
             nodeData.filePathRackMap = uploadRackFilePath;
        }
        var clusterId=$('#clusterDeploy').val();
		if(clusterId !=''){
			nodeData.clusterId = clusterId;
		}
       // dataCenterObj={};
       // dataCenterMapTable.fnClearTable();
        getNodeFlag = true;
        disableNodeCount = 0;
        var adminNodeCount=0;
        var nodeUrl = baseUrl + '/cluster/detectNodes';
        $("#nodeTable").show();
     //   $("#dataCenterTable").show();
        var herfFunction;
        var tooltipMsg;
        var divId;
        $('#inspectNodeBtn').attr('disabled','disabled');
        $.ajax({
                    'type' : 'POST',
                    'url' : nodeUrl,
                    'contentType' : 'application/json',
                    'data' : JSON.stringify(nodeData),
                    'async' : true,
                    'dataType' : 'json',
                    'success' : function(result) {
                    	 $('#inspectNodeBtn').removeAttr('disabled');
                        $('#retrieveNodeButton').button('reset');
                        uploadFileFlag=false;
                        nodeStatus = result.output;
                        $('#nodeIpTable tbody tr').css('border-bottom',
                                '1px solid #E1E3E4"');
                        if (oNodeTable != null) {
                            oNodeTable.fnClearTable();
                        }
                        nodeTableLength = nodeStatus.nodes.length;
                        if($("#ipModeGroupBtn .active").data("value")==1){
                            divId='filePath';
                            tooltipMsg='IP pattern is not valid in file';                          
                            herfFunction="javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")";
                          
                        }else{
                            divId='ipRange';
                            tooltipMsg='IP pattern is not valid.';
                            herfFunction="javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")";
                        }
                        if(nodeTableLength == 0){
                            errorMsg = nodeStatus.error;
                            flag = true;
                            $("#errorDivMain").append("<div class='errorLineDiv'><a href="+herfFunction+" >1. "+errorMsg+"</a></div>");                      
                            com.impetus.ankush.common.tooltipMsgChange(divId,tooltipMsg);
                            $("#errorDivMain").show();
                            $('#validateError').show().html('1 Error');
                        }else{
                        	 if($("#ipModeGroupBtn .active").data("value")==1){
                            com.impetus.ankush.common.tooltipOriginal('filePath','Browse File.');
                            $('#filePath').removeClass('error-box');
                            }else{
                            com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP pattern.');
                            $('#ipRange').removeClass('error-box');
                            }
                            $("#errorDivMain").hide();
                            $('#validateError').hide();  
                            }
                      /*  if (uploadRackFilePath==null || !$('#rackMapCheck').is(':checked')) {
                			oNodeTable.fnSetColumnVis( 4, false );
                			oNodeTable.fnSetColumnVis( 5, false );
                		}else{
                			oNodeTable.fnSetColumnVis( 4, true );
                			oNodeTable.fnSetColumnVis( 5, true );
                		}*/
                        for ( var i = 0; i < nodeStatus.nodes.length; i++) {
                       // 	com.impetus.ankush.oClusterSetup.dataCenterMap();
                            var addId = null;
                            if (nodeStatus.nodes[i][1] == "false") {
                                addId = oNodeTable.fnAddData([
                                                '<input type="checkbox" name="" value=""  id="nodeCheck'
                                                        + i
                                                        + '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.oClusterSetup.nodeCheckBox();"/>',
                                                nodeStatus.nodes[i][0],
                                                '<input id="adminCheckBox'
                                                        + i
                                                        + '" type="checkbox"><a  href="#" class="editableLabel" id="adminPort'
                                                        + i
                                                        + '"><label id=""></label></a>',
                                                '<a class="editableLabel" id="storageDirs'
                                                + i
                                                + '"></a>',
                                                '<a class="" id="datacenter'
                                                + i + '">'+ nodeStatus.nodes[i][5]+'</a>',
                                              /*  '<a class="" id="rack'
                                                + i + '">'+ nodeStatus.nodes[i][6]+'</a>'*/,
                                                
                                                '<a class="editableLabel" id="capacity'
                                                        + i + '">-</a>',
                                                '<a class="editableLabel" id="cpu'
                                                        + i + '">-</a>',
                                                '<a class="editableLabel" id="memory'
                                                        + i + '">-</a>',
                                                '<a class="editableLabel" id="registryPort'
                                                        + i + '">-</a>',
                                                        '<div><div style="float:left"><a class="editableLabel" id="haPort1'
                                                        + i
                                                        + '">-</a></div>',
                                                        '<div style="float:left"><a class="editableLabel" id="haPort2'
                                                        + i
                                                        + '">-</a></div></div>',
                                                '<a href="##" onclick="com.impetus.ankush.oracleSetupDetail.loadNodeDetailOracle('
                                                        + i
                                                        + ');"><img id="navigationImg-'
                                                        + i
                                                        + '" src="'
                                                        + baseUrl
                                                        + '/public/images/icon-chevron-right.png" /></a>' ]);
                            } else {
                                addId = oNodeTable.fnAddData([
                                                '<input type="checkbox" name="" value=""  id="nodeCheck'
                                                        + i
                                                        + '" class="nodeCheckBox inspect-node" onclick="com.impetus.ankush.oClusterSetup.nodeCheckBox('
                                                        + i + ');"/>',
                                                nodeStatus.nodes[i][0],
                                                '<input id="adminCheckBox'
                                                        + i
                                                        + '" class="adminCheck" style="margin-right:10px;" type="checkbox" ><a style=margin:left:5px;" href="#" class="editableLabel" id="adminPort'
                                                        + i + '">'+defaultValues.adminPort+'</a>',
                                                        '<a class="editableLabel" id="storageDirs'
                                                        + i
                                                        + '"></a>',
                                                        '<a class="" id="datacenter'
                                                        + i + '">'+ nodeStatus.nodes[i][5]+'</a>',
                                                        /*'<a class="" id="rack'
                                                        + i + '">'+ nodeStatus.nodes[i][6]+'</a>',*/
                                                        
                                                '<a class="editableLabel" id="capacity'
                                                        + i
                                                        + '">'
                                                        + defaultValues.capacity
                                                        + '</a>',
                                                '<a class="editableLabel" id="cpu'
                                                        + i + '">'
                                                        + defaultValues.cpuNo
                                                        + '</a>',
                                                '<a class="editableLabel" id="memory'
                                                        + i + '">'
                                                        + defaultValues.memory
                                                        + '</a>',
                                                '<a class="editableLabel" id="registryPort'
                                                        + i
                                                        + '">'
                                                        + $('#registryPort').val()
                                                        + '</a>',
                                                '<div><div style="float:left"><a class="editableLabel" id="haPort1'
                                                        + i
                                                        + '">'
                                                        + $('#haPort1').val()
                                                        + '</a></div>','<div style="float:left"><a class="editableLabel" id="haPort2'
                                                        + i
                                                        + '">'
                                                        + $('#haPort2').val()
                                                        + '</a></div></div>',
                                                '<div><a href="##" onclick="com.impetus.ankush.oracleSetupDetail.loadNodeDetailOracle('
                                                        + i
                                                        + ');"><img id="navigationImg-'
                                                        + i
                                                        + '" src="'
                                                        + baseUrl
                                                        + '/public/images/icon-chevron-right.png" /></a></div>' ]);
                            }                          
                            var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
                            theNode.setAttribute('id', 'node'+ nodeStatus.nodes[i][0].split('.').join('_'));
                            if (nodeStatus.nodes[i][1] != true
                                    || nodeStatus.nodes[i][2] != true
                                    || nodeStatus.nodes[i][3] != true) {
                                rowId = nodeStatus.nodes[i][0].split('.').join('_');
                                $('td', $('#node'+rowId)).addClass('error-row');
                                $('#node' + rowId).addClass('error-row');
                                $('#nodeCheck' + i).attr('disabled', true);
                                $('#adminCheckBox' + i).attr('disabled', true);
                                errorNodeCount++;
                                disableNodeCount++;
                            }else{
                              /*  $('#nodeCheck'+i).prop("checked", true);
                                if(adminNodeCount<3){
	                                $('#adminCheckBox'+i).prop("checked", true);  
	                                adminNodeCount++;
                                }*/
                            }
                            if(nodeStatus.nodes[i][1] != true){
                                var status='Unavailable';
                            $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
	                        }else if(nodeStatus.nodes[i][2] != true){
	                            var status='Unreachable';
	                            $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");
	                        }else if(nodeStatus.nodes[i][3] != true){
	                            var status='Unauthenticated';
	                            $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+(errorCount+errorNodeCount)+". Node "+nodeStatus.nodes[i][0]+" "+status+"</a></div>");  
	                        }
	                        }
	                       /* if(disableNodeCount != nodeTableLength){
	                            $('#nodeCheckHead').prop("checked", true);
	                        }*/
                        $('#nodeCheckHead').removeAttr("checked");
	                        $('.editableLabel').editable({
	                            type : 'text',
	                        });
	                    },
                error : function() {
                	 $('#inspectNodeBtn').removeAttr('disabled');
                    $('#retrieveNodeButton').button('reset');
                }
                });
    },
  //this function will inspect nodes
	inspectNodesObject : function(id){
		var data = {};
		data.nodePorts = {};
		$('.inspect-node').each(function(){
			if($(this).is(':checked')){
				$(this).addClass('inspect-node-ok');
				var ip = $(this).parent().next().text();
				data.nodePorts[ip] = com.impetus.ankush.oClusterSetup.getIpPorts(this);
			}
		});
		data.username = $('#userName').val();
		if($("#authGroupBtn .active").data("value")==0){
			data.password = $('#password').val();
        }else{
        	data.privateKey = uploadPathSharedKey;
        }
		com.impetus.ankush.inspectNodesCall(data,id,'retrieveNodeButton');
	},
    //this function will return usable ports
	getIpPorts : function(elem){
		var portArray = [];
		var isAdminChecked = $(elem).parent().next().next().children().first().is(':checked');
		if(isAdminChecked){
			var adminPort = $(elem).parent().next().next().children().next().text();
			portArray.push(adminPort);
		}
		var regPort = $(elem).parent().next().next().next().next().next().next().next().next().children().text();
		portArray.push(regPort);
		var haPort1 = $(elem).parent().next().next().next().next().next().next().next().next().next().children().children().text();
		var haPort2 = $(elem).parent().next().next().next().next().next().next().next().next().next().next().children().children().text();
		portArray.push(haPort1+'-'+haPort2);
		return portArray;
	},
 /*   dataCenterMap : function() {
    	dataCenterMapTable.fnClearTable();
        for ( var key in dataCenterObj) {
    	dataCenterMapTable.fnAddData([
    	                              key,
    	                              dataCenterObj[key].nodeCount,
    	                              dataCenterObj[key].capacity,
    	                              '<a id="replicationFactor'+key+'" class="editableLabel">1</a>',
    	                      ]);
        }
        $('.editableLabel').editable({
            type : 'text',
        });
    },*/
/*Function for check/uncheck all check boxes according to header check box of a table*/ 
    checkAllNode : function(elem) {
        if (nodeStatus == null)
            nodeStatus = defaultValuesError;
        var val = $('input:[name=nodeCheckHead]').is(':checked');
        if (val == true) {
            for ( var i = 0; i < nodeStatus.nodes.length; i++) {
                if ($('#nodeCheck' + i).is(':disabled') == false) {
                    $('#nodeCheck' + i).prop("checked", true);
                    $('#adminCheckBox' + i).prop("disabled", false);
                }
            }
        } else {
            $('.nodeCheckBox').prop("checked", false);
            $('.adminCheck').prop("disabled", true);
            $('.adminCheck').prop("checked", false);
        }

    },
   
/*Function for check/uncheck header check boxe according to node check box of a table & change properties accordingly*/ 
    nodeCheckBox : function(i) {
        if ($('#nodeCheck' + i).is(':checked')) {
            $('#adminCheckBox' + i).prop("disabled", false);
           
          /* if(dataCenterObj[$('#datacenter'+i).text()]){
        	   dataCenterObj[$('#datacenter'+i).text()].capacity= dataCenterObj[$('#datacenter'+i).text()].capacity+ parseInt($('#capacity'+i).text());
               dataCenterObj[$('#datacenter'+i).text()].nodeCount=dataCenterObj[$('#datacenter'+i).text()].nodeCount+1;
           }else{
        	   dataCenterObj[$('#datacenter'+i).text()]={};
        	   dataCenterObj[$('#datacenter'+i).text()].capacity=parseInt($('#capacity'+i).text());
               dataCenterObj[$('#datacenter'+i).text()].nodeCount=1;
           } */
        } else {
            $('#adminCheckBox' + i).prop("disabled", true);
            $('#adminCheckBox' + i).prop("checked", false);
      /*     if(dataCenterObj[$('#datacenter'+i).text()].nodeCount==1){
            	delete dataCenterObj[$('#datacenter'+i).text()];
            }else{
            	dataCenterObj[$('#datacenter'+i).text()].capacity=dataCenterObj[$('#datacenter'+i).text()].capacity - parseInt($('#capacity'+i).text());
                dataCenterObj[$('#datacenter'+i).text()].nodeCount=dataCenterObj[$('#datacenter'+i).text()].nodeCount-1;
            }
            */
        }
        //com.impetus.ankush.oClusterSetup.dataCenterMap(); 
        if ($("#nodeTable .nodeCheckBox:checked").length == $("#nodeTable .nodeCheckBox").length
                - disableNodeCount) {
            $("#nodeCheckHead").prop("checked", true);
        } else {
            $("#nodeCheckHead").removeAttr("checked");
        }
    },
    
/*Function for populating values on node drill down of a node of node table*/
  /*  nodeDetail : function(node) {
    	if(nodeStatus!=null && setupDetailData==null ){
        if(nodeStatus.nodes[node][1]==false){
            $('#nodeStatus').html('').text('Unavailable');
        }else if(nodeStatus.nodes[node][2]==false){
            $('#nodeStatus').html('').text('Unreachable');
        }else if(nodeStatus.nodes[node][3]==false){
            $('#nodeStatus').html('').text('Unauthenticated');
        }else{
            $('#nodeStatus').html('').text('Available');
        }
        if ($('#adminCheckBox' + node).is(':checked')) {
            $('#adminStatus').html('').text("Yes");
        } else {
            $('#adminStatus').html('').text("No");
        }
        $('#nodeDetailHead').html('').html(nodeStatus.nodes[node][0]);
        $('#nodeStorageDir').html('').text($('#storageDir' + node).text());
        $('#nodeDatacenter').html('').text($('#datacenter' + node).text());
     //   $('#nodeRack').html('').text($('#rack' + node).text());
        $('#nodeCapacity').html('').text($('#capacity' + node).text());
        $('#nodeCpu').html('').text($('#cpu' + node).text());
        $('#nodeMemory').html('').text($('#memory' + node).text());
        $('#nodeRegPort').html('').text($('#registryPort' + node).text());
        $('#nodeHA1').html('').text($('#haPort1' + node).text());
        $('#nodeHA2').html('').text($('#haPort2' + node).text());
        }else{
        	 $('#nodeDetailHead').html('').html(setupDetailData.nodes[node].publicIp);
        	  if ($('#adminCheckBox' + node).is(':checked')) {
                  $('#adminStatus').html('').text("Yes");
              } else {
                  $('#adminStatus').html('').text("No");
              }
        	  $('#nodeStatus').html('').text('Available');
        	  $('#nodeStorageDir').html('').text($('#storageDir' + node).text());
              $('#nodeDatacenter').html('').text($('#datacenter' + node).text());
           //   $('#nodeRack').html('').text($('#rack' + node).text());
             $('#nodeCapacity').html('').text($('#capacity' + node).text());
             $('#nodeCpu').html('').text($('#cpu' + node).text());
             $('#nodeMemory').html('').text($('#memory' + node).text());
             $('#nodeRegPort').html('').text($('#registryPort' + node).text());
             $('#nodeHA1').html('').text($('#haPort1' + node).text());
             $('#nodeHA2').html('').text($('#haPort2' + node).text());
             if(setupDetailData.state =='error'){
          		 if(Object.keys(setupDetailData.nodes[node].errors).length>0){
          			 $('#errorNodeDiv').show();
          		 }
          		  $('#nodeDeploymentError').css("margin-top","10px");
          		  for(var key in setupDetailData.nodes[node].errors){
          	          $('#nodeDeploymentError').append('<label class="text-left" style="color: black;" id="'+key+'">'+setupDetailData.nodes[node].errors[key]+'</label>');
          	           }
          	  }
        }
    },
    */

   /* setupDetailValuePopulate : function(currentClusterId){
    	com.impetus.ankush.common.pageStyleChange();
    	$('#haPort1').css('width','50%');
    	$('#haPort2').css('width','50%');
    	$('#nodeTable').show();
    	$('#clusterDeploy').attr('disabled',true);
    	$('.nodeCheckBox').attr('disabled','disabled');
    	$('.adminCheckBox').attr('disabled','disabled');
		$('#retrieveNodeButton').attr('disabled','disabled');
		$('#filePathDb').attr('disabled','disabled');
		$('#toggleButtonOracle').hide();
		$('.btnGrp').attr('disabled','disabled');
		if (oNodeTable != null) {
			oNodeTable.fnClearTable();
		}
		var url = baseUrl + '/monitor/' + currentClusterId + '/details';
		      $.ajax({
                  'type' : 'GET',
                  'url' : url,
                  'contentType' : 'application/json',
                  'dataType' : 'json',
                  'async' : true,
                  'success' : function(result) {
                	  setupDetailData=result.output;
                	  if(setupDetailData.state=='error'){
                		   $('#commonDeleteButton').removeAttr('disabled');
                		   $('#errorDivMain').html('');
                		   $('#validateError').html('');
                		   var count=0;
                		   for(var key in setupDetailData.errors){
                			   if(key=='ntpServer' ||key=='validation'){
                				   count++;
                    			   $("#errorDivMain").append("<div class='errorLineDiv'><a href='#'  >"+count+". "+setupDetailData.errors[key]+"</a></div>");   
                			   }
                			   else{
                				   count++;
                    			   $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+count+". "+setupDetailData.errors[key]+"</a></div>");   
                			   }
                			}
                		  if(Object.keys(setupDetailData.errors).length > 0){
                			  	$("#errorDivMain").show();
                			  	if(Object.keys(setupDetailData.errors).length > 1){
                			  		$('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Errors');
                			  	}
		                        else{
		                            $('#validateError').show().html(Object.keys(setupDetailData.errors).length+ ' Error');
		                        }
                		  }
                	   } if(!setupDetailData.status){
                		  $("#errorDivMain").html('').append("<div class='errorLineDiv'><a href='#'  >1. "+setupDetailData.errors[0]+"</a></div>");
                		  $('#errorDivMain').show();
                		  $('#validateError').html('').text('1 Error').show();
                		  $('#commonDeleteButton').removeAttr('disabled');
               		   return;
               	   }
                	   $('#storeName').val(setupDetailData.clusterName).removeAttr('title');
                	   $('#dataCenter').val(setupDetailData.oracleNoSQLConf.datacenterName).removeAttr('title');
                	   $('#topology').val(setupDetailData.oracleNoSQLConf.topologyName).removeAttr('title');
                	   $('#repFactor').val(setupDetailData.oracleNoSQLConf.replicationFactor).removeAttr('title');
                	   $('#partitions').val(setupDetailData.oracleNoSQLConf.partitionCount).removeAttr('title');
                	   $('#registryPort').val(setupDetailData.oracleNoSQLConf.registryPort).removeAttr('title');
                	   $('#haPort1').val(setupDetailData.oracleNoSQLConf.haPortRangeStart).removeAttr('title');
                	   $('#haPort2').val(setupDetailData.oracleNoSQLConf.haPortRangeEnd).removeAttr('title');
                	   $('#baseDir').val(setupDetailData.oracleNoSQLConf.basePath).removeAttr('title');
                	   $('#installationPath').val(setupDetailData.oracleNoSQLConf.installationPath).removeAttr('title');
                	   $('#dataPath').val(setupDetailData.oracleNoSQLConf.dataPath).removeAttr('title');
                	   var dbPackage=setupDetailData.oracleNoSQLConf.serverTarballLocation.split('repo/')[1];
                	   $("#selectDBPackage").html('').append(
                               "<option value=\"" + dbPackage+ "\">"
                                       + dbPackage + "</option>");
                	   $('#ntpServer').val(setupDetailData.oracleNoSQLConf.ntpServer).removeAttr('title');
                	   $('#userName').val(setupDetailData.username).removeAttr('title');
                	   $('#password').val(setupDetailData.password).removeAttr('title');
                	   if(setupDetailData.ipPattern==''){
            	    	   $('#filePath').val(setupDetailData.patternFile);
                		   $('#throughPassword').removeClass('active');
                		   $('#throughSharedKey').addClass('active');
                		   $('#fileUploadDiv').show();
                		   $('#ipRangeDiv').hide();
                	   }else{
                		   $('#ipRangeDiv').show();
                		   $('#fileUploadDiv').hide();
                		   $('#ipRange').val(setupDetailData.ipPattern).removeAttr('title');
                		   $('#throughSharedKey').removeClass('active');
                		   $('#throughPassword').addClass('active');
                	   }
                	   for ( var i = 0; i < setupDetailData.nodes.length; i++) {
                           var addId = null;
                           var adminPort='';
                            adminPort=setupDetailData.nodes[i].adminPort;
                           if(adminPort==null){
                        	   adminPort='';
                           }
                           addId = oNodeTable.fnAddData([
                                                         '<input type="checkbox" name="" value=""  id="nodeCheck'
                                                                 + i
                                                                 + '" class="nodeCheckBox" onclick="com.impetus.ankush.oClusterSetup.nodeCheckBox('
                                                                 + i + ');"/>',
                                                                 setupDetailData.nodes[i].publicIp,
                                                         '<input id="adminCheckBox'
                                                                 + i
                                                                 + '" class="adminCheck" style="margin-right:10px;" type="checkbox" ><a style=margin:left:5px;" href="#" class="editableLabel" id="adminPort'
                                                                 + i + '">'+adminPort+'</a>',
                                                         '<a class="editableLabel" id="storageDirs'
               		                                          + i
               		                                          + '">'
               		                                          + setupDetailData.nodes[i].storageDirs
               		                                          + '</a>',
               		                                     '<a class="editableLabel" id="datacenter'
               		                                          + i
               		                                          + '">'
               		                                          + setupDetailData.nodes[i].datacenter
               		                                          + '</a>',
               		                                     '<a class="editableLabel" id="rack'
               		                                          + i
               		                                          + '">'
               		                                          + setupDetailData.nodes[i].rack
               		                                          + '</a>',
               		                                          
                                                         '<a class="editableLabel" id="capacity'
                                                                 + i
                                                                 + '">'
                                                                 + setupDetailData.nodes[i].capacity
                                                                 + '</a>',
                                                         '<a class="" id="cpu'
                                                                 + i + '">'
                                                                 + setupDetailData.nodes[i].cpuNum
                                                                 + '</a>',
                                                         '<a class="" id="memory'
                                                                 + i + '">'
                                                                 + setupDetailData.nodes[i].memoryMb
                                                                 + '</a>',
                                                         '<a class="" id="registryPort'
                                                                 + i
                                                                 + '">'
                                                                 + setupDetailData.nodes[i].registryPort
                                                                 + '</a>',
                                                         '<div><div style="float:left"><a class="" id="haPort1'
                                                                 + i
                                                                 + '">'
                                                                 + setupDetailData.nodes[i].haPortRangeStart
                                                                 + '</a></div>','<div style="float:left"><a class="" id="haPort2'
                                                                 + i
                                                                 + '">'
                                                                 + setupDetailData.nodes[i].haPortRangeEnd
                                                                 + '</a></div></div>',
                                                         '<div><a href="##" onclick="com.impetus.ankush.oracleSetupDetail.loadNodeDetailOracle('
                                                                 + i
                                                                 + ');"><img id="navigationImg-'
                                                                 + i
                                                                 + '" src="'
                                                                 + baseUrl
                                                                 + '/public/images/icon-chevron-right.png" /></a></div>' ]);
                           var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
              				theNode.setAttribute('id', 'node'+ setupDetailData.nodes[i].publicIp.split('.').join('_'));
              				if (Object.keys(setupDetailData.nodes[i].errors).length>0 ){
              					rowId = setupDetailData.nodes[i].publicIp.split('.').join('_');
              					$('td', $('#node'+rowId)).addClass('error-row');
              					$('#node' + rowId).addClass('error-row');
              				}
              				$('.nodeCheckBox').attr('checked',true);
                      	    $('#nodeCheckHead').attr('checked',true);
                      	    $('.adminCheck').attr('disabled','disabled');
                      	    $('.nodeCheckBox').attr('disabled','disabled');
	                        if(setupDetailData.nodes[i].admin){
	                        	$('#adminCheckBox'+i).attr('checked',true);
	                        }else{
	                        	$('#adminCheckBox'+i).attr('checked',false);
                        }
                      }                          
                  }
		      });
    },*/
    

/*Function for client side validations to be checked on click on deploy button*/
    validatePage : function() {
        userFlag=false;
        passwordFlag=false;
        $('#validateError').hide();
        $('#validateError').html('');
        var errorMsg = '';
        $("#errorDivMain").html('');
        errorCount = 0;
        var flag = false;

        if (!com.impetus.ankush.validation.empty($('#storeName').val())) {
            errorCount++;
            errorMsg = 'Store Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('storeName','Store Name cannot be empty');
            flag = true;
            var divId='storeName';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if ($('#storeName').val().trim().split(' ').length > 1) {
            errorCount++;
            errorMsg = 'Store Name '+com.impetus.ankush.errorMsg.errorHash['ClusterNameBlankSpace'];
            com.impetus.ankush.common.tooltipMsgChange('storeName','Store Name cannot contain blank spaces');
            flag = true;
            var divId='storeName';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+"."+errorMsg+"</a></div>");
        }else if ($('#storeName').val().length > 20) {
            errorCount++;
            errorMsg = 'Store Name '+com.impetus.ankush.errorMsg.errorHash['ClusterNameLength'];
            com.impetus.ankush.common.tooltipMsgChange('storeName',errorMsg);
            flag = true;
            var divId='storeName';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
        }
        else if (!com.impetus.ankush.validation.alphaNumericChar($('#storeName').val())) {
            errorCount++;
            errorMsg = 'Store Name '+com.impetus.ankush.errorMsg.errorHash['ClusterNameSpecialChar'];
            com.impetus.ankush.common.tooltipMsgChange('storeName',errorMsg);
            flag = true;
            var divId='storeName';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
        }
        else {
            com.impetus.ankush.common.tooltipOriginal('storeName','Enter Store Name.');
            $('#storeName').removeClass('error-box');
        }
        if (!com.impetus.ankush.validation.empty($('#dataCenter').val())) {
            errorCount++;
            errorMsg = 'DataCenter '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('dataCenter','Data Center cannot be empty');
            flag = true;
            var divId='dataCenter';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        }else if (!com.impetus.ankush.validation.alphaNumericSpace($('#dataCenter').val())) {
            errorCount++;
            errorMsg = 'DataCenter '+com.impetus.ankush.errorMsg.errorHash['AlphaNumeric'];
            com.impetus.ankush.common.tooltipMsgChange('dataCenter',errorMsg);
            flag = true;
            var divId='dataCenter';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        }else if ($('#dataCenter').val().length>20) {
            errorCount++;
            errorMsg = 'DataCenter '+com.impetus.ankush.errorMsg.errorHash['ClusterNameLength'];
            com.impetus.ankush.common.tooltipMsgChange('dataCenter',errorMsg);
            flag = true;
            var divId='dataCenter';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        }
        else {
            $('#dataCenter').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('dataCenter','Enter Datacenter.');
        }
        if (!com.impetus.ankush.validation.empty($('#topology').val())) {  
            errorCount++;
            errorMsg = 'Topology '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            $("#popoveif(userName=='' || )r-content").append(errorMsg + '</br>');
            com.impetus.ankush.common.tooltipMsgChange('topology','Topology cannot be empty');
            flag = true;
            var divId='topology';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.alphaNumericSpace($('#topology').val())) {
            errorCount++;
            errorMsg = 'Topology '+com.impetus.ankush.errorMsg.errorHash['AlphaNumeric'];
            com.impetus.ankush.common.tooltipMsgChange('topology',errorMsg);
            flag = true;
            var divId='topology';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        }else if ($('#topology').val().length>20) {
            errorCount++;
            errorMsg = 'Topology '+com.impetus.ankush.errorMsg.errorHash['ClusterNameLength'];
            com.impetus.ankush.common.tooltipMsgChange('topology',errorMsg);
            flag = true;
            var divId='topology';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        }
        else {
            $('#topology').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('topology','Enter Topology');
        }
        if (!com.impetus.ankush.validation.empty($('#repFactor').val())) {
            errorCount++;
            errorMsg = 'Replication Factor '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('repFactor','Replication factor cannot be empty');
            flag = true;
            var divId='repFactor';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.numeric($('#repFactor').val())) {
            errorCount++;
            errorMsg = 'Replication Factor '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
            com.impetus.ankush.common.tooltipMsgChange('repFactor','Replication factor must be numeric');
            flag = true;
            var divId='repFactor';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        }
         else if ($('#repFactor').val()<1) {
                errorCount++;
                errorMsg = 'Replication Factor '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
                com.impetus.ankush.common.tooltipMsgChange('repFactor','Replication factor must be greater than 0');
                flag = true;
                var divId='repFactor';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
            } else {
            $('#repFactor').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('repFactor','Enter Replication Factor');
        }
        if (!com.impetus.ankush.validation.empty($('#partitions').val())) {
            errorCount++;
            errorMsg = 'Partitions '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('partitions','Partition value cannot be empty');
            flag = true;
            var divId='partitions';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.numeric($('#partitions').val())) {
            errorCount++;
            errorMsg = 'Partition '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
            com.impetus.ankush.common.tooltipMsgChange('partitions','Partition field must be a positive integer');
            flag = true;
            var divId='partitions';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if ($('#partitions').val()<1) {
            errorCount++;
            errorMsg = 'Partition '+com.impetus.ankush.errorMsg.errorHash['PositiveInteger'];
            com.impetus.ankush.common.tooltipMsgChange('partitions','Partition must be greater than 0');
            flag = true;
            var divId='partitions';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if ($('#partitions').val()>1000000) {
            errorCount++;
            errorMsg = 'Partition field must be less than or equal to 1000000';
            com.impetus.ankush.common.tooltipMsgChange('partitions',errorMsg);
            flag = true;
            var divId='partitions';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        } else {
            $('#partitions').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('partitions','Enter Partition');
        }
        if (!com.impetus.ankush.validation.empty($('#registryPort').val())) {
            errorCount++;
            errorMsg = 'Registry Port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('registryPort','Registry Port cannot be empty');
            flag = true;
            var divId='registryPort';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.numeric($('#registryPort').val())) {
            errorCount++;
            errorMsg = 'Registry Port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
            com.impetus.ankush.common.tooltipMsgChange('registryPort','Registry Port must be numeric');
            flag = true;
            var divId='registryPort';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.oPort($('#registryPort').val())) {
            errorCount++;
            errorMsg = 'Registry Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
            com.impetus.ankush.common.tooltipMsgChange('registryPort','Registry Port must be between 1024-65535');
            flag = true;
            var divId='registryPort';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else {
            $('#registryPort').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('registryPort','Enter Registry Port');
        }
        if (!com.impetus.ankush.validation.empty($('#haPort1').val())) {
            errorCount++;
            errorMsg = 'HA Port Start '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('haPort1','HA Port Start cannot be empty');
            flag = true;
            var divId='haPort1';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.numeric($('#haPort1').val())) {
            errorCount++;
            errorMsg = 'HA Port Start '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
            com.impetus.ankush.common.tooltipMsgChange('haPort1','HA Port Start must be numeric');
            flag = true;
            var divId='haPort1';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.oPort($('#haPort1').val())) {
            errorCount++;
            errorMsg = 'HA Port Start '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
            com.impetus.ankush.common.tooltipMsgChange('haPort1','HA Port Start must be between 1024-65535'); 
            flag = true;
            var divId='haPort1';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else {
            $('#haPort1').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('haPort1','Enter HA Port Start');
        }
        if (!com.impetus.ankush.validation.empty($('#haPort2').val())) {
            errorCount++;
            errorMsg = 'HA Port End '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('haPort2','HA Port End cannot be empty');
            flag = true;
            var divId='haPort2';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.numeric($('#haPort2').val())) {
            errorCount++;
            errorMsg = 'HA Port End '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
            com.impetus.ankush.common.tooltipMsgChange('haPort2','HA Port End must be numeric');
            flag = true;
            var divId='haPort2';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else if (!com.impetus.ankush.validation.oPort($('#haPort2').val())) {
            errorCount++;
            errorMsg = 'HA Port End '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
            com.impetus.ankush.common.tooltipMsgChange('haPort2','HA Port End must be between 1024-65535');
            flag = true;
            var divId='haPort2';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else {
            $('#haPort2').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('haPort2','Enter HA Port End');
        }
        if (!com.impetus.ankush.validation.empty($('#baseDir').val())) {
            errorCount++;
            errorMsg = 'Base directory Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('baseDir', 'Base directory path cannot be empty');
            flag = true;
            var divId='baseDir';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else {
            $('#baseDir').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('baseDir','Enter Base Directory path');
        }
        if (!com.impetus.ankush.validation.empty($('#installationPath').val())) {
            errorCount++;
            errorMsg = 'Installation Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('installationPath', 'Installation Path cannot be empty');
            flag = true;
            var divId='installationPath';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else {
            $('#installationPath').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('installationPath', 'Enter Installation Path');
        }
        if (!com.impetus.ankush.validation.empty($('#dataPath').val())) {
            errorCount++;
            errorMsg = 'Data Path '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('dataPath','Data Path cannot be empty');
            flag = true;
            var divId='dataPath';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else {
            $('#dataPath').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('dataPath','Enter Data Path');
        }
        if ($('#selectDBPackage').val()==null || $('#selectDBPackage').val()=='') {
            errorCount++;
            errorMsg = 'Oracle DB package '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
            com.impetus.ankush.common.tooltipMsgChange('selectDBPackage',errorMsg);
            flag = true;
            var divId='selectDBPackage';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        }else {
            $('#selectDBPackage').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('selectDBPackage','Select or upload the Oracle NoSQL Database Package version. Currently Support <kv-ee-2.0.26>');
        }
        if (!com.impetus.ankush.validation.empty($('#ntpServer').val())) {
            errorCount++;
            errorMsg = 'NTP Server '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('ntpServer','NTP server cannot be empty');
            flag = true;
            var divId='ntpServer';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        }
        else  if(!com.impetus.ankush.validation.alphaNumericChar($('#ntpServer').val())) {
            errorCount++;
            errorMsg = 'NTP Server '+com.impetus.ankush.errorMsg.errorHash['ClusterNameSpecialChar'];
            com.impetus.ankush.common.tooltipMsgChange('ntpServer',errorMsg);
            flag = true;
            var divId='ntpServer';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        }
        else {
            $('#ntpServer').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('ntpServer',
                    'Enter NTP server');
        }
        if (!com.impetus.ankush.validation.empty($('#userName').val())) {          
            errorCount++;
            errorMsg = 'User Name '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
            com.impetus.ankush.common.tooltipMsgChange('userName','Username cannot be empty');
            flag = true;
            var divId='userName';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
        } else {
            $('#userName').removeClass('error-box');
            com.impetus.ankush.common.tooltipOriginal('userName','Enter Username');
        }
        if($("#authGroupBtn .active").data("value")==0){
	        if (!com.impetus.ankush.validation.empty($('#password').val())) {
	            errorCount++;
	            errorMsg = 'Password '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
	            com.impetus.ankush.common.tooltipMsgChange('password','Password cannot be empty');
	            flag = true;
	            var divId='password';
	            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
	        } else {
	            $('#password').removeClass('error-box');
	            com.impetus.ankush.common.tooltipOriginal('password','Enter Password');
	        }
        } else{
          	  if (uploadPathSharedKey==null){
	      		  errorCount++;
	              errorMsg = 'Shared Key '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload']; 
	              var divId='filePathShareKey';
	              $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
          	  } else {
                    com.impetus.ankush.common.tooltipOriginal('filePathShareKey','Uploaad File.');
                    $('#filePathShareKey').removeClass('error-box');
          }
          }
        if($("#ipModeGroupBtn .active").data("value")==0){
            if (!com.impetus.ankush.validation.empty($('#ipRange').val()) && !nodeTableLength>0){      
                errorCount++;
                errorMsg = 'IP Range '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                com.impetus.ankush.common.tooltipMsgChange('ipRange','IP range cannot be empty');
                flag = true;
                var divId='ipRange';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");

            } else {
                $('#ipRange').removeClass('error-box');
                com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP range');
            }
        }
        if($("#ipModeGroupBtn .active").data("value")==1){
            if (!com.impetus.ankush.validation.empty($('#filePath').val()) && !nodeTableLength>0){      
                errorCount++;
                errorMsg = 'IP File '+com.impetus.ankush.errorMsg.errorHash['FileNotUpload'];
                com.impetus.ankush.common.tooltipMsgChange('filePath','File Path cannot be empty');
                flag = true;
                var divId='filePath';
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+"."+errorMsg+"</a></div>");
            }else {
                $('#filePath').removeClass('error-box');
                com.impetus.ankush.common.tooltipOriginal('filePath','Browse file');
            }
        } 
        if($('#clusterDeploy').val()!=''){
		getNodeFlag=true;
        }
        if ($('#nodeIpTable .nodeCheckBox:checked').length < $('#repFactor').val()) {
            errorMsg = com.impetus.ankush.errorMsg.errorHash['ReplicationFactorVal'];
            if(getNodeFlag){
                errorCount++;
                flag = true;
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+errorCount+". "+errorMsg+"</a></div>");
            }
        }
        if (parseInt($.trim($("#haPort2").val())) < parseInt($.trim($("#haPort1").val()))) {
            errorMsg = com.impetus.ankush.errorMsg.errorHash['HAPortRange'];
            errorCount++;
            flag = true;
            var divId='haPort2';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
        }
        if (!com.impetus.ankush.validation.portCheck($("#registryPort").val(),0, $("#haPort1").val(), $("#haPort2").val(), "")) {
            errorMsg = com.impetus.ankush.errorMsg.errorHash['RegistryPort'];
            errorCount++;
            flag = true;
            var divId='registryPort';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
        }
      
        if(getNodeFlag){
        if ($('#nodeIpTable .nodeCheckBox:checked').length < 1) {
            errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectNode'];
            errorCount++;
            flag = true;
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+errorCount+". "+errorMsg+"</a></div>");
            }else{
                var count=0;
                var adminPort=0;
                var status= false;
                $('.nodeCheckBox').each(function(){
                    status= false;
                    if($('#nodeCheck'+count).is(':checked')){
                        errorMsg='';
                        if($('#adminCheckBox'+count).is(':checked')){
                            if (!com.impetus.ankush.validation.empty($('#adminPort'+count).text())) {                  
                                errorMsg = errorMsg +' Admin port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                                status= true;
                            } else if (!com.impetus.ankush.validation.numeric($('#adminPort'+count).text())) {              
                                errorMsg = errorMsg+' Admin port '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];                  
                                status= true;
                            }
                             else if (!com.impetus.ankush.validation.oPort($('#adminPort'+count).text())) {
                                errorMsg = errorMsg+ ' Admin Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                                status= true;
                            }else{
                            adminPort=$('#adminPort'+count).text();
                            }
                        }
                        if (!com.impetus.ankush.validation.empty($('#capacity'+count).text())) {                  
                        errorMsg =errorMsg+ ' Capacity '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                        status= true;
                        } else if (!com.impetus.ankush.validation.numeric($('#capacity'+count).text())) {              
                            errorMsg = errorMsg+' Capacity '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];
                            status= true;
                        }
                        if (!com.impetus.ankush.validation.empty($('#cpu'+count).text())) {                  
                            errorMsg = errorMsg +' CPU '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                            status= true;
                        } else if (!com.impetus.ankush.validation.numeric($('#cpu'+count).text())) {              
                            errorMsg = errorMsg+' CPU '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];                  
                            status= true;
                        }
                        if (!com.impetus.ankush.validation.empty($('#memory'+count).text())) {                  
                        errorMsg = errorMsg+ ' Memory '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                        status= true;
                        } else if (!com.impetus.ankush.validation.numeric($('#memory'+count).text())) {              
                            errorMsg =errorMsg+ ' Memory '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];      
                            status= true;
                        }
                        if (!com.impetus.ankush.validation.empty($('#registryPort'+count).text())) {                  
                        errorMsg =errorMsg+  ' Registry Port '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                        status= true;
                        } else if (!com.impetus.ankush.validation.numeric($('#registryPort'+count).text())) {              
                            errorMsg =  errorMsg+ ' Registry '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];      
                            status= true;
                        } else if (!com.impetus.ankush.validation.oPort($('#registryPort'+count).text())) {
                            errorMsg = errorMsg+ ' Registry Port '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                            status= true;
                        }
                        if (!com.impetus.ankush.validation.empty($('#haPort1'+count).text())) {                  
                            errorMsg = errorMsg+ ' HA Port Start '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                            status= true;
                      
                            } else if (!com.impetus.ankush.validation.numeric($('#haPort1'+count).text())) {              
                                errorMsg = errorMsg+ ' HA Port Start '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];          
                                status= true;
                            } else if (!com.impetus.ankush.validation.oPort($('#haPort1'+count).text())) {
                                errorMsg = errorMsg+ ' HA Port Start '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                                status= true;
                            }
                        if (!com.impetus.ankush.validation.empty($('#haPort2'+count).text())) {                  
                            errorMsg = errorMsg+ ' HA Port End '+com.impetus.ankush.errorMsg.errorHash['FieldEmpty'];
                            status= true;
                          
                            } else if (!com.impetus.ankush.validation.numeric($('#haPort2'+count).text())) {              
                                errorMsg = errorMsg+ ' HA Port End '+com.impetus.ankush.errorMsg.errorHash['NotNumeric'];  
                                status= true;
                            } else if (!com.impetus.ankush.validation.oPort($('#haPort2'+count).text())) {
                                errorMsg = errorMsg+ ' HA Port End '+com.impetus.ankush.errorMsg.errorHash['PortRange'];
                                status= true;
                            }
                        if (parseInt($.trim($("#haPort2"+count).text())) < parseInt($.trim($("#haPort1"+count).text()))) {
                            errorMsg = errorMsg+''+com.impetus.ankush.errorMsg.errorHash['HAPortRange'];
                            status= true;                          
                        }
                        var portStatus=com.impetus.ankush.validation.allPortCheck($("#registryPort"+count).text(),adminPort, $("#haPort1"+count).text(), $("#haPort2"+count).text(), "");
                        if (portStatus!=0) {
                            if (portStatus==1) {
                                errorMsg = errorMsg+" "+com.impetus.ankush.errorMsg.errorHash['SamePort'];
                                status= true;
                            }
                            if (portStatus==2) {
                                errorMsg = errorMsg+" "+com.impetus.ankush.errorMsg.errorHash['RegistryPort'];
                                status= true;
                            }
                            if (portStatus==3) {
                                errorMsg = errorMsg+" "+com.impetus.ankush.errorMsg.errorHash['AdminPort'];
                                status= true;
                            }
                        }                          
                    }
                    if(status){
                        errorCount++;
                        var nodeIp=nodeStatus.nodes[count][0];
                        $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable' style='color: #5682C2;'>"+errorCount+".For "+nodeIp+": "+errorMsg+"</a></div>");
                    }
                    count++;
                });
            }
        }
        if(getNodeFlag){
        if ($('#nodeIpTable .nodeCheckBox:checked').length > 0) {
            errorMsg = com.impetus.ankush.errorMsg.errorHash['SelectOneNode']+' as an admin.';
            if ($('#nodeIpTable .adminCheck:checked').length < 1) {
                errorCount++;
                flag = true;
                $("#errorDivMain").append("<div class='errorLineDiv'><a href='#nodeIpTable'  >"+errorCount+". "+errorMsg+"</a></div>");
            }
            }
        }
        if (!getNodeFlag) {
        errorMsg = com.impetus.ankush.errorMsg.errorHash['RetrieveNode'];
            var divId='ipRange';
                errorCount++;
                flag = true;
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
        }
        if (errorCount > 0) {
            $("#errorDivMain").show();
               if(errorCount > 1)
                    $('#validateError').text(errorCount + " Errors");
                else
                    $('#validateError').text(errorCount + " Error");
            $('#validateError').show().html(errorCount + ' Error');
        } else {
            $("#errorDivMain").hide();
            $('#validateError').hide();
            com.impetus.ankush.oClusterSetup.oClusterDeploy();
        }
    },

/*Function for cluster deployment call---deployment data is prepared and send in post ajax call*/
	oClusterDeploy : function() {
		deployedCluster = true;
		var data = {
				oracleNoSQLConf:{
					nodes : new Array()
				},
		};
		var nodeArray = {};
		data.clusterName = $.trim($('#storeName').val());
		data.username = $.trim($('#userName').val());
		data.technology = "Oracle NoSQL Database";
		  if($("#ipModeGroupBtn .active").data("value")==0){
			data.ipPattern = $.trim($('#ipRange').val());
			data.patternFile='';
		}else{
			data.patternFile = $('#fileBrowse').val();
			data.ipPattern = '';
		}
		  if($("#authGroupBtn .active").data("value")==0){
			data.password = $('#password').val();
			data.privateKey='';
		}else{
			data.privateKey = uploadPathSharedKey;
			data.password = '';
		}
		data["@class"] = "com.impetus.ankush.oraclenosql.OracleNoSQLClusterConf";
		data.oracleNoSQLConf.datacenterName = $.trim($('#dataCenter').val());
		data.oracleNoSQLConf.topologyName = $.trim($('#topology').val());
		data.oracleNoSQLConf.replicationFactor = $.trim($('#repFactor').val());
		data.oracleNoSQLConf.partitionCount = $.trim($('#partitions').val());
		data.oracleNoSQLConf.registryPort = $.trim($('#registryPort').val());
		data.oracleNoSQLConf.haPortRangeStart = $.trim($('#haPort1').val());
		data.oracleNoSQLConf.haPortRangeEnd = $.trim($('#haPort2').val());
		data.oracleNoSQLConf.installationPath = $.trim($('#installationPath').val());
		data.oracleNoSQLConf.dataPath = $.trim($('#dataPath').val());
		data.oracleNoSQLConf.ntpServer = $.trim($('#ntpServer').val());
		data.oracleNoSQLConf.basePath = $.trim($('#baseDir').val());
		var component=$('#selectDBPackage').val().split('kv-ee-')[1];
		component=component.split('.');
		data.oracleNoSQLConf.componentVersion = component[0]+'.'+component[1]+'.'+component[2];
		data.oracleNoSQLConf.serverTarballLocation=repoPath+$('#selectDBPackage').val();
		if(nodeStatus==null){
			nodeStatus=defaultValuesError;
		}
		for ( var i = 0; i < nodeStatus.nodes.length; i++) {
			if ($("#nodeCheck" + i).is(':checked')) {
				var haPort = new Array();
				var storageDirs={};
				haPort.push(parseInt($('#haPort1' + i).text()));
				haPort.push(parseInt($('#haPort2' + i).text()));
				if($("#storageDirs" + i).text().trim()=='Empty'){
					storageDirs='';
				}else{
					storageDirs= $("#storageDirs" + i).text().trim();
				}
				if ($("#adminCheckBox" + i).is(':checked')) {
					nodeArray = {
					"publicIp" : nodeStatus.nodes[i][0],
					"privateIp" : nodeStatus.nodes[i][0],
					"admin" : true,
					"os" : nodeStatus.nodes[i][4],
					"adminPort" : $("#adminPort" + i).text().trim(),
					"registryPort" : $("#registryPort" + i).text().trim(),
					"haPortRangeStart" : $("#haPort1" + i).text().trim(),
					"haPortRangeEnd" : $("#haPort2" + i).text().trim(),
					"memoryMb" : $("#memory" + i).text().trim(),
					"capacity" : $("#capacity" + i).text().trim(),
					"cpuNum" : $("#cpu" + i).text().trim(),
					"storageDirs" : storageDirs,
					"datacenter" :$("#datacenter" + i).text().trim(),
				//	"rack" :$("#rack" + i).text().trim(),
					};
				}else{
					nodeArray = {
					"publicIp" :  nodeStatus.nodes[i][0],
					"privateIp" : nodeStatus.nodes[i][0],
					"admin" : false,
					"os" : nodeStatus.nodes[i][4],
					"registryPort" : $("#registryPort" + i).text().trim(),
					"haPortRangeStart" : $("#haPort1" + i).text().trim(),
					"haPortRangeEnd" : $("#haPort2" + i).text().trim(),
					"memoryMb" : $("#memory" + i).text().trim(),
					"capacity" : $("#capacity" + i).text().trim(),
					"cpuNum" : $("#cpu" + i).text().trim(),
					"storageDirs" : storageDirs,
					"datacenter" :$("#datacenter" + i).text().trim(),
				//	"rack" :$("#rack" + i).text().trim(),
					};
				}
				data.oracleNoSQLConf.nodes.push(nodeArray);
			}
		}
		var clusterId=$('#clusterDeploy').val();
		var deployUrl = baseUrl + '/cluster/create/Oracle NoSQL Database';
		if(clusterId!=''){
			deployUrl = baseUrl + '/cluster/redeploy/'+clusterId;
		}
		$('#clusterDeploy').button();
        $('#clusterDeploy').button('loading');
		$.ajax({
			'type' : 'POST',
			'url' : deployUrl,
			'data' : JSON.stringify(data),
			'contentType' : 'application/json',
			'dataType' : 'json',
			'async' : true,
			'success' : function(result) {
				$('#clusterDeploy').button('reset');
				if (result.output.status == false) {
					$("#errorDivMain").show().html('');
					if(result.output.error.length > 1){
						$('#validateError').show().html(result.output.error.length+ ' Errors');
					}
					else{
						$('#validateError').show().html(result.output.error.length+ ' Error');
					}
					for ( var i = 0; i < result.output.error.length; i++) {
						$("#errorDivMain").append("<div class='errorLineDiv'><a href='#' >"+(i+1)+". "+result.output.error[i]+"</a></div>");
					}
				}else{
					$("#errorDivMain").hide();
					$('#validateError').hide();
					com.impetus.ankush.oClusterSetup.loadDashboard();
				}
			},
			'error' : function() {
				$('#clusterDeploy').button('reset');
				com.impetus.ankush.oClusterSetup.loadDashboard();
			}
		});
	},
};
	     /*         $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");*/
