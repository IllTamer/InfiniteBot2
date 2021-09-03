package me.illtamer.infinitebot.api.event.group.member;

import me.illtamer.infinitebot.api.event.group.NotEnabledException;

/**
 * 成员离群事件
 * */
public class MemberLeaveEvent extends GroupMemberEvent {
    private final net.mamoe.mirai.event.events.MemberLeaveEvent miraiEvent;

    public MemberLeaveEvent(net.mamoe.mirai.event.events.MemberLeaveEvent event) {
        super(event);
        this.miraiEvent = event;
    }

    @Override
    public void kick(String msg) {
        throw new NotEnabledException();
    }

    @Override
    public void mute(int i) {
        throw new NotEnabledException();
    }

    @Override
    public void unmute() {
        throw new NotEnabledException();
    }

    @Override
    public net.mamoe.mirai.event.events.MemberLeaveEvent getEvent() {
        return miraiEvent;
    }
}
