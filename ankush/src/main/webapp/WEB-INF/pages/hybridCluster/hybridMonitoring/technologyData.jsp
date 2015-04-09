<!------------------------------------------------------------------------------
-  ===========================================================
-  Ankush : Big Data Cluster Management Solution
-  ===========================================================
-  
-  (C) Copyright 2014, by Impetus Technologies
-  
-  This is free software; you can redistribute it and/or modify it under
-  the terms of the GNU Lesser General Public License (LGPL v3) as
-  published by the Free Software Foundation;
-  
-  This software is distributed in the hope that it will be useful, but
-  WITHOUT ANY WARRANTY; without even the implied warranty of
-  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-  See the GNU Lesser General Public License for more details.
-  
-  You should have received a copy of the GNU Lesser General Public License 
-  along with this software; if not, write to the Free Software Foundation, 
- Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->

<!-- Fields showing Hadoop Cluster Details & link to its Advanced Settings on Cluster Monitoring main page  -->
<style>
 .table-striped tbody > tr:nth-child(2n) > td, .table-striped tbody > tr:nth-child(2n) > th {
    background-color: #F9F9F9;
}
.table-striped tbody > tr:nth-child(2n+1) > td, .table-striped tbody > tr:nth-child(2n+1) > th {
    background-color: #FFFFFF;
}
#technologyDivsForData > .panel > .panel-heading:after {
    border-bottom: 0px;
    content: "";
    display: block;
    height: 0;
    padding-bottom: 0px;
}
</style>
<script src="<c:out value='${baseUrl}' />/public/js3.0/technologyData.js" type="text/javascript"></script>	

<div class="panel-group" id="technologyDivsForData" role="tablist" aria-multiselectable="true">
</div>
<script>	
function NodeLevelCalls(tech,page,jsfile){
	var jsLink = $("<script type='text/javascript' src='<c:out value="${baseUrl}" />/public/js3.0/"+jsfile+"'>");
    $("head").append(jsLink); 
	var technology = tech;
	var url = baseUrl+'/'+technology.charAt(0).toLowerCase() + technology.slice(1)+'Monitoring/'+page;
	$.ajax({
     	  type: 'GET',
     	  'url': url,
     	  'async':false,
     	  'success' : function(data) {
     		$('#'+tech+'-data-populate').html('').append(data);
        },
        error : function() {
        	$('#'+tech+'-data-populate').parent().prev().removeClass('loadingNodeData');
        }
});
}
	$(document).ready(function() {
		var hash = {
				'Cassandra' : {
					"Page" : 'nodeOverview',
					"jsFile" : 'cassandra/nodeOverview.js',
				},
		};
		var techListUrl =  baseUrl+'/monitor/'+clusterId+'/components?host='+hostName;
		setTimeout(function(){
			com.impetus.ankush.placeAjaxCall(techListUrl,'GET',true,null,function(result){
				if(result.output.status == true){
					for(var i = 0 ; i < result.output.components.length ; i++){
						var technology = result.output.components[i];
						if((technology === 'Cassandra')){
							$('#technologyDivsForData').append(com.impetus.ankush.technologyData.getTemplateAccordian(technology));
							NodeLevelCalls(technology,hash[technology].Page,hash[technology].jsFile);
						}
					}
				}else{
					
				}
				
			});
		},1000)
	});
</script>

