package mailer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Подготовка web-клиента
 * данные берутся с application.properties
 */
@Configuration
@ComponentScan("mailer")
public class WebClientConfiguration {
    @Value("${BaseUrl}")
    private String BASE_URL;
    @Value("${Login}")
    private String LOGIN;
    @Value("${Password}")
    private String PASSWORD;


    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Authorization", "Basic " + Base64Utils.encodeToString(((LOGIN+":"+PASSWORD)
                        .getBytes())))
                .build();
    }
}
