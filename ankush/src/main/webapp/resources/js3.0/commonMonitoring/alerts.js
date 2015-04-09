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
var alertResultSave = {};
com.impetus.ankush.alerts = {
		//this function will save alerts page data
		  saveAlerts : function() {
	            var data = {};
	            data.thresholds = new Array();
	            var alertUrl = baseUrl + '/monitor/' + clusterId + '/alertsConf';
	            $("#mailingList").removeClass('error-box');
	            data.refreshInterval = $("#refreshInterval").text();
	            if($("#mailingList").val()!=""){
	                var mailingListArray = $("#mailingList").val().split(',');
	                for(var i=0 ; i < mailingListArray.length ; i++){
	                    if(!com.impetus.ankush.validate.email(mailingListArray[i])){
	                        $("#mailingList").addClass('error-box');
	                        com.impetus.ankush.validation.tooltipMsgChange_Error("mailingList", "Invalid email Address");
	                        com.impetus.ankush.validation.showAjaxCallErrors(['Invalid email Address'], 'popover-content-alert', 'error-div-alert', '');
	                        return;
	                    }
	                }
	                data.mailingList = $("#mailingList").val();
	            }else{
	                data.mailingList = "";
	            }
	            if ($("#alertInform").is(':checked')) {
	                data.informAllAdmins = true;
	            } else {
	                data.informAllAdmins = false;
	            }

	            for ( var i = 0; i < alertResultSave.output.thresholds.length; i++) {
	                var rowData = {
	                        "metricName" : $("#metricName" + i).text(),
	                        "warningLevel" : $("#warningLevel-" + i).text(),
	                        "alertLevel" : $("#alertLevel-" + i).text()
	                };	
	                data.thresholds.push(rowData);
	            }
	            com.impetus.ankush.placeAjaxCall(alertUrl, "POST", false,data,function(result){
	            
	            });
	        },
	        //this will fill event table with data in cluster
	        getDefaultAlertValues : function(clusterId) {
	            var alertUrl = baseUrl + '/monitor/' + clusterId + '/alertsConf';
	            com.impetus.ankush.placeAjaxCall(alertUrl, "GET", true,null,function(alertResult){
	            	alertResultSave = alertResult;
	            	$("#refreshInterval").html('').html(alertResult.output.refreshInterval);
		            if(alertResult.output.mailingList == null)
		                alertResult.output.mailingList = "";
		            if (alertResult.output.mailingList != "") {
		                $("#mailingList").val(alertResult.output.mailingList.split(';').join(','));
		            } else {
		                $("#mailingList").html('').val("");
		            }
		            if (alertResult.output.informAllAdmins) {
		                $("#alertInform").attr('checked', 'checked');
		            } else {
		                $("#alertInform").removeAttr('checked');
		            }
		            if (configAlertsUsageTable != null) {
		                configAlertsUsageTable.fnClearTable();
		            } else {
		                configAlertsUsageTable.datatable();
		            }
		            for ( var i = 0; i < alertResult.output.thresholds.length; i++) {
		                configAlertsUsageTable.fnAddData([
		                                                  '<a class="" id="metricName' + i
		                                                  + '" style="text-align: right;">'
		                                                  + alertResult.output.thresholds[i].metricName
		                                                  + '</a>',
		                                                  '<span class="editableLabel" id="warningLevel-' + i
		                                                  + '" style="width:20px;">'
		                                                  + alertResult.output.thresholds[i].warningLevel
		                                                  + '</span>',
		                                                  '<span class="editableLabel" id="alertLevel-' + i + '">'
		                                                  + alertResult.output.thresholds[i].alertLevel
		                                                  + '</span>', ]);
		            }
		            $('.editableLabel').editable({
		                type : 'text',
		                validate :function(value){
		                    return com.impetus.ankush.alerts.checkUsageValue(this,value);}
		            });
		            $('#refreshInterval').editable({
		                type : 'text',
		                validate :function(value){
		                	if(!com.impetus.ankush.validate.empty(value)) {
		    	                return "Refresh interval value must not be empty.";
		    	        	}else if(!com.impetus.ankush.validate.numeric(value)) {
		    	                return "Refresh interval must be numeric.";
		    	            }
		                }
		            });
	            });
	        },
	        
	      
	      //this will validate refresh interval
	        refreshIntervalCheck : function(value){
	        	var flag = true;
            	$('.haChecked').each(function(){
            		var delayInterval = $(this).parent().next().next().next().next().text();
            		if(com.impetus.ankush.validate.numeric(delayInterval)){
            			if(value >= delayInterval){
            				flag = false;
            				return ;
            			}
            		}
            	});
            	if(!flag)
            		return "Refresh interval value must not be greater than or equal to Delay interval.";
            	else
            		return '';
	            
	        },
	        //this will check usage
	        checkUsageValue : function(elem,value) {
	        	var id = elem.id;
	        	value = +$.trim(value);
	        	var textElement = $('#'+id).next().children().first().children().first()
				  .next().children().first().children().first().children().first().children().first();
	        	if(!com.impetus.ankush.validate.empty(value)) {
	        		textElement.attr('data-original-title', 'Value must not be empty.');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
	                return true;
	        	}else if(!com.impetus.ankush.validate.numeric(value)) {
	        		textElement.attr('data-original-title', "There must be a numeric value.");
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
	                return true;
	            }else if(!((value > 0) && (value < 100))){
	            	textElement.attr('data-original-title', "Invalid value. 0 < value < 100");
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
	                return true;
	            }else if(value.length > 3){
	            	textElement.attr('data-original-title', "Invalid value. There must be a 3 digit or less.");
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
	                return true;
	            }
	        	
	            var elementId= elem.id;
	            var id1 = elementId.split('-');
	            if(id1[0]=="warningLevel"){
	                if (parseInt(value,10) > parseInt($('#alertLevel-'+id1[1]).text(),10)) {
	                	textElement.attr('data-original-title', "Invalid value. Warning level must not be greater than alert level.");
						textElement.attr('data-placement', 'right');
						textElement.css('border-color','red');
						textElement.tooltip();
						textElement.bind('hover',function(){
							$('.tooltip').addClass('tootipDatatable');
						});
	                    return true;
	                }
	            }else if(id1[0]=="alertLevel"){
	                if (parseInt(value,10) < parseInt($('#warningLevel-'+id1[1]).text(),10)) {
	                	textElement.attr('data-original-title', "Invalid value. Alert level must not be less than warning level.");
						textElement.attr('data-placement', 'right');
						textElement.css('border-color','red');
						textElement.tooltip();	
						textElement.bind('hover',function(){
							$('.tooltip').addClass('tootipDatatable');
						});
	                    return true;
	                }
	            }
	        },
	        //this function will populate HA table
	        populateTableHA : function(){
	        	var url = baseUrl+'/monitor/'+clusterId+'/haservices';
	        	com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
	        		if(serviceHAConfTable != null)
	        			serviceHAConfTable.fnClearTable();
					if(result.output.status == true){
						if(result.output.services == 0){
							serviceHAConfTable.fnSettings().oLanguage.sEmptyTable = "No Records found.";
							serviceHAConfTable.fnClearTable();
						}
						for(var i = 0 ; i < result.output.services.length ; i++){
							if(result.output.services[i].tryCount == null){
								result.output.services[i].tryCount = 3;
							}
							if(result.output.services[i].delayInterval == null){
								result.output.services[i].delayInterval = 40;
							}
							serviceHAConfTable.fnAddData([
							                              result.output.services[i].enabled ? '<input type="checkbox" class="haChecked checkedHAServices" checked="checked" id="'+result.output.services[i].service+'" onclick="com.impetus.ankush.alerts.addOrRemoveHaClass(this)">': 
							                              '<input type="checkbox" class="haNotChecked checkedHAServices" id="'+result.output.services[i].service+'" onclick="com.impetus.ankush.alerts.addOrRemoveHaClass(this)">',
							                              result.output.services[i].component,
							                              result.output.services[i].service,
							                              '<span class="haEditableTryCount tooltipAlertPage" id="tryCount-'+i+'" data-original-title="Select row to edit." data-placement="right">'+result.output.services[i].tryCount+'</span>',
							                              '<span class="haEditableDelayInterval tooltipAlertPage" id="delayInterval-'+i+'" data-original-title="Select row to edit." data-placement="right">'+result.output.services[i].delayInterval+'</span>'
							                              ]);
							}
					}else{
						serviceHAConfTable.fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						serviceHAConfTable.fnClearTable();
					}
					$('.haEditableTryCount').editable({
		                type : 'text',
		                validate :function(value){
		                    return com.impetus.ankush.alerts.haEditValidate(this,value);}
		            });
					$('.haEditableDelayInterval').editable({
		                type : 'text',
		                validate :function(value){
		                    return com.impetus.ankush.alerts.haEditValidate(this,value);}
		            });
					for(var j = 0 ; j < result.output.services.length ; j++){
						if(result.output.services[j].enabled === true){
							$('#'+result.output.services[j].service).parent().next().next().next().children().first().editable('option', 'disabled', false);
							$('#'+result.output.services[j].service).parent().next().next().next().next().children().first().editable('option', 'disabled', false);
						}else{
							$('#'+result.output.services[j].service).parent().next().next().next().children().first().editable('option', 'disabled', true);
							$('#'+result.output.services[j].service).parent().next().next().next().next().children().first().editable('option', 'disabled', true);
						}
					}
				});
	        	
	        },
	        haEditValidate : function(elem,value){
	        	var id = elem.id;
	        	var textElement = $('#'+id).next().children().first().children().first()
				  .next().children().first().children().first().children().first().children().first();
	        	var column = (elem.id).split('-')[0];
	        	value = +$.trim(value);
	        	if(!com.impetus.ankush.validate.empty(value)) {
	        		textElement.attr('data-original-title', 'Value must not be empty.');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
	                return true;
	        	}else if(!com.impetus.ankush.validate.numeric(value)) {
	        		textElement.attr('data-original-title', "There must be a numeric value.");
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
	                return true;
	            }else if(value.length > 8){
	            	textElement.attr('data-original-title', "Invalid value. There must be a 8 digit or less.");
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
	                return true;
	            }else if(column === 'delayInterval'){
	            	if(value <= +($.trim($('#refreshInterval').text()))){
	            		textElement.attr('data-original-title', "Delay Interval must be greater than Refresh Interval	.");
						textElement.attr('data-placement', 'right');
						textElement.css('border-color','red');
						textElement.tooltip();
						textElement.bind('hover',function(){
							$('.tooltip').addClass('tootipDatatable');
						});
	            		return true;
	            	}
	            }
	            else if(column === 'tryCount'){
	            	if((value > 10) || (value <= 0) ){
	            		textElement.attr('data-original-title', "Try count must be greater than 0 and must not be greater than 10.");
						textElement.attr('data-placement', 'right');
						textElement.css('border-color','red');
						textElement.tooltip();
						textElement.bind('hover',function(){
							$('.tooltip').addClass('tootipDatatable');
						});
	            		return true;
	            	}
	            }
	        },
	        //this function will add or remove Ha checkbox class
	        addOrRemoveHaClass : function(elem){
	        	com.impetus.ankush.headerCheckedOrNot("checkedHAServices","haservicesHeadCheckbox");
	        	if($(elem).is(':checked')){
	        		$(elem).removeClass('haNotChecked').addClass('haChecked');
	        		$(elem).parent().next().next().next().children().first().editable('option', 'disabled', false);
		        	$(elem).parent().next().next().next().next().children().first().editable('option', 'disabled', false);
	        	}else{
	        		$(elem).removeClass('haChecked').addClass('haNotChecked');
	        		$(elem).parent().next().next().next().children().first().editable('option', 'disabled', true);
		        	$(elem).parent().next().next().next().next().children().first().editable('option', 'disabled', true);
	        	}
	        },
	        checkAll : function(checkboxClass,headerElement,autorefresh){
	        	if($(headerElement).is(':checked')){
	        		$('.'+checkboxClass).each(function(){
	        			$(this).removeClass('haNotChecked').addClass('haChecked');
	        			$(this).parent().next().next().next().children().first().editable('option', 'disabled', false);
			        	$(this).parent().next().next().next().next().children().first().editable('option', 'disabled', false);
					});
	        	}else{
	        		$('.'+checkboxClass).each(function(){
	        			$(this).removeClass('haChecked').addClass('haNotChecked');
		        		$(this).parent().next().next().next().children().first().editable('option', 'disabled', true);
			        	$(this).parent().next().next().next().next().children().first().editable('option', 'disabled', true);
					});
	        	}
	        	com.impetus.ankush.checked_unchecked_all(checkboxClass,headerElement,autorefresh);
	        },
	        //this function will save High Availability Configuration
	        saveHAConf : function(){
	        	var data = {};
	        	data = [];
	        	var validObj = {};
	        	validObj.flag = true;
	        	validObj.errorString = '';
	        	$('.haChecked').each(function(){
	        		var obj = {};
	        		obj.component = $(this).parent().next().text();
	        		obj.service = $(this).parent().next().next().text();
	        		obj.tryCount = $(this).parent().next().next().next().text();
	        		obj.delayInterval = $(this).parent().next().next().next().next().text();
	        		if(obj.tryCount === 'Empty'){
	        			validObj.flag = false;
	        			validObj.errorString = "Try Count value must be numeric for selected service.";
	        			return;
	        		}else if(!($(this).parent().next().next().next().children().first().is(':visible'))){
	        			validObj.flag = false;
	        			validObj.errorString = "Invalid Try Count.";
	        			return;
	        		}else if(obj.delayInterval === 'Empty'){
	        			validObj.flag = false;
	        			validObj.errorString = "Delay Interval value must be numeric for selected service.";
	        			return;
	        		}else if(!($(this).parent().next().next().next().next().children().first().is(':visible'))){
	        			validObj.flag = false;
	        			validObj.errorString = "Invalid Delay Interval.";
	        			return;
	        		}
	        		data.push(obj);
	        	});
	        	if(validObj.flag){
	        		var url = baseUrl + '/service/' + clusterId + '/haservices';
		        	com.impetus.ankush.placeAjaxCall(url,'POST',false,data,function(result){
		        		if(!result.output.status){
		        			validObj.flag = false;
		        			validObj.errorString = result.output.error[0];
		        		}
		        	});
	        	}
	        	return validObj;
	        },
	        //this function will call to separate function to save alerts and configuration
	        saveAlertsAndHAConf : function(){
	        	$('#saveAlertsButton').button('loading');
	        	if(!$('#refreshInterval').is(':visible')){
	        		com.impetus.ankush.validation.showAjaxCallErrors(["Refresh Interval text box must have been closed first."], 'popover-content', 'error-div', '');
	        		return;
	        	}
	        	var string = com.impetus.ankush.alerts.refreshIntervalCheck($.trim($('#refreshInterval').text()));
	        	if(string != ''){
	        		com.impetus.ankush.validation.showAjaxCallErrors([string], 'popover-content', 'error-div', '');
	        		return;
	        	}
	        	var returnObj = com.impetus.ankush.alerts.saveHAConf();
	        	if(returnObj.flag === false){
	        		com.impetus.ankush.validation.showAjaxCallErrors([returnObj.errorString], 'popover-content', 'error-div', '');
	        		return;
	        	}
	        	var data = com.impetus.ankush.alerts.saveAlerts();
	        	
	        	$(location).attr('href',( baseUrl+'/commonMonitoring/'+clusterName+'/C-D/'+clusterId+'/'+clusterTechnology));
	        	$('#saveAlertsButton').button('reset');
	        	
	        }
}
