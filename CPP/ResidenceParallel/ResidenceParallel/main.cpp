#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <mpi.h>
#include "functions.h"

using namespace std;

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
			vector <pair<double,double> > serviceData;
			vector <double> distances;
			
			serviceNumber = atoi(argv[1]);
			
			populateServiceData(serviceData, serviceNumber);

			//Create data type and data type buffer
			MPI_Datatype recType = createRecType();
			Rec_t rec; 
			Rec_t* rBuff = new Rec_t[numProcs];

			//Carry out algorithm and retrieve data
			clock_t start = clock();
				populateDistances(distances, serviceData, rank, numProcs);
				populateRec(rec, distances);
				MPI_Gather(&rec, 1, recType, rBuff, 1, recType, 0, MPI_COMM_WORLD);
			clock_t end = clock();

			if(rank == 0)
			{
				cout << "Service: " << getServiceName(serviceNumber) << endl;
				cout << "Service Code: " << serviceNumber << endl;
				cout << "Number of service locations: " << serviceData.size() << endl;
				cout << "Number of processes: " << numProcs << endl;
				cout << "Time elapsed: " << diffclock(end, start) << endl << endl;
				
				printOutput(rBuff, numProcs);
			}
		}
		else
            cerr << "This program requires a minimum of 2 processes!" << endl;

        MPI_Finalize();
	}

	return 0;
}