package pro.sky.AnimalShelter.handlers.sendingReportMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

/**
 * Обработчик команды "/report_template".
 */
@Service
@RequiredArgsConstructor
public class ReportTemplateCommandHandler implements CommandHandler {

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
     * Обрабатывает команду "/report_template" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == PET_REPORT) {
            String responseText = "Для формирования ежедневного отчета о вашем питомце, пожалуйста, пришлите следующую информацию:\n" +
                    "1. Фото животного: в этой части отчета вы можете отправить фотографию вашего питомца. Фото помогут нам видеть, как изменяется ваш пушистый друг со временем.\n" +
                    "2. Рацион животного: укажите, что ваш питомец ел сегодня. Это важно для контроля его питания и здоровья. Например, вы можете описать, сколько порций и какой еды ваш питомец съел сегодня.\n" +
                    "3. Общее самочувствие и привыкание к новому месту: поделитесь, как себя чувствует ваш пушистый друг. Важно знать, как он приспосабливается к новой обстановке и окружению. Напишите о его настроении, активности или любых изменениях в поведении.\n" +
                    "4. Изменение в поведении: опишите любые изменения в поведении вашего питомца. Может быть, он перестал делать что-то, что раньше делал, или, наоборот, начал проявлять новые привычки. Такие наблюдения могут быть важными для понимания его состояния.\n" +
                    "5. Назад (/back)\n" +
                    "6. Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText).parseMode(ParseMode.Markdown);
            telegramBot.execute(message);
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/report_template").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return REPORT_TEMPLATE;
    }
}
