package mailer.DAO;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardDaoTest {
    @Mock
    WebClient webClientMock;
    @Mock
    WebClient.RequestHeadersUriSpec uriSpecMock;
    @Mock
    WebClient.RequestHeadersSpec headersSpecMock;
    @Mock
    WebClient.ResponseSpec responseSpecMock;

    Gson gson;

    private CardDao cardDao ;

    private List<Card> cards;
    private String cardsJson;

    @BeforeEach
    public void init(){
        gson = new Gson();
        cardDao = new CardDao(webClientMock, gson);
        cards = new ArrayList<>();
        Cardholder cardholder = new Cardholder("Ivanov", "Ivan", "Ivanovich", 89888991324L, "mail");
        cardholder.setId(1L);
        Card card = new Card(cardholder, "2020-01-01", "2022-01-01", 1234567890123456L);
        card.setCardholder(cardholder);
        cards.add(card);
        Gson temp = new Gson();
        cardsJson = temp.toJson(cards);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getAllExpiredCardsTest(){
        when(webClientMock.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(anyString())).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        doReturn(Mono.just(cardsJson)).when(responseSpecMock).bodyToMono(String.class);
        List<Card> result = cardDao.getAllExpiredCards();
        assertEquals(cards, result);
    }

}