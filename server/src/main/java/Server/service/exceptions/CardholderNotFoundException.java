package Server.service.exceptions;



public class CardholderNotFoundException extends Exception{
    private final StringBuilder message = new StringBuilder();
    public CardholderNotFoundException(String param, long value) {
        message.append("Cardholder with ").append(param).append(" = ").append(value).append(" not found");
    }
    public CardholderNotFoundException(String name, String surname, String patronymic) {
        message.append("Cardholder with name = ").append(name).append(" surname = ").append(surname)
                .append(" patronymic = ").append(patronymic).append(" not found");
    }
    public String getMessage() {
        return message.toString();
    }
}
