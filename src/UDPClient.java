import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class UDPClient implements UDPMessageSender {
    private InetAddress IPaddress;

    UDPClient(InetAddress IPaddress) {
        this.IPaddress = IPaddress;
    }

    public InetAddress getIPaddress() {
        return IPaddress;
    }

    @Override
    public DatagramPacket sendUDPMessage(String message, String ip, int port, String messageType) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket(Constants.CLIENT_UDP_PORT, this.IPaddress);
        InetAddress server = InetAddress.getByName(ip);
        byte[] receiveData = new byte[1024];
        byte[] sendData;

        String fullMessage = messageType + message;
        sendData = fullMessage.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, server, port);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        clientSocket.close();
        return receivePacket;
    }
}
