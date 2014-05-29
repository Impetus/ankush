var width = 40,
    height = 23,
    radius = 18;
var color = d3.scale.ordinal().range(["#739247", "#EFEFEF"]);
var arc = d3.svg.arc()
    .outerRadius(radius - 10)
    .innerRadius(0);

var pie = d3.layout.pie()
    .sort(null)
    .value(function(d) {return d.utilization;});



function getChart(divId,JSON){
    d3.select("body").append("div").attr("id", divId);
    d3.json(JSON, function(error, data) {  

    	var CPUChart = JSON.CPU;
        var IOChart = JSON.MEMORY;
        getPieChart(CPUChart,divId,"CPU");
        getPieChart(IOChart,divId,"MEMORY"); 
    });
}

function getUtilizationChart(divId,JSON){    
         var span =   d3.select("#"+divId).append("span").attr('id', "span_"+divId);
         var CPUChart = JSON.CPU;
         var IOChart = JSON.MEMORY;
         getPieChart(CPUChart,divId,"CPU");
         getPieChart(IOChart,divId,"MEMORY");    
}

function getPieChart(data,divId,type){   
  var svg = d3.select("#span_"+divId).append("span").text(type+' :  ');
  var svg = d3.select("#span_"+divId).append("svg")
    .attr("width", width)
    .attr("height", height)
    .append("g")
    .attr("transform", "translate(9,15)");  
    
    data.forEach(function(d) {    
    d.utilization = +d.utilization;
  });

  var g = svg.selectAll(".arc")
      .data(pie(data))
    .enter().append("g")
      .attr("class", "arc");

   g.append("path")
      .attr("d", arc)
      .style("fill",function(d) {return color(d.data.total);});   
      
  
}     