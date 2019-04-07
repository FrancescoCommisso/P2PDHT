class Constants {

    /*

    To initialize multiple loopback ip's run this command in the terminal:

    sudo ifconfig lo0 alias 127.0.1.0 127.0.1.1 127.0.1.2 127.0.1.3 127.0.1.4 127.0.1.5 127.0.1.6 127.0.1.7

    ***** must be run individually for each ip****
    */

    static final int DIRECTORY_SERVER_UDP_PORT = 9001;
    static final int DIRECTORY_SERVER_TCP_PORT = 9002;
    static final int CLIENT_UDP_PORT = 9011;
    static final int PEER_TCP_IN_PORT = 9012;
    static final int PEER_TCP_OUT_PORT = 9013;


    static final String LOCAL_IP = "192.168.2.26";
    static final String LOCAL_IP_2 = "::1";

    static final String SERVER_1_IP = "127.0.1.0";
    static final String SERVER_2_IP = "127.0.1.1";
    static final String SERVER_3_IP = "127.0.1.2";
    static final String SERVER_4_IP = "127.0.1.3";

    static final String CLIENT_IP_1 = "127.0.1.4";
    static final String CLIENT_IP_2 = "127.0.1.5";
    static final String CLIENT_IP_3 = "127.0.1.6";
    static final String CLIENT_IP_4 = "127.0.1.7";

    static final String DS_RESPONSE_IMAGE_NOT_FOUND = "404 IMAGE NOT FOUND";


    /*                CLIENT MESSAGES                            */
    static final String INIT = "1";
    static final String INFORM_AND_UPDATE = "2";
    static final String QUERY_FOR_CONTENT = "3";
    static final String FILE_TRANSFER = "4";
    static final String EXIT = "5";
    /*                CLIENT MESSAGES                            */

}

