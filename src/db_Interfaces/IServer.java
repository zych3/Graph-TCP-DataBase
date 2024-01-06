package db_Interfaces;

import java.net.Socket;

public interface IServer {
     void startServer();
     void stopServer();
     void handleConnection(Socket clientSocket);
}
