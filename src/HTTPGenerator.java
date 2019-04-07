public class HTTPGenerator {

    static String createHTTPRequest(String path, String host) {

        return "GET " + path + " HTTP/1.1\r\n" +
                host +
                "Connection: close \r\n\r\n";
    }


}
