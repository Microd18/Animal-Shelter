package pro.sky.AnimalShelter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.state.ChatStateHolder;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

/**
 * Обработчик команды "/back". Позволяет вернуться в предыдущее меню.
 */
@Service
@RequiredArgsConstructor
public class BackCommandHandler implements CommandHandler {

    /**
     * Хранилище состояний чатов.
     */
    private final ChatStateHolder chatStateHolder;

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
        BotCommand currentState = chatStateHolder.getCurrentStateById(chatId);

        if (currentState == SHELTER_INFO) {
            BotCommand previousState = chatStateHolder.getPreviousState(chatId);
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
            chatStateHolder.addState(chatId, previousState);
        } else if (currentState == DOG || currentState == CAT) {
            chatStateHolder.addState(chatId, START);
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
            String responseText = "Для использования бота введите команду /start";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
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
