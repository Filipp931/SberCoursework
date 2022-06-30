package Server.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "Server")
@EnableJpaRepositories("Server.repository")
@EntityScan("Server.POJO")
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
        System.out.println("asda");
    }
}
