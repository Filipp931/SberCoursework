package mailer.messageSender;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Override
    public void send(String message, String email) {
        try {
            Path file = createFile(email);
            Files.writeString(file, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path createFile(String fileName) throws IOException {
        Path directory = Paths.get("mail/");
        if(!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
        Path file = Paths.get("mail/" + fileName + ".txt");
        if(Files.exists(file) && Files.isRegularFile(file)) {
            Files.delete(file);
        }
        Files.createFile(file);
        return file;
    }

}
