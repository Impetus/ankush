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

com.impetus.ankush.hadoopMonitoring = {
		// this function will populate tiletable for index drill down and node drill
		// down
		populateTileTables : function(tableName) {
			var url = baseUrl + '/monitor/'+clusterId+'/'+tableName;;
			if(hybridTechnology !== null)
				url = baseUrl + '/monitor/'+clusterId+'/'+tableName+'?component='+hybridTechnology;
				com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
					if (eval(tableName+'Table') != null)
						eval(tableName+'Table').fnClearTable();
					if(result.output.status == true){
						for ( var dataKey in result.output.data) {
							var secondColumn = result.output.data[dataKey];
							if(result.output.data[dataKey].indexOf('http://') !== -1){
								secondColumn = '<a href="'+result.output.data[dataKey]+'" target="newTab">'+result.output.data[dataKey]+'</a>';
							}
							eval(tableName+'Table').fnAddData([
							                           dataKey,
							                           secondColumn 
							                          ]);
						}
					}else{
						eval(tableName+'Table').fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						eval(tableName+'Table').fnClearTable();
					}
				});
		},
		populateProcessSummaryTables : function(tableName,process) {
			$("."+process).html(process);
				url = baseUrl + '/monitor/'+clusterId+'/'+tableName+'?component='+hybridTechnology+'&process='+process;
				com.impetus.ankush.placeAjaxCall(url,'GET',true,null,function(result){
					if (eval(tableName+'Table'+process) != null)
						eval(tableName+'Table'+process).fnClearTable();
					if(result.output.status == true){
						for ( var dataKey in result.output.data) {
							eval(tableName+'Table'+process).fnAddData([
							                           dataKey,
							                           result.output.data[dataKey] 
							                          ]);
						}
					}else{
						eval(tableName+'Table'+process).fnSettings().oLanguage.sEmptyTable = result.output.error[0];
						eval(tableName+'Table'+process).fnClearTable();
					}
				});
		},
		
		
		

};
