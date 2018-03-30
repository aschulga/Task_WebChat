package alexshulga;

import alexshulga.view.UserConsole;

public class User {
    private static String url = "ws://localhost:8080/user";

    public static void main(String args[]){
        UserConsole userConsole = new UserConsole(url);
        userConsole.init();
    }
}
