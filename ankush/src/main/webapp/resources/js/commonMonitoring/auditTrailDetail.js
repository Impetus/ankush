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

//this function will create audittrail page on monitoring page
		com.impetus.ankush.commonMonitoring.auditTrailDetail = function(i) {
			//com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/auditTrailDetails','get','Audit Trails Details','Audit Trails');
			$('#content-panel').sectionSlider('addChildPanel', {
				url : baseUrl + '/commonMonitoring/auditTrailDetails',
				method : 'get',
				title : 'Audit Trails Details',
				tooltipTitle : 'Audit Trails',
				callback : function(){
					com.impetus.ankush.commonMonitoring.fillAuditTrailDetails(i);
				}
			});
		};
		//this will fill child page of audit trails table
		com.impetus.ankush.commonMonitoring.fillAuditTrailDetails = function(i){
			var tempDate = new Date(parseInt(com.impetus.ankush.commonMonitoring.auditTrailRows[i].date));
			var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
			$('#commonSource').text(com.impetus.ankush.commonMonitoring.auditTrailRows[i].source);
			$('#commonType').text(com.impetus.ankush.commonMonitoring.auditTrailRows[i].type);
			$('#commonPropertyName').text(com.impetus.ankush.commonMonitoring.auditTrailRows[i].propertyName);
			$('#commonPropertyValue').text(com.impetus.ankush.commonMonitoring.auditTrailRows[i].propertyValue);
			$('#commonUserName').text(com.impetus.ankush.commonMonitoring.auditTrailRows[i].username);
			$('#commonHost').text(com.impetus.ankush.commonMonitoring.auditTrailRows[i].host);
			$('#commonDate').text(date);
		};
