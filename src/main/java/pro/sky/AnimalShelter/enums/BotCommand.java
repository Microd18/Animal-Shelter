package pro.sky.AnimalShelter.enums;

public enum BotCommand {
    CAT("/cat"),
    DOG("/dog"),
    HELP("/help"),
    PASS("/pass"),
    STOP("/stop"),
    START("/start"),
    SAFETY("/safety"),
    CONTACT("/contact"),
    SCHEDULE("/schedule"),
    DESCRIPTION("/description");


    private final String commandText;

    BotCommand(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }
}
