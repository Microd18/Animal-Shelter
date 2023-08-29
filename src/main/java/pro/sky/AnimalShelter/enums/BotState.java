package pro.sky.AnimalShelter.enums;

public enum BotState {
    START("/start"),
    STOP("/stop");

    private final String stateText;

    BotState(String commandText) {
        this.stateText = commandText;
    }

    public String getStatedText() {
        return stateText;
    }
}
