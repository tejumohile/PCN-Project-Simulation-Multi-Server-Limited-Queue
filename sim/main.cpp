
#include <iostream>
#include "rv.h"
#include "event.h"
#include "math.h"
// Simulates an M/M/n/k queueing system.  The simulation terminates
// once 100000 customers depart from the system.
using namespace std;


int main()
{



  EventList Elist;                // Create event list
  enum {ARR1EVENT, ARR2EVENT, DEP};                 // Define the event types
  
  int proc = 2;                  // Number of processors
  int k = 4 ;    // The total capacity of the system
  int l = 1;             // Threshold for lambda1 USER JOBS
  
  double mu = 3;                // Service rate
  double lambda2 = 4;            // Arrival rate for user jobs
  cout << "Enter the number of processors: ";
  cin >> proc ;
  cout << "Enter k: ";
  cin >> k ;
  cout << "Enter l: ";
  cin >> l ;
  cout << "Enter mu: ";
  cin >> mu ;
  cout << "Enter lambda2: ";
  cin >> l ;

  double rho = 0;
  for (int i = 0 ; i < 10 ; i++)
  {
  	rho += 0.1;
  	double lambda1 = rho * proc * mu;            // Arrival rate for admin jobs  
  double clock = 0.0;             // System clock


  //Number of customers counters
  int N = 0;                      // Number of customers in system
 

  int Ndep = 0;                   // Number of departure from system
 
  int Ndepadmin = 0;              // Number of admin job departures from system


  int Nblock =0;
  int Nuserblock = 0;
  int Nadminblock = 0;
	
  
  double EN = 0.0;                // For calculating E[N]
  int done = 0;                   // End condition satisfied?


  Event* CurrentEvent;


  int NArrivals = 0;
  int NUserArrivals =0;
  int NAdminArrivals = 0;
  //added new
  double prevDepClock = 0;
  
  // end added new
  
  Elist.insert(exp_rv(lambda1),ARR1EVENT); // Generate first arrival events


  Elist.insert(exp_rv(lambda2),ARR2EVENT);



 while(!done)
  {
    CurrentEvent = Elist.get();               // Get next Event from list
    double prev = clock;                      // Store old clock value
    clock=CurrentEvent->time;                 // Update system clock 
    switch (CurrentEvent->type) {
    case ARR1EVENT:                                 // If arrival 

                        	 	     //  update system statistics
    	NArrivals++;                                    //  update system size
    	NAdminArrivals++;
      if(N < k) {

		EN += N*(clock-prev);
		N++;
		Elist.insert(clock+exp_rv(lambda1),ARR1EVENT); //  generate next arrival
		if (N<=proc) {
			Elist.insert(clock+exp_rv(mu),DEP);   //  generate its departure event
		}		
      }
      else if(N >= k)
      {
    	  EN += N*(clock-prev);
    	  Nadminblock++;
		  Elist.insert(clock+exp_rv(lambda1),ARR1EVENT); //  generate next arrival which is blocked
      }
      
      break;
    case ARR2EVENT:                                 // If arrival 


                     //  update system statistics
    	NArrivals++;
    	NUserArrivals++;


      if(N < l ) {  


    	EN += N*(clock-prev);
    	N++;
	Elist.insert(clock+exp_rv(lambda2),ARR2EVENT); //  generate next arrival
	                                    
	if (N<=proc) {  	  


    	 
         Elist.insert(clock+exp_rv(mu),DEP);   //  generate its departure event
		}
      }
      else if ( N >= l)
      {
    	  EN += N*(clock-prev);
    	  Nuserblock++;
	  Elist.insert(clock+exp_rv(lambda2),ARR2EVENT); //blocked
      }
      
      break;


    case DEP:                                 // If departure
    
      EN += N*(clock-prev);// If customers remain
      N--;										 //  update system statistics
	                         	  	  //  decrement system size
      Ndep++;                                 //  increment num. of departures


      if (N>=proc) {    	  
	  
    	  Elist.insert(clock+exp_rv(mu),DEP);   //  generate next departure


      }

      break;
    }
    delete CurrentEvent;
    if (Ndep >= 100000)
        		done=1;
        // End condition
  }
  // output simulation results for N, E[N], E[T]
  cout << "rho: " << rho << endl;
  cout << "Expected number of customers (simulation): " << EN/clock << endl;
  cout << "Expected waiting time of a customers (simulation): " << EN/Ndep << endl;
  cout << "Total Arrivals: " << NArrivals << endl;
  cout << "Admin jobs arrived: " << NAdminArrivals << endl;
  cout << "Admin jobs blocked: " << Nadminblock << endl;
  cout << "User jobs arrived: " << NUserArrivals << endl;
  cout << "User jobs blocked: " << Nuserblock << endl;
  cout << "Probability of user jobs blocked: " << (double)Nuserblock / (double)NUserArrivals << endl;
  cout << "Probability of admin jobs blocked: " << (double)Nadminblock / (double)NAdminArrivals << endl;
  }
}