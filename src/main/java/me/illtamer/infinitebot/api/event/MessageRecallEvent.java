package me.illtamer.infinitebot.api.event;

import me.illtamer.infinitebot.bot.BotInitiation;
import net.mamoe.mirai.Bot;

/**
 * 消息撤回事件
 * */
public class MessageRecallEvent extends InfiniteEvent {
    private final net.mamoe.mirai.event.events.MessageRecallEvent miraiEvent;
    private final long authorId;
    private final int[] messageIds;

    public MessageRecallEvent(net.mamoe.mirai.event.events.MessageRecallEvent event) {
        super(event);
        this.miraiEvent = event;
        this.authorId = event.getAuthorId();
        this.messageIds = event.getMessageIds();
    }

    /**
     * 是否是Bot撤回
     * */
    public boolean isBot() {
        for (Bot bot : BotInitiation.BOTS) {
            if (bot.getId() == authorId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取撤回者qq
     * */
    public long getAuthorId() {
        return authorId;
    }

    /**
     * 获取撤回消息的id
     * */
    public int[] getMessageIds() {
        return messageIds;
    }

    @Override
    public net.mamoe.mirai.event.events.MessageRecallEvent getEvent() {
        return miraiEvent;
    }
}
