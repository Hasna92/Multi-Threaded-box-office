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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Theater {
	
	
	//Fields for theater class
	private int rowsInTheater;
	private int seatsPerRow;
	private String show;
	private Queue<Seat> seatsInTheater;
	private List<Ticket> ticketsSold;
	private int customers;
	

    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms.  Use it in your Thread.sleep()

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Represents a seat in the theater
     * A1, A2, A3, ... B1, B2, B3 ...
     * 
     * Implements comparable to have a natural ordering to seats. 
     */
    static class Seat implements Comparable<Seat> {
        private int rowNum;
        private int seatNum;
        
        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
        
        //Added methods to compare seats to one another so that they may
        //be sorted, not necessary with queue but helps with other lists and 
        //re-usability
		@Override
		public int compareTo(Seat o) {
			//Method will return -1 if seat is "better" than param seat
			//0 if equal and 1 if worse than param seat
			int toReturn = 0;
			
			if(this.rowNum<o.getRowNum()) {
				toReturn = -1;
			}
			else if(this.rowNum==o.getRowNum()) {
				
				if(this.seatNum<o.getSeatNum()) {
					toReturn = -1;
				}
				else if(this.seatNum==o.getSeatNum()) {
					toReturn =0;
				}
				else {
					toReturn =1;
				}
			}
			else {
				toReturn =1;
			}
			
			return toReturn;
		}

    }

    /**
     * Represents a ticket purchased by a client
     * 
     * Class wasn't changed from starter code. 
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;


        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol +
                    showLine + eol +
                    boxLine + eol +
                    seatLine + eol +
                    clientLine + eol +
                    dashLine;

            return result;
        }
    }

    public Theater(int numRows, int seatsPerRow, String show) {
        // Set some class fields
    	rowsInTheater = numRows;
    	this.seatsPerRow = seatsPerRow;
    	this.show =  show;
    	
    	//Make lists for theater of seats and sold tickets
    	seatsInTheater = new LinkedList<>();
    	ticketsSold = new LinkedList<>();
    	//Will ensure added in correct order
    	ticketsSold = Collections.synchronizedList(ticketsSold);
    	
    	//populate this QUEUE of seats in the theater.
    	//Adding them in order of best to worst in FIFO QUEUE
    	for(int i =0; i<rowsInTheater; i++) {
    		for (int j = 1; j <= seatsPerRow; j++) {
				seatsInTheater.add(new Seat(i,j));
			}
    	}
    	
    }

    /**
     * Calculates the best seat not yet reserved
     *
     * @return the best seat or null if theater is full
     */
    public Seat bestAvailableSeat() {
        //Send best seat available
    	//already sorted in order of best to worst. 
    	//Also will send Null if all taken 
    	//Seat will also be removed from list indicating its taken next iteration.
    	return seatsInTheater.poll();
    	
    }

    /**
     * Prints a ticket for the client after they reserve a seat
     * Also prints the ticket to the console
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
    	
    	Ticket ticket;
 
    	//Create Ticket to return    	
    	ticket = new Ticket(this.show,boxOfficeId,seat,client);
    	
    	//Print ticket to console and add it in order of purchase to List
    	System.out.println(ticket);
    	ticketsSold.add(ticket);
    	
		//delay this thread so it takes time for next thread to use this method
		try {
			Thread.sleep(printDelay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return ticket;
   
    }

    /**
     * Lists all tickets sold for this theater in order of purchase
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return ticketsSold;
    }
    
   /*
    *  Method will process sales for each client from each booth
    *  
    *  @ param bxoffice - string representation of the boxoffice(Thread)
    *  					  Calling this function at this time.
    *  @ return last - Boolean to tell calling thread if this was the last
    *  				   ticket available for the theater.
    */
    public synchronized boolean processSale(String bxoffice) { 
    	//Indicate whether this was the last ticket for the theater to get 
    	//Processed
    	boolean last =  false;
    	
    	//find best available seat and print ticket for it for client.
    	Seat s;
    	s= bestAvailableSeat();
    	if( s!=null ) {
    		//Keep track of client numbers
    		customers++;
    		printTicket(bxoffice, s,customers);
    		
    		//If the last ticket was for the last seat, (no more seats left)
    		//Indicate so to caller. 
    		if(seatsInTheater.isEmpty()) {
    			last = true;
    		}
		}
    	return last;
    }
}
