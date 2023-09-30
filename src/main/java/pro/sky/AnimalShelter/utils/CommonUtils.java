package pro.sky.AnimalShelter.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static pro.sky.AnimalShelter.utils.MessagesBot.OFFER_TO_START_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.SEND_INVALID_COMMAND_RESPONSE_TEXT;

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
        SendMessage message = new SendMessage(chatId.toString(), OFFER_TO_START_TEXT);
        telegramBot.execute(message);
    }

    /**
     * Отправляет сообщение о недопустимой команде.
     *
     * @param chatId ID чата для отправки сообщения.
     */
    public void sendInvalidCommandResponse(final Long chatId) {
        SendMessage message = new SendMessage(chatId.toString(), SEND_INVALID_COMMAND_RESPONSE_TEXT);
        telegramBot.execute(message);
    }

}
