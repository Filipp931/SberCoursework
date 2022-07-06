package Server.POJO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit4.class)
class CardTest {
    Cardholder cardholder1;
    Card card1;

    @BeforeEach
    public void init(){
        cardholder1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        cardholder1.setId(1L);
        card1 = new Card(cardholder1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
    }

    @Test
    void setAndGetId() {
        card1.setId(1L);
        assertEquals(card1.getId(), 1L);
    }

    @Test
    void getAndSetCardholder() {
        card1.setCardholder(cardholder1);
        assertEquals(card1.getCardholder(), cardholder1);
    }

    @Test
    void getAndSetIssueDate() {
        String issueDate = LocalDate.now().toString();
        card1.setIssueDate(issueDate);
        assertEquals(card1.getIssueDate(), issueDate);
    }

    @Test
    void getAndSetExpirationDate() {
        String expirationDate = LocalDate.now().toString();
        card1.setExpirationDate(expirationDate);
        assertEquals(card1.getExpirationDate(),expirationDate);
    }

    @Test
    void getAndSetNumber() {
        long number = 1984567892134567L;
        card1.setNumber(number);
        assertEquals(card1.getNumber(), number);
    }

    @Test
    void testToString() {
        card1.setId(1L);
        String card = card1.toString();
        assertTrue(card.contains( card1.getId().toString()) &&
                card.contains(card1.getNumber().toString()) &&
                card.contains(card1.getIssueDate()) &&
                card.contains(card1.getExpirationDate()));
    }

    @Test
    void testEquals() {
        Card card2 = new Card(card1.getCardholder(), card1.getIssueDate(), card1.getExpirationDate(), card1.getNumber());
        assertEquals(card2,card1);
    }
}