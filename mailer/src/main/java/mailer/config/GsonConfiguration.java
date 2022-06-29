package mailer.config;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * Настройка Json парсера
 */
@Configuration
public class GsonConfiguration {
    GsonBuilder gsonBuilder;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GsonConfiguration(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }


    /**
     * Исключение из сериализации поля List<Card> у Cardholder
     */
    private ExclusionStrategy exclusionStrategy(){
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return "cards".equals(fieldAttributes.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        };
    }

    /**
     * Настройка десериализатора объектов типа Card
     */
    @Component
    class CardDeserializer implements JsonDeserializer<Card>{

        @Override
        public Card deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Card card = new Card();
            card.setIssueDate(jsonObject.get("issueDate").getAsString());
            card.setExpirationDate(jsonObject.get("expirationDate").getAsString());
            card.setCardholderId(jsonObject.get("cardholderId").getAsLong());
            card.setNumber(jsonObject.get("number").getAsLong());
            card.setId(jsonObject.get("id").getAsLong());
            JsonElement cardholderJson = jsonObject.get("cardholder");
            Cardholder cardholder  = gsonBuilder.setExclusionStrategies(
                    exclusionStrategy()).create().fromJson(cardholderJson,
                    TypeToken.get(Cardholder.class).getType());
            card.setCardholder(cardholder);
            return card;
        }
    }

    @Bean
    @Autowired
    public Gson gson(CardDeserializer cardDeserializer){
        gsonBuilder
                .registerTypeAdapter(Card.class, cardDeserializer);
        return gsonBuilder.create();
    }

}
