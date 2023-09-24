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
import static pro.sky.AnimalShelter.utils.MessagesBot.ADOPT_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.ADOPT_DOG_TEXT;

@Service
@RequiredArgsConstructor
public class AdoptCommandHandler implements CommandHandler {

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
     * Обрабатывает команду "/adopt" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

        if (currentState == DOG || (currentState == ADOPT && previousState == DOG)) {
            String menuMessage = currentState == ADOPT ? "Вы уже в этом меню. " : "";
            String responseText = menuMessage + ADOPT_DOG_TEXT;
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            if (!(currentState == ADOPT)) {
                chatStateService.updateChatState(chatId, ADOPT);
            }
        } else if (currentState == CAT || (currentState == ADOPT && previousState == CAT)) {
            String menuMessage = currentState == ADOPT ? "Вы уже в этом меню. " : "";
            String responseText = menuMessage + ADOPT_CAT_TEXT;
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            if (!(currentState == ADOPT)) {
                chatStateService.updateChatState(chatId, ADOPT);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/adopt").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return ADOPT;
    }
}
