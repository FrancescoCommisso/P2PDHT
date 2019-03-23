import java.io.IOException;
import java.net.*;

public class Client {

    private String directoryServerIPs[];
    Client(){}

    protected String[] getDirectoryServerIPs(){
        return this.directoryServerIPs;
    }

    public void setDirectoryServerIPs(String[] directoryServerIPs) {
        this.directoryServerIPs = directoryServerIPs;
    }

    void init() throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress directoryServer1IP = InetAddress.getByName(Constants.SERVER_1_IP);

        byte[] receiveData = new byte[1024];
        byte[] sendData = Constants.INIT.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, directoryServer1IP,Constants.DIRECTORY_SERVER_UDP_PORT);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String response = new String(receivePacket.getData(),receivePacket.getOffset(), receivePacket.getLength());
        System.out.println("FROM SERVER: " + response);

        clientSocket.close();
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
