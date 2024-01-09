public class Test1 {
    public static void main(String[] args) {
        DatabaseNode node = new DatabaseNode(9001, "localhost", 1, 1);
        try{
            node.connectTo(9000, "localhost");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
