package me.illtamer.infinitebot.configuration;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitelib.util.StringUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class YamlConfig {
    private final File folder;

    protected YamlConfig(File plugFolder) {
        this.folder = new File(plugFolder, "data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    protected void setPlayerData(long qq, String uuid, YamlConfiguration yaml) {
        Validate.isTrue(qq > 1024 && uuid != null && !uuid.isEmpty(), StringUtil.format("Is it true about the param qq={0} uuid={1} ?", qq, uuid));
        File dataFile = new File(folder, qq + "@" + uuid + ".yml");
        try {
            if (!dataFile.exists()) { // 没有文件创建文件
                dataFile.createNewFile();
                return;
            }
            if (yaml != null) { // 文件不为空保存数据
                yaml.save(dataFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Set<PlayerData> getPlayerData() {
        Set<PlayerData> data = new HashSet<>();
        File[] files = folder.listFiles(((dir, name) -> name.endsWith(".yml")));
        if (files == null || files.length == 0) {
            return data;
        }
        for (File configFile : files) {
            String[] args = configFile.getName().split("@");
            data.add(new PlayerData(Long.parseLong(args[0]), args[1], YamlConfiguration.loadConfiguration(configFile)));
        }
        return data;
    }

    protected PlayerData getPlayerData(long qq, String uuid) {
        File[] files;
        if (uuid == null || uuid.isEmpty()) {
            files = folder.listFiles(((dir, name) -> name.substring(0, name.indexOf("@")).equals(String.valueOf(qq))));
        } else {
            files = folder.listFiles((dir, name) -> name.substring(name.indexOf("@")+1).equals(uuid + ".yml"));
        }
        if (files != null && files.length != 0) {
            Validate.isTrue(files.length == 1, StringUtil.format("Unexpected results ! There need 1 but find {0} !", files.length));
            String[] args = files[0].getName().split("@");
            return new PlayerData(Long.parseLong(args[0]), args[1].replace(".yml", ""), YamlConfiguration.loadConfiguration(files[0]));
        }
        return null;
    }

    public boolean rebindPlayerData(long qq, String uuid) {
        Validate.isTrue(qq > 1024 && uuid != null && !uuid.isEmpty(), StringUtil.format("Is it true about the param qq={0} uuid={1} ?", qq, uuid));
        File[] dataFiles = folder.listFiles(((dir, name) -> name.substring(0, name.indexOf("@")).equals(String.valueOf(qq))));
        if (dataFiles == null || dataFiles.length == 0) {
            InfiniteBot.LOGGER.debug("Can't find the file '" + qq + "@" + uuid + ".yml'");
            return false;
        } else if ((dataFiles.length > 1)){
            InfiniteBot.LOGGER.warn(StringUtil.format("The QQ({0}) has more than one data which is not supported now !", qq));
        }
        File dataFile = dataFiles[0];
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(dataFile);
        File newData = new File(folder, qq + "@" + uuid + ".yml");
        try {
            newData.createNewFile();
            yml.save(newData);
            return dataFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removePlayerData(long qq) {
        File[] files = folder.listFiles(((dir, name) -> name.substring(0, name.indexOf("@")).equals(String.valueOf(qq))));
        if (files == null || files.length == 0) {
            return false;
        }
        return files[0].delete();
    }
}


