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
var userDataTable = null;
com.impetus.ankush.configurationNew={
		errorCount : 0,
		mainFlag : true,
		
		//method to initailze the CommonConfiguration-page{contains method call to initialize different sections like userTable,email,server section}
		initConfig : function(){
			if(document.getElementById('goToLoginOrDashboard') != null){
				$('ul.left-nav li').each(function(){
					$(this).removeClass('selected');
				});
				$('.settings').addClass('selected');
			}
			var url = baseUrl + '/app/conf';
			com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result){
				com.impetus.ankush.configurationNew.loadUserTableSection(result);
				com.impetus.ankush.configurationNew.loadEmailSection(result);
				com.impetus.ankush.configurationNew.loadServerSection(result);
				com.impetus.ankush.configurationNew.tooltip();
			});
		},
		//method to add new Empty row in User Table,Called on click of AddUser button
		addNewRowInUserTable : function(){
		var aiNew = userDataTable.fnAddData( [ '', '', '', '', '','','',''
			                 		        ] );
			                 		    var nRow = userDataTable.fnGetNodes( aiNew[0] );
			                 		    com.impetus.ankush.configurationNew.editRow( userDataTable, nRow );
			                 		    nEditing = nRow;
		},
		
		//method assigns 'Empty' text to each column in the newly added-row of User-Table so-that user can click on the text to edit that.
		//Also assigns id to each column of User-Table.
		//make a call to delete the user to deleteUser() function.
		editRow : function(userDataTable, nRow){
			var tableLength=userDataTable.fnGetData().length;
		    var index=tableLength-1;
		    var jqTds = $('>td', nRow);
		    var currentTime = new Date().getTime();
		    jqTds[0].innerHTML = '<span id="user_id-'+index+'" class="'+ currentTime +'" style="display:none;">'+  currentTime +'</span>',
		    jqTds[1].innerHTML = '<div class="editable-text"><a href="#" class="editable firstName" id="firstName-'+index+'" style="color:red;">Empty</a></div>',
		    jqTds[2].innerHTML = '<div class="editable-text"><a href="#" class="editable lastName" id="lastName-'+index+'" style="color:red;">Empty</a></div>';
		    jqTds[3].innerHTML = '<div class="editable-text"><a href="#" class="editable userId" id="userId-'+index+'" style="color:red;">Empty</a></div>';
		    jqTds[4].innerHTML = '<div class="editable-text"><a href="#" class="editable emailId" id="emailId-'+index+'" style="color:red;">Empty</a></div>';
		    jqTds[5].innerHTML = '<div class="tableSelect"><select id="endis-'+index+'" onclick="com.impetus.ankush.configurationNew.updateStatusField('+index+')"><option>Enabled</option><option>Disabled</option></select></div>',
		    jqTds[6].innerHTML = "<div><a class='deleteUser' id='deleteUser-"+index+"' href='#' onclick='com.impetus.ankush.configurationNew.deleteUser(this);'><ins></ins></a></div>";
		    jqTds[7].innerHTML = '<div style="display:none;"><a class="status" id="status-'+index+'">Add</a></div>';
		    com.impetus.ankush.configurationNew.userEditable();
		},
		
		//method make a call to updateStatus() method that applies client-side validation to User-Table each row 
		//Also makes user-table columns editable
		userEditable : function(){
			$('.editable').editable({
				validate :function(){return com.impetus.ankush.configurationNew.updateStatus(this);}
			});
			for(var i=0;i<userDataTable.fnGetData().length;i++){
		    	if($("#status-"+i).text() == 'Add'){
		    		$("#userId-"+i).editable();
		    	}
		    }
		},
		
		//method to apply all the client-side validations on the User-Table 
		updateStatus : function(elem){
			if(elem == undefined)
				return true;
			var id = elem.id;
			var textElement = $('#'+id).next().children().first().children().first()
							  .next().children().first().children().first().children().first().children().first();
			if(textElement.val() == undefined)
				return false;
			var	textValue = textElement.val();
			var userIdFlag = true;
			$('#userDatatable > tbody > tr').each( function() {
		        if($(this).is(':visible')){
		        	var thirdTd = $(this).children().get(3);
		        	var savedValue = $(thirdTd).children().first().children().first().text();
		        	var savedValueAnchor = $(thirdTd).children().first().children().first();
		        	if($(savedValueAnchor).is(':visible')){
			        	if((savedValue != 'Empty')){
			        		if(savedValue == textValue){
			        			userIdFlag = false;
			        			return false;
			        		}
			        	}
		        	}
		        }
		    });
			textElement.attr('title', null);
			if($("#"+id).hasClass('firstName')){
				if($.trim(textValue).length == 0){
					textElement.attr('data-original-title', 'Please Enter First Name');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if($.trim(textValue).length > 50){
					textElement.attr('data-original-title', 'First Name can not be greater than 50 characters');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if(!com.impetus.ankush.validate.alphaNumeric(textValue)){
					textElement.attr('data-original-title', 'First Name can contain only alphabets and numbers');
					textElement.attr('data-placement', 'right');
					textElement.tooltip();
					textElement.css('border-color','red');
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
			}
			else if($("#"+id).hasClass('lastName')){
				if($.trim(textValue).length == 0){
					textElement.attr('data-original-title', 'Please Enter Last Name');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if($.trim(textValue).length > 50){
					textElement.attr('data-original-title', 'Last Name can not be greater than 50 characters');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if(!com.impetus.ankush.validate.alphaNumeric(textValue)){
					textElement.attr('data-original-title', 'Last Name can contain only alphabets and numbers');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
			}
			else if($("#"+id).hasClass('userId')){
				if($.trim(textValue).length == 0){
					textElement.attr('data-original-title', 'Please Enter UserId');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if(!com.impetus.ankush.validate.alphaNumeric(textValue)){
					textElement.attr('data-original-title', 'UserId can contain only alphabets and numbers');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if(($.trim(textValue).length < 5) || ($.trim(textValue).length > 50)){
					textElement.attr('data-original-title', 'Minimum no of characters are 5 and maximum no of characters are 50');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if(!userIdFlag){
					textElement.attr('data-original-title', 'UserId should be unique');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
			}	
			else if($("#"+id).hasClass('emailId')){
				if($.trim(textValue).length == 0){
					textElement.attr('data-original-title', 'Please Enter Email');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if($.trim(textValue).length > 255){
					textElement.attr('data-original-title', 'Email can not be greater than 255 character');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
				else if(!com.impetus.ankush.validate.email(textValue)){
					textElement.attr('data-original-title', 'Please Enter Valid Email');
					textElement.attr('data-placement', 'right');
					textElement.css('border-color','red');
					textElement.tooltip();
					textElement.bind('hover',function(){
						$('.tooltip').addClass('tootipDatatable');
					});
					return true;
				}
			}
			textElement.unbind('hover',function(){
				$('.tooltip').removeClass('tootipDatatable');
			});
			$('#'+id).text(textValue);
			if(textValue != 'Empty'){
				elem.style.color="#606060";
			}
				
			id = id.split('-');
	        if($('#status-'+id[1]).text() == 'None')
	        	$('#status-'+id[1]).text('Update');
	        return false;
	    },
	    
	    //method loads the User-table
		loadUserTableSection : function(result){
			if(userDataTable == null){
				userDataTable =$("#userDatatable").dataTable({
					 "bJQueryUI" : false,
			         "bPaginate" : false,
			         "bLengthChange" : true,
			         "bFilter" : true,
			         "bSort" : false,
			         "bInfo" : false,
			         "bAutoWidth" : false,
			         "sPaginationType": "full_numbers",
			         "bAutoWidth" : false, 
				});
			}else{
				userDataTable.fnClearTable();
			}
			$('#userDatatable_filter').hide();
			userDataTable=$('#userDatatable').dataTable();
			$('#userTableSearchBox').keyup(function(){
				userDataTable.fnFilter( $(this).val());
			});
			if(Object.keys(result.output.users).length != 0){
				for(var i=0;i<result.output.users.length;i++){
					var users = result.output.users;
					var deleteUser = "<div><a class='deleteUser' href='#' id='deleteUser-"+ i +"'onclick='com.impetus.ankush.configurationNew.deleteUser(this);'><ins></ins></a>";
					var enabledColumn = '<div class="tableSelect"><select id="endis-'+i+'" onclick="com.impetus.ankush.configurationNew.updateStatusField('+i+')"><option value="Enabled">Enabled</option><option value="Disabled">Disabled</option></select></div>';
					if(($('#userId').text() == users[i].username) || ((users[i].email == "admin@company.com") && (users[i].firstName == "Admin"))){
						deleteUser = "";
						enabledColumn = '<div style="display:none"><select id="endis-'+i+'"><option value="Enabled">Enabled</option><option value="Disabled">Disabled</option></select></div>';
					}
					userDataTable.fnAddData([
	        				                 '<span style="display:none;" id="user_id-'+i+'">'+users[i].id+'</span>',
	        				                 '<div class="dataTableFields"><a href="#" class="editable firstName" id="firstName-'+i+'">'+users[i].firstName+'</a></div>',
	        				                 '<div class="dataTableFields"><a href="#" class="editable lastName" id="lastName-'+i+'">'+users[i].lastName+'</a></div>',
	        				                 '<div class="dataTableFields"><a href="#" id="userId-'+i+'">'+users[i].username+'</a></div>',
	        				                 '<div class="dataTableFields"><a href="#" class="editable emailId" id="emailId-'+i+'">'+users[i].email+'</a></div>',
	        				                 enabledColumn,
	        				                 deleteUser,
	        				                 '<div style="display:none;"><a  href="#" class="status" id="status-'+i+'">'+users[i].userStatus+'</a></div>',
    				                   		]);
					if(users[i].enabled == true)
						$('#endis-'+i).val('Enabled');
					else
						$('#endis-'+i).val('Disabled');
				}
				com.impetus.ankush.configurationNew.userEditable();
			}
		},
		//update status field
		updateStatusField : function(index){
			if($("#status-"+index).text() != 'Add')
				$("#status-"+index).text('Update');
		},
		//method loads the emailSection
		loadEmailSection : function(result){
			if(Object.keys(result.output.email).length!=0){
				$("#emailServer").val(result.output.email.server);
    			$("#emailServerPort").val(result.output.email.port);
    			$("#userName").val(result.output.email.userName);
    			$("#emailAccount").val(result.output.email.emailAddress);
    			$("#accountPassword").val(result.output.email.password);
    			$("#confirmPassword").val(result.output.email.password);
    			$("#useSSLCheck").attr("checked",result.output.email.secured);
    			var verification = "Non Verified";
    			if(result.output.email.mailConfVerified){
    				verification = "Verified";
    			}
    			$("#verificationText").text(verification);
			}
		},
		
		//method loads the serverSection
		loadServerSection : function(result){
			if(Object.keys(result.output.serverIP).length!=0){
				$("#publicIpServer").val(result.output.serverIP.publicIp);
    			$("#publicIpServerPort").val(result.output.serverIP.port);
			}
		},
		
		//method send all the changes of CommonConfiguration at backend,called on Apply button click.
		applyConfiguration:function(confUrl){
			$('#dashboard-section').show();
			com.impetus.ankush.configurationNew.emptyFieldValidation();
			if(!com.impetus.ankush.configurationNew.mainFlag){
				return false;
			}else{
				$('#error-div').hide();
				$('#error-header-button').hide();
			}
			$('#applyConfiguration').button();
			$('#applyConfiguration').button('loading');
			var users=new Array();
			var enableDis = {
				'Enabled' : true,
				'Disabled' : false
			};
			for(var i=0;i<userDataTable.fnGetData().length;i++) {
				if($("#status-"+i).text()=='Delete'){
					var usersValue={
							"firstName":$("#firstName-"+i).text(),
							"lastName":$("#lastName-"+i).text(),
							"username":$("#userId-"+i).text(),
							"email":$("#emailId-"+i).text(),
							"userStatus":'Delete',
							"enabled":enableDis[$('#endis-'+i).val()],
							"id":$("#user_id-"+i).text()
					};
					users.push(usersValue);
					$("#user_id-"+i).parent().parent().remove();
				}
				else if($("#status-"+i).text()=='Add'){
					var usersValue={
							"id":$("#user_id-"+i).text(),
							"firstName":$("#firstName-"+i).text().trim(),
							"lastName":$("#lastName-"+i).text().trim(),
							"username":$("#userId-"+i).text().trim(),
							"email":$("#emailId-"+i).text().trim(),
							"enabled":enableDis[$('#endis-'+i).val()],
							"userStatus":$("#status-"+i).text()
					};
					users.push(usersValue);
				}
				else if($("#status-"+i).text()=='Update'){
					var usersValue={
							"firstName":$("#firstName-"+i).text().trim(),
							"lastName":$("#lastName-"+i).text().trim(),
							"username":$("#userId-"+i).text().trim(),
							"email":$("#emailId-"+i).text().trim(),
							"userStatus":'Update',
							"enabled":enableDis[$('#endis-'+i).val()],
							"id":$("#user_id-"+i).text()
					};
					users.push(usersValue);
				}
			}
			var useSSLCheck = false;
			if($('#useSSLCheck').is(':checked')){
				useSSLCheck = true;
			}
			var data={
					"email":
							{
	                                "server":$("#emailServer").val().trim(),
	                                "port":$("#emailServerPort").val().trim(),
	                                "userName":$("#userName").val().trim(),
	                                "emailAddress":$("#emailAccount").val().trim(),
	                                "password":$("#accountPassword").val(),
	                                "secured" : useSSLCheck
							},
							
				"serverIP":
							{
	                                "publicIp":$("#publicIpServer").val().trim(),
	                                "port":$("#publicIpServerPort").val().trim()
							},
				"users":users,
				
			};
			var url = baseUrl + '/app/conf';
			com.impetus.ankush.placeAjaxCall(url,"POST",false,data,
					function(result){
						$('#applyConfiguration').button('reset');
						var userMap = result.output.user;
						var userMapLength = Object.keys(userMap).length;
						if(userMapLength != 0){
							for(key in userMap){
								if(userMap[key].userStatus == "None"){
									$('.'+key).text(userMap[key].id);
									if($('.'+key).attr('id') != undefined){
										var id = $('.'+key).attr('id');
										var index = id.split("-")[1];
										$('#status-'+index).text("None");
									}
								}
							}
						}
						if(result.output.status === true){
							$(location).attr('href',(baseUrl + '/dashboard'));
						}else{
							com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,'popover-content', 'error-div', 'error-header-button');
						}
					},
					function(result){$('#applyConfiguration').button('reset');}
			);
		},
		
		//method to revert the Configuration changes.Loads the CommonConfiguration page with the current state of DataBase.
		revertConfiguration : function(){
			$('#error-div').hide();
			$('#errorBtn').hide();
			com.impetus.ankush.configurationNew.initConfig();
		},
		
		//this method deletes user from User-Table,called from editRow() & loadUserTableSection() methods.
		deleteUser : function(elem){
			var id=$(elem).attr('id');
			var rowNo = id.split('-');
			var rowCount = 0;
			var rowToDelete = 0;
			$('#userDatatable tbody tr').each(function(){
				if($(this).is(':visible')){
					var elemToDelete = $(this).children().first().children().first();
					var deleteIdArray = elemToDelete.attr('id').split('-');
					if(rowNo[1] == deleteIdArray[1])
						rowToDelete = rowCount;
					rowCount++;
				}
			});
			if($("#status-"+rowNo[1]).text() == "Add"){
				userDataTable.fnDeleteRow(rowToDelete);
   			}else{
   				$("#status-"+rowNo[1]).text('Delete');
   				$('#'+id).parents('tr').hide();
   		 	}
			$("#deleteUserConfirmDialog").appendTo("body").modal('show');
		},
		
		/*method is called to check client-side validation for User-table  on Apply button click and 
		  also makes a call to validate() method in order to validate email & server sections.*/
		emptyFieldValidation : function(){
		$('#popover-content').empty();
		com.impetus.ankush.configurationNew.errorCount = 0;
		com.impetus.ankush.configurationNew.mainFlag = true;
		if(userDataTable.fnGetData().length == 0){
			com.impetus.ankush.configurationNew.errorCount++;
			com.impetus.ankush.configurationNew.mainFlag = false,
			errorMessage = com.impetus.ankush.configurationNew.errorCount + ". Please fill at least one user in User Table";
			$("#popover-content").append('<div class="errorLineDiv"><a href="#userDatatable">'+errorMessage+'</a></div>');
		}else{
			var firstNameMsgCount = 0;
			var lastNameMsgCount = 0;
			var userIdMsgCount = 0;
			var emailIdMsgCount = 0;
			$('#userDatatable tbody tr').each(function(){
				if($(this).is(':visible')){
					var elemToDelete = $(this).children().first().children().first();
					var idIndex = elemToDelete.attr('id').split('-')[1];
					var firstName = document.getElementById('firstName-'+idIndex);
					if((com.impetus.ankush.configurationNew.updateStatus(firstName)) || ($("#firstName-"+idIndex).text() == 'Empty') && ($("#firstName-"+idIndex).is(':visible'))){
						firstNameMsgCount ++;
						if(firstNameMsgCount == 1){
							com.impetus.ankush.configurationNew.errorCount++;
							com.impetus.ankush.configurationNew.mainFlag = false,
							firstNameMsg = "";
							errorMessage = "First Name is an alphanumeric input and cannot be blank";
							$("#popover-content").append('<div class="alert alert-danger alert-dismissible" role="alert">'
				            		+'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
				            		+'<a class="alert-link">'+errorMessage+'</a></div>');
						}
					}
					var lastName = document.getElementById('lastName-'+idIndex);
					if((com.impetus.ankush.configurationNew.updateStatus(lastName)) || ($("#lastName-"+idIndex).text() == 'Empty') && ($("#lastName-"+idIndex).is(':visible'))){
						lastNameMsgCount ++;
						if(lastNameMsgCount == 1){
							com.impetus.ankush.configurationNew.errorCount++;
							com.impetus.ankush.configurationNew.mainFlag = false,
							errorMessage = "Last Name is an alphanumeric input and cannot be blank";
							$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="#userDatatable" class="alert-link">'+errorMessage+'</a></div>');
						}
					}
					var UserId = document.getElementById('userId-'+idIndex);
					if((com.impetus.ankush.configurationNew.updateStatus(UserId)) || (($("#userId-"+idIndex).text() == 'Empty') && ($("#userId-"+idIndex).is(':visible')))){
						userIdMsgCount ++;
						if(userIdMsgCount == 1){
							com.impetus.ankush.configurationNew.errorCount++;
							com.impetus.ankush.configurationNew.mainFlag = false,
							errorMessage = "User ID is an alphanumeric non empty unique ID less than 50 and more than 4 character.";
							$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="#userDatatable" class="alert-link">'+errorMessage+'</a></div>');
						}
					}
					var email = document.getElementById('emailId-'+idIndex);
					if((com.impetus.ankush.configurationNew.updateStatus(email)) || ($("#emailId-"+idIndex).text() == 'Empty') && ($("#emailId-"+idIndex).is(':visible'))){
						emailIdMsgCount ++;
						if(emailIdMsgCount == 1){
							com.impetus.ankush.configurationNew.errorCount++;
							com.impetus.ankush.configurationNew.mainFlag = false,
							errorMessage = "Email should have a proper format e.g. johndoe@company.com";
							$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="#userDatatable" class="alert-link">'+errorMessage+'</a></div>');
						}
					}
				}
			});
		}
		com.impetus.ankush.configurationNew.validate();
			$('#error-header-button').show().html('Error '+"<span class='badge'>"+com.impetus.ankush.configurationNew.errorCount+"</span>");
		},
		
		//method validates(client-side) email & server section for multiple validations and call iterateValidationFields(validationFields) for this.
		validate : function(){
			validationFields = {
					"emailServer" :{
						"empty":true,
						"maxValue":true
					},
					"emailServerPort":{
						"empty":true,
						"numeric":true,
						"maxValue":true,
						"portRange":true,
					},
					"emailAccount":{
						"empty":true,
						"email":true,
						"maxValue":true
					},
					"publicIpServer":{
						"empty":true,
						"ipAddressOrHost":true,
						"maxValue":true
					},
					"publicIpServerPort":{
						"empty":true,
						"numeric":true,
						"maxValue":true
					}
			};
			com.impetus.ankush.configurationNew.iterateValidationFields(validationFields);
			$('#error-div').show();
		},
		
		/*method is called from validate() method.It extract key from validationFields(all its keys are iterated) 
			and passes the key(it is basically the id of the field on which multiple validations needs to be applied) 
			and its corresponding value(object) to validateKey() method one by one*/ 
		iterateValidationFields:function(validationFields){
			for(key in validationFields){
				com.impetus.ankush.configurationNew.validateKey(key,validationFields[key]);
			}
		},
		
		/*This method makes a call to actualValidate() method and passes the fieldId and 
		 	all the keys for that fieldId to that method.*/
		validateKey:function(fieldId,fieldValidationData){
			for(key in fieldValidationData){
				var flag = com.impetus.ankush.configurationNew.actualValidate(key,fieldId);
				if(!flag){
					com.impetus.ankush.configurationNew.mainFlag = false;
					break;
				}
			}
		},
		
		/*This method makes a call to another methods who are actually taking-care of client-side validation.
		  The call to another methods is made depending upon the key passed to it.
		  The validation is applied on fieldId.*/
		actualValidate : function(key,fieldId){
			validationTypes ={
					"empty" : function(fieldId){return com.impetus.ankush.configurationNew.emptyValidation(fieldId);},
					"numeric":function(fieldId){return com.impetus.ankush.configurationNew.numericValidation(fieldId);},
					"email":function(fieldId){return com.impetus.ankush.configurationNew.emailValidation(fieldId);},
					"maxValue":function(fieldId){return com.impetus.ankush.configurationNew.maxValueValidation(fieldId,255);},
					"ipAddressOrHost":function(fieldId){return com.impetus.ankush.configurationNew.ipAddressOrHost(fieldId);},
					"portRange":function(fieldId){return com.impetus.ankush.configurationNew.portRangeValidation(fieldId);},
			};
			return validationTypes[key].call(this,fieldId);
		},
		
		//method changes the key(fieldId) to label so that it can be properly shown on UI when error comes in client-side validation.
		//so it split key(fieldId) with space whenever a Capital letter comes in the key and return the new Key.
		changeKeyToLabel : function(key){
			 return key.split(/(?=[A-Z])/).map(
						function(p) {
							return p.charAt(0).toUpperCase()
									+ p.slice(1);
				}).join(' ');
		},
		
		//method applies the emptyFieldValidation & for that it makes a call to empty() method of ankush.js
		emptyValidation : function(fieldId){
			if(!com.impetus.ankush.validate.empty($("#"+fieldId).val())){
				com.impetus.ankush.configurationNew.errorCount++;
				var errorMsg = com.impetus.ankush.configurationNew.changeKeyToLabel(fieldId) + " cannot be blank";
				$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="javascript:com.impetus.ankush.configurationNew.focusError(\''+ fieldId + '\');"class="alert-link">' + errorMsg+'</a></div>');
				com.impetus.ankush.configurationNew.tooltipMsgChange_Error(fieldId,errorMsg);
				return false;
			}
			return true;
		},
		
		//method applies the numericValidation & for that it makes a call to numeric() method of ankush.js
		numericValidation:function(fieldId){
			if(!com.impetus.ankush.validate.numeric($("#"+fieldId).val())){
				com.impetus.ankush.configurationNew.errorCount++;
				var errorMsg = com.impetus.ankush.configurationNew.changeKeyToLabel(fieldId) + " can be only Numeric";
				$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="javascript:com.impetus.ankush.configurationNew.focusError(\''+ fieldId + '\');"class="alert-link">'+ errorMsg+'</a></div>');
				com.impetus.ankush.configurationNew.tooltipMsgChange_Error(fieldId,errorMsg);
				return false;
			}
			return true;
		},
		//method applies the portRangeValidation & for that it makes a call to numeric() method of ankush.js
		portRangeValidation:function(fieldId){
			if(!com.impetus.ankush.validate.port($("#"+fieldId).val())){
				com.impetus.ankush.configurationNew.errorCount++;
				var errorMsg = com.impetus.ankush.configurationNew.changeKeyToLabel(fieldId) + " can be between 0-65535";
				$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="javascript:com.impetus.ankush.configurationNew.focusError(\''+ fieldId + '\');"class="alert-link">' + errorMsg+'</a></div>');
				com.impetus.ankush.configurationNew.tooltipMsgChange_Error(fieldId,errorMsg);
				return false;
			}
			return true;
		},
		
		//method applies the emailValidation & for that it makes a call to email() method of ankush.js
		emailValidation:function(fieldId){
			if(!com.impetus.ankush.validate.email($("#"+fieldId).val())){
				com.impetus.ankush.configurationNew.errorCount++;
				var errorMsg = com.impetus.ankush.configurationNew.changeKeyToLabel(fieldId) + " should be a valid emailId.";
				$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="javascript:com.impetus.ankush.configurationNew.focusError(\''+ fieldId + '\');"class="alert-link">'+ errorMsg+'</a></div>');
				com.impetus.ankush.configurationNew.tooltipMsgChange_Error(fieldId,errorMsg);
				return false;
			}
			return true;
		},
		
		//method applies the maxValueValidation & for that it makes a call to maxValue() method of ankush.js
		maxValueValidation:function(fieldId,maxLength){
			if(!com.impetus.ankush.validate.maxValue($("#"+fieldId).val(),maxLength)){
				com.impetus.ankush.configurationNew.errorCount++;
				var errorMsg = com.impetus.ankush.configurationNew.changeKeyToLabel(fieldId) + " cannot be more than " + maxLength;
				$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="javascript:com.impetus.ankush.configurationNew.focusError(\''+ fieldId + '\');"class="alert-link">' + errorMsg+'</a></div>');
				com.impetus.ankush.configurationNew.tooltipMsgChange_Error(fieldId,errorMsg);
				return false;
			}
			return true;
		},
		
		//method applies the confirmPasswordValidation & for that it makes a call to confirmPassword() method of ankush.js
		confirmPasswordValidation:function(fieldId){
			if(!com.impetus.ankush.validate.confirmPassword($("#"+fieldId).val(),$("#accountPassword").val())){
				com.impetus.ankush.configurationNew.errorCount++;
				var errorMsg = com.impetus.ankush.configurationNew.changeKeyToLabel(fieldId) + " should be same as above password";
				$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="javascript:com.impetus.ankush.configurationNew.focusError(\''+ fieldId + '\');" class="alert-link">'+ errorMsg+'</a></div>');
				com.impetus.ankush.configurationNew.tooltipMsgChange_Error(fieldId,errorMsg);
				return false;
			}
			return true;
		},
		
		//method applies the ipAddressOrHost & for that it makes a call to ipAddressOrHost() method of ankush.js
		ipAddressOrHost : function(fieldId){
			if(!com.impetus.ankush.validate.ipAddressOrHost($("#"+fieldId).val())){
				com.impetus.ankush.configurationNew.errorCount++;
				var errorMsg = com.impetus.ankush.configurationNew.changeKeyToLabel(fieldId) + " should be a valid IP";
				$("#popover-content").append('<div class="alert alert-danger alert-dismissible"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="javascript:com.impetus.ankush.configurationNew.focusError(\''+ fieldId + '\');">'+ errorMsg+'</a></div>');
				com.impetus.ankush.configurationNew.tooltipMsgChange_Error(fieldId,errorMsg);
				return false;
			}
			return true;
		},
		
		//method to focus errorMessage Div when user click on a button showing totalErrorCount{ ie. 2 Errors}
		focusErrorMsgDiv:function(){
			//$('#popover-content').focus();
			//$("#popover-content").attr("tabindex",-1).focus();
			//$(window).scrollTop(0);
			$(window).scrollTop(0);
		},
		
		//method is used to create tooltip on each field of CommonConfiguration page
		tooltip:function(){
			$("#emailServer").tooltip();
			$("#emailServerPort").tooltip();
			$("#userName").tooltip();
			$("#emailAccount").tooltip();
			$("#accountPassword").tooltip();
			$("#confirmPassword").tooltip();
			$("#publicIpServer").tooltip();
			$("#publicIpServerPort").tooltip();
			$("#noOfClusters").tooltip();
			$("#noOfNodes").tooltip();
			$("#validity").tooltip();
			$("#noOfUser").tooltip();
			$("#comments").tooltip();
			$("#useSSLCheck").tooltip();
			
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

		},
		/*method to getProperNoun as per error-count.
		  This method must be written here rather than using the same method written 
		  in ankush.dashboard.js as we need configurations-settings to be done before login as well
		  and at beforLogin.jsp page we shouldn't include ankush.dashboard.js file just only for this method.*/
		getProperNounForError : function(errorCount){
			var errorCountMsg = errorCount + " Errors";
			if(errorCount == 1){
				errorCountMsg = errorCount + " Error";
			}
			return errorCountMsg;
		},
		// Function to remove the Fake Path text in Chrome & Safari browsers during file upload
        removeFakePathFromPath : function(fieldId) {
                var value = $('#' + fieldId).val().replace('C:\\fakepath\\', '');
                $('#' + fieldId).val(value);
        },

        // Function to initialise the JAR file upload functionality
        
        
        // Function to upload the JAR file to the Ankush Server         
       
       
	openTestMailDialog : function(){
		$("#sendMail").appendTo('body').modal();
		setTimeout(function(){
			$("#mailtoTest").focus();
			$("#validEmailText").removeClass('text-danger').addClass('text-default').text("An email will be sent to above email Id.");
		},500);
	},
	validationMail : function(){
		if(!com.impetus.ankush.validate.email($("#mailtoTest").val())){
			$("#validEmailText").removeClass('text-default').addClass('text-danger').text("Enter Valid Email.");
		}else{
			var data = {
					"server" : $("#emailServer").val(),
					"port" : $("#emailServerPort").val(),
					"secured" : $("#useSSLCheck").prop(":checked"),
					"emailAddress" : $("#emailAccount").val(),
					"userName" : $("#userName").val(),
					"password" : $("#accountPassword").val()
					};
			var url = baseUrl+ '/app/conf/testMailConf/'+$("#mailtoTest").val();
			com.impetus.ankush.placeAjaxCall(url,'POST',true,data,function(result){
				if(result.output.mailSendStatus){
					$("#sendMail").modal('hide');
				}else{
					$("#validEmailText").removeClass('text-default').addClass('text-danger').text("An Email was not sent. Please check mail configuration.");
				}
			});
		}
	},
	
};
