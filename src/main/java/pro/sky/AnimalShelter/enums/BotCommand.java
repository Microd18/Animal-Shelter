package pro.sky.AnimalShelter.enums;

/**
 * Перечисление, представляющее команды бота.
 */
public enum BotCommand {
    CAT("/cat"),                    //приют для кошек
    DOG("/dog"),                    //приют для собак
    HELP("/help"),                  //Если бот не может ответить на вопросы клиента, то можно позвать волонтера.
    BACK("/back"),                  //назад
    PASS("/pass"),                  //Бот может выдать контактные данные охраны для оформления пропуска на машину.
    STOP("/stop"),                  //остановить бота
    START("/start"),                //запустить бота
    SAFETY("/safety"),              //Бот может выдать общие рекомендации о технике безопасности на территории приюта.
    CONTACT("/contact"),            //Бот может принять и записать контактные данные для связи.
    SCHEDULE("/schedule"),          //Бот может выдать расписание работы приюта и адрес, схему проезда.
    DESCRIPTION("/description"),    //Бот может рассказать о приюте
    SHELTER_INFO("/shelter_info");  //Узнать информацию о приюте

    /**
     * Текстовое представление команды.
     */
    private final String commandText;

    /**
     * Конструктор, устанавливающий текстовое представление команды.
     *
     * @param commandText Текстовое представление команды.
     */
    BotCommand(String commandText) {
        this.commandText = commandText;
    }

    /**
     * Возвращает текстовое представление команды.
     *
     * @return Текстовое представление команды.
     */
    public String getCommandText() {
        return commandText;
    }
}