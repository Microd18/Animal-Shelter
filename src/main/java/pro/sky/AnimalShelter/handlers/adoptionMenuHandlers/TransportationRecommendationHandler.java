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
public class TransportationRecommendationHandler implements CommandHandler {
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
     * Обрабатывает команду "/transportation_recommendation" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADOPT) {
            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

            if (previousState == DOG) {
                String responseText = "Желательно для перевозки собаки иметь специальную клетку-контейнер, либо переноску, как для кошек,допустимо для мелких пород собак.\n" +
                        "             \n" +
                        "Возврат в предыдущее меню (/back)\n" +
                        "Выключить бота (/stop)";
                SendMessage message = new SendMessage(chatId.toString(), responseText);
                telegramBot.execute(message);
            }
            if (previousState == CAT) {
                String responseText = "Для перевозки кошки обязательно нужна переноска для кошек. \n" +
                        "             \n" +
                        "Возврат в предыдущее меню (/back)\n" +
                        "Выключить бота (/stop)";
                SendMessage message = new SendMessage(chatId.toString(), responseText);
                telegramBot.execute(message);
            }
        } else if (currentState == STOP) {
                commonUtils.offerToStart(chatId);
            } else {
                commonUtils.sendInvalidCommandResponse(chatId);
            }
        }

        /**
         * Возвращает команду, связанную с этим обработчиком ("/transportation_recommendation").
         *
         * @return Команда, связанная с обработчиком.
         */

        @Override
        public BotCommand getCommand () {
            return TRANSPORTATION_RECOMMENDATION;
        }
    }
