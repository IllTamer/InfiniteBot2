package me.illtamer.infinitebot.api.event.group;

import me.illtamer.infinitebot.api.event.MessageReceiveEvent;

/**
 * 群成员临时会话事件
 * */
public class GroupTempMessageEvent extends MessageReceiveEvent {
    private final net.mamoe.mirai.event.events.GroupTempMessageEvent miraiEvent;

    public GroupTempMessageEvent(net.mamoe.mirai.event.events.GroupTempMessageEvent event) {
        super(event);
        this.miraiEvent = event;
    }

    @Override
    public net.mamoe.mirai.event.events.GroupTempMessageEvent getEvent() {
        return miraiEvent;
    }
}
