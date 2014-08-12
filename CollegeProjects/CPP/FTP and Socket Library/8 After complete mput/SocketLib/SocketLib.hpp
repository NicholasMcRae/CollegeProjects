#if !defined(GUARD_SOCKET_HPP)
#define GUARD_SOCKET_HPP

#if defined(_DEBUG) && !defined(_DLL)
#pragma comment (lib,"SocketLib-mt-s-gd.lib")
#elif defined(_DEBUG) && defined(_DLL)
#pragma comment (lib,"SocketLib-mt-gd.lib")
#elif !defined(_DEBUG) && !defined(_DLL)
#pragma comment (lib,"SocketLib-mt-s.lib")
#elif !defined(_DEBUG) && defined(_DLL)
#pragma comment (lib,"SocketLib-mt.lib")
#endif

#include <WinSock2.h>
#pragma comment (lib, "ws2_32.lib")

class Socket {

private:
	int exitCode;
	WSADATA wsaData;
	SOCKET fSocket;
	SOCKET cSocket;
	SOCKET fAccepted;
	SOCKET cAccepted;
	sockaddr_in fService;
	sockaddr_in cService;
	std::string addr;
	USHORT port;
	USHORT port2;

	bool initializeWSA();
	bool initializeFSocket();
	bool initializeCSocket();
	bool bind();
	bool listen();	
	bool connect();	

public:
	bool close();
	void acceptConnection();
	Socket() {}
	int sendData(char socketType, char* data, int size);
	int receiveData(char socketType, char* data, int size);
	
	Socket(std::string addr, USHORT port, USHORT port2, bool actAsServer = false) : addr( addr ), port( port ), port2( port2)
	{
		cAccepted = NULL;
		fAccepted = NULL;
		
		initializeWSA();
		initializeFSocket();
		initializeCSocket();

		fService.sin_family = AF_INET;
		fService.sin_addr.s_addr = inet_addr(addr.c_str());
		fService.sin_port = htons(port);
		
		cService.sin_family = AF_INET;
		cService.sin_addr.s_addr = inet_addr(addr.c_str());
		cService.sin_port = htons(port2);

		if( actAsServer )
		{
			bind();
			listen();
		}
		else
			connect();
	}

	~Socket() 
	{ 
		close(); 
	}
};

#endif