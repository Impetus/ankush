#ankush

## Introduction

A big data cluster management tool that creates and manages clusters of different technologies. It provides visual, graphical, and email notifications regarding the health of a Cluster that allow Cluster Administrators to take informed actions.

## Supported OS

* Ubuntu Server 64 bit (12.04)
* CentOS Server 64 bit (6.2, 6.3, 6.4)

## Requirements

The software prerequisites are as follow:
* Java >= 1.6
* nmap
* SSh client (ssh)
* Maven (mvn)


## Getting Started

### How to install

The guide will help you to setup and start the server.

Check out source code from git repo.
````
git clone https://github.com/impetus-opensource/ankush.git
````
Or you can download source from https://github.com/impetus-opensource/ankush/archive/master.zip and extract it.

Change directory to the repository folder.

Build package
````
mvn package
````
You will get package at target/ankush-x.y.z.tar.gz. Copy the package to the location where you want to deploy.

Extract package
````
tar -xvf ankush-x.y.z.tar.gz
````

### How to use

Start Ankush
````
bin/start-ankush.sh
````
You can access server with credentials admin:admin at ````http://<your IP>:8080/ankush````

Stop Ankush
````
bin/stop-ankush.sh
````
