#include <vector>
#include <iostream>
#include <fstream>
#include <string>
#include <time.h>
#include <sstream>
#include <iomanip>

using namespace std;

//Purpose: struct that will be populated by processes and then passed back to the master process for reporting
//double members represent the band percentages calculated by that process ( 5% for < 1 km, 25% for < 2 km.. etc)
//int members represent the raw number of residences that fall into each band
typedef struct
{
	double one, two, three, four;

	int oneI, twoI, threeI, fourI;
} Rec_t;

//Pow function without math.h
double customPow(double base, double powerOf)
{
	double result = 1.0;
	
	for(int i = 0; i < powerOf; ++i)
	{
		result *= base;
	}
	
	return result;
}

//Sqrt function without math.h
double customSqrt(double val) {
    double epsilon = 0.0000001;
	double low = 0; 
    double high = 10000000; 
    double mid = 0; 

    while (high - low > epsilon) {
            mid = low + (high - low) / 2;
            if (mid*mid > val) {
                    high = mid;
            } else {
                    low = mid;
            }    
    }   
    return mid;
}

//abs function without math.h
double customAbs(double a)
{
	return a < 0 ? -a : a;
}

//method used for calculating the shortest distance between a specific residence, and an instance of a service location
//input: service locations, residence coordinates
double findShortestDistance(vector<pair<double,double> > serviceData, double positionOne, double positionTwo)
{
	double temp = 0.0;
	double distance = customSqrt(customPow(customAbs(serviceData[0].first - positionOne), 2) + customPow(customAbs(serviceData[0].second - positionTwo), 2))/1000;

	for(unsigned i = 1; i < serviceData.size(); i++)
	{
		temp = customSqrt(customPow(customAbs(serviceData[i].first - positionOne), 2) + customPow(customAbs(serviceData[i].second - positionTwo), 2));
		temp = temp / 1000;

		if(temp < distance)		
			distance = temp;
	}

	return distance;
}

//extracts the service name from an excel file with the service number
string getServiceName(int serviceNumber)
{
	string serviceDatum;
	ifstream myfile ("service-codes.csv");
	string serviceName = "";

	if (myfile.is_open())
	{
		while ( getline (myfile, serviceDatum) )
		{
			int spaceOne = serviceDatum.find(',' , 0);
			string fileNumber = serviceDatum.substr(0,spaceOne);

			if(serviceNumber == atoi(fileNumber.c_str()))
			{
				serviceName = serviceDatum.substr(spaceOne + 1, serviceDatum.length() - 1);
			}
		}
		myfile.close();
	}
	else
	{
		cout << "Error reading file in get service name" << endl;
	}	
	
	return serviceName;
}

//populates a vector with the coordinates of all services of a certain type for later processing
void populateServiceData(vector <pair<double,double> >& serviceData, int serviceNumber)
{
	string serviceDatum;
	ifstream myfile ("services.dat");

	if (myfile.is_open())
	{
		while ( getline (myfile, serviceDatum) )
		{
			int spaceOne = serviceDatum.find('\t' , 0);
			string datumServiceNumber = serviceDatum.substr(0,spaceOne);

			if(serviceNumber == atoi(datumServiceNumber.c_str()))
			{
				int spaceTwo = serviceDatum.find('\t', spaceOne+1);
				string positionOne = serviceDatum.substr(spaceOne+1,(spaceTwo -1) - spaceOne);
				string positionTwo = serviceDatum.substr(spaceTwo+1,serviceDatum.length() - spaceTwo);
				pair<double,double> locationData;
				locationData.first = atof(positionOne.c_str());
				locationData.second = atof(positionTwo.c_str());
				serviceData.push_back(locationData);
			}
		}
		myfile.close();
	}
	else
	{
		cout << "Error in populate service data" << endl;
	}	
}

//method that calls "findShortestDistance" and populates the distances vector to report on
//will be called from each process
void populateDistances(vector<double>& distances, vector <pair<double,double> >& serviceData, int rank, int numProcs)
{	
	string myfile("residences.dat");
	string residenceDatum;

	ifstream in(myfile.c_str());
	if (!in.is_open() || in.fail())
	{
		cerr << "ERROR (process " << rank << "): Could not open file '"
			<< myfile << "'" << endl;
	}
	else
	{			
		string rec;
		getline(in, rec);
		int sinBytes = (int)in.tellg();
		
		if(rank == 0)
		{
			in.seekg(0, ios::beg);
		}
		else
		{
			in.seekg(26 * rank, ios::beg);
		}				

		int i = 1;

		while ( getline(in, residenceDatum) )
		{	
			int spaceOne = residenceDatum.find('\t', 0);
			string positionOne = residenceDatum.substr(0, spaceOne);
			string positionTwo = residenceDatum.substr(spaceOne + 1, residenceDatum.length() - spaceOne);
			distances.push_back(findShortestDistance(serviceData, atof(positionOne.c_str()), atof(positionTwo.c_str())));
						
			in.seekg(26 * (numProcs) * i, ios::beg);
			i++;
		}
	}
}

//returns elapsed time in seconds for timing main process
double diffclock(clock_t end,clock_t start)
{
    double diffticks = end - start;
    return diffticks / CLOCKS_PER_SEC;
}

//pass a derived data type through by reference and populate its variables
void populateRec(Rec_t& rec, vector<double> distances)
{
	//numbers correspond to the four bands: 0-1, 1-2, 2-5, 5+
	int one = 0;
	int two = 0;
	int three = 0;
	int four = 0;

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

	//populate raw data
	rec.oneI = one;
	rec.twoI = two;
	rec.threeI = three;
	rec.fourI = four;

	int total = one + two + three + four;

	//populate percentages
	double temp = ((double)one / (double)total) * 100.0;
	rec.one = temp;
	temp = ((double)two / (double)total) * 100.0;
	rec.two = temp;
	temp = ((double)three / (double)total) * 100.0;
	rec.three = temp;
	temp = ((double)four / (double)total) * 100.0;
	rec.four = temp;
}

//print rec output
void printOutput(Rec_t* rBuff, int numProcs)
{
	int one = 0;
	int two = 0;
	int three = 0;
	int four = 0;

	double oneP = 0.0;
	double twoP = 0.0;
	double threeP = 0.0;
	double fourP = 0.0;

	int recTotal = -1;

	for(int i = 0; i < numProcs; i++)
	{
		recTotal = rBuff[i].oneI + rBuff[i].twoI + rBuff[i].threeI + rBuff[i].fourI;
		
		cout << "Process: " << i << " for " << recTotal << " addresses." << endl;
		cout << "Nearest Service (km)    # of Addresses    % of Addresses" << endl;
		cout << "--------------------    --------------    --------------" << endl; 

		cout << setw(18) << std::right << "0-1" << setw(16) << rBuff[i].oneI << setw(16) << rBuff[i].one << endl;	
		cout << setw(18) << std::right << "1-2" << setw(16) << rBuff[i].twoI << setw(16) << rBuff[i].two << endl;	
		cout << setw(18) << std::right << "2-5" << setw(16) << rBuff[i].threeI << setw(16) << rBuff[i].three << endl;	
		cout << setw(18) << std::right << "5+"  << setw(16) << rBuff[i].fourI << setw(16) << rBuff[i].four << endl << endl;

		one += rBuff[i].oneI;
		two += rBuff[i].twoI;
		three += rBuff[i].threeI;
		four += rBuff[i].fourI;		
	}
	
	int total = one + two + three + four;

	oneP = ((double)one / (double)total) * 100;
	twoP = ((double)two / (double)total) * 100;
	threeP = ((double)three / (double)total) * 100;
	fourP = ((double)four / (double)total) * 100;

	cout << "Aggregate results for " << total << " residences" << endl;
	cout << "Num processes: " << numProcs << endl;
	cout << setw(18) << std::right << "0-1" << setw(16) << one << setw(16) << oneP << endl;	
	cout << setw(18) << std::right << "1-2" << setw(16) << two << setw(16) << twoP << endl;	
	cout << setw(18) << std::right << "2-5" << setw(16) << three << setw(16) << threeP << endl;	
	cout << setw(18) << std::right << "5+"  << setw(16) << four << setw(16) << fourP << endl;
}

//function taken from class examples, updated for project, corresponds to Rec struct in functions.h
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