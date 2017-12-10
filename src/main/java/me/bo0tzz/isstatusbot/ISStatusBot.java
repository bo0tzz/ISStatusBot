package me.bo0tzz.isstatusbot;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import com.jtelegram.api.update.PollingUpdateProvider;
import com.jtelegram.api.update.UpdateType;
import me.bo0tzz.isstatusbot.handler.ISStatusRegistry;

public class ISStatusBot {

    private final TelegramBotRegistry registry;
    private TelegramBot telegramBot;

    public ISStatusBot(String key) {
        this.registry = TelegramBotRegistry.builder()
                .updateProvider(PollingUpdateProvider.builder()
                        .allowedUpdate(UpdateType.INLINE_QUERY)
                        .allowedUpdate(UpdateType.MESSAGE)
                        .build())
                .build();

        registry.registerBot(key, (bot, error) -> {
            if (error != null) {
                System.out.println(error.getDescription());
                return;
            }

            this.telegramBot = bot;
            new ISStatusRegistry(this);
        });
    }

    public TelegramBot getTelegramBot() {
        return telegramBot;
    }

    public static void main(String[] args) {

        String key = System.getenv("BOT_KEY");
        key = key == null ? args[0] : key;
        new ISStatusBot(key);

    }

}
