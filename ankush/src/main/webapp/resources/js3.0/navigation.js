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
com.impetus.ankush.navigation = {
		//these are component level links in Hybrid cluster
		links : {
			"Hadoop" : {
				"Job Monitoring" : {
					url : baseUrl + '/hadoop-cluster-monitoring/'
				},
				"Parameters" : {
					url : baseUrl + '/commonMonitoring/'
				},
				"Logs" : {
					url : baseUrl + '/commonMonitoring/'
				}
			},
			
			"Cassandra" : {
				"Keyspaces" : {
					url : baseUrl + '/cassandraMonitoring/'
				},
				"Parameters" : {
					url : baseUrl + '/commonMonitoring/'
				},
				"Logs" : {
					url : baseUrl + '/commonMonitoring/'
				}
			},
			
		},
		//This will call populateDashboard()
		populateMenu : function(){
			if((jspPage !== "login" && decisionVar !== 'enforce-password' && decisionVar !== 'configure'))
				com.impetus.ankush.navigation.populateDashboard();
			else{
				localStorage.removeItem("open-menu");
			}
		},
		//This will populate alerts
		populateAlerts : function(){
			if(clusterId){
				var url = baseUrl + "/monitor/"+clusterId+"/alerts";
				com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
					if(!result.output.status){
						return;
					}
					if(result.output.events){
						$("#notificationAlertsCount").text(result.output.events.length);
						$("#alertsPara").text(result.output.events.length);
						$("#notificationAlertsShow").empty();
						if(result.output.events.length > 0){
							var eventLength = 8;
							if(result.output.events.length < 8)
								eventLength = result.output.events.length;
							for(var i = 0 ; i < eventLength ; i++){
								var icon = '<span class="fa fa-warning text-warning"></span>';
								if(result.output.events[i].severity === 'CRITICAL'){
									icon = '<span class="fa fa-warning text-danger"></span>';
								}
								var tempDate = new Date(parseInt(result.output.events[i].date));
								var today = new Date();
								var diff =  Math.abs(today - tempDate);
								
								var date = $.format.date(tempDate, "dd/MM/yyyy hh:mm:ss a");
								var eventValue = result.output.events[i].value;
								if(result.output.events[i].type === 'USAGE'){
									eventValue = eventValue+'%';
								}
								var hostUrl = baseUrl+'/commonMonitoring/'+clusterName+'/nodeDetails/C-D/'+clusterId+'/'+clusterTechnology+'/'+result.output.events[i].host;
								$("#notificationAlertsShow").append('<li><a href="'+hostUrl+'">'+icon+'&nbsp;&nbsp;'+result.output.events[i].host+' '+result.output.events[i].name+' '+eventValue+'</a></li>')
							}
						}
					}
				});
			}
		},
		//This will populate operations
		populateOperations : function(){
			clusterTechnology = "Hybrid";
			if(clusterId){
				var url = baseUrl + "/monitor/"+clusterId+"/operations?status=INPROGRESS";
				com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
					if(!result.output.status){
						return;
					}
					if(result.output.operations){
						$("#notificationOperationCount").text(result.output.operations.length);
						$("#operationsPara").text(result.output.operations.length);
						$("#notificationOperationShow").empty();
						if(result.output.operations.length > 0){
							var operationLength = 8;
							if(result.output.operations.length < 8)
								operationLength = result.output.operations.length;
							for(var i = 0 ; i < operationLength ; i++){
								var icon = '<span class="fa fa-circle-o-notch fa-spin text-primary"></span>';
								var opUrl = baseUrl + '/commonMonitoring/'+clusterName+'/operations/'+result.output.operations[i].operationId+'/C-D/'+clusterId+'/'+clusterTechnology
								$("#notificationOperationShow").append('<li><a href="'+opUrl+'">'+icon+'&nbsp;&nbsp;'+result.output.operations[i].opName+'</a></li>');
							}
						}
					}
				});
			}
		},
		//This will populate clusters under dashboard section in menu
		populateDashboard : function(){
			var url = baseUrl + "/clusteroverview";
			com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
				if(result.output.length > 0){
					$("#dashboard > ul").remove();
					$("#dashboard").append('<ul class="nav-sub" data-index="1" style="display: none;"></ul>');
					for(var i = 0 ; i < result.output.length ; i++){
						result.output[i].technology = "Hybrid";
						var url = baseUrl + '/commonMonitoring/'+result.output[i].name+'/C-D/'+result.output[i].clusterId+'/'+result.output[i].technology;
						if(result.output[i].status === com.impetus.ankush.constants.stateError){
							url = baseUrl + '/common-cluster/'+result.output[i].name+'/C-D/'+result.output[i].clusterId+'/'+result.output[i].technology;
						}else if(result.output[i].status === com.impetus.ankush.constants.stateRemoving){
							url = baseUrl + '/common-cluster/'+result.output[i].name+'/C-D/deleteCluster/'+result.output[i].clusterId+'/'+result.output[i].technology;
						}
						else if(result.output[i].status === com.impetus.ankush.constants.stateDeploying){
							url = baseUrl + '/common-cluster/'+result.output[i].name+'/C-D/'+result.output[i].clusterId+'/'+result.output[i].technology;
						}
						var shortClusterName = $.trim(result.output[i].name).substring(0, 10) + "...";
						$("#dashboard > ul").append('<li id="dashboard-'+result.output[i].name.replace(/[\.]+/g,'_')+'"><a title="'+result.output[i].name+'" href="'+url+'"><i class="fa fa-fw fa-caret-right"></i>'+shortClusterName+'</a></li>');
						if((result.output[i].status === com.impetus.ankush.constants.stateDeployed) || (result.output[i].status === com.impetus.ankush.constants.stateCritical) || (result.output[i].status === com.impetus.ankush.constants.stateWarning)){
							com.impetus.ankush.navigation.populateCommonLinks(result.output[i].technology,result.output[i].name,result.output[i].clusterId);
							com.impetus.ankush.navigation.populateTechnologies(result.output[i].clusterId,result.output[i].name,result.output[i].technology);	
						}
					}
				}
			});
		},
		//this will populate common links for all component under menu dashboard sections
		populateCommonLinks : function(clusterTechnology,clusterName,clusterId){
			clusterTechnology = "Hybrid";
			$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +" > ul").remove();
			$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') ).append('<ul class="nav-sub" data-index="1" style="display: none;"></ul>');
			for(var key in com.impetus.ankush.techListsAndActions[clusterTechnology]['links']){
				var linkUrl = baseUrl + '/commonMonitoring/'+clusterName+'/'+com.impetus.ankush.toCamelCase(key)+'/C-D/'+clusterId+'/'+clusterTechnology;
				$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +" > ul").append('<li id="dashboard-'+clusterName.replace(/[\.]+/g,'_') +'-'+com.impetus.ankush.toCamelCase(key)+'"><a class="" href="'+linkUrl+'"><i class="fa fa-fw fa-caret-right"></i>'+key+'</a>	</li>');
			}
			$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +" > ul").append('<li id="dashboard-'+clusterName.replace(/[\.]+/g,'_') +'-Components"><a class="" href="#"><i class="fa fa-fw fa-caret-right"></i>Components</a></li>');
		},
		//this will populate all components under all clusters
		populateTechnologies : function(clusterId,clusterName,clusterTechnology){
			clusterTechnology = "Hybrid";
			var url = baseUrl + "/monitor/"+clusterId+"/clustercomponents";
			com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
				if(result.output.status){
					$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +"-Components > ul").remove();
					$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +"-Components").append('<ul class="nav-sub" data-index="1" style="display: none;"></ul>');
					for(var key in result.output.components){
						var tech = key;
						if(key === "Ganglia")
							continue;
						if(key === "Hadoop" && com.impetus.ankush.isHadoop2(clusterId))
							tech = "Hadoop2";
						var url = baseUrl + '/hybrid-monitoring/'+clusterName+'/'+key+'/C-D/'+clusterId+'/'+clusterTechnology;
						$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +"-Components > ul").append('<li id="dashboard-'+clusterName.replace(/[\.]+/g,'_') +'-Components-'+com.impetus.ankush.toCamelCase(tech)+'"><a title="" href="'+url+'"><i class="fa fa-fw fa-caret-right"></i>'+(key.indexOf("Zookeeper") > -1 ? "Zookeeper" : key)+'</a></li>');
						$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +"-Components-"+com.impetus.ankush.toCamelCase(tech)).append('<ul class="nav-sub" data-index="1" style="display: none;"></ul>');
						for(var link in com.impetus.ankush.navigation.links[key]){
							var page = com.impetus.ankush.toCamelCase(link);
							var linkUrl = com.impetus.ankush.navigation.links[key][link].url+clusterName+'/'+key+'/'+page+'/C-D/'+clusterId+'/'+clusterTechnology;
							if(key === 'Hadoop'){
								if(link === "Job Monitoring" && com.impetus.ankush.isHadoop2(clusterId)){
									linkUrl = com.impetus.ankush.navigation.links[key][link].url+clusterName+'/'+key+'/applicationMonitoring/C-D/'+clusterId+'/'+clusterTechnology;
									link = "Application Monitoring";
									page = com.impetus.ankush.toCamelCase(link);
								}
							}
							$("#dashboard-"+clusterName.replace(/[\.]+/g,'_') +"-Components-"+com.impetus.ankush.toCamelCase(tech)+" > ul").append('<li id="dashboard-'+clusterName.replace(/[\.]+/g,'_') +'-Components-'+com.impetus.ankush.toCamelCase(tech)+'-'+page+'"><a title="'+link+'" href="'+linkUrl+'"><i class="fa fa-fw fa-caret-right"></i>'+link+'</a></li>');
						}
					}
				}
			});
		},
		//this will populate breadcrumb
		getBreadCrumb : function(){
			$("#breadcrumb-ankush").empty();
			var url = window.location.href;
			var meanigfulUrl = url.split('ankush/');
			var restPreUrl = meanigfulUrl[0];
			var breadCrumb = meanigfulUrl[1].split('/C-D/');
			var preBreadcrumb = breadCrumb[0];
			var postBreadcrumb = breadCrumb[1];
			breadcrumbArray = preBreadcrumb.split('/');
			var monitoring = breadcrumbArray[0]; 
			breadcrumbArray[0] = 'Dashboard';
			for(var i = 0 ; i < breadcrumbArray.length ; i++){
				var url = restPreUrl+'ankush/C-D/'+postBreadcrumb;
				var text = com.impetus.ankush.toSentenceCase(breadcrumbArray[i]);
				if(i === 0){
					url = restPreUrl+'ankush/dashboard';
				}
				if(i === 1){
					var id = "dashboard-"+clusterName.replace(/[\.]+/g,'_') ;
					url = $('#'+id).children().first().attr('href');
				}
				if(i === 2 && hybridTechnology){
					var temp = breadcrumbArray[2];
					if(com.impetus.ankush.isHadoop2(clusterId) && breadcrumbArray[2] === "Hadoop")
						temp = "Hadoop2";
					var id = "dashboard-"+clusterName.replace(/[\.]+/g,'_') +"-Components-"+com.impetus.ankush.toCamelCase(temp);
					url = $('#'+id).children().first().attr('href');
				}
				else if(i >= 2){
					var subUrl = '';
					var index = 2;
					var subUrl1 = '/'+clusterName;
					if(hybridTechnology){
						index = 3;
						subUrl1 = subUrl1+'/'+hybridTechnology;
					}	
					for(var j = index ; j <= i ; j++)
						subUrl += '/'+breadcrumbArray[j];
					url = restPreUrl+'ankush/'+monitoring+subUrl1+subUrl+'/C-D/'+postBreadcrumb;
				}
				if(i === breadcrumbArray.length-1){
					if(clusterName && i === 1){
						$("#breadcrumb-ankush").append('<li class="active"><a href="javascript:;">'+breadcrumbArray[i]+'</a></li>');
					}
					else{
						if(breadcrumbArray[i].indexOf("Zookeeper") > -1){
							text = "Zookeeper";
						}
						$("#breadcrumb-ankush").append('<li class="active"><a href="javascript:;">'+text+'</a></li>');
					}
				}
				else if(clusterName && i === 1)
					$("#breadcrumb-ankush").append('<li><a href="'+url+'">'+breadcrumbArray[i]+'</a></li>');
				else{
					if(breadcrumbArray[i].indexOf("Zookeeper") > -1){
						text = "Zookeeper";
					}
					$("#breadcrumb-ankush").append('<li><a href="'+url+'">'+text+'</a></li>');
				}
			}
			for(var index = 0 ; index < breadcrumbArray.length ; index++){
				if((index === 0)){
					breadcrumbArray[index] = com.impetus.ankush.toCamelCase(breadcrumbArray[index]);
				}
				else if((index === 2) && (hybridTechnology)){
						breadcrumbArray[index] = com.impetus.ankush.toCamelCase(breadcrumbArray[index]);
						breadcrumbArray[index] = 'Components-'+breadcrumbArray[index];
					}
			}	
			com.impetus.ankush.navigation.menuOpenLinks(breadcrumbArray);
		},
		//this will open relevant links in menu tree
		menuOpenLinks : function(breadcrumbArray){
			var menuOpenId = breadcrumbArray.join('-');
			menuOpenId = menuOpenId.replace(/[\.]+/g,'_');
			while(document.getElementById(menuOpenId) === null)
			{
				var splitArray = menuOpenId.split('-');
				splitArray.pop();
				menuOpenId = splitArray.join('-');
			}
			$("#"+menuOpenId).parents('li').addClass('open');
			$("#"+menuOpenId).parents('ul').show();
			$("#"+menuOpenId).addClass('open');
			$("#"+menuOpenId+" > ul").show();
		},
		//this will update cluster level alerts and warnings
		getAlertsAndWarnings : function(){
			var url = baseUrl + "/clusteroverview";
			com.impetus.ankush.placeAjaxCall(url,'GET',false,null,function(result){
				$.each(result.output,function(key,cluster){
					if(cluster.notifications){
						$('#dashboard-'+cluster.name.replace(/[\.]+/g,'_')+' > a > .label').remove();
						if(cluster.notifications.alerts && cluster.notifications.alerts !== 0)
							$('#dashboard-'+cluster.name.replace(/[\.]+/g,'_') +' > a').append('<i class="label label-danger mrgl30">'+cluster.notifications.alerts+'</i>');
						else if(cluster.notifications.warnings && cluster.notifications.warnings !== 0)
							$('#dashboard-'+cluster.name.replace(/[\.]+/g,'_') +' > a').append('<i class="label label-warning mrgl30">'+cluster.notifications.warnings+'</i>');	
					}
				});
			});
		}
};
