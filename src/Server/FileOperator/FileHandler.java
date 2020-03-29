package Server.FileOperator;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class FileHandler {
    private FileLister lister;

    public FileHandler() {
        lister=new FileLister();
    }

    public boolean put(Socket socket){
        try {
            InputStream is=socket.getInputStream();
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String name=in.readLine();
            int size=Integer.parseInt(in.readLine());
            System.out.println("Getting File: "+name+" with size: "+size+" bytes");
            File inFile=new File("ServerFiles"+File.separator+name);
            if(!inFile.createNewFile())
                System.out.println("Overwriting file");

            int byteRead;

            byte[] inBuff=new byte[size+32];

            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(inFile));

            byteRead=is.read(inBuff,0,inBuff.length);
            bos.write(inBuff,0,byteRead);
            bos.flush();
            System.out.println("Writen into file "+inFile.getName());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean get(Socket socket,String name){
        try{
            File file;
            if(name.split("/").length<2)
                file=new File("ServerFiles"+File.separator+name);
            else
                file=new File(name);
            System.out.println(file.getAbsoluteFile());
            if(!file.exists())
                return false;
            byte[] sanding=new byte[(int)file.length()];

            FileInputStream fis=new FileInputStream(file);
            BufferedInputStream bis=new BufferedInputStream(fis);
            bis.read(sanding,0,sanding.length);

            OutputStream os=socket.getOutputStream();
            PrintWriter out=new PrintWriter(new BufferedOutputStream(os),true);
            out.println(file.getName());
            out.println(file.length());
            System.out.println("Sending "+file.getName()+" to "+socket.getInetAddress());
            os.write(sanding,0,sanding.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void list(Socket socket){
        try {
            List<String> list=lister.listFiles();
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            out.println(list.size());
            for(String str:list)
                out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
