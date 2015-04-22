import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by MatthewRowe on 4/21/15.
 */
public class test {

    public static void main(String[] args) {
        try {
            FileReader reader = new FileReader("../javaFinalProjectKeysAndSecrets/spotifyKeys.txt");
            BufferedReader bufReader = new BufferedReader(reader);
            String line = bufReader.readLine();
            String[] lineArray = line.split(",");
            System.out.println(lineArray[0]);
            line = bufReader.readLine();
            lineArray = line.split(",");
            System.out.println(lineArray[0]);
            reader.close();
        } catch  (Exception e) {
            System.out.println("Problem reading files");
        }
    }
}
