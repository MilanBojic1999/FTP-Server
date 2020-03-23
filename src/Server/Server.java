package Server;

import Server.FileOperator.FileLister;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class Server {

    private List<User> users;
    private Socket socket;
    private FileLister lister;

    public Server() throws Exception{
        utilInit();

        ServerSocket serverConnectionSocket=new ServerSocket(21);
        ServerSocket serverTransferSocket=new ServerSocket(20);
        Socket connectSocket;
        while (true) {
            if(connectSocket(serverConnectionSocket.accept())){
                socket=serverTransferSocket.accept();
                transferSocket(socket);
                socket.close();
            }
        }


        //transferSocket.close();

    }

    private boolean connectSocket(Socket socket) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        out.println("Log in please!!!");
        User dummy = new User("1", "1");
        while (true) {
            out.println("Enter username: ");
            dummy.setUsername(in.readLine());
            out.println("Enter password: ");
            dummy.setPassword(in.readLine());
            if (users.contains(dummy) || dummy.getUsername().equalsIgnoreCase("anonymous")) {
                out.println("Welcome to server");
                break;
            } else {
                out.println("Wrong information");
            }
        }

        return true;
    }

    private boolean transferSocket(Socket socket) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        boolean flag=false;
        do {
            out.println("ftp> ");
            String comm = in.readLine();
            switch (comm) {
                case "get":
                    out.println("Get command");
                    break;
                case "put":
                    put();
                    out.println("Put command");
                    break;
                case "quit":
                    out.println("Quiting...");
                    flag = true;
                    break;
                default:
                    out.println("Unknown command");
            }

        } while (!flag);

        return false;
    }

    private File get(){
        return null;
    }

    private void put(){
        for(String name:lister.listFiles()){
            System.out.println("->"+name);
        }
    }

    private void utilInit(){
        users=new ArrayList<>();
        users.add(new User("Pera","1234"));
        lister=new FileLister();
    }

    public static void main(String[] args) {
        try {
            new Server();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
