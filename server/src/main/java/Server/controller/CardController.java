package Server.controller;

import Server.POJO.Card;
import Server.service.CardService;
import Server.service.CardholderService;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping( "/card")
@Controller
public class CardController {
    final CardService cardService;
    CardholderService cardholderService;

    @Autowired
    public CardController(CardService cardService, CardholderService cardholderService) {
        this.cardService = cardService;
        this.cardholderService = cardholderService;
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
        try {
            card.setCardholder(cardholderService.getById(cardholderId));
        } catch (CardholderNotFoundException e) {
            e.printStackTrace();
        }
        model.addAttribute("card", card);
        return "card/addNewCard";
    }

    @PostMapping("/create")
    String createCard(@ModelAttribute("card") @Valid Card card, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "card/create";
        }
        try {
            cardService.addNewCard(card);
        } catch (CardholderNotFoundException e) {
            model.addAttribute("message", "Cardholder not found");
            return "card/addNewCard";
        } catch (CardAlreadyExistsException e) {
            model.addAttribute("message", "Card with such number already exists");
            return "card/addNewCard";
        }
        return "redirect:all";
    }

}
