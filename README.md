# banker
=====================
Create a Teller application
=====================
=======
Personas
=======
-Maker
-Checker
- Authorizer
Integrate with -> IAM
===================
UI (React) (CRUD)
===================
- Create Customer 
- Create Accounts 
- One Customer may have more than 1 account
 
App should be able to do below operations on UI
- Deposits - API
- Withdrawals - API
 
==========
Business rule
==========
If any customer wants to withdraw over $1000, Authorizer has to approve it.
Create dashboards ( for transaction type by date by Customer)
 
===========
Search Screen:
===========
Input account number, it should display transactions for that account, if input is customer id it should display for all transaction for his accounts
====
APIs
====
1. Input Customer ID; able to retrieve account balances (opening balance, closing balances) for all accounts for that customer
2. Input customer ID, Account no. able to retrieve account balances (opening balance, closing balances) for that account
3. Input Account Number, return the Customer Id, Account Id, opening balance, closing balance, Interest posted
 
Integrate with any open source any API gateway =========
Batch jobs
=========
Create batch job to calculate interest; - API
Create a batch job e-statement for all customers (PDF)
============
XML File loading
============
XML file is being sent from external application, pick up file and load into APP
 
===============
XML File Generation
===============
Generate XML file for all the Credit/Debit entry
capture the fields like: Cust_id, Account_no,Transaction_date, Transaction_amt, Interest_amount
add the total number of transactions and add signature
 
 
 

Prepare data model,
 
Prepare Technical Design document for the above, include user journey flow with respect to login/logout in context to open source IAM
