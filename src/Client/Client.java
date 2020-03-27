package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    FileClient filler;

    public Client() throws Exception{
        Socket socket=new Socket("192.168.1.10",21);

        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

        Scanner sc=new Scanner(System.in);
        String msg="";
        System.out.println(in.readLine());
        String info;
        do{
            if(!msg.equals(""))
                System.out.println(msg);
            System.out.print(in.readLine());
            info=sc.nextLine();
            out.println(info);
            System.out.print(in.readLine());
            info=sc.nextLine();
            out.println(info);

        }while (!(msg=in.readLine()).equals("Welcome to server"));
        System.out.println(msg);

        socket=new Socket("192.168.1.10",20);

        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

        do{
            System.out.print(in.readLine());
            info=sc.nextLine();
            out.println(info);
            msg=in.readLine();
            System.out.println(msg);
        }while (!msg.equals("Quiting..."));

        sc.close();
        socket.close();
    }


    public static void main(String[] args) {
        try {
            new Client();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
