package mailer.sender;

import org.springframework.stereotype.Service;

@Service
public class EmailEmailSenderServiceImpl implements EmailSenderService {

    @Override
    public void send(String message, String email) {
        System.out.println(message);
    }
}
