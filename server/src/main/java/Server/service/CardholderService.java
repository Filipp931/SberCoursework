package Server.service;


import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.service.exceptions.CardholderAlreadyExistsException;
import Server.service.exceptions.CardholderNotFoundException;

import java.util.List;

public interface CardholderService {
    void delete(long cardholder_id) throws CardholderNotFoundException;
    Cardholder getById(long cardholder_id) throws CardholderNotFoundException;
    List<Cardholder> getAll();
    List<Card> getCards(long cardholder_id) throws CardholderNotFoundException;
    void addNewCardholder(Cardholder cardholder) throws CardholderAlreadyExistsException;
    Card addNewCard(long cardholderId, Card card) throws CardholderNotFoundException;
    Cardholder getByName(String name, String surname, String patronymic) throws CardholderNotFoundException;
    Cardholder getByPhoneNumber(long phoneNumber) throws CardholderNotFoundException;
    void update(Cardholder newCardholder, long cardholderId) throws CardholderNotFoundException;
}

