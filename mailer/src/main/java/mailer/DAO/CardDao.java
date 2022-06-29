package mailer.DAO;


import Server.POJO.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Доступ к данным сервера посредством http
 */
@Service
public class CardDao {
    private final WebClient webClient;
    private final Gson gson;
    private final Logger logger = (Logger) LogManager.getLogger(CardDao.class);

    public CardDao(WebClient webClient, Gson gson) {
        this.webClient = webClient;
        this.gson = gson;
    }

    /**
     * Получение списка всех просроченных карт
     * @return список просроченных карт
     */
    public List<Card> getAllExpiredCards(){
        String ALL_EXPIRED = "rest/getAllExpiredCards";
        String response;
        try {
            response = webClient.get().uri(ALL_EXPIRED).retrieve().bodyToMono(String.class).block();
        } catch (Exception e) {
            logger.error("Error getting list cards from server", e);
            return new ArrayList<>();
        }
        return gson.fromJson(response, TypeToken.getParameterized(List.class, Card.class).getType());
    }
}
