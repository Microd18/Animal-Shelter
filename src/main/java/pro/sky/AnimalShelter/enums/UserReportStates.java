package pro.sky.AnimalShelter.enums;

/**
 * Перечисление, представляющее состояние отправки отчёта пользователем.
 */
public enum UserReportStates {

    PHOTO("/photo"),            //фото питомца
    RATION("/ration"),          //рацион питомца
    BEHAVIOR("/behavior"),      //изменение поведения питомца
    WELL_BEING("/well_being");  //благополучие питомца

    /**
     * Текстовое представление команды.
     */
    private final String stateValue;

    /**
     * Конструктор, устанавливающий текстовое представление команды.
     *
     * @param stateValue Текстовое представление команды.
     */
    UserReportStates(String stateValue) {
        this.stateValue = stateValue;
    }
}
