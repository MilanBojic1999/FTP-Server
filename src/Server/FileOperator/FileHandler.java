package Server.FileOperator;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * This class is directly connected to data transfer and
 * perform all actions representing command for data transfer
 */
public class FileHandler {
    private FileLister lister;
    private File rename;

    public FileHandler() {
        lister=new FileLister();
    }

    public boolean put(Socket socket){
        try {
            InputStream is=socket.getInputStream();
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String name=in.readLine();

            //Proverava da li fajl koji se delefirao za prenos postoji kod klijenta
            if(name.equals("NULL")) {
                System.err.println("Fajl nije primljen");
                return false;
            }
            String mmm=in.readLine();
            System.out.println("**"+mmm);
            long size=Long.parseLong(mmm);
            System.out.println("Getting File: "+name+" with size: "+size+" bytes");
            File inFile=new File("ServerFiles"+File.separator+name);
            if(!inFile.createNewFile())
                System.out.println("Overwriting file");


            byte[] inBuff=new byte[(int) size];

            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(inFile));


            int byteRead;
            int current=0;
            do {
                System.out.println("-->"+current);
                byteRead = is.read(inBuff, current, inBuff.length-current);
                if(byteRead>=0) current+=byteRead;
            }while(byteRead>0);

            bos.write(inBuff,0,current);
            bos.flush();

            System.out.println("Writen into file "+inFile.getName());
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean get(Socket socket,String name){
        try{

            File file;
            if (name.startsWith("ServerFiles"))
                file = new File(name);
            else
                file = new File("ServerFiles"+File.separator+name);
            System.out.println(file.getAbsoluteFile());
            byte[] sanding=new byte[(int)file.length()];

            OutputStream os=socket.getOutputStream();
            PrintWriter out=new PrintWriter(new BufferedOutputStream(os),true);

            //Proveravamo da li traženi fajl postoji u sistemu
            if(!file.exists()) {
                System.err.println("Traženi fajl ne postoji");
                out.println("NULL");
                return false;
            }


            FileInputStream fis=new FileInputStream(file);
            BufferedInputStream bis=new BufferedInputStream(fis);
            int inn=bis.read(sanding,0,sanding.length);
            System.err.println(inn);

            out.println(file.getName());
            out.println(file.length());
            System.out.println("Sending "+file.getName()+" to "+socket.getInetAddress().getHostName());
            os.write(sanding,0,sanding.length);
            os.flush();

            bis.close();
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

    public boolean delete(Socket socket,String name){
        File file;
        System.out.println(name);
        if(name.contains("ServerFiles"))
            file=new File(name);
        else
            file=new File("ServerFiles"+File.separator+name);
        return file.delete();
    }

    public void setRename(String rename) {
        if(rename.contains("ServerFiles"))
            this.rename=new File(rename);
        else
            this.rename=new File("ServerFiles"+File.separator+rename);
    }

    public boolean renameFile(String nname){
        if(nname.contains("ServerFiles"))
            return rename.renameTo(new File(nname));

        return rename.renameTo(new File("ServerFiles"+File.separator+nname));
    }
}
