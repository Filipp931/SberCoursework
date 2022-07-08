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

    /**
     * Отправка сообщения (в данном случае запись в файл)
     * @param message сообщение
     * @param email адрес для отправки
     */
    @Override
    public void send(String message, String email) {
        try {
            Path file = createFile(email);
            Files.writeString(file, message);
        } catch (IOException e) {
            logger.error("Error sending message", e);
        }
    }

    /**
     * Создание файла для записи сообщения
     * @param fileName - имя файла без расширения
     * @return - пцть к файлу
     * @throws IOException - при ошибке чтения/записи
     */
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
