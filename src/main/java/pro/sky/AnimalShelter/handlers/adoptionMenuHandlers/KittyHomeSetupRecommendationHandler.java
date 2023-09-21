package pro.sky.AnimalShelter.handlers.adoptionMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

@Service
@RequiredArgsConstructor
public class KittyHomeSetupRecommendationHandler implements CommandHandler {
    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Обрабатывает команду "/kitty_home_setup_recommendation" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADOPT) {
            String responseText = "    6 советов по обустройству дома для котенка:\n" +
                    "1.Уберите мелкие предметы.\n" +
                    "2.Уделите максимум внимания в первые дни\n" +
                    "3.Подготовьте котенку место для сна\n" +
                    "4.Подготовьте лоток и место для приема пищи\n" +
                    "5.Приготовьте игрушки для котенка\n" +
                    "6.Будьте готовы к уходу за когтями\n" +
                    "             \n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);

        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/kitty_home_setup_recommendation").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return KITTY_HOME_SETUP_RECOMMENDATION;
    }
}
