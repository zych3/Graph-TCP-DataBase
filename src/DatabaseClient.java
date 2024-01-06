import db_Interfaces.IClient;
import db_Utils.GDBP_Packet;
import db_Utils.PacketCreationException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class DatabaseClient implements IClient {

    public static void main(String[] args) {
        DatabaseClient client = new DatabaseClient(9000, "localhost");
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

        String response = "CONN_ACC"; //debug as well, will be a reply from the server
        if(response == "CONN_ACC")
            System.out.println("Connection established");
        else
            return;
        System.out.println("Type \"exit\" to close connection");

        while(true)
        {
            String input = getUserInput();
            sendPacket(handleUserInput(input), writer);
            if(input.equals("exit"))
                return;
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
