package pro.sky.AnimalShelter.enums;

/**
 * Перечисление, представляющее команды бота.
 */
public enum BotCommand {
    CAT("/cat"),                                                        //приют для кошек
    DOG("/dog"),                                                        //приют для собак
    HELP("/help"),                                                      //Если бот не может ответить на вопросы клиента, то можно позвать волонтера
    BACK("/back"),                                                      //назад
    PASS("/pass"),                                                      //Бот может выдать контактные данные охраны для оформления пропуска на машину
    STOP("/stop"),                                                      //остановить бота
    ADOPT("/adopt"),                                                    //Как взять животное из приюта (этап 2)
    START("/start"),                                                    //запустить бота
    SAFETY("/safety"),                                                  //Бот может выдать общие рекомендации о технике безопасности на территории приюта.
    CONTACT("/contact"),                                                //Бот может принять и записать контактные данные для связи.
    SCHEDULE("/schedule"),                                              //Бот может выдать расписание работы приюта и адрес, схему проезда
    DOCUMENTS("/documents"),                                            //Бот может выдать список документов, необходимых для того, чтобы взять животное из приюта
    DESCRIPTION("/description"),                                        //Бот может рассказать о приюте
    SHELTER_INFO("/shelter_info"),                                      //Узнать информацию о приюте(этап 1)
    DATING_RULES("/dating_rules"),                                      //Бот может выдать правила знакомства с животным до того, как забрать его из приюта
    REFUSAL_REASON("/refusal_reason"),                                  //Бот может выдать список причин, почему могут отказать и не дать забрать собаку из приюта
    DOG_TRAINER_ADVICE("/dog_trainer_advice"),                          //Бот может выдать советы кинолога по первичному общению с собакой
    VERIFIED_DOG_HANDLERS("/verified_dog_handlers"),                    //Бот может выдать рекомендации по проверенным кинологам для дальнейшего обращения к ним
    HOME_SETUP_RECOMMENDATIONS("/home_setup_recommendations"),          //Бот может  выдать список рекомендаций по обустройству дома для взрослого животного
    SPECIAL_NEED_RECOMMENDATION("/special_need_recommendation"),        //Бот может  выдать список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)
    TRANSPORTATION_RECOMMENDATION("/transportation_recommendation"),    //Бот может  выдать список рекомендаций по транспортировке животного
    PUPPY_HOME_SETUP_RECOMMENDATION("/puppy_home_setup_recommendation"),//Бот может  выдать список рекомендаций по обустройству дома для щенка
    KITTY_HOME_SETUP_RECOMMENDATION("/kitty_home_setup_recommendation");//Бот может  выдать список рекомендаций по обустройству дома для котёнка

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