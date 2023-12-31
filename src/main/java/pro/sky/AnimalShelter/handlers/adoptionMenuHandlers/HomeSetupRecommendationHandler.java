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
import static pro.sky.AnimalShelter.enums.BotCommand.CAT;

@Service
@RequiredArgsConstructor
public class HomeSetupRecommendationHandler implements CommandHandler {
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
     * Обрабатывает команду "/home_setup_recommendations" в зависимости от текущего состояния чата.
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
                String responseText = " 1. Позвольте собаке проявить инициативу\n" +
                        "Когда собака окажется у вас в доме, не будьте навязчивым и не ходите за ней по пятам. " +
                        "Ей будет интересно обойти дом самостоятельно, привыкнуть к новым запахам, познакомиться с окружающей обстановкой. Не всегда выбранное вами место может понравиться собаке. " +
                        "Иногда хозяева самостоятельно выбирают место для питомца, а он принципиально не хочет отдыхать там. Вы можете последить за собакой, чтобы понять, какое место ей приглянулось: она может самостоятельно его выбрать, например, пристроившись там поспать.\n" +
                        "             \n" +
                        "2.Подумайте о безопасности\n" +
                        "Проведите ревизию своего дома: уберите все провода наверх, спрячьте под плинтус или защитите с помощью муфты. " +
                        "Осмотрите выбранное для собаки место, убедитесь, что рядом не располагаются бьющиеся и острые предметы. Возле питомца также не должны храниться лекарства и бытовая химия, убирайте все, что может по вашему мнению навредить собаке.\n" +
                        "             \n" +
                        "3.Как обустроить собаке место\n" +
                        "Выберите уютное место для своей собаки, где она будет отдыхать и спать. " +
                        "Не размещайте питомца на проходе, также убедитесь, что он не будет лежать на сквозняке или под палящим солнцем в жаркие дни. " +
                        "У собаки должно быть свое личное пространство, в котором ей будет комфортно и уютно. " +
                        "Для этого можете приобрести мягкую лежанку и клетку (если планируете приучать питомца к ней).\n" +
                        "Лежанка должна подходить по размерам собаке и быть выполнена из качественного материала. " +
                        "Чтобы понять, какой размер подойдет, измерьте питомца от носа до кончика хвоста и прибавьте к этому значению около 15 сантиметров. " +
                        "Если вы берете щенка, учитывайте, что лежанку придется менять по мере роста собаки.\n" +
                        "Лучше не размещайте собаку в коридоре. Ей будет сложно уединиться, а звуки за дверью будут всячески беспокоить питомца. " +
                        "Со своего места собака должна видеть все то, что происходит вокруг нее: где находится хозяин, кто зашел сейчас домой. " +
                        "Лучше выбирать место, открытое с двух сторон, чтобы питомец мог беспрепятственно уйти, если перед ним, например, появится гость, с которым собака не хочет взаимодействовать.\n" +
                        "Собака быстро адаптируется в новом месте, если вы будете уделять ей необходимое количество внимания, а также позаботитесь о том, чтобы в новом доме она чувствовала себя комфортно и безопасно.\n" +
                        "             \n" +
                        "Возврат в предыдущее меню (/back)\n" +
                        "Выключить бота (/stop)";
                SendMessage message = new SendMessage(chatId.toString(), responseText);
                telegramBot.execute(message);
            }
            if (previousState == CAT) {
                String responseText = "1.Сделайте комнату уединенной. Вашей кошке будет гораздо спокойнее в комнате, если ей не придется нервничать из-за постоянных посетителей. " +
                        "Выбирая помещение, помните о том, что в комнате должно быть спокойно и там не должны постоянно находиться люди и другие животные.\n" +
                        "Если у вас есть собака, ваша кошка, скорее всего, будет прятаться в комнате от нее, поэтому собака не должна иметь возможность заходить в комнату кошки. Установите небольшую дверцу для кошки, куда не смогла бы пролезть собака. Можно также установить специальное ограждение, через которое собака не сможет перепрыгнуть.\n" +
                        "Если вы не можете выделить целую комнату кошке, попробуйте устроить кошке уголок в своей комнате. Ничего не случится, если вы будете находиться рядом с кошкой время от времени. Если у вас есть кабинет, который вы редко используете, кошку можно поселить там.\n" +
                        "             \n" +
                        "2.Сделайте место безопасным. Уберите все предметы, которые могут представлять опасность для жизни и здоровья кошки. Избавьтесь от проводов и кабелей, токсичных растений и прочих предметов, которые кошка может погрызть.[2]\n" +
                        "Если вам нужно хранить в этой комнате чистящие средства и другие токсические вещества, поставьте их в шкафчик, который надежно закрывается, чтобы кошка не могла до них добраться.\n" +
                        "Уберите все предметы, которые кошка может перевернуть, либо поставьте их по-другому. Если у вас много безделушек, которые стоят на полках или на столе, спрячьте их за стеклянные дверцы.\n" +
                        "             \n" +
                        "3.Установите перегородки и устройте места, где можно прятаться. Кошки чувствуют себя в безопасности, когда сидят на высоком месте и оттуда наблюдают за происходящим. Многим кошкам нравится прятаться в укромные места.\n" +
                        "Можно купить кошачье дерево или домик, а можно сделать его самостоятельно из деревянных панелей и кусков коврового покрытия.\n" +
                        "Высокие полки тоже подойдут, если кошка сможет запрыгнуть туда. Если кошка не любит прыгать, поставьте рядом с полками маленький столик или высокий стул, чтобы кошке было проще забираться наверх.\n" +
                        "Кошки могут прятаться за или под мебелью, внутри кошачьих домиков и в картонных коробках. Кошке понравится специальная лежанка или одеяло в ее любимом месте.\n" +
                        "Предложите кошке несколько вариантов.\n" +
                        "             \n" +
                        "\n" +
                        "4.Принесите все необходимое. Кошка будет чувствовать себя спокойнее, если все, что ей нужно, будет находиться в одном месте. Оставьте в комнате еду, воду и лоток.[3]\n" +
                        "Некоторым кошкам не нравится, когда еда, вода и лоток стоят близко друг к другу. Постарайтесь поставить их как можно дальше друг от друга.\n" +
                        "По возможности поставьте несколько лотков и побольше мисок с водой. Лучше всего поставить их и в комнате кошки, и за ее пределами. Вам потребуется по одному лотку и миске с водой на кошку плюс еще по одной дополнительной штуке.\n" +
                        "Если вы большую часть времени проводите не дома, попробуйте автоматизировать комнату. Можно купить автоматическую кормушку, которая выдавала бы сухой корм в определенное время, а также фонтанчик с водой. Можно даже купить самоочищающийся лоток.\n" +
                        "Важно установить как минимум одну когтеточку — так кошка не будет царапать мебель и обои.\n" +
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
     * Возвращает команду, связанную с этим обработчиком ("/home_setup_recommendations").
     *
     * @return Команда, связанная с обработчиком.
     */

    @Override
    public BotCommand getCommand() {
        return HOME_SETUP_RECOMMENDATIONS;
    }
}

