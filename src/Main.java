import db_Utils.GDBP_OperationsMap;
import db_Utils.GDBP_Packet;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
       DatabaseClient client = new DatabaseClient();
        System.out.println(client.getUserInput());
    }
}
