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
        DatabaseNode server = new DatabaseNode(9000, "localhost", 17, 32);
        server.startServer();

    }
    // --- General stuff ---
    private int mPort;
    private String mIp;
    private int mKey;
    private int mValue;

    public DatabaseNode(){}

    public DatabaseNode(int port, String ip, int key, int val) {
        mPort = port;
        mIp = ip;
        mKey = key;
        mValue = val;
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
            String initialLine = inputLine;
            {
                System.out.println("Received: " + inputLine);
                writer.write(S_Initial_Response);
                writer.newLine();
                writer.flush();
                while(!((inputLine = reader.readLine()).equals("VOID_DONE")) &&
                        !((inputLine).equals("INT_DONE")) &&
                        !((inputLine).equals("PAIR_DONE")))
                {
                    if(!inputLine.isEmpty())
                    {
                        System.out.println("Received: " + inputLine);
                        setCurrPacket(makePacket(inputLine));
                        if(initialLine.equals("CON_REQ_CLI"))
                            processClientRequest(reader,writer);
                        else if(initialLine.equals("CON_REQ_NOD"))
                            processNodeRequest(reader, writer, currPacket);
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

    private void processNodeRequest(BufferedReader reader, BufferedWriter writer, GDBP_Packet packet){
        try{
            currPacket = packet;
                switch(Process(currPacket)){
                    case 4:
                        if(mKey == currPacket.getVal1())
                            writer.write("DONE " + mValue);
                        else
                            writer.write(askOtherNodes(reader, writer, currPacket));
                        writer.newLine();
                        writer.flush();
                }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private void processClientRequest(BufferedReader reader, BufferedWriter writer){
        try{
            switch(Process(currPacket)){
                case 4:
                    if(mKey == currPacket.getVal1())
                        writer.write("Value at: " + mKey + " : " + mValue);
                    else
                        writer.write(askOtherNodes(reader, writer, currPacket));
                    writer.newLine();
                    writer.flush();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private String askOtherNodes(BufferedReader reader, BufferedWriter writer, GDBP_Packet packet){
        try{
            writer.write(packet.toString());
            writer.newLine();
            writer.flush();
            String message;
            while(!((message = reader.readLine()).equals("VOID_DONE")) &&
                  !((message).equals("INT_DONE")) &&
                  !((message).equals("PAIR_DONE"))
            ){
                if(!(message.isEmpty()))
                    System.out.println(message);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return "temp";
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
        String input;
        while(!((input = reader.readLine())).equals("KILL")){
            if(!(input.isEmpty()))
                try{
                    Thread.sleep(500);
                } catch (Exception e){
                    e.printStackTrace();
                }
        }

    }

    static final String C_Initial = "CON_REQ_NOD";
    static final String S_Initial_Response = "CON_ACC";



}
