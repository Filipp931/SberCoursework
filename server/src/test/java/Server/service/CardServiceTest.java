package Server.service;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.repository.CardRepository;
import Server.repository.CardholderRepository;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;
import Server.service.impl.CardServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    CardholderRepository cardholderRepository;
    @Mock
    CardRepository cardRepository;
    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void deleteNotExistsCardTest() {
        when(cardRepository.existsById(1L)).thenReturn(false);
        assertThrows(CardNotFoundException.class, () -> cardService.delete(1L));
    }

    @Test
    void addNewCardToNotExistsCardholderTest() {
        Card card = new Card();
        Cardholder cardholderMock = Mockito.mock(Cardholder.class);
        card.setCardholder(cardholderMock);
        when(cardholderMock.getId()).thenReturn(1L);
        when(cardholderRepository.existsById(1L)).thenReturn(false);
        assertThrows(CardholderNotFoundException.class, () -> cardService.addNewCard(card));
    }

    @Test
    void addNewAlreadyExistsCardTest() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        Cardholder cardholderMock = Mockito.mock(Cardholder.class);
        when(cardholderMock.getId()).thenReturn(1L);
        card.setCardholder(cardholderMock);
        when(cardholderRepository.existsById(card.getCardholder().getId())).thenReturn(true);
        when(cardRepository.existsByNumber(card.getNumber())).thenReturn(true);
        assertThrows(CardAlreadyExistsException.class, () -> cardService.addNewCard(card));
    }

    @Test
    void testAddNewCardWithNotExistsCardholderJsonTest() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        Cardholder cardholderMock = Mockito.mock(Cardholder.class);
        when(cardholderMock.getId()).thenReturn(1L);
        card.setCardholder(cardholderMock);
        when(cardRepository.existsByNumber(card.getNumber())).thenReturn(false);
        when(cardholderRepository.existsById(card.getCardholder().getId())).thenReturn(false);
        Gson gson = new Gson();
        String cardJson = gson.toJson(card);
        assertThrows(CardholderNotFoundException.class, () -> cardService.addNewCard(cardJson));
    }

    @Test
    void addNewAlreadyExistsCardJsonTest() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        when(cardRepository.existsByNumber(card.getNumber())).thenReturn(true);
        Gson gson = new Gson();
        String cardJson = gson.toJson(card);
        assertThrows(CardAlreadyExistsException.class, () -> cardService.addNewCard(cardJson));
    }

    @Test
    void addNewCardJsonTest() throws CardholderNotFoundException, CardAlreadyExistsException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        Cardholder cardholderMock = Mockito.mock(Cardholder.class);
        when(cardholderMock.getId()).thenReturn(1L);
        card.setCardholder(cardholderMock);
        when(cardRepository.existsByNumber(card.getNumber())).thenReturn(false);
        when(cardholderRepository.existsById(card.getCardholder().getId())).thenReturn(true);
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(card);
        Gson gson = new Gson();
        String cardJson = gson.toJson(card);
        assertEquals(cardService.addNewCard(cardJson), card);
    }

    @Test
    void getByIdTest() throws CardNotFoundException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        card.setId(1L);
        when(cardRepository.existsById(card.getId())).thenReturn(true);
        when(cardRepository.getById(card.getId())).thenReturn(card);
        assertEquals(cardService.getById(card.getId()), card);
    }

    @Test
    void getByIdNotExistsTest() throws CardNotFoundException {
        assertThrows(CardNotFoundException.class, () -> cardService.getById(1L));
    }

    @Test
    void getByNumberTest() throws CardNotFoundException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        card.setId(1L);
        when(cardRepository.existsByNumber(card.getNumber())).thenReturn(true);
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(card);
        assertEquals(cardService.getByNumber(card.getNumber()), card);
    }

    @Test
    void getByNumberNotExistsTest() {
        assertThrows(CardNotFoundException.class, () -> cardService.getByNumber(1234567890123456L));
    }

    @Test
    void getAllTest() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Cardholder c2 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(2L);
        Card card1 = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        Card card2 = new Card(
                c2,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123476L);
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        when(cardRepository.findAll()).thenReturn(cards);
        assertEquals(cardService.getAll(), cards);
    }

    @Test
    void getAllExpiredTest() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Cardholder c2 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(2L);
        Card card1 = new Card(
                c1,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123456L);
        Card card2 = new Card(
                c2,
                LocalDate.now().toString(),
                LocalDate.now().toString(),
                1234567890123476L);
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        when(cardRepository.getAllByExpirationDateBefore(LocalDate.now().toString())).thenReturn(cards);
        assertEquals(cardService.getAllExpired(), cards);
    }

    @Test
    void getCardholderTest() throws CardNotFoundException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        when(cardRepository.existsById(1L)).thenReturn(true);
        when(cardRepository.getCardHolder(1L)).thenReturn(c1);
        assertEquals(cardService.getCardholder(1L), c1);
    }

    @Test
    void getCardholderNotExistsTest(){
        assertThrows(CardNotFoundException.class, () -> cardService.getById(1L));
    }
}