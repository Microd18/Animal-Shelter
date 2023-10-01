package pro.sky.AnimalShelter.handlers.sendingReportMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static pro.sky.AnimalShelter.enums.BotCommand.PET_REPORT;
import static pro.sky.AnimalShelter.enums.BotCommand.REPORT_TEMPLATE;

@ExtendWith(MockitoExtension.class)
class ReportTemplateCommandHandlerTest {
    @Mock
    private ChatStateService chatStateService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private CommonUtils commonUtils;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @InjectMocks
    private ReportTemplateCommandHandler reportTemplateCommandHandler;

    @BeforeEach
    void setUp() {
        lenient().when(update.message()).thenReturn(message);
        lenient().when(message.chat()).thenReturn(chat);
        lenient().when(chat.id()).thenReturn(123L);
    }
    @Test
    @DisplayName("Проверяет, что при выполнении команды /pet_report, если текущее состояние чата " +
            "(chatId) равно /pet_report, будет отправлено правильное сообщение в чат с указанным chatId")
    public void testHandleStateSendPetReportCorrectMessage() {
        Long chatId = 123L;
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(PET_REPORT);
        reportTemplateCommandHandler.handle(update);
        String responseText = "Для формирования ежедневного отчета о вашем питомце, пожалуйста, пришлите следующую информацию:\n" +
                "1. Фото животного: в этой части отчета вы можете отправить фотографию вашего питомца. Фото помогут нам видеть, как изменяется ваш пушистый друг со временем.\n" +
                "2. Рацион животного: укажите, что ваш питомец ел сегодня. Это важно для контроля его питания и здоровья. Например, вы можете описать, сколько порций и какой еды ваш питомец съел сегодня.\n" +
                "3. Общее самочувствие и привыкание к новому месту: поделитесь, как себя чувствует ваш пушистый друг. Важно знать, как он приспосабливается к новой обстановке и окружению. Напишите о его настроении, активности или любых изменениях в поведении.\n" +
                "4. Изменение в поведении: опишите любые изменения в поведении вашего питомца. Может быть, он перестал делать что-то, что раньше делал, или, наоборот, начал проявлять новые привычки. Такие наблюдения могут быть важными для понимания его состояния.\n" +
                "5. Назад (/back)\n" +
                "6. Выключить бота (/stop)";
        SendMessage message = new SendMessage(chatId, responseText).parseMode(ParseMode.Markdown);
        telegramBot.execute(message);
        verify(telegramBot, times(1)).execute(message);
    }
    @Test
    @DisplayName("Проверяет, что при вызове метода handle " +
            "класса ReportTemplateCommandHandler в состоянии \"Стоп\" вызывается метод offerToStart " +
            "класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsStop() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.STOP);

        reportTemplateCommandHandler.handle(update);

        verify(commonUtils).offerToStart(123L);
    }

    @Test
    @DisplayName("Проверяет, что при вызове метода handle класса ReportTemplateCommandHandler \" +\n"
            + "в состоянии Назад вызывается метод sendInvalidCommandResponse класса CommonUtils с заданным chatId.")
    public void testHandleWhenCurrentStateIsBack() {
        when(chatStateService.getCurrentStateByChatId(123L)).thenReturn(BotCommand.BACK);

        reportTemplateCommandHandler.handle(update);

        verify(commonUtils).sendInvalidCommandResponse(123L);
    }

    @Test
    @DisplayName("Проверяет, что метод getCommand класса ReportTemplateCommandHandler возвращает правильную команду BotCommand.DATING_RULES")
    public void testGetCommand() {
        BotCommand actualCommand = reportTemplateCommandHandler.getCommand();
        assertEquals(REPORT_TEMPLATE, actualCommand);
    }

}