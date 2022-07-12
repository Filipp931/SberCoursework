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
import org.springframework.web.context.request.WebRequest;

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
     * @param id - id карты
     * @return card
     */
    @GetMapping("/{id}")
    String byId(@PathVariable("id") long id, Model model) throws CardNotFoundException {
        model.addAttribute("card" , cardService.getById(id));
        return "card/byId";
    }

    /**
     * удаление карты по ID
     * @param id - ID
     * @return перенаправление на страницу со списком всех карт
     * @throws CardNotFoundException - если неверный ID
     */
    @GetMapping("/{id}/delete")
    String delete(@PathVariable("id") long id) throws CardNotFoundException {
        cardService.delete(id);
        return "redirect:card/all";
    }

    /**
     * Страница создания новой карты
     * @param cardholderId - ID владельца
     * @return перенаправление данных на create
     * @throws CardholderNotFoundException - при неверном id
     */
    @GetMapping("/addNewCard{id}")
    String newCard(@PathVariable("id") long cardholderId, Model model) throws CardholderNotFoundException {
        Card card = new Card();
        card.setCardholder(cardholderService.getById(cardholderId));
        model.addAttribute("card", card);
        return "card/addNewCard";
    }

    /**
     * создание новой карты
     * @param card - карта
     * @param bindingResult - ошибки в бине Card
     * @return  перенаправление на страницу со списком всех карт
     * @throws CardholderNotFoundException - при неверном Id владельца
     * @throws CardAlreadyExistsException - если карта с таким number существует в базе
     */
    @PostMapping("/create")
    String createCard(@ModelAttribute("card") @Valid Card card, BindingResult bindingResult) throws CardholderNotFoundException, CardAlreadyExistsException {
        if(bindingResult.hasErrors()) {
            return "card/create";
        }
        cardService.addNewCard(card);
        return "redirect:all";
    }

    /**
     * Обработка исключений (визуализация сообщения)
     * @param webRequest - запрос в котором произошла ошибка
     * @param e - ошибка
     * @return страница с выводом текста ошибки
     */
    @ExceptionHandler
    public String error(WebRequest webRequest, Exception e, Model model){
        logger.error(webRequest.toString(), e);
        model.addAttribute("message", e.getMessage());
        return "exception";
    }

}
