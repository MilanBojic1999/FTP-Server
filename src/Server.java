
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private List<User> users;

    public Server() throws Exception{
        utilInit();

        ServerSocket serverConnectionSocket=new ServerSocket(21);
        ServerSocket serverTransferSocket=new ServerSocket(20);

        Socket connectSocket=serverConnectionSocket.accept();

        BufferedReader in=new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
        PrintWriter out=new PrintWriter(new OutputStreamWriter(connectSocket.getOutputStream()),true);

        out.println("Log in pleas:");
        User dummy=new User("1","1");
        while (true){
            out.print("Enter username: ");
            dummy.setUsername(in.readLine());
            out.print("Enter password: ");
            dummy.setPassword(in.readLine());
            if(users.contains(dummy)){
                out.println("Welcome to server");
                break;
            }else {
                out.println("Wrong information");
            }
        }

        Socket transferSocket=serverTransferSocket.accept();


    }

    void utilInit(){
        users=new ArrayList<>();
        users.add(new User("Pera","1234"));
    }
}
