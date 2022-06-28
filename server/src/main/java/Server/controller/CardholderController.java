package Server.controller;


import Server.POJO.Card;
import Server.service.CardholderService;
import Server.POJO.Cardholder;
import Server.service.exceptions.CardholderAlreadyExistsException;
import Server.service.exceptions.CardholderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping( "/cardholder")
@Controller
public class CardholderController {
    final
    CardholderService cardholderService;

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
        model.addAttribute("cardholder" ,cardholderService.getById(id));
        return "cardholder/byId";
    }

    @GetMapping("/{id}/cards")
    String getCards(@PathVariable("id") long id, Model model) throws CardholderNotFoundException {
        model.addAttribute("cards", cardholderService.getCards(id));
        return "card/all";
    }
    @GetMapping( "/delete")
    void delete(@RequestParam long id) throws CardholderNotFoundException {
        cardholderService.delete(id);
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
            e.printStackTrace();
        }
        return "redirect:/all";
    }
    @GetMapping("/{id}/update")
    String edit(@PathVariable("id") long id, Model model)  {
        try {
            model.addAttribute("cardholder", cardholderService.getById(id));
        } catch (CardholderNotFoundException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return "redirect:all";
    }
}
