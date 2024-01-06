package db_Utils;

public class GDBP_Packet {

    public static GDBP_Packet createPacket(String com) throws PacketCreationException {
        return new GDBP_Packet(com);
    }

    public static GDBP_Packet createPacket(String com, int first) throws PacketCreationException{
        return new GDBP_Packet(com, first);
    }
    public static GDBP_Packet createPacket(String com, int first, int second) throws PacketCreationException{
        return new GDBP_Packet(com, first, second);
    }

    private GDBP_Packet() {

    }

    private GDBP_Packet(String com) throws PacketCreationException {
        if(!GDBP_OperationsMap.getMap().containsKey(com))
            throw new PacketCreationException("Error: Invalid communicat");
        var wrapper = GDBP_OperationsMap.getMap().get(com);
        if(!GDBP_OperationsMap.getNoArgsOps().contains(wrapper))
            throw new PacketCreationException("Error: Invalid arguments. Check if you've provided enough " +
                    "arguments for your communicat");
        this.com = com;
    }



    private GDBP_Packet(String com, int first) throws PacketCreationException {
        if(!GDBP_OperationsMap.getMap().containsKey(com))
            throw new PacketCreationException("Error: Invalid communicat");
        var wrapper = GDBP_OperationsMap.getMap().get(com);
        if(!GDBP_OperationsMap.getOneArgOps().contains(wrapper))
            throw new PacketCreationException("Error: Invalid arguments. Check if you've provided enough arguments for your communicat");
        this.com = com;
        this.val1 = first;
    }

    private GDBP_Packet(String com, int first, int second) throws PacketCreationException {
        if(!GDBP_OperationsMap.getMap().containsKey(com))
            throw new PacketCreationException("Error: Invalid communicat");
        var wrapper = GDBP_OperationsMap.getMap().get(com);
        if(!GDBP_OperationsMap.getTwoArgOps().contains(wrapper))
            throw new PacketCreationException("Invalid arguments. Check if you've provided enough arguments for your communicat");
        this.com = com;
        this.val1 = first;
        this.val2 = second;
    }

    private String com;
    private int val1;
    private int val2;
}
