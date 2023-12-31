package pro.sky.AnimalShelter.handlers.adoptionMenuHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.handlers.CommandHandler;
import pro.sky.AnimalShelter.service.ChatStateService;
import pro.sky.AnimalShelter.utils.CommonUtils;

import static pro.sky.AnimalShelter.enums.BotCommand.*;

@Service
@RequiredArgsConstructor
public class DogTrainerAdviceHandler implements CommandHandler {
    /**
     * Сервис для управления очередью состояний чатов.
     */
    private final ChatStateService chatStateService;

    /**
     * Экземпляр Telegram-бота для отправки сообщений.
     */
    private final TelegramBot telegramBot;

    /**
     * Экземпляр утилитарного класс для общих методов.
     */
    private final CommonUtils commonUtils;

    /**
     * Обрабатывает команду "/dog_trainer_advice" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        if (currentState == ADOPT) {
            BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

            if (previousState == DOG) {
                String responseText = "1. Будьте надёжным\n" +
                        "Собака – это зеркало владельца. Как вы будете относиться к ней, так она будет отражать это отношение к вам. " +
                        "Важно быть надежным и предсказуемым для своего питомца: не нужно кричать на собаку во время обучения, устанавливать правила, а затем их менять, постоянно изменять график прогулок и кормления. " +
                        "При общении со своим питомцем важно следить и за своим состоянием: собака всегда поймет, когда вы нервничаете или, например, злитесь, ей не всегда нужны от вас дополнительные сигналы. Многие собаки, особенно те, которые долгое время живут с хозяином, могут считывать сигналы человеческого тела: изменения в голосе, позы, жесты. " +
                        "Будьте спокойным, предсказуемым и любящим хозяином, который уделяет достаточно времени своему питомцу.\n" +
                        "             \n" +
                        "2.Устанавливайте правила\n" +
                        "Собака будет спокойна и предсказуема, если будет знать правила, по которым она с вами живет. " +
                        "Она всегда будет знать, как вести себя в той или иной ситуации, ей не нужно переживать за вашу реакцию. " +
                        "Для вас главное правило – это последовательность и системность. Вводите правила постепенно и раз и навсегда: если собаке можно спать на диване, ей можно там спать всегда, а не только, когда у вас хорошее настроение. " +
                        "Также не забывайте хвалить и поощрять любимца за правильное поведение, это всегда отлично сказывается на воспитании питомца.\n" +
                        "             \n" +
                        "3.Обучайте командам\n" +
                        "Дрессировка собаки – это не чья-то прихоть, а необходимость. " +
                        "С питомцем, который знает команды, проще жить. Важно дать собаке базовые навыки: питомец должен знать свою кличку, запрещающую команду, уметь ходить на поводке, носить намордник, подходить по команде и останавливаться по команде. " +
                        "Вам стоит обучить собаку основным командам, таким, как «Ко мне!», «Дай!», «Фу!», «Место!», «Рядом!», «Нельзя!», «Сидеть!».\n"+
                        "             \n" +
                        "4.Научите собаку оставаться в одиночестве\n" +
                        "Вы не всегда можете находиться рядом со своей собакой, поэтому важно, чтобы она могла оставаться в одиночестве, не испытывая стресс." +
                        "Безусловно, в этом вопросе важную роль играет ранняя социализация щенка. " +
                        "Кроме этого, нужно постепенно приучать собаку к одиночеству: сначала оставляйте ее ненадолго одной в комнате, постепенно увеличивая время и каждый раз поощряя за правильное поведение. " +
                        "Затем на короткие промежутки времени оставляйте ее дома в одиночестве. Чтобы она не заскучала, оставляйте ей умные игрушки, которые могут увлечь ее самостоятельной игрой. Постепенно собака научится спокойно ждать хозяина дома в одиночестве, не испытывая стресса.\n"+
                        "             \n" +
                        "5.Выбирайте то, что подходит вашей собаке\n" +
                        "Всегда ищите индивидуальный подход к питомцу. " +
                        "Не все навыки одинаково полезны для разных собак. Учитывайте потребности и породные особенности вашего питомца, когда выбираете для него то или иное занятие. " +
                        "Если вы сможете подобрать активности, которые будут нравиться вашей собаке, вы увидите отличные результаты. Это касается не только досуга, но и в целом жизни с питомцем: не заставляйте питомца делать то, что ему не нравится, мягко обучайте командам, обращайте внимание на то, что лучше всего получается у собаки и используйте эти навыки для достижения результатов.\n"+
                        "             \n" +
                        "Возврат в предыдущее меню (/back)\n" +
                        "Выключить бота (/stop)";
                SendMessage message = new SendMessage(chatId.toString(), responseText);
                telegramBot.execute(message);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/dog_trainer_advice").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return DOG_TRAINER_ADVICE;
    }
}


