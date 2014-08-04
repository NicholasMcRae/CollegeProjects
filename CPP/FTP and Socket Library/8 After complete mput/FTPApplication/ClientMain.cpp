#include <iostream>
#include <string>
#include <fstream>
#include <boost/filesystem.hpp> 
#include <SocketLib.hpp>
using namespace std;
#include <WinSock2.h>
#pragma comment (lib, "ws2_32.lib")

using namespace boost::filesystem;

/********************************************************
Purpose: FTP Client - app has the ability to either receive all the files in a directory from the server application, or send all the files in a directory 
					  to the server application. The command is sent from the client.
Authors: Nick McRae / Milan Sobat
Revision History:
March 8-9th/2014   - Researched how an FTP application worked and how I might go about building one, built a basic prototype that was able to send a single file 
                     from the client to server
March 15-16th/2014 - Realized we needed to implement directory functionality, re-installed boost, implemented send-files function that recursed through and 
                     sent all files in directory. Also built trimString function.
March 22-23rd/2014 - Built socket library and got it functional with the server
March 29-30th/2014 - Implemented socket library logic on server
April 5-6th/2014 -   Touched up code
adding some more text
*********************************************************/

string trimString (string input)
{
	char slash = '\\';
	int loc = input.rfind(slash);
	input = input.substr(loc+1, input.size());
	return input;
}

/*
Purpose: Take socket library object, directory path, and send all files within that directory across the network
Input: Directory path and socket object
Output: Files sent across the network
*/
void send_files( const path & dir_path, Socket& socket )            
{
  std::string fileName;
  char* command = "";

  if ( !exists( dir_path ) ) return;

  directory_iterator end_itr; 
  for ( directory_iterator itr( dir_path ); itr != end_itr; ++itr )
  { 
	  if ( is_directory(itr->status()) )
      {   
		  send_files( itr->path(), socket );
      }
	  else if(is_regular_file(itr->path()))
	  {
	     cout << "Inside send files" << endl;
		  
		  fileName = itr->path().string();
		  std::string trimmedFileName = trimString(fileName);
		  char *a = new char[trimmedFileName.size()+1];
		  a[trimmedFileName.size()] = 0;
          memcpy(a,trimmedFileName.c_str(),trimmedFileName.size());
		  char *b = new char[fileName.size()+1];
		  b[fileName.size()] = 0;
          memcpy(b,fileName.c_str(),fileName.size());
		  
		  int bytesSent = socket.sendData('c', a, strlen(a)+1);
		  		
		  FILE* fp;
		  errno_t err = fopen_s( &fp, b, "r" );

		  if( err != 00)
		  {
			std::cout << "Error code is " << err << std::endl;
			std::cout << "Problem opening stream" << std::endl;
		  }
		  
		  fseek(fp, 0, SEEK_END);
		  long FileSize = ftell(fp);
		  rewind(fp);
		  char GotFileSize[1024];		  
		  _itoa_s(FileSize, GotFileSize, 10);

		  char* sentSize = (char*) malloc (sizeof(char)*1024);
		  sentSize  = GotFileSize;

		  bytesSent = socket.sendData('c', sentSize, strlen(sentSize)+1);
		  
		  char * buffer;
		  long SizeCheck = 0;
		  buffer = (char*) malloc (sizeof(char)*FileSize);
		  while(SizeCheck < FileSize){			
			int Read = fread(buffer, 1, FileSize, fp);	
			int Sent = socket.sendData('f', buffer, Read);
			SizeCheck += Sent;
	      }
		  free(buffer);		  
	  }
  }
}

bool dirExists(const std::string& dirName_in)
{
  DWORD ftyp = GetFileAttributesA(dirName_in.c_str());
  if (ftyp == INVALID_FILE_ATTRIBUTES)
    return false;  //something is wrong with your path!

  if (ftyp & FILE_ATTRIBUTE_DIRECTORY)
    return true;   // this is a directory!

  return false;    // this is not a directory!
}

Socket socketObject("127.0.0.1", 27022, 27021, false);

int main()
{
	char name [1024];
	char comm [1024];
	char* fileName;
	char* command = "";		
	long FileSize = 0;
	long SizeCheck = 0;	
	string text = "";
	char * buffer;
	char * mfcc;

	std::cout << "Client opened.." << std::endl;

	while (true)
	{
		cout << "Enter a command" << endl;
		gets_s(comm, 1024);
		cout << "Enter a file structure" << endl;
		gets_s(name, 1024);

		command = comm;
		fileName = name;

		if(_stricmp(command,"mget") == 0)
		{
			int bytesSent = socketObject.sendData( 'c', command, strlen(command)+1 );
			bytesSent = socketObject.sendData( 'c', fileName, strlen(fileName)+1);

			if(true)
			{
				while( true ) {

					 SizeCheck = 0;
					 FileSize = 0;				 
					 
					 fileName = (char*)malloc(sizeof(char)*1024);
					 int bytesRecv = socketObject.receiveData('c', fileName, 1025);

					 if(_stricmp(fileName,"goodbye") == 0)
						{						
							break;
						}
				
					while( true )
					{
						char GotFileSize[1024];
						char* sentSize = GotFileSize;
						socketObject.receiveData('c', sentSize, 1024);
						strncpy(GotFileSize, sentSize, 1024);
						FileSize = atoi(GotFileSize);

						if (FileSize > 0)
						{	
							break;
						}
					}

					mfcc = (char*)malloc(sizeof(char)*FileSize);
					FILE *fp = fopen(fileName, "wb");
				
					while(SizeCheck < FileSize){
						int received = socketObject.receiveData( 'f', mfcc, FileSize);

						if(received != SOCKET_ERROR)
						{				
							int written = fwrite(mfcc, 1, FileSize, fp);
							SizeCheck += received;
							fflush(fp);
						}	
					}//while transfer


				}//while true
			}//end file good if
		}
		else if(_stricmp(command,"mput") == 0)
		{
			if(!dirExists(fileName))
			{
				cout << "Directory does not exist" << endl;				
			}
			
			int byteSent = socketObject.sendData( 'c', command, strlen(command)+1);

			const path targetDir(fileName); 
			
			send_files( targetDir, socketObject );
			
			strcpy(command, "goodbye");

			byteSent = socketObject.sendData( 'c', command, strlen(command)+1);
		}
		else if(_stricmp(command,"quit") == 0)
		{
			int byteSent = socketObject.sendData( 'c', command, strlen(command)+1);
			break;
		}
	}

	cout << "Program Exiting..." << endl;
}