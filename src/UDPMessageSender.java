import java.io.IOException;
import java.net.DatagramPacket;

public interface UDPMessageSender {
    DatagramPacket sendUDPMessage(String message, String ip, int port, String messageType) throws IOException;
}
