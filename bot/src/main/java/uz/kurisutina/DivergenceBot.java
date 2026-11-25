package uz.kurisutina;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Locale;

public class DivergenceBot implements LongPollingSingleThreadUpdateConsumer {
    private static final int MAX_LEN = 2000;

    private final TelegramClient client;
    private final ModelClient model;

    public DivergenceBot(String token, ModelClient model) {
        this.client = new OkHttpTelegramClient(token);
        this.model = model;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage()) return;

        Message msg = update.getMessage();
        long chatId = msg.getChatId();

        if (!msg.hasText()) {
            send(chatId, "Отправьте сообщение текстом, чтобы я его проверил.");
            return;
        }

        String text = msg.getText().trim();

        if (text.equals("/start") || text.equals("/help")) {
            send(chatId, "Divergence Meter.\nПришлите мне подозрительное SMS или сообщение, "
                    + "и я покажу, насколько оно отклоняется от нормы.");
            return;
        }
        if (text.isEmpty()) {
            send(chatId, "Сообщение пустое.");
            return;
        }
        if (text.length() > MAX_LEN) {
            send(chatId, "Сообщение слишком длинное (максимум " + MAX_LEN + " символов).");
            return;
        }

        try {
            Prediction p = model.predict(text);
            String reply = String.format(Locale.US,
                    "Divergence: %.6f%nВердикт: %s%nВлияющие слова: %s",
                    p.score(), p.verdict(), String.join(", ", p.topWords()));
            send(chatId, reply);
        } catch (Exception e) {
            e.printStackTrace();
            send(chatId, "Не удалось получить ответ от модели. Попробуйте позже.");
        }
    }

    private void send(long chatId, String text) {
        try {
            client.execute(SendMessage.builder().chatId(chatId).text(text).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}