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
    private int directoryServerID;

    public TCPServer(String IPAddress, int directoryServerID) throws UnknownHostException {
        this.IPAddress =InetAddress.getByName(IPAddress);
        this.directoryServerID = directoryServerID;
    }

    public InetAddress getIPAddress() {
        return IPAddress;
    }

    public int getDirectoryServerID() {
        return directoryServerID;
    }

    private void createTCPSocket() throws IOException {
        String clientMessage;
        String response;
        ServerSocket welcomeSocket = new ServerSocket(Constants.DIRECTORY_SERVER_TCP_PORT,0,IPAddress);
        System.out.println("DirectoryServer: "+ directoryServerID + " creating TCP Socket at: "+ welcomeSocket.getInetAddress().toString() + ":" + welcomeSocket.getLocalPort());

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientMessage = inFromClient.readLine();

            System.out.println("From Client: " + clientMessage);
            response = "Got your message boy!" + '\n';
            outToClient.writeBytes(response);
        }
    }

    void openTCPSocket(){

        Thread thread1 = new Thread(() -> {
            try {
                this.createTCPSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
    }

    void sendTCPMessage(String data, InetAddress directoryServerIP) throws IOException {
        String response;

        Socket clientSocket = new Socket(directoryServerIP, Constants.DIRECTORY_SERVER_TCP_PORT);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.writeBytes(data + '\n');
        response = inFromServer.readLine();
        System.out.println("FROM SERVER: " + response);
        clientSocket.close();
    }

    void sendTCPMessage(byte[] byteArray, InetAddress directoryServerIP) throws IOException {
        String response;

        Socket clientSocket = new Socket(directoryServerIP, Constants.DIRECTORY_SERVER_TCP_PORT);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        outToServer.write(byteArray);
        response = inFromServer.readLine();
        System.out.println("FROM SERVER: " + response);
        clientSocket.close();
    }

}
