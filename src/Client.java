import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    String parsePath(String path) {
        int lastDash = 0;
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.substring(i, i + 1).equals("/")) {
                lastDash = i + 1;
                break;
            }
        }
        return path.substring(lastDash);
    }

    void informAndUpdate(String path) throws IOException {
        String contentName = parsePath(path);
        int serverIndex = hashContentName(contentName);
        Path srcPath = Paths.get(path);
        Path dstPath = Paths.get(imagesPath + "/" + contentName);
        Files.copy(srcPath, dstPath, StandardCopyOption.REPLACE_EXISTING);
        sendUDPMessage(contentName, directoryServerIPs.get(serverIndex), Constants.DIRECTORY_SERVER_UDP_PORT, Constants.INFORM_AND_UPDATE);
    }

    String queryForContent(String contentName) throws IOException {
        int serverIndex = hashContentName(contentName);
        DatagramPacket receivePacket = sendUDPMessage(contentName, directoryServerIPs.get(serverIndex), Constants.DIRECTORY_SERVER_UDP_PORT, Constants.QUERY_FOR_CONTENT);
        String result = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
//        System.out.println("query for content returned: " + result);
        return result;
    }

    BufferedImage handleFileTransferRequest(String clientRequest) throws IOException {
        String fileName = clientRequest.substring(4, clientRequest.length() - 9);
        return ImageIO.read(new File(imagesPath + "/" + fileName));
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


}


