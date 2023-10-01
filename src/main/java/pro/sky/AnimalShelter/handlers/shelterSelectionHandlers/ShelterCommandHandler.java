package pro.sky.AnimalShelter.handlers.shelterSelectionHandlers;

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
import static pro.sky.AnimalShelter.utils.MessagesBot.SHELTER_COMMAND_TEXT;

/**
 * Абстрактный класс для обработки команд в контексте приютов.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public abstract class ShelterCommandHandler implements CommandHandler {

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
     * Команда, связанная с текущим обработчиком.
     */
    private final BotCommand selectedCommand;

    /**
     * Тип приюта, связанный с текущим обработчиком (например, "приют для кошек").
     */
    private final String shelterType;

    /**
     * Обрабатывает команду, основываясь на текущем состоянии чата и выбранной команде.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == START) {
            String responseText = "Вы выбрали " + shelterType + SHELTER_COMMAND_TEXT;
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            chatStateService.updateChatState(chatId, selectedCommand);
        } else if (currentState == CAT || currentState == DOG) {
            var shelter = currentState == CAT ? "приют для кошек" : "приют для собак";
            String responseText = "Вы уже выбрали " + shelter + ".\n" +
                    " Для возврата в предыдущее меню введите команду назад /back,\n" +
                    " Чтобы выключить бота введите команду /stop";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }
}
