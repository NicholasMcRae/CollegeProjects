#include <iostream>
using namespace std;
#include "SocketLib.hpp"


/*
Purpose: Initialise WSA for the application
Input: None
Output: Initialized WSA
*/
bool Socket::initializeWSA()
{	
	exitCode = EXIT_SUCCESS;
	int iResult = WSAStartup( MAKEWORD(2,2), &wsaData );
	if( iResult != NO_ERROR ) {
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	return true;
}

/*
Purpose: Initialise file transfer socket
Input: None
Output: Initialized file transfer socket, returns a bool
*/
bool Socket::initializeFSocket()
{	
	fSocket = socket( AF_INET, SOCK_STREAM, IPPROTO_TCP );
	if( fSocket == INVALID_SOCKET )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	return true;
}

/*
Purpose: Initialise command socket
Input: None
Output: Initialized command socket, returns a bool
*/
bool Socket::initializeCSocket()
{	
	cSocket = socket( AF_INET, SOCK_STREAM, IPPROTO_TCP );
	if( cSocket == INVALID_SOCKET )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	return true;
}

/*
Purpose: Clean-up socket stuff
Input: Error or object destruction
Output: closes sockets and calls WSACleanup, returns a bool
*/
bool Socket::close() 
{	
	closesocket( fSocket );
	closesocket( cSocket );
	WSACleanup();
	return true;
}

/*
Purpose: Bind the server sockets
Input: Object construction
Output: Bound sockets
*/
bool Socket::bind()
{	
	if( ::bind( cSocket, (SOCKADDR*)&cService, sizeof(cService) ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	
	if( ::bind( fSocket, (SOCKADDR*)&fService, sizeof(fService) ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	
	return true;
}

/*
Purpose: Listen for connections
Input: Object construction
Output: Listening sockets
*/
bool Socket::listen()
{
	if( ::listen( fSocket, 1 ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	if( ::listen( cSocket, 1 ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	return true;
}

/*
Purpose: Accept connections from clients
Input: None
Output: Accepted connections
*/
void Socket::acceptConnection() {

	cAccepted = SOCKET_ERROR;
	while( cAccepted == SOCKET_ERROR )
	{		
		cAccepted = accept( cSocket, NULL, NULL );	
	}
	
	fAccepted = SOCKET_ERROR;
	while( fAccepted == SOCKET_ERROR )
	{
		fAccepted = accept( fSocket, NULL, NULL );
	}
}

/*
Purpose: Connect to sockets on the server
Input: Object construction
Output: Connected sockets
*/
bool Socket::connect()
{
	if( ::connect( cSocket, (SOCKADDR*)&cService, sizeof(cService) ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	
	if( ::connect( fSocket, (SOCKADDR*)&fService, sizeof(fService) ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	return true;
}

/*
Purpose: Send data over sockets
Input: file or command socket type, data, data size
Output: sent data, returns bytes sent or -1 for error
*/
int Socket::sendData(char socketType, char* data, int size)
{
	if(socketType != 'f' && socketType != 'c')
	{
		return -1;
	}
	if(socketType == 'f')
	{
		if( fAccepted != NULL)
		{
			int bytes = send( fAccepted, data, size, 0);
			return bytes;
		}
		else
		{
			int bytes = send( fSocket, data, size, 0);
			return bytes;
		}
	}
	else if(socketType == 'c')
	{
		if( cAccepted != NULL)
		{
			int bytes = send( cAccepted, data, size, 0);
			return bytes;
		}
		else
		{
			int bytes = send( cSocket, data, size, 0);
			std::cout << "Cleanup. Last error was: " << WSAGetLastError() << endl;
			return bytes;
		}
	}
	return -1;
}

/*
Purpose: Receive data over sockets
Input: file or command socket type, data, data size
Output: received data, return bytes received or -1 for error
*/
int Socket::receiveData(char socketType, char* data, int size)
{
	if(socketType != 'f' && socketType != 'c')
	{
		return -1;
	}
	if(socketType == 'f')
	{
		if( fAccepted != NULL)
		{
			int bytes = recv( fAccepted, data, size, 0);
			return bytes;
		}
		else
		{
			int bytes = recv( fSocket, data, size, 0);
			return bytes;
		}
	}
	else if(socketType == 'c')
	{	
		if( cAccepted != NULL)
		{
			int bytes = recv( cAccepted, data, size, 0);
			return bytes;
		}
		else
		{
			int bytes = recv( cSocket, data, size, 0);
			return bytes;
		}
    }
	
	return -1;
}