pg_core
=======
Payment gateway for VAS services from HTS, and Online Payment/Topup
1.	Description: Payment Gateway to allow:
-	Prepaid customers can use Scratch card to pay for VAS services from HTS (e.g. game online)– SM request
-	Postpaid customers make online payment and Prepaid customers buy Top-up online in Vietnamobile website (using their bank account) – Finance request
2.	Scope of work
2.1	Payment Gateway for VAS services from HTS
-	Payment gateway system provides authentication and call methods as the gateway via socket protocol
-	Add new function on Payment gateway to allows third vendor to validate scratch card by code. If the scratch card is Active, change state to used or recharge to prepaid subscriber.
2.2 Payment Gateway for Online Postpaid Payment/Prepaid Top up 
Postpaid customers make online payment and Prepaid customers buy Top up online in Vietnamobile website – DSP (using their bank account):
-	DSP: In DSP, customer clicks for online Postpaid payment/Prepaid Top up.
-	DSP: DSP will route to the payment gateway of banking vendor. 
-	Customer will fill in required information on interface of banking gateway vendor for verification, and get the notification from Banking Payment Gateway. 
-	Depending on the bank transaction is successful/fail, banking gateway vendor will send the notification to DSP and to customers.
-	DSP: If the notification is successful, DSP calls to Vietnamobile payment gateway with clarifying Postpaid Payment/Prepaid Top-up. DSP will provide report and reconciliation. 
-	Payment Gateway: perform Postpaid Payment/Prepaid Top up. 
-	Payment gateway provides the reports and reconciliation.
The detail is as below: 



