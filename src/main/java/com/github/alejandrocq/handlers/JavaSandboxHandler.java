package com.github.alejandrocq.handlers;

import com.github.alejandrocq.BotConfig;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class JavaSandboxHandler extends TelegramLongPollingBot {

    private final String LOGTAG = JavaSandboxHandler.class.getSimpleName();

    public JavaSandboxHandler() {

    }

    @Override public void onUpdateReceived(Update update) {

    }

    @Override public String getBotToken() {
        return BotConfig.JAVA_SANDBOX_BOT_TOKEN;
    }

    @Override public String getBotUsername() {
        return BotConfig.JAVA_SANDBOX_BOT_USERNAME;
    }
}
