package pro.sky.AnimalShelter.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для настройки Telegram бота.
 */
@Configuration
public class TelegramBotConfiguration {

    /**
     * Значение токена Telegram бота, считываемое из конфигурации.
     */
    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Создает и настраивает экземпляр TelegramBot с использованием токена.
     *
     * @return Экземпляр TelegramBot.
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);

        // Удаляет предыдущие команды бота.
        bot.execute(new DeleteMyCommands());

        return bot;
    }
}
