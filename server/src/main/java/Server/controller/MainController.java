package Server.controller;

import Server.POJO.Cardholder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    @GetMapping( "/")
    String main(){
        return "mainView";
    }
}
