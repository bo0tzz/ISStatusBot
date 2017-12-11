package me.bo0tzz.isstatusbot.handler;

import com.jtelegram.api.events.EventHandler;
import com.jtelegram.api.events.inline.keyboard.CallbackQueryEvent;
import com.jtelegram.api.requests.message.edit.EditMessageReplyMarkup;
import me.bo0tzz.isstatusbot.live.LiveLocationMessage;
import me.bo0tzz.isstatusbot.live.LiveLocationUpdateManager;

public class CallbackQueryEventHandler implements EventHandler<CallbackQueryEvent> {

    private final ISStatusRegistry registry;
    private final LiveLocationUpdateManager updateManager;

    public CallbackQueryEventHandler(ISStatusRegistry registry, LiveLocationUpdateManager updateManager) {
        this.registry = registry;
        this.updateManager = updateManager;
    }

    @Override
    public void onEvent(CallbackQueryEvent event) {
        registry.getMain().getTelegramBot().perform(EditMessageReplyMarkup.builder()
                .inlineMessageId(event.getQuery().getInlineMessageId())
                .replyMarkup(null)
                .errorHandler(e -> System.out.println(e.getDescription()))
                .build()
        );
        updateManager.addMessage(new LiveLocationMessage(
                null,
                null,
                event.getQuery().getInlineMessageId(),
                System.currentTimeMillis() + 30 * 60 * 1000 // Go for 30 minutes
        ));
    }

}
