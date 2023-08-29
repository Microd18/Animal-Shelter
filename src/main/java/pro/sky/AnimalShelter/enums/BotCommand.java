package pro.sky.AnimalShelter.enums;

public enum BotCommand {
    START("/start"),
    STOP("/stop"),
    DOG("/dog"),
    CAT("/cat");

    private final String commandText;

    BotCommand(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }
}
