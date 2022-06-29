package mailer.messageSender;

import Server.POJO.Card;

/**
 * Вспомагательный класс для формирования текствого уведомления о просроченной карте
 */
public class Message {

    /**
     * Получение уведомления о просроченной карте
     * @param expiredCard - просроченная карта
     * @return сообщение для отправки
     */
    public static String getMessage(Card expiredCard){
        String template = "Dear %s %s %s!\n" +
                "\tYour card number %d has expired %s.";
        return String.format(template,
                expiredCard.getCardholder().getSurname(),
                expiredCard.getCardholder().getName(),
                expiredCard.getCardholder().getPatronymic(),
                expiredCard.getNumber(),
                expiredCard.getExpirationDate());
    }
}
