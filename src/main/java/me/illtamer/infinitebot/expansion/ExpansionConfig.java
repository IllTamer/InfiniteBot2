package me.illtamer.infinitebot.expansion;

import me.illtamer.infinitebot.api.IExpansion;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ExpansionConfig {
    private final String fileName;
    private final IExpansion expansion;
    private File file;
    private volatile FileConfiguration config;

    public ExpansionConfig(IExpansion expansion, String fileName) {
        this.expansion = expansion;
        this.fileName = fileName;
        this.config = load();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = load();
    }

    public void reload() {
        config = load();
    }

    private FileConfiguration load() {
        File file = new File(expansion.getDataFolder(), fileName);
        if (!file.exists()) {
            expansion.saveResource(fileName, false);
        }
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(file);
            this.file = file;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return yaml;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
