class Constants {

    /* To initialize multiple localhosts run this command in the terminal:

    sudo ifconfig lo0 alias 127.0.1.0 127.0.1.1 127.0.1.2 127.0.1.3

    */

    static final int DIRECTORY_SERVER_UDP_PORT = 9001;
    static final int DIRECTORY_SERVER_TCP_PORT = 9002;
    static final int CLIENT_UDP_PORT = 9011;
    static final int SERVER_UDP_PORT = 9012;

    static final String LOCAL_IP = "192.168.2.26";
    static final String LOCAL_IP_2 = "::1";

    static final String SERVER_1_IP = "127.0.1.0";
    static final String SERVER_2_IP = "127.0.1.1";
    static final String SERVER_3_IP = "127.0.1.2";
    static final String SERVER_4_IP = "127.0.1.3";


    /*                CLIENT MESSAGES                            */
    static final String INIT = "init";
    static final String INFORM_AND_UPDATE = "inform and update";
    static final String QUERY_FOR_CONTENT = "query for content";
    static final String FILE_TRANSFER = "file transfer";
    static final String EXIT = "exit";
    /*                CLIENT MESSAGES                            */

    /*                DIRECTORY SERVER MESSAGES                          */


}

