Software Design Document
==== 
Statement of Goals
----
This UML class diagram is a Bowling Alley Management System for the company GoBowl. The specifications or assumptions will describe functionality of the design between classes. The assumptions do not specify the attributes or user interaction with the system, but additional information about the design. 

Assumptions
----
- Customer makes payment through the Checkout System. This is shown by the association relationship between the Customer and CheckoutSystem class. 
- The Checkout System is connected to the external library, but will not have an additional association between it and the CreditCardScanner class. This relationship is assumed necessary because the External Library enables the system to connect to CheckoutSystem class. 
- processTransaction() in CheckoutSystem class is assumed to be  used to get the total transactions of the current year. The number of transactions during the current year will determine if the customer has reached VIP status or will reach VIP status during the current transaction. If this method returns greater than or equal to 500 dollars, then the customer will receive an email confirming VIP status for the coming year. 
- There will need to be a memberStatus attribute to distinguish between general and VIP customers; this information is also important when giving rewards during a transaction. 
- Transaction class is created separate from CheckoutSystem class to store customer transaction each visit and also display the amount per purchase.
- It is assumed that a customer can have zero to many discounts and the checkout service can apply zero to many discounts. Therefor there isn't a limit on the number of discounts a customer can apply to the total purchase.