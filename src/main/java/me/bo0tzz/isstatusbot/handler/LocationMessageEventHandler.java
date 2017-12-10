package me.bo0tzz.isstatusbot.handler;

import com.jtelegram.api.events.EventHandler;
import com.jtelegram.api.events.message.LocationMessageEvent;
import com.jtelegram.api.message.media.Location;
import com.jtelegram.api.requests.message.send.SendText;
import me.bo0tzz.isstatusbot.util.TimezoneMapper;
import me.bo0tzz.opennotify4j.bean.Pass;
import me.bo0tzz.opennotify4j.requests.ISSPassRequest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class LocationMessageEventHandler implements EventHandler<LocationMessageEvent> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ISStatusRegistry registry;

    public LocationMessageEventHandler(ISStatusRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onEvent(LocationMessageEvent locationMessageEvent) {

        Location location = locationMessageEvent.getMessage().getLocation();
        String tzString = TimezoneMapper.latLngToTimezoneString((double)location.getLatitude(), (double)location.getLongitude());
        ZoneId timeZone = TimeZone.getTimeZone(tzString).toZoneId();

        ISSPassRequest.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .callback(issPass -> {
                    Pass pass = issPass.nextPass();
                    String formattedTime = Instant.ofEpochSecond(pass.getRisetime())
                            .atZone(timeZone)
                            .format(formatter);
                    String out = String.format("The next pass by the ISS at your location will be at %s and last for %02d minutes and %02d seconds.",
                            formattedTime,
                            pass.getDuration() / 60,
                            pass.getDuration() % 60);

                    registry.getMain().getTelegramBot().perform(SendText.builder()
                            .chatId(locationMessageEvent.getMessage().getChat().getChatId())
                            .text(out)
                            .build());
                })
                .build()
                .perform();

    }
}
