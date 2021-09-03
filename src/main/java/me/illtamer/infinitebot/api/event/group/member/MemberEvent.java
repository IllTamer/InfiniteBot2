package me.illtamer.infinitebot.api.event.group.member;

import me.illtamer.infinitebot.api.event.SendableEvent;

public interface MemberEvent extends SendableEvent {

    /**
     * 获取事件群组的id
     * */
    long getGroupId();

    /**
     * 踢出玩家
     * @param msg 附加消息
     * */
    void kick(String msg);

    /**
     * 禁言
     * */
    void mute(int i);

    /**
     * 解禁言
     * */
    void unmute();
}
