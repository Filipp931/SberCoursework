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
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
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
    String byId(@PathVariable("id") long id, Model model) throws CardholderNotFoundException {
        Cardholder cardholder;
        cardholder = cardholderService.getById(id);
        model.addAttribute("cardholder" ,cardholder);
        return "cardholder/byId";
    }

    @GetMapping("/{id}/cards")
    String getCards(@PathVariable("id") long id, Model model) throws CardholderNotFoundException {
        List<Card> cards;
        Cardholder cardholder = cardholderService.getById(id);
        cards = cardholder.getCards();
        model.addAttribute("cards", cards);
        return "card/all";
    }

    @GetMapping( "/delete")
    String delete(@RequestParam long id) throws CardholderNotFoundException {
        cardholderService.delete(id);
        return "redirect:all";
    }

    @GetMapping("/addNewCardholder")
    String newCardholder(Model model){
        model.addAttribute("cardholder", new Cardholder());
        return "cardholder/addNewCardholder";
    }

    @PostMapping("/create")
    String createCardholder(@ModelAttribute("cardholder") @Valid Cardholder cardholder,
                            BindingResult bindingResult) throws CardholderAlreadyExistsException {
        if(bindingResult.hasErrors()){
            return "cardholder/addNewCardholder";
        }
        cardholderService.addNewCardholder(cardholder);
        return "redirect:all";
    }
    @GetMapping("/{id}/update")
    String edit(@PathVariable("id") long id, Model model) throws CardholderNotFoundException {
        model.addAttribute("cardholder", cardholderService.getById(id));
        return "cardholder/edit";
    }

    @PostMapping("/{id}")
    String update(@Valid @ModelAttribute("cardholder")  Cardholder cardholder,
                  BindingResult bindingResult,
                  @PathVariable long id) throws CardholderNotFoundException {
        if(bindingResult.hasErrors()){
            return "cardholder/edit";
        }
        cardholderService.update(cardholder, id);
        return "redirect:all";
    }

    @ExceptionHandler
    public String error(WebRequest webRequest, Exception e, Model model){
        logger.error(webRequest.toString(), e);
        model.addAttribute("message", e.getMessage());
        return "exception";
    }
}
