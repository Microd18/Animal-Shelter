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
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

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
        BotCommand lastState = chatStateService.getLastStateByChatId(chatId);
        if (currentState == CONTACT) {
            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);
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
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            chatStateService.updateChatState(chatId, START);
            chatStateService.updateChatState(chatId, lastState);
            chatStateService.updateChatState(chatId, previousState);
        }
        if (currentState == SHELTER_INFO || currentState == ADOPT) {
            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

            var shelterType = previousState == CAT ? "приют для кошек" : "приют для собак";
            String responseText = "Вы вернулись назад. У вас выбран " + shelterType + ". Чем я могу помочь?\n" +
                    "1. Узнать информацию о приюте (/shelter_info)\n" +
                    "2. Как взять животное из приюта (/adopt)\n" +
                    "3. Прислать отчет о питомце (/pet_report)\n" +
                    "4. Позвать волонтера (/help)\n" +
                    "5. Назад (/back)\n" +
                    "6. Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            chatStateService.updateChatState(chatId, previousState);
            //todo тут будет не до конца корректно, так как шаг назад станет текущим, а два шага назад там и останется
        } else if (currentState == DOG || currentState == CAT) {
            chatStateService.updateChatState(chatId, START);
            String responseText = "Вы вернулись в главное меню." +
                    "Чтобы начать приключение и найти своего нового друга, просто выбери один из вариантов ниже:\n" +
                    "    Приют для кошек \uD83D\uDC31: Здесь мы заботимся о пушистых котиках всех возрастов и размеров, каждый из которых ищет свой дом и своего человека. " +
                    "Если у вас есть желание подарить теплый дом и заботу коту, этот приют идеально подойдет. Для выбора введи команду /cat\n" +
                    "    Приют для собак \uD83D\uDC36: Если вы думаете о взятии пушистого друга на прогулки и веселые игры, здесь живут забавные и преданные собачки. " +
                    "От маленьких щенков до зрелых спутников жизни - выбор за вами. Для выбора введи команду /dog\n" +
                    "\n" +
                    "Остановить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
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
