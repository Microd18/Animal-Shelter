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
public class VerifiedDogHandlersHandler implements CommandHandler {
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
     * Обрабатывает команду "/verified_dog_handlers" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADOPT) {
            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

            if (previousState == DOG) {
                String responseText = "Кинологи Алматы\n" +
                        "1. Анастасия Евгеньевна Коровникова\n" +
                        "Полная информация по ссылке на Профи:" +
                        "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileTabName=reviews&profileId=AnastasiyaKY3" +
                        "             \n" +
                        "2. Коркин Андрей\n" +
                        "Полная информация по ссылке на Профи:" +
                        "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileTabName=reviews&profileId=KorkinAV5&fromSection=page_listing" +
                        "             \n" +
                        "3. Светлана Сергеевна Чернобаева\n" +
                        "Полная информация по ссылке на Профи:" +
                        "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileId=ChernobayevaSS&profileTabName=reviews&fromSection=page_listing" +
                        "             \n" +
                        "4. Эльвира Агзамовна Альмухамедова\n" +
                        "Полная информация по ссылке на Профи:" +
                        "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileId=AlmukhamedovaEA&profileTabName=reviews&fromSection=page_listing" +
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
     * Возвращает команду, связанную с этим обработчиком ("/verified_dog_handlers").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return VERIFIED_DOG_HANDLERS;
    }
}

