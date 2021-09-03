package me.illtamer.infinitebot.api.event.personal;

import me.illtamer.infinitebot.api.event.MessageReceiveEvent;

/**
 * 好友消息接受事件
 * */
public class FriendMessageEvent extends MessageReceiveEvent {
    private final net.mamoe.mirai.event.events.FriendMessageEvent miraiEvent;

    public FriendMessageEvent(net.mamoe.mirai.event.events.FriendMessageEvent event) {
        super(event);
        this.miraiEvent = event;
    }

    @Override
    public net.mamoe.mirai.event.events.FriendMessageEvent getEvent() {
        return miraiEvent;
    }
}
