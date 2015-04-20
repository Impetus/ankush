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
com.impetus.ankush.password={
		mainFlag : true,
		errorCount : 0,
		//method to change password and called when user click on Apply button on changePassword page.
		//This method first checks for client-side validation before going to server-end and in order to do that it makes a call to validate().
		changePassword : function(){
			com.impetus.ankush.password.mainFlag = true;
			com.impetus.ankush.password.errorCount = 0;
			$('#errorBtn').hide();
			$('#error-div').hide();
			$("#popover-content").empty();
			com.impetus.ankush.password.validate();
			if(!com.impetus.ankush.password.mainFlag){
				if(com.impetus.ankush.password.errorCount==1){
					$('#errorBtn').text(com.impetus.ankush.password.errorCount + " Error").show();
				}else{
					$('#errorBtn').text(com.impetus.ankush.password.errorCount + " Errors").show();	
				}
				$('#error-div').show();
				return false;
			}else{
				$('#errorBtn').hide();
				$('#error-div').hide();
			}
			$('#changePasswordButton').button('loading');
			var existingPassword = $('#existingPassword').val();
			var newPassword = $('#newPassword').val();
			var data={
					password : existingPassword,
					newpassword: newPassword
			};
			url = baseUrl + '/user/changepassword';
			com.impetus.ankush.placeAjaxCall(url,"POST",false,data,
					function(result){
						if(result.output.status){
							$('#changePasswordButton').button('reset');
							com.impetus.ankush.login.signOut();
						}else{
							$('#changePasswordButton').button('reset');
							com.impetus.ankush.validation.showAjaxCallErrors(result.output.errors, 'popover-content', 'error-div', 'errorBtn');
						}
					},
					function(responseData){
					});
	},
	
	//This method is used to split key(fieldId) with space whenever a Capital letter comes in the key and return the new Key.
	changeKeyToLabel : function(key){
		 return key.split(/(?=[A-Z])/).map(
					function(p) {
						return p.charAt(0).toUpperCase()
								+ p.slice(1);
			}).join(' ');
	},
	
	//applies client-side validation and call iterateValidationFields() for this
	validate : function(){
		var validationFields = {
			"existingPassword" :{
				"empty" : true
			},
			"newPassword":{
				"empty" : true,
				"minLength" : 4, 
				"sameAsexistingPassword":true
			},
			"confirmPassword":{
				"empty" : true,
				"confirmPassword":true
			}
		};
		com.impetus.ankush.password.iterateValidationFields(validationFields);
	},
	
	//called by validate() method in order to continue client-side validation
	iterateValidationFields : function(validationFields){
		for(key in validationFields){
			com.impetus.ankush.password.validateKey(key,validationFields[key]);
		}
	},
	
	//called by iterateValidationFields() method in order to continue client-side validation
	validateKey:function(fieldId,fieldValidationData){
		for(key in fieldValidationData){
			var flag = com.impetus.ankush.password.actualValidate(key,fieldId);
			if(!flag){
				com.impetus.ankush.password.mainFlag = false;
				break;
			}
		}
	},
	
	//called by validateKey() method in order to continue client-side validation
	actualValidate : function(key,fieldId){
		validationTypes ={
				"empty" : function(fieldId){return com.impetus.ankush.password.emptyValidation(fieldId);},
				"minLength" : function(fieldId){return com.impetus.ankush.password.minLengthValidation(fieldId);},
				"sameAsexistingPassword": function(fieldId){return com.impetus.ankush.password.sameAsexistingPassword(fieldId);},
				"confirmPassword":function(fieldId){return com.impetus.ankush.password.confirmPasswordValidation(fieldId);}
		};
		return validationTypes[key].call(this,fieldId);
	},
	minLengthValidation : function(fieldId){
		if($.trim($("#"+fieldId).val()).length < 4){
			com.impetus.ankush.password.errorCount++;
			var errorMsg = com.impetus.ankush.password.changeKeyToLabel(fieldId) + " must be greater than 3 character.";
			$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a class="alert-link" href="javascript:com.impetus.ankush.password.focusError(\''+ 
					  fieldId + '\');">'+errorMsg+'</a></br></div>');
			com.impetus.ankush.password.tooltipMsgChange_Error(fieldId,errorMsg);
			return false;
		}
		return true;
	},
	//method applies the emptyFieldValidation & for that it makes a call to empty() method of ankush.js
	emptyValidation : function(fieldId){
		if(!com.impetus.ankush.validate.empty($("#"+fieldId).val())){
			com.impetus.ankush.password.errorCount++;
			var errorMsg = com.impetus.ankush.password.changeKeyToLabel(fieldId) + " cannot be blank";
			$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a class="alert-link" href="javascript:com.impetus.ankush.password.focusError(\''+ 
					  fieldId + '\');">'+errorMsg+'</a></br></div>');
			com.impetus.ankush.password.tooltipMsgChange_Error(fieldId,errorMsg);
			return false;
		}
		return true;
	},
	
	//methods checks at client-side whether the newPassword to be changed is same as existing password.
	sameAsexistingPassword : function(fieldId){
		if($("#"+fieldId).val() == $("#existingPassword").val()){
			com.impetus.ankush.password.errorCount++;
			var errorMsg = com.impetus.ankush.password.changeKeyToLabel(fieldId) + " cannot be same as Existing Password";
			$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a class="alert-link" href="javascript:com.impetus.ankush.password.focusError(\''+ 
					  fieldId + '\');">'+errorMsg+'</a></br></div>');
			com.impetus.ankush.password.tooltipMsgChange_Error(fieldId,errorMsg);
			return false;
		}
		return true;
	},
	
	//method applies the confirmPasswordValidation & for that it makes a call to confirmPassword() method of ankush.js
	confirmPasswordValidation:function(fieldId){
		if(!com.impetus.ankush.validate.confirmPassword($("#"+fieldId).val(),$("#newPassword").val())){
			com.impetus.ankush.password.errorCount++;
			var errorMsg = com.impetus.ankush.password.changeKeyToLabel(fieldId) + " should be same as above password";
			$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a class="alert-link" href="javascript:com.impetus.ankush.password.focusError(\''+ 
					  fieldId + '\');">'+errorMsg+'</a></br></div>');
			com.impetus.ankush.password.tooltipMsgChange_Error(fieldId,errorMsg);
			return false;
		}
		return true;
	},
	
	//method is used to create tooltip on each field of changePassword page
	tooltip:function(){
		$("#existingPassword").tooltip();
		$("#newPassword").tooltip();
		$("#confirmPassword").tooltip();
	},
	
	//method adds the error-box class to each field whose id is passed to it.
	focusError : function(id) {
		$('#' + id).focus().addClass("error-box");
	},
	
	//method changes the tooltip whenever error comes in client-side validation and makes the tooltip-border red
	tooltipMsgChange_Error : function(id,msg){
		$("#" + id).focus(function() {
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
		});
		$("#" + id).tooltip('hide').attr('data-original-title', msg).tooltip(
				'fixTitle');

	}
};
$(document).ready(function(){
	com.impetus.ankush.password.tooltip();
});
