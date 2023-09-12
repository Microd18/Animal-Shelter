package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.exception.ChatStateNotFoundException;
import pro.sky.AnimalShelter.repository.ChatStateRepository;
import pro.sky.AnimalShelter.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChatStateRepository chatStateRepository;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    public User saveUser(String userName, Long chatId) throws ChatStateNotFoundException {
        if (userRepository.findByChatStateId(chatId).isPresent()) {
            logger.info("User has been already saved");
            return userRepository.findByChatStateId(chatId).get();
        } else {
            User newUser = new User();
            newUser.setUsername(userName);
            newUser.setChatState(chatStateRepository.findByChatId(chatId).get());
            userRepository.save(newUser);
            logger.info("User is saved");
            return newUser;


        }

    }
}
