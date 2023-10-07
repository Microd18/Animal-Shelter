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
import static pro.sky.AnimalShelter.utils.MessagesBot.PASS_COMMAND_CAT_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.PASS_COMMAND_DOG_TEXT;

/**
 * Обработчик команды "/pass".
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PassCommandHandler implements CommandHandler {

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
     * Обрабатывает команду "/pass" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == SHELTER_INFO) {
            BotCommand previousState = chatStateService.getLastStateCatOrDogByChatId(chatId);

            if (previousState == DOG) {
                SendMessage message = new SendMessage(chatId.toString(), PASS_COMMAND_DOG_TEXT);
                telegramBot.execute(message);
            }
            if (previousState == CAT) {
                SendMessage message = new SendMessage(chatId.toString(), PASS_COMMAND_CAT_TEXT);
                telegramBot.execute(message);
            }

        } else if (currentState == STOP){
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/pass").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return PASS;
    }
}
