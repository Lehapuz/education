package basavets.service;

import basavets.config.BotConfig;
import basavets.connection.Connection;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

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
                sendText(chaTId, "What kind of currency do you need?");
                setConnection(chaTId);
                sendText(chaTId, "Input name of currency to know course");
            } else if (connection.getCurrency().containsKey(messageText)) {
                getCurrency(connection.getCurrency(), chaTId, messageText);
            } else {
                sendText(chaTId, "This currency was not recognize");
                sendText(chaTId, "please input /start");
            }
        }
    }


    private void textPrint(long chatId, String name) {
        String answer = "Hi " + name + " nice to meet you";
        sendText(chatId, answer);
    }


    private void sendText(long chatId, String sendText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(sendText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setConnection(long chatId) {
        SendMessage message = new SendMessage();
        connection.setCurrency();
        message.setChatId(String.valueOf(chatId));
        for (String c : connection.getCurrency().keySet()) {
            message.setText(c);
            try {
                execute(message);
            } catch (TelegramApiException telegramApiException) {
                telegramApiException.printStackTrace();
            }
        }
    }


    private void getCurrency(HashMap<String, String> courses, long chatId, String key) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(courses.get(key));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
