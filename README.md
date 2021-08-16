# Demo Project Fabrick
This demo project is written in Java with Spring Boot framework.
The application exposes three endpoints which invoke Fabrick REST APIs, regarding cash account opeations. 
* {accountId}/balance
* {accountId}/transactions?fromAccountingDate={from}&toAccountingDate={to}
* {accountId}/payments/money-transfers

example:
    http://localhost:8080/14537780/balance

### Installation
The project can be imported as a Maven project into an IDE of your choice, in order to compile it and execute test.


### Reference Documentation
For further reference, please consider the following Fabrick documentation:

* [Cash Account Management Documentation](https://docs.fabrick.com/platform/apis/gbs-banking-account-cash-v4.0)
* [Post Create Payments / Money Transfers](https://docs.fabrick.com/platform/apis/gbs-banking-payments-moneyTransfers-v4.0)

