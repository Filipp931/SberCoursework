package mailer.sender;

public interface EmailSenderService {
    /**
     * Отправить сообщение
     * @param message сообщение
     * @param email адрес для отправки
     */
    void send(String message, String email);
}
