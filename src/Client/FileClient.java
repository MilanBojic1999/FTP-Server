package Client;

import java.io.*;
import java.net.Socket;

public class FileClient {
    private Socket socket;
    public FileClient(Socket socket) {
        this.socket = socket;
    }

    public boolean send(String filename){

        try{
            File file=new File(filename);
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

    public boolean receive(){
        try {
            InputStream is=socket.getInputStream();
            BufferedReader in=new BufferedReader(new InputStreamReader(is));
            String name=in.readLine();
            long size=Long.parseLong(in.readLine());
            System.out.println("Getting File: "+name+" with size: "+size+" bytes");
            File nFile=new File("ClientsFiles"+File.separator+name);
            int i=1;
            //Ovde sistem pokušava da napraci novi fajl, ako fajl sa istim imenom postoji
            //Pravi se fajl sa inkrementiranom brojnom vrednošću dodato uz originalno ime
            if(!nFile.createNewFile())
                System.out.println("File already exist");
            byte[] inBuff=new byte[(int) size+64];

            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(nFile));

            int byteRead=0;
            byteRead=is.read(inBuff,0,inBuff.length);
            bos.write(inBuff,0,byteRead);
            bos.flush();
            System.out.println("Writen into file "+nFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }
}
