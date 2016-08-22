Assignment 5 – Nicholas Athanasiades, na11

1.  The *bowling alley manager* uses the system to (1) *add
    customers*, (2) print customer cards, and (3) *edit customer
    information*. For simplicity, we assume that the manager is the only
    employee working at the bowling alley.

> The requirement is satisfied by the **Manager** class which provides
> central control through creating and tracking of the **Customer**
> objects via the *customers* array. *customers* array is static as
> there is only one ( in the diagram it shows as underlined) The 3
> actions are covered by the respective methods i.e.:

1.  *addCustomer*: It instantiates a customer object for the customer
    and adds to the *customers* array. The method takes in name and
    e-mail as strings and returns true, if successful. The id of the
    customer is created and managed by the system.

2.  *editCustomerName* and *editCustomerEmail*: These methods together
    allow editing an existing customer object. They take in the object
    and a string which will replace the name or the e-mail of the
    customer object.

3.  *printCustomerCard*: It will initiate the printing of a card via the
    **CardPrinter** utility class. *printCustomerCard* takes in the
    customer object, however the *CardPrinter**.**printCustomerCard*
    takes in only the id. This is done for flexibility, in case in the
    future things change. This is the reason for the dependency of the
    **Manager** to the **CardPrinter** utility class.

> Note that the relationship between **Customer** and **Manager** class
> is a composition. That is because the **Manager** class creates the
> **Customer** objects. Every customer is connected to the **Manager**
> singleton but the **Manager** can have none or many customers.

1.  The *bowling alley customers* use the system to request lanes and
    checkout when done playing.

2.  A customer in the system is characterized by the following
    information:

    1.  Name

    2.  Email address

    3.  4-digit unique hexadecimal ID

> Requirements 2 and 3 are satisfied by the *Customer* class. The class
> has 3 attributes mentioned in requirement 3 and the 2 methods
> indicated in requirement 2 i.e. requestLane() and checkout().

1.  When a customer is added to the system using his/her name and email
    address, the system automatically generates an ID and prints a card
    for the customer. The card is printed using a special card printer
    that is accessed through an external library.

> The creation of the Customer id is handled by the constructor of the
> **Customer** class. It can be anything. We could even use a strategy
> pattern to manage multiple possible implementations, however, I do not
> think, this is important as it does not make any difference how the id
> is encoded. The id has to be 4 digits and in hex. That is all. The
> printing of the card is initiated through the constructor of the
> **Customer** class. Therefore there is a dependency to the
> **CardPrinter** utility class which is part of the card printer API.

1.  The customer card contains a [*QR
    code*](http://en.wikipedia.org/wiki/QR_code) that can be read using
    a videocam attached to the system and that encodes the customer’s
    unique ID.

> The **CardPrinter** utility class has the printCustomerCard method
> which takes the unique customer id and creates the QR code. That is
> part of the external API which we assume comes from the card printer
> manufacturer.

1.  The process to request a lane works as follows:

    1.  A customer launches the “Lane request” functionality.

    <!-- -->

    1.  The system scans the customer card.

    2.  The system asks the customer for the number of players.

    3.  After the customer enters the number of players, the system
        requests all players to scan their cards, one at a time (only
        registered customers can play).

    4.  The system assigns a lane to the players and shows the lane
        number on screen (assume that there is an unlimited number of
        lanes for now).

> This requirement described the operation of *Customer.requestLane().*
> We explain the implementation of each step below:

1.  So *Customer.requestLane()* is called of a *Customer* object
    representing a customer.

2.  Within the method *requestLane()* the scanCustomerCard() method is
    call from the **CameraReader** utility class which is part of the
    API of the Video camera. It returns the Customer hex id. The system
    compares the id to the one of the object and verifies that they are
    the same.

3.  The customer inputs the number of players. We have not modeled the
    interface classes and methods for the system as this would be too
    much detailed. They were not modeled in P3L2. However, a method is
    provided to capture the number of players
    *Customer.setNumPlayers()*.

    Before proceeding we need to explain the relationship between
    Customer, Player and Lane. The Customer is the contact person for
    the Lane. The Lane groups players. A customer is a player. We do not
    draw a distinction between player and customer as only registered
    customers can play.

    Then, the system instantiates an object of **Lane** class. It
    creates a lane by giving it a number and start time. The
    relationship between **Customer** and **Lane** is that
    of composition. The **Customer** class creates the **Lane**
    class object. There is no restriction on who many lanes we have. A
    customer can have many lanes. So it is a 1 to many relationship.
    Each customer can have no lanes or many. E.g. they have a group of
    30 people split over multiple lanes. Lanes should be able to exist
    independently of customers and that is why we should not have
    a composition. However, the unlimited number of lanes allows to work
    around the conflicts from having two customers on a lane by creating
    always a new one. In addition, we do not need to keep information
    associated with a given lane. So we can use a composition.
    Connecting a lane in the system with an actual physical lane is done
    by the manager and is out of scope for the system.

4.  Within *Customer.requestLane()* we loop as many times specified and
    request players to scan their cards.

5.  Each time the CameraReacer.scanCustomerCard() returns the id on
    the card. The Lane.addPlayer() method is called. Customer object is
    found or created if it does not exist and added to the players for
    that lane.

    The assignment of the lane happens both at the **Lane** and
    **Customer** classes and it means different things. The **Customer**
    assigned to the **Lane** is a player. The **Lane** assigned to the
    **Customer** indicates that this is the main the contact person of
    the Lane. This choice keeps the number of classes and methods low.
    However, it would make debugging more difficult as the data
    introduces new meaning to the situation.

<!-- -->

1.  When a group of players is done bowling, the checkout process works
    as follows:

    1.  A customer launches the “Checkout” functionality.

    2.  The system asks the customer for his/her lane number.

    3.  After the customer enters the lane number, the system asks
        whether any player wants to save his/her score. If the answer is
        yes, the system shows the list of players for that lane and
        allows the customer to select any of them and enter a score.
        When a score is entered, the system adds the score, together
        with the current date, to the list of scores for the
        selected player. This process continues until the customer
        indicates that he/she is done entering scores.

    4.  The system then asks how the players want to split the bill for
        the lane (from 1 to &lt;number of players&gt; ways).

    5.  Once the players enter a number, the system splits the bill
        evenly and asks to swipe as many credit cards as needed,
        charging the right amount on every card. (All payments are by
        credit card. No cash payments are allowed.)

        This requirement described the operation of
        *Customer.checkout().* We explain the implementation of each
        step below:

    <!-- -->

    1.  So *Customer.checkout()* is called of a *Customer* object
        representing a customer.

    2.  Within the method *checkout()* the lane number is requested. The
        **Customer** class can have many lanes, hence, the correct one
        has to be identified.

    3.  To present the list of players the Lanes\[laneNum\].getPlayers()
        is called. For the specific lane, the **Lane** class provides
        the customers who are players. When a player (i.e. customer)
        decides to store their score, an object of the **Score** class
        is created. The **Score** class object is a pair of date and the
        actual score. That object is then added to an array of Score
        objects for the customer via the *addScore* method. The current
        date come the **TimeDate** utility class and it method
        *currentTimeDate()*.

    4.  If the bill to be split, the current date and time is obtained
        from **TimeDate** utility class. Then based on the schedule
        defined in requirement 8, the total number of players and time
        the players played the *getLaneCost* method returns the total
        cost for the lane discounted by the *Customer.discount*. The
        duration of the game is calculate as the difference between
        *Lane.Start* and *currentTimeDate()*. Then the cost is split
        using the *splitCost* which divides the total cost to the number
        of people specified.

    5.  In the *checkout*() method iterates through the players of the
        lanes and the *chargeCreditCard* method is called from the
        utility class **chargeCreditCard** to charge the amount from the
        *splitCost* method.

2.  The cost of a lane is:

    1.  \$20/hour from 9am to 5pm, Mon, Tue, Thu, Fri.

    2.  \$25/hour from 5pm to midnight, Mon, Tue, Thu, Fri.

    3.  \$30/hour all day on Sat and Sun.

    4.  \$10/hour all day on Wed.

> See requirement 7

1.  A credit card scanner attached to the system allows the system to
    read, when a card is swiped, (1) the cardholder’s name, (2) the
    card’s account number, (3) the card's expiration date, and (4) the
    card's security code.

> The utility class **chargeCreditCard** covers the requirement along
> with the methods:

-   *getCardholderName(): String*

-   *getCardAccountNumber(): Long*

-   *getExpirationDate(): Date*

-   *getCardSecurityCode(): Long*

1.  All the hardware devices attached to the system (card printer,
    videocam, and credit card scanner) can be accessed through existing
    external libraries. Similarly, external libraries enable the system
    to connect to a payment-processing service provider that can process
    credit card transactions. You must model each of these external
    libraries as a utility class (i.e., a class marked with the
    stereotype “&lt;&lt;utility&gt;&gt;”) with one or more
    suitable methods.

> The utility classes were modeled as:

-   card printer -&gt; CardPrinter

-   videocam -&gt; CameraReader

-   credit card scanner -&gt; CreditCardScanner

1.  Customers can use any credit card they want to perform a purchase
    (including someone else’s card, if that person is present). In other
    words, (1) the credit card information does not have to be
    associated with the customer in the system, and (2) the credit card
    information should not be stored after the transaction is completed.

> Neither the **Customer** class nor the **CreditCardScanner** store
> credit card or customer information respectively. The separation is
> maintained.

1.  Customers who spend \$500 or more in a single calendar year achieve
    *VIP* status, which entitles them to a 10% discount on the cost of
    the lanes for the following year. The change of status is effective
    from January 1 of the following year, and the customer has to be the
    one requesting the lane to get the discount. The discount applies to
    the whole cost of the lane.

> The **Customer** class has the following 3 attributes:

-   totalSpend: Float Tracks how much is spent in the year.

-   VIP: Boolean Characterizes the customer as VIP or not

-   discount: Float Defined the discount applicable for the customer.
    This is not necessary, however, it is a good practice since the
    discount rate may change in the future.

> During the checkout process the *checkout* method will update the cost
> the *totalSpend* with the payment amount. Once the \$500 threshold is
> reached the VIP flag is set to true and by the *setVIP()* method. Once
> the new year is reached, the *applyDiscount()* sets:

-   the discount to 10% if VIP is True and 0% if false,

-   the VIP to False

-   resets the totalSpend to 0.

14. Unlike the class diagram in the P3L2 lesson, where there was no main
    class represented (so to speak), your class diagram should contain
    two classes, called Manager and Customer, that contain one method
    for each of the main features available to these two types of users
    (e.g., addCustomer for the Manager and  requestLane for
    the Customer.) This is in addition to any other class you may need
    in your design.

> Both classes and methods can be found on the diagram
