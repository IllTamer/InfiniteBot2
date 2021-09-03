package me.illtamer.infinitebot.api.event.group.member;

/**
 * 成员进群事件
 * */
public class MemberJoinEvent extends GroupMemberEvent {
    private final net.mamoe.mirai.event.events.MemberJoinEvent miraiEvent;

    public MemberJoinEvent(net.mamoe.mirai.event.events.MemberJoinEvent event) {
        super(event);
        this.miraiEvent = event;
    }

    @Override
    public net.mamoe.mirai.event.events.MemberJoinEvent getEvent() {
        return miraiEvent;
    }
}
