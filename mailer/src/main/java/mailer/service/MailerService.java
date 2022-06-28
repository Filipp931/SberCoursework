package mailer.service;

import Server.POJO.Card;
import mailer.DAO.CardDao;
import mailer.sender.Message;
import mailer.sender.EmailSenderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Рассылка уведомлений о просроченных картах по расписанию
 */
@Service
public class MailerService {
    final CardDao cardDao;
    final EmailSenderService emailSenderService;

    public MailerService(CardDao cardDao, EmailSenderService emailSenderService) {
        this.cardDao = cardDao;
        this.emailSenderService = emailSenderService;
    }

    /**
     * Рассылка уведомлений
     */
    @Scheduled(fixedRate = (24*60*60*1000))
    public void checkCardAndNotifyCardholders() {
        List<Card> expiredCards = cardDao.getAllExpiredCards();
        expiredCards.forEach(card -> emailSenderService.send(Message.getMessage(card), card.getCardholder().getEmail()));
    }

}
