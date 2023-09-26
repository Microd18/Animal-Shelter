package pro.sky.AnimalShelter.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.repository.ChatRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest
class ChatServiceTest {

    @MockBean
    private ChatRepository chatRepository;

    @SpyBean
    private ChatService chatService;

    @Test
    public void isBotStartedTest() {
        Chat chat = new Chat(null, true, null, null, null);

        when(chatRepository.findByChatId(any(Long.class))).thenReturn(Optional.of(chat));

        Assertions.assertThat(chatService.isBotStarted(null)).isEqualTo(false);
        Assertions.assertThat(chatService.isBotStarted(1L)).isEqualTo(true);
    }

}