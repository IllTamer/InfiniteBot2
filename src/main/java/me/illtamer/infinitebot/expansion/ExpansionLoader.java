package me.illtamer.infinitebot.expansion;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.api.IExpansion;
import me.illtamer.infinitebot.expansion.manager.InfinitePluginLoader;
import me.illtamer.infinitelib.util.StringUtil;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 附属总加载类
 * */
public class ExpansionLoader {
    private static final InfinitePluginLoader PLUGIN_LOADER = new InfinitePluginLoader();
    private static final List<IExpansion> EXPANSIONS = new CopyOnWriteArrayList<>();
    private final InfiniteBot instance;
    private final File pluginFolder;

    public ExpansionLoader(InfiniteBot instance) {
        this.instance = instance;
        this.pluginFolder = new File(instance.getDataFolder(), "expansions");
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
    }

    /**
     * 加载所有插件并调用其onEnable
     * */
    public void loadExpansions() {
        if (EXPANSIONS.size() != 0) {
            disableExpansions();
        }
        File[] expansions = pluginFolder.listFiles(((dir, name) -> name.endsWith(".jar")));
        if (expansions == null || expansions.length == 0) {
            return;
        }
        for (File expansion : expansions) {
            try {
                EXPANSIONS.add(PLUGIN_LOADER.loadExpansion(expansion));
            } catch (InvalidExpansionException e) {
                e.printStackTrace();
            }
        }
        InfiniteBot.LOGGER.info(StringUtil.format("检测到 {0} 个附属, 正在初始化...", EXPANSIONS.size()));
        for (IExpansion expansion : EXPANSIONS) {
            Validate.isTrue(!expansion.isEnabled(), StringUtil.format("附属 {0} 已被异常加载!", expansion.getExpansionName()));
            PLUGIN_LOADER.enableExpansion(expansion);
        }
    }

    /**
     * 卸载所有插件并调用其所有onDisable
     * */
    public void disableExpansions() {
        for (IExpansion expansion : EXPANSIONS) {
            PLUGIN_LOADER.disableExpansion(expansion);
            EXPANSIONS.remove(expansion);
        }
    }

    /**
     * 卸载指定附属
     * */
    public boolean disableExpansion(String expansionName) {
        for (IExpansion expansion : EXPANSIONS) {
            if (expansion.getExpansionName().equals(expansionName)) {
                PLUGIN_LOADER.disableExpansion(expansion);
                return EXPANSIONS.remove(expansion);
            }
        }
        return false;
    }

    /**
     * 获取所有附属的名称
     * */
    public List<String> getExpansionNames() {
        List<String> list = new ArrayList<>();
        for (IExpansion expansion : EXPANSIONS) {
            list.add(expansion.getExpansionName());
        }
        return list;
    }
}
