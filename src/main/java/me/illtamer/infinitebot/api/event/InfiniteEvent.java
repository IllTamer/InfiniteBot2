package me.illtamer.infinitebot.api.event;

import net.mamoe.mirai.event.Event;

/**
 * 基础事件
 * 任何 Mirai Event都会触发
 * */
public class InfiniteEvent {
    private final Event miraiEvent;

    public InfiniteEvent(Event event) {
        this.miraiEvent = event;
    }
    /**
     * 获取事件对应的 Mirai Event
     * */
    public Event getEvent() {
        return miraiEvent;
    }

}
