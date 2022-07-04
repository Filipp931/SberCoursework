package mailer.messageSender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@RunWith(JUnit4.class)
class EmailSenderServiceImplTest {
    private final EmailSenderService emailSenderService = new EmailSenderServiceImpl();
    Path path;

    @Test
    public void sendMessageTest() throws IOException {
        emailSenderService.send("message", "testMail");
        Path expectedFile = Paths.get("mail/"+"testMail"+".txt");
        String result = Files.readString(expectedFile);
        Assertions.assertTrue(result.contains("message"));
        Files.delete(expectedFile);
    }

    @Test
    public void createFileTest() throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException {
        Method createFileMethod = Arrays.stream(
                emailSenderService.getClass().getDeclaredMethods()).
                filter(method -> method.getName().equals("createFile")).
                findAny().orElseThrow(NoSuchMethodException::new);
        createFileMethod.setAccessible(true);
        path = (Path) createFileMethod.invoke(emailSenderService, "name");
        Assertions.assertTrue(Files.exists(path) && Files.isRegularFile(path));
        Files.delete(path);
    }


}