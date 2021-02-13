package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient {

    public static String askServer(String hostname, int port, String ToServer) throws IOException {

      byte[] fromServerBuffer= new byte[1024];
      Socket clientSocket= new Socket(hostname, port);
      clientSocket.setSoTimeout(1000);


      byte [] encodedBytes= ToServer.getBytes(StandardCharsets.UTF_8);
      clientSocket.getOutputStream().write(encodedBytes);

      try {
      clientSocket.getInputStream().read(fromServerBuffer);
      }
      catch(java.net.SocketTimeoutException e) {
        System.out.println("***Server time out***");
      }


      String decodedString= new String(fromServerBuffer, StandardCharsets.UTF_8);
      return decodedString;

    }

    public static String askServer(String hostname, int port) throws IOException {

      byte[] fromServerBuffer= new byte[1024];
      Socket clientSocket= new Socket(hostname, port);
      clientSocket.setSoTimeout(1000);

      try {
      clientSocket.getInputStream().read(fromServerBuffer);
      }
      catch(java.net.SocketTimeoutException e) {
        System.out.println("***Server time out***");
      }

      String decodedString= new String(fromServerBuffer, StandardCharsets.UTF_8);
      return decodedString;

    }
}
