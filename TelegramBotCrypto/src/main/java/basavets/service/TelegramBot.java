package basavets.service;

import basavets.config.BotConfig;
import basavets.connection.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final Connection connection;
    private final String BTC_TO_USDT = "Хочу обменять BTC-WXG на USDT-WXG";
    private final String USDT_TO_BTC = "Хочу обменять USDT-WXG на BTC-WXG";
    private String textCurrency;
    private boolean calculateUSDTResult = false;
    private boolean calculateBTCResult = false;


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
        if (update.hasMessage() && update.getMessage().hasText() && !calculateBTCResult && !calculateUSDTResult) {
            String messageText = update.getMessage().getText();
            long chaTId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    textPrint(chaTId, update.getMessage().getChat().getFirstName());
                    textCurrency = connection.getTextCurrency();
                    printCurrency(chaTId, textCurrency);
                    setButtons(chaTId);
                    break;
                case BTC_TO_USDT:
                    setAmount(chaTId);
                    calculateUSDTResult = true;
                    return;
                case USDT_TO_BTC:
                    setAmount(chaTId);
                    calculateBTCResult = true;
                    return;
                default:
                    wrongMessage(chaTId);
                    break;
            }
        }
        if (update.hasMessage() && update.getMessage().hasText() && calculateUSDTResult) {
            long chaTId = update.getMessage().getChatId();
            if (update.getMessage().getText().matches("^[0-9]*[.,]?[0-9]+$")) {
                calculateUSDT(chaTId, update.getMessage());
            } else {
                wrongNumberFormat(chaTId);
            }
            calculateUSDTResult = false;
        }
        if (update.hasMessage() && update.getMessage().hasText() && calculateBTCResult) {
            long chaTId = update.getMessage().getChatId();
            if (update.getMessage().getText().matches("^[0-9]*[.,]?[0-9]+$")) {
                calculateBTC(chaTId, update.getMessage());
            } else {
                wrongNumberFormat(chaTId);
            }
            calculateBTCResult = false;
        }
    }


    private void wrongMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Комманда не распознана, нажмите /start");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void wrongNumberFormat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Нужно вводить число, нажмите /start");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void textPrint(long chatId, String name) {
        String answer = "Привет " + name + " рад вас видеть. Ожидайте 20 секунд, идет получение курса обмена на сегодня";
        sendText(chatId, answer);
    }


    private void printCurrency(long chatId, String currency) {
        String answer = "Курс обмена - " + currency;
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


    private void setButtons(long chatId) {
        SendMessage message = new SendMessage();
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        message.setChatId(String.valueOf(chatId));
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(BTC_TO_USDT));
        keyboardRow.add(new KeyboardButton(USDT_TO_BTC));
        keyboardRowList.add(keyboardRow);
        setButton(message).setKeyboard(keyboardRowList);
        message.setText("Выберите какую валюту желаете обменять");
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
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private void setAmount(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Введите сумму");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void calculateUSDT(long chatId, Message getMessage) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Вы получите сумму " +
                String.valueOf(connection.changeBitcoin(getMessage.getText(), textCurrency)) + " USDT");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
            e.printStackTrace();
        }
        message.setText("https://waves.exchange");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void calculateBTC(long chaTId, Message getMessage) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chaTId));
        message.setText("Вы получите сумму " +
                String.valueOf(connection.changeUSDT(getMessage.getText(), textCurrency)) + " BTK");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
            e.printStackTrace();
        }
        message.setText("https://waves.exchange");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error " + e.getMessage());
            e.printStackTrace();
        }
    }
}
