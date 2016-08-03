## Telegram Sandbox

Repository to test Java APIs to create Telegram bots. At this moment, this API is being tested: [Telegram Bot Java Library](https://github.com/rubenlagus/TelegramBots)
Link to the bot created for the tests: http://telegram.me/JavaSandboxBot

To run the project: ```mvn compile && mvn exec:java```

Two branches have been created for testing:

- **inline-queries-test**: To test inline queries functionality and also commands, extending _TelegramLongPollingBot_ class.
- **commands-test**: To test commands extending _TelegramLongPollingCommandBot_ class.
