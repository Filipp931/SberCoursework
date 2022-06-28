package Server.service.impl;


import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.repository.CardRepository;
import Server.repository.CardholderRepository;
import Server.service.CardholderService;
import Server.service.exceptions.CardholderAlreadyExistsException;
import Server.service.exceptions.CardholderNotFoundException;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CardholderServiceImpl implements CardholderService {
    private final CardholderRepository cardholderRepository;
    private final CardRepository cardRepository;

    public CardholderServiceImpl(CardholderRepository cardholderRepository, CardRepository cardRepository) {
        this.cardholderRepository = cardholderRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public void delete(long cardholder_id) throws CardholderNotFoundException {
        if(!cardholderRepository.existsById(cardholder_id)) throw new CardholderNotFoundException("Id", cardholder_id);
        cardholderRepository.deleteById(cardholder_id);
    }

    @Override
    public Cardholder getById(long cardholder_id) throws CardholderNotFoundException {
        if(!cardholderRepository.existsById(cardholder_id)) throw new CardholderNotFoundException("Id", cardholder_id);
        return cardholderRepository.getById(cardholder_id);
    }

    @Override
    public List<Cardholder> getAll() {
        return cardholderRepository.findAll();
    }

    @Override
    public List<Card> getCards(long cardholder_id) throws CardholderNotFoundException {
        if(!cardholderRepository.existsById(cardholder_id)) throw new CardholderNotFoundException("Id", cardholder_id);
        Cardholder cardholder = cardholderRepository.getById(cardholder_id);
        return cardholder.getCards();
    }

    @Override
    public void addNewCardholder(Cardholder cardholder) throws CardholderAlreadyExistsException {
        if(cardholderRepository.existsByNameAndSurnameAndPatronymicAndPhoneNumber(
                cardholder.getName(),
                cardholder.getSurname(),
                cardholder.getPatronymic(),
                cardholder.getPhoneNumber())) {
            throw new CardholderAlreadyExistsException(cardholder);
        }
        cardholderRepository.saveAndFlush(cardholder);
    }

    @Override
    public Cardholder addNewCardholder(String cardholderJson) throws CardholderAlreadyExistsException {
        Cardholder cardholder;
        cardholder = new Gson().fromJson(cardholderJson, Cardholder.class);
        if(cardholderRepository.existsByNameAndSurnameAndPatronymicAndPhoneNumber(cardholder.getName(), cardholder.getSurname(), cardholder.getPatronymic(), cardholder.getPhoneNumber())){
            throw new CardholderAlreadyExistsException(cardholder);
        } else {
            cardholderRepository.saveAndFlush(cardholder);
        }
        return cardholderRepository.getByName(cardholder.getName(), cardholder.getSurname(), cardholder.getPatronymic());
    }

    @Override
    public Card addNewCard(long cardholderId, Card card) throws CardholderNotFoundException{
        if(!cardholderRepository.existsById(cardholderId)) {
            throw new CardholderNotFoundException("Id", cardholderId);
        }
        card.setCardholderId(cardholderId);
        cardRepository.saveAndFlush(card);
        return cardRepository.findByNumber(card.getNumber());
    }

    @Override
    public Cardholder getByName(String name, String surname, String patronymic) throws CardholderNotFoundException {
        Cardholder result = cardholderRepository.getByName(name, surname, patronymic);
        if(result == null){
            throw new CardholderNotFoundException(name, surname, patronymic);
        }
        return result;
    }

    @Override
    public Cardholder getByPhoneNumber(long phoneNumber) throws CardholderNotFoundException {
        Cardholder result = cardholderRepository.getByPhoneNumber(phoneNumber);
        if(result == null){
            throw new CardholderNotFoundException("phone number", phoneNumber);
        }
        return result;
    }

    @Override
    public void update(Cardholder newCardholder, long cardholderId) throws CardholderNotFoundException{
        if (!cardholderRepository.existsById(cardholderId)) {
            throw new CardholderNotFoundException("Id", cardholderId);
        }
        Cardholder cardholder = cardholderRepository.getById(cardholderId);
        cardholder.setName(newCardholder.getName());
        cardholder.setSurname(newCardholder.getSurname());
        cardholder.setPatronymic(newCardholder.getPatronymic());
        cardholder.setEmail(newCardholder.getEmail());
        cardholder.setPhoneNumber(newCardholder.getPhoneNumber());
        cardholderRepository.saveAndFlush(cardholder);
    }

}
