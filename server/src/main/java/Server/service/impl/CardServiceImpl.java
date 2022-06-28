package Server.service.impl;

import Server.repository.CardRepository;
import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.repository.CardholderRepository;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import Server.service.CardService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardholderRepository cardholderRepository;

    public CardServiceImpl(CardRepository cardRepository, CardholderRepository cardholderRepository) {
        this.cardRepository = cardRepository;
        this.cardholderRepository = cardholderRepository;
    }

    @Override
    public void delete(Long cardId) throws CardNotFoundException {
        if(!cardRepository.existsById(cardId)) throw new CardNotFoundException(cardId, "Id");
        cardRepository.deleteById(cardId);
    }

    @Override
    public void addNewCard(Card card) throws CardAlreadyExistsException, CardholderNotFoundException {
        if(!cardholderRepository.existsById(card.getCardholderId())) throw new CardholderNotFoundException("Id", card.getCardholderId());
        if(cardRepository.existsByNumber(card.getNumber())) throw new CardAlreadyExistsException(card);
        card.setIssueDate(LocalDate.now().toString());
        this.cardRepository.saveAndFlush(card);
    }

    @Override
    public Card addNewCard(String cardJson) throws CardAlreadyExistsException, CardholderNotFoundException {
        Card card;
        card = new Gson().fromJson(cardJson, Card.class);
        if(cardRepository.existsByNumber(card.getNumber())){
            throw new CardAlreadyExistsException(card);}
        if(!cardholderRepository.existsById(card.getCardholderId())) {
            throw new CardholderNotFoundException("Id",card.getCardholderId());
        }
            this.cardRepository.saveAndFlush(card);
        return cardRepository.findByNumber(card.getNumber());
    }

    @Override
    public Card getById(Long cardId) throws CardNotFoundException {
        if(!cardRepository.existsById(cardId)) throw new CardNotFoundException(cardId, "Id");
        return cardRepository.getById(cardId);
    }

    @Override
    public Card getByNumber(Long number) throws CardNotFoundException {
        if(!cardRepository.existsByNumber(number)) throw new CardNotFoundException(number, "number");
        return cardRepository.findByNumber(number);
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    @Override
    public List<Card> getAllExpired() {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
        return cardRepository.getAllByExpirationDateBefore(dtf.format(LocalDate.now()));
    }

    @Override
    public Cardholder getCardholder(Long cardId) throws CardNotFoundException {
        if(!cardRepository.existsById(cardId)) throw new CardNotFoundException(cardId, "Id");
        return cardRepository.getCardHolder(cardId);
    }

}
