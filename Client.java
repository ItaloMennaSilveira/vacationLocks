package vacationteste2;

import java.util.concurrent.locks.*;

public class Client extends Thread {
	int id;
	Manager managerPtr;
	Random randomPtr;
	int numOperation;
	int numQueryPerTransaction;
	int queryRange;
	int percentUser;
    Lock lock;

	public Client() {}

public Client(int id,
	Manager managerPtr, 
	int numOperation,
	int numQueryPerTransaction, 
	int queryRange,
	int percentUser,
        Lock lock) {
	this.randomPtr = new Random();
	this.randomPtr.random_alloc();
	this.id=id;
	this.managerPtr = managerPtr;
	randomPtr.random_seed(id);
	this.numOperation = numOperation;
	this.numQueryPerTransaction = numQueryPerTransaction;
	this.queryRange = queryRange;
	this.percentUser = percentUser;
        this.lock = lock;
}

public int selectAction (int r, int percentUser) {
	if (r < percentUser) {
		return DefinesLock.ACTION_MAKE_RESERVATION;
	} else if ((r & 1)==1) {
		return DefinesLock.ACTION_DELETE_CUSTOMER;
	} else {
		return DefinesLock.ACTION_UPDATE_TABLES;
	}
}

public void run() {
	int myId = id;
        
	Manager managerPtr = this.managerPtr;
	Random randomPtr  = this.randomPtr;

	int numOperation           = this.numOperation;
	int numQueryPerTransaction = this.numQueryPerTransaction;//NUMBER
	int queryRange             = this.queryRange;
	int percentUser            = this.percentUser;

	int types[]  = new int[numQueryPerTransaction];
	int ids[]    = new int[numQueryPerTransaction];
	int ops[]    = new int[numQueryPerTransaction];
	int prices[] = new int[numQueryPerTransaction];
        

        for (int i = 0; i < numOperation; i++) {
		int r = randomPtr.posrandom_generate() % 100;
		int action = selectAction(r, percentUser);


		if(action==DefinesLock.ACTION_MAKE_RESERVATION) 
		{
			int maxPrices[]=new int[DefinesLock.NUM_RESERVATION_TYPE];
			int maxIds[]=new int[DefinesLock.NUM_RESERVATION_TYPE];
			maxPrices[0]=-1;
			maxPrices[1]=-1;
			maxPrices[2]=-1;
			maxIds[0]=-1;
			maxIds[1]=-1;
			maxIds[2]=-1;
			int n;

			int numQuery = randomPtr.posrandom_generate() % numQueryPerTransaction + 1;

			int customerId = randomPtr.posrandom_generate() % queryRange + 1;

			for (n = 0; n < numQuery; n++) 
			{
				types[n] = randomPtr.random_generate() % DefinesLock.NUM_RESERVATION_TYPE;

				ids[n] = (randomPtr.random_generate() % queryRange) + 1;

			}
			boolean isFound = false;

                        lock.lock();
			try {

				for (n = 0; n < numQuery; n++) 
				{
					int t = types[n];
					int id = ids[n];
					int price = -1;

					if (t==DefinesLock.RESERVATION_CAR) 
					{
						if (managerPtr.manager_queryCar(id) >= 0) 
						{
							price = managerPtr.manager_queryCarPrice(id);
						}
					} 
					else if (t==DefinesLock.RESERVATION_FLIGHT) 
					{
						if (managerPtr.manager_queryFlight(id) >= 0) 
						{
							price = managerPtr.manager_queryFlightPrice(id);
						}
					} 
					else if (t==DefinesLock.RESERVATION_ROOM) 
					{
						if (managerPtr.manager_queryRoom(id) >= 0) 
						{
							price = managerPtr.manager_queryRoomPrice(id);
						}
					}
					if (price > maxPrices[t]) 
					{
						maxPrices[t] = price;
						maxIds[t] = id;
						isFound = true;
					}
				}
				if (isFound) 
				{
					managerPtr.manager_addCustomer(customerId);
				}
				if (maxIds[DefinesLock.RESERVATION_CAR] > 0) 
				{
					managerPtr.manager_reserveCar(customerId, maxIds[DefinesLock.RESERVATION_CAR]);
                                       
				}
				if (maxIds[DefinesLock.RESERVATION_FLIGHT] > 0) 
				{
					managerPtr.manager_reserveFlight(customerId, maxIds[DefinesLock.RESERVATION_FLIGHT]);
                                       
				}
				if (maxIds[DefinesLock.RESERVATION_ROOM] > 0) {
					managerPtr.manager_reserveRoom(customerId, maxIds[DefinesLock.RESERVATION_ROOM]);
                                       
				}
                              
			} finally {
                            lock.unlock();
                        }      


		} 

		else if (action==DefinesLock.ACTION_DELETE_CUSTOMER) 
		{
			int customerId = randomPtr.posrandom_generate() % queryRange + 1;

                        lock.lock();
			try {
                                
				int bill = managerPtr.manager_queryCustomerBill(customerId);
				if (bill >= 0) 
				{
					managerPtr.manager_deleteCustomer(customerId);
                                      
				}
                              
			} finally {
                            lock.unlock();
                        }
		} 
		else if (action==DefinesLock.ACTION_UPDATE_TABLES) 
		{
			int numUpdate = randomPtr.posrandom_generate() % numQueryPerTransaction + 1;
			int n;
			for (n = 0; n < numUpdate; n++) 
			{
				types[n] = randomPtr.posrandom_generate() % DefinesLock.NUM_RESERVATION_TYPE;
				ids[n] = (randomPtr.posrandom_generate() % queryRange) + 1;
				ops[n] = randomPtr.posrandom_generate() % 2;
                              
				if (ops[n]==1) 
				{
					prices[n] = ((randomPtr.posrandom_generate() % 5) * 10) + 50;
                                       
				}
			}

                        lock.lock();
			try {
                             
                                for (n = 0; n < numUpdate; n++) 
				{
					int t = types[n];
					int id = ids[n];
					int doAdd = ops[n];
					if (doAdd==1) 
					{
						int newPrice = prices[n];
						if (t==DefinesLock.RESERVATION_CAR) 
						{
							managerPtr.manager_addCar(id, 100, newPrice);
						} 
						else if (t==DefinesLock.RESERVATION_FLIGHT) 
						{
							managerPtr.manager_addFlight(id, 100, newPrice);
						} 
						else if (t==DefinesLock.RESERVATION_ROOM) 
						{
							managerPtr.manager_addRoom(id, 100, newPrice);
						}
					} 
					else 
					{ /* do delete */
						if (t==DefinesLock.RESERVATION_CAR) 
						{
							managerPtr.manager_deleteCar(id, 100);
						} 
						else if (t==DefinesLock.RESERVATION_FLIGHT) 
						{
							managerPtr.manager_deleteFlight(id);
						} 
						else if (t==DefinesLock.RESERVATION_ROOM) 
						{
							managerPtr.manager_deleteRoom(id, 100);
						}
					}
				}
			} finally {
                            lock.unlock();
                        }

		}
	}
}
}





