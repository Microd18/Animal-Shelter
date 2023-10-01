package pro.sky.AnimalShelter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.repository.ChatRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    public void testIsBotStartedWhenChatDoesNotExist() {
        when(chatRepository.findByChatId(anyLong())).thenReturn(Optional.empty());

        boolean result = chatService.isBotStarted(123L);

        verify(chatRepository, times(1)).findByChatId(anyLong());
        assertFalse(result);
    }

    @Test
    public void testIsBotStartedWhenBotIsNotStarted() {
        Chat chat = new Chat();
        chat.setBotStarted(false);

        when(chatRepository.findByChatId(anyLong())).thenReturn(Optional.of(chat));

        boolean result = chatService.isBotStarted(123L);

        verify(chatRepository, times(2)).findByChatId(anyLong());
        assertFalse(result);
    }

    @Test
    public void testIsBotStartedWhenBotIsStarted() {
        Chat chat = new Chat();
        chat.setBotStarted(true);

        when(chatRepository.findByChatId(anyLong())).thenReturn(Optional.of(chat));

        boolean result = chatService.isBotStarted(123L);

        verify(chatRepository, times(2)).findByChatId(anyLong());
        assertTrue(result);
    }
}