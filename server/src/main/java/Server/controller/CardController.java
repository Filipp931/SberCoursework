package Server.controller;

import Server.POJO.Card;
import Server.service.CardService;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping( "/card")
@Controller
public class CardController {
    final
    CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Получение списка всех карт
     * @return  List<Card>
     */
    @GetMapping( "/all")
    String all(Model model){
        model.addAttribute("cards", cardService.getAll());
        return "card/all";
    }
    /**
     * Получение карты по ее Id
     * @param id
     * @return card
     */
    @GetMapping("/{id}")
    String byId(@PathVariable("id") long id, Model model) throws CardNotFoundException {
        model.addAttribute("card" ,cardService.getById(id));
        return "card/byId";
    }

    @GetMapping("/{id}/delete")
    String delete(@PathVariable("id") long id) throws CardNotFoundException {
        cardService.delete(id);
        return "redirect:card/all";
    }

    @GetMapping("/addNewCard{id}")
    String newCard(@PathVariable("id") long cardholderId, Model model){
        Card card = new Card();
        card.setCardholderId(cardholderId);
        model.addAttribute("card", card);
        return "card/addNewCard";
    }

    @PostMapping("/create")
    String createCard(@ModelAttribute("card") Card card) throws CardholderNotFoundException, CardAlreadyExistsException {
        cardService.addNewCard(card);
        return "redirect:/all";
    }

}
