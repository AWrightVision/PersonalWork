# Extra Requirements

This document outlines supplementary requirements and those additional requirements that do not naturally file in the use-case model.

**Author**: Team 30  
**Current Version**: V1

| *Version* | *Description*       |
| ----------|:-------------------:|
| *V1*      | *Deliverable 2*     |


## Business Rules

  1. There is only one manager working in the bowling alley. There is no need to authenticate them.
  2. The card is printed using a special card printer.
  * There is an external library that is accessed to generate the QR Code.
  * The QR Code is encoding to the 4-digit Hex Customer Id.
  * The Customer is a 4-digit unique hexadecimal ID
  * The customer card contains a QR code.
  * The cost of a lane is:
      * $20/hour from 9am to 5pm, Mon, Tue, Thu, Fri.
      * $25/hour from 5pm to midnight, Mon, Tue, Thu, Fri.
      * $30/hour all day on Sat and Sun.
      * $10/hour all day on Wed.
  * The system should allow to split the bill for the lane from 1 to <number of players> ways.
  * Bills are split only evenly.
  * The system asks to swipe as many credit cards as needed based on the split.
  * The system charges the right amount on every card.
  * All payments are by credit card. No cash payments are allowed.
  * The system needs to appropriately interface with multiple external devices (card printer, videocam, and credit card scanner)
  * Customers can use any credit card they want to perform a purchase (including someone else’s card, if that person is present). In other words, (1) the credit card information does not have to be associated with the customer in the system, and (2) the credit card information should not be stored after the transaction is completed.
  * Customers who spend $500 or more in a single calendar year achieve VIP status, which entitles them to a 10% discount on the cost of the lanes for the following year. The change of status is effective from January 1 of the following year, and the customer has to be the one requesting the lane to get the discount. The discount applies to the whole cost of the lane.

## Non-Functional Requirements

  1. _Availability:_ A system's availability, or "uptime," is the amount of time that it is operational and
available for use.
    1. The system shall operate without error or interuption during the bolwing alley's business hours.
    2. The system shall be able to store information temporarily until internet connection can be re-established to avoid data loss.
  * _Robustness:_ A robust system is able to handle error conditions gracefully, without failure. This
  includes a tolerance of invalid data, software defects, and unexpected operating conditions.
    1. The system shall check user input for the e-mail and lane feilds.
    * If a printed card cannot be read or the hexadecimal code read does not correspond to a customer or player, the system should inform the user and not proceed further.
    * If the credit card cannot be charged either because there is not enough balance or it does not exist, the customer should be notified to use another card.
  * _Documentation:_ The bolwing alley manager shall receive Documentation about how to use the system. The customer should have in app help.
  * _Security:_ Confidentiality, availability and privacy of information should
      1. System should be secure given connection to payment processing system and bill discount
      * System should no store credit card information
  * _Scalability:_ Software that is scalable has the ability to handle a wide variety of system
  configuration sizes.
    1. The system shall be able to handle multiple simultanious Customers.
    * The system shall be able to handle unlimitated lanes.
    * The system shall be able to handle unlimitated players per lane.
  * _Usability:_ Ease of use requirements address the factors that constitute the capacity of the software
  to be understood, learned, and used by its intended users.
    1. The system shall use breadcrums.
    2. The system shall use outline possible user actions.
    3. The system shall not have navigation more than 2 screens deep.
  * _Performance:_ The performance constraints specify the timing characteristics of the software. Certain
  tasks or features are more time sensitive than others;
    1. The system shall respond in less than 1 second to user actions.
    2. The system shall have minimal memory footprint.
    3. The system shall install in less than a minute.
  * _Portability:_ Portability specifies the ease with which the software can be installed on all necessary
  platforms, and the platforms on which it is expected to run.
    1. The system shall be operational only on Android devices.
  * _Flexibility:_ Since we intend to increase or extend the functionality of the software after it
  is deployed.
    1. The system will use patterns.
    2. The system will assign responsibility to single components.
    3. The system will use fine grain components which it will orchistrate at higher level components.
