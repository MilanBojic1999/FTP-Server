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

    public boolean send(String filename) {
        System.out.println("-->"+filename);
        try {
            File file;
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(new BufferedOutputStream(os), true);


            if (filename.contains("ClientsFiles")) {
                file = new File(filename);
            }
            else {
                file = new File("ClientsFiles"+File.separator+filename);
            }

            //Proveravamo da li traženi fajl postoji u sistemu
            if(!file.exists()) {
                System.err.println("Fajl ne postoji za slanje");
                out.println("NULL");
                return false;
            }

            byte[] sanding = new byte[(int) file.length()];

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(sanding, 0, sanding.length);

            out.println(file.getName());
            out.println(file.length());
            System.out.println("Sending " + file.getName() + " to server");
            os.write(sanding, 0, sanding.length);
            os.flush();
        }catch (FileNotFoundException e0){
            System.err.println("No such FILE!!!!");
            return false;
        }catch (IOException e) {
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

            //Proveravamo da li je server sadrži traženi fajl
            if(fullname.equals("NULL")) {
                System.err.println("Fajl nije primljen");
                return false;
            }
            long size=Long.parseLong(in.readLine());


            System.out.println("Getting File: "+fullname+" with size: "+size+" bytes");
            File nFile;
            nFile=new File("ClientsFiles"+File.separator+fullname);

            int i=1;
            //Ovde sistem pokušava da napraci novi fajl, ako fajl sa istim imenom postoji
            //Pravi se fajl sa inkrementiranom brojnom vrednošću dodato uz originalno ime
            if(!nFile.createNewFile()) {
                nFile=new File("ClientsFiles"+File.separator+"F"+i+fullname);
                ++i;
            }
            byte[] inBuff=new byte[(int) size];


            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(nFile));

            int byteRead;
            byteRead=is.read(inBuff,0,inBuff.length);
            bos.write(inBuff,0,byteRead);
            bos.flush();

            //Zatvaram strimove
            bos.close();
            System.out.println("Writen into file "+nFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }
}
