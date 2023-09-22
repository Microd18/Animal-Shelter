package pro.sky.AnimalShelter.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Утилитарный компонент для работы с Telegram ботом.
 */
@Component
@RequiredArgsConstructor
public class CommonUtils {

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Отправляет сообщение с предложением начать использовать бота.
     *
     * @param chatId ID чата для отправки сообщения.
     */
    public void offerToStart(final Long chatId) {
        String responseText = "Для использования бота введите команду /start";
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
    }

    /**
     * Отправляет сообщение о недопустимой команде.
     *
     * @param chatId ID чата для отправки сообщения.
     */
    public void sendInvalidCommandResponse(final Long chatId) {
        String responseText = "Данная команда не допустима в этом меню.\n" +
                " Для возврата в предыдущее меню введите команду назад /back\n" +
                " Чтобы выключить бота введите команду /stop";
        SendMessage message = new SendMessage(chatId.toString(), responseText);
        telegramBot.execute(message);
    }
}
