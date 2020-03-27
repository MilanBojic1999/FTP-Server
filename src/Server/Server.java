package Server;

import Server.FileOperator.FileLister;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class Server {

    private List<User> users;
    private FileLister lister;

    public Server() throws Exception{
        utilInit();
        ServerSocket csSocket=new ServerSocket(21);
        ServerSocket tsSocket=new ServerSocket(20);
        System.out.println("Server buted up");
        while (true){
            Socket socket=csSocket.accept();

            ServerThread st=new ServerThread(socket,tsSocket,users);
            Thread th=new Thread(st);
            th.start();
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
