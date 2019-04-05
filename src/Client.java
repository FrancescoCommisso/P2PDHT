import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class Client extends UDPClient {

    private int clientID;
    private ArrayList<String> directoryServerIPs;
    private File imageDirectory;

    Client(int id, String IPaddress) throws UnknownHostException {
        super(InetAddress.getByName(IPaddress));
        this.clientID = id;
        directoryServerIPs = new ArrayList<>();
        directoryServerIPs.add(Constants.SERVER_1_IP);
        String imagesPath = "/Users/Francesco/Desktop/P2PDHT/Client_" + clientID + "_Images";
        imageDirectory = new File(imagesPath);
        imageDirectory.mkdirs();
    }

    void init() throws IOException {

        while (directoryServerIPs.size() < 4) {
            String ds = directoryServerIPs.get(directoryServerIPs.size() - 1);
            DatagramPacket receivePacket = sendUDPMessage("", ds, Constants.DIRECTORY_SERVER_UDP_PORT, Constants.INIT);
            String response = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            directoryServerIPs.add(response);
        }
        System.out.println();
        System.out.println("Client: " + clientID + " initialized with Server IPs:");
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

    String queryForContent(String contentName) throws IOException {
        int serverIndex = hashContentName(contentName);
        DatagramPacket receivePacket = sendUDPMessage(contentName, directoryServerIPs.get(serverIndex), Constants.DIRECTORY_SERVER_UDP_PORT, Constants.QUERY_FOR_CONTENT);
        String result = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        System.out.println("query for content returned: " + result);
        return result;
    }

    void exit() throws IOException {
        for (String ds : directoryServerIPs) {
            sendUDPMessage("", ds, Constants.DIRECTORY_SERVER_UDP_PORT, Constants.EXIT);
        }
        System.out.println("Client: " + clientID + " has exited the network");
        imageDirectory.delete();

    }
}


