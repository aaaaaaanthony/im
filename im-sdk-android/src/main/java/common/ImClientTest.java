package common;


public class ImClientTest {

    public static void main(String[] args) throws InterruptedException {
        ImClient imClient = new ImClient();
        imClient.connect("127.0.0.1", 8080);
        imClient.auth("anthony", "anthony_token");
        imClient.send("anthony", "测试信息");

//        while (true) {
//            Thread.sleep(30 * 1000);
//
//        }
    }

}