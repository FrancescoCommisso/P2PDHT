import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class Client extends UDPClient {

    private int clientID;
    private ArrayList<String> directoryServerIPs;

    Client(int id, String IPaddress) throws UnknownHostException {
        super(InetAddress.getByName(IPaddress));
        this.clientID = id;
        directoryServerIPs = new ArrayList<>();
        directoryServerIPs.add(Constants.SERVER_1_IP);
    }

    void init() throws IOException {

        while (directoryServerIPs.size() < 4) {
            String ds = directoryServerIPs.get(directoryServerIPs.size() - 1);
            DatagramPacket receivePacket = sendUDPMessage("", ds, Constants.DIRECTORY_SERVER_UDP_PORT, Constants.INIT);
            String response = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            directoryServerIPs.add(response);
        }

        System.out.println("Client: " + clientID + " initialized with Server IDs:");
        for (String s : directoryServerIPs) {
            System.out.println(s);
        }
    }

    private int hashContentName(String contentName) {
        //sum up characters
        int sum = 0;
        for (int i = 0; i < contentName.length(); i++) {
            sum += contentName.charAt(i);
        }
        // returns index of server
        return (sum % 4);
    }

    void informAndUpdate(String contentName) throws IOException {
        int serverIndex = hashContentName(contentName);
        sendUDPMessage(contentName, directoryServerIPs.get(serverIndex), Constants.DIRECTORY_SERVER_UDP_PORT, Constants.INFORM_AND_UPDATE);
    }

}
