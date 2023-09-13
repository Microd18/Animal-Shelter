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
public class DocumentsListHandler implements CommandHandler {
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

        if (currentState == DOG || currentState == CAT || currentState == ADOPT || (currentState == DOCUMENTS && previousState == ADOPT)) {
            String menuMessage = currentState == DOCUMENTS ? "Вы уже в этом меню. \n" : "";
            String responseText = menuMessage + "     В этом меню я расскажу какие документы нужны, чтобы взять животное из приюта.\n" +
                    "             \n" +
                    "  Чтобы забрать животное из приюта, необходимо при себе необходимо иметь копию паспорта и справку с места жительства.\n " +
                    "Приют пристраивает только обработанных от паразитов, вакцинированных и стерилизованных животных.\n" +
                    "\n" +
                    "После передачи животного администрация приюта просит новых владельцев раз в месяц отправлять фотоотчёты о жизни бывшего подопечного.\n" +
                    "             \n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            if (!(currentState == DOCUMENTS)) {
                chatStateService.updateChatState(chatId, DOCUMENTS);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/documents").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return BotCommand.DOCUMENTS;
    }
}
