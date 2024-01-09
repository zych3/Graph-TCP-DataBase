import db_Interfaces.IClient;
import db_Utils.ClientInputTranslator;
import db_Utils.ClientInputValidator;
import db_Utils.GDBP_Packet;
import db_Utils.PacketCreationException;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class DatabaseClient implements IClient {

    public static void main(String[] args) { //java dbClient -gatevay localhost:2137 -operation set-value 17
        // args = {-gatway, localhost:2137, operation, set-value, 17}
        //input is -gateway <ip>:<port> -operation [packet]

        //Create mappings
        HashMap<String, String> startArgsMap = new HashMap<>();
        startArgsMap.put(args[0], args[1]);
        StringBuilder sb = new StringBuilder();
        for(int i = 3; i < args.length; i++ ){
            if(i == 3)
                sb.append(
                        ClientInputTranslator.translateToGDBP(args[i])
                ).append(" ");
            else
                sb.append(args[i]).append(" ");
        }
        startArgsMap.put(args[2], sb.toString());

        //Create a client, based on mappings
        String[] params = startArgsMap.get("-gateway").split(":"); // get IP and port
        DatabaseClient client = new DatabaseClient(
                Integer.parseInt(params[1]), params[0], startArgsMap.get("-operation")
        );

    }

    public DatabaseClient(){}
    public DatabaseClient(int port, String ip)
    {
        try {
            connectTo(port, ip);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public DatabaseClient(int port, String ip, String firstOp)
    {
        try {
            connectTo(port, ip, firstOp);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    static final String initial = "CON_REQ_CLI";



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

        writer.write(initial);
        writer.newLine();
        writer.flush();
        try{
            Thread.sleep(1000);
        } catch(Exception e){
            System.out.println("error");
        }
        String response = reader.readLine(); //debug, will be caught
        System.out.println("Type \"exit\" to close connection");
        String input;
        while(!(input = getUserInput()).equals("exit"))
        {
            sendPacket(handleUserInput(input), writer);
            response = reader.readLine();
            System.out.println(response);
        }


    }

    public void connectTo(int port, String ip, String firstOp) throws IOException, PacketCreationException {
        Socket socket = new Socket(ip, port);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()
                ));
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()
                ));
        //---debug---

        writer.write(initial);
        writer.newLine();
        writer.flush();
        try{
            Thread.sleep(1000);
        } catch(Exception e){
            System.out.println("error");
        }
        String response;
        response = reader.readLine();
        validateOutput(response);
        sendPacket(handleUserInput(firstOp), writer);
        response = reader.readLine();
        validateOutput(response);

        System.out.println("Type \"exit\" to close connection");
        String input;
        while(!(input = getUserInput()).equals("exit"))
        {
            sendPacket(handleUserInput(input), writer);
            response = reader.readLine();
            validateOutput(response);
        }


    }

    void validateOutput(String s){
        String[] temp = s.split(" ");
        if(!(ClientInputTranslator.getTranslator().containsValue(temp[0])))
            System.out.println(s);
    }

    private void handleFirstConnection(BufferedReader reader, BufferedWriter writer, GDBP_Packet packet)
    {
        try{
            sendPacket(packet, writer);
            String res = reader.readLine();
            System.out.println(res);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getUserInput(){
        java.util.Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public GDBP_Packet handleUserInput(String input) {
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

    public void sendPacket(GDBP_Packet packet, BufferedWriter writer)
    {
        String message =
                packet.getCom() +
                (packet.getVal1() != 0 ? " " + packet.getVal1() : "") +
                (packet.getVal2() != 0 ? " " + packet.getVal2() : "");

        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
            System.out.println("[DEBUG] Sent: " + message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
