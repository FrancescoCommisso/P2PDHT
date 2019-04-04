import java.io.IOException;

public class Main {

    public static void main(String args[]) throws IOException {

        ServerPool sp = new ServerPool();

        Client client1 = new Client(1, Constants.CLIENT_IP_1);
        Client client2 = new Client(2, Constants.CLIENT_IP_2);
        Client client3 = new Client(3, Constants.CLIENT_IP_3);
        Client client4 = new Client(4, Constants.CLIENT_IP_4);

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


        for (DirectoryServer ds : sp.getDirectoryServers()) {
            ds.printAllRecords();
        }

        client1.queryForContent("a red car asd ");

        client2.exit();


        for (DirectoryServer ds : sp.getDirectoryServers()) {
            ds.printAllRecords();
        }

    }
}
