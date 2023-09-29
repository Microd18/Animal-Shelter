package pro.sky.AnimalShelter.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.DogReport;
import pro.sky.AnimalShelter.entity.VolunteerInfoDog;
import pro.sky.AnimalShelter.repository.DogReportRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoDogRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
@Component
public class DogScheduler {

    private final DogReportRepository dogReportRepository;
    private final TelegramBot telegramBot;
    private final VolunteerInfoDogRepository volunteerInfoDogRepository;

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 2)
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

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 12)
    @Transactional
    public void twoDayCounter() {
        LocalDateTime elapsedTime = LocalDateTime.now().minus(48, ChronoUnit.HOURS);

        dogReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getUpdated()))
                //.filter(dogReport -> dogReport.getUpdated().isAfter(elapsedTime)) // для теста
                .map(DogReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(441625131,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nне присылал отчет более 2х дней!")));

    }

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedDelay = 1)
    @Transactional
    public void thirtyDaysCounter() {
        int time;
        boolean extra14 = false;
        boolean extra30 = false; //добавить поля в таблицу и дергать с нее

        if (extra14) {
            time = 44;
        } else if (extra30) {
            time = 60;
        } else {
            time = 30;
        }

        LocalDateTime elapsedTime = LocalDateTime.now().minus(time, ChronoUnit.DAYS);

        dogReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getCreated()))
                .map(DogReport::getUser)
                .forEach(u -> telegramBot.execute(new SendMessage(441625131,
                        "Усыновитель:\n"
                                + u.getUsername()
                                + " "
                                + u.getPhone()
                                + "\nсодержит животное уже 30 дней, пора принимать решение!")));
    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 2)
    @Transactional
    public void setDayInTable() {
        log.info("task X started");

        dogReportRepository
                .findAll()
                .forEach(dogReport -> saveDaysCounter(dogReport.getUser().getId(), differenceInDays(dogReport.getCreated(), LocalDateTime.now())));
    }

    @Transactional
    public void saveDaysCounter(Long userId, Integer daysCounter) {

        VolunteerInfoDog report = volunteerInfoDogRepository.findByUserId(userId).get();
        report.setAmountOfDays(daysCounter);

        volunteerInfoDogRepository.save(report);
    }

    @Transactional
    public int differenceInDays(LocalDateTime after, LocalDateTime now) {
        return Math.toIntExact(ChronoUnit.DAYS.between(after, now));
    }
}
