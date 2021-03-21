import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho {


    public static void main( String[] args) throws Exception {

      int port = Integer.parseInt(args[0]);
      ServerSocket serversocket = new ServerSocket(port);


      while(true) {


        Socket connectionSocket = serversocket.accept();
        String httpRespons = "HTTP/1.1 200 OK\r\n\r\n";
        byte [] encodedRespons= httpRespons.getBytes(StandardCharsets.UTF_8);
        connectionSocket.getOutputStream().write(encodedRespons);

        int bufferSize = 0;

        while(true) {

        byte[] toClientBuffer= new byte[1024];
        bufferSize = connectionSocket.getInputStream().read​(toClientBuffer);

        if(bufferSize != 1024) {
          byte[] newarr = new byte[bufferSize];

          for(int i = 0; i < bufferSize; i++){
            newarr[i] = toClientBuffer[i];
          }

          String decodedString= new String(newarr, StandardCharsets.UTF_8);
          System.out.println(decodedString);
          connectionSocket.getOutputStream().write(newarr);
        

          if (newarr[bufferSize-1] == 10) {
            break; }

        }
        else {

          connectionSocket.getOutputStream().write(toClientBuffer);

          if (toClientBuffer[bufferSize-1] == 10) {
            break; }

        }
      }
      connectionSocket.close();
    }
  }
}
