/*

#include <iostream>
using namespace std;
#include "Socket.h"

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

bool Socket::close() 
{
	closesocket( fSocket );
	closesocket( cSocket );
	WSACleanup();
	return true;
}

bool Socket::bind()
{
	if( ::bind( fSocket, (SOCKADDR*)&service, sizeof(service) ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	if( ::bind( cSocket, (SOCKADDR*)&service, sizeof(service) ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	return true;
}

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
Socket* Socket::acceptConnection() {

	SOCKET fAccepted = SOCKET_ERROR;
	while( fAccepted == SOCKET_ERROR )
		fAccepted = accept( fSocket, NULL, NULL );
	return new Socket( *this, fAccepted );
}
*/
/*

bool Socket::connect()
{
	if( ::connect( fSocket, (SOCKADDR*)&service, sizeof(service) ) == SOCKET_ERROR )
	{
		exitCode = EXIT_FAILURE;
		close();
		return false;
	}
	return true;
}

*/