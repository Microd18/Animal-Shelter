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
    ADMIN("/admin"),                                                    //Меню взаимодействия с волонтером
    ADOPT("/adopt"),                                                    //Как взять животное из приюта (этап 2)
    START("/start"),                                                    //запустить бота
    SAFETY("/safety"),                                                  //Бот может выдать общие рекомендации о технике безопасности на территории приюта.
    CONTACT("/contact"),                                                //Бот может принять и записать контактные данные для связи.
    SCHEDULE("/schedule"),                                              //Бот может выдать расписание работы приюта и адрес, схему проезда
    DOCUMENTS("/documents"),                                            //Бот может выдать список документов, необходимых для того, чтобы взять животное из приюта
    PET_REPORT("/pet_report"),                                          //меню отправки отчёта
    DESCRIPTION("/description"),                                        //Бот может рассказать о приюте
    SEND_REPORT("/send_report"),                                        //отправка отчёта
    MAKE_ADOPTER("/make_adopter"),                                      //Перевести юзера в усыновителя кошки или собаки
    ALL_ADOPTERS("/all_adopters"),                                      //Получить список усыновителей
    CHECK_REPORT("/check_report"),                                      //Проверить отчет по усыновителю
    SHELTER_INFO("/shelter_info"),                                      //Узнать информацию о приюте(этап 1)
    DATING_RULES("/dating_rules"),                                      //Бот может выдать правила знакомства с животным до того, как забрать его из приюта
    REFUSAL_REASON("/refusal_reason"),                                  //Бот может выдать список причин, почему могут отказать и не дать забрать собаку из приюта
    REPORT_TEMPLATE("/report_template"),                                //шаблон отчёта
    FIND_USER_BY_PHONE("/find_user_by_phone"),                          //Найти юзеров по номеру телефона
    DOG_TRAINER_ADVICE("/dog_trainer_advice"),                          //Бот может выдать советы кинолога по первичному общению с собакой
    FIND_ANIMAL_BY_NAME("/find_animal_by_name"),                        //Найти животное по кличке
    EXTENSION_PROBATION("/extension_probation"),                        //продлить срок юзеру
    CHECK_ADMIN_PASSWORD("/password"),                                  //проверка пароля админа
    VERIFIED_DOG_HANDLERS("/verified_dog_handlers"),                    //Бот может выдать рекомендации по проверенным кинологам для дальнейшего обращения к ним
    WITHOUT_REPORT_ADOPTERS("/without_report_adopters"),                //Получить список усыновителей, которые не высылали отчет более 2 дней
    HOME_SETUP_RECOMMENDATIONS("/home_setup_recommendations"),          //Бот может выдать список рекомендаций по обустройству дома для взрослого животного
    SPECIAL_NEED_RECOMMENDATION("/special_need_recommendation"),        //Бот может выдать список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)
    COMPLETED_PROBATION_ADOPTERS("/completed_probation_adopters"),      //Получить список усыновителей, у которых испытательный срок подошел к концу
    TRANSPORTATION_RECOMMENDATION("/transportation_recommendation"),    //Бот может выдать список рекомендаций по транспортировке животного
    PUPPY_HOME_SETUP_RECOMMENDATION("/puppy_home_setup_recommendation"),//Бот может выдать список рекомендаций по обустройству дома для щенка
    KITTY_HOME_SETUP_RECOMMENDATION("/kitty_home_setup_recommendation");//Бот может выдать список рекомендаций по обустройству дома для котёнка

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