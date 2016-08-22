# Test Plan

**Author**: Team 30  
**Current Version**: V4

| *Version* | *Description*       |
| ----------|:-------------------:|
| *V4*      | *Deliverable 4*     |


## 1 Testing Strategy

### 1.1 Overall strategy


As a collaborative decision the team has agreed each test will be performed through out the process of this project. The testing will be ordered in a way that allows the team to build the code piece by piece. Testing each module of code will assure that each isolated unit runs correctly. There after the testing of two or more modules together will attest to the least amount of failures in the code. Lastly the entire system will be test. Respectively, the coordination of test will run as such, Unit, integration and system testing.

To perform these task the main designation proceeds to Amanda for she is one of the Developers/Testers for the team. Her job is to create the test for modules. To support the process will be Nicholas for he can advise and arrange integration of the testing modules. Finally, Gus will manage the system testing by collectively running all units of code to get the software to perform with out any bugs.


### 1.2 Test Selection


The selection of the test case will gradually form as the project forms though for now the unit testing will be a decision table testing technique derived from black box testing. This technique takes a table structured as such:

| *Input*               | *Function Pointer*      |
| ----------------------|:-----------------|
| "1"   				|generateId() 	   |
|                       | ...        	   |
| "43376"      			|chargeCreditCard()|
|                       | ...        	   |
| ".10"	 	            |applyDiscount()   |

Decision table technique takes the possible input (as like in the input column above) and give the out come.

Branch Coverage will be the second technique from white-box test that will be applied to the code. Here you will see test cases that will uncover errors in classes. This will reduce the total number of test cases due to the process of testing a set of modules/unit test; which is well defined as integration testing.

The final technique will demonstrate a path technique from white box testing. This style shows the possible paths of execution at least one time in the system. Because the team will know the source code this will allow there to be emphasizes on designing a test cases that will cover a 100% of the system testing.

### 1.3 Adequacy Criterion

Our goal for this project is to reach a completed set of obligations that satisfies the test suit. The end result will be that all test pass and that each test is satisfied by at least one test cases in the suit. In the testing process the coverage goal depends on the different cases in the code. Therefore it is possible to fall short of a 100% functional coverage but achieve 100% code coverage. To do this we will target our functional coverage at 90% by using a combination of activities such as: tracking the number of test running, maintaining a low bug rate, schedule testing, and check device under test (DUT) stability. Using functional coverage will help know how much of our code specification was covered.


### 1.4 Bug Tracking

The quickest way for our team to track bugs and enhancement request is to use JIRA software. This software is available in arms reach to our users allowing the team to plan out issues, track team's work, and release a better version of the app. On the software users can find common request to solve bugs and quick plugins to solve our personal project problems.

### 1.5 Technology

The testing technology intended to be used for this project will be Android Studio's Andorid JUnit Runner framework. This structure is pre built into Android Studio and is worker-friendly when already established in the IDE , making it efficient for the team to use at a click of a button. Therefore it is no need to download other version of the system or other testing technology. This framework will test the performance interaction between the user and the app/system. This testing tool allows you to perform the system while in the background the Runner test user input. The Runner then takes the source path to the code document, run and compile the code, and finally run the test. These test are assured to run based on the set up and support by the JUnit 3 or JUnit 4 style.

## 2 Test Cases

Included in this section is a description of how the team used both automated and manual written test. Choosing each kind of test benefit the app to cover all possible features of the app.

Automated Written Test:
In the package "edu.gatech.seclass.gobowl" LoginActivityTest.java and ManagerActivityTest.java are both automated test files using a white box testing technique that gathers sample user interaction. The backend of the app test the database; for example addCustomerTest test if the customer's information(i.e, first name, last name, email) entered by the manager is the same instance in the database. 

Manual Written Test:
As for the remaining java classes in the "edu.gatech.seclass.gobowl" (i.e, CustomerActivityTest.java, SplitBillTest.java) these are written using Branch Coverage. The team test in detail, specifically user interaction and backend accessibility. For instance testAddNewLane_cancel which verifies if the user presses cancel button that the CustomerActivity.java intent does Not start. Other examples of manual test:
		
		- Request lane for one or more players
		- Pressing the checkout button
		- Success of saving the score to the database
		- Starting a new activity to SplitBill.java

Directions on how to run the test:
To run the test in GoBowl app got to the directory "edu.gatech.seclass.gobowl" in src/androidTest/java, there right click on the folder. Then click "Run Test in edu.gatech.seclass.gobowl". The user will be prompted to run the emulator, click Ok and wait for the test to run.

The following table lists the set of test that will be performed to assess the quality of the system.

|Class/Function|Expected|Actual|Pass/Fail|Purpose|Steps To Perform The Test|Additional Information|
|---|---|---|---|---|---|---|
|Manager/addCustomer()|1|1|pass|Add a new customer|Read in manager input for customer number or system generate a random number| - |
|Manager/addCustomer()|2|1|fail|Add a new customer|Ask manager to enter another number for the new customer or run the random generator again| - |
|Customer/laneRequest()|8|8|Pass|Assign a lane|Customer must check into the system to start a game| - |
|Customer/laneRequest()|8|Not Available|Fail|Assign a lane|The system must generate another lane because the chosen in use| - |
|CardPrinter/printCard()|Print Running!|Print Running!|Pass|Print customer card|Manager will press print and new card will print| - |
|Lane/landNumber|Integer|Integer|Pass|Lane provided is an integer for a reference to customer to start a game at that lane.|System will generate a lane when the customer finishes registering/signing in and setting up a game|  - |
|Score/score|Integer|Integer|Pass|Allows customer to save score for future reference|Customer will be prompt to enter these scores in when saveScore() is executed| - |
|Bill/splitBillNumberOfWays|Integer|Interger|Pass|Customer must enter a number|System will prompt the user to enter a integer value| - |
|Bill/splitBillNumberOfWays|Integer|String/Boolean|Fail|To assure the customer is entering a valid number|System will redirect the user to enter a valid number| - |
