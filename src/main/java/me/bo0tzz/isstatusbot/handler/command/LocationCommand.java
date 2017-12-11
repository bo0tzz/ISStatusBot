package me.bo0tzz.isstatusbot.handler.command;

import com.jtelegram.api.chat.ChatType;
import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.CommandHandler;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendLocation;
import me.bo0tzz.isstatusbot.handler.ISStatusRegistry;
import me.bo0tzz.isstatusbot.live.LiveLocationMessage;
import me.bo0tzz.isstatusbot.live.LiveLocationUpdateManager;
import me.bo0tzz.opennotify4j.requests.ISSLocationRequest;

public class LocationCommand implements CommandHandler {

    private final String commandName = "location";
    private final ISStatusRegistry registry;
    private final LiveLocationUpdateManager updateManager;

    public LocationCommand(ISStatusRegistry registry, LiveLocationUpdateManager updateManager) {
        this.registry = registry;
        this.updateManager = updateManager;
    }

    @Override
    public void onCommand(TextMessageEvent textMessageEvent, Command command) {
        ISSLocationRequest.builder()
                .callback(issLocation ->
                        registry.getMain().getTelegramBot().perform(
                            SendLocation.builder()
                                .chatId(command.getChat().getChatId())
                                .latitude(issLocation.getLocation().getLatitude())
                                .longitude(issLocation.getLocation().getLongitude())
                                .livePeriod(1800)
                                .callback(message ->
                                    updateManager.addMessage(new LiveLocationMessage(
                                            message.getChat().getChatId(),
                                            message.getMessageId(),
                                            null,
                                            System.currentTimeMillis() + 30 * 60 * 1000 // Run for 30 minutes
                                    )))
                                .errorHandler(e -> System.out.println(e.getDescription()))
                                .build()
                    ))
                .build().perform();
    }

    @Override
    public boolean test(TextMessageEvent event, Command command) {
        if ((command.isMentioned() || event.getMessage().getChat().getType() == ChatType.PRIVATE) && command.getBaseCommand().equalsIgnoreCase(commandName)) {
            onCommand(event, command);
            return true;
        }

        return false;
    }
}
