package pro.sky.AnimalShelter.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.CatReport;
import pro.sky.AnimalShelter.entity.DogReport;
import pro.sky.AnimalShelter.entity.VolunteerInfoCat;
import pro.sky.AnimalShelter.entity.VolunteerInfoDog;
import pro.sky.AnimalShelter.repository.CatReportRepository;
import pro.sky.AnimalShelter.repository.VolunteerInfoCatRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatScheduler {
    private final CatReportRepository catReportRepository;
    private final TelegramBot telegramBot;
    private final VolunteerInfoCatRepository volunteerInfoCatRepository;

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 2)
    @Transactional
    public void oneDayCounter() {
        log.info("OneDayCounter started");

        LocalDateTime elapsedTime = LocalDateTime.now().minus(24, ChronoUnit.HOURS);

        catReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getUpdated()))
                .forEach(dogReport -> telegramBot.execute(new SendMessage(dogReport.getUser().getChat().getChatId(), "Вы уже сутки не отправляли отчет по питомцу!")));
    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 12)
    @Transactional
    public void twoDayCounter() {
        log.info("TwoDayCounter started");

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

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedDelay = 1)
    @Transactional
    public void thirtyDaysCounter() {
        int time;
        boolean extra14 = false;
        boolean extra30 = false; //возможно нужно добавить поля в таблицу и дергать с нее

        if (extra14) {
            time = 44;
        } else if (extra30) {
            time = 60;
        } else {
            time = 30;
        }

        log.info("ThirtyDaysCounter started");
        LocalDateTime elapsedTime = LocalDateTime.now().minus(time, ChronoUnit.DAYS);

        catReportRepository
                .findAll()
                .stream()
                .filter(dogReport -> elapsedTime.isAfter(dogReport.getCreated()))
                .map(CatReport::getUser)
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

        catReportRepository
                .findAll()
                .forEach(dogReport -> saveDaysCounter(dogReport.getUser().getId(), differenceInDays(dogReport.getCreated(), LocalDateTime.now())));
    }

    @Transactional
    public void saveDaysCounter(Long userId, Integer daysCounter) {

        VolunteerInfoCat report = volunteerInfoCatRepository.findByUserId(userId).get();
        report.setAmountOfDays(daysCounter);

        volunteerInfoCatRepository.save(report);
    }

    @Transactional
    public int differenceInDays(LocalDateTime after, LocalDateTime now) {
        return Math.toIntExact(ChronoUnit.DAYS.between(after, now));
    }

}
