package hu.berlin.file;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Duc on 09.01.17.
 */
public class FileLoader {

    public static File loadRessource(String path) throws FileNotFoundException {
        URL location = FileLoader.class.getClassLoader().getResource(path);
        if (location == null) {
            FileNotFoundException ex = new FileNotFoundException("Ressource cant be found at path " + location.getPath());
            throw ex;
        }

        String dirPath = location.getFile();
        File dir = new File(dirPath);

        return dir;
    }

    public static String loadContentOfFile(String path) throws IOException {
        File file = FileLoader.loadRessource(path);
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        return new String(bytes, StandardCharsets.UTF_8);
    }

}
