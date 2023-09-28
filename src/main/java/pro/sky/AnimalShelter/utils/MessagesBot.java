package pro.sky.AnimalShelter.utils;

/**
 * Перечисление константных строк бота.
 */
public class MessagesBot {
    /**
     * Описание меню adopt для собак.
     */
    public static final String ADOPT_DOG_TEXT = "В этом меню я расскажу вам как взять животное из приюта для собак. Какую информацию вы бы хотели получить?\n" +
            "1. Правила знакомства с животным (/dating_rules)\n" +
            "2. Список необходимых документов (/documents)\n" +
            "3. Рекомендации по транспортировке животного (/transportation_recommendation)\n" +
            "4. Рекомендации по обустройству дома для щенка (/puppy_home_setup_recommendation)\n" +
            "5. Рекомендации по обустройству дома для взрослого животного (/home_setup_recommendations)\n" +
            "6. Рекомендации по обустройству дома для животного с ограниченными возможностями (/special_need_recommendation)\n" +
            "7. Советы кинолога (/dog_trainer_advice)\n" +
            "8. Рекомендации по проверенным кинологам (/verified_dog_handlers)\n" +
            "9. Причины, почему могут отказать и не дать забрать собаку из приюта (/refusal_reason)\n" +
            "10. Оставить контактные данные (/contact)\n" +
            "11. Позвать волонтера (/help)\n" +
            "12. Назад (/back)\n" +
            "13. Выключить бота (/stop)";
    /**
     * Описание меню adopt для кошек.
     */
    public static final String ADOPT_CAT_TEXT = "В этом меню я расскажу вам как взять животное из приюта для кошек. Какую информацию вы бы хотели получить?\n" +
            "1. Правила знакомства с животным (/dating_rules)\n" +
            "2. Список необходимых документов (/documents)\n" +
            "3. Рекомендации по транспортировке животного (/transportation_recommendation)\n" +
            "4. Рекомендации по обустройству дома для котёнка (/kitty_home_setup_recommendation)\n" +
            "5. Рекомендации по обустройству дома для взрослого животного (/home_setup_recommendations)\n" +
            "6. Рекомендации по обустройству дома для животного с ограниченными возможностями (/special_need_recommendation)\n" +
            "7. Оставить контактные данные (/contact)\n" +
            "8. Позвать волонтера (/help)\n" +
            "9. Назад (/back)\n" +
            "10. Выключить бота (/stop)";
    public static final String DATING_RULES_DOG_TEXT = "      В этом разделе я расскажу Вам правила знакомства с собакой. \n" +
            "             \n" +
            "  Не смотрите собаке прямо в глаза (это для них признак агрессии) - смотрите на хвост, на уши, на холку.\n " +
            "Вообще, из приюта та собака доставит вам меньше проблем, которая уже при первой встрече захочет с вами гулять, играть, гладить, пусть даже вначале немного испугается. " +
            "Если вы по незнанию допустите какую-то ошибку и собака начнет на вас лаять, а вам захочется взять именно ее. " +
            "То чтобы помириться, встаньте в ней боком и смотрите в другую сторону, не на собаку.\n" +
            "Итак, не смотреть в глаза, не класть руку прямо на спину, не трепать по шее, холке, не гладить живот (гладить в первый раз незнакомую собаку можно только по боку). " +
            "Когда будете надевать ошейник или поводок, или начнете гладить, не наклоняйтесь над собакой сверху, а присаживайтесь на корточки.\n" +
            "Потом, когда собака станет вашей и привыкнет к вам, она уже не боится и ей можно смотреть в глаза, трепать по холке, чесать пузо. Я говорю о мерах \"собачьей вежливости\" при первом знакомстве.\n" +
            "             \n" +
            "Возврат в предыдущее меню (/back\n" +
            "Выключить бота (/stop)";
    public static final String DATING_RULES_CAT_TEXT = "В этом разделе я расскажу Вам правила знакомства с кошкой. \n" +
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

    public static final String DOCUMENTS_LIST_TEXT = "В этом разделе я расскажу какие документы нужны, чтобы взять животное из приюта.\n" +
            "             \n" +
            "  Чтобы забрать животное из приюта, необходимо при себе необходимо иметь копию паспорта и справку с места жительства.\n" +
            " Приют пристраивает только обработанных от паразитов, вакцинированных и стерилизованных животных.\n" +
            "\n" +
            "После передачи животного администрация приюта просит новых владельцев раз в месяц отправлять фотоотчёты о жизни бывшего подопечного." +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";

    public static final String DOG_TRAINER_ADVICE_TEXT = "1. Будьте надёжным\n" +
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
            "Вам стоит обучить собаку основным командам, таким, как «Ко мне!», «Дай!», «Фу!», «Место!», «Рядом!», «Нельзя!», «Сидеть!».\n" +
            "             \n" +
            "4.Научите собаку оставаться в одиночестве\n" +
            "Вы не всегда можете находиться рядом со своей собакой, поэтому важно, чтобы она могла оставаться в одиночестве, не испытывая стресс." +
            "Безусловно, в этом вопросе важную роль играет ранняя социализация щенка. " +
            "Кроме этого, нужно постепенно приучать собаку к одиночеству: сначала оставляйте ее ненадолго одной в комнате, постепенно увеличивая время и каждый раз поощряя за правильное поведение. " +
            "Затем на короткие промежутки времени оставляйте ее дома в одиночестве. Чтобы она не заскучала, оставляйте ей умные игрушки, которые могут увлечь ее самостоятельной игрой. Постепенно собака научится спокойно ждать хозяина дома в одиночестве, не испытывая стресса.\n" +
            "             \n" +
            "5.Выбирайте то, что подходит вашей собаке\n" +
            "Всегда ищите индивидуальный подход к питомцу. " +
            "Не все навыки одинаково полезны для разных собак. Учитывайте потребности и породные особенности вашего питомца, когда выбираете для него то или иное занятие. " +
            "Если вы сможете подобрать активности, которые будут нравиться вашей собаке, вы увидите отличные результаты. Это касается не только досуга, но и в целом жизни с питомцем: не заставляйте питомца делать то, что ему не нравится, мягко обучайте командам, обращайте внимание на то, что лучше всего получается у собаки и используйте эти навыки для достижения результатов.\n" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    public static final String HOME_SETUP_RECOMMENDATION_DOG_TEXT = " 1. Позвольте собаке проявить инициативу\n" +
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

    public static final String HOME_SETUP_RECOMMENDATION_CAT_TEXT = "1.Сделайте комнату уединенной. Вашей кошке будет гораздо спокойнее в комнате, если ей не придется нервничать из-за постоянных посетителей. " +
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

    public static final String KITTY_HOME_SETUP_RECOMMENDATION_TEXT = "    6 советов по обустройству дома для котенка:\n" +
            "1.Уберите мелкие предметы.\n" +
            "2.Уделите максимум внимания в первые дни\n" +
            "3.Подготовьте котенку место для сна\n" +
            "4.Подготовьте лоток и место для приема пищи\n" +
            "5.Приготовьте игрушки для котенка\n" +
            "6.Будьте готовы к уходу за когтями\n" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    public static final String PUPPY_HOME_SETUP_RECOMMENDATION_TEXT = "    Щенок в доме — с чего начинать первые дни\n" +
            "Просьба ознакомится по ссылке ниже:\n" +
            "https://glorypets.ru/sobaki/uhod-za-sobakami/shhenok-v-dome" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    public static final String REFUSAL_REASON_TEXT = "    5 причин отказа забрать питомца из приюта:\n" +
            "1.Большое количество животных дома.\n" +
            "2.Нестабильные отношения в семье\n" +
            "3.Наличие маленьких детей\n" +
            "4.Съемное жилье\n" +
            "5.Животное в подарок или для работы\n" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";

    public static final String SPECIAL_NEEDS_RECOMMENDATION_DOG_TEXT = "Создание безопасного и уютного дома для собаки-инвалида\n" +
            "\n" +
            "Собаки-инвалида нуждаются в особом уходе и внимании, особенно когда дело касается их дома. Вот несколько советов, как создать безопасный и уютный дом для вашей собаки-спинальника.\n" +
            "\n" +
            "Используйте мягкие поверхности. Собаки-инвалида часто испытывают боли в области спины, поэтому им нужны мягкие и удобные поверхности, на которых они могут отдыхать. Избегайте твердых и гладких поверхностей, таких как кафель или ламинат, в пользу ковров, ковровых дорожек и мягких покрытий.\n" +
            "\n" +
            "Обеспечьте безопасность. Избегайте использования материалов, которые могут быть опасными для вашей собаки-инвалида. Например, избегайте использования лакокрасочных материалов, которые могут содержать токсичные вещества. Также обратите внимание на острые углы и края мебели и других предметов, которые могут причинить травмы.\n" +
            "\n" +
            "Предоставьте лестницы и полки. Собаки-инвалида могут иметь трудности с перемещением по дому, поэтому предоставление им лестниц и полок поможет им легче перемещаться. Полки также могут быть использованы в качестве места отдыха, где ваша кошка-инвалид может чувствовать себя безопасно.\n" +
            "\n" +
            "Предоставьте место для еды и воды. Важно, чтобы ваша кошка-инвалид имела легкий доступ к месту для еды и воды. При этом следует убедиться, что миски находятся на уровне, который не потребует от собаки-инвалида слишком большого усилия.\n" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    public static final String SPECIAL_NEEDS_RECOMMENDATION_CAT_TEXT = "Создание безопасного и уютного дома для кошки-инвалида\n" +
            "\n" +
            "Кошки-инвалида нуждаются в особом уходе и внимании, особенно когда дело касается их дома. Вот несколько советов, как создать безопасный и уютный дом для вашей кошки-спинальника.\n" +
            "\n" +
            "Используйте мягкие поверхности. Кошки-инвалида часто испытывают боли в области спины, поэтому им нужны мягкие и удобные поверхности, на которых они могут отдыхать. Избегайте твердых и гладких поверхностей, таких как кафель или ламинат, в пользу ковров, ковровых дорожек и мягких покрытий.\n" +
            "\n" +
            "Обеспечьте безопасность. Избегайте использования материалов, которые могут быть опасными для вашей кошки-инвалида. Например, избегайте использования лакокрасочных материалов, которые могут содержать токсичные вещества. Также обратите внимание на острые углы и края мебели и других предметов, которые могут причинить травмы.\n" +
            "\n" +
            "Предоставьте лестницы и полки. Кошки-инвалида могут иметь трудности с перемещением по дому, поэтому предоставление им лестниц и полок поможет им легче перемещаться. Полки также могут быть использованы в качестве места отдыха, где ваша кошка-инвалид может чувствовать себя безопасно.\n" +
            "\n" +
            "Предоставьте место для еды и воды. Важно, чтобы ваша кошка-инвалид имела легкий доступ к месту для еды и воды. При этом следует убедиться, что миски находятся на уровне, который не потребует от кошки-инвалида слишком большого усилия.\n" +
            "\n" +
            "Убедитесь в наличии места для лотка. Кошки-инвалиды могут иметь трудности с использованием лотка, поэтому следует предоставить им легкий доступ к месту для схода в туалет.\n" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";

    public static final String TRANSPORTATION_RECOMMENDATION_DOG_TEXT = "Желательно для перевозки собаки иметь специальную клетку-контейнер, либо переноску, как для кошек,допустимо для мелких пород собак.\n" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";

    public static final String TRANSPORTATION_RECOMMENDATION_CAT_TEXT = "Для перевозки кошки обязательно нужна переноска для кошек. \n" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";

    public static final String VERIFIED_DOG_TEXT = "Кинологи Алматы\n" +
            "1. Анастасия Евгеньевна Коровникова\n" +
            "Полная информация по ссылке на Профи:" +
            "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileTabName=reviews&profileId=AnastasiyaKY3" +
            "             \n" +
            "2. Коркин Андрей\n" +
            "Полная информация по ссылке на Профи:" +
            "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileTabName=reviews&profileId=KorkinAV5&fromSection=page_listing" +
            "             \n" +
            "3. Светлана Сергеевна Чернобаева\n" +
            "Полная информация по ссылке на Профи:" +
            "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileId=ChernobayevaSS&profileTabName=reviews&fromSection=page_listing" +
            "             \n" +
            "4. Эльвира Агзамовна Альмухамедова\n" +
            "Полная информация по ссылке на Профи:" +
            "https://alm.profi.kz/veterinar/kinologia/?seamless=1&tabName=PROFILES&profileId=AlmukhamedovaEA&profileTabName=reviews&fromSection=page_listing" +
            "             \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    public static final String ADMIN_COMMAND_TEXT = "Добро пожаловать в меню волонтера!\uD83D\uDE3C\n\n" +

            "1. Найти юзера по номеру телефона \n(/find_user_by_phone)\n" +
            "2. Найти питомца по кличке \n(/find_animal_by_name)\n" +
            "3. Перевести юзера в усыновителя кошки или собаки \n(/make_adopter)\n" +
            "4. Получить список усыновителей \n(/all_adopters)\n" +
            "5. Проверить отчет по усыновителю \n(/check_report)\n" +
            "6. Получить список усыновителей, у которых испытательный срок подошел к концу\n(/completed_probation_adopters)\n" +
            "7. Получить список усыновителей, которые не высылали отчет более 2 дней\n(/completed_probation_adopters)\n" +
            "8. Выключить бота \n(/stop)";

    public static final String ADMIN_COMMAND_RETURN_TEXT = "Вы вернулись в меню волонтера\uD83D\uDE3C\n\n" +
            "1. Найти юзера по номеру телефона \n(/find_user_by_phone)\n" +
            "2. Найти питомца по кличке \n(/find_animal_by_name)\n" +
            "3. Перевести юзера в усыновителя кошки или собаки \n(/make_adopter)\n" +
            "4. Получить список усыновителей \n(/all_adopters)\n" +
            "5. Проверить отчет по усыновителю \n(/check_report)\n" +
            "6. Получить список усыновителей, у которых испытательный срок подошел к концу\n(/completed_probation_adopters)\n" +
            "7. Получить список усыновителей, которые не высылали отчет более 2 дней\n(/completed_probation_adopters)\n" +
            "8. Выключить бота \n(/stop)";


    public static final String HELP_COMMAND_TEXT = "Для связи с волонтером пройдите по ссылке: \n" +
            "\n" +
            "По четным дням месяца Вам поможет Дмитрий, ссылка на Телеграмм - https://t.me/DmitriyVolkov \n" +
            "\n" +
            "По нечетным дням месяца Вам поможет Елена, ссылка на Телеграмм - https://t.me/koroliana \n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    /**
     * Описание кошачьего приюта.
     */
    public static final String CAT_SHELTER_DESCRIPTION_TEXT =
            "Наш «кошачий рай»\uD83D\uDC31 занимает площадь 500 квадратных метров и насчитывает более 150 питомцев, " +
                    "2 постоянных сотрудника и 10 волонтеров.\n\n" +
                    "Сотрудники и волонтеры приюта занимаются следующими вопросами:\n" +
                    "• Консультируют и дают советы по различным вопросам, касающихся кошек и котят " +
                    "(поведенческие проблемы, поиск новых домов для кошек и т. д.)\n" +
                    "• Находят новые дома для жителей приюта, все кошки из приюта могут быть усыновлены.\n" +
                    "• Следят за состоянием усыновленных кошек в течение 30 дней и окончательно " +
                    "передают питомца только в надежные руки." +
                    "• Заботятся обо всех кошках, находящихся в приюте. Если кошку никто не усыновил, " +
                    "она проживает там до самой смерти от старости.\n" +
                    "• Вновь прибывшей кошке оказывается вся необходимая ветеринарная помощь. " +
                    "Затем она помещается в коттедж, где она проживает какое-то время, чтобы ознакомиться с окружением. " +
                    "Как только кошка привыкнет, она выпускается из коттеджа и может бродить по территории по своему желанию.\n\n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";

    /**
     * Описание собачьего приюта.
     */
    public static final String DOG_SHELTER_DESCRIPTION_TEXT =
            "Наша «обитель для собак»\uD83D\uDC36 находится на площади в 600 квадратных метров и населяется более чем 100 пушистыми друзьями. " +
                    "У нас работает 3 постоянных сотрудника и 12 преданных волонтеров.\n\n" +
                    "Сотрудники и волонтеры отвечают за следующие вопросы:\n" +
                    "• Предоставляют консультации и делятся советами по всем аспектам ухода за собаками и щенками, " +
                    "вопросам здоровья, их воспитанию, а также помогают найти новые дома для наших подопечных.\n" +
                    "• Следят за состоянием и благополучием усыновленных собак в течение 30 дней и убеждаются, " +
                    "что питомцы попадают в надежные и заботливые руки.\n" +
                    "• Заботятся и ухаживают за всеми собаками, находящимися в нашем приюте. " +
                    "Если собака не находит семью, мы берем на себя ответственность " +
                    "за ее благополучие и уход до конца ее жизни.\n" +
                    "• Каждая прибывшая собака получает необходимую ветеринарную помощь. " +
                    "Затем ее временно размещают в отдельном вольере, где она может привыкнуть к новому окружению. " +
                    "Как только собака адаптируется и социализируется, она получает свободу гулять в вольере на несколько собак.\n\n" +
                    "Возврат в предыдущее меню (/back)\n" +
                    "Выключить бота (/stop)";

    public static final String PASS_COMMAND_DOG_TEXT = "Пункт охраны приюта для собак находится по адресу:\n" +
            "ул. Аккорган, 5/1, микрорайон Коктал, Астана\n" +
            "телефон для связи: +7(999)4567890\n\n" +
            "Для получения пропуска при себе иметь: \n" +
            "-Удостоверение личности \n" +
            "-Документы на автомобиль \n\n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    public static final String PASS_COMMAND_CAT_TEXT = "Пункт охраны приюта для кошек находится по адресу:\n" +
            "ул. Кенесары, 52, Астана\n" +
            "телефон для связи: +7(888)0987654\n\n" +
            "Для получения пропуска при себе иметь: \n" +
            "-Удостоверение личности \n" +
            "-Документы на автомобиль \n\n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";

    public static final String SAFETY_COMMAND_TEST = "Находясь на территории приюта, пожалуйста, соблюдайте наши правила и технику безопасности!\n" +
            "\n" +
            "                ЗАПРЕЩАЕТСЯ:\n" +
            "\n" +
            "- Самостоятельно открывать выгулы и вольеры без разрешения работника приюта.\n" +
            "\n" +
            "- Кормить животных. Этим Вы можете спровоцировать драку. \n" +
            "\n" +
            "- Оставлять после себя мусор на территории приюта и прилегающей территории.\n" +
            "\n" +
            "- Подходить близко к вольерам и гладить собак через сетку на выгулах. Животные могут быть агрессивны!\n" +
            "\n" +
            "- Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных.\n" +
            "\n" +
            "- Посещение приюта для детей дошкольного и младшего школьного возраста без сопровождения взрослых.\n" +
            "\n" +
            "- Нахождение на территории приюта детей среднего и старшего школьного возраста без  сопровождения взрослых или письменной справки-разрешения от родителей или законных представителей.\n" +
            "\n" +
            "- Посещение приюта в состоянии алкогольного, наркотического опьянения.\n" +
            " Возврат в предыдущее меню (/back)\n" +
            " Выключить бота (/stop)";

    public static final String SCHEDULE_COMMAND_TEXT = "Мы находимя по адресу:\n" +
            "ул. Аккорган, 5В, микрорайон Коктал, Астана\n" +
            "телефон для связи: +7(123)4567890\n" +
            "Расписание работы приюта: \n" +
            "Понедельник 09:00–16:00 \n" +
            "Вторник 09:00–16:00 \n" +
            "Среда 09:00–16:00 \n" +
            "Четверг 09:00–16:00 \n" +
            "Пятница 09:00–16:00 \n" +
            "Суббота 09:00–16:00 \n" +
            "Воскресенье 09:00–16:00\n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";
    public static final String WAY_BACK_TEXT = "\n" +
            "Возврат в предыдущее меню (/back) \n" +
            "Выключить бота (/stop)";
    public static final String USER_FOUND_BY_PHONE_TEXT = "Вот что я нашел по введенному номеру телефона:\n";

    public static final String ANIMAL_FOUND_BY_NAME_TEXT = "Вот что я нашел по введенной кличке: \n";
    public static final String USER_NOT_FOUND_BY_PHONE_TEXT = "К сожалению, я никого не нашел по введенному номеру. Попробуйте снова.";
    public static final String USER_NOT_FOUND_BY_ID_TEXT = "К сожалению, я не нашел юзера по введенному id. Попробуйте снова.";

    public static final String CAT_NOT_FOUND_BY_NAME_TEXT = "К сожалению, я не нашел кошек с кличкой ";
    public static final String CAT_NOT_FOUND_BY_ID_TEXT = "К сожалению, я не нашел кошку по введенному id. Попробуйте снова.";
    public static final String DOG_NOT_FOUND_TEXT = "К сожалению, я не нашел собак с кличкой ";
    public static final String DOG_NOT_FOUND_BY_ID_TEXT = "К сожалению, я не нашел собаку по введенному id. Попробуйте снова.";
    public static final String WAITING_PHONE_NUMBER_TEXT = "Пожалуйста, введите номер телефона.";

    public static final String WAITING_ANIMAL_NAME_TEXT = "Пожалуйста, введите кличку питомца в формате:\n" +
            "Собака(или Кошка),Кличка";

    public static final String DATA_IS_NOT_CORRECT_TEXT = "Проверьте, пожалуйста, " +
            "соответствует ли введенная информация рекомендуемому формату.";

    public static final String WAITING_USER_PET_DATA_TEXT = "Для подтверждения усыновления, " +
            "пожалуйста, введите id юзера и id питомца в формате:\n" +
            "user_id, Собака(или Кошка), pet_id";

    public static final String ADOPTION_SUCCESS_TEXT = "Юзер успешно переведен в усыновители";

    public static final String CONTACT_TEXT="Пожалуйста, введите контактные данные в формате" +
            ": Имя, Телефон, Email (даже если хотите поменять не все данные)\n" +
            "Возврат в предыдущее меню (/back)\n" +
            "Выключить бота (/stop)";

}




