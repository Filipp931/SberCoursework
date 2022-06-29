package mailer.service;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import mailer.DAO.CardDao;
import mailer.messageSender.EmailSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MailerServiceTest {
    @Mock
    CardDao cardDao;
    @Mock
    EmailSenderService emailSenderService;
    @InjectMocks
    MailerService mailerService;
    private List<Card> cards;

    @BeforeEach
    public void init(){
        cards = new ArrayList<>();
        Card card = new Card(1L, "2020-01-01", "2022-01-01", 1234567890123456L);
        Cardholder cardholder = new Cardholder("Ivanov", "Ivan", "Ivanovich", 89888991324L, "mail");
        card.setCardholder(cardholder);
        cards.add(card);
    }

    @Test
    public void checkCardAndNotifyCardholdersTest(){
        when(cardDao.getAllExpiredCards()).thenReturn(cards);
        mailerService.checkCardAndNotifyCardholders();
        verify(emailSenderService, times(cards.size())).send(any(), any());
    }

    @Test
    public void checkCardAndNotifyCardholdersNoExpiredCardsTest(){
        when(cardDao.getAllExpiredCards()).thenReturn(new ArrayList<>());
        mailerService.checkCardAndNotifyCardholders();
        verify(emailSenderService, times(0)).send(any(), any());
    }
}