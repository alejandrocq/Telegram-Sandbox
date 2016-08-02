package com.github.alejandrocq.handlers;

import com.github.alejandrocq.BotConfig;
import com.github.alejandrocq.commands.StartCommand;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.logging.BotLogger;

public class JavaSandboxHandler extends TelegramLongPollingCommandBot {

    private final String LOGTAG = JavaSandboxHandler.class.getSimpleName();

    public JavaSandboxHandler() {
        register(new StartCommand());

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId().toString());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot.");
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        });
    }

    @Override public void processNonCommandUpdate(Update update) {
        //Handle situations when the user doesn't type a command

        Message message = update.getMessage();

        if (message.hasText()) {
            SendMessage msgToSend = new SendMessage();
            msgToSend.setChatId(message.getChatId().toString());
            msgToSend.setText("Please type a command to begin");

            try {
                sendMessage(msgToSend);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        }
    }

    @Override public String getBotToken() {
        return BotConfig.JAVA_SANDBOX_BOT_TOKEN;
    }

    @Override public String getBotUsername() {
        return BotConfig.JAVA_SANDBOX_BOT_USERNAME;
    }
}
