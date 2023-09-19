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

@Slf4j
@Service
@RequiredArgsConstructor
public class DescriptionCommandHandler implements CommandHandler {

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
     * Описание кошачьего приюта.
     */
    private final String CAT_SHELTER_DESCRIPTION =
            "Наш «кошачий рай»\uD83D\uDC31 занимает площадь 500 квадратных метров и насчитывает более 150 питомцев, " +
                    "2 постоянных сотрудника и 10 волонтеров.\n\n" +
                    "Сотрудники и волонтеры приюта занимаются следующими вопросами:\n" +
                    "• Консультируют и дают советы по различным вопросам, касающихся кошек и котят " +
                    "(поведенческие проблемы, поиск новых домов для кошек и т. д.)\n" +
                    "• Находят новые дома для жителей приюта, все кошки из приюта могут быть усыновлены.\n" +
                    "• Следят за состоянием усыновленных кошек в течение 30 дней и окончательно " +
                    "передают питомца только в надежные руки." +
                    "• Заботятся обо всех кошках, находящихся в приюте. Если кошку никто не усыновил, " +
                    "она проживает там до самой смерти от старости.\n" +
                    "• Вновь прибывшей кошке оказывается вся необходимая ветеринарная помощь. " +
                    "Затем она помещается в коттедж, где она проживает какое-то время, чтобы ознакомиться с окружением. " +
                    "Как только кошка привыкнет, она выпускается из коттеджа и может бродить по территории по своему желанию.\n\n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";

    /**
     * Описание собачьего приюта.
     */
    private final String DOG_SHELTER_DESCRIPTION =
            "Наша «обитель для собак»\uD83D\uDC36 находится на площади в 600 квадратных метров и населяется более чем 100 пушистыми друзьями. " +
                    "У нас работает 3 постоянных сотрудника и 12 преданных волонтеров.\n\n" +
                    "Сотрудники и волонтеры отвечают за следующие вопросы:\n" +
                    "• Предоставляют консультации и делятся советами по всем аспектам ухода за собаками и щенками, " +
                    "вопросам здоровья, их воспитанию, а также помогают найти новые дома для наших подопечных.\n" +
                    "• Следят за состоянием и благополучием усыновленных собак в течение 30 дней и убеждаются, " +
                    "что питомцы попадают в надежные и заботливые руки.\n" +
                    "• Заботятся и ухаживают за всеми собаками, находящимися в нашем приюте. " +
                    "Если собака не находит семью, мы берем на себя ответственность " +
                    "за ее благополучие и уход до конца ее жизни.\n" +
                    "• Каждая прибывшая собака получает необходимую ветеринарную помощь. " +
                    "Затем ее временно размещают в отдельном вольере, где она может привыкнуть к новому окружению. " +
                    "Как только собака адаптируется и социализируется, она получает свободу гулять в вольере на несколько собак.\n\n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";

    /**
     * Обрабатывает команду "/description" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == SHELTER_INFO) {
            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

            if (previousState == DOG) {
                SendMessage message = new SendMessage(chatId.toString(), DOG_SHELTER_DESCRIPTION);
                telegramBot.execute(message);
            }
            if (previousState == CAT) {
                SendMessage message = new SendMessage(chatId.toString(), CAT_SHELTER_DESCRIPTION);
                telegramBot.execute(message);
            }

        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/description").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return DESCRIPTION;
    }
}

