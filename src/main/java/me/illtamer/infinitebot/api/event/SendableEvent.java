package me.illtamer.infinitebot.api.event;

import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.List;

/**
 * 可回复事件
 * */
public interface SendableEvent {

    /**
     * 向消息源发送消息
     * */
    void sendMessage(Message message);

    /**
     * 向消息源发送消息
     * */
    void sendMessage(String msg);

    /**
     * 向消息源发送消息
     * */
    void sendMessage(List<String> msgs);

    /**
     * 引用回复消息源
     * */
    void reply(String msg);

    /**
     * 引用回复消息源
     * */
    void reply(List<String> msgs);

    /**
     * 引用回复消息源
     * */
    void reply(Message message);

    /**
     * 获取发送者的qq
     * */
    long getSenderId();

    /**
     * 获取消息发出的时间
     * */
    int getTime();

    /**
     * 戳一戳
     * */
    void nudge();
}
