package alexshulga;

import alexshulga.view.Console;

public class User {
    private static String url = "ws://localhost:8080/user";

    public static void main(String args[]){
        Console console = new Console(url);
        console.init();
        //new CountDownLatch(1).await();
    }
}
