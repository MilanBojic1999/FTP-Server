package Client;

import java.io.*;
import java.net.Socket;

public class FileClient {
    private Socket socket;
    public FileClient(Socket socket) {
        this.socket = socket;
    }

    public boolean send(File file){
        int bread=0;
        int curr=0;
        byte[] sanding=new byte[(int)file.length()];
        try{
            FileInputStream fis=new FileInputStream(file);
            BufferedInputStream bis=new BufferedInputStream(fis);
            //PrintWriter in=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
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
            int size=Integer.parseInt(in.readLine());
            System.out.println("Getting File: "+name+" with size: "+size+" bytes");
            File nFile=new File("ClientsFiles"+File.pathSeparator+name);
            int i=1;
            //Ovde sistem pokušava da napraci novi fajl, ako fajl sa istim imenom postoji
            //Pravi se fajl sa inkrementiranom brojnom vrednošću dodato uz originalno ime
            while (!nFile.createNewFile())
                nFile=new File("ClientsFiles"+File.pathSeparator+name+"("+(i++)+")");
            byte[] inBuff=new byte[size+32];

            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(nFile));

            int byteRead=0;
            int curr=0;
            do{
                byteRead=is.read(inBuff,curr,(inBuff.length-curr));
                if(byteRead>=0)
                     curr+=byteRead;
            }while (byteRead>-1);

            bos.write(inBuff,0,curr);
            bos.flush();
            System.out.println("Writen into file "+nFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }
}
