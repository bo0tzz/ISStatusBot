package me.bo0tzz.isstatusbot.handler.command;

import com.jtelegram.api.chat.ChatType;
import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.CommandHandler;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendLocation;
import me.bo0tzz.isstatusbot.handler.ISStatusRegistry;
import me.bo0tzz.opennotify4j.requests.ISSLocationRequest;

public class LocationCommand implements CommandHandler {

    private final String commandName = "location";
    private final ISStatusRegistry registry;

    public LocationCommand(ISStatusRegistry registry) {
        this.registry = registry;
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
