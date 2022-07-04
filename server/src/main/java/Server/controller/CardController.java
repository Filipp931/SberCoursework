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
    private final CardService cardService;
    private final CardholderService cardholderService;
    private static final Logger logger = (Logger) LogManager.getLogger(CardController.class);

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
    String byId(@PathVariable("id") long id, Model model) {
        Card card;
        try {
            card = cardService.getById(id);
        } catch (CardNotFoundException e) {
            logger.warn("Method: byId, id = " + id +" , Card not found");
            card = new Card();
        }
        model.addAttribute("card" , card);
        return "card/byId";
    }

    @GetMapping("/{id}/delete")
    String delete(@PathVariable("id") long id) {
        try {
            cardService.delete(id);
        } catch (CardNotFoundException e) {
            logger.error("Method: delete, id = " + id + " , card not found");
        }
        return "redirect:card/all";
    }

    @GetMapping("/addNewCard{id}")
    String newCard(@PathVariable("id") long cardholderId, Model model){
        Card card = new Card();
        try {
            card.setCardholder(cardholderService.getById(cardholderId));
        } catch (CardholderNotFoundException e) {
            logger.error("Method: newCard, id = " + cardholderId + " , cardholder not found");
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
            logger.error("Method: createCard, id = " + card.getCardholder().getId() + " , cardholder not found");
            return "card/addNewCard";
        } catch (CardAlreadyExistsException e) {
            model.addAttribute("message", "Card with such number already exists");
            logger.debug("Method: createCard, card with number "+card.getNumber()+" not found");
            return "card/addNewCard";
        }
        return "redirect:all";
    }

}
