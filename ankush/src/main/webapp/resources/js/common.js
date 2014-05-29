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

var oNodeTable=null;
var uploadFilePath=null;
var uploadPathSharedKey=null;
var nodeStatus = null;
var disableNodeCount = 0;
com.impetus.ankush.common={
/*Function for populating default values*/
		populateDefaultValues:function(){
			
		},
		
/*Function For Changing css tooltip in case of error*/
		
	    tooltipMsgChange : function(id, msg) {
	        $("#" + id).hover(function() {
	            $('.tooltip-inner').css({
	                'background-color' : 'white',
	                'color' : 'black',
	                'border' : "1px solid #EF3024",
	                '-webkit-border-radius' : ' 4px',
	                '-moz-border-radius' : '4px',
	                'border-radius' : '4px',
	                '-webkit-box-shadow' : '3px 3px 3px 3px #888888',
	                '-moz-box-shadow' : ' 3px 3px 3px 3px #888888',
	                'box-shadow' : '3px 3px 3px 3px #888888',
	            });
	            $('.tooltip.right').css('border-right-color', '#EF3024');
	            $(".tooltip.right .tooltip-arrow").css({
	                "top" : "50%",
	                "left" : "0",
	                "margin-top" : "-5px",
	                "border-right-color" : "#EF3024",
	                "border-width" : "5px 5px 5px 0"
	            });
	        });
	        $("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
	                'fixTitle');

	    },
	    
	/*Function for changing css of tootip to original*/
	    tooltipOriginal : function(id, msg ){
	        $("#" + id).hover(function() {
	            $('.tooltip-inner').css({
	              'max-width': '250px',
	              'padding': '8px',
	              'color': '#686A6C',
	              'text-align': 'center',
	              'text-decoration': 'none',
	              'border' : "1px solid #E7EFFF",
	              '-webkit-box-shadow' : '0px',
	              '-moz-box-shadow' : ' 0px;',
	              'box-shadow' : '0px;',
	              'background-color': 'green',
	              '-webkit-border-radius': '4px',
	              '-moz-border-radius': '4px',
	              'border-radius': '4px',
	              'font-family':'Franklin Gothic Book',
	              'font-size':'14px',
	            });
	            $('.tooltip.right').css('border-right-color', 'E7EFFF');
	            $(".tooltip.right .tooltip-arrow").css({
	                "top" : "50%",
	                "left" : "0",
	                "margin-top" : "-5px",
	                "border-right-color" : "#E7EFFF",
	                "border-width" : "5px 5px 5px 0"
	            });
	        });  
	        $("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
	        'fixTitle');
	    },
	    focusDiv:function(id){
	        $('#'+id).focus().tooltip('hide').addClass('error-box');
	    },
	    
		
		
		
		/*validateNodeRetrieve:function(){ 
		var userName = $.trim($('#inputUserName').val());
        var password = $('#inputPasswordName').val();
        var ipRange= $('#inputIpRange').val();
        var errorMsg = '';
        errorCount = 0;
        if(!com.impetus.ankush.validation.empty(userName)){
            errorCount++;
            errorMsg = 'Username field empty.';
            com.impetus.ankush.common.tooltipMsgChange('inputUserName','Username cannot be empty');
            flag = true;
            var divId='inputUserName';
            $("#errorDivMain").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");          
        }else{
            com.impetus.ankush.common.tooltipOriginal('inputUserName',' Enter Username.');
            $('#inputUserName').removeClass('error-box');
        }
        if(errorCount>0 && errorMsg!=''){
       	 $('#validateError').show().html(errorCount + ' Error');
       	  $("#errorDivMain").show();
       }
		
		},*/
		
/*Function for toggling between divs on radio button click*/
    toggleAuthenticate:function(radioName,div1,div2){
        if($('input:radio[name='+radioName+']:checked').val()==0){
            $('#'+div1).show();
            $('#'+div2).hide();
        }else{
            $('#'+div2).show();
            $('#'+div1).hide();
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
    
/*Function for retriving nodes and populate on node table*/
    getNodes : function() {
    	$('#errormainDiv').html('');
    	 var userName = $.trim($('#inputUserName').val());
         var password = $('#inputPassword').val();
         var nodeData = {};
         var errorNodeCount=0;
         if($('input[name=authentication]:checked').val()==1){
             nodeData.userName = userName;
             nodeData.password = password;
             nodeData.isFileUploaded = true;
             nodeData.nodePattern=uploadFilePath;
             if($('input[name=authenticationType]:checked').val()==0){
             	nodeData.authTypePassword=true;
             }else{
                  nodeData.authTypePassword=false;
                  nodeData.password = uploadPathSharedKey;
             }
         }else{
             nodeData.userName = userName;
             nodeData.password = password;
             nodeData.nodePattern =$.trim($('#inputIpRange').val());
             nodeData.isFileUploaded = false;
             if($('input[name=authenticationType]:checked').val()==0){
                 nodeData.authTypePassword=true;
             }else{
                  nodeData.authTypePassword=false;
                  nodeData.password = uploadPathSharedKey;
             }
         }
         var url = baseUrl + '/cluster/detectNodes';
         $("#nodeListDiv").show();
         // ajax call for the data populate.
         $.ajax({
             'type' : 'POST',
             'url' : url,
             'contentType' : 'application/json',
             'data' : JSON.stringify(nodeData),
             'async' : true,
             'dataType' : 'json',
             'success' : function(result) {
                 $('#retrieveNodeButton').button('reset');
                 uploadFileFlag=false;
                 nodeStatus = result.output;
                 if (oNodeTable != null) {
                     oNodeTable.fnClearTable();
                 }
                 nodeTableLength = nodeStatus.nodes.length;
                 console.log(nodeTableLength);
                 if($('input[name=authentication]:checked').val()==1){
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
                     if($('input[name=authentication]:checked').val()==1){
                     com.impetus.ankush.common.tooltipOriginal('filePath','Browse File.');
                     $('#filePath').removeClass('error-box');
                     }else{
                     com.impetus.ankush.common.tooltipOriginal('ipRange','Enter IP pattern.');
                     $('#ipRange').removeClass('error-box');
                     }
                     $("#errorDivMain").hide();
                     $('#validateError').hide();  
                     }
                 for (var i = 0; i < nodeStatus.nodes.length; i++) {
                	  var addId=null;
                	  var nodeState="Yes";
                		  if(nodeStatus.nodes[i][3]=='false'){
                			  nodeState="No";
                		  }
                         addId = oNodeTable.fnAddData([
                                         '<input type="checkbox" name="" value=""  id="nodeCheck'
                                                 + i
                                                 + '" class="nodeCheckBox" onclick="com.impetus.ankush.oClusterSetup.nodeCheckBox();"/>',
                                         nodeStatus.nodes[i][0],
                                         nodeStatus.nodes[i][4],
                                         nodeState,
                                         '<a href="##" onclick="com.impetus.ankush.oClusterSetup.loadNodeDetailOracle('
                                                 + i
                                                 + ');"><img id="navigationImg-'
                                                 + i
                                                 + '" src="'
                                                 + baseUrl
                                                 + '/public/images/icon-chevron-right.png" /></a>' ]);
                                              
                     var theNode = oNodeTable.fnSettings().aoData[addId[0]].nTr;
                     theNode.setAttribute('id', 'node'+ nodeStatus.nodes[i][0].split('.').join('_'));
                     console.log('1'+nodeStatus.nodes);
                     if (nodeStatus.nodes[i][1] != true
                             || nodeStatus.nodes[i][2] != true
                             || nodeStatus.nodes[i][3] != true) {
                         rowId = nodeStatus.nodes[i][0].split('.').join('_');
                         $('td', $('#node'+rowId)).addClass('error-row');
                         $('#node' + rowId).addClass('error-row');
                         $('#nodeCheck' + i).attr('disabled', true);
                         errorNodeCount++;
                         disableNodeCount++;
                     }else{
                         $('#nodeCheck'+i).prop("checked", true);
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
                     if(disableNodeCount != nodeTableLength){
                         $('#nodeCheckHead').prop("checked", true);
                     }
                     $('.editableLabel').editable({
                         type : 'text',
                     });
                 },
         error : function() {
             $('#retrieveNodeButton').button('reset');
         }
         });
    },
	
};
		

