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

var parameterTable = null;
var parameters = null;
var deleteText="Parameter will be permanently Deleted.";
var successText = "Request has been placed successfully";
var currentClusterId = null;

com.impetus.ankush.hadoopParameters={

		// function used to initialize parameters table and load data in it
		initializeParameterTable : function(clusterId){
			currentClusterId = clusterId;
			var filenameid=null;
			if(parameterTable == null){
				$("#parameterTable").dataTable().fnDestroy();
				parameterTable = $('#parameterTable').dataTable({
					"aoColumns": [null,null,null,null,null,null],
					              "bJQueryUI" : false,
					              "bPaginate" : false,
					              "bLengthChange" : false,
					              "bFilter" : true,
					              "bSort" : false,
					              "bInfo" : false ,
					              "sPaginationType": "full_numbers",
					              "bAutoWidth" : false, 
					              "bRetrieve" : true,
					              "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ){
					            	  $(nRow).attr('id','parameter-'+iDisplayIndex);
					              }
				}).rowGrouping({bExpandableGrouping: true,
					iGroupingColumnIndex : 1});
				$('#parameterTable_filter').hide();
				$('#searchParametersTableHadoop').keyup(function(){
					$("#parameterTable").dataTable().fnFilter( $(this).val() );
				});
				$("#div_addParameter").appendTo('body');
				$("#deleteParameterDialogHadoop").appendTo('body');
			}else{
				parameterTable.fnClearTable();
			}
			$('#parameterTable_filter').hide();
			parameterTable = $('#parameterTable').dataTable();
			var url = null;
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				url = baseUrl + '/monitor/' + clusterId + '/listconfparam?technology=Hadoop';
			else
				url = baseUrl + '/monitor/'+ currentClusterId + '/listconfparam';
			parameters = com.impetus.ankush.placeAjaxCall(url,"GET",false);
			
			if(!parameters.output.status){
				com.impetus.ankush.validation.showAjaxCallErrors(parameters.output.error, 
						'popover-content-hadoopParameters', 'error-div-hadoopParameters', 'errorBtnHadoopParameters');
				$("#applyParameters").attr('disabled', true);
			}
			
			for(var fileName in parameters.output){
				filenameid = fileName.split('.').join('_');
				if(!(fileName=="status"||fileName=="error")){
					var k=parameters.output[fileName].length-1;
					for(var i=0;i<parameters.output[fileName].length;i++){
						parameterTable.fnAddData([
						                          '',
						                          fileName,
						                          '<span id="parameterName-'+i+'-'+filenameid+'">'+parameters.output[fileName][i].name+'</span>',
						                          '<span class="editableLabel" id="parameterValue-'
						                          + i+'-'+filenameid+ '">'
						                          + parameters.output[fileName][i].value
						                          + '</span>',
						                          '<div><a class="deleteParameter" href="#"><input  style="width:22px;height:22px;" type="image" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.hadoopParameters.deleteParameters(\''+i+'\',\''+filenameid+'\')"></input></a></div>',
						                          '<div><a href="#" ><a  class="delete" id="status-'+i+'-'+filenameid+'" style="display: none;">none</a></div>'                        									
						                          ]);
					}
				}
			}
			$("#fileNameValue").empty();
			for(var filename in parameters.output){

				if(!(filename=="status"||filename=="error")){
					filenameid = filename.split('.').join('_');
					$("#fileNameValue").append("<option id='" + filenameid + "'>" + filename + "</option>");
				}
			}
			$('.editableLabel').editable({
				type : 'text',
				validate :function(){return com.impetus.ankush.hadoopParameters.updateStatus(this);}
			});
		},

		// function used to show delete parameter dialog
		deleteParameters : function(i,filenameid) {
			$('#parameterTable .deleteParameter').live('click', function (e) {
				nRow= $(this).parents('tr')[0];
			} );
			$("#deleteParameterDialogHadoop").modal('show');
			$('.ui-dialog-titlebar').hide();
			$("#confirmDeleteParameterHadoop").click(function() {
				com.impetus.ankush.hadoopParameters.deleteParameterHadoop(i,filenameid,nRow);
				$('#deleteParameterDialogHadoop').modal('hide');
			});
		},

		// function used to delete parameters
		deleteParameterHadoop : function(i,filenameid,nRow) {
			$('#status-'+i +'-'+filenameid).empty().append('delete');
			var filename=filenameid.split('_').join('.');
			parameters.output[filename][i].status=$('#status-'+i +'-'+filenameid).text();
			$(nRow).hide();
		},

		// function used to update status of parameter viz, edit, add, etc.
		updateStatus : function(elem) {
			var id = elem.id;
			id = id.split('-');
			var k=id.length-1;
			var addId = "";
			var statusId=id[1];
			for(var i=1;i<id.length;i++){
				addId += id[i];
				if(i!=k){
					addId+='-';
				}
			}
			if($('#status-'+addId).text()=='add'){
				$('#status-'+addId).text('add');
			}else if($('#status-'+addId).text()=='none'){
				$('#status-'+addId).text('edit');
			}
			var modifiedFileName="";
			for(var j=2;j<id.length;j++){
				modifiedFileName += id[j];
				if(j!=k){
					modifiedFileName+='-';
				}
			}
			var filename = modifiedFileName.split('_').join('.');
			parameters.output[filename][statusId].status=$('#status-'+addId).text();
		},

		// function used to show add parameter dialog
		addParameterDialog : function() {
			com.impetus.ankush.hadoopParameters.editUpdateParameterTable();
			$("#div_addParameter").modal('show');
			$('.ui-dialog-titlebar').hide();
			com.impetus.ankush.hadoopParameters.resetAddParameterField("newParameterName", "Parameter Name");
			com.impetus.ankush.hadoopParameters.resetAddParameterField("newParameterValue", "Parameter Value");
		},

		// function used to reset Add Parameter Dialog Field
		resetAddParameterField : function(fieldId,fieldName) {
			$('#'+fieldId).val('');
			$('#'+fieldId).removeClass('error-box');
			com.impetus.ankush.common.tooltipOriginal(fieldId,'Enter ' + fieldName);
		},

		// function used to validate a new parameter added 
		validateNewParameter : function() {
			com.impetus.ankush.validation.errorCount = 0;
			com.impetus.ankush.validation.validateField('', 'newParameterName', 'Parameter Name', '');
			com.impetus.ankush.validation.validateField('', 'newParameterValue', 'Parameter Value', '');
		},

		// function used to add a new parameter to a table
		addData : function() {
			com.impetus.ankush.hadoopParameters.validateNewParameter();
			if(com.impetus.ankush.validation.errorCount > 0) {
				return;
			}
			
			$('#newParameterName').val($.trim($('#newParameterName').val()));
			$('#newParameterValue').val($.trim($('#newParameterValue').val()));
			$("#div_addParameter").modal('hide');
			var parametersValue=null;
			var fileName = $('#fileNameValue').val();
			parametersValue={
					"name": $('#newParameterName').val(),
					"value": $('#newParameterValue').val(),
					"status": "add"
			};
			parameters.output[fileName].push(parametersValue);
			parameterTable.fnClearTable();
			for(var fileName in parameters.output){
				filenameid = fileName.split('.').join('_');
				if(!(fileName=="status"||fileName=="error")){
					var k=parameters.output[fileName].length-1;
					for(var i=0;i<parameters.output[fileName].length;i++){
						if(i==k){
							addImage='<div><a class="deleteParameter" href="#"><input  style="width:22px;height:22px;" type="image" src="'+baseUrl + '/public/images/newUI-Icons/circle-plus.png" onclick="com.impetus.ankush.hadoopParameters.addRow(\''+filenameid+'\')"></input></a></div>';
						}else{
							addImage='';
						}
						if(parameters.output[fileName][i].status!='delete'){
							parameterTable.fnAddData([
							                          '',
							                          fileName,
							                          '<span id="parameterName-'+i+'-'+filenameid+'">'+parameters.output[fileName][i].name+'</span>',
							                          '<span class="editableLabel" id="parameterValue-'
							                          + i+'-'+filenameid+ '">'
							                          + parameters.output[fileName][i].value
							                          + '</span>',
							                          '<div><a class="deleteParameter" href="#"><input  style="width:22px;height:22px;" type="image" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" onclick="com.impetus.ankush.hadoopParameters.deleteParameters(\''+i+'\',\''+filenameid+'\')"></input></a></div>',
							                          '<div><a href="#" ><a  class="delete" id="status-'+i+'-'+filenameid+'" style="display:none;">'+parameters.output[fileName][i].status+'</a></div>'                        									
							                          ]);
						}
					}
				}
			}
			$('.editableLabel').editable({
				type : 'text',
				validate :function(){return com.impetus.ankush.hadoopParameters.updateStatus(this);}
			});
		},

		// function used to update parameter table when a parameter is edited , added or deleted
		editUpdateParameterTable : function() {
			var filenameid=null;
			for(var fileName in parameters.output){
				filenameid = fileName.split('.').join('_');
				if(!(fileName=="status"||fileName=="error")){
					for(var i=0;i<parameters.output[fileName].length;i++){
						if($("#status-"+i+'-'+filenameid).text()=='edit'){
							parameters.output[fileName][i].value=$('#parameterValue-'+i+'-'+filenameid).text();
						}
					}
				}
			}
		},

		// function used to prepare parameters POST data
		postParameterData : function() {
			com.impetus.ankush.hadoopParameters.editUpdateParameterTable();
			var data={};
			var filenameid=null;
			var dataToSend ={};
			for(var fileName in parameters.output){
				var fileParameter = [];
				filenameid = fileName.split('.').join('_');
				if(parameters.output[fileName]!="status"){
					for(var i=0;i<parameters.output[fileName].length;i++){
						if($("#status-"+i+'-'+filenameid).text()!='none'){
							fileParameter.push(parameters.output[fileName][i]);
						}
					}
					dataToSend[fileName] = fileParameter;
				}
			}
			data.params = dataToSend ;
			console.log('final data- ' + data);
			com.impetus.ankush.hadoopParameters.parameterPostCall(data);
		},

		// function used to post parameters data
		parameterPostCall : function(data) {
			var url = null;
			if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
				url = baseUrl + '/monitor/' + currentClusterId + '/editconfparams?technology=Hadoop';
			else
				url = baseUrl + '/monitor/' + currentClusterId + '/editconfparams';
			$.ajax({
				'type' : 'POST',
				'url' : url,
				'contentType' : 'application/json',
				'dataType' : 'json',
				'data':JSON.stringify(data),
				'success' : function(result){
					com.impetus.ankush.removeCurrentChild();
				},
				'error':function(result){
				}
			});
		}
};

$(document).ready(function() { 
	$('input.input-medium').live("blur", function(){
		var paramStatusTxt = $(this).parents('tr').find("a.delete").text();
		if (paramStatusTxt == 'none'){
			$(this).parents('tr').find("a.delete").text('edit');
		}    
	}); 
});
