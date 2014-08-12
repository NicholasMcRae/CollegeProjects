#if !defined(SOCKET_H)
#define SOCKET_H
/*
#include <WinSock2.h>
#pragma comment (lib, "ws2_32.lib")

#if defined(_DEBUG) && defined(_DLL)
#pragma comment (lib,"Socket-mt-gd.lib")
#elif defined(_DEBUG) && !defined(_DLL)
#pragma comment (lib,"Socket-mt-sgd.lib")
#elif !defined(_DEBUG) && defined(_DLL)
#pragma comment (lib,"Socket-mt.lib")
#elif !defined(_DEBUG) && !defined(_DLL)
#pragma comment (lib,"Socket-mt-s.lib")
#endif

class Socket {

private:
	int exitCode;
	WSADATA wsaData;
	SOCKET fSocket;
	SOCKET cSocket;
	sockaddr_in service;
	std::string addr;
	USHORT port;

	bool initializeWSA();
	bool initializeFSocket();
	bool initializeCSocket();
	bool bind();
	bool listen();	
	bool connect();	

	//weird socket constructor that I'm not sure is needed
	//the main difference between this socket class and my socket class is that I have two different sockets so I need to account for that

public:
	bool close();
	Socket* acceptConnection();
	Socket() {}
	
	//this constructor should do for one socket, we can just pass in the data that we need
	//what would be the best way of dealing with this?
	//we could initialize both sockets within the constructor and change the bind, listen, and connect functions to deal with both sockets
	Socket(std::string addr, USHORT port, bool actAsServer = false) : addr( addr ), port( port )
	{
		initializeWSA();
		initializeFSocket();

		service.sin_family = AF_INET;
		service.sin_addr.s_addr = inet_addr(addr.c_str());
		service.sin_port = htons(port);

		if( actAsServer )
		{
			bind();
			listen();
		}
		else
			connect();
	}
};
*/

#endif