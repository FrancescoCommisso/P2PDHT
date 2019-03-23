import java.io.*;
import java.net.*;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

class DirectoryServer extends TCPServer{

    private InetAddress leftNeighbor;
    private InetAddress rightNeighbor;

    DirectoryServer(String IPAddress, int directoryServerID) throws UnknownHostException {
        super(IPAddress, directoryServerID);
    }

    public void setLeftNeighbor(String leftNeighbor) throws UnknownHostException {
        this.leftNeighbor = InetAddress.getByName(leftNeighbor);
    }

    public void setRightNeighbor(String rightNeighbor) throws UnknownHostException {
        this.rightNeighbor = InetAddress.getByName(rightNeighbor);
    }

    public InetAddress getLeftNeighbor() {
        return leftNeighbor;
    }

    public InetAddress getRightNeighbor() {
        return rightNeighbor;
    }

    private void createUDPSocket() throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(Constants.DIRECTORY_SERVER_UDP_PORT,this.getIPAddress());
        System.out.println("DirectoryServer: "+ this.getDirectoryServerID() + " creating UDP Socket at: "+ serverSocket.getLocalAddress() +":"+serverSocket.getLocalPort());

        byte[] receiveData = new byte[1024];
        byte[] sendData;
        while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String message = new String( receivePacket.getData(),receivePacket.getOffset(),receivePacket.getLength());

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            switch (message){
                case "init":
                    sendData = this.init();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                    break;
                case "informAndUpdate": this.informAndUpdate();
                    break;
                case "queryForContent": this.queryForContent();
                    break;
                case "exit": this.exit();
                    break;
                default: System.out.println("Host #"+ this.getDirectoryServerID() +" received a bad message: "+message);
            }
        }
    }

    void openUDPSocket(){
        Thread thread1 = new Thread(() -> {
            try {
                this.createUDPSocket();
            } catch (IOException e) {
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

    private byte[] init(){
        return this.getIPAddress().toString().substring(1).getBytes();
    }
    private byte[] getIPS(ArrayList<String> list) throws IOException {
        list.add(this.getIPAddress().toString().substring(1));
        return ListToByteArray(list);
    }

    private void informAndUpdate(){}

    private void queryForContent(){}

    private void exit(){}


}

