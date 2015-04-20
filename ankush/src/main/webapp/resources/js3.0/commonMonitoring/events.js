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
com.impetus.ankush.events = {
		//this function will populate events page
		commonEvents : function(){
			var eventsUrl = baseUrl + '/monitor/' + clusterId + "/events";
			com.impetus.ankush.placeAjaxCall(eventsUrl, 'GET', true,null,function(eventsData){
				if(eventsTable != null)
					eventsTable.fnClearTable();
				if(!eventsData.output.status){
					eventsTable.fnSettings().oLanguage.sEmptyTable = '<span class="text-danger">'+eventsData.output.error[0]+'</span>';
					eventsTable.fnClearTable();
					return;
				}else if (eventsData.output.events && eventsData.output.events.length > 0) {
					var eventsLength = eventsData.output.events.length;
					var eventsOutput = [];
					eventsOutput = eventsData.output.events;
					var mainList = [];
					for ( var i = 0; i < eventsLength; i++) {
						var dataList = [];
						var tempDate = new Date(parseInt(eventsOutput[i].date));
						var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
						var eventName = eventsOutput[i].name;
						var eventType = eventsOutput[i].type;
						var eventSeverity =eventsOutput[i].severity;
						var eventStatus =eventsOutput[i].value;
						var eventHost = eventsOutput[i].host;
						var eventTime = date;
						if(eventsOutput[i].type === 'USAGE'){
							eventStatus = eventStatus+'%';
						}
						if(eventsOutput[i].severity === 'NORMAL'){
							eventSeverity = '<span class="fa fa-check-circle text-success"></span>&nbsp;&nbsp;'+eventSeverity;
						}else if(eventsOutput[i].severity === 'CRITICAL'){
							eventSeverity = '<span class="fa fa-warning text-danger"></span>&nbsp;&nbsp;'+eventSeverity;
						}if(eventsOutput[i].severity === 'WARNING'){
							eventSeverity = '<span class="fa fa-warning text-warning"></span>&nbsp;&nbsp;'+eventSeverity;
						} 
						dataList.push(eventName);
						dataList.push(eventHost);
						dataList.push(eventType);
						dataList.push(eventSeverity);
						dataList.push(eventStatus);
						dataList.push(eventTime);
						mainList.push(dataList);
					}
					eventsTable.fnAddData(mainList);
				}else{
					eventsTable.fnSettings().oLanguage.sEmptyTable = "No data Available";
					eventsTable.fnClearTable();
				}
				
			});
			
		}
};
		
		
