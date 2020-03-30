package Server;

import Server.FileOperator.FileHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerThread implements Runnable{
    private Socket connectSocket;
    private ServerSocket transferSocket;
    private List<User> users;
    private FileHandler filler;
    private BufferedReader in;
    private PrintWriter out;
    private boolean renamePro;

    public ServerThread(Socket connectSocket,ServerSocket transferSocket, List<User> users) {
        this.connectSocket = connectSocket;
        this.transferSocket = transferSocket;
        this.users = users;
        filler=new FileHandler();
        renamePro=false;
    }

    private boolean connectSocket(Socket socket) throws IOException {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        out.println("Log in please!!!");
        User dummy = new User("1", "1");
        while (true) {
            out.println("Enter username: ");
            dummy.setUsername(in.readLine());
            out.println("Enter password: ");
            dummy.setPassword(in.readLine());
            if (users.contains(dummy) || dummy.getUsername().equalsIgnoreCase("anonymous")) {
                out.println("Welcome to server");
                System.out.println("New clint");
                return true;
            } else {
                out.println("Wrong information");
            }
        }

    }

    private boolean transferSocket(Socket socket) throws IOException{

        boolean flag=false;
        do {
            out.println("ftp> ");
            String info = in.readLine();
            String[] comms=info.split(" ");
            String comm=comms[0];
            StringBuilder fileName= new StringBuilder();
            for(int i=1;i<comms.length;++i)
                fileName.append(comms[i]);
            System.out.println("Client wants to "+comm);
            if(renamePro){
                out.println("Rename commands");
                if(comm.equalsIgnoreCase("renameto")) {
                    out.println("Renaming file...");
                    filler.renameFile(fileName.toString());
                }else{
                    out.println("Error commands order not supported");
                }
                renamePro=false;
                continue;
            }
            switch (comm) {
                case "get":
                    out.println("Get command");
                    filler.get(socket, fileName.toString());
                    System.out.println("gsg");
                    break;
                case "put":
                    out.println("Put command");
                    filler.put(socket);
                    break;
                case "quit":
                    out.println("Quiting...");
                    flag = true;
                    break;
                case "list":
                    out.println("List command");
                    filler.list(connectSocket);
                    break;
                case "delete":
                    out.println("Delete command");
                    if (filler.delete(socket,fileName.toString()))
                        System.out.println("File deleted");
                    break;
                case "renameFrom": case "renamefrom":
                    out.println("Rename commands");
                    if(fileName.toString().isEmpty())
                        continue;
                    renamePro=true;
                    filler.setRename(fileName.toString());
                    break;
                case "help":
                    out.println("Help commmands");
                    help();
                    break;
                default:
                    out.println("Unknown command");
            }

        } while (!flag);
        socket.close();
        return false;
    }

    @Override
    public void run() {
        try{
            if(connectSocket(connectSocket))
                transferSocket(transferSocket.accept());
                System.out.println("Client quitting");
            connectSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void help(){
        out.println(7);
        out.println("-get               -get file from server");
        out.println("-put <name>        -put your file to server");
        out.println("-delete <name>     -delete file from server");
        out.println("-list              -list all servers files");
        out.println("-renameFrom <name> -select servers file to be renamed, after this renameTo must follow");
        out.println("-renameTo <name>   -rename already selected servers file");
        out.println("-quit              -quit connection with the server");
    }
}
