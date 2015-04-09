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
com.impetus.ankush.technologyData = {
		getTemplateAccordian : function(technology){
	    	  var template = '<div class="panel panel-default">'+
	    	    '<div class="panel-heading" role="tab" id="headingOne">'+
	    	      '<h4 class="panel-title">'+
	    	        '<a data-toggle="collapse" data-parent="#technologyDivsForData" href="#collapse-'+technology+'" aria-expanded="false" aria-controls="collapse-'+technology+'">'+technology+'</a>'+
	    	      '</h4>'+
	    	    '</div>'+
	    	    '<div id="collapse-'+technology+'" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">'+
	    	      '<div class="panel-body" id="'+technology+'-data-populate"></div>'+
	    	    '</div>'+
	    	  '</div>';
	    	  return template;
	      }
};
