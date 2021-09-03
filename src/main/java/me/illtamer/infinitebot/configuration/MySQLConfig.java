package me.illtamer.infinitebot.configuration;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.illtamer.infinitebot.InfiniteBot;
import me.illtamer.infinitelib.util.StringUtil;
import me.illtamer.infinitelib.util.config.MySQL;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressWarnings("SqlResolve")
class MySQLConfig extends MySQL {
    private final File temp = new File("");

    protected MySQLConfig(String table) {
        super(table);
    }

    public boolean setPayerData(long qq, String uuid, YamlConfiguration yml) {
        String content = yml == null ? "" : yml.saveToString();
        Validate.isTrue(qq > 1024 && uuid != null && !uuid.isEmpty(), StringUtil.format("Is it true about the param qq={0} uuid={1} ?", qq, uuid));
        String sql = "SELECT `QQ`, `UUID`, `PlayerData` FROM " + super.table + " WHERE `QQ`=" + qq + " AND `UUID`='" + uuid + "'";
        try (PreparedStatement check = connection.prepareStatement(sql)) {
            int result; boolean update = false;
            if (check.executeQuery().next()) {
                sql = "UPDATE " + super.table + " SET `PlayerData`=?  WHERE `QQ`=" + qq + " AND `UUID`='" + uuid + "'";
                update = true;
            } else {
                sql = "INSERT INTO " + super.table + "(`PlayerData`, `QQ`, `UUID`) VALUES(?, ?, ?)";
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, content);
                if (!update) {
                    statement.setLong(2, qq);
                    statement.setString(3, uuid);
                }
                result = statement.executeUpdate();
            }
            return  result> 0;
        } catch (SQLException e) {
            InfiniteBot.LOGGER.warn("An exception happened when using MySQL", e);
        }
        return false;
    }

    public boolean rebindPlayerData(long qq, String uuid) {
        Validate.isTrue(qq > 1024 && uuid != null && !uuid.isEmpty(), StringUtil.format("Is it true about the param qq={0} uuid={1} ?", qq, uuid));
        String sql = StringUtil.format("UPDATE {0} SET `UUID`='{1}' WHERE `QQ`={2}", super.table, uuid, qq);
        int result = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result > 0;
    }

    public PlayerData getPlayerData(long qq, String uuid) {
        String sql = StringUtil.format("SELECT `QQ`, `UUID`, `PlayerData` FROM `{0}` WHERE `QQ`={1} OR `UUID`='{2}'", super.table, qq, uuid);
        try (ResultSet set = connection.prepareStatement(sql).executeQuery()) {
            if (set.next()) {
                long QQ = set.getLong("QQ");
                String UUID = set.getString("UUID");
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(temp);
                String playerData = set.getString("PlayerData");
                try {
                    yml.loadFromString(playerData == null ? "" : playerData);
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                return new PlayerData(QQ, UUID, yml);
            }
        }  catch (SQLException e) {
            InfiniteBot.LOGGER.warn("An exception happened when using MySQL", e);
        }
        return null;
    }

    public boolean removePlayerData(long qq) {
        String sql = "DELETE FROM '" + table + "' WHERE `QQ`=" + qq;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void createTable() throws Exception {
        String sql =
                "CREATE TABLE IF NOT EXISTS " + super.table + "(" +
                "`QQ` CHAR(15) PRIMARY KEY , " +
                "`UUID` CHAR(40) NOT NULL UNIQUE, " +
                "`PlayerData` MEDIUMTEXT " +
        ")";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
        statement.close();

//        sql = "ALTER TABLE " + super.table + " auto_increment= 1";
//        statement = connection.prepareStatement(sql);
//        statement.executeUpdate();
//        statement.close();
    }

    public void close() {
        try {
            this.connected = false;
            this.connection.close();
        } catch (SQLException e) {
            InfiniteBot.LOGGER.warn("An exception happened when closing MySQL", e);
        }
    }
}
