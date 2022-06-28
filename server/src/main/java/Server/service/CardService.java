package Server.service;

import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;

import java.util.List;

public interface CardService {
    void delete(Long id) throws CardNotFoundException;
    void addNewCard(Card card) throws CardAlreadyExistsException, CardholderNotFoundException;
    Card addNewCard(String card) throws CardAlreadyExistsException, CardholderNotFoundException;
    Card getById(Long id) throws CardNotFoundException;
    Card getByNumber(Long number) throws CardNotFoundException;
    List<Card> getAll();
    List<Card> getAllExpired();
    Cardholder getCardholder(Long id) throws CardholderNotFoundException, CardNotFoundException;
}
