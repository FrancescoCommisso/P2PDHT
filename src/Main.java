import com.sun.tools.internal.jxc.ap.Const;

import java.awt.peer.CanvasPeer;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        DirectoryServer ds1 = new DirectoryServer(Constants.SERVER_1_IP,1);
        DirectoryServer ds2 = new DirectoryServer(Constants.SERVER_2_IP,2);
        DirectoryServer ds3 = new DirectoryServer(Constants.SERVER_3_IP,3);
        DirectoryServer ds4 = new DirectoryServer(Constants.SERVER_4_IP,4);

        ds1.setLeftNeighbor(Constants.SERVER_4_IP);
        ds1.setRightNeighbor(Constants.SERVER_2_IP);

        ds2.setLeftNeighbor(Constants.SERVER_1_IP);
        ds2.setRightNeighbor(Constants.SERVER_3_IP);

        ds3.setLeftNeighbor(Constants.SERVER_2_IP);
        ds3.setRightNeighbor(Constants.SERVER_4_IP);

        ds4.setLeftNeighbor(Constants.SERVER_3_IP);
        ds4.setRightNeighbor(Constants.SERVER_1_IP);

        Client client = new Client(1,Constants.LOCAL_IP);
        Client client2 = new Client(2,Constants.LOCAL_IP);

        ds1.openUDPSocket();
        ds2.openUDPSocket();
        ds3.openUDPSocket();
        ds4.openUDPSocket();

        ds1.openTCPSocket();
        ds2.openTCPSocket();
        ds3.openTCPSocket();
        ds4.openTCPSocket();

//
//        TimeUnit.SECONDS.sleep(1);
//
        client2.init();
        client.init();
//
    }
}
