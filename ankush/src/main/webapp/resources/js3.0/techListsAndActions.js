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
com.impetus.ankush.techListsAndActions = {
		Default : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/addNodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Restart Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('restartCluster');
                    }
                },
                'Start Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('startCluster');
                    }
                },
                'Stop Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('stopCluster');
                    }
                },
                'Delete Cluster...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.deleteClusterDialog();
                    }
                },
            },
            links : {
                'Nodes' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/nodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Configuration' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/configuration','get','Configurations',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    },
                    confLinks : {
                        'Cluster' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/clusterConf','get','Cluster Configuration','Configurations');
                            }
                        },
                        'Alerts' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/alert','get','Alerts','Configurations');
                            }
                        }
                    }
                },
                'Events':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/events','get','Events',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Logs':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/logs','get','Logs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Audit Trails':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/auditTrails','get','Audit Trails',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                }
            }
        },
       
       
       
        Hybrid : {
            actions : {
                'Add Nodes...' : {
                	methodCall : function(){
                		url = baseUrl + '/hybrid-monitoring/'+clusterName+'/addNode/C-D/'+clusterId+'/'+clusterTechnology,
                    	$(location).attr('href',(url));
                    }
                },
               /* 'Restart Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('restartCluster');
                    }
                },*/
                'Start Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('startCluster');
                    }
                },
                'Stop Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('stopCluster');
                    }
                },
                'Delete Node...' : {
                    methodCall : function(){
                    	url = baseUrl+'/commonMonitoring/'+clusterName+'/nodes/C-D/'+clusterId+'/'+clusterTechnology,
                    	$(location).attr('href',(url));
                    }
                },
                'Delete Cluster...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.deleteClusterDialog();
                    }
                },
            },
            links : {
                'Nodes' : {
                	url : baseUrl + '/commonMonitoring/'+clusterName+'/nodes/C-D/'+clusterId+'/'+clusterTechnology,
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/nodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                /*'Cluster' : {
                        methodCall : function(){
                            //com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/clusterConf','get','Cluster Configuration','Configurations');
                            com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hybrid-cluster/clusterConf/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Cluster Configuration','Configurations',null,null,"com.impetus.ankush.hybridSetupDetail.setupDetailValuePopulate("+com.impetus.ankush.commonMonitoring.clusterId+")");
                        }
                    },*/
                /*'Deployment Logs' : {
                    methodCall : function(){
                    	url : baseUrl + '/commonMonitoring/'+clusterName+'/DeploymentLogs/C-D/'+clusterId+'/'+clusterTechnology,
                        //com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/clusterConf','get','Cluster Configuration','Configurations');
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/common-cluster/deploymentProgress/'+clusterId,'get','Cluster Deployment Logs','Configurations',null,null,"com.impetus.ankush.common.preDeploymentLog("+clusterId+")");
                    }
                },*/
                'Alerts and High Availability' : {
                		url : baseUrl + '/commonMonitoring/'+clusterName+'/alertsAndHighAvailability/C-D/'+clusterId+'/'+clusterTechnology,
                        methodCall : function(){
                            com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/alert','get','Alerts','Configurations');
                        }
                },
                'Events':{
                	url : baseUrl + '/commonMonitoring/'+clusterName+'/events/C-D/'+clusterId+'/'+clusterTechnology,
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/events','get','Events',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Operations':{
                	url : baseUrl + '/commonMonitoring/'+clusterName+'/operations/C-D/'+clusterId+'/'+clusterTechnology,
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/operations','get','Events',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                /*'Logs':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/logs','get','Logs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },*/
                'Audit Trails':{
                	url : baseUrl + '/commonMonitoring/'+clusterName+'/auditTrails/C-D/'+clusterId+'/'+clusterTechnology,
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/auditTrails','get','Audit Trails',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                }
            }
        },
       
        Cassandra : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/addNodes/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
                    }
                },
                'Restart Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('restartCluster');
                    }
                },
                'Start Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('startCluster');;
                    }
                },
                'Stop Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('stopCluster');;
                    }
                },
                'Delete Cluster...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.deleteClusterDialog();
                    }
                },
            },
            links : {
                'Nodes' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/nodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
                    }
                },
                'Keyspaces' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/keyspaces','get','Keyspaces',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
                    }
                },
                'Configuration' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/configuration','get','Configurations',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
                    },
                    confLinks : {
                        'Cluster' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandra-cluster/clusterConf/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Cluster Configuration','Configurations',null,null,"com.impetus.ankush.cassandraSetupDetail.setupDetailValuePopulate("+com.impetus.ankush.commonMonitoring.clusterId+")");
                            }
                        },
                        'Parameters' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/parameters','get','Parameters',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.configurationParam.removePage();');
                            }
                        },
                        'Alerts' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/alert','get','Alerts','Configurations');
                            }
                        }
                    }
                },
                'Events':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/events','get','Events',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
                    }
                },
                'Logs':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/logs','get','Logs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
                    }
                },
                'Audit Trails':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/auditTrails','get','Audit Trails',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
                    }
                }
            }
        },
        
        Hadoop : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/addNodes/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Add Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Submit Job...' : {
                    methodCall : function(){
                    	url = baseUrl + '/hadoop-cluster-monitoring/'+clusterName+'/'+hybridTechnology+'/submitJobs/C-D/'+clusterId+'/'+clusterTechnology;
                    	$(location).attr('href',(url));
                    }
                },
                'Submit Application...' : {
                    methodCall : function(){
                    	url = baseUrl + '/hadoop-cluster-monitoring/'+clusterName+'/'+hybridTechnology+'/submitJobs/C-D/'+clusterId+'/'+clusterTechnology;
                    	$(location).attr('href',(url));
                    }
                },
                /*'Run Commands...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/commands','get','Commands',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },*/
                'Manage Configurations...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/configuration','get','Configurations',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    },
                },
                'View Logs...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/logs','get','Logs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Manage Jobs...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/jobMonitoring/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Configurations',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'View Events...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/events','get','Events',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'View Audit Trail...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/auditTrails','get','Audit Trails',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Restart Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('restartCluster');
                    }
                },
                'Start Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('startCluster');;
                    }
                },
                'Stop Cluster...' : {
                    methodCall : function(){
                    	com.impetus.ankush.commonMonitoring.startStopClusterDialog('stopCluster');;
                    }
                },
                'Delete Cluster...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.deleteClusterDialog();
                    }
                },
            },
            links : {
                'Nodes' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/nodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Job Monitoring' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/jobMonitoring/'+com.impetus.ankush.commonMonitoring.clusterId+'','get','Job Monitoring',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Configuration' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/configuration','get','Configurations',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    },
                    confLinks : {
                        'Cluster' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/clusterConf','get','Cluster Configuration','Configurations');
                            }
                        },
                        'Hadoop Ecosystem' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/configurations/hadoopEcosystem','get','Hadoop Ecosystem','Configurations',null,null,'com.impetus.ankush.hadoopMonitoring.loadHadoopEcosystemInfo('+com.impetus.ankush.commonMonitoring.clusterId+')');
                            }
                        },
                        'Auto-provision' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/configurations/autoProvision/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Auto Provision','Configurations');
                            }
                        },
                        'Job Scheduler' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/jobscheduler/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Job Scheduler','Configurations');
                            }
                        },
                        'Parameters' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/parameters','get','Parameters',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.configurationParam.removePage();');
                            }
                        },
                        'Alerts' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/alert','get','Alerts','Configurations');
                            }
                        }
                    }
                },
                'Events':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/events','get','Events',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Logs':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/logs','get','Logs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Audit Trails':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/auditTrails','get','Audit Trails',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                }
            }
        },
       
};
