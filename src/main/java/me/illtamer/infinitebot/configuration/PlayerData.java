package me.illtamer.infinitebot.configuration;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * 三种储存方式
 * 1. 群内绑定玩家
 * 2. 游戏内绑定群
 * 3. 白名单(相当于2)
 * */
public class PlayerData {
    private final long qq;
    private final String uuid;
    private final YamlConfiguration yaml;

    public PlayerData(long qq, String uuid, YamlConfiguration yaml) {
        this.qq = qq;
        this.uuid = uuid;
        this.yaml = yaml;
    }

    public long getQq() {
        return qq;
    }

    public String getUuid() {
        return uuid;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(UUID.fromString(uuid));
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "qq=" + qq +
                ", uuid='" + uuid + '\'' +
                ", yaml=" + yaml.saveToString() +
                '}';
    }
}
