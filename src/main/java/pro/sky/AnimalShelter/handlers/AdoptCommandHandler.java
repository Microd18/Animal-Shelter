package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

@Service
@RequiredArgsConstructor
public class AdoptCommandHandler implements CommandHandler {

    /**
     * Хранилище состояний чатов.
     */
    private final ChatStateHolder chatStateHolder;

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
        BotCommand currentState = chatStateHolder.getCurrentStateById(chatId);
        BotCommand previousState = chatStateHolder.getPreviousState(chatId);

        if (currentState == DOG || (currentState == ADOPT && previousState == DOG)) {
            String menuMessage = currentState == ADOPT ? "Вы уже в этом меню. " : "";
            String responseText = menuMessage + "В этом меню я расскажу вам как взять животное из приюта для собак. Какую информацию вы бы хотели получить?\n" +
                    "1. Правила знакомства с животным (/dating_rules)\n" +
                    "2. Список необходимых документов (/documents)\n" +
                    "3. Рекомендации по транспортировке животного (/transportation_recommendation)\n" +
                    "4. Рекомендации по обустройству дома для щенка (/puppy_home_setup_recommendation)\n" +
                    "5. Рекомендации по обустройству дома для взрослого животного (/home_setup_recommendations)\n" +
                    "6. Рекомендации по обустройству дома для животного с ограниченными возможностями (/special_need_recommendation)\n" +
                    "7. Советы кинолога (/dog_trainer_advice)\n" +
                    "8. Рекомендации по проверенным кинологам (/verified_dog_handlers)\n" +
                    "9. Причины, почему могут отказать и не дать забрать собаку из приюта (/refusal_reason)\n" +
                    "10. Оставить контактные данные (/contact)\n" +
                    "11. Позвать волонтера (/help)\n" +
                    "12. Назад (/back)\n" +
                    "13. Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            if (!(currentState == ADOPT)) {
                chatStateHolder.addState(chatId, ADOPT);
            }
        } else if (currentState == CAT || (currentState == ADOPT && previousState == CAT)) {
            String menuMessage = currentState == ADOPT ? "Вы уже в этом меню. " : "";
            String responseText = menuMessage + "В этом меню я расскажу вам как взять животное из приюта для кошек. Какую информацию вы бы хотели получить?\n" +
                    "1. Правила знакомства с животным (/dating_rules)\n" +
                    "2. Список необходимых документов (/documents)\n" +
                    "3. Рекомендации по транспортировке животного (/transportation_recommendation)\n" +
                    "4. Рекомендации по обустройству дома для котёнка (/kitty_home_setup_recommendation)\n" +
                    "5. Рекомендации по обустройству дома для взрослого животного (/home_setup_recommendations)\n" +
                    "6. Рекомендации по обустройству дома для животного с ограниченными возможностями (/special_need_recommendation)\n" +
                    "7. Оставить контактные данные (/contact)\n" +
                    "8. Позвать волонтера (/help)\n" +
                    "9. Назад (/back)\n" +
                    "10. Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            if (!(currentState == ADOPT)) {
                chatStateHolder.addState(chatId, ADOPT);
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
