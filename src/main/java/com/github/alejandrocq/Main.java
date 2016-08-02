package com.github.alejandrocq;

import com.github.alejandrocq.handlers.JavaSandboxHandler;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.logging.BotsFileHandler;

public class Main {

    private static final String LOGTAG = Main.class.getSimpleName();

    public static void main(String[] args) {
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(LOGTAG, e);
        }

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new JavaSandboxHandler());
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
            e.printStackTrace();
        }
    }
}
