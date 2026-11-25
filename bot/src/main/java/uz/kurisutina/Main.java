package uz.kurisutina;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) throws Exception {
        String token = System.getenv("BOT_TOKEN");
        if (token == null || token.isBlank()) {
            System.err.println("Задайте переменную окружения BOT_TOKEN");
            System.exit(1);
        }

        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        app.registerBot(token, new DivergenceBot(token, new StubModelClient()));
        System.out.println("Бот запущен");

        Thread.currentThread().join();
    }
}