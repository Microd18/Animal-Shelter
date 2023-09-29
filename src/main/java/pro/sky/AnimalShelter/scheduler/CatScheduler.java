package pro.sky.AnimalShelter.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.CatReport;
import pro.sky.AnimalShelter.entity.VolunteerInfoCat;
import pro.sky.AnimalShelter.repository.CatReportRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoCatRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Счетчик для подсчета дней нахождения кошки у усыновителя
 * отправляет уведомления на 1/2/30/44/60 день волонтеру либо усыновителю
 */
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatScheduler {
    /**
     * Репозиторий для доступа к отчетам по кошкам
     */
    private final CatReportRepository catReportRepository;
    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;
    /**
     * Репозиторий для доступа к дополнительной информации об отчетах по кошкам
     */
    private final VolunteerInfoCatRepository volunteerInfoCatRepository;

    /**
     * Метод для отправки уведомления пользователю если он не отправлял отчет более 1 дня
     */
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 2)
    @Transactional
    public void oneDayCounter() {
        log.info("oneDayCounter started");

        LocalDateTime elapsedTime = LocalDateTime.now().minus(24, ChronoUnit.HOURS);

        catReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getUpdated()))
                .forEach(dogReport -> telegramBot.execute(new SendMessage(dogReport.getUser().getChat().getChatId(), "Вы уже сутки не отправляли отчет по питомцу!")));
    }

    /**
     * Метод для отправки уведомления волонтеру если пользователь не отправлял отчет более 1 дня
     */
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 12)
    @Transactional
    public void twoDayCounter() {
        log.info("twoDayCounter started");

        LocalDateTime elapsedTime = LocalDateTime.now().minus(48, ChronoUnit.HOURS);

        catReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getUpdated()))
                //.filter(dogReport -> dogReport.getUpdated().isAfter(elapsedTime)) // для теста
                .map(CatReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(441625131,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nне присылал отчет более 2х дней!")));

    }

    /**
     * Метод для отправки уведомления волонтеру которое сообщает что прошло 30 дней со дня усыновления
     */
    @Scheduled(timeUnit = TimeUnit.DAYS, fixedDelay = 1)
    @Transactional
    public void thirtyDaysCounter() {

        LocalDateTime elapsedTime1 = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        LocalDateTime elapsedTime2 = LocalDateTime.now().minus(44, ChronoUnit.DAYS);
        LocalDateTime elapsedTime3 = LocalDateTime.now().minus(60, ChronoUnit.DAYS);

        catReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime1.isAfter(dogReport.getCreated()))
                .map(CatReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(441625131,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nсодержит животное уже 30 дней, пора принимать решение!")));


        catReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime2.isAfter(dogReport.getCreated()))
                .map(CatReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(441625131,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nсодержит животное уже 44 дня, пора принимать решение!")));


        catReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime3.isAfter(dogReport.getCreated()))
                .map(CatReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(441625131,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nсодержит животное уже 60 дней, пора принимать решение!")));
    }

    /**
     * Метод, который проставляет количество дней в таблицу с дополнительной информацией для волонтера
     */
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 2)
    @Transactional
    public void setDayInTable() {
        log.info("task X started");

        catReportRepository
                .findAll()
                .forEach(dogReport -> saveDaysCounter(dogReport.getUser().getId(), differenceInDays(dogReport.getCreated(), LocalDateTime.now())));
    }

    /**
     * Метод, который сохраняет измененную сущность
     */
    @Transactional
    public void saveDaysCounter(Long userId, Integer daysCounter) {

        VolunteerInfoCat report = volunteerInfoCatRepository.findByUserId(userId).get();
        report.setAmountOfDays(daysCounter);

        volunteerInfoCatRepository.save(report);
    }

    /**
     * Метод, который считает прошедшие дни между созданием первого отчета и текущим временем
     */
    @Transactional
    public int differenceInDays(LocalDateTime after, LocalDateTime now) {
        return Math.toIntExact(ChronoUnit.DAYS.between(after, now));
    }

}
