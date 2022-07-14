package Server.service;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;

import java.util.List;

public interface CardService {
    /**
     * Удаление карты из базы
     * @param id - id
     * @throws CardNotFoundException - если неверный id
     */
    void delete(Long id) throws CardNotFoundException;

    /**
     * Добавление новой карты
     * @param card - карту, которую хотим добавить
     * @throws CardAlreadyExistsException - если карта с таким номером уже есть в базе
     * @throws CardholderNotFoundException - если хотим привязать карту к несуществующему Cardholder-у
     */
    void addNewCard(Card card) throws CardAlreadyExistsException, CardholderNotFoundException;

    /**
     * Поиск карты по id
     * @param id - id
     * @return - card
     * @throws CardNotFoundException - если не найдено
     */
    Card getById(Long id) throws CardNotFoundException;

    /**
     * Поиск карты по номеру
     * @param number - номер
     * @return card
     * @throws CardNotFoundException  - если не найдено
     */
    Card getByNumber(Long number) throws CardNotFoundException;

    /**
     * Получение списка всех карт
     */
    List<Card> getAll();

    /**
     * Получение списка всех просроченных карт
     */
    List<Card> getAllExpired();

    /**
     * Получение Владельца карты по ее id
     * @param id - id карты
     * @return - cardholder
     * @throws CardholderNotFoundException - если не найден cardholder
     * @throws CardNotFoundException - если карта не найдена
     */
    Cardholder getCardholder(Long id) throws CardholderNotFoundException, CardNotFoundException;
}
