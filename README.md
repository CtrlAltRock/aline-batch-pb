# Aline Financial: Enrichment and Analysis Batch Service

## Description

In today's big data climate, parsing large amounts of data accurately and efficiently is how businesses make data driven decisions. In this Spring Batch program I take millions of lines of de-identified transaction data, created by an existing codebase, and enrich and analyze it into meaningful data. 

## Table of Contents
* [Installation](#installation)
* [Transactions](#transactions)
* [Usage](#usage)
* [Design](#design)
* [Credits](#credits)

## Installation
1. Clone the main repository into your desired system.
2. Modify the input and output paths in the application.properties
3. Install the dependencies and run the program.

## Transactions
* Your .csv file of transactions should include a header of the records columns
* This application is designed to use a transaction .csv with a header described below.
* User,Card,Year,Month,Day,Time,Amount,Use Chip,Merchant Name,Merchant City,Merchant State,Zip,MCC,Errors?,Is Fraud?

## Usage
* Once the new file transaction csv file has been added:
    * If you are running from your IDE, make sure maven can build the application, then you may run the main from aline-batch-pb/src/main/java/com/smoothstack/BatchMicroservice/BatchMicroserviceApplication.java
    * If you are running from a console, you can build and run using "mvn spring-boot:run"
    * If you'd rather run from a JAR you can use "mvn package clean" and run the JAR from the target subdirectory

## Design
![Batch Microservice Design](https://github.com/CtrlAltRock/aline-batch-pb/blob/dev/AlineBatchDesignV3.png)

## Credits

This app uses the following libraries and modules:

* Spring Batch
* MySQL
* JavaFaker
* LuhnAlgorithms
* Lombok
* Swagger
* Micrometer
* XStream