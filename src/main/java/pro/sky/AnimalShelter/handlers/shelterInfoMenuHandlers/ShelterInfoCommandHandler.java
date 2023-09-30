package pro.sky.AnimalShelter.handlers.shelterInfoMenuHandlers;

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
import static pro.sky.AnimalShelter.utils.MessagesBot.SHELTER_INFO_COMMAND_TEXT;

/**
 * Обработчик команды "/shelter_info".
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShelterInfoCommandHandler implements CommandHandler {

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
     * Обрабатывает команду "/shelter_info" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == DOG || currentState == CAT || currentState == SHELTER_INFO) {
            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);
            String s = currentState == SHELTER_INFO ? "Вы уже в этом меню." : "";
            String shelterType = currentState == DOG ? "приюте для собак" : currentState == SHELTER_INFO
                    ? previousState == DOG ? "приюте для собак" : "приюте для кошек" : "приюте для кошек";
            String responseText = s + "Какую информацию вы бы хотели получить о " + shelterType + ":\n" +
                    SHELTER_INFO_COMMAND_TEXT;
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);

            if (!(currentState == SHELTER_INFO)) {
                chatStateService.updateChatState(chatId, SHELTER_INFO);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/shelter_info").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return SHELTER_INFO;
    }
}
