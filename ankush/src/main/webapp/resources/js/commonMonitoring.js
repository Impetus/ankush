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

var is_autorefreshTiles_monitoringPage = null;
var is_autoRefreshHeatMap_monitoringPage = null;
var is_autoRefreshRingTopology_monitoringPage = null;
var addNodeCommonClusterTable = null;
var nodeIpProgressTable = null;
var is_autorefreshTiles_addingNodes = null;
var is_autoRefreshSparkLine_monitoringPage = null;
var nodeDeployLogDataCommon = null;
var addingNodeUrlData = null;
var nodeDeploymentIntervalCommon = null;
var refreshIntervalCommon = 5000;
var logFileNameOutputCommon = null;
var nodeHadoopJSON_AddNodesCommon = null;
var saveNodePattern = null;
var loadClusterLevelGraphsStartTime = 'lasthour';
var fileIPAddress_ServerPath = null;
var inspectNodeData = {};
var clusterUtilizationTrend = {};
var nodeTableLength_AddNodes = 0;
com.impetus.ankush.commonMonitoring = {
        clusterId : null,
        clusterTechnology : null,
        clusterName : null,
        clusterEnv : null,
        auditTrailRows : [],
        eventRows : [],
        nodesData : null,
        errorCount_NodeDrillDown : 0,
        nodeIndexForAutoRefresh : null,
        hybridTechnology : null,
        //this key will show default monitoring page
        Default : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/addNodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
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
        //this key will show default monitoring page
        Hbase : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/addNodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
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
        ElasticSearch : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/addNodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Create Index...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/elasticSearchMonitoring/createIndex','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.elasticSearchMonitoring.removeIndexDrilldown()');
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
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/configuration','get','Configurations',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.elasticSearchMonitoring.removeIndexDrilldown()');
                    },
                    confLinks : {
                        'Cluster' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/elasticSearch-cluster/clusterConf/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Cluster Configuration','Configurations',null,null,"com.impetus.ankush.elasticSearchSetupDetail.setupDetailValuePopulate("+com.impetus.ankush.commonMonitoring.clusterId+")");
                            }
                        },
                        'Alerts' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/alert','get','Alerts','Configurations');
                            }
                        },
                        'Parameters' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/parameters','get','Parameters',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.configurationParam.removePage();');
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
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/logs','get','Logs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.elasticSearchMonitoring.removeIndexDrilldown()');
                    }
                },
                'Audit Trails':{
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/auditTrails','get','Audit Trails',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                }
            }
        },
        Kafka : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/addNodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Create Topic...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/kafkaMonitoring/topicCreation','get','Create Topic',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
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
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/kafka-cluster/clusterConf/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Cluster Configuration','Configurations',null,null,"com.impetus.ankush.kafkaSetupDetail.setupDetailValuePopulate("+com.impetus.ankush.commonMonitoring.clusterId+")");
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
       
        Storm : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/addNodes','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Submit Topology...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/stormMonitoring/submitTopology','get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.submitTopoPreviousCall();');
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
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/storm-cluster/clusterConf/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Cluster Configuration','Configurations',null,null,"com.impetus.ankush.stormSetupDetail.setupDetailValuePopulate("+com.impetus.ankush.commonMonitoring.clusterId+")");
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
                        },
                       
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
        Cassandra : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/cassandraMonitoring/addNodes/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad();');
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
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/submitJobs/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Submit Jobs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);','com.impetus.ankush.hadoopJobs.initTables_SubmitJob();');
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
                        'Job Scheduler' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/jobscheduler/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Job Scheduler','Configurations');
                            }
                        },
                        'Parameters' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/configurations/parameters/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Parameters','Configurations');
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
        Hadoop2 : {
            actions : {
                'Add Nodes...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/addNodes/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Add Nodes',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
                    }
                },
                'Submit Application...' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/submitJobs/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Submit Jobs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);','com.impetus.ankush.hadoopJobs.initTables_SubmitJob();');
                    }
                },
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
                'Application Monitoring' : {
                    methodCall : function(){
                        com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/appMonitoring/'+com.impetus.ankush.commonMonitoring.clusterId+'','get','Job Monitoring',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
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
                        'Parameters' : {
                            methodCall : function(){
                                com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/hadoop-cluster-monitoring/configurations/parameters/'+com.impetus.ankush.commonMonitoring.clusterId,'get','Parameters','Configurations');
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
        init : function(clusterData){
            com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/home','post',clusterData.name,'Cluster Overview',clusterData , 'com.impetus.ankush.commonMonitoring.removeChildPreviousMonitoringPageLoad();' );
        },
       
        //this will load child page
        loadChild : function(url , method , title , tooltipTitle , data , previousCallBack, callback , callbackData){
            if(data){
                $('#content-panel').sectionSlider('addChildPanel', {
                    url : url,
                    method : method,
                    title : title,
                    tooltipTitle : tooltipTitle,
                    previousCallBack : previousCallBack,
                    params : {
                        clusterId : data.id,
                        clusterName : data.name,
                        clusterTechnology : data.technology,
                        clusterEnvironment : data.env
                    },
                    callback : function() {
                        eval(callback);
                    },
                });
            }
            else{
                $('#content-panel').sectionSlider('addChildPanel', {
                    url : url,
                    method : method,
                    title : title,
                    tooltipTitle : tooltipTitle,
                    previousCallBack : previousCallBack,
                    callback : function() {
                        eval(callback);
                    },
                });
            }
           
            //UPG -- Code for top space Starts
            var getScrollPos = $(window).scrollTop();
            setTimeout(function() {                 
                //console.log(title);
                //console.log(previousCallBack);
                //console.log(getScrollPos);
               
                var id=title+'///'+previousCallBack+'///'+getScrollPos;
                //console.log("TEST::"+id);
                var ele = document.getElementById(id);
                //console.log(document.getElementById(id));
                //console.log(jQuery("#"+id));
                //console.log($(ele).parent());
                //console.log($(ele).parent().attr("class"));
                var childCount = $(ele).parent().attr("class").split("section-children-")[1];
                           
                var headerHgtRoot = $(".section-header", $('.section-root')).height();
                var headerHgtChild = $(".section-header", $('.section-children-' + (childCount))).height();
               
                //console.log(headerHgtRoot+"::"+childCount);
                //console.log(headerHgtChild+"::"+childCount);
                
                $(".section-body", $('.section-root')).animate({"margin-top":headerHgtRoot+10+"px"}, 50);
                 $(".section-body", $('.section-children-' + (childCount))).animate({'margin-top': headerHgtChild+10+'px'}, 50);
                
                 //console.log("id::"+id);
            }, 1000);
           
            //UPG -- Code for top space Ends
        },
        //this will create common tiles for common monitoring page
        createCommonTiles : function(currentClusterId){
            //$('#allTilesCluster').masonry('destroy');
            if(undefined == currentClusterId)
                return;
            var tileUrl = baseUrl + '/monitor/' + currentClusterId + '/clustertiles';
            var clusterTiles = {};
            com.impetus.ankush.placeAjaxCall(tileUrl, "GET", true, null, function(tileData){
                //this object variable will set css passing by refrence
                var tileVar = {
                        'leftCss' : 0,
                        'topCss' : 0,
                        'lineCounter' : 1,
                        'tyleType' : 'bigTile',
                };
                if(tileData.output.status == false){
                    return ;
                }
                else{
                    clusterTiles = com.impetus.ankush.tileReordring(tileData.output.tiles);
                }
                //this var will set whether tile is 2*2 or 1*2
                var tileFlag = false;
                for(var j = 0 ; j < clusterTiles.length ; j++){
                    if(clusterTiles[j].tileType == 'big_text'){
                        tileFlag = true;
                        break;
                    }
                }
                $('#allTilesCluster').empty();
                var tile = document.getElementById('allTilesCluster');
                for(var i = 0 ; i < clusterTiles.length ; i++){
                    if((tileVar.leftCss+200) > ($('#allTilesCluster').width())){
                        tileVar.leftCss = 0;
                        tileVar.topCss = tileVar.lineCounter*200;
                        tileVar.lineCounter++;
                    }
                    // && ((clusterTiles[i].tileType == 'small_text') && (clusterTiles[i].tileType == undefined))
                    if((i == 0) && ((clusterTiles[i].tileType == 'small_text') || (clusterTiles[i].tileType == undefined)))
                        tileVar.tyleType = 'smallTileOdd';
                    com.impetus.ankush.createTyleUsingType(tileVar,i,clusterTiles,tile,'common-id-for-tile');
                }
                if(tileFlag == false){
                    $('#allTilesCluster').masonry('destroy');
                    $('#allTilesCluster').masonry({
                        itemSelector : '.item',
                        columnWidth : 100,
                        isAnimated : true
                    });
                }else
                    setTimeout(function(){$('#allTilesCluster').css('height',(tileVar.lineCounter)*200+'px');},50);
                $('.clip').tooltip();
            });
           
        },
        //this function will create heatmap on monitoring page
        commonHeatMaps : function(currentClusterId){
            $('#heat_map').empty();
            getCommonHeatMapChart(currentClusterId,'0');
        },
        //this will fill event table with data in cluster
        getDefaultAlertValues : function(clusterId) {
            var alertUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/alertsConf';
            alertResult = com.impetus.ankush.placeAjaxCall(alertUrl, "GET", false);
            $("#refreshInterval").html('').text(alertResult.output.refreshInterval);
            if(alertResult.output.mailingList == null)
                alertResult.output.mailingList = "";
            if (alertResult.output.mailingList != "") {
                $("#mailingList").val(alertResult.output.mailingList.split(';').join(','));
            } else {
                $("#mailingList").html('').val("");
            }
            if (alertResult.output.informAllAdmins) {
                $("#alertInform").attr('checked', 'checked');
            } else {
                $("#alertInform").removeAttr('checked');
            }
            if (configAlertsUsageTable != null) {
                configAlertsUsageTable.fnClearTable();
            } else {
                configAlertsUsageTable.datatable();
            }
            for ( var i = 0; i < alertResult.output.thresholds.length; i++) {
                configAlertsUsageTable.fnAddData([
                                                  '<a class="" id="metricName' + i
                                                  + '" style="text-align: right;">'
                                                  + alertResult.output.thresholds[i].metricName
                                                  + '</a>',
                                                  '<span class="editableLabel" id="warningLevel-' + i
                                                  + '" style="width:20px;">'
                                                  + alertResult.output.thresholds[i].warningLevel
                                                  + '</span>',
                                                  '<span class="editableLabel" id="alertLevel-' + i + '">'
                                                  + alertResult.output.thresholds[i].alertLevel
                                                  + '</span>', ]);
            }
            $('.editableLabel').editable({
                type : 'text',
                validate :function(value){
                    return com.impetus.ankush.commonMonitoring.checkUsageValue(this,value);}
            });
        },
        saveAlerts : function() {
            var data = {};
            data.thresholds = new Array();
            var alertUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/alertsConf';
            $("#mailingList").removeClass('error-box');
            data.refreshInterval = $("#refreshInterval").text();
            if($("#mailingList").val()!=""){
                var mailingListArray = $("#mailingList").val().split(',');
                for(var i=0 ; i < mailingListArray.length ; i++){
                    if(!com.impetus.ankush.validate.email(mailingListArray[i])){
                        $("#mailingList").addClass('error-box');
                        com.impetus.ankush.validation.tooltipMsgChange_Error("mailingList", "Invalid email Address");
                        com.impetus.ankush.validation.showAjaxCallErrors(['Invalid email Address'], 'popover-content-alert', 'error-div-alert', '');
                        return;
                    }
                }
                data.mailingList = $("#mailingList").val();
            }else{
                data.mailingList = "";
            }
            if ($("#alertInform").is(':checked')) {
                data.informAllAdmins = true;
            } else {
                data.informAllAdmins = false;
            }

            for ( var i = 0; i < alertResult.output.thresholds.length; i++) {
                var rowData = {
                        "metricName" : $("#metricName" + i).text(),
                        "warningLevel" : $("#warningLevel-" + i).text(),
                        "alertLevel" : $("#alertLevel-" + i).text()
                };
                data.thresholds.push(rowData);
            }
            com.impetus.ankush.placeAjaxCall(alertUrl, "POST", false,data);
            com.impetus.ankush.removeChild(3);
        },
       
        // create tiles on node deatail page
                // will draw graph on individual node detail page
              //normalize graph data , will be called from drawGraph_JobMonitoring function
      normalizeData: function (rrdData)
      {
        if(rrdData == null){
            return [];
        }
        var meta = rrdData.meta;
        var data = rrdData.data;
        var legends = meta.legend;
        var result = [];

        legends.forEach(function(legend, index)
        {
            result.push({key: legend, values: [], yvalues : []});
        });
        data.forEach(function(data, dataIndex)
        {
          legends.forEach(function(legend, legendIndex)
          {
            result[legendIndex].values.push([Number(data.t) , Number(data.v[legendIndex])]);
            result[legendIndex].yvalues.push(Number(data.v[legendIndex]));
          });         
        });     
        return result;
      },
           //this function will take node page on heatmap individual node click
      graphOnClick : function(nodeIp) {
          for(var i = 0 ; i < com.impetus.ankush.commonMonitoring.nodesData.output.nodes.length ; i++)
              if(com.impetus.ankush.commonMonitoring.nodesData.output.nodes[i].publicIp == nodeIp){
                  com.impetus.ankush.commonMonitoring.nodeDrillDown(i);
                  return ;
              }
        },
        //this will send delete request for cluster
      deleteCluster : function() {
            if (com.impetus.ankush.commonMonitoring.clusterId == null) {
                com.impetus.ankush.removeChild(1);
                return;
            } else {
                $('#deleteClusterDialogcommonMonitor').modal('hide');
                $('#deleteClusterDialogcommonMonitor').remove();
                var deleteUrl = baseUrl + '/cluster/remove/' + com.impetus.ankush.commonMonitoring.clusterId;
                $
                .ajax({
                    'type' : 'DELETE',
                    'url' : deleteUrl,
                    'contentType' : 'application/json',
                    'dataType' : 'json',
                    'success' : function(result) {
                        if (result.output.status) {
                            com.impetus.ankush.removeChild(1);
                        } else
                            alert(result.output.error[0]);
                    },
                    error : function(data) {

                    }
                });
            }
        },
        // function used to check/uncheck all the nodes that can be deleted from the cluster during checking/unchecking of header check box
        checkAll_NodeList : function(elem) {
            var val = $('input:[name=checkHead_NodeList]').is(':checked');
            var nodeCount = nodeDetailsTable.fnGetData().length;
            var flag = false;
            if(val){
                flag = true;
                var j=0;

                for ( var i = 0; i < nodeCount; i++){
                    if(!($("#checkboxNode-" + i).attr('disabled'))){
                        $("#checkboxNode-" + i).attr("checked", flag);
                        j++;
                    }
                }
                if(j>0){
                    $('#btnNodeList_DeleteNodes').removeClass('disabled');
                    $("#btnNodeList_DeleteNodes").attr("onclick","com.impetus.ankush.commonMonitoring.sendDeleteNodesRequest()");
                }
            }else{
                if(!($("#btnNodeList_DeleteNodes").hasClass('disabled'))){
                    $('#btnNodeList_DeleteNodes').addClass('disabled');
                    $("#btnNodeList_DeleteNodes").removeAttr("onclick");
                }
                for ( var i = 0; i < nodeCount; i++){
                    if(!($("#checkboxNode-" + i).attr('disabled'))){
                        $("#checkboxNode-" + i).removeAttr('checked');
                    }
                }
            }
        },
        // function used to check all available nodes, retrieved during node addtion
        checkAllNode : function(elem) {
            if (addNodeCommonClusterTable.fnGetData().length != 0) {
                for ( var i = 0; i < nodeHadoopJSON_AddNodesCommon.length; i++) {
                    var val = $('input:[name=addNodeCheckHead]').is(':checked');
                    if (val) {
                        if (nodeHadoopJSON_AddNodesCommon[i][1] && nodeHadoopJSON_AddNodesCommon[i][2]
                        && nodeHadoopJSON_AddNodesCommon[i][3]) {
                            $("#hadoopAddNodeCheckBox-" + i).attr('checked',
                            'checked');
                            $("#addDataNode-" + i).attr('checked', 'checked');
                        }
                    } else {
                        $("#hadoopAddNodeCheckBox-" + i).removeAttr('checked');
                        $("#addSecNameNode-" + i).removeAttr('checked');
                        $("#addDataNode-" + i).removeAttr('checked');
                    }
                }
            }
        },

        //this will enable delete node button on node select
        enableDeleteButton : function(id) {
        	var checkedflag = false; 
        	com.impetus.ankush.headerCheckedOrNot('checkboxoption','nodeListCommonTech');
        	$('.checkboxoption').each(function(){
        		if($(this).is(":checked")){
        			$("#btnNodeList_DeleteNodes").removeClass('disabled');
                    $("#btnNodeList_DeleteNodes").attr("onclick","com.impetus.ankush.commonMonitoring.sendDeleteNodesRequest()");
                    checkedflag = true;
                    return;
        		}
        	});
        	if(!checkedflag)
        		$("#btnNodeList_DeleteNodes").addClass('disabled');
        },
       
        //this will check usage
        checkUsageValue : function(elem,value) {
        	if(!com.impetus.ankush.validate.empty(value)) {
                return "Value must not be empty.";
        	}else if(!com.impetus.ankush.validate.numeric(value)) {
                return "There must be a numeric value.";
            }else if(!((value > 0) && (value < 100))){
                return "Invalid value. 0 < value < 100";
            }else if(value.length > 3){
                return "Invalid value. There must be a 3 digit value.";
            }
        	
            var elementId= elem.id;
            var id = elementId.split('-');
            if(id[0]=="warningLevel"){
                if (parseInt(value,10) > parseInt($('#alertLevel-'+id[1]).text(),10)) {
                    return "Invalid value. Warning level must not be greater than alert level.";
                }
            }else if(id[0]=="alertLevel"){
                if (parseInt(value,10) < parseInt($('#warningLevel-'+id[1]).text(),10)) {
                    return "Invalid value. Alert level must not be less than warning level.";
                }
            }
        },
        //this function will validate pattern
        validatePattern : function(value, pattern) {
            if(value.match(pattern) != null) {
                return true;
            }
            return false;
        },
        //this will check whether percentage lie or not
        percentageCheck : function(x, min, max) {
            x = parseInt(x);
            min = parseInt(min);
            max = parseInt(max);
            return x >= min && x <= max;
        },
        /*Function for clienT-side validations of add node*/
        validateNodes : function() {
            $('#addNodeTable').show();
            $('#validateError').hide();
            $('#errorDivMainAddNode').hide();
            $('#validateError').html('');
            $("#errorDivMainAddNode").empty();
            var errorMsg = '';
            var ipRangeAddNode = $.trim($('#ipRangeAddNode').val());
            var filePathAddNode = $('#filePathAddNode').val();
            errorCount = 0;
            var flag = false;
            if (ipRangeAddNode != '' || filePathAddNode != '') {
                com.impetus.ankush.commonMonitoring.getAllNodes();
            }else {
                if ($('input[name=nodeSearchType]:checked').val() == 0) {
                    if (!com.impetus.ankush.validation.empty($('#ipRangeAddNode').val())) {
                        // $('#ipRangeAddNode').addClass('error-box');
                        errorCount++;
                        errorMsg = 'IP Range Field Empty';
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('ipRangeAddNode', 'IP Range cannot be empty');
                        flag = true;
                        var divId = 'ipRangeAddNode';
                        $("#errorDivMainAddNode").append(
                                        "<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.oClusterMonitoring.focusDiv(\""
                                                + divId
                                                + "\")' style='color: #5682C2;'>"
                                                + errorCount
                                                + ". "
                                                + errorMsg
                                                + "</a></div>");
                    }else {
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('ipRangeAddNode', 'Enter IP Range.');
                        $('#ipRangeAddNode').removeClass('error-box');
                    }
                }
                if ($('input[name=nodeSearchType]:checked').val() == 1) {
                    if (!com.impetus.ankush.validation.empty($('#filePathAddNode').val())) {
                        // $('#filePathAddNode').addClass('error-box');
                        errorCount++;
                        errorMsg = 'File path field empty';
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('filePathAddNode', 'File path cannot be empty');
                        flag = true;
                        var divId = 'filePathAddNode';
                        $("#errorDivMainAddNode").append(
                                        "<div class='errorLineDiv'><a href='javascript:com.impetus.ankush.oClusterMonitoring.focusDiv(\""
                                                + divId
                                                + "\")' style='color: #5682C2;'>"
                                                + errorCount
                                                + ". "
                                                + errorMsg
                                                + "</a></div>");
                    } else {
                        com.impetus.ankush.oClusterMonitoring.tooltipMsgChange('filePathAddNode', ' Browse file.');
                        $('#filePathAddNode').removeClass('error-box');
                    }
                }
                $('#validateError').show().html(errorCount + ' Error');
                $("#errorDivMainAddNode").show();

            }
        },
        // function used to retrieve nodes during add node operation
        getNewlyAddedNodes : function() {
        	$("#inspectNodeBtn").attr('disabled',true);
        	inspectNodeData = {};
            var typeNode = {
                    'Storm' : "Supervisor",
                    'Kafka' : "KafkaNode",
                    'Cassandra':"SeedNode",
                    'ElasticSearch' : "ElasticSearchNode"
                };
            var id = com.impetus.ankush.commonMonitoring.clusterId;
            com.impetus.ankush.commonMonitoring.validateRetrieveEvent();                                                 

            if(com.impetus.ankush.validation.errorCount > 0) {
                com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
                return;
            }
            $('#retrieve').text('Retrieving...');
            $('#retrieve').attr('disabled', true);
            $('#addNode').attr('disabled', true);

            $("#nodeCheckHead").removeAttr("checked");
            $("#error-div-hadoop").css("display", "none");
            $('#errorBtnHadoop').text("");
            $('#errorBtnHadoop').css("display", "none");
            var clusterId = id.toString();
            var url = baseUrl + "/cluster/detectNodes";
            var nodeData = {};

            nodeData.isFileUploaded = false;
            //if ($('#ipFile').attr('checked')) {
            if($("#addNodeGroupBtn .active").data("value")=="Upload File"){
                nodeData.isFileUploaded = true;
                nodeData.nodePattern = fileIPAddress_ServerPath;
            } else {
                nodeData.nodePattern = $('#ipRangeHadoop').val();
                saveNodePattern = $('#ipRangeHadoop').val();
            }
            nodeData.clusterId = clusterId;
            var errorNodeCount = null;
            $('#btnAll_HSN').removeClass('active');
            $('#btnSelected_HSN').removeClass('active');
            $('#btnAvailable_HSN').removeClass('active');
            $('#btnError_HSN').removeClass('active');
            $('#btnAll_HSN').addClass('active');
            if (($('#hadoopAddNodesIPRange').val() != '')
                    || ($('#hadoopAddNodesFilePath').val() != '')) {
                $
                .ajax({
                    'type' : 'POST',
                    'url' : url,
                    'contentType' : 'application/json',
                    'data' : JSON.stringify(nodeData),
                    "async" : true,
                    'dataType' : 'json',
                    'success' : function(result) {
                    	$("#inspectNodeBtn").attr('disabled',false);
                        $('#retrieve').text('Retrieve');
                        $('#retrieve').removeAttr('disabled');
                        $('#addNode').removeAttr('disabled');

                        // IP pattern invalid error message
                        if (result.output.error != null) {
                            com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,
                                    'popover-content', 'error-div-hadoop', 'errorBtnHadoop');
                            return;
                        }
                        addNodeCommonClusterTable.fnClearTable();
                        nodeTableLength_AddNodes = result.output.nodes.length;
                        nodeHadoopJSON_AddNodesCommon = result.output.nodes;
                        if (nodeTableLength_AddNodes > 0) {

                            for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
                                var checkNodeHadoop = '<input type="checkbox" class="inspect-node checkBoxAddNode" onclick="com.impetus.ankush.headerCheckedOrNot(\'checkBoxAddNode\',\'addNodeCheckHead\')" id="hadoopAddNodeCheckBox-'
                                    + i
                                    + '" />';
                                var ipHadoop = nodeHadoopJSON_AddNodesCommon[i][0];
                                var os = '<span style="font-weight:bold;" id="addNodeOS-'
                                    + i
                                    + '">'
                                    + nodeHadoopJSON_AddNodesCommon[i][4]
                                + '</span>';
                                var navigationHadoop = '<a href="#"><img id="navigationImgAddHadoop'
                                    + i
                                    + '" src="'
                                    + baseUrl
                                    + '/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.commonMonitoring.loadNodeDetailCommon('
                                    + i + ');"/></a>';
                                var addId = addNodeCommonClusterTable
                                .fnAddData([
                                             checkNodeHadoop,
                                             ipHadoop,
                                             typeNode[com.impetus.ankush.commonMonitoring.clusterTechnology],
                                             os,
                                             navigationHadoop
                                             ]);
                                var theNode = addNodeCommonClusterTable.fnSettings().aoData[addId[0]].nTr;
                                nodeHadoopJSON_AddNodesCommon[i][0].split('.').join('_');
                                theNode.setAttribute('id', 'addNode-' + i);
                                if (!(nodeHadoopJSON_AddNodesCommon[i][1]
                                && nodeHadoopJSON_AddNodesCommon[i][2] && nodeHadoopJSON_AddNodesCommon[i][3])) {
                                    $('#hadoopAddNodeCheckBox-' + i).attr(
                                            'disabled', true);
                                    $("#addSecNameNode-" + i).attr(
                                            'disabled', true);
                                    $("#addDataNode-" + i).attr('disabled',
                                            true);
                                    $('#addNode-' + i)
                                    .addClass('error');
                                    errorNodeCount++;
                                }
                                $('#addNode-' + i).addClass('selected');
                            }
                        }
                        enabledNodes = nodeTableLength_AddNodes - errorNodeCount;
                    }
                });
            } else {
                $('#error').empty();
                $('#error').append("Please specify valid node pattern.");
            }
        },
        // function used to validate whether node Ip address is provided or not for node retrieval during add node operation
        validateRetrieveEvent : function() {
            $("#popover-content").empty();
            com.impetus.ankush.validation.errorCount = 0;
            if($("#addNodeGroupBtn .active").data("value")=="Range"){

//            if ($('#ipRange').attr('checked')){
                com.impetus.ankush.validation.validateField('required', 'ipRangeHadoop', 'IP Range', 'popover-content');
            }
            else{
                com.impetus.ankush.validation.validateField('required', 'filePath_IPAddressFile', 'Upload IP Address File', 'popover-content');
            }
        },
        // function used to add nodes to cluster
        addNodeSetup : function() {
            var hashForClass = {
                'Storm' : "com.impetus.ankush.storm.StormClusterConf",
                'Kafka' : "com.impetus.ankush.kafka.KafkaClusterConf",
                'Cassandra':"com.impetus.ankush.cassandra.CassandraClusterConf",
                'ElasticSearch' : "com.impetus.ankush.elasticsearch.ElasticSearchClusterConf"   
            };
            var clusterId = com.impetus.ankush.commonMonitoring.clusterId;
            com.impetus.ankush.commonMonitoring.validateNodeSelection();
            if(com.impetus.ankush.validation.errorCount > 0) {
                com.impetus.ankush.validation.showErrorDiv('error-div-hadoop', 'errorBtnHadoop');
                return;
            }
            $('#ipRangeHadoop').val($.trim($('#ipRangeHadoop').val()));
            $("#nodeCheckHead").removeAttr("checked");
            $("#error-div-hadoop").css("display", "none");
            $('#errorBtnHadoop').text("");
            $('#errorBtnHadoop').css("display", "none");
            var node = null;
            var addNodeData = {};
            addNodeData["@class"] = hashForClass[com.impetus.ankush.commonMonitoring.clusterTechnology];
            addNodeData.newNodes = new Array();
            if($("#addNodeGroupBtn .active").data("value")=="Range")
            //if ($('#ipRange').attr('checked'))
                addNodeData.ipPattern = $('#ipRangeHadoop').val();
            else
                addNodeData.patternFile = fileIPAddress_ServerPath;
            var numberOfRetrievedNodes = $('#addNodeIpTableHadoop tr').length - 1;
            if (numberOfRetrievedNodes > 0) {
                for ( var i = 0; i < numberOfRetrievedNodes; i++) {
                    if ($('#hadoopAddNodeCheckBox-' + i).is(':checked')) {
                        node = {
                                "publicIp" : nodeHadoopJSON_AddNodesCommon[i][0],
                                "privateIp" : nodeHadoopJSON_AddNodesCommon[i][0],
                                "os" : $("#addNodeOS-" + i).text(),
                                "nodeState" : "adding"
                        };
                        addNodeData.newNodes.push(node);
                    }
                }
            }
            var addNodeUrl = baseUrl + "/cluster/" + clusterId + "/add/";
            $.ajax({
                'type' : 'POST',
                'url' : addNodeUrl,
                'contentType' : 'application/json',
                'data' : JSON.stringify(addNodeData),
                "async" : false,
                'dataType' : 'json',
                'success' : function(result) {
                    if(result.output.status) {
                        com.impetus.ankush.removeChild(2);   
                    }
                    else {
                        com.impetus.ankush.validation.showAjaxCallErrors(result.output.error, 'popover-content-commonAddNode', 'error-div-commonAddNode', 'errorBtnCommonAddNode');
                        return;
                    }
                },
                'error' : function() {
                },
            });
        },
        // function used to validate whether a node is available for adding to cluster
        validateNodeSelection : function() {
            var flag_NodeCount = false;
            com.impetus.ankush.validation.errorCount = 0;
            $("#popover-content-commonAddNode").empty();
            if (nodeTableLength_AddNodes == null || nodeTableLength_AddNodes == 0) {
                errorMsg = 'Select at-least one node or retrieve node.';
                com.impetus.ankush.validation.showAjaxCallErrors([errorMsg], 'popover-content-commonAddNode', 'error-div-commonAddNode', 'errorBtnCommonAddNode');
            } else {
                for ( var i = 0; i < nodeTableLength_AddNodes; i++) {

                    if ($("#hadoopAddNodeCheckBox-" + i).attr("checked")) {
                        flag_NodeCount = true;
                        break;
                    }
                }
                if (!flag_NodeCount) {
                    errorMsg = 'Select at-least one node or retrieve node.';
                    com.impetus.ankush.validation.showAjaxCallErrors([errorMsg], 'popover-content-commonAddNode', 'error-div-commonAddNode', 'errorBtnCommonAddNode');
                }
            }
        },
        // this function will Create Utilization Trend Table
        utilizationTrend : function(){
            com.impetus.ankush.commonMonitoring.loadChild(baseUrl + '/commonMonitoring/utilizationTrend','get',com.impetus.ankush.commonMonitoring.clusterName+'/ Utilization Graphs',com.impetus.ankush.commonMonitoring.clusterName,null,'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);');
        },
        //this function will create Node Utilization trend class
        nodeUtilizationTrend : function(){
                $('#content-panel').sectionSlider('addChildPanel', {
                    url : baseUrl + '/commonMonitoring/nodeUtilizationTrend',
                    method : 'get',
                    title : ipForNodeDrillDown+'/Utilization Graphs',
                    tooltipTitle : com.impetus.ankush.commonMonitoring.clusterName,
                    previousCallBack : "com.impetus.ankush.commonMonitoring.removeChildPreviousNodeUtilizationTrendPageLoad();",
                    callback : function() {
                       
                    },
                    params : {
                        clusterTechnology : com.impetus.ankush.commonMonitoring.clusterTechnology,
                    },
                    callbackData : {
                    }
                });   
        },
        // graph related function
        loadClusterLevelGraphs : function(startTime,autorefresh) {
        	if(autorefresh != 'autorefresh'){
                loadClusterLevelGraphsStartTime = startTime;
                $('#pageHeadingUtilization').text(com.impetus.ankush.commonMonitoring.clusterName+'/Utilization Metrics');
    //            $("#utilizationTrendGraphs").clear();
                $('#utilizationTrendGraphs').empty();
                // to get the json for cpu graph.
                com.impetus.ankush.commonMonitoring.loadGraph(startTime, 'cpu', 'cpu_.*\\.rrd');
                // to get the json for memory graph.
                com.impetus.ankush.commonMonitoring.loadGraph(startTime, 'memory', '(mem|swap)_.*\\.rrd');
                // to get the json for network graph.
                com.impetus.ankush.commonMonitoring.loadGraph(startTime, 'network', 'bytes_.*\\.rrd');
                // to get the json for load graph.
                com.impetus.ankush.commonMonitoring.loadGraph(startTime, 'load', '(load_.*\\.rrd|proc_run.rrd)');
                // to get the json for packed graphs.
                com.impetus.ankush.commonMonitoring.loadGraph(startTime, 'packet', 'pkts_.*\\.rrd');
            }
            else{
                // to get the json for cpu graph.
                com.impetus.ankush.commonMonitoring.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'cpu', 'cpu_.*\\.rrd');
                // to get the json for memory graph.
                com.impetus.ankush.commonMonitoring.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'memory', '(mem|swap)_.*\\.rrd');
                // to get the json for network graph.
                com.impetus.ankush.commonMonitoring.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'network', 'bytes_.*\\.rrd');
                // to get the json for load graph.
                com.impetus.ankush.commonMonitoring.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'load', '(load_.*\\.rrd|proc_run.rrd)');
                // to get the json for packed graphs.
                com.impetus.ankush.commonMonitoring.loadGraphRedraw(loadClusterLevelGraphsStartTime, 'packet', 'pkts_.*\\.rrd');
            }
        },
        loadGraph : function(startTime, key, value,autorefresh) {
            var graphUrl =  baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/graphs?startTime='+startTime+'&pattern=' + value;
            if(autorefresh != 'autorefresh')
                $("#utilizationTrendGraphs").append('<div class="span6" style="margin-top:10px;"><div class="row-fluid section-heading" style="margin-top:10px;">'+key.toUpperCase()+'</div><div class="row-fluid box infobox masonry-brick"><div id="graph_'+key+'" style="margin-top:10px;" class="graphTile"><svg></svg></div></div></div>');
            $('#graph_'+key+' svg').css({
                'height': '250px',
                'width': '450px',
            });
            com.impetus.ankush.placeAjaxCall(graphUrl, "GET", true, null, function(result) {
                if(result.output.status) {
                    var formatString = '';
                    switch(startTime){
                    case 'lastday':
                        formatString = '%H:%M';
                        break;
                    case 'lastweek':
                        formatString = '%a %H:%M';
                        break;
                    case 'lastmonth':
                        formatString = '%d/%m';
                        break;
                    case 'lastyear':
                        formatString = '%b';
                        break;
                    default:
                        formatString = '%H:%M:%S';
                    }
                        var newResult = com.impetus.ankush.commonMonitoring.normalizeData(result.output.json);
                    nv.addGraph(function() {
                        clusterUtilizationTrend[key] = nv.models.lineChart()
                            .x(function(d) { return d[0]; })
                            .y(function(d) { return d[1]; })
                            .clipEdge(true);
                        clusterUtilizationTrend[key].margin().left = 85;
                        clusterUtilizationTrend[key].margin().right = 40;
                        clusterUtilizationTrend[key].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
                        var format = null;
                        var maxValue = 0;
                       
                        $.each(newResult, function(key, mapvalue){
                            var keyMaxVal = Math.max.apply(Math, mapvalue.yvalues);
                            if(keyMaxVal > maxValue) {
                                maxValue = keyMaxVal;
                            }
                        });
                        var maxDecimals = com.impetus.ankush.commonMonitoring.countDecimals(maxValue);
                        if(key == 'network' || key == 'memory' || key == 'packet') {
                        	clusterUtilizationTrend[key].yAxis.ticks(10).
                            tickFormat(function (d) {
                                var suffix = ' B';
                                if(maxDecimals < 3 ) {
                                    d = d;
                                } else if(maxDecimals < 6 ) {
                                    d = ((d/1024));
                                    suffix = " k";
                                } else if(maxDecimals < 9 ){
                                    d = ((d/1024)/1024);
                                    suffix = " M";
                                } else if(maxDecimals < 12){
                                    d = ((d/1024)/(1024*1024));
                                    suffix = " G";
                                } else {
                                    d = ((d/1024)/(1024*1024*1024));
                                    suffix = " T";
                                }
                                d = d3.format(".1f")(d);
                                return d + suffix;});
                        }else{
                        	clusterUtilizationTrend[key].yAxis.tickFormat(d3.format(".1f")).ticks(10);
                        }
                        clusterUtilizationTrend[key].yAxis.axisLabel(result.output.json.unit);
//                        var legend = chart.legend();
                        var svg = d3.select('#graph_'+key+' svg')
                            .datum(newResult)
                            .transition().duration(0).call(clusterUtilizationTrend[key]);
                        nv.utils.windowResize(clusterUtilizationTrend[key].update);
                   });
                }
                else {
                    $("#graph_"+key).empty();
                    $("#graph_"+key).append("<div id=error_"+key+"></div>");
                    $("#error_"+key).append('<h2>Sorry, Unable to get '+key+' graph.</h2>').css({
                        'text-align' : 'center',
                        'height': '250px',
                        'width': '450px',
                    });
                    $('#graphButtonGroup_JobMonitoring').css("display", "none");
                }
            });   
        },
        loadGraphRedraw : function(startTime, key, value) {
        	 var graphUrl =  baseUrl + '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/graphs?startTime='+startTime+'&pattern=' + value;
             $('#graph_'+key+' svg').css({
                 'height': '250px',
                 'width': '450px',
             });
             com.impetus.ankush.placeAjaxCall(graphUrl, "GET", true, null, function(result) {
                 if(result.output.status) {
                     var formatString = '';
                     switch(startTime){
                     case 'lastday':
                         formatString = '%H:%M';
                         break;
                     case 'lastweek':
                         formatString = '%a %H:%M';
                         break;
                     case 'lastmonth':
                         formatString = '%d/%m';
                         break;
                     case 'lastyear':
                         formatString = '%b';
                         break;
                     default:
                         formatString = '%H:%M:%S';
                     }
                         var newResult = com.impetus.ankush.commonMonitoring.normalizeData(result.output.json);
                         clusterUtilizationTrend[key].xAxis.tickFormat(function(d) { return d3.time.format(formatString)(new Date(d*1000)); }).ticks(10);
                         var format = null;
                         var maxValue = 0;
                        
                         $.each(newResult, function(key, mapvalue){
                             var keyMaxVal = Math.max.apply(Math, mapvalue.yvalues);
                             if(keyMaxVal > maxValue) {
                                 maxValue = keyMaxVal;
                             }
                         });
                         var maxDecimals = com.impetus.ankush.commonMonitoring.countDecimals(maxValue);
                         if(key == 'network' || key == 'memory' || key == 'packet') {
                         	clusterUtilizationTrend[key].yAxis.ticks(10).
                             tickFormat(function (d) {
                                 var suffix = ' B';
                                 if(maxDecimals < 3 ) {
                                     d = d;
                                 } else if(maxDecimals < 6 ) {
                                     d = ((d/1024));
                                     suffix = " k";
                                 } else if(maxDecimals < 9 ){
                                     d = ((d/1024)/1024);
                                     suffix = " M";
                                 } else if(maxDecimals < 12){
                                     d = ((d/1024)/(1024*1024));
                                     suffix = " G";
                                 } else {
                                     d = ((d/1024)/(1024*1024*1024));
                                     suffix = " T";
                                 }
                                 d = d3.format(".1f")(d);
                                 return d + suffix;});
                         }else{
                         	clusterUtilizationTrend[key].yAxis.tickFormat(d3.format(".1f")).ticks(10);
                         }
                         clusterUtilizationTrend[key].yAxis.axisLabel(result.output.json.unit);
//                         var legend = chart.legend();
                         var svg = d3.select('#graph_'+key+' svg')
                             .datum(newResult)
                             .transition().duration(0).call(clusterUtilizationTrend[key]);
                         nv.utils.windowResize(clusterUtilizationTrend[key].update);
                    
                 }
                 else {
                     $("#graph_"+key).empty();
                     $("#graph_"+key).append("<div id=error_"+key+"></div>");
                     $("#error_"+key).append('<h2>Sorry, Unable to get '+key+' graph.</h2>').css({
                         'text-align' : 'center',
                         'height': '250px',
                         'width': '450px',
                     });
                     $('#graphButtonGroup_JobMonitoring').css("display", "none");
                 }
             });    
        },
        countDecimals : function(v) {
            var test = v, count = 0;
            while(test > 10) {
                test /= 10;
                count++;
            }
            return count;
        },
        // custom formatting functions:
        toTerra :function (d) { return (Math.round(d/10000000000)/100) + " T"; },
        toGiga : function(d)  { return (Math.round(d/10000000)/100) + " G"; },
        toMega :function(d)  { return (Math.round(d/10000)/100) + " M"; },
        toKilo: function(d)  { return (Math.round(d/10)/100) + " k"; },
        loadAddNodeProgress : function() {
            $('#content-panel').sectionSlider(
                    'addChildPanel',
                    {
                        current : 'login-panel',
                        url : baseUrl + '/commonMonitoring/addNodeProgress',
                        method : 'get',
                        previousCallBack : 'com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);',
                        params : {},
                        title : 'Add Nodes Progress',
                        tooltipTitle : com.impetus.ankush.commonMonitoring.clusterName,
                        callback : function() {
                            com.impetus.ankush.commonMonitoring.addNodeProgress();
                            $('#ipRangeAddNode').val(saveNodePattern);
                        },
                        callbackData : {
                            //clusterId : clusterId
                        }
                    });
        },
        loadNodeDetailProgressLogs : function(nodeIndex){
            $('#content-panel').sectionSlider(
                    'addChildPanel',
                    {
                        url : baseUrl + '/commonMonitoring/addNodeProgressLogs',
                        method : 'get',
                        params : {},
                        title : 'Node Status',
                        tooltipTitle : 'Adding Nodes',
                        callback : function(data) {
                            com.impetus.ankush.commonMonitoring.nodeLogs(nodeIndex);
                        }
                    });
        },
        loadNodeDetailCommon : function(id){
            console.log(nodeHadoopJSON_AddNodesCommon[id]);
            $('#content-panel').sectionSlider(
                    'addChildPanel',
                    {
                        url : baseUrl + '/commonMonitoring/commonNodeDetails',
                        method : 'get',
                        params : {},
                        title : 'Node Status',
                        tooltipTitle : 'Add Nodes',
                        callback : function() {
                            com.impetus.ankush.commonMonitoring.loadCommonNodePage(id);
                        },
                        callbackData : {
                            //id : id
                        }
                    });
        },
        loadCommonNodePage : function(id){
            $('#nodeIp-Hadoop').text(nodeHadoopJSON_AddNodesCommon[id][0]);
            if(nodeHadoopJSON_AddNodesCommon[id][1])
                $('#nodeStatus-common').text('Available');
            else
                $('#nodeStatus-common').text('Unavailable');
            if(nodeHadoopJSON_AddNodesCommon[id][2])
                $('#nodeReachable-common').text('Yes');
            else
                $('#nodeReachable-common').text('No');
            if(nodeHadoopJSON_AddNodesCommon[id][3])
                $('#nodeAuthentication-common').text('Yes');
            else
                $('#nodeAuthentication-common').text('No');
            $('#OSName-common').text(nodeHadoopJSON_AddNodesCommon[id][4]);
        },
        addNodeProgress : function(){
            var addingNodeUrl = baseUrl+ '/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/addingnodes';
            com.impetus.ankush.placeAjaxCall(addingNodeUrl, "GET", false,null,function(result){
            	addingNodeUrlData = result;
            	nodeIpProgressTable.fnClearTable();
	            var progress = '<a href="#"><img id="navigationImgAddingNode'+ i+ '" src="'+ baseUrl+ '/public/images/loading.gif" width="16" height="16"/></a>';
	                if(addingNodeUrlData.output.status == true){
	                	if(addingNodeUrlData.output.nodes != undefined){
			            	if(addingNodeUrlData.output.state == 'deployed'){
			                    progress = '';
			                    $('#addNodeStatusLabel').addClass('label-success');
			                    $('#addNodeStatusLabel').text('Node Deployment success');
			                    is_autorefreshTiles_addingNodes = window.clearInterval(is_autorefreshTiles_addingNodes);
			                }
			                else if(addingNodeUrlData.output.state == 'error'){
			                    progress = '';
			                    $('#addNodeStatusLabel').addClass('label-important');
			                    $('#addNodeStatusLabel').text('Error in Node Deployment');
			                    is_autorefreshTiles_addingNodes = window.clearInterval(is_autorefreshTiles_addingNodes);
			                }
			                for(var i = 0 ; i < addingNodeUrlData.output.nodes.length ; i++){
			                    nodeIpProgressTable.fnAddData([
			                                                       progress,
			                                                       addingNodeUrlData.output.nodes[i].publicIp,
			                                                       addingNodeUrlData.output.nodes[i].type,
			                                                       addingNodeUrlData.output.nodes[i].message,
			                                                       '<a href="#"><img id="navigationImgAddingNode'
			                                                    + i
			                                                    + '" src="'
			                                                    + baseUrl
			                                                    + '/public/images/icon-chevron-right.png" onclick="com.impetus.ankush.commonMonitoring.loadNodeDetailProgressLogs('
			                                                    + i + ');"/></a>'
			                                                   ]);
			                }
	                	}
	                }
	               // else
	                	//com.impetus.ankush.validation.showAjaxCallErrors(result.output.error,'popover-content-addNodeProgress', 'error-div-addNodeProgress', 'errorBtnAddnodeProgress');
	        });
        },
        /*Function for showing node level deployment logs*/
        nodeLogs:function(node){
            nodeDeploymentIntervalCommon = window.clearInterval(nodeDeploymentIntervalCommon);
             var ipAddress =addingNodeUrlData.output.nodes[node].publicIp;
             $('#nodeDetailHead1').html('').text(ipAddress);
             for(var key in addingNodeUrlData.output.nodes[node]){
                 var newKey=com.impetus.ankush.camelCaseKey(key);
                 if(key !='errors')
                 $('#nodeDeploymentField').append('<div class="row-fluid"><div class="span2"><label class=" text-right form-label">'+newKey+':</label></div><div class="span10"><label class="form-label label-black">'+addingNodeUrlData.output.nodes[node][key]+'</label></div></div>');
             }
           
             var url = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/logs?ip='+ipAddress;
             $.ajax({
                    'type' : 'GET',
                    'url' : url,
                    'contentType' : 'application/json',
                    'dataType' : 'json',
                    'async' : true,
                    'success' : function(result) {
                      if(result.output.status){
                         nodeDeployLogDataCommon=result.output;
                          for ( var i = 0; i < nodeDeployLogDataCommon.logs.length; i++) {
                              if(nodeDeployLogDataCommon.logs[i].type == 'error'){
                                  $('#nodeDeployProgress').prepend('<div style="color:red">' +
                                          nodeDeployLogDataCommon.logs[i].longMessage +  '</div>');
                              }else{
                                  $('#nodeDeployProgress').prepend('<div>' +
                                          nodeDeployLogDataCommon.logs[i].longMessage +  '</div>');
                              }             
                          }
                             if(addingNodeUrlData.output.state =='error'){
                                 nodeDeploymentIntervalCommon=window.clearInterval(nodeDeploymentIntervalCommon);
                                 if(Object.keys(addingNodeUrlData.output.nodes[node].errors).length>0){
                                     $('#nodeErrorDiv').show();
                                 }
                                  $('#errorOnNodeDiv').css("margin-top","10px");
                                  for(var key in addingNodeUrlData.output.nodes[node].errors){
                                      $('#errorOnNodeDiv').append('<label class="text-left" style="color: black;" id="'+key+'">'+addingNodeUrlData.output.nodes[node].errors[key]+'</label>');
                                       }
                              } 
                          if(nodeDeployLogDataCommon.state =='deployed' || nodeDeployLogDataCommon.state =='error'){
                              nodeDeploymentIntervalCommon=window.clearInterval(nodeDeploymentIntervalCommon);
                          } else{
                              com.impetus.ankush.commonMonitoring.initNodeLogs(node);
                          }
                    }
                    },
             });
        },
        /* Function for initializing auto-refresh call of node deployment logs function*/
        initNodeLogs:function(nodeIndex){
        nodeDeploymentIntervalCommon=window.clearInterval(nodeDeploymentIntervalCommon);
             nodeDeploymentIntervalCommon = setInterval('com.impetus.ankush.commonMonitoring.nodeLogsRefresh('+nodeIndex+');', refreshIntervalCommon);
        },
    /*Function to be called for auto-refresh of node level deployment logs*/
        nodeLogsRefresh:function(nodeIndex){
            var ipAddress =addingNodeUrlData.output.nodes[nodeIndex].publicIp;   
             if(document.getElementById("nodeDeployProgress") == null){
                    nodeDeploymentIntervalCommon = window.clearInterval(nodeDeploymentIntervalCommon);
                    return;
                }
                var url = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId+ '/logs?ip=' + ipAddress +'&lastlog='+nodeDeployLogDataCommon.lastlog;
                $.ajax({
                    'type' : 'GET',
                    'url' : url,
                    'contentType' : 'application/json',
                    'dataType' : 'json',
                    'async' : true,
                    'success' : function(result) {
                      if(result.output.status){   
                          nodeDeployLogDataCommon = result.output;
                        $('#nodeDetailHead1').html('').text(ipAddress);
                        for ( var i = 0; i < nodeDeployLogDataCommon.logs.length; i++) {
                            if(nodeDeployLogDataCommon.logs[i].type == 'error'){
                                $('#nodeDeployProgress').prepend('<div style="color:red">' +
                                        nodeDeployLogDataCommon.logs[i].longMessage +  '</div>');
                            }else{
                                $('#nodeDeployProgress').prepend('<div>' +
                                        nodeDeployLogDataCommon.logs[i].longMessage +  '</div>');
                            }             
                        }
                        if(nodeDeployLogDataCommon.state =='deployed' || nodeDeployLogDataCommon.state =='error'){
                            nodeDeploymentIntervalCommon=window.clearInterval(nodeDeploymentIntervalCommon);
                        }
                    }
                    },
                    error : function() {
                    }
                });
        },
        // function used to show diffrent types of node in a cluster, viz. namenode , secondary namenode, etc.
        getDefaultLogDownloadValue : function() {
            $("#fileSizeExceed").appendTo('body');
            readCount = 0;
            var logUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/techlogs';
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid')
                logUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/techlogs?technology='+com.impetus.ankush.commonMonitoring.hybridTechnology;
            com.impetus.ankush.placeAjaxCall(logUrl, 'GET', true, null, function(data){
                logData = data;
                if (logData != null) {
                    $("#nodeType").empty();
                    for ( var type in logData.output) {
                        if (type != "status") {
                            $("#nodeType").append(
                                    "<option id='" + type + "'>" + type + "</option>");
                        }
                    }
                    com.impetus.ankush.commonMonitoring.fillNodeType();
                }
            });
        },
       
        diableLogButtons : function() {
        	$("#viewLogs").attr("disabled", true);
        	$("#downloadLogs").attr("disabled", true);
        },
        
        enableLogButtons : function() {
        	$("#viewLogs").removeAttr("disabled");
        	$("#downloadLogs").removeAttr("disabled");
        },
        
        // function used to get nodes Ip depending on the type of node selected in the node type Dropdown
        fillNodeType : function() {
            com.impetus.ankush.commonMonitoring.removeErrorClass();
            com.impetus.ankush.commonMonitoring.diableLogButtons();
            $("#nodeIP").empty();
            var currentNodeType = $("#nodeType").val();
            var numberOfNodes = logData.output[currentNodeType].length;
            logDataOutput = logData.output[currentNodeType];
            $("#div_Logs").css("display", "none");
            if (numberOfNodes > 0) {
            	for ( var index = 0 ; index < numberOfNodes ; index++) {
                	$("#nodeIP").append(
                            "<option id='node-" + index + "'>" + logDataOutput[index]
                            + "</option>");
                }
                com.impetus.ankush.commonMonitoring.nodeIPChange();
            }
        },
        // function used to show all the log files in hadoop cluster of node selected
        nodeIPChange : function() {
            com.impetus.ankush.commonMonitoring.removeErrorClass();
            com.impetus.ankush.commonMonitoring.diableLogButtons();
            var currentNodeType = $("#nodeType").val();
            var currentNodeIP = $("#nodeIP").val();
            var logFilenameUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId+ '/files?ip=' + currentNodeIP + '&type=' + currentNodeType;
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
                logFilenameUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId+ '/files?ip=' + currentNodeIP + '&type=' + currentNodeType+'&technology='+com.impetus.ankush.commonMonitoring.hybridTechnology;
            }
            com.impetus.ankush.placeAjaxCall(logFilenameUrl,'GET', true, null, function(logFileNameData){
                $("#div_Logs").css("display", "none");
                if (logFileNameData != null) {
                	if(!logFileNameData.output.status){
                        com.impetus.ankush.commonMonitoring.validateLogs(logFileNameData,"nodeDown");
                        $("#fileName").empty();
                    }else{
                        logFileNameOutputCommon = logFileNameData.output.files;
                        $("#fileName").empty();
                        var i =0 ;
                        $.each(logFileNameOutputCommon, function(key, value) {
                            $("#fileName").append(
                                    "<option id='filename-" + i + "'>"
                                    + value + "</option>");
                            i++;
                        });
                        $("#logView").empty();
                        com.impetus.ankush.commonMonitoring.enableLogButtons();
                    }
                }
            });
        },
        // function called during selection of a diffrent log file in a dropdown to clear the previous file data
        fileNameChange : function() {
            com.impetus.ankush.commonMonitoring.removeErrorClass();
            $("#logView").empty();
            $("#div_Logs").css("display", "none");
        },

        // function used to display logs of the file selected. Data is loaded using lazy loading
        logDisplay : function() {
            readCount = 0;
            $(window).unbind("scroll");
            $(window).bind("scroll" ,function () {           
                if ($(window).scrollTop() == ( $(document).height() - $(window).height())) {
                    com.impetus.ankush.commonMonitoring.appendLog();
                }
            });

            var currentNodeIP = $("#nodeIP").val();
            var currentFileName = $("#fileName").val();
            var logFileURL = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000';
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
                logFileURL = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000&technology='+com.impetus.ankush.commonMonitoring.hybridTechnology;
            }
            var logFileData = com.impetus.ankush.placeAjaxCall(logFileURL, 'GET',
                    false);
            if (logFileData != null) {
                if(logFileData.output.status) {
                	if(logFileData.output.content == ""){
                		logFileData.output.content = "No data found in file.";
                		$('#downloadLogs').attr('disabled',true);
                	}
                	else
                		$('#downloadLogs').attr('disabled',false);
                    $("#div_Logs").css("display", "block");
                    $("#logView").empty();
                    $("#logView").append(logFileData.output.content);
                    readCount = logFileData.output.readCount;
                }else{
                    com.impetus.ankush.commonMonitoring.validateLogs(logFileData,"filename");
                }
            } else {
                alert("Sorry, unable to retrieve the Log file");
            }
        },
        // function used to append log data, during lazy-load, at the end of the previously shown logs
        appendLog : function() {
            var currentNodeIP = $("#nodeIP").val();
            var currentFileName = $("#fileName").val();
            var logFileURL = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000';
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
                logFileURL = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId + '/view?ip=' + currentNodeIP + '&fileName='+ currentFileName+ '&readCount=' + readCount+ '&bytesCount=10000&technology='+com.impetus.ankush.commonMonitoring.hybridTechnology;
            }
            var logFileData = com.impetus.ankush.placeAjaxCall(logFileURL, 'GET',
                    false);
            if (logFileData != null) {
                if(logFileData.output.status) {
                	if(logFileData.output.content == ""){
                		logFileData.output.content = "No data found in file.";
                		$('#downloadLogs').attr('disabled',true);
                	}
                	else
                		$('#downloadLogs').attr('disabled',false);
                    $("#div_Logs").css("display", "block");
                    $("#logView").append(logFileData.output.content);
                    readCount = logFileData.output.readCount;
                }
                else{
                    $(window).unbind("scroll");
                }
            } else {
                alert("Sorry, unable to retrieve the Log file");
            }
        },

        // function used to download a log file
        download : function(clusterId) {
            var currentNodeIP = $("#nodeIP").val();
            var currentFileName = $("#fileName").val();
            var downloadUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId+ '/download?ip=' + currentNodeIP + '&fileName='+ currentFileName;
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
                downloadUrl = baseUrl + '/monitor/' + com.impetus.ankush.commonMonitoring.clusterId+ '/download?ip=' + currentNodeIP + '&fileName='+ currentFileName+'&technology='+com.impetus.ankush.commonMonitoring.hybridTechnology;
            }
            var downloadFileSize = logFileNameOutputCommon[currentFileName];

            if(downloadFileSize>5120){
                $("#fileSizeExceed").modal('show');
                $('.ui-dialog-titlebar').hide();
                $('.ui-dialog :button').blur();
                return;
            }
            var downloadUrlData = com.impetus.ankush.placeAjaxCall(downloadUrl,
                    'GET', false);
            if (downloadUrlData.output.status == true) {
                var downloadFilePath = baseUrl + downloadUrlData.output.downloadPath;
                var hiddenIFrameID = 'hiddenDownloader', iframe = document.getElementById(hiddenIFrameID);
                if (iframe === null) {
                    iframe = document.createElement('iframe');
                    iframe.id = hiddenIFrameID;
                    iframe.style.display = 'none';
                    document.body.appendChild(iframe);
                }
                iframe.src = downloadFilePath;
            }else {
                com.impetus.ankush.commonMonitoring.validateLogs(downloadUrlData,"filename");
            }
        },
        //this function will inspect nodes
    inspectNodesObject : function(id){
        var data = {};
        data.nodePorts = {};
        $('.inspect-node').each(function(){
            if($(this).is(':checked')){
                $(this).addClass('inspect-node-ok');
                var ip = $(this).parent().next().text();
                data.nodePorts[ip] = [];
            }
        });
        data.clusterId = com.impetus.ankush.commonMonitoring.clusterId;
        com.impetus.ankush.inspectNodesCall(data,id,'retrieve');
    },
        // function used to validate whether a log file can be dowloaded or not
        validateLogs : function(urlData,errorType) {
            var focusDivId = null;
            if(errorType == "nodeDown"){
                focusDivId = "nodeIP";
            }else if(errorType == "filename"){
                focusDivId = "fileName";
            }
            $("#popover-content-hadoopLogs").empty();
            $("#error-div-hadoopLogs").css("display", "none");
            $('#errorBtnHadoopLogs').text("");
            $('#errorBtnHadoopLogs').css("display", "none");
            com.impetus.ankush.validation.errorCount = 0;
            var i=0;
            $.each( urlData.output.error,
                    function(index,value){
                i = index + 1;
                com.impetus.ankush.validation.addNewErrorToDiv(focusDivId,'popover-content-hadoopLogs',value,null);
            });
            if(com.impetus.ankush.validation.errorCount > 0) {
                com.impetus.ankush.validation.showErrorDiv('error-div-hadoopLogs', 'errorBtnHadoopLogs');
                $('#viewLogs').attr('disabled',true);
				$('#downloadLogs').attr('disabled',true);
                return;
            }
        },

        // function used to remove error-class
        removeErrorClass : function() {
            $('#nodeIP').removeClass('error-box');
            $('#fileName').removeClass('error-box');
            $("#error-div-hadoopLogs").css("display", "none");
            $('#errorBtnHadoopLogs').css("display", "none");
        },
        //Delete cluster dialog
        deleteClusterDialog : function(){
            $("#deleteClusterDialogcommonMonitor").appendTo('body').modal('show');
            $('.ui-dialog-titlebar').hide();
            $('.ui-dialog :button').blur();
        },
        //decide previous call back
        submitTopoPreviousCall : function(){
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Hybrid'){
                com.impetus.ankush.stormMonitoring.stormMonitoringPageAutorefresh();
                com.impetus.ankush.stormMonitoring.createTopologySummaryTable();
                com.impetus.ankush.hybridMonitoring_storm.createTiles();
            }
            else
                com.impetus.ankush.commonMonitoring.removeChildPreviousNodesPageLoad(1);
        },
         // function used to show information on tooltip of node IP details
        divShowOnClickIPAddress : function(clickId) {
            $('#ipRangeHadoop').removeClass('error-box');
            $('#ipRangeHadoop').tooltip();
            com.impetus.ankush.common.tooltipOriginal(
                    'ipRangeHadoop', 'Enter IP Address Range');

            $('#filePath_IPAddressFile').removeClass('error-box');
            $('#filePath_IPAddressFile').tooltip();
            com.impetus.ankush.common.tooltipOriginal(
                    'filePath_IPAddressFile', 'Upload IP Address File');

            $('#div_IPRange').attr('style', 'display:none');
            $('#div_IPFileUpload').attr('style', 'display:none;');
            $('#' + clickId).attr('style', 'display:block;');
        },
        // function used to upload a file containing nodes IP to be added to cluster
        hadoopIPAddressFileUpload : function() {
            var uploadUrl = baseUrl + '/uploadFile';
            $('#fileBrowse_IPAddressFile').click();
            $('#fileBrowse_IPAddressFile').change(
                    function() {
                        $('#filePath_IPAddressFile').val(
                                $('#fileBrowse_IPAddressFile').val());
                        com.impetus.ankush.commonMonitoring.uploadFile(uploadUrl,
                                "fileBrowse_IPAddressFile", "fileIPAddress");
                    });
        },
     // function used to display retreived nodes on the basis of filtering, whether All, Selected, Available, or error nodes is to be displayed
		toggleDatatable : function(status) {
			$('.notSelected').show();
			$('.notSelected').removeClass('notSelected');
			$('.selected').removeClass('selected');
			$('#addNodeCheckHead').removeAttr('disabled');
			if (status == 'Selected') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if ($('#hadoopAddNodeCheckBox-' + i).attr('checked')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('#addNodeCheckHead').attr('disabled', true);
				$('.notSelected').hide();
			} else if (status == 'Available') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if (!$('#addNode-' + i).hasClass('error')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('.notSelected').hide();
			} else if (status == 'Error') {
				for ( var i = 0; i < nodeTableLength_AddNodes; i++) {
					if ($('#addNode-' + i).hasClass('error')) {
						$('#addNode-' + i).addClass('selected');
					} else
						$('#addNode-' + i).addClass('notSelected');
				}
				$('#addNodeCheckHead').removeAttr('checked');
				$('.notSelected').hide();
			}
		},
        // function used to upload a file
        uploadFile : function(uploadUrl, fileId, uploadType, callback, context) {
            jsonFileUploadString = null;
            $.ajaxFileUpload({
                url : uploadUrl,
                secureuri : false,
                fileElementId : fileId,
                dataType : 'text',
                accept : "application/png",
                success : function(result) {
                    if (callback)
                        callback(result, context);
                    var htmlObject = $(result);
                    var resultJSON = new Object();
                    eval("resultJSON  = " + htmlObject.text());
                    if (uploadType == "fileIPAddress") {
                        fileIPAddress_ServerPath = resultJSON.output;
                    }
                    else if(uploadType == "fileRack") {
                        fileRack_ServerPath = resultJSON.output;
                    }
                },
                error : function() {
                    alert('Unable to upload the file');
                }
            });
        },
        //Remove dialogue on parameterPage
        removePageDialogParameter : function(){
            $('#deleteParameterDialogStorm').remove();
            $('#div_addParameter').remove();
        },
        // this function will start auto refresh of monitoring page will be called on jsp load
        monitoringPageAutorefresh : function(){
            /*is_autorefreshTiles_monitoringPage = setInterval("com.impetus.ankush.commonMonitoring.createCommonTiles("+com.impetus.ankush.commonMonitoring.clusterId+");",30000);
            is_autoRefreshHeatMap_monitoringPage = setInterval("com.impetus.ankush.commonMonitoring.commonHeatMaps("+com.impetus.ankush.commonMonitoring.clusterId+");",30000);
            is_autoRefreshSparkLine_monitoringPage = setInterval("get_sparkline_json();",15000);*/
        },
        //this function will remove auto refresh of monitoring page will be called on slider child out
        removeMonitoringPageAutoRefresh : function(){
            /*is_autorefreshTiles_monitoringPage = window.clearInterval(is_autorefreshTiles_monitoringPage);
            is_autoRefreshHeatMap_monitoringPage = window.clearInterval(is_autoRefreshHeatMap_monitoringPage);
            is_autoRefreshSparkLine_monitoringPage = window.clearInterval(is_autoRefreshSparkLine_monitoringPage);
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Kafka')
                com.impetus.ankush.kafkaMonitoring.removeKafkaMonitoringPageAutorefresh();
            if(com.impetus.ankush.commonMonitoring.clusterTechnology == 'Storm')
                com.impetus.ankush.stormMonitoring.removeStormMonitoringPageAutorefresh();*/
        },
        //this function will do the task necessary on slider child out
        removeChildPreviousMonitoringPageLoad : function(){
            $("#invalidAddNodeOperation").remove();
            //com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
        },
       
       
};

//****************************************Spark Line***************************************//
var buffer = [];
function displayGraphExample(id, width, height, interpolation, animate,
        updateDelay, transitionDelay, data, normal, json_index, max_val, old_max) {
    // assigning the max value array.
    max_value[json_index] = max_val;
    // Clean the container
    d3.select(id).html('');
   
    // create an SVG element inside the #graph div that fills 100% of the div
    var graph = d3.select(id).append("svg:svg").attr("width", "100%").attr(
            "height", "100%");
   
    // creating rectangle in svg.
    graph.append("svg:rect").attr("width", '100%').attr("height", '100%').attr("y", '0%').style('fill', '#ebebeb');
           
    // Inverting the values
    if(old_max) {
        for ( var i = 0; i < data.length; i++) {
            data[i] = max_val - (old_max - data[i]);
        }
    } else {
        for ( var i = 0; i < data.length; i++) {
            data[i] = max_val - data[i];
        }
    }
   
    // X scale will fit values from 0-10 within pixels 0-100
    var x = d3.scale.linear().domain([ 0, data.length-1 ]).range([ -5, width ]);
    // starting point is -5 so the first value doesn't show and slides off the edge as part of the transition Y scale will fit values from 0-10 within pixels 0-100
    var y = d3.scale.linear().domain([ 0, max_val + (max_val/10) ]).range([ 0, height ]);

    // create a line object that represents the SVN line we're creating
    var line = d3.svg.line()
    // assign the X function to plot our line as we wish
    .x(function(d, i) {
        // return the X coordinate where we want to plot this datapoint
        return x(i);
    }).y(function(d) {
        // return the Y coordinate where we want to plot this datapoint
        return y(d);
    }).interpolate(interpolation);

    // display the line by appending an svg:path element with the data line we
    // created above
    graph.append("svg:path").attr("d", line(data)).attr("id", 'path').attr(
            "class", "path-spark");

    // appending the circle to point the current value.
    graph.append("svg:circle").attr("id", 'circle').attr("cx",
            width - 2).attr("cy", 4).attr("r", 2).attr("fill", 'red');

    // function to redraw the graph.
    function redrawWithAnimation() {

        // update with animation
        graph.selectAll("path").data([ data ]) // set the new data
        .attr("transform", "translate(" + x(1) + ")") // set the transform to
                                                        // the right by x(1)
                                                        // pixels (6 for the
                                                        // scale we've set) to
                                                        // hide the new value
        .attr("d", line) // apply the new data values ... but the new value
                            // is hidden at this point off the right of the
                            // canvas
        .transition() // start a transition to bring the new value into view
        .ease("linear").duration(transitionDelay) // for this demo we want a
                                                    // continual slide so set
                                                    // this to the same as the
                                                    // setInterval amount below
        .attr("transform", "translate(" + x(0) + ")"); // animate a slide to
                                                        // the left back to x(0)
                                                        // pixels to reveal the
                                                        // new value
    }

    // function to move circle on refresh call.
    function move_circle() {
        var tmp = graph.selectAll("path").attr('d').split('L');
        tmp = tmp[tmp.length - 1];
        tmp = tmp.split(',');
        tmp = tmp[tmp.length - 1];

        graph.selectAll("circle").data([ data ])
        // .transition()
        // .ease("basis")
        // .duration(80)
        .attr("cy", tmp);

        // setting current value in current value division.
        $(id).parent().parent().find('.sl_current_val').html(
                Math.round(max_val-(data[data.length - 1]))
        );
    }

    // function to redraw graph without animation.
    function redrawWithoutAnimation() {
        // static update without animation
        graph.selectAll("path").data([ data ]) // set the new data
        .attr("d", line); // apply the new data values
    }
   
    // calling move circle function.
    move_circle();
   
    // setting the auto refresh interval function.
    var intId = setInterval(function() {
        if (buffer[json_index].length) {
            // shifting the data.
            data.shift();
            // getting the new value from buffer.
            var nw_val = buffer[json_index].shift();
            // putting new valusing in data.
            data.push(nw_val);
            // getting the new max value.
            var new_max_val = max_value[json_index];
            // if new max value is greater than old max value.
            if(new_max_val > max_val)
            {
                // clearing the old running interval for the graph.
                clearInterval(intervalId[json_index]);
                // redrawing  the graph.
                displayGraphExample(id, width, height, interpolation, animate,
                        updateDelay, transitionDelay, data, normal, json_index, new_max_val, max_val);
                return 0;
            }
            else
            {
                // if animate
                if (animate) {
                    // redrawing with animation.
                    redrawWithAnimation();
                } else {
                    // redrawing without animation.
                    redrawWithoutAnimation();
                }
                // moving red circle.
                move_circle();
            }
        }
    }, updateDelay);
   
    // assigning the interval value in interval id array.
    intervalId[json_index] = intId;
}

var intervalId = [];
function generate_sparkline(json_data) {
    if(intervalId.length)
    {
        for(var intNo=0; intNo<intervalId.length; intNo++)
        {
            clearInterval(intervalId[intNo]);
        }
       
        intervalId = [];
    }
   
    if((json_data == undefined) || (json_data == null))
        return ;
    // Clear sparkline
    d3.select('#sparkline_container').html('');

    // Start generating HTML
    var sl_table = d3.select('#sparkline_container').append('table').attr("id",
            "spark_line").attr("cellpadding", "0").attr("cellspacing", "0");
    var sl_table_head = sl_table.append('tr');
    var sl_table_body = sl_table.append('tbody');
    var sl_table_row;

    sl_table_head.append('th').attr("align", "left").attr("valign", "top").attr("colspan", "2").append('div').attr("class", "sparkline_tab_heading").html(
            'Cluster Utilization<span class="clusterpart_line">|</span> <span><a onclick="com.impetus.ankush.commonMonitoring.utilizationTrend();" href="#">View All Metrics</a></span>');
   
    sl_table_head.append('th').attr("width", "90").attr("align", "center").attr("valign", "top").attr("colspan", "2").append('span').attr("class", "sparkline_tab_heading")
            .html("Current");
   
    for ( var i = 0; i < json_data.length; i++) {
        buffer[i] = [];
        max_value[i] = json_data[i].maxValue;
        sl_table_row = sl_table_body.append("tr");

        sl_table_row.append("td").attr("width", "75").html('<span class="sparkline_labels">'+json_data[i].label+'</span>');
       
        sl_table_row.append("td").attr("width", "330").append("div").attr('id',
                'sparkline_' + json_data[i].id).attr('class', 'sparkline_contain_div');
       
        sl_table_row.append("td").attr("align", "right").attr("width", "40").append('span').attr("class",
                "sl_current_val sparkline_labels");
       
        sl_table_row.append("td").html('<span class="sparkline_labels">'+json_data[i].unit+'</span>');
       
        displayGraphExample("#sparkline_" + json_data[i].id, 330, 30, "basis",
                true, json_data[i].delay, json_data[i].delay - 30,
                json_data[i].values, json_data[i].normalValue, i, json_data[i].maxValue, false);
    }
}

var jd;
// array to store max value.
var max_value = [];
// function to get sparkline json for update.
function get_sparkline_json()
{
    // url to update the sparkline graph.
    var sl_url = baseUrl+'/monitor/'+com.impetus.ankush.commonMonitoring.clusterId+'/sparkline?startTime=lastHour&update=true';
    // calling sparkline update rest call.
    com.impetus.ankush.placeAjaxCall(sl_url, "GET", true, null, function(new_sparkline_json){
        // new sparkline data.
        new_sparkline_json = new_sparkline_json.output.sparklineData;
        // Code to refresh buffer
        if(new_sparkline_json)
        {
            for ( var i = 0; i < new_sparkline_json.length; i++) {
                for ( var j = 0; j < new_sparkline_json[i].values.length; j++) {
                    //new_sparkline_json[i].values[j] = new_sparkline_json[i].values[j]/10;
                    new_sparkline_json[i].values[j] = max_value[i] - new_sparkline_json[i].values[j];
                    // pussing the values in buffer.
                    buffer[i].push(new_sparkline_json[i].values[j]);
                }
                // assining the new max value to max values array.
                max_value[i] = new_sparkline_json[i].maxValue;
            }
        }
    });
   
}
//***********************************Spark line End********************************************//

// ***********************************Spark line
// End********************************************//
