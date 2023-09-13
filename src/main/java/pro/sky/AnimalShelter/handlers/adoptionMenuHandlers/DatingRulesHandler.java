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
public class DatingRulesHandler implements CommandHandler {

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
     * Обрабатывает команду "/dating_rules" в зависимости от текущего состояния чата.
     *
     * @param update Объект, представляющий обновление от пользователя.
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        BotCommand currentState = chatStateService.getCurrentStateByChatId(chatId);
        BotCommand previousState = chatStateService.getPreviousStateByChatId(chatId);

        if ((currentState == DOG || currentState ==ADOPT) ||(currentState == DATING_RULES && previousState == ADOPT)) {
            String menuMessage = currentState == DATING_RULES ? "               Вы уже в этом меню. \n" : "";
            String responseText = menuMessage + "      В этом меню я расскажу Вам правила знакомства с собакой. \n" +
                    "             \n" +
                    "  Не смотрите собаке прямо в глаза (это для них признак агрессии) - смотрите на хвост, на уши, на холку.\n " +
                    "Вообще, из приюта та собака доставит вам меньше проблем, которая уже при первой встрече захочет с вами гулять, играть, гладить, пусть даже вначале немного испугается. " +
                    "Если вы по незнанию допустите какую-то ошибку и собака начнет на вас лаять, а вам захочется взять именно ее. " +
                    "То чтобы помириться, встаньте в ней боком и смотрите в другую сторону, не на собаку.\n" +
                    "Итак, не смотреть в глаза, не класть руку прямо на спину, не трепать по шее, холке, не гладить живот (гладить в первый раз незнакомую собаку можно только по боку). " +
                    "Когда будете надевать ошейник или поводок, или начнете гладить, не наклоняйтесь над собакой сверху, а присаживайтесь на корточки.\n" +
                    "Потом, когда собака станет вашей и привыкнет к вам, она уже не боится и ей можно смотреть в глаза, трепать по холке, чесать пузо. Я говорю о мерах \"собачьей вежливости\" при первом знакомстве.\n" +
                    "             \n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            if (!(currentState == DATING_RULES)) {
                chatStateService.updateChatState(chatId, DATING_RULES);
            }
        } else if (currentState == CAT || currentState ==ADOPT || (currentState == DATING_RULES && previousState == ADOPT)) {
            String menuMessage = currentState == DATING_RULES ? "Вы уже в этом меню. " : "";
            String responseText = menuMessage + "В этом меню я расскажу Вам правила знакомства с кошкой. \n" +
                    "  Как бы удивительно это ни звучало, но многие допускают массу ошибок, когда начинают гладить кошек. Правильно это делать надо так:\n" +
                    " 1.Кошка должна вас видеть полностью, не подходите сзади и не наклоняйтесь сверху.\n " +
                    " 2.Дайте кошке обнюхать вас и услышать, какие на вас запахи. Можно протянуть ей какую-то свою вещь для большего контакта. Постарайтесь сделать так, чтобы от вас не пахло другими животными.\n " +
                    " 3.Опуститесь на один уровень с животным.\n " +
                    " 4.Медленно и плавно протяните руку к животному.\n " +
                    " 5.Дайте кошке выбор: если она подойдет к вам — значит вы получили разрешение погладить ее, если нет — то пока стоит повременить с ласками.\n " +
                    " 6.Если кошка дала свое согласие, начните гладить ее медленно по шее, голове и спине. Не пытайтесь погладить живот и хвост, пока она сама не разрешит вам это сделать.\n " +
                    "             \n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";
            SendMessage message = new SendMessage(chatId.toString(), responseText);
            telegramBot.execute(message);
            if (!(currentState == DATING_RULES)) {
                chatStateService.updateChatState(chatId, DATING_RULES);
            }
        } else if (currentState == STOP) {
            commonUtils.offerToStart(chatId);
        } else {
            commonUtils.sendInvalidCommandResponse(chatId);
        }
    }

    /**
     * Возвращает команду, связанную с этим обработчиком ("/dating_rules").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return BotCommand.DATING_RULES;
    }
}
