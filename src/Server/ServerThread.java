package Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerThread implements Runnable{
    private Socket connectSocket;
    private Socket transferSocket;
    private List<User> users;

    public ServerThread(Socket connectSocket, Socket transferSocket, List<User> users) {
        this.connectSocket = connectSocket;
        this.transferSocket = transferSocket;
        this.users = users;
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

    @Override
    public void run() {

    }
}
