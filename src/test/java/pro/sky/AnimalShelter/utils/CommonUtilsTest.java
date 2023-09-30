package pro.sky.AnimalShelter.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static pro.sky.AnimalShelter.utils.MessagesBot.OFFER_TO_START_TEXT;
import static pro.sky.AnimalShelter.utils.MessagesBot.SEND_INVALID_COMMAND_RESPONSE_TEXT;

@ExtendWith(MockitoExtension.class)
public class CommonUtilsTest {
    @Mock
    private TelegramBot telegramBot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;
    @InjectMocks
    private CommonUtils commonUtils;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);

    }

    @Test
    @DisplayName("Проверка, что метод offerToStart корректно формируют сообщения и отправляют их через TelegramBot")
    public void testOfferToStart() {
        Long chatId = 123L;
        commonUtils.offerToStart(chatId);
        SendMessage message = new SendMessage(chatId, OFFER_TO_START_TEXT);
        telegramBot.execute(message);
        verify(telegramBot).execute(message);
    }


    @Test
    @DisplayName("Проверка, что метод SendInvalidCommandResponse корректно формируют сообщения и отправляют их через TelegramBot")
    public void testSendInvalidCommandResponse() {
        Long chatId = 123L;
        commonUtils.sendInvalidCommandResponse(chatId);
        SendMessage message = new SendMessage(chatId, SEND_INVALID_COMMAND_RESPONSE_TEXT);
        telegramBot.execute(message);
        verify(telegramBot).execute(message);
    }
}