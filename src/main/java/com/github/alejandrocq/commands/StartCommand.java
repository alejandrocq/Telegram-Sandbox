package com.github.alejandrocq.commands;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class StartCommand extends BotCommand {

    public StartCommand () {
        super("start", "Command to start the bot");
    }

    @Override public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String message = "Hi, " + user.getUserName() + "\n" + "Your ID is: " + user.getId();

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(message);

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
