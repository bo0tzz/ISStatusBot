package me.bo0tzz.isstatusbot.live;

import me.bo0tzz.isstatusbot.handler.ISStatusRegistry;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LiveLocationUpdateManager {

    private final CopyOnWriteArrayList<LiveLocationMessage> messages;
    private final ScheduledThreadPoolExecutor executor;
    private final ISStatusRegistry registry;

    public LiveLocationUpdateManager(ISStatusRegistry registry) {
        this.messages = new CopyOnWriteArrayList<>();
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.registry = registry;
        executor.scheduleAtFixedRate(
                new LiveLocationUpdater(messages, registry),
                0,
                30,
                TimeUnit.SECONDS);
    }

    public void addMessage(LiveLocationMessage message) {
        messages.add(message);
    }

}
