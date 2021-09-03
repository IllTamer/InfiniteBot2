package me.illtamer.infinitebot.util;

import me.illtamer.infinitebot.InfiniteBot;
import org.bukkit.Bukkit;

/**
 * 版本适配工具类
 * */
public class Adaptation {
    public static final String VERSION; // v1_16_R1
    public static final boolean PAPER;

    private static final String NMS = "net.minecraft.server.";

    static {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = packet.substring(packet.lastIndexOf(46) + 1);
        PAPER = "Paper".equals(Bukkit.getServer().getName());
        InfiniteBot.LOGGER.info("检测到服务器" + (PAPER ? "为" : "不为") + "Paper内核，相关适配已完成");
    }

    private Adaptation() {}

    ;

}
