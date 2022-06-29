package mailer.messageSender;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import Server.POJO.Card;
import Server.POJO.Cardholder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
class EmailSenderServiceImplTest {
    EmailSenderService emailSenderService = new EmailSenderServiceImpl();
    Path path;

    @Test
    public void sendMessageTest() throws IOException {
        emailSenderService.send("message", "testMail");
        Path expectedFile = Paths.get("mail/"+"testMail"+".txt");
        String result = Files.readString(expectedFile);
        Assert.assertTrue(result.contains("message"));
        Files.delete(expectedFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createFileTest() throws InvocationTargetException, IllegalAccessException, IOException {
        Method createFileMethod = Arrays.stream(
                emailSenderService.getClass().getDeclaredMethods()).
                filter(method -> method.getName().equals("createFile")).
                findAny().get();
        createFileMethod.setAccessible(true);
        path = (Path) createFileMethod.invoke(emailSenderService, "name");
        Assert.assertTrue(Files.exists(path) && Files.isRegularFile(path));
        Files.delete(path);
    }


}