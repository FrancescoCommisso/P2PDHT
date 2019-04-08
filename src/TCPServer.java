import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPServer {

    private InetAddress IPAddress;
    private int serverID;
    private int port;

    TCPServer(String IPAddress, int serverID, int port) throws UnknownHostException {
        this.IPAddress = InetAddress.getByName(IPAddress);
        this.serverID = serverID;
        this.port = port;
    }

    InetAddress getIPAddress() {
        return IPAddress;
    }

    int getServerID() {
        return serverID;
    }

    int getPort() {
        return port;
    }

    protected void createTCPSocket() throws IOException {
        String clientMessage;
        String response;
        ServerSocket welcomeSocket = new ServerSocket(port, 0, IPAddress);
        System.out.println("Server: " + serverID + " creating TCP Socket at: " + welcomeSocket.getInetAddress().toString() + ":" + welcomeSocket.getLocalPort());

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientMessage = inFromClient.readLine();

            System.out.println("From Client: " + clientMessage);
            response = "Got your message!" + '\n';
            outToClient.writeBytes(response);
        }
    }

    //OVERRIDE ME IN SUBCLASSES
//    protected BufferedImage handleClientMessage(String clientMessage) throws IOException {
//return new BufferedImage();
//    }

    void openTCPSocket() {

        Thread thread1 = new Thread(() -> {
            try {
                this.createTCPSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
    }

    void sendTCPMessage(String data, String dstIP, int dstPort) throws IOException {
        String response;
        Socket clientSocket = new Socket(InetAddress.getByName(dstIP), dstPort, IPAddress, Constants.PEER_TCP_OUT_PORT);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.writeBytes(data + '\n');
        response = inFromServer.readLine();
//        System.out.println("client: " + serverID + " received: " + response + " from: " + clientSocket.getRemoteSocketAddress().toString());
        clientSocket.close();
    }
//
}
