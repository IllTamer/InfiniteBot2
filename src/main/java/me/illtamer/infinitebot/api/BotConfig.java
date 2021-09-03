package me.illtamer.infinitebot.api;

import me.illtamer.infinitebot.configuration.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Set;

public interface BotConfig {

    /**
     * 重载配置文件
     * */
    void reload();

    /**
     * 获取管理员列表
     * */
    List<Long> getAdmins();

    /**
     * 获取启用群组列表
     * */
    List<Long> getGroups();

    /**
     * 是否启用纯文字消息替换图片
     * */
    boolean isMessageToImage();

    /**
     * 获取配置文件储存方式
     * */
    String getStoreMode();

    /**
     * 保存玩家数据
     * @param qq 玩家qq 必填
     * @param uuid 玩家uuid 必填
     * @param yaml 创建时可填null
     * @apiNote 没有创建 有则覆盖
     * */
    void setPlayerData(long qq, String uuid, YamlConfiguration yaml);

    /**
     * 查询玩家QQ 重绑定玩家UUID
     * @param qq 玩家qq
     * @param uuid 新账户uuid
     * */
    void rebindPlayerData(long qq, String uuid);

    /**
     * 获取指定玩家数据
     * @apiNote 两参数任选其一即可
     * */
    PlayerData getPlayerData(long qq, String uuid);

    /**
     * 移除指定玩家数据
     * */
    boolean removePlayerData(long qq);

    /**
     * 获取所有玩家数据
     * */
    Set<PlayerData> getPlayerData();

}
