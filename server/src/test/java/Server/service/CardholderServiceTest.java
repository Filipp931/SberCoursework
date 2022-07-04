package Server.service;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.repository.CardRepository;
import Server.repository.CardholderRepository;
import Server.service.exceptions.CardholderAlreadyExistsException;
import Server.service.exceptions.CardholderNotFoundException;
import Server.service.impl.CardholderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardholderServiceTest {

    @Mock
    CardholderRepository cardholderRepository;
    @Mock
    CardRepository cardRepository;
    @InjectMocks
    private CardholderServiceImpl cardholderService;

    Cardholder cardholder1;
    Cardholder cardholder2;
    Card card1;
    Card card2;

    @BeforeEach
    public void init(){
        cardholder1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        cardholder1.setId(1L);
        cardholder2 = new Cardholder("Sergej", "Sergeev", "Sergeevich", 89888991324L, "test@mail.ru");
        cardholder2.setId(2L);
        card1 = new Card(cardholder1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
        card2 = new Card(cardholder2, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123458L);
    }


    @Test
    void deleteNotExistsCardholder() {
        when(cardholderRepository.existsById(1L)).thenReturn(false);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.delete(1L));
    }

    @Test
    void getByIdNotExistsCardholder() {
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.getById(1L));
    }

    @Test
    void getById() throws CardholderNotFoundException {
        when(cardholderRepository.findById(cardholder1.getId())).thenReturn(Optional.of(cardholder1));
        assertEquals(cardholder1, cardholderService.getById(cardholder1.getId()));
    }

    @Test
    void getAll() {
        List<Cardholder> cardholders = new ArrayList<>();
        cardholders.add(cardholder1);
        cardholders.add(cardholder2);
        when(cardholderRepository.findAll()).thenReturn(cardholders);
        List<Cardholder> cardholdersList = cardholderService.getAll();
        assertEquals(cardholders, cardholdersList);
    }

    @Test
    void getCardsOfNotExistsCardholder() {
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.getCards(1L));
    }

    @Test
    void getCards() throws CardholderNotFoundException {
        cardholder1.setCards(Collections.singletonList(card1));
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder1));
        assertEquals(cardholderService.getCards(cardholder1.getId()), cardholder1.getCards());
    }

    @Test
    void addNewAlreadyExistsCardholder() {
        when(cardholderRepository.existsByNameAndSurnameAndPatronymicAndPhoneNumber(
                cardholder1.getName(),
                cardholder1.getSurname(),
                cardholder1.getPatronymic(),
                cardholder1.getPhoneNumber()
        )).thenReturn(true);
        assertThrows(CardholderAlreadyExistsException.class, () -> cardholderService.addNewCardholder(cardholder1));
    }

    @Test
    void addNewCardToNotExistsCardholder() {
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.addNewCard(1L, new Card()));
    }

    @Test
    void addNewCardTest() throws CardholderNotFoundException {
        when(cardholderRepository.findById(cardholder1.getId())).thenReturn(Optional.of(cardholder1));
        when(cardRepository.findByNumber(card1.getNumber())).thenReturn(card1);
        assertEquals(cardholderService.addNewCard(cardholder1.getId(),card1), card1);
    }

    @Test
    void getByName() throws CardholderNotFoundException {
        when(cardholderRepository.getByName(
                    cardholder1.getName(),
                    cardholder1.getSurname(),
                    cardholder1.getPatronymic())).
                thenReturn(cardholder1);
        assertEquals(cardholderService.getByName(
                    cardholder1.getName(),
                    cardholder1.getSurname(),
                    cardholder1.getPatronymic()),
                cardholder1);
    }

    @Test
    void getByNameNotExistsCardholder() {
        when(cardholderRepository.getByName(
                cardholder1.getName(),
                cardholder1.getSurname(),
                cardholder1.getPatronymic())).thenReturn(null);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.getByName(
                cardholder1.getName(),cardholder1.getSurname(),cardholder1.getPatronymic()));
    }

    @Test
    void getByPhoneNumber() throws CardholderNotFoundException {
        when(cardholderRepository.getByPhoneNumber(cardholder1.getPhoneNumber())).thenReturn(cardholder1);
        assertEquals(cardholderService.getByPhoneNumber(cardholder1.getPhoneNumber()), cardholder1);
    }

    @Test()
    void getByPhoneNumberNotExistsCardholder() {
        when(cardholderRepository.getByPhoneNumber(cardholder1.getPhoneNumber())).thenReturn(null);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.getByPhoneNumber(cardholder1.getPhoneNumber()));
    }

    @Test
    void updateNotExistsCardholder() {
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.update(new Cardholder(), 1L));
    }
}