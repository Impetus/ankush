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
var templateTable=null;
var templateName='';
com.impetus.ankush.template={
		
		//function for populating templates on template page
		getTemplate:function(){
			if (templateTable != null) {
				templateTable.fnClearTable();
			}
			var url = baseUrl +'/cluster/templates';
			com.impetus.ankush.placeAjaxCall(url,"GET",true,null,function(result) {
								for ( var i = 0; i < result.output.length; i++) {
										var tempDate = new Date(parseInt(result.output[i].updateDate));
										date = $.format.date(tempDate,"dd/MM/yyyy hh:mm:ss");
										templateTable.fnAddData([
									                  '<a class="" style="text-decoration:underline" href="#" onclick="com.impetus.ankush.template.loadTemplatePage(\''+result.output[i].name+'\',\''+result.output[i].technology+'\')">'+result.output[i].name+'</a>',
									                  result.output[i].technology,
									                  result.output[i].user,
									                 /* '<label>Email</label>',*/
									                  date,
									                  '<div  class="deleteTemplate" onclick="com.impetus.ankush.template.confirmDeleteDialog(this)" style="float:right;margin-right:5px;" id="'+ result.output[i].name+'"><ins></ins></div>',
										]);
								}
			});
		},
//function for template deletion confirmation dialog 
		confirmDeleteDialog:function(elem){
			 templateName=$('td:first', $(elem).parents('tr')).text();
			 $('#confirmDeleteTemplate').modal('show');
		},
		//function for deleting template
		deleteTemplate:function(elem){
		url = baseUrl + '/cluster/template?name='+templateName;
		com.impetus.ankush.placeAjaxCall(url,"DELETE",true,null,function(result) {
			com.impetus.ankush.template.getTemplate();
		});
		 $('#confirmDeleteTemplate').modal('hide');
		},
		
		//function for loading home page of cluster creation
		loadTemplatePage:function(templateName,techName){
			//com.impetus.ankush.removeCurrentParent();
			com.impetus.ankush.selectTechnology.createCluster('hybrid-cluster',templateName,techName);
			/*$("#templateIcon").removeClass("selected");
			$("#dashboardIcon").addClass("selected");*/
			//com.impetus.ankush.hybridSetupDetail.loadTemplate(templateName);
		}				
};