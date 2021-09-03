package me.illtamer.infinitebot.bot;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.configuration.InfiniteBotConfig;
import me.illtamer.infinitelib.util.StringUtil;
import me.illtamer.infinitelib.util.security.Base64Util;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarException;

public class BotInitiation {
    public static final List<Bot> BOTS = new CopyOnWriteArrayList<>();
    public static final List<Listener<?>> EVENT_LISTENERS = new CopyOnWriteArrayList<>();
    private Bot bot = null;
    private Listener<?> eventChannel = null;

    @SuppressWarnings("all")
    public void login() {
        if (bot != null) {
            bot.close();
            eventChannel.cancel(new CancellationException("Unknown Exception happened during reload Bot !"));
            EVENT_LISTENERS.remove(eventChannel);
            eventChannel = null;
        }
        InfiniteBotConfig config = (InfiniteBotConfig) InfiniteBot.getInstance().getBotConfig();
        long account = Long.parseLong(new String(Base64Util.decryBASE64(config.getAccount()), StandardCharsets.UTF_8));
        String password = new String(Base64Util.decryBASE64(config.getPassword()), StandardCharsets.UTF_8);

        bot = BotFactory.INSTANCE.newBot(account, password, new BotConfiguration() {
            {
                File folder = InfiniteBot.getInstance().getDataFolder();
                fileBasedDeviceInfo(folder.getAbsolutePath() + "/InfiniteBotPlusInfo.json");
                setProtocol(MiraiProtocol.valueOf(config.getProtocol()));
                setCacheDir(new File(folder, "temp"));
                if (!config.enableLog()) {
                    noBotLog();
                    noNetworkLog();
                }
                if (config.enableContactCache()) {
                    enableContactCache();
                }
            }
        });

        bot.login();
        EVENT_LISTENERS.add(eventChannel = EventExecutor.dispatchListener(bot));
        InfiniteBot.LOGGER.info(StringUtil.format("Bot {1}({0}) 登陆成功!", bot.getId(), bot.getNick()));
    }

    public static void reload(BotInitiation initiation) {
        initiation.eventChannel.cancel(new CancellationException());
        initiation.login();
    }

    public static void unloadAll() {
        for (Listener<?> listener : EVENT_LISTENERS) {
            listener.cancel(new CancellationException("Unknown Exception happened during reload Bot !"));
        }
        for (Bot bot : BOTS) {
            bot.close();
        }
    }

    public Bot getBot() {
        return bot;
    }

    public Listener<?> getEventChannel() {
        return eventChannel;
    }
}
