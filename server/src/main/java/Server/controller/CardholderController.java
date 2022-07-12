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

    /**
     * вывод списка всех владельцев
     * @return страница со списком
     */
    @GetMapping( "/all")
    String all(Model model){
        model.addAttribute("cardholders", cardholderService.getAll());
        return "cardholder/all";
    }

    /**
     * получение данных владельца по id
     * @param id - if
     * @return страница с информацией о владельце
     * @throws CardholderNotFoundException - если неверный id
     */
    @GetMapping("/{id}")
    String byId(@PathVariable("id") long id, Model model) throws CardholderNotFoundException {
        Cardholder cardholder;
        cardholder = cardholderService.getById(id);
        model.addAttribute("cardholder" ,cardholder);
        return "cardholder/byId";
    }

    /**
     * получение списка всех карт владельца
     * @param id - id владельца
     * @return страница со списком карт
     * @throws CardholderNotFoundException - - если неверный id владельца
     */
    @GetMapping("/{id}/cards")
    String getCards(@PathVariable("id") long id, Model model) throws CardholderNotFoundException {
        List<Card> cards;
        Cardholder cardholder = cardholderService.getById(id);
        cards = cardholder.getCards();
        model.addAttribute("cards", cards);
        return "card/all";
    }

    /**
     * удаление владельца
     * @param id - id
     * @return перенаправление на страницу со списком всех владельцев
     * @throws CardholderNotFoundException - если id владельца неверный
     */
    @GetMapping( "/delete")
    String delete(@RequestParam long id) throws CardholderNotFoundException {
        cardholderService.delete(id);
        return "redirect:all";
    }

    /**
     * форма заполнения нового владельца
     * @return отправляет post на создание владельца
     */
    @GetMapping("/addNewCardholder")
    String newCardholder(Model model){
        model.addAttribute("cardholder", new Cardholder());
        return "cardholder/addNewCardholder";
    }

    /**
     * создание нового владельца
     * @param cardholder - владелец
     * @param bindingResult - ошибки в бине Cardholder
     * @return - перенаправление на страницу со списком всех владельцев
     * @throws CardholderAlreadyExistsException - если владелец с таким ФИО и номером существует
     */
    @PostMapping("/create")
    String createCardholder(@ModelAttribute("cardholder") @Valid Cardholder cardholder,
                            BindingResult bindingResult) throws CardholderAlreadyExistsException {
        if(bindingResult.hasErrors()){
            return "cardholder/addNewCardholder";
        }
        cardholderService.addNewCardholder(cardholder);
        return "redirect:all";
    }

    /**
     * Форма обновления данных владельца
     * @param id - id владельца
     * @return post на обновление данных владельца
     * @throws CardholderNotFoundException - при неверном id владельца
     */
    @GetMapping("/{id}/update")
    String edit(@PathVariable("id") long id, Model model) throws CardholderNotFoundException {
        model.addAttribute("cardholder", cardholderService.getById(id));
        return "cardholder/edit";
    }

    /**
     * Обновление данных владельца
     * @param cardholder - владелец
     * @param bindingResult - ошибки в бине Cardholder
     * @param id - id владельца
     * @return - перенаправление на страницу со списком всех владельцев в случае успеха,
     * либо возврат на форму обновления данных при наличии ошибок в бине
     * @throws CardholderNotFoundException - при неверном id владельца
     */
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
