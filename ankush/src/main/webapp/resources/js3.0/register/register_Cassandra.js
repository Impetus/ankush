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
var cassandraObjReg={
		Defaults:{},
};
com.impetus.ankush.register_Cassandra={
		tooltipInitialize:function(){
			$('#vendorDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.vendor);
			$('#versionDropdown').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.version);
			$('#componentHome').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.componentHome);
			$('#jmxPort').tooltip().attr('data-original-title', com.impetus.ankush.tooltip.cassandraClusterCreation.jmxPort);
		},
		cassandraConfigPopulate:function(){
			com.impetus.ankush.register_Cassandra.tooltipInitialize();
			$('#vendorDropdown').html('');
			$('#versionDropdown').html('');
			var cassandraData=jsonDataHybrid.hybrid.Cassandra;
			for ( var key in cassandraData.Vendors){
				$("#vendorDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			for ( var key in cassandraData.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
			if(Object.keys(cassandraObj.Defaults).length > 0){
				cassandraData.Defaults.componentHome=cassandraObj.Defaults.componentHome;
				cassandraData.Defaults.jmxPort=cassandraObj.Defaults.jmxPort;
				$("#vendorDropdown").val(cassandraObj.vendor);
				$('#versionDropdown').html('');
				for ( var key in cassandraData.Vendors[$("#vendorDropdown").val()]){
					$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
				}
				$("#versionDropdown").val(cassandraObj.version);
			}
			$('#componentHome').val(cassandraData.Defaults.componentHome);
			$("#jmxPort").val(cassandraData.Defaults.jmxPort);
		},
		
		
/*Function for changing version and download path on change of Cassandra vendor dropdown*/
		vendorOnChangeCassandra:function(){
			$("#versionDropdown").html('');
			for ( var key in jsonDataHybrid.hybrid.Cassandra.Vendors[$("#vendorDropdown").val()]){
				$("#versionDropdown").append("<option value=\"" + key + "\">" + key + "</option>");
			}
		},
		cassandraConfigValidate:function(){
			$('#validateErrorCassandra').html('').hide();
	        var errorMsg = '';
	        $("#errorDivMainCassandra").html('').hide();
	        errorCount = 0;
	        if (!com.impetus.ankush.validation.empty($('#vendorDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No cassandra vendor selected';
  	            var divId='vendorDropdown';
  	            $("#errorDivMainCassandra").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
	            $('#vendorDropdown').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#versionDropdown').val())) {
  	            errorCount++;
  	            errorMsg = 'No cassandra version selected';
  	            var divId='versionDropdown';
  	            $("#errorDivMainCassandra").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
	            $('#versionDropdown').removeClass('error-box');
	        }
	        if (!com.impetus.ankush.validation.empty($('#componentHome').val())) {
  	            errorCount++;
  	            errorMsg = 'Component Home field Empty';
  	            com.impetus.ankush.common.tooltipMsgChange('componentHome','Component Home cannot be empty');
  	            var divId='componentHome';
  	            $("#errorDivMainCassandra").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
  	        }else {
	            com.impetus.ankush.common.tooltipOriginal('componentHome',com.impetus.ankush.tooltip.cassandraClusterCreation.componentHome);
	        }
	      
	        if(!com.impetus.ankush.validation.empty($('#jmxPort').val())){
                errorCount++;
                errorMsg = 'JMX Port field empty.';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port cannot be empty');
                var divId='jmxPort';
                $("#errorDivMainCassandra").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")' >"+errorCount+". "+errorMsg+"</a></div>");
            }else if (!com.impetus.ankush.validation.numeric($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be numeric';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be numeric');
                var divId='jmxPort';
                $("#errorDivMainCassandra").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else if (!com.impetus.ankush.validation.oPort($('#jmxPort').val())) {
                errorCount++;
                errorMsg = 'JMX Port field must be between 1024-65535';
                com.impetus.ankush.common.tooltipMsgChange('jmxPort','JMX Port must be between 1024-65535');
                var divId='jmxPort';
                $("#errorDivMainCassandra").append("<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.common.focusDiv(\""+divId+"\")'  >"+errorCount+". "+errorMsg+"</a></div>");
            } else{
                com.impetus.ankush.common.tooltipOriginal('jmxPort',com.impetus.ankush.tooltip.cassandraClusterCreation.jmxPort);
            }
	      
	        if(errorCount>0 && errorMsg!=''){
	        	 $('#validateErrorCassandra').show().html(errorCount + ' Error');
	        	  $("#errorDivMainCassandra").show();
	        }else{
	        	 com.impetus.ankush.register_Cassandra.cassandraConfigApply();
	        } 
		},
		cassandraConfigApply:function(){
			cassandraObj.vendor=$('#vendorDropdown').val();
			cassandraObj.version=$('#versionDropdown').val();
			cassandraObj.Defaults.componentHome=$('#componentHome').val();
			cassandraObj.Defaults.jmxPort=$('#jmxPort').val();
			cassandraObj.Defaults.deployComponentFlag=false;
			$('#confTypeCassandra').text('Custom');
			$("#confPageCassandra").removeClass("errorBtn defaultBtn");
			com.impetus.ankush.removeChild($.data(document, "panels").children.length);      
		},
		
		cassandraNodesPopulate:function(){
			if(cassandraNodeTable!=null){
				cassandraNodeTable.fnClearTable();
			}
			if(nodeStatus==null){
				return;
			}
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
				var addId = null;
					addId = cassandraNodeTable.fnAddData([
											nodeStatus.nodes[i][0],
											'<span class="" id="nodeRoleCassandra'+i+'">'+$('#nodeRole' + i).text()+'</span>',
											'<input id="cassandraNodeCheck'
												+ i
												+ '" class="cassandraNodeCheckBox" name="cassandraNodeRadio" style="margin-right:10px;" type="radio" onclick="">',
											nodeStatus.nodes[i][4],
											'<div><a href="##" onclick="com.impetus.ankush.register_Cassandra.loadCassandraNodeDetail(this);"><img id="navigationImg-'
												+ i
												+ '" src="'
												+ baseUrl
												+ '/public/images/icon-chevron-right.png" /></a></div>' ]);
					
				var theNode = cassandraNodeTable.fnSettings().aoData[addId[0]].nTr;
				theNode.setAttribute('id', 'cassandra'+ nodeStatus.nodes[i][0].split('.').join('_'));
				if (nodeStatus.nodes[i][1] == false	|| nodeStatus.nodes[i][2] == false|| nodeStatus.nodes[i][3] == false) {
					rowId = nodeStatus.nodes[i][0].split('.').join('_');
					$('td', $('#cassandra'+rowId)).addClass('error-row');
					$('#cassandra' + rowId).addClass('error-row');
					$('#cassandraNodeCheck' + i).attr('disabled', true);
				}
			}
			if(Object.keys(cassandraNodesObjReg).length>0){
				for(var j=0;j<nodeStatus.nodes.length;j++){
	     			   if(cassandraNodesObjReg.publicIp==nodeStatus.nodes[j][0]){
	     				   $('#cassandraNodeCheck'+j).attr('checked',true); 
	     				   break;
	     			   }
					}
			}
		},
		loadCassandraNodeDetail:function(elem){
			$('#content-panel').sectionSlider('addChildPanel', {
				current : 'login-panel',
				url : baseUrl + '/hybrid-cluster/nodeMapNodeDetail',
				method : 'get',
				title : 'Node Detail',
				callback : function(data) {
					com.impetus.ankush.register_Cassandra.cassandraNodeDetail(elem);
				},
				callbackData : {
				}
			});
		},
		cassandraNodeDetail:function(elem){
			var rowIndex=$(elem).closest('td').parent()[0].sectionRowIndex;
			var aData= cassandraNodeTable.fnGetData(rowIndex);
			
			$('#nodeHead').html($('td:first', $(elem).parents('tr')).text());
			$('#nodeType').html($("td:nth-child(1)", $(elem).parents('tr')).text());
			$('#os').html(aData[aData.length-2]);
		
			if(nodeStatus.nodes[rowIndex][1]==false){
				$('#nodeStatus').html('').text('Unavailable');
			}else if(nodeStatus.nodes[rowIndex][2]==false){
				$('#nodeStatus').html('').text('Unreachable');
			}else if(nodeStatus.nodes[rowIndex][3]==false){
				$('#nodeStatus').html('').text('Unauthenticated');
			}else{
				$('#nodeStatus').html('').text('Available');
			}
			if(Object.keys(cassandraNodeMap).length > 0){
				var cassandraNodeDetails = cassandraNodeMap[$('td:first', $(elem).parents('tr')).text()];
				if(cassandraNodeDetails.message !=undefined){
					$('#nodeStatus').html('').text(cassandraNodeDetails.message);	
				}
				if(cassandraNodeDetails.errors!=undefined && Object.keys(cassandraNodeDetails.errors).length>0){
					for(var key in cassandraNodeDetails.errors){
          				$('#nodeDeploymentError').append('<label class="text-left" style="color: black;" id="'+key+'">'+cassandraNodeDetails.errors[key]+'</label>');
          	        }
					$('#errorNodeDiv').show();
				}
			} 
		},
		cassandraNodesPopulateApplyValidate:function(){
	    	errorCount=0;
	    	errorMsg='';
	    	 $("#errorDivCassandraNodes").html('');
	            $('#validateErrorCassandra').html('');
	    	if(nodeStatus!=null){
    	            if ($('#cassandraNodeTable .cassandraNodeCheckBox:checked').length < 1) {
    	            	errorMsg = 'Select at least one node.';
    	                errorCount++;
    	                $("#errorDivCassandraNodes").append("<div class='errorLineDiv'><a href='#cassandraNodeTable'  >"+errorCount+". "+errorMsg+"</a></div>");
    	            }
	    		 if (errorCount > 0) {
	    	            $("#errorDivCassandraNodes").show();
	    	               if(errorCount > 1)
	    	                    $('#validateErrorCassandra').text(errorCount + " Errors");
	    	                else
	    	                    $('#validateErrorCassandra').text(errorCount + " Error");
	    	            $('#validateErrorCassandra').show().html(errorCount + ' Error');
	    	        } else {
	    	            $("#errorDivCassandraNodes").hide();
	    	            $('#validateErrorCassandra').hide();
	    	            com.impetus.ankush.register_Cassandra.cassandraNodesPopulateApply();
	    	        }
	    	}
	    },
		
		cassandraNodesPopulateApply:function(){
			cassandraNodeMap={}; 	
			cassandraNodesObjReg={};
			for ( var k = 0; k < nodeRoleArray.length; k++) {
				nodeRoleArray[k].role.Cassandra=0;
			}
				for ( var i = 0; i < nodeStatus.nodes.length; i++) {
					var cassandraNode={};
					if($("#cassandraNodeCheck"+i).attr('checked')) {
						cassandraNode.publicIp=nodeStatus.nodes[i][0];
						cassandraNode.privateIp=nodeStatus.nodes[i][0];
						cassandraNodesObjReg=cassandraNode;
					}
				}
			com.impetus.ankush.removeChild($.data(document, "panels").children.length);
			for ( var i = 0; i < nodeStatus.nodes.length; i++) {
					if(cassandraNodesObjReg.publicIp==nodeStatus.nodes[i][0]){
						nodeRoleArray[i].role.Cassandra=1;
						cassandraNodeMap[cassandraNodesObjReg.publicIp]=cassandraNodesObj.nodes;
					}
			}
			$("#nodeMapCassandra").removeClass("errorBtn");
			$('#nodeCountCassandra').text(Object.keys(cassandraNodeMap).length);
			com.impetus.ankush.hybridClusterCreation.nodeRoleMap('nodeRole');
		},	
		
};
