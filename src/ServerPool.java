import java.net.UnknownHostException;
import java.util.ArrayList;

class ServerPool {
    private ArrayList<DirectoryServer> directoryServers;

    ServerPool() throws UnknownHostException {
        this.directoryServers = new ArrayList<>();
        DirectoryServer ds1 = new DirectoryServer(Constants.SERVER_1_IP, 1);
        DirectoryServer ds2 = new DirectoryServer(Constants.SERVER_2_IP, 2);
        DirectoryServer ds3 = new DirectoryServer(Constants.SERVER_3_IP, 3);
        DirectoryServer ds4 = new DirectoryServer(Constants.SERVER_4_IP, 4);

        ds1.setLeftNeighbor(Constants.SERVER_4_IP);
        ds1.setRightNeighbor(Constants.SERVER_2_IP);

        ds2.setLeftNeighbor(Constants.SERVER_1_IP);
        ds2.setRightNeighbor(Constants.SERVER_3_IP);

        ds3.setLeftNeighbor(Constants.SERVER_2_IP);
        ds3.setRightNeighbor(Constants.SERVER_4_IP);

        ds4.setLeftNeighbor(Constants.SERVER_3_IP);
        ds4.setRightNeighbor(Constants.SERVER_1_IP);

        directoryServers.add(ds1);
        directoryServers.add(ds2);
        directoryServers.add(ds3);
        directoryServers.add(ds4);

        ds1.openUDPSocket();
        ds2.openUDPSocket();
        ds3.openUDPSocket();
        ds4.openUDPSocket();

        ds1.openTCPSocket();
        ds2.openTCPSocket();
        ds3.openTCPSocket();
        ds4.openTCPSocket();

    }

    ArrayList<DirectoryServer> getDirectoryServers() {
        return directoryServers;
    }
}
