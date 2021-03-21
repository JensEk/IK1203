import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import tcpclient.TCPClient;

public class MyRunnable implements Runnable{


  Socket connectionSocket;

  // constructor för att binda serverconnection till objektet
  public MyRunnable(Socket connection) {
    this.connectionSocket = connection;
  }


  public void run() {

    try{

    String serverOutput = null;
    String httpRespons = null;
    String hostname = null;
    String message = null;
    int clientPort = 0;

    int bufferSize = 0;
    int arrSize = 1024;
    int pos = 0;



    byte[] toClientBuffer = new byte[arrSize];
    bufferSize = connectionSocket.getInputStream().read​(toClientBuffer);


    // funktion för eventuell resize av ny array
    while(bufferSize == arrSize) {

      byte[] newArr = new byte[toClientBuffer.length + arrSize];
      int max = bufferSize + pos;

      for(int i = pos; i < max; i++){
        newArr[i] = toClientBuffer[i];
        pos++; }

        toClientBuffer = newArr.clone();

        bufferSize = connectionSocket.getInputStream().read​(toClientBuffer, pos, arrSize);
      }


      // splittar upp mottagen string i två omgångar för att plocka ut de saker som efterfrågas
      String decodedString= new String(toClientBuffer, StandardCharsets.UTF_8);
      String[] split_rad = decodedString.split("\r\n");
      String[] sub = split_rad[0].split(" |=|&");
      boolean http = false;
      int len = sub.length;

      if(sub[len-1].equals("HTTP/1.0") || sub[len-1].equals("HTTP/1.1") || sub[len-1].equals("HTTP/2.0"))
        http = true;


      if(sub.length > 2)
        hostname = sub[2];

      if(sub.length > 4)
        clientPort = Integer.parseInt(sub[4]);

      if(sub.length > 6)
        message = sub[6] + "\n";

      // Hanterar endast GET annars blir resterande förfrågningar 404 bad request
      if(sub[0].equals("GET") && http) {


        if(sub[1].matches("(?i).*/ask?.*")) {

          httpRespons = "HTTP/1.1 200 OK\r\n\r\n";

          try {

            if(sub.length > 6)
            serverOutput = TCPClient.askServer(hostname, clientPort, message);
            else
            serverOutput = TCPClient.askServer(hostname, clientPort);

          }catch(Exception ex) {
            httpRespons = "HTTP/1.1 404 Not Found\r\n\r\n";
            serverOutput = "HTTP/1.1 404 Not Found"; }
        }
        else {
          httpRespons = "HTTP/1.1 404 Not Found\r\n\r\n";
          serverOutput = "HTTP/1.1 404 Not Found"; }
      }
      else {
        httpRespons = "HTTP/1.1 400 Bad Request\r\n\r\n";
        serverOutput = "HTTP/1.1 400 Bad Request";
      }

      byte [] encodedHttp = httpRespons.getBytes(StandardCharsets.UTF_8);
      connectionSocket.getOutputStream().write(encodedHttp);

      if(serverOutput != null) {
        byte [] encodedRespons = serverOutput.getBytes(StandardCharsets.UTF_8);
        connectionSocket.getOutputStream().write(encodedRespons);
      }

      connectionSocket.close();
    }
    catch(Exception e) {
      System.out.println("Exception catched");
    }
  }
}
