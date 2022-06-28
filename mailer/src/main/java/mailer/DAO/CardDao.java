package mailer.DAO;


import Server.POJO.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Доступ к данным сервера посредством http
 */
@Service
public class CardDao {
    private final WebClient webClient;
    private final Gson gson;

    public CardDao(WebClient webClient, Gson gson) {
        this.webClient = webClient;
        this.gson = gson;
        System.out.println("created");
    }

    /**
     * Получение списка всех просроченных карт
     * @return список просроченных карт
     */
    public List<Card> getAllExpiredCards(){
        String ALL_EXPIRED = "rest/getAllExpiredCards";
        String response = webClient.get().uri(ALL_EXPIRED).retrieve().bodyToMono(String.class).block();
        return gson.fromJson(response, TypeToken.getParameterized(List.class, Card.class).getType());
    }
}
