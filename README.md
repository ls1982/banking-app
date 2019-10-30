## Simple bank REST API

A "sandbox" RESTful API that provides the following functionality:
- create a bank account
- deposit to an account
- withdraw money from account
- transfer money between accounts  


Implementation details:
- Data model was designed with minimum set of fields
- No authentication, security, API versioning e.t.c.
- Concurrent reliability is providing due to underlying database transaction's isolation
- Server is starting on port 8080

## Getting started
To build: 

    gradle clean build
    
To run tests: 

    gradle clean test

To run the application:

    java -jar ./build/libs/banking-app-0.1.jar

## The API description
#### Create an account

    POST /accounts
    
#### Get account's details

    GET /accounts/:accountNumber

#### Deposit an account with money

    PUT /accounts/:accountNumber/deposit
    
    {
        "amount": 10
    }
    
#### Withdraw money from account  
    
    PUT /accounts/:accountNumber/withdraw
    
    {
        "amount": 10
    }
 
#### Transfer money between accounts   
    
    PUT /accounts/:accountNumber/transfer
    
    {
        "accountTo": 2,
        "amount": 10
    }
    
#### Get full operations' history   
    
    GET /accounts/:accountNumber/history