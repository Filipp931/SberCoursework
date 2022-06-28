package Server.service.exceptions;


public class CardNotFoundException extends Exception{
    private final StringBuilder message = new StringBuilder();
    public CardNotFoundException(long num, String param) {
        message.append("Card with ").append(param).append(" = ").append(num).append(" not found");
    }
    public String getMessage() {
        return message.toString();
    }
}
