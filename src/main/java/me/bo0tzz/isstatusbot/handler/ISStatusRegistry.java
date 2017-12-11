package me.bo0tzz.isstatusbot.handler;


import com.jtelegram.api.events.inline.InlineQueryEvent;
import com.jtelegram.api.events.message.LocationMessageEvent;
import me.bo0tzz.isstatusbot.ISStatusBot;
import me.bo0tzz.isstatusbot.handler.command.LocationCommand;
import me.bo0tzz.isstatusbot.handler.command.PeopleCommand;
import me.bo0tzz.isstatusbot.live.LiveLocationUpdateManager;

public class ISStatusRegistry {

    private final ISStatusBot main;
    private final LiveLocationUpdateManager updateManager;

    public ISStatusRegistry(ISStatusBot main) {
        this.main = main;
        this.updateManager = new LiveLocationUpdateManager(this);

        //Register message event handlers
        main.getTelegramBot().getEventRegistry().registerEvent(LocationMessageEvent.class, new LocationMessageEventHandler(this));
        main.getTelegramBot().getEventRegistry().registerEvent(InlineQueryEvent.class, new InlineQueryEventHandler(this, updateManager));


        //Register command handlers
        main.getTelegramBot().getCommandRegistry().registerCommand(new PeopleCommand(this));
        main.getTelegramBot().getCommandRegistry().registerCommand(new LocationCommand(this, updateManager));
    }

    public ISStatusBot getMain() {
        return main;
    }
}
