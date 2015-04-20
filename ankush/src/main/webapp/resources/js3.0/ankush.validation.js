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
com.impetus.ankush.validation = {
						errorCount : 0,
						numeric : function(data) {
				    		var patt= /^[\d]*$/ ;
				    		data = $.trim(data);
				    		if(!patt.test($.trim(data))) {	
				    			return false;
				    		}
				    		return true;
				    	},
				    	email:function(data){
				    		var patt=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
				    		if(!patt.test($.trim(data))) {
				    			
				    			return false;
				    		}
				    			return true;
				    	},
				    	greaterThan_0 : function(data) {
				    		data = parseFloat($.trim(data));
				    		if(data <= 0){
				    			return false;
				    		}
				    		return true;
				    	},
				    	lessThan_100 : function(data) {
				    		data = parseFloat($.trim(data));
				    		if(data > 100){
				    			return false;
				    		}
				    		return true;
				    	},
				    	between :function (x, min, max) {
				    		x = parseInt($.trim(x));
				    		min = parseInt($.trim(min));
				    		max = parseInt($.trim(max));
				    		return x >= min && x <= max;
				    	},
				    	betweenFloat :function (x, min, max) {
				    		x = parseFloat(x);
				    		min = parseFloat(min);
				    		max = parseFloat(max);
				    		return x >= min && x <= max;
				    	}, 
				    	alphaNumeric : function(data) {
				    		var patt= /^[\w]*$/ ;
				    		if(!patt.test($.trim(data))) {
				    			return false;
				    		}
				    		return true;
				    			
				    	},
				    	alphaNumericWithoutUnderScore : function(data) {
				    		var patt= /^[a-zA-Z0-9]*$/ ;
				    		if(!patt.test($.trim(data))) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	alphaNumericWithSpace : function(data) {
				    		var patt= /^[\w\s]*$/ ;
				    		if(!patt.test($.trim(data))) {
				    			
				    			return false;
				    		}
				    		return true;
				    			
				    	},
				    	length : function(data, limit) {
				    		if($.trim(data).length > limit) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	aplhabets :function(data){
				    		var patt = /^[a-zA-Z]+$/;
				    		if(!patt.test($.trim(data))) {
				    			
				    			return false;
				    		}
				    		return true;
				    	},
				    	username:function(data){
				    		var patt=/^[\w\.]+$/;
				    		if(!patt.test($.trim(data))) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	linuxUser:function(data){
				    		var patt=/^[a-z][-a-z0-9_]*$/;
				    		if(!patt.test($.trim(data))) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	ipAddressOrHost : function(data){
				    		var patt= /^([a-zA-Z0-9_\.\-]*)$/ ;
				    		if(!patt.test($.trim(data))) {
				    		
				    			return false;
				    		}
				    		return true;
				    	},
				    	email:function(data){
				    		var patt=/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
				    		if(!patt.test($.trim(data))) {
				    			
				    			return false;
				    		}
				    			return true;
				    	},
				    	empty : function(data) {
				    		if($.trim(data).length == 0) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	withoutSpace : function(data) {
				    		if($.trim(data).split(' ').length > 1) {
				    			return false;
				    		}
				    		return true;
				    	}, 
				    	range : function(data1, lowerLimit, upperLimit) {
				    		if(!com.impetus.ankush.validation.between($.trim(data1), lowerLimit, upperLimit)) {
				    			
				    			return false;
				    		}
				    		return true;
				    	},
				    	oPort: function(data){
				    		if(!com.impetus.ankush.validation.numeric(data))
				    			return false;
				    		
				    		if(!com.impetus.ankush.validation.empty(data))
				    			return false;
				    		if(!com.impetus.ankush.validation.validPort(data))
				    			return false;
				    		if($.trim(data) <= 0) {
				    		
				    			return false;
				    		}		    		
				    		if(!com.impetus.ankush.validation.range(data, 1024, 65535))
				    			return false;
				    		
				    		return true;	
				    	},
				    	validPort:function(data){
				    		if($.trim(data) <1024) {
				    		
				    			return false;
				    		}
				    		return true;
				    	},
				    	pattern : function(value, pattern) {
							if(value.match(pattern) != null) {
								return true;
							}
							return false;
						},
				    	port : function(data) {
				    		if(!com.impetus.ankush.validation.numeric(data))
				    			return false;
				    		
				    		if(!com.impetus.ankush.validation.empty(data))
				    			return false;
				    		if(!com.impetus.ankush.validation.range(data, 0, 65535))
				    			return false;
				    		
				    		return true;	
				    	},				
				    
				    	alphaNumericSpace:function(data){
				    		var patt= /^[a-zA-Z0-9]+/ ;
				    		if(!patt.test($.trim(data))) {
				    			
				    			return false;
				    		}
				    		return true;
				    			
				    	},
				    	maxValue:function(data,maxLength){
				    		if($.trim(data).length>maxLength)
				    			return true;
				    		else
				    			return false;
				    	},
				    	
				    	/*function to check alphanumeric with - _ and . */
				    	
				    	alphaNumericChar:function(data){
				    		var patt= /^([a-zA-Z0-9 _.-]+)$/;
				    		if(!patt.test($.trim(data))) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	alphaNumericCharWithoutHyphen:function(data){
				    		var patt= /^([a-zA-Z0-9 _.]+)$/;
				    		if(!patt.test($.trim(data))) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	alphaNumericCharWithUnderscore:function(data){
				    		var patt= /^([a-zA-Z0-9 _]+)$/;
				    		if(!patt.test($.trim(data))) {
				    			return false;
				    		}
				    		return true;
				    	},
				    	maxLimit:function(data,maxLength){
				    		if($.trim(data)>maxLength)
				    			return true;
				    		else
				    			return false;
				    	},
				    	changeCssForError : function() {
							$('.tooltip-inner').css({
								'background-color' : 'white',
								'color' : 'black',
								'border' : "1px solid #EF3024",
								'-webkit-border-radius' : ' 4px',
								'-moz-border-radius' : '4px',
								'border-radius' : '4px',
								'-webkit-box-shadow' : '5px 5px 5px 5px #888888',
								'-moz-box-shadow' : ' 5px 5px 5px 5px #888888',
								'box-shadow' : '5px 5px 5px 5px #888888',
							});
							$('.tooltip.right').css('border-right-color', '#EF3024');
							$(".tooltip.right .tooltip-arrow").css({
								"top" : "50%",
								"left" : "0",
								"margin-top" : "-5px",
								"border-right-color" : "#EF3024",
								"border-width" : "5px 5px 5px 0"
							});
						},

						changeCssForNrml : function() {
							$('.tooltip-inner').css({
								'max-width': '250px',
								'padding': '8px',
								'color': '#686A6C',
								'text-align': 'center',
								'text-decoration': 'none',
								'background-color': '#E7EFFF',
								'-webkit-border-radius': '4px',
								'-moz-border-radius': '4px',
								'border-radius': '4px',
								'font-family':'Franklin Gothic Book',
								'font-size':'14px',
								'border' : "0px none",
								'-webkit-box-shadow' : '0px 0px 0px 0px #888888',
								'-moz-box-shadow' : '0px 0px 0px 0px #888888',
								'box-shadow' : '0px 0px 0px 0px #888888',
							});
							$('.tooltip.right').css({'padding': '0 5px', 'margin-left': '5px', 'border-right-color': '#E7EFFF',});
							$(".tooltip.right .tooltip-arrow").css({
								'top': '50%',
								'left': '0',
								'margin-top': '-5px',
								'border-right-color': '#E7EFFF',
								'border-width': '5px 5px 5px 0',
							});
						},
				    	tooltipMsgChange_Error : function(id, msg) {
				    		$("#" + id).hover(function() {
								com.impetus.ankush.validation.changeCssForError();
							});
							$("#" + id).focus(function() {
								com.impetus.ankush.validation.changeCssForError();
							});
							$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
							'fixTitle');
						},
						tooltipMsgChange_Nrml : function(id, msg) {
							$("#" + id).focus(function() {
								com.impetus.ankush.validation.changeCssForNrml();
							});

							$("#" + id).hover(function() {
								com.impetus.ankush.validation.changeCssForNrml();
							});
							$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
							'fixTitle');
						},
						showErrorDiv : function(errorDivIdContainer, errorHeaderBtnId) {
							$("#"+errorDivIdContainer).css("display", "block");
							$('#'+errorHeaderBtnId).css("display", "block");
							
							if(com.impetus.ankush.validation.errorCount > 1)
	            				 $('#'+errorHeaderBtnId).show().html('Errors '+"<span class='badge'>"+com.impetus.ankush.validation.errorCount+"</span>");
							else
								$('#'+errorHeaderBtnId).show().html('Error '+"<span class='badge'>"+com.impetus.ankush.validation.errorCount+"</span>");
						},
						validateField : function(validationType, fieldId, fieldName, errorDivId) {
				    		var isValid = false;
				    		var errorMsg = '';
				    		isValid = com.impetus.ankush.validation.empty($('#'+fieldId).val());
				    		if(!isValid) {
		    					com.impetus.ankush.validation.errorCount++;
		    					errorMsg = com.impetus.ankush.validation.errorCount + '. ' + fieldName + ' cannot be empty';
			    				tooltipMsg = fieldName + ' cannot be empty';	
		    				}
				    		else {
				    			switch (validationType)
					    		{
					    			case 'required':
					    				break;
					    			case 'requiredWithoutSpace':
					    				isValid = com.impetus.ankush.validation.withoutSpace($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Can't contain space";
							    			tooltipMsg = fieldName + " cannot contain space";	
						    			}
					    				break;
					    			case 'numeric':
					    				isValid = com.impetus.ankush.validation.numeric($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Should be a numeric value";
							    			tooltipMsg = fieldName + " should be a numeric value";	
						    			}
					    				break;
					    			case 'linuxUser':
					    				isValid = com.impetus.ankush.validation.linuxUser($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Should be a valid linux user name";
							    			tooltipMsg = fieldName + " should be a valid linux user name";	
						    			}
					    				break;
					    			case 'requiredAlphaNumeric':
					    				isValid = com.impetus.ankush.validation.alphaNumeric($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Should be an alphanumeric value";
							    			tooltipMsg = fieldName + " should be an alphanumeric value";	
						    			}
					    				break;
					    			case 'alphaNumericWithoutUnderScore':
					    				isValid = com.impetus.ankush.validation.alphaNumericWithoutUnderScore($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Should be an alphanumeric value";
							    			tooltipMsg = fieldName + " should be an alphanumeric value";	
						    			}
					    				break;
					    			case 'alphaNumericWithSpace':
					    				isValid = com.impetus.ankush.validation.alphaNumericWithSpace($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Should be an alphanumeric value with only spaces allowed";
							    			tooltipMsg = fieldName + " should be an alphanumeric value with only spaces allowed";	
						    			}
					    				break;
					    			case 'between_O_100':
					    				isValid = com.impetus.ankush.validation.numeric($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Should be a numeric value";
							    			tooltipMsg = fieldName + " should be a numeric value";	
							    			break;
						    			}
										isValid = com.impetus.ankush.validation.between(parseInt($('#'+fieldId).val(),10),'0','100');
										if(!isValid) {
											com.impetus.ankush.validation.errorCount++;
											errorMsg = com.impetus.ankush.validation.errorCount + ". " +fieldName+ " Limit should be within 100%";
											tooltipMsg = fieldName + " cannot exceed above 100%";	
										}
										
					    			case 'greaterThan_0':
					    				isValid = com.impetus.ankush.validation.numeric($('#'+fieldId).val());
					    				if(!isValid) {
					    					com.impetus.ankush.validation.errorCount++;
						    				errorMsg = com.impetus.ankush.validation.errorCount + ". Invalid " + fieldName + ": Should be a numeric value";
							    			tooltipMsg = fieldName + " should be a numeric value";	
							    			break;
						    			}
										isValid = com.impetus.ankush.validation.greaterThan_0($('#'+fieldId).val());
										if(!isValid) {
											com.impetus.ankush.validation.errorCount++;
											errorMsg = com.impetus.ankush.validation.errorCount + ". " +fieldName+ " Limit should be within 100%";
											tooltipMsg = fieldName + " cannot exceed above 100%";	
										}
										break;	
					    		}
				    		}
				    		
				    		if(!isValid) {
		    					com.impetus.ankush.validation.addErrorToDiv(fieldId, errorDivId, errorMsg,tooltipMsg);
		    				}
				    		else {
				    			tooltipMsg = "Enter " + fieldName;
				    			$('#'+fieldId).removeClass('error-box');
				    			com.impetus.ankush.common.tooltipOriginal(fieldId, tooltipMsg);	
				    		}
				    		return isValid;
				    	},
				    	addErrorToDiv : function(fieldId, errorDivId, errorMsg, tooltipMsg) {
				    		var appendToDiv = '<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="#" class="alert-link">' + 
				    						  errorMsg+'</a></br></div>';
				    		if(fieldId != null)
				    		{
				    			$('#'+fieldId).addClass('error-box');
				    			appendToDiv = '<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a class="alert-link" href="javascript:com.impetus.ankush.validation.focusError(\''+ 
											  fieldId + '\');">'+errorMsg+'</a></br></div>';	
				    			com.impetus.ankush.common.tooltipMsgChange(fieldId, tooltipMsg);
				    		}
							$("#"+errorDivId).append(appendToDiv);
				    	},
				    	
				    	addErrorToDivJumpToAnchor : function(fieldId, errorDivId, errorMsg, tooltipMsg) {
				    		var appendToDiv = '<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="#"class="alert-link">' + 
				    						  errorMsg+'</a></br></div>';
				    		if(fieldId != null)
				    		{
				    			$('#'+fieldId).addClass('error-box');
				    			appendToDiv = '<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a class="alert-link" href="javascript:com.impetus.ankush.validation.jumpToAnchor(\''+ 
											  fieldId + '\');">'+errorMsg+'</a></br></div>';	
				    			com.impetus.ankush.common.tooltipMsgChange(fieldId, tooltipMsg);
				    		}
							$("#"+errorDivId).append(appendToDiv);
				    	},
				    	// Function to Jump to an anchor tag on the page
						jumpToAnchor : function(tagName) {
							window.location.hash = tagName;
						},
						
						addNewErrorToDivJumpToAnchor: function(fieldId, errorDivId, errorMsg, tooltipMsg) {
							com.impetus.ankush.validation.errorCount++;
				    		var errorMsgToAppend =  errorMsg;
				    		com.impetus.ankush.validation.addErrorToDivJumpToAnchor(fieldId, errorDivId, errorMsgToAppend, tooltipMsg);
						},
						
				    	addNewErrorToDiv : function(fieldId, errorDivId, errorMsg, tooltipMsg) {
				    		com.impetus.ankush.validation.errorCount++;
				    		var errorMsgToAppend =errorMsg;
				    		com.impetus.ankush.validation.addErrorToDiv(fieldId, errorDivId, errorMsgToAppend, tooltipMsg);
				    	},
				    	showAjaxCallErrors : function (arrErrors, errorDivId, errorDivIdContainer, errorHeaderBtnId) {
				    		$("#"+errorDivId).empty();
				    		$("#"+errorDivId).css("display", "block");
				    		var numOfErrors = arrErrors.length;
				    		com.impetus.ankush.validation.errorCount = 0;
				    		for(var iCount = 0; iCount < numOfErrors; iCount++) {
				    			com.impetus.ankush.validation.addNewErrorToDiv(null, errorDivId, arrErrors[iCount], null);
				    		}
				    		if(com.impetus.ankush.validation.errorCount != 0) {
				    			com.impetus.ankush.validation.showErrorDiv(errorDivIdContainer, errorHeaderBtnId);	
				    		}
				    	},
				    	removeFakePathFromPath : function(fieldId) {
							var value = $('#' + fieldId).val().replace('C:\\fakepath\\', '');
							$('#' + fieldId).val(value);
						},
				    	focusError : function(id) {
							$('#' + id).focus();
						},
};
