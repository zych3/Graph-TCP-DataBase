public class Main {
    public static void main(String[] args) {
        DatabaseNode client = new DatabaseNode(9001, "localhost", 18, 42);
        try{
            client.connectTo(9000, "localhost");
        } catch(Exception e){
            e.printStackTrace();
        }
        /*DatabaseClient c = new DatabaseClient(9000, "localhost");*/
    }
}
