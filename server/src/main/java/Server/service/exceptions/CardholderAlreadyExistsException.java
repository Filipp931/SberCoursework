package Server.service.exceptions;

import Server.POJO.Cardholder;

public class CardholderAlreadyExistsException extends Exception{
    private final String message;
    public CardholderAlreadyExistsException(Cardholder cardholder) {
        message = String.format("Cardholder with name %s, surname %s, patronymic %s and phone number %s already exists.",
                cardholder.getName(), cardholder.getSurname(), cardholder.getPatronymic(), cardholder.getPhoneNumber());
    }
    public String getMessage() {
        return message;
    }
}
