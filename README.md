## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Requirements](#requirements)
* [Configuration](#configuration)
    * [Logging](#logging)
    * [Connector](#connector)
* [Installation](#installation)

## General info
This repository contains everything you need to create 'Live Objects to FlexibleEngine Data Ingestion Service' connector. This project is intended for Live Objects users wishing to explore integration patterns with FlexibleEngine and for organizations already running business logic on FlexibleEngine planning to work on events from IoT devices sourced via Live Objects.

Main feature is: 
* **messages synchronization** - every message which will be send from device to Live Objects will appear in Data Ingestion Service

One connector can handle one Live Objects account and one Data Ingestion Service. 

It can be many instances of connector per Live Objects account.

The software is an open source toolbox which has to be integrated into an end to end solution. The ordering of messages is not guaranteed to be preserved.
Live Objects platform supports load balancing between multiple MQTT subscribers.

## Technologies
* Java 8
* Spring Boot 2.3.4.RELEASE

## Requirements
In order to run the connector you need to have: 
* **Live Objects account** 
* **Flexible Engine account with an Data Ingestion Service** 

## Configuration

### Connector
All configuration can be found in **application.yaml** file located in src/main/resources

```
1     lo-mqtt:
2       hostname: liveobjects.orange-business.com
3       username: application
4       api-key: YOUR_API_KEY
5       topics:
6         - MESSAGES_TOPIC_1
7         - MESSAGES_TOPIC_2
8       recovery-interval: 10000
9       completion-timeout: 20000
10      connection-timeout: 30000
11      qos: 1
12      keep-alive-interval-seconds: 30  
13  
14    flexible-engine:
15      dis:
16        endpoint: https://dis.eu-west-0.prod-cloud-ocb.orange-business.com
17        ask: YOUR_ASK_KEY
18        sk: YOUR_SK_KEY
19        projectId: YOUR_PROJECT_ID
20        region: YOUR_REGION
21        stream-name: YOUR_STREAM_NAME
22        message-batch-size: 10
23        message-sending-fixed-delay: 1000
24
25    management:
26      endpoints:
27        web:
28          exposure:
29            include: info, health, metrics, beans
```

#### hostname
REST API endpoint url, leave the value of **https://liveobjects.orange-business.com/api**

#### username
Live Objects mqtt username (should be set to **application**)

#### api-key
Live Objects API key with at least DEVICE\_R, DEVICE\_W and BUS_R roles

Login to Live Objects Web Portal an go to **Administration** -> **API keys** 

![Api Keys 1](./assets/api_key_1.png) 

Click **Add** button and fill fields. 

![Api Keys 2](./assets/api_key_2_.png)

To  validate  the  creation  of  the  key,  click  on  the **Create** button.  Your  key  is  generated  in  the form of an alphanumeric sequence and a QR code.

#### topics
Names of the FIFO queue for messages

#### recovery-interval
Controls the interval (in miliseconds) at which the mqtt client attempts to reconnect after a failure. It defaults to 10000ms (10 seconds)

#### completion-timeout
Set the completion timeout for mqtt operations in miliseconds

#### connection-timeout
This value, measured in miliseconds, defines the maximum time interval the client will wait for the network connection to the MQTT server to be established

#### qos
Message QoS 

#### keep-alive-interval-seconds
This value, measured in seconds, defines the maximum time interval between messages sent or received. It enables the client to detect if the server is no longer available, without having to wait for the TCP/IP timeout. The client will ensure that at least one message travels across the network within each keep alive period.  In the absence of a data-related message during the time period, the client sends a very small "ping" message, which the server will acknowledge. A value of 0 disables keepalive processing in the client.

#### endpoint
REST API endpoint url, leave the value of https://dis.eu-west-0.prod-cloud-ocb.orange-business.com

#### ask
AK/SK-based Authentication

#### sk
AK/SK-based Authentication

#### projectId
Your project id

#### region
Region

#### stream-name 
Your stream name

#### message-batch-size
Size of batch

#### message-sending-fixed-delay
How often messages will be sent to DIS

### Logging
Logging configuration can be found in **logback.xml** file located in src/main/resources. You can find more information about how to configure your logs [here](http://logback.qos.ch/manual/configuration.html)


### Installation

You can deploy this connector wherever you want (local server, cloud provider etc.)

#### Build

Build the JAR file using command:
```
mvn clean package
```