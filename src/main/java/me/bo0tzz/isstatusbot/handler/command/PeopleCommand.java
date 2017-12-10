package me.bo0tzz.isstatusbot.handler.command;

import com.jtelegram.api.chat.ChatType;
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
        System.out.println("Command received: " + command.getBaseMessage().getContent());
        System.out.println("Command isMentioned: " + command.isMentioned());
        System.out.println("Chat type: " + command.getChat().getType());
        ISSPeopleRequest.builder()
                .callback(issPeople -> {
                    System.out.println("Started callback for ISSPeopleRequest");
                    StringBuilder people = new StringBuilder("They are:\n");
                    issPeople.getPeople().forEach(person -> people.append(String.format("%s\n", person.getName())));
                    System.out.println("Output is " + people.toString());
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
        return (command.isMentioned() || command.getChat().getType() == ChatType.PRIVATE) && command.getBaseCommand().equalsIgnoreCase(commandName);
    }
}
