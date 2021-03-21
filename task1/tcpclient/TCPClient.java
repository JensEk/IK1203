package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class TCPClient {

    public static String askServer(String hostname, int port, String toServer) throws IOException {


      Socket clientSocket= new Socket(hostname, port);
      clientSocket.setSoTimeout(2000);
      byte [] encodedBytes= toServer.getBytes(StandardCharsets.UTF_8);
      clientSocket.getOutputStream().write(encodedBytes);

      InputStream input = clientSocket.getInputStream();

      StringBuilder str = new StringBuilder();
      byte b;

      try {
      int ch = input.read();


      // Läser in varje tecken från input tills det är tomt(-1). Gör en IF koll beroende på om det är UTF8 specialtecken så som åäö. UTF8 varje code point är upp till 6 bytes så skapar en ny array som läser in och kopierar allt utom null.
      while(ch != -1) {

        byte[] arr = new byte[6];

        if(ch > 127){


          for(int i = 0; i < 6; i++) {
            b = (byte)ch;
            arr[i] = b;
            ch = input.read();

            if(ch < 128){
              break;
            }
          }

          int size = 0;
          for(int i = 0; i < 6; i++) {
            if (arr[i] != 0)
              size++;
          }
          byte[] realarr = new byte[size];
          for(int i = 0; i < size; i++) {
            realarr[i] = arr[i];
          }

          String decoded = new String(realarr, StandardCharsets.UTF_8);
          str.append(decoded);
          }
          else {
            str.append((char)ch);
            ch = input.read();
          }
        }
      }
      catch(java.net.SocketTimeoutException e) {
        System.out.println("***Server time out***");
      }

       return str.toString();

    }


    public static String askServer(String hostname, int port) throws IOException {

      Socket clientSocket= new Socket(hostname, port);
      clientSocket.setSoTimeout(2000);
      InputStream input = clientSocket.getInputStream();

      StringBuilder str = new StringBuilder();
      byte b;

      try {
      int ch = input.read();

      while(ch != -1) {

        byte[] arr = new byte[6];

        if(ch > 127){


          for(int i = 0; i < 6; i++) {
            b = (byte)ch;
            arr[i] = b;
            ch = input.read();

            if(ch < 128){
              break;
            }
          }

          int size = 0;
          for(int i = 0; i < 6; i++) {
            if (arr[i] != 0)
              size++;
          }
          byte[] realarr = new byte[size];
          for(int i = 0; i < size; i++) {
            realarr[i] = arr[i];
          }

          String decoded = new String(realarr, StandardCharsets.UTF_8);
          str.append(decoded);
          }
          else {
            str.append((char)ch);
            ch = input.read();
          }
        }
      }
      catch(java.net.SocketTimeoutException e) {
        System.out.println("***Server time out***");
      }

       return str.toString();
    }
}
