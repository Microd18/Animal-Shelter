package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.repository.ChatStateRepository;
import pro.sky.AnimalShelter.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChatStateRepository chatStateRepository;

    //TODO переписать в рамках 7 задачи по сохранению контактных данных
    public User saveUser(String userName, Long chatId)  {
        if (userRepository.findByChatStateId(chatId).isPresent()) {
            log.info("User has been already saved");
            return userRepository.findByChatStateId(chatId).get();
        } else {
            User newUser = new User();
            newUser.setUsername(userName);
            newUser.setChatState(chatStateRepository.findByChatId(chatId).get());
            userRepository.save(newUser);
            log.info("User is saved");
            return newUser;
        }
    }
}
