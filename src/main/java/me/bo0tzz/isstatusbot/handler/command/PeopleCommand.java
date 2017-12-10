package me.bo0tzz.isstatusbot.handler.command;

import com.jtelegram.api.commands.Command;
import com.jtelegram.api.commands.CommandHandler;
import com.jtelegram.api.events.message.TextMessageEvent;
import com.jtelegram.api.requests.message.send.SendText;
import me.bo0tzz.isstatusbot.handler.ISStatusRegistry;
import me.bo0tzz.opennotify4j.requests.ISSPeopleRequest;

public class PeopleCommand implements CommandHandler {

    private final String commandName = "people";
    private final ISStatusRegistry registry;

    public PeopleCommand(ISStatusRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onCommand(TextMessageEvent textMessageEvent, Command command) {
        ISSPeopleRequest.builder()
                .callback(issPeople -> {
                            StringBuilder people = new StringBuilder("They are:\n");
                            issPeople.getPeople().forEach(person -> {
                                people.append(String.format("%s\n", person.getName()));
                            });
                            registry.getMain().getTelegramBot().perform(
                                    SendText.builder()
                                            .chatId(command.getChat().getChatId())
                                            .text("There are currently %d people in the ISS. " + people.toString())
                                            .build());
                        }
                ).build().perform();
    }

    @Override
    public boolean test(TextMessageEvent event, Command command) {
        return command.isMentioned() && command.getBaseCommand().equalsIgnoreCase(commandName);
    }
}
