package Server.service;


import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.service.exceptions.CardholderAlreadyExistsException;
import Server.service.exceptions.CardholderNotFoundException;

import java.util.List;

public interface CardholderService {
    /**
     * удаление
     * @param cardholder_id - по id
     * @throws CardholderNotFoundException - если id неверный
     */
    void delete(long cardholder_id) throws CardholderNotFoundException;

    /**
     * получение Entity по id
     * @param cardholder_id - id
     * @return Cardholder
     * @throws CardholderNotFoundException - если id неверный
     */
    Cardholder getById(long cardholder_id) throws CardholderNotFoundException;

    /**
     * получение списка всех Cardholder из базы
     * @return List
     */
    List<Cardholder> getAll();

    /**
     * Получение списка всех Cards у Cardholder по его ID
     * @param cardholder_id - id
     * @return - List<Card>
     * @throws CardholderNotFoundException - если id неверный
     */
    List<Card> getCards(long cardholder_id) throws CardholderNotFoundException;

    /**
     * Добавление нового Cardholder-а в базу
     * @param cardholder кого добавляем
     * @throws CardholderAlreadyExistsException - Если в базе с такими ФИО и номером уже есть
     */
    void addNewCardholder(Cardholder cardholder) throws CardholderAlreadyExistsException;

    /**
     * Добавление Card Cardholder-у по его ID
     * @param cardholderId - id
     * @param card - Card, которую хотим добавить
     * @return card с присвоенным ID и привязкаой к Cardholder
     * @throws CardholderNotFoundException  - если id неверный
     */
    Card addNewCard(long cardholderId, Card card) throws CardholderNotFoundException;

    /**
     * Поиск Cardholder по ФИО
     * @param name - имя
     * @param surname - фамилия
     * @param patronymic - отчество
     * @return - cardholder
     * @throws CardholderNotFoundException - если не найдено в базе
     */
    Cardholder getByName(String name, String surname, String patronymic) throws CardholderNotFoundException;

    /**
     * Поиск Cardholder по номеру телефона
     * @param phoneNumber - номер
     * @return - cardholder
     * @throws CardholderNotFoundException - если не найдено в базе
     */
    Cardholder getByPhoneNumber(long phoneNumber) throws CardholderNotFoundException;

    /**
     * Обновление данных Cardholder
     * @param newCardholder - обновленный
     * @param cardholderId - id того, которого хотим обновить
     * @throws CardholderNotFoundException - если некорректный id
     */
    void update(Cardholder newCardholder, long cardholderId) throws CardholderNotFoundException;
}

