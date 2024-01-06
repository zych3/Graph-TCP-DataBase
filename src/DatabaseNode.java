import db_Interfaces.IClient;

import db_Utils.PacketCreationException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DatabaseNode implements IClient {
    public static void main(String[] args) {
        DatabaseNode test = new DatabaseNode(9000, "localhost");
        test.startServer();

    }
    // --- Default stuff ---
    private int port;
    private String ip;

    public DatabaseNode(){}
    public DatabaseNode(int port, String ip)
    {
        port = port;
        ip = ip;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // --- Server Side ---
    private boolean isProcessing;
    private boolean isServerActive;
    private boolean clientConnected;
    private ServerSocket serverSocket;

    public void startServer() {
        isServerActive = true;
        Thread connectionListener = new Thread(() -> {
            while (isServerActive) {
                try {
                    Socket clientSocket = serverSocket.accept(); // Listen for incoming connections
                    // Handle the incoming connection in a separate thread
                    Thread clientHandler = new Thread(() -> handleConnection(clientSocket));
                    clientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        connectionListener.start();

        // Perform other operations in the main thread while listening for connections
        while (isServerActive) {
            // Perform other tasks or operations here
            // This code will run concurrently with the connection listener
            try {
                Thread.sleep(1000); // Placeholder for other operations
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void stopServer() {
        isServerActive = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleConnection(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String inputLine = reader.readLine();
            if(inputLine.equals("CON_REQ_CLI")) {
                System.out.println("Received: " + inputLine);
                writer.write(S_Initial_Response);
                writer.newLine();
                writer.flush();
            }
            while(!(inputLine = reader.readLine()).equals("exit"))
            {
                if(!inputLine.isEmpty())
                {
                    System.out.println("Received: " + inputLine);
                }
            }
            reader.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // --- Client side ---

    @Override
    public void connectTo(int port, String ip) throws IOException, PacketCreationException {
        Socket socket = new Socket(ip, port);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()
                ));
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()
                ));
        //---debug---
        writer.write(C_Initial);
        writer.newLine();
        writer.flush();

        String response = "CONN_ACC"; //debug as well, will be a reply from the server
        if(response == "CONN_ACC")
            System.out.println("Connection established");
    }

    static final String C_Initial = "CONN_REQ_NOD";
    static final String S_Initial_Response = "CON_ACC";
}
