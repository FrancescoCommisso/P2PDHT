import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class Client extends UDPClient {

    private int clientID;
    private ArrayList<String> directoryServerIPs;
    private String imagesPath;
    private File imageDirectory;
    private PeerTCPServer tcpServer;

    Client(int id, String IPaddress) throws UnknownHostException {
        super(InetAddress.getByName(IPaddress));
        this.clientID = id;
        directoryServerIPs = new ArrayList<>();
        directoryServerIPs.add(Constants.SERVER_1_IP);
        imagesPath = "/Users/Francesco/Desktop/P2PDHT/Client_" + clientID + "_Images";
        imageDirectory = new File(imagesPath);
        imageDirectory.mkdirs();
        tcpServer = new PeerTCPServer(IPaddress, clientID, Constants.PEER_TCP_IN_PORT, this);
        tcpServer.openTCPSocket();
    }

    String getImagesPath() {
        return imagesPath;
    }

    void testTCP() throws IOException {
        String httpMessage = HTTPGenerator.createHTTPRequest("a.jpeg", Constants.CLIENT_IP_1);
        this.tcpServer.sendTCPMessage(httpMessage, queryForContent("a"), Constants.PEER_TCP_IN_PORT);
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

    void fileTransfer(String contentName) throws IOException {
        String peerIP = queryForContent(contentName);
        String httpMessage = HTTPGenerator.createHTTPRequest(contentName, peerIP);
        this.tcpServer.sendTCPMessage(httpMessage, peerIP, Constants.PEER_TCP_IN_PORT);
    }

    void exit() throws IOException {
        for (String ds : directoryServerIPs) {
            sendUDPMessage("", ds, Constants.DIRECTORY_SERVER_UDP_PORT, Constants.EXIT);
        }
        System.out.println("Client: " + clientID + " has exited the network");
        imageDirectory.delete();
    }

    BufferedImage handleFileTransgerRequest(String clientRequest) throws IOException {
        String fileName = clientRequest.substring(4, clientRequest.length() - 9);
        System.out.println("about to read: " + imagesPath + "/" + fileName);
        return ImageIO.read(new File(imagesPath + "/" + fileName));
    }

}


