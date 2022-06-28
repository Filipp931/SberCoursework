package Server.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@ComponentScan("Server")
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
        System.out.println("asda");
    }
}
