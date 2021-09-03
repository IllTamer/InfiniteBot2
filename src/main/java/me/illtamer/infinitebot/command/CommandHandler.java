package me.illtamer.infinitebot.command;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.api.BotConfig;
import me.illtamer.infinitebot.bot.BotInitiation;
import me.illtamer.infinitebot.bot.EventExecutor;
import me.illtamer.infinitebot.configuration.PlayerData;
import me.illtamer.infinitebot.expansion.ExpansionLoader;
import me.illtamer.infinitelib.command.CommandListener;
import me.illtamer.infinitelib.command.CommandTabCompleter;
import me.illtamer.infinitelib.command.SenderType;
import me.illtamer.infinitelib.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandHandler {

    public CommandHandler(Plugin plugin) {
        CommandTabCompleter.INSTANCE
                .execute("InfiniteBot2")
                .addTab("help", 1, true)
                .addTab("reload", 1, true)
                .addTab("unload", 1, true)
                .addTab("config", new String[] {"reload"}, 2)
                .addTab("expansions", new String[] {"reload"}, 2)
                .addTab("<expansion-name>", new String[] {"unload"}, 2)
                .install(plugin);
    }

    @CommandListener(label = "InfiniteBot2")
    public void onHelp(String[] args, CommandSender sender) {
        if (args.length == 1 && "help".equals(args[0])) {
            sender.sendMessage("适配中");
        } else if (args.length == 1 && "debug".equals(args[0])) {
            // TODO
            if (sender instanceof Player) {
                Player player = (Player) sender;
                BotConfig botConfig = InfiniteBot.getInstance().getBotConfig();
                PlayerData data = botConfig.getPlayerData(765743073, player.getUniqueId().toString());
                player.sendMessage(data.toString());
            }
        }
    }

    @CommandListener(label = "InfiniteBot2", type = SenderType.OP)
    public void onReload(String[] args, CommandSender sender) {
        if (args.length == 2 && "reload".equalsIgnoreCase(args[0])) {
            if ("config".equalsIgnoreCase(args[1])) {
                sender.sendMessage("配置文件重载中...");
                Bukkit.getScheduler().runTaskAsynchronously(InfiniteBot.getInstance(), () -> {
                    InfiniteBot.getInstance().getBotConfig().reload();
                    BotInitiation.reload(InfiniteBot.getInstance().getInitiation());
                });
                sender.sendMessage("配置文件重载完成!");
            } else if ("expansions".equalsIgnoreCase(args[1])) {
                EventExecutor.unregisterListeners("all");
                Bukkit.getScheduler().runTaskAsynchronously(InfiniteBot.getInstance(), () -> {
                    InfiniteBot.getInstance().getExpansionLoader().disableExpansions();
                    InfiniteBot.getInstance().getExpansionLoader().loadExpansions();
                    EventExecutor.loadListenerMethods();
                });
            }
        }
    }

    @CommandListener(label = "InfiniteBot2", type = SenderType.OP)
    public void onUnload(String[] args, CommandSender sender) {
        if (args.length == 2 && "unload".equalsIgnoreCase(args[0])) {
            EventExecutor.unregisterListeners(args[1]);
            ExpansionLoader loader = InfiniteBot.getInstance().getExpansionLoader();
            if (loader.disableExpansion(args[1])) {
                sender.sendMessage(StringUtil.format("{0} 卸载完成", args[1]));
            } else {
                sender.sendMessage(StringUtil.format("未找到插件 {0}", args[1]));
            }
        }
    }
}
