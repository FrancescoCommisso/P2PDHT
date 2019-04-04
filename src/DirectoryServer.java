import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class DirectoryServer extends TCPServer {

    private InetAddress leftNeighbor;
    private InetAddress rightNeighbor;

    private HashMap<String, String> clientLookup;

    DirectoryServer(String IPAddress, int directoryServerID) throws UnknownHostException {
        super(IPAddress, directoryServerID);
        clientLookup = new HashMap<>();
    }

    void setLeftNeighbor(String leftNeighbor) throws UnknownHostException {
        this.leftNeighbor = InetAddress.getByName(leftNeighbor);
    }

    private InetAddress getRightNeighbor() {
        return rightNeighbor;
    }

    void setRightNeighbor(String rightNeighbor) throws UnknownHostException {
        this.rightNeighbor = InetAddress.getByName(rightNeighbor);
    }

    void printAllRecords() {
        System.out.println();
        System.out.println("**********************************************");
        System.out.println("Directory Server: " + getDirectoryServerID() + " has the following entries:");
        for (HashMap.Entry<String, String> entry : clientLookup.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("    (" + key + ":" + value + ")");
        }
        System.out.println("**********************************************");

    }

    private void createUDPSocket() throws IOException, InterruptedException {
        DatagramSocket serverSocket = new DatagramSocket(Constants.DIRECTORY_SERVER_UDP_PORT, this.getIPAddress());
        System.out.println("DirectoryServer: " + this.getDirectoryServerID() + " creating UDP Socket at: " + serverSocket.getLocalAddress() + ":" + serverSocket.getLocalPort());

        byte[] receiveData = new byte[1024];
        byte[] sendData;
        while (true) {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String message = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            switch (message.substring(0, 1)) {
                case Constants.INIT:
                    sendData = init();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                    break;
                case Constants.INFORM_AND_UPDATE:
                    this.informAndUpdate(receivePacket.getAddress().toString().substring(1), message.substring(1));
                    sendData = "updated".getBytes();
                    DatagramPacket sendPacket1 = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket1);
                    break;
                case Constants.QUERY_FOR_CONTENT:
                    sendData = this.queryForContent(message.substring(1)).getBytes();
                    DatagramPacket sendPacket2 = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket2);
                    break;
                case Constants.EXIT:
                    sendData = this.exit((receivePacket.getAddress().toString().substring(1))).getBytes();
                    DatagramPacket sendPacket3 = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket3);
                    break;
                default:
                    System.out.println("Host #" + this.getDirectoryServerID() + " received a bad message: " + message.substring(1) + " should have received " + Constants.INIT);
            }
        }
    }

    void openUDPSocket() {
        Thread thread1 = new Thread(() -> {

            try {
                this.createUDPSocket();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        });
        thread1.start();
    }

    private byte[] init() {
        return getRightNeighbor().toString().substring(1).getBytes();
    }

    private void informAndUpdate(String clientIp, String contentName) {
        clientLookup.put(contentName, clientIp);
    }

    private String queryForContent(String contentName) {
        return clientLookup.getOrDefault(contentName, "Image Not Found");
    }

    private String exit(String clientIP) {
        Set<String> keys = new HashSet<>();
        for (HashMap.Entry<String, String> entry : clientLookup.entrySet()) {
            if (entry.getValue().equals(clientIP)) {
                keys.add(entry.getKey());
            }
        }
        clientLookup.keySet().removeAll(keys);
        String rc = String.valueOf(keys.size());
        String result = "Directory Server: " + getDirectoryServerID() + " removed " + rc + " records associated with client: " + clientIP;
        return result;
    }


}

