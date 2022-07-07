package Server.controller;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.main.Server;
import Server.service.CardService;
import Server.service.CardholderService;
import Server.service.exceptions.CardholderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private void init() throws Exception {
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
    void byIdExistsTest() throws Exception {
        when(cardholderService.getById(cardholder1.getId())).thenReturn(cardholder1);
        mockMvc.perform(get("/cardholder/" + cardholder1.getId())).
                andExpect(status().isOk()).
                andExpect(model().attribute("cardholder", cardholder1));
    }

    @Test
    void delete() {
    }

    @Test
    void newCard() {
    }

    @Test
    void createCard() {
    }
}