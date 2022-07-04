package Server.controller;


import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.service.CardholderService;
import Server.service.exceptions.CardholderAlreadyExistsException;
import Server.service.exceptions.CardholderNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping( "/cardholder")
@Controller
public class CardholderController {
    final CardholderService cardholderService;
    private static final Logger logger = (Logger) LogManager.getLogger(CardholderController.class);

    public CardholderController(CardholderService cardholderService) {
        this.cardholderService = cardholderService;
    }

    @GetMapping( "/all")
    String all(Model model){
        model.addAttribute("cardholders", cardholderService.getAll());
        return "cardholder/all";
    }

    @GetMapping("/{id}")
    String byId(@PathVariable("id") long id, Model model) {
        Cardholder cardholder;
        try {
            cardholder = cardholderService.getById(id);
        } catch (CardholderNotFoundException e) {
            logger.warn("Method: byId, id = " + id +" , Card not found");
            cardholder = new Cardholder();
        }
        model.addAttribute("cardholder" ,cardholder);
        return "cardholder/byId";
    }

    @GetMapping("/{id}/cards")
    String getCards(@PathVariable("id") long id, Model model) {
        List<Card> cards;
        try {
            Cardholder cardholder = cardholderService.getById(id);
            cards = cardholder.getCards();
        } catch (CardholderNotFoundException e) {
            logger.error("Method: getCards, id = " + id +" , Cardholder not found");
            cards = new ArrayList<>();
        }
        model.addAttribute("cards", cards);
        return "card/all";
    }

    @GetMapping( "/delete")
    void delete(@RequestParam long id)  {
        try {
            cardholderService.delete(id);
        } catch (CardholderNotFoundException e) {
            logger.error("Method: delete, id = " + id +" , Cardholder not found");
        }
    }

    @GetMapping("/addNewCardholder")
    String newCardholder(Model model){
        model.addAttribute("cardholder", new Cardholder());
        return "cardholder/addNewCardholder";
    }

    @PostMapping("/create")
    String createCardholder(@ModelAttribute("cardholder") @Valid Cardholder cardholder,
                            BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "cardholder/addNewCardholder";
        }
        try {
            cardholderService.addNewCardholder(cardholder);
        } catch (CardholderAlreadyExistsException e) {
            logger.error("Method: createCardholder, Cardholder "+cardholder.toString()+" already exists");
        }
        return "redirect:all";
    }
    @GetMapping("/{id}/update")
    String edit(@PathVariable("id") long id, Model model)  {
        try {
            model.addAttribute("cardholder", cardholderService.getById(id));
        } catch (CardholderNotFoundException e) {
            logger.error("Method: edit, id = " + id +" , Cardholder not found");
        }
        return "cardholder/edit";
    }

    @PostMapping("/{id}")
    String update(@Valid @ModelAttribute("cardholder")  Cardholder cardholder,
                  BindingResult bindingResult,
                  @PathVariable long id) {
        if(bindingResult.hasErrors()){
            return "cardholder/edit";
        }
        try {
            cardholderService.update(cardholder, id);
        } catch (CardholderNotFoundException e) {
            logger.error("Method: update, id = " + id +" , Cardholder not found");
        }
        return "redirect:all";
    }
}
