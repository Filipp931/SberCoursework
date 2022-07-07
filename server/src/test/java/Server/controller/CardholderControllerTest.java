package Server.controller;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.main.Server;
import Server.service.CardService;
import Server.service.CardholderService;
import Server.service.exceptions.CardholderAlreadyExistsException;
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
class CardholderControllerTest {
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
        cardholder2 = new Cardholder("Sergej", "Sergeev", "Sergeevich", 89888991324L, "test@mail.ru");
        cardholder2.setId(2L);
        card1 = new Card(cardholder1, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123456L);
        card2 = new Card(cardholder2, LocalDate.now().toString(), LocalDate.now().toString(), 1234567890123458L);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void all() throws Exception {
        List<Cardholder> cardholders = new ArrayList<>();
        cardholders.add(cardholder1);
        cardholders.add(cardholder2);
        when(cardholderService.getAll()).thenReturn(cardholders);
        mockMvc.perform(get("/cardholder/all")).
                andExpect(status().isOk()).
                andExpect(model().attribute("cardholders", cardholders));
    }

    @Test
    void byId() throws Exception {
        when(cardholderService.getById(cardholder1.getId())).thenReturn(cardholder1);
        mockMvc.perform(get("/cardholder/" + cardholder1.getId())).
                andExpect(status().isOk()).
                andExpect(model().attribute("cardholder", cardholder1));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(cardholderService).delete(cardholder1.getId());
        mockMvc.perform(get("/cardholder/delete?id=" + cardholder1.getId())).
                andExpect(status().is(302));
        Mockito.verify(cardholderService, times(1)).delete(cardholder1.getId());
    }

    @Test
    void getCards() throws Exception {
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        when(cardholderService.getById(cardholder1.getId())).thenReturn(cardholder1);
        cardholder1.setCards(cards);
        mockMvc.perform(get("/cardholder/" + cardholder1.getId() + "/cards")).
                andExpect(status().isOk()).
                andExpect(model().attribute("cards", cards));
    }

    @Test
    void newCardholder() throws Exception {
        Class<?> modelCardholder = Objects.requireNonNull(mockMvc.perform(get("/cardholder/addNewCardholder")).
                andExpect(status().isOk()).
                andReturn().getModelAndView()).getModel().get("cardholder").getClass();
        Assertions.assertEquals(modelCardholder, Cardholder.class);
    }

    @Test
    void createCardholder() throws Exception {
        doNothing().when(cardholderService).addNewCardholder(cardholder1);
        mockMvc.perform(post("/cardholder/create").flashAttr("cardholder", cardholder1)).
                andExpect(status().is(302));
        Mockito.verify(cardholderService, times(1)).addNewCardholder(cardholder1);
    }
    @Test
    void createAlreadyExistsCardholder() throws Exception {
        Exception e = new CardholderAlreadyExistsException(cardholder1);
        doThrow(e).when(cardholderService).addNewCardholder(cardholder1);
        Assertions.assertEquals(Objects.requireNonNull(mockMvc.perform(post("/cardholder/create").flashAttr("cardholder", cardholder1)).
                andExpect(status().is(200)).
                andReturn().
                getModelAndView()).
                getModel().
                get("message").
                toString(), e.getMessage());
        Mockito.verify(cardholderService, times(1)).addNewCardholder(cardholder1);
    }

    @Test
    void edit() throws Exception {
        when(cardholderService.getById(cardholder1.getId())).thenReturn(cardholder1);
        Object o = Objects.requireNonNull(mockMvc.perform(get("/cardholder/" + cardholder1.getId() + "/update")).
                andExpect(status().is(200)).
                andReturn().
                getModelAndView()).
                getModel().get("cardholder");
        Assertions.assertEquals(o, cardholder1);
    }

    @Test
    void editNotExistsCardholder() throws Exception {
        Exception e = new CardholderNotFoundException("id", cardholder1.getId());
        when(cardholderService.getById(cardholder1.getId())).thenThrow(e);
        String message = Objects.requireNonNull(mockMvc.perform(get("/cardholder/" + cardholder1.getId() + "/update")).
                andExpect(status().is(200)).
                andReturn().
                getModelAndView()).
                getModel().get("message").toString();
        Assertions.assertTrue(message.contains(e.getMessage()));
    }

    @Test
    void updateNotExists() throws Exception {
        Exception e = new CardholderNotFoundException("id", cardholder1.getId());
        doThrow(e).when(cardholderService).update(cardholder1, 1L);
        String message = Objects.requireNonNull(mockMvc.perform(post("/cardholder/" + cardholder1.getId()).flashAttr("cardholder", cardholder1)).
                andExpect(status().is(200)).
                andReturn().
                getModelAndView()).
                getModel().get("message").toString();
        Assertions.assertTrue(message.contains(e.getMessage()));
    }
    @Test
    void  update() throws Exception {
        doNothing().when(cardholderService).update(cardholder1, 1L);
        mockMvc.perform(post("/cardholder/" + cardholder1.getId()).flashAttr("cardholder", cardholder1)).
                andExpect(status().is(302));
        Mockito.verify(cardholderService, times(1)).update(cardholder1, 1L);
    }


}