package mailer.messageSender;

import org.junit.jupiter.api.Test;

class EmailSenderServiceImplTest {
    EmailSenderService emailSenderService = new EmailSenderServiceImpl();

    @Test
    public void sendMessageTest(){
        emailSenderService.send("","");
    }
}