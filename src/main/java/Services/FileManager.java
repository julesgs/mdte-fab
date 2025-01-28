package Services;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    // ========================== ÉCRITURE ==========================

    public void write(String filePath, String content) {
        File file = new File(filePath);

        String id = content.split(";")[0];

        for (String line : readLines(filePath)) {
            if (line.startsWith(id)) {
                System.out.println("Existe déjà : " + line);
                return;
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




    // ========================== LECTURE ==========================

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

    public void readFTP() throws IOException {
        String server = "ftpperso.free.fr";
        int port = 21;
        String user = "pottarn";
        String pass = "cydeaxch0";

        FTPClient ftpClient = new FTPClient();

        try {
            // Connexion au serveur FTP
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Chemin du fichier à lire
            String ftpPath = "/CCI/readme.txt";

            // Téléchargement du fichier en mémoire
            InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);
            if (inputStream != null) {
                // Lecture du contenu du fichier
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                ftpClient.completePendingCommand();
            } else {
                System.out.println("Impossible d'ouvrir le fichier !");
            }

            // Déconnexion du serveur
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
