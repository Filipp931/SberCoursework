package Server.service.exceptions;


import Server.POJO.Card;

public class CardAlreadyExistsException extends Exception{
    private final StringBuilder message = new StringBuilder();
    public CardAlreadyExistsException(Card card) {
        message.append("Card with number = ").append(card.getNumber()).append(" already exists. Id = ").append(card.getId());
    }
    public String getMessage() {
        return message.toString();
    }
}
