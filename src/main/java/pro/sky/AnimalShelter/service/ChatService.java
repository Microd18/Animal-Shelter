package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.repository.ChatRepository;

/**
 * Сервис для работы с чатами.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    /**
     * Репозиторий для доступа к данным о чатах.
     */
    private final ChatRepository chatRepository;

    /**
     * Проверяет, запущен ли бот в чате с указанным идентификатором.
     *
     * @param chatId Идентификатор чата.
     * @return {@code true}, если бот запущен в чате, в противном случае - {@code false}.
     */
    public Boolean isBotStarted(Long chatId) {
        log.info("isBotStarted method was invoked");
        if (chatRepository.findByChatId(chatId).isEmpty()) {
            return false;
        } else {
            Chat foundChat = chatRepository.findByChatId(chatId).get();
            return foundChat.isBotStarted();
        }
    }
}
