import db_Interfaces.IClient;

import db_Utils.GDBP_OperationsMap;
import db_Utils.GDBP_Packet;
import db_Utils.PacketCreationException;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DatabaseNode implements IClient {
    public static void main(String[] args) {

        Thread serverThread = new Thread(new Runnable(){
            @Override
            public void run() {
                DatabaseNode test = new DatabaseNode(9000, "localhost");
                test.startServer();
            }
        });
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseNode c1 = new DatabaseNode(9001, "localhost");
                try{
                    c1.connectTo(9000, "localhost");
                    //System.out.println("connection finished");

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Thread otherClient = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseNode c2 = new DatabaseNode(9002, "localhost");
                try{
                    c2.connectTo(9000, "localhost");
                    //System.out.println("connection finished");

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
        clientThread.start();
        otherClient.start();


    }
    // --- General stuff ---
    private int port;
    private String ip;
    private int key;
    private int value;

    public DatabaseNode(){}
    public DatabaseNode(int port, String ip) {
        port = port;
        ip = ip;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    GDBP_Packet currPacket;



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
            } else if(inputLine.equals("CON_REQ_NOD"))
            {
                System.out.println("New node connected: " + clientSocket.getPort());
            }
            while(!(inputLine = reader.readLine()).equals("KILL"))
            {
                if(!inputLine.isEmpty()){
                    setCurrPacket(makePacket(inputLine));
                    switch(Process(currPacket)){
                        case 1 << 3:
                            value = currPacket.getVal2();
                            writer.write("Value changed :D");
                            writer.newLine();
                            writer.flush();
                            System.out.println("Value changed");
                            break;
                        case 1 << 4:
                            System.out.println("Value here is " + value);
                            writer.write("Value: " + value);
                            writer.newLine();
                            writer.flush();
                            break;


                    }
                }

            }

            reader.close();
            clientSocket.close();
            System.out.println("client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    GDBP_Packet makePacket(String input){
        GDBP_Packet packet = null;
        String[] words = input.split(" ");
        try {
            packet = switch (words.length) {
                case 1 -> GDBP_Packet.createPacket(words[0]);
                case 2 -> GDBP_Packet.createPacket(
                        words[0], Integer.parseInt(words[1])
                );
                case 3 -> GDBP_Packet.createPacket(
                        words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2])
                );
                default -> packet;
            };
        } catch (Exception e){
            e.printStackTrace();

        }
        return packet;
    }
    void setCurrPacket(GDBP_Packet p){
        currPacket = p;
        isProcessing = true;
    }

    private int Process(GDBP_Packet packet){
        isProcessing = false;
        return GDBP_OperationsMap.getMap().get(packet.getCom());
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
        try{
            Thread.sleep(2000);

        } catch(Exception e){
            System.out.println("error");
        }
        /*String response = reader.readLine();
        System.out.println(response);*/
        System.out.println("connection finished");
    }

    static final String C_Initial = "CON_REQ_NOD";
    static final String S_Initial_Response = "CON_ACC";



}
