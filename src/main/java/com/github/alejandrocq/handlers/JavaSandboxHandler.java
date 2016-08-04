package com.github.alejandrocq.handlers;

import com.github.alejandrocq.BotConfig;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class JavaSandboxHandler extends TelegramLongPollingBot {

    private final String LOGTAG = JavaSandboxHandler.class.getSimpleName();

    public JavaSandboxHandler() {

    }

    @Override public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();

        //With this, we can check if the user is typing a certain command in this implementation
        if (msg.toString().contains("/start")) {
            SendMessage sendMsg = new SendMessage();
            sendMsg.setChatId(msg.getChatId().toString());
            sendMsg.setText("You have typed /start command");
            try {
                sendMessage(sendMsg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override public String getBotToken() {
        return System.getProperty("telegram.token", System.getenv("TELEGRAM_TOKEN"));
    }

    @Override public String getBotUsername() {
        return BotConfig.JAVA_SANDBOX_BOT_USERNAME;
    }
}
