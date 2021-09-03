package me.illtamer.infinitebot.api.event.group;

import me.illtamer.infinitebot.api.event.MessageReceiveEvent;
import me.illtamer.infinitelib.util.StringUtil;
import net.mamoe.mirai.message.data.Message;

import java.util.List;

/**
 * 群消息接受事件
 * */
public class GroupMessageEvent extends MessageReceiveEvent {
    private final net.mamoe.mirai.event.events.GroupMessageEvent miraiEvent;
    private final long groupId;

    public GroupMessageEvent(net.mamoe.mirai.event.events.GroupMessageEvent event) {
        super(event);
        this.miraiEvent = event;
        this.groupId = miraiEvent.getGroup().getId();
    }

    /**
     * 向消息发送者发送消息
     * */
    public void sendPersonalMessage(String msg) {
        miraiEvent.getSender().sendMessage(msg);
    }

    /**
     * 向消息发送者发送消息
     * */
    public void sendPersonalMessage(List<String> msgs) {
        miraiEvent.getSender().sendMessage(StringUtil.toString(msgs));
    }

    /**
     * 向消息发送者发送消息
     * */
    public void sendPersonalMessage(Message message) {
        miraiEvent.getSender().sendMessage(message);
    }

    public long getGroupId() {
        return groupId;
    }

    @Override
    public net.mamoe.mirai.event.events.GroupMessageEvent getEvent() {
        return miraiEvent;
    }
}
