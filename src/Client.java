import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Client {

    private int clientID;
    private InetAddress IPaddress;
    private ArrayList<String> directoryServerIPs;
    Client(int id,String IPaddress) throws UnknownHostException {
        this.IPaddress = InetAddress.getByName(IPaddress);
        this.clientID=id;
        directoryServerIPs = new ArrayList<>();
        directoryServerIPs.add(Constants.SERVER_1_IP);
    }

    protected ArrayList<String> getDirectoryServerIPs(){
        return this.directoryServerIPs;
    }

    void init() throws IOException {

        while(directoryServerIPs.size()<4){
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress nextServer = InetAddress.getByName(directoryServerIPs.get(directoryServerIPs.size()-1));
            byte[] receiveData = new byte[1024];
            byte[] sendData = Constants.INIT.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, nextServer ,Constants.DIRECTORY_SERVER_UDP_PORT);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(),receivePacket.getOffset(), receivePacket.getLength());
            directoryServerIPs.add(response);

            clientSocket.close();
        }

        System.out.println("Client: "+clientID + " initialized with Server IDs:");
        for(String s:directoryServerIPs){
            System.out.println(s);
        }
    }



    public int hashContentName(String contentName){
        //sum up characters
        int sum = 0;
        for(int i = 0; i<contentName.length();i++){
            sum+= contentName.charAt(i);
        }

        // 4 Servers with ID's 1,2,3,4.
        // Have to add 1 because there is no serverID = 0
        return (sum % 4) + 1;
    }



    void informAndUpdate(String imgPath){
//        File imgFile = null;
//        BufferedImage img = null;
//
//        try {
//           imgFile = new File(imgPath);
//           img = ImageIO.read(imgFile);
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//
//        int serverID = hashContentName(imgFile.getName());
    }



    public static void main(String args[]) throws IOException {

//        Client c = new Client("000.000.000.000");
//        c.sendDatagram();
////        int s = c.hashContentName("PhotoOfPerson");
////        System.out.print(s);


    }

}
