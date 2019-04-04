import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

    private int clientID;
    private InetAddress IPaddress;
    private ArrayList<String> directoryServerIPs;

    Client(int id, String IPaddress) throws UnknownHostException {
        this.IPaddress = InetAddress.getByName(IPaddress);
        this.clientID = id;
        directoryServerIPs = new ArrayList<>();
        directoryServerIPs.add(Constants.SERVER_1_IP);
    }

    public static void main(String args[]) throws IOException {

//        Client c = new Client("000.000.000.000");
//        c.sendDatagram();
////        int s = c.hashContentName("PhotoOfPerson");
////        System.out.print(s);


    }

    protected ArrayList<String> getDirectoryServerIPs() {
        return this.directoryServerIPs;
    }

    void init() throws IOException {

        while (directoryServerIPs.size() < 4) {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress nextServer = InetAddress.getByName(directoryServerIPs.get(directoryServerIPs.size() - 1));
            byte[] receiveData = new byte[1024];
            byte[] sendData = Constants.INIT.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, nextServer, Constants.DIRECTORY_SERVER_UDP_PORT);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            directoryServerIPs.add(response);

            clientSocket.close();
        }

        System.out.println("Client: " + clientID + " initialized with Server IDs:");
        for (String s : directoryServerIPs) {
            System.out.println(s);
        }
    }

    private void sendUDPMessage(String message, String ip, int port, String messageType) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket(Constants.CLIENT_UDP_PORT, this.IPaddress);
        InetAddress server = InetAddress.getByName(ip);
        byte[] receiveData = new byte[1024];
        byte[] sendData;

        String fullMessage = messageType + message;
        sendData = fullMessage.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, server, port);
        System.out.println();
        clientSocket.send(sendPacket);
//        System.out.println("PACKET SENT FROM CLIENT HAS IP: " + sendPacket.getSocketAddress().toString());

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
//        System.out.println("PACKET RECIEVED BY CLIENT HAS IP: " + receivePacket.getSocketAddress().toString());


//        String response = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
//        System.out.println("Client: " + clientID + "Received response: " + response + " from server: " + ip);

        clientSocket.close();

    }

    public int hashContentName(String contentName) {
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
