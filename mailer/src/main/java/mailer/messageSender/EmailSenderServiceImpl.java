package mailer.messageSender;

import mailer.DAO.CardDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final Logger logger = (Logger) LogManager.getLogger(CardDao.class);
    @Override
    public void send(String message, String email) {
        try {
            Path file = createFile(email);
            Files.writeString(file, message);
        } catch (IOException e) {
            logger.error("Error sending message", e);
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
