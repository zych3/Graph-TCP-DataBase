public class Test3 {
    public static void main(String[] args) {
        DatabaseNode node = new DatabaseNode(9003, "localhost", 3, 3);
        try{
            node.connectTo(9000, "localhost");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
