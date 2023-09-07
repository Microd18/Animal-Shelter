package pro.sky.AnimalShelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.User;
import pro.sky.AnimalShelter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(String userName, Long chatId) {
        if (userRepository.findByChatId(chatId).isPresent()){
            logger.info("User has been already saved");
            return userRepository.findByChatId(chatId).get();
        } else {
            User newUser = new User(userName, chatId);
            userRepository.save(newUser);
            logger.info("User is saved");
            return newUser;
        }

    }
}
