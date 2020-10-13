INSERT INTO RMT.MT_CLIENT(CLIENT_ID, CLIENT_FIRST_NAME, CLIENT_LAST_NAME, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (1, 'Rajaram', 'Sivanesan', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_CLIENT(CLIENT_ID, CLIENT_FIRST_NAME, CLIENT_LAST_NAME, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (2, 'Paul', 'Anderson', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_CLIENT(CLIENT_ID, CLIENT_FIRST_NAME, CLIENT_LAST_NAME, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (3, 'Revolut', 'Revolut', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_CLIENT(CLIENT_ID, CLIENT_FIRST_NAME, CLIENT_LAST_NAME, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (4, 'Dhakshid', 'Rajaram', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');


INSERT INTO RMT.MT_ACCOUNT(ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (1000, 1, 100, 'USD', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_ACCOUNT(ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (2000, 2, 400, 'USD', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_ACCOUNT(ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (3000, 4, 300, 'USD', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_ACCOUNT(ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (4000, 4, 500, 'USD', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_ACCOUNT(ACCOUNT_ID, CLIENT_ID, ACCOUNT_BALANCE, ACCOUNT_CURRENCY_CODE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES (5000, 4, 600, 'USD', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');


INSERT INTO RMT.MT_CURRENCY (CURRENCY_CODE, CURRENCY_NAME, CURRENCY_COUNTRY, CURRENCY_ACTIVE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES ('USD', 'UNITED STATES DOLLAR', 'US', 'Y', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_CURRENCY (CURRENCY_CODE, CURRENCY_NAME, CURRENCY_COUNTRY, CURRENCY_ACTIVE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES ('EUR', 'EURO', 'EUROPEAN UNION', 'Y', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

INSERT INTO RMT.MT_CURRENCY (CURRENCY_CODE, CURRENCY_NAME, CURRENCY_COUNTRY, CURRENCY_ACTIVE, CREATED_DATE, CREATED_USER, UPDATED_DATE, UPDATED_USER) 
VALUES ('GBP', 'GREAT BRITISH POUND', 'UK', 'Y', CURRENT_TIMESTAMP, 'ADMIN', CURRENT_TIMESTAMP, 'ADMIN');

COMMIT;