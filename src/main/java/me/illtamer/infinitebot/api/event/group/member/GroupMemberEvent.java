package me.illtamer.infinitebot.api.event.group.member;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.api.event.InfiniteEvent;
import me.illtamer.infinitebot.api.event.group.NotEnabledException;
import me.illtamer.infinitebot.util.ImageUtil;
import me.illtamer.infinitelib.util.StringUtil;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.apache.commons.lang.Validate;

import java.util.List;

/**
 * 群成员事件
 * */
public class GroupMemberEvent extends InfiniteEvent implements MemberEvent {
    private final net.mamoe.mirai.event.events.GroupMemberEvent miraiEvent;
    private final long groupId;
    private final Member member;
    private final boolean strToImage;

    public GroupMemberEvent(net.mamoe.mirai.event.events.GroupMemberEvent event) {
        super(event);
        this.miraiEvent = event;
        this.groupId = event.getGroup().getId();
        this.member = event.getMember();
        this.strToImage = InfiniteBot.getInstance().getBotConfig().isMessageToImage();
    }

    /**
     * 获取成员qq
     * */
    public long getMemberId() {
        return member.getId();
    }

    /**
     * 获取成员昵称
     * */
    public String getMemberName() {
        return member.getNameCard();
    }

    @Deprecated
    public void sendMessage(Message message) {
        miraiEvent.getGroup().sendMessage(message);
    }

    @Override
    public void sendMessage(String msg) {
        if (!strToImage) {
            miraiEvent.getGroup().sendMessage(msg);
        } else {
            miraiEvent.getGroup().sendMessage(ImageUtil.strToImage(miraiEvent.getGroup(), msg));
        }
    }

    @Override
    public void sendMessage(List<String> msgs) {
        if (!strToImage) {
            miraiEvent.getGroup().sendMessage(StringUtil.toString(msgs));
        } else {
            miraiEvent.getGroup().sendMessage(ImageUtil.strToImage(miraiEvent.getGroup(), StringUtil.toArray(msgs)));
        }
    }

    @Deprecated
    @Override
    public void reply(String msg) {
        throw new NotEnabledException();
    }

    @Deprecated
    @Override
    public void reply(List<String> msgs) {
        throw new NotEnabledException();
    }

    @Deprecated
    @Override
    public void reply(Message message) {
        throw new NotEnabledException();
    }

    public void reply(MessageChain message, String msg) {
        MessageChainBuilder builder = new MessageChainBuilder().append(new QuoteReply(message));
        if (!strToImage) {
            builder.append(msg);
        } else {
            builder.append(ImageUtil.strToImage(miraiEvent.getMember(), msg));
        }
        miraiEvent.getMember().sendMessage(builder.build());
    }

    public void reply(MessageChain message, List<String> msgs) {
        MessageChainBuilder builder = new MessageChainBuilder().append(new QuoteReply(message));
        if (!strToImage) {
            builder.append(StringUtil.toString(msgs));
        } else {
            builder.append(ImageUtil.strToImage(miraiEvent.getMember(), StringUtil.toArray(msgs)));
        }
        miraiEvent.getMember().sendMessage(builder.build());
    }

    public void reply(MessageChain messageChain, Message message) {
        miraiEvent.getMember().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(messageChain))
                .append(message)
                .build());
    }

    @Deprecated
    public long getSenderId() {
        return member.getId();
    }

    @Deprecated
    public int getTime() {
        throw new NotEnabledException();
    }

    @Override
    public void nudge() {
        member.nudge();
    }

    @Override
    public long getGroupId() {
        return groupId;
    }

    @Override
    public void kick(String msg) {
        NormalMember normalMember =  miraiEvent.getGroup().get(member.getId());
        Validate.notNull(normalMember, "Can't find the 'NormalMember' " + member.getId());
        normalMember.kick(msg);
    }

    @Override
    public void mute(int i) {
        member.mute(i);
    }

    @Override
    public void unmute() {
        NormalMember normalMember =  miraiEvent.getGroup().get(member.getId());
        Validate.notNull(normalMember, "Can't find the 'NormalMember' " + member.getId());
        normalMember.unmute();
    }

    @Override
    public net.mamoe.mirai.event.events.GroupMemberEvent getEvent() {
        return miraiEvent;
    }
}
