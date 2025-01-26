package Services;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public void write(String filePath, String content) {
        File file = new File(filePath);

        String id = content.split(";")[0];

        for (String line : readLines(filePath)){
            if (line.startsWith(id)){
                System.out.println("Existe deja" + line);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            System.out.println("Fichier introuvable : " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'écriture dans le fichier : " + e.getMessage(), e);
        }
    }

    public void writeFTP(String filePath, String content) throws IOException {

        URL url = new URL("ftp://username:password@www.superland.example");
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }


    public String read(String filePath) {
        File file = new File(filePath);
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier introuvable : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier : " + e.getMessage());
        }

        return content.toString().trim();
    }

    public List<String> readLines(String filePath) {
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier introuvable : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier : " + e.getMessage());
        }
        return lines;
    }
}
