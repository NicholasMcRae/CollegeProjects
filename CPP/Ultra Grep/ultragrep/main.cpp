#include <iostream>
#include <vector>
#include <string>
#include <boost\shared_ptr.hpp>
#include "Recursion.h"

using namespace std;

int main( int argc, char *argv[] )
{
	if(argc < 4 || argc > 5)
	{
		wstring title = L"Error";
		wstring msg = L"Incorrect number of input parameters, try again!";
		MessageBox(NULL, msg.c_str(), title.c_str(), MB_OK | MB_ICONERROR);
		return 0;
	}	
	
	string paramOne = argv[1];	
	bool verbose = true;
	string directory;
	string regExpr;
	string fileTypes;

	try
	{
		if( paramOne == "-v" || paramOne == "-V" )
		{
			verbose = true;
			directory = argv[2];
			regExpr = argv[3];

			if( argc == 5 )
			{
				fileTypes = argv[4];			
			}
			else
			{
				fileTypes = ".txt";
			}
		}
		else
		{
			verbose = false;
			directory = argv[1];
			regExpr = argv[2];

			if( argc == 4 )
			{
				fileTypes = argv[3];			
			}
			else
			{
				fileTypes = ".txt";
			}
		}

		shared_ptr<recursive> recurse( new recursive());
		unsigned matches = 0;
		unsigned matchedFiles = 0;
		vector <HANDLE> handles;
		path inputDirectory = path(directory);
		Win32Struct package(inputDirectory.leaf(), regExpr, recurse->output, matches, matchedFiles );
		package.data.resize(package.data.size() + 1);

		if(!verbose)
		{		
			recurse->extensions = recurse->findExtensions(fileTypes);		
			unsigned i = 0;
			
			recurse->output.resize(recurse->output.size() + 1);
			recurse->recurse_directory(recurse->output, inputDirectory, regExpr, recurse->extensions, matches, matchedFiles, handles, package, scan, i );
		}
		else if (verbose)
		{
			recurse->extensions = recurse->findExtensions(fileTypes);		
			unsigned i = 0;
			path inputDirectory = path(directory);
			recurse->output.resize(recurse->output.size() + 1);
			recurse->recurse_directory_verbose(recurse->output, inputDirectory, regExpr, recurse->extensions, matches, matchedFiles, handles, package, scan_verbose, i );
		}

		WaitForMultipleObjects( handles.size(), handles.data(), TRUE, INFINITE );

		recurse->printVector(package.data);

		cout << endl << "Number of matches: " << package.matches << endl;
		cout << endl << "Number of matched files: " << package.matchedFiles << endl;
	 
		for( auto& h : handles )
			CloseHandle( h );	

	}
	catch(...)
	{
		wstring title = L"Error";
		wstring msg = L"Something went wrong!";
		MessageBox(NULL, msg.c_str(), title.c_str(), MB_OK | MB_ICONERROR);
	}

	return 0;
}