package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is a small client class, just in use for
 * testing FTP server and its methods
 * It connects to Server, gives command to server
 * and displays recaving messages from server
 */
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

        Socket tsocket=new Socket("192.168.1.10",20);

        filler=new FileClient(tsocket);
        do{
            System.out.print(in.readLine());
            info=sc.nextLine();
            if(info.length()<1)
                continue;
            String[] comms=info.split(" ");
            out.println(info);
            msg=in.readLine();
            if(comms[0].equalsIgnoreCase("put")){
                filler.send(comms[1]);
            }else if(comms[0].equalsIgnoreCase("get")){
                filler.receive();
            }else if(comms[0].equalsIgnoreCase("list")) {
                int size=Integer.parseInt(in.readLine());
                for(int i=0;i<size;++i)
                    System.out.println("-"+in.readLine());
            }else if(comms[0].equalsIgnoreCase("renameto")){
                System.out.println(in.readLine());
            }else if(comms[0].equalsIgnoreCase("help")){
                int num=Integer.parseInt(in.readLine());
                for(int i=0;i<num;i++)
                    System.out.println(in.readLine());
            }

            System.out.println(msg);
        }while (!msg.equals("Quiting..."));

        sc.close();
        tsocket.close();
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
