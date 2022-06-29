package mailer.service;

import Server.POJO.Card;
import mailer.DAO.CardDao;
import mailer.messageSender.Message;
import mailer.messageSender.EmailSenderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Рассылка уведомлений о просроченных картах по расписанию
 */
@Service
public class MailerService {
    private static final Logger logger = (Logger) LogManager.getLogger(MailerService.class);
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
        logger.info("Starting checking cards");
        List<Card> expiredCards = cardDao.getAllExpiredCards();
        if(expiredCards.isEmpty()) {
            logger.warn("List of expired cards is empty");
            return;
        }
        expiredCards.forEach(card -> emailSenderService.send(Message.getMessage(card), card.getCardholder().getEmail()));
    }

}
