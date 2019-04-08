import java.io.IOException;

public class Main {

    public static void main(String args[]) throws IOException {

        ServerPool sp = new ServerPool();

        Client client1 = new Client(11, Constants.CLIENT_IP_1);
        Client client2 = new Client(12, Constants.CLIENT_IP_2);
        Client client3 = new Client(13, Constants.CLIENT_IP_3);
        Client client4 = new Client(14, Constants.CLIENT_IP_4);

        client1.init();
        client2.init();
        client3.init();
        client4.init();

        client1.informAndUpdate("/Users/Francesco/Desktop/P2PImages/a.jpeg");
        client2.informAndUpdate("/Users/Francesco/Desktop/P2PImages/b.jpeg");
        client3.informAndUpdate("/Users/Francesco/Desktop/P2PImages/c.jpeg");
        client4.informAndUpdate("/Users/Francesco/Desktop/P2PImages/d.jpeg");


        client1.queryForContent("c.jpeg");

        System.out.println("\nAFTER PLACING ALL IMAGES THE DIRECTORY SERVERS LOOK LIKE:\n");

        for (DirectoryServer ds : sp.getDirectoryServers()) {
            ds.printAllRecords();
        }

        client1.fileTransfer("c.jpeg");
        System.out.println("\nAFTER FILE TRANSFERS, ALL IMAGES THE DIRECTORY SERVERS LOOK LIKE:\n");
        for (DirectoryServer ds : sp.getDirectoryServers()) {
            ds.printAllRecords();
        }


        client1.exit();
        client2.exit();
        client3.exit();
        client4.exit();

        System.out.println("\nAFTER EXITING, ALL IMAGES THE DIRECTORY SERVERS LOOK LIKE:\n");

        for (DirectoryServer ds : sp.getDirectoryServers()) {
            ds.printAllRecords();
        }

    }
}
