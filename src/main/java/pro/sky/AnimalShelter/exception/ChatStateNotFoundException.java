package pro.sky.AnimalShelter.exception;

public class ChatStateNotFoundException extends RuntimeException {
    private final Long chatId;

    public ChatStateNotFoundException(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String getMessage() {
        return "ChatState для чата " + chatId + " не найден";
    }

}
