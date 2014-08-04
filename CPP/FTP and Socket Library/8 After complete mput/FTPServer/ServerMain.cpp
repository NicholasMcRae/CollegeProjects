#include <iostream>
#include <string>
#include <fstream>
#include <boost/filesystem.hpp>
#include <SocketLib.hpp>
#include "Socket.h"
#include <WinSock2.h>
#pragma comment (lib, "ws2_32.lib")

using namespace boost::filesystem;

/********************************************************
Purpose: FTP Server - app has the ability to either receive all the files in a directory from the client application,
		 or send all the files in a directory to the client application.
         The command is sent from the client.
Authors: Nick McRae / Milan Sobat
Revision History:
March 8-9th/2014 - Researched how an FTP application worked and how I might go about building one, built a basic prototype that
				   was able to send a single file from the client to server
March 15-16th/2014 - Realized we needed to implement directory functionality, re-installed boost, implemented send-files function 
					 that recursed through and sent all files in directory.
                     Also built trimString function.
March 22-23rd/2014 - Built socket library and got it functional with the server
March 29-30th/2014 - Implemented socket library logic on server
*********************************************************/

/************************

  More detailed comments on code functionality exist on client, which has a similar structure to the server

  *************************/


/*
Purpose: Trim the files found in the directory to send across the network so the file can be recreated on the client
Input: Directory string
Output: Filename - directory
*/
std::string trimString (std::string input)
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
void send_files(const path & dir_path, Socket& socket)            
{ 
  std::string fileName;
  char* command = "";

  if (!exists( dir_path)) return;

  directory_iterator end_itr; 
  for (directory_iterator itr( dir_path ); itr != end_itr; ++itr)
  { 
	  if (is_directory(itr->status()))
      {   
		  send_files( itr->path(), socket );
      }
	  else if(is_regular_file(itr->path()))
	  {  
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

int main()
{
	std::cout << "Server opened.." << std::endl;
	
	Socket socketObject("127.0.0.1", 27022, 27021, true);	

	char* fileName = "";
	char* command = "";		
	char* mfcc = "";	
	long FileSize = 0;
	long SizeCheck = 0;

	socketObject.acceptConnection();	

	int i = 0;

	//implemented this bool logic while testing application, fleshed out FTP server would need more work
	while (i < 1)
	{	
		++i;
		command = (char*)malloc(sizeof(char)*32);
		int bytesRecv = socketObject.receiveData('c', command, 32);

		if(_stricmp(command,"mput") == 0)
		{	
			free(command);

			while( true ) 
			{	
				 SizeCheck = 0;
				 FileSize = 0;
				 
				 fileName = (char*)malloc(sizeof(char)*1024);
				 bytesRecv = socketObject.receiveData('c', fileName, 1024);
				
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
				FILE *fp;
				if( (fp = fopen(fileName, "wb")) == NULL)
				{
					std::cout << "fopen has caused an error" << std::endl;
				}
				
				while(SizeCheck < FileSize){
					int Received = socketObject.receiveData('f', mfcc, FileSize);

					if(Received != SOCKET_ERROR)
					{	int written = fwrite(mfcc, 1, FileSize, fp);
						SizeCheck += Received;
						fflush(fp);
					}	
				}//while transfer	
			}//while true
		}//end mput if
		else if(_stricmp(command,"mget") == 0)
		{
			fileName = (char*)malloc(sizeof(char)*1024);
			int bytes = socketObject.receiveData('c', fileName, 1024);
			
			const path targetDir(fileName); 
			
			send_files( targetDir, socketObject );
						
			strcpy(command, "goodbye");
			bytes = socketObject.sendData( 'c', command, strlen(command)+1);

		}
		else if(_stricmp(command,"quit") == 0)
		{
			break;
		}
	}

	std::cout << "Program Exiting..." << std::endl;

	return 0;
}