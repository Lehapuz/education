package basavets.service;

import basavets.config.BotConfig;
import basavets.connection.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final Connection connection;


    public TelegramBot(BotConfig botConfig, Connection connection) {
        this.botConfig = botConfig;
        this.connection = connection;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chaTId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                textPrint(chaTId, update.getMessage().getChat().getFirstName());
                setConnection(chaTId);
            } else if (connection.getCurrency().containsKey(messageText)) {
                getCurrency(connection.getCurrency(), chaTId, messageText);
                log.info("User -" + update.getMessage().getChat().getFirstName() + " received answer");
            } else {
                sendText(chaTId, "Команда не распознана");
                sendText(chaTId, "Нажмите /start");
            }
        }
    }


    private void textPrint(long chatId, String name) {
        String answer = "Привет " + name + " рад тебя видеть";
        sendText(chatId, answer);
    }


    private void sendText(long chatId, String sendText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(sendText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void setConnection(long chatId) {
        SendMessage message = new SendMessage();
        connection.setCurrency();
        message.setChatId(String.valueOf(chatId));
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        for (String c : connection.getCurrency().keySet()) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(c));
            keyboardRowList.add(keyboardRow);
            setButton(message).setKeyboard(keyboardRowList);
        }
        message.setText("Выберите курс какой валюты желаете узнать");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
        }
    }


    private void getCurrency(HashMap<String, String> courses, long chatId, String key) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        message.setText(courses.get(key) + " рубля за " + connection.getHashCurrency().get(courses.get(key)));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
        }
    }


    private ReplyKeyboardMarkup setButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
