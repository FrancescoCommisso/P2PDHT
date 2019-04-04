import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class DirectoryServer extends TCPServer {

    private InetAddress leftNeighbor;
    private InetAddress rightNeighbor;

    private HashMap<String, String> clientLookup;

    DirectoryServer(String IPAddress, int directoryServerID) throws UnknownHostException {
        super(IPAddress, directoryServerID);
        clientLookup = new HashMap<>();
    }

    public InetAddress getLeftNeighbor() {
        return leftNeighbor;
    }

    void setLeftNeighbor(String leftNeighbor) throws UnknownHostException {
        this.leftNeighbor = InetAddress.getByName(leftNeighbor);
    }

    public InetAddress getRightNeighbor() {
        return rightNeighbor;
    }

    void setRightNeighbor(String rightNeighbor) throws UnknownHostException {
        this.rightNeighbor = InetAddress.getByName(rightNeighbor);
    }

    void printAllRecords() {
        System.out.println("Directory Server: " + getDirectoryServerID() + " has the following entries:");
        for (HashMap.Entry<String, String> entry : clientLookup.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("(" + key + ":" + value + ")");
            // do stuff
        }
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
                    this.queryForContent();
                    break;
                case Constants.EXIT:
                    this.exit();
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

    private byte[] ListToByteArray(List<String> list) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : list) {
            out.writeUTF(element);
        }
        return baos.toByteArray();
    }

    private List<String> byteArrayToList(byte[] byteArray) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        DataInputStream in = new DataInputStream(bais);
        ArrayList<String> list = new ArrayList<>();
        while (in.available() > 0) {
            String element = in.readUTF();
            list.add(element);
        }
        return list;
    }

    private void getAllDirectoryServers(ArrayList<String> list) throws IOException, InterruptedException {
        list.add(this.getIPAddress().toString().substring(1));
        sendTCPMessage(ListToByteArray(list), leftNeighbor);
    }

    private void getAllDirectoryServers(String servers) throws IOException, InterruptedException {
        servers += this.getIPAddress().toString().substring(1) + "/n";
        sendTCPMessage(servers, leftNeighbor);
    }

    private byte[] init() {
        return getRightNeighbor().toString().substring(1).getBytes();
    }

    private void informAndUpdate(String clientIp, String contentName) {
//        System.out.println("Server: " + getDirectoryServerID() + " Received: " + contentName + " from client: " + clientIp);
        clientLookup.put(contentName, clientIp);
    }

    private void queryForContent() {
    }

    private void exit() {
    }


}

