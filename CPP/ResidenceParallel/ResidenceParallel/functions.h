#include <vector>
#include <iostream>
#include <fstream>
#include <string>
#include <time.h>

using namespace std;

typedef struct
{
	double one, two, three, four;
	int oneI, twoI, threeI, fourI;
} Rec_t;

double findShortestDistance(vector<pair<double,double>> serviceData, double positionOne, double positionTwo)
{
	double temp = 0.0;
	double distance = sqrt(pow(abs(serviceData[0].first - positionOne), 2) + pow(abs(serviceData[0].second - positionTwo), 2))/1000;

	for(unsigned i = 1; i < serviceData.size(); i++)
	{
		temp = sqrt(pow(abs(serviceData[i].first - positionOne), 2) + pow(abs(serviceData[i].second - positionTwo), 2));
		temp = temp / 1000;

		if(temp < distance)		
			distance = temp;		
	}

	return distance;
}

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
		cout << "Error" << endl;
	}	
	
	return serviceName;
}

void populateServiceData(vector <pair<double,double>>& serviceData, int serviceNumber)
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
		cout << "Error" << endl;
	}	
}

void populateDistances(vector<double>& distances, vector <pair<double,double>>& serviceData, int numProcs, int stripe)
{
	ifstream myfile("residences.dat");
	string residenceDatum;
	int count = 1;
	if (myfile.is_open())
	{
		while ( getline (myfile, residenceDatum) )
		{	
			if(count == stripe)
			{
				int spaceOne = residenceDatum.find('\t', 0);
				string positionOne = residenceDatum.substr(0, spaceOne);
				string positionTwo = residenceDatum.substr(spaceOne + 1, residenceDatum.length() - spaceOne);
				distances.push_back(findShortestDistance(serviceData, atof(positionOne.c_str()), atof(positionTwo.c_str())));
			}

			if(count == numProcs)
			{
				count = 1;
			}

			count++;
		}
		myfile.close();
	}
	else
	{
		cout << "Error" << endl;
	}
}

double diffclock(clock_t end,clock_t start)
{
    double diffticks = end - start;
    return diffticks / CLOCKS_PER_SEC;
}

void populateRec(Rec_t& rec, vector<double> distances)
{
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

	rec.oneI = one;
	rec.twoI = two;
	rec.threeI = three;
	rec.fourI = four;

	int total = one + two + three + four;

	double temp = ((double)one / (double)total) * 100.0;
	rec.one = temp;
	temp = ((double)two / (double)total) * 100.0;
	rec.two = temp;
	temp = ((double)three / (double)total) * 100.0;
	rec.three = temp;
	temp = ((double)four / (double)total) * 100.0;
	rec.four = temp;
}

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

	for(int i = 0; i < numProcs; i++)
	{
		one += rBuff[i].oneI;
		two += rBuff[i].twoI;
		three += rBuff[i].threeI;
		four += rBuff[i].fourI;
		oneP += rBuff[i].one;
		twoP += rBuff[i].two;
		threeP += rBuff[i].three;
		fourP += rBuff[i].four;
	}
	
	cout << "                 0-1    " << one << "            " << oneP << endl;	
	cout << "                 1-2    " << two << "            " << twoP << endl;	
	cout << "                 2-5    " << three << "            " << threeP << endl;	
	cout << "                 5+    " << four << "            " << fourP << endl;
}