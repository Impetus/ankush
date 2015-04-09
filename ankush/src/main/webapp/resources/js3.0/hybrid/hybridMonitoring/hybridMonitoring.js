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
var hybridDetailsTable=null;
var nodeStatus = {};
var jsonDataHybrid = {};
var zookeeperNodeObj = {};
var postDataAddnode = {};
var disableNodeCount = 0;
var fileIPAddress_ServerPath = null;
var addNodeIpRole = {};
var manipulateCheckBox = {};
var clusterComponentsResponseObject = null;
com.impetus.ankush.hybridMonitoring={
	  techTableCreate:function(){
		  if(hybridDetailsTable!=null){
			  hybridDetailsTable.fnClearTable();
		  }
		  var url = baseUrl + '/monitor/' + clusterId + '/clustercomponents';	
		  com.impetus.ankush.placeAjaxCall(url, "GET", true, null, function(result) {
			  		  clusterComponentsResponseObject = result.output.components;	
		              if(result.output.status){
				  		  var hybridTechData=result.output;
				  		  hybridTechnologiesData = result.output.components;
		            	  var img='<a href="##"><img id="navigationImg-' + tech+ '" src="' + baseUrl + '/public/images/icon-chevron-right.png" /></a>';
		            	  for(var key in result.output.components){
		            		  var monitoringTechUrl = baseUrl + '/hybrid-monitoring/'+clusterName+'/'+key+'/C-D/'+clusterId+'/'+clusterTechnology;
		            		  var tech = key;
		            		img='<a href="'+monitoringTechUrl+'"><img id="navigationImg-' + tech+ '" src="' + baseUrl + '/public/images/icon-chevron-right.png" /></a>';
							if((key == 'Ganglia'))
								img = '';
							var addIdParent = hybridDetailsTable
		            				.fnAddData([
		            		                       '<ins onclick="com.impetus.ankush.hybridMonitoring.toggleParentRow(this,\''+key+'\')"></ins>',
		            		                       key.indexOf("Zookeeper") > -1 ? "Zookeeper" : key,
		      				                       result.output.components[key].nodes,
		      				                       '<span>' + result.output.components[key].vendor + '</span>',
		      				                       '<span>'+ result.output.components[key].version + '</span>',
		      				                       img,
		      				                       ]);
		            		var theNodeParent = hybridDetailsTable.fnSettings().aoData[addIdParent[0]].nTr;
		            		theNodeParent.setAttribute('class', 'parent-row-' + key);
		            		for(var key1 in result.output.components[key].roles){
		            			var addIdChild = hybridDetailsTable
		            			.fnAddData([
		   		            		                       '',
		   		      				                       '<span style="margin-left:20px;">'+key1+'</span>',
		   		      				                       result.output.components[key].roles[key1],
		   		      				                       '',
		   		      				                       '',
		   		      				                       '',
		   		      				                       ]);
		            			var theNodeChild = hybridDetailsTable.fnSettings().aoData[addIdChild[0]].nTr;
		            			theNodeChild.setAttribute('class', 'child-row-' + key);
		            		}
		      			}
		              }
	            	  /*if(result.output.registerableCluster){
	            		  delete com.impetus.ankush.commonMonitoring.Hybrid.actions['Add Nodes...'];
	            	  }*/
		   });
		},
		//this function will use as a rowgrouping
		toggleParentRow : function(elem,key){
			if($('.child-row-'+key).is(':visible')){
				$(elem).css('background-position','-314px -118px');
			}else{
				$(elem).css('background-position','-288px -118px');
			}
			$('.child-row-'+key).toggle();
		},
		
		
			
			
			
			
};
