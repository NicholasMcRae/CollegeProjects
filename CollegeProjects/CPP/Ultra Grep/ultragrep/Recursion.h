#ifndef _Recursion_h_
#define _Recursion_h_

#include <string>
#include <vector>
#include <filesystem>
#include <iostream>
#include <regex>
#include <thread>
#include <Windows.h>
#include <mutex>
#include <sstream>
#include <atomic>
using namespace std::tr2::sys;
using namespace std;

/********************************************************************************/
/*Author              Date                 Description
 *********************************************************************************
Nicholas McRae        June. 4/13            Worked out logic and proof of concept with comments and other test solutions
Nicholas McRae        June. 9/13            Wrote a rough recursive and scan function
Nicholas McRae        June. 10/13           Wrote and tested check extension function, worked out bugs, brought app to rough completion
Nicholas McRae        June. 11/13           Implemented threading, critical sections, polished output, implemented verbose functions
Nicholas McRae        June. 16-17/13        Implemented Win32 threading via scan and scan_verbose
Nicholas McRae        June. 20/13           Made the program work via the command line, looked at some weird bugs that appear to be related to threading throwing
											some kind of error when I test a .cpp file. Every now and then the program seems to work intermittently with .cpp
											
*/


//package to be passed by reference into recursive function, 
struct Win32Struct 
{
	//( recursive::scan, f / d->path(), regex, std::ref(data), std::ref(matches), std::ref(matchedFiles) );
	string pathString;
	string regex;
	vector<vector<pair<int,string>>> data;
	unsigned matches;
	unsigned matchedFiles;

	Win32Struct( path p, string r, vector<vector<pair<int,string>>> d, unsigned m, unsigned mF ) :
		pathString(p), regex(r), data(d), matches(m), matchedFiles(mF) { }
};

//Purpose: implement functions and variables necessary for app to function
class recursive
{
public:
	recursive(){};
	~recursive(){};
	void recurse_directory(vector<vector<pair<int,string>>>& data, path const& f, string regex, vector <string> extensions, unsigned& matches, unsigned& matchedFiles, vector <HANDLE>& handles, Win32Struct& package, DWORD WINAPI scan( void* p ), unsigned i = 0);//working algorithm that parses through data	
	void recurse_directory_verbose(vector<vector<pair<int,string>>>& data, path const& f, string regex, vector <string> extensions, unsigned& matches, unsigned& matchedFiles, vector<HANDLE>& vecHandles, Win32Struct& package, DWORD WINAPI scan_verbose( void* p ), unsigned i = 0 );//working algorithm that parses through data	
	//DWORD WINAPI recursive::scan( void* p );
	static void scan_verbose(path const& f, string term, vector<vector<pair<int,string>>>& data, unsigned& matchNumber, unsigned& matchedFileCounter);
	bool checkExtension ( path const& f, vector <string> extensions );
	vector <string> findExtensions ( string extensions );	
	string ws2s(const std::wstring& s);
	void printVector(vector <vector<pair<int,string>>> output);
	vector<vector<pair<int,string>>> output;
	vector<string> extensions;
};

//Purpose: thread locking
class CriticalSection {
	CRITICAL_SECTION m_cs;
public:
	CriticalSection() {
		InitializeCriticalSection( &m_cs );
	}

	~CriticalSection() {
		DeleteCriticalSection( &m_cs );
	}
private:
	void Enter() {
		EnterCriticalSection( &m_cs );
	}

	void Leave() {
		LeaveCriticalSection( &m_cs );
	}

	friend class CSLock;
};

//Purpose: thread locking
class CSLock {
	CriticalSection& m_csr;
public:
	CSLock( CriticalSection& csr ) : m_csr( csr ) {
		m_csr.Enter();
	}
	~CSLock() { m_csr.Leave(); }
};

//Purpose: this function pulls in the extensions arg from the command line and puts each of them in a vector that will be checked via "check extensions"
//Pre-conditions: command line param
//Post-conditions: vector of extension strings
vector <string> recursive::findExtensions( string extensions )
{
	//cout << "enter find extensions" << endl;

	vector <string> allExtensions;
	int found = 0;
	int last = 0;
	string extension;

	found = extensions.find(".");

	if (found != string::npos)//if we found a period there is at least one extension
	{
		last = extensions.find(".", found+1);//looking to see if there is a second extension

		if(last != string::npos)// if there is no second extension we push_back the only extension into the vector
		{
			extension = extensions.substr(found+1, last-1);
			allExtensions.push_back(extension);
		}
		else//if we didn't find last we use the whole string size to push back the thing
		{
			extension = extensions.substr(found+1, extensions.size());
			allExtensions.push_back(extension);
		}
	}

	while(last != string::npos)//while found isn't empty run this
	{
		found = last; // finding the next period after the last period
		last = extensions.find(".", found+1); // seeing if there is another period after the found period

		if(last != string::npos)
		{
			extension = extensions.substr(found+1, (last-found-1));
			allExtensions.push_back(extension);			
		}
		else
		{
			extension = extensions.substr(found+1, extensions.size());
			allExtensions.push_back(extension);			
		}
	}

	//cout << "Exit find extensions" << endl;

	return allExtensions;
}

//Purpose: change a wstring to a string
//Pre-conditions: wstring
//Post-conditions: string
inline string recursive::ws2s(const std::wstring& s)
{
std::string ws;
ws.assign(s.begin(), s.end());
return ws;
}

//Purpose: checks extension in the file path when recursion is happening against the extension vector provided by findExtensions
//		   Returns a true if the path extension matches one of the command line extensions, which will cause the file to be scanned by the scan function
//Pre-conditions: path with extension, vector of extensions
//Post-conditions: bool
inline bool recursive::checkExtension ( path const& f, vector <string> extensions )
{	
	//cout << "Enter check extension" << endl;

	try
		{
		string filePath = f.leaf();		
		int found = 0;
		string extension;	

		found = filePath.rfind(".");
		int last = filePath.size();	
		extension = filePath.substr(found + 1, last);

		//cout << "Inside checkExtension " << f << endl;

		cout << extensions.size() << endl;
		int counter = 0;

		for( string& k : extensions)
		{

			if(k == extension)
			{
				//cout << "leave check extension" << endl;
				return true;
			}
			//cout << "after extension check" << endl;
		}

		//cout << "Leave check extension" << endl;
		return false;
	}
	catch(...)
	{
		return false;
	}
}

//Purpose: recurse directory, the parent of all other function calls in the app, parses through directory and calls scan on appropriate files, passes through counter variables
//		   and output vector
//Pre-conditions: empty vector and matchedFile/matchedLine counters
//Post-conditions: populated vector and matchedFile/matchedLine counters
inline void recursive::recurse_directory(vector<vector<pair<int,string>>>& data, path const& f, string regex, vector <string> extensions, unsigned& matches, unsigned& matchedFiles, vector<HANDLE>& vecHandles, Win32Struct& package, DWORD WINAPI scan( void* p ), unsigned i )
{
	wstring indent( i, L'\t' );

	//cout << "Enter recurse directory" << endl;

	for( directory_iterator d(f), e; d!=e; d++)
	{
		if( is_directory( d->status() ) )
		{
			recurse_directory(data, f / d->path(), regex, extensions, matches, matchedFiles, vecHandles, package, scan, i);
		}
		else if (is_regular_file( d->status()))
		{			

			//cout << d->path().leaf() << endl;

			bool foundExt = checkExtension(f / d->path(), extensions);		    			

			if(foundExt)
			{	
				path somePath = f / d->path();	
				path finalPath = (somePath);				
				package.pathString = finalPath.string();
				vecHandles.push_back( CreateThread( NULL, 0, scan, &package, 0, NULL ) );				
			}			
		}			
	}
	//cout << "Exit recurse directory" << endl;
}

//Purpose: same as recurse directory but prints during each subsequent call. I decided to write two functions instead of 1 to reduce overhead when the original was called
//Pre-conditions: same as recurse directory
//Post-conditions: same as recurse directory but with additional information outputted to the console
inline void recursive::recurse_directory_verbose(vector<vector<pair<int,string>>>& data, path const& f, string regex, vector <string> extensions, unsigned& matches, unsigned& matchedFiles, vector<HANDLE>& vecHandles, Win32Struct& package, DWORD WINAPI scan_verbose( void* p ), unsigned i )
{
	wstring indent( i, L'\t' );

	for( directory_iterator d(f), e; d!=e; ++d)
	{
		if( is_directory( d->status() ) )
		{
			recurse_directory_verbose(data, f / d->path(), regex, extensions, matches, matchedFiles, vecHandles, package, scan_verbose, i);
		}
		else if (is_regular_file( d->status()))
		{			
			bool foundExt = checkExtension(f / d->path(), extensions);

			if(foundExt)
			{	
				std::ostringstream oss;
                oss << "Grepping: " << f / d->path() << endl;
                std::cout << oss.str();								
				path somePath = f / d->path();	
				path finalPath = (somePath);				
				package.pathString = finalPath.string();
				vecHandles.push_back( CreateThread( NULL, 0, scan_verbose, &package, 0, NULL ) );				
			}			
		}			
	}
}


//Purpose: scan function that populates reference variables while scanning appropriate files, built for use with Win32 threading
//Pre-conditions: struct pointer
//Post-conditions: updated counters and data vector
DWORD WINAPI scan( void* p ) {

	//cout << "Enter scan" << endl;

	CRITICAL_SECTION cs;

	InitializeCriticalSection(&cs);
	EnterCriticalSection(&cs);


	/*CriticalSection lock;
	CSLock local( lock );*/
	
	Win32Struct& pack = *reinterpret_cast<Win32Struct*>( p );	

	try
		{
		
		string r = pack.regex;
		const string f = pack.pathString;
		ifstream inStream (f);	
		//inStream.open(f.c_str());
		int lineNumber = 0;
		vector<pair<int,string>> pushed;
		regex reg( r );
		bool fileContainsMatch = false;
		atomic_int fileNumber;
		int lineMatch = 0;
		

		while(!inStream.eof())
		{ 	
			string x = "";
			getline(inStream, x, '\n');
			lineNumber++;
			int found = 0;
			int counterContainer = 0;
			bool lineContainsMatch = false;

			auto it = x.cbegin();
			auto end = x.cend();
			smatch m;
		

			while( regex_search( it, end, m, reg ))//if we call this function and something is was found it returns true. . keeps going until something isn't found
			{			
				it = m[lineMatch].second;
				lineMatch++;			

				if( !fileContainsMatch )
				{
					pair<int,string> pa;
					pa.first = NULL;
					pa.second = pack.pathString;
					pushed.push_back(pa);
					pack.matchedFiles++;
					fileNumber = pack.matchedFiles;
					fileContainsMatch = true;				
				}

				if( !lineContainsMatch )
				{
					lineContainsMatch = true;
				}

			}//end inner while

			if(lineContainsMatch)
			{	
				pack.matches++;
				pair<int,string> pa;
				pa.first = lineNumber;
				pa.second = x;
				pushed.push_back(pa);
			}
		}//end outer while

		if(fileContainsMatch)
		{
			cout << "before loading pack data" << endl;
			pack.data.resize(pack.data.size() + 1);
			pack.data[fileNumber] = pushed;		
		}

		inStream.close();

		//cout << "Exit scan" << endl;

		LeaveCriticalSection(&cs);
	}
	catch(...)
	{

	}
	return 0;
}

//Purpose: scan function that populates reference variables while scanning appropriate files, built for use with Win32 threading, also outputs verbose output
//Pre-conditions: struct pointer
//Post-conditions: updated counters and data vector as well as output to console
DWORD WINAPI scan_verbose( void* p ) {
	
	Win32Struct& pack = *reinterpret_cast<Win32Struct*>( p );

	CriticalSection lock;
	CSLock local( lock );
	
		
	string r = pack.regex;
	const string f = pack.pathString;
	ifstream inStream (f);	
	//inStream.open(f.c_str());
	int lineNumber = 0;
	vector<pair<int,string>> pushed;
	regex reg( r );
	bool fileContainsMatch = false;
	int fileNumber = 0;
	int lineMatch = 0;	

	while(!inStream.eof())
	{ 	
		string x = "";
		getline(inStream, x, '\n');
		lineNumber++;
		int found = 0;
		int counterContainer = 0;
		bool lineContainsMatch = false;

		auto it = x.cbegin();
		auto end = x.cend();
		smatch m;
		

		while( regex_search( it, end, m, reg ))//if we call this function and something is was found it returns true. . keeps going until something isn't found
		{			
			it = m[lineMatch].second;
			lineMatch++;			

			if( !fileContainsMatch )
			{
				pair<int,string> pa;
				pa.first = NULL;
				pa.second = pack.pathString;
				pushed.push_back(pa);
				pack.matchedFiles++;
				fileNumber = pack.matchedFiles;
				fileContainsMatch = true;				
			}

			if( !lineContainsMatch )
			{		
				std::ostringstream oss;
                oss << "Matched: " << x << endl;
                std::cout << oss.str();				
				lineContainsMatch = true;
			}

		}//end inner while

		if(lineContainsMatch)
		{	
			pack.matches++;
			pair<int,string> pa;
			pa.first = lineNumber;
			pa.second = x;
			pushed.push_back(pa);
		}
	}//end outer while

	if(fileContainsMatch)
	{
		pack.data.resize(pack.data.size() + 1);
		pack.data[fileNumber] = pushed;		
	}

	inStream.close();

	return 0;
}

//Purpose: same as scan but with output, wrote two functions to reduce overhead in the call of the original function
//Pre-conditions: counters and vector
//Post-conditions: incremented counters and vector
inline void recursive::scan_verbose(path const& fileName, string term, vector<vector<pair<int,string>>>& data, unsigned& matchNumber, unsigned& matchedFileCounter)
{
	ifstream inStream;	
	inStream.open(fileName);
	string filePath = fileName.leaf();
	int lineNumber = 0;
	vector<pair<int,string>> pushed;
	regex reg( term );
	bool fileContainsMatch = false;
	int fileNumber = 0;
	int lineMatch = 0;
	CriticalSection lock;
	CSLock local( lock );	

	while(!inStream.eof())
	{ 	
		string x = "";
		getline(inStream, x, '\n');
		lineNumber++;
		int found = 0;
		int counterContainer = 0;
		bool lineContainsMatch = false;

		auto it = x.cbegin();
		auto end = x.cend();
		smatch m;
		

		while( regex_search( it, end, m, reg ))//if we call this function and something is was found it returns true. . keeps going until something isn't found
		{			
			it = m[lineMatch].second;
			lineMatch++;			

			if( !fileContainsMatch )
			{
				pair<int,string> p;
				p.first = NULL;
				p.second = fileName;
				pushed.push_back(p);
				matchedFileCounter++;
				fileNumber = matchedFileCounter;
				fileContainsMatch = true;				
			}

			if( !lineContainsMatch )
			{
				cout << "Matched: " << x << endl;
				lineContainsMatch = true;
			}

		}//end inner while

		if(lineContainsMatch)
		{
			matchNumber++;
			pair<int,string> p;
			p.first = lineNumber;
			p.second = x;
			pushed.push_back(p);
		}
	}//end outer while

	if(fileContainsMatch)
	{
		data.resize(data.size() + 1);
		data[fileNumber] = pushed;
	}

	inStream.close();	
}

//Purpose: print the contents of the populated output vector
//Pre-conditions: vector
//Post-conditions: vector printed to console
inline void recursive::printVector(vector <vector<pair<int,string>>> output)
{
	cout << endl;

	for(vector<pair<int,string>> i : output)
	{
		for(pair<int,string> p : i)
		{
			cout << "["<< p.first << "]" << "  " << p.second << endl;
		}

	}
}



#endif