package me.illtamer.infinitebot.api.event;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.util.ImageUtil;
import me.illtamer.infinitelib.util.StringUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;

/**
 * 消息接收事件
 * */
public class MessageReceiveEvent extends InfiniteEvent implements SendableEvent {
    private final MessageEvent miraiEvent;
    private final MessageChain message;
    private final String msg;
    private final long senderId;
    private final String senderName;
    private final int time;
    private final boolean strToImage;

    public MessageReceiveEvent(MessageEvent event) {
        super(event);
        this.miraiEvent = event;
        this.message = event.getMessage();
        this.msg = message.contentToString();
        this.senderId = event.getSender().getId();
        this.senderName = event.getSenderName();
        this.time = event.getTime();
        this.strToImage = InfiniteBot.getInstance().getBotConfig().isMessageToImage();
    }

    /**
     * 获取消息对应的文本
     * */
    public String getMsg() {
        return this.msg;
    }

    /**
     * 获取消息对应的消息链
     * */
    public MessageChain getMessage() {
        return this.message;
    }

    /**
     * 撤回对应消息
     * @apiNote 无权限会抛异常
     * */
    public void recall(Bot bot) {
        Mirai.getInstance().recallMessage(bot, miraiEvent.getSource());
    }

    /**
     * 获取消息发送者昵称
     * */
    public String getSenderName() {
        return this.senderName;
    }

    @Override
    public MessageEvent getEvent() {
        return this.miraiEvent;
    }

    @Override
    public void sendMessage(Message message) {
        miraiEvent.getSubject().sendMessage(message);
    }

    @Override
    public void sendMessage(String msg) {
        if (!strToImage) {
            miraiEvent.getSubject().sendMessage(msg);
        } else {
            miraiEvent.getSubject().sendMessage(ImageUtil.strToImage(miraiEvent.getSubject(), msg));
        }
    }

    @Override
    public void sendMessage(List<String> msgs) {
        if (!strToImage) {
            miraiEvent.getSubject().sendMessage(StringUtil.toString(msgs));
        } else {
            miraiEvent.getSubject().sendMessage(ImageUtil.strToImage(miraiEvent.getSubject(), StringUtil.toArray(msgs)));
        }
    }

    @Override
    public void reply(String msg) {
        MessageChainBuilder builder = new MessageChainBuilder().append(new QuoteReply(message));
        if (!strToImage) {
            builder.append(msg);
        } else {
            builder.append(ImageUtil.strToImage(miraiEvent.getSubject(), msg));
        }
        miraiEvent.getSubject().sendMessage(builder.build());
    }

    @Override
    public void reply(List<String> msgs) {
        MessageChainBuilder builder = new MessageChainBuilder().append(new QuoteReply(message));
        if (!strToImage) {
            builder.append(StringUtil.toString(msgs));
        } else {
            builder.append(ImageUtil.strToImage(miraiEvent.getSubject(), StringUtil.toArray(msgs)));
        }
        miraiEvent.getSubject().sendMessage(builder.build());
    }

    @Override
    public void reply(Message message) {
        miraiEvent.getSubject().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(this.message))
                .append(message)
                .build());
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public void nudge() {
        miraiEvent.getSender().nudge();
    }
}
