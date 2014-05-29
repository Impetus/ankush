/*******************************************************************************
*===========================================================
*Ankush : Big Data Cluster Management Solution
*===========================================================
*
*(C) Copyright 2014, by Impetus Technologies
*
*This is free software; you can redistribute it and/or modify it under
*the terms of the GNU Lesser General Public License (LGPL v3) as
*published by the Free Software Foundation;
*
*This software is distributed in the hope that it will be useful, but
*WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*See the GNU Lesser General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License 
*along with this software; if not, write to the Free Software Foundation, 
*Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/

com.impetus.ankush.commonMonitoring.commonEvents = function(){
			/*if((com.impetus.ankush.commonMonitoring.clusterId == null) || (com.impetus.ankush.commonMonitoring.clusterId == undefined))
				return ;*/
			var eventsTable = $('#eventsTable').dataTable({
				"bJQueryUI" : true,
				"bPaginate" : false,
				"bLengthChange" : true,
				"bFilter" : true,
				"bSort" : true,
				"bInfo" : false,
				"bAutoWidth" : false,
				"sPaginationType" : "full_numbers",
				"bAutoWidth" : false,
				"aaSorting": [[5,'desc']],
				"aoColumnDefs": [
				                 { 'sType': "date", 'aTargets': [5] }
				                ], 
			});
			$('#eventsTable_filter').hide();
			$('#searchEvents').keyup(function() {
				$("#eventsTable").dataTable().fnFilter($(this).val());
			});
			var eventsUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + "/events";
			var eventsData = com.impetus.ankush.placeAjaxCall(eventsUrl, 'GET', false);
			var eventsLength = eventsData.output.events.length;
			var eventsOutput = [];
			eventsOutput = eventsData.output.events;
			var mainList = [];
			if (eventsLength > 0) {
				for ( var i = 0; i < eventsLength; i++) {
					var dataList = [];
					var tempDate = new Date(parseInt(eventsOutput[i].date));
					var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
					var eventName = eventsOutput[i].name;
					var eventType = eventsOutput[i].type;
					var eventSeverity =eventsOutput[i].severity;
					var eventHost = eventsOutput[i].host;
					var eventDescription = eventsOutput[i].currentValue;
					var eventTime = date;
					com.impetus.ankush.commonMonitoring.eventRows[i] = eventsOutput[i];
					dataList.push(eventName);
					dataList.push(eventType);
					dataList.push(eventSeverity);
					dataList.push(eventHost);
					dataList.push(eventDescription);
					dataList.push(eventTime);
					mainList.push(dataList);
				}
			}
			eventsTable.fnAddData(mainList);
		};
		
		
