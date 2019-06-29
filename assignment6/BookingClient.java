 /* MULTITHREADING <MyClass.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Syed Naqvi
 * smn756
 * 16225
 * Slip days used: 1
 * Spring 2019
 */
package assignment6;

import java.util.Map;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Thread;

public class BookingClient {

	//fields for booking client
	private Map<String, Integer> bxInfo;
	private Theater mainTheater;
	
    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater theater) {
        // Set to specifications
    	bxInfo = office;
    	mainTheater = theater;
    }

    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
    	//Create list for Threads
    	List<Thread> threads = new ArrayList<>();
    	
    	//Create a thread for each box office
    	for(String boxOffice : bxInfo.keySet()) {
    		
    		//Will tell each thread(boxoffice) what to do when it runs(runnnable)
    		Thread bx = new Thread(new Runnable() {
				@Override
				public void run() {
					//Get box office name
					String s = Thread.currentThread().getName();
					
					//For all customers in line at this box office
					for(int i =0; i<bxInfo.get(s);i++) {	
						//Buy them a ticket for next best seat, if it was last seat
						//Print message to indicate sold out and stop all threads.
						if(mainTheater.processSale(s)) {
							System.out.println("Sorry, we are sold out!");
							//Interrupt all are running threads
							for (Thread t : Thread.getAllStackTraces().keySet()) 
							{  if (t.getState()==Thread.State.RUNNABLE) 
							     t.interrupt(); 
							} 
						}
					}
					
				}
			},boxOffice);
    		
    		//add thread to list
    		threads.add(bx);
    	}
     
    	//Start all threads and return list of threads
    	for(Thread t : threads) {
    		t.start();
    	}
    	
    	return threads;
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
    	
    	//Initialize box office data
    	Map<String, Integer> boxOffices = new HashMap<String, Integer>();
    	boxOffices.put("BX1", 3);
    	boxOffices.put("BX2", 4);
    	boxOffices.put("BX3", 3);
    	boxOffices.put("BX4", 3);
    	boxOffices.put("BX5", 3);
    	
    	//initialize theater
    	Theater t = new Theater(3,5,"Ouija");
    	
    	//Initialize Booking Client for this theater and these boxOffices
    	BookingClient BC = new BookingClient(boxOffices,t);
    	
    	//Let box Offices sell tickets!
    	List<Thread> ls = BC.simulate();
    
   		
    }
}
