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

//Parameter file for cassandra Node level parameters
var postNodeParameterObject = {};
com.impetus.ankush.nodeParameters = {
	//this function is called from parameters.jsp and populate page
	confParameterPopulate : function(nodeIndex){
		$('#fileNameValueAddNodeParam').empty();
		var parameterUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/nodeparams?ip='+com.impetus.ankush.commonMonitoring.nodesData.output.nodes[nodeIndex].publicIp;
		if(cassandraNodeParameterTable != null)
			cassandraNodeParameterTable.fnClearTable();
		$.ajax({
			'type' : 'GET',
			'url' : parameterUrl,
			'contentType' : 'application/json',
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				columnFamilyDetailData = result;
				if(result.output.status == true){
					if((result.output.params == undefined) || (result.output.params == null))
						return;
					for(var key in result.output.params){
							for(var prop in result.output.params[key]){
								var thirdColumn = null;
								if(result.output.params[key][prop][1]){
									thirdColumn = '<span class="edit-param-cassandra" id="'+key.replace('.','_')+'">'+result.output.params[key][prop][0]+'</span>';
									fourthColumn = '<a class="deleteParameter" href="#" onclick="com.impetus.ankush.nodeParameters.postParamObjDialog(this)" id="'+key.replace('.','_')+'"><input  style="width:22px;height:22px;" type="image" src="'+baseUrl+'/public/images/newUI-Icons/circle-minus.png" ></input></a>';
								}
								else{
									thirdColumn = '<span class="">'+result.output.params[key][prop][0]+'</span>';
									fourthColumn = "";
								}
								cassandraNodeParameterTable.fnAddData([
								                                        key,'',
								                                        prop,	
								                                        thirdColumn,
								                                        fourthColumn
								                                        ]);
							}
							postNodeParameterObject[key] = [];		
							$('#fileNameValueAddNodeParam').append('<option value="'+key+'">'+key+'</option>');
					}
					
				}else{
					$('#error-div-cassandraParameters').show();
					$('#error-div-cassandraParameters').html(result.output.error[0]);
					$('#addNodeParameters').addClass('disabled');
					var addNodeParameters = document.getElementById('addNodeParameters');
					addNodeParameters.onclick = '';
					$('#applyParameters').addClass('disabled');
					var applyParameters = document.getElementById('applyParameters');
					applyParameters.onclick = '';
					cassandraNodeParameterTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
					cassandraNodeParameterTable.fnClearTable();
				}
				$('.edit-param-cassandra').editable({
					validate :function(){return com.impetus.ankush.nodeParameters.postParamObj(this,'edit');}
				});
			},
			'error' : function(){
			}
			
		});
	},
	//this function makes object has to send in edit parameter post data
	postParamObj : function(elem,status){
		var object = {};
		if(status == 'edit'){
			var textElement = $(elem).next().children().first().children().first()
				.next().children().first().children().first().children().first().children().first();
			object.name = $(elem).parent().prev().text();
			object.value = textElement.val();
		}
		if(status == 'delete'){
			object.name = $(elem).parent().prev().prev().text();
			object.value = $(elem).parent().prev().children().first().text();
			$(elem).parent().parent().hide();
			$('#deleteParameterDialogCassandra').modal('hide');
			$('#confirmDeleteParameterCassandra').unbind('click');
		}
		if(status == 'add'){
			if(!com.impetus.ankush.validate.empty($('#newParameterName').val()) && !com.impetus.ankush.validate.empty($('#newParameterValue').val())){
				$('#newParameterName').addClass('error-box');
				$('#newParameterValue').addClass('error-box');
				return ;
			}
			if(!com.impetus.ankush.validate.empty($('#newParameterName').val())){
				$('#newParameterName').addClass('error-box');
				$('#newParameterValue').removeClass('error-box');
				return ;
			}
			if(!com.impetus.ankush.validate.empty($('#newParameterValue').val())){
				$('#newParameterName').removeClass('error-box');
				$('#newParameterValue').addClass('error-box');
				return ;
			}
			object.name = $('#newParameterName').val();
			object.value = $('#newParameterValue').val();
			$('#div_addParameter').modal('hide');
			$('#addParamCassandra').unbind('click');
			$('#newParameterName').val('');
			$('#newParameterValue').val('');
		}
		object.status = status;
		if(status != 'add')
			postNodeParameterObject[(elem.id).replace('_','.')].push(object);
		else
			postNodeParameterObject[elem].push(object);
		console.log(postNodeParameterObject);
	},
	//this function will open dialog for parameter object
	postParamObjDialog : function(elem){
		$('#deleteParameterDialogCassandra').appendTo('body').modal();
		$('#confirmDeleteParameterCassandra').bind('click',function(){ com.impetus.ankush.nodeParameters.postParamObj(elem,'delete'); });
	},
	//this function will open dialog to add parameter
	addParamDialog : function(){
		$('#div_addParameter').appendTo('body').modal();
		$('#newParameterName').removeClass('error-box');
		$('#newParameterValue').removeClass('error-box');
		$('#addParamCassandra').bind('click',function(){ com.impetus.ankush.nodeParameters.postParamObj($('#fileNameValueAddNodeParam').val(),'add'); });
	},
	//this function will do necessary things on page removal
	removePage : function(){
		$('#div_addParameter').remove();
		$('#deleteParameterDialogCassandra').remove();
	},
	//this function will post data for edit parameters
	applyChangesPostParamObj : function(){
		$("#applyParameters").button('loading');
		var editParameterUrl =  baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/editparams';
		var data = {};
		data.params = {};
		data.params = postNodeParameterObject;
		data.ip = com.impetus.ankush.commonMonitoring.nodesData.output.nodes[com.impetus.ankush.commonMonitoring.nodeIndexForAutoRefresh].publicIp;
		$.ajax({
			'type' : 'POST',
			'url' : editParameterUrl,
			'contentType' : 'application/json',
			'data' : JSON.stringify(data),
			'async' : true,
			'dataType' : 'json',
			'success' : function(result) {
				$("#applyParameters").button('reset');
				postNodeParameterObject = {};
				if(result.output.status == true){
					com.impetus.ankush.removeCurrentChild();
				}else{
					$('#errorBtnCassandraParameters').show();
					$('#errorBtnCassandraParameters').text('Error');
					$('#error-div-cassandraParameters').show();
					$('#popover-content-cassandraParameters').html(result.output.error[0]);
				}
			},
			'error' : function(){}
		});
	}
};
