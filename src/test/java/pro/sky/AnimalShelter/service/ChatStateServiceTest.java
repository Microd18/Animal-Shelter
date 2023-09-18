package pro.sky.AnimalShelter.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.ChatStateRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pro.sky.AnimalShelter.enums.BotCommand.*;

@WebMvcTest
public class ChatStateServiceTest {

    @MockBean
    private ChatStateRepository chatStateRepository;

    @MockBean
    private ChatRepository chatRepository;

    @SpyBean
    private ChatStateService chatStateService;

    @Test
    public void getCurrentStateByChatIdTest() {
        Chat chat1 = new Chat(1L, true, null, null);
        Chat chat2 = new Chat(2L, false, null, null);
        ChatState chatState = new ChatState(START, DOG, SHELTER_INFO, chat1);

        when(chatRepository.findByChatId(eq(1L))).thenReturn(Optional.of(chat1));
        when(chatStateRepository.findByChatId(null)).thenReturn(Optional.of(chatState));
        when(chatRepository.findByChatId(eq(2L))).thenReturn(Optional.of(chat2));

        Assertions.assertThat(chatStateService.getCurrentStateByChatId(1L)).isEqualTo(SHELTER_INFO);
        Assertions.assertThat(chatStateService.getCurrentStateByChatId(2L)).isEqualTo(STOP);
    }

    @Test
    public void getPreviousStateByChatIdTest() {

        Chat chat1 = new Chat(1L, true, null, null);
        ChatState chatState = new ChatState(DOG, SHELTER_INFO, HELP, chat1);

        when(chatRepository.findByChatId(eq(1L))).thenReturn(Optional.of(chat1));
        Assertions.assertThat(chatStateService.getPreviousStateByChatId(1L)).isEqualTo(STOP);

        when(chatStateRepository.findByChatId(null)).thenReturn(Optional.of(chatState));
        Assertions.assertThat(chatStateService.getPreviousStateByChatId(1L)).isEqualTo(SHELTER_INFO);

        when(chatStateRepository.findByChatId(null)).thenReturn(null);
        Assertions.assertThat(chatStateService.getPreviousStateByChatId(null)).isEqualTo(STOP);
    }

    @Test
    public void stopBotTest() {
        Chat chat1 = new Chat(1L, true, null, null);
        Chat chat2 = new Chat(2L, false, null, null);
        ChatState actualState = new ChatState(DOG, SHELTER_INFO, SAFETY, chat1);
        ChatState expectedState = new ChatState(null, null, null, chat2);

        when(chatRepository.findByChatId(any())).thenReturn(Optional.of(chat1));
        when(chatStateRepository.findByChatId(any())).thenReturn(Optional.of(actualState));

        chatStateService.stopBot(1L);

        Assertions.assertThat(actualState.getCurrentState()).isEqualTo(expectedState.getCurrentState());
        Assertions.assertThat(actualState.getStepBackState()).isEqualTo(expectedState.getStepBackState());
        Assertions.assertThat(actualState.getTwoStepBackState()).isEqualTo(expectedState.getTwoStepBackState());
        Assertions.assertThat(chat1.isBotStarted()).isEqualTo(chat2.isBotStarted());
    }

    @Test
    public void updateChatStateTest() {
        Chat chat1 = new Chat(1L, false, null, null);
        Chat chat2 = new Chat(2L, true, null, null);

        ChatState chatState1 = new ChatState(DOG, SHELTER_INFO, HELP, chat1);
        ChatState chatState2 = new ChatState(CAT, CAT, CAT, chat1);


        when(chatRepository.findByChatId(eq(1L))).thenReturn(Optional.of(chat1));
        when(chatRepository.findByChatId(eq(2L))).thenReturn(Optional.empty());
        when(chatStateRepository.findByChatId(any())).thenReturn(Optional.of(chatState1));

        chatStateService.updateChatState(1L, CAT);
        Assertions.assertThat(chatState1.getCurrentState()).isEqualTo(chatState2.getCurrentState());

        chatStateService.updateChatState(2L, START);
        Assertions.assertThat(chatState1.getCurrentState()).isEqualTo(chatState2.getCurrentState());

        chatStateService.updateChatState(1L, START);
        Assertions.assertThat(chat1.isBotStarted()).isEqualTo(chat2.isBotStarted());
    }


}

