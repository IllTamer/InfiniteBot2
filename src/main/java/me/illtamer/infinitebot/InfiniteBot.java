package me.illtamer.infinitebot;

import me.illtamer.infinitebot.api.BotConfig;
import me.illtamer.infinitebot.bot.BotInitiation;
import me.illtamer.infinitebot.bot.EventExecutor;
import me.illtamer.infinitebot.command.CommandHandler;
import me.illtamer.infinitebot.configuration.InfiniteBotConfig;
import me.illtamer.infinitebot.expansion.ExpansionLoader;
import me.illtamer.infinitebot.expansion.ExpansionLogger;
import me.illtamer.infinitelib.util.InfiniteScheduler;
import me.illtamer.infinitelib.command.CommandExecutor;
import me.illtamer.infinitelib.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

/**
 * InfiniteBot 2.X
 * @author IllTamer
 * */
public class InfiniteBot extends JavaPlugin {
    public static final Logger LOGGER = new ExpansionLogger(InfiniteBot.class);
    private static InfiniteBot instance;

    private final InfiniteBotConfig botConfig = new InfiniteBotConfig(this);
    private final ExpansionLoader loader = new ExpansionLoader(this);
    private BotInitiation initiation;

    @Override
    public void onEnable() {
        instance = this;
        initiation = new BotInitiation();
        CommandExecutor.INSTANCE
                .execute("InfiniteBot2")
                .addListener(new CommandHandler(this))
                .install(this);
        ((ExpansionLogger) LOGGER).setDebug(true);

        loader.loadExpansions();
        EventExecutor.loadListenerMethods();

        // Socket计划...
        // 自动日志...
        LOGGER.info("机器人登陆中, 请稍后");
        initiation.login();
    }

    @Override
    public void onDisable() {
        // 主线程调用防止卸载任务被取消
        BotInitiation.unloadAll();
        InfiniteScheduler.close();
        loader.disableExpansions();
        botConfig.unload();
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        instance = null;
    }

    public BotInitiation getInitiation() {
        return initiation;
    }

    public ExpansionLoader getExpansionLoader() {
        return loader;
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }

    public static InfiniteBot getInstance() {
        return instance;
    }
}
