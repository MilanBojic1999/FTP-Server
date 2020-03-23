package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class Server {

    private List<User> users;

    public Server() throws Exception{
        utilInit();

        ServerSocket serverConnectionSocket=new ServerSocket(21);
        ServerSocket serverTransferSocket=new ServerSocket(20);
        Socket connectSocket;
        while (true) {
            connectSocket(serverConnectionSocket.accept());
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
            if (users.contains(dummy)) {
                out.println("Welcome to server");
                break;
            } else {
                out.println("Wrong information");
            }
        }

        return true;
    }

    private boolean transferSocket(Socket socket){


        return false;
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
