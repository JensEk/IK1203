import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;


public class ConcHTTPAsk {

  public static void main( String[] args) throws Exception {

    int port = Integer.parseInt(args[0]);
    ServerSocket serversocket = new ServerSocket(port);

    try {

    // Skapar ett Runnable objekt för att kunna starta flera trådar och kunna hantera flera klienter på samma gång utan att invänta connection.close från varje.
    while(true) {

      Socket connectionSocket = serversocket.accept();
      MyRunnable connect = new MyRunnable(connectionSocket);
      new Thread(connect).start();
      }
    } catch(Exception ex) {
        System.err.println("Exception catched" + ex);
    }
  }
}
