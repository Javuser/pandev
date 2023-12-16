package com.example.pandev;

import com.example.pandev.bot.CategoryTreeBot;
import com.example.pandev.config.CategoryTreeBotConfig;
import com.example.pandev.entity.Category;
import com.example.pandev.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PandevApplicationTests {

//    private final Category category = new Category();



//    @Test
//    void addTreeTest() {
//        String command = "/addTree Samsung";
//        assertEquals("Samsung", category.getName());
//    }

    @Test
    public void testAddTreeCommand() {
        // Создаем мок для CategoryService
        CategoryService categoryServiceMock = mock(CategoryService.class);

        // Создаем объект CategoryTreeBot с мок-объектом CategoryService
        CategoryTreeBot categoryTreeBot = new CategoryTreeBot(new CategoryTreeBotConfig(), categoryServiceMock);

        // Создаем Update для теста
        Update update = new Update();
        Message message = new Message();
        message.setText("/addTree Samsung S20");
        update.setMessage(message);

        // Тестируем метод addTreeCommand
        categoryTreeBot.onUpdateReceived(update);

        // Проверяем, что в CategoryService был вызван соответствующий метод
        verify(categoryServiceMock, times(1)).addElement(eq("S20"), isNull());
    }


}
