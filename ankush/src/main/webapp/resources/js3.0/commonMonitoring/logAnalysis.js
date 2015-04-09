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
com.impetus.ankush.commonMonitoring.validateLogAnalysis =  function() {
	$("#dpd1").removeClass('error-box');
    $("#dpd2").removeClass('error-box');
    $("#error-div-logAnalysis").hide();
	var startDate=$("#dpd1").val();
	var endDate=$("#dpd2").val();
	var startts  = Math.round((new Date(startDate).getTime()));
	var endts  = Math.round((new Date(endDate).getTime()));
	if(startDate=="" && endDate==""){
            $("#dpd1").addClass('error-box');
            $("#dpd2").addClass('error-box');
            com.impetus.ankush.validation.tooltipMsgChange_Error("dpd1", "Start date text box empty");
            com.impetus.ankush.validation.tooltipMsgChange_Error("dpd2", "End date text box empty");
            com.impetus.ankush.validation.showAjaxCallErrors(['Start & End date text box empty'], 'popover-content-logAnalysis', 'error-div-logAnalysis', '');
	}else if(startDate=="" ){
		  $("#dpd1").addClass('error-box');
          com.impetus.ankush.validation.tooltipMsgChange_Error("dpd1", "Start date text box empty");
          com.impetus.ankush.validation.showAjaxCallErrors(['Start date text box empty'], 'popover-content-logAnalysis', 'error-div-logAnalysis', '');
	}else if(endDate==""){
          $("#dpd2").addClass('error-box');
          com.impetus.ankush.validation.tooltipMsgChange_Error("dpd2", "End date text box empty");
          com.impetus.ankush.validation.showAjaxCallErrors(['End date text box empty'], 'popover-content-logAnalysis', 'error-div-logAnalysis', '');
	}else if(startts>endts){
		  $("#dpd1").addClass('error-box');
          $("#dpd2").addClass('error-box');
          com.impetus.ankush.validation.showAjaxCallErrors(['Start date is greater than End date'], 'popover-content-logAnalysis', 'error-div-logAnalysis', '');	
	}else{
		$("#dpd1").removeClass('error-box');
	    $("#dpd2").removeClass('error-box');
	    $("#error-div-logAnalysis").hide();
		com.impetus.ankush.commonMonitoring.getLogAnalysisData();
	}
};
com.impetus.ankush.commonMonitoring.getLogAnalysisData =  function() {
	var data={};
	var startDate=$("#dpd1").val();
	var endDate=$("#dpd2").val();
	
	if(startDate!="" && endDate!=""){
		logAnalysisTable.fnClearTable();
		startDate  = Math.round((new Date(startDate).getTime()));
		endDate  = Math.round((new Date(endDate).getTime()));
		data.starttime=startDate;
		data.endtime=endDate;
		}
	var logAnalysisUrl = baseUrl + '/monitor/clusterlogs';
	 com.impetus.ankush.placeAjaxCall(logAnalysisUrl, "POST", true,data, function(result){
		 if(result.output==null || result.output.output.hits.hits.length==0){
			 logAnalysisTable.fnSettings().oLanguage.sEmptyTable = "No Records found";
			 return;
		 }
		 for(var i=0;i<result.output.output.hits.hits.length;i++){
			 logAnalysisTable.fnAddData([
			                             result.output.output.hits.hits[i]._source.log_time,                     
			                             result.output.output.hits.hits[i]._source.message
			                             ]);
		 }
	 });
	 com.impetus.ankush.commonMonitoring.barChartDraw();
};

com.impetus.ankush.commonMonitoring.barChartDraw=  function() {
	var data={};
	var startDate=$("#dpd1").val();
	var endDate=$("#dpd2").val();
	
	if(startDate!="" && endDate!=""){
		startDate  = Math.round((new Date(startDate).getTime()));
		endDate  = Math.round((new Date(endDate).getTime()));
		data.starttime=startDate;
		data.endtime=endDate;
		}
	var logAnalysisUrl = baseUrl + '/monitor/timebasedcount';
	 com.impetus.ankush.placeAjaxCall(logAnalysisUrl, "POST", true,data, function(result){
		// result={"output":{"status":true,"output":{"total":2,"aggregations":{"time":{"1404504498000":3,"1405004498000":1,"1405104498000":9,"1405204498000":12,"1405304498000":17,"1405404498000":1,"1405604498000":2,"1405704498000":3,"1405804498000":7,"1405990098000":12}}}},"status":"200","description":"Logstash logs","errors":null};
		 var data = [], item;

		 for (var type in result.output.output.aggregations.time) {
		     item = {};
		     item.time = type;
		     item.count = result.output.output.aggregations.time[type];
		     data.push(item);
		 }
		
		 var margin = {top: 40, right: 20, bottom: 30, left: 40},
		    width = 960 - margin.left - margin.right,
		    height = 150 - margin.top - margin.bottom;

		 var formatPercent = d3.format("d");
		var x = d3.scale.ordinal()
		    .rangeRoundBands([0, width], .9);

		var y = d3.scale.linear()
	    .range([height, 0]);

		var xAxis = d3.svg.axis()
		    .scale(x)
		    .orient("bottom");
		
		var yAxis = d3.svg.axis()
		    .scale(y)
		    .orient("left")
		    .tickFormat(formatPercent);

	/*	var tip = d3.tip()
		  .attr('class', 'd3-tip')
		  .offset([-10, 0])
		  .html(function(d) {
		    return "<strong>Frequency:</strong> <span style='color:red'>" + d.count + "</span>";
		  })*/
		$("#barChartDiv").empty();
		var svg = d3.select("#barChartDiv").append("svg")
		    .attr("width","100%")
		    .attr("height", height + margin.top + margin.bottom)
		  .append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		//svg.call(tip);
		  x.domain(data.map(function(d) {
			  var tempDate = new Date(parseInt(d.time));
				var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss");
			  return date; }));
		  y.domain([0, d3.max(data, function(d) { return d.count; })]);

		  svg.append("g")
		      .attr("class", "x axis")
		      .attr("transform", "translate(0," + height + ")")
		      .call(xAxis);

		  svg.append("g")
		      .attr("class", "y axis aa")
		      .call(yAxis)
		    .append("text")
		      .attr("transform", "rotate(-90)")
		      .attr("y", 6)
		      .attr("dy", ".71em")
		      .style("text-anchor", "end")
		      .text("Log Count");

		  svg.selectAll(".bar")
		      .data(data)
		    .enter().append("rect")
		      .attr("class", "bar")
		      .attr("x", function(d) { return x(d.time); })
		      .attr("width", x.rangeBand())
		      .attr("y", function(d) { return y(d.count); })
		      .attr("height", function(d) { return height - y(d.count); })
		     /* .on('mouseover', tip.show)
		      .on('mouseout', tip.hide);*/
		function type(d) {
		  d.count = +d.count;
		  return d;
		}
	 });
	
};
