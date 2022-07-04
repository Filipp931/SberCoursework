package Server.service;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.repository.CardRepository;
import Server.repository.CardholderRepository;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;
import Server.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    CardholderRepository cardholderRepository;
    @Mock
    CardRepository cardRepository;
    @InjectMocks
    private CardServiceImpl cardService;

    private Cardholder cardholder1;
    private Card card1;
    private Card card2;

    @BeforeEach
    public void init(){
        cardholder1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        cardholder1.setId(1L);
        card1 = new Card(cardholder1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
        card2 = new Card(cardholder1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123458L);
        card1.setCardholder(cardholder1);
    }

    @Test
    void deleteNotExistsCardTest() {
        assertThrows(CardNotFoundException.class, () -> cardService.delete(cardholder1.getId()));
    }

    @Test
    void addNewCardToNotExistsCardholderTest() {
        assertThrows(CardholderNotFoundException.class, () -> cardService.addNewCard(card1));
    }

    @Test
    void addNewAlreadyExistsCardTest() {
        when(cardholderRepository.existsById(card1.getCardholder().getId())).thenReturn(true);
        when(cardRepository.existsByNumber(card1.getNumber())).thenReturn(true);
        assertThrows(CardAlreadyExistsException.class, () -> cardService.addNewCard(card1));
    }

      @Test
    void getByIdTest() throws CardNotFoundException {
        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));
        assertEquals(cardService.getById(card1.getId()), card1);
    }

    @Test
    void getByIdNotExistsTest() {
        assertThrows(CardNotFoundException.class, () -> cardService.getById(1L));
    }

    @Test
    void getByNumberTest() throws CardNotFoundException {
        when(cardRepository.existsByNumber(card1.getNumber())).thenReturn(true);
        when(cardRepository.findByNumber(card1.getNumber())).thenReturn(card1);
        assertEquals(cardService.getByNumber(card1.getNumber()), card1);
    }

    @Test
    void getByNumberNotExistsTest() {
        assertThrows(CardNotFoundException.class, () -> cardService.getByNumber(1234567890123456L));
    }

    @Test
    void getAllTest() {
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        when(cardRepository.findAll()).thenReturn(cards);
        assertEquals(cardService.getAll(), cards);
    }

    @Test
    void getAllExpiredTest() {
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        when(cardRepository.getAllByExpirationDateBefore(LocalDate.now().toString())).thenReturn(cards);
        assertEquals(cardService.getAllExpired(), cards);
    }

    @Test
    void getCardholderTest() throws CardNotFoundException {
        when(cardRepository.existsById(1L)).thenReturn(true);
        when(cardRepository.getCardHolder(1L)).thenReturn(cardholder1);
        assertEquals(cardService.getCardholder(1L), cardholder1);
    }

    @Test
    void getCardholderNotExistsTest(){
        assertThrows(CardNotFoundException.class, () -> cardService.getById(1L));
    }
}