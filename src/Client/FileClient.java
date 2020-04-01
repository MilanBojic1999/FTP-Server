package Client;

import java.io.*;
import java.net.Socket;

/**
 * This class handles file transfer from the client side
 */
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

            //Dobijanje meta podataka o fajlu
            String fullname=in.readLine();
            long size=Long.parseLong(in.readLine());
            String[] fullnameParts=fullname.split("/"); //deli dobijenu putanju na imenea direktorijuma
            String name=fullnameParts[fullnameParts.length-1]; // ovo je poslednji deo putanje, odnosno ime fajla


            System.out.println("Getting File: "+fullname+" with size: "+size+" bytes");
            File nFile;
            nFile=new File("ClientsFiles"+File.separator+name);

            int i=1;
            //Ovde sistem pokušava da napraci novi fajl, ako fajl sa istim imenom postoji
            //Pravi se fajl sa inkrementiranom brojnom vrednošću dodato uz originalno ime
            if(!nFile.createNewFile()) {
                nFile=new File("ClientsFiles"+File.separator+name+"("+ i +")"+".txt");
                ++i;
            }
            byte[] inBuff=new byte[(int) size+64];

            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(nFile));

            int byteRead;
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
