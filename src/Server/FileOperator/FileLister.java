package Server.FileOperator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLister {
    private List<String> filesNames;

    public FileLister() {
        filesNames=new ArrayList<>();
    }

    public List<String> listFiles(){
        try (Stream<Path> walk= Files.walk(Paths.get("ServerFiles"))){
            filesNames=walk.map(Path::toString).collect(Collectors.toList());
        }catch (IOException e){
            e.printStackTrace();
        }

        return filesNames;
    }
}
