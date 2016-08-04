package com.github.alejandrocq;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.CommandRegistry;

public class Main {
    public static void main(String[] args) throws Exception {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new MyHandler());
    }

    private static class MyHandler extends TelegramLongPollingBot {
        private static final Logger log = Logger.getLogger("telegram-handler");
        private final CommandRegistry commands = new CommandRegistry();
        private final Map<Integer, Subscription> subscriptions = new HashMap<>();

        static class Subscription {
            final User user;
            final Chat chat;
            final String[] arguments;
            Subscription(User user, Chat chat, String[] arguments) {
                this.user = user; this.chat = chat; this.arguments = arguments;
            }

            @Override public String toString() {
                return "Subscription{" +
                        "user=" + user + ", " +
                        "chat=" + chat + ", " +
                        "arguments=" + Arrays.toString(arguments) +
                        '}';
            }
        }

        {
            commands.register(new BotCommand("/start", "Command to start the bot") {
                @Override public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
                    send(chat, msg -> msg.setText("You have typed /start command"));
                }
            });
            commands.register(new BotCommand("/subscribe", "Command to subscribe to this bot notification list") {
                @Override public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
                    if (subscriptions.containsKey(user.getId())) {
                        send(chat, msg -> msg.setText("Mmm... you are already subscribed!"));
                    } else {
                        subscriptions.put(user.getId(), new Subscription(user, chat, arguments));
                        send(chat, msg -> msg.setText("Success! will try to kill your battery with thousands "
                                + "of notifications!"));
                    }
                }
            });
            commands.register(new BotCommand("/unsubscribe", "Command to unsubscribe to this bot notification list") {
                @Override public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
                    if (!subscriptions.containsKey(user.getId())) {
                        send(chat, msg -> msg.setText("Mmm... you aren't subscribed to this bot!"));
                    } else {
                        subscriptions.remove(user.getId());
                        send(chat, msg -> msg.setText("Success! your battery is safe again!"));
                    }
                }
            });
            commands.register(new BotCommand("/status", "Command to check your subscription status") {
                @Override public void execute(AbsSender sender, User user, Chat chat, String[] arguments) {
                    if (!subscriptions.containsKey(user.getId())) {
                        send(chat, msg -> msg.setText("Mmm... you aren't subscribed to this bot!"));
                    } else {
                        send(chat, msg -> msg.setText("Status: " + subscriptions.get(user.getId())));
                    }
                }
            });
            log.info("Registered commands:\n" + commands.getRegisteredCommands()
                    .stream().map(c -> c.getCommandIdentifier() + " :: " + c.getDescription())
                    .collect(Collectors.joining("\n")));
            // TODO this list of command should be sent to the botfather so command auto-completion works

            log.info("Starting message generator...");
            new Timer(true).scheduleAtFixedRate(new TimerTask() {
                @Override public void run() {
                    subscriptions.values().forEach(s -> send(s.chat, msg -> msg.setText("Hey! you now that "
                            + "the server time is " + Instant.now() + "!")));
                }
            }, 0, TimeUnit.SECONDS.toMillis(30));

        }

        @Override public void onUpdateReceived(Update update) {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.isCommand() && commands.executeCommand(this, message)) return;
            }

            log.warning("ops! unsupported update received: " + update);
        }

        @Override public String getBotToken() {
            // TODO tokens and bots should be created programmatically (eg. write to botfather using the tel. cli. api)
            return System.getProperty("telegram.token", System.getenv("TELEGRAM_TOKEN"));
        }

        @Override public String getBotUsername() {
            return System.getProperty("telegram.name", System.getenv().getOrDefault("TELEGRAM_NAME", "JavaSandboxBot"));
        }

        void send(Chat chat, Consumer<SendMessage> fn) {
            SendMessage message = new SendMessage();
            message.setChatId(chat.getId().toString());
            fn.accept(message);
            send(message);
        }

        void send(SendMessage message) {
            try {
                this.sendMessage(message);
            } catch (TelegramApiException e) {
                log.log(Level.SEVERE, "error sending message response: " + message, e);
            }
        }
    }
}
