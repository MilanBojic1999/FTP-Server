package Server.FileOperator;

import java.io.*;
import java.net.Socket;

public class FileHandler {


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

            int byteRead=0;
            int curr=0;
            byte[] inBuff=new byte[size+32];

            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(inFile));
            do{
                byteRead=is.read(inBuff,curr,(inBuff.length-curr));
                if(byteRead>=0)
                    curr+=byteRead;
            }while (byteRead>-1);

            bos.write(inBuff,0,curr);
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
            File file=new File("ServerFiles"+File.separator+name);
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
}
