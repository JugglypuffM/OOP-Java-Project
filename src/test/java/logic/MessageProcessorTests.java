package logic;

import database.main.Database;
import database.main.DatabaseMock;
import database.main.DatabaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageProcessorTests {
    /**
     * Mock database class.
     * Has the same functionality as the main {@link DatabaseService} class.
     */
    private Database database;
    /**
     * Instance of {@link MessageProcessor}.
     * Used to process all test messages.
     */
    private MessageProcessor processor;
    String platformId = "0";
    /**
     * Utility method to fill all profile data at once
     * @param platformId user id
     * @param processor instance of {@link MessageProcessor}
     */
    public void fillProfile(String platformId, MessageProcessor processor){
        processor.processMessage(platformId, "/start");
        processor.processMessage(platformId, "datastas|TELEGRAM");
        processor.processMessage(platformId, "Стас");
        processor.processMessage(platformId, "Стас");
        processor.processMessage(platformId, "19");
        processor.processMessage(platformId, "Парень");
        processor.processMessage(platformId, "Екатеринбург");
        processor.processMessage(platformId, "просто круд");
        processor.processMessage(platformId, "17");
        processor.processMessage(platformId, "23");
        processor.processMessage(platformId, "Девушка");
        processor.processMessage(platformId, "Екатеринбург");
        processor.processPhoto(platformId, "Екатеринбург");
        processor.processMessage(platformId, "да");
    }

    /**
     * Initialization of {@link MessageProcessor} and basic user with id 0
     */
    @BeforeEach
    public void initialize(){
        this.database = new DatabaseMock();
        this.processor = new MessageProcessor(database, null);
        processor.processMessage(platformId, "/start");
        processor.processMessage(platformId, "datastas|TELEGRAM");
        processor.processMessage(platformId, "Стас");
        processor.processMessage(platformId, "Стас");
        processor.processMessage(platformId, "19");
        processor.processMessage(platformId, "Парень");
        processor.processMessage(platformId, "Екатеринбург");
        processor.processMessage(platformId, "просто круд");
        processor.processMessage(platformId, "17");
        processor.processMessage(platformId, "23");
        processor.processMessage(platformId, "Девушка");
        processor.processMessage(platformId, "Екатеринбург");
        processor.processPhoto(platformId, "Екатеринбург");
    }
    /**
     * Test of profile filling procedure.
     * Tests if states switch correctly and data stores appropriately
     */
    @Test
    public void profileFillTest(){
        processor.processMessage(platformId, "да");
        String[] reply = processor.processMessage(platformId, "/myProfile");
        Assertions.assertEquals("""
                Имя: Стас
                Возраст: 19
                Пол: парень
                Город: Екатеринбург
                Информация о себе: просто круд
                Диапазон возраста собеседника: 17 - 23
                Пол собеседника: девушка
                Город собеседника: Екатеринбург""", reply[0]);
        Assertions.assertEquals("Екатеринбург", reply[12]);
    }

    /**
     * Test of transition from fill to edit
     */
    @Test
    public void editAfterFillTest(){
        String[] reply = processor.processMessage(platformId, "нет");
        Assertions.assertEquals("Что хочешь изменить?", reply[0]);
        Assertions.assertEquals("""
                Вот список полей доступных для изменения:\s
                1 - Имя(Стас)
                2 - Возраст(19)
                3 - Пол(парень)
                4 - Город(Екатеринбург)
                5 - Информация о себе(просто круд)
                6 - Нижний порог возраста собеседника(17)
                7 - Верхний порог возраста собеседника(23)
                8 - Пол собеседника(девушка)
                9 - Город собеседника(Екатеринбург)
                10 - Фото""", reply[1]);
        processor.processMessage(platformId, "2");
        processor.processMessage(platformId, "18");
    }

    /**
     * Test of profile editing procedure.
     * Tests state switching and appropriate data storage
     */
    @Test
    public void profileEditTest(){
        processor.processMessage(platformId, "да");
        processor.processMessage(platformId, "/editProfile");
        Assertions.assertEquals("Напиши либо цифру соответствующую полю, либо название поля.", processor.processMessage(platformId, "svfand")[0]);
        Assertions.assertEquals("Напиши цифрами новый возраст.", processor.processMessage(platformId, "2")[0]);
        Assertions.assertEquals("Изменение внесено.", processor.processMessage(platformId, "18")[0]);
        Assertions.assertEquals("""
                Имя: Стас
                Возраст: 18
                Пол: парень
                Город: Екатеринбург
                Информация о себе: просто круд
                Диапазон возраста собеседника: 17 - 23
                Пол собеседника: девушка
                Город собеседника: Екатеринбург""", processor.processMessage(platformId, "/myProfile")[0]);
    }

    /**
     * Test for photo editing case.
     * Tests if processPhoto method works correctly
     */
    @Test
    public void photoEditTest(){
        processor.processMessage(platformId, "да");
        processor.processMessage(platformId, "/editProfile");
        processor.processMessage(platformId, "10");
        Assertions.assertEquals("Пожалуйста, отправь картинку.", processor.processMessage(platformId, "dfgsdfgsdfg")[0]);
        Assertions.assertEquals("Изменение внесено.", processor.processPhoto(platformId, "dfgsdfgsdfg")[0]);
        Assertions.assertEquals("dfgsdfgsdfg", processor.processMessage(platformId, "/myProfile")[12]);
    }

    /**
     * Test for profile change procedure.
     * Edit added just in case it may work wrong.
     */
    @Test
    public void changeProfileTest(){
        processor.processMessage(platformId, "да");
        processor.processMessage(platformId, "/editProfile");
        processor.processMessage(platformId, "2");
        processor.processMessage(platformId, "18");
        processor.processMessage(platformId, "/changeProfile");
        processor.processMessage(platformId, "Стас");
        processor.processMessage(platformId, "сатС");
        processor.processMessage(platformId, "91");
        processor.processMessage(platformId, "Девушка");
        processor.processMessage(platformId, "грубниретакЕ");
        processor.processMessage(platformId, "дурк отсорп");
        processor.processMessage(platformId, "71");
        processor.processMessage(platformId, "82");
        processor.processMessage(platformId, "Парень");
        processor.processMessage(platformId, "грубниретакЕ");
        processor.processPhoto(platformId, "грубниретакЕ");
        processor.processMessage(platformId, "да");
        Assertions.assertEquals("""
                Имя: сатС
                Возраст: 91
                Пол: девушка
                Город: грубниретакЕ
                Информация о себе: дурк отсорп
                Диапазон возраста собеседника: 71 - 82
                Пол собеседника: парень
                Город собеседника: грубниретакЕ""", processor.processMessage(platformId, "/myProfile")[0]);
        Assertions.assertEquals("грубниретакЕ", processor.processMessage(platformId, "/myProfile")[12]);
    }

    /**
     * Test of matching procedure.
     */
    @Test
    public void matchingFailTest(){
        processor.processMessage(platformId, "да");
        fillProfile("1", processor);
        Assertions.assertEquals("Не нашлось никого, кто соответствует твоей уникальности ;(", processor.processMessage("0", "/match")[0]);
    }

    /**
     * Test case for mutual like of two users
     */
    @Test
    public void matchingLikeLikeTest(){
        processor.processMessage(platformId, "да");
        fillProfile("1", processor);
        processor.processMessage("0", "/editProfile");
        processor.processMessage("0", "8");
        processor.processMessage("0", "парень");
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "8");
        processor.processMessage("1", "парень");
        Assertions.assertEquals(processor.processMessage("1", "/myProfile")[0], processor.processMessage("0", "/match")[0]);
        Assertions.assertEquals("Введи да или нет.", processor.processMessage("0", "дварыера")[0]);
        Assertions.assertEquals("Я уведомил этого пользователя, что он тебе приглянулся :)\nЕсли он ответит взаимностью, то вы сможете перейти к общению!", processor.processMessage("0", "да")[0]);
        Assertions.assertEquals("Введи да или нет.", processor.processMessage("1", "дварыера")[0]);
        String[] reply = processor.processMessage("1", "да");
        Assertions.assertEquals("Ура! Теперь вы можете перейти к общению.", reply[0]);
        Assertions.assertEquals("Вот ссылка на профиль собеседника - @stas", reply[1]);
    }

    /**
     * Test case for rejection by second user
     */
    @Test
    public void matchingLikeDislikeTest(){
        processor.processMessage(platformId, "да");
        fillProfile("1", processor);
        processor.processMessage("0", "/editProfile");
        processor.processMessage("0", "8");
        processor.processMessage("0", "парень");
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "8");
        processor.processMessage("1", "парень");
        Assertions.assertEquals(processor.processMessage("1", "/myProfile")[0], processor.processMessage("0", "/match")[0]);
        Assertions.assertEquals("Я уведомил этого пользователя, что он тебе приглянулся :)\nЕсли он ответит взаимностью, то вы сможете перейти к общению!", processor.processMessage("0", "да")[0]);
        String[] reply = processor.processMessage("1", "нет");
        Assertions.assertEquals("Хорошо, больше ты этого человека не увидишь. Если только не решишь удалить его из списка не понравившихся профилей.", reply[0]);
    }

    /**
     * Test case for rejection by first user
     */
    @Test
    public void matchingDislikeTest(){
        processor.processMessage(platformId, "да");
        fillProfile("1", processor);
        processor.processMessage("0", "/editProfile");
        processor.processMessage("0", "8");
        processor.processMessage("0", "парень");
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "8");
        processor.processMessage("1", "парень");
        Assertions.assertEquals(processor.processMessage("1", "/myProfile")[0], processor.processMessage("0", "/match")[0]);
        Assertions.assertEquals("Очень жаль, в следующий раз постараюсь лучше :(", processor.processMessage("0", "нет")[0]);
    }
    @Test
    public void TwoUsersMatchTest(){
        processor.processMessage(platformId, "да");
        fillProfile("1", processor);
        processor.processMessage("0", "/editProfile");
        processor.processMessage("0", "8");
        processor.processMessage("0", "парень");
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "8");
        processor.processMessage("1", "парень");
        processor.processMessage("0", "/match");
        Assertions.assertEquals("Не нашлось никого, кто соответствует твоей уникальности ;(", processor.processMessage("1", "/match")[0]);
        Assertions.assertEquals("Я уведомил этого пользователя, что он тебе приглянулся :)\nЕсли он ответит взаимностью, то вы сможете перейти к общению!", processor.processMessage("0", "да")[0]);
        Assertions.assertEquals("Ура! Теперь вы можете перейти к общению.", processor.processMessage("1", "да")[0]);
    }
    /**
     * Test of /myMatches command
     */
    @Test
    public void myMatchesTest(){
        processor.processMessage(platformId, "да");
        fillProfile("1", processor);
        processor.processMessage("0", "/editProfile");
        processor.processMessage("0", "8");
        processor.processMessage("0", "парень");
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "8");
        processor.processMessage("1", "парень");
        Assertions.assertEquals("Просмотренных профилей пока что нет ;(\nПопробуй ввести /match", processor.processMessage("0", "/myMatches")[0]);
        processor.processMessage("0", "/match");
        processor.processMessage("0", "да");
        processor.processMessage("1", "да");
        Assertions.assertEquals("Какой список профилей вывести(лайки/дизлайки)?", processor.processMessage("0", "/myMatches")[0]);
        Assertions.assertEquals("Такого списка нет, введи либо \"лайки\", либо \"дизлайки\". Или \"выйти\", если передумал.", processor.processMessage("0", "ыважлпдлавп")[0]);
        Assertions.assertEquals("Этот список пуст :(", processor.processMessage("0", "дизлайки")[0]);
        processor.processMessage("0", "/myMatches");
        String[] reply = processor.processMessage("0", "лайки");
        Assertions.assertEquals("Профили на странице 1:", reply[0]);
        Assertions.assertEquals("Профиль 1:\n" + processor.processMessage("1", "/myProfile")[0] +
                "\nВот ссылка на профиль этого пользователя - @stas", reply[2]);
        Assertions.assertEquals("Больше страниц нет.", processor.processMessage("0", "далее")[0]);
        Assertions.assertEquals("Это первая страница.", processor.processMessage("0", "назад")[0]);
        Assertions.assertEquals("Введи \"далее\" или \"назад\" для смены страниц, \"выйти\" для выхода или номер профиля, который хочешь удалить.", processor.processMessage("0", "выфжадвыьфа")[0]);
        Assertions.assertEquals("Нет профиля с таким номером.", processor.processMessage("0", "2")[0]);
        Assertions.assertEquals("Профиль успешно удален из списка.", processor.processMessage("0", "1")[0]);
        Assertions.assertEquals("Просмотренных профилей пока что нет ;(\nПопробуй ввести /match", processor.processMessage("0", "/myMatches")[0]);
    }

    /**
     * User deletion command test
     */
    @Test
    public void deleteTest(){
        processor.processMessage(platformId, "да");
        Assertions.assertEquals("Ты уверен, что хочешь этого? Все твои данные удалятся, в том числе и список понравившихся тебе людей!", processor.processMessage(platformId, "/deleteProfile")[0]);
        Assertions.assertEquals("Введено неверное значение, процедура удаления прекращена.", processor.processMessage(platformId, "stas1")[0]);
        processor.processMessage(platformId, "/deleteProfile");
        Assertions.assertEquals("Профиль успешно удален.", processor.processMessage(platformId, "stas")[0]);
        Assertions.assertEquals("требуются данные", processor.processMessage(platformId, "/start")[0]);
    }
}
