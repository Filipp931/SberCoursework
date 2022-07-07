package Server.controller;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.main.Server;
import Server.service.CardService;
import Server.service.CardholderService;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Server.class)
class CardControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    Cardholder cardholder1;
    Cardholder cardholder2;
    Card card1;
    Card card2;

    @MockBean
    CardService cardService;
    @MockBean
    CardholderService cardholderService;

    @BeforeEach
    private void init() {
        cardholder1 = new Cardholder("Ivan", "Ivanov", "Ivanovich", 89888991324L, "test@mail.ru");
        cardholder1.setId(1L);
        cardholder2 = new Cardholder("Sergej", "Sergeevich", "Sergeev", 89881231324L, "test1@mail.ru");
        cardholder2.setId(2L);
        card1 = new Card(cardholder1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
        card1.setId(1L);
        card2 = new Card(cardholder2, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123458L);
        card2.setId(2L);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void all() throws Exception {
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cardholder1.setId(1L);
        cardholder2.setId(2L);
        when(cardService.getAll()).thenReturn(cards);
        mockMvc.perform(get("/card/all")).
                andExpect(status().isOk()).
                andExpect(model().attribute("cards", cards));
    }

    @Test
    void byId() throws Exception {
        when(cardService.getById(card1.getId())).thenReturn(card1);
        mockMvc.perform(get("/card/" + card1.getId())).
                andExpect(status().isOk()).
                andExpect(model().attribute("card", card1));
    }
    @Test
    void byIdNotExists() throws Exception {
        Exception e = new CardNotFoundException(card1.getId(), "id");
        when(cardService.getById(card1.getId())).thenThrow(e);
        String message = Objects.requireNonNull(mockMvc.perform(get("/card/" + card1.getId())).
                andExpect(status().isOk()).
                andReturn().getModelAndView()).getModel().get("message").toString();
        Assertions.assertTrue(message.contains(e.getMessage()));
    }


    @Test
    void delete() throws Exception {
        doNothing().when(cardService).delete(card1.getId());
        mockMvc.perform(get("/card/" + card1.getId() +"/delete")).
                andExpect(status().is(302));
        Mockito.verify(cardService, times(1)).delete(card1.getId());
    }

    @Test
    void deleteNotExists() throws Exception {
        Exception e = new CardNotFoundException(card1.getId(), "id");
        doThrow(e).when(cardService).delete(card1.getId());
        String message = Objects.requireNonNull(mockMvc.perform(get("/card/" + card1.getId() + "/delete")).
                andExpect(status().isOk()).
                andReturn().
                getModelAndView()).getModel().get("message").toString();
        Assertions.assertTrue(message.contains(e.getMessage()));
    }

    @Test
    void newCard() throws Exception {
        when(cardholderService.getById(cardholder1.getId())).thenReturn(cardholder1);
        Object card = Objects.requireNonNull(mockMvc.perform(get("/card/addNewCard" + cardholder1.getId())).
                andExpect(status().isOk()).
                andReturn().
                getModelAndView()).getModel().get("card");
        Assertions.assertTrue(card.getClass().equals(Card.class) &&
                ((Card) card).getCardholder().equals(cardholder1));
    }

    @Test
    void newCardCardholderNotFound() throws Exception {
        Exception e = new CardholderNotFoundException("id", cardholder1.getId());
        when(cardholderService.getById(cardholder1.getId())).thenThrow(e);
        String message = Objects.requireNonNull(mockMvc.perform(get("/card/addNewCard" + cardholder1.getId())).
                andExpect(status().isOk()).
                andReturn().
                getModelAndView()).getModel().get("message").toString();
        Assertions.assertTrue(message.contains(e.getMessage()));
    }

    @Test
    void createCardAlreadyExists() throws Exception {
        Exception e = new CardAlreadyExistsException(card1);
        doThrow(e).when(cardService).addNewCard(card1);
        String message = Objects.requireNonNull(mockMvc.perform(post("/card/create").flashAttr("card", card1)).
                andExpect(status().isOk()).
                andReturn().
                getModelAndView()).getModel().get("message").toString();
        Assertions.assertTrue(message.contains(e.getMessage()));
    }

    @Test
    void createCard() throws Exception {
       doNothing().when(cardService).addNewCard(card1);
        mockMvc.perform(post("/card/create").flashAttr("card", card1)).
                andExpect(status().is(302));
        Mockito.verify(cardService, times(1)).addNewCard(card1);
    }
}