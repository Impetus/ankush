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
//Parameter file for cassandra parameters
var postParameterObject = {};
com.impetus.ankush.configurationParam = {
		//this function is called from parameters.jsp and populate page
		confParameterPopulate : function(){
			$('#fileNameValueAddParam').empty();
			var parameterUrl =  null;
			
			if(clusterTechnology == 'Hybrid'){
				parameterUrl = baseUrl+'/monitor/'+clusterId+'/params?component='+hybridTechnology;
			}
			else
			{
				parameterUrl = baseUrl+'/monitor/'+clusterId+'/params';
			}
			if(cassandraParameterTable != null)
				cassandraParameterTable.fnClearTable();
			$("#showLoading").removeClass('element-hide');
			 com.impetus.ankush.placeAjaxCall(parameterUrl, "GET", true, null, function(result) {
				 $("#showLoading").addClass('element-hide');
					columnFamilyDetailData = result;
					if(result.output.status == true){
						var testAllFileBlankFlag = false;
						if((result.output.params == undefined) || (result.output.params == null) || (Object.keys(result.output.params).length == 0)){
							cassandraParameterTable.fnSettings().oLanguage.sEmptyTable = "No parameters available.";
							cassandraParameterTable.fnClearTable();
							return;
						}
						for(var key in result.output.params){
							if(Object.keys(result.output.params[key]).length != 0){
								testAllFileBlankFlag = true;
								break;
							}
						}
						if(testAllFileBlankFlag === false){
							cassandraParameterTable.fnSettings().oLanguage.sEmptyTable = "No parameters available.";
							cassandraParameterTable.fnClearTable();
							return;
						}
						for(var key in result.output.params){
								for(var prop in result.output.params[key]){
									var thirdColumn = null;
									if(result.output.params[key][prop][1]){
										thirdColumn = '<span class="edit-param-cassandra" id="'+key.replace('.','_')+'">'+result.output.params[key][prop][0]+'</span>';
										fourthColumn = '<a class="deleteParameter" href="#" onclick="com.impetus.ankush.configurationParam.postParamObjDialog(this,\'DELETE\')" id="'+key.replace('.','_')+'"><ins></ins></a>';
									}
									else{
										thirdColumn = '<span class="">'+result.output.params[key][prop][0]+'</span>';
										fourthColumn = '';
									}
									if(result.output.paramsMetadata){
										if(result.output.paramsMetadata[key]){
											if(result.output.paramsMetadata[key][prop]){
												if(result.output.paramsMetadata[key][prop]['values']){
													if(Object.keys(result.output.paramsMetadata[key][prop]['values']).length != 0){
														var options = '';
														for(var value in result.output.paramsMetadata[key][prop]['values']){
															options += ('<option value="'+result.output.paramsMetadata[key][prop]['values'][value]+'">'+value+'</option>');
														}
														thirdColumn = '<select  class="'+prop+'" style="color:#000000" onchange="com.impetus.ankush.configurationParam.postParamObj(this,\'editDrop\');" id="'+key.replace('.','_')+'">'+result.output.params[key][prop][0]+'>'+options+'</select>';
													}
												}
											}	
										}
									}
									cassandraParameterTable.fnAddData([
									                                        '<ins class="left"></ins><div class="left">&nbsp;&nbsp;&nbsp;&nbsp;'+key+'</div>','',
									                                        prop,	
									                                        thirdColumn,
									                                        fourthColumn
									                                        ]);
									if(result.output.paramsMetadata){
										if(result.output.paramsMetadata[key]){
											if(result.output.paramsMetadata[key][prop]){
												$('#'+key.replace('.','_')+'.'+prop).val(result.output.params[key][prop][0]);
											}
										}
									}
								}
								postParameterObject[key] = [];		
								$('#fileNameValueAddParam').append('<option value="'+key+'">'+key+'</option>');
								
						}
						
					}else{
						com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content', 'error-div', '');
						$("#applyParameters").attr('disabled', true);
						$("#addParameters").attr('disabled', true);
						cassandraParameterTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						cassandraParameterTable.fnClearTable();
					}
					$('.edit-param-cassandra').editable({
						validate :function(){return com.impetus.ankush.configurationParam.postParamObj(this,com.impetus.ankush.constants.typeEdit);}
					});
			});
		},
		//this function makes object has to send in edit parameter post data
		postParamObj : function(elem,status){
			var object = {};
			if(status == com.impetus.ankush.constants.typeEdit){
				var textElement = $(elem).next().children().first().children().first()
					.next().children().first().children().first().children().first().children().first();
				object.name = $(elem).parent().prev().text();
				object.value = textElement.val();
			}
			else if(status == 'editDrop'){
				object.name = $(elem).parent().prev().text();
				object.value = $(elem).val();
				status = com.impetus.ankush.constants.typeEdit;
			}
			else if(status == 'deleteTemp'){
				object.name = $(elem).parent().prev().prev().text();
				for(var i = 0 ; i < postParameterObject[(elem.id).replace('_','.')].length ; i++){
					if((postParameterObject[(elem.id).replace('_','.')][i].status == com.impetus.ankush.constants.typeAdd) && (postParameterObject[(elem.id).replace('_','.')][i].name == object.name)){
						postParameterObject[(elem.id).replace('_','.')].splice(i, 1);
						break;
					}
				}
				$(elem).parent().parent().hide();
				$('#deleteParameterDialogCassandra').modal('hide');
				$('#confirmDeleteParameterCassandra').unbind('click');
				return;
			}
			else if(status == com.impetus.ankush.constants.typeDelete){
				object.name = $(elem).parent().prev().prev().text();
				object.value = $(elem).parent().prev().children().first().text();
				$(elem).parent().parent().hide();
				$('#deleteParameterDialogCassandra').modal('hide');
				$('#confirmDeleteParameterCassandra').unbind('click');
			}
			else if(status == com.impetus.ankush.constants.typeAdd){
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
				$("#group-id-cassandraParameterTable_-ins-class-left-ins-div-class-left-nbsp-nbsp-nbsp-nbsp-"+elem.replace(/[\.]+/g,'-')+"-div-")
					.after('<tr style="font-weight:bold;"><td></td><td>'+object.name+'</td><td>'+object.value+'</td><td><a class="deleteParameter" href="#" onclick="com.impetus.ankush.configurationParam.postParamObjDialog(this,\'deleteTemp\')" id="'+elem.replace('.','_')+'"><ins></ins></a></td></tr>');
				$('#div_addParameter').modal('hide');
				$('#addParamCassandra').unbind('click');
				$('#newParameterName').val('');
				$('#newParameterValue').val('');
			}
			object.status = status;
			var pushFlag = true;
			if(status != com.impetus.ankush.constants.typeAdd){
				$.each(postParameterObject[(elem.id).replace('_','.')],function(key,valueObject){
					if(valueObject.name === object.name){
						valueObject.value = object.value; 
						pushFlag = false;
						return;
					}
				});
				if(pushFlag)
					postParameterObject[(elem.id).replace('_','.')].push(object);
			}
			else
				postParameterObject[elem].push(object);
			$('#div_addParameter').modal('hide');
		},
		//this function will open dialog for parameter object
		postParamObjDialog : function(elem,status){
			if(status != 'deleteTemp')
				status = com.impetus.ankush.constants.typeDelete;
			$('#deleteParameterDialogCassandra').appendTo('body').modal();
			$('#confirmDeleteParameterCassandra').bind('click',function(){ com.impetus.ankush.configurationParam.postParamObj(elem,status); });
		},
		//this function will open dialog to add parameter
		addParamDialog : function(){
			$('#div_addParameter').modal('show');
			setTimeout(function(){
				$("#newParameterName").focus();
			},500)
			$('#addParamCassandra').bind('click',function(){ com.impetus.ankush.configurationParam.postParamObj($('#fileNameValueAddParam').val(),com.impetus.ankush.constants.typeAdd); });
			
		},
		//this function will post data for edit parameters
		applyChangesPostParamObj : function(){
			$("#applyParameters").button('loading');
			var editParameterUrl =  null;
			if(clusterTechnology == 'Hybrid')
				editParameterUrl = baseUrl+'/monitor/'+clusterId+'/editparams?component='+hybridTechnology;
			else
				editParameterUrl = baseUrl+'/monitor/'+clusterId+'/editparams';
			var data = {};
			data.params = {};
			data.params = postParameterObject;
				com.impetus.ankush.placeAjaxCall(editParameterUrl, "POST", true, data, function(result) {
						$("#applyParameters").button('reset');
						if(!result.output.status){
							$('#error-div').show();
							$('#popover-content').html(result.output.error[0]);
						}
						for(var key in postParameterObject){
							postParameterObject[key] = [];
						}
				});
			
		}
		
};
