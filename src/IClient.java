import db_Utils.PacketCreationException;

import java.io.IOException;

public interface IClient {
    void connectTo(int port, String ip) throws IOException, PacketCreationException;
}
