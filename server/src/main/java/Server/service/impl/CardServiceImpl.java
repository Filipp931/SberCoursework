package Server.service.impl;

import Server.repository.CardRepository;
import Server.POJO.Card;
import Server.POJO.Cardholder;
import Server.repository.CardholderRepository;
import Server.service.exceptions.CardAlreadyExistsException;
import Server.service.exceptions.CardNotFoundException;
import Server.service.exceptions.CardholderNotFoundException;
import org.springframework.stereotype.Component;
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
        if(!cardholderRepository.existsById(card.getCardholder().getId())) throw new CardholderNotFoundException("Id", card.getCardholder().getId());
        if(cardRepository.existsByNumber(card.getNumber())) throw new CardAlreadyExistsException(card);
        card.setIssueDate(LocalDate.now().toString());
        this.cardRepository.saveAndFlush(card);
    }


    @Override
    public Card getById(Long cardId) throws CardNotFoundException {
        return cardRepository.findById(cardId).orElseThrow(() ->  new CardNotFoundException(cardId, "Id"));
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
