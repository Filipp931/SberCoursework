package Server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    /**
     * основная страница
     */
    @GetMapping( "/")
    String main(){
        return "mainView";
    }
}
