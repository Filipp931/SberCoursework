package mailer.messageSender;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageTest {

    @Mock
    Cardholder cardholder;
    @Mock
    Card card;

    @Test
    public void messageTest(){
        when(cardholder.getName()).thenReturn("Ivan");
        when(cardholder.getSurname()).thenReturn("Ivanov");
        when(cardholder.getPatronymic()).thenReturn("Ivanovich");
        when(card.getCardholder()).thenReturn(cardholder);
        when(card.getNumber()).thenReturn(1234567890123456L);
        when(card.getExpirationDate()).thenReturn("2022-01-01");
        String result = Message.getMessage(card);
        assertTrue( result.contains(cardholder.getName()) &&
                result.contains(cardholder.getSurname()) &&
                result.contains(cardholder.getPatronymic()) &&
                result.contains(card.getNumber().toString()) &&
                result.contains(card.getExpirationDate()));
    }

}