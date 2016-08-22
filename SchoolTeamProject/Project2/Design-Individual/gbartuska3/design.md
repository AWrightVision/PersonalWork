# GoBowl

Software design for a management system for a bowling alley

### Person
  * Person is a inferface to both manager and customer and contains the shared name attribute 
  * As an interface, Person cannot have an instance of it directly, rather, the manager ans customer classes implement it

### VIP & Discount
  * vip, discount, and amountSpent attributes are interpreted for the current year
  * at year end, setVipStatus method is run which will update vip and discount attributes if amountSpent > 500 and subsequently zero out amountSpent

### Request
  * the cardinality is 1 to 1 since one customer makes the request for a lane and enters the number of players
  * the cardinality for the Checkout if many to 1 since many customers can all be playing on the same lane

### AlleyLane & Lane
  * AlleyLane is a generic concept around all physical lanes since users make request for a free lane, not a specific lane number
  * the actual relation, however, is between customer and lane since specific customer(s) play on a specific lane

### Score
  * the Score class functions similar to a data type and is stored as a list for each customer when requested as part of the checkout process

### Paying the Bill
  * The Bill class holds all the details related to charging a customer for checking out a lane
  * The Bill class is linked to CheckedOut association between customer and lane
  * Further, CheckedOut also has an association to the CreditCardScanner which knows about a CreditCard and talks to the PaymentProcessing utility.