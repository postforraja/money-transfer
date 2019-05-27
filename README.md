Designed and implemented a Java RESTful API for money transfers between accounts.

#Technologies used:
* Java
* Vertx
* H2 In-memory database
* Log4j
* Maven
* Junit
* Rest assured

#Data Model diagram:
Present under project root folder with file name 

MoneyTransferDataModel.jpg

#To Build the application:
Check out the project and run the below command

mvn clean package -Dmaven.test.skip=true


#To run the application:
Go to target folder and run the below command. The application starts on port 9090

java -jar money-transfer-1.0.0-SNAPSHOT-fat.jar


#To execute test cases:
Run the below command

mvn verify


#To know the available endpoints:
Go to 

http://localhost:9090/


#To create an account:
curl -H "Content-Type: application/json" -X POST "http://localhost:9090/api/createAccount" -d "{\"clientId\": \"3\", \"balance\": \"50000\",\"currencyCode\": \"GBP\", \"user\": \"Raja\"}"

Response: HTTP 201 Created
{
  "accountId" : 1,
  "clientId" : 3,
  "balance" : 50000,
  "currencyCode" : "GBP",
  "user" : "Raja"
}

#To deposit into an account:
curl -H "Content-Type: application/json" -X POST "http://localhost:9090/api/deposit" -d "{\"accountId\": \"1\",\"deltaAmount\": \"300\",\"currencyCode\": \"GBP\",\"user\": \"Raja\"}"

Response: HTTP 200 OK
{
  "accountId" : 1,
  "clientId" : 3,
  "balance" : 50300.0000,
  "currencyCode" : "GBP"
}

#To withdraw from an account:
curl -H "Content-Type: application/json" -X POST "http://localhost:9090/api/withdraw" -d "{\"accountId\": \"1\",\"deltaAmount\": \"100\",\"currencyCode\": \"GBP\",\"user\": \"Raja\"}"

Response: HTTP 200 OK
{
  "accountId" : 1,
  "clientId" : 3,
  "balance" : 50200.0000,
  "currencyCode" : "GBP"
}

#To get account by account id:
curl -H "Content-Type: application/json" -X GET "http://localhost:9090/api/account/1"

Response: HTTP 200 OK
{
  "accountId" : 1,
  "clientId" : 3,
  "balance" : 50200.0000,
  "currencyCode" : "GBP"
}

#To get all accounts:
curl -H "Content-Type: application/json" -X GET "http://localhost:9090/api/accounts"

Response: HTTP 200 OK
[ {
  "accountId" : 1,
  "clientId" : 3,
  "balance" : 50200.0000,
  "deltaAmount" : null,
  "currencyCode" : "GBP",
  "user" : null
}, {
  "accountId" : 1000,
  "clientId" : 1,
  "balance" : 100.0000,
  "deltaAmount" : null,
  "currencyCode" : "USD",
  "user" : null
}, {
  "accountId" : 2000,
  "clientId" : 2,
  "balance" : 400.0000,
  "deltaAmount" : null,
  "currencyCode" : "USD",
  "user" : null
}, {
  "accountId" : 3000,
  "clientId" : 4,
  "balance" : 300.0000,
  "deltaAmount" : null,
  "currencyCode" : "USD",
  "user" : null
}, {
  "accountId" : 4000,
  "clientId" : 4,
  "balance" : 500.0000,
  "deltaAmount" : null,
  "currencyCode" : "USD",
  "user" : null
}, {
  "accountId" : 5000,
  "clientId" : 4,
  "balance" : 600.0000,
  "deltaAmount" : null,
  "currencyCode" : "USD",
  "user" : null
} ]

#To delete an account by account id:
curl -H "Content-Type: application/json" -X DELETE "http://localhost:9090/api/account/1"

Response: HTTP 204

#To transfer fund between accounts:
curl -H "Content-Type: application/json" -X POST "http://localhost:9090/api/transferFund" -d "{\"sourceAccountId\": \"1000\",\"destinationAccountId\": \"2000\",\"amount\": \"10\",\"currencyCode\": \"USD\",\"comment\": \"test transfer\",   \"user\": \"Raja\"}"

Response: HTTP 200 OK

#To get all transactions:
curl -H "Content-Type: application/json" -X GET "http://localhost:9090/api/transactions"

Response: HTTP 200 OK
[ {
  "transactionId" : 1,
  "sourceAccountId" : 1000,
  "destinationAccountId" : 2000,
  "amount" : 10.0000,
  "currencyCode" : "USD",
  "comment" : "test transfer",
  "status" : "COMPLETED"
} ]