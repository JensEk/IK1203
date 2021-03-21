public class test {

  public static void main( String[] args) throws Exception {

    String str = "GET /ask?hostname=time.nist.gov&port=13 HTTP/1.1";
    String[] split = str.split(" |=|&");
    int len = split.length;
    System.out.println("längd är : " + len);
    System.out.println(split[len-1]);

    }
}
