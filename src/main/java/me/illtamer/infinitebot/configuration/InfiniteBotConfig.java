package me.illtamer.infinitebot.configuration;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitebot.api.BotConfig;
import me.illtamer.infinitelib.util.DownloadUtil;
import me.illtamer.infinitelib.util.StringUtil;
import me.illtamer.infinitelib.util.config.Config;
import me.illtamer.infinitelib.util.security.Base64Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

public class InfiniteBotConfig implements BotConfig {
    private final InfiniteBot instance;
    private final Config configFile;

    public InfiniteBotConfig(InfiniteBot instance) {
        this.instance = instance;
        this.configFile = new Config("main.yml", instance);
        loadConfigurations();
    }

    @Override
    public void reload() {
        configFile.reload();
        loadConfigurations();
        // 重登录机器人
    }


    private YamlConfig yamlConfig;

    private MySQLConfig mysqlConfig;
    private DependencyLoader loader;

    private ConfigurationSection botConfig;
    private StoreMode storeMode;

    @Override
    public void setPlayerData(long qq, String uuid, YamlConfiguration yaml) {
        if (storeMode == StoreMode.MySQL) {
            mysqlConfig.setPayerData(qq, uuid, yaml);
        } else {
            yamlConfig.setPlayerData(qq, uuid, yaml);
        }
    }

    @Override
    public void rebindPlayerData(long qq, String uuid) {
        if (storeMode == StoreMode.MySQL) {
            mysqlConfig.rebindPlayerData(qq, uuid);
        } else {
            // TODO
            yamlConfig.rebindPlayerData(qq, uuid);
        }
    }

    @Override
    public PlayerData getPlayerData(long qq, String uuid) {
        return storeMode == StoreMode.MySQL ? mysqlConfig.getPlayerData(qq, uuid) : yamlConfig.getPlayerData(qq, uuid);
    }

    @Override
    public Set<PlayerData> getPlayerData() {
        if (storeMode == StoreMode.MySQL) {
            throw new RuntimeException();
        } else {
            return yamlConfig.getPlayerData();
        }
    }

    @Override
    public boolean removePlayerData(long qq) {
        return storeMode == StoreMode.MySQL ? mysqlConfig.removePlayerData(qq) : yamlConfig.removePlayerData(qq);
    }

    private void loadConfigurations() {
        FileConfiguration config = configFile.getConfig();
        loader = new DependencyLoader(config);
        if (loader.loadMiraiCore()) {
            InfiniteBot.LOGGER.info("Mirai依赖已加载, 版本 " + loader.version);
        } else {
            InfiniteBot.LOGGER.error("Mirai依赖加载失败, 插件初始化已终止, 请排查错误后重载配置文件 !");
            return;
        }
        botConfig = config.getConfigurationSection("BotConfig");
        ConfigurationSection storeConfig = config.getConfigurationSection("StoreConfig");
        storeMode = StoreMode.getStoreMode(storeConfig.getString("StoreMode"));
        if (storeMode == StoreMode.MySQL) {
            mysqlConfig = new MySQLConfig("InfiniteBot2");
            if (!mysqlConfig.init(storeConfig.getConfigurationSection("MySQL"))) {
                storeMode = StoreMode.YAML;
                InfiniteBot.LOGGER.warn("Exception in connecting MySQL, the StoreMode change to YAML !");
            }
        }
        yamlConfig = storeMode == StoreMode.YAML ? new YamlConfig(instance.getDataFolder()) : null;
    }


    public String getAccount() {
        return Base64Util.encryptBASE64(botConfig.getString("QQ").getBytes(StandardCharsets.UTF_8));
    }

    public String getPassword() {
        return Base64Util.encryptBASE64(botConfig.getString("Password").getBytes(StandardCharsets.UTF_8));
    }

    public String getProtocol() {
        return botConfig.getString("Protocol");
    }

    public boolean enableContactCache() {
        return botConfig.getBoolean("ContactCache");
    }

    public boolean enableLog() {
        return botConfig.getBoolean("Log");
    }

    @Override
    public List<Long> getAdmins() {
        return botConfig.getLongList("Admins");
    }

    @Override
    public List<Long> getGroups() {
        return botConfig.getLongList("Groups");
    }

    @Override
    public boolean isMessageToImage() {
        return botConfig.getBoolean("MessageToImage");
    }

    @Override
    public String getStoreMode() {
        return storeMode.name();
    }

    private class DependencyLoader {
        private static final String HTTPS = "https://infinitebot.coding.net/p/infinitebot/d/libs/git/raw/master/";
        private final String version;
        private final File directory;
        private final Set<JarFile> jars = new HashSet<>();

        public DependencyLoader(FileConfiguration config) {
            this.directory = new File(instance.getDataFolder(), "libs");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            this.version = config.getString("MiraiCoreVersion");
        }

        public boolean loadMiraiCore() {
            if (jars.size() != 0) {
                return true;
            }
            try {
                for (int i = 1; i <= 2; i ++) {
                    File dependency = new File(directory, StringUtil.format("Dependency-Mirai-{0}-{1}.jar", version, i));;
                    if (!dependency.exists()) {
                        InfiniteBot.LOGGER.info(StringUtil.format("未找到依赖项 [{0}], 自动下载中...", dependency.getName()));
                        DownloadUtil.file(dependency, HTTPS +
                                StringUtil.format("Mirai-{0}/Dependency-{1}.jar?download=true", version, i));
                    }
                    URL miraiLib = dependency.toURI().toURL();
                    URLClassLoader classLoader = (URLClassLoader) instance.getClass().getClassLoader();
                    Class<?> system = URLClassLoader.class;
                    Method method = system.getDeclaredMethod("addURL", URL.class);
                    method.setAccessible(true);
                    method.invoke(classLoader, miraiLib);
                    jars.add(new JarFile(dependency));
                }
                return true;
            } catch (Exception e) {
                InfiniteBot.LOGGER.warn("An exception occurred on download Mirai dependency!", e);
            }
            return false;
        }

        public void close() {
            try {
                for (JarFile jar : jars) {
                    jar.close();
                }
                System.gc();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据储存方式
     * */
    private enum StoreMode {
        YAML,
        MySQL;
        public static StoreMode getStoreMode(String mode) {
            if (mode == null || mode.isEmpty()) {
                return YAML;
            }
            if ("mysql".equalsIgnoreCase(mode)) {
                return MySQL;
            } else {
                return YAML;
            }
        }
    }

    public void unload() {
        this.loader.close();
        if (mysqlConfig != null) {
            this.mysqlConfig.close();
        }
    }
}
