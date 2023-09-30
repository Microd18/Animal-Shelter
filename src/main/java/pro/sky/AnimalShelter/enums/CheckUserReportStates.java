package pro.sky.AnimalShelter.enums;

/**
 * Перечисление, представляющее состояния проверки и оценки отчета.
 */
public enum CheckUserReportStates {
    ALL_REPORTS("/all_reports"), //список отчетов
    VIEW_REPORT("/view_report"),   //просмотреть отчет
    EVALUATE_REPORT("/evaluate_report");      //оценить отчет

    /**
     * Текстовое представление команды.
     */
    private final String stateValue;

    /**
     * Конструктор, устанавливающий текстовое представление команды.
     *
     * @param stateValue Текстовое представление команды.
     */
    CheckUserReportStates(String stateValue) {
        this.stateValue = stateValue;
    }
}
