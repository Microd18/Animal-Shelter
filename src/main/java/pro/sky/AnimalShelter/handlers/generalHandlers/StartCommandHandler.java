package pro.sky.AnimalShelter.handlers.generalHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatService;
import pro.sky.AnimalShelter.service.ChatStateService;

import static pro.sky.AnimalShelter.enums.BotCommand.START;

/**
 * Обработчик команды "/start".
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {

    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    private final ChatService chatService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Обрабатывает команду "/start" и инициализирует бота.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        log.info("Bot received the /start command. Inclusion...");
        Long chatId = update.message().chat().id();
        if (chatService.isBotStarted(chatId)) {
            telegramBot.execute(new SendMessage(chatId.toString(), "Бот уже запущен"));
            return;
        }
        String response = "Добро пожаловать! \uD83C\uDF1F\n" +
                "Я - твой верный компаньон, телеграм-бот помощник. " +
                "Моя цель - помогать тебе найти идеального пушистого или верного друга на четырех лапках. \uD83D\uDC36\uD83D\uDC31\n" +
                "\n" +
                "Чтобы начать приключение и найти своего нового друга, просто выбери один из вариантов ниже:\n" +
                "\n" +
                "    Приют для кошек \uD83D\uDC31: Здесь мы заботимся о пушистых котиках всех возрастов и размеров, каждый из которых ищет свой дом и своего человека. " +
                "Если у вас есть желание подарить теплый дом и заботу коту, этот приют идеально подойдет. Для выбора введи команду /cat\n" +
                "\n" +
                "    Приют для собак \uD83D\uDC36: Если вы думаете о взятии пушистого друга на прогулки и веселые игры, здесь живут забавные и преданные собачки. " +
                "От маленьких щенков до зрелых спутников жизни - выбор за вами. Для выбора введи команду /dog\n" +
                "\n" +
                "Остановить бота (/stop)";


        telegramBot.execute(new SendMessage(chatId.toString(), response));
        chatStateService.updateChatState(chatId, START);
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/start").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return START;
    }
}
