package me.illtamer.infinitebot.api;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.configuration.PlayerData;

import java.util.UUID;

public class InfiniteBotAPI {

    /**
     * 判断是否为管理员
     * */
    public static boolean isAdmin(long qq) {
        return InfiniteBot.getInstance().getBotConfig().getAdmins().contains(qq);
    }

    /**
     * 判断是否为管理组
     * */
    public static boolean isGroup(long group) {
        return InfiniteBot.getInstance().getBotConfig().getGroups().contains(group);
    }

    /**
     * 获取玩家数据
     * */
    public static PlayerData getPlayerData(long qq) {
        return InfiniteBot.getInstance().getBotConfig().getPlayerData(qq, null);
    }

    /**
     * 获取玩家数据
     * */
    public static PlayerData getPlayerData(UUID uuid) {
        return InfiniteBot.getInstance().getBotConfig().getPlayerData(0, uuid.toString());
    }

    /**
     * 获取玩家数据
     * */
    public static PlayerData getPlayerData(long qq, UUID uuid) {
        return InfiniteBot.getInstance().getBotConfig().getPlayerData(qq, uuid.toString());
    }
}
