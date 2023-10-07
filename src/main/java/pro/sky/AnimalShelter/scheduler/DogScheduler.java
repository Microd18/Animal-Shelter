package pro.sky.AnimalShelter.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.DogReport;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.entity.VolunteerInfoDog;
import pro.sky.AnimalShelter.repository.DogReportRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoDogRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Счетчик для подсчета дней нахождения собаки у усыновителя
 * отправляет уведомления на 1/2/30/44/60 день волонтеру либо усыновителю
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DogScheduler {
    /**
     * Репозиторий для доступа к отчетам по собакам
     */
    private final DogReportRepository dogReportRepository;
    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;
    /**
     * Репозиторий для доступа к дополнительной информации об отчетах по собакам
     */
    private final VolunteerInfoDogRepository volunteerInfoDogRepository;

    /**
     * Метод для отправки уведомления пользователю если он не отправлял отчет более 1 дня
     */
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    @Transactional
    public void oneDayCounter() {
        log.info("oneDayCounter started");

        LocalDateTime elapsedTime = LocalDateTime.now().minus(24, ChronoUnit.HOURS);

        dogReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getUpdated()))
                .forEach(dogReport -> telegramBot.execute(new SendMessage(dogReport.getUser().getChat().getChatId(), "Вы уже сутки не отправляли отчет по питомцу!")));
    }

    /**
     * Метод для отправки уведомления волонтеру если пользователь не отправлял отчет более 1 дня
     */
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    @Transactional
    public void twoDayCounter() {
        log.info("twoDayCounter started");
        LocalDateTime elapsedTime = LocalDateTime.now().minus(48, ChronoUnit.HOURS);

        List<User> userList = dogReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getUpdated()))
                .map(DogReport::getUser)
                .collect(Collectors.toList());

        if (!userList.isEmpty()) {
            userList.forEach(u -> telegramBot.execute(new SendMessage(315863400,
                    "Усыновитель:\n"
                            + u.getUsername()
                            + " "
                            + u.getPhone()
                            + "\nне присылал отчет более 2х дней!")));
        } else {
            log.error("USER LIST IS EMPTY !!!!!!!!!");
        }
    }

    /**
     * Метод для отправки уведомления волонтеру которое сообщает что прошло 30 дней со дня усыновления
     */
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    @Transactional
    public void thirtyDaysCounter() {
        log.info("thirtyDaysCounter started");
        LocalDateTime elapsedTime1 = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        LocalDateTime elapsedTime2 = LocalDateTime.now().minus(44, ChronoUnit.DAYS);
        LocalDateTime elapsedTime3 = LocalDateTime.now().minus(60, ChronoUnit.DAYS);

        dogReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime1.isAfter(dogReport.getCreated()))
                .map(DogReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(315863400,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nсодержит животное уже 30 дней, пора принимать решение!")));


        dogReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime2.isAfter(dogReport.getCreated()))
                .map(DogReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(315863400,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nсодержит животное уже 44 дня, пора принимать решение!")));


        dogReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime3.isAfter(dogReport.getCreated()))
                .map(DogReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(315863400,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nсодержит животное уже 60 дней, пора принимать решение!")));
    }

    /**
     * Метод, который проставляет количество дней в таблицу с дополнительной информацией для волонтера
     */
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    @Transactional
    public void setDayInTable() {
        log.info("setDayInTable started");
        dogReportRepository
                .findAll()
                .forEach(dogReport -> saveDaysCounter(dogReport.getUser().getId(), differenceInDays(dogReport.getCreated(), LocalDateTime.now())));
    }

    /**
     * Метод, который сохраняет измененную сущность
     */
    @Transactional
    public void saveDaysCounter(Long userId, Integer daysCounter) {

        try {
            VolunteerInfoDog report = volunteerInfoDogRepository.findByUserId(userId).get();
            report.setAmountOfDays(daysCounter);

            volunteerInfoDogRepository.save(report);
        } catch (RuntimeException e) {
            log.error(e.toString());
        }
    }

    /**
     * Метод, который считает прошедшие дни между созданием первого отчета и текущим временем
     */
    @Transactional
    public int differenceInDays(LocalDateTime after, LocalDateTime now) {
        return Math.toIntExact(ChronoUnit.DAYS.between(after, now));
    }
}
