package Server.service;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.repository.CardRepository;
import Server.repository.CardholderRepository;
import Server.service.exceptions.CardholderAlreadyExistsException;
import Server.service.exceptions.CardholderNotFoundException;
import Server.service.impl.CardholderServiceImpl;
import com.google.gson.Gson;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CardholderServiceTest {

    @Mock
    CardholderRepository cardholderRepository;
    @Mock
    CardRepository cardRepository;
    @InjectMocks
    private CardholderServiceImpl cardholderService;

    @Test
    void deleteNotExistsCardholder() {
        when(cardholderRepository.existsById(1L)).thenReturn(false);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.delete(1L));
    }

    @Test
    void getByIdNotExistsCardholder() {
        when(cardholderRepository.existsById(1L)).thenReturn(false);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.getById(1L));
    }

    @Test
    void getById() throws CardholderNotFoundException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(cardholderRepository.existsById(1L)).thenReturn(true);
        assertEquals(c1, cardholderService.getById(1L));
    }

    @Test
    void getAll() {
        List<Cardholder> cardholders = new ArrayList<>();
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Cardholder c2 = new Cardholder("Sergej", "Sergeev", "Sergeevich", 89888991324L, "test@mail.ru");
        c2.setId(2L);
        cardholders.add(c1);
        cardholders.add(c2);
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
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(c1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
        c1.setCards(Collections.singletonList(card));
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(c1));
        assertEquals(cardholderService.getCards(1L), c1.getCards());
    }

    @Test
    void addNewAlreadyExistsCardholder() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        when(cardholderRepository.existsByNameAndSurnameAndPatronymicAndPhoneNumber(
                c1.getName(),
                c1.getSurname(),
                c1.getPatronymic(),
                c1.getPhoneNumber()
        )).thenReturn(true);
        assertThrows(CardholderAlreadyExistsException.class, () -> cardholderService.addNewCardholder(c1));
    }

    @Test
    void addNewAlreadyExistsCardholderJson() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Gson gson = new Gson();
        String cardholderJson = gson.toJson(c1);
        when(cardholderRepository.existsByNameAndSurnameAndPatronymicAndPhoneNumber(
                c1.getName(),
                c1.getSurname(),
                c1.getPatronymic(),
                c1.getPhoneNumber()
        )).thenReturn(true);
        assertThrows(CardholderAlreadyExistsException.class, () -> cardholderService.addNewCardholder(cardholderJson));
    }

    @Test
    void addNewCardholderJson() throws CardholderAlreadyExistsException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Gson gson = new Gson();
        String cardholderJson = gson.toJson(c1);
        when(cardholderRepository.existsByNameAndSurnameAndPatronymicAndPhoneNumber(
                c1.getName(),
                c1.getSurname(),
                c1.getPatronymic(),
                c1.getPhoneNumber()
        )).thenReturn(false);
        when(cardholderRepository.getByName(
                c1.getName(),
                c1.getSurname(),
                c1.getPatronymic()
        )).thenReturn(c1);
        assertEquals(c1, cardholderService.addNewCardholder(cardholderJson));
    }

    @Test
    void addNewCardToNotExistsCardholder() {
        when(cardholderRepository.existsById(1L)).thenReturn(false);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.addNewCard(1L, new Card()));
    }

    @Test
    void addNewCardTest() throws CardholderNotFoundException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        Card card = new Card(c1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
        when(cardholderRepository.existsById(1L)).thenReturn(true);
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(cardRepository.findByNumber(card.getNumber())).thenReturn(card);
        assertEquals(cardholderService.addNewCard(1L,card), card);
    }

    @Test
    void getByName() throws CardholderNotFoundException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        when(cardholderRepository.getByName("Ivan", "Ivanov", "Ivanovich")).thenReturn(c1);
        assertEquals(cardholderService.getByName("Ivan", "Ivanov", "Ivanovich"), c1);
    }

    @Test
    void getByNameNotExistsCardholder() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        when(cardholderRepository.getByName(
                c1.getName(),
                c1.getSurname(),
                c1.getPatronymic())).thenReturn(null);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.getByName(c1.getName(),c1.getSurname(),c1.getPatronymic()));
    }

    @Test
    void getByPhoneNumber() throws CardholderNotFoundException {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        when(cardholderRepository.getByPhoneNumber(89888991324L)).thenReturn(c1);
        assertEquals(cardholderService.getByPhoneNumber(89888991324L), c1);
    }

    @Test()
    void getByPhoneNumberNotExistsCardholder() {
        Cardholder c1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        c1.setId(1L);
        when(cardholderRepository.getByPhoneNumber(89888991324L)).thenReturn(null);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.getByPhoneNumber(89888991324L));
    }

    @Test
    void updateNotExistsCardholder() {
        when(cardholderRepository.existsById(1L)).thenReturn(false);
        assertThrows(CardholderNotFoundException.class, () -> cardholderService.update(new Cardholder(), 1L));
    }
}