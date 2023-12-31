package com.example.pandev.bot;

import com.example.pandev.config.CategoryTreeBotConfig;
import com.example.pandev.entity.Category;
import com.example.pandev.repository.CategoryRepository;
import com.example.pandev.service.CategoryService;
import com.example.pandev.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CategoryTreeBot extends TelegramLongPollingBot {


    final CategoryTreeBotConfig categoryTreeBotConfig;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private  DocumentService documentService;

    public CategoryTreeBot(CategoryTreeBotConfig categoryTreeBotConfig) {
        this.categoryTreeBotConfig = categoryTreeBotConfig;
        List<BotCommand> listOfCommands = new ArrayList();
        listOfCommands.add(new BotCommand("/start", "Нажмите чтоб запустить бота"));
        listOfCommands.add(new BotCommand("/viewTree", "дерево категорий"));
        listOfCommands.add(new BotCommand("/help", "помощь"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }


    private final static String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "You can execute commands from the main menu on the left or by typing commands:\n\n" +
            "Type /start to see welcome message\n\n" +
            "Type /viewTree to see category tree\n\n" +
            "Type /help to see this message again\n\n";

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            String[] messageArray = message.split("\\s+");


            switch (messageArray[0]) {
                case "/start":
                    startMessage(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "/viewTree":
                    viewCommand(chatId);
//                    sendDocument(chatId);
                    break;

                case "/download":
                    sendDocument(chatId);

                case "/deleteTree":
                    deleteTreeCommand(messageArray, chatId);
                    break;


                    //это по сути /addTree
                default:
                    addTreeCommand(message.split("\\s+"), chatId);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
            }
        }

    }

    private void startMessage(long chatId, String name) {
        String answer = "Hi, " + name;
        sendMessage(chatId, answer);
    }

    private void viewCommand(Long chatId) {
        String tree = categoryService.getCategoriesTree();
        sendMessage(chatId, "Дерево категорий:\n" + tree);
    }


    private void sendDocument(Long chatId){
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            XSSFWorkbook workbook = documentService.generateDocument();
            workbook.write(bos);
            byte[] barray = bos.toByteArray();

            try(InputStream is = new ByteArrayInputStream(barray)) {
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                sendDocument.setDocument(new InputFile(is, "categories.xlsx"));
                execute(sendDocument);
            }

        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void addTreeCommand(String[] str, Long chatId){

        if (str.length == 2) {
            categoryService.addElement(str[1], null);
            sendMessage(chatId, "Элемент добавлен.");
        } else if (str.length > 2) {
            Category parent = categoryService.getCategoryByName(str[1]);
            if (parent != null) {
                categoryService.addElement(str[2], parent);
                sendMessage(chatId, "Дочерний элемент добавлен");
            } else {
                sendMessage(chatId, "Родительский элемент не найден");
            }
        } else {
            sendMessage(chatId, "Неверное количество команд");
        }
    }

    private void deleteTreeCommand(String[] str, Long chatId) {
        Category parent = categoryService.getCategoryByName(str[1]);
        if (str.length == 2) {
            categoryService.deleteElement(str[1], null);
            sendMessage(chatId, "Элемент удален.");
        } else if (str.length > 2) {
            if (parent != null) {
                categoryService.deleteElement(str[2], parent);
                sendMessage(chatId, "Дочерний элемент удален.");
            } else {
                sendMessage(chatId, "Родительский элемент не найден.");
            }
        } else {
            sendMessage(chatId, "Неверное количество команд");
        }
    }




    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        }catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return categoryTreeBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return categoryTreeBotConfig.getBotToken();
    }
}
