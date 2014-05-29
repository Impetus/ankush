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

com.impetus.ankush.commonMonitoring.commonAuditTrail =  function() {
	if((com.impetus.ankush.commonMonitoring.clusterId == null) || (com.impetus.ankush.commonMonitoring.clusterId == undefined))
		return ;
	var auditTrailTable = $('#auditTrailTable').dataTable({
		"aoColumns" : [ {
			"sWidth" : "23%"
		}, {
			"sWidth" : "5%"
		}, {
			"sWidth" : "24%"
		}, {
			"sWidth" : "10%"
		}, {
			"sWidth" : "6%"
		}, {
			"sWidth" : "22%"
		}, {
			"sWidth" : "10%"
		}, ],
		"bJQueryUI" : false,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers"
	});
	$('#auditTrailTable_filter').hide();
	$('#searchAuditTrail').keyup(function() {
		$("#auditTrailTable").dataTable().fnFilter($(this).val());
	});
	var auditTrailUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/audits';
	var auditTrailData = com.impetus.ankush.placeAjaxCall(auditTrailUrl,
			'GET', false);
	if(auditTrailData.output == null)
		return;
	var auditTrailLength = auditTrailData.output.length;
	var auditTrailOutput = auditTrailData.output;
	var mainList = [];
	if (auditTrailLength > 0) {
		for ( var i = 0; i < auditTrailLength; i++) {
			var dataList = [];
			var tempDate = new Date(parseInt(auditTrailOutput[i].date));
			var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
			var fileName = '<span style="font-weight:bold;" id="fileName-'
				+ i + '"/>' + auditTrailOutput[i].source + '</span>';
			var auditType = '<span style="font-weight:bold;" id="auditType-'
				+ i + '">' + auditTrailOutput[i].type + '</span>';
			var auditPropertyName = '<span style="font-weight:bold;" id="auditPropertyName-'
				+ i
				+ '">'
				+ auditTrailOutput[i].propertyName
				+ '</span>';
			var auditPropertyValue = '<span style="font-weight:bold;" id="auditPropertyValue-'
				+ i
				+ '">'
				+ auditTrailOutput[i].propertyValue
				+ '</span>';
			var auditUser = '<span style="font-weight:bold;" id="auditUser-'
				+ i + '">' + auditTrailOutput[i].username + '</span>';
			var auditTime = '<span style="font-weight:bold;" id="auditTime-'
				+ i + '">' + date + '</span>';
			var childPageLink = '<a href="#" id="navigationImgAdd'
				+ i
				+ '" onclick="com.impetus.ankush.commonMonitoring.auditTrailDetail('
				+ i + ');"><img src="' + baseUrl
				+ '/public/images/icon-chevron-right.png"/></a>';
			com.impetus.ankush.commonMonitoring.auditTrailRows[i] = auditTrailOutput[i];
			dataList.push(fileName);
			dataList.push(auditType);
			dataList.push(auditPropertyName);
			dataList.push(auditPropertyValue);
			dataList.push(auditUser);
			dataList.push(auditTime);
			dataList.push(childPageLink);
			mainList.push(dataList);
		}
	}
	auditTrailTable.fnAddData(mainList);
};
