Manager Class
	1. the manager may invoke the resetSpentMoney operation to reset the amount spent for all customers to 0. This is to be done at the beginning of the year. 

Customer Class
	1. The Customer's bowling card is not explicitly defined as its own class, but rather within the Customer class since a customer has a distinct card attached to it. 
	2. Customer class can call requestLane and checkout to get the Lane(Manager) class. 

Lane Class
	1. The Lane class will hold all the appropiate information for a given lane when requestLane is called like players and laneNumber. Since this information is only relevent to the request call it deserves its own class. 
		1.  requestLane class calls on the scanner utility in the askToScanCard() method. 
	2. the Lane class also holds the appropiate information for the checkout method.
		1. the checkout method also calls on scoresaver which will save the information and update the customers scores.
        
	3. Lane has the getDate function to find the price of the lane



