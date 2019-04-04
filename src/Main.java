import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        DirectoryServer ds1 = new DirectoryServer(Constants.SERVER_1_IP, 1);
        DirectoryServer ds2 = new DirectoryServer(Constants.SERVER_2_IP, 2);
        DirectoryServer ds3 = new DirectoryServer(Constants.SERVER_3_IP, 3);
        DirectoryServer ds4 = new DirectoryServer(Constants.SERVER_4_IP, 4);

        ArrayList<DirectoryServer> serverPool = new ArrayList<>();
        serverPool.add(ds1);
        serverPool.add(ds2);
        serverPool.add(ds3);
        serverPool.add(ds4);


        ds1.setLeftNeighbor(Constants.SERVER_4_IP);
        ds1.setRightNeighbor(Constants.SERVER_2_IP);

        ds2.setLeftNeighbor(Constants.SERVER_1_IP);
        ds2.setRightNeighbor(Constants.SERVER_3_IP);

        ds3.setLeftNeighbor(Constants.SERVER_2_IP);
        ds3.setRightNeighbor(Constants.SERVER_4_IP);

        ds4.setLeftNeighbor(Constants.SERVER_3_IP);
        ds4.setRightNeighbor(Constants.SERVER_1_IP);

        Client client1 = new Client(1, Constants.CLIENT_IP_1);
        Client client2 = new Client(2, Constants.CLIENT_IP_2);
        Client client3 = new Client(3, Constants.CLIENT_IP_3);
        Client client4 = new Client(4, Constants.CLIENT_IP_4);

        ds1.openUDPSocket();
        ds2.openUDPSocket();
        ds3.openUDPSocket();
        ds4.openUDPSocket();

        ds1.openTCPSocket();
        ds2.openTCPSocket();
        ds3.openTCPSocket();
        ds4.openTCPSocket();

        client1.init();
        client2.init();
        client3.init();
        client4.init();

        client2.informAndUpdate("picture of a dag");
        client1.informAndUpdate("a");
        client2.informAndUpdate("b");
        client2.informAndUpdate("selfie123");
        client1.informAndUpdate("grad");
        client3.informAndUpdate("c");
        client4.informAndUpdate("d");
        client4.informAndUpdate("a red car");
        client2.informAndUpdate("the monalisa");
        client3.informAndUpdate("a tiger");

        for (DirectoryServer ds : serverPool) {
            ds.printAllRecords();
        }


    }
}
