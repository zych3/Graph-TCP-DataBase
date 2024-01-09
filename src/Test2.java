public class Test2 {
    public static void main(String[] args) {
        DatabaseNode node = new DatabaseNode(9002, "localhost", 2, 2);
        try{
            node.connectTo(9000, "localhost");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
