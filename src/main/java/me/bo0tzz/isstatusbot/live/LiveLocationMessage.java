package me.bo0tzz.isstatusbot.live;

import com.jtelegram.api.chat.id.ChatId;

public class LiveLocationMessage {

    private final ChatId chatId;
    private final Integer messageId;
    private final String inlineMessageId;
    private final Long until;

    public LiveLocationMessage(ChatId chatId, Integer messageId, String inlineMessageId, Long until) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.inlineMessageId = inlineMessageId;
        this.until = until;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public String getInlineMessageId() {
        return inlineMessageId;
    }

    public Long getUntil() {
        return until;
    }
}
