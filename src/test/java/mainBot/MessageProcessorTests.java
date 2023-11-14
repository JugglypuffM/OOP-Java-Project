package mainBot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageProcessorTests {
    MessageProcessor processor;
    String id;
    /**
     * Utility method to fill all profile data at once
     * @param id user id
     * @param processor instance of {@link MessageProcessor}
     */
    public void fillProfile(String id, MessageProcessor processor){
        processor.processMessage(id, "/start");
        processor.processMessage(id, "usernamestas");
        processor.processMessage(id, "Стас");
        processor.processMessage(id, "Стас");
        processor.processMessage(id, "19");
        processor.processMessage(id, "Парень");
        processor.processMessage(id, "Екатеринбург");
        processor.processMessage(id, "просто круд");
        processor.processMessage(id, "17");
        processor.processMessage(id, "23");
        processor.processMessage(id, "Девушка");
        processor.processMessage(id, "Екатеринбург");
        processor.processPhoto(id, "Екатеринбург");
        processor.processMessage(id, "да");
    }

    /**
     * Initialization of {@link MessageProcessor} and basic user with id 0
     */
    @BeforeEach
    public void initialize(){
        this.processor = new MessageProcessor();
        this.id = "0";
        processor.processMessage(id, "/start");
        processor.processMessage(id, "usernamestas");
        processor.processMessage(id, "Стас");
        processor.processMessage(id, "Стас");
        processor.processMessage(id, "19");
        processor.processMessage(id, "Парень");
        processor.processMessage(id, "Екатеринбург");
        processor.processMessage(id, "просто круд");
        processor.processMessage(id, "17");
        processor.processMessage(id, "23");
        processor.processMessage(id, "Девушка");
        processor.processMessage(id, "Екатеринбург");
        processor.processPhoto(id, "Екатеринбург");
    }
    /**
     * Test of profile filling procedure.
     * Tests if states switch correctly and data stores appropriately
     */
    @Test
    public void profileFillTest(){
        processor.processMessage(id, "да");
        String[] reply = processor.processMessage(id, "/myProfile");
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
        String[] reply = processor.processMessage(id, "нет");
        Assertions.assertEquals("Что хочешь изменить?", reply[0]);
        Assertions.assertEquals("Вот список полей доступных для изменения: \n" +
                "1 - Имя(Стас)\n" +
                "2 - Возраст(19)\n" +
                "3 - Пол(парень)\n" +
                "4 - Город(Екатеринбург)\n" +
                "5 - Информация о себе(просто круд)\n" +
                "6 - Нижний порог возраста собеседника(17)\n" +
                "7 - Верхний порог возраста собеседника(23)\n" +
                "8 - Пол собеседника(девушка)\n" +
                "9 - Город собеседника(Екатеринбург)\n" +
                "10 - Фото", reply[1]);
    }

    /**
     * Test of profile editing procedure.
     * Tests state switching and appropriate data storage
     */
    @Test
    public void profileEditTest(){
        processor.processMessage(id, "да");
        processor.processMessage(id, "/editProfile");
        Assertions.assertEquals("Напиши либо цифру соответствующую полю, либо название поля.", processor.processMessage(id, "svfand")[0]);
        Assertions.assertEquals("Введи новое значение.", processor.processMessage(id, "2")[0]);
        Assertions.assertEquals("Изменение внесено.", processor.processMessage(id, "18")[0]);
        Assertions.assertEquals("""
                Имя: Стас
                Возраст: 18
                Пол: парень
                Город: Екатеринбург
                Информация о себе: просто круд
                Диапазон возраста собеседника: 17 - 23
                Пол собеседника: девушка
                Город собеседника: Екатеринбург""", processor.processMessage(id, "/myProfile")[0]);
    }

    /**
     * Test for photo editing case.
     * Tests if processPhoto method works correctly
     */
    @Test
    public void photoEditTest(){
        processor.processMessage(id, "да");
        processor.processMessage(id, "/editProfile");
        processor.processMessage(id, "10");
        Assertions.assertEquals("Пожалуйста, отправь картинку.", processor.processMessage(id, "dfgsdfgsdfg")[0]);
        Assertions.assertEquals("Изменение внесено.", processor.processPhoto(id, "dfgsdfgsdfg")[0]);
        Assertions.assertEquals("dfgsdfgsdfg", processor.processMessage(id, "/myProfile")[12]);
    }

    /**
     * Test for profile change procedure.
     * Edit added just in case it may work wrong.
     */
    @Test
    public void changeProfileTest(){
        processor.processMessage(id, "да");
        processor.processMessage(id, "/editProfile");
        processor.processMessage(id, "2");
        processor.processMessage(id, "18");
        processor.processMessage(id, "/changeProfile");
        processor.processMessage(id, "Стас");
        processor.processMessage(id, "сатС");
        processor.processMessage(id, "91");
        processor.processMessage(id, "Девушка");
        processor.processMessage(id, "грубниретакЕ");
        processor.processMessage(id, "дурк отсорп");
        processor.processMessage(id, "71");
        processor.processMessage(id, "82");
        processor.processMessage(id, "Парень");
        processor.processMessage(id, "грубниретакЕ");
        processor.processPhoto(id, "грубниретакЕ");
        processor.processMessage(id, "да");
        Assertions.assertEquals("""
                Имя: сатС
                Возраст: 91
                Пол: девушка
                Город: грубниретакЕ
                Информация о себе: дурк отсорп
                Диапазон возраста собеседника: 71 - 82
                Пол собеседника: парень
                Город собеседника: грубниретакЕ""", processor.processMessage(id, "/myProfile")[0]);
        Assertions.assertEquals("грубниретакЕ", processor.processMessage(id, "/myProfile")[12]);

    }

    /**
     * Test of all profiles getting procedure.
     */
    @Test
    public void allProfilesTest(){
        processor.processMessage(id, "да");
        Assertions.assertEquals("Кроме тебя пока никого нет ;(", processor.processMessage(id, "/allProfiles")[0]);
        fillProfile("1", processor);
        Assertions.assertEquals("Какую страницу анкет вывести(Всего: 1)?", processor.processMessage(id, "/allProfiles")[0]);
        Assertions.assertEquals("Пожалуйста, введи ответ цифрами.", processor.processMessage(id, "/allProfiles")[0]);
        Assertions.assertEquals("Нет страницы с таким номером.", processor.processMessage(id, "2")[0]);
        Assertions.assertEquals("""
                Имя: Стас
                Возраст: 19
                Пол: парень
                Город: Екатеринбург
                Информация о себе: просто круд
                Диапазон возраста собеседника: 17 - 23
                Пол собеседника: девушка
                Город собеседника: Екатеринбург""", processor.processMessage(id, "1")[2]);
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "1");
        processor.processMessage("1", "Тсас");
        processor.processMessage(id, "/allProfiles");
        Assertions.assertEquals("""
                Имя: Тсас
                Возраст: 19
                Пол: парень
                Город: Екатеринбург
                Информация о себе: просто круд
                Диапазон возраста собеседника: 17 - 23
                Пол собеседника: девушка
                Город собеседника: Екатеринбург""", processor.processMessage(id, "1")[2]);
        processor.processMessage("1", "/match");
        Assertions.assertEquals("""
                Имя: Стас
                Возраст: 19
                Пол: парень
                Город: Екатеринбург
                Информация о себе: просто круд
                Диапазон возраста собеседника: 17 - 23
                Пол собеседника: девушка
                Город собеседника: Екатеринбург""", processor.processMessage("1", "1")[2]);

    }

    /**
     * Test of matching procedure.
     */
    @Test
    public void matchingTest(){
        processor.processMessage(id, "да");
        fillProfile("1", processor);
        fillProfile("2", processor);
        String[] reply;
        processor.processMessage("0", "/editProfile");
        processor.processMessage("0", "8");
        processor.processMessage("0", "парень");
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "8");
        processor.processMessage("1", "парень");
        reply = processor.processMessage("0", "/match");
        Assertions.assertEquals(processor.processMessage("1", "/myProfile")[0], reply[0]);
        reply = processor.processMessage("0", "/match");
        Assertions.assertEquals("Не нашлось никого, кто соответствует твоей уникальности ;(", reply[0]);
    }

    /**
     * Test of /myMatches command
     */
    @Test
    public void myMatchesTest(){
        processor.processMessage(id, "да");
        MessageProcessor processor = new MessageProcessor();
        fillProfile("0", processor);
        fillProfile("1", processor);
        processor.processMessage("0", "/editProfile");
        processor.processMessage("0", "8");
        processor.processMessage("0", "парень");
        processor.processMessage("1", "/editProfile");
        processor.processMessage("1", "8");
        processor.processMessage("1", "парень");
        processor.processMessage("0", "/match");
        processor.processMessage("0", "/myMatches");
        String[] reply = processor.processMessage("0", "1");
        Assertions.assertEquals(processor.processMessage("1", "/myProfile")[0], reply[2]);
    }
}
