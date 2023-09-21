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
public class SpecialNeedsRecommendationHandler implements CommandHandler {
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
     * Обрабатывает команду "/special_need_recommendation" в зависимости от текущего состояния чата.
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
                String responseText = "Создание безопасного и уютного дома для собаки-инвалида\n" +
                        "\n" +
                        "Собаки-инвалида нуждаются в особом уходе и внимании, особенно когда дело касается их дома. Вот несколько советов, как создать безопасный и уютный дом для вашей собаки-спинальника.\n" +
                        "\n" +
                        "Используйте мягкие поверхности. Собаки-инвалида часто испытывают боли в области спины, поэтому им нужны мягкие и удобные поверхности, на которых они могут отдыхать. Избегайте твердых и гладких поверхностей, таких как кафель или ламинат, в пользу ковров, ковровых дорожек и мягких покрытий.\n" +
                        "\n" +
                        "Обеспечьте безопасность. Избегайте использования материалов, которые могут быть опасными для вашей собаки-инвалида. Например, избегайте использования лакокрасочных материалов, которые могут содержать токсичные вещества. Также обратите внимание на острые углы и края мебели и других предметов, которые могут причинить травмы.\n" +
                        "\n" +
                        "Предоставьте лестницы и полки. Собаки-инвалида могут иметь трудности с перемещением по дому, поэтому предоставление им лестниц и полок поможет им легче перемещаться. Полки также могут быть использованы в качестве места отдыха, где ваша кошка-инвалид может чувствовать себя безопасно.\n" +
                        "\n" +
                        "Предоставьте место для еды и воды. Важно, чтобы ваша кошка-инвалид имела легкий доступ к месту для еды и воды. При этом следует убедиться, что миски находятся на уровне, который не потребует от собаки-инвалида слишком большого усилия.\n" +
                        "             \n" +
                        "Возврат в предыдущее меню (/back)\n" +
                        "Выключить бота (/stop)";
                SendMessage message = new SendMessage(chatId.toString(), responseText);
                telegramBot.execute(message);
            }
            if (previousState == CAT) {
                String responseText = "Создание безопасного и уютного дома для кошки-инвалида\n" +
                        "\n" +
                        "Кошки-инвалида нуждаются в особом уходе и внимании, особенно когда дело касается их дома. Вот несколько советов, как создать безопасный и уютный дом для вашей кошки-спинальника.\n" +
                        "\n" +
                        "Используйте мягкие поверхности. Кошки-инвалида часто испытывают боли в области спины, поэтому им нужны мягкие и удобные поверхности, на которых они могут отдыхать. Избегайте твердых и гладких поверхностей, таких как кафель или ламинат, в пользу ковров, ковровых дорожек и мягких покрытий.\n" +
                        "\n" +
                        "Обеспечьте безопасность. Избегайте использования материалов, которые могут быть опасными для вашей кошки-инвалида. Например, избегайте использования лакокрасочных материалов, которые могут содержать токсичные вещества. Также обратите внимание на острые углы и края мебели и других предметов, которые могут причинить травмы.\n" +
                        "\n" +
                        "Предоставьте лестницы и полки. Кошки-инвалида могут иметь трудности с перемещением по дому, поэтому предоставление им лестниц и полок поможет им легче перемещаться. Полки также могут быть использованы в качестве места отдыха, где ваша кошка-инвалид может чувствовать себя безопасно.\n" +
                        "\n" +
                        "Предоставьте место для еды и воды. Важно, чтобы ваша кошка-инвалид имела легкий доступ к месту для еды и воды. При этом следует убедиться, что миски находятся на уровне, который не потребует от кошки-инвалида слишком большого усилия.\n" +
                        "\n" +
                        "Убедитесь в наличии места для лотка. Кошки-инвалиды могут иметь трудности с использованием лотка, поэтому следует предоставить им легкий доступ к месту для схода в туалет.\n" +
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
     * Возвращает команду, связанную с этим обработчиком ("/special_need_recommendation").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return SPECIAL_NEED_RECOMMENDATION;
    }
}
