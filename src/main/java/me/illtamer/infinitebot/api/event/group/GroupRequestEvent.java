package me.illtamer.infinitebot.api.event.group;

import me.illtamer.infinitebot.api.event.InfiniteEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;

public class GroupRequestEvent extends InfiniteEvent {
    private final MemberJoinRequestEvent miraiEvent;
    private final long groupId;
    private final long requesterId;
    private final String requesterName;

    public GroupRequestEvent(MemberJoinRequestEvent event) {
        super(event);
        this.miraiEvent = event;
        this.groupId = event.getGroupId();
        this.requesterId = event.getFromId();
        this.requesterName = event.getFromNick();
    }

    public void accept() {
        miraiEvent.accept();
    }

    public void reject() {
        miraiEvent.reject();
    }

    public void reject(boolean blackList) {
        miraiEvent.reject(blackList);
    }

    public void reject(boolean blackList, String msg) {
        miraiEvent.reject(blackList, msg);
    }

    public long getGroupId() {
        return groupId;
    }

    public long getRequesterId() {
        return requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    @Override
    public MemberJoinRequestEvent getEvent() {
        return miraiEvent;
    }
}
