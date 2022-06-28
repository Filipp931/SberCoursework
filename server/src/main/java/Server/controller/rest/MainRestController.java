package Server.controller.rest;

import Server.POJO.Card;
import Server.service.CardService;
import Server.service.CardholderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/rest")
@RestController
public class MainRestController {
    final CardholderService cardholderService;
    final CardService cardService;

    public MainRestController(CardholderService cardholderService, CardService cardService) {
        this.cardholderService = cardholderService;
        this.cardService = cardService;
    }

    @GetMapping("/getAllExpiredCards")
    List<Card> getAllExpiredCards()  {
        return cardService.getAllExpired();
    }

}
