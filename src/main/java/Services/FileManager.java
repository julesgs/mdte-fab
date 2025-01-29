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

    public void write(String filePath, String content, boolean isFTP) {
        if (!isFTP) {
            // Gestion de l'écriture en local
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
                fos.write(System.lineSeparator().getBytes()); // Ajouter un saut de ligne
            } catch (FileNotFoundException e) {
                System.out.println("Fichier introuvable : " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException("Erreur d'écriture dans le fichier : " + e.getMessage(), e);
            }
        } else {
            String server = "ftpperso.free.fr";
            int port = 21;
            String user = "pottarn";
            String pass = "cydeaxch0";

            FTPClient ftpClient = new FTPClient();

            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                String ftpPath = "/CCI/" + filePath;

                String id = content.split(";")[0];
                InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);

                if (inputStream != null) {
                    boolean exists = false;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith(id)) {
                                System.out.println("Existe déjà : " + line);
                                exists = true;
                                break;
                            }
                        }
                    }
                    ftpClient.completePendingCommand();

                    if (exists) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                        return;
                    }
                }

                InputStream inputStreamContent = new ByteArrayInputStream((content + System.lineSeparator()).getBytes());
                boolean appended = ftpClient.appendFile(ftpPath, inputStreamContent);
                inputStreamContent.close();

                if (appended) {
                    System.out.println("Écriture réussie sur le serveur FTP.");
                } else {
                    System.out.println("Échec de l'écriture sur le serveur FTP.");
                    System.out.println("Réponse FTP : " + ftpClient.getReplyString());
                }

                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                System.out.println("Erreur lors de l'écriture sur le serveur FTP : " + e.getMessage());
            }
        }
    }




    // ========================== LECTURE ==========================

    public String read(String filePath, boolean isFTP) {

        StringBuilder content = new StringBuilder();

        if (!isFTP) {
            File file = new File(filePath);

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
        } else {
            String server = "ftpperso.free.fr";
            int port = 21;
            String user = "pottarn";
            String pass = "cydeaxch0";

            FTPClient ftpClient = new FTPClient();

            try {

                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                String ftpPath = "/CCI/" + filePath;

                InputStream inputStream = ftpClient.retrieveFileStream(ftpPath);
                if (inputStream != null) {

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                    }
                    ftpClient.completePendingCommand();
                } else {
                    System.out.println("Impossible d'ouvrir le fichier !");
                }

                ftpClient.logout();
                ftpClient.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return content.toString().trim();
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
