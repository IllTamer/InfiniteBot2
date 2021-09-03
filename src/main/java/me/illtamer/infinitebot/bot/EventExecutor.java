package me.illtamer.infinitebot.bot;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.api.IExpansion;
import me.illtamer.infinitebot.api.event.InfiniteEvent;
import me.illtamer.infinitebot.bot.event.EventHandler;
import me.illtamer.infinitebot.bot.event.Listener;
import me.illtamer.infinitebot.bot.event.Priority;
import me.illtamer.infinitelib.util.InfiniteScheduler;
import me.illtamer.infinitelib.util.StringUtil;
import me.illtamer.infinitelib.util.clazz.ClassUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.AbstractEvent;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.*;
import org.apache.commons.lang.Validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class EventExecutor {

    /**
     * 经注册的Listener实现类对象
     * */
    private static final HashMap<IExpansion, Set<Listener>> LISTENERS = new HashMap<>();

    /**
     * 读取到的需要监听的方法
     * */
    private static final HashMap<Object, HashMap<Method, Annotation>> METHODS = new HashMap<>();

    /**
     * 注册监听器
     * @param listener 实现Listener的监听对象
     * @see #loadListenerMethods()
     * */
    public static void registerEvents(Listener listener, IExpansion instance) {
        Set<Listener> expansions = LISTENERS.get(instance);
        if (expansions == null) {
            LISTENERS.put(instance, new HashSet<>(Collections.singleton(listener)));
        } else {
            expansions.add(listener);
        }
    }

    /**
     * 注销附属的监听
     * @param expansionName 注销的附属名称 'all' 全注销
     * */
    public static void unregisterListeners(String expansionName) {
        if ("all".equals(expansionName)) {
            LISTENERS.clear();
        } else {
            LISTENERS.entrySet().removeIf(entry -> entry.getKey().getExpansionName().equals(expansionName));
        }
    }

    /**
     * 加载注册方法
     * */
    public static void loadListenerMethods() {
        HashMap<Object, HashMap<Method, Annotation>> map = new HashMap<>();
        for (Set<Listener> set : LISTENERS.values()) {
            for (Listener listener : set) {
                map.put(listener, ClassUtil.getMethods(listener, EventHandler.class));
            }
        }
        if (map.size() != 0) {
            if (METHODS.size() != 0) {
                METHODS.clear();
            }
            METHODS.putAll(map);
        }
        InfiniteBot.LOGGER.debug(StringUtil.format("已注册 {0} 个监听", METHODS.size()));
    }

    /**
     * 轮询执行事件 调度异步线程
     * @see InfiniteScheduler#runAsyncTask(Runnable)
     * */
    protected static net.mamoe.mirai.event.Listener<?> dispatchListener(Bot bot) {
        return bot.getEventChannel().subscribeAlways(Event.class, miraiEvent -> {
            if (METHODS.size() == 0) {
                return;
            }
            InfiniteScheduler.runAsyncTask(() -> {
                try {
                    for (Map.Entry<Object, HashMap<Method, Annotation>> map : METHODS.entrySet()) { // class
                        Object instance = map.getKey();
                        for (int i = Priority.values().length-1; i >= 0; i --) {
                            Priority current = Priority.values()[i];
                            for (Map.Entry<Method, Annotation> entry : map.getValue().entrySet()) { // method
                                if (current != ((EventHandler) entry.getValue()).priority()) {
                                    continue;
                                }
                                Method method = entry.getKey();
                                if (method.getParameterCount() == 0) {
                                    continue;
                                }
                                Class<?> paramType = method.getParameterTypes()[0];
                                Validate.isTrue(InfiniteEvent.class.isAssignableFrom(paramType), "Unknown param type !");
                                InfiniteEvent event = getMiraiEvent(miraiEvent);

                                if (paramType.isInstance(event)) {
                                    method.invoke(instance, event);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * 获取 {@link Event} 对应的 {@link InfiniteEvent} 实例
     * @param miraiEvent 触发的mirai事件
     * @return  {@link InfiniteEvent}
     * */
    private static InfiniteEvent getMiraiEvent(Event miraiEvent) {
        InfiniteEvent event = null;
        if (miraiEvent instanceof MessageEvent) { // 消息事件
            if (miraiEvent instanceof MessageRecallEvent) {
                event = new me.illtamer.infinitebot.api.event.MessageRecallEvent((MessageRecallEvent) miraiEvent);
            } else if (miraiEvent instanceof GroupMessageEvent) {
                event = new me.illtamer.infinitebot.api.event.group.GroupMessageEvent((GroupMessageEvent) miraiEvent);
            } else if (miraiEvent instanceof GroupTempMessageEvent) {
                event = new me.illtamer.infinitebot.api.event.group.GroupTempMessageEvent((GroupTempMessageEvent) miraiEvent);
            } else if (miraiEvent instanceof FriendMessageEvent) {
                event = new me.illtamer.infinitebot.api.event.personal.FriendMessageEvent((FriendMessageEvent) miraiEvent);
            } else { // 未注册的基事件
                event = new me.illtamer.infinitebot.api.event.MessageReceiveEvent((MessageEvent) miraiEvent);
            }
        } else if (miraiEvent instanceof GroupMemberEvent) { // 群事件
            if (miraiEvent instanceof MemberJoinEvent) {
                event = new me.illtamer.infinitebot.api.event.group.member.MemberJoinEvent((MemberJoinEvent) miraiEvent);
            } else if (miraiEvent instanceof MemberLeaveEvent) {
                event = new me.illtamer.infinitebot.api.event.group.member.MemberLeaveEvent((MemberLeaveEvent) miraiEvent);
            } else { // 未注册的基事件
                event = new me.illtamer.infinitebot.api.event.group.member.GroupMemberEvent((GroupMemberEvent) miraiEvent);
            }
        } else if (miraiEvent instanceof AbstractEvent) { // 其它抽象事件
            if (miraiEvent instanceof MessageRecallEvent) {
                event = new me.illtamer.infinitebot.api.event.MessageRecallEvent((MessageRecallEvent) miraiEvent);
            } else if (miraiEvent instanceof MemberJoinRequestEvent) {
                event = new me.illtamer.infinitebot.api.event.group.GroupRequestEvent((MemberJoinRequestEvent) miraiEvent);
            } else if (miraiEvent instanceof NewFriendRequestEvent) {
                event = new me.illtamer.infinitebot.api.event.personal.FriendRequestEvent((NewFriendRequestEvent) miraiEvent);
            }
        }
        if (event == null) { // 未注册的基事件
            InfiniteBot.LOGGER.debug("未知的事件触发 " + miraiEvent.getClass().getName());
            event = new InfiniteEvent(miraiEvent);
        }
        return event;
    }
}
