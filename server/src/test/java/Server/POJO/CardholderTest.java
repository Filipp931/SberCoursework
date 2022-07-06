package Server.POJO;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit4.class)
class CardholderTest {
    Cardholder cardholder1 = new Cardholder();
    Card card1;

    @Test
    void getAndSetId() {
        cardholder1.setId(1L);
        assertEquals(cardholder1.getId(), 1L);
    }

    @Test
    void getAndSetCards() {
        List<Card> cards = new ArrayList<>();
        card1 = new Card(cardholder1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
        Card card2 = new Card(new Cardholder(), LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123458L);
        cards.add(card1);
        cards.add(card2);
        cardholder1.setCards(cards);
        assertEquals(cardholder1.getCards(), cards);
    }

    @Test
    void getAndSetName() {
        cardholder1.setName("Name");
        assertEquals(cardholder1.getName(), "Name");
    }

    @Test
    void getAndSetSurname() {
        cardholder1.setSurname("Surname");
        assertEquals(cardholder1.getSurname(), "Surname");
    }

    @Test
    void getAndSetPatronymic() {
        cardholder1.setPatronymic("Patronymic");
        assertEquals(cardholder1.getPatronymic(), "Patronymic");
    }

    @Test
    void getPhoneNumber() {
        long phoneNumber = 1234567890123456L;
        cardholder1.setPhoneNumber(phoneNumber);
        assertEquals(cardholder1.getPhoneNumber(), phoneNumber);
    }

    @Test
    void getAndSetEmail() {
        String email = "email@email";
        cardholder1.setEmail(email);
        assertEquals(cardholder1.getEmail(), email);
    }

    @Test
    void testToString() {
        String name = "Name";
        String surname = "Surname";
        String patronymic = "Patronymic";
        String email = "email@email";
        long phoneNumber = 89888991324L;
        long id = 1L;
        cardholder1 = new Cardholder(name, surname, patronymic, phoneNumber, email);
        cardholder1.setId(id);
        String cardholder = cardholder1.toString();
        assertTrue(cardholder.contains(name) &&
                cardholder.contains(surname) &&
                cardholder.contains(patronymic) &&
                cardholder.contains(email) &&
                cardholder.contains(String.valueOf(id)) &&
                cardholder.contains(String.valueOf(phoneNumber)));
    }

    @Test
    void testEquals() {
        cardholder1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        cardholder1.setId(1L);
        Cardholder cardholder2 = new Cardholder(
                cardholder1.getName(),
                cardholder1.getSurname(),
                cardholder1.getPatronymic(),
                cardholder1.getPhoneNumber(),
                cardholder1.getEmail());
        cardholder2.setId(cardholder1.getId());
        assertEquals(cardholder1, cardholder2);
    }
}