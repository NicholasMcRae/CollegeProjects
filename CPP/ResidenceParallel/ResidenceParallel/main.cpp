#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <mpi.h>
#include "functions.h"

using namespace std;

/*
Author     Date             Description
******************************************
Nick	   Nov. 4 2014	    Initial Implementation, Broke functions into header, set up master and slave, set up striping

*/

int TAG_DATA = 0;
int TAG_QUIT = 1;



MPI_Datatype createRecType()
{
	// Setup the five arguments for a call to MPI_Type_struct()
	int count = 2;	// 2 blocks
	int blocklens[] = { 4, 4 };	// 4 doubles, 4 ints
	MPI_Aint offsets[2];	
	offsets[0] = offsetof(Rec_t, one); // offset in bytes to block 1
	offsets[1] = offsetof(Rec_t, oneI); // offset in bytes to block 2
	MPI_Datatype old_types[] = { MPI_DOUBLE, MPI_INT };	// input data types
	MPI_Datatype t; // output data type
	
	// Call the datatype contructor function
	MPI_Type_struct(count, blocklens, offsets, old_types, &t);

	// Allocate memory for the new type
	MPI_Type_commit(&t);

	return t;
}



void processMaster(int numProcs, int serviceNumber)
{
	

	
	
	
}

void processSlave(int numProcs, int rank, int serviceNumber)
{
	vector <pair<double,double>> serviceData;
	vector <double> distances;

	//receive service number here

	populateServiceData(serviceData, serviceNumber);
	populateDistances(distances, serviceData, numProcs, rank + 1);

	int one = 0;
	int two = 0;
	int three = 0;
	int four = 0;
	int five = 0;
	int six = 0;

	for(unsigned i = 0; i < distances.size(); i++)
	{
		if(distances[i] < 1.0)
			one++;
		else if(distances[i] < 2.0)
			two++;
		else if(distances[i] < 5.0)
			three++;
		else if(distances[i] > 5.0)
			four++;
	}

	//do calculations and broadcast using a derived type here
}

int main(int argc, char* argv[])
{
	if (MPI_Init(&argc, &argv) == MPI_SUCCESS)
	{
		int numProcs, rank;
        MPI_Comm_size(MPI_COMM_WORLD, &numProcs);
        
		if( numProcs > 1 )
		{
		    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

			int serviceNumber = -1;
			vector <pair<double,double>> serviceData;
			vector <double> distances;
	
			if(rank == 0)
			{
				cout << "Enter a service number" << endl;
				cin >> serviceNumber;
			}

			MPI_Bcast(&serviceNumber, 1, MPI_INT, 0, MPI_COMM_WORLD);

			clock_t start = clock();
				populateServiceData(serviceData, serviceNumber);
				populateDistances(distances, serviceData, numProcs, 1);
			clock_t end = clock();

			MPI_Datatype recType = createRecType();

			Rec_t rec; 
			Rec_t* rBuff = new Rec_t[numProcs];
			populateRec(rec, distances);			

			MPI_Gather(&rec, 1, recType, rBuff, 1, recType, 0, MPI_COMM_WORLD);

			if(rank == 0)
			{
				cout << "Service: " << getServiceName(serviceNumber) << endl;
				cout << "Service Code: " << serviceNumber << endl;
				cout << "Number of service locations: " << serviceData.size() << endl;
				cout << "Time elapsed: " << diffclock(end, start) << endl << endl;

				cout << "Nearest Service (km)    # of Addresses    % of Addresses" << endl;
				cout << "--------------------    --------------    --------------" << endl; 
				printOutput(rBuff, numProcs);
			}
		}
		else
            cerr << "This program requires a minimum of 2 processes!" << endl;

        MPI_Finalize();
	}

	return 0;
}