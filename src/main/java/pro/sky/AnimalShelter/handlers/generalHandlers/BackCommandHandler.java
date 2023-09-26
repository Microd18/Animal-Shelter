package pro.sky.AnimalShelter.handlers.generalHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.service.UserReportStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADMIN_COMMAND_RETURN_TEXT;

/**
 * Обработчик команды "/back". Позволяет вернуться в предыдущее меню.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BackCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Сервис для управления очередью состояний отчётов пользователей.
     */
    private final UserReportStateService userReportStateService;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/back" и возвращает пользователя в предыдущее меню.
     *
     * @param update Объект, содержащий информацию о сообщении пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        BotCommand lastState = chatStateService.getLastStateCatOrDogByChatId(chatId);
        if (currentState == SEND_REPORT) {
            String responseText = "Вы вернулись назад. Это меню для отправки отчёта, выберите действие:\n" +
                    "1. Отправить отчёт: выберите этот пункт меню, чтобы отправить ежедневный отчёт о вашем питомце (/send_report)\n" +
                    "2. Посмотреть шаблон для отчёта: если вам нужно ознакомиться с шаблоном для ежедневного отчёта перед его отправкой, выберите этот пункт меню (/report_template)\n" +
                    "3. Назад* (/back)\n" +
                    "4. Выключить бота (/stop)";

            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            userReportStateService.clearUserReportStates(chatId);
            chatStateService.goToPreviousState(chatId);
        }
        if (currentState == CONTACT) {
            String shelterType = lastState == DOG ? "приюте для собак" : "приюте для кошек";
            String responseText = "Вы вернулись назад. Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                    "1. Описание приюта (/description)\n" +
                    "2. Расписание работы и контакты (/schedule)\n" +
                    "3. Контактные данные охраны для пропуска (/pass)\n" +
                    "4. Техника безопасности на территории приюта (/safety)\n" +
                    "5. Оставить контактные данные (/contact)\n" +
                    "6. Позвать волонтера (/help)\n" +
                    "7. Назад (/back)\n" +
                    "8. Выключить бота (/stop)";

            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            chatStateService.goToPreviousState(chatId);
        }

        if (currentState == FIND_USER_BY_PHONE || currentState == FIND_ANIMAL_BY_NAME || currentState == MAKE_ADOPTER) {
            SendMessage message = new SendMessage(chatId.toString(), ADMIN_COMMAND_RETURN_TEXT);
            telegramBot.execute(message);
            chatStateService.goToPreviousState(chatId);
        }

        if (currentState == SHELTER_INFO || currentState == ADOPT || currentState == PET_REPORT) {

            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

            var shelterType = previousState == CAT ? "приют для кошек" : "приют для собак";
            String responseText = "Вы вернулись назад. У вас выбран " + shelterType + ". Чем я могу помочь?\n" +
                    "1. Узнать информацию о приюте (/shelter_info)\n" +
                    "2. Как взять животное из приюта (/adopt)\n" +
                    "3. Прислать отчет о питомце (/pet_report)\n" +
                    "4. Позвать волонтера (/help)\n" +
                    "5. Назад (/back)\n" +
                    "6. Выключить бота (/stop)";

            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            chatStateService.goToPreviousState(chatId);
        } else if (currentState == DOG || currentState == CAT || currentState == START) {
            String responseText = "Вы вернулись в главное меню." +
                    "Чтобы начать приключение и найти своего нового друга, просто выбери один из вариантов ниже:\n" +
                    "    Приют для кошек \uD83D\uDC31: Здесь мы заботимся о пушистых котиках всех возрастов и размеров, каждый из которых ищет свой дом и своего человека. " +
                    "Если у вас есть желание подарить теплый дом и заботу коту, этот приют идеально подойдет. Для выбора введи команду /cat\n" +
                    "    Приют для собак \uD83D\uDC36: Если вы думаете о взятии пушистого друга на прогулки и веселые игры, здесь живут забавные и преданные собачки. " +
                    "От маленьких щенков до зрелых спутников жизни - выбор за вами. Для выбора введи команду /dog\n" +
                    "\n" +
                    "Остановить бота (/stop)";

            telegramBot.execute(new SendMessage(chatId.toString(), responseText));
            if (currentState != START) {
                chatStateService.goToPreviousState(chatId);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        }
    }

    /**
     * Получает команду, обрабатываемую этим обработчиком.
     *
     * @return Команда обработчика.
     */
    @Override
    public BotCommand getCommand() {
        return BACK;
    }
}
