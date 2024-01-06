import db_Utils.GDBP_Packet;
import db_Utils.PacketCreationException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class DatabaseClient implements IClient{

    public DatabaseClient(){}

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

        String response;
        while(!(response=reader.readLine()).isEmpty()) //basically readline on each iteration
            System.out.println(response);
        System.out.println("Connection terminated");

    }

    public String getUserInput(){
        java.util.Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
