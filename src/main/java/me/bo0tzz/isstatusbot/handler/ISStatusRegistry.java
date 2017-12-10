package me.bo0tzz.isstatusbot.handler;


import com.jtelegram.api.events.message.LocationMessageEvent;
import me.bo0tzz.isstatusbot.ISStatusBot;
import me.bo0tzz.isstatusbot.handler.command.LocationCommand;
import me.bo0tzz.isstatusbot.handler.command.PeopleCommand;

public class ISStatusRegistry {

    private final ISStatusBot main;

    public ISStatusRegistry(ISStatusBot main) {
        this.main = main;

        //Register message event handlers
        main.getTelegramBot().getEventRegistry().registerEvent(LocationMessageEvent.class, new LocationMessageEventHandler(this));

        //Register command handlers
        main.getTelegramBot().getCommandRegistry().registerCommand(new PeopleCommand(this));
        main.getTelegramBot().getCommandRegistry().registerCommand(new LocationCommand(this));
    }

    public ISStatusBot getMain() {
        return main;
    }
}
