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

/**
 * Обработчик команды "/safety".
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SafetyCommandHandler implements CommandHandler {

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
     * Обрабатывает команду "/safety" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == SHELTER_INFO) {
            String responseText = "Находясь на территории приюта, пожалуйста, соблюдайте наши правила и технику безопасности!\n" +
                    "\n" +
                    "                ЗАПРЕЩАЕТСЯ:\n" +
                    "\n" +
                    "- Самостоятельно открывать выгулы и вольеры без разрешения работника приюта.\n" +
                    "\n" +
                    "- Кормить животных. Этим Вы можете спровоцировать драку. \n" +
                    "\n" +
                    "- Оставлять после себя мусор на территории приюта и прилегающей территории.\n" +
                    "\n" +
                    "- Подходить близко к вольерам и гладить собак через сетку на выгулах. Животные могут быть агрессивны!\n" +
                    "\n" +
                    "- Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных.\n" +
                    "\n" +
                    "- Посещение приюта для детей дошкольного и младшего школьного возраста без сопровождения взрослых.\n" +
                    "\n" +
                    "- Нахождение на территории приюта детей среднего и старшего школьного возраста без  сопровождения взрослых или письменной справки-разрешения от родителей или законных представителей.\n" +
                    "\n" +
                    "- Посещение приюта в состоянии алкогольного, наркотического опьянения.\n" +
                    " Возврат в предыдущее меню (/back)\n" +
                    " Выключить бота (/stop)";

            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/safety").
     *
     * @return Команда, связанная с обработчиком.
     */
    @Override
    public BotCommand getCommand() {
        return SAFETY;
    }
}
