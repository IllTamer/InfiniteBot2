package me.illtamer.infinitebot.api.event.personal;

import me.illtamer.infinitebot.api.event.InfiniteEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;

/**
 * 好友请求事件
 * */
public class FriendRequestEvent extends InfiniteEvent {
    private final NewFriendRequestEvent miraiEvent;
    private final String msg;
    private final long requesterGroupId;
    private final long requesterId;
    private final String requesterName;

    public FriendRequestEvent(NewFriendRequestEvent event) {
        super(event);
        this.miraiEvent = event;
        this.msg = event.getMessage();
        this.requesterId = event.getFromId();
        this.requesterName = event.getFromNick();
        this.requesterGroupId = event.getFromGroupId();
    }

    public void accept() {
        this.miraiEvent.accept();
    }

    public void reject() {
        this.miraiEvent.reject(true);
    }

    public void reject(boolean blackList) {
        miraiEvent.reject(blackList);
    }

    public String getMsg() {
        return msg;
    }

    public long getRequesterGroupId() {
        return requesterGroupId;
    }

    public long getRequesterId() {
        return requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    @Override
    public NewFriendRequestEvent getEvent() {
        return miraiEvent;
    }
}
