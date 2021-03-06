package me.bo0tzz.isstatusbot.handler;

import com.jtelegram.api.events.EventHandler;
import com.jtelegram.api.events.inline.InlineQueryEvent;
import com.jtelegram.api.inline.keyboard.InlineKeyboardButton;
import com.jtelegram.api.inline.keyboard.InlineKeyboardMarkup;
import com.jtelegram.api.inline.keyboard.InlineKeyboardRow;
import com.jtelegram.api.inline.result.InlineResultLocation;
import com.jtelegram.api.requests.inline.AnswerInlineQuery;
import me.bo0tzz.isstatusbot.live.LiveLocationUpdateManager;
import me.bo0tzz.opennotify4j.requests.ISSLocationRequest;

public class InlineQueryEventHandler implements EventHandler<InlineQueryEvent> {

    private final ISStatusRegistry registry;
    private final LiveLocationUpdateManager updateManager;

    public InlineQueryEventHandler(ISStatusRegistry registry, LiveLocationUpdateManager updateManager) {
        this.registry = registry;
        this.updateManager = updateManager;
    }

    @Override
    public void onEvent(InlineQueryEvent event) {
        // Welcome to builder hell
        ISSLocationRequest.builder()
                .callback(issLocation ->
                    registry.getMain().getTelegramBot().perform(
                        AnswerInlineQuery.builder()
                            .addResult(InlineResultLocation.builder()
                                    .longitude(issLocation.getLocation().getLongitude())
                                    .latitude(issLocation.getLocation().getLatitude())
                                    .title("ISS Location")
                                    .livePeriod(1800)
                                    .id(Integer.toString(issLocation.hashCode()))
                                    .replyMarkup(InlineKeyboardMarkup.builder()
                                            .keyboard(InlineKeyboardRow.builder()
                                                    .button(InlineKeyboardButton.builder()
                                                            .label("Start live updates (30 minutes)")
                                                            .callbackData("startlive")
                                                            .build()
                                                    ).build()
                                            ).build()
                                    ).build()
                            )
                            .queryId(event.getQuery().getId())
                            .cacheTime(30)
                            .errorHandler(e -> System.out.println(e.getDescription()))
                            .build()
                    )
                )
                .build().perform();
    }
}
