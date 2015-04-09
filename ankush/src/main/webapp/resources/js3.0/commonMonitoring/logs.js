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
var readCount = 0;
com.impetus.ankush.logs = {
	// function used to show diffrent types of node in a cluster, viz. namenode
	// , secondary namenode, etc.
	getDefaultLogDownloadValue : function() {
		$("#fileSizeExceed").appendTo('body');
		readCount = 0;
		var logUrl = baseUrl + '/monitor/' + clusterId + '/techlogs';
		if (clusterTechnology == 'Hybrid')
			logUrl = baseUrl + '/monitor/' + clusterId + '/techlogs?component='
					+ hybridTechnology;
		com.impetus.ankush.placeAjaxCall(logUrl, 'GET', true, null, function(
				data) {
			logData = data;
			if (logData != null) {
				$("#nodeType").empty();
				for ( var type in logData.output) {
					if (type != "status") {
						$("#nodeType").append(
								"<option id='" + type + "'>" + type
										+ "</option>");
					}
				}
				com.impetus.ankush.logs.fillNodeType();
			}
		});
	},
	// function used to get nodes Ip depending on the type of node selected in
	// the node type Dropdown
	fillNodeType : function() {
		com.impetus.ankush.logs.removeErrorClass();
		com.impetus.ankush.logs.diableLogButtons();
		$("#nodeIP").empty();
		var currentNodeType = $("#nodeType").val();
		var numberOfNodes = logData.output[currentNodeType].length;
		logDataOutput = logData.output[currentNodeType];
		$("#div_Logs").css("display", "none");
		if (numberOfNodes > 0) {
			for ( var index = 0; index < numberOfNodes; index++) {
				$("#nodeIP").append(
						"<option id='node-" + index + "'>"
								+ logDataOutput[index] + "</option>");
			}
			com.impetus.ankush.logs.nodeIPChange();
		}
	},
	diableLogButtons : function() {
		$("#viewLogs").attr("disabled", true);
		$("#downloadLogs").attr("disabled", true);
	},

	enableLogButtons : function() {
		$("#viewLogs").removeAttr("disabled");
		$("#downloadLogs").removeAttr("disabled");
	},
	// function used to show all the log files in hadoop cluster of node
	// selected
	nodeIPChange : function() {
		com.impetus.ankush.logs.removeErrorClass();
		com.impetus.ankush.logs.diableLogButtons();
		var currentNodeType = $("#nodeType").val();
		var currentNodeIP = $("#nodeIP").val();
		var logFilenameUrl = baseUrl + '/monitor/' + clusterId + '/files?host='
				+ currentNodeIP + '&type=' + currentNodeType;
		if (clusterTechnology == 'Hybrid') {
			logFilenameUrl = baseUrl + '/monitor/' + clusterId + '/files?host='
					+ currentNodeIP + '&type=' + currentNodeType
					+ '&component=' + hybridTechnology;
		}
		$("#showLoading").removeClass('element-hide');
		com.impetus.ankush
				.placeAjaxCall(
						logFilenameUrl,
						'GET',
						true,
						null,
						function(logFileNameData) {
							$("#showLoading").addClass('element-hide');
							$("#div_Logs").css("display", "none");
							if (logFileNameData != null) {
								if (!logFileNameData.output.status) {
									com.impetus.ankush.validation
											.showAjaxCallErrors(
													logFileNameData.output.error,
													'popover-content',
													'error-div',
													'error-header-button');
									$("#fileName").empty();
								} else {
									$("#error-div").hide();
									$("#error-header-button").hide();
									logFileNameOutputCommon = logFileNameData.output.files;
									$("#fileName").empty();
									var i = 0;
									$.each(logFileNameOutputCommon, function(
											key, value) {
										$("#fileName").append(
												"<option id='filename-" + i
														+ "' value = '" + value
														+ "'>" + key
														+ "</option>");
										i++;
									});
									$("#logView").empty();
									com.impetus.ankush.logs.enableLogButtons();
								}
							}
						});
	},
	// function used to remove error-class
	removeErrorClass : function() {
		$('#nodeIP').removeClass('error-box');
		$('#fileName').removeClass('error-box');
		$("#error-div-hadoopLogs").css("display", "none");
		$('#errorBtnHadoopLogs').css("display", "none");
	},
	// function used to validate whether a log file can be dowloaded or not
	validateLogs : function(urlData, errorType) {
		var focusDivId = null;
		if (errorType == "nodeDown") {
			focusDivId = "nodeIP";
		} else if (errorType == "filename") {
			focusDivId = "fileName";
		}
		$("#popover-content-hadoopLogs").empty();
		$("#error-div-hadoopLogs").css("display", "none");
		$('#errorBtnHadoopLogs').text("");
		$('#errorBtnHadoopLogs').css("display", "none");
		com.impetus.ankush.validation.errorCount = 0;
		var i = 0;
		$.each(urlData.output.error, function(index, value) {
			i = index + 1;
			com.impetus.ankush.validation.addNewErrorToDiv(focusDivId,
					'popover-content-hadoopLogs', value, null);
		});
		if (com.impetus.ankush.validation.errorCount > 0) {
			com.impetus.ankush.validation.showErrorDiv('error-div-hadoopLogs',
					'errorBtnHadoopLogs');
			$('#viewLogs').attr('disabled', true);
			$('#downloadLogs').attr('disabled', true);
			return;
		}
	},
	// function used to display logs of the file selected. Data is loaded using
	// lazy loading
	logDisplay : function() {
		readCount = "0";
		var currentNodeType = $("#nodeType").val();
		var currentNodeIP = $("#nodeIP").val();
		var filePath = $("#fileName").val();
		var data = {
			"host" : currentNodeIP,
			"logFilePath" : filePath,
			"readCount" : readCount,
			"bytesCount" : "10000",
			"type" : currentNodeType
		};
		var logFileURL = baseUrl + '/monitor/' + clusterId + '/view';
		$("#showLoading").removeClass('element-hide');
		com.impetus.ankush
				.placeAjaxCall(
						logFileURL,
						'POST',
						true,
						data,
						function(logFileData) {
							$("#showLoading").addClass('element-hide');
							if (logFileData != null) {
								if (logFileData.output.status) {
									if (logFileData.output.content == "") {
										logFileData.output.content = "No data found in file.";
										$('#downloadLogs').attr('disabled',
												true);
									} else
										$('#downloadLogs').attr('disabled',
												false);
									$("#div_Logs").css("display", "block");
									$("#logView").empty();
									$("#logView").append(
											logFileData.output.content);
									readCount = logFileData.output.readCount;
								} else {
									com.impetus.ankush.validation
											.showAjaxCallErrors(
													logFileData.output.error,
													'popover-content',
													'error-div',
													'error-header-button');
								}
							} else {
								com.impetus.ankush.validation
										.showAjaxCallErrors(
												[ "Sorry, unable to retrieve the Log file" ],
												'popover-content', 'error-div',
												'error-header-button');
							}
						});

	},
	// function used to append log data, during lazy-load, at the end of the
	// previously shown logs
	appendLog : function() {
		if ((($(window).scrollTop() + document.body.clientHeight) == $(window)
				.height())) {
			var currentNodeType = $("#nodeType").val();
			var currentNodeIP = $("#nodeIP").val();
			var filePath = $("#fileName").val();
			var data = {
				"host" : currentNodeIP,
				"logFilePath" : filePath,
				"readCount" : readCount,
				"bytesCount" : "10000",
				"type" : currentNodeType
			};
			var logFileURL = baseUrl + '/monitor/' + clusterId + '/view?host='
					+ currentNodeIP + '&fileName=' + filePath + '&readCount='
					+ readCount + '&bytesCount=10000&type=' + currentNodeType;
			if (clusterTechnology == 'Hybrid') {
				logFileURL = baseUrl + '/monitor/' + clusterId + '/view?host='
						+ currentNodeIP + '&fileName=' + filePath
						+ '&readCount=' + readCount
						+ '&bytesCount=10000&component=' + hybridTechnology
						+ '&type=' + currentNodeType;
			}
			com.impetus.ankush
					.placeAjaxCall(
							logFileURL,
							'POST',
							true,
							data,
							function(logFileData) {
								if (logFileData !== null) {
									if (logFileData.output.status) {
										if (logFileData.output.content == "") {
											logFileData.output.content = "No data found in file.";
											$('#downloadLogs').attr('disabled',
													true);
										} else
											$('#downloadLogs').attr('disabled',
													false);
										$("#div_Logs").css("display", "block");
										$("#logView").append(
												logFileData.output.content);
										readCount = logFileData.output.readCount;
									} else {
										com.impetus.ankush.validation
												.showAjaxCallErrors(
														logFileData.output.error,
														'popover-content',
														'error-div',
														'error-header-button');
									}
								}
							});
		} else
			return;
	},
	// function used to download a log file
	download : function(clusterId) {
		var currentNodeType = $("#nodeType").val();
		var currentNodeIP = $("#nodeIP").val();
		var filePath = $("#fileName").val();
		var data = {
			"host" : currentNodeIP,
			"logFilePath" : filePath,
			"type" : currentNodeType
		};
		var downloadUrl = baseUrl + '/monitor/' + clusterId + '/download';
		var downloadFileSize = logFileNameOutputCommon[filePath];

		if (downloadFileSize > 5120) {
			$("#fileSizeExceed").modal('show');
			$('.ui-dialog-titlebar').hide();
			$('.ui-dialog :button').blur();
			return;
		}
		com.impetus.ankush
				.placeAjaxCall(
						downloadUrl,
						'POST',
						true,
						data,
						function(downloadUrlData) {
							if (downloadUrlData.output.status == true) {
								var downloadFilePath = baseUrl
										+ downloadUrlData.output.downloadPath;
								var hiddenIFrameID = 'hiddenDownloader', iframe = document
										.getElementById(hiddenIFrameID);
								if (iframe === null) {
									iframe = document.createElement('iframe');
									iframe.id = hiddenIFrameID;
									iframe.style.display = 'none';
									document.body.appendChild(iframe);
								}
								iframe.src = downloadFilePath;
							} else {
								com.impetus.ankush.validation
										.showAjaxCallErrors(
												downloadUrlData.output.error,
												'popover-content', 'error-div',
												'error-header-button');
							}
						});
	},
	// function used to validate whether a log file can be dowloaded or not
	validateLogs : function(urlData, errorType) {
		var focusDivId = null;
		if (errorType == "nodeDown") {
			focusDivId = "nodeIP";
		} else if (errorType == "filename") {
			focusDivId = "fileName";
		}
		$("#popover-content-hadoopLogs").empty();
		$("#error-div-hadoopLogs").css("display", "none");
		$('#errorBtnHadoopLogs').text("");
		$('#errorBtnHadoopLogs').css("display", "none");
		com.impetus.ankush.validation.errorCount = 0;
		var i = 0;
		$.each(urlData.output.error, function(index, value) {
			i = index + 1;
			com.impetus.ankush.validation.addNewErrorToDiv(focusDivId,
					'popover-content-hadoopLogs', value, null);
		});
		if (com.impetus.ankush.validation.errorCount > 0) {
			com.impetus.ankush.validation.showErrorDiv('error-div-hadoopLogs',
					'errorBtnHadoopLogs');
			$('#viewLogs').attr('disabled', true);
			$('#downloadLogs').attr('disabled', true);
			return;
		}
	},
	fileNameChange : function() {
		com.impetus.ankush.logs.removeErrorClass();
		$("#logView").empty();
		$("#div_Logs").css("display", "none");
	},
	// function used to remove error-class
	removeErrorClass : function() {
		$('#nodeIP').removeClass('error-box');
		$('#fileName').removeClass('error-box');
		$("#error-div-hadoopLogs").css("display", "none");
		$('#errorBtnHadoopLogs').css("display", "none");
	},
};
