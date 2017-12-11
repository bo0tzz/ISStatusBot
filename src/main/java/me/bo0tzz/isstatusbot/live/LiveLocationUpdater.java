package me.bo0tzz.isstatusbot.live;

import com.jtelegram.api.requests.message.edit.EditMessageLiveLocation;
import me.bo0tzz.isstatusbot.handler.ISStatusRegistry;
import me.bo0tzz.opennotify4j.requests.ISSLocationRequest;

import java.util.concurrent.CopyOnWriteArrayList;

public class LiveLocationUpdater implements Runnable {

    private final CopyOnWriteArrayList<LiveLocationMessage> messages;
    private final ISStatusRegistry registry;

    public LiveLocationUpdater(CopyOnWriteArrayList<LiveLocationMessage> messages, ISStatusRegistry registry) {
        this.messages = messages;
        this.registry = registry;
    }

    @Override
    public void run() {
        messages.removeIf(liveLocationMessage -> liveLocationMessage.getUntil() < System.currentTimeMillis());
        ISSLocationRequest.builder()
                .callback(issLocation ->
                    messages.iterator().forEachRemaining(liveLocationMessage ->
                        registry.getMain().getTelegramBot().perform(EditMessageLiveLocation.builder()
                                .chatId(liveLocationMessage.getChatId())
                                .messageId(liveLocationMessage.getMessageId())
                                .inlineMessageId(liveLocationMessage.getInlineMessageId())
                                .latitude(issLocation.getLocation().getLatitude())
                                .longitude(issLocation.getLocation().getLongitude())
                                .build()))
                ).build().perform();
    }
}
