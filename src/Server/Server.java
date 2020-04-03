package Server;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Header of whole Server it operates with all regestared users
 * and all attempts from some client is handeled here
 * It uses two server sockets, with ports 20 and 21
 */
class Server {

    private List<User> users;

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
    }

    public static void main(String[] args) {
        try {
            new Server();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
